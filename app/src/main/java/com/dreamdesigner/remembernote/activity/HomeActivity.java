package com.dreamdesigner.remembernote.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamdesigner.floatball.AccessibilityUtil;
import com.dreamdesigner.library.BaseActivity.WriteActivity;
import com.dreamdesigner.library.Utils.PopupList;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.adapter.RvAdapter;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.models.Note;
import com.dreamdesigner.remembernote.database.NoteData;
import com.dreamdesigner.remembernote.database.NoteMonth;
import com.dreamdesigner.remembernote.database.NoteYear;
import com.dreamdesigner.remembernote.dialog.WriteDialog;
import com.dreamdesigner.remembernote.models.DataModel;
import com.dreamdesigner.remembernote.models.Level;
import com.dreamdesigner.remembernote.utils.AlertInfoUtils;
import com.dreamdesigner.remembernote.utils.AndroidJurisdictionUtils;
import com.dreamdesigner.remembernote.utils.FloatAuxiliaryUtils;
import com.dreamdesigner.remembernote.utils.IoHelper;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.dreamdesigner.remembernote.utils.ViewUtils;
import com.dreamdesigner.remembernote.utils.art.HomeActivityPermissionsDispatcher;
import com.dreamdesigner.remembernote.utils.audioManager.MediaManager;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.greendao.rx.RxDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class HomeActivity extends WriteActivity implements AlertInfoUtils.OnInformationClick {
    private RecyclerView rv;
    private WriteDialog mDialog;
    private RvAdapter rvAdapter;
    private View view_line_1;
    private View view_line_2;
    private View view_line_3;
    private List<String> popupMenuItemList = new ArrayList<>();
    private RxDao<Note, Long> noteDao;
    private PopupList popupList;
    private RelativeLayout rl_root, root;
    private TextView noData;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences preferences;
    private AlertInfoUtils alertInfoUtils;
    public static boolean isExit = false;
    private AndroidJurisdictionUtils androidJurisdictionUtils;
    private boolean granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getSharedPreferences("NoteState", 0);
        root = (RelativeLayout) findViewById(R.id.content_home);
        if (NoteAppliction.getInstance().getDrawable() != null)
            root.setBackground(NoteAppliction.getInstance().getDrawable());
        mDialog = new WriteDialog(this);
        String[] items = getResources().getStringArray(R.array.action_items);
        for (String item : items) {
            popupMenuItemList.add(item);
        }
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao().rx();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FabPlus != null) {
                if (FabPlus.mStatus) {
                    FabPlus.closeAllItems();
                    return true;
                } else if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    Timer tExit = null;
                    if (isExit == false) {
                        isExit = true; // 准备退出
                        Toast.makeText(this, getString(R.string.prompt_exit_note), Toast.LENGTH_SHORT).show();
                        tExit = new Timer();
                        tExit.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                isExit = false; // 取消退出
                            }
                        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
                    } else {
                        MediaManager.release();
                        super.onKeyDown(keyCode, event);
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void onFocusChanged() {
        alertInfoUtils = new AlertInfoUtils(this);
        androidJurisdictionUtils = new AndroidJurisdictionUtils(this);
        alertInfoUtils.setOnInformationClick(this);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        noData = (TextView) findViewById(R.id.noData);
        rv = (RecyclerView) findViewById(R.id.rv);
        view_line_1 = findViewById(R.id.view_line_1);
        view_line_2 = findViewById(R.id.view_line_2);
        view_line_3 = findViewById(R.id.view_line_3);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, getResources().getColor(R.color.colorPrimary), 10);
        //view line 1 is handled form xml no need to handle programmatically we are only handling line two and three
        ViewUtils.handleVerticalLines(findViewById(R.id.view_line_2), findViewById(R.id.view_line_3));

        popupList = new PopupList() {
            @Override
            protected void OnNoteLongClick(View view, int position) {
                Note note = (Note) view.getTag(R.id.rv_item_card);
                if (note == null)
                    return;
                this.showPopupListWindow();
            }

            @Override
            protected void OnNoteClick(View view, int position) {
                Note note = (Note) view.getTag(R.id.rv_item_card);
                if (note == null)
                    return;
                if (note.getType() == 2) {
                    final ImageView icon_write_comment = (ImageView) view.findViewById(R.id.icon_write_comment);
                    icon_write_comment.setBackgroundResource(R.drawable.play_anim);
                    icon_write_comment.setImageDrawable(null);
                    AnimationDrawable anim = (AnimationDrawable) icon_write_comment.getBackground();
                    anim.start();
                    // 播放音频
                    MediaManager.playSound(note.getTitle(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            icon_write_comment.setBackground(null);
                            icon_write_comment.setImageDrawable(getDrawable(R.drawable.v_anim3));
                        }
                    });
                } else if (note.getType() == 3) {
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Note", note);
                    HomeActivity.this.doActivity(ContentActivity.class, bundle);
                }
            }
        };

        popupList.init(this, rv, popupMenuItemList, new PopupList.OnPopupListClickListener() {
            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                popupList.hidePopupListWindow();
                final Note note = (Note) contextView.getTag(R.id.rv_item_card);
                if (note == null)
                    return;
                switch (position) {
                    case 0:
                        mDialog.show();
                        break;
                    case 1:
                        if (note.getType() != 2 && note.getType() != 3) {
                            mDialog.setNotes(note);
                            mDialog.updateShow();
                        } else {
                            Toast.makeText(HomeActivity.this, getString(R.string.prompt_update), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        noteDao.deleteByKey(note.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.d("DaoExample", "Deleted note, ID: " + note.getId());
                                        loadNewData();
                                        Intent intent = new Intent();
                                        intent.setAction(StaticValueUtils.ACTION_REFRESH_MANUAL);
                                        HomeActivity.this.sendBroadcast(intent);
                                    }
                                });
                        break;
                }
            }
        });
        popupList.setTextSize(popupList.sp2px(12));
        popupList.setTextPadding(popupList.dp2px(10), popupList.dp2px(10), popupList.dp2px(10), popupList.dp2px(10));
        popupList.setIndicatorView(popupList.getDefaultIndicatorView(popupList.dp2px(16), popupList.dp2px(8), 0xFF444444));

        rvAdapter = new RvAdapter(this);
        rv.setAdapter(rvAdapter);
        loadNewNote();
        isCheckAccessShowOtherAppUpInHome();
        //外部跳转启动
        String open = getIntent().getStringExtra(StaticValueUtils.SCHEME_DATA_KEY);
        String openVoice = getIntent().getStringExtra(StaticValueUtils.SCHEME_VOICE_KEY);
        if (!TextUtils.isEmpty(open) && open.equals(StaticValueUtils.OPEN)) {
            if (mDialog == null)
                return;
            mDialog.show();
        } else if (!TextUtils.isEmpty(openVoice) && openVoice.endsWith(StaticValueUtils.VOICE_OPEN)) {
            HomeActivityPermissionsDispatcher.showSorageWithCheck(this);
        }
    }

    @Override
    protected void WriteNewNote(View view) {
        if (mDialog == null)
            return;
        mDialog.show();
    }

    @Override
    protected void AudioRecorderNote(View view) {
        /**
         * 获取录音权限
         */
        HomeActivityPermissionsDispatcher.showSorageWithCheck(this);
    }

    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final String AUDIO_FILE_PATH =
            StaticValueUtils.getSDPath() + "/RememberNotesBackup/AudioRecorder/";

    public void recordAudio(View v) {
        doActivity(AudioRecorderActivity.class);
    }

    /**
     * 加载数据
     */
    private void loadNewNote() {
        Reg();
        loadNewData();
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        NoteAppliction.getInstance().getReceivers().add(receiver);
        return super.registerReceiver(receiver, filter);
    }

    private void Reg() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticValueUtils.HomeNoteChangeValue);
        getApplicationContext().registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (action.equals(StaticValueUtils.HomeNoteChangeValue)) {
                loadNewData();
            }
        }
    };

    private void loadNewData() {
        List<NoteYear> noteYearList = NoteData.loadDate();
        if (noteYearList == null) {
            isVisible(false);
            return;
        }
        if (noteYearList.size() <= 0) {
            isVisible(false);
            return;
        }

        rvAdapter.data.clear();
        loadAdapter(noteYearList);
    }

    private void loadAdapter(List<NoteYear> noteYearList) {
        for (int i = 0; i < noteYearList.size(); i++) {
            NoteYear noteYear = noteYearList.get(i);
            rvAdapter.addItem(new DataModel(Level.LEVEL_ONE, noteYear.Year + " " + getString(R.string.action_year), null));
            List<NoteMonth> noteMonthList = noteYear.monthList;
            if (noteMonthList != null) {
                for (int j = 0; j < noteMonthList.size(); j++) {
                    NoteMonth noteMonth = noteMonthList.get(j);
                    rvAdapter.addItem(new DataModel(Level.LEVEL_TWO, noteMonth.Month + " " + getString(R.string.action_month), null));
                    List<Note> list = noteMonth.dayList;
                    if (list != null) {
                        for (int s = 0; s < list.size(); s++) {
                            Note note = list.get(s);
                            rvAdapter.addItem(new DataModel(Level.LEVEL_THREE, note.getTime() + "  " + note.getTitle(), note));
                        }
                    }
                }
            }
        }
        isVisible(true);
        rv.getAdapter().notifyDataSetChanged();
    }

    private void isVisible(boolean bool) {
        if (bool) {
            rl_root.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.VISIBLE);
            rl_root.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (NoteAppliction.getInstance().getReceivers() != null)
                for (BroadcastReceiver item : NoteAppliction.getInstance().getReceivers()) {
                    unregisterReceiver(item);
                }
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_manage:
                doActivity(SettingActivity.class);
                break;
            case R.id.nav_send:
                doActivity(AboutActivity.class);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.dreamdesigner.library.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void isCheckAccessShowOtherAppUpInHome() {
        SharedPreferences preferences = getSharedPreferences("NoteState", 0);
        boolean isAble = preferences.getBoolean("FloatState", false);
        if (isAble) {
            if (FloatAuxiliaryUtils.isCheckAccessShowOtherAppUp(this)) {
                if (!FloatAuxiliaryUtils.isCheckAccessibility(this)) {
                    alertInfoUtils.Warning(getString(R.string.prompt_access_title), getString(R.string.prompt_access));
                }
            } else {
                alertInfoUtils.Warning(getString(R.string.prompt_other_app_up_title), getString(R.string.prompt_other_app_up));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            checkAccessibility();
        } else if (requestCode == 2) {
            checkAccessShowOtherAppUp();
        }
    }

    private void checkAccessibility() {
        // 判断辅助功能是否开启
        if (!AccessibilityUtil.isAccessibilitySettingsOn(this)) {
            // 引导至辅助功能设置页面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivityForResult(intent, 2);
        }
    }

    private void checkAccessShowOtherAppUp() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    public void onAgree(String title) {
        if (title.equals(getString(R.string.prompt_access_title))) {
            checkAccessibility();
        } else if (title.equals(getString(R.string.prompt_other_app_up_title))) {
            checkAccessShowOtherAppUp();
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);//将回调交给代理类处理
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void onSuccess() {//权限申请成功
        recordAudio(null);
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    public void showRationaleForAudio(PermissionRequest request) {
        showRationaleDialog(getString(R.string.prompt_audio_explain), request);
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    public void onAudioDenied() {//被拒绝
        Toast.makeText(this, getString(R.string.prompt_audio_explain2), Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    public void onAudioNeverAskAgain() {//被拒绝并且勾选了不再提醒
        if (!IoHelper.getAudioIoDontAsk()) AskForPermission();
    }

    /**
     * 再用户拒绝过一次之后,告知用户具体需要权限的原因
     *
     * @param messageResId
     * @param request
     */
    public void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.prompt_enter), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();//请求权限
                    }
                })
                .setTitle(getString(R.string.prompt_audio_explain3))
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    public void AskForPermission() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.prompt_audio_explain4))
                .setMessage(getString(R.string.prompt_audio_explain5))
                .setNegativeButton(getString(R.string.prompt_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this, getString(R.string.prompt_audio_explain6), Toast.LENGTH_SHORT).show();

                    }
                })
                .setNeutralButton(getString(R.string.prompt_audio_explain7), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IoHelper.putAudioIoDontAsk(true);
                        Toast.makeText(HomeActivity.this, getString(R.string.prompt_audio_explain8), Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(getString(R.string.prompt_audio_explain9), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        }).create().show();
    }
}

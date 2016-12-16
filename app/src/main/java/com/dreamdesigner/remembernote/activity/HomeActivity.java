package com.dreamdesigner.remembernote.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamdesigner.library.BaseActivity.WriteActivity;
import com.dreamdesigner.library.Utils.PopupList;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.adapter.RvAdapter;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteData;
import com.dreamdesigner.remembernote.database.NoteMonth;
import com.dreamdesigner.remembernote.database.NoteYear;
import com.dreamdesigner.remembernote.dialog.WriteDialog;
import com.dreamdesigner.remembernote.models.DataModel;
import com.dreamdesigner.remembernote.models.Level;
import com.dreamdesigner.remembernote.utils.ExitApplictionUtils;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.dreamdesigner.remembernote.utils.ViewUtils;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.greendao.rx.RxDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class HomeActivity extends WriteActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            new ExitApplictionUtils(HomeActivity.this).exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    @Override
    protected void onFocusChanged() {
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("Note", note);
                HomeActivity.this.doActivity(ContentActivity.class, bundle);
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
                        mDialog.setNotes(note);
                        mDialog.show();
                        break;
                    case 2:
                        noteDao.deleteByKey(note.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Void>() {
                                    @Override
                                    public void call(Void aVoid) {
                                        Log.d("DaoExample", "Deleted note, ID: " + note.getId());
                                        loadNewData();
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
    }

    @Override
    protected void WriteNewNote(View view) {
        if (mDialog == null)
            return;
        mDialog.show();
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
        registerReceiver(receiver, filter);
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
    protected void onDestroy() {
        if (NoteAppliction.getInstance().getReceivers() != null)
            for (BroadcastReceiver item : NoteAppliction.getInstance().getReceivers()) {
                unregisterReceiver(item);
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
//        if (id == com.dreamdesigner.library.R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == com.dreamdesigner.library.R.id.nav_gallery) {
//
//        } else if (id == com.dreamdesigner.library.R.id.nav_slideshow) {
//
//        } else if (id == com.dreamdesigner.library.R.id.nav_manage) {
//
//        } else if (id == com.dreamdesigner.library.R.id.nav_share) {
//
//        } else if (id == com.dreamdesigner.library.R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.dreamdesigner.library.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

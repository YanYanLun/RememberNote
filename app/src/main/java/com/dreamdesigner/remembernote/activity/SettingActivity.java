package com.dreamdesigner.remembernote.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dreamdesigner.floatball.AccessibilityUtil;
import com.dreamdesigner.library.BaseActivity.NoCollapsingActivity;
import com.dreamdesigner.library.funswitch.FunSwitch;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.models.Note;
import com.dreamdesigner.remembernote.database.NoteData;
import com.dreamdesigner.remembernote.utils.AlertInfoUtils;
import com.dreamdesigner.remembernote.utils.ExcelUtils;
import com.dreamdesigner.remembernote.utils.FloatAuxiliaryUtils;
import com.dreamdesigner.remembernote.utils.IoHelper;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.dreamdesigner.remembernote.utils.art.SettingActivityPermissionsDispatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

public class SettingActivity extends NoCollapsingActivity implements View.OnClickListener, AlertInfoUtils.OnInformationClick {
    private FunSwitch switchButton, shortcutButton, floatButton;
    private SharedPreferences preferences;
    private RelativeLayout action_data_backup, action_data_reduction;
    private String[] title = {"Id", "Title", "Content", "Images", "Url", "Year", "Month", "Day", "Time"};
    private ArrayList<ArrayList<String>> notes2List = new ArrayList<>();
    private File file;
    private AlertInfoUtils alertInfoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onFocusChanged() {
        setTitle(getString(R.string.action_settings));
        alertInfoUtils = new AlertInfoUtils(this);
        alertInfoUtils.setOnInformationClick(this);
        preferences = getSharedPreferences("NoteState", 0);
        switchButton = (FunSwitch) findViewById(R.id.switchButton);
        switchButton.setState(preferences.getBoolean("SwithcState", false));
        switchButton.setOnFunSwitchLinstener(new FunSwitch.OnFunSwitchLinstener() {
            @Override
            public void OnChangeState(Boolean state) {
                preferences.edit().putBoolean("SwithcState", state).commit();
            }
        });

        shortcutButton = (FunSwitch) findViewById(R.id.shortcutButton);
        shortcutButton.setState(preferences.getBoolean("ShortcutState", false));
        shortcutButton.setOnFunSwitchLinstener(new FunSwitch.OnFunSwitchLinstener() {
            @Override
            public void OnChangeState(Boolean state) {
                preferences.edit().putBoolean("ShortcutState", state).commit();
                if (state) {
                    NoteAppliction.getInstance().addShortcut();
                } else {
//                    ShortcutUtils.deleteShortCut(SettingActivity.this);
//                    Toast.makeText(SettingActivity.this, "Delete Shortcut!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        floatButton = (FunSwitch) findViewById(R.id.floatButton);
        floatButton.setState(preferences.getBoolean("FloatState", false));
        floatButton.setOnFunSwitchLinstener(new FunSwitch.OnFunSwitchLinstener() {
            @Override
            public void OnChangeState(Boolean state) {
                preferences.edit().putBoolean("FloatState", state).commit();
                if (state)
                    isCheckAccessShowOtherAppUpInHome();
            }
        });
        action_data_backup = (RelativeLayout) findViewById(R.id.action_data_backup);
        action_data_reduction = (RelativeLayout) findViewById(R.id.action_data_reduction);
        action_data_backup.setOnClickListener(this);
        action_data_reduction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_data_backup:
                SettingActivityPermissionsDispatcher.showSorageWithCheck(this);
                break;
            case R.id.action_data_reduction:
                ExcelUtils.read2Excel(this);
                break;
        }
    }

    private void writeExcel() {
        List<Note> noteList = NoteData.getNoteList();
        if (noteList == null) {
            Toast.makeText(this, getString(R.string.prompt_no_notes), Toast.LENGTH_SHORT).show();
            return;
        }
        if (noteList.size() <= 0) {
            Toast.makeText(this, getString(R.string.prompt_no_notes), Toast.LENGTH_SHORT).show();
            return;
        }
        file = new File(StaticValueUtils.Back_Route);
        StaticValueUtils.makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/NotesBackup.xls", title);
        ExcelUtils.writeObjListToExcel(getNotesData(noteList), file.toString() + "/NotesBackup.xls", this);
    }

    private ArrayList<ArrayList<String>> getNotesData(List<Note> noteList) {
        for (Note item : noteList) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(String.valueOf(item.getId()));
            beanList.add(item.getTitle());
            beanList.add(item.getContent());
            beanList.add(item.getImages());
            beanList.add(item.getUrl());
            beanList.add(String.valueOf(item.getYear()));
            beanList.add(String.valueOf(item.getMonth()));
            beanList.add(String.valueOf(item.getDay()));
            beanList.add(item.getTime());
            notes2List.add(beanList);
        }
        return notes2List;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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
        SettingActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);//将回调交给代理类处理
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onSuccess() {//权限申请成功
        writeExcel();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForStorage(PermissionRequest request) {
        showRationaleDialog(getString(R.string.prompt_storage_explain), request);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStorageDenied() {//被拒绝
        Toast.makeText(SettingActivity.this, getString(R.string.prompt_storage_explain2), Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStorageNeverAskAgain() {//被拒绝并且勾选了不再提醒
        if (!IoHelper.getIoDontAsk()) AskForPermission();
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
                .setTitle(getString(R.string.prompt_storage_explain3))
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    public void AskForPermission() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.prompt_storage_explain4))
                .setMessage(getString(R.string.prompt_storage_explain5))
                .setNegativeButton(getString(R.string.prompt_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingActivity.this, getString(R.string.prompt_storage_explain6), Toast.LENGTH_SHORT).show();

                    }
                })
                .setNeutralButton(getString(R.string.prompt_storage_explain7), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IoHelper.putIoDontAsk(true);
                        Toast.makeText(SettingActivity.this, getString(R.string.prompt_storage_explain8), Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(getString(R.string.prompt_storage_explain9), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        }).create().show();
    }
}

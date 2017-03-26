package com.dreamdesigner.remembernote.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.models.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.utils.IoHelper;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;
import com.dreamdesigner.remembernote.utils.art.AudioRecorderActivityPermissionsDispatcher;
import com.dreamdesigner.remembernote.widget.AudioRecorderButton;
import com.jaeger.library.StatusBarUtil;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;

/**
 * Created by XIANG on 2016/12/12.
 */

public class AudioRecorderActivity extends AppCompatActivity implements AudioRecorderButton.AudioFinishRecorderListener {
    private EditText et, et1;
    private NoteDao noteDao;
    private AudioRecorderButton recorder_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        setStatusBar();
        AudioRecorderActivityPermissionsDispatcher.showSorageWithCheck(this);
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
    }

    @Override
    public void onFinish(float seconds, String filePath) {
        Note note = new Note();
        note.setTitle(filePath);
        note.setContent(seconds + "");
        note.setYear(StaticValueUtils.getYear());
        note.setMonth(StaticValueUtils.getMonth());
        note.setDay(StaticValueUtils.getDay());
        note.setTime(StaticValueUtils.getDataTime());
        note.setType(2);
        long status = noteDao.insert(note);
        if (status > 0) {
            Intent intent = new Intent();
            intent.setAction(StaticValueUtils.HomeNoteChangeValue);
            sendBroadcast(intent);
            intent = new Intent();
            intent.setAction(StaticValueUtils.ACTION_REFRESH_MANUAL);
            sendBroadcast(intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.prompt_new_note_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private void load() {
        recorder_button = (AudioRecorderButton) findViewById(R.id.recorder_button);
        recorder_button.setAudioFinishRecorderListener(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AudioRecorderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);//将回调交给代理类处理
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onSuccess() {//权限申请成功
        load();
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForStorage(PermissionRequest request) {
        showRationaleDialog(getString(R.string.prompt_storage_explain), request);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void onStorageDenied() {//被拒绝
        Toast.makeText(this, getString(R.string.prompt_storage_explain2), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AudioRecorderActivity.this, getString(R.string.prompt_storage_explain6), Toast.LENGTH_SHORT).show();

                    }
                })
                .setNeutralButton(getString(R.string.prompt_storage_explain7), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IoHelper.putIoDontAsk(true);
                        Toast.makeText(AudioRecorderActivity.this, getString(R.string.prompt_storage_explain8), Toast.LENGTH_SHORT).show();
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

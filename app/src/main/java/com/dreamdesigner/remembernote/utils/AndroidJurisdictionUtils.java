package com.dreamdesigner.remembernote.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.dreamdesigner.remembernote.activity.HomeActivity;

/**
 * Created by XIANG on 2017/3/22.
 */

public class AndroidJurisdictionUtils {
    private Context mContext;

    public AndroidJurisdictionUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 检测是否需要录音权限
     *
     * @return
     */
    public boolean checkSelfPermissionAudioRecorder() {
        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            //需不需要解释的dialog
            if (!shouldRequest()) {
                //请求权限
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.RECORD_AUDIO)) {
            //显示一个对话框，给用户解释
            explainDialog();
            return true;
        }
        return false;
    }

    private void explainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("应用需要获取您的录音权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();

    }
}

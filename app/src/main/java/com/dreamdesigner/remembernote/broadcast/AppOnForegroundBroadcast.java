package com.dreamdesigner.remembernote.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dreamdesigner.remembernote.activity.QuickNoteActivity;
import com.dreamdesigner.remembernote.activity.WelcomeActivity;
import com.dreamdesigner.remembernote.service.FloatBallService;

/**
 * Created by XIANG on 2017/2/25.
 */

public class AppOnForegroundBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action))
            return;
        if (action.equals("activity.isAppOnForeground")) {

        } else if (action.equals("activity.isAppUnForeground")) {

        } else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            System.out.println("手机开机了....");
            intent = new Intent(context, FloatBallService.class);
            context.startService(intent);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            intent = new Intent(context, FloatBallService.class);
            context.startService(intent);
        } else if (action.equals("colse.activity")) {
            intent = new Intent();
            intent.setClass(context, QuickNoteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}

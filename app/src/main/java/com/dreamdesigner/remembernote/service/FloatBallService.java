package com.dreamdesigner.remembernote.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Time;

import com.dreamdesigner.library.Utils.AppOnForegroundUtils;
import com.dreamdesigner.remembernote.utils.FloatAuxiliaryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANG on 2017/2/26.
 */

public class FloatBallService extends Service {
    private boolean flag = true;
    private SharedPreferences preferences;
    private boolean isAble;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("NoteState", 0);
        isAble = preferences.getBoolean("FloatState", false);
        new MyThread().start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag = false;
        Intent intent = new Intent(FloatBallService.this, FloatBallService.class);
        startService(intent);
    }

    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有桌面应用的包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
            //属于桌面的应用:com.android.launcher(启动器)
            System.out.println("属于桌面的应用:" + ri.activityInfo.packageName);

        }
        return names;
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isAble = preferences.getBoolean("FloatState", false);
                if (isAble) {
                    if (!isHome()) {
                        Intent intent = new Intent(FloatBallService.this, com.dreamdesigner.floatball.FloatBallService.class);
                        Bundle data = new Bundle();
                        data.putInt("type", com.dreamdesigner.floatball.FloatBallService.TYPE_DEL);
                        intent.putExtras(data);
                        startService(intent);
                    } else {
                        Intent intent = new Intent(FloatBallService.this, com.dreamdesigner.floatball.FloatBallService.class);
                        Bundle data = new Bundle();
                        data.putInt("type", com.dreamdesigner.floatball.FloatBallService.TYPE_ADD);
                        intent.putExtras(data);
                        startService(intent);
                    }
                } else {
                    Intent intent = new Intent(FloatBallService.this, com.dreamdesigner.floatball.FloatBallService.class);
                    Bundle data = new Bundle();
                    data.putInt("type", com.dreamdesigner.floatball.FloatBallService.TYPE_DEL);
                    intent.putExtras(data);
                    startService(intent);
                }
            }
        }
    }
}

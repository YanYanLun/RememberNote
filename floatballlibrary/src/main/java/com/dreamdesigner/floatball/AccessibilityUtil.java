package com.dreamdesigner.floatball;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by wangxiandeng on 2016/11/25.
 */

public class AccessibilityUtil {
    /**
     * 单击返回功能
     *
     * @param service
     */
    public static boolean doBack(AccessibilityService service) {
        Intent intent = new Intent();
        intent.setAction("colse.activity");
        service.getApplicationContext().sendBroadcast(intent);
        return true;
    }

    /**
     * 下拉打开通知栏
     *
     * @param service
     */
    public static boolean doPullDown(AccessibilityService service) {
        return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS);
    }

    /**
     * 上拉返回桌面
     *
     * @param service
     */
    public static boolean doPullUp(AccessibilityService service) {
        return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /**
     * 左右滑动打开多任务
     *
     * @param service
     */
    public static boolean doLeftOrRight(AccessibilityService service) {
        //多任务
//        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
        //返回
        return service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }


}

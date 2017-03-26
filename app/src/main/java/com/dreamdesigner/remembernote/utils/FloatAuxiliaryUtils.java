package com.dreamdesigner.remembernote.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.dreamdesigner.floatball.AccessibilityUtil;
import com.dreamdesigner.remembernote.activity.SettingActivity;

/**
 * Created by XIANG on 2017/2/27.
 */

public class FloatAuxiliaryUtils {


    public static boolean isCheckAccessibility(Context context) {
        // 判断辅助功能是否开启
        return AccessibilityUtil.isAccessibilitySettingsOn(context);
    }

    public static boolean isCheckAccessShowOtherAppUp(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }
}

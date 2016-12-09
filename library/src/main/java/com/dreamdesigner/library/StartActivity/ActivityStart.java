package com.dreamdesigner.library.StartActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityStart {
    public static <T> void Start(Activity context, Class<T> ss) {
        Intent intent = new Intent();
        intent.setClass(context, ss);
        context.startActivity(intent);
        // 设置切换动画，从右边进入，左边退出
//		context.overridePendingTransition(ActivityStartBean.in_from_right,
//				ActivityStartBean.out_to_left);
    }

    public static <T> void Start(Activity context, Class<T> ss, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(context, ss);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
        // 设置切换动画，从右边进入，左边退出
//		context.overridePendingTransition(ActivityStartBean.in_from_right,
//				ActivityStartBean.out_to_left);
    }

    public static <T> void NoAnimStart(Activity context, Class<T> ss) {
        Intent intent = new Intent();
        intent.setClass(context, ss);
        context.startActivity(intent);
    }
}

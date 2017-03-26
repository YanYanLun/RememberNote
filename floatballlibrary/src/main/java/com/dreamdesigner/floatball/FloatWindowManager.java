package com.dreamdesigner.floatball;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by wangxiandeng on 2016/11/25.
 */

public class FloatWindowManager {
    public static FloatBallView mBallView;

    private static WindowManager mWindowManager;


    public static void addBallView(Context context) {
        if (mBallView == null) {
            try {
                WindowManager windowManager = getWindowManager(context);
                int screenWidth = windowManager.getDefaultDisplay().getWidth();
                int screenHeight = windowManager.getDefaultDisplay().getHeight();
                mBallView = new FloatBallView(context);
                LayoutParams params = new LayoutParams();
                params.x = screenWidth;
                params.y = screenHeight / 2;
                params.width = LayoutParams.WRAP_CONTENT;
                params.height = LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.LEFT | Gravity.TOP;
                params.type = LayoutParams.TYPE_PHONE;
                params.format = PixelFormat.RGBA_8888;
                params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                mBallView.setLayoutParams(params);
                windowManager.addView(mBallView, params);
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public static void removeBallView(Context context) {
        if (mBallView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mBallView);
            mBallView = null;
        }
    }

    public static void hideBallView() {
        if (mBallView != null)
            mBallView.setVisibility(View.GONE);
    }

    public static void displayBallView() {
        if (mBallView != null)
            mBallView.setVisibility(View.VISIBLE);
    }

    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

}

package com.dreamdesigner.remembernote.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.activity.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by XIANG on 2016/12/16.
 */

public class ExitApplictionUtils {
    public static boolean isExit = false;
    private Context mContext;

    public ExitApplictionUtils(Context context) {
        this.mContext = context;
    }

    public void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(mContext, mContext.getString(R.string.prompt_exit_note), Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ((HomeActivity) mContext).finish();
            // System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}

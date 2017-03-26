package com.dreamdesigner.remembernote.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by XIANG on 2016/11/23.
 */

public class StaticValueUtils {
    public static final String HomeNoteChangeValue = "home.note.change";
    public static final String ACTION_REFRESH_MANUAL = "com.dreamdesigner.remembernote.utils.APPWIDGET_REFRESH_MANUAL";
    public static final String SCHEME_DATA_KEY = "dataKey";
    public static final String OPEN = "OpenWithAppWidget";
    public static final String ACTION_WRITE_LOGO = "com.dreamdesigner.remembernote.utils.ACTION_WRITE_LOGO";
    public static final String ColseActivity = "colse.activity";
    public static final String FloatBallToShareValue = "FloatBallToShare.Value";
    public static final String Back_Route = StaticValueUtils.getSDPath() + "/RememberNotesBackup";
    public static final String Voice_Route = StaticValueUtils.getSDPath() + "/RememberNotesBackup/Voice";
    public static final String SCHEME_VOICE_KEY = "VOICE";
    public static final String VOICE_OPEN = "OpenWithQuickNote";

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdirs();
    }

    public static String getDataTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }

    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.SECOND);
    }
}

package com.dreamdesigner.remembernote.application;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.dreamdesigner.library.Utils.BitmapUtil;
import com.dreamdesigner.library.Utils.FastBlurUtil;
import com.dreamdesigner.library.Utils.ShortcutUtils;
import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.activity.WelcomeActivity;
import com.dreamdesigner.remembernote.database.DaoMaster;
import com.dreamdesigner.remembernote.database.DaoSession;
import com.dreamdesigner.remembernote.utils.ExitApplictionUtils;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIANG on 2016/11/22.
 */

public class NoteAppliction extends Application {
    private DaoSession mDaoSession;
    private static NoteAppliction instance;
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = true;
    public static Bitmap blurBitmap;
    private Drawable drawable;
    private List<BroadcastReceiver> receivers;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ExitApplictionUtils.isExit = false;
        addShortcut();
    }

    public static NoteAppliction getInstance() {
        return instance;
    }

    /*
       * GreenDao相关
       */
    public synchronized DaoSession getDaoSession() {
        if (mDaoSession == null) {
            initDaoSession();
        }
        return mDaoSession;
    }

    private void initDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public synchronized Bitmap getBlurBitmap() {
        if (blurBitmap == null)
            blurBitmap = getBackGround();
        return blurBitmap;
    }

    private Bitmap getBackGround() {
        Bitmap scaledBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xbi);

        //        scaledBitmap为目标图像，10是缩放的倍数（越大模糊效果越高）
        return blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
    }

    public synchronized Drawable getDrawable() {
        try {
            if (drawable == null) {
                drawable = BitmapUtil.daDrawable(getBlurBitmap());
            }
            return drawable;
        } catch (Exception e) {
            return null;
        }
    }

    public synchronized List<BroadcastReceiver> getReceivers() {
        if (receivers == null)
            receivers = new ArrayList<>();
        return receivers;
    }

    public synchronized void addShortcut() {
        SharedPreferences preferences = getSharedPreferences("NoteState", 0);
        if (preferences == null) {
            return;
        }
        if (!preferences.contains("ShortcutState")) {
            return;
        }
        if (ShortcutUtils.hasShortcut(getApplicationContext(), getString(R.string.app_name))) {
            if (!preferences.getBoolean("ShortcutState", false)) {
                ShortcutUtils.deleteShortCut(getApplicationContext());
            }
            return;
        }
        if (!preferences.getBoolean("ShortcutState", false))
            return;
        final Intent shortcutIntent = new Intent();
        shortcutIntent.setClass(getApplicationContext(), WelcomeActivity.class);
        shortcutIntent.setAction("android.intent.action.MAIN");
        shortcutIntent.addCategory("android.intent.category.LAUNCHER");
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher);
        ShortcutUtils.addShortcut(getApplicationContext(), getString(R.string.app_name), iconRes, shortcutIntent);
    }
}

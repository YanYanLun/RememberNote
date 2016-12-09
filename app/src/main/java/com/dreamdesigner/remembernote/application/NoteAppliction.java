package com.dreamdesigner.remembernote.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.dreamdesigner.remembernote.database.DaoMaster;
import com.dreamdesigner.remembernote.database.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by XIANG on 2016/11/22.
 */

public class NoteAppliction extends Application {
    private DaoSession mDaoSession;
    private static NoteAppliction instance;
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
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
}

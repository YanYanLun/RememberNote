package com.dreamdesigner.remembernote.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 *
 */
@SuppressLint("NewApi")
public class NotesWidgetService extends RemoteViewsService {

    private static final String TAG = "NotesWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NotesListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
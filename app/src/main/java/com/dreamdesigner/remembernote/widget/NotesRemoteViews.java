package com.dreamdesigner.remembernote.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;

import java.util.List;

public class NotesRemoteViews extends RemoteViews {

    private Context mContext;

    private AppWidgetManager mAppWidgetManager;

    private int[] mAppWidgetIds;
    private static List<Note> notes;
    private static NoteDao noteDao;

    public NotesRemoteViews(Context context) {
        super(context.getPackageName(), R.layout.notes_app_widget);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mAppWidgetManager = AppWidgetManager.getInstance(mContext);
        this.mAppWidgetIds = getAppWidgetIds();
    }

    private Class<? extends AppWidgetProvider> getAppWidgetProvider() {
        return NotesAppWidget.class;
    }

    private Intent getProviderIntent() {
        return new Intent(mContext, getAppWidgetProvider());
    }

    public int[] getAppWidgetIds() {
        ComponentName provider = new ComponentName(mContext, getAppWidgetProvider());
        return mAppWidgetManager.getAppWidgetIds(provider);
    }

    public void loading() {
        final int no_data = R.id.no_data;
        final int view = R.id.view;
        final int note_title = R.id.note_title;
        final int list = R.id.list;
        setViewVisibility(no_data, View.VISIBLE);
        setViewVisibility(view, View.GONE);
        setViewVisibility(note_title, View.GONE);
        setViewVisibility(list, View.GONE);
    }

    public void loadComplete() {
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
        notes = noteDao.QueryLastNotes();
        Log.i("zhaoaoaooaoaoaoaoaoa", notes.size() + "");
        if (notes != null) {
            if (notes.size() > 0) {
                Note note = notes.get(0);
                final int no_data = R.id.no_data;
                final int view = R.id.view;
                final int note_title = R.id.note_title;
                final int list = R.id.list;
                setViewVisibility(no_data, View.GONE);
                setViewVisibility(view, View.VISIBLE);
                setViewVisibility(note_title, View.VISIBLE);
                setViewVisibility(list, View.VISIBLE);
                setTextViewText(note_title, note.getTitle());
                bindListViewAdapter();
                notifyAppWidgetViewDataChanged();
            }
        }
        setOnLogoClickPendingIntent();
    }

    public void bindListViewAdapter() {
        int listViewResId = R.id.list;
        Intent serviceIntent = new Intent(mContext, NotesWidgetService.class);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // rv.setEmptyView(listViewResId, R.id.tv_empty);//指定集合view为空时显示的view
        setRemoteAdapter(listViewResId, serviceIntent);
    }

    public void notifyAppWidgetViewDataChanged() {
        int[] appIds = getAppWidgetIds();
        // 更新ListView
        mAppWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.list);
    }

    public void setOnLogoClickPendingIntent() {
        final int action_new = R.id.action_new;
        Intent intent = getProviderIntent();
        intent.setAction(StaticValueUtils.ACTION_WRITE_LOGO);
        PendingIntent logoPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        setOnClickPendingIntent(action_new, logoPendingIntent);
    }

    public void loadView() {
        mAppWidgetManager.updateAppWidget(mAppWidgetIds, this);
    }
}

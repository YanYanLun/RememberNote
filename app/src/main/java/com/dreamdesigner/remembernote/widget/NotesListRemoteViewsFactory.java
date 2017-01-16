package com.dreamdesigner.remembernote.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;

import java.util.ArrayList;
import java.util.List;

class NotesListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static NoteDao noteDao;

    private static final int VIEW_TYPE_COUNT = 1;

    private List<Note> mList = new ArrayList<Note>();

    private Context mContext;

    public NotesListRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() == 0) {
            return null;
        }
        Note note = mList.get(position);
        if (note == null)
            return null;
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.notes_app_widget_text);
        views.setTextViewText(R.id.note_text, note.getContent());
        return views;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        mList.clear();
        mList = getNotes();
    }

    private List<Note> getNotes() {
        noteDao = NoteAppliction.getInstance().getDaoSession().getNoteDao();
        return noteDao.QueryLastNotes();
    }

    @Override
    public void onDestroy() {
        mList.clear();
    }


}
package com.dreamdesigner.remembernote.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.dreamdesigner.remembernote.R;
import com.dreamdesigner.remembernote.activity.HomeActivity;
import com.dreamdesigner.remembernote.application.NoteAppliction;
import com.dreamdesigner.remembernote.database.Note;
import com.dreamdesigner.remembernote.database.NoteDao;
import com.dreamdesigner.remembernote.utils.StaticValueUtils;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NotesAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        NotesRemoteViews views = new NotesRemoteViews(context);
        views.loading();
        views.loadComplete();
        views.loadView();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (StaticValueUtils.ACTION_REFRESH_MANUAL.equals(intent.getAction())) {
            NotesRemoteViews views = new NotesRemoteViews(context);
            views.loading();
            views.loadComplete();
            views.loadView();
        } else if (StaticValueUtils.ACTION_WRITE_LOGO.equalsIgnoreCase(intent.getAction())) {
            Intent newsListIntent = new Intent(context, HomeActivity.class);
            newsListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newsListIntent.putExtra(StaticValueUtils.SCHEME_DATA_KEY, StaticValueUtils.OPEN);
            context.startActivity(newsListIntent);
        }
        super.onReceive(context, intent);
    }
}


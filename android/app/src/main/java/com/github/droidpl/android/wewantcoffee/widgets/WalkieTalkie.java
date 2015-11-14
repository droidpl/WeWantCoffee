package com.github.droidpl.android.wewantcoffee.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.activities.MainActivity;
import com.github.droidpl.android.wewantcoffee.activities.WalkieTalkieConfigureActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WalkieTalkieConfigureActivity WalkieTalkieConfigureActivity}
 */
public class WalkieTalkie extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            WalkieTalkieConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WalkieTalkieConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.walkie_talkie);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.appwidget, getPendingIntent(context, WalkieTalkieConfigureActivity.loadPhonePref(context, appWidgetId)));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static PendingIntent getPendingIntent(Context c, String phone){
        Intent intent = new Intent(c, MainActivity.class);
        intent.putExtra("phone", phone);
        PendingIntent pi = PendingIntent.getActivity(c, 0, intent, 0);
        return pi;
    }
}


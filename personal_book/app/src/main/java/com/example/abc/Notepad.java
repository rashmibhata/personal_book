package com.example.abc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Notepad extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int appwidgetid:appWidgetIds)
        {
            Intent launchintent=new Intent(context,StickyNote.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,launchintent,0);
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget_layout);
            remoteViews.setOnClickPendingIntent(R.id.idtvwidget,pendingIntent);
            appWidgetManager.updateAppWidget(appwidgetid,remoteViews);
        }
    }
}

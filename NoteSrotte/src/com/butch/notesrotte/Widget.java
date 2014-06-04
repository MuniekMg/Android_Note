package com.butch.notesrotte;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
public class Widget extends AppWidgetProvider 
{
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews remoteViewsText = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		RemoteViews remoteViewsImage = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		//////////////////////////////Lunch MainActivity
		Intent launchMainActivity = new Intent(context, MainActivity.class);
		PendingIntent pendingIntentMain = PendingIntent.getActivity(context, 0, launchMainActivity, 0);
		remoteViewsText.setOnClickPendingIntent(R.id.widgetText, pendingIntentMain);
		remoteViewsImage.setOnClickPendingIntent(R.id.imageView, pendingIntentMain);
	    ComponentName thisWidget = new ComponentName(context, Widget.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(thisWidget, remoteViewsText);
	    manager.updateAppWidget(thisWidget, remoteViewsImage);
	    //////////////////////////////
	    
	    
	    remoteViewsText.setTextViewText(R.id.widgetText, MainActivity.message);
	    manager.updateAppWidget(thisWidget, remoteViewsText);
	}

	   public void onReceive(Context context, Intent intent) 
	  {
			//super.onUpdate(context, appWidgetManager, appWidgetIds);
			RemoteViews remoteViewsText = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			RemoteViews remoteViewsImage = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			
			//////////////////////////////Lunch MainActivity
			Intent launchMainActivity = new Intent(context, MainActivity.class);
			PendingIntent pendingIntentMain = PendingIntent.getActivity(context, 0, launchMainActivity, 0);
			remoteViewsText.setOnClickPendingIntent(R.id.widgetText, pendingIntentMain);
			remoteViewsImage.setOnClickPendingIntent(R.id.imageView, pendingIntentMain);
		    ComponentName thisWidget = new ComponentName(context, Widget.class);
		    AppWidgetManager manager = AppWidgetManager.getInstance(context);
		    manager.updateAppWidget(thisWidget, remoteViewsText);
		    manager.updateAppWidget(thisWidget, remoteViewsImage);
		    //////////////////////////////
		    
		    
		    remoteViewsText.setTextViewText(R.id.widgetText, MainActivity.message);
		    manager.updateAppWidget(thisWidget, remoteViewsText);
	        if (intent.getAction().equals("update_widget")) 
	        {
	            // Manual or automatic widget update started

	            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

	            // Update text, images, whatever - here
	            remoteViews.setTextViewText(R.id.widgetText, MainActivity.message);

	            // Trigger widget layout update
	            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Widget.class), remoteViews);
	        }
	    }
}

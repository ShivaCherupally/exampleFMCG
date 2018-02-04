package com.fmcg.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.fmcg.Dotsoft.R;
import com.fmcg.Activity.HomeActivity.DashboardActivity;

/**
 * Created by RuchiTiwari on 5/28/2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{
	String EventName = "";
	String EventDateAndTime = "";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent notificationIntent = new Intent(context, DashboardActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(DashboardActivity.class);
		stackBuilder.addNextIntent(notificationIntent);

		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		try
		{
			EventName = SharedPrefsUtil.getStringPreference(context, "RemainderName");
			EventDateAndTime = SharedPrefsUtil.getStringPreference(context, "RemainderDate");
			Log.e("EventName", EventName);
			Log.e("EventDateAndTime", EventDateAndTime);
			if (EventName != null && EventDateAndTime != null)
			{
				Notification notification = builder.setContentTitle(EventName)
				                                   .setContentText(EventDateAndTime)
				                                   .setTicker("New Message Alert!")
				                                   .setSmallIcon(R.drawable.fmcg)
				                                   .setContentIntent(pendingIntent).build();

				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(0, notification);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

//
	}

}

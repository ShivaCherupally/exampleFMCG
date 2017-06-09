package com.fmcg.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.RemainderData;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shiva on 6/7/2017.
 */

public class RemainderService extends Service
{
	Context mContext;
	RemainderDataBase remainderDb;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		mContext = this;
		// Let it continue running until it is stopped.
		remanderDateAndTimeCheck();
		//Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	private void remanderDateAndTimeCheck()
	{
		String CurrentDateAndTime = DateUtil.presentDate() + ", " + DateUtil.presentTime();
		Log.e("CurrentDateAndTime", CurrentDateAndTime);

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
		notificationIntent.addCategory("android.intent.category.DEFAULT");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 15);

		PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		try
		{
			remainderDb = new RemainderDataBase(getApplicationContext());
			List<RemainderData> bookaTestDatas = new ArrayList<>();
			bookaTestDatas = remainderDb.getRemainderListData();
			List<RemainderData> mRemainderData = new ArrayList<>();
			for (RemainderData bookdata : bookaTestDatas)
			{
				mRemainderData.add(bookdata);
			}
			if (mRemainderData.size() != 0)
			{
				for (int j = 0; j < mRemainderData.size(); j++)
				{
					String remanderDate = mRemainderData.get(j).getEventDate();
					String remainderName = mRemainderData.get(j).getEventName();
					Log.e("RemainderTime", remanderDate);
					if (CurrentDateAndTime.equalsIgnoreCase(remanderDate))
					{
						if (android.os.Build.VERSION.SDK_INT >= 19)
						{
							SharedPrefsUtil.setStringPreference(mContext, "RemainderName", mRemainderData.get(j).getEventName());
							SharedPrefsUtil.setStringPreference(mContext, "RemainderDate", mRemainderData.get(j).getEventDate());
							alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
						}
						else
						{
							SharedPrefsUtil.setStringPreference(mContext, "RemainderName", mRemainderData.get(j).getEventName());
							SharedPrefsUtil.setStringPreference(mContext, "RemainderDate", mRemainderData.get(j).getEventDate());
							alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
						}
					}
				}


			}
			else
			{
			}
		}
		catch (Exception e)
		{

		}
	}
}

//package com.fmcg.ui;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.util.Log;
//
//
///**
// * Created by Shiva on 12/15/2017.
// */
//
//public class LocationReceiverTracker extends BroadcastReceiver
//{
//	@Override
//	public void onReceive(final Context context, final Intent intent)
//	{
//		if (null != intent && intent.getAction().equals("my.action"))
//		{
//			Location locationData = (Location) intent.getParcelableExtra(SettingsLocationTracker.LOCATION_MESSAGE);
//			Log.e("Location: ", "Latitude: " + locationData.getLatitude() + "Longitude:" + locationData.getLongitude());
//			//send your call to api or do any things with the of location data
//		}
//	}
//}

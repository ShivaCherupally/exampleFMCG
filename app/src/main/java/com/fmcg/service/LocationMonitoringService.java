package com.fmcg.service;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.ui.DashboardActivity;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * Created by Shiva on 12/15/2017.
 */

public class LocationMonitoringService extends Service implements
                                                       GoogleApiClient.ConnectionCallbacks,
                                                       GoogleApiClient.OnConnectionFailedListener,
                                                       LocationListener, NetworkOperationListener
{


	private static final String TAG = LocationMonitoringService.class.getSimpleName();
	GoogleApiClient mLocationClient;
	LocationRequest mLocationRequest = new LocationRequest();


	public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
	public static final String EXTRA_LATITUDE = "extra_latitude";
	public static final String EXTRA_LONGITUDE = "extra_longitude";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		mLocationClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();


		mLocationRequest.setInterval(60000);
		mLocationRequest.setFastestInterval(60000);


		int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
		//PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


		mLocationRequest.setPriority(priority);
		mLocationClient.connect();

		//Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	/*
	 * LOCATION CALLBACKS
	 */
	@Override
	public void onConnected(Bundle dataBundle)
	{
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat
				.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED)
		{
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.

			Log.d(TAG, "== Error On onConnected() Permission not granted");
			//Permission not granted by user so cancel the further execution.

			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

		Log.d(TAG, "Connected to Google API");
	}

	/*
	 * Called by Location Services if the connection to the
	 * location client drops because of an error.
	 */
	@Override
	public void onConnectionSuspended(int i)
	{
		Log.d(TAG, "Connection suspended");
	}


	//to get the location change
	@Override
	public void onLocationChanged(Location location)
	{
		Log.d(TAG, "Location changed");


		if (location != null)
		{
			Log.d(TAG, "== location != null");

			//Send result to activities
			sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
		}

	}

	private void sendMessageToUI(String lat, String lng)
	{

		Log.d(TAG, "Sending info...");

		Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
		intent.putExtra(EXTRA_LATITUDE, lat);
		intent.putExtra(EXTRA_LONGITUDE, lng);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

		String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
		String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

		Toast.makeText(getApplicationContext(), "Lat " + latitude, Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), "Lon " + longitude, Toast.LENGTH_SHORT).show();

		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "CHECK_ONE_MIN_TRACK") != null && !SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "CHECK_ONE_MIN_TRACK").isEmpty())
		{
			Log.e("lastsavedTime", SharedPrefsUtil.getStringPreference(getApplicationContext(), "CHECK_ONE_MIN_TRACK"));
			Log.e("currentTime", DateUtil.currentTime());
			if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "CHECK_ONE_MIN_TRACK").equals(DateUtil.currentTime()))
			{
//				Toast.makeText(getApplicationContext(), "One Min Not Completed", Toast.LENGTH_SHORT).show();
			}
			else
			{
				saveLatLontoServer(intent);
			}
		}
		else
		{
			saveLatLontoServer(intent);
		}


	}

	private void saveLatLontoServer(Intent intent)
	{
		String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
		String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

//		Toast.makeText(getApplicationContext(), "Lat " + latitude, Toast.LENGTH_SHORT).show();
//		Toast.makeText(getApplicationContext(), "Lon " + longitude, Toast.LENGTH_SHORT).show();

		if (latitude != null && longitude != null)
		{
			try
			{
				Geocoder geocoder = new Geocoder(LocationMonitoringService.this, Locale.getDefault());
				List<Address> address = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
				android.location.Address obj = address.get(0);
				String addressname = obj.getAddressLine(0);

				String locality = obj.getLocality();
				String areaname = obj.getFeatureName();

				android.location.Address returnedAddress = address.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");
				for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++)
				{
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				String strAdd = strReturnedAddress.toString();
				Log.e("Current Location", strReturnedAddress.toString());
				String startTime = SharedPrefsUtil.getStringPreference(getApplicationContext(), "STARTTIME");
//								obj.getAdminArea();
								/*add = add + "\n" + obj.getCountryName();
		add = add + "\n" + obj.getCountryCode();
        add = add + "\n" + obj.getAdminArea();
        add = add + "\n" + obj.getPostalCode();
        add = add + "\n" + obj.getSubAdminArea();
        add = add + "\n" + obj.getLocality();
        add = add + "\n" + obj.getSubThoroughfare();*/
								/*heading.setText("Your Location" + "\n Address :" + locality
										                + "\n Latitude : " + latitude
										                + "\n Longitude: " + longitude);*/
				String jsonString = createJsonTrack(SharedPrefsUtil.getStringPreference(getApplicationContext(), "EmployeeId"), startTime, DateUtil.currentTime(), latitude,
				                                    longitude, strAdd);
				HttpAdapter.userTracking(LocationMonitoringService.this, "TRACKING_STATUS", jsonString);
			}
			catch (Exception e)
			{

			}


			Log.e("trackLat Lan", "Service Started"
					+ "\n Latitude : " + latitude + "\n Longitude: " + longitude);
		}
	}


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		Log.d(TAG, "Failed to connect to Google API");

	}

	@Override
	public void operationCompleted(final NetworkResponse response)
	{
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				if (response.getTag().equals("TRACKING_STATUS"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						SharedPrefsUtil.setStringPreference(getApplicationContext(), "CHECK_ONE_MIN_TRACK", DateUtil.currentTime());
						Toast.makeText(getApplicationContext(), "Your Location Tracking...", Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Tracking Failed", Toast.LENGTH_SHORT).show();
					}

				}
			}
			catch (Exception e)
			{

			}
		}

	}

	@Override
	public void showToast(final String string, final int lengthLong)
	{

	}

	private String createJsonTrack(String Userid, String TarckInTime, String TrackOutTime,
	                               String Latitude, String Longitude, String Areaname)
	{
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("Userid", Userid);
			dataObj.putOpt("TrackInTime", "0");
			dataObj.putOpt("TrackOutTime", "1");
			dataObj.putOpt("Latitude", Latitude);
			dataObj.putOpt("Longitude", Longitude);
			dataObj.putOpt("Areaname", Areaname);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("params", dataObj.toString());
		return dataObj.toString();
	}
}

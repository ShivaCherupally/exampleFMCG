//package com.fmcg.ui;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.widget.Toast;
//
//import com.fmcg.network.NetworkOperationListener;
//import com.fmcg.network.NetworkResponse;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Created by Shiva on 12/15/2017.
// */
//
//public class TrackerService extends Service implements GoogleApiClient.ConnectionCallbacks,
//                                                       LocationListener, GoogleApiClient.OnConnectionFailedListener,
//                                                       NetworkOperationListener
//{
//	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
//	private GoogleApiClient mGoogleApiClient;
//	private LocationRequest mLocationRequest;
//	private LocationManager locationManager;
//	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//	Context mContext;
//
//	public static final int notify = 300000;  //interval between two services(Here Service run every 5 Minute)
//	private Handler mHandler = new Handler();   //run on another Thread to avoid crash
//	private Timer mTimer = null;    //timer handling
//
//	@Override
//	public IBinder onBind(Intent intent)
//	{
//		throw new UnsupportedOperationException("Not yet implemented");
//	}
//
//	@Override
//	public void onCreate()
//	{
//		if (mTimer != null) // Cancel if already existed
//		{
//			mTimer.cancel();
//		}
//		else
//		{
//			mTimer = new Timer();   //recreate new
//		}
//		mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
//
//		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//		{
//			checkLocationPermission();
//		}
//		else
//		{
//			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//			{
////				showGPSDisabledAlertToUser();
//			}
//			if (mGoogleApiClient == null)
//			{
//				buildGoogleApiClient();
//			}
//		}
//		mLocationRequest = LocationRequest.create()
//		                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//		                                  .setInterval(10 * 1000)        // 10 seconds, in  milliseconds
//		                                  .setFastestInterval(1 * 1000); // 1 second, in milliseconds
//
//	}
//
//	public boolean checkLocationPermission()
//	{
//		if (ContextCompat.checkSelfPermission(TrackerService.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//		{
//			if (ActivityCompat.shouldShowRequestPermissionRationale(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION))
//			{
//				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//				                                  MY_PERMISSIONS_REQUEST_LOCATION);
//			}
//			else
//			{
//				// No explanation needed, we can request the permission.
//				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//				                                  MY_PERMISSIONS_REQUEST_LOCATION);
//			}
//			return false;
//		}
//		else
//		{
//			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//			{
////				showGPSDisabledAlertToUser();
//			}
//			if (mGoogleApiClient == null)
//			{
//				buildGoogleApiClient();
//			}
//
//			return true;
//		}
//	}
//
//	private void buildGoogleApiClient()
//	{
//		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.addConnectionCallbacks(this)
//				.addOnConnectionFailedListener(this)
//				.addApi(LocationServices.API)
//				.build();
//		mGoogleApiClient.connect();
//	}
//
//	@Override
//	public void onDestroy()
//	{
//		super.onDestroy();
//		mTimer.cancel();    //For Cancel Timer
//		Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public void onConnected(@Nullable final Bundle bundle)
//	{
//
//	}
//
//	@Override
//	public void onConnectionSuspended(final int i)
//	{
//
//	}
//
//	@Override
//	public void onConnectionFailed(@NonNull final ConnectionResult connectionResult)
//	{
//
//	}
//
//	@Override
//	public void onLocationChanged(final Location location)
//	{
//
//	}
//
//	@Override
//	public void operationCompleted(final NetworkResponse response)
//	{
//
//	}
//
//	@Override
//	public void showToast(final String string, final int lengthLong)
//	{
//
//	}
//
//	//class TimeDisplay for handling task
//	class TimeDisplay extends TimerTask
//	{
//		@Override
//		public void run()
//		{
//			// run on another thread
//			mHandler.post(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					// display toast
//					Toast.makeText(TrackerService.this, "Service is running", Toast.LENGTH_SHORT).show();
//				}
//			});
//		}
//	}
//}

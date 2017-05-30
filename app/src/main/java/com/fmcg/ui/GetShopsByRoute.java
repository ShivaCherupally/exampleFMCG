package com.fmcg.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.fmcg.Dotsoft.R;
import com.fmcg.models.GetShopsArray;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class GetShopsByRoute extends AppCompatActivity
		implements OnMapReadyCallback, View.OnClickListener, DirectionCallback, NetworkOperationListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	public Timer timer;
	public TextView endTrip;
	public static long startTime, endTime, duration;
	GoogleMap googleMap;
	View btnRequestDirection;
	private LatLngBounds.Builder bounds;
	private String serverKey = "AIzaSyAFAg_3bEWFB-Nj6lvZv3UATmXpr0aDYc0";
	private LatLng camera = new LatLng(13.7457211, 100.5646619);
	private LatLng origin = new LatLng(13.7371063, 100.5642539);
	private LatLng destination = new LatLng(13.7604896, 100.5594266);
	ArrayList<LatLng> points = null;
	SharedPreferences sharedPreferences;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	Marker markerName;
	Location locationCt;
	private static final String[] INITIAL_PERMS = {
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};
	public static final int TIME_INTERVAL = 10000;
	MapFragment mapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps);

		endTrip = (TextView) findViewById(R.id.endtrip);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		bounds = new LatLngBounds.Builder();
		mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{

			PackageManager pm = this.getPackageManager();
			int hasPerm = pm.checkPermission(
					Manifest.permission.READ_EXTERNAL_STORAGE,
					this.getPackageName());
			if (hasPerm != PackageManager.PERMISSION_GRANTED)
			{
				requestPermissions(INITIAL_PERMS, 1);
			}
		}
		else
		{
			googleMapsSetup();
			mGoogleApiClient.connect();
		}
		startTimer();
		endTrip.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.endtrip)
		{
			endTime = System.currentTimeMillis();
			duration = (endTime - startTime);

			Log.d("duration", "" + duration);
			timer.cancel();
			showAlertOnly(this, "EndTrip Duration", "" + duration, "Ok");

		}
	}

	public void showAlertOnly(final Context context, String title, String message, String positiveButton)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);        // Setting Dialog Title
		TextView titlee = new TextView(context);
		// You Can Customise your Title here
		titlee.setText(title);
		//title.setBackgroundColor(Color.DKGRAY);
		titlee.setPadding(10, 10, 10, 10);
		titlee.setGravity(Gravity.CENTER);
		titlee.setTextColor(Color.BLACK);
		titlee.setTextSize(20);
		// Setting Dialog Title
		builder.setCustomTitle(titlee);
		// Setting Dialog Message
		builder.setMessage(message);

		builder.setNegativeButton(positiveButton, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				//finish();
				onBackPressed();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
		alert.getWindow().getAttributes();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		textView.setTextSize(18);
		Button btn1 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn1.setTextSize(16);
	}

	public void startTimer()
	{
		timer = new Timer();
		Log.d("Constants", "Timer Started");
		timer.scheduleAtFixedRate(new java.util.TimerTask()
		{

			@SuppressLint("DefaultLocale")
			@Override
			public void run()
			{

				//Performing my Operations
				startTime = System.currentTimeMillis();
				Log.d("start", "" + startTime);

			}
		}, 0, TIME_INTERVAL);

	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String permissions[], int[] grantResults)
	{

		// If request is cancelled, the result arrays are empty.
		if (grantResults.length > 0
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
		{

			googleMapsSetup();
			mGoogleApiClient.connect();
		}
		return;
	}


	public void googleMapsSetup()
	{
		mapFragment.getMapAsync(this);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Drive.API)
				.addScope(Drive.SCOPE_FILE)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
		                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
		                                  .setInterval(10 * 1000)        // 10 seconds, in milliseconds
		                                  .setFastestInterval(1 * 1000); // 1 second, in milliseconds
		mGoogleApiClient.connect();
	}

	@Override
	public void onMapReady(GoogleMap map)
	{
		try
		{
//DO WHATEVER YOU WANT WITH GOOGLEMAP
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
					.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
			}
			googleMap = map;
			//map.setMyLocationEnabled(true);
			map.setTrafficEnabled(true);
			map.setIndoorEnabled(true);
			map.setBuildingsEnabled(true);
			map.getUiSettings().setZoomControlsEnabled(true);
			sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
			LocationManager locationManagerCt = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationCt = locationManagerCt.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			LatLng latLng = new LatLng(locationCt.getLatitude(), locationCt.getLongitude());
			markerName = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation)));
			map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			// Zoom in the Google Map
			map.animateCamera(CameraUpdateFactory.zoomTo(15));
			HttpAdapter.getShops(this, "getshops", "DTOPAT");
		}
		catch (Exception e)
		{
			Log.e("error", e.toString() + "");
		}


	}

	public void requestDirection()
	{
		GoogleDirection.withServerKey(serverKey)
		               .from(origin)
		               .to(destination)
		               .transportMode(TransportMode.TRANSIT)
		               .execute(this);
	}

	@Override
	public void onDirectionSuccess(Direction direction, String rawBody)
	{
		if (direction.isOK())
		{
			ArrayList<LatLng> sectionPositionList = direction.getRouteList().get(0).getLegList().get(0).getSectionPoint();
			for (LatLng position : sectionPositionList)
			{
				googleMap.addMarker(new MarkerOptions().position(position));
			}

			List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
			ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
			for (PolylineOptions polylineOption : polylineOptionList)
			{
				googleMap.addPolyline(polylineOption);
			}

			btnRequestDirection.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDirectionFailure(Throwable t)
	{
	}

	@Override
	public void operationCompleted(NetworkResponse response)
	{
		if (response.getStatusCode() == 200)
		{

			try
			{
				JSONObject result = new JSONObject(response.getResponseString());
				JSONArray shopsData = result.getJSONArray("Data");
				Log.d("latlang", String.valueOf(shopsData));
				PolylineOptions polyLineOptions = new PolylineOptions();
				for (int i = 0; i < 6; i++)
				{
					JSONObject statuss = shopsData.getJSONObject(i);
					GetShopsArray getShopsArray = new Gson().fromJson(statuss.toString(), GetShopsArray.class);
					points = new ArrayList<LatLng>();

					LatLng latLng = new LatLng(Double.parseDouble(getShopsArray.Latitude), Double.parseDouble(getShopsArray.Longitude));
					points.add(latLng);
					Log.d("latlang", String.valueOf(latLng));
					MarkerOptions markerOptions = new MarkerOptions();

					// Setting latitude and longitude of the marker position
					markerOptions.position(latLng);

					// Setting titile of the infowindow of the marker
					markerOptions.title(getShopsArray.ShopName);
					bounds.include(latLng);
					googleMap.addMarker(markerOptions);
					polyLineOptions.addAll(points);

				}

				polyLineOptions.width(10);
				polyLineOptions.color(Color.RED);
				googleMap.addPolyline(polyLineOptions);
				googleMapszoom();
				requestDirection();

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void googleMapszoom()
	{

		final LatLngBounds bound = bounds.build();
		//BOUND_PADDING is an int to specify padding of bound.. try 100.
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bound, 50);
		googleMap.animateCamera(cu);
	}

	@Override
	public void showToast(String string, int lengthLong)
	{

	}


	@Override
	public void onLocationChanged(Location location)
	{

		if (markerName == null)
		{
			markerName = googleMap.addMarker(new MarkerOptions().position(
					new LatLng(location.getLatitude(), location.getLongitude())).icon(
					BitmapDescriptorFactory.fromResource(R.drawable.navigation)));

		}
		else
		{
			markerName.remove();
			markerName = googleMap.addMarker(new MarkerOptions().position(
					new LatLng(location.getLatitude(), location.getLongitude())).icon(
					BitmapDescriptorFactory.fromResource(R.drawable.navigation)));
		}
		CameraUpdate cu = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
		googleMap.animateCamera(cu);


	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
				.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
				.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (location == null)
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

		}
	}

	@Override
	public void onConnectionSuspended(int i)
	{

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return false;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
		finish();
	}
}


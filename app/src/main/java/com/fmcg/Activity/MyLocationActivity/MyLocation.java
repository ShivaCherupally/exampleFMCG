package com.fmcg.Activity.MyLocationActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fmcg.Activity.HomeActivity.DashboardActivity;
import com.fmcg.Dotsoft.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


public class MyLocation extends AppCompatActivity implements OnMapReadyCallback
{

	Location locationCt;
	MapFragment mapFragment;
	private static final String[] INITIAL_PERMS = {
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.fmcg.Dotsoft.R.layout.mylocation);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.my_location);
		try
		{
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
			}
		}
		catch (Exception e)
		{

		}


	}

	public void googleMapsSetup()
	{
		mapFragment.getMapAsync(this);
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
		}
		return;
	}

	@Override
	public void onMapReady(GoogleMap map)
	{
		try
		{
//DO WHATEVER YOU WANT WITH GOOGLEMAP
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
					.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
				// TODO: Consider calling

				return;
			}
			map.setMyLocationEnabled(true);
			map.setTrafficEnabled(true);
			map.setIndoorEnabled(true);
			map.setBuildingsEnabled(true);
			map.getUiSettings().setZoomControlsEnabled(true);
			LocationManager locationManagerCt = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationCt = locationManagerCt
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			LatLng latLng = new LatLng(locationCt.getLatitude(),
			                           locationCt.getLongitude());


			map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			// Zoom in the Google Map
			map.animateCamera(CameraUpdateFactory.zoomTo(15));
		}
		catch (Exception e)
		{
		}

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
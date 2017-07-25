package com.fmcg.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.models.GetAreaDetailsByRouteId;
import com.fmcg.models.GetRouteDetails;
import com.fmcg.models.GetRouteDropDown;
import com.fmcg.models.GetShopTypeDropDown;
import com.fmcg.models.GetZoneDetails;
import com.fmcg.models.PaymentDropDown;
import com.fmcg.models.ReligionsDropDown;
import com.fmcg.models.ShopNamesData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.Common;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.fmcg.util.Utility;
import com.fmcg.util.Validation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shiva on 7/14/2017.
 */

public class UpdateCustomerNewActivity extends AppCompatActivity implements View.OnClickListener, LocationListener,
                                                                            GoogleApiClient.OnConnectionFailedListener,
                                                                            GoogleApiClient.ConnectionCallbacks,
                                                                            NetworkOperationListener
{
	SharedPreferences sharedPreferences;
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	private String religionDropdown, paymentDropDown;
	TextView shop, order, invoice;
	EditText shopname, ownername, shop_address, pin, mobile, phone, lat, lang, createdby, locationName, payment, emailId;
	private TextView submit;
	private Spinner routecd, religion, payment_sp, shoptype_sp, areaName_sp, zone_sp, shopName_spinner;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private LocationManager locationManager;
	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
	Context mContext;
	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>(); //Shop Names Newly added
	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<ShopNamesData> _routeCodesData = new ArrayList<ShopNamesData>(); //Route Drop Down
	ArrayList<ShopNamesData> _areaNamesData = new ArrayList<ShopNamesData>(); //Area Drop down
	ArrayList<ShopNamesData> _shoptypesData = new ArrayList<ShopNamesData>(); //Shop Type Drop Down

	ArrayList<ShopNamesData> _religionsData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _paymentsSelectData = new ArrayList<ShopNamesData>(); //Select Payment

	ArrayList<String> shooNamestitle = new ArrayList<String>(); // Shop Name Title
	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	ArrayList<String> routeNamestitle = new ArrayList<String>();
	ArrayList<String> areaNamestitle = new ArrayList<String>();
	ArrayList<String> shoptypesNamestitle = new ArrayList<String>();

	ArrayList<String> religionsNamestitle = new ArrayList<String>();
	ArrayList<String> paymentNamestitle = new ArrayList<String>();

	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_ShopId = "";
	String selected_ShopTypeId = "";
	String selected_religionNameId = "";
	String selected_paymentNameId = "";
	String GPSL = "";
	boolean routeDropDownItemSeleted = false;
	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	Button alert_submit;
	boolean check1 = false;
	boolean check2 = false;

	boolean zoneTouchClick = false;
	boolean routeTouchClick = false;
	boolean areaTouchClick = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_customer_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = UpdateCustomerNewActivity.this;

		shopname = (EditText) findViewById(R.id.shopname);
		ownername = (EditText) findViewById(R.id.ownername);
		shop_address = (EditText) findViewById(R.id.shop_address);
		mobile = (EditText) findViewById(R.id.mobile);
		pin = (EditText) findViewById(R.id.pin);
		phone = (EditText) findViewById(R.id.phone);
		lat = (EditText) findViewById(R.id.lat);
		lang = (EditText) findViewById(R.id.lang);
		createdby = (EditText) findViewById(R.id.createdby);
		locationName = (EditText) findViewById(R.id.location_name);
		payment = (EditText) findViewById(R.id.payment);
		submit = (TextView) findViewById(R.id.submit);


		shopName_spinner = (Spinner) findViewById(R.id.shopName_spinner);
		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routecd = (Spinner) findViewById(R.id.routecd);
		areaName_sp = (Spinner) findViewById(R.id.area_name);
		shoptype_sp = (Spinner) findViewById(R.id.shop_type_dp);
		religion = (Spinner) findViewById(R.id.religion);
		payment_sp = (Spinner) findViewById(R.id.payment_sp);
		emailId = (EditText) findViewById(R.id.emailId);

		zoneDetailsDP = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();

		HttpAdapter.getShopDetailsDP(UpdateCustomerNewActivity.this, "shopNames", "0");
		HttpAdapter.getZoneDetailsDP(UpdateCustomerNewActivity.this, "zoneName");
		HttpAdapter.getReligion(UpdateCustomerNewActivity.this, "getReligion");
		HttpAdapter.getPayment(UpdateCustomerNewActivity.this, "payment");
		HttpAdapter.shopType(UpdateCustomerNewActivity.this, "shoptypeDP");

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			checkLocationPermission();
		}
		else
		{
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				showGPSDisabledAlertToUser();
			}
			if (mGoogleApiClient == null)
			{
				buildGoogleApiClient();
			}
		}
		mLocationRequest = LocationRequest.create()
		                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
		                                  .setInterval(10 * 1000)        // 10 seconds, in milliseconds
		                                  .setFastestInterval(1 * 1000); // 1 second, in milliseconds

		zone_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				zoneTouchClick = true;
				routeTouchClick = false;
				areaTouchClick = false;
				return false;
			}
		});

		routecd.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				routeTouchClick = true;
				zoneTouchClick = false;
				areaTouchClick = false;
				return false;
			}
		});

		areaName_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				routeTouchClick = false;
				zoneTouchClick = false;
				areaTouchClick = true;
				return false;
			}
		});


		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean validated = validationEntryData();
				if (validated)
				{
					if (Utility.isOnline(mContext))
					{
						new UpdateCustomerNewActivity.CreateShopTask().execute();
					}
					else
					{
						Toast.makeText(mContext, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void defaultAreaNameSelect()
	{
		selected_areaNameId = "";
		areaNamestitle.clear();
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
	}

	@Override
	public void onClick(final View v)
	{
	}

	@Override
	public void operationCompleted(final NetworkResponse response)
	{
		Common.disMissDialog();
		Log.d("outputResponse", String.valueOf(response));
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				//ZoneDetails DropDown
				if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
					}
				}
				//Route Codes
				else if (response.getTag().equals("routeName")) //"routeName"
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						routeNoSpinnerAdapter(jsonArray);
					}
					else
					{
					}
				}
				//Area Drop Down
				else if (response.getTag().equals("areaNameDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						areaNameSpinnerAdapter(jsonArray);
					}
				}

				else if (response.getTag().equals("shopNames"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						shopNameSpinnerAdapter(jsonArray);
					}
				}
				else if (response.getTag().equals("shoptypeDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						shopTypeNameSpinnerAdapter(jsonArray);
					}
				}

				//ReligionDP
				else if (response.getTag().equals("getReligion"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						religionNamesSpinnerAdapter(jsonArray);
					}
				}
				//PaymentDP
				else if (response.getTag().equals("payment"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						paymentSpinnerAdapter(jsonArray);
					}
				}
				//shopName_spinner DropDown // Change after giving service
				else if (response.getTag().equals("editShopDetails"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONObject jsonArray = mJson.getJSONObject("Data");
						shopCompleteDetails(jsonArray);
					}
				}
				else if (response.getTag().equals("UpdateCustomerSave"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONObject result = mJson.getJSONObject("Data");
						Log.e("response", result.toString() + "");
						if (result.getString("Status").equals("OK"))
						{
							Toast.makeText(UpdateCustomerNewActivity.this, "Successfully Shop Details Updated..", Toast.LENGTH_SHORT).show();
							dailogBoxAfterSubmit();
						}
						else
						{
							Toast.makeText(UpdateCustomerNewActivity.this, "Details Failed to Updated", Toast.LENGTH_SHORT).show();
							dailogBoxAfterSubmit();
						}
					}
					else
					{
						Toast.makeText(UpdateCustomerNewActivity.this, "Details Failed to Updated", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}
				}

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}


		}
	}

	private void shopCompleteDetails(final JSONObject jsnobj)
	{
		try
		{
			Log.e("response", jsnobj.toString() + "");
			int shopId = jsnobj.getInt("ShopId");
			String ShopCode = jsnobj.getString("ShopCode");
			int ShopCategoryId = jsnobj.getInt("ShopCategoryId");
			String ShopName = jsnobj.getString("ShopName");
			String ShopDescription = jsnobj.getString("ShopDescription");
			String OwnerName = jsnobj.getString("OwnerName");
			String Address = jsnobj.getString("Address");
			String MobileNumber = jsnobj.getString("MobileNumber");
			String PhoneNumber = jsnobj.getString("PhoneNumber");
			String Latitude = jsnobj.getString("Latitude");
			String Longitude = jsnobj.getString("Longitude");
			GPSL = jsnobj.getString("GPSL");
			String LocationName = jsnobj.getString("LocationName");
			int ShopTypeId = jsnobj.getInt("ShopTypeId");
			String ShopTypeName = jsnobj.getString("ShopTypeName");
			int ReligionId = jsnobj.getInt("ReligionId");
			String ReligionName = jsnobj.getString("ReligionName");
			int PaymentTermsId = jsnobj.getInt("PaymentTermsId");
			String PaymentName = jsnobj.getString("PaymentName");
			String routeName = jsnobj.getString("RouteName");
			String emailIdStr = jsnobj.getString("EmailAddress");
			String zoneName = jsnobj.getString("ZoneName");
			String areaName = jsnobj.getString("AreaName");
			int zoneId = jsnobj.getInt("ZoneId");
			int RouteId = jsnobj.getInt("RouteId");
			int AreaId = jsnobj.getInt("AreaId");


			selected_zoneId = String.valueOf(zoneId);
			selected_roueId = String.valueOf(RouteId);
			selected_areaNameId = String.valueOf(AreaId);

//			placingAvailZoneName(Integer.parseInt(selected_zoneId));
			zoneTouchClick = false;
			routeTouchClick = false;
			areaTouchClick = false;

			selected_zoneId = String.valueOf(zoneId);
			zone_sp.setSelection(getIndexWithId(zone_sp, zoneId, _zoneNamesData), false);
			HttpAdapter.getRouteDetails(UpdateCustomerNewActivity.this, "routeName", selected_zoneId);

			if (ShopTypeId != 0)
			{
				selected_ShopTypeId = String.valueOf(ShopTypeId);
				if (_shoptypesData.size() > 1)
				{
					shoptype_sp.setSelection(getIndexWithId(shoptype_sp, ShopTypeId, _shoptypesData), false);
				}
			}
			try
			{
				//ShopName
				{
					if (ShopName != null && !ShopName.isEmpty() && !ShopName.equalsIgnoreCase("null"))
					{
						shopname.setText(ShopName + "");
					}
				}

				//Owner Name
				if (OwnerName != null && !OwnerName.isEmpty() && !OwnerName.equalsIgnoreCase("null"))
				{
					ownername.setText(OwnerName + "");
				}

				//LandMark
				if (LocationName != null && !LocationName.isEmpty() && !LocationName.equalsIgnoreCase("null"))
				{
					locationName.setText(LocationName + "");
				}

				//Address
				if (Address != null && !Address.isEmpty() && !Address.equalsIgnoreCase("null"))
				{
					shop_address.setText(Address + "");
				}

				try
				{
					String PinCode = jsnobj.getString("Pincode");
					if (PinCode != null && !PinCode.isEmpty() && !PinCode.equalsIgnoreCase("null"))
					{
						pin.setText(PinCode + "");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				if (ReligionId != 0)
				{
//					religion.setSelection(ReligionId);
					religion.setSelection(getIndex(religion, ReligionName, _religionsData));
					religionDropdown = String.valueOf(ReligionId);
				}

				//Payment Drop Down selection
				if (PaymentTermsId != 0)
				{
					payment_sp.setSelection(getIndex(payment_sp, PaymentName, _paymentsSelectData));
					paymentDropDown = String.valueOf(PaymentTermsId);
				}

				//Email Id
				if (emailIdStr != null && !emailIdStr.isEmpty() && !emailIdStr.equalsIgnoreCase("null"))
				{
					emailId.setText(emailIdStr + "");
				}

				//Mobile No
				if (MobileNumber != null && !MobileNumber.isEmpty() && !MobileNumber.equalsIgnoreCase("null"))
				{
					mobile.setText(MobileNumber + "");
				}

				//Phone Number phone
				if (PhoneNumber != null && !PhoneNumber.isEmpty() && !PhoneNumber.equalsIgnoreCase("null"))
				{
					phone.setText(PhoneNumber + "");
				}

			}
			catch (Exception e)
			{
				Log.e("error", e + "");

			}


		}
		catch (Exception e)
		{
			Log.e("error", e + "");
		}

	}

	@Override
	public void showToast(final String string, final int lengthLong)
	{
	}

	public class CreateShopTask extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(UpdateCustomerNewActivity.this);

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			pd.setMessage("Please wait...");
			pd.setIndeterminate(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			// TODO: attempt authentication against a network service.

			String result = serviceCall();
			return result;
		}

		@Override
		protected void onPostExecute(String success)
		{
			if (success != null)
			{
				try
				{
					JSONObject result = new JSONObject(success);
					Log.e("response", result.toString() + "");
					if (result.getString("Status").equals("OK"))
					{
						Toast.makeText(UpdateCustomerNewActivity.this, "Successfully Shop Details Updated..", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}
					else
					{
						Toast.makeText(UpdateCustomerNewActivity.this, "Details Failed to Updated", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			pd.dismiss();
		}
	}

	@Override
	public void onConnected(Bundle bundle)
	{
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}

		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (location == null)
		{
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

		}
		else
		{
			Toast.makeText(getApplicationContext(), "Latitude : " + location.getLatitude(), Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Longitude : " + location.getLongitude(), Toast.LENGTH_SHORT).show();
			lat.setText("" + location.getLatitude());
			lang.setText("" + location.getLongitude());

		}
	}


	@Override
	public void onConnectionSuspended(int i)
	{
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		if (connectionResult.hasResolution())
		{
			try
			{
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			}
			catch (IntentSender.SendIntentException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
		}
	}

	@Override
	public void onLocationChanged(Location location)
	{
		lat.setText("" + location.getLatitude());
		lang.setText("" + location.getLongitude());
	}

	public boolean checkLocationPermission()
	{
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
			{
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
				                                  MY_PERMISSIONS_REQUEST_LOCATION);
			}
			else
			{
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
				                                  MY_PERMISSIONS_REQUEST_LOCATION);
			}
			return false;
		}
		else
		{
			if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				showGPSDisabledAlertToUser();
			}
			if (mGoogleApiClient == null)
			{
				buildGoogleApiClient();
			}

			return true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{
		switch (requestCode)
		{
			case MY_PERMISSIONS_REQUEST_LOCATION:
			{
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
					{

						if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
						{
							showGPSDisabledAlertToUser();
						}

						if (mGoogleApiClient == null)
						{
							buildGoogleApiClient();
						}
					}
				}
				else
				{
				}
				return;
			}
		}
	}

	private void showGPSDisabledAlertToUser()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
		                  .setCancelable(false)
		                  .setPositiveButton("Settings", new DialogInterface.OnClickListener()
		                  {
			                  public void onClick(DialogInterface dialog, int id)
			                  {
				                  Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				                  startActivity(callGPSSettingIntent);
			                  }
		                  });
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	private void buildGoogleApiClient()
	{

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	private boolean validationEntryData()
	{
		boolean ret = true;
		Log.e("ZoneId", selected_zoneId);
		if (selected_zoneId == null || selected_zoneId.isEmpty() || selected_zoneId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Zone Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		if (selected_roueId == null || selected_roueId.isEmpty() || selected_roueId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Route No", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (selected_areaNameId == null || selected_areaNameId.isEmpty() || selected_areaNameId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Area Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (shopname.getText().toString().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "Please Enter Shopname", Toast.LENGTH_LONG).show();
			//shopname.setError("Shopname  Can not be Blank");
			ret = false;
			return ret;
		}


		if (selected_ShopId == null || selected_ShopId.isEmpty() || selected_ShopId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Shop Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		if (selected_ShopTypeId == null || selected_ShopTypeId.isEmpty() || selected_ShopTypeId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Shop Type", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (ownername.getText().toString().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "Please Enter Owner Name", Toast.LENGTH_LONG).show();
			//ownername.setError("OwnerName  Can not be Blank");
			ret = false;
			return ret;
		}
		if (locationName.getText().toString().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "Please Enter Land mark", Toast.LENGTH_LONG).show();
			//createdby.setError("Enter Your Location Name");
			ret = false;
			return ret;
		}

		if (shop_address.getText().toString().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "ShopAddress Can not be Blank", Toast.LENGTH_LONG).show();
			//shop_address.setError("ShopAddress Can not be Blank");
			ret = false;
			return ret;
		}


		if (pin.getText().toString().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "Please Enter Your Pincode ", Toast.LENGTH_LONG).show();
			//pin.setError("Enter Your Pincode");
			ret = false;
			return ret;
		}
		if (selected_religionNameId == null || selected_religionNameId.isEmpty() || selected_religionNameId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Religion", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		if (selected_paymentNameId == null || selected_paymentNameId.isEmpty() || selected_paymentNameId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Payment ", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		///			nameValuePairs.add(new BasicNameValuePair("ShopTypeId", selected_ShopTypeId));

		/*if (mobile.getText().toString().length() == 0)
		{
			Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
			//mobile.setError("Enter Your Mobile Number");
			return ret;
		}*/

		if (!Validation.isEmailAddress(emailId, true))
		{
			emailId.requestFocus();
			ret = false;
			return ret;
		}

		if (!Validation.isValidPhoneNumber(mobile))
		{
			mobile.requestFocus();
			ret = false;
			return ret;
		}


		return ret;
	}


	private void zoneSpinnerAdapter(JSONArray jsonArray)
	{
		try
		{
			_zoneNamesData.clear();
			zoneNamestitle.clear();
			_zoneNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("ZoneId");
				String shopNamee = jsnobj.getString("ZoneName");
				_zoneNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
			zoneNamestitle.add("Zone Name");
			if (_zoneNamesData.size() > 0)
			{
				for (int i = 0; i < _zoneNamesData.size(); i++)
				{
					zoneNamestitle.add(_zoneNamesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zoneNamestitle);
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zone_sp.setAdapter(dataAdapter_zoneName);


		zone_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (zoneTouchClick)
				{
					Toast.makeText(mContext, "Zone Touch true", Toast.LENGTH_SHORT).show();
					if (position != 0)
					{
						selected_zoneId = _zoneNamesData.get(position - 1).getShopId();
						selected_roueId = "";
						selected_areaNameId = "";
						HttpAdapter.getRouteDetails(UpdateCustomerNewActivity.this, "routeName", selected_zoneId);
					}
				}
				else
				{
//					Toast.makeText(mContext, "Zone Touch False", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void routeNoSpinnerAdapter(JSONArray jsonArray)
	{
		try
		{
			_routeCodesData.clear();
			routeNamestitle.clear();
			_routeCodesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("RouteId");
				String shopNamee = jsnobj.getString("RouteName");
				Log.e("RouteId + RouteName", shopId + shopNamee);
				_routeCodesData.add(new ShopNamesData(shopId, shopNamee));
			}
			routeNamestitle.add("Select Route No");
			if (_routeCodesData.size() > 0)
			{
				for (int i = 0; i < _routeCodesData.size(); i++)
				{
					routeNamestitle.add(_routeCodesData.get(i).getShopName());
				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Routedetails adapter
		ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNamestitle);
		dataAdapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routecd.setAdapter(dataAdapter_routeName);

		if (!zoneTouchClick)
		{
			routecd.setSelection(getIndexWithId(routecd, Integer.parseInt(selected_roueId), _routeCodesData), false);
			HttpAdapter.getAreaDetailsByRoute(UpdateCustomerNewActivity.this, "areaNameDP", selected_roueId);
		}
		else if (!routeTouchClick)
		{
			defaultAreaNameSelect();
//			HttpAdapter.getAreaDetailsByRoute(UpdateCustomerNewActivity.this, "areaNameDP", selected_roueId);
		}
		/*if (!routeDropDownItemSeleted)
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			placingAvailRouteNo(Integer.parseInt(selected_roueId));
		}
		else
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			areaName_sp.setVisibility(View.VISIBLE);
		}*/


		routecd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (routeTouchClick)
				{
					if (position != 0)
					{
						routeDropDownItemSeleted = true;
						selected_roueId = _routeCodesData.get(position - 1).getShopId(); //3
						HttpAdapter.getAreaDetailsByRoute(UpdateCustomerNewActivity.this, "areaNameDP", selected_roueId);
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void areaNameSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_areaNamesData.clear();
			areaNamestitle.clear();
			_areaNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("AreaId");
				String shopNamee = jsnobj.getString("AreaName");
				_areaNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
			areaNamestitle.add("Select Area Name");
			if (_areaNamesData.size() > 0)
			{
				for (int i = 0; i < _areaNamesData.size(); i++)
				{
					areaNamestitle.add(_areaNamesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);

		if (!zoneTouchClick && !routeTouchClick)
		{
			areaName_sp.setSelection(getIndexWithId(areaName_sp, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
		}
		else
		{
//			areaName_sp.setSelection(getIndexWithId(areaName_sp, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
//			defaultAreaNameSelect();
		}
		/*if (!routeDropDownItemSeleted)
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			placingAvailAreaName(Integer.parseInt(selected_areaNameId));
		}
		else
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			areaName_sp.setVisibility(View.VISIBLE);
		}*/


		areaName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (areaTouchClick)
				{
					if (position != 0)
					{
						selected_areaNameId = _areaNamesData.get(position - 1).getShopId();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void shopNameSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("shopNamesDropdown", jsonArray.toString() + "");
		try
		{
			_shopNamesData.clear();
			shooNamestitle.clear();
			_shopNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("ShopId");
				String shopNamee = jsnobj.getString("ShopName");
				_shopNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
			shooNamestitle.add("Select Shop Name");
			if (_shopNamesData.size() > 0)
			{
				for (int i = 0; i < _shopNamesData.size(); i++)
				{
					shooNamestitle.add(_shopNamesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
		}
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shooNamestitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopName_spinner.setAdapter(dataAdapter_shopType);
		shopName_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
				{
					if (position != 0)
					{
						selected_ShopId = _shopNamesData.get(position - 1).getShopId();
						HttpAdapter.shopEditDetails(UpdateCustomerNewActivity.this, "editShopDetails", selected_ShopId);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void shopTypeNameSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("shopTypeDropdown", jsonArray.toString() + "");
		try
		{
			_shoptypesData.clear();
			shoptypesNamestitle.clear();
			_shoptypesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("ShopTypeId");
				String shopNamee = jsnobj.getString("ShopTypeName");
				_shoptypesData.add(new ShopNamesData(shopId, shopNamee));
			}
			shoptypesNamestitle.add("Select Shop Type");
			if (_shoptypesData.size() > 0)
			{
				for (int i = 0; i < _shoptypesData.size(); i++)
				{
					shoptypesNamestitle.add(_shoptypesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{

		}
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this,
		                                                                     android.R.layout.simple_spinner_item, shoptypesNamestitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shoptype_sp.setAdapter(dataAdapter_shopType);
		shoptype_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					selected_ShopTypeId = _shoptypesData.get(position - 1).getShopId();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void religionNamesSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_religionsData.clear();
			religionsNamestitle.clear();
			_religionsData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("ReligionId");
				String shopNamee = jsnobj.getString("ReligionName");
				_religionsData.add(new ShopNamesData(shopId, shopNamee));
			}
			religionsNamestitle.add("Select Religion");
			if (_religionsData.size() > 0)
			{
				for (int i = 0; i < _religionsData.size(); i++)
				{
					religionsNamestitle.add(_religionsData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		ArrayAdapter<String> dataAdapter_religion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, religionsNamestitle);
		dataAdapter_religion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		religion.setAdapter(dataAdapter_religion);
		religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					religionDropdown = _religionsData.get(position - 1).getShopId();
					selected_religionNameId = _religionsData.get(position - 1).getShopId();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void paymentSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_paymentsSelectData.clear();
			paymentNamestitle.clear();
			_paymentsSelectData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("PaymentTermsId");
				String shopNamee = jsnobj.getString("PaymentName");
				_paymentsSelectData.add(new ShopNamesData(shopId, shopNamee));
			}
			paymentNamestitle.add("Select Payment");
			if (_paymentsSelectData.size() > 0)
			{
				for (int i = 0; i < _paymentsSelectData.size(); i++)
				{
					paymentNamestitle.add(_paymentsSelectData.get(i).getShopName());

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		ArrayAdapter<String> dataAdapter_payment = new ArrayAdapter<String>(this,
		                                                                    android.R.layout.simple_spinner_item, paymentNamestitle);
		dataAdapter_payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		payment_sp.setAdapter(dataAdapter_payment);
		payment_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					paymentDropDown = _paymentsSelectData.get(position - 1).getShopId();
					selected_paymentNameId = _paymentsSelectData.get(position - 1).getShopId();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	public String serviceCall()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost("http://202.143.96.20/Orderstest/api/Services/updateShopDetails");
		HttpPost httppost = new HttpPost(HttpAdapter.UPDATESHOP_DETAILS);
		try
		{
			String updateDate = "";
			Calendar c = Calendar.getInstance();
			SimpleDateFormat simDf = new SimpleDateFormat("dd-MM-yyyy");
			try
			{
				updateDate = simDf.format(c.getTime());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			Log.e("Ordering Date", updateDate + "");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("shopId", selected_ShopId));
			nameValuePairs.add(new BasicNameValuePair("ShopName", shopname.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ShopDescription", "Good"));
			nameValuePairs.add(new BasicNameValuePair("OwnerName", ownername.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Address", shop_address.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Latitude", lat.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Longitude", lang.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("PhoneNumber", phone.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("MobileNumber", mobile.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("GPSL", GPSL));
			nameValuePairs.add(new BasicNameValuePair("LocationName", locationName.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ZoneId", selected_zoneId));
			nameValuePairs.add(new BasicNameValuePair("RouteId", selected_roueId));
			nameValuePairs.add(new BasicNameValuePair("ShopTypeId", selected_ShopTypeId));
			nameValuePairs.add(new BasicNameValuePair("ReligionID", religionDropdown));
			nameValuePairs.add(new BasicNameValuePair("PaymentTermId", paymentDropDown));
			nameValuePairs.add(new BasicNameValuePair("AreaId", selected_areaNameId));
			nameValuePairs.add(new BasicNameValuePair("Active", "Y"));
			nameValuePairs.add(new BasicNameValuePair("DateTime", DateUtil.currentDate()));
			nameValuePairs.add(new BasicNameValuePair("Createdby", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
			nameValuePairs.add(new BasicNameValuePair("EmailID", emailId.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Pincode", pin.getText().toString()));
			Log.e("params", nameValuePairs.toString());
			//[shopId=346, ShopName=Micro Soft, ShopDescription=Good, OwnerName=Micro , Address=Dilsukhnagar , Latitude=17.4951797, Longitude=78.4125424, PhoneNumber=040223344, MobileNumber=1234567890, GPSL=0, LocationName=Dilsukhnagar , ZoneId=2, RouteId=2, ShopTypeId=346, ReligionID=1, PaymentTermId=2, AreaId, Active=Y, DateTime=03-07-2017, Createdby=4, EmailID=sai@s.com, Pincode=508880]
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("ShopName", shopname.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("ShopDescription", "shopDescription"));
					nameValuePairs.add(new BasicNameValuePair("OwnerName", ownername.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Address", shop_address.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Latitude", lat.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Longitude", lang.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("PhoneNumber", phone.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("MobileNumber", mobile.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("GPSL", "0"));
					nameValuePairs.add(new BasicNameValuePair("LocationName", locationName.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("ZoneId", selected_zoneId));
					nameValuePairs.add(new BasicNameValuePair("RouteId", selected_roueId));
					nameValuePairs.add(new BasicNameValuePair("ShopTypeId", shopTypeDropdown));
					nameValuePairs.add(new BasicNameValuePair("ReligionID", religionDropdown));
					nameValuePairs.add(new BasicNameValuePair("PaymentTermId", paymentDropDown));
					nameValuePairs.add(new BasicNameValuePair("AreaId", areaDropDwn));
					nameValuePairs.add(new BasicNameValuePair("Active", "Y"));
					nameValuePairs.add(new BasicNameValuePair("DateTime", DateUtil.currentDate()));
					nameValuePairs.add(new BasicNameValuePair("Createdby", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
					nameValuePairs.add(new BasicNameValuePair("EmailID", emailId.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair("Pincode", pin.getText().toString()));*/

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == 200)
			{
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					responseBody = EntityUtils.toString(entity);

				}

			}
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
		}

		return responseBody;
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

	private int getIndex(Spinner spinner, String searchName, ArrayList<ShopNamesData> _availbleDropDownData)
	{
		int searchIdIndex = 0;
		try
		{
			//Log.d(LOG_TAG, "getIndex(" + searchString + ")");
			if (searchName == null || spinner.getCount() == 0)
			{
				searchIdIndex = -1;
				return -1; // Not found
			}
			else
			{
				for (int i = 0; i < spinner.getCount(); i++)
				{
					String availName = _availbleDropDownData.get(i).getShopName();
					if (availName.equals(searchName))
					{
						searchIdIndex = i + 1;
						Log.e("availbleStringName", _availbleDropDownData.get(i).getShopName() + "");
						return searchIdIndex;
					}
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return searchIdIndex; // Not found
	}

	private int getIndexWithId(Spinner spinner, int searchId, ArrayList<ShopNamesData> _availbleDropDownData)
	{
		int searchIdIndex = 0;
		try
		{
			if (searchId == 0)
			{
				searchIdIndex = -1;
				return -1; // Not found
			}
			else
			{
				for (int i = 0; i < spinner.getCount(); i++)
				{
					String avaliableListDataid = _availbleDropDownData.get(i).getShopId();
					if (avaliableListDataid.equals(String.valueOf(searchId)))
					{
						searchIdIndex = i + 1;
						Log.e("availbleId", _availbleDropDownData.get(i).getShopId() + "");
						return searchIdIndex;
					}
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return searchIdIndex; // Not found
	}

	private void dailogBoxAfterSubmit()
	{
		promoDialog = new Dialog(this);
		promoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		promoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		promoDialog.setCancelable(false);
		promoDialog.setContentView(R.layout.dailog_for_acceptance);
		close_popup = (ImageView) promoDialog.findViewById(R.id.close_popup);
		alert_submit = (Button) promoDialog.findViewById(R.id.alert_submit);
		select_option_radio_grp = (RadioGroup) promoDialog.findViewById(R.id.select_option_radio_grp);

		promoDialog.show();

		select_option_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final RadioGroup radioGroup, final int i)
			{
				switch (i)
				{
					case R.id.orderBook:
						check1 = true;
						break;
					case R.id.inovice:
						check2 = true;
						break;


				}
			}


		});

		close_popup.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (promoDialog != null)
				{
					promoDialog.dismiss();
					Util.hideSoftKeyboard(mContext, v);
					Intent in = new Intent(UpdateCustomerNewActivity.this, DashboardActivity.class);
					Util.killorderBook();
					startActivity(in);
//					refreshActivity();
				}
			}
		});

		alert_submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (check1)
				{
					Intent in = new Intent(UpdateCustomerNewActivity.this, Order.class);
					Util.killorderBook();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(UpdateCustomerNewActivity.this, Invoice.class);
					Util.killorderBook();
					startActivity(inten);
				}
				else
				{
					Toast.makeText(mContext, "Please Select Order Book or Invoice", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

}




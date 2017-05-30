package com.fmcg.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.Common;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Utility;
import com.fmcg.util.Validation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

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
 * Created by RuchiTiwari on 5/23/2017.
 */

public class UpdateShopDetailsActvity extends AppCompatActivity implements View.OnClickListener, LocationListener,
                                                                           GoogleApiClient.OnConnectionFailedListener,
                                                                           GoogleApiClient.ConnectionCallbacks,
                                                                           NetworkOperationListener
{
	SharedPreferences sharedPreferences;

	public List<GetZoneDetails> zoneDetailsDP;
	private List<GetRouteDropDown> routeDp;
	private List<PaymentDropDown> paymentDP;
	private List<ReligionsDropDown> religionsDP;
	private List<GetShopTypeDropDown> shopTypeDP;
	private List<GetAreaDetailsByRouteId> areaDp;
	public List<GetRouteDetails> routeDetailsDP;

	private List<String> zoneDetailsDP_str;
	private List<String> routeDetailsDP_str;
	private List<String> routeDp_str;
	private List<String> religionsDP_str;
	private List<String> paymentDP_str;
	private List<String> shopTypeDP_str;
	private List<String> areaDp_str;


	private String routeCode, religionDropdown, paymentDropDown, shopTypeDropdown, areaDropDwn, routeNameDropDown;
	TextView mydayPlan, shop, maps, getshops, mylocation, new_customer, endTrip, remarks, logout, order, invoice, userName, shop_update;
	DrawerLayout drawer;
	Toolbar toolbar;
	EditText shopname, ownername, shop_address, pin, mobile, phone, lat, lang, location, createdby, locationName, payment, emailId;
	private TextView submit;
	private Spinner routecd, religion, payment_sp, shoptype_sp, areaName_sp, zone_sp, shopName_spinner;
	//Define a request code to send to Google Play services
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private LocationManager locationManager;
	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
	Context mContext;
	String UserId = "";

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

	String selected_shopNameId = "";
	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_ShopId = "";
	String selected_religionNameId = "";
	String selected_paymentNameId = "";

	String GPSL = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_shop_details_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = UpdateShopDetailsActvity.this;

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
		religionsDP = new ArrayList<>();
		routeDp = new ArrayList<>();
		paymentDP = new ArrayList<>();
		shopTypeDP = new ArrayList<>();
		areaDp = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();

		routeDetailsDP_str = new ArrayList<>();
		zoneDetailsDP_str = new ArrayList<>();
		routeDp_str = new ArrayList<>();
		religionsDP_str = new ArrayList<>();
		paymentDP_str = new ArrayList<>();
		shopTypeDP_str = new ArrayList<>();
		areaDp_str = new ArrayList<>();


		defaultAreaNameSelect();


		HttpAdapter.getZoneDetailsDP(UpdateShopDetailsActvity.this, "zoneName");
		HttpAdapter.getRoute(UpdateShopDetailsActvity.this, "routeCode");
		HttpAdapter.getReligion(UpdateShopDetailsActvity.this, "getReligion");
		HttpAdapter.getPayment(UpdateShopDetailsActvity.this, "payment");
//		HttpAdapter.shopType(UpdateShopDetailsActvity.this, "shoptypeDP");
		HttpAdapter.getShopDetailsDP(UpdateShopDetailsActvity.this, "shoptypeDP", "0");
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
			// mMap.setMyLocationEnabled(true);
		}

		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create()
		                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
		                                  .setInterval(10 * 1000)        // 10 seconds, in milliseconds
		                                  .setFastestInterval(1 * 1000); // 1 second, in milliseconds


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
						/*String jsonString = new Gson()
								.toJson(registerShops(shopname.getText().toString(), "shopDescription", "1", routeCode, ownername.getText().toString(),
								                      shop_address.getText().toString(),
								                      mobile.getText().toString(),
								                      phone.getText().toString(), lat.getText().toString(), lang.getText().toString(), "0", locationName.getText().toString(),
								                      shopTypeDropdown, "1", "2016-01-01", createdby.getText().toString(), religionDropdown, paymentDropDown, "1"));
						Log.d("jsonString", jsonString);*/
						new CreateShopTask().execute();
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
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
	}

	private static Map<String, String> registerShops(String shopName, String shopDescription, String routeId, String routeCode, String ownerName, String address,
	                                                 String mobileNumber, String phoneNumbe,
	                                                 String latitude, String longitude, String gPSL, String locationName, String shopTypeId, String active, String createdOn,
	                                                 String createdby, String ReligionID, String PaymentTermID, String AreaId)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("shopName", shopName);
		map.put("shopDescription", shopDescription);
		map.put("routeId", routeId);
		map.put("routeCode", routeCode);
		map.put("ownerName", ownerName);
		map.put("address", address);
		map.put("mobileNumber", mobileNumber);
		map.put("phoneNumbe", phoneNumbe);
		map.put("latitude", latitude);
		map.put("longitude", longitude);
		map.put("gPSL", gPSL);
		map.put("locationName", locationName);
		map.put("shopTypeId", shopTypeId);
		map.put("active", active);
		map.put("createdOn", createdOn);
		map.put("createdby", createdby);
		map.put("ReligionID", ReligionID);
		map.put("PaymentTermID", PaymentTermID);
		map.put("AreaId", AreaId);
		return map;
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
				//register
				if (response.getTag().equals("registerShops"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						AlertDialogManager.showAlertOnly(this, "ShopCreated", mJson.getString("Message"), "Ok");
						shopname.setText("");
						ownername.setText("");
						shop_address.setText("");
						pin.setText("");
						mobile.setText("");
						phone.setText("");
						locationName.setText("");
						payment.setText("");
					}
					else
					{
						AlertDialogManager.showAlertOnly(this, "BrightUdyog", mJson.getString("Message"), "Ok");
					}
				}


				//ZoneDetails DropDown
				else if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
					}
				}
				//RouteNames DropDown
				//Route Codes
				else if (response.getTag().equals("routeCode"))
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

				else if (response.getTag().equals("shoptypeDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						shopNameSpinnerAdapter(jsonArray);
//						shopNamesSpinnerAdapter(jsonArray);
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

					else
					{
						//shopName_spinner.setVisibility(View.GONE);
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
			int zoneId = jsnobj.getInt("ZoneId");
			String areaName = jsnobj.getString("AreaName");
			int areaId = jsnobj.getInt("AreaId");


			try
			{
				//Zone Name Drop down
				/*if (zoneId != 0)
				{
					zone_sp.setSelection(zoneId);
				}*/
				if (zoneId != 0)
				{
//					zone_sp.setSelection(zoneId);
					zone_sp.setSelection(getIndex(zone_sp, zoneName, _zoneNamesData));
					selected_zoneId = String.valueOf(zoneId);

					if (_zoneNamesData.size() != 0)
					{
						for (int i = 0; i < _zoneNamesData.size(); i++)
						{
							String availName = _zoneNamesData.get(i).getShopName();
							if (availName.equals(zoneName))
							{
								selected_zoneId = _zoneNamesData.get(i).getShopId();
								Log.e("routeId", selected_zoneId + "");
							}
						}
					}
				}



				//Route Name Drop down
				if (routeName != null && !routeName.isEmpty() && !routeName.equalsIgnoreCase("null"))
				{
//					routecd.setSelection(routeId); //routeName
//					routeName = "Route No 04";
					routecd.setSelection(getIndex(routecd, routeName, _routeCodesData));
					if (_routeCodesData.size() != 0)
					{
						for (int i = 0; i < _routeCodesData.size(); i++)
						{
							String availName = _routeCodesData.get(i).getShopName();
							if (availName.equals(routeName))
							{
								routeCode = _routeCodesData.get(i).getShopId();
								Log.e("routeId", routeCode + "");
							}
						}
					}
				}

				//Area Name Drop down
			/*	if (areaId != 0)
				{
					areaName_sp.setSelection(areaId);
				}*/
				if (areaId != 0)
				{
					//areaName_sp.setSelection(areaId);
					areaName_sp.setSelection(getIndex(areaName_sp, areaName, _areaNamesData));
					selected_areaNameId = String.valueOf(areaId);

					if (_areaNamesData.size() != 0)
					{
						for (int i = 0; i < _areaNamesData.size(); i++)
						{
							String availName = _areaNamesData.get(i).getShopName();
							if (availName.equals(areaName))
							{
								selected_areaNameId = _areaNamesData.get(i).getShopId();
								Log.e("routeId", selected_areaNameId + "");
							}
						}
					}
				}

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

				//PinCode
				/*if (PinCode != null && !PinCode.isEmpty() && !PinCode.equalsIgnoreCase("null"))
				{
					pin.setText(PinCode + "");
				}*/
				//Religion Drop Down selection
				if (ReligionId != 0)
				{
//					religion.setSelection(ReligionId);
					religion.setSelection(getIndex(religion, ReligionName, _religionsData));
					religionDropdown = String.valueOf(ReligionId);
				}

				//Payment Drop Down selection
				if (PaymentTermsId != 0)
				{
//					payment_sp.setSelection(PaymentTermsId);
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
		ProgressDialog pd = new ProgressDialog(UpdateShopDetailsActvity.this);

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
						//AlertDialogManager.showAlertOnly(UpdateShopDetailsActvity.this, "BrightUdyog", "Shop Created Successfully" /*result.getString("Message")*/, "Ok");
//						if (result.getString("Message").equalsIgnoreCase(""))
						Toast.makeText(UpdateShopDetailsActvity.this, "Successfully Shop Details Updated..", Toast.LENGTH_SHORT).show();
						refreshActivity();
					}
					else
					{
						//Toast.makeText(UpdateShopDetailsActvity.this, "Shop Creation Failed", Toast.LENGTH_SHORT).show();
						Toast.makeText(UpdateShopDetailsActvity.this, "Details Failed to Updated", Toast.LENGTH_SHORT).show();
						refreshActivity();
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

	private void refreshActivity()
	{
		Intent i = getIntent();
		finish();
		startActivity(i);
	}

	/**
	 * If connected get lat and long
	 */
	@Override
	public void onConnected(Bundle bundle)
	{
	  /*  mLocationRequest = new LocationRequest();
	    mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);*/

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
			//If everything went fine lets get latitude and longitude
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
	        /*
	         * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
		if (connectionResult.hasResolution())
		{
			try
			{
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
				    /*
				     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
			}
			catch (IntentSender.SendIntentException e)
			{
				// Log the error
				e.printStackTrace();
			}
		}
		else
		{
		        /*
		         * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
			Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
		}
	}

	/**
	 * If locationChanges change lat and long
	 *
	 * @param location
	 */
	@Override
	public void onLocationChanged(Location location)
	{
		lat.setText("" + location.getLatitude());
		lang.setText("" + location.getLongitude());
		/*CameraUpdate cu = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
		googleMap.animateCamera(cu);*/

	}

	public boolean checkLocationPermission()
	{
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
			{
				// Show an expanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

				//Prompt the user once explanation has been shown
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
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				{
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
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
						//mMap.setMyLocationEnabled(true);
					}
				}
				else
				{
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					// Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
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

		//Initializing googleapi client
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
			Toast.makeText(getApplicationContext(), "Please Select Route Name", Toast.LENGTH_SHORT).show();
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
				if (position != 0)
				{
//					zoneNameDropdown = _zoneNamesData.get(position - 1).getShopId();
					selected_zoneId = _zoneNamesData.get(position - 1).getShopId();
					selected_roueId = "";
					HttpAdapter.getRouteDetails(UpdateShopDetailsActvity.this, "routeCode", selected_zoneId);
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
				_routeCodesData.add(new ShopNamesData(shopId, shopNamee));
			}
			routeNamestitle.add("Route Name");
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

		routecd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					routeNameDropDown = _routeCodesData.get(position - 1).getShopId();
					selected_roueId = _routeCodesData.get(position - 1).getShopId(); //3
					HttpAdapter.getAreaDetailsByRoute(UpdateShopDetailsActvity.this, "areaNameDP", routeNameDropDown);
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
		areaName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					areaDropDwn = _areaNamesData.get(position - 1).getShopId();
					selected_areaNameId = _areaNamesData.get(position - 1).getShopId();
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
		Log.e("shopDropdown", jsonArray.toString() + "");
		try
		{
			_shoptypesData.clear();
			shoptypesNamestitle.clear();
			_shoptypesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				/*String shopId = jsnobj.getString("ShopTypeId");
				String shopNamee = jsnobj.getString("ShopTypeName");*/
				String shopId = jsnobj.getString("ShopId");
				String shopNamee = jsnobj.getString("ShopName");
				//ShopName
				_shoptypesData.add(new ShopNamesData(shopId, shopNamee));
			}
			shoptypesNamestitle.add("Select Shop Name");
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
				try
				{
					if (position != 0)
					{
						shopTypeDropdown = _shoptypesData.get(position - 1).getShopId();
						selected_ShopId = _shoptypesData.get(position - 1).getShopId();
						HttpAdapter.shopEditDetails(UpdateShopDetailsActvity.this, "editShopDetails", selected_ShopId);
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
			/*nameValuePairs.add(new BasicNameValuePair("ShopName", shopname.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ShopDescription", "shopDescription"));
			nameValuePairs.add(new BasicNameValuePair("OwnerName", ownername.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Address", shop_address.getText().toString()));
			//nameValuePairs.add(new BasicNameValuePair("Pincode", pin.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Latitude", lat.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Longitude", lang.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("PhoneNumber", phone.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("MobileNumber", mobile.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("GPSL", "0"));
			nameValuePairs.add(new BasicNameValuePair("LocationName", locationName.getText().toString()));
			//nameValuePairs.add(new BasicNameValuePair("PaymentMode", payment.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ZoneId", selected_zoneId));
			nameValuePairs.add(new BasicNameValuePair("RouteId", routeCode));
			nameValuePairs.add(new BasicNameValuePair("ShopTypeId", shopTypeDropdown));
			nameValuePairs.add(new BasicNameValuePair("ReligionID", religionDropdown));
			nameValuePairs.add(new BasicNameValuePair("PaymentTermId", paymentDropDown));
			nameValuePairs.add(new BasicNameValuePair("AreaId", areaDropDwn));
			nameValuePairs.add(new BasicNameValuePair("Active", "Y"));*/
			nameValuePairs.add(new BasicNameValuePair("shopId", selected_ShopId));
			nameValuePairs.add(new BasicNameValuePair("ShopName", shopname.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ShopDescription", "Good"));
			nameValuePairs.add(new BasicNameValuePair("OwnerName", ownername.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Address", shop_address.getText().toString()));
			//nameValuePairs.add(new BasicNameValuePair("Pincode", pin.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Latitude", lat.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Longitude", lang.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("PhoneNumber", phone.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("MobileNumber", mobile.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("GPSL", GPSL));
			nameValuePairs.add(new BasicNameValuePair("LocationName", locationName.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ZoneId", selected_zoneId));
			nameValuePairs.add(new BasicNameValuePair("RouteId", routeCode));
			nameValuePairs.add(new BasicNameValuePair("ShopTypeId", shopTypeDropdown));
			nameValuePairs.add(new BasicNameValuePair("ReligionID", religionDropdown));
			nameValuePairs.add(new BasicNameValuePair("PaymentTermId", paymentDropDown));
			nameValuePairs.add(new BasicNameValuePair("AreaId", areaDropDwn));
			nameValuePairs.add(new BasicNameValuePair("Active", "Y"));
			nameValuePairs.add(new BasicNameValuePair("DateTime", updateDate));
			nameValuePairs.add(new BasicNameValuePair("Createdby", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
			nameValuePairs.add(new BasicNameValuePair("EmailID", emailId.getText().toString()));
			Log.d("params", nameValuePairs.toString());
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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

	/*//private method of your class
	private int getIndex(Spinner spinner, String myString)
	{
		int index = 0;

		for (int i = 0; i < spinner.getCount(); i++)
		{
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString))
			{
				index = i;
				break;
			}
		}
		return index;
	}*/

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
}

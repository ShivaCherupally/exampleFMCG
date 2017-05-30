package com.fmcg.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import com.fmcg.models.AreaDropDown;
import com.fmcg.models.GetAreaDetailsByRouteId;
import com.fmcg.models.GetRouteDetails;
import com.fmcg.models.GetRouteDropDown;
import com.fmcg.models.GetZoneDetails;
import com.fmcg.models.PaymentDropDown;
import com.fmcg.models.ReligionsDropDown;
import com.fmcg.models.GetShopTypeDropDown;
import com.fmcg.models.ShopNamesData;
import com.fmcg.models.UserDetails;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.Common;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Utility;
import com.fmcg.util.Validation;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.MultihomePlainSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.fmcg.Dotsoft.R.id.emailId;


public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                     View.OnClickListener, ConnectionCallbacks,
                                                                     LocationListener, OnConnectionFailedListener, NetworkOperationListener, AdapterView.OnItemSelectedListener
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
	TextView mydayPlan, shop, maps, getshops, mylocation, new_customer, endTrip, remarks, logout, order, invoice, userName, shop_update, remainder;
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


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawerview);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Home");
		//setSupportActionBar(toolbar);
		mContext = NavigationActivity.this;

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.app_name, R.string.app_name);
		drawer.setDrawerListener(toggle);
		toggle.syncState();


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
		emailId = (EditText) findViewById(R.id.emailId);
		submit = (TextView) findViewById(R.id.submit);


		shopName_spinner = (Spinner) findViewById(R.id.shopName_spinner);
		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routecd = (Spinner) findViewById(R.id.routecd);
		areaName_sp = (Spinner) findViewById(R.id.area_name);
		shoptype_sp = (Spinner) findViewById(R.id.shop_type_dp);
		religion = (Spinner) findViewById(R.id.religion);
		payment_sp = (Spinner) findViewById(R.id.payment_sp);
		//routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);
		shopName_spinner.setVisibility(View.GONE);

		defaultAreaNameSelect();

		HttpAdapter.getZoneDetailsDP(NavigationActivity.this, "zoneName");
		HttpAdapter.getRoute(NavigationActivity.this, "routeCode");
		HttpAdapter.getReligion(NavigationActivity.this, "getReligion");
		HttpAdapter.getPayment(NavigationActivity.this, "payment");
		HttpAdapter.shopType(NavigationActivity.this, "shoptypeDP");

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
						String jsonString = new Gson()
								.toJson(registerShops(shopname.getText().toString(), "shopDescription", "1", routeCode, ownername.getText().toString(),
								                      shop_address.getText().toString(),
								                      mobile.getText().toString(),
								                      phone.getText().toString(), lat.getText().toString(), lang.getText().toString(), "0", locationName.getText().toString(),
								                      shopTypeDropdown, "1", "2016-01-01", createdby.getText().toString(), religionDropdown, paymentDropDown, "1"));
						Log.d("jsonString", jsonString);
						new CreateShopTask().execute();
					}
					else
					{
						Toast.makeText(mContext, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
					}

				}


			}
		});
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

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.side_menu, navigationView);
		mydayPlan = (TextView) view.findViewById(R.id.myDayPlan);
		shop = (TextView) view.findViewById(R.id.shop_tv);
		remarks = (TextView) view.findViewById(R.id.remarks);
		// maps = (TextView) view.findViewById(R.id.maps_tv);
		getshops = (TextView) view.findViewById(R.id.get_shops_tv);
		mylocation = (TextView) view.findViewById(R.id.my_loc_tv);
		new_customer = (TextView) view.findViewById(R.id.add_new_customer);
		endTrip = (TextView) view.findViewById(R.id.end_trip);
		logout = (TextView) view.findViewById(R.id.logout);
		order = (TextView) view.findViewById(R.id.order_booking);
		invoice = (TextView) view.findViewById(R.id.invoice_tv);
		shop_update = (TextView) view.findViewById(R.id.shop_update);
		remainder = (TextView) view.findViewById(R.id.remainder);

		userName = (TextView) view.findViewById(R.id.userName);
		try
		{
			UserId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeCode");
			if (!UserId.equalsIgnoreCase(null) && !UserId.isEmpty())
			{
				userName.setText("User ID : " + UserId);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}


		mydayPlan.setOnClickListener(this);
		//maps.setOnClickListener(this);
		/*shop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_menu_icon, 0, 0, 0);
		shop.setCompoundDrawablePadding(5);*/
		shop.setOnClickListener(this);
		mylocation.setOnClickListener(this);
		getshops.setOnClickListener(this);
		new_customer.setOnClickListener(this);
		endTrip.setOnClickListener(this);
		remarks.setOnClickListener(this);
		logout.setOnClickListener(this);
		order.setOnClickListener(this);
		invoice.setOnClickListener(this);
		shop_update.setOnClickListener(this);
		remainder.setOnClickListener(this);

		navigationView.setNavigationItemSelectedListener(this);

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
	public void onBackPressed()
	{
		//drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		// drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onClick(View v)
	{

		/*if (v.getId() == R.id.shop_tv)
		{
			shopName_spinner.setVisibility(View.GONE);
			refreshActivity();
			   *//* Intent homepage=new Intent(this,AddNewCustomer.class);
			    startActivity(homepage);*//*
		}
		else if (v.getId() == R.id.get_shops_tv)
		{
			Intent i = new Intent(this, GetShopsByRoute.class);
			startActivity(i);
		}
		else if (v.getId() == R.id.my_loc_tv)
		{
			Intent mylocation = new Intent(this, MyLocation.class);
			startActivity(mylocation);
		}
		else if (v.getId() == R.id.shop_update)
		{
			Intent mylocation = new Intent(this, UpdateShopDetailsActvity.class);
			startActivity(mylocation);
			*//*shopName_spinner.setVisibility(View.VISIBLE);
			refreshActivity();*//*
		}
		else if (v.getId() == R.id.add_new_customer)
		{
			shopName_spinner.setVisibility(View.GONE);
			refreshActivity();
		}
		else if (v.getId() == R.id.remarks)
		{
			Intent remarks = new Intent(NavigationActivity.this, Remarks.class);
			startActivity(remarks);
		}
		else if (v.getId() == R.id.remainder)
		{
			Intent remarks = new Intent(NavigationActivity.this, RemainderListActivity.class);
			startActivity(remarks);
		}

		else if (v.getId() == R.id.logout)
		{
			logoutUserDetails();
		}
		else if (v.getId() == R.id.order_booking)
		{
			orderBookingActivity();
		}
		else if (v.getId() == R.id.invoice_tv)
		{
			invoiceGenartionActivity();
		}
		else if (v.getId() == R.id.myDayPlan)
		{
			Intent myDayplan = new Intent(NavigationActivity.this, RouteDetails.class);
			startActivity(myDayplan);
		}*/

		//drawer.closeDrawer(GravityCompat.START);
	}

	private void logoutUserDetails()
	{
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
		Intent setIntent = new Intent(this, LoginActivity.class);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
		finish();
	}

	private void invoiceGenartionActivity()
	{
		String PLAN_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		Log.e("PLAN_ACCEPTED", PLAN_ACCEPTED + "");
		if (!PLAN_ACCEPTED.isEmpty() && PLAN_ACCEPTED != null)
		{
			if (PLAN_ACCEPTED.equals("ACCEPTED"))
			{
				Intent invoice = new Intent(NavigationActivity.this, Invoice.class);
				startActivity(invoice);
			}
			else
			{
				Toast.makeText(mContext, "Please Accept My Day Plan", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "Please Accept My Day Plan", Toast.LENGTH_SHORT).show();
		}
	}

	private void orderBookingActivity()
	{
		String PLAN_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		if (!PLAN_ACCEPTED.isEmpty() && PLAN_ACCEPTED != null)
		{
			if (PLAN_ACCEPTED.equals("ACCEPTED"))
			{
				Intent order = new Intent(NavigationActivity.this, Order.class);
				startActivity(order);
			}
			else
			{
				Toast.makeText(mContext, "Please Accept My Day Plan", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "Please Accept My Day Plan", Toast.LENGTH_SHORT).show();
		}
	}

	public String serviceCall()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://202.143.96.20/Orderstest/api/Services/RegisterShop");
		try
		{
			String currentDate = DateUtil.currentDate();
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
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
			Log.e("params", nameValuePairs.toString());
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
	public void operationCompleted(NetworkResponse response)
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

				//shopName_spinner DropDown // Change after giving service
				/*else if (response.getTag().equals("zoneName"))
				{
					if (shopName_spinner.getVisibility() == View.VISIBLE)
					{
						if (mJson.getString("Message").equals("SuccessFull"))
						{
							JSONArray jsonArray = mJson.getJSONArray("Data");
							shopNamesSpinnerAdapter(jsonArray);
							zoneSpinnerAdapter(jsonArray);
						}
					}
					else
					{
						shopName_spinner.setVisibility(View.GONE);
					}
				}*/
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
						shopNamesSpinnerAdapter(jsonArray);
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
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}


		}
	}

	private void shopNamesSpinnerAdapter(JSONArray jsonArray)
	{
		try
		{
			_shopNamesData.clear();
			shooNamestitle.clear();
			_shopNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("ZoneId");
				String shopNamee = jsnobj.getString("ZoneName");
				_shopNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
			shooNamestitle.add("Zone Name");
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
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shooNamestitle);
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopName_spinner.setAdapter(dataAdapter_zoneName);
		shopName_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					selected_shopNameId = _shopNamesData.get(position - 1).getShopId();
					HttpAdapter.getRouteDetails(NavigationActivity.this, "routeCode", selected_shopNameId);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
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
					HttpAdapter.getRouteDetails(NavigationActivity.this, "routeCode", selected_zoneId);
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
					selected_roueId = _routeCodesData.get(position - 1).getShopId();
					HttpAdapter.getAreaDetailsByRoute(NavigationActivity.this, "areaNameDP", routeNameDropDown);
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
					shopTypeDropdown = _shoptypesData.get(position - 1).getShopId();
					selected_ShopId = _shoptypesData.get(position - 1).getShopId();
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


	@Override
	public void showToast(String string, int lengthLong)
	{

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{

	}


	public class CreateShopTask extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(NavigationActivity.this);

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
			Log.e("response", success);
			if (success != null)
			{
				try
				{
					JSONObject result = new JSONObject(success);


					if (result.getString("Status").equals("OK"))
					{
						//AlertDialogManager.showAlertOnly(NavigationActivity.this, "BrightUdyog", "Shop Created Successfully" /*result.getString("Message")*/, "Ok");
//						if (result.getString("Message").equalsIgnoreCase(""))
						Toast.makeText(NavigationActivity.this, "Shop Created Successfully", Toast.LENGTH_SHORT).show();
						refreshActivity();
					}
					else
					{
						Toast.makeText(NavigationActivity.this, "Shop Creation Failed", Toast.LENGTH_SHORT).show();
//						Toast.makeText(NavigationActivity.this, "Shop Created Successfully", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(getApplicationContext(), "Please Enter Your Location Name", Toast.LENGTH_LONG).show();
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

	private void defaultAreaNameSelect()
	{
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
	}

}




package com.fmcg.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.AuctionFragment.AuctionFeedFragment;
import com.fmcg.Dotsoft.R;
import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.RemainderData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.service.LocationMonitoringService;
import com.fmcg.util.Common;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by SHiva on 5/28/2017.
 */

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                    View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
                                                                    LocationListener, GoogleApiClient.OnConnectionFailedListener,
                                                                    NetworkOperationListener, AdapterView.OnItemSelectedListener,
                                                                    SeekBar.OnSeekBarChangeListener,
                                                                    OnChartValueSelectedListener, FragmentChangeInterface
{
	SharedPreferences sharedPreferences;
	TextView mydayPlan, shop, profile, getshops, mylocation, new_customer, endTrip, remarks, logout, order, invoice, userName, shop_update, remainder,
			viewList, pendingBills, mastercreation, month_summary;
	ImageView profileIv;
	DrawerLayout drawer;
	Toolbar toolbar;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private LocationManager locationManager;
	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
	Context mContext;
	String UserId = "", EmployeeId = "", RoleId = "";

	//////// Pie Chart
	private PieChart mChart;// mChart2;
	private SeekBar mSeekBarX, mSeekBarY;
	private TextView tvX, tvY;
	protected String[] mParties = new String[]{
			"Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
			"Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
			"Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
			"Party Y", "Party Z"
	};

	private float[] yData = {25.3f, 44.7f, 25.00f, 25.00f};
	private String[] xData = {"Sample 1", "Sample 2"};

	private float[] mCircleValues = new float[2];
	private float[] mBarGraphValues = new float[2];

	BarChart barChart;

	RemainderDataBase remainderDb;
	TextView targetAmount, salesAmount, month;
	String MonthName = "";

	Activity mactivity;
	boolean doubleBackToExitPressedOnce = false;

	private boolean mAlreadyStartedService = false;
	TextView heading;
	NavigationView navigationView;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		initializeViews();
		initRest();
		locationAccess();
		remanderDateAndTimeCheck();
	}

	private void initRest()
	{
		mydayPlan.setOnClickListener(this);
		shop.setOnClickListener(this);
		profile.setOnClickListener(this);
		profileIv.setOnClickListener(this);
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
		viewList.setOnClickListener(this);
		pendingBills.setOnClickListener(this);
		mastercreation.setOnClickListener(this);
		month_summary.setOnClickListener(this);
		navigationView.setNavigationItemSelectedListener(this);
		startService(new Intent(getBaseContext(), RemainderService.class));
		try
		{
			UserId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeCode");
			EmployeeId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeId");
			RoleId = SharedPrefsUtil.getStringPreference(mContext, "RoleId");
			Log.e("EmployeeId", EmployeeId);
			Log.e("RoleId", RoleId);
			if (!UserId.equalsIgnoreCase(null) && !UserId.isEmpty())
			{
				profile.setText("User ID : " + UserId);
			}

			if (!RoleId.equalsIgnoreCase(null) && !RoleId.isEmpty())
			{
				if (RoleId.equals("5"))
				{
					mastercreation.setVisibility(View.VISIBLE);
				}
				else
				{
					mastercreation.setVisibility(View.VISIBLE);
				}

			}


			if (!EmployeeId.isEmpty() && EmployeeId != null && !EmployeeId.equalsIgnoreCase("null"))
			{
				HttpAdapter.dashboardTargetAmount(DashboardActivity.this, "TargetAmount", EmployeeId);
				HttpAdapter.dashboardSalesAmount(DashboardActivity.this, "SalesAmount", EmployeeId);
				HttpAdapter.dashboardSalesRatio(DashboardActivity.this, "SalesRatio", EmployeeId);
				HttpAdapter.dashboardSalesMonth(DashboardActivity.this, "SalesMonth", EmployeeId);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}

	private void locationAccess()
	{

		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{

		}
		else
		{
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				checkLocationPermission();
			}
			else
			{
				if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
				{
				}
				if (mGoogleApiClient == null)
				{
					buildGoogleApiClient();
				}
			}
			mLocationRequest = LocationRequest.create()
			                                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
			                                  .setInterval(60000)        // 10 seconds, in milliseconds
			                                  .setFastestInterval(60000); // 1 second, in milliseconds
		}

	}

	private void initializeViews()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Home");
		targetAmount = (TextView) findViewById(R.id.targetAmount);
		salesAmount = (TextView) findViewById(R.id.salesAmount);
		month = (TextView) findViewById(R.id.month);
		heading = (TextView) findViewById(R.id.heading);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mactivity = DashboardActivity.this;
		mContext = DashboardActivity.this;
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.app_name, R.string.app_name);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		Log.e("Current Time", "Time" + DateUtil.currentTime());
		SharedPrefsUtil.setStringPreference(mContext, "STARTTIME", DateUtil.currentTime());

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.side_menu, navigationView);
		mydayPlan = (TextView) view.findViewById(R.id.myDayPlan);
		shop = (TextView) view.findViewById(R.id.shop_tv);
		profile = (TextView) view.findViewById(R.id.profile);
		profileIv = (ImageView) view.findViewById(R.id.profileIv);
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

		viewList = (TextView) view.findViewById(R.id.viewList);
		pendingBills = (TextView) view.findViewById(R.id.pendingBills);
		mastercreation = (TextView) view.findViewById(R.id.mastercreation);
		month_summary = (TextView) view.findViewById(R.id.month_summary);


		userName = (TextView) view.findViewById(R.id.userName);
		fragmentManager = getSupportFragmentManager();
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

	/////////////////////Pie Chart 1
	private void pieChart1GraphAccess()
	{
		mChart = (PieChart) findViewById(R.id.chart1);
		mChart.setRotationEnabled(true);
//		mChart.setCenterText("Agent");
//		mChart.setCenterTextSize(10);
		mChart.setDrawEntryLabels(true);
		mChart.setDrawHoleEnabled(true);
		mChart.setHoleRadius(1);
		mChart.setTransparentCircleRadius(1);
		mChart.setContentDescription("Shiva");
		addSetData();
	}

	private void addSetData()
	{
		ArrayList<PieEntry> yEntry = new ArrayList<>();
		ArrayList<String> xEntry = new ArrayList<>();
		/*for (int i = 0; i < yData.length; i++)
		{
			yEntry.add(new PieEntry(yData[i], i));
		}*/
		for (int i = 0; i < mCircleValues.length; i++)
		{
			yEntry.add(new PieEntry(mCircleValues[i], i));
		}

		for (int i = 0; i < xData.length; i++)
		{
			xEntry.add(xData[i]);
		}

		//create the Data set
		PieDataSet pieDataset = new PieDataSet(yEntry, "");
		pieDataset.setSliceSpace(2);
		pieDataset.setValueTextSize(9);


		//add color to data set
		ArrayList<Integer> colors = new ArrayList<>();
//		colors.add(Color.GRAY);
//		colors.add(Color.BLUE);
//		colors.add(Color.RED);
		colors.add(Color.parseColor("#3366cc"));
		colors.add(Color.parseColor("#ffad33")); //3366cc
		colors.add(Color.GREEN);

		colors.add(Color.CYAN);
		colors.add(Color.YELLOW);
		colors.add(Color.MAGENTA);
		pieDataset.setColors(colors);

		PieData pieData = new PieData(pieDataset);
		pieData.setValueTextColor(Color.WHITE);
		pieData.setValueTextSize(8.5f);
		pieData.setValueFormatter(new PercentFormatter());
		mChart.setData(pieData);
		mChart.invalidate();


		Description des = mChart.getDescription();
		des.setEnabled(false);

		Legend l = mChart.getLegend();
		l.setEnabled(false);

		mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
		{
			@Override
			public void onValueSelected(final Entry e, final Highlight h)
			{
				Log.e("SelectedValue", e.toString());
				Log.e("Selected", h.toString());
			}

			@Override
			public void onNothingSelected()
			{

			}
		});

	}

	/////Bar Chart
	private void barGraphDataGet()
	{
		barChart = (BarChart) findViewById(R.id.barChart);
		ArrayList<BarEntry> yVals1 = new ArrayList<>();
		ArrayList<String> xEntry = new ArrayList<>();
		barChart.setVisibleYRangeMaximum(mBarGraphValues[0], YAxis.AxisDependency.LEFT);
		barChart.setVisibleXRangeMaximum(mBarGraphValues[0]);
		barChart.moveViewTo(10, 10, YAxis.AxisDependency.LEFT);
		barChart.invalidate();
		for (int i = 0; i < mBarGraphValues.length; i++)
		{
			if (i == 0)
			{
				yVals1.add(new BarEntry(0, mBarGraphValues[i]));
			}
			if (i == 1)
			{
				yVals1.add(new BarEntry(1, mBarGraphValues[i]));
			}

		}

		final String[] ds = new String[2];
		ds[0] = "Target Value";
		ds[1] = "Sales Value";//MonthName;

		XAxis xval = barChart.getXAxis();
		xval.setDrawLabels(true);
		xval.setLabelCount(1);
//		xval.setAxisMaxValue(50f);
		xval.setPosition(XAxis.XAxisPosition.BOTTOM);
		xval.setValueFormatter(new IAxisValueFormatter()
		{
			@Override
			public String getFormattedValue(final float value, final AxisBase axis)
			{
				Log.i("zain", "value " + value);
//				return ds[0];
				return ds[Math.round(value)];

			}

		});



		/*Log.i("zain", "value " + value);
				return ds[Math.round(value)];*/


		// hide legend
		Legend legend = barChart.getLegend();
		legend.setEnabled(false);

		Description des = barChart.getDescription();
		des.setEnabled(false);

		BarDataSet set1;
		if (MonthName != null && !MonthName.isEmpty())
		{
			set1 = new BarDataSet(yVals1, "Month : " + MonthName);
			set1.setColors(ColorTemplate.MATERIAL_COLORS);

		}
		else
		{
			set1 = new BarDataSet(yVals1, "");
			set1.setColors(ColorTemplate.MATERIAL_COLORS);
		}

		ArrayList<Integer> colors = new ArrayList<>();
//		colors.add(Color.GRAY);
//		colors.add(Color.BLUE);
//		colors.add(Color.RED);
		colors.add(Color.parseColor("#ffad33")); //3366cc
		colors.add(Color.parseColor("#3366cc"));
		colors.add(Color.GREEN);

		colors.add(Color.CYAN);
		colors.add(Color.YELLOW);
		colors.add(Color.MAGENTA);
		set1.setColors(colors);


		ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
		dataSets.add(set1);
		BarData data = new BarData(dataSets);
		data.setValueTextSize(10f);
		data.setBarWidth(0.9f);
		barChart.setTouchEnabled(false);
		barChart.setData(data);


	}

	@Override
	public void onBackPressed()
	{
		assert drawer != null;
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
			return;
		}
		else if (doubleBackToExitPressedOnce)
		{
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
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

		if (v.getId() == R.id.shop_tv)
		{
			refreshActivity();
		}


		else if (v.getId() == R.id.get_shops_tv)
		{
			Intent i = new Intent(this, GetShopsByRoute.class);
			startActivity(i);
		}
		else if (v.getId() == R.id.profile)
		{
			profilePageAccess();

		}
		else if (v.getId() == R.id.profileIv)
		{
			profilePageAccess();
		}
		//
		else if (v.getId() == R.id.my_loc_tv)
		{
			Intent mylocation = new Intent(this, MyLocation.class);
			startActivity(mylocation);
		}
		else if (v.getId() == R.id.shop_update)
		{
//			Intent mylocation = new Intent(this, UpdateCustomerActivity.class);
			Intent mylocation = new Intent(this, UpdateCustomerNewActivity.class);
			startActivity(mylocation);
		}
		else if (v.getId() == R.id.add_new_customer)
		{

			addNewCustomerActivity();
		}
		else if (v.getId() == R.id.mastercreation)
		{
			Intent inttt = new Intent(DashboardActivity.this, MasterCreationActivity.class);
			Util.killMaster();
			startActivity(inttt);
		}
		else if (v.getId() == R.id.month_summary)
		{
			Intent inttt = new Intent(DashboardActivity.this, MonthlySummary.class);
			Util.killMonth();
			startActivity(inttt);
		}
		else if (v.getId() == R.id.remarks)
		{
			Intent remarks = new Intent(DashboardActivity.this, Remarks.class);
			Util.killRemarks();
			startActivity(remarks);
		}
		else if (v.getId() == R.id.remainder)
		{
			Intent remarks = new Intent(DashboardActivity.this, RemainderListActivity.class);
			Util.killRemainderList();
			startActivity(remarks);
		}
		else if (v.getId() == R.id.viewList)
		{
//			beginTransact(new AuctionFeedFragment());

			Intent remarks = new Intent(DashboardActivity.this, ViewListActivity.class);
			Util.killPendingListActivity();
			startActivity(remarks);
		}
		else if (v.getId() == R.id.pendingBills)
		{
			Intent inttt = new Intent(DashboardActivity.this, PendingBillsActivity.class);
			Util.killMonth();
			startActivity(inttt);
			//Toast.makeText(DashboardActivity.this, "Coming Soon..", Toast.LENGTH_SHORT).show();
			/*Intent remarks = new Intent(DashboardActivity.this, SampleGoogleDirectionMap.class);
			Util.killAddRemainder();
			startActivity(remarks);*/
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
			Intent myDayplan = new Intent(DashboardActivity.this, RouteDetails.class);
			startActivity(myDayplan);
		}

		drawer.closeDrawer(GravityCompat.START);
	}

	private void profilePageAccess()
	{
		Intent i = new Intent(this, ProfileActivity.class);
		Util.killProfileActivity();
		startActivity(i);
	}

	private void logoutUserDetails()
	{
		SharedPrefsUtil.setStringPreference(getApplicationContext(), "USER_LOGOUT", "YES");
		Intent intent = new Intent(this, LocationMonitoringService.class);
		stopService(intent);
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

	@Override
	public void operationCompleted(NetworkResponse response)
	{
		Common.disMissDialog();
//		Log.e("response", response.getResponseString());
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("response", mJson.toString());
				if (response.getTag().equals("TargetAmount"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						try
						{
							double TargetAmount = mJson.getDouble("Data");
//							long TargetAmount = jsonobj.getLong("TargetAmount");
//							if (TargetAmount != 0.0000)
//							{
							targetAmount.setText("\u20B9" + " " + String.format("%.2f", TargetAmount) + System.getProperty("line.separator") + "Target Amount ");
//							}
						}
						catch (Exception e)
						{
							Log.e("error", e + "");
						}
					}
				}
				else if (response.getTag().equals("SalesAmount"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						try
						{
							double SalesAmount = mJson.getDouble("Data");
							Log.e("salesamount", String.valueOf(SalesAmount));
//							long SalesAmount = jsonobj.getLong("SalesAmount");
							/*if (SalesAmount != 0.0000)
							{*/
							salesAmount.setText("\u20B9" + " " + String.format("%.2f", SalesAmount) + System.getProperty("line.separator") + "Sales Amount ");
//								salesAmount.setText("First line of text" + System.getProperty("line.separator") + "Linija 2");

							//<string name="Rs">\u20B9</string>
//							}
						}
						catch (Exception e)
						{
							Log.e("error", e + "");
						}
					}
				}
				else if (response.getTag().equals("SalesRatio"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						try
						{
							JSONObject jsonobj = mJson.getJSONObject("Data");
							double TotalAmount = jsonobj.getDouble("TotalAmount");
							double SalesRatio = jsonobj.getDouble("SalesRatio");


							double differenceValue = 100.0 - SalesRatio;

							mCircleValues[0] = (float) (SalesRatio);
							mCircleValues[1] = (float) (differenceValue);

							pieChart1GraphAccess();

							if (TotalAmount != 0.0000)
							{
							}
							if (SalesRatio != 0.0000)
							{
							}
						}
						catch (Exception e)
						{
							Log.e("error", e + "");
						}
					}
				}
				else if (response.getTag().equals("SalesMonth"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						try
						{
							JSONObject jsonobj = mJson.getJSONObject("Data");
							MonthName = jsonobj.getString("MonthName");
							double TargetAmount = jsonobj.getDouble("TargetAmount");
							double SalesAmount = jsonobj.getDouble("SalesAmount");

							if (MonthName != null && !MonthName.equalsIgnoreCase("null"))
							{
								month.setText("Month : " + MonthName);
							}
							/*{"Data":{"MonthName":"Jan","TargetAmount":0.0000,"SalesAmount":0.0000},"StatusCode":"000","Status":"OK","ResponseID":"000","Message":"SuccessFull"}*/

							/*TargetAmount = 100.0;
							SalesAmount = 20.0;*/
							if (TargetAmount != 0.0000)
							{
								double differenceValue = TargetAmount - SalesAmount;
								mBarGraphValues[0] = (float) (TargetAmount);
								mBarGraphValues[1] = (float) (SalesAmount);
								//barGraphData(TargetAmount, SalesAmount);
								//barGraphDataInserting();
								barGraphDataGet();
							}
						}
						catch (Exception e)
						{
							Log.e("error", e + "");
						}
					}
				}
				else if (response.getTag().equals("TRACKING_STATUS"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						Toast.makeText(getApplicationContext(), "Your Location Tracking...", Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Tracking Failed", Toast.LENGTH_SHORT).show();
					}

				}
				//

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}


		}
		else
		{
			Log.e("server error", "server error");
		}
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

	@Override
	public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser)
	{
		tvX.setText("" + (mSeekBarX.getProgress()));
		tvY.setText("" + (mSeekBarY.getProgress()));
		setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
	}

	@Override
	public void onStartTrackingTouch(final SeekBar seekBar)
	{

	}

	@Override
	public void onStopTrackingTouch(final SeekBar seekBar)
	{

	}

	@Override
	public void onValueSelected(final Entry e, final Highlight h)
	{
		if (e == null)
		{
			return;
		}
		Log.i("VAL SELECTED",
		      "Value: " + e.getY() + ", index: " + h.getX()
				      + ", DataSet index: " + h.getDataSetIndex());
	}

	@Override
	public void onNothingSelected()
	{
		Log.i("PieChart", "nothing selected");
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

		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{

		}
		else
		{
			if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED)
			{
				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
			}

			Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

			if (location == null)
			{
			}
			else
			{
			}
		}
	}


	@Override
	public void onConnectionSuspended(int i)
	{
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{

		}
		else
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

	}

	/**
	 * If locationChanges change lat and long
	 *
	 * @param location
	 */
	@Override
	public void onLocationChanged(Location location)
	{
		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{

		}
		else
		{
			location.getLatitude();
			location.getLongitude();
		}

	}

	public boolean checkLocationPermission()
	{

		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{
			return true;
		}
		else
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
				}
				if (mGoogleApiClient == null)
				{
					buildGoogleApiClient();
				}

				return true;
			}
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{
		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{

		}
		else
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
								trackLocationEveryFiveMint();
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

	}

	private void buildGoogleApiClient()
	{
		if (SharedPrefsUtil.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED") != null && SharedPrefsUtil
				.getStringPreference(getApplicationContext(), "LOCATION_ACCESSED").equals("DONE"))
		{

		}
		else
		{
			//Initializing googleapi client
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
			mGoogleApiClient.connect();
			trackLocationEveryFiveMint();
		}

	}


	private void orderBookingActivity()
	{
		String PLAN_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		String ORDER_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "ORDER_ACCEPTED");
		//
//		PLAN_ACCEPTED  = "ACCEPTED";
		if (!PLAN_ACCEPTED.isEmpty() && PLAN_ACCEPTED != null)
		{
			if (PLAN_ACCEPTED.equals("ACCEPTED"))
			{
				Intent order = new Intent(DashboardActivity.this, Order.class);
				startActivity(order);
			}
			else
			{
				Toast.makeText(mContext, "Please Accept My Daily Program", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "Please Accept My Daily Program", Toast.LENGTH_SHORT).show();
		}
	}

	private void addNewCustomerActivity()
	{
		String PLAN_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		//
		if (!PLAN_ACCEPTED.isEmpty() && PLAN_ACCEPTED != null)
		{
			if (PLAN_ACCEPTED.equals("ACCEPTED"))
			{
				Intent mylocation = new Intent(this, AddNewCustomer.class);
				startActivity(mylocation);
			}
			else
			{
				Toast.makeText(mContext, "Please Accept My Daily Program", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "Please Accept My Daily Program", Toast.LENGTH_SHORT).show();
		}
	}


	private void invoiceGenartionActivity()
	{
		String PLAN_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		Log.e("PLAN_ACCEPTED", PLAN_ACCEPTED + "");
		if (!PLAN_ACCEPTED.isEmpty() && PLAN_ACCEPTED != null)
		{
			if (PLAN_ACCEPTED.equals("ACCEPTED"))
			{
				Intent invoice = new Intent(DashboardActivity.this, Invoice.class);
				startActivity(invoice);
			}
			else
			{
				Toast.makeText(mContext, "Please Accept My Daily Program", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "Please Accept My Daily Program", Toast.LENGTH_SHORT).show();
		}
	}

	//////////////////
	private void setData(int count, float range)
	{
		float mult = range;
		ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
		for (int i = 0; i < count; i++)
		{
			entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
			                         mParties[i % mParties.length],
			                         getResources().getDrawable(R.drawable.ic_audiotrack)));
		}

		PieDataSet dataSet = new PieDataSet(entries, "Election Results");

//		dataSet.setDrawIcons(false);

		dataSet.setSliceSpace(3f);
//		dataSet.setIconsOffset(new MPPointF(0, 40));
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<Integer>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
		{
			colors.add(c);
		}

		for (int c : ColorTemplate.JOYFUL_COLORS)
		{
			colors.add(c);
		}

		for (int c : ColorTemplate.COLORFUL_COLORS)
		{
			colors.add(c);
		}

		for (int c : ColorTemplate.LIBERTY_COLORS)
		{
			colors.add(c);
		}

		for (int c : ColorTemplate.PASTEL_COLORS)
		{
			colors.add(c);
		}

		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		//dataSet.setSelectionShift(0f);

		PieData data = new PieData(dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.WHITE);
		mChart.setData(data);
		mChart.highlightValues(null);
		mChart.invalidate();
	}

	@Override
	protected void onResume()
	{
		startService(new Intent(getBaseContext(), RemainderService.class));
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		startService(new Intent(getBaseContext(), RemainderService.class));
		super.onPause();

	}


	@Override
	protected void onDestroy()
	{
		stopService(new Intent(this, LocationMonitoringService.class));
		mAlreadyStartedService = false;
		super.onDestroy();
	}

	private void trackLocationEveryFiveMint()
	{
		if (!mAlreadyStartedService && heading != null)
		{
			Intent intent = new Intent(this, LocationMonitoringService.class);
			startService(intent);
			mAlreadyStartedService = true;
		}
	}

	@Override
	public void beginTransact(final Fragment fragment)
	{
//		fragmentManager.beginTransaction()
//		               .replace(R.id.main_container, fragment)
//		               .addToBackStack("")
//		               .commit();
	}
}

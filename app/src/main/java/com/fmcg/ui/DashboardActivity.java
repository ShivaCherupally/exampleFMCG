
package com.fmcg.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import com.fmcg.Dotsoft.R;
import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.RemainderData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
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
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SHiva on 5/28/2017.
 */

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                    View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
                                                                    LocationListener, GoogleApiClient.OnConnectionFailedListener, NetworkOperationListener, AdapterView.OnItemSelectedListener,
                                                                    SeekBar.OnSeekBarChangeListener,
                                                                    OnChartValueSelectedListener
{
	SharedPreferences sharedPreferences;
	TextView mydayPlan, shop, profile, maps, getshops, mylocation, new_customer, endTrip, remarks, logout, order, invoice, userName, shop_update, remainder,
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
	String UserId = "", EmployeeId = "";

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

	/*private float[] yData = {25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f};
	private String[] xData = {"Sample 1", "Sample 2", "Sample 3", "Sample 4", "Sample 5", "Sample 6", "Sample 7"};*/
	private float[] yData = {25.3f, 44.7f, 25.00f, 25.00f};
	private String[] xData = {"Sample 1", "Sample 2"};

	private float[] mCircleValues = new float[2];
	private float[] mBarGraphValues = new float[2];

	BarChart barChart;
	BarChart chart;
	ArrayList<BarEntry> BARENTRY;
	ArrayList<String> BarEntryLabels;
	BarDataSet Bardataset;
	BarData BARDATA;

	ArrayList<String> dates;
	Random random;
	ArrayList<BarEntry> barEntries;
	RemainderDataBase remainderDb;
	TextView targetAmount, salesAmount, month;
	String MonthName = "";

	Activity mactivity;
	boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		                     WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.home_activity);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("Home");

		targetAmount = (TextView) findViewById(R.id.targetAmount);
		salesAmount = (TextView) findViewById(R.id.salesAmount);
		month = (TextView) findViewById(R.id.month);

		//normalTest();
		mactivity = DashboardActivity.this;

		//setSupportActionBar(toolbar);
		mContext = DashboardActivity.this;
		remanderDateAndTimeCheck();
		startService(new Intent(getBaseContext(), RemainderService.class));
//		startService(mactivity);
		//pieChart1GraphAccess();
//		pieChart2GraphAccess();
		//barGraphDataGet();
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.app_name, R.string.app_name);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

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

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
		try
		{
			UserId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeCode");
			EmployeeId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeId");
			Log.e("EmployeeId", EmployeeId);

			if (!UserId.equalsIgnoreCase(null) && !UserId.isEmpty())
			{
				profile.setText("User ID : " + UserId);
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

	}

//	private void normalTest()
//	{
//		String a = "10", b = null, c = "30", d = "40", e = "50", f = "60";
//		try
//		{
//			if (a.equals("10"))
//			{
//				Toast.makeText(getApplicationContext(), "Success " + "state1", Toast.LENGTH_SHORT).show();
//			}
//			if (b.equals("10"))
//			{
//				Toast.makeText(getApplicationContext(), "Success " + "state2", Toast.LENGTH_SHORT).show();
//			}
//			if (a.equals("10"))
//			{
//				Toast.makeText(getApplicationContext(), "Success " + "state3", Toast.LENGTH_SHORT).show();
//			}
//		}
//		catch (Exception ex)
//		{
//			if (c.equals("30"))
//			{
//				Toast.makeText(getApplicationContext(), "Success " + "state4", Toast.LENGTH_SHORT).show();
//			}
//		}
//		finally
//		{
//			if (b.equals("40"))
//			{
//				Toast.makeText(getApplicationContext(), "Success " + "state5", Toast.LENGTH_SHORT).show();
//			}
//		}
//		if (a.equals("10"))
//		{
//			Toast.makeText(getApplicationContext(), "Success " + "state6", Toast.LENGTH_SHORT).show();
//		}
//	}

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

	public void startService(View view)
	{
		startService(new Intent(getBaseContext(), RemainderService.class));
	}

	// Method to stop the service
	public void stopService(View view)
	{
		stopService(new Intent(getBaseContext(), RemainderService.class));
	}


	/*private void pieChart2GraphAccess()
	{
		*//*mChart2 = (PieChart) findViewById(R.id.chart2);
		mChart2.setRotationEnabled(true);
		mChart2.setCenterText("Employee");
		mChart2.setCenterTextSize(10);
		mChart2.setDrawEntryLabels(true);*//*
		addSetData2();
		// configure pie chart
		mChart.setUsePercentValues(true);
//		mChart.setDescription("Smartphones Market Share");
	}

	private void addSetData2()
	{
		ArrayList<PieEntry> yEntry = new ArrayList<>();
		ArrayList<String> xEntry = new ArrayList<>();
		for (int i = 0; i < yData.length; i++)
		{
			yEntry.add(new PieEntry(yData[i], i));
		}
		for (int i = 0; i < xData.length; i++)
		{
			xEntry.add(xData[i]);
		}

		//create the Data set
		PieDataSet pieDataset = new PieDataSet(yEntry, "");
		pieDataset.setSliceSpace(2);
		pieDataset.setValueTextSize(8);

		//add color to data set
		ArrayList<Integer> colors = new ArrayList<>();
		colors.add(Color.BLUE);
		colors.add(Color.RED);
//		colors.add(Color.RED);
//		colors.add(Color.GREEN);
//		colors.add(Color.CYAN);
		colors.add(Color.YELLOW);
		colors.add(Color.MAGENTA);
		pieDataset.setColors(colors);

		//add legend to chart
		*//*Legend legend = mChart.getLegend();
		legend.setForm(Legend.LegendForm.CIRCLE);
		legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);*//*

		//creat pie data object
		PieData pieData = new PieData(pieDataset);
		*//*mChart2.setData(pieData);
		mChart2.invalidate();

		mChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
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
		});*//*


	}*/


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

		//add legend to chart
	/*	Legend legend = mChart.getLegend();
		legend.setForm(Legend.LegendForm.CIRCLE);
		legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);*/

		//creat pie data object
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

		/*for (int i = 0; i < mBarGraphValues.length; i++)
		{
			yVals1.add(new BarEntry(mBarGraphValues[i], i));
//			yVals1.add(new BarEntry(i, mBarGraphValues[i]));
		}*/

		barChart.setVisibleYRangeMaximum(mBarGraphValues[0], YAxis.AxisDependency.LEFT);
		barChart.setVisibleXRangeMaximum(mBarGraphValues[0]);
		barChart.moveViewTo(10, 10, YAxis.AxisDependency.LEFT);
		barChart.invalidate();
		for (int i = 0; i < mBarGraphValues.length; i++)
		{
//			yVals1.add(new BarEntry(mBarGraphValues[i], i));
//			yVals1.add(new BarEntry(i, mBarGraphValues[i]));
//			float val = (float) (Math.random());
			if (i == 0)
			{
				yVals1.add(new BarEntry(0, mBarGraphValues[i]));
			}
			if (i == 1)
			{
				yVals1.add(new BarEntry(1, mBarGraphValues[i]));
			}

		}

		/*for (int i = 0; i < xData.length; i++)
		{
			xEntry.add(xData[i]);
		}*/

		//XAxis xAxis = barChart.getXAxis();
		//xAxis.setEnabled(false);


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


	//

	@Override
	public void onBackPressed()
	{
		/*//drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}*/

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


	/*public String serviceCall()
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
	}*/

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

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}


		}
	}

	private void barGraphDataInserting()
	{
		barChart = (BarChart) findViewById(R.id.barChart);
		barChart.setDrawBarShadow(false);
		barChart.setDrawValueAboveBar(true);
		barChart.setMaxVisibleValueCount(60);
		barChart.getDescription().setEnabled(false);
		barChart.setPinchZoom(false);

		barChart.setDrawGridBackground(false);
		XAxis xAxis = barChart.getXAxis();
		xAxis.setEnabled(false);

		YAxis leftAxis = barChart.getAxisLeft();
		leftAxis.setLabelCount(6, false);
		leftAxis.setAxisMinimum(-2.5f);
		leftAxis.setAxisMaximum(2.5f);
		leftAxis.setGranularityEnabled(true);
		leftAxis.setGranularity(0.1f);

		YAxis rightAxis = barChart.getAxisRight();
		rightAxis.setDrawGridLines(false);
		rightAxis.setLabelCount(6, false);
		rightAxis.setAxisMinimum(-2.5f);
		rightAxis.setAxisMaximum(2.5f);
		rightAxis.setGranularity(0.1f);

		/*mSeekBarX.setOnSeekBarChangeListener(this);
		mSeekBarX.setProgress(150); // set data*/

		Legend l = barChart.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
		l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
		l.setDrawInside(false);
		l.setForm(Legend.LegendForm.SQUARE);
		l.setFormSize(9f);
		l.setTextSize(11f);
		l.setXEntrySpace(4f);

		barChart.animateXY(2000, 2000);

		ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
		entries.add(new BarEntry(0.09983341664682815f, 0));
		entries.add(new BarEntry(-0.8672021794855902f, 1));

		/*for (int i = 0; i < 2; i++)
		{
			entries.add(mSinusData.get(i));
		}*/

		BarDataSet set;

		if (barChart.getData() != null &&
				barChart.getData().getDataSetCount() > 0)
		{
			set = (BarDataSet) barChart.getData().getDataSetByIndex(0);
			set.setValues(entries);
			barChart.getData().notifyDataChanged();
			barChart.notifyDataSetChanged();
		}
		else
		{
			set = new BarDataSet(entries, "Sinus Function");
			set.setColor(Color.rgb(240, 120, 124));
		}

		BarData data = new BarData(set);
		data.setValueTextSize(10f);
		data.setDrawValues(false);
		data.setBarWidth(0.8f);

		barChart.setData(data);
	}

	public void AddValuesToBARENTRY()
	{

		BARENTRY.add(new BarEntry(2f, 0));
		BARENTRY.add(new BarEntry(4f, 1));
		BARENTRY.add(new BarEntry(6f, 2));
		BARENTRY.add(new BarEntry(8f, 3));
		BARENTRY.add(new BarEntry(7f, 4));
		BARENTRY.add(new BarEntry(3f, 5));

	}

	public void AddValuesToBarEntryLabels()
	{

		BarEntryLabels.add("January");
		BarEntryLabels.add("February");
		BarEntryLabels.add("March");
		BarEntryLabels.add("April");
		BarEntryLabels.add("May");
		BarEntryLabels.add("June");

	}

	private void barGraphData(double TargetAmount, double SalesAmount)
	{
		barChart = (BarChart) findViewById(R.id.barChart);
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		yVals1.add(new BarEntry(44f, 0));
		yVals1.add(new BarEntry(88f, 1));
		/*yVals1.add(new BarEntry((float) TargetAmount, 0));
		yVals1.add(new BarEntry((float) SalesAmount, 1));*/

		BarDataSet barDataSet = new BarDataSet(yVals1, "Dates");


		/*ArrayList<String> thDataa = new ArrayList<>();
		if (MonthName != null && !MonthName.isEmpty())
		{
			thDataa.add(MonthName);
		}


		BarDataSet set1 = new BarDataSet(yVals1, "BarDataSet");
		BarData data2 = new BarData(set1);
		data2.setBarWidth(0.9f); // set custom bar width
		barChart.setData(data2);*/

		BarDataSet set;

		if (barChart.getData() != null &&
				barChart.getData().getDataSetCount() > 0)
		{
			set = (BarDataSet) barChart.getData().getDataSetByIndex(0);
			set.setValues(yVals1);
			mChart.getData().notifyDataChanged();
			mChart.notifyDataSetChanged();
		}
		else
		{
			set = new BarDataSet(yVals1, "Sinus Function");
			set.setColor(Color.rgb(240, 120, 124));
		}

		BarData data = new BarData(set);
		data.setValueTextSize(10f);
		data.setDrawValues(false);
		data.setBarWidth(0.8f);

		barChart.setData(data);

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


	public class CreateShopTask extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(DashboardActivity.this);

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

			String result = "";// = serviceCall();
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
						//AlertDialogManager.showAlertOnly(DashboardActivity.this, "BrightUdyog", "Shop Created Successfully" /*result.getString("Message")*/, "Ok");
//						if (result.getString("Message").equalsIgnoreCase(""))
						Toast.makeText(DashboardActivity.this, "Shop Created Successfully", Toast.LENGTH_SHORT).show();
						refreshActivity();
					}
					else
					{
						Toast.makeText(DashboardActivity.this, "Shop Creation Failed", Toast.LENGTH_SHORT).show();
//						Toast.makeText(DashboardActivity.this, "Shop Created Successfully", Toast.LENGTH_SHORT).show();
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
			/*lat.setText("" + location.getLatitude());
			lang.setText("" + location.getLongitude());*/

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
		location.getLatitude();
		location.getLongitude();
		/*lat.setText("" + location.getLatitude());
		lang.setText("" + location.getLongitude());*/
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


	private void orderBookingActivity()
	{
		String PLAN_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		String ORDER_ACCEPTED = SharedPrefsUtil.getStringPreference(mContext, "ORDER_ACCEPTED");
		//
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

		// NOTE: The order of the entries when being added to the entries array determines their position around the center of
		// the chart.
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
//		data.setValueTypeface(mTfLight);
		mChart.setData(data);

		// undo all highlights
		mChart.highlightValues(null);

		mChart.invalidate();
	}

	private SpannableString generateCenterSpannableText()
	{

		SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
		s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
		s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
		s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
		s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
		s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
		s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
		return s;
	}


	public void createRandomBarGraph(String Date1, String Date2)
	{

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

		try
		{
			Date date1 = simpleDateFormat.parse(Date1);
			Date date2 = simpleDateFormat.parse(Date2);

			Calendar mDate1 = Calendar.getInstance();
			Calendar mDate2 = Calendar.getInstance();
			mDate1.clear();
			mDate2.clear();

			mDate1.setTime(date1);
			mDate2.setTime(date2);

			dates = new ArrayList<>();
			dates = getList(mDate1, mDate2);

			barEntries = new ArrayList<>();
			float max = 0f;
			float value = 0f;
			random = new Random();
			for (int j = 0; j < dates.size(); j++)
			{
				max = 100f;
				value = random.nextFloat() * max;
				barEntries.add(new BarEntry(value, j));
			}

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
		/*BarData barData = new BarData(dates,barDataSet);
		barChart.setData(barData);*/
		/*BarDataSet set = new BarDataSet(barEntries, "Election Results");
		BarData data = new BarData(set);
		barChart.setData(data);*/

		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(0f, 30f));
		entries.add(new BarEntry(1f, 80f));
		entries.add(new BarEntry(2f, 60f));
		entries.add(new BarEntry(3f, 50f));
		// gap of 2f
		entries.add(new BarEntry(5f, 70f));
		entries.add(new BarEntry(6f, 60f));
		BarDataSet set1 = new BarDataSet(entries, "BarDataSet");
		BarData data2 = new BarData(set1);
		data2.setBarWidth(0.9f); // set custom bar width
		barChart.setData(data2);
		barChart.setFitBars(true); // make the x-axis fit exactly all bars
		barChart.invalidate(); // refresh
//

	}

	public ArrayList<String> getList(Calendar startDate, Calendar endDate)
	{
		ArrayList<String> list = new ArrayList<String>();
		while (startDate.compareTo(endDate) <= 0)
		{
			list.add(getDate(startDate));
			startDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		return list;
	}

	public String getDate(Calendar cld)
	{
		String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
				+ cld.get(Calendar.DAY_OF_MONTH);
		try
		{
			Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
			curDate = new SimpleDateFormat("yyy/MM/dd").format(date);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return curDate;
	}

	@Override
	protected void onResume()
	{
		//remanderDateAndTimeCheck();
		startService(new Intent(getBaseContext(), RemainderService.class));
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		startService(new Intent(getBaseContext(), RemainderService.class));
		super.onPause();
	}
}

package com.fmcg.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.RouteDetailsModel;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RouteDetails extends AppCompatActivity implements NetworkOperationListener
{
	private TextView employeeName, routeName, routeDate, noPlantxt, areanametxt, zonenametxt, route_number, accepttxt;
	LinearLayout sublayout, acceptLLID;
	Context mContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_details_byemployee);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = RouteDetails.this;

		employeeName = (TextView) findViewById(R.id.employee_name);
		routeName = (TextView) findViewById(R.id.route_name);
		routeDate = (TextView) findViewById(R.id.route_date);
		zonenametxt = (TextView) findViewById(R.id.zonenametxt);
		zonenametxt = (TextView) findViewById(R.id.areanametxt);
		route_number = (TextView) findViewById(R.id.route_number);
		accepttxt = (TextView) findViewById(R.id.accepttxt);

		noPlantxt = (TextView) findViewById(R.id.noPlantxt);

		sublayout = (LinearLayout) findViewById(R.id.sublayout);
		sublayout.setVisibility(View.GONE);
		acceptLLID = (LinearLayout) findViewById(R.id.acceptLLID);
		acceptLLID.setVisibility(View.GONE);
		String accepted = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		if (accepted != null && !accepted.isEmpty())
		{
			if (accepted.equals("ACCEPTED"))
			{
				accepttxt.setText("Accepted");
			}
		}
		else
		{
			accepttxt.setText("Accept");
		}

		accepttxt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				acceptedSubmitd();
			}
		});

		acceptLLID.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				acceptedSubmitd();
			}
		});

		Log.e("EmpolyeeId", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
		HttpAdapter.getRouteDetailsByEmployee(RouteDetails.this, "RouteDetailsModel", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));

	}

	private void acceptedSubmitd()
	{
		if (accepttxt.getText().toString().equals("Accept"))
		{
			Toast.makeText(mContext, "My Day Plan Successfully Accepted", Toast.LENGTH_SHORT).show();
			SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "ACCEPTED");
			accepttxt.setText("Accepted");
			refreshActivity();
		}
		else
		{
			Toast.makeText(mContext, "Your My Day Plan Already Accepted", Toast.LENGTH_SHORT).show();
		}
	}


	private void refreshActivity()
	{
		Intent i = getIntent();
		finish();
		startActivity(i);
	}

	@Override
	public void operationCompleted(NetworkResponse response)
	{
		Common.disMissDialog();
		Log.d("outPutResponse", String.valueOf(response));
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());

				if (response.getTag().equals("RouteDetailsModel"))
				{

					if (mJson.getString("Status").equals("OK"))
					{
						if (mJson.getString("Data").equals("null"))
						{
							sublayout.setVisibility(View.GONE);
							acceptLLID.setVisibility(View.GONE);
							noPlantxt.setVisibility(View.VISIBLE);
							SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "NOT_ACCEPTED");
							AlertDialogManager.showAlertOnly(this, "Route Details", "No Routes Found As per Now", "Ok");
						}
						else
						{
							try
							{
								sublayout.setVisibility(View.VISIBLE);
								acceptLLID.setVisibility(View.VISIBLE);
								noPlantxt.setVisibility(View.GONE);
								JSONObject jsonObject = mJson.getJSONObject("Data");
								employeeName.setText(jsonObject.getString("EmployeeName"));
								routeName.setText(jsonObject.getString("RouteName"));
								if (jsonObject.getString("RouteDate") != null && !jsonObject.getString("RouteDate").isEmpty())
								{
									String routeDateStr = jsonObject.getString("RouteDate");
									Log.e("routeDate", routeDateStr);

									SimpleDateFormat simDf = new SimpleDateFormat("yyyy-MM-dd");
									try
									{
										Date yourDate = simDf.parse(routeDateStr);
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(yourDate);
										calendar.get(Calendar.YEAR); //Day of the Year :)
										calendar.get(Calendar.DATE); //Day of the day :)
									int orderMonth = 	calendar.get(Calendar.MONTH) + 1;
										String orderDatetemp = calendar.get(Calendar.DATE) + "-" + orderMonth + "-" + calendar.get(Calendar.YEAR);
										routeDate.setText(orderDatetemp + "");
										Log.e("date", calendar.get(Calendar.YEAR) + "");
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
									//	routeDate.setText(routeDateStr + "");
								}
								zonenametxt.setText(jsonObject.getString("ZoneName"));
								route_number.setText(jsonObject.getString("RouteNumber"));
								areanametxt.setText(jsonObject.getString("AreaName"));


							}
							catch (Exception e)
							{
								Log.e("error", e + "");
							}
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

	@Override
	public void showToast(String string, int lengthLong)
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

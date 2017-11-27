package com.fmcg.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.Common;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Utility;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MonthlySummary extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity remarks;
	public SharedPreferences sharedPreferences;
	LinearLayout layouttdata;
	public TextView monthNametxt, targetamountxt, salesamounttxt, nodata;
	Context mContext;
	String zonenamestr, zonecodestr, zonedesstr;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_summary);
		remarks = MonthlySummary.this;

		mContext = MonthlySummary.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		if (Utility.isOnline(mContext))
		{
			HttpAdapter.monthSummary(MonthlySummary.this, "MonthSummary", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
		}
		else
		{
			Toast.makeText(mContext, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
		}
		layouttdata = (LinearLayout) findViewById(R.id.layouttdata);
		nodata = (TextView) findViewById(R.id.nodata);
		monthNametxt = (TextView) findViewById(R.id.monthNametxt);
		targetamountxt = (TextView) findViewById(R.id.targetamountxt);
		salesamounttxt = (TextView) findViewById(R.id.salesamounttxt);
		salesamounttxt.setVisibility(View.GONE);


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

	@Override
	public void operationCompleted(NetworkResponse response)
	{
		Common.disMissDialog();
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("mJson", mJson.toString());
				//register
				if (response.getTag().equals("MonthSummary"))
				{
					if (mJson.getString("Status").equals("OK"))
					{

						Toast.makeText(mContext, "Successfully Month Data", Toast.LENGTH_SHORT).show();
						JSONObject mJsonData = mJson.getJSONObject("Data");
						if (mJsonData != null)
						{
							layouttdata.setVisibility(View.VISIBLE);
							nodata.setVisibility(View.GONE);
							if (mJsonData.getString("MonthName") != null && !mJsonData.getString("MonthName").isEmpty())
							{
								monthNametxt.setText("Month Name : " + mJsonData.getString("MonthName"));
							}
							else
							{
								monthNametxt.setVisibility(View.GONE);
							}

							if (String.valueOf(mJsonData.getInt("TargetAmount")) != null && !String.valueOf(mJsonData.getInt("TargetAmount")).isEmpty())
							{
								targetamountxt.setText("Target Amount : " + "\u20B9" + " " + String.valueOf(mJsonData.getInt("TargetAmount")));
							}
							else
							{
								targetamountxt.setVisibility(View.GONE);
							}


							if (mJsonData.getString("SalesAmount") != null)
							{
								if (String.valueOf(mJsonData.getDouble("SalesAmount")) != null && !String.valueOf(mJsonData.getDouble("SalesAmount")).isEmpty())
								{
									salesamounttxt.setVisibility(View.VISIBLE);
									salesamounttxt.setText("Sales Amount : " + "\u20B9" + " " + String.valueOf(mJsonData.getDouble("SalesAmount")));
								}
								else
								{
									salesamounttxt.setVisibility(View.GONE);
								}
							}
							else
							{
								salesamounttxt.setVisibility(View.GONE);
							}


						}
						else
						{
							layouttdata.setVisibility(View.GONE);
							nodata.setVisibility(View.VISIBLE);
						}

//						refeshActivity();
					}
					else
					{
						Toast.makeText(mContext, "No Data", Toast.LENGTH_SHORT).show();
						layouttdata.setVisibility(View.GONE);
						nodata.setVisibility(View.VISIBLE);
//
					}
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			layouttdata.setVisibility(View.GONE);
			nodata.setVisibility(View.VISIBLE);
		}

	}

	private void refeshActivity()
	{
		Intent i = getIntent();
		finish();
		startActivity(i);
	}

	@Override
	public void showToast(String string, int lengthLong)
	{
	}
}



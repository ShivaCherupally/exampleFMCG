package com.fmcg.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Utility;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shiva on 9/24/2017.
 */

public class PendingBillsActivity extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity remarks;
	public SharedPreferences sharedPreferences;
	LinearLayout layouttdata;
	public TextView monthNametxt, targetamountxt, salesamounttxt, nodata;
	public TextView subtotaltxt, taxamounttxt, totalamounttxt;


	Context mContext;
	String zonenamestr, zonecodestr, zonedesstr;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_summary);
		remarks = PendingBillsActivity.this;

		mContext = PendingBillsActivity.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		if (Utility.isOnline(mContext))
		{
			HttpAdapter.pendingBills(PendingBillsActivity.this, "PendingBills", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
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

		subtotaltxt = (TextView) findViewById(R.id.subtotaltxt);
		taxamounttxt = (TextView) findViewById(R.id.taxamounttxt);
		totalamounttxt = (TextView) findViewById(R.id.totalamounttxt);


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
				if (response.getTag().equals("PendingBills"))
				{
					if (mJson.getString("Status").equals("OK"))
					{

						Toast.makeText(mContext, "Pending Data", Toast.LENGTH_SHORT).show();
						JSONObject mJsonData = mJson.getJSONObject("Data");
						if (mJsonData != null)
						{
							layouttdata.setVisibility(View.VISIBLE);
							nodata.setVisibility(View.GONE);
							if (mJsonData.getString("OrderNumber") != null && !mJsonData.getString("OrderNumber").isEmpty())
							{
								monthNametxt.setText("Order Number : " + mJsonData.getString("OrderNumber"));
							}
							if (mJsonData.getString("OrderDate") != null && !mJsonData.getString("OrderDate").isEmpty())
							{
								targetamountxt.setText("Order Date : " + mJsonData.getString("OrderDate"));
							}
							if (mJsonData.getString("ShopName") != null && !mJsonData.getString("ShopName").isEmpty())
							{
								salesamounttxt.setText("Shop Name : " + mJsonData.getString("ShopName"));
							}

							try
							{

								if (String.valueOf(mJsonData.getInt("SubTotalAmount")) != null && !String.valueOf(mJsonData.getInt("SubTotalAmount")).isEmpty())
								{
									subtotaltxt.setVisibility(View.VISIBLE);
									subtotaltxt.setText("Sub Total Amount : " + String.valueOf(mJsonData.getDouble("SubTotalAmount")));
								}
							}
							catch (Exception e)
							{

							}

							if (String.valueOf(mJsonData.getDouble("TaxAmount")) != null && !String.valueOf(mJsonData.getDouble("TaxAmount")).isEmpty())
							{
								taxamounttxt.setVisibility(View.VISIBLE);
								taxamounttxt.setText("Tax Amount : " + String.valueOf(mJsonData.getDouble("TaxAmount")));
							}
							if (String.valueOf(mJsonData.getDouble("TotalAmount")) != null && !String.valueOf(mJsonData.getDouble("TotalAmount")).isEmpty())
							{
								totalamounttxt.setVisibility(View.VISIBLE);
								totalamounttxt.setText("Total Amount : " + String.valueOf(mJsonData.getDouble("TotalAmount")));
							}
						}
						else
						{
							Toast.makeText(mContext, "No Data", Toast.LENGTH_SHORT).show();
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

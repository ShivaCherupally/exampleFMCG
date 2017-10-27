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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class PendingBillsActivity extends AppCompatActivity implements NetworkOperationListener, View.OnClickListener
{
	public static Activity remarks;
	public SharedPreferences sharedPreferences;
//	LinearLayout layouttdata;
//	public TextView monthNametxt, targetamountxt, salesamounttxt, nodata;
//	public TextView subtotaltxt, taxamounttxt, totalamounttxt;

	public TextView ordernotxt, orderdatetxt, shopNametxt, subtotaltxt, taxamounttxt, totalamounttxt;
	Button editBtn;
//	public TextView orderlabeltxt, shopNameLabeltxt, noOfPrductLabeltxt, subTotalLabeltxt, taxAmountLabel;

	public FrameLayout userdetailsframe;  // Arrow click first time Visible
	private FrameLayout expanded_frameLayout; // Arrow Close Icon invisible
	LinearLayout oreder_invoiceViewDetailsLayout, statusLayout, noofprductLayout;

	Context mContext;
	String zonenamestr, zonecodestr, zonedesstr;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_or_invice_item);
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
//		layouttdata = (LinearLayout) findViewById(R.id.layouttdata);
//		nodata = (TextView) findViewById(R.id.nodata);
		ordernotxt = (TextView) findViewById(R.id.ordernotxt);
		orderdatetxt = (TextView) findViewById(R.id.orderdatetxt);
		shopNametxt = (TextView) findViewById(R.id.shopNametxt);

		subtotaltxt = (TextView) findViewById(R.id.subtotaltxt);
		taxamounttxt = (TextView) findViewById(R.id.taxamounttxt);
		totalamounttxt = (TextView) findViewById(R.id.totalamounttxt);
		editBtn = (Button) findViewById(R.id.editBtn);
		editBtn.setVisibility(View.INVISIBLE);

		//View Details
		userdetailsframe = (FrameLayout) findViewById(R.id.userdetailsframe);
		expanded_frameLayout = (FrameLayout) findViewById(R.id.expanded_frameLayout);
		oreder_invoiceViewDetailsLayout = (LinearLayout) findViewById(R.id.oreder_invoiceViewDetailsLayout);

		statusLayout = (LinearLayout) findViewById(R.id.statusLayout);
		noofprductLayout = (LinearLayout) findViewById(R.id.noofprductLayout);
		statusLayout.setVisibility(View.GONE);
		noofprductLayout.setVisibility(View.GONE);
		oreder_invoiceViewDetailsLayout.setVisibility(View.GONE);
		userdetailsframe.setOnClickListener(this);
		expanded_frameLayout.setOnClickListener(this);


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
//							layouttdata.setVisibility(View.VISIBLE);
//							nodata.setVisibility(View.GONE);
							if (mJsonData.getString("OrderNumber") != null && !mJsonData.getString("OrderNumber").isEmpty())
							{
								ordernotxt.setText(mJsonData.getString("OrderNumber"));
							}
							if (mJsonData.getString("OrderDate") != null && !mJsonData.getString("OrderDate").isEmpty())
							{
								orderdatetxt.setText(mJsonData.getString("OrderDate"));
							}
							if (mJsonData.getString("ShopName") != null && !mJsonData.getString("ShopName").isEmpty())
							{
								shopNametxt.setText(mJsonData.getString("ShopName"));
							}

							try
							{

								if (String.valueOf(mJsonData.getInt("SubTotalAmount")) != null && !String.valueOf(mJsonData.getInt("SubTotalAmount")).isEmpty())
								{
									subtotaltxt.setVisibility(View.VISIBLE);
									subtotaltxt.setText(String.valueOf(mJsonData.getDouble("SubTotalAmount")));
								}
							}
							catch (Exception e)
							{

							}

							if (String.valueOf(mJsonData.getDouble("TaxAmount")) != null && !String.valueOf(mJsonData.getDouble("TaxAmount")).isEmpty())
							{
								taxamounttxt.setVisibility(View.VISIBLE);
								taxamounttxt.setText(String.valueOf(mJsonData.getDouble("TaxAmount")));
							}
							if (String.valueOf(mJsonData.getDouble("TotalAmount")) != null && !String.valueOf(mJsonData.getDouble("TotalAmount")).isEmpty())
							{
								totalamounttxt.setVisibility(View.VISIBLE);
								totalamounttxt.setText(String.valueOf(mJsonData.getDouble("TotalAmount")));
							}
						}
						else
						{
							Toast.makeText(mContext, "No Data", Toast.LENGTH_SHORT).show();
//							layouttdata.setVisibility(View.GONE);
//							nodata.setVisibility(View.VISIBLE);
						}

//						refeshActivity();
					}
					else
					{
						Toast.makeText(mContext, "No Data", Toast.LENGTH_SHORT).show();
//						layouttdata.setVisibility(View.GONE);
//						nodata.setVisibility(View.VISIBLE);
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
//			layouttdata.setVisibility(View.GONE);
//			nodata.setVisibility(View.VISIBLE);
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

	@Override
	public void onClick(final View viewid)
	{
		if (viewid.getId() == R.id.userdetailsframe)
		{
			oreder_invoiceViewDetailsLayout.setVisibility(View.VISIBLE);
			expanded_frameLayout.setVisibility(View.VISIBLE);
		}
		else if (viewid.getId() == R.id.expanded_frameLayout)
		{
			oreder_invoiceViewDetailsLayout.setVisibility(View.GONE);
			expanded_frameLayout.setVisibility(View.GONE);
		}
		else
		{

		}

		/*viewHolder.userdetailsframe.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View view)
				{
					viewHolder.oreder_invoiceViewDetailsLayout.setVisibility(View.VISIBLE);
					viewHolder.expanded_frameLayout.setVisibility(View.VISIBLE);
				}
			});*/
	}
}

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
import android.widget.Spinner;
import android.widget.TextView;

import com.fmcg.Dotsoft.R;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.Common;
import com.fmcg.util.SharedPrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileActivity extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity profileact;
	public SharedPreferences sharedPreferences;
	public EditText usernameett, mobileet, addresset;
	public TextView submit_tv;
	Context mContext;
	TextView userName;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		profileact = ProfileActivity.this;

		mContext = ProfileActivity.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		userName = (TextView) findViewById(R.id.userName);
		usernameett = (EditText) findViewById(R.id.usernameett);
		mobileet = (EditText) findViewById(R.id.mobileet);
		addresset = (EditText) findViewById(R.id.addresset);
		submit_tv = (TextView) findViewById(R.id.submit);

		if (SharedPrefsUtil.getStringPreference(mContext, "EmployeeCode") != null && !SharedPrefsUtil.getStringPreference(mContext, "EmployeeCode").isEmpty())
		{
			userName.setText("User Code : " + SharedPrefsUtil.getStringPreference(mContext, "EmployeeCode") + "");
		}

		if (SharedPrefsUtil.getStringPreference(mContext, "EmployeeName") != null && !SharedPrefsUtil.getStringPreference(mContext, "EmployeeName").isEmpty())
		{
			usernameett.setText(SharedPrefsUtil.getStringPreference(mContext, "EmployeeName") + "");
		}

		if (SharedPrefsUtil.getStringPreference(mContext, "EmployeeDesignation") != null && !SharedPrefsUtil.getStringPreference(mContext, "EmployeeDesignation").isEmpty())
		{
			mobileet.setText(SharedPrefsUtil.getStringPreference(mContext, "EmployeeDesignation") + "");
		}

		//MobileNumber
		//LastName


		submit_tv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ((usernameett.getText().toString() != null) && (usernameett.getText().toString() != ""))
				{
					Common.showDialog(ProfileActivity.this);
					HttpAdapter.routeCreationSubmit(ProfileActivity.this, "RouteCreateSubmitData", usernameett.toString(), mobileet.toString());
				}
			}
		});
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
				if (response.getTag().equals("RouteCreateSubmitData"))
				{
					if (mJson.getString("Status").equals("OK"))
					{

						AlertDialogManager.showAlertOnly(this, "Successfully Route Created", mJson.getString("Message"), "Ok");
//						remarks_et.setText("");
						refeshActivity();

					}
					else
					{
						AlertDialogManager.showAlertOnly(this, "Failed to Create Route", mJson.getString("Message"), "Ok");
						refeshActivity();
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
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("mJson", mJson.toString());
			}
			catch (Exception e)
			{

			}

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



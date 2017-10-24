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
import android.widget.TextView;

import com.fmcg.Dotsoft.R;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.Common;
import com.fmcg.util.SharedPrefsUtil;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ZoneCreationActivity extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity remarks;
	public SharedPreferences sharedPreferences;
	public EditText zonenameet, zonecodeet;
	public TextView submit_tv;
	Context mContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone_creation_activity);
		remarks = ZoneCreationActivity.this;

		mContext = ZoneCreationActivity.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		zonenameet = (EditText) findViewById(R.id.zonenameet);
		zonecodeet = (EditText) findViewById(R.id.zonecodeet);
		submit_tv = (TextView) findViewById(R.id.submit);

		submit_tv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ((zonenameet.getText().toString() != null) && (zonenameet.getText().toString() != ""))
				{
					Common.showDialog(ZoneCreationActivity.this);
					HttpAdapter.zoneCreationSubmit(ZoneCreationActivity.this, "ZoneCreateSubmitData", zonenameet.toString(), zonecodeet.toString());
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
		Intent intent = new Intent(this, MasterCreationActivity.class);
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
				if (response.getTag().equals("ZoneCreateSubmitData"))
				{
					if (mJson.getString("Status").equals("OK"))
					{

						AlertDialogManager.showAlertOnly(this, "Successfully Zone Created", mJson.getString("Message"), "Ok");
//						remarks_et.setText("");
						refeshActivity();

					}
					else
					{
						AlertDialogManager.showAlertOnly(this, "Failed to Create Zone", mJson.getString("Message"), "Ok");
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



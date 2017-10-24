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

import org.json.JSONException;
import org.json.JSONObject;


public class AreaCreationActivity extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity routecreation;
	public SharedPreferences sharedPreferences;
	public EditText areashortet, areanameet,areadeset;
	public TextView submit_tv;
	Context mContext;
	Spinner route_name_spinner;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.area_creation_activity);
		routecreation = AreaCreationActivity.this;

		mContext = AreaCreationActivity.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		route_name_spinner = (Spinner) findViewById(R.id.route_name_spinner);
		areashortet = (EditText) findViewById(R.id.areashortet);
		areanameet = (EditText) findViewById(R.id.areanameet);
		areadeset = (EditText) findViewById(R.id.areadeset);
		submit_tv = (TextView) findViewById(R.id.submit);

		submit_tv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ((areashortet.getText().toString() != null) && (areashortet.getText().toString() != ""))
				{
					Common.showDialog(AreaCreationActivity.this);
					HttpAdapter.routeCreationSubmit(AreaCreationActivity.this, "RouteCreateSubmitData", areashortet.toString(), areanameet.toString());
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



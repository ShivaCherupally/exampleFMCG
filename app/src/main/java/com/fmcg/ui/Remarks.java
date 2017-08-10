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

import static android.app.PendingIntent.getActivity;


public class Remarks extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity remarks;
	public SharedPreferences sharedPreferences;
	public EditText remarks_et;
	public TextView submit_tv;
	Context mContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remarks);
		remarks = Remarks.this;

		mContext = Remarks.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		remarks_et = (EditText) findViewById(R.id.remarks_text);
		submit_tv = (TextView) findViewById(R.id.submit_tv);

		remarks_et.setHorizontallyScrolling(false);
		remarks_et.setMaxLines(Integer.MAX_VALUE);

		submit_tv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ((remarks_et.getText().toString() != null) && (remarks_et.getText().toString() != ""))
				{
					Common.showDialog(Remarks.this);
					/*String jsonString = new Gson().toJson(insertFeedback(SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"), remarks_et.getText().toString()));
					Log.d("jsonString", jsonString);*/
					String jsonString = createdummy();
					//HttpAdapter.insertFeedback(Remarks.this, "remarks", jsonString);
					HttpAdapter.updateInvoiceSubmit(Remarks.this, "remarks", jsonString);

				}
			}
		});
	}

	public static Map<String, String> insertFeedback(String EmployeeId, String FeedbackMessages)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("EmployeeId", EmployeeId);
		map.put("FeedbackMessages", FeedbackMessages);
		return map;
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

	private String createdummy()
	{

		RequestParams params = new RequestParams();
		params.put("ws_api_key", "10633A#_@D41134469_$BB63B84C_CCC602F"); //Mahendra.Aarakh
		params.put("user_mobile_email", "9848365548"); //password
		params.put("user_password", "123456"); //password

		/*JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("ws_api_key", "10633A#_@D41134469_$BB63B84C_CCC602F");
			dataObj.putOpt("user_mobile_email", "9848365548");
			dataObj.putOpt("user_password", "123456");
			//
			*//*dataObj.putOpt("email", "shiva@fb.com");
			dataObj.putOpt("password", "123456");*//*
//			dataObj.putOpt("ws_api_key", "!@#$%&OIUYTRE123456");
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Log.e("params", params.toString());
		return params.toString();
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
				if (response.getTag().equals("remarks"))
				{

					if (mJson.getString("Status").equals("OK"))
					{

						AlertDialogManager.showAlertOnly(this, "Feedback", mJson.getString("Message"), "Ok");
						remarks_et.setText("");
						refeshActivity();

					}
					else
					{
						refeshActivity();
						AlertDialogManager.showAlertOnly(this, "Feedback", mJson.getString("Message"), "Ok");
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



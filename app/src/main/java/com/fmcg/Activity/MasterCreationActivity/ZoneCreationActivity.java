package com.fmcg.Activity.MasterCreationActivity;

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


public class ZoneCreationActivity extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity remarks;
	public SharedPreferences sharedPreferences;
	public EditText zonenameet, zonecodeet, zonedeset;
	public TextView submit_tv;
	Context mContext;
	String zonenamestr, zonecodestr, zonedesstr;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone_creation_activity);
		remarks = ZoneCreationActivity.this;

		mContext = ZoneCreationActivity.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		zonenameet = (EditText) findViewById(R.id.zonenameet);
		zonecodeet = (EditText) findViewById(R.id.zonecodeet);
		zonedeset = (EditText) findViewById(R.id.zonedeset);
		submit_tv = (TextView) findViewById(R.id.submit);

		submit_tv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean Validated = validationFields();
				if (Validated)
				{
					if (Utility.isOnline(mContext))
					{
						new ZoneCreationActivity.ZoneCreationService().execute();
					}
					else
					{
						Toast.makeText(mContext, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private boolean validationFields()
	{
		boolean valid = true;
		zonenamestr = zonenameet.getText().toString();
		zonecodestr = zonecodeet.getText().toString();
		zonedesstr = zonedeset.getText().toString();

		if (zonenamestr.length() == 0)
		{
			valid = false;
			zonenameet.setError("Please Enter Zone Name");
			zonenameet.requestFocus();
			return valid;
		}
		else if (zonecodestr.length() == 0)
		{
			valid = false;
			zonecodeet.setError("Please Enter Zone Code");
			zonecodeet.requestFocus();
			return valid;
		}
		/*else if (zonedesstr.length() == 0)
		{
			valid = false;
			zonedeset.setError("Please Enter Zone ");
			zonedeset.requestFocus();
			return valid;
		}*/
		return valid;
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


	public class ZoneCreationService extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(ZoneCreationActivity.this);

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

			String result = serviceCall();
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
						Toast.makeText(ZoneCreationActivity.this, "Successfully Zone Created", Toast.LENGTH_SHORT).show();
						refeshActivity();
					}
					else
					{
						Toast.makeText(ZoneCreationActivity.this, "Data" + result.getString("Data"), Toast.LENGTH_SHORT).show();
						Toast.makeText(ZoneCreationActivity.this, "Failed to create Zone", Toast.LENGTH_SHORT).show();
						refeshActivity();
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


	public String serviceCall()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(HttpAdapter.ZONE_CREATION);
		try
		{
			String currentDate = DateUtil.currentDate();
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ZoneId", "0"));
			nameValuePairs.add(new BasicNameValuePair("ZoneCode", zonecodestr));
			nameValuePairs.add(new BasicNameValuePair("ZoneName", zonenamestr));
			nameValuePairs.add(new BasicNameValuePair("ZoneDescription", zonedesstr));
			nameValuePairs.add(new BasicNameValuePair("Active", "Y"));
			nameValuePairs.add(new BasicNameValuePair("CreatedOn", currentDate));
			nameValuePairs.add(new BasicNameValuePair("Createdby", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));

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
	}
}



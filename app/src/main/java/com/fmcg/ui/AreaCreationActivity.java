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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.models.ShopNamesData;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AreaCreationActivity extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity routecreation;
	public SharedPreferences sharedPreferences;
	public EditText areashortet, areanameet, areadeset;
	public TextView submit_tv;
	Context mContext;
	Spinner route_name_spinner;

	String areashortstr, areanamestr, areadesstr;

	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	String selected_routeId = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.area_creation_activity);
		routecreation = AreaCreationActivity.this;

		mContext = AreaCreationActivity.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		route_name_spinner = (Spinner) findViewById(R.id.route_name_spinner);
		areashortet = (EditText) findViewById(R.id.areashortet);
		areanameet = (EditText) findViewById(R.id.areanameet);
		areadeset = (EditText) findViewById(R.id.areadeset);
		submit_tv = (TextView) findViewById(R.id.submit);

		HttpAdapter.getRoute(AreaCreationActivity.this, "routeCode");

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
						new AreaCreationActivity.ZoneCreationService().execute();
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
		areashortstr = areashortet.getText().toString();
		areanamestr = areanameet.getText().toString();
		areadesstr = areadeset.getText().toString();
		if (areashortstr.length() == 0)
		{
			valid = false;
			areashortet.setError("Please Enter Area Short Name");
			areashortet.requestFocus();
			return valid;
		}
		else if (areanamestr.length() == 0)
		{
			valid = false;
			areanameet.setError("Please Enter Area Name");
			areanameet.requestFocus();
			return valid;
		}
		/*else if (routedesstr.length() == 0)
		{
			valid = false;
			routedeset.setError("Please Enter Zone ");
			routedeset.requestFocus();
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

				if (response.getTag().equals("routeCode"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
					}
				}
				//register
				/*if (response.getTag().equals("RouteCreateSubmitData"))
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
				}*/
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

	private void zoneSpinnerAdapter(JSONArray jsonArray)
	{
		try
		{
			_zoneNamesData.clear();
			zoneNamestitle.clear();
			_zoneNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("RouteId");
				String shopNamee = jsnobj.getString("RouteName");
				_zoneNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
//			zoneNamestitle.add("Zone Name");
			if (_zoneNamesData.size() > 0)
			{
				for (int i = 0; i < _zoneNamesData.size(); i++)
				{
					zoneNamestitle.add(_zoneNamesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zoneNamestitle);
		dataAdapter_zoneName.setDropDownViewResource(R.layout.list_item);
		route_name_spinner.setAdapter(dataAdapter_zoneName);
		route_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				selected_routeId = _zoneNamesData.get(position).getShopId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	public class ZoneCreationService extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(AreaCreationActivity.this);

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
						Toast.makeText(AreaCreationActivity.this, "Successfully Area Created", Toast.LENGTH_SHORT).show();
						refeshActivity();
					}
					else
					{
						Toast.makeText(AreaCreationActivity.this, "Data" + result.getString("Data"), Toast.LENGTH_SHORT).show();
						Toast.makeText(AreaCreationActivity.this, "Failed to create Area", Toast.LENGTH_SHORT).show();
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
		HttpPost httppost = new HttpPost(HttpAdapter.AREA_CREATION);
		try
		{
			String currentDate = DateUtil.currentDate();
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("AreaShortName", areashortstr));
			nameValuePairs.add(new BasicNameValuePair("AreaName", areanamestr));
			nameValuePairs.add(new BasicNameValuePair("AreaDescription", areadesstr));
			nameValuePairs.add(new BasicNameValuePair("RouteId", selected_routeId));
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



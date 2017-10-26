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


public class RouteCreationActivity extends AppCompatActivity implements NetworkOperationListener, AdapterView.OnItemSelectedListener
{
	public static Activity routecreation;
	public SharedPreferences sharedPreferences;
	public EditText routenoet, routenameet, routedeset;
	public TextView submit_tv;
	Context mContext;
	Spinner zone_name_spinner;
	String routenostr, routenamestr, routedesstr;
	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	String selected_zoneId = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_creation_activity);
		routecreation = RouteCreationActivity.this;

		mContext = RouteCreationActivity.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		zone_name_spinner = (Spinner) findViewById(R.id.zone_name_spinner);
		routenoet = (EditText) findViewById(R.id.routenoet);
		routenameet = (EditText) findViewById(R.id.routenameet);
		routedeset = (EditText) findViewById(R.id.routedeset);
		submit_tv = (TextView) findViewById(R.id.submit);

		HttpAdapter.getZoneDetailsDP(RouteCreationActivity.this, "zoneName");


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
						new RouteCreationActivity.ZoneCreationService().execute();
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
		routenostr = routenoet.getText().toString();
		routenamestr = routenameet.getText().toString();
		routedesstr = routedeset.getText().toString();

		if (routenostr.length() == 0)
		{
			valid = false;
			routenoet.setError("Please Enter Route No");
			routenoet.requestFocus();
			return valid;
		}
		else if (routenamestr.length() == 0)
		{
			valid = false;
			routenameet.setError("Please Enter Route Name");
			routenameet.requestFocus();
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
				if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
					}
				}
				/*else if (response.getTag().equals("RouteCreateSubmitData"))
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

	@Override
	public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id)
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
				String shopId = jsnobj.getString("ZoneId");
				String shopNamee = jsnobj.getString("ZoneName");
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
		zone_name_spinner.setAdapter(dataAdapter_zoneName);
		zone_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				selected_zoneId = _zoneNamesData.get(position).getShopId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	@Override
	public void onNothingSelected(final AdapterView<?> parent)
	{

	}

	public class ZoneCreationService extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(RouteCreationActivity.this);

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
						Toast.makeText(RouteCreationActivity.this, "Successfully Route Created", Toast.LENGTH_SHORT).show();
						refeshActivity();
					}
					else
					{
						Toast.makeText(RouteCreationActivity.this, "Data" + result.getString("Data"), Toast.LENGTH_SHORT).show();
						Toast.makeText(RouteCreationActivity.this, "Failed to create Route", Toast.LENGTH_SHORT).show();
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
		HttpPost httppost = new HttpPost(HttpAdapter.ROUTE_CREATION);
		try
		{
			String currentDate = DateUtil.currentDate();
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("RouteName", routenamestr));
			nameValuePairs.add(new BasicNameValuePair("RouteNumber", routenostr));
			nameValuePairs.add(new BasicNameValuePair("RouteDescription", routedesstr));
			nameValuePairs.add(new BasicNameValuePair("ZoneId", selected_zoneId));
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



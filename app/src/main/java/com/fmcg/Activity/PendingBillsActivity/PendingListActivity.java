package com.fmcg.Activity.PendingBillsActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Activity.HomeActivity.DashboardActivity;
import com.fmcg.Dotsoft.R;
import com.fmcg.models.ShopNamesData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.Util;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shiva on 6/16/2017.
 */

public class PendingListActivity extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	Spinner zoneDrpDwn, routeDrpdwn, areaDrpdwn, custmrDrpdwn;
	TextView pendingbillLable, shopNametxt, amounttxt, payamounttxt;
	LinearLayout detailsLayout, bottomHRLID;
	Button pay;
	
	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<ShopNamesData> _routeCodesData = new ArrayList<ShopNamesData>(); //Route Drop Down
	ArrayList<ShopNamesData> _areaNamesData = new ArrayList<ShopNamesData>(); //Area Drop down
	ArrayList<ShopNamesData> _customerNamesData = new ArrayList<ShopNamesData>(); //Customer Drop down

	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	ArrayList<String> routeNametitle = new ArrayList<String>();
	ArrayList<String> areaNamestitle = new ArrayList<String>();
	ArrayList<String> customertitle = new ArrayList<String>();

	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_customerId = "";

	Context mContext;
	ProgressDialog progressDailog;
	//public static Activity pendingListactvity;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pending_list_activity);

		//commonheaderandActivity();
		mContext = Util.commonHeaderandActivity(PendingListActivity.this);

		zoneDrpDwn = (Spinner) findViewById(R.id.zoneDrpDwn);
		routeDrpdwn = (Spinner) findViewById(R.id.routeDrpdwn);
		areaDrpdwn = (Spinner) findViewById(R.id.areaDrpdwn);
		custmrDrpdwn = (Spinner) findViewById(R.id.custmrDrpdwn);

		pendingbillLable = (TextView) findViewById(R.id.pendingbillLable);
		shopNametxt = (TextView) findViewById(R.id.shopNametxt);
		amounttxt = (TextView) findViewById(R.id.amounttxt);
		payamounttxt = (TextView) findViewById(R.id.payamounttxt);

		detailsLayout = (LinearLayout) findViewById(R.id.detailsLayout);
		detailsLayout.setVisibility(View.GONE);
		bottomHRLID = (LinearLayout) findViewById(R.id.bottomHRLID);
		bottomHRLID.setVisibility(View.GONE);
		pay = (Button) findViewById(R.id.pay);
		pay.setOnClickListener(this);



		defaultSelecteOptionForDropDowns();

		/*if (Utility.isOnline(mContext))
		{
			progressDailog = new ProgressDialog(mContext);
			progressDailog.setMessage("Please wait...");
			progressDailog.setIndeterminate(false);
			progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDailog.setCancelable(false);
			progressDailog.show();
			HttpAdapter.getZoneDetailsDP(PendingListActivity.this, "zoneNamesDropDown");
		}
		else
		{
			progressDailog.dismiss();
			Toast.makeText(PendingListActivity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
		}*/

	}

	private void commonheaderandActivity()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = PendingListActivity.this;
//		pendingListactvity = PendingListActivity.this;
	}

	private void defaultSelecteOptionForDropDowns()
	{
		selectZoneNameBind();
		selectRouteNameBind();
		selectAreaNameBind();
		selectCustomerNameBind();
	}


	@Override
	public void onClick(final View v)
	{
		switch (v.getId())
		{
			case R.id.pay:

				boolean validated = validationEntryData();
				if (validated)
				{
					if (Utility.isOnline(mContext))
					{
						new PaymentOption().execute();
					}
					else
					{
						Toast.makeText(mContext, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
					}

				}

				break;
		}

	}

	private void selectZoneNameBind()
	{
		zoneNamestitle.add("Select Zone Name");
		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zoneNamestitle);
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zoneDrpDwn.setAdapter(dataAdapter_zoneName);
	}

	private void selectRouteNameBind()
	{
		routeNametitle.add("Select Route No");
		ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNametitle);
		dataAdapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routeDrpdwn.setAdapter(dataAdapter_routeName);
	}

	private void selectAreaNameBind()
	{
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaDrpdwn.setAdapter(dataAdapter_areaName);
	}

	private void selectCustomerNameBind()
	{
		customertitle.add("Select Customer");
		ArrayAdapter<String> dataAdapter_customerName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, customertitle);
		dataAdapter_customerName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		custmrDrpdwn.setAdapter(dataAdapter_customerName);
	}

	@Override
	public void operationCompleted(final NetworkResponse response)
	{
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("response", mJson.toString());
				progressDailog.dismiss();
				//register
				if (response.getTag().equals("zoneNamesDropDown"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
					}
				}
				else if (response.getTag().equals("routeNosDropDown"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						routeNoSpinnerAdapter(jsonArray);
					}
				}
				else if (response.getTag().equals("areaNameDropDown"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						areaNameSpinnerAdapter(jsonArray);
					}

				}
				else if (response.getTag().equals("customerNameDropDown"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						customerNameSpinnerAdapter(jsonArray);
					}

				}
				else if (response.getTag().equals("pendingBillDetilas"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						pendingBillDetails(jsonArray);
					}

				}
				//


			}
			catch (Exception e)
			{
				progressDailog.dismiss();
				Log.e("error", e.toString());
			}
		}
		else
		{
			progressDailog.dismiss();
			Toast.makeText(mContext, "Failed to load", Toast.LENGTH_SHORT).show();
		}
	}


	//Zone Names
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
			zoneNamestitle.add("Select Zone Name");
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
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zoneDrpDwn.setAdapter(dataAdapter_zoneName);
		zoneDrpDwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					selected_zoneId = _zoneNamesData.get(position - 1).getShopId();
					HttpAdapter.getRouteDetails(PendingListActivity.this, "routeNosDropDown", selected_zoneId);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	//Route Nos
	private void routeNoSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_routeCodesData.clear();
			routeNametitle.clear();
			_routeCodesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("RouteId");
				String shopNamee = jsnobj.getString("RouteName");
				_routeCodesData.add(new ShopNamesData(shopId, shopNamee));
			}
			routeNametitle.add("Select Route No");
			if (_routeCodesData.size() > 0)
			{
				for (int i = 0; i < _routeCodesData.size(); i++)
				{
					routeNametitle.add(_routeCodesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//Routedetails adapter
		ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNametitle);
		dataAdapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routeDrpdwn.setAdapter(dataAdapter_routeName);

		routeDrpdwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					selected_roueId = _routeCodesData.get(position - 1).getShopId(); //3
					HttpAdapter.getAreaNamesByRouteId(PendingListActivity.this, "areaNameDropDown", selected_roueId);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	//Area Name
	private void areaNameSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_areaNamesData.clear();
			areaNamestitle.clear();
			_areaNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("AreaId");
				String shopNamee = jsnobj.getString("AreaName");
				_areaNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
			areaNamestitle.add("Select Area Name");
			if (_areaNamesData.size() > 0)
			{
				for (int i = 0; i < _areaNamesData.size(); i++)
				{
					areaNamestitle.add(_areaNamesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaDrpdwn.setAdapter(dataAdapter_areaName);
		areaDrpdwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					selected_areaNameId = _areaNamesData.get(position - 1).getShopId();
					HttpAdapter.getShopDetailsDP(PendingListActivity.this, "customerNameDropDown", "0");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}


	//custmrDrpdwn
	private void customerNameSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("shopDropdown", jsonArray.toString() + "");
		try
		{
			_customerNamesData.clear();
			customertitle.clear();
			_customerNamesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				/*String shopId = jsnobj.getString("ShopTypeId");
				String shopNamee = jsnobj.getString("ShopTypeName");*/
				String shopId = jsnobj.getString("ShopId");
				String shopNamee = jsnobj.getString("ShopName");
				//ShopName
				_customerNamesData.add(new ShopNamesData(shopId, shopNamee));
			}
			customertitle.add("Select Customer Name");
			if (_customerNamesData.size() > 0)
			{
				for (int i = 0; i < _customerNamesData.size(); i++)
				{
					customertitle.add(_customerNamesData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{

		}
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this,
		                                                                     android.R.layout.simple_spinner_item, customertitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		custmrDrpdwn.setAdapter(dataAdapter_shopType);
		custmrDrpdwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
				{
					if (position != 0)
					{
						progressDailog.show();
						selected_customerId = _customerNamesData.get(position - 1).getShopId();
						HttpAdapter.getPedingbillgDetailsByCustomerId(PendingListActivity.this, "pendingBillDetilas", selected_customerId);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}


	private void pendingBillDetails(final JSONArray jsonArray)
	{
		detailsLayout.setVisibility(View.VISIBLE);
		bottomHRLID.setVisibility(View.VISIBLE);
	}

	@Override
	public void showToast(final String string, final int lengthLong)
	{

	}

	private boolean validationEntryData()
	{
		boolean ret = true;
		Log.e("ZoneId", selected_zoneId);
		if (selected_zoneId == null || selected_zoneId.isEmpty() || selected_zoneId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Zone Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		if (selected_roueId == null || selected_roueId.isEmpty() || selected_roueId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Route Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (selected_areaNameId == null || selected_areaNameId.isEmpty() || selected_areaNameId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Area Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (selected_customerId == null || selected_customerId.isEmpty() || selected_customerId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Customer Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		return ret;
	}

	public class PaymentOption extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(mContext);

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
			if (success != null)
			{
				try
				{
					JSONObject result = new JSONObject(success);
					Log.e("response", result.toString() + "");
					if (result.getString("Status").equals("OK"))
					{
						//AlertDialogManager.showAlertOnly(UpdateShopDetailsActvity.this, "BrightUdyog", "Shop Created Successfully" /*result.getString("Message")*/, "Ok");
//						if (result.getString("Message").equalsIgnoreCase(""))
						Toast.makeText(PendingListActivity.this, "Payment Successfully Done", Toast.LENGTH_SHORT).show();
						refreshActivity();
					}
					else
					{
						//Toast.makeText(UpdateShopDetailsActvity.this, "Shop Creation Failed", Toast.LENGTH_SHORT).show();
						Toast.makeText(PendingListActivity.this, "UnSuccessfully Paid", Toast.LENGTH_SHORT).show();
						refreshActivity();
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

	private void refreshActivity()
	{
		Intent i = getIntent();
		finish();
		startActivity(i);
	}


	public String serviceCall()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost("http://202.143.96.20/Orderstest/api/Services/updateShopDetails");
		HttpPost httppost = new HttpPost(HttpAdapter.PAY_DONE);
		try
		{
			String updateDate = "";
			Calendar c = Calendar.getInstance();
			SimpleDateFormat simDf = new SimpleDateFormat("dd-MM-yyyy");
			try
			{
				updateDate = simDf.format(c.getTime());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			Log.e("Ordering Date", updateDate + "");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ZoneId", selected_zoneId));
			nameValuePairs.add(new BasicNameValuePair("RouteId", selected_roueId));
			nameValuePairs.add(new BasicNameValuePair("AreaId", selected_areaNameId));
			nameValuePairs.add(new BasicNameValuePair("shopId", selected_customerId));


			/*nameValuePairs.add(new BasicNameValuePair("ShopName", shopname.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("ShopDescription", "Good"));
			nameValuePairs.add(new BasicNameValuePair("OwnerName", ownername.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Address", shop_address.getText().toString()));
			//nameValuePairs.add(new BasicNameValuePair("Pincode", pin.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Latitude", lat.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("Longitude", lang.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("PhoneNumber", phone.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("MobileNumber", mobile.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("GPSL", GPSL));
			nameValuePairs.add(new BasicNameValuePair("LocationName", locationName.getText().toString()));


			nameValuePairs.add(new BasicNameValuePair("ShopTypeId", shopTypeDropdown));
			nameValuePairs.add(new BasicNameValuePair("ReligionID", religionDropdown));
			nameValuePairs.add(new BasicNameValuePair("PaymentTermId", paymentDropDown));

			nameValuePairs.add(new BasicNameValuePair("Active", "Y"));
			nameValuePairs.add(new BasicNameValuePair("DateTime", updateDate));
			nameValuePairs.add(new BasicNameValuePair("Createdby", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
			nameValuePairs.add(new BasicNameValuePair("EmailID", emailId.getText().toString()));*/
			Log.d("params", nameValuePairs.toString());
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
}

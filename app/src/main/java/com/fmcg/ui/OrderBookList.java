package com.fmcg.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.adapter.OrderOrInvoiceListAdapter;
import com.fmcg.adapter.RouteCheckListAdapter;
import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.OrderBookOrInvoiceListData;
import com.fmcg.models.RemainderData;
import com.fmcg.models.RouteDetailsData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

import static com.fmcg.util.Common.orderNUm;


/**
 * Created by Shiva on 6/9/2017.
 */

public class OrderBookList extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	public static Activity order_or_invoiceActivity;
	RecyclerView mRecyclerView;
	TextView nodata;
	TextView swipeLefttxt;
	LinearLayout swipeleftlabellayout;
	RemainderDataBase remainderDb;
	RemainderData _RemainderData = null;
	RecyclerView.Adapter mAdapter;
	Button addRemainder;
	Context mContext;
	ArrayList<OrderBookOrInvoiceListData> _orderBookOrInvoiceListData = new ArrayList<OrderBookOrInvoiceListData>();
	int deletedOrderPosition;

	ProgressDialog progressdailog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_or_invoice_list_activity);
		order_or_invoiceActivity = OrderBookList.this;
		mContext = OrderBookList.this;

		progressdailog = new ProgressDialog(OrderBookList.this);
		progressdailog.setMessage("Please wait...");
		progressdailog.setIndeterminate(false);
		progressdailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdailog.setCancelable(false);


		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		swipeLefttxt = (TextView) findViewById(R.id.swipeLefttxt);
		swipeleftlabellayout = (LinearLayout) findViewById(R.id.swipeleftlabellayout);

		nodata = (TextView) findViewById(R.id.nodata);
		mRecyclerView = (RecyclerView) findViewById(R.id.orderorinvoiceRecyclerView);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		mRecyclerView.setHasFixedSize(true);

		String ACCESS_LIST = SharedPrefsUtil.getStringPreference(mContext, "ACCESS_LIST");
		if (ACCESS_LIST != null && !ACCESS_LIST.equalsIgnoreCase("null") && !ACCESS_LIST.isEmpty())
		{
			if (ACCESS_LIST.equalsIgnoreCase("BOOK_LIST"))
			{
				HttpAdapter.getOrderBookList(OrderBookList.this, "Order_Book_List");
				order_or_invoiceActivity.setTitle("Book List");
				swipeLefttxt.setText("Swipe left to delete a order book list item");
			}
			else
			{
				swipeLefttxt.setText("Swipe left to delete a invoice list item");
				swipeleftlabellayout.setVisibility(View.GONE);
				HttpAdapter.getInvoiceList(OrderBookList.this, "Invoice_List");
				order_or_invoiceActivity.setTitle("Invoice List");
			}
		}


	}

	@Override
	public void onClick(final View v)
	{


	}

	@Override
	public void operationCompleted(final NetworkResponse response)
	{
		Common.disMissDialog();
		Log.e("responseCode", response.getStatusCode() + "");
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("response", mJson.toString());

				if (response.getTag().equals("Order_Book_List"))
				{
					if (mJson.getString("Status").equals("OK"))
					{
						if (mJson.getString("Data").equals("null"))
						{

							swipeleftlabellayout.setVisibility(View.GONE);
							Toast.makeText(mContext, "Data " + "null", Toast.LENGTH_SHORT).show();
							nodata.setVisibility(View.VISIBLE);
						}
						else
						{
							try
							{
								nodata.setVisibility(View.GONE);
								JSONArray jsonArray = mJson.getJSONArray("Data");
								orderBookListData(jsonArray);
							}
							catch (Exception e)
							{
								Log.e("error", e + "");
							}
						}

					}
				}
				else if (response.getTag().equals("Invoice_List"))
				{
					if (mJson.getString("Status").equals("OK"))
					{
						if (mJson.getString("Data").equals("null"))
						{
							nodata.setVisibility(View.VISIBLE);
							swipeleftlabellayout.setVisibility(View.GONE);
							Toast.makeText(mContext, "Data " + "null", Toast.LENGTH_SHORT).show();
							nodata.setVisibility(View.VISIBLE);
						}
						else
						{
							try
							{
								nodata.setVisibility(View.GONE);
								JSONArray jsonArray = mJson.getJSONArray("Data");
								invoiceListData(jsonArray);
//								orderBookandInvoiceListData(jsonArray);
							}
							catch (Exception e)
							{
								Log.e("error", e + "");
							}
						}
					}
				}
				else if (response.getTag().equals("Order_Delete"))
				{
					if (mJson.getString("Status").equals("OK"))
					{
						try
						{
							progressdailog.dismiss();
							Toast.makeText(mContext, "Your Order Successfully Deleted", Toast.LENGTH_SHORT).show();
							_orderBookOrInvoiceListData.remove(deletedOrderPosition);
							mAdapter.notifyDataSetChanged(); //update adapter
						}
						catch (Exception e)
						{
							Log.e("error", e + "");
						}
					}
					else
					{
						progressdailog.dismiss();
						Toast.makeText(mContext, "Order Failed to Delete", Toast.LENGTH_SHORT).show();
					}
				} //
				else if (response.getTag().equals("Order_Edit"))
				{
					if (mJson.getString("Status").equals("OK"))
					{
						try
						{
							progressdailog.dismiss();
							JSONObject jsonObj = mJson.getJSONObject("Data");
							String editOrderJsonDataToString = jsonObj.toString();
							SharedPrefsUtil.setStringPreference(mContext, "EDIT_ORDER_DATA_STRING", editOrderJsonDataToString);
							Toast.makeText(mContext, "Successfully Order Editing", Toast.LENGTH_SHORT).show();
							Intent i = new Intent(OrderBookList.this, UpdateOrderActvity.class);
							startActivity(i);
							/*_orderBookOrInvoiceListData.remove(deletedOrderPosition);
							mAdapter.notifyDataSetChanged(); //update adapter*/
						}
						catch (Exception e)
						{
							progressdailog.dismiss();
							Log.e("error", e + "");
						}
					}
					else
					{
						progressdailog.dismiss();
						Toast.makeText(mContext, "Failed to Edit Order", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(mContext, "Server to failed.. " + response.getStatusCode(), Toast.LENGTH_SHORT).show();
			if (progressdailog != null)
			{
				progressdailog.dismiss();
			}
		}
	}

	private void invoiceListData(final JSONArray jsonArray)
	{
		_orderBookOrInvoiceListData = new ArrayList<OrderBookOrInvoiceListData>();
		try
		{
			if (jsonArray.length() != 0)
			{
				for (int i = 0; i < jsonArray.length(); i++)
				{
					String OrderInvoiceNumber = "";
					String InvoiceDate = "";
					String Status_invoice = "";
					String Customer = "";
					int PaidAmount = 0;
					int Balance = 0;
					double DueAmount = 0.0;
					double TotalAmount = 0.0;

					JSONObject jObj = jsonArray.getJSONObject(i);

					//int OrdersId = jObj.getInt("OrdersId");

					if (jObj.getString("OrderInvoiceNumber") != null && !jObj.getString("OrderInvoiceNumber").equalsIgnoreCase("null"))
					{
						OrderInvoiceNumber = jObj.getString("OrderInvoiceNumber");
					}

					if (jObj.getString("InvoiceDate") != null && !jObj.getString("InvoiceDate").equalsIgnoreCase("null"))
					{
						InvoiceDate = jObj.getString("InvoiceDate");
					}
					if (jObj.getString("Customer") != null && !jObj.getString("Customer").equalsIgnoreCase("null"))
					{
						Customer = jObj.getString("Customer");
					}
					if (jObj.getString("Status") != null && !jObj.getString("Status").equalsIgnoreCase("null"))
					{
						Status_invoice = jObj.getString("Status");
					}
					if (jObj.getInt("PaidAmount") != 0)
					{
						PaidAmount = jObj.getInt("PaidAmount");
					}
					if (jObj.getInt("DueAmount") != 0)
					{
						DueAmount = jObj.getDouble("DueAmount");
					}
					Balance = jObj.getInt("Balance");

					TotalAmount = jObj.getDouble("TotalAmount");

					_orderBookOrInvoiceListData
							.add(new OrderBookOrInvoiceListData(0, 0, 0, OrderInvoiceNumber, InvoiceDate, Customer, Status_invoice, PaidAmount, Balance, DueAmount,
							                                    TotalAmount));
				}
				adapterAssigning(_orderBookOrInvoiceListData);
			}
			else
			{
				nodata.setVisibility(View.VISIBLE);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}

	private void orderBookListData(final JSONArray jsonArray)
	{
		_orderBookOrInvoiceListData = new ArrayList<OrderBookOrInvoiceListData>();
		try
		{
			if (jsonArray.length() != 0)
			{
				for (int i = 0; i < jsonArray.length(); i++)
				{
					String OrderNumber = "";
					String OrderDate = "";
					String ShopName = "";
					String Status = "";
					int NoOfProducts = 0;
					int SubTotalAmount = 0;
					double TaxAmount = 0.0;
					double TotalAmount = 0.0;

					JSONObject jObj = jsonArray.getJSONObject(i);

					int EmployeeId = jObj.getInt("EmployeeId");
					int RouteId = jObj.getInt("RouteId");


					int OrdersId = jObj.getInt("OrdersId");

					if (jObj.getString("OrderNumber") != null && !jObj.getString("OrderNumber").equalsIgnoreCase("null"))
					{
						OrderNumber = jObj.getString("OrderNumber");
					}

					if (jObj.getString("OrderDate") != null && !jObj.getString("OrderDate").equalsIgnoreCase("null"))
					{
						OrderDate = jObj.getString("OrderDate");
					}
					if (jObj.getString("ShopName") != null && !jObj.getString("ShopName").equalsIgnoreCase("null"))
					{
						ShopName = jObj.getString("ShopName");
					}
					if (jObj.getString("Status") != null && !jObj.getString("Status").equalsIgnoreCase("null"))
					{
						Status = jObj.getString("Status");
					}
					if (jObj.getInt("NoOfProducts") != 0)
					{
						NoOfProducts = jObj.getInt("NoOfProducts");
					}
					if (jObj.getInt("SubTotalAmount") != 0)
					{
						SubTotalAmount = jObj.getInt("SubTotalAmount");
					}
					/*if (jObj.getDouble("OrderNumber") != 0.0 && !jObj.getString("OrderNumber").equalsIgnoreCase("null"))
					{*/
					TaxAmount = jObj.getDouble("TaxAmount");
					//}
					if (jObj.getDouble("TotalAmount") != 0.0)
					{
						TotalAmount = jObj.getDouble("TotalAmount");
					}
					_orderBookOrInvoiceListData
							.add(new OrderBookOrInvoiceListData(OrdersId, EmployeeId, RouteId, OrderNumber, OrderDate, ShopName, Status, NoOfProducts, SubTotalAmount, TaxAmount,
							                                    TotalAmount));
				}
				adapterAssigning(_orderBookOrInvoiceListData);
			}
			else
			{
				nodata.setVisibility(View.VISIBLE);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}

	private void adapterAssigning(final ArrayList<OrderBookOrInvoiceListData> orderBookOrInvoiceListData)
	{
		if (orderBookOrInvoiceListData.size() != 0)
		{
			mAdapter = new OrderOrInvoiceListAdapter(OrderBookList.this, orderBookOrInvoiceListData);
			mRecyclerView.setAdapter(mAdapter);
		}
		else
		{
			nodata.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showToast(final String string, final int lengthLong)
	{

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
		Intent intent = new Intent(this, ViewListActivity.class);
		startActivity(intent);
		finish();
	}

	public void updateAdapter(final int positionValue)
	{
		try
		{
			if (Utility.isOnline(mContext))
			{
				//	progressdailog.show();

				deletedOrderPosition = positionValue;
				new DeleteOrder().execute();

				/*Log.e("orderno", _orderBookOrInvoiceListData.get(positionValue).getOrderNumber() + "Deleting");
				Log.e("orderId", _orderBookOrInvoiceListData.get(positionValue).getOrderId()+ "Deleting");
				deletedOrderPosition = positionValue;
				String jsonParametres = deleteOrderJsonRequest(String.valueOf(_orderBookOrInvoiceListData.get(positionValue).getOrderId()), SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
				HttpAdapter.orderDelete(OrderBookList.this, "Order_Delete",jsonParametres);*/
			}
			else
			{
				Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			progressdailog.dismiss();
			e.printStackTrace();
		}


	}

	private String deleteOrderJsonRequest(String OrderId, String UserId)
	{
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("OrderId", OrderId);
			dataObj.putOpt("UserId", UserId);
			//http://202.143.96.20/Orderstest/api/Services/DeleteOrders?OrderId=6&UserId=4
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("orderjson", dataObj.toString());
		return dataObj.toString();
	}

	public void editUpdateAdapter(final int positionValue)
	{
		try
		{

			if (Utility.isOnline(mContext))
			{
				progressdailog.show();
				Log.e("orderno", _orderBookOrInvoiceListData.get(positionValue).getOrderNumber() + "Editing");
				deletedOrderPosition = positionValue;
				HttpAdapter.orderEdit(OrderBookList.this, "Order_Edit", String.valueOf(_orderBookOrInvoiceListData.get(positionValue).getOrderId()));
			}
			else
			{
				Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	public class DeleteOrder extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(OrderBookList.this);

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
			String result = "";
			try
			{
				result = serviceCallInvoiceCancelOrder();
				Log.e("loginResult", result);
			}
			catch (Exception e)
			{
				e.printStackTrace();

			}

			return result;
		}

		@Override
		protected void onPostExecute(String success)
		{
			Log.e("successData", String.valueOf(success));
			if (success != null && !success.equals("null"))
			{
				pd.dismiss();
				try
				{
					JSONObject mJson = new JSONObject(success);
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						progressdailog.dismiss();
						Toast.makeText(mContext, "Your Order Successfully Deleted", Toast.LENGTH_SHORT).show();
						_orderBookOrInvoiceListData.remove(deletedOrderPosition);
						mAdapter.notifyDataSetChanged(); //update adapter
					}
					else
					{

						Toast.makeText(mContext, "Order Failed to Delete", Toast.LENGTH_SHORT).show();
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				pd.dismiss();
			}


		}
	}

	public String serviceCallInvoiceCancelOrder()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				HttpAdapter.DELETE_ORDER + "?OrderId=" + String
						.valueOf(_orderBookOrInvoiceListData.get(deletedOrderPosition).getOrderId() + "&UserID=" + SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("OrderId", orderNUm));
			nameValuePairs.add(new BasicNameValuePair("UserId", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
			Log.d("Invoice", "" + nameValuePairs.toString());
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
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

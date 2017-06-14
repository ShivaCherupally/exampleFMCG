package com.fmcg.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Shiva on 6/9/2017.
 */

public class OrderBookList extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	public static Activity order_or_invoiceActivity;
	RecyclerView mRecyclerView;
	TextView nodata;
	RemainderDataBase remainderDb;
	RemainderData _RemainderData = null;
	RecyclerView.Adapter mAdapter;
	Button addRemainder;
	Context mContext;
	ArrayList<OrderBookOrInvoiceListData> _orderBookOrInvoiceListData = new ArrayList<OrderBookOrInvoiceListData>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_or_invoice_list_activity);
		order_or_invoiceActivity = OrderBookList.this;
		mContext = OrderBookList.this;

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);


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
			}
			else
			{
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
		Log.d("outPutResponse", String.valueOf(response));
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("responsebookInvoicelist", mJson.toString());

				if (response.getTag().equals("Order_Book_List"))
				{
					if (mJson.getString("Status").equals("OK"))
					{
						if (mJson.getString("Data").equals("null"))
						{
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
			}
			catch (JSONException e)
			{
				e.printStackTrace();
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
							.add(new OrderBookOrInvoiceListData(0, 0, OrderInvoiceNumber, InvoiceDate, Customer, Status_invoice, PaidAmount, Balance, DueAmount,
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
							.add(new OrderBookOrInvoiceListData(EmployeeId, RouteId, OrderNumber, OrderDate, ShopName, Status, NoOfProducts, SubTotalAmount, TaxAmount,
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
			mAdapter = new OrderOrInvoiceListAdapter(orderBookOrInvoiceListData);
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
}

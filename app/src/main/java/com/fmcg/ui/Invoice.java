package com.fmcg.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.GetAreaDetails;
import com.fmcg.models.GetOrderNumberDP;
import com.fmcg.models.GetOrderSummary;
import com.fmcg.models.GetProductCategory;
import com.fmcg.models.GetRouteDetails;
import com.fmcg.models.GetRouteDropDown;
import com.fmcg.models.GetShopDetailsDP;
import com.fmcg.models.GetZoneDetails;
import com.fmcg.models.OrderStatusDropdown;
import com.fmcg.models.PaymentDropDown;
import com.fmcg.models.ShopNamesData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.fmcg.util.Utility;
import com.google.gson.Gson;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fmcg.util.Common.orderNUm;


public class Invoice extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	public static Activity invoiceActivity;
	public SharedPreferences sharedPreferences;
	Dialog alertDialog;
	public List<PaymentDropDown> paymentDP;
	public List<GetShopDetailsDP> shopsDP;
	public List<OrderStatusDropdown> orderstatusDP;
	public List<GetProductCategory> productDP;
	public List<GetOrderNumberDP> orderNumberDP;
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetAreaDetails> areaDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	public List<GetProductCategory> list;
	public List<GetOrderSummary> orderSummary;
	private List<GetRouteDropDown> routeDp;

	private List<String> paymentDP_str;
	private List<String> shopNameDP_str;
	private List<String> orderNumberDP_str;
	private List<String> orderstatusDP_str;
	private List<String> zoneDetailsDP_str;
	private List<String> areaDetailsDP_str;
	private List<String> routeDetailsDP_str;
	private List<String> productDP_str;
	private List<String> routeDp_str;

	public String paymentDropDown, shopNameDropDown, orderStatusDropDown, productNameDropDown, orderNumberDropDown,
			zoneNameDropdown, areaNameDropDown, routeNameDropDown, routeCode;


	public Spinner shopName_sp, orderStatus_sp, category_sp, payment_sp, orderNumber_sp, zone_sp, routeName_sp, areaName_sp, routecd;
	public CheckBox isShopClosed, ordered, invoice;
	public TextView uploadImage, shopClosed, orderDate, submit, remarksTV, tvDisplayDate, totalAmt, invoiceNum, cancel;
	private EditText remarksET, paidAmt;
	private LinearLayout list_li;
	private DatePicker dpResult;
	private int year;
	private int month;
	private int day;
	private TableLayout tableLayout;

	String ZoneId = "";
	String RouteId = "";
	String AreaId = "";
	String ShopId = "";
	String OrderStatusId = "";
	String productCategoryId = "";
	String paymentTermsId = "";
	String OrderDeliveryDate = "";
	Context mContext;

	String TotalAmount = "";
	boolean orderCancel = false;
	String invoiceOrderno = "";
	String paidAmount = "";

	private Map<String, String> mParams;
	public ProgressDialog mPDialog;
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>();
	ArrayList<String> shopNamestitle = new ArrayList<String>();
	String selected_shopId = "";

	ArrayList<ShopNamesData> _orderNumberData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<String> orderNosTitle = new ArrayList<String>();
	String selected_orderId = "";

	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	Button alert_submit;
	boolean check1 = false;
	boolean check2 = false;

	public List<GetOrderSummary> storedProductCategories = new ArrayList<GetOrderSummary>();
	int j;
	public final List<GetOrderSummary> list_of_orders = new ArrayList<GetOrderSummary>();

	//Payment Selection
	EditText creditdays;
	LinearLayout creditDaysLayout;
	DatePicker dateselect;
	Button dateaccept;
	private static TextView paymentSelectedvalue;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_invoice);
		mContext = Invoice.this;
		invoiceActivity = Invoice.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		//initializing the variables
		tableLayout = (TableLayout) findViewById(R.id.tableRow1);

		orderStatus_sp = (Spinner) findViewById(R.id.order_type_dp);
		category_sp = (Spinner) findViewById(R.id.product_category);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_name);
		shopName_sp = (Spinner) findViewById(R.id.shopname_spinner);
		orderNumber_sp = (Spinner) findViewById(R.id.order_number);
		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);
		routecd = (Spinner) findViewById(R.id.routecd);
		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);

		isShopClosed = (CheckBox) findViewById(R.id.isClosed);
		ordered = (CheckBox) findViewById(R.id.isOrder);
		invoice = (CheckBox) findViewById(R.id.isInvoice);
		remarksET = (EditText) findViewById(R.id.Remarks_et);
		paidAmt = (EditText) findViewById(R.id.paidAmount);
		uploadImage = (TextView) findViewById(R.id.upLoadImage);
		shopClosed = (TextView) findViewById(R.id.shopClosed);
		totalAmt = (TextView) findViewById(R.id.totalAmount);
		invoiceNum = (TextView) findViewById(R.id.invoice_number);
		submit = (TextView) findViewById(R.id.submit);

		paymentSelectedvalue = (TextView) findViewById(R.id.paymentSelectedvalue);
		paymentSelectedvalue.setVisibility(View.GONE);

		cancel = (TextView) findViewById(R.id.cancel);
		list_li = (LinearLayout) findViewById(R.id.items_li);

		HttpAdapter.getPayment(Invoice.this, "payment");
		HttpAdapter.getOrderStatus(Invoice.this, "orderStatus");
		HttpAdapter.getProductCategoryDP(Invoice.this, "productCategoryName");
		HttpAdapter.getZoneDetailsDP(Invoice.this, "zoneName");
		HttpAdapter.getRoute(Invoice.this, "routeCode");
		HttpAdapter.GetInvoiceNumber(Invoice.this, "GetInvoiceNumber");


		paymentDP = new ArrayList<>();
		shopsDP = new ArrayList<>();
		orderstatusDP = new ArrayList<>();
		productDP = new ArrayList<>();
		orderNumberDP = new ArrayList<>();
		zoneDetailsDP = new ArrayList<>();
		areaDetailsDP = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();
		list = new ArrayList<>();
		orderSummary = new ArrayList<>();
		routeDp = new ArrayList<>();

		paymentDP_str = new ArrayList<>();
		shopNameDP_str = new ArrayList<>();
		orderstatusDP_str = new ArrayList<>();
		orderNumberDP_str = new ArrayList<>();
		productDP_str = new ArrayList<>();
		zoneDetailsDP_str = new ArrayList<>();
		areaDetailsDP_str = new ArrayList<>();
		routeDetailsDP_str = new ArrayList<>();
		routeDp_str = new ArrayList<>();

		invoiceNum.setOnClickListener(this);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}


	public String serviceCallInvoiceCancelOrder()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		/*HttpPost httppost = new HttpPost(
				"http://202.143.96.20/Orderstest/api/Services/CancelOrderNumber?ShopId="
						+ selected_shopId + "&OrderNumber=" + orderNUm);*/
		HttpPost httppost = new HttpPost(
				HttpAdapter.CANCEL_ORDER_NUMBER + "?ShopId="
						+ selected_shopId + "&OrderNumber=" + orderNUm + "&UserID=" + SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("ShopId", selected_shopId));
			nameValuePairs.add(new BasicNameValuePair("OrderNumber", orderNUm));
			nameValuePairs.add(new BasicNameValuePair("UserID", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId")));
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


	public String serviceCallTotalAmountGet()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://202.143.96.20/Orderstest/api/Services/GetOrderNumberAmount?OrderNumber="
						+ orderNUm);
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("OrderNumber", orderNUm));
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


	public class InvoiceCancel extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(Invoice.this);

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub

			pd.setMessage("Please wait...");
			pd.setIndeterminate(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			//pd.show();
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
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Order Cancelled.", Toast.LENGTH_SHORT).show();
						//com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Successfully Uploaded..", "OK");
						Intent i = new Intent(Invoice.this, Invoice.class);
						startActivity(i);
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Order Cancelled.", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(Invoice.this, Invoice.class);
						startActivity(i);
						//	com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Failed Uploaded", "OK");
						/*Intent i = new Intent(Order.this, Order.class);
						startActivity(i);*/
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}


		}
	}

	public class TotalAmountGet extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(Invoice.this);

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub

			pd.setMessage("Please wait...");
			pd.setIndeterminate(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			//pd.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			// TODO: attempt authentication against a network service.
			String result = "";
			try
			{
				result = serviceCallTotalAmountGet();
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
					Log.e("response", mJson.toString());
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");

						Double total = 0.0;
						JSONArray jsonArray = mJson.getJSONArray("Data");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							Log.e("TotalAmount", obj.toString() + "SS");
							totalAmt.setText("Total Amount: " + String.format("%.2f", Double.valueOf(obj.toString())) + "");
							/*GetOrderSummary getOrderSummary = new Gson().fromJson(obj.toString(), GetOrderSummary.class);
							total = total + Double.parseDouble(getOrderSummary.TotalAmount);*/
						}

						/*Toast.makeText(mContext, "Successfully Order Cancelled.", Toast.LENGTH_SHORT).show();
						//com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Successfully Uploaded..", "OK");
						Intent i = new Intent(Invoice.this, Invoice.class);
						startActivity(i);*/
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Order Cancelled.", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(Invoice.this, Invoice.class);
						startActivity(i);
						//	com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Failed Uploaded", "OK");
						/*Intent i = new Intent(Order.this, Order.class);
						startActivity(i);*/
					}

				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}


		}
	}


	@Override
	public void onClick(View v)
	{

		switch (v.getId())
		{
			case R.id.submit:
				String paymentSelected = "";
				String CreditDays = "";
				String chequeDate = "";
				orderCancel = false;
				boolean validated = validationEntryData();
				if (validated)
				{
					if (Utility.isOnline(mContext))
					{
						boolean valid = validationEntryData();
						if (valid)
						{

							TotalAmount = String.format("%.2f", Double.valueOf(TotalAmount));

							/*String jsonString = createJsonInvoiceSubmit(invoiceNum.getText().toString(), ZoneId, RouteId, AreaId,
							                                            selected_shopId, orderNUm, "Y", "",
							                                            SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"), TotalAmount, paidAmt.getText().toString());*/

							try
							{
								paymentSelected = SharedPrefsUtil.getStringPreference(mContext, "paymentSelected");
								if (paymentSelected != null && !paymentSelected.isEmpty() && !paymentSelected.equalsIgnoreCase("null"))
								{

									if (paymentSelected.equalsIgnoreCase("Credit-days"))
									{
										CreditDays = paymentSelectedvalue.getText().toString();
									}
									else if (paymentSelected.equalsIgnoreCase("Days to Cheque"))
									{
										CreditDays = paymentSelectedvalue.getText().toString();
									}
									else if (paymentSelected.equalsIgnoreCase("Cheque"))
									{
										chequeDate = paymentSelectedvalue.getText().toString();
									}
								}
								else
								{
									paymentSelected = "";
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();

							}


							String jsonString = createJsonInvoiceSubmit(invoiceNum.getText().toString(), ZoneId, RouteId, AreaId,
							                                            selected_shopId, orderNUm, "Y", "",
							                                            SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"), TotalAmount, paidAmt.getText().toString(),
							                                            paymentTermsId, chequeDate, CreditDays);
							Log.e("id", selected_shopId + "" + "");
							Log.e("parameters", jsonString + "");
							HttpAdapter.invoiceSubmit(this, "invoiceSubmit", jsonString);
						}
						else
						{
							Toast.makeText(mContext, "No internet Connection", Toast.LENGTH_SHORT).show();
						}
					}
				}
				break;

			case R.id.cancel:
				if (Utility.isOnline(mContext))
				{
					orderCancel = true;
					boolean validation = validationEntryData();
					if (validation)
					{
						/*String jsonString = createJsonInvoiceCancel(orderNUm, selected_shopId);
						Log.e("parameters", jsonString + "");*/
						//HttpAdapter.invoiceCancel(this, "invoiceCancel", jsonString);
						new InvoiceCancel().execute();
					}
				}
				else
				{
					Toast.makeText(mContext, "No internet Connection", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void displayTableView(final List<GetOrderSummary> productDP)
	{
		tableLayout.removeAllViews();
		headers();

		storedProductCategories.clear();
		storedProductCategories.addAll(productDP);

		for (int i = 0; i < productDP.size(); i++)
		{
			j = i;
			Invoice.OrderSummary row = new Invoice.OrderSummary(this, productDP.get(i), i);
			tableLayout.addView(row, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	public void operationCompleted(NetworkResponse response)
	{
		Common.disMissDialog();
		Log.d("outPutResponse", String.valueOf(response));
		if (response.getStatusCode() == 200)
		{
			try
			{

				JSONObject mJson = new JSONObject(response.getResponseString());
				Log.e("response", mJson.toString());
				//Payment Terms Name Dropdown
				if (response.getTag().equals("payment"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						paymentDP_str.add("Payment Terms Name");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							PaymentDropDown paymentDropDown = new Gson().fromJson(obj.toString(), PaymentDropDown.class);
							paymentDP.add(paymentDropDown);
							paymentDP_str.add(paymentDropDown.PaymentName);
						}
						//Payment adapter
						ArrayAdapter<String> dataAdapter_payment = new ArrayAdapter<String>(this,
						                                                                    android.R.layout.simple_spinner_item, paymentDP_str);
						dataAdapter_payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						payment_sp.setAdapter(dataAdapter_payment);
						payment_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								paymentTermsId = String.valueOf(position);
								if (position != 0)
								{
									paymentDropDown = paymentDP.get(position - 1).PaymentTermsId;
									Log.e("paymentId", paymentDropDown);
//									Log.e("paymentname", paymentDP.get(position - 1).PaymentName);
									String paymentSelected = paymentDP.get(position - 1).PaymentName;
									SharedPrefsUtil.setStringPreference(mContext, "paymentSelected", paymentSelected);
									Log.e("paymentSelected", paymentSelected);
									if (paymentSelected != null && !paymentSelected.isEmpty() && !paymentSelected.equalsIgnoreCase("null"))
									{
										if (paymentSelected.equalsIgnoreCase("Credit-days"))
										{
											dailogBoxforPaymentSelection("Credit-days");
										}
										else if (paymentSelected.equalsIgnoreCase("Days to Cheque"))
										{
											dailogBoxforPaymentSelection("Days to Cheque");
										}
										else if (paymentSelected.equalsIgnoreCase("Cheque"))
										{
											dailogBoxforPaymentSelection("Cheque");
										}
									}
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});
					}

				}
				//Shop Name DropDown
				else if (response.getTag().equals("shopName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						shopsDP.clear();
						shopNameDP_str.clear();
						shopNameDP_str.add("Shop Name");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetShopDetailsDP getShopsArrayDropdown = new Gson().fromJson(obj.toString(), GetShopDetailsDP.class);
							shopsDP.add(getShopsArrayDropdown);
							shopNameDP_str.add(getShopsArrayDropdown.ShopName);
						}


						//////////Shop Names and ShopId
						_shopNamesData.clear();
						shopNamestitle.clear();
						_shopNamesData = new ArrayList<ShopNamesData>();
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject jsnobj = jsonArray.getJSONObject(i);
							String shopId = jsnobj.getString("ShopId");
							String shopNamee = jsnobj.getString("ShopName");
							_shopNamesData.add(new ShopNamesData(shopId, shopNamee));
						}
						shopNamestitle.add("Shop Name");

						if (_shopNamesData.size() > 0)
						{

							for (int i = 0; i < _shopNamesData.size(); i++)
							{
								shopNamestitle.add(_shopNamesData.get(i).getShopName());
							}
						}

						//shopname adapter
						ArrayAdapter<String> dataAdapter_shopName = new ArrayAdapter<String>(this,
						                                                                     android.R.layout.simple_spinner_item, shopNamestitle);
						dataAdapter_shopName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						shopName_sp.setAdapter(dataAdapter_shopName);
//						shopName_sp.setAdapter(new ArrayAdapter<String>(Invoice.this, android.R.layout.simple_spinner_item, shopNamestitle));
						shopName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								try
								{

									if (position != 0)
									{

//										ShopId = String.valueOf(position);
										position = position - 1;
										selected_shopId = _shopNamesData.get(position).getShopId();
										//shopNameDropDown = shopsDP.get(position - 1).ShopId;
										ShopId = selected_shopId;
										Log.e("selected_shopId", selected_shopId);
										HttpAdapter.getOrderNumberDp(Invoice.this, "orderNumber", selected_shopId);
									}
								}
								catch (Exception e)
								{

								}

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});
					}
				}
				// OrderStatus DropDown
				else if (response.getTag().equals("orderStatus"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						orderstatusDP.clear();
						shopNameDP_str.clear();
						orderstatusDP_str.add("Order Status");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							OrderStatusDropdown orderStatusDropdown = new Gson().fromJson(obj.toString(), OrderStatusDropdown.class);
							orderstatusDP.add(orderStatusDropdown);
							orderstatusDP_str.add(orderStatusDropdown.OrderStatusDescription);
						}
						//OrderStatus adapter
						ArrayAdapter<String> dataAdapter_orderStatus = new ArrayAdapter<String>(this,
						                                                                        android.R.layout.simple_spinner_item, orderstatusDP_str);
						dataAdapter_orderStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						orderStatus_sp.setAdapter(dataAdapter_orderStatus);
						orderStatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								OrderStatusId = String.valueOf(position);
								if (position != 0)
								{
									orderStatusDropDown = orderstatusDP.get(position - 1).OrderStatusDescription;
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});
					}

				}
				// orderNumber DropDown
				else if (response.getTag().equals("orderNumber"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						//orderNumbersSpinnerAdapter(jsonArray);
						orderNumberDP.clear();
						orderNumberDP_str.clear();
						orderNumberDP_str.add("Order Number");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetOrderNumberDP orderStatusDropdown = new Gson().fromJson(obj.toString(), GetOrderNumberDP.class);
							orderNumberDP.add(orderStatusDropdown);
							orderNumberDP_str.add(orderStatusDropdown.OrderNumber);
						}
						//orderNumber adapter
						ArrayAdapter<String> dataAdapter_orderNumber = new ArrayAdapter<String>(this,
						                                                                        android.R.layout.simple_spinner_item, orderNumberDP_str);
						dataAdapter_orderNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						orderNumber_sp.setAdapter(dataAdapter_orderNumber);
						orderNumber_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{

								if (position != 0)
								{
									orderNumberDropDown = orderNumberDP.get(position - 1).OrderNumber;
									orderNUm = orderNumberDropDown;
									HttpAdapter.getOrderSummary(Invoice.this, "orderSummary", orderNumberDropDown);
									HttpAdapter.getOrderTotal(Invoice.this, "totalAmount", orderNUm);

								/*	if (Utility.isOnline(mContext))
									{
										new TotalAmountGet().execute();
									}
									else
									{
										Toast.makeText(mContext, "No internet Connection", Toast.LENGTH_SHORT).show();
									}*/
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});
					}

				}
				//ZoneDetails DropDown
				else if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneDetailsDP.clear();
						zoneDetailsDP_str.clear();
						zoneDetailsDP_str.add("Zone Name");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetZoneDetails getZoneDetails = new Gson().fromJson(obj.toString(), GetZoneDetails.class);
							zoneDetailsDP.add(getZoneDetails);
							zoneDetailsDP_str.add(getZoneDetails.ZoneName);

						}

						//zoneDetails adapter
						ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this,
						                                                                     android.R.layout.simple_spinner_item, zoneDetailsDP_str);
						dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						zone_sp.setAdapter(dataAdapter_zoneName);

						zone_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								ZoneId = String.valueOf(position);
								if (position != 0)
								{
									zoneNameDropdown = zoneDetailsDP.get(position - 1).ZoneId;
									HttpAdapter.getRouteDetails(Invoice.this, "routeName", zoneNameDropdown);
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});

					}

				}
				//AreaDetails DropDown
				else if (response.getTag().equals("areaNameDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						areaDetailsDP.clear();
						areaDetailsDP_str.clear();
						areaDetailsDP_str.add("Area Name");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetAreaDetails getAreaDetails = new Gson().fromJson(obj.toString(), GetAreaDetails.class);
							areaDetailsDP.add(getAreaDetails);
							areaDetailsDP_str.add(getAreaDetails.AreaName);

						}

						//AreaDetails adapter
						ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this,
						                                                                     android.R.layout.simple_spinner_item, areaDetailsDP_str);
						dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						areaName_sp.setAdapter(dataAdapter_areaName);

						areaName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								AreaId = String.valueOf(position);
								if (position != 0)
								{
									areaNameDropDown = areaDetailsDP.get(position - 1).AreaId;
									HttpAdapter.getShopDetailsDP(Invoice.this, "shopName", areaNameDropDown);
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});

					}

				}
				//RouteDetails DropDown
				else if (response.getTag().equals("routeName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						routeDetailsDP.clear();
						routeDetailsDP_str.clear();
						routeDetailsDP_str.add("Route Name");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetRouteDetails getRoute = new Gson().fromJson(obj.toString(), GetRouteDetails.class);
							routeDetailsDP.add(getRoute);
							routeDetailsDP_str.add(getRoute.RouteName);
						}

						//Routedetails adapter
						ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this,
						                                                                      android.R.layout.simple_spinner_item, routeDetailsDP_str);
						dataAdapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						routeName_sp.setAdapter(dataAdapter_routeName);

						routeName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								RouteId = String.valueOf(position);
								if (position != 0)
								{
									routeNameDropDown = routeDetailsDP.get(position - 1).RouteId;
									HttpAdapter.getAreaDetailsByRoute(Invoice.this, "areaNameDP", routeNameDropDown);

								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});

					}

				}
				else if (response.getTag().equals("orderSummary"))
				{
					Double total = 0.0;
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						orderSummary.clear();
						for (int i = 0; i < jsonArray.length(); i++)
						{
							/*JSONObject obj = jsonArray.getJSONObject(i);
							GetOrderSummary getOrderSummary = new Gson().fromJson(obj.toString(), GetOrderSummary.class);
							total = total + Double.parseDouble(getOrderSummary.TotalAmount);
							orderSummary.add(getOrderSummary);*/

							JSONObject obj = jsonArray.getJSONObject(i);
//							Log.e("categorydata", obj.toString());
							GetOrderSummary getOrderSummary = new Gson().fromJson(obj.toString(), GetOrderSummary.class);
							list_of_orders.add(getOrderSummary);
						}
						displayTableView(list_of_orders);
					}
				}

				else if (response.getTag().equals("totalAmount"))
				{
					try
					{
						Double total = 0.0;
						if (mJson.getString("Message").equals("SuccessFull"))
						{
							JSONArray jsonArray = mJson.getJSONArray("Data");
							for (int i = 0; i < jsonArray.length(); i++)
							{
								TotalAmount = jsonArray.getString(i);
								Log.e("totalAmount", TotalAmount);
							}
							totalAmt.setText("Total Amount: " + String.format("%.2f", Double.valueOf(TotalAmount)));
						}
					}
					catch (Exception e)
					{

					}

				}

				else if (response.getTag().equals("GetInvoiceNumber"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						invoiceNum.setText(mJson.getString("Data"));
					}
				}
				else if (response.getTag().equals("invoiceSubmit"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("Invoiceresponse", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
						//com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Successfully Uploaded..", "OK");
						/*Intent i = new Intent(Invoice.this, Invoice.class);
						startActivity(i);*/
						dailogBoxAfterSubmit();
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Uploaded.", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
						//	com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Failed Uploaded", "OK");
						/*Intent i = new Intent(Order.this, Order.class);
						startActivity(i);*/
					}

				}
				else
				{

				}
//				else if (response.getTag().equals("invoiceCancel"))
//				{
//					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
//					{
//						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
//						Toast.makeText(mContext, "Order Cancelled Successfully.", Toast.LENGTH_SHORT).show();
//						//com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Successfully Uploaded..", "OK");
//						Intent i = new Intent(Invoice.this, Order.class);
//						startActivity(i);
//					}
//					else
//					{
//						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
//						Toast.makeText(mContext, "UnSuccessfully Order Cancelled.", Toast.LENGTH_SHORT).show();
//						//com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Successfully Uploaded..", "OK");
//						Intent i = new Intent(Invoice.this, Order.class);
//						startActivity(i);
//					}
//
//				}

			/*	else if (response.getTag().equals("invoiceCancel"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
						com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Successfully Uploaded..", "OK");
						Intent i = new Intent(Invoice.this, Order.class);
						startActivity(i);
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						com.fmcg.util.AlertDialogManager.showAlertOnly(this, "BrightUdyog", "Failed Uploaded", "OK");
						*//*Intent i = new Intent(Order.this, Order.class);
						startActivity(i);*//*
					}
					//invoiceCancel

				}*/

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void orderNumbersSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_orderNumberData.clear();
			orderNosTitle.clear();
			_orderNumberData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("ZoneId");
				String shopNamee = jsnobj.getString("ZoneName");
				_orderNumberData.add(new ShopNamesData(shopId, shopNamee));
			}
			orderNosTitle.add("Zone Name");
			if (_orderNumberData.size() > 0)
			{
				for (int i = 0; i < _orderNumberData.size(); i++)
				{
					orderNosTitle.add(_orderNumberData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, orderNosTitle);
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zone_sp.setAdapter(dataAdapter_zoneName);
		zone_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					zoneNameDropdown = _orderNumberData.get(position - 1).getShopId();
					HttpAdapter.getRouteDetails(Invoice.this, "routeCode", zoneNameDropdown);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	@Override
	public void showToast(String string, int lengthLong)
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
		Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
		finish();
	}

	/*private void headers()
	{
		android.widget.TableRow row = new android.widget.TableRow(this);

		TextView taskdate = new TextView(Invoice.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Prod");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(Invoice.this);
		title.setText("Prc");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                               android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(Invoice.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(Invoice.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(Invoice.this);
		description.setText("TaxAmt");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                     android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(Invoice.this);
		description2.setText("STot");
		description2.setBackgroundColor(getResources().getColor(R.color.light_green));
		description2.setTextSize(15);
		description2.setPadding(10, 10, 10, 10);
		row.addView(description2);
		description2.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      TableRow.LayoutParams.WRAP_CONTENT));


		tableLayout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

	}*/
	private void headers()
	{
		android.widget.TableRow row = new TableRow(this);

		TextView taskdate = new TextView(Invoice.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Product");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(Invoice.this);
		title.setText("Price");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(Invoice.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(Invoice.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(Invoice.this);
		description.setText("VAT");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                      TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(Invoice.this);
		description2.setText("SubTotal");
		description2.setBackgroundColor(getResources().getColor(R.color.light_green));
		description2.setTextSize(15);
		description2.setPadding(10, 10, 10, 10);
		row.addView(description2);
		description2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));


		tableLayout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

	}

	private String createJsonInvoiceSubmit(String OrderNumber, String ZoneId, String RouteId,
	                                       String AreaId, String ShopId, String orderNo, String inVoice,
	                                       String Remarks, String EmployeeId,
	                                       String totalAmountStr, String paidAmountstr
			, String paymentTermsId, String chequeDate, String creditDays
	)
	{
		JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("OrderInvoiceNumber", OrderNumber);
			dataObj.putOpt("ZoneId", ZoneId);
			dataObj.putOpt("RouteId", RouteId);
			dataObj.putOpt("AreaId", AreaId);
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("OrderNumber", orderNo);
			dataObj.putOpt("IsInvoice", inVoice);
			dataObj.putOpt("Remarks", Remarks);
			dataObj.putOpt("EmployeeId", EmployeeId);
			dataObj.putOpt("TotalAmount", totalAmountStr);
			dataObj.putOpt("PaidAmount", paidAmountstr);

			dataObj.putOpt("PaymentTermsId", paymentTermsId);
			if (chequeDate != null && !chequeDate.isEmpty())
			{
				dataObj.putOpt("ChequeDate", chequeDate);
			}
			else
			{
				dataObj.putOpt("ChequeDate", null);
			}

			if (creditDays != null && !creditDays.isEmpty())
			{
				dataObj.putOpt("CreditDays", creditDays);
			}
			else
			{
				dataObj.putOpt("CreditDays", null);
			}

			studentsObj.put("InVoiceSubmitData", dataObj);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("params", studentsObj.toString());
		return studentsObj.toString();
	}

	private String createJsonInvoiceCancel(String OrderNumber, String ShopId
	)
	{
		JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();

		try
		{
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("OrderNumber", OrderNumber);
			studentsObj.put("UpdateTripStartEndDetails", dataObj);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("orderjson", studentsObj.toString());
		return studentsObj.toString();
	}

	private boolean validationEntryData()
	{
		boolean ret = true;
		Log.e("ZoneId", ZoneId);
		if (ZoneId == null || ZoneId.isEmpty() || ZoneId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Zone Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		if (RouteId == null || RouteId.isEmpty() || RouteId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Route Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (AreaId == null || AreaId.isEmpty() || AreaId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Area Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (ShopId == null || ShopId.isEmpty() || ShopId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Shop Name", Toast.LENGTH_SHORT).show();
			ret = false;

			return ret;
		}
		if (orderCancel)
		{
			//return ret;
		}
		else
		{
			if (OrderStatusId == null || OrderStatusId.isEmpty() || OrderStatusId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Order Status", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}

			if (paymentTermsId == null || paymentTermsId.isEmpty() || paymentTermsId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Payment Terms Name", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}
			if (paymentTermsId == null || paymentTermsId.isEmpty() || paymentTermsId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Payment Terms Name", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}

//			if (TotalAmount.equals(paidAmt.getText().toString()))
//			{
////				Toast.makeText(getApplicationContext(), "Please Check Paid Amount", Toast.LENGTH_SHORT).show();
////				ret = false;
////				return ret;
//			}

			String paidAmount = paidAmt.getText().toString();
			if (!paidAmount.equalsIgnoreCase(null) && !paidAmount.isEmpty())
			{
				double totalValue = Double.parseDouble(TotalAmount);
				double paidValue = Double.parseDouble(paidAmount);
				if (totalValue >= paidValue)
				{
					Toast.makeText(getApplicationContext(), "Paid Amount " + paidValue, Toast.LENGTH_SHORT).show();
					/*Toast.makeText(getApplicationContext(), "Please Check Paid Amount", Toast.LENGTH_SHORT).show();
					ret = false;
					return ret;*/
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Check Paid Amount", Toast.LENGTH_SHORT).show();
					ret = false;
					return ret;
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Please Enter Paid Amount", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}
		}

		invoiceOrderno = invoiceNum.getText().toString();
		paidAmount = paidAmt.getText().toString();


		return ret;
	}

	private void dailogBoxAfterSubmit()
	{
		promoDialog = new Dialog(this);
		promoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		promoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		promoDialog.setCancelable(false);
		promoDialog.setContentView(R.layout.dailog_for_acceptance);
		close_popup = (ImageView) promoDialog.findViewById(R.id.close_popup);
		alert_submit = (Button) promoDialog.findViewById(R.id.alert_submit);
		select_option_radio_grp = (RadioGroup) promoDialog.findViewById(R.id.select_option_radio_grp);

		promoDialog.show();

		select_option_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(final RadioGroup radioGroup, final int i)
			{
				switch (i)
				{
					case R.id.orderBook:
						check1 = true;
						break;
					case R.id.inovice:
						check2 = true;
						break;


				}
			}


		});

		close_popup.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (promoDialog != null)
				{
					promoDialog.dismiss();
					Util.hideSoftKeyboard(mContext, v);
					refreshActivity();
				}
			}
		});

		alert_submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (check1)
				{

					Intent in = new Intent(Invoice.this, Order.class);
					Util.killInvoice();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(Invoice.this, Invoice.class);
					Util.killInvoice();
					startActivity(inten);
				}
				else
				{
					Toast.makeText(mContext, "Please Select Order Book or Invoice", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void refreshActivity()
	{
		Intent i = getIntent();
		finish();
		startActivity(i);
	}

	private class OrderSummary extends TableRow
	{

		private Context mContext;
		private GetOrderSummary mProductCategory;

		private EditText quantityETID;
		private EditText fresETID;

		private String afterTextChanged = "";
		private String beforeTextChanged = "";
		private String onTextChanged = "";

		private final int position;

		public OrderSummary(final Context context, final GetOrderSummary productCategory, int index)
		{
			super(context);
			mContext = context;
			mProductCategory = productCategory;
			position = index;
			init();
		}

		public GetOrderSummary getProductCategory()
		{
			// update your new data
			if (!TextUtils.isEmpty(quantityETID.getText()))
			{
				mProductCategory.Quantity = Integer.parseInt(quantityETID.getText().toString());
				mProductCategory.Frees = Integer.parseInt(fresETID.getText().toString());
			}
			return mProductCategory;
		}

		public String getAfterTextChanged()
		{
			return afterTextChanged;
		}

		public String getBeforeTextChanged()
		{
			return beforeTextChanged;
		}

		public String getOnTextChanged()
		{
			return onTextChanged;
		}

		private void init()
		{
			try
			{
				TextView taskdate = new TextView(mContext);
				taskdate.setTextSize(15);
				taskdate.setText(mProductCategory.ProductName);
				taskdate.setPadding(0, 0, 0, 10);
				taskdate.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.36f));
				addView(taskdate);

				TextView title = new TextView(mContext);
				title.setText(String.valueOf(mProductCategory.ProductPrice));
				title.setTextSize(15);
				title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                       LayoutParams.WRAP_CONTENT));
				addView(title);

				quantityETID = new EditText(mContext);
				quantityETID.setText(String.valueOf(mProductCategory.Quantity));
				quantityETID.setBackgroundColor(Color.TRANSPARENT);
				quantityETID.setClickable(false);
				quantityETID.setCursorVisible(true);
				quantityETID.setFocusableInTouchMode(true);
				quantityETID.setEnabled(false);
				quantityETID.setTextSize(15);
				quantityETID.setTextColor(Color.BLACK);
				quantityETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				quantityETID.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				quantityETID.addTextChangedListener(mTextWatcher);
				addView(quantityETID);

			/*TextView description3 = new TextView(mContext);
			description3.setText("-");
			description3.setTextSize(15);
			addView(description3);
			description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
			                                                       TableRow.LayoutParams.WRAP_CONTENT));*/
				fresETID = new EditText(mContext);
				/*if (mProductCategory.Frees != null && !mProductCategory.Frees.isEmpty())
				{*/
				fresETID.setText(String.valueOf(mProductCategory.Frees));
//				}
				/*else
				{
					fresETID.setText("-");
				}*/
				fresETID.setBackgroundColor(Color.TRANSPARENT);
				fresETID.setClickable(true);
				fresETID.setCursorVisible(true);
				fresETID.setFocusableInTouchMode(true);
				fresETID.setTextSize(15);
				fresETID.setEnabled(false);
				fresETID.setTextColor(Color.BLACK);
				fresETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				fresETID.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                          LayoutParams.WRAP_CONTENT));
				fresETID.addTextChangedListener(mTextWatcherFres);
				addView(fresETID);


				TextView description = new TextView(mContext);
				description.setText(String.valueOf(mProductCategory.VAT));
				description.setTextSize(15);
				//description.setPadding(15,0,0,0);
				description.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                             LayoutParams.WRAP_CONTENT));
				addView(description);

				TextView description2 = new TextView(mContext);
				description2.setText(String.valueOf(mProductCategory.SubTotalAmount));
				description2.setTextSize(15);
//				description2.setVisibility(GONE);
				description2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				addView(description2);

				/*ImageView deleteimg = new ImageView(mContext);
				deleteimg.setImageResource(R.drawable.delete);
				deleteimg.setVisibility(View.GONE);
				deleteimg.setMaxWidth(28);
				deleteimg.setMaxHeight(28);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					deleteimg.setForegroundGravity(Gravity.CENTER_VERTICAL);
				}
				deleteimg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				addView(deleteimg);
				deleteimg.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						try
						{
							int temposition = position + 1;
							TableRow row = (TableRow) tableLayout.getChildAt(temposition);
							tableLayout.removeView(row);
							productDP.remove(position - 1);
							storedProductCategories.remove(position - 1);
							list.remove(position - 1);


						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});*/

			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.e("", e + "");
			}
		}

		private TextWatcher mTextWatcher = new TextWatcher()
		{
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
			{
				beforeTextChanged = quantityETID.getText().toString();
			}

			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
			{
				onTextChanged = quantityETID.getText().toString();
			}

			@Override
			public void afterTextChanged(final Editable s)
			{
				afterTextChanged = s.toString();
				storedProductCategories.get(position).setQuantity(Integer.valueOf(s.toString()));
			}
		};

		private TextWatcher mTextWatcherFres = new TextWatcher()
		{
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
			{
				beforeTextChanged = "";
				beforeTextChanged = fresETID.getText().toString();
			}

			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
			{
				onTextChanged = "";
				onTextChanged = fresETID.getText().toString();
			}

			@Override
			public void afterTextChanged(final Editable s)
			{
				afterTextChanged = "";
				afterTextChanged = s.toString();
				try
				{
					if (!afterTextChanged.isEmpty() && !afterTextChanged.equalsIgnoreCase(null))
					{
						int fresValue = Integer.parseInt(afterTextChanged);
						int quantityValue = storedProductCategories.get(position).getQuantity();
						if (quantityValue > fresValue)
						{
							storedProductCategories.get(position).setFrees(Integer.parseInt(s.toString()));
						}
						else
						{
							fresETID.setText(storedProductCategories.get(position).getFrees());
							Toast.makeText(mContext, "Frees Must be not Equal or Less than to the Quantity", Toast.LENGTH_SHORT).show();
						}
					}
				}
				catch (Exception e)
				{
					Log.e("error", e + "");
				}


			}
		};
	}

	private void dailogBoxforPaymentSelection(final String type)
	{
		promoDialog = new Dialog(this);
		promoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		promoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		promoDialog.setCancelable(false);
		promoDialog.setContentView(R.layout.pop_up_dailog_for_payment_selection);
		close_popup = (ImageView) promoDialog.findViewById(R.id.close_popup);
		alert_submit = (Button) promoDialog.findViewById(R.id.alert_submit);
		creditdays = (EditText) promoDialog.findViewById(R.id.creditdays);
		creditDaysLayout = (LinearLayout) promoDialog.findViewById(R.id.creditDaysLayout);

		dateselect = (DatePicker) promoDialog.findViewById(R.id.dateselect);
		dateaccept = (Button) promoDialog.findViewById(R.id.dateaccept);

		if (type.equals("Days to Cheque"))
		{
			daysAccess();
		}
		else if (type.equals("Cheque"))
		{
			DialogFragment newFragment = new Invoice.DatePickerFragmentDailog();
			newFragment.show(getSupportFragmentManager(), "datePicker");
		}
		else if (type.equals("Credit-days"))
		{
			daysAccess();
		}


		close_popup.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (promoDialog != null)
				{
					promoDialog.dismiss();
					Util.hideSoftKeyboard(mContext, v);
					//paymentTermsId = "";
					//payment_sp.setSelection(0);
//					refreshActivity();
				}
			}
		});

		alert_submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				String daysCredits = creditdays.getText().toString();
				if (daysCredits != null && !daysCredits.isEmpty())
				{
					paymentSelectedvalue.setText(daysCredits);
					paymentSelectedvalue.setVisibility(View.VISIBLE);
				}
				promoDialog.dismiss();
				Util.hideSoftKeyboard(mContext, v);
			}
		});


//		datePicker.


	}

	private void daysAccess()
	{
		try
		{
			promoDialog.show();
			paymentSelectedvalue.setText("");
			creditDaysLayout.setVisibility(View.VISIBLE);
			dateselect.setVisibility(View.GONE);
			dateaccept.setVisibility(View.GONE);
		}
		catch (Exception e)
		{

		}

	}

	public static class DatePickerFragmentDailog extends DialogFragment
			implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
//			dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
			dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
			return dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			Date date = new Date(year, month, day);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM");
			String sectiondate = simpleDateFormat.format(date.getTime()) + "-" + year;//simDf.format(c.getTime());
			paymentSelectedvalue.setText("");
			paymentSelectedvalue.setVisibility(View.VISIBLE);
			paymentSelectedvalue.setText(sectiondate);

			SharedPrefsUtil.setStringPreference(getContext(), "SelectedDate", sectiondate);
			// Do something with the date chosen by the user

		}


	}
}

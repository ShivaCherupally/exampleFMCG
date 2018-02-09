package com.fmcg.Activity.UpdateOrderAndBilling;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.fmcg.Activity.HomeActivity.DashboardActivity;
import com.fmcg.Activity.OrderAndBillingActivity.Invoice;
import com.fmcg.Activity.OrderAndBillingActivity.OrderBookingActivity;
import com.fmcg.Activity.PendingBillsActivity.PendingBillsActivity;
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
import java.util.List;

import static com.fmcg.util.Common.orderNUm;

/**
 * Created by Shiva on 9/24/2017.
 */

public class BillingEditActivity extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener, AdapterView.OnItemSelectedListener, View.OnTouchListener
{
	public static Activity invoiceActivity;
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

	private List<String> orderNumberDP_str;

	public String orderNumberDropDown;


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
	Context mContext;
	String TotalAmount = "";
	Double TotalAmountdouble;
	boolean orderCancel = false;
	String invoiceOrderno = "";
	String paidAmount = "";

	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

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

	//////////All Spinner model classes
	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>(); //Shop Names Newly added
	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<ShopNamesData> _routeCodesData = new ArrayList<ShopNamesData>(); //Route Drop Down
	ArrayList<ShopNamesData> _areaNamesData = new ArrayList<ShopNamesData>(); //Area Drop down
	ArrayList<ShopNamesData> _shoptypesData = new ArrayList<ShopNamesData>(); //Shop Type Drop Down
	ArrayList<ShopNamesData> _orderStatusData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _paymentsSelectData = new ArrayList<ShopNamesData>(); //Select Payment


	ArrayList<String> shooNamestitle = new ArrayList<String>(); // Shop Name Title
	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	ArrayList<String> routeNamestitle = new ArrayList<String>();
	ArrayList<String> areaNamestitle = new ArrayList<String>();
	ArrayList<String> shoptypesNamestitle = new ArrayList<String>();
	ArrayList<String> orderStatusTitle = new ArrayList<String>();
	ArrayList<String> productCatogeryTitle = new ArrayList<String>();
	ArrayList<String> paymentNamestitle = new ArrayList<String>();

	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_ShopId = "";
	String selected_orderStatusId = "";
	String selected_paymentTermsId = "";
	String SPINNER_SELECTION = "";

	AutoCompleteTextView shopName_autoComplete;
	String AvailShopName = "";

	boolean zoneTouchClick = false;
	boolean routeTouchClick = false;
	boolean areaTouchClick = false;
	boolean shopNamesTouchClick = false;
	boolean orderStatusTouchClick = false;
	boolean paymentTermsTouchClick = false;

	EditText availzonenametxt, availroutenoetxt;

	Button pendingBtn;
	String AvailableTotalAmount = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_invoice);
		mContext = BillingEditActivity.this;
		invoiceActivity = BillingEditActivity.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		tableLayout = (TableLayout) findViewById(R.id.tableRow1);
		tableLayout.setVisibility(View.GONE);

		category_sp = (Spinner) findViewById(R.id.product_category);
		orderNumber_sp = (Spinner) findViewById(R.id.order_number);
		pendingBtn = (Button) findViewById(R.id.pendingBtn);
		pendingBtn.setOnClickListener(this);


		/*zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);*/

		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		zone_sp.setVisibility(View.GONE);
		zone_sp.setEnabled(false);
		zone_sp.setClickable(false);
//		zone_sp.setBackgroundColor(Color.TRANSPARENT);
		routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);
		routeName_sp.setVisibility(View.GONE);
//		routeName_sp.setBackgroundColor(Color.TRANSPARENT);
		routeName_sp.setEnabled(false);
		routeName_sp.setClickable(false);

		availzonenametxt = (EditText) findViewById(R.id.availzonenametxt);
		availzonenametxt.setVisibility(View.VISIBLE);
		availroutenoetxt = (EditText) findViewById(R.id.availroutenoetxt);
		availroutenoetxt.setVisibility(View.VISIBLE);

		String availablezonename = SharedPrefsUtil.getStringPreference(mContext, "SELECTED_ZONENAME");
		if (availablezonename != null && !availablezonename.isEmpty())
		{
			availzonenametxt.setText(availablezonename);
		}

		String availableroutename = SharedPrefsUtil.getStringPreference(mContext, "SELECTED_ROUTENAME");
		if (availableroutename != null && !availableroutename.isEmpty())
		{
			availroutenoetxt.setText(availableroutename);
		}


		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);
		//shopName_sp = (Spinner) findViewById(R.id.shopname_spinner);
		shopName_autoComplete = (AutoCompleteTextView) findViewById(R.id.shopName_autoComplete);
		orderStatus_sp = (Spinner) findViewById(R.id.order_status_spinner);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_spinner);


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

		HttpAdapter.getPayment(BillingEditActivity.this, "payment");
		HttpAdapter.getOrderStatus(BillingEditActivity.this, "orderStatus");
		HttpAdapter.getProductCategoryDP(BillingEditActivity.this, "productCategoryName");
		HttpAdapter.getZoneDetailsDP(BillingEditActivity.this, "zoneName");
		HttpAdapter.getRoute(BillingEditActivity.this, "routeCode");
		HttpAdapter.GetInvoiceNumber(BillingEditActivity.this, "GetInvoiceNumber");


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

		orderNumberDP_str = new ArrayList<>();

		invoiceNum.setOnClickListener(this);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);

		selectRouteNameBind();
		selectAreaNameBind();
		selectOrderNumber();


		zone_sp.setOnItemSelectedListener(this);
		routeName_sp.setOnItemSelectedListener(this);
		areaName_sp.setOnItemSelectedListener(this);
//		shopName_sp.setOnItemSelectedListener(this);
		orderStatus_sp.setOnItemSelectedListener(this);
		payment_sp.setOnItemSelectedListener(this);
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
						+ selected_ShopId + "&OrderNumber=" + orderNUm + "&UserID=" + SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("ShopId", selected_ShopId));
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

	@Override
	public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id)
	{
		String selectedSpinner = "";
		switch (parent.getId())
		{
			case R.id.zone_name_spinner:
				if (zoneTouchClick)
				{
					AvailShopName = "";
					selectedSpinner = "ZONE";
					dropDownValueSelection(position, _zoneNamesData, selectedSpinner);
				}
				break;
			case R.id.routeName_spinner:
				if (routeTouchClick)
				{
					AvailShopName = "";
					selectedSpinner = "ROUTE";
					dropDownValueSelection(position, _routeCodesData, selectedSpinner);
				}
				break;
			case R.id.areaName_spinner:
				if (areaTouchClick)
				{
					AvailShopName = "";
					selectedSpinner = "AREA";
					dropDownValueSelection(position, _areaNamesData, selectedSpinner);
				}
				break;
			/*case R.id.shopname_spinner:
				selectedSpinner = "SHOP";
				dropDownValueSelection(position, _shopNamesData, selectedSpinner);
				break;*/
			case R.id.order_status_spinner:
				if (orderStatusTouchClick)
				{
					selectedSpinner = "ORDER_STATUS";
					dropDownValueSelection(position, _orderStatusData, selectedSpinner);
				}
				break;
			case R.id.payment_terms_spinner:
				if (paymentTermsTouchClick)
				{
					selectedSpinner = "PAYMENT_TYPE";
					dropDownValueSelection(position, _paymentsSelectData, selectedSpinner);
				}
				break;

		}
	}

	@Override
	public void onNothingSelected(final AdapterView<?> parent)
	{
	}

	private void dropDownValueSelection(int position, ArrayList<ShopNamesData> _dropDownData, String selectedSpinner)
	{
		try
		{
			if (position != 0)
			{
				if (_dropDownData.size() != 0)
				{
					if (selectedSpinner.equals("ZONE"))
					{
						selected_zoneId = _dropDownData.get(position - 1).getShopId();
						HttpAdapter.getRouteDetails(BillingEditActivity.this, "routeName", selected_zoneId);
					}
					else if (selectedSpinner.equals("ROUTE"))
					{
						selected_roueId = _dropDownData.get(position - 1).getShopId(); //3
						HttpAdapter.getAreaDetailsByRoute(BillingEditActivity.this, "areaNameDP", selected_roueId);
					}
					else if (selectedSpinner.equals("AREA"))
					{
						selected_areaNameId = _dropDownData.get(position - 1).getShopId();
						HttpAdapter.getShopDetailsDP(BillingEditActivity.this, "shopName", selected_areaNameId);
					}
					/*else if (selectedSpinner.equals("SHOP"))
					{
						selected_ShopId = _dropDownData.get(position - 1).getShopId();
						HttpAdapter.getOrderNumberDp(BillingEditActivity.this, "orderNumber", selected_ShopId);
					}*/
					else if (selectedSpinner.equals("ORDER_STATUS"))
					{
						selected_orderStatusId = _dropDownData.get(position - 1).getShopId();
					}
					else if (selectedSpinner.equals("PAYMENT_TYPE"))
					{
						selected_paymentTermsId = _dropDownData.get(position - 1).getShopId();
						String paymentSelected = _dropDownData.get(position - 1).getShopName();
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
			}
		}
		catch (Exception e)
		{
		}
	}


	private void selectRouteNameBind()
	{
		routeNamestitle.clear();
		routeNamestitle.add("Select Route No");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNamestitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		routeName_sp.setAdapter(dataAdapter_areaName);
	}

	private void selectAreaNameBind()
	{
		areaNamestitle.clear();
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
		//selectShopNameBind();
	}

	private void selectOrderNumber()
	{
		orderNumberDP_str.clear();
		orderNumberDP_str.add("Select Order Number");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, orderNumberDP_str);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		orderNumber_sp.setAdapter(dataAdapter_areaName);
		//selectShopNameBind();
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event)
	{
		switch (v.getId())
		{

			case R.id.zone_name_spinner:
				zoneTouchClick = false;
				routeTouchClick = false;
				areaTouchClick = false;
				shopNamesTouchClick = false;
				break;
			case R.id.routeName_spinner:
				zoneTouchClick = false;
				routeTouchClick = false;
				areaTouchClick = false;
				shopNamesTouchClick = false;
				break;
			case R.id.areaName_spinner:
				zoneTouchClick = false;
				routeTouchClick = false;
				areaTouchClick = true;
				shopNamesTouchClick = false;
				break;
			case R.id.shopname_spinner:
				zoneTouchClick = false;
				routeTouchClick = false;
				areaTouchClick = false;
				shopNamesTouchClick = true;
				break;
			case R.id.order_status_spinner:
				orderStatusTouchClick = true;
				break;
			case R.id.payment_terms_spinner:
				paymentTermsTouchClick = true;
				break;
		}
		return false;
	}


	public class InvoiceCancel extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(BillingEditActivity.this);

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
						Intent i = new Intent(BillingEditActivity.this, Invoice.class);
						startActivity(i);
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Order Cancelled.", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(BillingEditActivity.this, Invoice.class);
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


							String jsonString = createJsonInvoiceSubmit(invoiceNum.getText().toString(), selected_zoneId, selected_roueId, selected_areaNameId,
							                                            selected_ShopId, orderNUm, "Y", "",
							                                            SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"),
							                                            TotalAmount, paidAmt.getText().toString(),
							                                            selected_orderStatusId, selected_paymentTermsId, chequeDate, CreditDays);
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
						new BillingEditActivity.InvoiceCancel().execute();
					}
				}
				else
				{
					Toast.makeText(mContext, "No internet Connection", Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.pendingBtn:
				if (v.getId() == R.id.pendingBtn)
				{

					if (selected_ShopId == null || selected_ShopId.isEmpty() || selected_ShopId.equals("0"))
					{
						Toast.makeText(getApplicationContext(), "Please Enter Shop Name", Toast.LENGTH_SHORT).show();
						return;
					}
					else
					{
						Log.e("ShopId", selected_ShopId);
						SharedPrefsUtil.setStringPreference(mContext, "ACCESS_INVOICE", "ACCESS_INVOICE_PAGE");
						SharedPrefsUtil.setStringPreference(mContext, "KEY_SHOP_ID", selected_ShopId);
						Intent i = new Intent(BillingEditActivity.this, PendingBillsActivity.class);
						startActivity(i);
					}
				}
				break;
		}
	}

	private void displayTableView(final List<GetOrderSummary> productDP)
	{
		tableLayout.setVisibility(View.VISIBLE);
		tableLayout.removeAllViews();
		headers();

		storedProductCategories.clear();
		storedProductCategories.addAll(productDP);

		for (int i = 0; i < productDP.size(); i++)
		{
			j = i;
			BillingEditActivity.OrderSummary row = new BillingEditActivity.OrderSummary(this, productDP.get(i), i);
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
						paymentSpinnerAdapter(jsonArray);
						Log.e("DataPaymentDrp", jsonArray.toString());
					}

				}
				// OrderStatus DropDown
				else if (response.getTag().equals("orderStatus"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						orderStatusSpinnerAdapter(jsonArray);
						Log.e("DataOrderStausDrp", jsonArray.toString());
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
						dataAdapter_orderNumber.setDropDownViewResource(R.layout.list_item);
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
									HttpAdapter.getOrderSummary(BillingEditActivity.this, "orderSummary", orderNumberDropDown);
									HttpAdapter.getOrderTotal(BillingEditActivity.this, "totalAmount", orderNUm);
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
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						orderSummary.clear();
						list_of_orders.clear();
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
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
							if (jsonArray != null)
							{
								for (int i = 0; i < jsonArray.length(); i++)
								{
									TotalAmountdouble = jsonArray.getDouble(i);
									TotalAmount = String.valueOf(TotalAmountdouble);
									Log.e("totalAmount", TotalAmount);
									AvailableTotalAmount = String.format("%.2f", TotalAmountdouble);
								}
//								Log.e("totalAmounttxt", String.format("%.2f", TotalAmountdouble + ""));
								totalAmt.setText("Total Amount: " + String.format("%.2f", TotalAmountdouble));
							}
							else
							{
								totalAmt.setText("null");
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();

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
						//SI_00002
						Log.e("Invoiceresponse", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Saved", Toast.LENGTH_SHORT).show();
						Toast.makeText(mContext, "Your Invoice Number is " + mJson.getString("Data"), Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Saved.", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}

				}
				//ZoneDetails DropDown
				else if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
						Log.e("DataZoneDrp", jsonArray.toString());
					}
				}
				//RouteDetails DropDown
				else if (response.getTag().equals("routeName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						routeNoSpinnerAdapter(jsonArray);
						Log.e("DataRouteDrp", jsonArray.toString());
					}
				}
				//AreaDetails DropDown
				else if (response.getTag().equals("areaNameDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						areaNameSpinnerAdapter(jsonArray);
						Log.e("DataAreaDrp", jsonArray.toString());
					}
				}
				//Shop Name DropDown
				else if (response.getTag().equals("shopName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						shopNameSpinnerAdapter(jsonArray);
						Log.e("DataShopNameDrp", jsonArray.toString());
					}
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
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


	private void headers()
	{
		android.widget.TableRow row = new android.widget.TableRow(this);

		TextView taskdate = new TextView(BillingEditActivity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Product");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(BillingEditActivity.this);
		title.setText("Price");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                               android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(BillingEditActivity.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(BillingEditActivity.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(BillingEditActivity.this);
		description.setText("GST");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                     android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(BillingEditActivity.this);
		description2.setText("SubTotal");
		description2.setBackgroundColor(getResources().getColor(R.color.light_green));
		description2.setTextSize(15);
		description2.setPadding(10, 10, 10, 10);
		row.addView(description2);
		description2.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));


		tableLayout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));
	}

	private String createJsonInvoiceSubmit(String OrderNumber, String ZoneId, String RouteId,
	                                       String AreaId, String ShopId, String orderNo, String inVoice,
	                                       String Remarks, String EmployeeId,
	                                       String totalAmountStr, String paidAmountstr, String OrderStatusId
			, String paymentTermsId, String chequeDate, String creditDays
	)
	{
		JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
//			dataObj.putOpt("OrderInvoiceNumber", OrderNumber);
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
			dataObj.putOpt("OrderStatusId", OrderStatusId);
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


	private boolean validationEntryData()
	{
		boolean ret = true;
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

		if (selected_ShopId == null || selected_ShopId.isEmpty() || selected_ShopId.equals("0"))
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
			if (selected_orderStatusId == null || selected_orderStatusId.isEmpty() || selected_orderStatusId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Order Status", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}

			if (selected_paymentTermsId == null || selected_paymentTermsId.isEmpty() || selected_paymentTermsId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Payment Terms Name", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}
			String paidAmount = paidAmt.getText().toString();
			if (!paidAmount.equalsIgnoreCase(null) && !paidAmount.isEmpty())
			{
				/*double totalValue = Double.parseDouble(TotalAmount);
				double paidValue = Double.parseDouble(paidAmount);
				if (totalValue >= paidValue)
				{
					//Toast.makeText(getApplicationContext(), "Paid Amount " + paidValue, Toast.LENGTH_SHORT).show();
					*//*Toast.makeText(getApplicationContext(), "Please Check Paid Amount", Toast.LENGTH_SHORT).show();
					ret = false;
					return ret;*//*
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Check Paid Amount", Toast.LENGTH_SHORT).show();
					ret = false;
					return ret;
				}*/

				double totalValue = Double.parseDouble(AvailableTotalAmount);
				double paidValue = Double.parseDouble(paidAmount);
				if (totalValue != paidValue)
				{
					Toast.makeText(getApplicationContext(), "Please Check Total Amount", Toast.LENGTH_SHORT).show();
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
		try
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
					try
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
					catch (Exception e)
					{

					}

				}


			});

			close_popup.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					try
					{
						if (promoDialog != null)
						{
							promoDialog.dismiss();
							Util.hideSoftKeyboard(mContext, v);
							refreshActivity();
						}
					}
					catch (Exception e)
					{

					}

				}
			});

			alert_submit.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					try
					{
						if (check1)
						{
							Intent in = new Intent(BillingEditActivity.this, OrderBookingActivity.class);
							Util.killInvoice();
							startActivity(in);
						}
						else if (check2)
						{
							Intent inten = new Intent(BillingEditActivity.this, Invoice.class);
							Util.killInvoice();
							startActivity(inten);
						}
						else
						{
							Toast.makeText(mContext, "Please Select Order Book or Invoice", Toast.LENGTH_SHORT).show();
						}
					}
					catch (Exception e)
					{

					}

				}
			});
		}
		catch (Exception e)
		{

		}


	}

	private void refreshActivity()
	{
		SharedPrefsUtil.setStringPreference(mContext, "KEY_SHOP_ID", "");
		SharedPrefsUtil.setStringPreference(mContext, "KEY_SHOP_NAME", "");
		SharedPrefsUtil.setStringPreference(mContext, "KEY_ORDER_STATUS_ID", "");
		SharedPrefsUtil.setStringPreference(mContext, "KEY_AREANAME_ID", "");
		HttpAdapter.getAreaDetailsByRoute(BillingEditActivity.this, "areaNameDP", selected_roueId);
		//

		Intent i = getIntent();
		finish();
		startActivity(i);
	}

	private class OrderSummary extends android.widget.TableRow
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
				description.setText(String.valueOf(mProductCategory.VAT) + "%");
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
			zoneNamestitle.add("Zone Name");
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
		SPINNER_SELECTION = "ZONE";
		adapterDataAssigingToSpinner(zoneNamestitle, SPINNER_SELECTION);
		autoFillDetails();
	}

	private void routeNoSpinnerAdapter(JSONArray jsonArray)
	{
		try
		{
			_routeCodesData.clear();
			routeNamestitle.clear();
			_routeCodesData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("RouteId");
				String shopNamee = jsnobj.getString("RouteName");
				Log.e("RouteId + RouteName", shopId + shopNamee);
				_routeCodesData.add(new ShopNamesData(shopId, shopNamee));
			}
			routeNamestitle.add("Select Route No");
			if (_routeCodesData.size() > 0)
			{
				for (int i = 0; i < _routeCodesData.size(); i++)
				{
					routeNamestitle.add(_routeCodesData.get(i).getShopName());
				}
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SPINNER_SELECTION = "ROUTE";
		adapterDataAssigingToSpinner(routeNamestitle, SPINNER_SELECTION);
		if (!zoneTouchClick)
		{
			routeName_sp.setSelection(getIndexWithId(routeName_sp, Integer.parseInt(selected_roueId), _routeCodesData), false);
			HttpAdapter.getAreaDetailsByRoute(BillingEditActivity.this, "areaNameDP", selected_roueId);
		}
		else if (!routeTouchClick)
		{
			//selectAreaNameBind();
			selected_areaNameId = "";
			areaNamestitle.clear();
			areaNamestitle.add("Select Area Name");
			shoptypesNamestitle.clear();
			shopName_autoComplete.setText("");
			ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
			dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
			areaName_sp.setAdapter(dataAdapter_areaName);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, shoptypesNamestitle);
			shopName_autoComplete.setThreshold(1);
			shopName_autoComplete.setAdapter(adapter);
			shopName_autoComplete.setTextColor(Color.BLACK);
			shopName_autoComplete.setTextSize(16);
		}
		else if (zoneTouchClick)
		{
			clearShopNamesData(shoptypesNamestitle);
		}
		else if (routeTouchClick)
		{
			clearShopNamesData(shoptypesNamestitle);
		}
	}

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
		SPINNER_SELECTION = "AREA";
		adapterDataAssigingToSpinner(areaNamestitle, SPINNER_SELECTION);
		if (zoneTouchClick)
		{
			clearShopNamesData(shoptypesNamestitle);
		}
		else if (routeTouchClick)
		{
			clearShopNamesData(shoptypesNamestitle);
		}
		else if (!zoneTouchClick && !routeTouchClick && !areaTouchClick)
		{
			if (selected_areaNameId != null && !selected_areaNameId.isEmpty())
			{
				areaName_sp.setSelection(getIndexWithId(areaName_sp, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
				HttpAdapter.getShopDetailsDP(BillingEditActivity.this, "shopName", selected_areaNameId);
			}
		}
	}

	private void shopNameSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("shopNamesDropdown", jsonArray.toString() + "");
		if (zoneTouchClick)
		{
			clearShopNamesData(shoptypesNamestitle);
		}
		else if (routeTouchClick)
		{
			clearShopNamesData(shoptypesNamestitle);
		}
		else
		{
			try
			{
				_shopNamesData.clear();
				shooNamestitle.clear();
				_shopNamesData = new ArrayList<ShopNamesData>();
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsnobj = jsonArray.getJSONObject(i);
					String shopId = jsnobj.getString("ShopId");
					String shopNamee = jsnobj.getString("ShopName");
					_shopNamesData.add(new ShopNamesData(shopId, shopNamee));
				}
				//shooNamestitle.add("Select Shop Name");
				if (_shopNamesData.size() > 0)
				{
					for (int i = 0; i < _shopNamesData.size(); i++)
					{
						shooNamestitle.add(_shopNamesData.get(i).getShopName());
					}
				}
			}
			catch (Exception e)
			{
			}
			shopDataBinding(shoptypesNamestitle);
		}
		/*SPINNER_SELECTION = "SHOP";
		adapterDataAssigingToSpinner(shooNamestitle, SPINNER_SELECTION);*/

		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, shooNamestitle);
		shopName_autoComplete.setThreshold(1);
		shopName_autoComplete.setAdapter(adapter);
		shopName_autoComplete.setTextColor(Color.BLACK);
		shopName_autoComplete.setTextSize(16);*/

		shopName_autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
			{
				try
				{
					String selectedName = shopName_autoComplete.getText().toString();
					Log.e("entryShopName", selectedName);
					for (int i = 0; i < _shopNamesData.size(); i++)
					{
						String availName = _shopNamesData.get(i).getShopName();
						if (availName.equals(selectedName))
						{
							selected_ShopId = _shopNamesData.get(i).getShopId();
							Log.e("selected_ShopId", selected_ShopId + "");
							HttpAdapter.getOrderNumberDp(BillingEditActivity.this, "orderNumber", selected_ShopId);
							break;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void orderStatusSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("orderStatus", jsonArray.toString() + "");
		try
		{
			_orderStatusData.clear();
			orderStatusTitle.clear();
			_orderStatusData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				int shopId = jsnobj.getInt("OrderStatusId");
				String shopNamee = jsnobj.getString("OrderStatusDescription");
				_orderStatusData.add(new ShopNamesData(String.valueOf(shopId), shopNamee));
			}
			orderStatusTitle.add("Select Order Status");
			if (_orderStatusData.size() > 0)
			{
				for (int i = 0; i < _orderStatusData.size(); i++)
				{
					orderStatusTitle.add(_orderStatusData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
		}
		SPINNER_SELECTION = "ORDER_STATUS";
		adapterDataAssigingToSpinner(orderStatusTitle, SPINNER_SELECTION);
	}

	private void paymentSpinnerAdapter(final JSONArray jsonArray)
	{
		try
		{
			_paymentsSelectData.clear();
			paymentNamestitle.clear();
			_paymentsSelectData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				String shopId = jsnobj.getString("PaymentTermsId");
				String shopNamee = jsnobj.getString("PaymentName");
				_paymentsSelectData.add(new ShopNamesData(shopId, shopNamee));
			}
			paymentNamestitle.add("Select Payment");
			if (_paymentsSelectData.size() > 0)
			{
				for (int i = 0; i < _paymentsSelectData.size(); i++)
				{
					paymentNamestitle.add(_paymentsSelectData.get(i).getShopName());

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SPINNER_SELECTION = "PAYMENT_SELECT";
		adapterDataAssigingToSpinner(paymentNamestitle, SPINNER_SELECTION);
	}

	private void adapterDataAssigingToSpinner(ArrayList<String> spinnerTitles, String spinnerSelction)
	{
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTitles);
		dataAdapter.setDropDownViewResource(R.layout.list_item);
		if (spinnerSelction.equals("ZONE"))
		{
			zone_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("ROUTE"))
		{
			routeName_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("AREA"))
		{
			areaName_sp.setAdapter(dataAdapter);
		}
		/*else if (spinnerSelction.equals("SHOP"))
		{
			shopName_sp.setAdapter(dataAdapter);
		}*/
		else if (spinnerSelction.equals("ORDER_STATUS"))
		{
			orderStatus_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("PAYMENT_SELECT"))
		{
			payment_sp.setAdapter(dataAdapter);
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

	private void autoFillDetails()
	{
		try
		{
			if (SharedPrefsUtil.getStringPreference(mContext, "KEY_DESCRIPTION") != null && !SharedPrefsUtil.getStringPreference(mContext, "KEY_DESCRIPTION").isEmpty())
			{
				remarksET.setText(SharedPrefsUtil.getStringPreference(mContext, "KEY_DESCRIPTION") + "");
			}

			if (SharedPrefsUtil.getStringPreference(mContext, "KEY_PAYMENT_ID") != null && !SharedPrefsUtil.getStringPreference(mContext, "KEY_PAYMENT_ID").isEmpty())
			{
				selected_paymentTermsId = SharedPrefsUtil.getStringPreference(mContext, "KEY_PAYMENT_ID");
			}
			else
			{
				selected_paymentTermsId = "2";
			}
			Log.e("selected_paymentTermsId", selected_paymentTermsId);
			selected_zoneId = SharedPrefsUtil.getStringPreference(mContext, "KEY_ZONE_ID");// String.valueOf(zoneId);
			Log.e("selected_zoneId", selected_zoneId);
			selected_roueId = SharedPrefsUtil.getStringPreference(mContext, "KEY_ROUTE_ID");
			selected_areaNameId = SharedPrefsUtil.getStringPreference(mContext, "KEY_AREANAME_ID");
			selected_ShopId = SharedPrefsUtil.getStringPreference(mContext, "KEY_SHOP_ID");


			if (selected_ShopId != null && !selected_ShopId.isEmpty())
			{
				HttpAdapter.getOrderNumberDp(BillingEditActivity.this, "orderNumber", selected_ShopId);
			}


			AvailShopName = SharedPrefsUtil.getStringPreference(mContext, "KEY_SHOP_NAME");
			selected_orderStatusId = SharedPrefsUtil.getStringPreference(mContext, "KEY_ORDER_STATUS_ID");
			Log.e("AvailShopName", AvailShopName);
			Log.e("selected_orderStatusId", selected_orderStatusId);


			if (selected_zoneId != null && !selected_zoneId.isEmpty())
			{
				zone_sp.setSelection(getIndexWithId(zone_sp, Integer.parseInt(selected_zoneId), _zoneNamesData), false);
				HttpAdapter.getRouteDetails(BillingEditActivity.this, "routeName", selected_zoneId);
			}
			if (selected_paymentTermsId != null && !selected_paymentTermsId.equals("0") && !selected_paymentTermsId.equalsIgnoreCase("null"))
			{
				if (_paymentsSelectData.size() > 0)
				{
					payment_sp.setSelection(getIndexWithId(payment_sp, Integer.parseInt(selected_paymentTermsId), _paymentsSelectData), false);
				}
			}

			if (selected_orderStatusId != null && !selected_orderStatusId.equals("0") && !selected_orderStatusId.equalsIgnoreCase("null"))
			{
				if (_orderStatusData.size() > 0)
				{
					orderStatus_sp.setSelection(getIndexWithId(orderStatus_sp, Integer.parseInt(selected_orderStatusId), _orderStatusData), false);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	private int getIndexWithId(Spinner spinner, int searchId, ArrayList<ShopNamesData> _availbleDropDownData)
	{
		int searchIdIndex = 0;
		try
		{
			if (searchId == 0)
			{
				searchIdIndex = -1;
				return -1; // Not found
			}
			else
			{
				for (int i = 0; i < spinner.getCount(); i++)
				{
					String avaliableListDataid = _availbleDropDownData.get(i).getShopId();
					if (avaliableListDataid.equals(String.valueOf(searchId)))
					{
						searchIdIndex = i + 1;
						Log.e("availbleId", _availbleDropDownData.get(i).getShopId() + "");
						return searchIdIndex;
					}
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return searchIdIndex; // Not found
	}

	private void clearShopNamesData(final ArrayList<String> shoptypesNamestitles)
	{
		shoptypesNamestitles.clear();
		selected_ShopId = "";
		_shoptypesData.clear();
		shopName_autoComplete.setText("");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, shoptypesNamestitle);
		shopName_autoComplete.setThreshold(1);
		shopName_autoComplete.setAdapter(adapter);
		shopName_autoComplete.setTextColor(Color.BLACK);
		shopName_autoComplete.setTextSize(16);
	}

	private void shopDataBinding(final ArrayList<String> shoptypesNamestitle)
	{
		if (!AvailShopName.isEmpty())
		{
			shopName_autoComplete.setText(AvailShopName);
		}
		else
		{
			selected_ShopId = "";
			shopName_autoComplete.setText("");
		}


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, shoptypesNamestitle);
		shopName_autoComplete.setThreshold(1);
		shopName_autoComplete.setAdapter(adapter);
		shopName_autoComplete.setTextColor(Color.BLACK);
		shopName_autoComplete.setTextSize(16);
	}
}

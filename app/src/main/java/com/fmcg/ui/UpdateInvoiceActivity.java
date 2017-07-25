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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.GetAreaDetails;
import com.fmcg.models.GetOrderNumberDP;
import com.fmcg.models.UpdateInvoiceCategeoryData;
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
import org.json.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fmcg.util.Common.orderNUm;

/**
 * Created by Shiva on 7/20/2017.
 */

public class UpdateInvoiceActivity extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	public static Activity invoiceActivity;
	public SharedPreferences sharedPreferences;
	public List<PaymentDropDown> paymentDP;
	public List<GetShopDetailsDP> shopsDP;
	public List<OrderStatusDropdown> orderstatusDP;
	public List<GetProductCategory> productDP;
	public List<GetOrderNumberDP> orderNumberDP;
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetAreaDetails> areaDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	public List<GetProductCategory> list;
	public List<UpdateInvoiceCategeoryData> orderSummary;
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

	public String paymentDropDown, orderStatusDropDown;
	public Spinner shopName_sp, orderStatus_sp, category_sp, payment_sp, orderNumber_sp, zone_sp, areaName_sp, routecd;
	public CheckBox isShopClosed, ordered, invoice;
	public TextView uploadImage, shopClosed, submit, totalAmt, invoiceNum, cancel, order_No_Txt, dueAmount;
	private EditText paidAmt;
	private TableLayout tableLayout;

	String ZoneId = "";
	String RouteId = "";
	String AreaId = "";
	String ShopId = "";
	String OrderStatusId = "";
	String paymentTermsId = "";
	Context mContext;

	String TotalAmount = "";
	boolean orderCancel = false;
	String invoiceOrderno = "";
	String paidAmount = "";
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	Button alert_submit;
	RadioButton viewList;
	boolean check1 = false;
	boolean check2 = false;
	boolean check3 = false;


	public List<UpdateInvoiceCategeoryData> storedProductCategories = new ArrayList<UpdateInvoiceCategeoryData>();
	int j;
	public final List<UpdateInvoiceCategeoryData> list_of_orders = new ArrayList<UpdateInvoiceCategeoryData>();

	//Payment Selection
	EditText creditdays;
	LinearLayout creditDaysLayout;
	DatePicker dateselect;
	Button dateaccept;
	private static TextView paymentSelectedvalue;

	///////All Drop Downs
	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>(); //Shop Names Newly added
	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<ShopNamesData> _routeCodesData = new ArrayList<ShopNamesData>(); //Route Drop Down
	ArrayList<ShopNamesData> _areaNamesData = new ArrayList<ShopNamesData>(); //Area Drop down
	ArrayList<ShopNamesData> _orderStatusData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _shoptypesData = new ArrayList<ShopNamesData>(); //Shop Type Drop Down
	ArrayList<ShopNamesData> _religionsData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _paymentsSelectData = new ArrayList<ShopNamesData>(); //Select Payment

	ArrayList<String> shooNamestitle = new ArrayList<String>(); // Shop Name Title
	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	ArrayList<String> routeNamestitle = new ArrayList<String>();
	ArrayList<String> areaNamestitle = new ArrayList<String>();
	ArrayList<String> orderStatusTitle = new ArrayList<String>();
	ArrayList<String> shoptypesNamestitle = new ArrayList<String>();
	ArrayList<String> religionsNamestitle = new ArrayList<String>();
	ArrayList<String> paymentNamestitle = new ArrayList<String>();

	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_ShopId = "";
	String selected_orderStatusId = "";
	String selected_ShopTypeId = "";
	String selected_religionNameId = "";
	String selected_paymentNameId = "";

	boolean zoneTouchClick = false;
	boolean routeTouchClick = false;
	boolean areaTouchClick = false;
	boolean shopNamesTouchClick = false;
	boolean orderStatusTouchClick = false;
	boolean paymentTermsTouchClick = false;

	String orderStatusName = "";
	String OrderSalesId = "";


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_invoice_activity);
		mContext = UpdateInvoiceActivity.this;
		invoiceActivity = UpdateInvoiceActivity.this;

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		//initializing the variables
		tableLayout = (TableLayout) findViewById(R.id.tableRow1);

		orderStatus_sp = (Spinner) findViewById(R.id.order_type_dp);
		category_sp = (Spinner) findViewById(R.id.product_category);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_name);

		orderNumber_sp = (Spinner) findViewById(R.id.order_number);


		isShopClosed = (CheckBox) findViewById(R.id.isClosed);
		ordered = (CheckBox) findViewById(R.id.isOrder);
		invoice = (CheckBox) findViewById(R.id.isInvoice);
		paidAmt = (EditText) findViewById(R.id.paidAmount);
		uploadImage = (TextView) findViewById(R.id.upLoadImage);
		shopClosed = (TextView) findViewById(R.id.shopClosed);
		totalAmt = (TextView) findViewById(R.id.totalAmount);
		invoiceNum = (TextView) findViewById(R.id.invoice_number);
		submit = (TextView) findViewById(R.id.submit);

		paymentSelectedvalue = (TextView) findViewById(R.id.paymentSelectedvalue);
		paymentSelectedvalue.setVisibility(View.GONE);

		order_No_Txt = (TextView) findViewById(R.id.order_No_Txt);
		dueAmount = (TextView) findViewById(R.id.dueAmount);

		cancel = (TextView) findViewById(R.id.cancel);

		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routecd = (Spinner) findViewById(R.id.routeName_spinner);
		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);
		shopName_sp = (Spinner) findViewById(R.id.shopname_spinner);

		zone_sp.setEnabled(false);
		routecd.setEnabled(false);
		areaName_sp.setEnabled(false);
		shopName_sp.setEnabled(false);


		HttpAdapter.getOrderStatus(UpdateInvoiceActivity.this, "orderStatus");
		HttpAdapter.getPayment(UpdateInvoiceActivity.this, "payment");
		HttpAdapter.getProductCategoryDP(UpdateInvoiceActivity.this, "productCategoryName");
		HttpAdapter.getZoneDetailsDP(UpdateInvoiceActivity.this, "zoneName");


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

		/*zone_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				zoneTouchClick = true;
				routeTouchClick = false;
				areaTouchClick = false;
				shopNamesTouchClick = false;
				return false;
			}
		});

		routecd.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				routeTouchClick = true;
				zoneTouchClick = false;
				areaTouchClick = false;
				shopNamesTouchClick = false;
				return false;
			}
		});

		areaName_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				routeTouchClick = false;
				zoneTouchClick = false;
				areaTouchClick = true;
				shopNamesTouchClick = false;
				return false;
			}
		});
		shopName_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				shopNamesTouchClick = true;
				routeTouchClick = false;
				zoneTouchClick = false;
				areaTouchClick = false;
				return false;
			}
		});*/

		orderStatus_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				orderStatusTouchClick = true;
				return false;
			}
		});
		payment_sp.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{
				paymentTermsTouchClick = true;
				return false;
			}
		});
	}

	public String serviceCallInvoiceCancelOrder()
	{
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(HttpAdapter.CANCEL_ORDER_NUMBER + "?ShopId=" + selected_ShopId + "&OrderNumber=" + orderNUm + "&UserID=" + SharedPrefsUtil
				.getStringPreference(mContext, "EmployeeId"));
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

	public class InvoiceCancel extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(UpdateInvoiceActivity.this);

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
						Intent i = new Intent(UpdateInvoiceActivity.this, Invoice.class);
						startActivity(i);
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Order Cancelled.", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(UpdateInvoiceActivity.this, Invoice.class);
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

							JSONArray cartItemsArray = new JSONArray();
							JSONArray griddataArray = new JSONArray();
							if (storedProductCategories != null && !storedProductCategories.isEmpty())
							{
								Log.d("Order", "Stored_Products" + " : " + storedProductCategories.toString());
								for (UpdateInvoiceCategeoryData getProducts : storedProductCategories)
								{
									try
									{
										cartItemsArray.put(getProducts.toJSON());
									}
									catch (Exception e)
									{
										e.printStackTrace();
									}
								}
							}
							if (cartItemsArray.length() == 0)
							{
								for (int i = 0; i < list.size(); i++)
								{
									try
									{
										cartItemsArray.put(list.get(i).toJSON());
									}
									catch (Exception e)
									{
									}
								}
							}


//							TotalAmount = String.format("%.2f", Double.valueOf(TotalAmount));
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

							try
							{
								if (cartItemsArray.length() != 0)
								{
									JSONObject categoryData = new JSONObject();
									for (int i = 0, len = cartItemsArray.length(); i < len; i++)
									{
										categoryData.put("ProductId", cartItemsArray.getJSONObject(i).getInt("ProductId"));
										categoryData.put("ProductPrice", cartItemsArray.getJSONObject(i).getInt("ProductPrice"));
										categoryData.put("Quantity", cartItemsArray.getJSONObject(i).getInt("Quantity"));
										categoryData.put("Frees", cartItemsArray.getJSONObject(i).getInt("Frees"));
										categoryData.put("VAT", cartItemsArray.getJSONObject(i).getDouble("VAT"));
										categoryData.put("GST", cartItemsArray.getJSONObject(i).getInt("GST"));
									}
									griddataArray.put(categoryData);
									Log.e("griddataArray", griddataArray.toString());
								}

							}
							catch (Exception e)
							{
							}


							String jsonString = createJsonInvoiceSubmit(invoiceNum.getText().toString(), selected_zoneId, selected_roueId,
							                                            selected_areaNameId,
							                                            selected_ShopId, order_No_Txt.getText().toString(), "Y", "",
							                                            SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"),
							                                            TotalAmount, paidAmt.getText().toString(),
							                                            selected_paymentNameId, chequeDate, CreditDays, griddataArray);
							Log.e("parameters", jsonString + "");
							HttpAdapter.updateInvoiceSave(this, "updateInvoiceSubmit", jsonString);
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

	private void displayTableView(final List<UpdateInvoiceCategeoryData> productDP)
	{
		tableLayout.removeAllViews();
		headers();

		storedProductCategories.clear();
		storedProductCategories.addAll(productDP);

		for (int i = 0; i < productDP.size(); i++)
		{
			j = i;
			UpdateInvoiceActivity.OrderSummary row = new UpdateInvoiceActivity.OrderSummary(this, productDP.get(i), i);
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
						paymentNamesSpinnerAdapter(jsonArray);
					}
				}
				//Shop Name DropDown
				else if (response.getTag().equals("shopName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						shopNameSpinnerAdapter(jsonArray);
					}
				}
				// OrderStatus DropDown
				else if (response.getTag().equals("orderStatus"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						orderStatusSpinnerAdapter(jsonArray);
					}
				}
				//ZoneDetails DropDown
				else if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
					}

				}
				//AreaDetails DropDown
				else if (response.getTag().equals("areaNameDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						areaNameSpinnerAdapter(jsonArray);
					}

				}
				//RouteDetails DropDown
				else if (response.getTag().equals("routeName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						routeNoSpinnerAdapter(jsonArray);
					}

				}
				else if (response.getTag().equals("updateInvoiceSubmit"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("Invoiceresponse", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "UnSuccessfully Uploaded.", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}

				}
				else if (response.getTag().equals("productCategoryItems"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						Log.e("responseEditOrder", jsonArray.toString() + "Success");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							Log.e("objData", obj.toString());
							UpdateInvoiceCategeoryData UpdateInvoiceCategeoryData = new Gson().fromJson(obj.toString(), UpdateInvoiceCategeoryData.class);
							list_of_orders.add(UpdateInvoiceCategeoryData);
							displayTableView(list_of_orders);
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

		TextView taskdate = new TextView(UpdateInvoiceActivity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Product");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(UpdateInvoiceActivity.this);
		title.setText("Price");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                               android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(UpdateInvoiceActivity.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(UpdateInvoiceActivity.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(UpdateInvoiceActivity.this);
		description.setText("VAT");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                     android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(UpdateInvoiceActivity.this);
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
	                                       String totalAmountStr, String paidAmountstr
			, String paymentTermsId, String chequeDate, String creditDays, JSONArray cartItemsArray
	)
	{

		String OrderDeliveryDate = "";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat simDf = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			OrderDeliveryDate = simDf.format(c.getTime());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		JSONObject productListObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("ZoneId", ZoneId);
			dataObj.putOpt("RouteId", RouteId);
			dataObj.putOpt("AreaId", AreaId);
			dataObj.putOpt("OrderNumber", orderNo);
			dataObj.putOpt("PaymentTermsId", paymentTermsId);
			if (chequeDate != null && !chequeDate.isEmpty())
			{
				dataObj.putOpt("PaymentDateCheque", chequeDate);
			}
			else
			{
				dataObj.putOpt("PaymentDateCheque", null);
			}

			if (creditDays != null && !creditDays.isEmpty())
			{
				dataObj.putOpt("CreditDays", creditDays);
			}
			else
			{
				dataObj.putOpt("CreditDays", null);
			}
			dataObj.putOpt("OrderSalesId", OrderSalesId);
			dataObj.putOpt("OrderDeliveryDate", OrderDeliveryDate);
			dataObj.putOpt("OrderStatusId", selected_orderStatusId);
			productListObj.put("ProductList", cartItemsArray);
			productListObj.put("InVoiceSubmitData", dataObj);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("params", productListObj.toString());
		return productListObj.toString();
	}

	private boolean validationEntryData()
	{
		boolean ret = true;
		Log.e("selected_zoneId", selected_zoneId);
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
		}
		else
		{
			if (selected_orderStatusId == null || selected_orderStatusId.isEmpty() || selected_orderStatusId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Order Status", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}

			if (selected_paymentNameId == null || selected_paymentNameId.isEmpty() || selected_paymentNameId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Payment Terms Name", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}
			/*String paidAmount = paidAmt.getText().toString();
			if (!paidAmount.equalsIgnoreCase(null) && !paidAmount.isEmpty())
			{
				double totalValue = Double.parseDouble(TotalAmount);
				double paidValue = Double.parseDouble(paidAmount);
				if (totalValue >= paidValue)
				{
					Toast.makeText(getApplicationContext(), "Paid Amount " + paidValue, Toast.LENGTH_SHORT).show();
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
			}*/
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

		viewList = (RadioButton) promoDialog.findViewById(R.id.viewList);
		viewList.setVisibility(View.VISIBLE);
		promoDialog.show();

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
					case R.id.viewList:
						check3 = true;
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
//					refreshActivity();
					Intent i = new Intent(mContext, DashboardActivity.class);
					startActivity(i);
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

					Intent in = new Intent(UpdateInvoiceActivity.this, Order.class);
					Util.killInvoice();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(UpdateInvoiceActivity.this, Invoice.class);
					Util.killInvoice();
					startActivity(inten);
				}
				else if (check3)
				{
					Intent inten = new Intent(UpdateInvoiceActivity.this, ViewListActivity.class);
					Util.killupdateorderBook();
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

	private class OrderSummary extends android.widget.TableRow
	{

		private Context mContext;
		private UpdateInvoiceCategeoryData mProductCategory;

		private EditText quantityETID;
		private EditText fresETID;

		private String afterTextChanged = "";
		private String beforeTextChanged = "";
		private String onTextChanged = "";

		private final int position;

		public OrderSummary(final Context context, final UpdateInvoiceCategeoryData productCategory, int index)
		{
			super(context);
			mContext = context;
			mProductCategory = productCategory;
			position = index;
			init();
		}

		public UpdateInvoiceCategeoryData getProductCategory()
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
			DialogFragment newFragment = new UpdateInvoiceActivity.DatePickerFragmentDailog();
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
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
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
			String editInovoiceData = SharedPrefsUtil.getStringPreference(mContext, "EDIT_INVOICE_DATA_STRING");
			Log.e("editInovoiceData", editInovoiceData);
			if (editInovoiceData != null && !editInovoiceData.isEmpty())
			{
				editInoviceDetails(editInovoiceData);
			}
			else
			{

			}
		}
		catch (Exception e)
		{

		}
	}

	private void editInoviceDetails(final String editInovoiceData)
	{
		zoneTouchClick = false;
		routeTouchClick = false;
		areaTouchClick = false;
		orderStatusTouchClick = false;
		paymentTermsTouchClick = false;
		try
		{
			JSONObject editDatajsonObj = new JSONObject(editInovoiceData);
			OrderSalesId = String.valueOf(editDatajsonObj.getInt("OrderSalesId"));
			String OrderInvoiceNumber = editDatajsonObj.getString("OrderInvoiceNumber");
			if (OrderInvoiceNumber != null && !OrderInvoiceNumber.equalsIgnoreCase("null"))
			{
				Log.e("OrderNumber", OrderInvoiceNumber);
				invoiceNum.setText(OrderInvoiceNumber);
			}

			String InvoiceDate = editDatajsonObj.getString("InvoiceDate");
			String Customer = editDatajsonObj.getString("Customer");
			try
			{
				double TotalAmountdbl = editDatajsonObj.getDouble("TotalAmount");
				TotalAmount = String.valueOf(TotalAmountdbl);
				totalAmt.setText("Total Amount: " + String.format("%.2f", Double.valueOf(TotalAmountdbl)));
				double PaidAmount = editDatajsonObj.getDouble("PaidAmount");
				paidAmt.setText("Paid Amount: " + String.format("%.2f", Double.valueOf(PaidAmount)));
				int DueAmount = editDatajsonObj.getInt("DueAmount");
				dueAmount.setText("Due Amount: " + DueAmount);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}


			int ShopId = editDatajsonObj.getInt("ShopId");
			String OrderNumber = editDatajsonObj.getString("OrderNumber");
			if (OrderNumber != null && !OrderNumber.equalsIgnoreCase("null"))
			{
				order_No_Txt.setText(OrderNumber + "");
				HttpAdapter.orderSummryProductCategory(UpdateInvoiceActivity.this, "productCategoryItems", OrderNumber);
			}

			int Balance = editDatajsonObj.getInt("Balance");
			String Status = editDatajsonObj.getString("Status");
			int zoneId = editDatajsonObj.getInt("ZoneId");
			int RouteId = editDatajsonObj.getInt("RouteId");
			int AreaId = editDatajsonObj.getInt("AreaId");
			selected_zoneId = String.valueOf(zoneId);
			selected_roueId = String.valueOf(RouteId);
			selected_areaNameId = String.valueOf(AreaId);
			selected_ShopId = String.valueOf(ShopId);

			zone_sp.setBackgroundColor(Color.TRANSPARENT);
			zone_sp.setClickable(false);
			zone_sp.setSelection(getIndexWithId(zone_sp, zoneId, _zoneNamesData), false);
			HttpAdapter.getRouteDetails(UpdateInvoiceActivity.this, "routeName", selected_zoneId);

			try
			{
				int PaymentTermsId = editDatajsonObj.getInt("PaymentTermsId");
				PaymentTermsId = 2;
				selected_paymentNameId = String.valueOf(PaymentTermsId);
				payment_sp.setSelection(getIndexWithId(payment_sp, PaymentTermsId, _paymentsSelectData), false);
			}
			catch (Exception e)
			{
			}
			try
			{
				int orderStatusID = editDatajsonObj.getInt("orderStatusID");
				selected_orderStatusId = String.valueOf(orderStatusID);
				if (Status != null && !Status.equalsIgnoreCase("null"))
				{
					orderStatusName = Status;
				}
				orderStatus_sp.setSelection(getIndexWithName(orderStatus_sp, orderStatusName, _orderStatusData), false);
			}
			catch (Exception e)
			{

			}

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

		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, R.layout.spinner_item, zoneNamestitle);
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zone_sp.setAdapter(dataAdapter_zoneName);

		autoFillDetails();
		zone_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (zoneTouchClick)
				{
					Toast.makeText(mContext, "Zone Touch true", Toast.LENGTH_SHORT).show();
					if (position != 0)
					{
						selected_zoneId = _zoneNamesData.get(position - 1).getShopId();
						selected_roueId = "";
						selected_areaNameId = "";
						HttpAdapter.getRouteDetails(UpdateInvoiceActivity.this, "routeName", selected_zoneId);
					}
				}
				else
				{
//					Toast.makeText(mContext, "Zone Touch False", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
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
		//Routedetails adapter
		ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this, R.layout.spinner_item, routeNamestitle);
		dataAdapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routecd.setAdapter(dataAdapter_routeName);

		if (!zoneTouchClick)
		{
			routecd.setBackgroundColor(Color.TRANSPARENT);
			routecd.setClickable(false);
			routecd.setSelection(getIndexWithId(routecd, Integer.parseInt(selected_roueId), _routeCodesData), false);
			HttpAdapter.getAreaDetailsByRoute(UpdateInvoiceActivity.this, "areaNameDP", selected_roueId);
		}
		else if (!routeTouchClick)
		{
			defaultAreaNameSelect();
		}


		routecd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (routeTouchClick)
				{
					if (position != 0)
					{
						selected_roueId = _routeCodesData.get(position - 1).getShopId(); //3
						HttpAdapter.getAreaDetailsByRoute(UpdateInvoiceActivity.this, "areaNameDP", selected_roueId);
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
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

		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, R.layout.spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);

		if (!zoneTouchClick && !routeTouchClick && !areaTouchClick)
		{
			areaName_sp.setBackgroundColor(Color.TRANSPARENT);
			areaName_sp.setClickable(false);
			areaName_sp.setSelection(getIndexWithId(areaName_sp, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
			HttpAdapter.getShopDetailsDP(UpdateInvoiceActivity.this, "shopName", selected_areaNameId);
		}
		else
		{
		}


		areaName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (areaTouchClick)
				{
					if (position != 0)
					{
						selected_areaNameId = _areaNamesData.get(position - 1).getShopId();
						HttpAdapter.getShopDetailsDP(UpdateInvoiceActivity.this, "shopName", selected_areaNameId);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void shopNameSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("shopNamesDropdown", jsonArray.toString() + "");
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
			shooNamestitle.add("Select Shop Name");
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
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this, R.layout.spinner_item, shooNamestitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopName_sp.setAdapter(dataAdapter_shopType);

		if (!zoneTouchClick && !routeTouchClick && !areaTouchClick && !shopNamesTouchClick)
		{
			shopName_sp.setBackgroundColor(Color.TRANSPARENT);
			shopName_sp.setClickable(false);
			shopName_sp.setSelection(getIndexWithId(shopName_sp, Integer.valueOf(selected_ShopId), _shopNamesData), false);
		}
		else
		{
//			areaName_sp.setSelection(getIndexWithId(areaName_sp, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
//			defaultAreaNameSelect();
		}


		shopName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
				{
					if (position != 0)
					{
						selected_ShopId = _shopNamesData.get(position - 1).getShopId();
//						HttpAdapter.shopEditDetails(UpdateInvoiceActivity.this, "editShopDetails", selected_ShopId);
						HttpAdapter.getOrderNumberDp(UpdateInvoiceActivity.this, "orderNumber", selected_ShopId);
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
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this,
		                                                                     android.R.layout.simple_spinner_item,
		                                                                     orderStatusTitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		orderStatus_sp.setAdapter(dataAdapter_shopType);

		/*if (!areaDropDownItemSeleted)
		{
			placingAvailShopName(Integer.parseInt(selected_shopId));
		}*/


		orderStatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
				{
					if (orderStatusTouchClick)
					{
						if (position != 0)
						{
							selected_orderStatusId = _orderStatusData.get(position - 1).getShopId();
						}
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


	private void paymentNamesSpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("paymentTermsData", jsonArray.toString() + "");
		try
		{
			_paymentsSelectData.clear();
			paymentNamestitle.clear();
			_paymentsSelectData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				int shopId = jsnobj.getInt("PaymentTermsId");
				String shopNamee = jsnobj.getString("PaymentName");
				_paymentsSelectData.add(new ShopNamesData(String.valueOf(shopId), shopNamee));
			}
			paymentNamestitle.add("Select Payment Terms Name");
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
		}
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this,
		                                                                     android.R.layout.simple_spinner_item,
		                                                                     paymentNamestitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		payment_sp.setAdapter(dataAdapter_shopType);
		payment_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
				{
					if (paymentTermsTouchClick)
					{
						if (position != 0)
						{
							selected_paymentNameId = _paymentsSelectData.get(position - 1).getShopId();
							String paymentSelected = _paymentsSelectData.get(position - 1).getShopName();
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

	private void defaultAreaNameSelect()
	{
		selected_areaNameId = "";
		areaNamestitle.clear();
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
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


	private int getIndexWithName(Spinner spinner, String searchName, ArrayList<ShopNamesData> _availbleDropDownData)
	{
		int searchIdIndex = 0;
		try
		{
			//Log.d(LOG_TAG, "getIndex(" + searchString + ")");
			if (searchName == null || spinner.getCount() == 0)
			{
				searchIdIndex = -1;
				return -1; // Not found
			}
			else
			{
				for (int i = 0; i < spinner.getCount(); i++)
				{
					String availName = _availbleDropDownData.get(i).getShopName();
					if (availName.equals(searchName))
					{
						searchIdIndex = i + 1;
						Log.e("availbleStringName", _availbleDropDownData.get(i).getShopName() + "");
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
}



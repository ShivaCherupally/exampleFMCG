package com.fmcg.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.TableRow;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.GetAreaDetails;
import com.fmcg.models.GetProductCategoryInOrderUpdate;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fmcg.util.SharedPrefsUtil.getStringPreference;

/**
 * Created by Shiva on 7/26/2017.
 */

public class UpdateOrderDetailsActivity extends AppCompatActivity implements NetworkOperationListener, AdapterView.OnItemSelectedListener, View.OnTouchListener
{
	public static Activity orderBookActivity;
	//	public List<PaymentDropDown> paymentDP;
	public List<OrderStatusDropdown> orderstatusDP;
	public List<GetProductCategoryInOrderUpdate> productDP;
	public List<GetProductCategoryInOrderUpdate> storedProductCategories = new ArrayList<GetProductCategoryInOrderUpdate>();
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetAreaDetails> areaDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	public final List<GetProductCategoryInOrderUpdate> list = new ArrayList<GetProductCategoryInOrderUpdate>();
	private List<GetRouteDropDown> routeDp;

	private List<String> paymentDP_str;
	private List<String> shopNameDP_str;
	private List<String> orderstatusDP_str;
	private List<String> productDP_str;
	private List<String> zoneDetailsDP_str;
	private List<String> areaDetailsDP_str;
	private List<String> routeDetailsDP_str;
	private List<String> routeDp_str;

	public Spinner order_status_spinner, product_category_spinner, payment_terms_spinner, routeName_spinner, areaName_spinner, zone_name_spinner;
	public TextView orderNumInvoice, submit;
	private static TextView paymentSelected;
	private TableLayout tableLayout;
	int j;
	Context mContext;
	ArrayList<String> routenostitle = new ArrayList<String>();
	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	RadioButton viewList;
	Button alert_submit;
	String AvailShopName = "";
	boolean check1 = false;
	boolean check2 = false;
	boolean check3 = false;


	//Payment Selection
	EditText creditdays;
	LinearLayout creditDaysLayout;
	DatePicker dateselect;
	Button dateaccept;

	String OrderDeliveryDate = "";

	//////////All Spinner model classes
//	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>(); //Shop Names Newly added
	ArrayList<ShopNamesData> _zoneNamesData = new ArrayList<ShopNamesData>(); //Zone Drop down
	ArrayList<ShopNamesData> _routeCodesData = new ArrayList<ShopNamesData>(); //Route Drop Down
	ArrayList<ShopNamesData> _areaNamesData = new ArrayList<ShopNamesData>(); //Area Drop down
	ArrayList<ShopNamesData> _shoptypesData = new ArrayList<ShopNamesData>(); //Shop Type Drop Down
	ArrayList<ShopNamesData> _orderStatusData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _productCategoryData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _paymentsSelectData = new ArrayList<ShopNamesData>(); //Select Payment


	ArrayList<String> shooNamestitle = new ArrayList<String>(); // Shop Name Title
	ArrayList<String> zoneNamestitle = new ArrayList<String>();
	ArrayList<String> routeNamestitle = new ArrayList<String>();
	ArrayList<String> areaNamestitle = new ArrayList<String>();
	ArrayList<String> shoptypesNamestitle = new ArrayList<String>();
	ArrayList<String> orderStatusTitle = new ArrayList<String>();
	ArrayList<String> productCatogeryTitle = new ArrayList<String>();
	ArrayList<String> paymentNamestitle = new ArrayList<String>();

	String selected_shopNameId = "";
	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_ShopId = "";
	String selected_orderStatusId = "";
	String selected_productCatogeryId = "";
	String selected_paymentTermsId = "";

	boolean routeDropDownItemSeleted = false;
	boolean areaDropDownItemSeleted = false;
	int OrdersId;

	boolean zoneTouchClick = false;
	boolean routeTouchClick = false;
	boolean areaTouchClick = false;
	boolean shopNamesTouchClick = false;
	boolean orderStatusTouchClick = false;
	boolean paymentTermsTouchClick = false;

	String SPINNER_SELECTION = "";
	AutoCompleteTextView shopName_autoComplete;
	ImageView product_addiv;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_orderdetails_activity);
		try
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		catch (Exception e)
		{
		}

		//initializing the variables
		mContext = UpdateOrderDetailsActivity.this;
		orderBookActivity = UpdateOrderDetailsActivity.this;

		orderNumInvoice = (TextView) findViewById(R.id.orderNumber_invoice);

		zone_name_spinner = (Spinner) findViewById(R.id.zone_name_spinner);
		routeName_spinner = (Spinner) findViewById(R.id.routeName_spinner);
		areaName_spinner = (Spinner) findViewById(R.id.areaName_spinner);
		//shopname_spinner = (Spinner) findViewById(R.id.shopname_spinner);
		shopName_autoComplete = (AutoCompleteTextView) findViewById(R.id.shopName_autoComplete);
		order_status_spinner = (Spinner) findViewById(R.id.order_status_spinner);
		product_category_spinner = (Spinner) findViewById(R.id.product_category_spinner);
		payment_terms_spinner = (Spinner) findViewById(R.id.payment_terms_spinner);
		product_addiv = (ImageView) findViewById(R.id.product_addiv);

		tableLayout = (TableLayout) findViewById(R.id.tableLayout);
		submit = (TextView) findViewById(R.id.submit);

		paymentSelected = (TextView) findViewById(R.id.paymentSelected);
		paymentSelected.setVisibility(View.GONE);


		orderstatusDP = new ArrayList<>();
		productDP = new ArrayList<>();
		zoneDetailsDP = new ArrayList<>();
		areaDetailsDP = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();
		routeDp = new ArrayList<>();
		paymentDP_str = new ArrayList<>();
		shopNameDP_str = new ArrayList<>();
		orderstatusDP_str = new ArrayList<>();
		productDP_str = new ArrayList<>();
		zoneDetailsDP_str = new ArrayList<>();
		areaDetailsDP_str = new ArrayList<>();
		routeDetailsDP_str = new ArrayList<>();
		routeDp_str = new ArrayList<>();

		selectZoneNameBind();

		if (Utility.isOnline(mContext))
		{
			HttpAdapter.getPayment(UpdateOrderDetailsActivity.this, "payment");
			HttpAdapter.getOrderStatus(UpdateOrderDetailsActivity.this, "orderStatus");
			HttpAdapter.getZoneDetailsDP(UpdateOrderDetailsActivity.this, "zoneName");
			HttpAdapter.getProductCategoryDP(UpdateOrderDetailsActivity.this, "productCategoryName");
		}
		else
		{
			Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
		}

		zone_name_spinner.setOnItemSelectedListener(this);
		routeName_spinner.setOnItemSelectedListener(this);
		areaName_spinner.setOnItemSelectedListener(this);
//		shopname_spinner.setOnItemSelectedListener(this);
		order_status_spinner.setOnItemSelectedListener(this);
		//product_category_spinner.setOnItemSelectedListener(this);
		payment_terms_spinner.setOnItemSelectedListener(this);

		zone_name_spinner.setOnTouchListener(this);
		routeName_spinner.setOnTouchListener(this);
		areaName_spinner.setOnTouchListener(this);
		shopName_autoComplete.setOnTouchListener(this);
		order_status_spinner.setOnTouchListener(this);
//		product_category_spinner.setOnTouchListener(this);
		payment_terms_spinner.setOnTouchListener(this);


		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (validationEntryData())
				{
					dataSubmittingInServer();
				}
			}
		});

		product_addiv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				product_category_spinner.performClick();
			}
		});
/*
		zone_name_spinner.setOnItemSelectedListener(this);
		routeName_spinner.setOnItemSelectedListener(this);
		areaName_spinner.setOnItemSelectedListener(this);
//		shopname_spinner.setOnItemSelectedListener(this);
		order_status_spinner.setOnItemSelectedListener(this);
		product_category_spinner.setOnItemSelectedListener(this);
		payment_terms_spinner.setOnItemSelectedListener(this);*/


	}

	private void editOrderDetailsaccess(final String editBookListData)
	{
		try
		{
			zoneTouchClick = false;
			routeTouchClick = false;
			areaTouchClick = false;
			shopNamesTouchClick = false;
			orderStatusTouchClick = false;
			paymentTermsTouchClick = false;

			JSONObject editDatajsonObj = new JSONObject(editBookListData);
			String OrderNumber = editDatajsonObj.getString("OrderNumber");
			if (OrderNumber != null && !OrderNumber.equalsIgnoreCase("null"))
			{
				Log.e("OrderNumber", "#" + OrderNumber);
				orderNumInvoice.setText(OrderNumber);
				HttpAdapter.orderSummryProductCategory(UpdateOrderDetailsActivity.this, "productCategoryItems", OrderNumber);
			}
			String OrderDate = editDatajsonObj.getString("OrderDate");
			AvailShopName = editDatajsonObj.getString("ShopName");
			int NoOfProducts = editDatajsonObj.getInt("NoOfProducts");
			int SubTotalAmount = editDatajsonObj.getInt("SubTotalAmount");
			double TaxAmount = editDatajsonObj.getDouble("TaxAmount");
			double TotalAmount = editDatajsonObj.getDouble("TotalAmount");

			String OrderDeliveryDatestr = editDatajsonObj.getString("OrderDeliveryDate");
			if (OrderDeliveryDatestr != null && !OrderDeliveryDatestr.isEmpty())
			{
				OrderDeliveryDate = OrderDeliveryDatestr;
			}
			OrdersId = editDatajsonObj.getInt("OrdersId");
			String Status = editDatajsonObj.getString("Status");


			selected_zoneId = String.valueOf(editDatajsonObj.getInt("ZoneId"));
			selected_roueId = String.valueOf(editDatajsonObj.getInt("RouteId"));
			selected_areaNameId = String.valueOf(editDatajsonObj.getInt("AreaId"));
			selected_ShopId = String.valueOf(editDatajsonObj.getInt("ShopId"));
			selected_orderStatusId = String.valueOf(editDatajsonObj.getInt("OrderStatusId"));
			selected_paymentTermsId = editDatajsonObj.getString("PaymentTermsId");

			if (selected_zoneId != null && !selected_zoneId.isEmpty())
			{
				zone_name_spinner.setSelection(getIndex(zone_name_spinner, Integer.parseInt(selected_zoneId), _zoneNamesData), false);
				HttpAdapter.getRouteDetails(UpdateOrderDetailsActivity.this, "routeName", selected_zoneId);
			}


			if (selected_orderStatusId != null && !selected_orderStatusId.isEmpty())
			{
				order_status_spinner.setSelection(getIndex(order_status_spinner, Integer.parseInt(selected_orderStatusId), _orderStatusData), false);
			}

			if (selected_paymentTermsId != null && !selected_paymentTermsId.equals("0") && !selected_paymentTermsId.equalsIgnoreCase("null"))
			{
//				selected_paymentTermsId = "2";
				if (_paymentsSelectData.size() > 0)
				{
					payment_terms_spinner.setSelection(getIndexPositionPayment(payment_terms_spinner, Integer.parseInt(selected_paymentTermsId), _paymentsSelectData), false);
				}
			}
			else
			{
				paymentTermsTouchClick = true;
				selected_paymentTermsId = "2";
				if (_paymentsSelectData.size() > 0)
				{
					payment_terms_spinner.setSelection(getIndexPositionPayment(payment_terms_spinner, Integer.parseInt(selected_paymentTermsId), _paymentsSelectData), true);
				}
			}
		}
		catch (Exception e)
		{

		}
	}

	/*private void headers()
	{
		android.widget.TableRow row = new android.widget.TableRow(this);

		TextView taskdate = new TextView(UpdateOrderDetailsActivity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Prod");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(UpdateOrderDetailsActivity.this);
		title.setText("Prc");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                               android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(UpdateOrderDetailsActivity.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(UpdateOrderDetailsActivity.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(UpdateOrderDetailsActivity.this);
		description.setText("VAT");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                     android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(UpdateOrderDetailsActivity.this);
		description2.setText("GST");
		description2.setBackgroundColor(getResources().getColor(R.color.light_green));
		description2.setTextSize(15);
		description2.setPadding(10, 10, 10, 10);
		description2.setVisibility(View.GONE);
		row.addView(description2);
		description2.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));


		tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
		                                                      TableLayout.LayoutParams.WRAP_CONTENT));

	}*/

	private void headers()
	{
		android.widget.TableRow row = new android.widget.TableRow(this);

		TextView taskdate = new TextView(UpdateOrderDetailsActivity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Product");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(UpdateOrderDetailsActivity.this);
		title.setText("Price");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                               android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(UpdateOrderDetailsActivity.this);
		taskhour.setText("Quantity");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(UpdateOrderDetailsActivity.this);
		description3.setText("Frees");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView remove = new TextView(UpdateOrderDetailsActivity.this);
		remove.setText("VAT");
		remove.setBackgroundColor(getResources().getColor(R.color.light_green));
		remove.setTextSize(15);
		remove.setPadding(10, 10, 10, 10);
		row.addView(remove);
		remove.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                TableRow.LayoutParams.WRAP_CONTENT));
/*		TextView description = new TextView(Order.this);
		description.setText("VAT");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                      TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(Order.this);
		description2.setText("GST");
		description2.setBackgroundColor(getResources().getColor(R.color.light_green));
		description2.setTextSize(15);
		description2.setPadding(10, 10, 10, 10);
		description2.setVisibility(View.GONE);
		row.addView(description2);
		description2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));*/

		tableLayout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

	}

	private void displayTableView(final List<GetProductCategoryInOrderUpdate> productDP)
	{
		tableLayout.setVisibility(View.VISIBLE);
		tableLayout.removeAllViews();
		headers();
		storedProductCategories.clear();
		storedProductCategories.addAll(productDP);

		for (int i = 0; i < productDP.size(); i++)
		{
			j = i;
			UpdateOrderDetailsActivity.OrderSummary row = new UpdateOrderDetailsActivity.OrderSummary(this, productDP.get(i), i);
			tableLayout.addView(row, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}


	private void dataSubmittingInServer()
	{
		String paymentSelectedStr = "";
		String CreditDays = "";
		String chequeDate = "";
		JSONArray cartItemsArray = new JSONArray();
		if (storedProductCategories != null && !storedProductCategories.isEmpty())
		{
			Log.d("Order", "Stored_Products" + " : " + storedProductCategories.toString());
			for (GetProductCategoryInOrderUpdate getProducts : storedProductCategories)
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
		Log.e("Ordering Date", OrderDeliveryDate + "");

		String EmployeeId = "";
		String employeeId = getStringPreference(mContext, "EmployeeId");
		if (employeeId != null && !employeeId.isEmpty())
		{
			EmployeeId = employeeId;
		}
		try
		{
			paymentSelectedStr = SharedPrefsUtil.getStringPreference(mContext, "paymentSelected");
			if (paymentSelectedStr != null && !paymentSelectedStr.isEmpty() && !paymentSelectedStr.equalsIgnoreCase("null"))
			{

				if (paymentSelectedStr.equalsIgnoreCase("Credit-days"))
				{
					CreditDays = paymentSelected.getText().toString();
					chequeDate = "";
				}
				else if (paymentSelectedStr.equalsIgnoreCase("Days to Cheque"))
				{
					CreditDays = paymentSelected.getText().toString();
					chequeDate = "";
				}
				else if (paymentSelectedStr.equalsIgnoreCase("Cheque"))
				{
					CreditDays = "0";
					chequeDate = paymentSelected.getText().toString();
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}

		String jsonString = createJsonOrderSubmit(String.valueOf(OrdersId), selected_zoneId, selected_roueId,
		                                          selected_areaNameId, selected_ShopId,
		                                          selected_paymentTermsId, OrderDeliveryDate,
		                                          CreditDays, chequeDate, selected_orderStatusId,
		                                          EmployeeId, cartItemsArray);
		Log.e("parameters", jsonString + "");
		HttpAdapter.updateOrderBooking(this, "updateorderbook", jsonString);
	}


	@Override
	public void operationCompleted(NetworkResponse response)
	{
		Common.disMissDialog();
		Log.e("outPutResponse", response.getStatusCode() + "");
		if (response.getStatusCode() == 200)
		{
			try
			{
				JSONObject mJson = new JSONObject(response.getResponseString());
				//ZoneDetails DropDown
				if (response.getTag().equals("zoneName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						zoneSpinnerAdapter(jsonArray);
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
				//AreaDetails DropDown
				else if (response.getTag().equals("areaNameDP"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						areaNameSpinnerAdapter(jsonArray);
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
				//productCategory DropDown
				else if (response.getTag().equals("productCategoryName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						productDP_str.add("Product Category Name");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							//GetProductCategoryInOrderUpdate getProductCategory = new Gson().fromJson(obj.toString(), GetProductCategoryInOrderUpdate.class);
							GetProductCategoryInOrderUpdate getProductCategory = new Gson().fromJson(obj.toString(), GetProductCategoryInOrderUpdate.class);
							productDP.add(getProductCategory);
							productDP_str.add(getProductCategory.ProductName);
						}

						//Product category adapter
						ArrayAdapter<String> dataAdapter_productName = new ArrayAdapter<String>(this,
						                                                                        android.R.layout.simple_spinner_item, productDP_str);
						dataAdapter_productName.setDropDownViewResource(R.layout.list_item);
						product_category_spinner.setAdapter(dataAdapter_productName);

						product_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, final int positionvalue, long id)
							{
								try
								{
									if (positionvalue != 0)
									{
										list.add(productDP.get(positionvalue - 1));
										displayTableView(list);
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
					else
					{
						productDP_str.add("Add Product");
						ArrayAdapter<String> dataAdapter_productName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productDP_str);
						dataAdapter_productName.setDropDownViewResource(R.layout.list_item);
						product_category_spinner.setAdapter(dataAdapter_productName);
					}
				}
				//Payment Terms Name Dropdown
				else if (response.getTag().equals("payment"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						paymentNamesSpinnerAdapter(jsonArray);
					}
				}
				//Product Grid List Data
				else if (response.getTag().equals("productCategoryItems"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{

						JSONArray jsonArray = mJson.getJSONArray("Data");
						Log.e("responseEditOrder", jsonArray.toString() + "Success");
//						areaNameSpinnerAdapter(jsonArray);
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject objresp = jsonArray.getJSONObject(i);
							Log.e("objData", objresp.toString());
							JSONObject jsonObject = new JSONObject();
							jsonObject.putOpt("ProductId", objresp.getInt("ProductId"));
							jsonObject.putOpt("ProductName", objresp.getString("ProductName"));
							jsonObject.putOpt("ProductPrice", objresp.getInt("ProductPrice"));
							jsonObject.putOpt("VAT", objresp.getDouble("VAT"));
							jsonObject.putOpt("GST", objresp.getInt("GST"));
							jsonObject.putOpt("Quantity", objresp.getInt("Quantity"));
							jsonObject.putOpt("Frees", objresp.getInt("Frees"));
							Log.e("objDataAfterCon", jsonObject.toString());

							GetProductCategoryInOrderUpdate getProductCategory = new Gson().fromJson(jsonObject.toString(), GetProductCategoryInOrderUpdate.class);
							productDP.add(getProductCategory);
							list.add(getProductCategory);
							displayTableView(list);
						}
					}
				}
				//Update Order Save
				else if (response.getTag().equals("updateorderbook"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Updated.", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}
					else
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "Update Failed..", Toast.LENGTH_SHORT).show();
						Intent in = new Intent(UpdateOrderDetailsActivity.this, DashboardActivity.class);
						Util.killupdateorderBook();
						startActivity(in);
					}
				}
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void autoFillDetails()
	{
		try
		{
			String editBookListData = SharedPrefsUtil.getStringPreference(mContext, "EDIT_ORDER_DATA_STRING");
			Log.e("editOrderData", editBookListData);
			if (editBookListData != null && !editBookListData.isEmpty())
			{
				editOrderDetailsaccess(editBookListData);
			}
			else
			{

			}
		}
		catch (Exception e)
		{

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
		Intent intent = new Intent(this, OrderBookList.class);
		startActivity(intent);
		finish();
	}

	private String createJsonOrderSubmit(String OrderId, String ZoneId, String RouteId,
	                                     String AreaId, String ShopId, String PaymentTermsId,
	                                     String OrderDeliveryDate,
	                                     String creditDays, String chequeDate, String OrderStatusId,
	                                     String EmployeeId, JSONArray cartItemsArray
	)
	{
		/*JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("ZoneId", ZoneId);
			dataObj.putOpt("RouteId", RouteId);
			dataObj.putOpt("AreaId", AreaId);
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("OrderDeliveryDate", OrderDeliveryDate);
			dataObj.putOpt("OrderStatusId", OrderStatusId);
			dataObj.putOpt("EmployeeId", EmployeeId);
			dataObj.putOpt("OrderId", OrderId);
			dataObj.putOpt("OrderNumber", orderNumInvoice.getText().toString());
			studentsObj.put("ProductList", cartItemsArray);
			studentsObj.put("OrderBookingDate", dataObj);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("orderjson", studentsObj.toString());
		return studentsObj.toString();*/


		JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("ZoneId", ZoneId);
			dataObj.putOpt("RouteId", RouteId);
			dataObj.putOpt("AreaId", AreaId);
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("OrderDeliveryDate", OrderDeliveryDate);
			dataObj.putOpt("OrderStatusId", OrderStatusId);
			dataObj.putOpt("PaymentTermsId", PaymentTermsId); //PaymentTermsId // Added New param for paymnet tersms
			dataObj.putOpt("EmployeeId", EmployeeId);
			dataObj.putOpt("OrderId", OrderId);


			if (chequeDate != null && !chequeDate.isEmpty())
			{
				dataObj.putOpt("PaymentDateCheque", chequeDate);
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
				dataObj.putOpt("CreditDays", "0");

			}

			dataObj.putOpt("OrderNumber", orderNumInvoice.getText().toString());
			studentsObj.put("ProductList", cartItemsArray);
			studentsObj.put("OrderBookingDate", dataObj);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("orderjson", studentsObj.toString());
		return studentsObj.toString();
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{

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
				if (shopNamesTouchClick)
				{
					selectedSpinner = "SHOP";
					dropDownValueSelection(position, _shoptypesData, selectedSpinner);
				}
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

	private void dropDownValueSelection(int position, ArrayList<ShopNamesData> _dropDownData, String selectedSpinner)
	{
		if (position != 0)
		{
			if (selectedSpinner.equals("ZONE"))
			{
				selected_zoneId = _dropDownData.get(position - 1).getShopId();
				HttpAdapter.getRouteDetails(UpdateOrderDetailsActivity.this, "routeName", selected_zoneId);
			}
			else if (selectedSpinner.equals("ROUTE"))
			{
				selected_roueId = _dropDownData.get(position - 1).getShopId(); //3
				HttpAdapter.getAreaDetailsByRoute(UpdateOrderDetailsActivity.this, "areaNameDP", selected_roueId);
			}
			else if (selectedSpinner.equals("AREA"))
			{
				selected_areaNameId = _dropDownData.get(position - 1).getShopId();
				HttpAdapter.getShopDetailsDP(UpdateOrderDetailsActivity.this, "shopName", selected_areaNameId);
			}
			/*else if (selectedSpinner.equals("SHOP"))
			{
				selected_ShopId = _dropDownData.get(position - 1).getShopId();
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

	@Override
	public void onNothingSelected(final AdapterView<?> parent)
	{

	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event)
	{
		switch (v.getId())
		{
			case R.id.zone_name_spinner:
				zoneTouchClick = true;
				routeTouchClick = false;
				areaTouchClick = false;
				shopNamesTouchClick = false;
				break;
			case R.id.routeName_spinner:
				zoneTouchClick = false;
				routeTouchClick = true;
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

	private class OrderSummary extends android.widget.TableRow
	{

		private Context mContext;
		private GetProductCategoryInOrderUpdate mProductCategory;

		private EditText quantityETID;
		private EditText fresETID;

		private String afterTextChanged = "";
		private String beforeTextChanged = "";
		private String onTextChanged = "";

		private final int position;

		public OrderSummary(final Context context, final GetProductCategoryInOrderUpdate productCategory, int index)
		{
			super(context);
			mContext = context;
			mProductCategory = productCategory;
			position = index;
			init();
		}

		public GetProductCategoryInOrderUpdate getProductCategory()
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
				title.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
				title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                       LayoutParams.WRAP_CONTENT));
				addView(title);

				quantityETID = new EditText(mContext);
				quantityETID.setText(String.valueOf(mProductCategory.Quantity));
				quantityETID.setBackgroundColor(Color.TRANSPARENT);
				quantityETID.setClickable(true);
				quantityETID.setCursorVisible(true);
				quantityETID.setFocusableInTouchMode(true);
				quantityETID.setTextSize(15);
//				quantityETID.setEnabled(false);
				quantityETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				quantityETID.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
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
				if (String.valueOf(mProductCategory.Frees) != null && !String.valueOf(mProductCategory.Frees).isEmpty())
				{
					fresETID.setText(String.valueOf(mProductCategory.Frees));
				}
				else
				{
					fresETID.setText("-");
				}
				fresETID.setBackgroundColor(Color.TRANSPARENT);
				fresETID.setClickable(true);
				fresETID.setCursorVisible(true);
				fresETID.setFocusableInTouchMode(true);
				fresETID.setTextSize(15);
//				fresETID.setEnabled(false);
				fresETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				fresETID.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
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
				description2.setText(String.valueOf(mProductCategory.GST));
				description2.setTextSize(15);
				description2.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
				description2.setVisibility(GONE);
				description2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				addView(description2);

				ImageView deleteimg = new ImageView(mContext);
				/*deleteimg.setImageResource(R.drawable.delete);
				deleteimg.setMaxWidth(28);
				deleteimg.setMaxHeight(28);*/
				deleteimg.setPadding(0, 10, 0, 0);
				deleteimg.setImageResource(R.drawable.deleteiconimg);
				deleteimg.setMaxWidth(25);
				deleteimg.setMaxHeight(25);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					deleteimg.setForegroundGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
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
							android.widget.TableRow row = (android.widget.TableRow) tableLayout.getChildAt(temposition);
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
				});

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
//				afterTextChanged = String.valueOf(Integer.parseInt(s.toString()));

				try
				{
					String quantity = s.toString();
					Log.e("quantity", quantity);
					if (!quantity.isEmpty() && quantity != null)
					{
						storedProductCategories.get(position).setQuantity(Integer.parseInt(quantity));
					}
				}
				catch (Exception e)
				{

				}

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
			Toast.makeText(getApplicationContext(), "Please Enter Area Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (selected_ShopId == null || selected_ShopId.isEmpty() || selected_ShopId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Shop Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
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
					Intent in = new Intent(UpdateOrderDetailsActivity.this, DashboardActivity.class);
					Util.killorderBook();
					startActivity(in);
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
					Intent in = new Intent(UpdateOrderDetailsActivity.this, Order.class);
					Util.killorderBook();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(UpdateOrderDetailsActivity.this, Invoice.class);
					Util.killorderBook();
					startActivity(inten);
				}
				else if (check3)
				{
					Intent inten = new Intent(UpdateOrderDetailsActivity.this, ViewListActivity.class);
					Util.killupdateorderBook();
					startActivity(inten);
				}
				else
				{
					Toast.makeText(mContext, "Please select a value from the list", Toast.LENGTH_SHORT).show();
				}

			}
		});

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
			DialogFragment newFragment = new UpdateOrderDetailsActivity.DatePickerFragmentDailog();
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
					selected_paymentTermsId = "";
					payment_terms_spinner.setSelection(0);
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
					paymentSelected.setText(daysCredits + " Days" + "");
					paymentSelected.setVisibility(View.VISIBLE);
				}
				promoDialog.dismiss();
				Util.hideSoftKeyboard(mContext, v);
			}
		});
	}

	private void daysAccess()
	{
		promoDialog.show();
		paymentSelected.setText("");
		creditDaysLayout.setVisibility(View.VISIBLE);
		dateselect.setVisibility(View.GONE);
		dateaccept.setVisibility(View.GONE);
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
			paymentSelected.setText("");
			paymentSelected.setVisibility(View.VISIBLE);
			paymentSelected.setText(sectiondate);
			SharedPrefsUtil.setStringPreference(getContext(), "SelectedDate", sectiondate);

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
			routeName_spinner.setSelection(getIndex(routeName_spinner, Integer.parseInt(selected_roueId), _routeCodesData), false);
			HttpAdapter.getAreaDetailsByRoute(UpdateOrderDetailsActivity.this, "areaNameDP", selected_roueId);
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
			areaName_spinner.setAdapter(dataAdapter_areaName);

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
			areaName_spinner.setSelection(getIndex(areaName_spinner, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
			HttpAdapter.getShopDetailsDP(UpdateOrderDetailsActivity.this, "shopName", selected_areaNameId);
		}
	}

	private void shopNameSpinnerAdapter(final JSONArray jsonArray)
	{
		/*SPINNER_SELECTION = "SHOP";
		adapterDataAssigingToSpinner(shoptypesNamestitle, SPINNER_SELECTION);*/
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
			Log.e("shopDropdown", jsonArray.toString() + "");
			try
			{
				_shoptypesData.clear();
				shoptypesNamestitle.clear();
				_shoptypesData = new ArrayList<ShopNamesData>();
				for (int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsnobj = jsonArray.getJSONObject(i);
					int shopId = jsnobj.getInt("ShopId");
					Log.e("ShopIdList", String.valueOf(shopId));
					String shopNamee = jsnobj.getString("ShopName");
					_shoptypesData.add(new ShopNamesData(String.valueOf(shopId), shopNamee));
				}
//			shoptypesNamestitle.add("Select Shop Name");
				if (_shoptypesData.size() > 0)
				{
					for (int i = 0; i < _shoptypesData.size(); i++)
					{
						shoptypesNamestitle.add(_shoptypesData.get(i).getShopName());
					}
				}
			}
			catch (Exception e)
			{
			}
			shopDataBinding(shoptypesNamestitle);
		}
		shopName_autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
			{
				try
				{
					if (shoptypesNamestitle.size() != 0)
					{
						String selectedName = shopName_autoComplete.getText().toString();
						Log.e("entryShopName", selectedName);
						for (int i = 0; i < _shoptypesData.size(); i++)
						{
							String availName = _shoptypesData.get(i).getShopName();
							if (availName.equals(selectedName))
							{
								selected_ShopId = _shoptypesData.get(i).getShopId();
								Log.e("selected_ShopId", selected_ShopId + "");
								break;
							}
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
//			paymentNamestitle.add("Select Payment Terms Name");
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
		SPINNER_SELECTION = "PAYMENT_SELECT";
		adapterDataAssigingToSpinner(paymentNamestitle, SPINNER_SELECTION);
	}

	//By Default Bind Selection Drop downs
	private void selectZoneNameBind()
	{
		zoneDetailsDP_str.add("Select Zone No");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zoneDetailsDP_str);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		routeName_spinner.setAdapter(dataAdapter_areaName);
		selectRouteNameBind();
	}

	private void selectRouteNameBind()
	{
		routenostitle.add("Select Route No");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routenostitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		routeName_spinner.setAdapter(dataAdapter_areaName);
		selectAreaNameBind();
	}

	private void selectAreaNameBind()
	{
		_shoptypesData.clear();
		shoptypesNamestitle.clear();
		_shoptypesData = new ArrayList<ShopNamesData>();
		areaDetailsDP_str.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaDetailsDP_str);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		areaName_spinner.setAdapter(dataAdapter_areaName);
		//selectShopNameBind();
	}

	/*private void selectShopNameBind()
	{
		shopNameDP_str.add("Select Shop Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shopNameDP_str);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		shopname_spinner.setAdapter(dataAdapter_areaName);
	}*/

	////////
	private int getIndex(Spinner spinner, int searchId, ArrayList<ShopNamesData> _availbleDropDownData)
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

	private int getIndexPositionPayment(Spinner spinner, int searchId, ArrayList<ShopNamesData> _availbleDropDownData)
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
				for (int i = 1; i < spinner.getCount(); i++)
				{
					String avaliableListDataid = _availbleDropDownData.get(i).getShopId();
					if (avaliableListDataid.equals(String.valueOf(searchId)))
					{
						searchIdIndex = i;
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


	private void adapterDataAssigingToSpinner(ArrayList<String> spinnerTitles, String spinnerSelction)
	{
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTitles);
		dataAdapter.setDropDownViewResource(R.layout.list_item);
		if (spinnerSelction.equals("ZONE"))
		{
			zone_name_spinner.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("ROUTE"))
		{
			routeName_spinner.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("AREA"))
		{
			areaName_spinner.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("SHOP"))
		{
			//shopname_spinner.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("ORDER_STATUS"))
		{
			order_status_spinner.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("PAYMENT_SELECT"))
		{
			payment_terms_spinner.setAdapter(dataAdapter);
		}


	}
}


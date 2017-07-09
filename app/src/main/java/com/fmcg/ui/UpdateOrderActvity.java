package com.fmcg.ui;

/**
 * Created by Shiva on 6/26/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.TableRow;

import com.fmcg.Dotsoft.BuildConfig;
import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.GetAreaDetails;
import com.fmcg.models.GetProductCategoryInOrderUpdate;
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
import com.fmcg.permission.DangerousPermResponseCallBack;
import com.fmcg.permission.DangerousPermissionResponse;
import com.fmcg.permission.DangerousPermissionUtils;
import com.fmcg.util.DateUtil;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.fmcg.util.Utility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.fmcg.util.SharedPrefsUtil.getStringPreference;


public class UpdateOrderActvity extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	public static Activity orderBookActivity;
	public SharedPreferences sharedPreferences;
	Dialog alertDialog;
	public List<PaymentDropDown> paymentDP;
	public List<GetShopDetailsDP> shopsDP;
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

	public String routeCode;


	public Spinner shopName_sp, orderStatus_sp, category_sp, payment_sp, routeName_sp, areaName_sp, zone_sp;
	public CheckBox isShopClosed, ordered, invoice;
	public TextView uploadImage, shopClosed, orderDate, submit, tvDisplayDate, orderNumInvoice;
	private static TextView paymentSelected;
	private LinearLayout list_li;
	private TableLayout tableLayout;
	int j;
	Context mContext;
	ArrayList<String> routenostitle = new ArrayList<String>();
	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	Button alert_submit;
	boolean check1 = false;
	boolean check2 = false;


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
	String selected_paymentNameId = "";
	EditText availZonenametxt, availRoutetxt, availAreatxt, availShopnametxt;

	boolean routeDropDownItemSeleted = false;
	boolean areaDropDownItemSeleted = false;
	int OrdersId;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_order_activity);
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		try
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		catch (Exception e)
		{
		}

		//initializing the variables
		mContext = UpdateOrderActvity.this;
		orderBookActivity = UpdateOrderActvity.this;


		tableLayout = (TableLayout) findViewById(R.id.tableLayout);
		orderStatus_sp = (Spinner) findViewById(R.id.order_type_dp);
		category_sp = (Spinner) findViewById(R.id.product_category);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_name);
		shopName_sp = (Spinner) findViewById(R.id.shopname_spinner);
		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);
		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);

		isShopClosed = (CheckBox) findViewById(R.id.isClosed);
		ordered = (CheckBox) findViewById(R.id.isOrder);
		invoice = (CheckBox) findViewById(R.id.isInvoice);
		orderNumInvoice = (TextView) findViewById(R.id.orderNumber_invoice);
		submit = (TextView) findViewById(R.id.submit);

		paymentSelected = (TextView) findViewById(R.id.paymentSelected);
		paymentSelected.setVisibility(View.GONE);

		list_li = (LinearLayout) findViewById(R.id.items_li);

		availZonenametxt = (EditText) findViewById(R.id.availZonenametxt);
		availRoutetxt = (EditText) findViewById(R.id.availRoutetxt);
		availAreatxt = (EditText) findViewById(R.id.availAreatxt);
		availShopnametxt = (EditText) findViewById(R.id.availShopnametxt);

		paymentDP = new ArrayList<>();
		shopsDP = new ArrayList<>();
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
			HttpAdapter.getPayment(UpdateOrderActvity.this, "payment");
			HttpAdapter.getOrderStatus(UpdateOrderActvity.this, "orderStatus");
			HttpAdapter.getZoneDetailsDP(UpdateOrderActvity.this, "zoneName");
			HttpAdapter.getProductCategoryDP(UpdateOrderActvity.this, "productCategoryName");
		}
		else
		{
			Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
		}


		availRoutetxt.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{

				availRoutetxt.setVisibility(View.GONE);
				routeName_sp.hasFocusable();
				routeName_sp.performClick();
				routeName_sp.setVisibility(View.VISIBLE);

				return false;
			}
		});

		availAreatxt.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{

				availAreatxt.setVisibility(View.GONE);
				areaName_sp.hasFocusable();
				areaName_sp.performClick();
				areaName_sp.setVisibility(View.VISIBLE);

				return false;
			}
		});

		availShopnametxt.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, final MotionEvent event)
			{

				availShopnametxt.setVisibility(View.GONE);
				shopName_sp.hasFocusable();
				shopName_sp.performClick();
				shopName_sp.setVisibility(View.VISIBLE);
				return false;
			}
		});

		list_li.setOnClickListener(this);
		submit.setOnClickListener(this);

	}

	private void editOrderDetailsaccess(final String editBookListData)
	{
		try
		{
			JSONObject editDatajsonObj = new JSONObject(editBookListData);
			String OrderNumber = editDatajsonObj.getString("OrderNumber");
			if (OrderNumber != null && !OrderNumber.equalsIgnoreCase("null"))
			{
				Log.e("OrderNumber", "#" + OrderNumber);
				orderNumInvoice.setText(OrderNumber);
				HttpAdapter.orderSummryProductCategory(UpdateOrderActvity.this, "productCategoryItems", OrderNumber);
			}
			String OrderDate = editDatajsonObj.getString("OrderDate");
			String ShopName = editDatajsonObj.getString("ShopName");
			int NoOfProducts = editDatajsonObj.getInt("NoOfProducts");
			int SubTotalAmount = editDatajsonObj.getInt("SubTotalAmount");
			double TaxAmount = editDatajsonObj.getDouble("TaxAmount");
			double TotalAmount = editDatajsonObj.getDouble("TotalAmount");

			String OrderDeliveryDatestr = editDatajsonObj.getString("OrderDeliveryDate");
			if (OrderDeliveryDatestr != null && !OrderDeliveryDatestr.isEmpty())
			{
				OrderDeliveryDate = OrderDeliveryDatestr;
			}


			int zoneId = editDatajsonObj.getInt("ZoneId");
			int RouteId = editDatajsonObj.getInt("RouteId");
			int AreaId = editDatajsonObj.getInt("AreaId");
			int ShopId = editDatajsonObj.getInt("ShopId");


			int OrderStatusId = editDatajsonObj.getInt("OrderStatusId");
			String Status = editDatajsonObj.getString("Status");

			OrdersId = editDatajsonObj.getInt("OrdersId");

			String PaymentTermsId = editDatajsonObj.getString("PaymentTermsId");


			selected_zoneId = String.valueOf(zoneId);
			selected_roueId = String.valueOf(RouteId);
			selected_areaNameId = String.valueOf(AreaId);
			selected_ShopId = String.valueOf(ShopId);

			placingAvailZoneName(Integer.parseInt(selected_zoneId));

			if (OrderStatusId != 0)
			{
				selected_orderStatusId = String.valueOf(OrderStatusId);
				orderStatus_sp.setSelection(OrderStatusId);
			}

			if (PaymentTermsId != null && !PaymentTermsId.equals("0") && !PaymentTermsId.equalsIgnoreCase("null"))
			{
				if (_paymentsSelectData.size() > 0)
				{
					payment_sp.setSelection(getIndex(payment_sp, Integer.parseInt(PaymentTermsId), _paymentsSelectData));
				}
			}
		}
		catch (Exception e)
		{

		}


	}

	private void placingAvailZoneName(final int zoneId)
	{
		String zoneName = "";
		if (zoneId != 0)
		{
			//zone_sp.setSelection(getIndex(zone_sp, zoneId, _zoneNamesData));
			selected_zoneId = String.valueOf(zoneId);

			HttpAdapter.getRouteDetails(UpdateOrderActvity.this, "routeName", selected_zoneId);

			if (_zoneNamesData.size() != 0)
			{
				for (int i = 0; i < _zoneNamesData.size(); i++)
				{
					String availZoneId = _zoneNamesData.get(i).getShopId();
					if (availZoneId.equals(selected_zoneId))
					{
						zoneName = _zoneNamesData.get(i).getShopName();
						Log.e("routeId", selected_zoneId + "");
					}
				}
			}
		}

		if (zoneName != null && !zoneName.equalsIgnoreCase("null") && !zoneName.isEmpty())
		{
			availZonenametxt.setVisibility(View.VISIBLE);
			availZonenametxt.setText(zoneName + "");
			zone_sp.setVisibility(View.GONE);
			selected_zoneId = String.valueOf(zoneId);
		}
		else
		{
			zone_sp.setVisibility(View.VISIBLE);
		}
	}

	private void placingAvailRouteNo(final int routeId)
	{
		String routeName = "";
		if (routeId != 0)
		{
			HttpAdapter.getAreaDetailsByRoute(UpdateOrderActvity.this, "areaNameDP", selected_roueId);
			if (_routeCodesData.size() != 0)
			{
				for (int i = 0; i < _routeCodesData.size(); i++)
				{
					String availRouteId = _routeCodesData.get(i).getShopId();
					if (availRouteId.equals(selected_roueId))
					{
						routeName = _routeCodesData.get(i).getShopName();
					}
				}
			}
		}

		if (routeName != null && !routeName.equalsIgnoreCase("null"))
		{
			availRoutetxt.setVisibility(View.VISIBLE);
			availRoutetxt.setText(routeName + "");
			routeCode = String.valueOf(selected_roueId);
			selected_roueId = routeCode;
			routeName_sp.setVisibility(View.GONE);

		}
		else
		{
			routeName_sp.setVisibility(View.VISIBLE);
		}
	}

	private void placingAvailAreaName(final int areaId)
	{
		String areaName = "";
		if (areaId != 0)
		{
			//	areaName_sp.setSelection(getIndex(areaName_sp, areaId, _areaNamesData));
			selected_areaNameId = String.valueOf(areaId);

			HttpAdapter.getShopDetailsDP(UpdateOrderActvity.this, "shopName", selected_areaNameId);


			if (_areaNamesData.size() != 0)
			{
				for (int i = 0; i < _areaNamesData.size(); i++)
				{
					String availAreaId = _areaNamesData.get(i).getShopId();
					if (availAreaId.equals(selected_areaNameId))
					{
						areaName = _areaNamesData.get(i).getShopName();
					}
				}
			}
		}

		if (areaName != null && !areaName.equalsIgnoreCase("null") && !areaName.isEmpty())
		{
			availAreatxt.setVisibility(View.VISIBLE);
			availAreatxt.setText(areaName + "");
			areaName_sp.setVisibility(View.GONE);
		}
		else
		{
			areaName_sp.setVisibility(View.VISIBLE);
		}
	}

	private void placingAvailShopName(final int ShopId)
	{
		String shopName = "";
		if (ShopId != 0)
		{
//			shopName_sp.setSelection(getIndex(shopName_sp, ShopId, _shoptypesData));
			selected_ShopId = String.valueOf(ShopId);//19
			if (_shoptypesData.size() != 0)
			{
				for (int i = 0; i < _shoptypesData.size(); i++)
				{
					String availShopId = _shoptypesData.get(i).getShopId();
					if (availShopId.equals(selected_ShopId))
					{
						shopName = _shoptypesData.get(i).getShopName();
					}
				}
			}
		}

		if (shopName != null && !shopName.equalsIgnoreCase("null") && !shopName.isEmpty())
		{
			availShopnametxt.setVisibility(View.VISIBLE);
			availShopnametxt.setText(shopName + "");
			shopName_sp.setVisibility(View.GONE);
		}
		else
		{
			shopName_sp.setVisibility(View.VISIBLE);
		}
	}

	private void headers()
	{
		android.widget.TableRow row = new TableRow(this);

		TextView taskdate = new TextView(UpdateOrderActvity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Prod");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(UpdateOrderActvity.this);
		title.setText("Prc");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(UpdateOrderActvity.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(UpdateOrderActvity.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(UpdateOrderActvity.this);
		description.setText("VAT");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                      TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(UpdateOrderActvity.this);
		description2.setText("GST");
		description2.setBackgroundColor(getResources().getColor(R.color.light_green));
		description2.setTextSize(15);
		description2.setPadding(10, 10, 10, 10);
		description2.setVisibility(View.GONE);
		row.addView(description2);
		description2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));


		tableLayout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
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
			UpdateOrderActvity.OrderSummary row = new UpdateOrderActvity.OrderSummary(this, productDP.get(i), i);
			tableLayout.addView(row, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.submit)
		{
			boolean validated = validationEntryData();
			if (validated)
			{
				list_li.setVisibility(View.GONE);
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

				String jsonString = createJsonOrderSubmit(String.valueOf(OrdersId), selected_zoneId, selected_roueId,
				                                          selected_areaNameId, selected_ShopId, OrderDeliveryDate,
				                                          selected_orderStatusId, EmployeeId, cartItemsArray);
				Log.e("parameters", jsonString + "");
				HttpAdapter.updateOrderBooking(this, "updateorderbook", jsonString);
			}
		}
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
				//productCategory DropDown
				else if (response.getTag().equals("productCategoryName"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						//productCategorySpinnerAdapter(jsonArray);
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
						dataAdapter_productName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						category_sp.setAdapter(dataAdapter_productName);

						category_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, final int positionvalue, long id)
							{
								try
								{
									//productCategoryId = String.valueOf(positionvalue);
									if (positionvalue != 0)
									{
//										 productNameDropDown = productDP.get(position).ProductId;
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
						Intent in = new Intent(UpdateOrderActvity.this, DashboardActivity.class);
						Util.killupdateorderBook();
						startActivity(in);
					}
				}
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
				//

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

	private void refreshActivity()
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

	private String createJsonOrderSubmit(String OrderId, String ZoneId, String RouteId,
	                                     String AreaId, String ShopId,
	                                     String OrderDeliveryDate, String OrderStatusId,
	                                     String EmployeeId, JSONArray cartItemsArray
	)
	{
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
		return studentsObj.toString();
	}
	//ZoneId, RouteId, AreaId, ShopId,OrderDeliveryDate, OrderStatusId, EmployeeId,OrderId


	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{

	}

	private class OrderSummary extends TableRow
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

		/*private void init()
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
				title.setText(String.valueOf(mProductCategory.Price));
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

			*//*TextView description3 = new TextView(mContext);
			description3.setText("-");
			description3.setTextSize(15);
			addView(description3);
			description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
			                                                       TableRow.LayoutParams.WRAP_CONTENT));*//*
				fresETID = new EditText(mContext);
				*//*if (mProductCategory.Frees != null && !mProductCategory.Frees.isEmpty())
				{*//*
				fresETID.setText(String.valueOf(mProductCategory.Frees));
//				}
				*//*else
				{
					fresETID.setText("-");
				}*//*
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

			*//*	TextView description2 = new TextView(mContext);
				description2.setText(String.valueOf(mProductCategory.SubTotalAmount));
				description2.setTextSize(15);
				description.setVisibility(View.GONE);
				description2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				addView(description2);
*//*
				ImageView deleteimg = new ImageView(mContext);
				deleteimg.setImageResource(R.drawable.delete);

				deleteimg.setMaxWidth(25);
				deleteimg.setMaxHeight(25);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					deleteimg.setForegroundGravity(Gravity.CENTER_VERTICAL);
				}
//				deleteimg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				addView(deleteimg);
				deleteimg.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						try
						{
							if (1 < storedProductCategories.size())
							{
								int temposition = position + 1;
								TableRow row = (TableRow) tableLayout.getChildAt(temposition);
								tableLayout.removeView(row);
//								productDP.remove(position - 1);
								storedProductCategories.remove(position - 1);
								list.remove(position - 1);
							}
							else
							{
								Toast.makeText(mContext, "At lease one product item must", Toast.LENGTH_SHORT).show();
							}


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
		}*/
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
				quantityETID.setClickable(true);
				quantityETID.setCursorVisible(true);
				quantityETID.setFocusableInTouchMode(true);
				quantityETID.setTextSize(15);
//				quantityETID.setEnabled(false);
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
				description2.setVisibility(GONE);
				description2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				addView(description2);

				ImageView deleteimg = new ImageView(mContext);
//				deleteimg.setImageResource(getResources().getDrawable(R.drawable.delete));
//				deleteimg.setPadding(0, 10, 0, 0);
				deleteimg.setImageResource(R.drawable.delete);

				deleteimg.setMaxWidth(28);
				deleteimg.setMaxHeight(28);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
				{
					deleteimg.setForegroundGravity(Gravity.CENTER_VERTICAL);
				}
//				deleteimg.setLayoutParams(new TableRow.LayoutParams(24,
//				                                                    TableRow.LayoutParams.WRAP_CONTENT));
				deleteimg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				deleteimg.gr
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

//						notifyDataSetChanged();
						//notifyAll();
						/*int childCount = tableLayout.getChildCount();
						// Remove all rows except the first one
						if (childCount > position)
						{
//							tableLayout.removeViews(position, childCount - position);
//							int ll = position;
							tableLayout.removeViews(0, position);
						}*/
						/*storedProductCategories.get(position).getQuantity();
						tableLayout.removeView(position);*/
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
		/*if (!cameracaptured)
		{


		}*/

		/*if (productCategoryId == null || productCategoryId.isEmpty() || productCategoryId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Product Category Name", Toast.LENGTH_SHORT).show();
				ret = false;
				return ret;
			}*/
		if (selected_paymentNameId == null || selected_paymentNameId.isEmpty() || selected_paymentNameId.equals("0"))
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
					Intent in = new Intent(UpdateOrderActvity.this, DashboardActivity.class);
					Util.killorderBook();
					startActivity(in);
//					refreshActivity();
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

					Intent in = new Intent(UpdateOrderActvity.this, Order.class);
					Util.killorderBook();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(UpdateOrderActvity.this, Invoice.class);
					Util.killorderBook();
					startActivity(inten);
				}
				else
				{
					Toast.makeText(mContext, "Please Select Order Book or Invoice", Toast.LENGTH_SHORT).show();
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
//			DialogFragment newFragment = new DatePickerFragmentDailog();
//			newFragment.show(getSupportFragmentManager(), "datePicker");

		}
		else if (type.equals("Cheque"))
		{
			DialogFragment newFragment = new UpdateOrderActvity.DatePickerFragmentDailog();
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
					selected_paymentNameId = "";
					payment_sp.setSelection(0);
//					refreshActivity();
				}
			}
		});

		alert_submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				//Days to Cheque
				/*String Days = "Days to Cheque";
				if (type.equals(Days))
				{*/
				String daysCredits = creditdays.getText().toString();
				if (daysCredits != null && !daysCredits.isEmpty())
				{
					paymentSelected.setText(daysCredits + " Days" + "");
					paymentSelected.setVisibility(View.VISIBLE);
				}
				promoDialog.dismiss();
				Util.hideSoftKeyboard(mContext, v);
//				}


			}
		});


//		datePicker.


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
			// Do something with the date chosen by the user

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

		ArrayAdapter<String> dataAdapter_zoneName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zoneNamestitle);
		dataAdapter_zoneName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zone_sp.setAdapter(dataAdapter_zoneName);

		autoFillDetails();

		zone_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					selected_zoneId = _zoneNamesData.get(position - 1).getShopId();
//					availRoutetxt.setVisibility(View.GONE);
					availAreatxt.setVisibility(View.GONE);
//					routeName_sp.setVisibility(View.VISIBLE);
					areaName_sp.setVisibility(View.VISIBLE);
					HttpAdapter.getRouteDetails(UpdateOrderActvity.this, "routeName", selected_zoneId);
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

//			_areaNamesData.clear();
//			areaNamestitle.clear();
			//selectAreaNameBind();

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
		//Routedetails adapter
		ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNamestitle);
		dataAdapter_routeName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routeName_sp.setAdapter(dataAdapter_routeName);

		if (!routeDropDownItemSeleted)
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			placingAvailRouteNo(Integer.parseInt(selected_roueId));
		}
		else
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			areaName_sp.setVisibility(View.VISIBLE);
			shopName_sp.setVisibility(View.VISIBLE);
		}


		routeName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					availRoutetxt.setVisibility(View.GONE);
					availAreatxt.setVisibility(View.GONE);
					availShopnametxt.setVisibility(View.GONE);
					routeName_sp.setVisibility(View.VISIBLE);
					areaName_sp.setVisibility(View.VISIBLE);
					shopName_sp.setVisibility(View.VISIBLE);

					routeDropDownItemSeleted = true;
					selected_roueId = _routeCodesData.get(position - 1).getShopId(); //3
					selected_areaNameId = "";
					selected_ShopId = "";
					HttpAdapter.getAreaDetailsByRoute(UpdateOrderActvity.this, "areaNameDP", selected_roueId);
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

		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);

		if (!routeDropDownItemSeleted)
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			placingAvailAreaName(Integer.parseInt(selected_areaNameId));
		}
		else
		{
			Log.e("routeDropDownItem", routeDropDownItemSeleted + "");
			areaName_sp.setVisibility(View.VISIBLE);
			shopName_sp.setVisibility(View.VISIBLE);
		}


		areaName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
//					availRoutetxt.setVisibility(View.GONE);
					availAreatxt.setVisibility(View.GONE);
					availShopnametxt.setVisibility(View.GONE);
					shopName_sp.setVisibility(View.VISIBLE);
					areaDropDownItemSeleted = true;
					selected_areaNameId = _areaNamesData.get(position - 1).getShopId();
					selected_ShopId = "";

					HttpAdapter.getShopDetailsDP(UpdateOrderActvity.this, "shopName", selected_areaNameId);
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
			shoptypesNamestitle.add("Select Shop Name");
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
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this,
		                                                                     android.R.layout.simple_spinner_item,
		                                                                     shoptypesNamestitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopName_sp.setAdapter(dataAdapter_shopType);

		if (!areaDropDownItemSeleted)
		{
			placingAvailShopName(Integer.parseInt(selected_ShopId));
		}
		else
		{
			shopName_sp.setVisibility(View.VISIBLE);
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
						selected_ShopId = _shoptypesData.get(position - 1).getShopId();
						//HttpAdapter.shopEditDetails(UpdateOrderActvity.this, "editShopDetails", selected_ShopId);
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
					if (position != 0)
					{
						selected_orderStatusId = _orderStatusData.get(position - 1).getShopId();
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


	private void productCategorySpinnerAdapter(final JSONArray jsonArray)
	{
		Log.e("shopDropdown", jsonArray.toString() + "");
		try
		{
			_productCategoryData.clear();
			productCatogeryTitle.clear();
			_productCategoryData = new ArrayList<ShopNamesData>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsnobj = jsonArray.getJSONObject(i);
				int shopId = jsnobj.getInt("ProductId");
				String shopNamee = jsnobj.getString("ProductName");
				_productCategoryData.add(new ShopNamesData(String.valueOf(shopId), shopNamee));
			}
			productCatogeryTitle.add("Select Product Category");
			if (_productCategoryData.size() > 0)
			{
				for (int i = 0; i < _productCategoryData.size(); i++)
				{
					productCatogeryTitle.add(_productCategoryData.get(i).getShopName());
				}
			}
		}
		catch (Exception e)
		{
		}
		ArrayAdapter<String> dataAdapter_shopType = new ArrayAdapter<String>(this,
		                                                                     android.R.layout.simple_spinner_item,
		                                                                     productCatogeryTitle);
		dataAdapter_shopType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		orderStatus_sp.setAdapter(dataAdapter_shopType);

		/*if (!areaDropDownItemSeleted)
		{
			placingAvailShopName(Integer.parseInt(selected_shopId));
		}*/

/*
		orderStatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
				{
					if (position != 0)
					{
						selected_productCatogeryId = _productCategoryData.get(position - 1).getShopId();
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
		});*/
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

		/*if (!areaDropDownItemSeleted)
		{
			placingAvailShopName(Integer.parseInt(selected_shopId));
		}*/


		payment_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				try
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

	//By Default Bind Selection Drop downs
	private void selectZoneNameBind()
	{
		zoneDetailsDP_str.add("Select Zone No");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zoneDetailsDP_str);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routeName_sp.setAdapter(dataAdapter_areaName);
		selectRouteNameBind();
	}

	private void selectRouteNameBind()
	{
		routenostitle.add("Select Route No");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routenostitle);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routeName_sp.setAdapter(dataAdapter_areaName);
		selectAreaNameBind();
	}

	private void selectAreaNameBind()
	{
		areaDetailsDP_str.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaDetailsDP_str);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
		selectShopNameBind();
	}

	private void selectShopNameBind()
	{
		shopNameDP_str.add("Select Shop Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shopNameDP_str);
		dataAdapter_areaName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		shopName_sp.setAdapter(dataAdapter_areaName);
	}

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
}



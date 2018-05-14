package com.fmcg.Activity.OrderAndBillingActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.fmcg.Activity.HomeActivity.DashboardActivity;
import com.fmcg.Activity.UpdateOrderAndBilling.BillingEditActivity;
import com.fmcg.Dotsoft.BuildConfig;
import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.GetAreaDetails;
import com.fmcg.models.GetProductCategory;
import com.fmcg.models.GetRouteDetails;
import com.fmcg.models.GetZoneDetails;
import com.fmcg.models.ShopNamesData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.permission.DangerousPermResponseCallBack;
import com.fmcg.permission.DangerousPermissionResponse;
import com.fmcg.permission.DangerousPermissionUtils;
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

import static com.fmcg.util.Common.orderNUmberString;


public class OrderBookingActivity extends AppCompatActivity implements NetworkOperationListener, AdapterView.OnItemSelectedListener
{
	public static Activity orderBookActivity;
	public List<GetProductCategory> productDP;
	public List<GetProductCategory> storedProductCategories = new ArrayList<GetProductCategory>();
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetAreaDetails> areaDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	public final List<GetProductCategory> list = new ArrayList<GetProductCategory>();
	private List<String> productDP_str;

	public Spinner orderStatus_sp, category_sp, payment_sp, areaName_sp, shopname_spinner;

	public TextView uploadImage, shopClosed, submit;
	private static TextView paymentSelected;
	private EditText remarksET;
	private LinearLayout list_li;
	private ImageView capture;
	CardView cardvieww;
	private TableLayout tableLayout;
	int j;

	boolean cameracaptured = false;
	Context mContext;
	String OrderDeliveryDate = "";


	///Image Capture
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private File mCapturedImageFile;
	private Bitmap capturedImage = null;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static int RESULT_LOAD_IMAGE = 1;
	private static int CAMERA_REQUES_CODE = 101;
	String captured_img_str;
	public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
	public static final String ALLOW_KEY = "ALLOWED";
	public static final String CAMERA_PREF = "camera_pref";

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

	//////////All Spinner model classes
	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>(); //Shop Names Newly added
	ArrayList<ShopNamesData> _areaNamesData = new ArrayList<ShopNamesData>(); //Area Drop down
	ArrayList<ShopNamesData> _orderStatusData = new ArrayList<ShopNamesData>(); //Religion Drop Down
	ArrayList<ShopNamesData> _paymentsSelectData = new ArrayList<ShopNamesData>(); //Select Payment


	ArrayList<String> shooNamestitle = new ArrayList<String>(); // Shop Name Title
	ArrayList<String> areaNamestitle = new ArrayList<String>();
	ArrayList<String> orderStatusTitle = new ArrayList<String>();
	ArrayList<String> paymentNamestitle = new ArrayList<String>();

	String selected_zoneId = "";
	String selected_roueId = "";
	String selected_areaNameId = "";
	String selected_ShopId = "";
	String selected_orderStatusId = "";
	String selected_paymentTermsId = "";
	String SPINNER_SELECTION = "";

	AutoCompleteTextView shopName_autoComplete;

	ImageView product_addiv;

	EditText availzonenametxt, availroutenoetxt;
	LinearLayout statusBaseLayout;
	String orderStatusName = "";

	ProgressDialog progressdailog;
	private static long back_pressed;
	boolean backIconClick = false;

	String Selcted_AreaName = "";
	String Selcted_Shopname = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderbooking_activity);
		initializeViews();
		initializeActions();
		availableDetails();
		gettingDataFromServer();
		selectAreaNameBind();
		selectOrderStatus();
		selectShopNames();

	}

	private void initializeViews()
	{
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = OrderBookingActivity.this;
		orderBookActivity = OrderBookingActivity.this;
		tableLayout = (TableLayout) findViewById(R.id.tableRow1);
		tableLayout.setVisibility(View.GONE);
		category_sp = (Spinner) findViewById(R.id.product_category);
		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);
		orderStatus_sp = (Spinner) findViewById(R.id.order_status_spinner);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_spinner);
		capture = (ImageView) findViewById(R.id.capturedImage);
		cardvieww = (CardView) findViewById(R.id.cardvieww);
		uploadImage = (TextView) findViewById(R.id.upLoadImage);
		shopClosed = (TextView) findViewById(R.id.shopClosed);
		submit = (TextView) findViewById(R.id.submit);
		shopName_autoComplete = (AutoCompleteTextView) findViewById(R.id.shopName_autoComplete);
		paymentSelected = (TextView) findViewById(R.id.paymentSelected);
		paymentSelected.setVisibility(View.GONE);
		remarksET = (EditText) findViewById(R.id.Remarks_et);
		availzonenametxt = (EditText) findViewById(R.id.availzonenametxt);
		availroutenoetxt = (EditText) findViewById(R.id.availroutenoetxt);
		product_addiv = (ImageView) findViewById(R.id.product_addiv);
		list_li = (LinearLayout) findViewById(R.id.items_li);
		statusBaseLayout = (LinearLayout) findViewById(R.id.statusBaseLayout);

		shopname_spinner = (Spinner) findViewById(R.id.shopname_spinner);

	}

	private void initializeActions()
	{
		productDP = new ArrayList<>();
		zoneDetailsDP = new ArrayList<>();
		areaDetailsDP = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();

		productDP_str = new ArrayList<>();
		product_addiv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				category_sp.performClick();
			}
		});
		areaName_sp.setOnItemSelectedListener(this);
		orderStatus_sp.setOnItemSelectedListener(this);
		payment_sp.setOnItemSelectedListener(this);
		shopname_spinner.setOnItemSelectedListener(this);

		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				boolean validated = validationEntryData();
				if (validated)
				{
					progressdailog = Utility.setProgressDailog(mContext);
					progressdailog.show();
					dataUploadInServer();
				}
			}
		});
	}

	private void availableDetails()
	{
		selected_zoneId = SharedPrefsUtil.getStringPreference(mContext, "SELECTED_ZONEID");
		selected_roueId = SharedPrefsUtil.getStringPreference(mContext, "SELECTED_ROUTEID");
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
	}

	private void gettingDataFromServer()
	{
		if (Utility.isOnline(mContext))
		{
			HttpAdapter.getAreaDetailsByRoute(OrderBookingActivity.this, "areaNameDP", selected_roueId);
			HttpAdapter.getPayment(OrderBookingActivity.this, "payment");
			HttpAdapter.getOrderStatus(OrderBookingActivity.this, "orderStatus");
			HttpAdapter.getProductCategoryDP(OrderBookingActivity.this, "productCategoryName");
			HttpAdapter.GetOrderNumber(OrderBookingActivity.this, "GetOrderNumber");
		}
		else
		{
			Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
		{
			try
			{
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.MediaColumns.DATA};

				Cursor cursor = getContentResolver().query(selectedImage,
				                                           filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				capturedImage = BitmapFactory.decodeFile(picturePath, options);

				try
				{
					ExifInterface ei = new ExifInterface(picturePath);
					int orientation = ei.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					switch (orientation)
					{
						case ExifInterface.ORIENTATION_ROTATE_90:

							capturedImage = rotateImageIfRequired(mContext,
							                                      capturedImage, selectedImage);
							break;
						case ExifInterface.ORIENTATION_ROTATE_180:
							capturedImage = rotateImageIfRequired(mContext,
							                                      capturedImage, selectedImage);
							break;
					}
				}
				catch (Exception e)
				{
				}
				if (capturedImage != null)
				{
					Toast.makeText(getApplicationContext(),
					               "Captured successfully.", Toast.LENGTH_SHORT)
					     .show();
					cameracaptured = true;
					imageViewDisplay(capturedImage);
				}
			}
			catch (Exception e)
			{

			}
		}
		else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				previewCapturedImage();
			}
			else if (resultCode == RESULT_CANCELED)
			{
				cameracaptured = true;
//				isShopClosed.i(f);
				capture.setVisibility(View.GONE);
				list_li.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
			}
			else
			{
				cameracaptured = true;
				capture.setVisibility(View.GONE);
				list_li.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
				     .show();
			}
		}
	}

	private void displayTableView(final List<GetProductCategory> productDP)
	{
		tableLayout.setVisibility(View.VISIBLE);
		tableLayout.removeAllViews();
		headers();

		storedProductCategories.clear();
		storedProductCategories.addAll(productDP);

		for (int i = 0; i < productDP.size(); i++)
		{
			j = i;
			ProductCategoryTableRow row = new ProductCategoryTableRow(this, productDP.get(i), i);
			tableLayout.addView(row, new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}

	private void dataUploadInServer()
	{
		String paymentSelectedStr = "";
		String CreditDays = "";
		String chequeDate = "";
		JSONArray cartItemsArray = new JSONArray();
		if (storedProductCategories != null && !storedProductCategories.isEmpty())
		{
			Log.d("Order", "Stored_Products" + " : " + storedProductCategories.toString());
			for (GetProductCategory getProducts : storedProductCategories)
			{
				try
				{
					cartItemsArray.put(getProducts.toJSON());
				}
				catch (Exception e)
				{
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
		Log.e("Camera Captered", cameracaptured + "");
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
		String IsShopClosed = "";
		String ShopClosedImage = "";
		if (cameracaptured)
		{
			IsShopClosed = "Y";
			if (captured_img_str != null && !captured_img_str.isEmpty())
			{
				ShopClosedImage = captured_img_str;
			}
			else
			{
				ShopClosedImage = "";
			}
		}
		else
		{
			IsShopClosed = "N";
			ShopClosedImage = "";
		}
		String IsOrdered = "Y";
		String IsInvoice = "N";
		String EmployeeId = "";
		String employeeId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeId");
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

		String jsonString = createJsonOrderSubmit(selected_zoneId, selected_roueId, selected_areaNameId,
		                                          selected_ShopId, IsShopClosed, ShopClosedImage, OrderDeliveryDate,
		                                          selected_orderStatusId, selected_paymentTermsId,
		                                          CreditDays, chequeDate,
		                                          IsOrdered, IsInvoice, remarksET.getText().toString(),
		                                          EmployeeId, cartItemsArray);
		Log.e("parameters", jsonString + "");
		HttpAdapter.orderbook(this, "orderbook", jsonString);
	}

	private void headers()
	{
		TableRow row = new TableRow(this);

		TextView taskdate = new TextView(OrderBookingActivity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Product");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(OrderBookingActivity.this);
		title.setText("Price");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(OrderBookingActivity.this);
		taskhour.setText("Quantity");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(OrderBookingActivity.this);
		description3.setText("Frees");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));

		TextView remove = new TextView(OrderBookingActivity.this);
		remove.setText("Delete");
		remove.setBackgroundColor(getResources().getColor(R.color.light_green));
		remove.setTextSize(15);
		remove.setPadding(10, 10, 10, 10);
		row.addView(remove);
		remove.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                 TableRow.LayoutParams.WRAP_CONTENT));

		tableLayout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

	}


	private void selectAreaNameBind()
	{
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
		//selectShopNameBind();
	}

	private void selectOrderStatus()
	{
		orderStatusTitle.add("Select Order Status");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, orderStatusTitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		orderStatus_sp.setAdapter(dataAdapter_areaName);
		//selectShopNameBind();
	}

	private void selectShopNames()
	{
		shooNamestitle.add("Select Shop Names");
		ArrayAdapter<String> dataAdapter_shonames = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		                                                                     shooNamestitle);
		dataAdapter_shonames.setDropDownViewResource(R.layout.list_item);
		shopname_spinner.setAdapter(dataAdapter_shonames);
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
				//productCategory DropDown
				else if (response.getTag().equals("productCategoryName"))
				{
					Log.e("Productdata", mJson.toString());
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						JSONArray jsonArray = mJson.getJSONArray("Data");
						productDP_str.add("Add Product");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetProductCategory getProductCategory = new Gson().fromJson(obj.toString(), GetProductCategory.class);
							productDP.add(getProductCategory);
							productDP_str.add(getProductCategory.ProductName);
						}

						//Product category adapter
						ArrayAdapter<String> dataAdapter_productName = new ArrayAdapter<String>(this,
						                                                                        android.R.layout.simple_spinner_item, productDP_str);
						dataAdapter_productName.setDropDownViewResource(R.layout.list_item);
						category_sp.setAdapter(dataAdapter_productName);

						category_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, final int positionvalue, long id)
							{
								try
								{
									if (positionvalue != 0)
									{
//										productCategoryId = String.valueOf(positionvalue);
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
						category_sp.setAdapter(dataAdapter_productName);
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
				else if (response.getTag().equals("orderbook"))
				{
					progressdailog.dismiss();
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Your Order Booked.", Toast.LENGTH_SHORT).show();
						Toast.makeText(mContext, "Your Order Number is " + mJson.getString("Data"), Toast.LENGTH_SHORT).show();
						SharedPrefsUtil.setStringPreference(mContext, "KEY_ZONE_ID", selected_zoneId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_ROUTE_ID", selected_roueId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_AREANAME_ID", selected_areaNameId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_AREANAME", Selcted_AreaName);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_SHOP_NAME", Selcted_Shopname);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_PAYMENT_ID", selected_paymentTermsId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_SHOP_ID", selected_ShopId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_ORDER_STATUS_ID", selected_orderStatusId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_DESCRIPTION", remarksET.getText().toString());
						Toast.makeText(mContext, "Successfully Your Order Booked.", Toast.LENGTH_SHORT).show();
						Toast.makeText(mContext, "Your Order Number is " + mJson.getString("Data"), Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
					}
					else
					{
						if (mJson.getString("Message").equalsIgnoreCase("Shop Closed"))
						{
							Toast.makeText(mContext, "Successfully Shop Closed", Toast.LENGTH_SHORT).show();
							dailogBoxAfterSubmit();
						}
						else
						{
							Toast.makeText(mContext, "Upload Failed.." + mJson.getString("Data"), Toast.LENGTH_SHORT).show();
							refreshActivity();
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
				backIconClick = true;
				onBackPressed();
				return true;
		}
		return false;
	}

	@Override
	public void onBackPressed()
	{
		if (backIconClick)
		{
			super.onBackPressed();
			Intent intent = new Intent(this, DashboardActivity.class);
			startActivity(intent);
			finish();
		}
		else
		{
			if (back_pressed + 1000 > System.currentTimeMillis())
			{
				super.onBackPressed();
				Intent intent = new Intent(this, DashboardActivity.class);
				startActivity(intent);
				finish();
			}
			else
			{
				Toast.makeText(getBaseContext(), "Press once again to back!", Toast.LENGTH_SHORT).show();
			}
			back_pressed = System.currentTimeMillis();
		}


	}

	private String createJsonOrderSubmit(String ZoneId, String RouteId,
	                                     String AreaId, String ShopId, String IsShopClosed, String ShopClosedImage,
	                                     String OrderDeliveryDate, String OrderStatusId, String PaymentTermsId,
	                                     String creditDays, String chequeDate, String IsOrdered,
	                                     String IsInvoice,
	                                     String Remarks, String EmployeeId,
	                                     JSONArray cartItemsArray
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
			dataObj.putOpt("IsShopClosed", IsShopClosed);
			Log.e("ShopClosedImage", ShopClosedImage);
			dataObj.putOpt("ShopClosedImage", ShopClosedImage);
			dataObj.putOpt("OrderDeliveryDate", OrderDeliveryDate);
			dataObj.putOpt("OrderStatusId", OrderStatusId);
			dataObj.putOpt("PaymentTermsId", PaymentTermsId); //PaymentTermsId // Added New param for paymnet tersms
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

			if (orderStatusName.equals("Order Not Given"))
			{
				dataObj.putOpt("IsOrdered", "Y");
				Log.e("isOrder", dataObj.putOpt("IsOrdered", "Y") + "");
			}
			else
			{
				if (IsShopClosed.equals("Y"))
				{
					dataObj.putOpt("IsOrdered", "N");
					Log.e("isOrder", dataObj.putOpt("IsOrdered", "N") + "");
				}
				else
				{
					dataObj.putOpt("IsOrdered", "Y");
					Log.e("isOrder", dataObj.putOpt("IsOrdered", "Y") + "");
				}
			}

			dataObj.putOpt("IsInvoice", IsInvoice);
			dataObj.putOpt("Remarks", Remarks);
			dataObj.putOpt("EmployeeId", EmployeeId);
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

	private void showAlert()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(OrderBookingActivity.this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage("Bright Udyog needs to access the Camera.");

		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
		                      new DialogInterface.OnClickListener()
		                      {
			                      public void onClick(DialogInterface dialog, int which)
			                      {
				                      dialog.dismiss();
				                      finish();
			                      }
		                      });

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
		                      new DialogInterface.OnClickListener()
		                      {

			                      public void onClick(DialogInterface dialog, int which)
			                      {
				                      dialog.dismiss();
				                      ActivityCompat.requestPermissions(OrderBookingActivity.this,
				                                                        new String[]{Manifest.permission.CAMERA},
				                                                        MY_PERMISSIONS_REQUEST_CAMERA);
			                      }
		                      });
		alertDialog.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	{
		switch (requestCode)
		{
			case MY_PERMISSIONS_REQUEST_CAMERA:
			{
				for (int i = 0, len = permissions.length; i < len; i++)
				{
					String permission = permissions[i];

					if (grantResults[i] == PackageManager.PERMISSION_DENIED)
					{
						boolean
								showRationale =
								ActivityCompat.shouldShowRequestPermissionRationale(
										this, permission);

						if (showRationale)
						{
							showAlert();
						}
						else if (!showRationale)
						{
							saveToPreferences(OrderBookingActivity.this, ALLOW_KEY, true);
						}
					}
				}
			}
		}
	}

	public static void saveToPreferences(Context context, String key, Boolean allowed)
	{
		SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
		                                                         Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putBoolean(key, allowed);
		prefsEditor.commit();
	}


	private void openCamera()
	{
		mCapturedImageFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
		Uri fileUri = getOutputMediaFileUri(mCapturedImageFile);
		if (fileUri != null)
		{
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			for (ResolveInfo resolveInfo : resInfoList)
			{
				String packageName = resolveInfo.activityInfo.packageName;
				mContext.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		}
	}

	private static File getOutputMediaFile(int type)
	{
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);
		if (!mediaStorageDir.exists())
		{
			if (!mediaStorageDir.mkdirs())
			{
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
		                                        Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE)
		{
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					                     + "IMG_" + timeStamp + ".jpg");
		}
		else
		{
			return null;
		}

		return mediaFile;
	}

	public Uri getOutputMediaFileUri(File mediaFile)
	{
		if (mediaFile != null)
		{
			return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", mediaFile);
		}
		return null;
	}

	@Override
	public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id)
	{
		String selectedSpinner = "";
		switch (parent.getId())
		{
			case R.id.areaName_spinner:
				selectedSpinner = "AREA";
				dropDownValueSelection(position, _areaNamesData, selectedSpinner);
				break;
			case R.id.order_status_spinner:
				selectedSpinner = "ORDER_STATUS";
				dropDownValueSelection(position, _orderStatusData, selectedSpinner);
				break;
			case R.id.payment_terms_spinner:
				selectedSpinner = "PAYMENT_TYPE";
				dropDownValueSelection(position, _paymentsSelectData, selectedSpinner);
				break;
			case R.id.shopname_spinner:
				selectedSpinner = "SHOP_NAMES";
				dropDownValueSelection(position, _shopNamesData, selectedSpinner);
				break;
		}

	}

	private void dropDownValueSelection(int position, ArrayList<ShopNamesData> _dropDownData, String selectedSpinner)
	{
		try
		{
			if (position != 0)
			{
				if (_dropDownData.size() != 0)
				{
					if (selectedSpinner.equals("AREA"))
					{
						selected_areaNameId = _dropDownData.get(position - 1).getShopId();
						Selcted_AreaName = _dropDownData.get(position - 1).getShopName();
						HttpAdapter.getShopDetailsDP(OrderBookingActivity.this, "shopName", selected_areaNameId);
					}
					else if (selectedSpinner.equals("ORDER_STATUS"))
					{
						selected_orderStatusId = _dropDownData.get(position - 1).getShopId();
						orderStatusName = _dropDownData.get(position - 1).getShopName();
						Log.e("orderStatusName", orderStatusName);

						if (orderStatusName.equals("Shop Was Closed"))
						{
							cameraAccess();
						}
						else
						{
							if (orderStatusName.equals("Order Not Given"))
							{
								statusBaseLayout.setVisibility(View.GONE);
								cardvieww.setVisibility(View.GONE);
							}
							else
							{
								normalModeAccess();

							}
						}
					}
					else if (selectedSpinner.equals("PAYMENT_TYPE"))
					{
						selected_paymentTermsId = _dropDownData.get(position).getShopId();
						String paymentSelected = _dropDownData.get(position).getShopName();
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
					if (selectedSpinner.equals("SHOP_NAMES"))
					{
						selected_ShopId = _dropDownData.get(position - 1).getShopId();
						Selcted_Shopname = _dropDownData.get(position - 1).getShopName();
						String selectedName = _dropDownData.get(position - 1).getShopName();
						Log.e("entryShopName", selectedName);
						for (int i = 0; i < _shopNamesData.size(); i++)
						{
							String availName = _shopNamesData.get(i).getShopName();
							if (availName.equals(selectedName))
							{
								selected_ShopId = _shopNamesData.get(i).getShopId();
								Log.e("selected_ShopId", selected_ShopId + "");
								break;
							}
						}
					}
				}
			}
			else if (selectedSpinner.equals("PAYMENT_TYPE"))
			{
				selected_paymentTermsId = _dropDownData.get(position).getShopId();
				String paymentSelected = _dropDownData.get(position).getShopName();
				Log.e("paymentSelected", paymentSelected);
			}
		}
		catch (Exception e)
		{
		}


	}

	private void normalModeAccess()
	{
		cardvieww.setVisibility(View.GONE);
		list_li.setVisibility(View.VISIBLE);
		statusBaseLayout.setVisibility(View.VISIBLE);
	}

	private void cameraAccess()
	{

		if (list_li.getVisibility() == View.VISIBLE)
		{
			list_li.setVisibility(View.GONE);
		}
		else
		{
			list_li.setVisibility(View.VISIBLE);
		}
		cardvieww.setVisibility(View.VISIBLE);
		capture.setVisibility(View.VISIBLE);
		handleTaskWithUserPermission(CAMERA_REQUES_CODE);

	}

	@Override
	public void onNothingSelected(final AdapterView<?> parent)
	{

	}

	private class ProductCategoryTableRow extends TableRow
	{

		private Context mContext;
		private GetProductCategory mProductCategory;

		private EditText quantityETID;
		private EditText fresETID;

		private String afterTextChanged = "";
		private String beforeTextChanged = "";
		private String onTextChanged = "";

		private final int position;

		public ProductCategoryTableRow(final Context context, final GetProductCategory productCategory, int index)
		{
			super(context);
			mContext = context;
			mProductCategory = productCategory;
			position = index;
			init();
		}

		public GetProductCategory getProductCategory()
		{
			// update your new data
			if (!TextUtils.isEmpty(quantityETID.getText()))
			{
				mProductCategory.Quantity = quantityETID.getText().toString();
				mProductCategory.Frees = fresETID.getText().toString();
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
				taskdate.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.42f));
				addView(taskdate);


				TextView title = new TextView(mContext);
				title.setText(mProductCategory.ProductPrice);
				title.setTextSize(15);
				title.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
				title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                       LayoutParams.WRAP_CONTENT));
				addView(title);

				quantityETID = new EditText(mContext);
				quantityETID.setText(mProductCategory.Quantity);
				quantityETID.setBackgroundColor(Color.TRANSPARENT);
				quantityETID.setClickable(true);
				quantityETID.setCursorVisible(true);
				quantityETID.setFocusableInTouchMode(true);
				quantityETID.setTextSize(15);
				quantityETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				quantityETID.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
				quantityETID.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				quantityETID.addTextChangedListener(mTextWatcher);
				addView(quantityETID);
				fresETID = new EditText(mContext);
				if (mProductCategory.Frees != null && !mProductCategory.Frees.isEmpty())
				{
					fresETID.setText(mProductCategory.Frees);
				}
				else
				{
					fresETID.setText("0");
				}
				fresETID.setBackgroundColor(Color.TRANSPARENT);
				fresETID.setClickable(true);
				fresETID.setCursorVisible(true);
				fresETID.setFocusableInTouchMode(true);
				fresETID.setTextSize(15);
				fresETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				fresETID.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
				fresETID.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                          LayoutParams.WRAP_CONTENT));
				fresETID.addTextChangedListener(mTextWatcherFres);
				addView(fresETID);


				/*TextView description = new TextView(mContext);
				description.setText(mProductCategory.VAT);
				description.setTextSize(15);
				//description.setPadding(15,0,0,0);
				description.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                             LayoutParams.WRAP_CONTENT));
				addView(description);*/

				/*TextView description2 = new TextView(mContext);
				description2.setText(mProductCategory.GST);
				description2.setTextSize(15);
				description2.setVisibility(GONE);
				description2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				                                              LayoutParams.WRAP_CONTENT));
				addView(description2);*/

				ImageView deleteimg = new ImageView(mContext);
//				deleteimg.setImageResource(getResources().getDrawable(R.drawable.delete));
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
				afterTextChanged = s.toString();
				storedProductCategories.get(position).setQuantity(s.toString());
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
						int quantityValue = Integer.parseInt(storedProductCategories.get(position).getQuantity());
						if (quantityValue > fresValue)
						{
							storedProductCategories.get(position).setFres(s.toString());
						}
						else
						{
							fresETID.setText(storedProductCategories.get(position).getFres());
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

	public String BitMapToString(Bitmap bitmap)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	private boolean validationEntryData()
	{
		boolean ret = true;
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
		if (!orderStatusName.equals("Order Not Given"))
		{
			if (!cameracaptured)
			{
				if (list.size() == 0)
				{
					Toast.makeText(getApplicationContext(), "Please Add Product", Toast.LENGTH_SHORT).show();
					ret = false;
					return ret;
				}
				if (selected_paymentTermsId == null || selected_paymentTermsId.isEmpty() || selected_paymentTermsId.equals("0"))
				{
					Toast.makeText(getApplicationContext(), "Please Select Payment Terms Name", Toast.LENGTH_SHORT).show();
					ret = false;
					return ret;
				}
			}
		}
		return ret;
	}


	private void handleTaskWithUserPermission(int requestCode)
	{
		DangerousPermissionUtils.getPermission(mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode)
		                        .enqueue(new DangerousPermResponseCallBack()
		                        {
			                        @Override
			                        public void onComplete(final DangerousPermissionResponse permissionResponse)
			                        {
				                        if (permissionResponse.isGranted())
				                        {
					                        if (permissionResponse.getRequestCode() == CAMERA_REQUES_CODE)
					                        {
						                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat
								                        .checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
						                        {
							                        return;
						                        }
						                        cameracaptured = true;
						                        openCamera();
					                        }
				                        }
			                        }
		                        });
	}


	private void previewCapturedImage()
	{
		try
		{

			String filePath = mCapturedImageFile.getAbsolutePath();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			capturedImage = BitmapFactory.decodeFile(filePath, options);
			try
			{
				Uri fileUri = getOutputMediaFileUri(mCapturedImageFile);
				ExifInterface ei = new ExifInterface(filePath);
				int orientation = ei.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);

				switch (orientation)
				{
					case ExifInterface.ORIENTATION_ROTATE_90:
						capturedImage = rotateImageIfRequired(mContext,
						                                      capturedImage, fileUri);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						capturedImage = rotateImageIfRequired(mContext,
						                                      capturedImage, fileUri);
						break;
				}
			}
			catch (Exception e)
			{
			}
			if (capturedImage != null)
			{
				imageViewDisplay(capturedImage);
			}
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	private static Bitmap rotateImageIfRequired(Context context, Bitmap img,
	                                            Uri selectedImage)
	{

		int rotation = getRotation(context, selectedImage);
		if (rotation != 0)
		{
			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(),
			                                        img.getHeight(), matrix, true);
			img.recycle();
			return rotatedImg;
		}
		else
		{
			return img;
		}
	}

	private static int getRotation(Context context, Uri selectedImage)
	{
		int rotation = 0;
		ContentResolver content = context.getContentResolver();

		Cursor mediaCursor = content.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{
						"orientation", "date_added"}, null, null,
				"date_added desc");

		if (mediaCursor != null && mediaCursor.getCount() != 0)
		{
			while (mediaCursor.moveToNext())
			{
				rotation = mediaCursor.getInt(0);
				break;
			}
		}
		mediaCursor.close();
		return rotation;
	}

	private void imageViewDisplay(Bitmap captured_img_bitMap)
	{
		capture.setBackgroundResource(R.color.empty);
		capture.setImageBitmap(null);
		capture.setImageBitmap(captured_img_bitMap);
		captured_img_str = BitMapToString(captured_img_bitMap);
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

					Intent in = new Intent(OrderBookingActivity.this, OrderBookingActivity.class);
					Util.killorderBook();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(OrderBookingActivity.this, BillingEditActivity.class);
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
		}
		else if (type.equals("Cheque"))
		{
			DialogFragment newFragment = new DatePickerFragmentDailog();
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
//					selected_paymentTermsId = "2";
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
		SPINNER_SELECTION = "SHOP_NAMES";
		adapterDataAssigingToSpinner(shooNamestitle, SPINNER_SELECTION);

		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, shooNamestitle);
		shopName_autoComplete.setThreshold(1);
		shopName_autoComplete.setAdapter(adapter);
		shopName_autoComplete.setTextColor(Color.BLACK);
		shopName_autoComplete.setTextSize(16);

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
							break;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});*/

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


		if (spinnerSelction.equals("AREA"))
		{
			areaName_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("ORDER_STATUS"))
		{
			orderStatus_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("PAYMENT_SELECT"))
		{
			payment_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("SHOP_NAMES"))
		{
			shopname_spinner.setAdapter(dataAdapter);
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
			paymentSelected.setText("");
			paymentSelected.setVisibility(View.VISIBLE);
			paymentSelected.setText(sectiondate);
			SharedPrefsUtil.setStringPreference(getContext(), "SelectedDate", sectiondate);

		}
	}

}


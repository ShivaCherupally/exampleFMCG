package com.fmcg.ui;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Shiva on 9/22/2017.
 */

public class OrderEditActivity extends AppCompatActivity implements NetworkOperationListener, AdapterView.OnItemSelectedListener, View.OnTouchListener
{
	public static Activity orderBookeditActivity;
	public List<GetProductCategory> productDP;
	public List<GetProductCategory> storedProductCategories = new ArrayList<GetProductCategory>();
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetAreaDetails> areaDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	public final List<GetProductCategory> list = new ArrayList<GetProductCategory>();
	private List<String> productDP_str;

	public Spinner shopName_sp, orderStatus_sp, category_sp, payment_sp, routeName_sp, areaName_sp, routecd, zone_sp;

	public CheckBox isShopClosed, ordered, invoice;
	public TextView uploadImage, shopClosed, orderDate, submit, tvDisplayDate, orderNumInvoice;
	private static TextView paymentSelected;
	private EditText remarksET;
	private LinearLayout list_li;
	private ImageView capture;
	private TableLayout tableLayout;
	int j;

	boolean cameracaptured = false;
	Context mContext;
	String capturedImgaestr;

	String productCategoryId = "";
	String OrderDeliveryDate = "";


	///Image Capture
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private File mCapturedImageFile;
	private Bitmap capturedImage = null;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static int RESULT_LOAD_IMAGE = 1;
	ArrayList<String> capturelist = new ArrayList<String>();
	private static int CAMERA_REQUES_CODE = 101;
	String captured_img_str;
	public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
	public static final String ALLOW_KEY = "ALLOWED";
	public static final String CAMERA_PREF = "camera_pref";

	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	RadioButton orderBook;
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
	String AvailShopName = "";

	AutoCompleteTextView shopName_autoComplete;
	String selectedName = "";

	ImageView product_addiv;

	boolean zoneTouchClick = false;
	boolean routeTouchClick = false;
	boolean areaTouchClick = false;
	boolean shopNamesTouchClick = false;
	boolean orderStatusTouchClick = false;
	boolean paymentTermsTouchClick = false;

	EditText availzonenametxt, availroutenoetxt;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = OrderEditActivity.this;

		orderBookeditActivity = OrderEditActivity.this;
		tableLayout = (TableLayout) findViewById(R.id.tableRow1);
		tableLayout.setVisibility(View.GONE);

		category_sp = (Spinner) findViewById(R.id.product_category);
		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		((TextView) zone_sp.getChildAt(0)).setTextColor(getResources().getColor(R.color.HeaderColor));
		zone_sp.setVisibility(View.VISIBLE);
		zone_sp.setEnabled(false);
		zone_sp.setClickable(false);
		zone_sp.setBackgroundColor(Color.TRANSPARENT);
		routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);
		((TextView) routeName_sp.getChildAt(0)).setTextColor(getResources().getColor(R.color.HeaderColor));
		routeName_sp.setVisibility(View.VISIBLE);
		routeName_sp.setBackgroundColor(Color.TRANSPARENT);
		routeName_sp.setEnabled(false);
		routeName_sp.setClickable(false);

		availzonenametxt = (EditText) findViewById(R.id.availzonenametxt);
		availzonenametxt.setVisibility(View.GONE);
		availroutenoetxt = (EditText) findViewById(R.id.availroutenoetxt);
		availroutenoetxt.setVisibility(View.GONE);

		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);
//		shopName_sp = (Spinner) findViewById(R.id.shopname_spinner);
		orderStatus_sp = (Spinner) findViewById(R.id.order_status_spinner);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_spinner);

		isShopClosed = (CheckBox) findViewById(R.id.isClosed);
		ordered = (CheckBox) findViewById(R.id.isOrder);
		invoice = (CheckBox) findViewById(R.id.isInvoice);
		capture = (ImageView) findViewById(R.id.capturedImage);
		uploadImage = (TextView) findViewById(R.id.upLoadImage);
		shopClosed = (TextView) findViewById(R.id.shopClosed);
		orderNumInvoice = (TextView) findViewById(R.id.orderNumber_invoice);
		submit = (TextView) findViewById(R.id.submit);

		shopName_autoComplete = (AutoCompleteTextView) findViewById(R.id.shopName_autoComplete);

		paymentSelected = (TextView) findViewById(R.id.paymentSelected);
		paymentSelected.setVisibility(View.GONE);
		remarksET = (EditText) findViewById(R.id.Remarks_et);

		product_addiv = (ImageView) findViewById(R.id.product_addiv);
		list_li = (LinearLayout) findViewById(R.id.items_li);

		selectRouteNameBind();
		selectAreaNameBind();

		if (Utility.isOnline(mContext))
		{
			HttpAdapter.getPayment(OrderEditActivity.this, "payment");
			HttpAdapter.getOrderStatus(OrderEditActivity.this, "orderStatus");
			HttpAdapter.getProductCategoryDP(OrderEditActivity.this, "productCategoryName");
			HttpAdapter.getZoneDetailsDP(OrderEditActivity.this, "zoneName");
			HttpAdapter.GetOrderNumber(OrderEditActivity.this, "GetOrderNumber");
		}
		else
		{
			Toast.makeText(mContext, "Please check internet connection", Toast.LENGTH_SHORT).show();
		}

		productDP = new ArrayList<>();
		zoneDetailsDP = new ArrayList<>();
		areaDetailsDP = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();

		productDP_str = new ArrayList<>();

		/*orderNumInvoice.setOnClickListener(this);
		list_li.setOnClickListener(this);*/
		isShopClosed.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (isShopClosed.isChecked())
				{
					if (list_li.getVisibility() == View.VISIBLE)
					{
						list_li.setVisibility(View.GONE);
					}
					else
					{
						list_li.setVisibility(View.VISIBLE);
					}
					capture.setVisibility(View.VISIBLE);
					handleTaskWithUserPermission(CAMERA_REQUES_CODE);
				}
				else
				{
					capture.setVisibility(View.GONE);
					list_li.setVisibility(View.VISIBLE);
				}
			}
		});

		product_addiv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				category_sp.performClick();
			}
		});


		zone_sp.setOnItemSelectedListener(this);
		routeName_sp.setOnItemSelectedListener(this);
		areaName_sp.setOnItemSelectedListener(this);
//		shopName_sp.setOnItemSelectedListener(this);
		orderStatus_sp.setOnItemSelectedListener(this);
		payment_sp.setOnItemSelectedListener(this);

		zone_sp.setOnTouchListener(this);
		routeName_sp.setOnTouchListener(this);
		areaName_sp.setOnTouchListener(this);
//		shopName_sp.setOnItemSelectedListener(this);
		orderStatus_sp.setOnTouchListener(this);
		payment_sp.setOnTouchListener(this);


		submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				boolean validated = validationEntryData();
				if (validated)
				{
					dataUploadInServer();
				}
			}
		});
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
				Toast.makeText(getApplicationContext(),
				               "User cancelled image capture", Toast.LENGTH_SHORT)
				     .show();
			}
			else
			{
				cameracaptured = true;
				Toast.makeText(getApplicationContext(),
				               "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
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
			OrderEditActivity.ProductCategoryTableRow row = new OrderEditActivity.ProductCategoryTableRow(this, productDP.get(i), i);
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
		String orderNumber = orderNumInvoice.getText().toString();
		String IsShopClosed = "";
		String ShopClosedImage = "";
		if (cameracaptured)
		{
			IsShopClosed = "Y";
			if (captured_img_str != null && !captured_img_str.isEmpty())
			{
				ShopClosedImage = captured_img_str;//"captured";// capturedImgaestr;
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

		String jsonString = createJsonOrderSubmit(orderNumber, selected_zoneId, selected_roueId, selected_areaNameId,
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
		android.widget.TableRow row = new android.widget.TableRow(this);

		TextView taskdate = new TextView(OrderEditActivity.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Product");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(OrderEditActivity.this);
		title.setText("Price");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                               android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(OrderEditActivity.this);
		taskhour.setText("Quantity");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                  android.widget.TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(OrderEditActivity.this);
		description3.setText("Frees");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                      android.widget.TableRow.LayoutParams.WRAP_CONTENT));

		TextView remove = new TextView(OrderEditActivity.this);
		remove.setText("Delete");
		remove.setBackgroundColor(getResources().getColor(R.color.light_green));
		remove.setTextSize(15);
		remove.setPadding(10, 10, 10, 10);
		row.addView(remove);
		remove.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT,
		                                                                android.widget.TableRow.LayoutParams.WRAP_CONTENT));
/*		TextView description = new TextView(OrderEditActivity.this);
		description.setText("VAT");
		description.setBackgroundColor(getResources().getColor(R.color.light_green));
		description.setTextSize(15);
		description.setPadding(10, 10, 10, 10);
		row.addView(description);
		description.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                      TableRow.LayoutParams.WRAP_CONTENT));

		TextView description2 = new TextView(OrderEditActivity.this);
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

	private void selectRouteNameBind()
	{
		routeNamestitle.add("Select Route No");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNamestitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		routeName_sp.setAdapter(dataAdapter_areaName);
	}

	private void selectAreaNameBind()
	{
		areaNamestitle.add("Select Area Name");
		ArrayAdapter<String> dataAdapter_areaName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaNamestitle);
		dataAdapter_areaName.setDropDownViewResource(R.layout.list_item);
		areaName_sp.setAdapter(dataAdapter_areaName);
		//selectShopNameBind();
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
										productCategoryId = String.valueOf(positionvalue);
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
				else if (response.getTag().equals("orderbook"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						SharedPrefsUtil.setStringPreference(mContext, "KEY_ZONE_ID", selected_zoneId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_ROUTE_ID", selected_roueId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_AREANAME_ID", selected_areaNameId);
						SharedPrefsUtil.setStringPreference(mContext, "KEY_SHOP_NAME", shopName_autoComplete.getText().toString());
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
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("Fail") + "Fail");
						Toast.makeText(mContext, "Upload Failed..", Toast.LENGTH_SHORT).show();
						refreshActivity();
					}
				}
				else if (response.getTag().equals("GetOrderNumber"))
				{
					if (mJson.getString("Message").equals("SuccessFull"))
					{
						orderNumInvoice.setText(mJson.getString("Data"));
						orderNUmberString = mJson.getString("Data");
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

	private String createJsonOrderSubmit(String OrderNumber, String ZoneId, String RouteId,
	                                     String AreaId, String ShopId, String IsShopClosed, String ShopClosedImage,
	                                     String OrderDeliveryDate, String OrderStatusId, String PaymentTermsId,
	                                     String creditDays, String chequeDate, String IsOrdered,
	                                     String IsInvoice,
	                                     String Remarks, String EmployeeId, JSONArray cartItemsArray
	)
	{
		JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
//			dataObj.putOpt("OrderNumber", OrderNumber);
			dataObj.putOpt("ZoneId", ZoneId);
			dataObj.putOpt("RouteId", RouteId);
			dataObj.putOpt("AreaId", AreaId);
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("IsShopClosed", IsShopClosed);
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

			dataObj.putOpt("IsOrdered", IsOrdered);
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
		AlertDialog alertDialog = new AlertDialog.Builder(OrderEditActivity.this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage("App needs to access the Camera.");

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
				                      ActivityCompat.requestPermissions(OrderEditActivity.this,
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
							// user denied flagging NEVER ASK AGAIN
							// you can either enable some fall back,
							// disable features of your app
							// or open another dialog explaining
							// again the permission and directing to
							// the app setting
							saveToPreferences(OrderEditActivity.this, ALLOW_KEY, true);
						}
					}
				}
			}

			// other 'case' lines to check for other
			// permissions this app might request
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
		/*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, 0);*/
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
//		return Uri.fromFile(getOutputMediaFile(type));
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
					break;
				}
			/*case R.id.shopName_autoComplete:
				selectedSpinner = "SHOP";
				dropDownValueSelection(position, _shopNamesData, selectedSpinner);
				break;*/
			case R.id.order_status_spinner:
				selectedSpinner = "ORDER_STATUS";
				dropDownValueSelection(position, _orderStatusData, selectedSpinner);
				break;
			case R.id.payment_terms_spinner:
				if (paymentTermsTouchClick)
				{
//					selected_paymentTermsId = 2;
					selectedSpinner = "PAYMENT_TYPE";
					dropDownValueSelection(position, _paymentsSelectData, selectedSpinner);
				}
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
					if (selectedSpinner.equals("ZONE"))
					{
						selected_zoneId = _dropDownData.get(position - 1).getShopId();
						HttpAdapter.getRouteDetails(OrderEditActivity.this, "routeName", selected_zoneId);
					}
					else if (selectedSpinner.equals("ROUTE"))
					{
						selected_roueId = _dropDownData.get(position - 1).getShopId(); //3
						HttpAdapter.getAreaDetailsByRoute(OrderEditActivity.this, "areaNameDP", selected_roueId);
					}
					else if (selectedSpinner.equals("AREA"))
					{
						selected_areaNameId = _dropDownData.get(position - 1).getShopId();
						HttpAdapter.getShopDetailsDP(OrderEditActivity.this, "shopName", selected_areaNameId);
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
		}
		catch (Exception e)
		{

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

	private class ProductCategoryTableRow extends android.widget.TableRow
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

			/*TextView description3 = new TextView(mContext);
			description3.setText("-");
			description3.setTextSize(15);
			addView(description3);
			description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
			                                                       TableRow.LayoutParams.WRAP_CONTENT));*/
				fresETID = new EditText(mContext);
				if (mProductCategory.Frees != null && !mProductCategory.Frees.isEmpty())
				{
					fresETID.setText(mProductCategory.Frees);
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
//				deleteimg.setLayoutParams(new TableRow.LayoutParams(24,
//				                                                    TableRow.LayoutParams.WRAP_CONTENT));
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
							android.widget.TableRow row = (TableRow) tableLayout.getChildAt(temposition);
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
		if (!cameracaptured)
		{
			if (productCategoryId == null || productCategoryId.isEmpty() || productCategoryId.equals("0"))
			{
				Toast.makeText(getApplicationContext(), "Please Select Product Category Name", Toast.LENGTH_SHORT).show();
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
						                        /*if (selectedListItem.equalsIgnoreCase("Camera"))
						                        {
							                        captureImage();
						                        }
						                        else
						                        {
							                        if (selectedListItem.equalsIgnoreCase("Gallery"))
							                        {
								                        try
								                        {
									                        Intent i = new Intent(
											                        Intent.ACTION_PICK,
											                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									                        startActivityForResult(i, RESULT_LOAD_IMAGE);
								                        }
								                        catch (Exception e)
								                        {
								                        }

							                        }
						                        }*/
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
			//mCapturedimage_Imageview.setVisibility(View.VISIBLE);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;

			capturedImage = BitmapFactory
					.decodeFile(filePath, options);


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
		orderBook = (RadioButton) promoDialog.findViewById(R.id.orderBook);
		orderBook.setVisibility(View.GONE);
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
					Intent in = new Intent(OrderEditActivity.this, BillingEditActivity.class);
					Util.killorderBook();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(OrderEditActivity.this, BillingEditActivity.class);
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
			DialogFragment newFragment = new Order.DatePickerFragmentDailog();
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
			routeName_sp.setSelection(getIndex(routeName_sp, Integer.parseInt(selected_roueId), _routeCodesData), false);
			HttpAdapter.getAreaDetailsByRoute(OrderEditActivity.this, "areaNameDP", selected_roueId);
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
			areaName_sp.setSelection(getIndex(areaName_sp, Integer.valueOf(selected_areaNameId), _areaNamesData), false);
			HttpAdapter.getShopDetailsDP(OrderEditActivity.this, "shopName", selected_areaNameId);
		}
	}

	/*private void shopNameSpinnerAdapter(final JSONArray jsonArray)
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
//			shooNamestitle.add("Select Shop Name");
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
		*//*SPINNER_SELECTION = "SHOP";
		adapterDataAssigingToSpinner(shooNamestitle, SPINNER_SELECTION);*//*


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, shooNamestitle);
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
		});

	}*/

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
//			paymentNamestitle.add("Select Payment");
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
			autoFillDetails();
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
			ArrayAdapter<String> dataAdapterShops = new ArrayAdapter<String>(this, R.layout.list_item, spinnerTitles);
			dataAdapterShops.setDropDownViewResource(R.layout.list_item);
//			shopName_sp.setAdapter(dataAdapter);
			shopName_autoComplete.setThreshold(1);//will start working from first character
			shopName_autoComplete.setAdapter(dataAdapterShops);//setting the adapter data into the AutoCompleteTextView
			shopName_autoComplete.setTextColor(Color.BLACK);
			shopName_autoComplete.setTextSize(16);
		}*/
		else if (spinnerSelction.equals("ORDER_STATUS"))
		{
			orderStatus_sp.setAdapter(dataAdapter);
		}
		else if (spinnerSelction.equals("PAYMENT_SELECT"))
		{
//			selected_paymentTermsId = "2";
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
			paymentSelected.setText("");
			paymentSelected.setVisibility(View.VISIBLE);
			paymentSelected.setText(sectiondate);

			SharedPrefsUtil.setStringPreference(getContext(), "SelectedDate", sectiondate);
			// Do something with the date chosen by the user

		}


	}

	private int searchByName(Spinner spinner, String searchName, ArrayList<ShopNamesData> _availbleDropDownData)
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
			selected_roueId = SharedPrefsUtil.getStringPreference(mContext, "KEY_ROUTE_ID");
			selected_areaNameId = SharedPrefsUtil.getStringPreference(mContext, "KEY_AREANAME_ID");
			selected_ShopId = SharedPrefsUtil.getStringPreference(mContext, "KEY_SHOP_ID");
			AvailShopName = SharedPrefsUtil.getStringPreference(mContext, "KEY_SHOP_NAME");


			if (selected_zoneId != null && !selected_zoneId.isEmpty())
			{
				zone_sp.setSelection(getIndex(zone_sp, Integer.parseInt(selected_zoneId), _zoneNamesData), false);
				HttpAdapter.getRouteDetails(OrderEditActivity.this, "routeName", selected_zoneId);
			}

			if (selected_paymentTermsId != null && !selected_paymentTermsId.equals("0") && !selected_paymentTermsId.equalsIgnoreCase("null"))
			{
				if (_paymentsSelectData.size() > 0)
				{
					payment_sp.setSelection(getIndex(payment_sp, Integer.parseInt(selected_paymentTermsId), _paymentsSelectData), false);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}


	}


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
						selectedName = shopName_autoComplete.getText().toString();
						AvailShopName = selectedName;
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
					else
					{
						selectedName = "";
					}


				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
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


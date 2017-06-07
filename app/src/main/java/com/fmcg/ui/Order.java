package com.fmcg.ui;

import android.Manifest;
import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.TableRow;

import com.fmcg.Dotsoft.BuildConfig;
import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.models.GetAreaDetails;
import com.fmcg.models.GetProductCategory;
import com.fmcg.models.GetProducts;
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
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.google.gson.Gson;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.fmcg.util.Common.orderNUmberString;


public class Order extends AppCompatActivity implements View.OnClickListener, NetworkOperationListener
{
	public static Activity orderBookActivity;
	public SharedPreferences sharedPreferences;
	Dialog alertDialog;
	public List<PaymentDropDown> paymentDP;
	public List<GetShopDetailsDP> shopsDP;
	public List<OrderStatusDropdown> orderstatusDP;
	public List<GetProductCategory> productDP;
	public List<GetProductCategory> storedProductCategories = new ArrayList<GetProductCategory>();
	public List<GetZoneDetails> zoneDetailsDP;
	public List<GetAreaDetails> areaDetailsDP;
	public List<GetRouteDetails> routeDetailsDP;
	public List<GetProductCategory> list;
	private List<GetRouteDropDown> routeDp;

	private List<String> paymentDP_str;
	private List<String> shopNameDP_str;
	private List<String> orderstatusDP_str;
	private List<String> productDP_str;
	private List<String> zoneDetailsDP_str;
	private List<String> areaDetailsDP_str;
	private List<String> routeDetailsDP_str;
	private List<String> routeDp_str;

	public String paymentDropDown, shopNameDropDown, orderStatusDropDown, productNameDropDown,
			zoneNameDropdown, areaNameDropDown, routeNameDropDown, routeCode;


	public Spinner shopName_sp, orderStatus_sp, category_sp, payment_sp, zone_sp, routeName_sp, areaName_sp, routecd;
	public CheckBox isShopClosed, ordered, invoice;
	public TextView uploadImage, shopClosed, orderDate, submit, tvDisplayDate, orderNumInvoice;
	private EditText remarksET;
	private LinearLayout list_li;
	private DatePicker dpResult;
	private ImageView capture;
	private int year;
	private int month;
	private int day;
	private TableLayout tableLayout;

	String quantityItems;
	//	EditText quantityETID;
	int j;

	private Activity activity;

	boolean cameracaptured = false;
	Context mContext;
	String capturedImgaestr;

	String ZoneId = "";
	String RouteId = "";
	String AreaId = "";
	String ShopId = "";
	String OrderStatusId = "";
	String productCategoryId = "";
	String paymentTermsId = "";
	String OrderDeliveryDate = "";

	ArrayList<ShopNamesData> _shopNamesData = new ArrayList<ShopNamesData>();
	ArrayList<String> shopNamestitle = new ArrayList<String>();
	String selected_shopId = "";

	ArrayList<ShopNamesData> _routeNamesData = new ArrayList<ShopNamesData>();
	ArrayList<String> routenostitle = new ArrayList<String>();
	String selected_routeId = "";

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
	Button alert_submit;
	boolean check1 = false;
	boolean check2 = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order);
		//setCurrentDateOnView();

		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		//initializing the variables
		activity = Order.this;
		mContext = Order.this;

		orderBookActivity = Order.this;

		tableLayout = (TableLayout) findViewById(R.id.tableRow1);

		orderStatus_sp = (Spinner) findViewById(R.id.order_type_dp);
		category_sp = (Spinner) findViewById(R.id.product_category);
		payment_sp = (Spinner) findViewById(R.id.payment_terms_name);
		shopName_sp = (Spinner) findViewById(R.id.shopname_spinner);
		zone_sp = (Spinner) findViewById(R.id.zone_name_spinner);
		routeName_sp = (Spinner) findViewById(R.id.routeName_spinner);
		areaName_sp = (Spinner) findViewById(R.id.areaName_spinner);
		routecd = (Spinner) findViewById(R.id.routecd);

		isShopClosed = (CheckBox) findViewById(R.id.isClosed);
		ordered = (CheckBox) findViewById(R.id.isOrder);
		invoice = (CheckBox) findViewById(R.id.isInvoice);
		capture = (ImageView) findViewById(R.id.capturedImage);
		uploadImage = (TextView) findViewById(R.id.upLoadImage);
		shopClosed = (TextView) findViewById(R.id.shopClosed);
		orderNumInvoice = (TextView) findViewById(R.id.orderNumber_invoice);
		//orderDate = (TextView)findViewById(R.id.order_date);
		submit = (TextView) findViewById(R.id.submit);

		remarksET = (EditText) findViewById(R.id.Remarks_et);

		list_li = (LinearLayout) findViewById(R.id.items_li);

		HttpAdapter.getPayment(Order.this, "payment");
		HttpAdapter.getOrderStatus(Order.this, "orderStatus");
		HttpAdapter.getProductCategoryDP(Order.this, "productCategoryName");
		HttpAdapter.getZoneDetailsDP(Order.this, "zoneName");
		HttpAdapter.getRoute(Order.this, "routeCode");
		HttpAdapter.GetOrderNumber(Order.this, "GetOrderNumber");

		paymentDP = new ArrayList<>();
		shopsDP = new ArrayList<>();
		orderstatusDP = new ArrayList<>();
		productDP = new ArrayList<>();
		zoneDetailsDP = new ArrayList<>();
		areaDetailsDP = new ArrayList<>();
		routeDetailsDP = new ArrayList<>();
		list = new ArrayList<>();
		routeDp = new ArrayList<>();

		paymentDP_str = new ArrayList<>();
		shopNameDP_str = new ArrayList<>();
		orderstatusDP_str = new ArrayList<>();
		productDP_str = new ArrayList<>();
		zoneDetailsDP_str = new ArrayList<>();
		areaDetailsDP_str = new ArrayList<>();
		routeDetailsDP_str = new ArrayList<>();
		routeDp_str = new ArrayList<>();

		orderNumInvoice.setOnClickListener(this);
		//remarksET.setOnClickListener(this);
		list_li.setOnClickListener(this);
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
					//visible
					capture.setVisibility(View.VISIBLE);

					handleTaskWithUserPermission(CAMERA_REQUES_CODE);
					//openCamera();
				}
				else
				{
					//gone
					capture.setVisibility(View.GONE);
					list_li.setVisibility(View.VISIBLE);
				}
			}
		});


		submit.setOnClickListener(this);

		/*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
		{
			if (getFromPref(this, ALLOW_KEY))
			{
				showSettingsAlert();
			}
			else if (ContextCompat.checkSelfPermission(this,
			                                           Manifest.permission.CAMERA)

					!= PackageManager.PERMISSION_GRANTED)
			{

				// Should we show an explanation?
				if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				                                                        Manifest.permission.CAMERA))
				{
					showAlert();
				}
				else
				{
					// No explanation needed, we can request the permission.
					ActivityCompat.requestPermissions(this,
					                                  new String[]{Manifest.permission.CAMERA},
					                                  MY_PERMISSIONS_REQUEST_CAMERA);
				}
			}
		}*/

	}


	public String serviceCall()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://202.143.96.20/Orderstest/api/Services/Saveorderbooking?OrderBookingDate={\"OrderStatusId\":\"1\",\"ShopId\":\"1\",\"OrderDate\":\"4/7/2017\",\"IsShopClosed\":\"N\",\"ShopClosedImage\":\"\",\"IsOrdered\":\"Y\",\"IsInvoice\":\"Y\",\"Remarks\":\"\",\"EmployeeId\":\"4\"}&ProductList=[{\"ProductDescription\":\"Bright It Loose \",\"Price\":10,\"GST\":0,\"VAT\":14.5,\"ProductName\":\"Bright It Loose \",\"Id\":1,\"Quantity\":1,\"ProductId\":3},{\"ProductDescription\":\"Bright It 3kg \",\"Price\":10,\"GST\":0,\"VAT\":14.5,\"ProductName\":\"Bright It 3kg \",\"Id\":2,\"Quantity\":1,\"ProductId\":2}]");

		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("OrderBookingDate", ""));
			nameValuePairs.add(new BasicNameValuePair("ProductList", ""));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

//	public class UserLoginTask extends AsyncTask<String, String, String>
//	{
//		ProgressDialog pd = new ProgressDialog(Order.this);
//
//		@Override
//		protected void onPreExecute()
//		{
//			// TODO Auto-generated method stub
//
//			pd.setMessage("Please wait...");
//			pd.setIndeterminate(false);
//			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			pd.setCancelable(false);
//			pd.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params)
//		{
//			// TODO: attempt authentication against a network service.
//
//			String result = serviceCall();
//			//Toast.makeText(LoginActivity.this,""+result,Toast.LENGTH_SHORT).show();
//			Log.d("loginResult", result);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String success)
//		{
//			Log.d("successData", String.valueOf(success));
//			pd.dismiss();
//
//		}
//	}
	// display current date
  /*  public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.order_date);
        dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append("Order Date  ")
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        dpResult.init(year, month, day, null);

    }*/

	public static Map<String, String> insertFeedback(String EmployeeId, String FeedbackMessages)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("EmployeeId", EmployeeId);
		map.put("FeedbackMessages", FeedbackMessages);
		return map;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		/*// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			Bitmap bp = (Bitmap) data.getExtras().get("data");
			capture.setImageBitmap(bp);
			capturedImgaestr = BitMapToString(bp);

			cameracaptured = true;
			Toast.makeText(getApplicationContext(), "Captured successfully.", Toast.LENGTH_SHORT).show();
		}
		else
		{
			cameracaptured = false;
			Toast.makeText(getApplicationContext(), "Captured Failed.", Toast.LENGTH_SHORT).show();
		}*/
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
//				DateFormat dateInstance = SimpleDateFormat.getDateInstance();
//				String orderDate = dateInstance.format(Calendar.getInstance().getTime());
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
					if (capturedImgaestr != null && !capturedImgaestr.isEmpty())
					{
						ShopClosedImage = "captured";// capturedImgaestr;
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
				//String OrderDeliveryDate = or;
				String IsOrdered = "Y";
				String IsInvoice = "N";
				String Remarks = remarksET.getText().toString();
				String EmployeeId = "";
				String employeeId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeId");
				if (employeeId != null && !employeeId.isEmpty())
				{
					EmployeeId = employeeId;
				}

				if (Remarks == null || Remarks.isEmpty())
				{
					Remarks = "";
				}

				String jsonString = createJsonOrderSubmit(orderNumber, ZoneId, RouteId, AreaId,
				                                          selected_shopId, IsShopClosed, ShopClosedImage, OrderDeliveryDate,
				                                          OrderStatusId, IsOrdered, IsInvoice, Remarks,
				                                          EmployeeId, cartItemsArray);
				Log.e("parameters", jsonString + "");
				HttpAdapter.orderbook(this, "orderbook", jsonString);
			}
		}
	}

	private void headers()
	{
		android.widget.TableRow row = new TableRow(this);

		TextView taskdate = new TextView(Order.this);
		taskdate.setTextSize(15);
		taskdate.setPadding(10, 10, 10, 10);
		taskdate.setText("Prod");
		taskdate.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskdate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskdate);

		TextView title = new TextView(Order.this);
		title.setText("Prc");
		title.setBackgroundColor(getResources().getColor(R.color.light_green));
		title.setTextSize(15);
		title.setPadding(10, 10, 10, 10);
		title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(title);


		TextView taskhour = new TextView(Order.this);
		taskhour.setText("Qty");
		taskhour.setBackgroundColor(getResources().getColor(R.color.light_green));
		taskhour.setTextSize(15);
		taskhour.setPadding(10, 10, 10, 10);
		taskhour.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                   TableRow.LayoutParams.WRAP_CONTENT));
		row.addView(taskhour);

		TextView description3 = new TextView(Order.this);
		description3.setText("Fres");
		description3.setBackgroundColor(getResources().getColor(R.color.light_green));
		description3.setTextSize(15);
		description3.setPadding(10, 10, 10, 10);
		row.addView(description3);
		description3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));

		TextView description = new TextView(Order.this);
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
		row.addView(description2);
		description2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
		                                                       TableRow.LayoutParams.WRAP_CONTENT));


		tableLayout.addView(row, new TableLayout.LayoutParams(
				TableLayout.LayoutParams.MATCH_PARENT,
				TableLayout.LayoutParams.WRAP_CONTENT));

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
						paymentDP.clear();
						paymentDP_str.clear();
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
						/*ArrayAdapter<String> dataAdapter_shopName = new ArrayAdapter<String>(this,
						                                                                     android.R.layout.simple_spinner_item, shopNameDP_str);*/
						ArrayAdapter<String> dataAdapter_shopName = new ArrayAdapter<String>(this,
						                                                                     android.R.layout.simple_spinner_item, shopNamestitle);
						dataAdapter_shopName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						shopName_sp.setAdapter(dataAdapter_shopName);
						shopName_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
						{
							@Override
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								try
								{
									//ShopId = String.valueOf(position);
									if (position != 0)
									{
										position = position - 1;
										selected_shopId = _shopNamesData.get(position).getShopId();
										ShopId = selected_shopId;
										//shopNameDropDown = shopsDP.get(position - 1).ShopId;
										Log.e("selected_shopId", selected_shopId);
									}
								}
								catch (Exception e)
								{
									Log.e("", e + "");
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
									orderStatusDropDown = orderstatusDP.get(position - 1).OrderId;
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent)
							{

							}
						});
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
							GetProductCategory getProductCategory = new Gson().fromJson(obj.toString(), GetProductCategory.class);
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
							public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
							{
								productCategoryId = String.valueOf(position);
								if (position != 0)
								{
									// productNameDropDown = productDP.get(position).ProductId;
									list.add(productDP.get(position - 1));
									displayTableView(list);
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
									HttpAdapter.getRouteDetails(Order.this, "routeName", zoneNameDropdown);
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
									HttpAdapter.getShopDetailsDP(Order.this, "shopName", areaNameDropDown);
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
						routeDetailsDP_str.add("Route Number");
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject obj = jsonArray.getJSONObject(i);
							GetRouteDetails getRoute = new Gson().fromJson(obj.toString(), GetRouteDetails.class);
							routeDetailsDP.add(getRoute);
							routeDetailsDP_str.add(getRoute.RouteName);

						}


						//Route no new
						_routeNamesData.clear();
						routenostitle.clear();
						_routeNamesData = new ArrayList<ShopNamesData>();
						for (int i = 0; i < jsonArray.length(); i++)
						{
							JSONObject jsnobj = jsonArray.getJSONObject(i);
							String shopId = jsnobj.getString("RouteId");
							String shopNamee = jsnobj.getString("RouteName");
							_routeNamesData.add(new ShopNamesData(shopId, shopNamee));
						}
						routenostitle.add("Route Number");

						if (_routeNamesData.size() > 0)
						{
							for (int i = 0; i < _routeNamesData.size(); i++)
							{
								routenostitle.add(_routeNamesData.get(i).getShopId());
							}
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
									try
									{
										RouteId = String.valueOf(position);
										routeNameDropDown = routeDetailsDP.get(position - 1).RouteId;
										HttpAdapter.getAreaDetailsByRoute(Order.this, "areaNameDP", routeNameDropDown);
									/*	selected_routeId = _routeNamesData.get(routeName_sp.getSelectedItemPosition()).getShopId();
										RouteId = selected_routeId;*/
									}
									catch (Exception e)
									{

										Log.e("error", e + "");
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
				else if (response.getTag().equals("orderbook"))
				{
					if (mJson.getString("Message").equalsIgnoreCase("SuccessFull"))
					{
						Log.e("response", mJson.getString("Message").equalsIgnoreCase("SuccessFull") + "Success");
						Toast.makeText(mContext, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
						dailogBoxAfterSubmit();
//						refreshActivity();
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
	                                     String OrderDeliveryDate, String OrderStatusId, String IsOrdered, String IsInvoice,
	                                     String Remarks, String EmployeeId, JSONArray cartItemsArray
	)
	{
		JSONObject studentsObj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		try
		{
			dataObj.putOpt("OrderNumber", OrderNumber);
			dataObj.putOpt("ZoneId", ZoneId);
			dataObj.putOpt("RouteId", RouteId);
			dataObj.putOpt("AreaId", AreaId);
			dataObj.putOpt("ShopId", ShopId);
			dataObj.putOpt("IsShopClosed", IsShopClosed);
			dataObj.putOpt("ShopClosedImage", ShopClosedImage);
			dataObj.putOpt("OrderDeliveryDate", OrderDeliveryDate);
			dataObj.putOpt("OrderStatusId", OrderStatusId);
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
		AlertDialog alertDialog = new AlertDialog.Builder(Order.this).create();
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
				                      ActivityCompat.requestPermissions(Order.this,
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
							saveToPreferences(Order.this, ALLOW_KEY, true);
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

	public static void startInstalledAppDetailsActivity(final Activity context)
	{
		if (context == null)
		{
			return;
		}

		final Intent i = new Intent();
		i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.setData(Uri.parse("package:" + context.getPackageName()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		context.startActivity(i);
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

	private class ProductCategoryTableRow extends TableRow
	{

		private Context mContext;
		private GetProductCategory mProductCategory;

		private EditText quantityETID;
		private EditText fresETID;

		private String afterTextChanged = "";
		private String beforeTextChanged = "";
		private String onTextChanged = "";

		private int position;

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
				taskdate.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.36f));
				addView(taskdate);

				TextView title = new TextView(mContext);
				title.setText(mProductCategory.ProductPrice);
				title.setTextSize(15);
				title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
				                                                TableRow.LayoutParams.WRAP_CONTENT));
				addView(title);

				quantityETID = new EditText(mContext);
				quantityETID.setText(mProductCategory.Quantity);
				quantityETID.setBackgroundColor(Color.TRANSPARENT);
				quantityETID.setClickable(true);
				quantityETID.setCursorVisible(true);
				quantityETID.setFocusableInTouchMode(true);
				quantityETID.setTextSize(15);
				quantityETID.setInputType(InputType.TYPE_CLASS_NUMBER);
				quantityETID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
				                                                       TableRow.LayoutParams.WRAP_CONTENT));
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
				fresETID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
				                                                   TableRow.LayoutParams.WRAP_CONTENT));
				fresETID.addTextChangedListener(mTextWatcherFres);
				addView(fresETID);


				TextView description = new TextView(mContext);
				description.setText(mProductCategory.VAT);
				description.setTextSize(15);
				//description.setPadding(15,0,0,0);
				description.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
				                                                      TableRow.LayoutParams.WRAP_CONTENT));
				addView(description);

				TextView description2 = new TextView(mContext);
				description2.setText(mProductCategory.GST);
				description2.setTextSize(15);
				description2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
				                                                       TableRow.LayoutParams.WRAP_CONTENT));
				addView(description2);
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
			Toast.makeText(getApplicationContext(), "Please Enter Area Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (ShopId == null || ShopId.isEmpty() || ShopId.equals("0"))
		{
			Toast.makeText(getApplicationContext(), "Please Select Shop Name", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		if (OrderStatusId == null || OrderStatusId.isEmpty() || OrderStatusId.equals("0"))
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
			if (paymentTermsId == null || paymentTermsId.isEmpty() || paymentTermsId.equals("0"))
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
		//captured_img_str = BitMapToString(captured_img_bitMap);
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

					Intent in = new Intent(Order.this, Order.class);
					Util.killorderBook();
					startActivity(in);
				}
				else if (check2)
				{
					Intent inten = new Intent(Order.this, Invoice.class);
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
}


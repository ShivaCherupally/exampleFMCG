package com.fmcg.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.adapter.RouteCheckListAdapter;
import com.fmcg.models.GetRouteDetails;
import com.fmcg.models.RouteDetailsData;
import com.fmcg.models.ShopNamesData;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.util.AlertDialogManager;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RouteDetails extends AppCompatActivity implements NetworkOperationListener
{
	public static Activity routeActiviy;
	//	private TextView employeeName, routeName, routeDate, noPlantxt, areanametxt, zonenametxt, route_number, accepttxt;
	private TextView purpose, routestartDate, routeEndDate, targetType, targetAmount;
	LinearLayout sublayout, acceptLLID;
	Context mContext;
	Spinner routeNoSpnr;
	public List<GetRouteDetails> routeDetailsDP;
	private List<String> routeDetailsDP_str;
	ArrayList<ShopNamesData> _routeNamesData = new ArrayList<ShopNamesData>();
	ArrayList<String> routenostitle = new ArrayList<String>();
	String selected_routeId = "";
	private Button acceptBtn, resetBtn;


	////Route Number 
	ArrayList<ShopNamesData> _routeCodesData = new ArrayList<ShopNamesData>(); //Route Drop Down
	ArrayList<String> routeNamestitle = new ArrayList<String>();
	String employeeRoutId = "";

	String EmployeeId = "";

	///Route List Details
	ArrayList<RouteDetailsData> _routeDetailsData = new ArrayList<RouteDetailsData>();
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	TextView noPlantxt, accepttxt;

	///Dailog
	private Dialog promoDialog;
	private ImageView close_popup;
	RadioGroup select_option_radio_grp;
	RadioButton orderBook, inovice;
	Button alert_submit;
	boolean check1 = false;
	boolean check2 = false;

	ProgressDialog progressDailog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routedetails_list);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = RouteDetails.this;
		routeActiviy = RouteDetails.this;

		progressDailog = new ProgressDialog(RouteDetails.this);
		/*employeeName = (TextView) findViewById(R.id.employee_name);
		routeName = (TextView) findViewById(R.id.routeNametxt);
		routeDate = (TextView) findViewById(R.id.route_date);
		zonenametxt = (TextView) findViewById(R.id.zonenametxt);
		areanametxt = (TextView) findViewById(R.id.areanametxt);
		route_number = (TextView) findViewById(R.id.route_number);


		purpose = (TextView) findViewById(R.id.purpose);
		routestartDate = (TextView) findViewById(R.id.routestartDate);
		routeEndDate = (TextView) findViewById(R.id.routeEndDate);
		targetType = (TextView) findViewById(R.id.targetType);
		targetAmount = (TextView) findViewById(R.id.targetAmount);




		routeNoSpnr = (Spinner) findViewById(R.id.routeNoSpnr);*/

		noPlantxt = (TextView) findViewById(R.id.noPlantxt);
		accepttxt = (TextView) findViewById(R.id.accepttxt);

		acceptBtn = (Button) findViewById(R.id.acceptBtn);
		resetBtn = (Button) findViewById(R.id.resetBtn);


//		sublayout = (LinearLayout) findViewById(R.id.sublayout);
//		sublayout.setVisibility(View.GONE);
//		byDefaultSelctRouteNo();


		acceptLLID = (LinearLayout) findViewById(R.id.acceptLLID);
		acceptLLID.setVisibility(View.GONE);
		String accepted = SharedPrefsUtil.getStringPreference(mContext, "PLAN_STARTED");
		if (accepted != null && !accepted.isEmpty())
		{
			/*String avalible_list_str = SharedPrefsUtil.getStringPreference(mContext, "AVAILABLE_LIST");
			Log.e("AVAILABLE_LIST", avalible_list_str);

			convertStringToArraylist(avalible_list_str);
			System.out.println(myList);

			for (int i = 0; i < myList.size(); i++)
			{
				RouteDetailsData singleStudent = myList.get(i);
				if (singleStudent.isSelected() == true)
				{
					//data = data + "\n" + String.valueOf(singleStudent.getRouteId()).toString();
					//employeeRoutId = String.valueOf(singleStudent.getRouteId());
					selectedrouteNos.put(String.valueOf(singleStudent.getRouteId()).toString());
				}
				else
				{
					*//*final List<RouteDetailsData> objs = stList;
					objs.remove(singleStudent.getRouteId());*//*
				}*/
			//}

			/*if (accepted.equals("ACCEPTED"))
			{
				accepttxt.setText("Accepted");
			}*/
			HttpAdapter.getRouteDetailsByEmployee(RouteDetails.this, "RouteDetailsModel", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
		}
		else
		{
			HttpAdapter.getRouteDetailsByEmployee(RouteDetails.this, "RouteDetailsModel", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));
			//accepttxt.setText("Accept");
		}


		/*accepttxt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				acceptedSubmitd();
			}
		});*/

		/*acceptLLID.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				progressDailog.setMessage("Please wait...");
				progressDailog.setIndeterminate(false);
				progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDailog.setCancelable(false);
				progressDailog.show();
				acceptedSubmitd();
			}
		});*/

		acceptBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				progressDailog.setMessage("Please wait...");
				progressDailog.setIndeterminate(false);
				progressDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDailog.setCancelable(false);
				progressDailog.show();
				acceptedSubmitd();
			}
		});

		resetBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				refreshActivity();
			}
		});


		try
		{
			EmployeeId = SharedPrefsUtil.getStringPreference(mContext, "EmployeeId");
			Log.e("EmpolyeeId", EmployeeId);
		}
		catch (Exception e)
		{

		}


//		HttpAdapter.getRouteDetailsByEmployee(RouteDetails.this, "RouteDetailsModel", SharedPrefsUtil.getStringPreference(mContext, "EmployeeId"));


	}

	private void dailogBoxAfterSubmit()
	{
		promoDialog = new Dialog(this);
		promoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		promoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		promoDialog.setCancelable(false);
		promoDialog.setContentView(R.layout.popup_starttrip);
		close_popup = (ImageView) promoDialog.findViewById(R.id.close_popup);
		alert_submit = (Button) promoDialog.findViewById(R.id.alert_startTrip);
		promoDialog.show();

		/*select_option_radio_grp = (RadioGroup) promoDialog.findViewById(R.id.select_option_radio_grp);
		orderBook = (RadioButton) promoDialog.findViewById(R.id.orderBook);
		inovice = (RadioButton) promoDialog.findViewById(R.id.inovice);
		orderBook.setVisibility(View.GONE);
		inovice.setVisibility(View.GONE);*/

		/*select_option_radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
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


		});*/

		close_popup.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (promoDialog != null)
				{
					promoDialog.dismiss();
					Util.hideSoftKeyboard(mContext, v);
					SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "ACCEPTED");
					refreshActivity();
				}
			}
		});

		alert_submit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "ACCEPTED");
				Intent in = new Intent(RouteDetails.this, GetShopsByRoute.class);
				Util.killRouteDetails();
				startActivity(in);
				/*if (check1)
				{
					SharedPrefsUtil.setStringPreference(mContext, "ORDER_ACCEPTED", "ACTIVE");
					SharedPrefsUtil.setStringPreference(mContext, "INVOICE_ACCEPTED", "");
					SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "ACCEPTED");
					Intent in = new Intent(RouteDetails.this, Order.class);
					Util.killRouteDetails();
					startActivity(in);
				}
				else if (check2)
				{
					SharedPrefsUtil.setStringPreference(mContext, "ORDER_ACCEPTED", "");
					SharedPrefsUtil.setStringPreference(mContext, "INVOICE_ACCEPTED", "ACTIVE");
					SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "ACCEPTED");
					Intent inten = new Intent(RouteDetails.this, Invoice.class);
					Util.killRouteDetails();
					startActivity(inten);
				}
				else
				{
					Toast.makeText(mContext, "Please Select Order Book or Invoice", Toast.LENGTH_SHORT).show();
				}*/

			}
		});

	}

	private void acceptedSubmitd()
	{
		try
		{
			String data = "";
			JSONArray selectedrouteNos = new JSONArray();
			List<RouteDetailsData> stList = ((RouteCheckListAdapter) mAdapter).getStudentist();

			String alreadyAccessList = Arrays.toString(stList.toArray());
			Log.e("availlist", alreadyAccessList);
			SharedPrefsUtil.setStringPreference(mContext, "AVAILABLE_LIST", alreadyAccessList);

			if (stList.size() != 0)
			{
				for (int i = 0; i < stList.size(); i++)
				{
					RouteDetailsData singleStudent = stList.get(i);
					if (singleStudent.isSelected() == true)
					{
						data = data + "\n" + String.valueOf(singleStudent.getRouteId()).toString();
						employeeRoutId = String.valueOf(singleStudent.getRouteId());
						selectedrouteNos.put(String.valueOf(singleStudent.getRouteId()).toString());

					}
					else
					{
						acceptLLID.setVisibility(View.VISIBLE);
					/*final List<RouteDetailsData> objs = stList;
					objs.remove(singleStudent.getRouteId());*/
					}
				}
				if (employeeRoutId != null && !employeeRoutId.isEmpty())
				{
					String selcetdRoutesStr = selectedrouteNos.toString();
					Log.e("selcetdRoutesJSONARRay", selcetdRoutesStr + "");
					selcetdRoutesStr = selcetdRoutesStr.replace("[", "");
					selcetdRoutesStr = selcetdRoutesStr.replace("]", "");
					selcetdRoutesStr = selcetdRoutesStr.replaceAll("\"", "");
					selcetdRoutesStr = selcetdRoutesStr.replaceAll("\"", "");

					Log.e("selcetdRoutesStr", selcetdRoutesStr + "");

//				String jsonString = new Gson().toJson(insertRouteIds(selcetdRoutesStr));
//				Log.d("jsonString", jsonString);
					HttpAdapter.routeAccept(RouteDetails.this, "acceptRoute", selcetdRoutesStr);
				}
				else
				{
					progressDailog.dismiss();
					Toast.makeText(mContext, "Please Select Route Number", Toast.LENGTH_SHORT).show();
				}

			/*if (!data.isEmpty() && data != null)
			{
				Toast.makeText(RouteDetails.this, "Selected RouteId : \n" + data, Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(RouteDetails.this, "Please select at least one route number.", Toast.LENGTH_LONG).show();
			}*/

			}
			else
			{
				progressDailog.dismiss();
			}
		}
		catch (Exception e)
		{

		}






		/*else
		{
			Toast.makeText(mContext, "Your My Day Plan Already Accepted", Toast.LENGTH_SHORT).show();
		}*/

	}


	private void refreshActivity()
	{
		Intent i = getIntent();
		finish();
		startActivity(i);
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

				if (response.getTag().equals("RouteDetailsModel"))
				{
					if (mJson.getString("Status").equals("OK"))
					{
						if (mJson.getString("Data").equals("null"))
						{
							progressDailog.dismiss();
//							sublayout.setVisibility(View.GONE);
							///acceptLLID.setVisibility(View.GONE);
							noPlantxt.setVisibility(View.VISIBLE);
							SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "NOT_ACCEPTED");
							AlertDialogManager.showAlertOnly(this, "Route Details", "No Routes Found As per Now", "Ok");
						}
						else
						{
							try
							{
//								sublayout.setVisibility(View.GONE);
								//acceptLLID.setVisibility(View.GONE);
								noPlantxt.setVisibility(View.GONE);
								JSONArray jsonArray = mJson.getJSONArray("Data");
//								routeNoSpinnerAdapter(jsonArray);
								routeListDetails(jsonArray);
							}
							catch (Exception e)
							{
								Log.e("error", e + "");
							}
						}

					}
				}
				else if (response.getTag().equals("acceptRoute"))
				{
					try
					{
						if (mJson.getString("Message").equals("SuccessFull"))
						{
							progressDailog.dismiss();
							Toast.makeText(mContext, "My Day Plan Successfully Accepted", Toast.LENGTH_SHORT).show();
							dailogBoxAfterSubmit();

//							accepttxt.setText("Accepted");
							//refreshActivity();
						}
					}
					catch (Exception e)
					{
						progressDailog.dismiss();
					}
				}
				else if (response.getTag().equals("routeDetails"))
				{
				/*	try
					{
						if (mJson.getString("Message").equals("SuccessFull"))
						{
							sublayout.setVisibility(View.VISIBLE);
							acceptLLID.setVisibility(View.VISIBLE);
							noPlantxt.setVisibility(View.GONE);

							JSONObject jsonObject = mJson.getJSONObject("Data");

							if (jsonObject.getString("EmployeeName") != null && !jsonObject.getString("EmployeeName").equalsIgnoreCase("null"))
							{
								employeeName.setText(jsonObject.getString("EmployeeName"));
							}
							if (jsonObject.getString("ZoneName") != null && !jsonObject.getString("ZoneName").equalsIgnoreCase("null"))
							{
								zonenametxt.setText(jsonObject.getString("ZoneName"));
							}

							if (jsonObject.getString("RouteName") != null && !jsonObject.getString("RouteName").equalsIgnoreCase("null"))
							{
								routeName.setText(jsonObject.getString("RouteName"));
							}

							if (jsonObject.getString("Purpose") != null && !jsonObject.getString("Purpose").equalsIgnoreCase("null"))
							{
								purpose.setText(jsonObject.getString("Purpose"));
							}

							if (jsonObject.getString("RouteStartDate") != null && !jsonObject.getString("RouteStartDate").isEmpty())
							{
								String routeDateStr = jsonObject.getString("RouteStartDate");
								Log.e("routeDate", routeDateStr);
								SimpleDateFormat simDf = new SimpleDateFormat("yyyy-MM-dd");
								try
								{
									Date yourDate = simDf.parse(routeDateStr);
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(yourDate);
									calendar.get(Calendar.YEAR); //Day of the Year :)
									calendar.get(Calendar.DATE); //Day of the day :)
									int orderMonth = calendar.get(Calendar.MONTH) + 1;
									String orderDatetemp = calendar.get(Calendar.DATE) + "-" + orderMonth + "-" + calendar.get(Calendar.YEAR);
									routestartDate.setText(orderDatetemp + "");
									Log.e("startdate", calendar.get(Calendar.YEAR) + "");
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}

							if (jsonObject.getString("RouteEndDate") != null && !jsonObject.getString("RouteEndDate").isEmpty())
							{
								String routeDateStr = jsonObject.getString("RouteEndDate");
								Log.e("routeDate", routeDateStr);
								SimpleDateFormat simDf = new SimpleDateFormat("yyyy-MM-dd");
								try
								{
									Date yourDate = simDf.parse(routeDateStr);
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(yourDate);
									calendar.get(Calendar.YEAR); //Day of the Year :)
									calendar.get(Calendar.DATE); //Day of the day :)
									int orderMonth = calendar.get(Calendar.MONTH) + 1;
									String orderDatetemp = calendar.get(Calendar.DATE) + "-" + orderMonth + "-" + calendar.get(Calendar.YEAR);
									routeEndDate.setText(orderDatetemp + "");
									Log.e("enddate", calendar.get(Calendar.YEAR) + "");
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}

							if (jsonObject.getString("TargetType") != null && !jsonObject.getString("TargetType").equalsIgnoreCase("null"))
							{
								if (jsonObject.getString("TargetType").equals("M"))
								{
									targetType.setText("Monthly");
								}
								else if (jsonObject.getString("TargetType").equals("W"))
								{
									targetType.setText("Weekly");
								}
								else if (jsonObject.getString("TargetType").equals("D"))
								{
									targetType.setText("Daily");
								}
								//purpose.setText(jsonObject.getString("Purpose"));
							}
							if (jsonObject.getString("TargetAmount") != null)
							{
								targetAmount.setText(jsonObject.getString("TargetAmount") + "");
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();

					}*/
				}
				//routeDetails

			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			progressDailog.dismiss();
			Toast.makeText(mContext, "Failed to accept..", Toast.LENGTH_SHORT).show();
		}

	}

	private void routeListDetails(final JSONArray jsonArray)
	{
		_routeDetailsData = new ArrayList<RouteDetailsData>();
		try
		{
			if (jsonArray.length() != 0)
			{
				for (int i = 0; i < jsonArray.length(); i++)
				{
					String ZoneName = "";
					String RouteNumber = "";
					String TargetAmount = "";
					String RouteAcceptFlag = "";
					boolean isChecked = false;
					JSONObject jObj = jsonArray.getJSONObject(i);
					int RouteId = jObj.getInt("EmployeeRouteId"); //EmployeeRouteId
					if (jObj.getString("ZoneName") != null && !jObj.getString("ZoneName").equalsIgnoreCase("null"))
					{
						ZoneName = jObj.getString("ZoneName");
					}

					if (jObj.getString("RouteNumber") != null && !jObj.getString("RouteNumber").equalsIgnoreCase("null"))
					{
						RouteNumber = jObj.getString("RouteNumber");
					}

					if (jObj.getString("Purpose") != null)
					{
						//"Purpose" -> "Order and Billing"
						TargetAmount = jObj.getString("Purpose");
					}

					if (jObj.getString("RouteAcceptFlag") != null)
					{
						//"Purpose" -> "Order and Billing"
						RouteAcceptFlag = jObj.getString("RouteAcceptFlag");
					}

					//RouteAcceptFlag
//					if ()
					if (RouteAcceptFlag != null && !RouteAcceptFlag.equalsIgnoreCase("null"))
					{
						if (RouteAcceptFlag.equalsIgnoreCase("Y"))
						{
							isChecked = true;
//							isChecked = false;
						}
						else
						{
							isChecked = false;
						}

					}
					_routeDetailsData.add(new RouteDetailsData(RouteId, ZoneName, RouteNumber, TargetAmount, isChecked));

				}

				adapterAssigning(_routeDetailsData);
			}
			else
			{
				noPlantxt.setVisibility(View.VISIBLE);
				acceptLLID.setVisibility(View.GONE);
				acceptBtn.setVisibility(View.GONE);
				resetBtn.setVisibility(View.GONE);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();

		}

	}

	private void adapterAssigning(ArrayList<RouteDetailsData> _routeDetailsData)
	{
		if (_routeDetailsData.size() != 0)
		{
			mRecyclerView = (RecyclerView) findViewById(R.id.routeRecyclerView);
			mRecyclerView.setHasFixedSize(true);
			mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
			mAdapter = new RouteCheckListAdapter(_routeDetailsData);
			mRecyclerView.setAdapter(mAdapter);
			acceptLLID.setVisibility(View.GONE);
			checkRouteNosChecking();
		}
		else
		{

			noPlantxt.setVisibility(View.VISIBLE);
			acceptLLID.setVisibility(View.GONE);
			acceptBtn.setVisibility(View.GONE);
		}

	}

	@Override
	public void showToast(String string, int lengthLong)
	{

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
				int shopId = jsnobj.getInt("RouteId");
				String shopNamee = jsnobj.getString("RouteNumber");
				_routeCodesData.add(new ShopNamesData(String.valueOf(shopId), shopNamee));
			}
			routeNamestitle.add("Select Route Number");
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
		dataAdapter_routeName.setDropDownViewResource(R.layout.list_item);
		routeNoSpnr.setAdapter(dataAdapter_routeName);

		routeNoSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position != 0)
				{
					employeeRoutId = _routeCodesData.get(position - 1).getShopId();
					Log.e("employeeRoutId", employeeRoutId);
					HttpAdapter.getrouteDetails(RouteDetails.this, "routeDetails", EmployeeId, employeeRoutId);

				}
				else
				{
					acceptLLID.setVisibility(View.GONE);
					sublayout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
	}

	private void byDefaultSelctRouteNo()
	{
		routeNamestitle.clear();
		routeNamestitle.add("Select Route Number");
		ArrayAdapter<String> dataAdapter_routeName = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeNamestitle);
		dataAdapter_routeName.setDropDownViewResource(R.layout.list_item);
		routeNoSpnr.setAdapter(dataAdapter_routeName);

		sublayout.setVisibility(View.GONE);
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

	/*public static JSONArray remove(final int idx, final JSONArray from, RouteDetailsData singleStudent)
	{
		final List<JSONObject> objs = singleStudent(from);
		objs.remove(idx);
		final JSONArray ja = new JSONArray();
		for (final JSONObject obj : objs)
		{
			ja.put(obj);
		}

		return ja;
	}*/

	public static Map<String, String> insertRouteIds(String selectedRouteids)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("EmployeeRouteId", selectedRouteids);
		return map;
	}

	public ArrayList<Character> convertStringToArraylist(String str)
	{
		ArrayList<Character> charList = new ArrayList<Character>();
		for (int i = 0; i < str.length(); i++)
		{
			charList.add(str.charAt(i));

		}
		return charList;
	}

	private void checkRouteNosChecking()
	{
		try
		{
			String data = "";
			JSONArray selectedrouteNos = new JSONArray();
			List<RouteDetailsData> stList = ((RouteCheckListAdapter) mAdapter).getStudentist();

			String alreadyAccessList = Arrays.toString(stList.toArray());
			Log.e("availlist", alreadyAccessList);
			SharedPrefsUtil.setStringPreference(mContext, "AVAILABLE_LIST", alreadyAccessList);

			if (stList.size() != 0)
			{
				for (int i = 0; i < stList.size(); i++)
				{
					RouteDetailsData singleStudent = stList.get(i);
					if (singleStudent.isSelected() == true)
					{
						data = data + "\n" + String.valueOf(singleStudent.getRouteId()).toString();
						employeeRoutId = String.valueOf(singleStudent.getRouteId());
						selectedrouteNos.put(String.valueOf(singleStudent.getRouteId()).toString());
						SharedPrefsUtil.setStringPreference(mContext, "PLAN_STARTED", "ACCEPTED");
					}
					else
					{
						acceptLLID.setVisibility(View.VISIBLE);
					}
				}
				if (employeeRoutId != null && !employeeRoutId.isEmpty())
				{
					String selcetdRoutesStr = selectedrouteNos.toString();
					Log.e("selcetdRoutesJSONARRay", selcetdRoutesStr + "");
					selcetdRoutesStr = selcetdRoutesStr.replace("[", "");
					selcetdRoutesStr = selcetdRoutesStr.replace("]", "");
					selcetdRoutesStr = selcetdRoutesStr.replaceAll("\"", "");
					selcetdRoutesStr = selcetdRoutesStr.replaceAll("\"", "");
					Log.e("selcetdRoutesStr", selcetdRoutesStr + "");
				}
				else
				{
					//Toast.makeText(mContext, "Please Select Route Number", Toast.LENGTH_SHORT).show();
				}
			}
		}
		catch (Exception e)
		{

		}
	}
}

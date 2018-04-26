package com.fmcg.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.fmcg.Dotsoft.R;
import com.fmcg.Activity.HomeActivity.DashboardActivity;
import com.fmcg.Activity.LoginActivity.LoginActivity;
import com.fmcg.network.HttpAdapter;
import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;
import com.fmcg.service.LocationMonitoringService;
import com.fmcg.util.SharedPrefsUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Splashscreen extends AppCompatActivity implements NetworkOperationListener
{

	SharedPreferences sharedPreferences;
	// Splash screen timer
	public static int SPLASH_TIME_OUT = 3000;
	public Context context;

	IntentFilter intentFilter;
	TextView textViewId;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		textViewId = (TextView) findViewById(R.id.textViewId);
		context = Splashscreen.this;

		//getActionBar().hide();
		try
		{
			ActionBar actionBar = getSupportActionBar();
			actionBar.hide();
//			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			textViewId.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/germania_one.ttf"));
			textViewId.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/germania_one.ttf"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}


//		getActionBar().setDisplayShowTitleEnabled(false);

		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
		final String userLogin = sharedPreferences.getString("username", "");


//		DateFormat dateInstance = SimpleDateFormat.getDateInstance();
//		String PresentDate = dateInstance.format(Calendar.getInstance().getTime());
//		SharedPrefsUtil.setStringPreference(context, "LoginTime", PresentDate);
//		Log.e("LoginDate", PresentDate + "Y");

//		DateFormat dateInstance = SimpleDateFormat.getDateInstance();
//		String presentDate = dateInstance.format(Calendar.getInstance().getTime());

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				// This method will be executed once the timer is over
				// Start your app main activity
				String LoginDate = SharedPrefsUtil.getStringPreference(context, "LoginTime");
				DateFormat dateInstance = SimpleDateFormat.getDateInstance();
				String PresentDate = dateInstance.format(Calendar.getInstance().getTime());
				Log.e("LoginDate", LoginDate + "Y");
				Log.e("PresentDate", PresentDate + "Y");

				if (LoginDate != null && !LoginDate.isEmpty())
				{
					if (LoginDate.equals(PresentDate))
					{
						if (userLogin.length() >= 1)
						{

							SharedPrefsUtil.setStringPreference(getApplicationContext(), "LOCATION_SERVICE_RESTART", "ENABLE");
							Intent i = new Intent(Splashscreen.this, DashboardActivity.class);
							startActivity(i);
						}
						else
						{
							loginExpired();
						}
					}
					else
					{
						loginExpired();
					}
				}
				else
				{
					loginExpired();
				}
				finish();


			}
		}, SPLASH_TIME_OUT);
	}

	private void loginExpired()
	{
		HttpAdapter.logoutUser(Splashscreen.this, "LOG_OUT_USER",
		                       SharedPrefsUtil.getStringPreference(getApplicationContext(), "EmployeeId"));

	}




	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public void operationCompleted(final NetworkResponse response)
	{
		if (response.getStatusCode() == 200)
		{
			if (response.getTag().equals("LOG_OUT_USER"))
			{
				SharedPrefsUtil.setStringPreference(context, "PLAN_STARTED", "");
				SharedPrefsUtil.setStringPreference(getApplicationContext(), "USER_LOGOUT", "YES");
				Intent intent = new Intent(this, LocationMonitoringService.class);
				stopService(intent);
				sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				SharedPrefsUtil.setStringPreference(getApplicationContext(), "LOCATION_SERVICE_RESTART", "ENABLE");
				Intent setIntent = new Intent(this, LoginActivity.class);
				setIntent.addCategory(Intent.CATEGORY_HOME);
				setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(setIntent);
				finish();
			}
		}
	}

	@Override
	public void showToast(final String string, final int lengthLong)
	{

	}
}



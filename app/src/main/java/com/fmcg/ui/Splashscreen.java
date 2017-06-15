package com.fmcg.ui;

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
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.fmcg.Dotsoft.R;
import com.fmcg.Dotsoft.util.Common;
import com.fmcg.util.SharedPrefsUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Splashscreen extends AppCompatActivity
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
			textViewId.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf"));
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
		SharedPrefsUtil.setStringPreference(context, "PLAN_STARTED", "");
		Intent next = new Intent(Splashscreen.this, LoginActivity.class);
		startActivity(next);
	}


	@Override
	protected void onResume()
	{
		super.onResume();
	}


}



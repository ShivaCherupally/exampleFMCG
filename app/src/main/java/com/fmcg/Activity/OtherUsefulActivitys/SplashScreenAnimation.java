//package com.fmcg.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import com.daimajia.androidanimations.library.Techniques;
//import com.fmcg.Activity.HomeActivity.DashboardActivity;
//import com.fmcg.Activity.LoginActivity.LoginActivity;
//import com.fmcg.Dotsoft.R;
//import com.fmcg.util.AwesomeSplashScreen;
//import com.fmcg.util.SharedPrefsUtil;
//import com.viksaa.sssplash.lib.cnst.Flags;
//import com.viksaa.sssplash.lib.model.ConfigSplash;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
///**
// * Created by Proxim on 10/7/2017.
// */
//
//public class SplashScreenAnimation extends AwesomeSplashScreen
//{
//	SharedPreferences sharedPreferences;
//	Context context;
//	//DO NOT OVERRIDE onCreate()!
//	//if you need to start some services do it in initSplash()!
//	String userLogin;
//
//	@Override
//	public void initSplash(ConfigSplash configSplash)
//	{
//		context = SplashScreenAnimation.this;
//			/* you don't have to override every property */
//
//		//Customize Circular Reveal
//		configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
//		//Customize Title
//		configSplash.setTitleSplash("Bright Udyog");
//		configSplash.setTitleTextColor(R.color.white);
//		configSplash.setTitleTextSize(35f); //float value
//		configSplash.setAnimTitleDuration(10);
//		configSplash.setAnimTitleTechnique(Techniques.FadeInRight);
//		configSplash.setTitleFont("fonts/germania_one.ttf"); //provide string to your font located in assets/fonts/
//		configSplash.setAnimCircularRevealDuration(500); //int ms
//		configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
//		configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP
//
//		//Choose LOGO OR PATH; if you don't provide String value for path it's logo by default
//
//        /*//Customize Logo
////        configSplash.setLogoSplash(R.drawable.images); //or any other drawable
//        configSplash.setAnimLogoSplashDuration(2000); //int ms
//        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
//
//
//        //Customize Path
////        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
//        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
//        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
//        configSplash.setAnimPathStrokeDrawingDuration(3000);
//        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
//        configSplash.setPathSplashStrokeColor(R.color.Wheat); //any color you want form colors.xml
//        configSplash.setAnimPathFillingDuration(3000);
//        configSplash.setPathSplashFillColor(R.color.Wheat); //path object filling color*/
//
//
//		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
//		userLogin = sharedPreferences.getString("username", "");
////
//	}
//
//	@Override
//	public void animationsFinished()
//	{
//
//		String LoginDate = SharedPrefsUtil.getStringPreference(context, "LoginTime");
//		DateFormat dateInstance = SimpleDateFormat.getDateInstance();
//		String PresentDate = dateInstance.format(Calendar.getInstance().getTime());
//		Log.e("LoginDate", LoginDate + "Y");
//		Log.e("PresentDate", PresentDate + "Y");
//
//		if (LoginDate != null && !LoginDate.isEmpty())
//		{
//			if (LoginDate.equals(PresentDate))
//			{
//				if (userLogin.length() >= 1)
//				{
//					Intent i = new Intent(SplashScreenAnimation.this, DashboardActivity.class);
//					startActivity(i);
//				}
//				else
//				{
//					loginExpired();
//				}
//			}
//			else
//			{
//				loginExpired();
//			}
//		}
//		else
//		{
//			loginExpired();
//		}
//		finish();
//		//transit to another activity here
//		//or do whatever you want
//	}
//
//	private void loginExpired()
//	{
//		SharedPrefsUtil.setStringPreference(context, "PLAN_STARTED", "");
//		Intent next = new Intent(SplashScreenAnimation.this, LoginActivity.class);
//		startActivity(next);
//	}
//}

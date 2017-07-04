package com.fmcg.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fmcg.ui.AddNewCustomer;
import com.fmcg.ui.AddRemainderActivity;
import com.fmcg.ui.GetShopsByRoute;
import com.fmcg.ui.Invoice;
import com.fmcg.ui.Order;
import com.fmcg.ui.PendingListActivity;
import com.fmcg.ui.RemainderListActivity;
import com.fmcg.ui.Remarks;
import com.fmcg.ui.RouteDetails;

import java.util.ArrayList;
import java.util.List;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;


//import com.codefyne.mysrl.OffersListActivity;

public class Util
{
	/**
	 * Hides the soft keyboard
	 */
	public static int PHONE_REQUES_CODE = 1;
	public static Dialog alertDialog;
	public static ImageView close_popup;
	public static TextView phnno;
	public static TextView callnow;
	public static TextView cancel, calltext;
	public static ProgressDialog mProgressDialog;
	public static Context mContext;
	public static Activity pendingListactvity;

	public static HintSpinner<String> defaultHintSpinner;
	private static List<String> defaults;

	public static void hideSoftKeyboard(Context context, View currentFocusedView)
	{
		if (Validate.notNull(currentFocusedView))
		{
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);
		}
	}

	/**
	 * Shows the soft keyboard
	 */
	public static void showSoftKeyboard(Context context, View view)
	{
		if (Validate.notNull(view))
		{
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			view.requestFocus();
			inputMethodManager.showSoftInput(view, 0);
		}
	}


	public static boolean isRem(Context context)
	{
		boolean isRem = SharedPrefsUtil.getBooleanPreference(context, "remember", true);
		return isRem;
	}


	@SuppressWarnings("deprecation")


	public static void shareToGMail(Context context, String email)
	{
		try
		{
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					"mailto", email, null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			context.startActivity(Intent.createChooser(emailIntent, null));
		}
		catch (Exception e)
		{

		}
	}


	public static boolean isCity(Context context)
	{
		String city = SharedPrefsUtil.getStringPreference(context, "selectedcity");

		if (Validate.notEmpty(city))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public static void showCityAlert(Context context)
	{
		try
		{
			android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
					context, AlertDialog.THEME_HOLO_LIGHT);
			builder.setMessage("Please select city")
			       .setCancelable(false)
			       .setPositiveButton("Ok",
			                          new DialogInterface.OnClickListener()
			                          {
				                          public void onClick(DialogInterface dialog, int which)
				                          {
					                          dialog.cancel();
				                          }
			                          });
			builder.show();
		}
		catch (Exception e)
		{

		}
	}


	public static void hideProgressDialog()
	{
		try
		{
			if (mProgressDialog != null)
			{
				mProgressDialog.dismiss();
			}
		}
		catch (Exception e)
		{

		}
	}


	public static void showProgressDialog(Context context, String message)
	{

		try
		{
			hideProgressDialog();
			mProgressDialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
			mProgressDialog.setMessage(message);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(false);
//			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
//			                          new DialogInterface.OnClickListener()
//			                          {
//				                          @Override
//				                          public void onClick(DialogInterface dialog, int which)
//				                          {
//					                          dialog.dismiss();
//				                          }
//			                          });
			mProgressDialog.show();
		}
		catch (Exception e)
		{

		}
	}







    /*public static void handleTaskWithUserPermission(final Context context, int requestCode, final String phn) {
        DangerousPermissionUtils.getPermission(context, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE}, requestCode)
                .enqueue(new DangerousPermResponseCallBack() {
                    @Override
                    public void onComplete(final DangerousPermissionResponse permissionResponse) {
                        if (permissionResponse.isGranted()) {
                            if (permissionResponse.getRequestCode() == PHONE_REQUES_CODE) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                                        .checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }

                                try {

                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + phn + ""));
                                    context.startActivity(callIntent);
                                } catch (Exception e) {

                                }


                            }
                        }
                    }
                });
    }*/


	public static void killRemarks()
	{
		if (Remarks.remarks != null)
		{
			Remarks.remarks.finish();
		}
	}

	public static void killRemainderList()
	{
		if (RemainderListActivity.remainderListActivity != null)
		{
			RemainderListActivity.remainderListActivity.finish();
		}
	}

	public static void killAddRemainder()
	{
		if (AddRemainderActivity.addremainder != null)
		{
			AddRemainderActivity.addremainder.finish();
		}
	}

	public static void killRouteDetails()
	{
		if (RouteDetails.routeActiviy != null)
		{
			RouteDetails.routeActiviy.finish();
		}
	}

	public static void killStartTrip()
	{
		if (GetShopsByRoute.startTripActivity != null)
		{
			GetShopsByRoute.startTripActivity.finish();
		}
	}


	public static void killPendingListActivity()
	{
		if (pendingListactvity != null)
		{
			pendingListactvity.finish();
		}
	}


	public static void killAddNewCoustmer()
	{
		if (AddNewCustomer.addCustomeractivity != null)
		{
			AddNewCustomer.addCustomeractivity.finish();
		}
	}

	public static void killorderBook()
	{
		if (Order.orderBookActivity != null)
		{
			Order.orderBookActivity.finish();
		}
	}

	public static void killInvoice()
	{
		if (Invoice.invoiceActivity != null)
		{
			Invoice.invoiceActivity.finish();
		}
	}


	public static Context commonHeaderandActivity(final AppCompatActivity activity)
	{
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
		mContext = activity;
		pendingListactvity = activity;
		return mContext;
	}

	public static String toTitleCase(String str)
	{

		if (str == null)
		{
			return null;
		}

		boolean space = true;
		StringBuilder builder = new StringBuilder(str);
		final int len = builder.length();

		for (int i = 0; i < len; ++i)
		{
			char c = builder.charAt(i);
			if (space)
			{
				if (!Character.isWhitespace(c))
				{
					// Convert to title case and switch out of whitespace mode.
					builder.setCharAt(i, Character.toTitleCase(c));
					space = false;
				}
			}
			else if (Character.isWhitespace(c) || c == '(')
			{
				space = true;
			}
			else
			{
				builder.setCharAt(i, Character.toLowerCase(c));
			}
		}

		return builder.toString();
	}


	public static String getIntegerToString(String amount)
	{

		int amt = 0;
		try
		{
			double dou = 0;
			if (amount.contains("."))
			{
				dou = Double.valueOf(amount);
				amt = (int) (dou);
			}
			else
			{
				amt = Integer.valueOf(amount);
			}
		}
		catch (Exception e)
		{

			Log.e("intexc", e + "");
		}

		return amt + "";
	}

	public static int getIntegerToInteger(String amount)
	{

		int amt = 0;
		try
		{
			amt = Integer.valueOf(amount);
		}
		catch (Exception e)
		{

		}

		return amt;
	}


	public static int convertandcal(String unread, String read)
	{
		int calculated = 0;

		int cal1 = 0;
		int cal2 = 0;
		try
		{

			if (Validate.notEmpty(unread))
			{
				cal1 = Integer.valueOf(unread);
			}
			if (Validate.notEmpty(read))
			{
				cal2 = Integer.valueOf(read);
			}

			calculated = cal1 - cal2;
		}
		catch (Exception e)
		{


		}

		return calculated;
	}

	public static void byDefaultSelectServerIdData(Spinner _spinner, String selectString, ArrayList<String> _textitle, Context mContext)
	{
//		_spinner.selectHint();
		HintSpinner<String> hintSpinner = new HintSpinner<>(
				_spinner,
				// Default layout - You don't need to pass in any layout id, just your hint text and
				// your list data
				new HintAdapter<String>(mContext, selectString, _textitle),
				new HintSpinner.Callback<String>()
				{
					@Override
					public void onItemSelected(int position, String itemAtPosition)
					{
						// Here you handle the on item selected event (this skips the hint selected event)
					}
				});
		hintSpinner.init();

	}

	public static void byDefaultSelectHintData(Spinner _spinner, String selectString, ArrayList<String> _textitle, Context mContext)
	{
		defaultHintSpinner = new HintSpinner<>(
				_spinner,
				// Default layout - You don't need to pass in any layout id, just your hint text and
				// your list data
				new HintAdapter<String>(mContext, selectString, _textitle),
				new HintSpinner.Callback<String>()
				{
					@Override
					public void onItemSelected(int position, String itemAtPosition)
					{
						// Here you handle the on item selected event (this skips the hint selected
						// event)
//						showSelectedItem(itemAtPosition);
					}
				});
		defaultHintSpinner.init();
	}


}



package com.fmcg.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class AlertDialogManager
{

	/**
	 * Function to display alert dialog with event
	 *
	 * @param context
	 * @param title
	 * @param message
	 * @param positiveButton
	 */
	public static void showAlertOnly(Context context, String title, String message, String positiveButton)
	{
		try
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);        // Setting Dialog Title
			TextView titlee = new TextView(context);
			// You Can Customise your Title here
			titlee.setText(title);
			//title.setBackgroundColor(Color.DKGRAY);
			titlee.setPadding(10, 10, 10, 10);
			titlee.setGravity(Gravity.CENTER);
			titlee.setTextColor(Color.BLACK);
			titlee.setTextSize(20);
			// Setting Dialog Title
			builder.setCustomTitle(titlee);
			// Setting Dialog Message
			builder.setMessage(message);

			builder.setNegativeButton(positiveButton, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.cancel();
				}
			});
			// this will solve your error
			AlertDialog alert = builder.create();
			alert.show();
			alert.getWindow().getAttributes();

			TextView textView = (TextView) alert.findViewById(android.R.id.message);
			textView.setTextSize(18);
			Button btn1 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
			btn1.setTextSize(16);
		}
		catch (Exception e)
		{

		}

	}

	public static void showValidationAlertMessage(Context context, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		TextView title = new TextView(context);
		// You Can Customise your Title here
		title.setText("Speedy");
		//title.setBackgroundColor(Color.DKGRAY);
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);
		// Setting Dialog Title
		builder.setCustomTitle(title);
		// Setting Dialog Message
		builder.setMessage(message);

		builder.setNegativeButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		// this will solve your error
		AlertDialog alert = builder.create();
		alert.show();
		alert.getWindow().getAttributes();

		TextView textView = (TextView) alert.findViewById(android.R.id.message);
		textView.setTextSize(18);
		Button btn1 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn1.setTextSize(16);
	}

	public static void showAlertWithoutTitle(Context context, String message, String positiveButton)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setMessage(message);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButton, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		});
		alertDialog.show();
	}
}

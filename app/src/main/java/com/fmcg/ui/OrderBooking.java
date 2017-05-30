package com.fmcg.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;

public class OrderBooking extends AppCompatActivity implements View.OnClickListener
{
	public Spinner shopName, orderStatusName, category, paymentTermNames;
	public CheckBox isShopClosed, ordered, delivered;
	public TextView uploadImage, shopClosed, orderDate, submit;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderbooking);

		//initializing the variables
		shopName = (Spinner) findViewById(R.id.shopName);
		orderStatusName = (Spinner) findViewById(R.id.orderStatusName);
		category = (Spinner) findViewById(R.id.category);
		paymentTermNames = (Spinner) findViewById(R.id.paymentTermName);

		isShopClosed = (CheckBox) findViewById(R.id.isShopClosed);
		ordered = (CheckBox) findViewById(R.id.isOrdered);
		delivered = (CheckBox) findViewById(R.id.isDelivered);

		uploadImage = (TextView) findViewById(R.id.upLoadImage);
		shopClosed = (TextView) findViewById(R.id.shopClosed);
		orderDate = (TextView) findViewById(R.id.orderDate);
		submit = (TextView) findViewById(R.id.submit);


	}

	@Override
	public void onClick(View v)
	{
		StringBuilder result = new StringBuilder();
		result.append("Selected Items:");
		if (isShopClosed.isChecked())
		{
			result.append("\nShop Is Closed");
			Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
		}
		if (ordered.isChecked())
		{
			result.append("\nOrdered");
			Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
		}
		if (delivered.isChecked())
		{
			result.append("\nDelivered");
			Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
		}
	}
}

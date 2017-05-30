package com.fmcg.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.models.Product;

/**
 * Created by RuchiTiwari on 5/3/2017.
 */

public class MainActivity extends Activity
{
	String afterTextChangedss = "";
	String beforeTextChanged = "";
	String onTextChanged = "";
	private MyCustomAdapter dataAdapter = null;
	private Double orderTotal = 0.00;
	private EditText quantity;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_list);

		//Generate list View from ArrayList
		displayListView();

	}

	private void displayListView()
	{

		//Array list of products
		ArrayList<Product> productList = new ArrayList<Product>();
		Product product = new Product("A001", "Small Chair", 10.00);
		productList.add(product);
		product = new Product("A002", "Medium Chair", 12.00);
		productList.add(product);
		product = new Product("A003", "Large Chair", 15.00);
		productList.add(product);
		product = new Product("B001", "Small Table", 50.00);
		productList.add(product);
		product = new Product("B002", "Medium Table", 60.00);
		productList.add(product);
		product = new Product("B003", "Large Table", 70.00);
		productList.add(product);


		//create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.invoice_order_item, productList);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

	}

	private class MyCustomAdapter extends ArrayAdapter<Product>
	{

		private ArrayList<Product> productList;

		public MyCustomAdapter(Context context, int textViewResourceId,
		                       ArrayList<Product> productList)
		{
			super(context, textViewResourceId, productList);
			this.productList = new ArrayList<Product>();
			this.productList.addAll(productList);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent)
		{

			DecimalFormat df = new DecimalFormat("0.00##");
			Product product = productList.get(position);

			if (view == null)
			{
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.orders_item, null);
				quantity = (EditText) view.findViewById(R.id.quantity);
				//quantity.setText("$" + "10");
				 /* Set Text Watcher listener */
				quantity.addTextChangedListener(passwordWatcher);


				//attach the TextWatcher listener to the EditText
//				quantity.addTextChangedListener(new MyTextWatcher(view));
				quantity.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after)
					{
						beforeTextChanged = quantity.getText().toString();
					}

					@Override
					public void onTextChanged(final CharSequence s, final int start, final int before, final int count)
					{
						onTextChanged = quantity.getText().toString();
					}

					@Override
					public void afterTextChanged(final Editable s)
					{
						afterTextChangedss = quantity.getText().toString();
						Toast.makeText(MainActivity.this, "before: " + beforeTextChanged
								               + '\n' + "on: " + onTextChanged
								               + '\n' + "after: " + afterTextChangedss
								, Toast.LENGTH_SHORT).show();
					}
				});
				if (position % 2 == 0)
				{
					view.setBackgroundColor(Color.rgb(238, 233, 233));
				}
			}

			/*quantity = (EditText) view.findViewById(R.id.quantity);
			quantity.setTag(product);
			if (product.getQuantity() != 0)
			{
				quantity.setText(String.valueOf(product.getQuantity()));
			}
			else
			{
				quantity.setText("10");
			}*/

			TextView itemNumber = (TextView) view.findViewById(R.id.itemNumber);
			itemNumber.setText(product.getItemNumber());
			TextView description = (TextView) view.findViewById(R.id.description);
			description.setText(product.getDescription());
			TextView price = (TextView) view.findViewById(R.id.price);
			price.setText("$" + df.format(product.getPrice()));
			TextView ext = (TextView) view.findViewById(R.id.ext);
			if (product.getQuantity() != 0)
			{
				ext.setText("$" + df.format(product.getExt()));
			}
			else
			{
				ext.setText("");
			}

			return view;

		}

	}

	private class MyTextWatcher implements TextWatcher
	{

		private View view;

		private MyTextWatcher(View view)
		{
			this.view = view;
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
			//do nothing
			onTextChanged = quantity.getText().toString();
		}

		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			//do nothing
			beforeTextChanged = quantity.getText().toString();
		}

		public void afterTextChanged(Editable s)
		{

			String afterTextChanged = quantity.getText().toString();
			Toast.makeText(MainActivity.this, "before: " + beforeTextChanged
					               + '\n' + "on: " + onTextChanged
					               + '\n' + "after: " + afterTextChanged
					, Toast.LENGTH_SHORT).show();
			/*DecimalFormat df = new DecimalFormat("0.00##");
			String qtyString = s.toString().trim();
			int quantity = qtyString.equals("") ? 0 : Integer.valueOf(qtyString);

			EditText qtyView = (EditText) view.findViewById(R.id.quantity);
			qtyView.setTextColor(Color.BLACK);
			Product product = (Product) qtyView.getTag();*/

			/*if (product.getQuantity() != quantity)
			{
				Double currPrice = product.getExt();
				Double extPrice = quantity * product.getPrice();
				Double priceDiff = Double.valueOf(df.format(extPrice - currPrice));

				product.setQuantity(quantity);
				product.setExt(extPrice);

				TextView ext = (TextView) view.findViewById(R.id.ext);
				if (product.getQuantity() != 0)
				{
					ext.setText("$" + df.format(product.getExt()));
					qtyView.setTextColor(Color.BLACK);
				}
				else
				{
					ext.setText("");
				}

				if (product.getQuantity() != 0)
				{
					qtyView.setText(String.valueOf(product.getQuantity()));
				}
				else
				{
					qtyView.setText("");
				}

				orderTotal += priceDiff;
				TextView cartTotal = (TextView) findViewById(R.id.cartTotal);
				cartTotal.setText(df.format(orderTotal));

			}*/

			return;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private final TextWatcher passwordWatcher = new TextWatcher()
	{
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{

		}

		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			//textView.setVisibility(View.VISIBLE);
		}

		public void afterTextChanged(Editable s)
		{
			if (s.length() == 0)
			{
				//textView.setVisibility(View.GONE);
			}
			else
			{
				//textView.setText("You have entered : " + quantity.getText());
				Toast.makeText(MainActivity.this, "after: " + quantity.getText().toString()
						, Toast.LENGTH_SHORT).show();
			}
		}
	};
}


package com.fmcg.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fmcg.Dotsoft.R;
import com.fmcg.util.SharedPrefsUtil;

/**
 * Created by Shiva on 6/9/2017.
 */

public class ViewListActivity extends AppCompatActivity implements View.OnClickListener
{
	Button bookList, invoiceList;
	Context mContext;

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_list_activity);
		mContext = ViewListActivity.this;

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		bookList = (Button) findViewById(R.id.bookList);
		invoiceList = (Button) findViewById(R.id.invoiceList);
		bookList.setOnClickListener(this);
		invoiceList.setOnClickListener(this);
	}

	@Override
	public void onClick(final View v)
	{
		switch (v.getId())
		{
			case R.id.bookList:
				SharedPrefsUtil.setStringPreference(mContext, "ACCESS_LIST", "BOOK_LIST");
				Intent i = new Intent(ViewListActivity.this, OrderBookList.class);
				startActivity(i);
				break;
			case R.id.invoiceList:
				SharedPrefsUtil.setStringPreference(mContext, "ACCESS_LIST", "INVOICE_LIST");
				Intent in = new Intent(ViewListActivity.this, OrderBookList.class);
				startActivity(in);
				break;
		}

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
		Intent intent = new Intent(this, OrderBookList.class);
		startActivity(intent);
		finish();
	}
}

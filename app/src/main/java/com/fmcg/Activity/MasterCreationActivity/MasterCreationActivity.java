package com.fmcg.Activity.MasterCreationActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.fmcg.Activity.HomeActivity.DashboardActivity;
import com.fmcg.Dotsoft.R;

/**
 * Created by Shiva on 6/9/2017.
 */

public class MasterCreationActivity extends AppCompatActivity implements View.OnClickListener
{
	Button zonecreationbtn, routecreationbtn, areacreationbtn;
	Context mContext;
	public static Activity masteract;

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master_creation_activity);
		mContext = MasterCreationActivity.this;
		masteract = MasterCreationActivity.this;

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		zonecreationbtn = (Button) findViewById(R.id.zonecreationbtn);
		routecreationbtn = (Button) findViewById(R.id.routecreationbtn);
		areacreationbtn = (Button) findViewById(R.id.areacreationbtn);
		zonecreationbtn.setOnClickListener(this);
		routecreationbtn.setOnClickListener(this);
		areacreationbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(final View v)
	{
		switch (v.getId())
		{
			case R.id.zonecreationbtn:
				Intent i = new Intent(MasterCreationActivity.this, ZoneCreationActivity.class);
				startActivity(i);
				break;
			case R.id.routecreationbtn:
				Intent in = new Intent(MasterCreationActivity.this, RouteCreationActivity.class);
				startActivity(in);
				break;
			case R.id.areacreationbtn:
				Intent ints = new Intent(MasterCreationActivity.this, AreaCreationActivity.class);
				startActivity(ints);
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
		Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
		finish();
	}
}

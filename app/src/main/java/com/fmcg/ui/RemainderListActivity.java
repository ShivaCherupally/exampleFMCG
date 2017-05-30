package com.fmcg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmcg.Dotsoft.R;
import com.fmcg.adapter.RemainderAdapter;
import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.RemainderData;
import com.fmcg.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RuchiTiwari on 5/26/2017.
 */

public class RemainderListActivity extends AppCompatActivity
{
	public static Activity remainderListActivity;
	RecyclerView mRecyclerView;
	TextView nodata;
	RemainderDataBase remainderDb;
	RemainderData _RemainderData = null;
	RecyclerView.Adapter mAdapter;
	Button addRemainder;
	LinearLayout swipe;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remainder_list_activity);
		remainderListActivity = RemainderListActivity.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		nodata = (TextView) findViewById(R.id.nodata);
		mRecyclerView = (RecyclerView) findViewById(R.id.remainderRecyclerView);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		mRecyclerView.setHasFixedSize(true);

		addRemainder = (Button) findViewById(R.id.addRemainder);

		swipe  = (LinearLayout)findViewById(R.id.swipe);

		addRemainder.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Intent i = new Intent(RemainderListActivity.this, AddRemainderActivity.class);
				Util.killRemainderList();
				startActivity(i);
			}
		});


		try
		{
			remainderDb = new RemainderDataBase(getApplicationContext());
			List<RemainderData> bookaTestDatas = new ArrayList<>();
			bookaTestDatas = remainderDb.getRemainderListData();
			List<RemainderData> mRemainderData = new ArrayList<>();
			for (RemainderData bookdata : bookaTestDatas)
			{
				mRemainderData.add(bookdata);
			}
			if (mRemainderData.size() != 0)
			{
				nodata.setVisibility(View.GONE);
				swipe.setVisibility(View.VISIBLE);
				mAdapter = new RemainderAdapter(RemainderListActivity.this, mRemainderData);
				mRecyclerView.setAdapter(mAdapter);
			}
			else
			{
				swipe.setVisibility(View.GONE);
				nodata.setVisibility(View.VISIBLE);
			}
		}
		catch (Exception e)
		{

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

	public void updateAdapter() {
		mAdapter.notifyDataSetChanged(); //update adapter
	}

}

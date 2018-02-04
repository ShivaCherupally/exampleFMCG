//package com.fmcg.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import RemainderAdapter;
//import com.fmcg.database.RemainderDataBase;
//import com.fmcg.models.RemainderData;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by RuchiTiwari on 5/26/2017.
// */
//
//public class RemainderActivity extends AppCompatActivity
//{
//	RecyclerView mRecyclerView;
//	TextView nodata;
//	RemainderDataBase remainderDb;
//	RemainderData _RemainderData = null;
//	RecyclerView.Adapter mAdapter;
//	Button addRemainder;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.);
//
//		nodata = (TextView) findViewById(R.id.nodata);
//		mRecyclerView = (RecyclerView) findViewById(R.id.remainderRecyclerView);
//		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//		mRecyclerView.setHasFixedSize(true);
//
//		addRemainder = (Button) findViewById(R.id.addRemainder);
//
//		addRemainder.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(final View v)
//			{
//				Intent i = new Intent(RemainderActivity.this, AddRemainderActivity.class);
//				startActivity(i);
//			}
//		});
//
//
//		try
//		{
//			remainderDb = new RemainderDataBase(getApplicationContext());
//			List<RemainderData> bookaTestDatas = new ArrayList<>();
//			bookaTestDatas = remainderDb.getRemainderListData();
//			List<RemainderData> mRemainderData = new ArrayList<>();
//			for (RemainderData bookdata : bookaTestDatas)
//			{
//				mRemainderData.add(bookdata);
//			}
//			if (mRemainderData.size() != 0)
//			{
//				nodata.setVisibility(View.GONE);
//				mAdapter = new RemainderAdapter(RemainderActivity.this, mRemainderData);
//				mRecyclerView.setAdapter(mAdapter);
//			}
//			else
//			{
//				nodata.setVisibility(View.VISIBLE);
//			}
//		}
//		catch (Exception e)
//		{
//
//		}
//	}
//
//
//}

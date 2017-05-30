package com.fmcg.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.database.RemainderDataBase;
import com.fmcg.models.RemainderData;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Util;
import com.fmcg.util.Validate;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by RuchiTiwari on 5/26/2017.
 */

public class AddRemainderActivity extends AppCompatActivity implements View.OnClickListener

{
	public static Activity addremainder;
	EditText evenNameEt;
	static TextView dateAndTime;
	Button selectDate, selectTime, add;
	RemainderDataBase remainderDB;
	Context mContext;
	public static ArrayList<RemainderData> _remainderData = new ArrayList<>();
	String selectedDateandTime = "";
	static boolean dateSelection = false;
	static boolean timeSelection = false;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_remainder_activity);
		evenNameEt = (EditText) findViewById(R.id.evenNameEt);
		dateAndTime = (TextView) findViewById(R.id.dateAndTime);
		selectTime = (Button) findViewById(R.id.selectTime);
		selectDate = (Button) findViewById(R.id.selectDate);
		add = (Button) findViewById(R.id.add);


		mContext = AddRemainderActivity.this;
		addremainder = AddRemainderActivity.this;

		dateSelection = false;
		timeSelection = false;

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		selectTime.setOnClickListener(this);
		selectDate.setOnClickListener(this);
		add.setOnClickListener(this);
	}

	@Override
	public void onClick(final View v)
	{

		switch (v.getId())
		{
			case R.id.selectDate:
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
				/*DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");*/

				break;
			case R.id.selectTime:
				if (dateSelection)
				{
					DialogFragment newtimeFragment = new TimePickerFragment();
					newtimeFragment.show(getSupportFragmentManager(), "timePicker");
				}
				else
				{
					Toast.makeText(mContext, "Please Select Date first", Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.add:
				boolean validated = validatingFields();
				if (validated)
				{
					addingInRemainderInDB();
				}

				break;
		}
	}

	private boolean validatingFields()
	{
		boolean ret = true;
		String eventname = evenNameEt.getText().toString();
//		String eventDate = "Shiva"; //dateAndTime.getText().toString();
		if (eventname == null || eventname.isEmpty())
		{
			Toast.makeText(mContext, "Please enter Remainder", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		/*if (eventDate.isEmpty() && eventDate == null)
		{
			Toast.makeText(mContext, "Please Select date and Time", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}*/
		if (dateSelection == false)
		{
			Toast.makeText(mContext, "Please Select date", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}

		if (timeSelection == false)
		{
			Toast.makeText(mContext, "Please Select time", Toast.LENGTH_SHORT).show();
			ret = false;
			return ret;
		}
		return ret;
	}

	private void addingInRemainderInDB()
	{
		remainderDB = new RemainderDataBase(this);
//		String currentDate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		_remainderData = new ArrayList<>();
		RemainderData _remainderdtaset = new RemainderData();
		_remainderdtaset.setEventName(evenNameEt.getText().toString() + "");
		_remainderdtaset.setEventDate(dateAndTime.getText().toString() + "");
//		_remainderdtaset.setEventTime(currentDate + ""); // here passing Date and Time
		_remainderData.add(_remainderdtaset);
		remainderDB.addRemaiderData(_remainderdtaset);

		Toast.makeText(mContext, "Successfully Added Remainder", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(AddRemainderActivity.this, RemainderListActivity.class);
		Util.killAddRemainder();
		startActivity(i);
	}

	/*@Override
	public void onDateSet(final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth)
	{

	}*/


	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener
	{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
//			dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
			dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
			return dialog;
		}

		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			Date date = new Date(year, month, day);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM");
			String sectiondate = simpleDateFormat.format(date.getTime()) + "-" + year;//simDf.format(c.getTime());
			dateAndTime.setText(sectiondate);
			SharedPrefsUtil.setStringPreference(getContext(), "SelectedDate", sectiondate);
			dateSelection = true;
//		AddRemainderActivity.dateAndTime.setText(SelectedDate);
		}
	}


	public static class TimePickerFragment extends DialogFragment
			implements TimePickerDialog.OnTimeSetListener
	{

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it


			return new TimePickerDialog(getActivity(), this, hour, minute,
			                            DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{
			Time time = new Time(hourOfDay, minute, 0);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
			String s = simpleDateFormat.format(time);
			String SelectedDate = SharedPrefsUtil.getStringPreference(getContext(), "SelectedDate");
			if (SelectedDate != null && !SelectedDate.isEmpty())
			{
				dateAndTime.setText(SelectedDate + ", " + s);
				timeSelection = true;
			}

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
		Intent intent = new Intent(this, RemainderListActivity.class);
		AddRemainderActivity.this.finish();
		startActivity(intent);
//		finish();
	}
}
package com.fmcg.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fmcg.models.RemainderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RuchiTiwari on 5/26/2017.
 */

public class RemainderDataBase extends SQLiteOpenHelper

{
	public static final String dbName = "FMCG_REM_DB";

	public static final String REMAINDER_TABLE = "remaindertable";

	public static final String EVENT_NAME = "eventName";
	public static final String EVENT_DATE = "eventDate";
	public static final String EVENT_TIME = "eventTime";

	public RemainderDataBase(Context _ctx)
	{
		super(_ctx, dbName, null, 5);
	}

	@Override
	public void onCreate(final SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + REMAINDER_TABLE + " (" + EVENT_NAME + " TEXT, "
				           + EVENT_DATE + " TEXT, "
				           + EVENT_TIME + " TEXT)");
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + REMAINDER_TABLE);
		onCreate(db);
	}


	public long addRemaiderData(RemainderData bmiUsersData)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(EVENT_NAME, bmiUsersData.getEventName() + "");
		cv.put(EVENT_DATE, bmiUsersData.getEventDate());
		cv.put(EVENT_TIME, bmiUsersData.getEventTime());
		return db.insert(REMAINDER_TABLE, null, cv);
	}


	public List<RemainderData> getRemainderListData()
	{
		List<RemainderData> remainder_data = new ArrayList<RemainderData>();
		String selectQuery = "SELECT  * FROM " + REMAINDER_TABLE;

		Log.e("", selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery(selectQuery, null);

		RemainderData remainderdata = null;
		// looping through all rows and adding to list
		if (cur.moveToFirst())
		{
			do
			{
				remainderdata = new RemainderData();
				remainderdata.setEventName(cur.getString(cur.getColumnIndex(EVENT_NAME)));
				remainderdata.setEventDate(cur.getString(cur.getColumnIndex(EVENT_DATE)));
				remainderdata.setEventTime(cur.getString(cur.getColumnIndex(EVENT_TIME)));
				// adding to todo list
				remainder_data.add(remainderdata);
			}
			while (cur.moveToNext());
		}

		return remainder_data;

		///////////////
	}

	public boolean deleteRemainder(String eventName) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(REMAINDER_TABLE, EVENT_NAME + "='" + eventName + "' ;", null) > 0;

	}

}

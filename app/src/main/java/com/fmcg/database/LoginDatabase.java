//package com.fmcg.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by RuchiTiwari on 4/27/2017.
// */
//
//public class LoginDatabase extends SQLiteOpenHelper
//{
//	public static final String dbName = "fmcg";
//	public static final String oneTimeRegistration = "oneTimeRegistration";
//	public static final String deviceID = "deviceID";
//
//	static final String loginTable = "loginNames";
//	static final String name = "Username";
//	static final String pwd = "Password";
//	static final String date = "loginDate";
//
//
//	public LoginDatabase(Context _ctx)
//	{
//		super(_ctx, dbName, null, 1);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db)
//	{
//		db.execSQL("CREATE TABLE " + oneTimeRegistration + " (" + deviceID + " TEXT)");
//		db.execSQL("CREATE TABLE " + loginTable + " (" + name + " TEXT, " + pwd + " TEXT, " + date + " TEXT)");
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
//	{
//		db.execSQL("DROP TABLE IF EXISTS " + oneTimeRegistration);
//		db.execSQL("DROP TABLE IF EXISTS " + loginTable);
//		onCreate(db);
//	}
//
//	////Login Details Adding,Deleting and getting
//
//	public void addLoginDetails(String username, String password, String loginDate)
//	{
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.execSQL("DELETE FROM " + loginTable);
//		ContentValues cv = new ContentValues();
//
//		cv.put(name, username);
//		cv.put(pwd, password);
//		cv.put(date, loginDate);
//
//		db.insert(loginTable, name, cv);
//		db.close();
//	}
//
//
//	public void deleteLoginDetails()
//	{
//		SQLiteDatabase db = this.getWritableDatabase();
//		db.execSQL("DELETE FROM " + loginTable);
//	}
//
//	public LoginDetails getLoginDetails()
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cur = db.rawQuery("SELECT * FROM " + loginTable, new String[]{});
//		LoginDetails loginDetails = null;
//		if (cur.moveToFirst())
//		{
//			do
//			{
//				loginDetails = new LoginDetails();
//				loginDetails.setUsername(cur.getString(cur.getColumnIndex(name)));
//				loginDetails.setPassword(cur.getString(cur.getColumnIndex(pwd)));
//				loginDetails.setLoginDate(cur.getString(cur.getColumnIndex(date)));
//			}
//			while (cur.moveToNext());
//		}
//		cur.close();
//		db.close();
//		if (loginDetails != null)
//		{
//			return loginDetails;
//		}
//		else
//		{
//			return null;
//		}
//	}
//
//
//	public void addRegisteredUDID(String _UDID)
//	{
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues cv = new ContentValues();
//		cv.put(deviceID, _UDID);
//		db.insert(oneTimeRegistration, deviceID, cv);
//		db.close();
//	}
//
//
//	public void updateStatusRecords(String updateStatus)
//	{
//
//	}
//
//	public String getRegisteredDeviceID()
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		Cursor cur = db.rawQuery("SELECT * FROM " + oneTimeRegistration, new String[]{});
//		String UDID = null;
//		if (cur.getCount() > 0)
//		{
//			if (cur.moveToFirst())
//			{
//				do
//				{
//					UDID = cur.getString(cur.getColumnIndex(deviceID));
//				}
//				while (cur.moveToNext());
//			}
//		}
//		cur.close();
//		db.close();
//		return UDID;
//	}
//
//
//	public String getExperiedTime()
//	{
//		SQLiteDatabase db = this.getReadableDatabase();
//		String sql = "DELETE FROM " + oneTimeRegistration + " WHERE " + deviceID + "<=" + "date('now','-1 day')";
//		db.execSQL(sql);
//		db.close();
//		return sql;
//	}
//
//}

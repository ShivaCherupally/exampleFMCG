package com.fmcg.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fmcg.Dotsoft.R;
import com.fmcg.models.UserDetails;
import com.fmcg.util.SharedPrefsUtil;
import com.fmcg.util.Utility;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
	public SharedPreferences sharedPreferences;
	EditText username, password;
	TextView login;
	String user, pass;
	public Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		username = (EditText) findViewById(R.id.username_et);
		password = (EditText) findViewById(R.id.pass_et);
		login = (TextView) findViewById(R.id.login_tv);

		context = LoginActivity.this;
		sharedPreferences = getSharedPreferences("userlogin", Context.MODE_PRIVATE);


		login.setOnClickListener(this);
	}

	public void registerUser()
	{
		user = username.getText().toString().trim();
		pass = password.getText().toString().trim();


		if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
		{
			Toast.makeText(this, "Please Enter Your User Name and Password", Toast.LENGTH_SHORT).show();
			return;
		}
		else
		{
			if (Utility.isOnline(context))
			{
				new UserLoginTask().execute();
			}
			else
			{
				Toast.makeText(this, "Please Check Internet Connection.. ", Toast.LENGTH_SHORT).show();
			}
		}

	}

	public String serviceCall()
	{
		// Create a new HttpClient and Post Header
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://202.143.96.20/Orderstest/api/Services/LoginService");
		try
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("Username", user));
			nameValuePairs.add(new BasicNameValuePair("Password", pass));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == 200)
			{
				HttpEntity entity = response.getEntity();
				if (entity != null)
				{
					responseBody = EntityUtils.toString(entity);

				}
			}
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
		}
		return responseBody;
	}

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.login_tv)
		{
			registerUser();
		}
	}

	public class UserLoginTask extends AsyncTask<String, String, String>
	{
		ProgressDialog pd = new ProgressDialog(LoginActivity.this);

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub

			pd.setMessage("Please wait...");
			pd.setIndeterminate(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params)
		{
			// TODO: attempt authentication against a network service.
			String result = "";
			try
			{
				result = serviceCall();
			}
			catch (Exception e)
			{

			}

			//Toast.makeText(LoginActivity.this,""+result,Toast.LENGTH_SHORT).show();
			//Log.d("login result",result);
			return result;
		}

		@Override
		protected void onPostExecute(String success)
		{
			try
			{
				if (success != null)
				{
					UserDetails userDetails = new Gson().fromJson(success.toString(), UserDetails.class);
					if (!userDetails.Status.equals(null))
					{
						if (userDetails.Status.equals("OK"))
						{
							Toast.makeText(LoginActivity.this, userDetails.Message, Toast.LENGTH_SHORT).show();
							//insert code or saveing code
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.putString("username", userDetails.Data.EmployeeId);
							editor.putString("password", userDetails.Data.EmployeeCode);

							SharedPrefsUtil.setStringPreference(context, "EmployeeId", userDetails.Data.EmployeeId);
							SharedPrefsUtil.setStringPreference(context, "EmployeeCode", userDetails.Data.EmployeeCode);

							DateFormat dateInstance = SimpleDateFormat.getDateInstance();
							String LoginDateandTime = dateInstance.format(Calendar.getInstance().getTime());
							SharedPrefsUtil.setStringPreference(context, "LoginTime", LoginDateandTime);
							Log.e("LoginDate", LoginDateandTime + "Y");
							editor.commit();

							Intent login = new Intent(LoginActivity.this, DashboardActivity.class);
							startActivity(login);
							finish();
						}
						else
						{
							Toast.makeText(LoginActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
							//Toast.makeText(LoginActivity.this, userDetails.Message, Toast.LENGTH_SHORT).show();
							/*Intent login = new Intent(LoginActivity.this, NavigationActivity.class);
							startActivity(login);*/
						}
					}else {
						Toast.makeText(LoginActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
					}
				}
				pd.dismiss();
			}
			catch (Exception e)
			{
				Toast.makeText(LoginActivity.this, "Login Failed..", Toast.LENGTH_SHORT).show();
				/*Intent login = new Intent(LoginActivity.this, NavigationActivity.class);
				startActivity(login);*/
				e.printStackTrace();
				pd.dismiss();
			}

		}
	}


}

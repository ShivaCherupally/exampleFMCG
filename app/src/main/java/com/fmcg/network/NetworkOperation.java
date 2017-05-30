
package com.fmcg.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import com.fmcg.network.NetworkOperationListener;
import com.fmcg.network.NetworkResponse;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


public class NetworkOperation extends
		AsyncTask<String, Integer, NetworkResponse> {

	private static final String RESPONSE_STATUS = "status";
	private static final String RESPONSE_MESSAGE = "responseMsg";
	private static final String RESPONSE_CODE = "code";
	private NetworkOperationListener listener;
	private Object tag;
	private String method;
	private String contentType;
	private String url;
	private static String sessionCookie;
	private static HttpClient httpclient;
	int timeoutConnection = 50000;
	

	public NetworkOperation(NetworkOperationListener listener, Object tag) {
		this.listener = listener;
		this.tag = tag;
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established. settingsDialog
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutConnection);
		httpclient = new DefaultHttpClient(httpParameters);
	}
	
	public void timerDelayRemoveDialog(long time, final Dialog d){
	    new Handler().postDelayed(new Runnable() {
	        public void run() {                
	            d.dismiss();         
	        }
	    }, time); 
	}

	public static String getSessionCookie() {
		return sessionCookie;
	}

	private HttpRequestBase createRequest(String... params)
			throws UnsupportedEncodingException {
		HttpRequestBase request = null;
		String url = params[0];
		this.url = url;
		method = params[1];
		if (method == HttpAdapter.METHOD_GET) {
			request = new HttpGet(url);
		} else if (method == HttpAdapter.METHOD_POST) {
			if (params.length > 2) {
				HttpPost post = new HttpPost(url);
				System.out.println(params[2]);
				post.setEntity(new StringEntity(params[2]));
				if (contentType == null) {
					post.addHeader(HttpAdapter.HEADER_CONTENT_TYPE,
							HttpAdapter.CONTENT_TYPE_APPLICATION_URL_ENCODED);
				} else {
					post.addHeader(HttpAdapter.HEADER_CONTENT_TYPE, contentType);
				}
				request = post;
			}
		} else if (method == HttpAdapter.METHOD_DELETE) {
			HttpDelete delete = new HttpDelete(url);
			request = delete;
		} else if (method == HttpAdapter.METHOD_UPDATE) {
			return null;
		} else if (method == HttpAdapter.METHOD_PUT) {
			HttpPut put = new HttpPut(url);
			if (contentType == null) {
				put.addHeader(HttpAdapter.HEADER_CONTENT_TYPE,

								HttpAdapter.CONTENT_TYPE_APPLICATION_JSON);
			} else {
				put.addHeader(HttpAdapter.HEADER_CONTENT_TYPE, contentType);
			}
			request = put;
		}

		return request;
	}

	
	@Override
	protected NetworkResponse doInBackground(String... params) {

		NetworkResponse nResponse = new NetworkResponse();
		nResponse.setTag(tag);
		HttpResponse response;
		boolean success = true;
		try {
			response = httpclient.execute(createRequest(params));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= HttpStatus.SC_OK
					&& statusLine.getStatusCode() <= HttpStatus.SC_ACCEPTED) {
				Header header = response.getFirstHeader("Set-Cookie");
				if (header != null && url.endsWith("preAuth/accounts/verify")) {
					sessionCookie = header.getValue();
					Log.i("LOGIN_SESSION", sessionCookie);
				}
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				String json = extractResponseString(content);
				// JSONObject object = new JSONObject(builder.toString());
				// object.put("dateOfBirth", "2014-06-17");
				// System.out.println("json:"+object);
				nResponse.setStatusCode(statusLine.getStatusCode());
				if (json != null && json.trim().length() > 0) {
					nResponse.setResponseObject(parseResponseString(json,
							params[0]));
				}
				nResponse.setResponseString(json);
				return nResponse;
			} else {
				nResponse.setStatusCode(statusLine.getStatusCode());
				String responseString = extractResponseString(response
						.getEntity().getContent());
				nResponse.setResponseString(responseString);
				return nResponse;
				// throw new
				// IOException(statusLine.getStatusCode()+" "+statusLine.getReasonPhrase()+" "+responseString);
			}
		} catch (ClientProtocolException e) {
			success = false;
			e.printStackTrace();
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
			onProgress(e.toString());
		} catch (JSONException e) {
			success = false;
			e.printStackTrace();
		}
		 
		nResponse
				.setResponseString("Unable to contact server, please try again later");
		nResponse.setResponseStatus("ERROR");
		return nResponse;
	}

	private String extractResponseString(InputStream content)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				content));
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		// JSONObject object = new JSONObject(builder.toString());
		String json = builder.toString();
		System.out.println(json);
		return json;
	}

	public Object parseResponseString(String jsonString, String call)
			throws JSONException {
		System.out.println("Response....." + jsonString);
		/*
		 * if(call.startsWith(HttpAdapter.URL_USER) && method ==
		 * HttpAdapter.METHOD_GET) { return new Gson().fromJson(jsonString,
		 * User.class); } else if(call.startsWith(HttpAdapter.URL_PLACE) &&
		 * method == HttpAdapter.METHOD_GET) { return new
		 * Gson().fromJson(jsonString, Place.class); } else { //Order group =
		 * Order(new JSONObject(jsonString));
		 * //Order.getOrder().updateOrderWithDetails(new
		 * JSONObject(jsonString)); //return Order.getOrder(); }
		 */

		return null;
	}

	@Override
	protected void onPostExecute(NetworkResponse result) {
		super.onPostExecute(result);
		listener.operationCompleted(result);
	}
	
	
	
	protected String onProgress(String value) {
		return value;
	}

	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 
	 */
	public static void clearCookies() {
		sessionCookie = "";
	}

	public static void setSessionCookie(String cookie) {
		sessionCookie = cookie;
	}

}

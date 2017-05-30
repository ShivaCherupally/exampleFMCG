
package com.fmcg.network;

import android.util.Log;
import android.view.View;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.NameValuePair;

public class HttpAdapter
{
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_UPDATE = "UPDATE";
	public static final String METHOD_PUT = "PUT";
	public static final String BASE_URL = "http://202.143.96.20/Orderstest/api/Services/";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	public static final String CONTENT_TYPE_APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded";
	public static final String SHOPROUTE = BASE_URL + "GetShopeDetailsByRouteID";
	public static final String REGISTERSHOP = BASE_URL + "RegisterShop";
	public static final String SHOPTYPEDROPDOWN = BASE_URL + "usp_GetShopTypeDropDown";
	public static final String SHOPDETAILSDP = BASE_URL + "GetShopsDetailsByArea";
	public static final String ORDERSTATUS = BASE_URL + "GetOrderStatusDropDown";
	public static final String PRODUCTCATEGORYNAME = BASE_URL + "GetProductDropDown?ProductId=0";
	public static final String RELIGION = BASE_URL + "usp_GetReligionsDropDown";
	public static final String PAYMENT = BASE_URL + "usp_GetPaymentDropDown";
	public static final String ORDERNUMBER = BASE_URL + "GetOrderNumberDropDown";
	public static final String GETROUTEDETAILS_BYEMPLOYEE = BASE_URL + "GetRouteDetailsByEmployee";
	public static final String GETZONEDETAILS = BASE_URL + "GetZoneDetailsDropDown?ZoneId=0";
	public static final String GET_ROUTEDETAILS = BASE_URL + "GetRouteDetailsByZone";
	public static final String GET_AREADETAILS = BASE_URL + "GetAreaDetailsDropDown";
	public static final String GET_ORDER_SUMMARY = BASE_URL + "GetOrderSummaryByOrderNumber";
	public static final String ROUTE = BASE_URL + "usp_GetRouteDropDown";
	public static final String AREANAME = BASE_URL + "GetShopsDetailsByArea?AreaId=0";
	public static final String GETAREADETAILS_BY_ROUTE = BASE_URL + "GetAreaDetailsByRoute";
	public static final String CATERGORY = BASE_URL + "usp_GetCategoryDropDown";
	public static final String FEEDBACK = BASE_URL + "InsertFeedback?EmployeeId=4&FeedbackMessages=hello";
	public static final String ORDERBOOK = BASE_URL + "Saveorderbooking";
	public static final String GET_ORDERNUMBER = BASE_URL + "GetOrderNumber";
	public static final String GET_INVOICENUMBER = BASE_URL + "GetInvoiceNumber";
	public static final String INVOICE_SUBMIT = BASE_URL + "SaveInvoice";
	public static final String INVOICE_CANCEL = BASE_URL + "CancelOrderNumber";
	public static final String ORDER_TOTAL_AMOUNT = BASE_URL + "GetOrderNumberAmount";

	public static final String EDITSHOP_DETAILS = BASE_URL + "EditShopDetail";
	public static final String UPDATESHOP_DETAILS = BASE_URL + "updateShopDetails";

	public static final String SHOP_NAMES_DROPDOWN = BASE_URL + "GetShopDetailsDropDown";
//CancelOrderNumber
	//http://202.143.96.20/Orderstest/Api/Services/GetRouteDataAgent?AgentCode
	//http://202.143.96.20/Orderstest/api/Services/GetShopeDetailsByRouteID?RouteID=1
	//http://202.143.96.20/Orderstest/api/Services/InsertFeedback?EmployeeId=4&FeedbackMessages=hello

	public static void registerShops(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(REGISTERSHOP, METHOD_POST, jsonString);
	}

	public static void getShops(NetworkOperationListener listener, Object tag, String agentcode)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_URL_ENCODED);
		operation.execute(SHOPROUTE + "?RouteID=1", METHOD_GET, "");
	}

	public static void orderbook(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ORDERBOOK, METHOD_POST, jsonString);
	}


	public static void invoiceSubmit(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString + "");
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(INVOICE_SUBMIT, METHOD_POST, jsonString);
	}

	public static void invoiceCancel(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(INVOICE_CANCEL, METHOD_POST, jsonString);
	}

	public static void shopType(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(SHOPTYPEDROPDOWN, METHOD_POST, "NONE");
	}

	//Shop Names Drop Down
	public static void shopNamesDropDown(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(SHOP_NAMES_DROPDOWN, METHOD_POST, "NONE");
	}

	public static void shopEditDetails(NetworkOperationListener listener, Object tag, String ShopId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(EDITSHOP_DETAILS + "?ShopId=" + ShopId, METHOD_POST, "NONE");
	}

	public static void getReligion(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(RELIGION, METHOD_POST, "NONE");
	}

	public static void getPayment(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(PAYMENT, METHOD_POST, "NONE");
	}

	public static void getRoute(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ROUTE, METHOD_POST, "NONE");
	}

	public static void getCategory(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(CATERGORY, METHOD_POST, "NONE");
	}

	//http://202.143.96.20/Orderstest/api/Services/InsertFeedback?EmployeeId=4&FeedbackMessages=hello
	public static void insertFeedback(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(FEEDBACK, METHOD_POST, jsonString);
	}

	public static void getShopDetailsDP(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(SHOPDETAILSDP + "?AreaId=" + value, METHOD_GET, "NONE");
	}

	public static void getOrderStatus(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ORDERSTATUS, METHOD_GET, "NONE");
	}

	public static void getProductCategoryDP(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(PRODUCTCATEGORYNAME, METHOD_GET, "NONE");
	}

	/*public static void getOrderNumberDp(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ORDERNUMBER + "?ShopId=" + value, METHOD_GET, "NONE");
	}*/

	public static void getOrderNumberDp(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ORDERNUMBER + "?ShopId=" + value, METHOD_GET, "NONE");
	}

	public static void getRouteDetailsByEmployee(NetworkOperationListener listener, Object tag, String empId)
	{
		// TODO Auto-generated method stub
		Log.e("empId", empId);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GETROUTEDETAILS_BYEMPLOYEE + "?EmployeeId=" + empId, METHOD_GET, "NONE");
	}

	public static void getZoneDetailsDP(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GETZONEDETAILS, METHOD_GET, "NONE");
	}

	public static void getRouteDetails(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_ROUTEDETAILS + "?ZoneId=" + value, METHOD_GET, "");
	}

	public static void getAreaDetails(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_AREADETAILS + "?AreaId=" + value, METHOD_GET, "NONE");
	}

	public static void getOrderSummary(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_ORDER_SUMMARY + "?OrderNumber=" + value, METHOD_GET, "NONE");
	}

	public static void getOrderTotal(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ORDER_TOTAL_AMOUNT + "?OrderNumber=" + value, METHOD_GET, "NONE");
	}

	public static void areaNameDP(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(AREANAME, METHOD_GET, "NONE");
	}

	public static void getAreaDetailsByRoute(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GETAREADETAILS_BY_ROUTE + "?RouteId=" + value, METHOD_GET, "NONE");
	}

	public static void GetOrderNumber(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_ORDERNUMBER, METHOD_GET, "NONE");
	}

	public static void GetInvoiceNumber(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_INVOICENUMBER, METHOD_GET, "NONE");
	}

}
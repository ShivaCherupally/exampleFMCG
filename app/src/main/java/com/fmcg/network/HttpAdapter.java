
package com.fmcg.network;

import android.util.Log;

import com.fmcg.util.SharedPrefsUtil;

public class HttpAdapter
{
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_DELETE = "DELETE";
	public static final String METHOD_UPDATE = "UPDATE";
	public static final String METHOD_PUT = "PUT";

	//Live Url
	public static final String BASE_URL = "http://202.143.96.20/Orderstest/api/Services/";
	//Test Url
//	public static final String BASE_URL = "http://202.143.96.20/BRIGHTUDYOGWEBAPI/api/Services/";


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

	//GetRouteDetailsByEmpRouteIds
	public static final String GETZONEDETAILS = BASE_URL + "GetZoneDetailsDropDown?ZoneId=0";
	public static final String GET_ROUTEDETAILS = BASE_URL + "GetRouteDetailsByZone";
	public static final String GET_AREADETAILS = BASE_URL + "GetAreaDetailsDropDown";
	public static final String GET_ORDER_SUMMARY = BASE_URL + "GetOrderSummaryByOrderNumber";
	public static final String ROUTE = BASE_URL + "usp_GetRouteDropDown";
	public static final String AREANAME = BASE_URL + "GetShopsDetailsByArea?AreaId=0";
	public static final String GETAREADETAILS_BY_ROUTE = BASE_URL + "GetAreaDetailsByRoute";
	public static final String CATERGORY = BASE_URL + "usp_GetCategoryDropDown";
	//public static final String FEEDBACK = BASE_URL + "InsertFeedback?EmployeeId=4&FeedbackMessages=hello";
	public static final String FEEDBACK = BASE_URL + "InsertFeedback";
	public static final String ORDERBOOK = BASE_URL + "Saveorderbooking";
	public static final String GET_ORDERNUMBER = BASE_URL + "GetOrderNumber";
	public static final String GET_INVOICENUMBER = BASE_URL + "GetInvoiceNumber";

	public static final String INVOICE_SUBMIT = BASE_URL + "SaveInvoice";
	public static final String CANCEL_ORDER_NUMBER = BASE_URL + "CancelOrderNumber";


	public static final String INVOICE_CANCEL = BASE_URL + "CancelOrderNumber";
	public static final String ORDER_TOTAL_AMOUNT = BASE_URL + "GetOrderNumberAmount";

	public static final String EDITSHOP_DETAILS = BASE_URL + "EditShopDetail";
	public static final String UPDATESHOP_DETAILS = BASE_URL + "updateShopDetails";

	public static final String SHOP_NAMES_DROPDOWN = BASE_URL + "GetShopDetailsDropDown";


	//Shop Creation
	public static final String SHOP_CREATION_URL = BASE_URL + "RegisterShop";
	//http://202.143.96.20/Orderstest/api/Services/

	////////Dashboard Details
	public static final String GET_DASHBOARD_TARGET_AMOUNT = BASE_URL + "GetDashboardTargetAmount";
	public static final String GET_DASHBOARD_SALES_AMOUNT = BASE_URL + "GetDashboardSalesAmount";
	public static final String GET_DASHBOARD_SALES_RATIO = BASE_URL + "GetDashboardTotalSalesRatio";
	public static final String GET_DASHBOARD_SALES_MONTH = BASE_URL + "GetDashboardTotalSalesBymonth";

	//Route Details Bind services
	public static final String GET_ROUTE_DETAILS_WITH_ROUTEID = BASE_URL + "GetRouteDetailsByEmpRouteIds";
	public static final String GET_ROUTE_ACCEPT_BY_EMP = BASE_URL + "GetRouteAcceptByEmployee";

	//Order Book List
	public static final String GET_ORDER_BOOK_LIST = BASE_URL + "GetOrderBookingDetails?OrderNumber=0";
	//	public static final String GET_INVOICE_LIST = BASE_URL + "GetInvoiceDetails?InvoiceNumber=0";
	public static final String GET_INVOICE_LIST = BASE_URL + "GetInvoiceDetails?OrderSalesId=0";
	//OrderSalesId

	//Star Trip Services
	public static final String GET_ROUTE_DROPDOWN = BASE_URL + "GetRouteDetailsByEmpRouteIds";
	public static final String GET_AREA_NAME = BASE_URL + "GetAreaDetailsByRoute";
	public static final String GET_SHOP_DETAILS = BASE_URL + "GetShopsDetailsByArea";

	//Pending Bill List Services
	public static final String PENDING_BILL_DETAILS = BASE_URL + "GetShopsDetailsByArea";
	public static final String PAY_DONE = BASE_URL + "GetShopsDetailsByArea";

	//Order Delete
	public static final String DELETE_ORDER = BASE_URL + "DeleteOrders";
	//http://202.143.96.20/Orderstest/api/Services/DeleteOrders?OrderId=6&UserId=4

	//Order Edit
	public static final String EDIT_ORDER = BASE_URL + "GetOrderDetailsByOrderId";
	//Order Summary
	public static final String ORDER_PRODCTCATEGORY_SUMMARY = BASE_URL + "GetOrderSummaryByOrderNumber";

	public static final String UPDATE_ORDERBOOK = BASE_URL + "Updateorderbooking";


	//Invoice Delete
	public static final String DELETE_INVOICE = BASE_URL + "DeleteInvoice";

	//Invoice Edit
	public static final String EDIT_INVOICE = BASE_URL + "GetInvoiceByOrderSalesId";

	//Update Invoice Save
	public static final String UPDATE_INVOICE = BASE_URL + "UpdateInvoice";


	public static final String UPDATE_INVOICE_SUBMIT = "http://182.156.90.123/oreso/web_services/dev/services/login";


	public static final String SHOP_COLORS_URL = BASE_URL + "GetShopColorForOrders";

	public static final String ZONE_CREATION = BASE_URL + "InsertZone";
	public static final String ROUTE_CREATION = BASE_URL + "InsertRoute";
	public static final String AREA_CREATION = BASE_URL + "InsertArea";

	public static final String MONTH_SUMMARY = BASE_URL + "GetMonthlySummaryByEmployee";
	public static final String PENDING_BILLS = BASE_URL + "GetCustomerPendingBills";
	//GetCustomerPendingBills?EmployeeId=4
	//GetMonthlySummaryByEmployee?EmployeeId=4

//	public static final String SHOP_COLORS_URL = "http://www.mocky.io/v2/5994a54e110000f103723163";

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
		//?EmployeeId=4&FeedbackMessages=hello
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
//		operation.execute("http://www.mocky.io/v2/59de74921000002613a85156", METHOD_GET, "NONE");
		//
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


	//Target Amount
	public static void dashboardTargetAmount(NetworkOperationListener listener, Object tag, String empolyeeId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_DASHBOARD_TARGET_AMOUNT + "?EmployeeId=" + empolyeeId, METHOD_GET, "NONE");
	}

	//Sales Amount
	public static void dashboardSalesAmount(NetworkOperationListener listener, Object tag, String empolyeeId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_DASHBOARD_SALES_AMOUNT + "?EmployeeId=" + empolyeeId, METHOD_GET, "NONE");
	}

	//Sales Ratio
	public static void dashboardSalesRatio(NetworkOperationListener listener, Object tag, String empolyeeId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_DASHBOARD_SALES_RATIO + "?EmployeeId=" + empolyeeId, METHOD_GET, "NONE");
	}

	//Sales Month Graph
	public static void dashboardSalesMonth(NetworkOperationListener listener, Object tag, String empolyeeId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_DASHBOARD_SALES_MONTH + "?EmployeeId=" + empolyeeId, METHOD_GET, "NONE");
	}

	//Selected Route No Details
	public static void getrouteDetails(NetworkOperationListener listener, Object tag, String empolyeeId, String RouteId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_ROUTE_DETAILS_WITH_ROUTEID + "?EmployeeId=" + empolyeeId + "&RouteId=" + RouteId, METHOD_GET, "NONE");
	}

	//Month Summary
	public static void monthSummary(NetworkOperationListener listener, Object tag, String empolyeeId)
	{
		Log.d("jsonrequest", empolyeeId);
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(MONTH_SUMMARY + "?EmployeeId=" + empolyeeId, METHOD_GET, "NONE");
	}

	//Month Summary
	public static void pendingBills(NetworkOperationListener listener, Object tag, String empolyeeId)
	{
		Log.d("jsonrequest", empolyeeId);
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(PENDING_BILLS + "?EmployeeId=" + empolyeeId, METHOD_GET, "NONE");
	}

	public static void routeAccept(NetworkOperationListener listener, Object tag, String checkedRoutesAfterreplace, String uncheckedRoutesAfterreplace)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		Log.e("RoutesParams", checkedRoutesAfterreplace + uncheckedRoutesAfterreplace);
		//operation.execute(GET_ROUTE_ACCEPT_BY_EMP + "?EmployeeRouteId=" + selectedRouteids, METHOD_POST, "NONE");
//		operation.execute(GET_ROUTE_ACCEPT_BY_EMP + "?EmployeeRouteId=" + selectedRouteids, METHOD_POST, "NONE");
		operation.execute(GET_ROUTE_ACCEPT_BY_EMP + "?EnableEmployeeRouteId=" + checkedRoutesAfterreplace + "&DisbleEmployeeRouteId=" + uncheckedRoutesAfterreplace, METHOD_POST,
		                  "NONE");
	}

	public static void getOrderBookList(NetworkOperationListener listener, String Userid, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_ORDER_BOOK_LIST + "&UserId=" + Userid, METHOD_GET, "NONE");
	}

	public static void getInvoiceList(NetworkOperationListener listener, String Userid, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_INVOICE_LIST + "&UserId=" + Userid, METHOD_GET, "NONE");
	}
	//http://202.143.96.20/Orderstest/api/Services/GetOrderBookingDetails?OrderNumber=0

	///////Start Trip Services
	//Route Drop down
	public static void getRoutedetails(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_ROUTE_DROPDOWN + "?EmployeeId=" + value, METHOD_GET, "NONE");
	}

	//Route Drop down
	public static void getAreaNamesByRouteId(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_AREA_NAME + "?RouteId=" + value, METHOD_GET, "NONE");
	}

	//Get Shop Color
	public static void getShopColors(NetworkOperationListener listener, Object tag)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(SHOP_COLORS_URL, METHOD_GET, "NONE");
	}

	//Route Drop down
	public static void getShopDetailsByAreaId(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(GET_SHOP_DETAILS + "?AreaId=" + value, METHOD_GET, "NONE");
	}


	//////Pending Bill Details
	public static void getPedingbillgDetailsByCustomerId(NetworkOperationListener listener, Object tag, String value)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(PENDING_BILL_DETAILS + "?AreaId=" + value, METHOD_GET, "NONE");
	}


	//////Pending Bill Details
	public static void orderDelete(NetworkOperationListener listener, Object tag, String params)
	{
		Log.d("jsonrequest", params);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(DELETE_ORDER, METHOD_POST, params);
	}

	//////Order Edit
	public static void orderEdit(NetworkOperationListener listener, Object tag, String orderId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(EDIT_ORDER + "?OrderId=" + orderId, METHOD_GET, "NONE");
	}

	//////Invoice Edit
	public static void invoiceEdit(NetworkOperationListener listener, Object tag, String orderId)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(EDIT_INVOICE + "?OrderSalesId=" + orderId, METHOD_GET, "NONE");
	}


	//////Pending Bill Details
	public static void orderSummryProductCategory(NetworkOperationListener listener, Object tag, String orderNumber)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(ORDER_PRODCTCATEGORY_SUMMARY + "?OrderNumber=" + orderNumber, METHOD_GET, "NONE");
	}

	//Updateorderbooking
	public static void updateOrderBooking(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(UPDATE_ORDERBOOK, METHOD_POST, jsonString);
	}


	//Update Customer
	public static void updateCustomerSave(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString + "");
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(UPDATESHOP_DETAILS, METHOD_POST, jsonString);
	}


	//Update Invoice
	public static void updateInvoiceSave(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(UPDATE_INVOICE, METHOD_POST, jsonString);
	}

	public static void updateInvoiceSubmit(NetworkOperationListener listener, Object tag, String jsonString)
	{
		// TODO Auto-generated method stub
		Log.d("jsonrequest", jsonString);
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		operation.execute(UPDATE_INVOICE_SUBMIT, METHOD_POST, jsonString);
	}

//	public static void zoneCreationSubmit(NetworkOperationListener listener, Object tag, String ZoneCode, String ZoneName,String ZoneDescription)
//	{
//		// TODO Auto-generated method stub
//		NetworkOperation operation = new NetworkOperation(listener, tag);
//		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
//		Log.e("Params", checkedRoutesAfterreplace + uncheckedRoutesAfterreplace);
//		operation.execute(ZONE_CREATION
//				                  + "?EnableEmployeeRouteId=" + checkedRoutesAfterreplace
//				                  + "&DisbleEmployeeRouteId=" + uncheckedRoutesAfterreplace, METHOD_POST, "NONE");
//	}

	public static void routeCreationSubmit(NetworkOperationListener listener, Object tag, String checkedRoutesAfterreplace, String uncheckedRoutesAfterreplace)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		Log.e("Params", checkedRoutesAfterreplace + uncheckedRoutesAfterreplace);
		operation.execute(ROUTE_CREATION
				                  + "?EnableEmployeeRouteId=" + checkedRoutesAfterreplace
				                  + "&DisbleEmployeeRouteId=" + uncheckedRoutesAfterreplace, METHOD_POST, "NONE");
	}

	public static void areaCreationSubmit(NetworkOperationListener listener, Object tag, String checkedRoutesAfterreplace, String uncheckedRoutesAfterreplace)
	{
		// TODO Auto-generated method stub
		NetworkOperation operation = new NetworkOperation(listener, tag);
		operation.setContentType(CONTENT_TYPE_APPLICATION_JSON);
		Log.e("Params", checkedRoutesAfterreplace + uncheckedRoutesAfterreplace);
		operation.execute(AREA_CREATION
				                  + "?EnableEmployeeRouteId=" + checkedRoutesAfterreplace
				                  + "&DisbleEmployeeRouteId=" + uncheckedRoutesAfterreplace, METHOD_POST, "NONE");
	}


}
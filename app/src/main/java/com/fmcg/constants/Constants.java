package com.fmcg.constants;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruchi Tiwari on 05-12-2016.
 */

public class Constants
{
	public static boolean isResetPIN = false;
	public static boolean isFamilySel = false;
	public static boolean isTrack = false;
	public static boolean isLogin = false;
	
	public static final String pendingRegistrationSTATUS = "STATUS";
	public static final String login_credentials = "login_credentials";
	public static final String loggedin_username = "loggedin_username";
	public static final String loggedin_pwd = "loggedin_pwd";
	public static final String rememberme = "rememberme";

	public static final String isAppLaunched = "isAppLaunched";


	public static String isMember = "";

	public static final String constantUsername = "userid";

	public static String storedAccesstion = "";

	public static final String successOrFailed = "";
	public static final String fieldToFetchStatus = "";

	public static final String registered_name = "name";
	public static final String registered_fname = "fname";
	public static final String registered_lname = "lname";
	public static final String registered_salutation = "salutation";
	public static final String registered_mobile = "mobile";
	public static final String registered_email = "email";
	public static final String registered_dob = "dob";
	public static final String registered_gender = "gender";
	public static final String registered_apprxage = "age";
	public static final String registered_years = "years";
	public static final String registered_months = "months";
	public static final String registered_days = "days";

	
	//----pending registration constants
	public static final String deviceID = "deviceid";
	public static String devicetobepassed = "0005";

	//-----userregistration constants
	public static final String IN_TITLE = "IN_TITLE";
	public static final String IN_FIRST_NAME = "IN_FIRST_NAME";
	public static final String IN_LAST_NAME = "IN_LAST_NAME";
	public static final String IN_DOB = "IN_DOB";
	public static final String IN_AGE_YYY = "IN_AGE_YYY";
	public static final String IN_AGE_MM = "IN_AGE_MM";
	public static final String IN_AGE_DD = "IN_AGE_DD";
	public static final String IN_GENDER = "IN_GENDER";
	public static final String IN_MOBILE_NO = "IN_MOBILE_NO";
	public static final String IN_ADDRESS1 = "IN_ADDRESS1";
	public static final String IN_ADDRESS2 = "IN_ADDRESS2";
	public static final String IN_LANDMARK = "IN_LANDMARK";
	public static final String IN_CITY = "IN_CITY";
	public static final String IN_STATE = "IN_STATE";
	public static final String IN_COUNTRY = "IN_COUNTRY";
	public static final String IN_EMAIL_ID = "IN_EMAIL_ID";
	public static final String IN_ZIP = "IN_ZIP";
	public static final String IN_DEVICE_ID = "IN_DEVICE_ID";
	public static final String IN_OSTYPE = "IN_OSTYPE";
	public static final String IN_OSVERSION = "IN_OSVERSION";
	public static final String IN_MYSRLVER = "IN_MYSRLVER";
	public static final String IN_USER_ID = "IN_USER_ID";
	public static final String IN_OTP = "IN_OTP";
	public static final String resendOTPRegistration = "OTP";
	public static final String ptntcode = "ptntcode";
	public static final String pwd = "pwd";

	public static final String loginResponse = "msg";
	public static final String userRegMsg = "message";
	public static final String fieldGetResponse = "msg";
	public static final String fetchedForgotPwdResponse = "RESPONSE";

	//Authenicated User Details
	public static final String jsonFieldUserID = "USER_ID";
	public static final String jsonFieldPtnt_cd = "PTNT_CD";
	public static final String jsonFieldPtnt_title = "PTNT_TITTLE";
	public static final String jsonFieldFirstName = "FIRST_NAME";
	public static final String jsonFieldLastName = "LAST_NAME";
	public static final String jsonFieldGender = "GENDER";
	public static final String jsonFieldDob = "DOB";
	public static final String jsonFieldmartial_status = "MARITAL_STATUS";
	public static final String jsonFieldEmail = "EMAIL_ID";
	public static final String jsonFieldZip = "ZIP";
	public static final String jsonFieldMOBILE = "MOBILE_NO";
	public static final String jsonFieldAddress1 = "ADDRESS1";
	public static final String jsonFieldAddress2 = "ADDRESS2";
	public static final String jsonFieldlandmark = "LANDMARK";
	public static final String jsonFieldcity = "CITY";
	public static final String jsonFieldstate = "STATE";
	public static final String jsonFieldMcountry = "COUNTRY";
	public static final String jsonFieldparentid = "PARENT_ID";
	public static final String STATUS = "STATUS";   //main user Login Testing

	public static boolean isPatientDetails = false;
	public static boolean isPackage = false;

	public static boolean isRegEdited = false;
	public static boolean isPatEdited = false;



	public static final String response_code = "code";
	public static final String response_msg = "msg";
	public static final String response_data_create = "data";
	public static final String top_products = "TOP_PRDCT";
	public static final String products = "PRODUCTS";

	public static final String prdct = "PRDCT";





	public static final String booktest_package_id = "ID";
	public static final String booktest_package_product_code = "PRDCT_CODE";
	public static final String booktest_package_product_aliases = "PRDCT_ALIASES";
	public static final String booktest_package_gross_amt = "GROSS_AMT";
	public static final String booktest_package_price = "PRICE";
	public static final String booktest_package_disc = "DISC";
	public static final String booktest_package_disc_type = "DISC_TYPE";
	public static final String booktest_package_prdct_constnts = "PRDCT_CONSTNS";
	public static final String booktest_package_category = "CATEGORY";
	public static final String booktest_package_product_instrctn = "PTNT_INSTRCTN";
	public static final String booktest_package_rep_tat = "REP_TAT";
	public static final String booktest_package_info = "INFO";
	public static final String packages = "PACKAGES";

	public static final String headerID = "LBL_ID";
	public static final String header_lbl_name = "LBL_NAME";
	public static final String header_lbl_header = "LBL_HDR";
	public static final String header_display_flag = "DISP_FLAG";
	public static final String header_order_id = "ORDER_ID";

	public static final String sharedPreferencePatientData = "patientData";
	public static final String sharedPreferenceCollectionData = "collectionData";
	public static final String sharedPreferenceSelectedLoginActivity = "selectedloginactivity";

	public static int m_mycart = 2;
	public static final int m_reports = 1;
	public static final int m_cart = 2;
	public static final int m_lab = 3;
	public static final int m_family = 4;
	public static final int m_profile= 5;
	public static final int m_health= 6;
	public static final int m_settings= 7;
	public static final int m_orders= 8;
	public static final int m_pwd= 9;
	public static final int m_login = 0;

	public static double latitude = 0;
	public static double longitude = 0;

	//Offer List
	public static final String JSON_TAG_DATA = "data";
	public static final String offer_id = "ID";
	public static final String offer_description = "DESCRIPTION";
	public static final String offer_image_url = "IMAGE_URL";
	public static final String offer_created_date = "CREATED_DATE";
	public static final String offer_active_date = "ACTIVE_FLAG";
	public static final String offer_name = "NAME";
	public static final String offer_from_date = "FRM_DT";
	public static final String offer_to_date = "TO_DT";
	public static final String offer_sequence = "SEQUENCE";
	public static final String offer_test_code = "TESTCODE";
	public static final String offer_discount_type = "DISCOUNT_TYPE";
	public static final String offer_perc_amt = "PERC_AMT";
	public static final String offer_action = "ACTION";
	public static final String offer_banner_flag = "BANNER_FLG";



	public static boolean isLabOrCollection = false;


	//User information
	public static final String userid = "USER_ID";
	public static final String ptnt_cd = "PTNT_CD";
	public static final String ptnt_tittle = "PTNT_TITTLE";
	public static final String first_name = "FIRST_NAME";
	public static final String last_name = "LAST_NAME";
	public static final String gender = "GENDER";
	public static final String dob = "DOB";
	public static final String marital_status = "MARITAL_STATUS";
	public static final String email_id = "EMAIL_ID";
	public static final String zip = "ZIP";
	public static final String mobile_no = "MOBILE_NO";
	public static final String address1 = "ADDRESS1";
	public static final String address2 = "ADDRESS2";
	public static final String landmark = "LANDMARK";
	public static final String city = "CITY";
	public static final String state = "STATE";
	public static final String country = "COUNTRY";
	public static final String parent_id = "PARENT_ID";


	public static final String playStoreUrl ="https://play.google.com/store/apps/details?id=com.srllimited.srl&hl=en";

	public static final String accessionNo = "";

	public static boolean isRadio = false;

	public static final String state_id = "STATE_ID";
	public static final String state_name = "STATE_NAME";

	public static final String city_name = "CITY_NAM";
	public static final String mobikwikUrl = "http://test.mobikwik.com/wallet?email=ritz@gmail.com&amount=345&cell=9704683480&orderid=abcd&mid=MBK9002&merchantname=mobikwik.com&redirecturl=https://www.mobikwik.com/moneytransfer/send-to-wallet&showmobile=true&version=1&checksum=ju6tygh7u7tdg554k098ujd5468o";

//	public static final String mobikwikUrl = "http://test.mobikwik.com/wallet?email=ritz@gmail.com&amount=345&cell=9704683480&orderid=abcd&mid=MBK9002&merchantname=mobikwik.com&redirecturl=https://www.mobikwik.com/&showmobile=true&version=1&checksum=ju6tygh7u7tdg554k098ujd5468o";

}

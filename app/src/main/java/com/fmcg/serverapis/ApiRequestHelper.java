//package com.fmcg.serverapis;
//
//import com.srllimited.srl.util.Log;
//
//import java.util.Map;
//
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.ALL_CATEGORIES;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.CANCEL_ORDER;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.ChangePassword;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.FILTER_PRODUCTS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.FORGOT_PWD;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_CAROUSELS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_CITY;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_FEEDBACK;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_PAY_SUCCESS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_PDF;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_PRIVACY;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_PROMO;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.GET_TERMS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.Get_CONTENT;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.LAB_LOCATIONS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.LOGIN;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.MY_CART;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.OFFER_LIST;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.ORDERS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.PACKAGE_DETAILS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.PAY;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.PENDING_REGISTRATION;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.REPEAT_ORDER;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.REPORTS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.RESEND_OTP;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.SEARCH_PRODUCTS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.TOP_PRODUCTS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.TRACK_ORDER;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.UPDATE_USER_DETAILS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.USER;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.USER_DETAILS;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.USER_REGISTRATION;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.VALIDATE_MOBIKWIK;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.VALIDATE_PROMO;
//import static com.srllimited.srl.serverapis.ApiRequestReferralCode.VALIDATE_REGISTRATION;
//
///**
// * Created by RuchiTiwari on 1/14/2017.
// */
//public class ApiRequestHelper
//{
//    public static ApiRequest getTopProducts(String city, String patientCode)
//    {
//
//        Map<String, String> params = ApiParameters.passGetTopProductsParams(city, patientCode);
//
//        ApiRequest request = new ApiRequest(TOP_PRODUCTS, ApiConstants.getTopProductsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getCity()
//    {
//        Map<String, String> params = ApiParameters.passGetCitiesParams();
//
//        ApiRequest request = new ApiRequest(GET_CITY, ApiConstants.cityListurl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getCarousels(String city, String patient_code, String str)
//    {
//        Map<String, String> params = ApiParameters.getOfferList(city, patient_code, "");
//
//        ApiRequest request = new ApiRequest(GET_CAROUSELS, ApiConstants.getCarouselsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
////
////	public static ApiRequest getCarousels()
////	{
////
////		ApiRequest request = new ApiRequest(GET_CAROUSELS, ApiConstants.getCarouselsUrl);
////		return request;
////	}
//
//    public static ApiRequest getTrackOrder(String order)
//    {
//        Map<String, String> params = ApiParameters.getTrackOrder(order);
//
//        ApiRequest request = new ApiRequest(TRACK_ORDER, ApiConstants.TRACK_ORDERURL);
//        request.setParams(params);
//        return request;
//    }
//
//    public static ApiRequest getRepeatOrder(String order)
//    {
//        Map<String, String> params = ApiParameters.repeatOrder(order);
//
//        ApiRequest request = new ApiRequest(REPEAT_ORDER, ApiConstants.REPEAT_ORDERURL);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getCancelOrder(String order)
//    {
//        Map<String, String> params = ApiParameters.cancelOrder(order);
//
//        ApiRequest request = new ApiRequest(CANCEL_ORDER, ApiConstants.CANCEL_ORDERURL);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getFilterProducts(String city, String type, String category)
//    {
//        Map<String, String> params = ApiParameters.passFilterProducts(city, type, category);
//
//        ApiRequest request = new ApiRequest(FILTER_PRODUCTS, ApiConstants.filterProductsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getPackageDetails(String city, String category, String ptntcode, String str)
//    {
//        Map<String, String> params = ApiParameters.passGetPackageDetails(city, category, ptntcode, str);
//
//        ApiRequest request = new ApiRequest(PACKAGE_DETAILS, ApiConstants.getPackageDetailsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getPaySuccess(String orderno, String payid, String paydt, String amt, String transid, String paymethod, String ipaddress, String paystatus,
//                                           String payopt)
//    {
//        Map<String, String> params = ApiParameters.passGetPaySuccess(orderno, payid, paydt, amt, transid, paymethod, ipaddress, paystatus, payopt);
//
//        ApiRequest request = new ApiRequest(GET_PAY_SUCCESS, ApiConstants.getPaySuccessUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getAllCategories()
//    {
//        Map<String, String> params = ApiParameters.passGetAllCategories();
//
//        ApiRequest request = new ApiRequest(ALL_CATEGORIES, ApiConstants.getAllCategoriesUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getMyCartDetails(String city, String testCode, String testId, String totalAmt, String grossAmt,
//                                              String totaldisc, String ofrCode, String productGrossAmt, String disc, String promodisc,
//                                              String otherCharge1, String otherCharge2, String grandTotal, String roundAmt, String promoCode)
//    {
//
//        Map<String, String> params = ApiParameters.passMyCartDetails(city, testCode, testId, totalAmt, grossAmt, totaldisc,
//                                                                     ofrCode, productGrossAmt, disc, promodisc, otherCharge1,
//                                                                     otherCharge2, grandTotal, roundAmt, promoCode);
//
//        ApiRequest request = new ApiRequest(MY_CART, ApiConstants.myCartUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getPayDetails(String flag, String orderid, String ptntcode, String address, String loc,
//                                           String city, String state, String country, String zip, String doctor,
//                                           String colDateFrom, String colDateTo, String hardCopy, String colContact,
//                                           String test, String ptntName, String createBy, String grossAmt, String discount_flag,
//                                           String discount, String promocode, String paymode, String paymentOpt,
//                                           String title, String firstName, String lastName, String dob, String gender,
//                                           String dobActFlg, String mobileNo, String emailId, String cartId,
//                                           String coltype, String labid)
//    {
//
//        Map<String, String> params = ApiParameters.passPayDetails(flag, orderid, ptntcode, address, loc,
//                                                                  city, state, country, zip, doctor,
//                                                                  colDateFrom, colDateTo, hardCopy, colContact,
//                                                                  test, ptntName, createBy, grossAmt, discount_flag,
//                                                                  discount, promocode, paymode, paymentOpt,
//                                                                  title, firstName, lastName, dob, gender,
//                                                                  dobActFlg, mobileNo, emailId, cartId, coltype, labid);
//
//        Log.e("params", params + "");
//        ApiRequest request = new ApiRequest(PAY, ApiConstants.getPayUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    //Get Offer Details List
//    public static ApiRequest getOfferList(String city, String patient_code, String str)
//    {
//        Map<String, String> params = ApiParameters.getOfferList(city, patient_code, "");
//
//        ApiRequest request = new ApiRequest(OFFER_LIST, ApiConstants.getPromotionOffersUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getLabLocations(String city, String landmark, String pincode, String versionId,
//                                             String radius, String lon, String lat)
//    {
//        Map<String, String> params = ApiParameters.passLabLocations(city, landmark, pincode, versionId, radius, lon, lat);
//
//        ApiRequest request = new ApiRequest(LAB_LOCATIONS, ApiConstants.getLabLocationsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getOrders(String createdBy)
//    {
//        Map<String, String> params = ApiParameters.passGetOrders(createdBy);
//
//        ApiRequest request = new ApiRequest(ORDERS, ApiConstants.getOrdersUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getReports(String ptntcode, String pwd, String deviceid, String deviceToken,
//                                        String ostype, String osversion, String mysrl_version, String timestamp)
//    {
//        Map<String, String> params = ApiParameters.passGetReports(ptntcode, pwd, deviceid, deviceToken, ostype, osversion, mysrl_version, timestamp);
//        ApiRequest request = new ApiRequest(REPORTS, ApiConstants.getREportsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getUserDetails(String ptntcode)
//    {
//        Map<String, String> params = ApiParameters.getUserDetails(ptntcode);
//
//        ApiRequest request = new ApiRequest(USER_DETAILS, ApiConstants.getUserDetailsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getUpdateUserDetails(String commonno, String userId, String password, String mobileNo, String emailid,
//                                                  String address1, String address2, String landmark, String city, String state, String country, String zip)
//    {
//
//        Map<String, String> params = ApiParameters.getUpdateUserDetails(commonno, userId, password, mobileNo, emailid,
//                                                                        address1, address2, landmark, city, state, country, zip);
//
//        ApiRequest request = new ApiRequest(UPDATE_USER_DETAILS, ApiConstants.updateUserDetails);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getPromoCodes(String deviceid, String ptntcode, String mobile)
//    {
//        Map<String, String> params = ApiParameters.getPromoCodes(deviceid, ptntcode, mobile);
//
//        ApiRequest request = new ApiRequest(GET_PROMO, ApiConstants.getPromoUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getValidatedPromo(String promo, String deviceid, String ptntcode, String mobile)
//    {
//        Map<String, String> params = ApiParameters.getValidatedPromo(promo, deviceid, ptntcode, mobile);
//
//        ApiRequest request = new ApiRequest(VALIDATE_PROMO, ApiConstants.validatePromoUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getSearchedProducts(String city, String text)
//    {
//        Map<String, String> params = ApiParameters.getSearchedProducts(city, text);
//
//        ApiRequest request = new ApiRequest(SEARCH_PRODUCTS, ApiConstants.searchProductsUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getPdfReports(String city, String acc, String ptntcode, String mobile, String str,
//                                           String deviceid, String deviceToken, String ostype, String osver, String mysrlver)
//    {
//        Map<String, String> params = ApiParameters.getPdfReports(city, acc, ptntcode, mobile, str, deviceid, deviceToken, ostype, osver, mysrlver);
//
//        ApiRequest request = new ApiRequest(GET_PDF, ApiConstants.getpdfUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getLogin(String userid, String pwd)
//    {
//        Map<String, String> params = ApiParameters.login(userid, pwd);
//
//        ApiRequest request = new ApiRequest(LOGIN, ApiConstants.loginUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getForgotPwd(String userid)
//    {
//        Map<String, String> params = ApiParameters.forgotPwd(userid);
//
//        ApiRequest request = new ApiRequest(FORGOT_PWD, ApiConstants.forgotPwdUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getPendingRegistration(String devid, String devtobpassed)
//    {
//        Map<String, String> params = ApiParameters.pendingRegistration(devid, devtobpassed);
//
//        ApiRequest request = new ApiRequest(PENDING_REGISTRATION, ApiConstants.pendingRegistrationUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getUser(String userid)
//    {
//        Map<String, String> params = ApiParameters.forgotPwd(userid);
//        ApiRequest request = new ApiRequest(USER, ApiConstants.getUserUrl);
//        request.setParams(params);
//        return request;
//    }
//
//
//    public static ApiRequest getResendOtp(String devidtobepassed)
//    {
//        Map<String, String> params = ApiParameters.resendOtp(devidtobepassed);
//
//        ApiRequest request = new ApiRequest(RESEND_OTP, ApiConstants.resendOTPUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getChangePassword(String ptntcode, String oldpwd, String newpwd)
//    {
//        Map<String, String> params = ApiParameters.getConfirmPassword(ptntcode, oldpwd, newpwd);
//
//        ApiRequest request = new ApiRequest(ChangePassword, ApiConstants.changePasswordURl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getContent(String screenid, String verid)
//    {
//        Map<String, String> params = ApiParameters.getContentAPI(screenid, verid);
//
//        ApiRequest request = new ApiRequest(Get_CONTENT, ApiConstants.getContentUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getPrivacy(String screenid, String verid)
//    {
//        Map<String, String> params = ApiParameters.getPrivacyAPI(screenid, verid);
//        ApiRequest request = new ApiRequest(GET_PRIVACY, ApiConstants.getContentUrl);
//        request.setParams(params);
//        return request;
//    }
//
//    public static ApiRequest getTermsOfUse(String screenid, String verid)
//    {
//        Map<String, String> params = ApiParameters.getTermsAPI(screenid, verid);
//        ApiRequest request = new ApiRequest(GET_TERMS, ApiConstants.getContentUrl);
//        request.setParams(params);
//        return request;
//    }
//
//
//    public static ApiRequest getValidateRegistation(String devid, String devidtobepassed)
//    {
//        Map<String, String> params = ApiParameters.validateRegistration(devid, devidtobepassed);
//        ApiRequest request = new ApiRequest(VALIDATE_REGISTRATION, ApiConstants.validateRegistrationUrl);
//        request.setParams(params);
//        return request;
//    }
//
//    public static ApiRequest getMobikiwik(String email, String
//            amount, String
//                                                  cell, String
//                                                  orderid, String
//                                                  mid, String
//                                                  merchantname, String
//                                                  redirecturl, String
//                                                  showmobile, String
//                                                  version, String
//                                                  checksum)
//    {
//        Map<String, String> params = ApiParameters.getMobikwik(email,
//                                                               amount,
//                                                               cell,
//                                                               orderid,
//                                                               mid,
//                                                               merchantname,
//                                                               redirecturl,
//                                                               showmobile,
//                                                               version,
//                                                               checksum);
//
//        ApiRequest request = new ApiRequest(VALIDATE_MOBIKWIK, ApiConstants.validatemobikwikurl);
//        request.setParams(params);
//
//        return request;
//    }
//
//
//    public static ApiRequest getUserRegistation(String salutation, String fname, String lname, String dob, String years,
//                                                String months, String days, String gender, String mobile, String address1,
//                                                String address2, String landmark, String city, String state, String country,
//                                                String email, String zip, String deviceid, String ostype,
//                                                String osversion, String mysrlver)
//    {
//        Map<String, String> params = ApiParameters.userRegistration(salutation, fname, lname, dob, years, months, days,
//                                                                    gender, mobile, address1, address2, landmark, city,
//                                                                    state, country, email, zip, deviceid, ostype, osversion,
//                                                                    mysrlver);
//
//        ApiRequest request = new ApiRequest(USER_REGISTRATION, ApiConstants.userRegistrationUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    ///
////Rate Us
//    public static ApiRequest getRateUs(String ptnt_cd, String p_rating, String p_feedBack, String p_deviceId, String p_os_version,
//                                       String p_os_type, String p_app_version)
//    {
//        Map<String, String> params = ApiParameters.getRateusSuccess(ptnt_cd, p_rating, p_feedBack, p_deviceId, p_os_version, p_os_type, p_app_version);
//
//        ApiRequest request = new ApiRequest(GET_FEEDBACK, ApiConstants.getFeedbackUrl);
//        request.setParams(params);
//
//        return request;
//    }
//
//    public static ApiRequest getAllStates()
//    {
//        Map<String, String> params = ApiParameters.getStatesList();
//
//        ApiRequest request = new ApiRequest(ApiRequestReferralCode.GET_STATES, ApiConstants.allStatesurl);
//        request.setParams(params);
//        return request;
//    }
//
//    //Get Cities List based on States
//    public static ApiRequest getCities(String stateId)
//    {
//        Map<String, String> params = ApiParameters.getCitiesList(stateId);
//        ApiRequest request = new ApiRequest(ApiRequestReferralCode.GET_CITIES, ApiConstants.citiesurl);
//        request.setParams(params);
//        return request;
//    }
/////
//
//
/////////////
//
//
//}

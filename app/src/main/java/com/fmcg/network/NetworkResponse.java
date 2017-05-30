
package com.fmcg.network;

public class NetworkResponse {

	private int statusCode;
	private String responseString;
	private String responseStatus;
	private Object tag;
	private Object responseObject;

	public Object getResponseObject() {
		return responseObject;
	}
	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statuscode) {
		this.statusCode = statuscode;
	}
	public String getResponseString() {
		return responseString;
	}
	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}

}

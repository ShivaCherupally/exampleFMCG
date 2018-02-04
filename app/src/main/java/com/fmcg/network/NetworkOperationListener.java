
package com.fmcg.network;


public interface NetworkOperationListener {
	public void operationCompleted(NetworkResponse response);

	public void showToast(String string, int lengthLong);
}

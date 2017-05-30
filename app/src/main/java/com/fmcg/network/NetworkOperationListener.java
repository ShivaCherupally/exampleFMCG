
package com.fmcg.network;

import com.fmcg.network.NetworkResponse;

public interface NetworkOperationListener {
	public void operationCompleted(NetworkResponse response);

	public void showToast(String string, int lengthLong);
}

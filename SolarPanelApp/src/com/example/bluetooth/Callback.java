package com.example.bluetooth;

import com.example.solarpanelmanager.api.responses.BaseResponse;

public interface Callback<T extends BaseResponse> extends GenericCallback<T> {

	@Override
	public void onComplete(T response);
}

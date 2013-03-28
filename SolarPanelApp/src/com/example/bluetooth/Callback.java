package com.example.bluetooth;

import com.example.solarpanelmanager.api.responses.BaseResponse;

public interface Callback<T extends BaseResponse> {

	public void onComplete(T response);
}

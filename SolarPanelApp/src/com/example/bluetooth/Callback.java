package com.example.bluetooth;

import com.example.solarpanelmanager.api.responses.BaseResponse;

public interface Callback {

	public void onComplete(BaseResponse json);
}

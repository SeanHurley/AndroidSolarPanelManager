package com.example.bluetooth;

import com.example.solarpanelmanager.api.responses.BaseResponse;


public class HandshakeHandler extends BaseResponseHandler {

	public HandshakeHandler(Callback callback, String target) {
		super(callback, target);
	}

	@Override
	protected String getRequest() {
 		return "{\"message\": \"hello!\"}";
	}

}

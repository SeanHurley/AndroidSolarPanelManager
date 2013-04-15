package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.responses.BaseResponse;

/**
 * @author mikecandido
 * 
 */
public class HandshakeHandler extends BaseResponseHandler {

	public HandshakeHandler(Callback<BaseResponse> callback, String target, String pass) {
		super(callback, target, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put("message", "hello!");
		return json;
	}

}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.responses.BaseResponse;

public class PINUpdateHandler extends CommunicationHandler {
	
	private String pin;

	public PINUpdateHandler(Callback callback, String pin) {
		super(callback);
		this.pin = pin;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", "pin-update");
		json.put("pin", pin);
		return json.toJSONString();
	}
}

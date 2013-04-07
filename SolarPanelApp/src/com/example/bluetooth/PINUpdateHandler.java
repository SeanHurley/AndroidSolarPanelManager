package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class PINUpdateHandler extends BaseResponseHandler {

	private String pin;

	public PINUpdateHandler(Callback<BaseResponse> callback, String device, String pass, String pin) {
		super(callback, device, pass);
		this.pin = pin;
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.PIN_UPDATE);
		json.put(MessageKeys.NEW_PIN_PASSWORD, pin);
		return json;
	}
}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class PINUpdateHandler extends CommunicationHandler {

	private String pin;

	public PINUpdateHandler(Callback callback, String pin) {
		super(callback);
		this.pin = pin;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.PIN_UPDATE);
		json.put(MessageKeys.PIN_PASSWORD, pin);
		return json.toJSONString();
	}
}

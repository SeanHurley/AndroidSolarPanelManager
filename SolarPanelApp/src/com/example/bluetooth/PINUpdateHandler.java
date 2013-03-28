package com.example.bluetooth;

import net.minidev.json.JSONObject;

public class PINUpdateHandler extends CommunicationHandler {
	
	private String pin;

	public PINUpdateHandler(Callback callback, String device, String pin) {
		super(callback, device);
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

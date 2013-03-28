package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class SetChargeConstraintsHandler extends CommunicationHandler {
	private int max;
	private int min;
	
	public SetChargeConstraintsHandler(Callback callback, String device, int max, int min) {
		super(callback, device);
		this.max = max;
		this.min = min;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", MessageTypes.SET_CHARGE_CONSTRAINTS);
		json.put("max", max);
		json.put("min", min);
		return json.toJSONString();
	}
}

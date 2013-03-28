package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class ViewChargeConstraintsHandler extends CommunicationHandler {
	
	public ViewChargeConstraintsHandler(Callback callback, String device) {
		super(callback, device);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", MessageTypes.VIEW_CHARGE_CONSTRAINTS);
		return json.toJSONString();
	}
	
	protected ViewChargeConstraintsResponse parseResponse(String data) {
		return ResponseParser.parseViewChargeConstraintsResponse(data);
	}

}

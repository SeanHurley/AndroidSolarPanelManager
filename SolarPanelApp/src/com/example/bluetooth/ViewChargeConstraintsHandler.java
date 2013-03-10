package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

import net.minidev.json.JSONObject;

public class ViewChargeConstraintsHandler extends CommunicationHandler {
	
	

	public ViewChargeConstraintsHandler(Callback callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.VIEW_CHARGE_CONSTRAINTS);
		return json.toJSONString();
	}
	
	protected ViewChargeConstraintsResponse parseResponse(String data) {
		return ResponseParser.parseViewChargeConstraintsResponse(data);
	}

}

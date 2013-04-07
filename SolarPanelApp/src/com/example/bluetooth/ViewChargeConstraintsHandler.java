package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class ViewChargeConstraintsHandler extends CommunicationHandler<ViewChargeConstraintsResponse> {

	public ViewChargeConstraintsHandler(Callback<ViewChargeConstraintsResponse> callback, String device, String pass) {
		super(callback, device, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.VIEW_CHARGE_CONSTRAINTS);
		return json;
	}

	@Override
	protected ViewChargeConstraintsResponse parseResponse(String data) {
		if (data == "") {
			return new ViewChargeConstraintsResponse(500, null, -1, -1);
		}

		return ResponseParser.parseViewChargeConstraintsResponse(data);
	}

}

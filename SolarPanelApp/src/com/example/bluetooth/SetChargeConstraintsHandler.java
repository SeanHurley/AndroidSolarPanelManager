package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class SetChargeConstraintsHandler extends BaseResponseHandler {
	private int max;
	private int min;

	public SetChargeConstraintsHandler(Callback<BaseResponse> callback, int max, int min) {
		super(callback);
		this.max = max;
		this.min = min;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SET_CHARGE_CONSTRAINTS);
		json.put(MessageKeys.CHARGE_MAX, max);
		json.put(MessageKeys.CHARGE_MIN, min);
		return json.toJSONString();
	}
}

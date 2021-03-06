package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

public class SetChargeConstraintsHandler extends BaseResponseHandler {
	private int max;
	private int min;

	public SetChargeConstraintsHandler(Callback<BaseResponse> callback, String device, String pass, int max, int min) {
		super(callback, device, pass);
		this.max = max;
		this.min = min;
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SET_CHARGE_CONSTRAINTS);
		json.put(MessageKeys.CHARGE_MAX, max);
		json.put(MessageKeys.CHARGE_MIN, min);
		return json;
	}
}

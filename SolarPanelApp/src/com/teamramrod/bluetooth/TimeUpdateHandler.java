package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

public class TimeUpdateHandler extends BaseResponseHandler {

	private long timestamp;

	public TimeUpdateHandler(Callback<BaseResponse> callback, String device, String pass, long timestamp) {
		super(callback, device, pass);
		this.timestamp = timestamp;
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.TIME_UPDATE);
		json.put(MessageKeys.TIME_TIME, this.timestamp);
		return json;
	}
}

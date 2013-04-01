package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class UnscheduleEventHandler extends BaseResponseHandler {

	private String id;

	public UnscheduleEventHandler(Callback<BaseResponse> callback, String device, String id) {
		super(callback, device);
		this.id = id;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.UNSCHEDULE_EVENT);
		json.put(MessageKeys.EVENT_ID, id);
		return json.toJSONString();
	}

}

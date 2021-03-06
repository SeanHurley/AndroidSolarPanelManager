package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

public class UnscheduleEventHandler extends BaseResponseHandler {

	private String id;

	public UnscheduleEventHandler(Callback<BaseResponse> callback, String device, String pass, String id) {
		super(callback, device, pass);
		this.id = id;
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.UNSCHEDULE_EVENT);
		json.put(MessageKeys.EVENT_ID, id);
		return json;
	}

}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class TimeUpdateHandler extends CommunicationHandler {
	
	private long timestamp;
	
	public TimeUpdateHandler(Callback callback, long timestamp) {
		super(callback);
		this.timestamp = timestamp;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.TIME_UPDATE);
		json.put(MessageKeys.TIME_TIME, this.timestamp);
		return json.toJSONString();
	}
}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

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
		json.put("type", MessageTypes.TIME_UPDATE);
		json.put("timestamp", this.timestamp);
		return json.toJSONString();
	}
}

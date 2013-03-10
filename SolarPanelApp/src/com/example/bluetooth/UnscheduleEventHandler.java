package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

import net.minidev.json.JSONObject;

public class UnscheduleEventHandler extends CommunicationHandler {
	
	private String id;
	
	public UnscheduleEventHandler(Callback callback, String id) {
		super(callback);
		this.id = id;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", MessageTypes.UNSCHEDULE_EVENT);
		json.put("identifier", id);
		return json.toJSONString();
	}

}

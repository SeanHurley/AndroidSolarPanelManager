package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class UnscheduleEventHandler extends CommunicationHandler {
	
	private String id;
	
	public UnscheduleEventHandler(Callback callback, String device, String id) {
		super(callback, device);
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

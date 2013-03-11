package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
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
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.UNSCHEDULE_EVENT);
		json.put(MessageKeys.EVENT_ID, id);
		return json.toJSONString();
	}

}

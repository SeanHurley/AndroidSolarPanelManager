package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.EventsResponse;

public class EventHandler extends CommunicationHandler {
	
	public EventHandler(Callback callback, String device) {
		super(callback, device);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", MessageTypes.EVENTS);
		return json.toJSONString();
	}
	
	protected EventsResponse parseResponse(String data) {
		return ResponseParser.parseEventsResponse(data);
	}

}

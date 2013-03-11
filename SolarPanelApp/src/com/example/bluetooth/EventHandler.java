package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.EventsResponse;

import net.minidev.json.JSONObject;

public class EventHandler extends CommunicationHandler {
	
	public EventHandler(Callback callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.EVENTS);
		return json.toJSONString();
	}
	
	protected EventsResponse parseResponse(String data) {
		return ResponseParser.parseEventsResponse(data);
	}

}

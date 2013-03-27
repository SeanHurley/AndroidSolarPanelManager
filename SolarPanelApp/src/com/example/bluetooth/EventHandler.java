package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.EventsResponse;

public class EventHandler extends CommunicationHandler<EventsResponse> {

	public EventHandler(Callback<EventsResponse> callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.EVENTS);
		return json.toJSONString();
	}

	@Override
	protected EventsResponse parseResponse(String data) {
		return ResponseParser.parseEventsResponse(data);
	}

}

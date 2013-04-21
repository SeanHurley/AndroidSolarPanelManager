package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.parsers.ResponseParser;
import com.teamramrod.solarpanelmanager.api.responses.EventsResponse;

public class EventHandler extends CommunicationHandler<EventsResponse> {

	public EventHandler(Callback<EventsResponse> callback, String device, String pass) {
		super(callback, device, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.EVENTS);
		return json;
	}

	@Override
	protected EventsResponse parseResponse(String data) {
		return ResponseParser.parseEventsResponse(data);
	}

}

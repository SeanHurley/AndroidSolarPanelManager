package com.example.solarpanelmanager.api.responses;

import java.util.List;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class EventsResponse extends BaseResponse {
	
	private List<Event> events;
	
	public EventsResponse(int result, String message, List<Event> events) {
		super(MessageTypes.EVENTS_RESPONSE, result, message);
		this.events = events;
	}
	
	public EventsResponse(int result, String message) {
		super(MessageTypes.EVENTS_RESPONSE, result, message);
	}
	
	public List<Event> getEvents() {
		return events;
	}

}

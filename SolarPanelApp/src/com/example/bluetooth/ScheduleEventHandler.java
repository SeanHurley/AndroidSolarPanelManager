package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;

import net.minidev.json.JSONObject;

public class ScheduleEventHandler extends CommunicationHandler {
	
	private String id;
	private long firstRun;
	private long duration;
	private long interval;
	
	public ScheduleEventHandler(Callback callback, String id, long firstRun, long duration, long interval) {
		super(callback);
		this.firstRun = firstRun;
		this.duration = duration;
		this.interval = interval;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SCHEDULE_EVENT);
		json.put(MessageKeys.EVENT_ID, id);
		json.put(MessageKeys.EVENT_FIRST_TIME, firstRun);
		json.put(MessageKeys.EVENT_DURATION, duration);
		json.put(MessageKeys.EVENT_INTERVAL, interval);
		return json.toJSONString();
	}

}

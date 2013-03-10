package com.example.bluetooth;

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
		json.put("type", MessageTypes.SCHEDULE_EVENT);
		json.put("identifier", id);
		json.put("first-run-timestamp", firstRun);
		json.put("run_duration", duration);
		json.put("interval-duration", interval);
		return json.toJSONString();
	}

}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class ScheduleEventHandler extends CommunicationHandler {
	
	private String id;
	private long firstRun;
	private long duration;
	private long interval;
	
	public ScheduleEventHandler(Callback callback, String device,
			String id, long firstRun, long duration, long interval) {
		super(callback, device);
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

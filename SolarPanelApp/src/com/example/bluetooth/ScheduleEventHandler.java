package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class ScheduleEventHandler extends BaseResponseHandler {

	private String name;
	private long firstRun;
	private long duration;
	private long interval;

	public ScheduleEventHandler(Callback<BaseResponse> callback, String device, 
			String name, long firstRun, long duration, long interval) {
		super(callback, device);
		this.name = name;
		this.firstRun = firstRun;
		this.duration = duration;
		this.interval = interval;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SCHEDULE_EVENT);
		json.put(MessageKeys.EVENT_NAME, name);
		json.put(MessageKeys.EVENT_FIRST_TIME, firstRun);
		json.put(MessageKeys.EVENT_DURATION, duration);
		json.put(MessageKeys.EVENT_INTERVAL, interval);
		return json.toJSONString();
	}

}

package com.example.solarpanelmanager.api.responses;

public class Event {

	private String id;
	private long firstTime;
	private long duration;
	private long interval;

	public Event(String id, long firstTime, long duration, long interval) {
		this.id = id;
		this.firstTime = firstTime;
		this.duration = duration;
		this.interval = interval;
	}

	public long getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(long firstTime) {
		this.firstTime = firstTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public String getId() {
		return id;
	}

}
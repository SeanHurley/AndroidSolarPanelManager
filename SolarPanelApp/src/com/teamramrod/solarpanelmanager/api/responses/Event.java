package com.teamramrod.solarpanelmanager.api.responses;

import java.io.Serializable;

public class Event implements Serializable {

	private String id;
	private String name;
	private long firstTime;
	private long duration;
	private long interval;

	public Event(String id, String name, long firstTime, long duration, long interval) {
		this.id = id;
		this.name = name;
		this.firstTime = firstTime;
		this.duration = duration;
		this.interval = interval;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
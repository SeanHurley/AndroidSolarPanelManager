package com.teamramrod.solarpanelmanager.api.responses;

import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable {

	private static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
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
	
	/**
	 * Generates the key used to index events based on their start time
	 * @return
	 */
	public String getKey(){
		Calendar day = Calendar.getInstance();
		day.setTimeInMillis(firstTime);
		return String.format("%d:%02d", day.get(Calendar.HOUR), day.get(Calendar.MINUTE));
	}
	
	/**
	 * Tests whether this event conflicts with another event 
	 * @param e the other event to be tested
	 * @return
	 */
	public boolean isOverlapping(Event e){
		long start = firstTime % DAY_MILLIS;
		long end = (start + duration) % DAY_MILLIS;	
		long otherStart = e.getFirstTime() % DAY_MILLIS;
		long otherEnd = (otherStart + e.getDuration()) % DAY_MILLIS;
		return (start >= otherStart && start < otherEnd) || (end <= otherEnd && end > otherStart);
	}
	

	/**
	 * The string representation of an event is its name, start time, and duration in minutes
	 */
	public String toString(){
		long minutes = duration / (1000 * 60);
		return String.format("%s at %s for %d minutes", name, getKey(), minutes);
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
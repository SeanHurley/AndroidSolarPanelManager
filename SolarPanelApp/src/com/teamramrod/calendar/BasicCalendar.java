package com.teamramrod.calendar;


import java.util.Collection;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import com.teamramrod.solarpanelmanager.api.responses.Event;

public class BasicCalendar {
	private Map<String, Event> calendar = new TreeMap<String, Event>();
	private static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
	
	public BasicCalendar(Collection<Event> events) {
		for (Event e : events) {
			addEvent(e);				
		}
	}
	
	public boolean addEvent(Event event) {
		for (Event e : calendar.values()) {
			if (e.isOverlapping(event))
				return false;
		}
		calendar.put(event.getKey(), event);
		return true;
	}
	
	public void removeEvent(Event event) {
		calendar.remove(event.getKey());
	}
	
	public void removeEvent(String id) {
		for (Event e : calendar.values()) {
			if (e.getId() == id) {
				removeEvent(e);
				break;
			}
		}
	}
	
	public Map<String, Event> getCalendar() {
		return calendar;
	}
	
}

package com.teamramrod.calendar;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.teamramrod.solarpanelmanager.api.responses.Event;

public class BasicCalendar {
	private Set<Event> eventList = new TreeSet<Event>();
	
	public BasicCalendar(Collection<Event> events) {
		for (Event e : events) {
			addEvent(e);				
		}
	}
	
	public boolean addEvent(Event event) {
		for (Event e : eventList) {
			if (e.isOverlapping(event))
				return false;
		}
		eventList.add(event);
		return true;
	}
	
	public void removeEvent(Event event) {
		eventList.remove(event);
	}
	
	public void removeEvent(String id) {
		for (Event e : eventList) {
			if (e.getId() == id) {
				removeEvent(e);
				break;
			}
		}
	}
	
	public Set<Event> getEventList(){
		return eventList;
	}
	
}

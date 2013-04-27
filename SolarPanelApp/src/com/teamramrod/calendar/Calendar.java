package com.teamramrod.calendar;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.teamramrod.solarpanelmanager.api.responses.Event;

/**
 * Basic implementation of a calendar used as a model for event scheduling.
 * This calendar makes the simplifying assumption that repeating events will
 * repeat every day at the same time. 
 */
public class Calendar {
	private Set<Event> eventList = new TreeSet<Event>();
	
	/**
	 * Create the calendar.
	 * 
	 * @param events   initial events to add to the calendar
	 */
	public Calendar(Collection<Event> events) {
		for (Event e : events) {
			addEvent(e);				
		}
	}
	
	/**
	 * Add an event to the calendar.
	 * 
	 * @param event   the event to add
	 * @return        true if no overlap with an existing event was found,
	 *                false otherwise
	 */
	public boolean addEvent(Event event) {
		for (Event e : eventList) {
			if (e.isOverlapping(event))
				return false;
		}
		eventList.add(event);
		return true;
	}
	
	/**
	 * Remove an event from the calendar.
	 * 
	 * @param event   the event to remove
	 */
	public void removeEvent(Event event) {
		eventList.remove(event);
	}
	
	/**
	 * Remove an event from the calendar.
	 * 
	 * @param id    the id of the event to remove
	 */
	public void removeEvent(String id) {
		for (Event e : eventList) {
			if (e.getId() == id) {
				removeEvent(e);
				break;
			}
		}
	}
	
	/**
	 * Get all the events stored in the calendar.
	 * 
	 * @return   a collection of the events stored in the calendar
	 */
	public Collection<Event> getEventList(){
		return eventList;
	}
	
}

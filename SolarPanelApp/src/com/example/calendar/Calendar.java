package com.example.calendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.example.solarpanelmanager.api.responses.Event;

@SuppressWarnings("deprecation") 
public class Calendar {
	public static final long DAILY = (new Date(0, 0, 2)).getTime() - (new Date(0, 0, 1)).getTime();
	public static final long WEEKLY = DAILY * 7;
	private static final long ONCE = Long.MAX_VALUE;
	
	private Map<Date, List<Event>> calendar;
	private ArrayList<Event> rawEvents;
	
	public Calendar(Collection<Event> events){
		generateRawEvents(events);
	}
	
	public void addEvent(String id, String name, Date start, Date end, long interval){
		Event event = new Event(id, name, start.getTime(), end.getTime() - start.getTime(), interval);
		addEvent(event);
	}
	
	public void addEvent(Event event){
		ArrayList<Event> expandedEvents = expandEvents(rawEvents, System.currentTimeMillis() + 5 * WEEKLY);
		ArrayList<Event> newExpanded = new ArrayList<Event>();
		newExpanded.add(event);
		newExpanded = expandEvents(newExpanded, System.currentTimeMillis() + 5 * WEEKLY);
		boolean conflict = false;
		for(Event newEvent : newExpanded){
			long newStart = newEvent.getFirstTime();
			long newEnd = newEvent.getFirstTime() + newEvent.getDuration();
			for(Event oldEvent : expandedEvents){
				long oldStart = oldEvent.getFirstTime();
				long oldEnd = oldEvent.getFirstTime() + newEvent.getDuration();
				if((newStart >= oldStart && newStart < oldEnd) || (newEnd >= oldStart && newEnd < oldEnd))
					conflict = true;
			}
		}
		if(! conflict){
			rawEvents.add(event);
			generateRawEvents(rawEvents);
		}
	}
	
	public void removeEvent(String id){
		for(int i = 0; i < rawEvents.size(); i++){
			if(id.equals(rawEvents.get(i).getId())){
				rawEvents.remove(i);
				break;
			}
		}
	}
	
	public Map<Date, List<Event>> getCalendar(){
		generateCalendar();
		return calendar;
	}
	
	public ArrayList<Event> getRawEvents(){
		return rawEvents;
	}
	
	private void generateRawEvents(Collection<Event> events){
		// updates events so that start times are after the current time
		ArrayList<Event> updatedEvents = new ArrayList<Event>();
		for(Event event : events){
			if(event.getFirstTime() > System.currentTimeMillis()){
				updatedEvents.add(event);
			}
			else if(event.getInterval() != ONCE){
				// one time events that have already occurred are discarded
				while(event.getFirstTime() > System.currentTimeMillis()){
					event.setFirstTime(event.getFirstTime() + event.getInterval());
				}
				updatedEvents.add(event);
			}
		}
		rawEvents = updatedEvents;
	}
	
	private void generateCalendar(){
		calendar = new TreeMap<Date, List<Event>>();
		ArrayList<Event> expandedEvents = expandEvents(rawEvents, System.currentTimeMillis() + 5 * WEEKLY);
		for(Event event : expandedEvents){
			// calendar maps midnight of a day to all the events that occur on that day
			Date eventDay = new Date(event.getFirstTime());
			eventDay.setHours(0);
			eventDay.setMinutes(0);
			eventDay.setSeconds(0);
			if(calendar.get(eventDay) == null){
				ArrayList<Event> eventList = new ArrayList<Event>();
				eventList.add(event);
				calendar.put(eventDay, eventList);
			}
			else{
				calendar.get(eventDay).add(event);
			}
		}
	}
	
	private ArrayList<Event> expandEvents(ArrayList<Event> events, long timeLimit){
		// creates an event for each time an event would be activated within the time limit
		ArrayList<Event> expandedEvents = new ArrayList<Event>();
		for(Event event : events){
			Event nextEvent = new Event(event.getId(), event.getName(), event.getFirstTime(), event.getDuration(), event.getInterval());
			while(nextEvent.getFirstTime() < timeLimit){
				expandedEvents.add(nextEvent);
				nextEvent = new Event(event.getId(), event.getName(), nextEvent.getFirstTime() + event.getInterval(), event.getDuration(), event.getInterval());
			}
		}
		return expandedEvents;
	}	
}

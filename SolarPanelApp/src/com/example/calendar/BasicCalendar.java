package com.example.calendar;

import java.util.Date;
import java.util.Map;

import com.example.solarpanelmanager.api.responses.Event;

public class BasicCalendar {
	private Map<String, Event> calendar;
	
	@SuppressWarnings("deprecation")
	public void addEvent(Event event){
		long first = event.getFirstTime();
		Date newDay = new Date(first);
		long newStart = newDay.getHours() * 60 + newDay.getMinutes();
		String key = day.getHours() + ":" + day.getMinutes();
		boolean conflict = false;
		for(Event e : calendar.values()){
			Date oldDay = new Date(e.getFirstTime());
			long oldStart = oldDay.getHours() * 60 + oldDay.getMinutes();
			long oldEnd = oldStart + e.getDuration();
			
			
		}
	}
}

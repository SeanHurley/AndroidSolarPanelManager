package com.example.solarpanelmanager.tests.calendar;

import java.util.Date;
import java.util.HashSet;

import junit.framework.TestCase;

import com.example.calendar.BasicCalendar;
import com.example.solarpanelmanager.api.responses.Event;

public class BasicCalendarTest extends TestCase {
	private BasicCalendar cal;
	private static final long HOUR = 60 * 60 * 1000;
	
	@Override
	protected void setUp(){
		cal = new BasicCalendar(new HashSet<Event>());
	}
	
	public void testAdd(){
		Date d = new Date(0, 0, 1, 1, 0);
		Event e = new Event("1", "Name", d.getTime(), HOUR, 0);
		assertEquals(0, cal.getCalendar().size());
		assertTrue(cal.addEvent(e));
		assertFalse(cal.addEvent(e));
		assertEquals(1, cal.getCalendar().size());
	}
	
	public void testNoConflict(){
		Date d1 = new Date(0, 0, 1, 1, 0);
		Date d2 = new Date(0, 0, 1, 3, 0);
		Event e1 = new Event("1", "Name", d1.getTime(), HOUR * 2, 0);
		Event e2 = new Event("2", "Name", d2.getTime(), HOUR * 10, 0);
		assertTrue(cal.addEvent(e1));
		assertTrue(cal.addEvent(e2));
	}
	
	public void testLoopoverNoConflict(){
		Date d1 = new Date(0, 0, 1, 1, 0);
		Date d2 = new Date(0, 0, 1, 23, 30);
		Event e1 = new Event("1", "Name", d1.getTime(), HOUR * 22, 0);
		Event e2 = new Event("2", "Name", d2.getTime(), HOUR, 0);
		assertTrue(cal.addEvent(e1));
		assertTrue(cal.addEvent(e2));
	}
	
	public void testStartConflict(){
		Date d1 = new Date(0, 0, 1, 1, 0);
		Date d2 = new Date(0, 0, 1, 3, 0);
		Event e1 = new Event("1", "Name", d1.getTime(), HOUR * 4, 0);
		Event e2 = new Event("2", "Name", d2.getTime(), HOUR, 0);
		assertTrue(cal.addEvent(e1));
		assertFalse(cal.addEvent(e2));
	}
	
	public void testEndConflict(){
		Date d1 = new Date(0, 0, 1, 11, 0);
		Date d2 = new Date(0, 0, 1, 5, 0);
		Event e1 = new Event("1", "Name", d1.getTime(), HOUR, 0);
		Event e2 = new Event("2", "Name", d2.getTime(), HOUR * 7, 0);
		assertTrue(cal.addEvent(e1));
		assertFalse(cal.addEvent(e2));
	}
}

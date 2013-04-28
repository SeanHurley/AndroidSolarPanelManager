package com.teamramrod.solarpanelmanager.tests.calendar;

import java.util.GregorianCalendar;
import java.util.HashSet;

import junit.framework.TestCase;

import com.teamramrod.calendar.Calendar;
import com.teamramrod.solarpanelmanager.api.responses.Event;

public class BasicCalendarTest extends TestCase {
	private Calendar cal;
	private static final long HOUR = 60 * 60 * 1000;
	
	@Override
	protected void setUp(){
		cal = new Calendar(new HashSet<Event>());
	}
	
	public void testAddRemove(){
		GregorianCalendar d = new GregorianCalendar(2100, 0, 1, 1, 0);
		Event e = new Event("1", "Name", d.getTimeInMillis(), HOUR, 0); // 1:00 AM - 2:00 AM
		assertEquals(0, cal.getEventList().size());
		assertTrue(cal.addEvent(e));
		assertFalse(cal.addEvent(e));
		assertEquals(1, cal.getEventList().size());
		cal.removeEvent(e);
		assertEquals(0, cal.getEventList().size());
		cal.addEvent(e);
		assertEquals(1, cal.getEventList().size());
	}
	
	public void testNoConflict(){
		GregorianCalendar d1 = new GregorianCalendar(2100, 4, 4, 1, 0);
		GregorianCalendar d2 = new GregorianCalendar(2100, 4, 4, 3, 0);
		Event e1 = new Event("1", "Name", d1.getTimeInMillis(), HOUR * 2, 0); // 1:00 AM - 3:00 AM
		Event e2 = new Event("2", "Name", d2.getTimeInMillis(), HOUR * 10, 0); // 3:00 AM - 1:00 PM
		assertTrue(cal.addEvent(e1));
		assertTrue(cal.addEvent(e2));
	}
	
	public void testLoopoverNoConflict(){
		GregorianCalendar d1 = new GregorianCalendar(2000, 3, 5, 1, 0);
		GregorianCalendar d2 = new GregorianCalendar(2000, 3, 5, 23, 30);
		Event e1 = new Event("1", "Name", d1.getTimeInMillis(), HOUR * 22, 0); // 1:00 AM - 11:00 PM
		Event e2 = new Event("2", "Name", d2.getTimeInMillis(), HOUR, 0); // 11:00 PM - 12:00 AM
		assertTrue(cal.addEvent(e1));
		assertTrue(cal.addEvent(e2));
	}
	
	public void testStartConflict(){
		GregorianCalendar d1 = new GregorianCalendar(2100, 0, 1, 1, 0);
		GregorianCalendar d2 = new GregorianCalendar(2100, 0, 1, 3, 0);
		Event e1 = new Event("1", "Name", d1.getTimeInMillis(), HOUR * 4, 0); // 1:00 AM - 5:00 AM
		Event e2 = new Event("2", "Name", d2.getTimeInMillis(), HOUR, 0); // 3:00 AM - 4:00 AM
		assertTrue(cal.addEvent(e1));
		assertFalse(cal.addEvent(e2));
	}
	
	public void testEndConflict(){
		GregorianCalendar d1 = new GregorianCalendar(0, 0, 1, 11, 0);
		GregorianCalendar d2 = new GregorianCalendar(0, 0, 1, 5, 0);
		Event e1 = new Event("1", "Name", d1.getTimeInMillis(), HOUR, 0); // 11:00 AM - 12:00 PM
		Event e2 = new Event("2", "Name", d2.getTimeInMillis(), HOUR * 7, 0); // 5:00 AM - 12:00 PM
		assertTrue(cal.addEvent(e1));
		assertFalse(cal.addEvent(e2));
	}
}

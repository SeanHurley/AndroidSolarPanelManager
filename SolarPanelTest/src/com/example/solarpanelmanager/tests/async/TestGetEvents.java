package com.example.solarpanelmanager.tests.async;

import java.util.List;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.EventHandler;
import com.example.solarpanelmanager.api.responses.Event;
import com.example.solarpanelmanager.api.responses.EventsResponse;

public class TestGetEvents extends TestCase {
	private class Container {
		public EventsResponse response;
	}

	public void testGetEvents() {
		final Container container = new Container();
		final Semaphore sem = new Semaphore(1);
		EventHandler handler = new EventHandler(new Callback<EventsResponse>() {

			@Override
			public void onComplete(EventsResponse json) {
				container.response = json;
				sem.release();
			}

		}, "14:10:9F:E7:CA:93");
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handler.performAction();

		handler.waitOnTask(5000);

		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Event> events = container.response.getEvents();
		Event event1 = events.get(0);
		Event event2 = events.get(1);
		assertEquals(200, container.response.getResult());
		assertEquals(1000, event1.getFirstTime());
		assertEquals(2000, event1.getDuration());
		assertEquals(3000, event1.getInterval());
		assertEquals(4000, event2.getFirstTime());
		assertEquals(5000, event2.getDuration());
		assertEquals(6000, event2.getInterval());

	}
}

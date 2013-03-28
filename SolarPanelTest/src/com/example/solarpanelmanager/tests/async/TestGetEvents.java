package com.example.solarpanelmanager.tests.async;

import java.util.List;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.EventHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.Event;
import com.example.solarpanelmanager.api.responses.EventsResponse;

public class TestGetEvents extends TestCase {
	private class Container {
		public EventsResponse response;
	}

	public void testGetEvents() {
		final Container container = new Container();
		EventHandler handler = new EventHandler(new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
				container.response = (EventsResponse) json;
			}

		}, "14:10:9F:E7:CA:93");
		handler.performAction();

		handler.waitOnTask(5000);

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

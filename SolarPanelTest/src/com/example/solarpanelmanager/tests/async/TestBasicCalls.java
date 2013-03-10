package com.example.solarpanelmanager.tests.async;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.CommunicationHandler;
import com.example.bluetooth.LocationUpdateHandler;
import com.example.bluetooth.PINUpdateHandler;
import com.example.bluetooth.ScheduleEventHandler;
import com.example.bluetooth.SetChargeConstraintsHandler;
import com.example.bluetooth.TimeUpdateHandler;
import com.example.bluetooth.UnscheduleEventHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class TestBasicCalls extends TestCase {
	private class Container {
		public BaseResponse response;
	}

	private Container container;
	private Callback callback = new Callback() {

		@Override
		public void onComplete(BaseResponse json) {
			container.response = json;
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		container = new Container();
	}

	private void testHandler(CommunicationHandler handler) {
		handler.performAction();
		handler.waitOnTask(5000);
		assertEquals(200, container.response.getResult());
	}

	public void testLocationUpdate() {
		LocationUpdateHandler handler = new LocationUpdateHandler(callback, 90, 90);
		testHandler(handler);
	}

	public void testPinHandler() {
		PINUpdateHandler handler = new PINUpdateHandler(callback, "1234");
		testHandler(handler);
	}

	public void testScheduleEventHandler() {
		ScheduleEventHandler handler = new ScheduleEventHandler(callback, "a", 1000, 2000, 3000);
		testHandler(handler);
	}

	public void testSetChargeContraintsHandler() {
		SetChargeConstraintsHandler handler = new SetChargeConstraintsHandler(callback, 90, 10);
		testHandler(handler);
	}

	public void testTimeUpdateHandler() {
		TimeUpdateHandler handler = new TimeUpdateHandler(callback, 1000);
		testHandler(handler);
	}

	public void testUnscheduleEventHandler() {
		UnscheduleEventHandler handler = new UnscheduleEventHandler(callback, "a");
		testHandler(handler);
	}
}

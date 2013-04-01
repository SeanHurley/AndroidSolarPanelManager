package com.example.solarpanelmanager.tests.async;

import java.util.concurrent.Semaphore;

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
	private Semaphore sem = new Semaphore(1);
	private Callback<BaseResponse> callback = new Callback<BaseResponse>() {

		@Override
		public void onComplete(BaseResponse json) {
			container.response = json;
			sem.release();
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		container = new Container();
	}

	private void testHandler(CommunicationHandler<BaseResponse> handler) {
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
		assertEquals(200, container.response.getResult());
	}

	public void testLocationUpdate() {
		LocationUpdateHandler handler = new LocationUpdateHandler(callback, "14:10:9F:E7:CA:93", 90, 90);
		testHandler(handler);
	}

	public void testPinHandler() {
		PINUpdateHandler handler = new PINUpdateHandler(callback, "14:10:9F:E7:CA:93", "1234");
		testHandler(handler);
	}

	public void testScheduleEventHandler() {
		ScheduleEventHandler handler = new ScheduleEventHandler(callback, "14:10:9F:E7:CA:93", "a", 1000, 2000, 3000);
		testHandler(handler);
	}

	public void testSetChargeContraintsHandler() {
		SetChargeConstraintsHandler handler = new SetChargeConstraintsHandler(callback, "14:10:9F:E7:CA:93", 90, 10);
		testHandler(handler);
	}

	public void testTimeUpdateHandler() {
		TimeUpdateHandler handler = new TimeUpdateHandler(callback, "14:10:9F:E7:CA:93", 1000);
		testHandler(handler);
	}

	public void testUnscheduleEventHandler() {
		UnscheduleEventHandler handler = new UnscheduleEventHandler(callback, "14:10:9F:E7:CA:93", "a");
		testHandler(handler);
	}
}

package com.example.solarpanelmanager.tests.async;

import java.util.List;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.HistoryHandler;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class TestGetHistory extends TestCase {
	private class Container {
		public HistoryResponse response;
	}

	public void testGetHistory() {
		final Container container = new Container();
		final Semaphore sem = new Semaphore(1);
		HistoryHandler handler = new HistoryHandler(new Callback<HistoryResponse>() {

			@Override
			public void onComplete(HistoryResponse json) {
				System.out.println("in hook");
				System.out.println(json);
				container.response = json;
				sem.release();
			}

		}, "14:10:9F:E7:CA:93", null);
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
		List<SnapshotResponse> responses = container.response.getHistoryData();
		SnapshotResponse response1 = responses.get(0);
		SnapshotResponse response2 = responses.get(1);

		assertEquals(.3, response1.getBatteryVoltage());
		assertEquals(.4, response1.getBatteryCurrent());
		assertEquals(.1, response1.getPVVoltage());
		assertEquals(.2, response1.getPVCurrent());
		assertEquals(25, response1.getBatteryPercent());
		assertEquals(.8, response2.getBatteryVoltage());
		assertEquals(.9, response2.getBatteryCurrent());
		assertEquals(.6, response2.getPVVoltage());
		assertEquals(.7, response2.getPVCurrent());
		assertEquals(30, response2.getBatteryPercent());
	}
}

package com.example.solarpanelmanager.tests.async;

import java.util.List;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.HistoryHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class TestGetHistory extends TestCase {
	private class Container {
		public HistoryResponse response;
	}

	public void testGetHistory() {
		final Container container = new Container();
		HistoryHandler handler = new HistoryHandler(new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
				System.out.println("in hook");
				System.out.println(json);
				container.response = (HistoryResponse) json;
			}

		}, "14:10:9F:E7:CA:93");
		handler.performAction();

		handler.waitOnTask(5000);

		assertEquals(200, container.response.getResult());
		List<SnapshotResponse> responses = container.response.getHistoryData();
		SnapshotResponse response1 = responses.get(0);
		SnapshotResponse response2 = responses.get(1);

		assertTrue(response1.getBatteryVoltage() == .3);
		assertTrue(response1.getBatteryCurrent() == .4);
		assertTrue(response1.getPVVoltage() == .1);
		assertTrue(response1.getPVCurrent() == .2);
		assertTrue(response1.getBatteryPercent() == 25);
		assertTrue(response2.getBatteryVoltage() == .8);
		assertTrue(response2.getBatteryCurrent() == .9);
		assertTrue(response2.getPVVoltage() == .6);
		assertTrue(response2.getPVCurrent() == .7);
		assertTrue(response2.getBatteryPercent() == 30);
	}
}

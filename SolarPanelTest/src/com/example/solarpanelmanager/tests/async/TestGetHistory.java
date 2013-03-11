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
				container.response = (HistoryResponse) json;
			}

		});
		handler.performAction();

		handler.waitOnTask(5000);

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

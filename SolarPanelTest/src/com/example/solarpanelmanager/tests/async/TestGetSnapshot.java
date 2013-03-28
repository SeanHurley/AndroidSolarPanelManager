package com.example.solarpanelmanager.tests.async;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.SnapshotHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class TestGetSnapshot extends TestCase {
	private class Container {
		public SnapshotResponse response;
	}

	public void testGetSnapshot() {
		final Container container = new Container();
		SnapshotHandler handler = new SnapshotHandler(new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
				container.response = (SnapshotResponse) json;
			}

		}, "14:10:9F:E7:CA:93");
		handler.performAction();

		handler.waitOnTask(5000);

		assertEquals(200, container.response.getResult());
		assertEquals(.5, container.response.getBatteryVoltage());
		assertEquals(.5, container.response.getBatteryCurrent());
		assertEquals(.5, container.response.getPVVoltage());
		assertEquals(.5, container.response.getPVCurrent());
		assertEquals(50, container.response.getBatteryPercent());
	}
}

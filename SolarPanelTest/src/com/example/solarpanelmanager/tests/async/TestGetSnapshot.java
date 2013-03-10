package com.example.solarpanelmanager.tests.async;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.SnapshotHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class TestGetSnapshot extends TestCase {
	private class Container {
		public SnapshotResponse json;
	}

	public void testGetSnapshot() {
		final Container container = new Container();
		SnapshotHandler handler = new SnapshotHandler(new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
				container.json = (SnapshotResponse) json;
			}

		});
		handler.performAction();

		handler.waitOnTask(5000);

		assertEquals(200, container.json.getResult());
		assertTrue((container.json.getBatteryVoltage()) == .5);
		assertTrue((container.json.getPVVoltage()) == .5);
		assertTrue((container.json.getPVCurrent()) == .5);
	}
}

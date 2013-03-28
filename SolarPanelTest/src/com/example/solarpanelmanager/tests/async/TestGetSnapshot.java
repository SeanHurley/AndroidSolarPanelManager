package com.example.solarpanelmanager.tests.async;

import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.SnapshotHandler;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class TestGetSnapshot extends TestCase {
	private class Container {
		public SnapshotResponse response;
	}

	public void testGetSnapshot() {
		final Container container = new Container();
		final Semaphore sem = new Semaphore(1);
		SnapshotHandler handler = new SnapshotHandler(new Callback<SnapshotResponse>() {

			@Override
			public void onComplete(SnapshotResponse json) {
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
		assertEquals(200, container.response.getResult());
		assertEquals(.5, container.response.getBatteryVoltage());
		assertEquals(.5, container.response.getBatteryCurrent());
		assertEquals(.5, container.response.getPVVoltage());
		assertEquals(.5, container.response.getPVCurrent());
		assertEquals(50, container.response.getBatteryPercent());
	}
}

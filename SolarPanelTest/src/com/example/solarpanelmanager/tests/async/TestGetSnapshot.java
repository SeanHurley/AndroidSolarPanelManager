package com.example.solarpanelmanager.tests.async;

import junit.framework.TestCase;
import net.minidev.json.JSONObject;

import com.example.bluetooth.Callback;
import com.example.bluetooth.SnapshotHandler;

public class TestGetSnapshot extends TestCase {
	private class Container {
		public JSONObject json;
	}

	public void testGetSnapshot() {
		final Container container = new Container();
		SnapshotHandler handler = new SnapshotHandler(new Callback() {

			@Override
			public void onComplete(JSONObject json) {
				container.json = json;
			}
		});
		handler.performAction();

		handler.waitOnTask(5000);

		assertEquals(200, container.json.get("result"));
		assertTrue(((Double) container.json.get("battery-voltage")) == .5);
		assertTrue(((Double) container.json.get("pv-current")) == .5);
		assertTrue(((Double) container.json.get("pv-voltage")) == .5);
		assertTrue(((Double) container.json.get("battery-current")) == .5);
	}
}

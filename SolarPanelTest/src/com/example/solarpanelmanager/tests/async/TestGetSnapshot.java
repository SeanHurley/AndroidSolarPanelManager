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
		final Container c = new Container();
		SnapshotHandler handler = new SnapshotHandler(new Callback() {

			@Override
			public void onComplete(JSONObject json) {
				c.json = json;
			}
		});
		handler.performAction();

		handler.waitOnTask(5000);

		assertEquals(200, c.json.get("result"));
		assertTrue(c.json.get("timestamp") instanceof Long);
		assertTrue(c.json.get("batteryvoltage") instanceof Double);
		assertTrue(c.json.get("pv-current") instanceof Double);
		assertTrue(c.json.get("pv-voltage") instanceof Double);
		assertTrue(c.json.get("battery-current") instanceof Double);
	}
}

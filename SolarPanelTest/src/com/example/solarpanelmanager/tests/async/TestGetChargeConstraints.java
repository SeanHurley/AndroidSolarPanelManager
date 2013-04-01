package com.example.solarpanelmanager.tests.async;

import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.ViewChargeConstraintsHandler;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class TestGetChargeConstraints extends TestCase {
	private class Container {
		public ViewChargeConstraintsResponse response;
	}

	public void testGetConstraints() {
		final Container container = new Container();
		final Semaphore sem = new Semaphore(1);
		ViewChargeConstraintsHandler handler = new ViewChargeConstraintsHandler(
				new Callback<ViewChargeConstraintsResponse>() {

					@Override
					public void onComplete(ViewChargeConstraintsResponse json) {
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
		assertEquals(90, container.response.getMax());
		assertEquals(10, container.response.getMin());
	}
}

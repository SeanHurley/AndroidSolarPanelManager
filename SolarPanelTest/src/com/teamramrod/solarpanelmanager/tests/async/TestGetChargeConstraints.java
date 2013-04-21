package com.teamramrod.solarpanelmanager.tests.async;

import java.util.concurrent.Semaphore;

import junit.framework.TestCase;

import com.teamramrod.bluetooth.Callback;
import com.teamramrod.bluetooth.ViewChargeConstraintsHandler;
import com.teamramrod.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

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

				}, "14:10:9F:E7:CA:93", null);
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handler.performAction();
		handler.waitOnTask(5000);

		// We need to make sure that the callback has completed before finishing
		// the test and doing the asserts
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

package com.example.solarpanelmanager.tests.async;

import junit.framework.TestCase;

import com.example.bluetooth.Callback;
import com.example.bluetooth.ViewChargeConstraintsHandler;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class TestGetChargeConstraints extends TestCase {
	private class Container {
		public ViewChargeConstraintsResponse response;
	}

	public void testGetConstraints() {
		final Container container = new Container();
		ViewChargeConstraintsHandler handler = new ViewChargeConstraintsHandler(new Callback() {

			@Override
			public void onComplete(BaseResponse json) {
				container.response = (ViewChargeConstraintsResponse) json;
			}

		});
		handler.performAction();

		handler.waitOnTask(5000);

		assertEquals(200, container.response.getResult());
		assertEquals(90, container.response.getMax());
		assertEquals(10, container.response.getMin());
	}
}

package com.example.bluetooth;


public class HandshakeHandler extends CommunicationHandler {

	public HandshakeHandler(Callback callback, String target) {
		super(callback, target);
	}

	@Override
	protected String getRequest() {
 		return "{\"message\": \"hello!\"}";
	}

}

package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

/**
 * Performs the initial handshake with the controller to verify that it is a
 * controller.
 * 
 * @author Michael Candido
 */
public class HandshakeHandler extends BaseResponseHandler {

	public HandshakeHandler(Callback<BaseResponse> callback, String target, String pass) {
		super(callback, target, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.HANDSHAKE);
		return json;
	}

}

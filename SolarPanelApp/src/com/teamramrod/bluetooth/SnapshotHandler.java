package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.parsers.ResponseParser;
import com.teamramrod.solarpanelmanager.api.responses.SnapshotResponse;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the history.
 */
public class SnapshotHandler extends CommunicationHandler<SnapshotResponse> {

	public SnapshotHandler(Callback<SnapshotResponse> callback, String device, String pass) {
		super(callback, device, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SNAPSHOT);
		return json;
	}

	@Override
	protected SnapshotResponse parseResponse(String data) {
		return ResponseParser.parseSnapshotResponse(data);
	}

}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the history.
 */
public class SnapshotHandler extends CommunicationHandler<SnapshotResponse> {

	public SnapshotHandler(Callback<SnapshotResponse> callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.SNAPSHOT);
		return json.toJSONString();
	}

	@Override
	protected SnapshotResponse parseResponse(String data) {
		return ResponseParser.parseSnapshotResponse(data);
	}

}

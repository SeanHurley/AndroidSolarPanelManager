package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the history.
 */
public class SnapshotHandler extends CommunicationHandler {

	public SnapshotHandler(Callback callback, String device) {
		super(callback, device);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", "snapshot");
		return json.toJSONString();
	}
	
	@Override
	protected BaseResponse parseResponse(String data) {
		return ResponseParser.parseSnapshotResponse(data);
	}
	
}

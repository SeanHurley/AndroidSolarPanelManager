package com.example.bluetooth;

import net.minidev.json.JSONObject;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the history.
 */
public class SnapshotHandler extends CommunicationHandler {

	public SnapshotHandler(Callback callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", "snapshot");
		return json.toJSONString();
	}
}

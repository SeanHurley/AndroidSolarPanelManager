package com.example.bluetooth;

import net.minidev.json.JSONObject;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the history.
 */
public class HistoryHandler extends CommunicationHandler {

	public HistoryHandler(Callback callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", "history");
		return json.toJSONString();
	}

}

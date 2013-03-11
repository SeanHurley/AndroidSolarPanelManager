package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;

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
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.HISTORY);
		return json.toJSONString();
	}

	@Override
	protected BaseResponse parseResponse(String data) {
		return ResponseParser.parseHistoryResponse(data);
	}

}

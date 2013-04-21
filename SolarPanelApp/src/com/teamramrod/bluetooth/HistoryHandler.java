package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.parsers.ResponseParser;
import com.teamramrod.solarpanelmanager.api.responses.HistoryResponse;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the history.
 */
public class HistoryHandler extends CommunicationHandler<HistoryResponse> {

	public HistoryHandler(Callback<HistoryResponse> callback, String device, String pass) {
		super(callback, device, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.HISTORY);
		return json;
	}

	@Override
	protected HistoryResponse parseResponse(String data) {
		return ResponseParser.parseHistoryResponse(data);
	}

}

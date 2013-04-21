package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.parsers.ResponseParser;
import com.teamramrod.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

/**
 * @author seanhurley
 * 
 *         This is the handler which will manage the communication for getting
 *         the current charge constraints.
 */
public class ViewChargeConstraintsHandler extends CommunicationHandler<ViewChargeConstraintsResponse> {

	public ViewChargeConstraintsHandler(Callback<ViewChargeConstraintsResponse> callback, String device, String pass) {
		super(callback, device, pass);
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.VIEW_CHARGE_CONSTRAINTS);
		return json;
	}

	@Override
	protected ViewChargeConstraintsResponse parseResponse(String data) {
		return ResponseParser.parseViewChargeConstraintsResponse(data);
	}

}

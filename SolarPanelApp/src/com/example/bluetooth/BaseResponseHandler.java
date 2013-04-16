package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;

/**
 * @author seanhurley
 * 
 *         Simply overrides the Base parseResponse so that that functionality
 *         can be shared throughout several different handlers
 */
public abstract class BaseResponseHandler extends CommunicationHandler<BaseResponse> {

	public BaseResponseHandler(Callback<BaseResponse> callback, String target, String pass) {
		super(callback, target, pass);
	}

	@Override
	protected BaseResponse parseResponse(String data) {
		return ResponseParser.parseBasicResponse(data);
	}
}

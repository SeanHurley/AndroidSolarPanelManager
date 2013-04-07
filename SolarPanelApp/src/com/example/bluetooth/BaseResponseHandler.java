package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public abstract class BaseResponseHandler extends CommunicationHandler<BaseResponse> {

	public BaseResponseHandler(Callback<BaseResponse> callback, String target, String pass) {
		super(callback, target, pass);
	}

	@Override
	protected BaseResponse parseResponse(String data) {
		return ResponseParser.parseBasicResponse(data);
	}
}

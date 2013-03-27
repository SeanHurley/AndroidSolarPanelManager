package com.example.bluetooth;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public abstract class BaseResponseHandler extends CommunicationHandler<BaseResponse> {

	public BaseResponseHandler(Callback<BaseResponse> callback) {
		super(callback);
	}

	@Override
	protected BaseResponse parseResponse(String data) {
		return ResponseParser.parseBasicResponse(data);
	}
}

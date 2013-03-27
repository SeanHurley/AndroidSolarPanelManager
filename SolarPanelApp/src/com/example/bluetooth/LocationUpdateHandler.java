package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageKeys;
import com.example.solarpanelmanager.api.parsers.MessageTypes;
import com.example.solarpanelmanager.api.responses.BaseResponse;

public class LocationUpdateHandler extends BaseResponseHandler {

	private float latitude;
	private float longitude;

	public LocationUpdateHandler(Callback<BaseResponse> callback, float longitude, float latitude) {
		super(callback);
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.LOCATION_UPDATE);
		json.put(MessageKeys.LOCATION_LONGITUDE, this.longitude);
		json.put(MessageKeys.LOCATION_LATITUDE, this.latitude);
		return json.toJSONString();
	}

}

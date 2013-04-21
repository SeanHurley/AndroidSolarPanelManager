package com.teamramrod.bluetooth;

import net.minidev.json.JSONObject;

import com.teamramrod.solarpanelmanager.api.parsers.MessageKeys;
import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;
import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;

public class LocationUpdateHandler extends BaseResponseHandler {

	private float latitude;
	private float longitude;

	public LocationUpdateHandler(Callback<BaseResponse> callback, String device, String pass, float longitude,
			float latitude) {
		super(callback, device, pass);
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	protected JSONObject getRequest() {
		JSONObject json = new JSONObject();
		json.put(MessageKeys.MESSAGE_TYPE, MessageTypes.LOCATION_UPDATE);
		json.put(MessageKeys.LOCATION_LONGITUDE, this.longitude);
		json.put(MessageKeys.LOCATION_LATITUDE, this.latitude);
		return json;
	}

}

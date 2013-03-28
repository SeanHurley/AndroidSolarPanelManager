package com.example.bluetooth;

import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class LocationUpdateHandler extends CommunicationHandler {
	
	private float latitude;
	private float longitude;
	
	public LocationUpdateHandler(Callback callback, String device, float longitude, float latitude) {
		super(callback, device);
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", MessageTypes.LOCATION_UPDATE);
		json.put("longitude", this.longitude);
		json.put("latitude", this.latitude);
		return json.toJSONString();
	}

}

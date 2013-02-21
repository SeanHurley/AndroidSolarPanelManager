package com.example.bluetooth;

import net.minidev.json.JSONObject;

public class HistoryCaller extends BaseBluetoothCaller {

	public HistoryCaller(Callback callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", "history");
		return json.toJSONString();
	}

}

package com.example.bluetooth;

import net.minidev.json.JSONObject;

public class SnapshotCaller extends BaseBluetoothCaller {

	public SnapshotCaller(Callback callback) {
		super(callback);
	}

	@Override
	protected String getRequest() {
		JSONObject json = new JSONObject();
		json.put("type", "snapshot");
		return json.toJSONString();
	}
}

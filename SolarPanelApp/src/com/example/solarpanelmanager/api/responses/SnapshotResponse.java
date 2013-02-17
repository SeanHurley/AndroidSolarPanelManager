package com.example.solarpanelmanager.api.responses;

public class SnapshotResponse extends BaseResponse {

	public SnapshotResponse(int result) {
		super(result);
		// Setup other values
	}

	public long getTimestamp() {
		return 0L;
	}

	public float getBatteryVoltage() {
		return 0.0f;
	}

	public float getPVCurrent() {
		return 0.0f;
	}

	public float getPVVoltage() {
		return 0.0f;
	}
}

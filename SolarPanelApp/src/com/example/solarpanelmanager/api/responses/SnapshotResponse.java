package com.example.solarpanelmanager.api.responses;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class SnapshotResponse extends BaseResponse {
	private long timestamp;
	private double batteryVoltage;
	private double PVCurrent;
	private double PVVoltage;

	public SnapshotResponse(int result, long timestamp, double batteryVoltage, double PVCurrent, double PVVoltage) {
		super(MessageTypes.SNAPSHOT_RESPONSE, result);
		this.timestamp = timestamp;
		this.batteryVoltage = batteryVoltage;
		this.PVCurrent = PVCurrent;
		this.PVVoltage = PVVoltage;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getBatteryVoltage() {
		return batteryVoltage;
	}

	public double getPVCurrent() {
		return PVCurrent;
	}

	public double getPVVoltage() {
		return PVVoltage;
	}
}

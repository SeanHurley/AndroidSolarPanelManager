package com.example.solarpanelmanager.api.responses;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class SnapshotResponse extends BaseResponse {
	@Override
	public String toString() {
		return "SnapshotResponse [timestamp=" + timestamp + ", batteryVoltage=" + batteryVoltage + ", PVCurrent="
				+ PVCurrent + ", PVVoltage=" + PVVoltage + ", batteryCurrent=" + batteryCurrent + ", batteryPercent="
				+ batteryPercent + "]";
	}

	private long timestamp;
	private double batteryVoltage;
	private double PVCurrent;
	private double PVVoltage;
	private double batteryCurrent;
	private int batteryPercent;

	public SnapshotResponse(int result, String message, long timestamp, int batteryPercent, double batteryVoltage, double PVCurrent,
			double PVVoltage, double batteryCurrent) {
		super(MessageTypes.SNAPSHOT_RESPONSE, result, message);
		this.timestamp = timestamp;
		this.batteryPercent = batteryPercent;
		this.batteryVoltage = batteryVoltage;
		this.PVCurrent = PVCurrent;
		this.PVVoltage = PVVoltage;
		this.batteryCurrent = batteryCurrent;
	}
	
	public SnapshotResponse(int result, String message) {
		super(MessageTypes.SNAPSHOT_RESPONSE, result, message);
	}

	public double getBatteryCurrent() {
		return batteryCurrent;
	}

	public int getBatteryPercent() {
		return batteryPercent;
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

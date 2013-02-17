package com.example.solarpanelmanager.tests.api;

import junit.framework.TestCase;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class ApiParserTest extends TestCase {
	private static int RESULT_OK = 200;

	public void testResultOK() {
		BaseResponse response = ResponseParser.parseBasicResponse(getBasicResponse());

		assertEquals(RESULT_OK, response.getResult());
	}

	public void testHistoryParser() {
		HistoryResponse response = ResponseParser.parseHistoryResponse(getHistoryResponse());

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}

	public void testSnapshotParser() {
		SnapshotResponse response = ResponseParser.parseSnapshotResponse(getSnapshotResponse());

		assertEquals("snapshot", response.getType());
		assertEquals(RESULT_OK, response.getResult());
		assertEquals(1361136155, response.getTimestamp());
		assertEquals(1.0f, response.getBatteryVoltage());
		assertEquals(2.0f, response.getPVVoltage());
		assertEquals(3.0f, response.getPVCurrent());
	}

	private String getSnapshotResponse() {
		JSONObject json = getSnapshotJSON(1361136155, 1.0f, 2.0f, 3.0f);
		return json.toJSONString();
	}

	private JSONObject getSnapshotJSON(long timestamp, float batteryVoltage, float pvCurrent, float pvVoltage) {
		JSONObject json = new JSONObject();
		json.put("type", "snapshot");
		json.put("result", RESULT_OK);
		json.put("timestamp", timestamp);
		json.put("battery-voltage", batteryVoltage);
		json.put("pv-current", pvCurrent);
		json.put("pv-voltage", pvVoltage);
		return json;
	}

	private String getBasicResponse() {
		JSONObject json = new JSONObject();
		json.put("result", RESULT_OK);
		return json.toJSONString();
	}

	private String getHistoryResponse() {
		JSONObject json = new JSONObject();
		JSONArray snapshots = new JSONArray();
		snapshots.add(getSnapshotJSON(1361136155, 1.0f, 2.0f, 3.0f));
		snapshots.add(getSnapshotJSON(1361136158, 1.1f, 1.9f, 2.9f));
		json.put("type", "history");
		json.put("result", RESULT_OK);
		json.put("history-data", snapshots);

		return json.toJSONString();
	}
}

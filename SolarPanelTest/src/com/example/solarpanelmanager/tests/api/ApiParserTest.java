package com.example.solarpanelmanager.tests.api;

import java.util.List;

import junit.framework.TestCase;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class ApiParserTest extends TestCase {
	private static int RESULT_OK = 200;

	public void testResultOK() throws ParseException {
		BaseResponse response = ResponseParser.parseBasicResponse(getBasicResponse());

		assertEquals(RESULT_OK, response.getResult());
	}

	public void testHistoryParser() throws ParseException {
		HistoryResponse response = ResponseParser.parseHistoryResponse(getHistoryResponse());
		List<SnapshotResponse> history = response.getHistoryData();
		SnapshotResponse firstSnapshot = history.get(0);
		SnapshotResponse secondSnapshot = history.get(1);

		assertEquals("snapshot", firstSnapshot.getType());
		assertEquals(RESULT_OK, firstSnapshot.getResult());
		assertEquals(1361136155, firstSnapshot.getTimestamp());
		assertEquals(1.0d, firstSnapshot.getBatteryVoltage());
		assertEquals(2.0d, firstSnapshot.getPVCurrent());
		assertEquals(3.0d, firstSnapshot.getPVVoltage());

		assertEquals("snapshot", secondSnapshot.getType());
		assertEquals(RESULT_OK, secondSnapshot.getResult());
		assertEquals(1361136158, secondSnapshot.getTimestamp());
		assertEquals(1.1d, secondSnapshot.getBatteryVoltage());
		assertEquals(1.9d, secondSnapshot.getPVCurrent());
		assertEquals(2.9d, secondSnapshot.getPVVoltage());

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}

	public void testSnapshotParser() throws ParseException {
		SnapshotResponse response = ResponseParser.parseSnapshotResponse(getSnapshotResponse());

		assertEquals("snapshot", response.getType());
		assertEquals(RESULT_OK, response.getResult());
		assertEquals(1361136155, response.getTimestamp());
		assertEquals(1.0d, response.getBatteryVoltage());
		assertEquals(2.0d, response.getPVCurrent());
		assertEquals(3.0d, response.getPVVoltage());
	}

	private String getSnapshotResponse() {
		JSONObject json = getSnapshotJSON(1361136155L, 1.0d, 2.0d, 3.0d);
		return json.toJSONString();
	}

	private JSONObject getSnapshotJSON(long timestamp, double batteryVoltage, double pvCurrent, double pvVoltage) {
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
		snapshots.add(getSnapshotJSON(1361136155, 1.0d, 2.0d, 3.0d));
		snapshots.add(getSnapshotJSON(1361136158, 1.1d, 1.9d, 2.9d));
		json.put("type", "history");
		json.put("result", RESULT_OK);
		json.put("history-data", snapshots);
		return json.toJSONString();
	}
}

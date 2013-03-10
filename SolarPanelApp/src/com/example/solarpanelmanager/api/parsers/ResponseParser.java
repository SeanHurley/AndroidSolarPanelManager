package com.example.solarpanelmanager.api.parsers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.Event;
import com.example.solarpanelmanager.api.responses.EventsResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;
import com.example.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

public class ResponseParser {

	/**
	 * @param response
	 *            The response that will be given from the bluetooth
	 *            communication layer
	 * @return A mapping of just plain KEY-VALUE information in a HashMap. The
	 *         object types may still need to be parsed as this will just be
	 *         pulling raw from the responses
	 */
	private static HashMap<String, Object> parseResponse(String response) {
		HashMap<String, Object> values = new HashMap<String, Object>();

		return values;
	}

	public static BaseResponse parseBasicResponse(String response) {
		// Call the parse response for the basic map, and then create the proper
		// response object
		JSONObject json = (JSONObject) JSONValue.parse(response);
		return new BaseResponse((String) json.get(MessageKeys.MESSAGE_TYPE), (Integer) json.get(MessageKeys.RESPONSE_CODE));
	}

	public static HistoryResponse parseHistoryResponse(String response) {
		// Call the parse response for the basic map, and then create the proper
		// response object
		JSONObject json = (JSONObject) JSONValue.parse(response);
		JSONArray snapshots = (JSONArray) json.get(MessageKeys.HISTORY_DATA);
		ArrayList<SnapshotResponse> snapshotResponses = new ArrayList<SnapshotResponse>();
		// SnapshotResponse[] snapshotResponses = new
		// SnapshotResponse[snapshots.size()];

		for (int i = 0; i < snapshots.size(); i++) {
			// snapshotResponses[i] = parseSnapshotResponse(((JSONObject)
			// snapshots.get(i)).toJSONString());
			snapshotResponses.add(parseSnapshotResponse(((JSONObject) snapshots.get(i)).toJSONString()));
		}

		int result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		return new HistoryResponse(result, snapshotResponses);
	}

	public static SnapshotResponse parseSnapshotResponse(String response) {
		// Call the parse response for the basic map, and then create the proper
		// response object
		System.out.println(response);
		JSONObject json = (JSONObject) (JSONValue.parse(response));
		int result = 200;
		if (json.containsKey(MessageKeys.RESPONSE_CODE)) {
			result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		}

		Object timestamp = json.get(MessageKeys.SNAPSHOT_TIMESTAMP);
		long longTimestamp;
		if (timestamp instanceof Long) {
			longTimestamp = (Long) timestamp;
		} else {
			longTimestamp = (Integer) timestamp;
		}


		double batteryVoltage = getDouble(json.get(MessageKeys.SNAPSHOT_BATTERY_VOLTAGE));
		double batteryCurrent = getDouble(json.get(MessageKeys.SNAPSHOT_BATTERY_CURRENT));
		double PVVoltage = getDouble(json.get(MessageKeys.SNAPSHOT_PANEL_VOLTAGE));
		double PVCurrent = getDouble(json.get(MessageKeys.SNAPSHOT_PANEL_CURRENT));
		
		int percent = (Integer) json.get(MessageKeys.SNAPSHOT_BATTERY_PERCENT);

		return new SnapshotResponse(result, longTimestamp, percent, batteryVoltage, PVCurrent, PVVoltage,
				batteryCurrent);
	}

	private static double getDouble(Object obj) {
		if (obj instanceof BigDecimal) {
			BigDecimal dec = (BigDecimal) obj;
			return dec.doubleValue();
		} else {
			return (Double) obj;
		}
	}

	public static EventsResponse parseEventsResponse(String response) {
		JSONObject json = (JSONObject) (JSONValue.parse(response));
		int result = (Integer) json.get(MessageKeys.RESPONSE_CODE);

		JSONArray eventsArray = (JSONArray) json.get(MessageKeys.EVENTS_DATA);
		ArrayList<Event> events = new ArrayList<Event>();

		for (int i = 0; i < eventsArray.size(); i++) {
			events.add(parseEvent((JSONObject) eventsArray.get(i)));
		}

		return new EventsResponse(result, events);
	}

	private static Event parseEvent(JSONObject json) {
		String id = (String) json.get(MessageKeys.EVENT_ID);
		long firstTime = (Long) json.get(MessageKeys.EVENT_FIRST_TIME);
		long duration = (Long) json.get(MessageKeys.EVENT_DURATION);
		long interval = (Long) json.get(MessageKeys.EVENT_INTERVAL);
		return new Event(id, firstTime, duration, interval);
	}

	public static ViewChargeConstraintsResponse parseViewChargeConstraintsResponse(String response) {
		JSONObject json = (JSONObject) (JSONValue.parse(response));
		int result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		int max = (Integer) json.get(MessageKeys.CHARGE_MAX);
		int min = (Integer) json.get(MessageKeys.CHARGE_MIN);
		return new ViewChargeConstraintsResponse(result, max, min);
	}

}

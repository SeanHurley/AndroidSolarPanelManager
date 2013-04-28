package com.teamramrod.solarpanelmanager.api.parsers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.teamramrod.solarpanelmanager.api.responses.BaseResponse;
import com.teamramrod.solarpanelmanager.api.responses.Event;
import com.teamramrod.solarpanelmanager.api.responses.EventsResponse;
import com.teamramrod.solarpanelmanager.api.responses.HistoryResponse;
import com.teamramrod.solarpanelmanager.api.responses.SnapshotResponse;
import com.teamramrod.solarpanelmanager.api.responses.ViewChargeConstraintsResponse;

/**
 * Used to parse JSON strings into various types of response objects
 * 
 * @author Aaron
 */
public class ResponseParser {
	
	/**
	 * Parse a response consisting of just a message type, response code, and message
	 * @param response
	 * @return
	 */
	public static BaseResponse parseBasicResponse(String response) {
		if (response == "") {
			return new BaseResponse(null, 500, null);
		}

		JSONObject json = (JSONObject) JSONValue.parse(response);
		return new BaseResponse((String) json.get(MessageKeys.MESSAGE_TYPE),
				(Integer) json.get(MessageKeys.RESPONSE_CODE), (String) json.get(MessageKeys.RESPONSE_MESSAGE));
	}

	/**
	 * Parse a response including a list of Snapshots
	 * @param response
	 * @return
	 */
	public static HistoryResponse parseHistoryResponse(String response) {
		JSONObject json = (JSONObject) JSONValue.parse(response);

		int result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		String message = (String) json.get(MessageKeys.RESPONSE_MESSAGE);

		if (result != 200) {
			return new HistoryResponse(result, message);
		}

		JSONArray snapshots = (JSONArray) json.get(MessageKeys.HISTORY_DATA);
		ArrayList<SnapshotResponse> snapshotResponses = new ArrayList<SnapshotResponse>();

		for (int i = 0; i < snapshots.size(); i++) {
			snapshotResponses.add(parseSnapshotResponse(((JSONObject) snapshots.get(i)).toJSONString()));
		}

		return new HistoryResponse(result, message, snapshotResponses);
	}

	/**
	 * Parse a response containing data on the state of the panel and battery
	 * @param response
	 * @return
	 */
	public static SnapshotResponse parseSnapshotResponse(String response) {
		if (response == "") {
			return new SnapshotResponse(500, null);
		}

		JSONObject json = (JSONObject) (JSONValue.parse(response));
		int result = 200;
		if (json.containsKey(MessageKeys.RESPONSE_CODE)) {
			result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		}
		String message = (String) json.get(MessageKeys.RESPONSE_MESSAGE);

		if (result != 200) {
			return new SnapshotResponse(result, message);
		}

		if (json.containsKey(MessageKeys.RESPONSE_CODE)) {
			result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		}

		long longTimestamp = getLong(json.get(MessageKeys.SNAPSHOT_TIMESTAMP));

		double batteryVoltage = getDouble(json.get(MessageKeys.SNAPSHOT_BATTERY_VOLTAGE));
		double batteryCurrent = getDouble(json.get(MessageKeys.SNAPSHOT_BATTERY_CURRENT));
		double PVVoltage = getDouble(json.get(MessageKeys.SNAPSHOT_PANEL_VOLTAGE));
		double PVCurrent = getDouble(json.get(MessageKeys.SNAPSHOT_PANEL_CURRENT));
		double intake = getDouble(json.get(MessageKeys.SNAPSHOT_INTAKE));
		double outtake = getDouble(json.get(MessageKeys.SNAPSHOT_OUTTAKE));

		int percent = (Integer) json.get(MessageKeys.SNAPSHOT_BATTERY_PERCENT);
		int min = (Integer) json.get(MessageKeys.CHARGE_MIN);
		int max = (Integer) json.get(MessageKeys.CHARGE_MAX);

		return new SnapshotResponse(result, message, longTimestamp, percent, min, max, batteryVoltage, PVCurrent,
				PVVoltage, batteryCurrent, intake, outtake);
	}

	/**
	 * Converts sufficiently small BigDecimals into doubles
	 * Needed because doubles can get converted to BigDecimal when added to a JSON object
	 * @param obj
	 * @return
	 */
	private static double getDouble(Object obj) {
		if (obj instanceof BigDecimal) {
			BigDecimal dec = (BigDecimal) obj;
			return dec.doubleValue();
		} else {
			return (Double) obj;
		}
	}

	/**
	 * Parse a response containing the list of Events stored on the control box
	 * @param response
	 * @return
	 */
	public static EventsResponse parseEventsResponse(String response) {
		if (response == "") {
			return new EventsResponse(500, null);
		}

		JSONObject json = (JSONObject) (JSONValue.parse(response));

		int result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		String message = (String) json.get(MessageKeys.RESPONSE_MESSAGE);

		if (result != 200) {
			return new EventsResponse(result, message);
		}

		JSONArray eventsArray = (JSONArray) json.get(MessageKeys.EVENTS_DATA);
		List<Event> events = new ArrayList<Event>();

		for (int i = 0; i < eventsArray.size(); i++) {
			events.add(parseEvent((JSONObject) eventsArray.get(i)));
		}

		return new EventsResponse(result, message, events);
	}

	/**
	 * Parse an individual event from a JSONObject
	 * @param json
	 * @return
	 */
	private static Event parseEvent(JSONObject json) {
		String id = (String) json.get(MessageKeys.EVENT_ID);
		String name = (String) json.get(MessageKeys.EVENT_NAME);
		long firstTime = getLong(json.get(MessageKeys.EVENT_FIRST_TIME));
		long duration = getLong(json.get(MessageKeys.EVENT_DURATION));
		long interval = getLong(json.get(MessageKeys.EVENT_INTERVAL));
		return new Event(id, name, firstTime, duration, interval);
	}

	/**
	 * Converts integers to longs
	 * Needed because sufficiently small longs are converted to integers when added to a JSON object
	 * @param obj
	 * @return
	 */
	private static long getLong(Object obj) {
		if (obj instanceof Integer) {
			return (Integer) obj;
		} else {
			return (Long) obj;
		}
	}

	/**
	 * Parses a response containing the min and max charge levels for the battery
	 * @param response
	 * @return
	 */
	public static ViewChargeConstraintsResponse parseViewChargeConstraintsResponse(String response) {
		if (response == "") {
			return new ViewChargeConstraintsResponse(500, null, -1, -1);
		}

		JSONObject json = (JSONObject) (JSONValue.parse(response));

		int result = (Integer) json.get(MessageKeys.RESPONSE_CODE);
		String message = (String) json.get(MessageKeys.RESPONSE_MESSAGE);

		if (result != 200) {
			return new ViewChargeConstraintsResponse(result, message);
		}

		int max = (Integer) json.get(MessageKeys.CHARGE_MAX);
		int min = (Integer) json.get(MessageKeys.CHARGE_MIN);
		return new ViewChargeConstraintsResponse(result, message, max, min);
	}

}

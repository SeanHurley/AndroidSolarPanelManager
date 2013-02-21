package com.example.solarpanelmanager.api.parsers;

import java.util.HashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

public class ResponseParser {
	private static JSONParser jparse = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

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

	public static BaseResponse parseBasicResponse(String response) throws ParseException {
		// Call the parse response for the basic map, and then create the proper
		// response object
		JSONObject json = (JSONObject) jparse.parse(response);
		return new BaseResponse((Integer) json.get("result"));
	}

	public static HistoryResponse parseHistoryResponse(String response) throws ParseException {
		// Call the parse response for the basic map, and then create the proper
		// response object
		JSONObject json = (JSONObject) jparse.parse(response);
		JSONArray snapshots = (JSONArray) json.get("history-data");
		SnapshotResponse[] snapshotResponses = new SnapshotResponse[snapshots.size()];
		
		for(int i = 0; i < snapshots.size(); i++)
			snapshotResponses[i] = parseSnapshotResponse(((JSONObject) snapshots.get(i)).toJSONString());
		
		int result = (Integer) json.get("result");
		return new HistoryResponse(result, snapshotResponses);
	}

	public static SnapshotResponse parseSnapshotResponse(String response) throws ParseException {
		// Call the parse response for the basic map, and then create the proper
		// response object
		JSONObject json = (JSONObject)(jparse.parse(response));
		int result = (Integer) json.get("result");
		
		Object timestamp = json.get("timestamp");
		long longTimestamp;
		if(timestamp instanceof Long)
			longTimestamp = (Long) timestamp;
		else // needed since JSON converts longs to integers when possible
			longTimestamp = (Integer) timestamp;
		
		double batteryVoltage = (Double) json.get("battery-voltage");
		double PVCurrent = (Double) json.get("pv-current");
		double PVVoltage = (Double) json.get("pv-voltage");
		
		return new SnapshotResponse(result, longTimestamp, batteryVoltage, PVCurrent, PVVoltage);
	}

}

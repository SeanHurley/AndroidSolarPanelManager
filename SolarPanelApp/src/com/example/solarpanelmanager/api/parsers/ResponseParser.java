package com.example.solarpanelmanager.api.parsers;

import java.util.HashMap;

import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;

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
		return null;
	}

	public static HistoryResponse parseHistoryResponse(String response) {
		// Call the parse response for the basic map, and then create the proper
		// response object
		return null;
	}

	public static SnapshotResponse parseSnapshotResponse(String response) {
		// Call the parse response for the basic map, and then create the proper
		// response object
		return null;
	}

}

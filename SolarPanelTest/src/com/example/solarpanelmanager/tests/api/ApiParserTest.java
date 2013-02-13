package com.example.solarpanelmanager.tests.api;

import java.util.HashMap;

import junit.framework.TestCase;

import com.example.solarpanelmanager.api.parsers.APIKeys;
import com.example.solarpanelmanager.api.parsers.BaseParser;

public class ApiParserTest extends TestCase {

	private static String DATE_TIME = "";
	private static String STREAM_PACKET = "";
	private static String HISTORY_DATA = "";
	private static String SNAPSHOT_DATA = "";
	private static int RESULT_OK = 200;

	public void testResultOK() {
		HashMap<String, Object> results = BaseParser.parseResponse(DATE_TIME);

		assertEquals(RESULT_OK, results.get(APIKeys.RESULT));
	}

	public void testStreamParser() {
		HashMap<String, Object> results = BaseParser.parseResponse(STREAM_PACKET);

		assertEquals(RESULT_OK, results.get(APIKeys.RESULT));
		// Test for other info here
	}

	public void testHistoryParser() {
		HashMap<String, Object> results = BaseParser.parseResponse(HISTORY_DATA);

		assertEquals(RESULT_OK, results.get(APIKeys.RESULT));
		// Test for other info here
	}

	public void testSnapshotParser() {
		HashMap<String, Object> results = BaseParser.parseResponse(SNAPSHOT_DATA);

		assertEquals(RESULT_OK, results.get(APIKeys.RESULT));
		// Test for other info here
	}
}

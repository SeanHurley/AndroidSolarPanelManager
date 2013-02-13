package com.example.solarpanelmanager.tests.api;

import junit.framework.TestCase;

import com.example.solarpanelmanager.api.parsers.BaseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;
import com.example.solarpanelmanager.api.responses.StreamResponse;

public class ApiParserTest extends TestCase {

	private static String DATE_TIME = "";
	private static String STREAM_PACKET = "";
	private static String HISTORY_DATA = "";
	private static String SNAPSHOT_DATA = "";
	private static int RESULT_OK = 200;

	public void testResultOK() {
		BaseResponse response = BaseParser.parseBasicResponse(DATE_TIME);

		assertEquals(RESULT_OK, response.getResult());
	}

	public void testStreamParser() {
		StreamResponse response = BaseParser.parseStreamResponse(STREAM_PACKET);

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}

	public void testHistoryParser() {
		HistoryResponse response = BaseParser.parseHistoryResponse(HISTORY_DATA);

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}

	public void testSnapshotParser() {
		SnapshotResponse response = BaseParser.parseSnapshotResponse(SNAPSHOT_DATA);

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}
}

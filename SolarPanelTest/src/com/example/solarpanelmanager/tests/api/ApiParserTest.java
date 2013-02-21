package com.example.solarpanelmanager.tests.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.example.solarpanelmanager.api.parsers.ResponseParser;
import com.example.solarpanelmanager.api.responses.BaseResponse;
import com.example.solarpanelmanager.api.responses.HistoryResponse;
import com.example.solarpanelmanager.api.responses.SnapshotResponse;
import com.example.solarpanelmanager.api.responses.StreamResponse;

public class ApiParserTest extends TestCase {

	private static String DATE_TIME = "";
	private static String STREAM_PACKET = "";
	private static String HISTORY_DATA = "history.json";
	private static String SNAPSHOT_DATA = "snapshot.json";
	private static final int RESULT_OK = 200;
	
	public void testResultOK() {
		BaseResponse response = ResponseParser.parseBasicResponse(DATE_TIME);

		assertEquals(RESULT_OK, response.getResult());
	}

	public void testStreamParser() {
		StreamResponse response = ResponseParser.parseStreamResponse(STREAM_PACKET);

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}

	public void testHistoryParser() throws IOException {
		String data = loadFile(HISTORY_DATA);
		HistoryResponse response = ResponseParser.parseHistoryResponse(data);

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}

	public void testSnapshotParser() throws IOException {
		String data = loadFile(SNAPSHOT_DATA);
		SnapshotResponse response = ResponseParser.parseSnapshotResponse(data);

		assertEquals(RESULT_OK, response.getResult());
		// Test for other info here
	}
	
	private static String loadFile(String name) throws IOException {
		InputStream in = ApiParserTest.class.getClassLoader().getResourceAsStream(name);
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer);
		return writer.toString();
	}
}

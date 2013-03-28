package com.example.solarpanelmanager.api.responses;

import java.util.List;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class HistoryResponse extends BaseResponse {

	@Override
	public String toString() {
		return "HistoryResponse [historyData=" + historyData + "]";
	}

	private List<SnapshotResponse> historyData;

	public HistoryResponse(int result, String message, List<SnapshotResponse> historyData) {
		super(MessageTypes.HISTORY_RESPONSE, result, message);
		this.historyData = historyData;
	}
	
	public HistoryResponse(int result, String message) {
		super(MessageTypes.HISTORY_RESPONSE, result, message);
	}

	public List<SnapshotResponse> getHistoryData() {
		return historyData;
	}
}

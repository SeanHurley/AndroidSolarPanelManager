package com.example.solarpanelmanager.api.responses;

import java.util.List;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class HistoryResponse extends BaseResponse {

	@Override
	public String toString() {
		return "HistoryResponse [historyData=" + historyData + "]";
	}

	private List<SnapshotResponse> historyData;

	public HistoryResponse(int result, List<SnapshotResponse> historyData) {
		super(MessageTypes.HISTORY_RESPONSE, result);
		this.historyData = historyData;
	}

	public List<SnapshotResponse> getHistoryData() {
		return historyData;
	}
}

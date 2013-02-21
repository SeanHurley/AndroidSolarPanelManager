package com.example.solarpanelmanager.api.responses;


public class HistoryResponse extends BaseResponse {
	private SnapshotResponse[] historyData;
	public HistoryResponse(int result, SnapshotResponse[] historyData) {
		super(result);
		this.historyData = historyData;
		setType("history-data");
	}
	
	public SnapshotResponse[] getHistoryData(){
		return historyData;
	}
}

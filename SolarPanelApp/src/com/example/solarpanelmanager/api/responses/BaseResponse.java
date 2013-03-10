package com.example.solarpanelmanager.api.responses;

public class BaseResponse {
	private int result;
	private String type;

	public BaseResponse(String type, int result) {
		// This will set the result, but that's all the base response has
		this.type = type;
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public int getResult() {
		return result;
	}

	public String getErrorMessage() {
		return "";
	}
}

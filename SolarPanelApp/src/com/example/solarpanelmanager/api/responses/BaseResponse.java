package com.example.solarpanelmanager.api.responses;

public class BaseResponse {
	private int result;

	public BaseResponse(int result) {
		// This will set the result, but that's all the base response has
		this.result = result;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return "";
	}
}

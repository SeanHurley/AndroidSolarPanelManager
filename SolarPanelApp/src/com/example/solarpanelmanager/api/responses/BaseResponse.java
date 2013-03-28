package com.example.solarpanelmanager.api.responses;

public class BaseResponse {
	@Override
	public String toString() {
		return "BaseResponse [result=" + result + ", type=" + type + "]";
	}

	private String type;
	private int result;
	private String message;

	public BaseResponse(String type, int result, String message) {
		// This will set the result, but that's all the base response has
		this.type = type;
		this.result = result;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public int getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}
}

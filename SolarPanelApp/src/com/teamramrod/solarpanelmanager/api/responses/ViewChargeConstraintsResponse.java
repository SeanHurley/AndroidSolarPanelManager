package com.teamramrod.solarpanelmanager.api.responses;

import com.teamramrod.solarpanelmanager.api.parsers.MessageTypes;

public class ViewChargeConstraintsResponse extends BaseResponse {
	
	private int max;
	private int min;
	
	public ViewChargeConstraintsResponse(int result, String message, int max, int min) {
		super(MessageTypes.VIEW_CHARGE_CONSTRAINTS_RESPONSE, result, message);
		this.max = max;
		this.min = min;
	}
	
	public ViewChargeConstraintsResponse(int result, String message) {
		super(MessageTypes.VIEW_CHARGE_CONSTRAINTS_RESPONSE, result, message);
	}
	
	public int getMax() {
		return this.max;
	}
	
	public int getMin() {
		return this.min;
	}

}

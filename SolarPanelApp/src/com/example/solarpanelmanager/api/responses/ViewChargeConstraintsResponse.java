package com.example.solarpanelmanager.api.responses;

import com.example.solarpanelmanager.api.parsers.MessageTypes;

public class ViewChargeConstraintsResponse extends BaseResponse {
	
	private int max;
	private int min;
	
	public ViewChargeConstraintsResponse(int result, int max, int min) {
		super(MessageTypes.VIEW_CHARGE_CONSTRAINTS_RESPONSE, result);
		this.max = max;
		this.min = min;
	}
	
	public int getMax() {
		return this.max;
	}
	
	public int getMin() {
		return this.min;
	}

}

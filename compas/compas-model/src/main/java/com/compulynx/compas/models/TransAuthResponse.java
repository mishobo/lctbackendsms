package com.compulynx.compas.models;

public class TransAuthResponse {
	public int respCode;
	public String respMessage;
	public int txnId;
	public double trans_amount;
	public double auth_amount;
	public String memberNo;
	public String reason;
	
	public TransAuthResponse() {
		super();
	}
	
	public TransAuthResponse(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	
}

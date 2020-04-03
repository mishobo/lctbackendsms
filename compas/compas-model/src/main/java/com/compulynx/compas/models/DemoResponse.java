package com.compulynx.compas.models;

public class DemoResponse {
	public DemoResponse() {
		super();
	}
	public int respCode;
	public String respMessage;
	public Demo user;
	
	
	public DemoResponse(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}
	
	public DemoResponse(int respCode, String respMessage, Demo user) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
		this.user = user;
	}
}

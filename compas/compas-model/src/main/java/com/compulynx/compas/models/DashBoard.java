package com.compulynx.compas.models;

public class DashBoard {
	
	public int posUsers;
	public int systemUsers;
	public int bnfCount;
	public int deviceCount;

	public int detailCount;
	public String detailDescription;
	public int load;
	public int purchase;
	public int totalTransCount;
	public String monthName;
	public int respCode;
	public String amount;
	public double totalTxns;
	public String service;
	public int transCount;
	
	public DashBoard(int respCode) {
		super();
		this.respCode = respCode;
	}
	
	public DashBoard() {
		super();
	}
	public DashBoard(String service, int transCount) {
		super();
		this.service = service;
		this.transCount = transCount;
	}
	public DashBoard(int detailCount, String detailDescription, int respCode) {
		super();
		this.detailCount = detailCount;
		this.detailDescription = detailDescription;
		this.respCode = respCode;
	}

	public DashBoard(int load, int purchase,
			int totalTransCount, String monthName, int respCode) {
		super();
		this.load = load;
		this.purchase = purchase;
		this.totalTransCount = totalTransCount;
		this.monthName = monthName;
		this.respCode = respCode;
	}
}

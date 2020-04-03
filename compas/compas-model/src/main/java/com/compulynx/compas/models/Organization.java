package com.compulynx.compas.models;

import java.util.List;

public class Organization {
	public int orgId;
	public String orgCode;
	public String orgName;
	public String orgContactPer;
	public String orgContactNo;
	public String orgEmail;
	public boolean active;
	public int createdBy;
	public int respCode;
	public List<Wallet> wallets;

	public Organization(int respCode) {
		super();
		this.respCode = respCode;
	}

	public Organization(int orgId, String orgCode,String orgName,String orgContactPer,String orgContactNo,String orgEmail, int respCode) {
		super();
		this.orgId = orgId;
		this.orgCode=orgCode;
		this.orgName = orgName;
		this.orgContactPer=orgContactPer;
		this.orgContactNo=orgContactNo;
		this.orgEmail=orgEmail;
		this.respCode = respCode;
	}

	public Organization() {
		super();
	}

}

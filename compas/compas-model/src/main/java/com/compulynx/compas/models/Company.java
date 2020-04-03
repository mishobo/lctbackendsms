package com.compulynx.compas.models;

public class Company {

	public int companyId;
	public String companyCode;
	public String companyName;
	public String companyLogo;
	public int createdBy;
	public int respCode;

	public Company() {
		super();
	}

	public Company(int companyId,String companyCode,  String companyName,
			 int createdBy, int respCode) {
		super();
		this.companyId = companyId;
		this.companyCode=companyCode;
		this.companyName = companyName;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}

	public Company(int respCode) {
		super();
		this.respCode = respCode;
	}
}

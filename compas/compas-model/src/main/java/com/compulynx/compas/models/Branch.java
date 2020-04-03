package com.compulynx.compas.models;


public class Branch {
	public Branch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int branchId;
	public String branchCode;
	public String branchName;
	public int merchantId;
	public String merchantName;
	public int classId;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isChecked;
	public int locationId;
	public String locationName;
	public int count;
	
	public Branch(int branchId, String branchName, boolean isChecked) {
		super();
		this.branchId = branchId;
		this.branchName = branchName;
		this.isChecked = isChecked;
	}

	public Branch(int branchId, String branchCode, String branchName,
			int merchantId,int classId, boolean active, int createdBy,
			int respCode,int locationId,String locationName,String merchantName,int count) {
		super();
		this.branchId = branchId;
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.merchantId = merchantId;
		this.classId = classId;
		this.active=active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.locationId=locationId;
		this.locationName=locationName;
		this.merchantName=merchantName;
		this.count=count;
	}
	
	public Branch(int respCode) {
		super();
		this.respCode = respCode;
	}

}

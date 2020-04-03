/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author shree
 *
 */
public class BeneficiaryGroup {

	public int bnfGrpId;
	public String bnfGrpCode;
	public String bnfGrpName;
	public int productId;
	public String productName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public boolean isActive;
	public double houseHoldValue;
	public int noOfBnfs;
	public double topupValue;
	public String topupValueDisplay;
	public String lastTopup;
	public String topupStatus;
	public String fromDate;
	public String toDate;
	public int minCap;
	public int maxCap;
	public int familySize;
	public String agentId;
	public int orgId;
	  public List retailerSelected;
	public BeneficiaryGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeneficiaryGroup(int bnfGrpId, String bnfGrpCode, String bnfGrpName,
			int productId, String productName, boolean active,int count,
			boolean isActive,double houseHoldValue,int minCap,int maxCap,int noOfBnfs) {
		super();
		this.bnfGrpId = bnfGrpId;
		this.bnfGrpCode = bnfGrpCode;
		this.bnfGrpName = bnfGrpName;
		this.productId = productId;
		this.productName = productName;
		this.active = active;
		this.count=count;
		this.isActive=isActive;
		this.houseHoldValue=houseHoldValue;
		this.minCap=minCap;
		this.maxCap=maxCap;
		this.noOfBnfs=noOfBnfs;
	}
}

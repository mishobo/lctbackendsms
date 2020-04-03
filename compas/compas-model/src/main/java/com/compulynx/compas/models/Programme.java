/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Programme {

	public Programme(int programmeId, String programmeCode,
			String programmeDesc, boolean active, int createdBy, int respCode) {
		super();
		this.programmeId = programmeId;
		this.programmeCode = programmeCode;
		this.programmeDesc = programmeDesc;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}
	public int programmeId;
	public String programmeCode;
	public String programmeDesc;
	public String programmeName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public List<Service> services;
	public double programmeValue;
	public String currency;
	public int productId;
	public int schemeId;
	public String productName;
	public List<Voucher> vouchers;
	public List<Voucher> membervouchers;
	public List<ItmModes> itmList;
	public List<ChtmModes> chtmList;
	public List<IntmModes> intmList;
	public  String startDate;
	public  String endDate;
	public  String itmModes;
	public  String chtmModes;
	public  String intmModes;
	public int count;
	public String programmeType;
	public int orgId;
	public int coverTypeId;
	public List<OtmModes> otmList;

	public Programme(int programmeId, String programmeDesc, boolean isActive,
			int createdBy, int respCode,double programmeValue,String currency) {
		super();
		this.programmeId = programmeId;
		this.programmeDesc = programmeDesc;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.programmeValue=programmeValue;
		this.currency=currency;
		
		
	}
	
	public Programme() {
		super();
	}
	public Programme(int respCode) {
		super();
		this.respCode=respCode;
	}
	public Programme(int programmeId,
			String programmeDesc, double programmeValue,boolean isActive, int createdBy, 
			int respCode,String itmModes,String chtmModes,String intmModes) {
		super();
		this.programmeId = programmeId;
		this.programmeDesc = programmeDesc;
		this.programmeValue = programmeValue;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.itmModes=itmModes;
		this.chtmModes=chtmModes;
		this.intmModes=intmModes;
	}
	
}

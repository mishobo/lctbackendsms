/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Merchant {

	public int merchantId;
	public String merchantName;
	public String merchantCode;
	public int orgId;
	public String merCnctPer;
	public String merCnctNo;
	public String merEmail;
	public String merAdd;
	public String currCode;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean self;
	public int count;
	public String accNumber;
	public String accName;
	public String bankCode;
	public String bankName;
	public String logo;
	public boolean hasHmsi;
	public String stageServer;
	public int userId;
	public String LoggedInUser;
	
	public Merchant(int merchantId,String merchantCode, String merchantName,String merCnctPer,String merCnctNo, 
			String merEmail,String merAdd,String currCode,boolean active,
			int respCode,int count,String accNumber,String accName,String bankCode,String bankName) {
		super();
		this.merchantId = merchantId;
		this.merchantCode=merchantCode;
		this.merchantName = merchantName;
		this.merCnctPer=merCnctPer;
		this.merCnctNo=merCnctNo;
		this.merEmail=merEmail;
		this.merAdd=merAdd;
		this.currCode=currCode;
		this.active = active;
		this.respCode = respCode;
		this.count=count;
		this.accNumber=accNumber;
		this.accName=accName;
		this.bankCode=bankCode;
		this.bankName=bankName;
	}
	public Merchant(int respCode) {
		super();
		this.respCode = respCode;
	}
	public Merchant() {
		super();
		// TODO Auto-generated constructor stub
	}
}

 
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class MemberCard {
	 
	public int id;
	public String cardNumber;
	public String status;
	public String memberId;
	public int createdBy;
	public int respCode;
	public int accType;
	public List<Wallet> wallets;
	public String serialNumber;
	public String binRange;
	public int organizationId;
	public String respMessage;
	public String issueDate;
	public String expiryDate;
	public String programmeDesc;
	public String firstName;
	public String surName;
	public double loadAmount;
	public double balance;
	public int customerId;
	public String pinNumber;
	public int programmeId;
	public double programmeValue;
	public String loadCardString;
	public List<Programme> programmes;
	public List<BioImage> bioImages;
	public String oldCardNo;
	public String oldSerialNo;
	public String oldPinNo;
	public String issueType;

	public MemberCard(int id,String cardNumber, String memberId,int respCode,String respMessage,int createdBy,int accType) {
		super();
		this.id=id;
		this.cardNumber = cardNumber;
		this.memberId = memberId;
		this.respCode=respCode;
		this.respMessage=respMessage;
		this.createdBy=createdBy;
		this.accType=accType;
	}	
	
	public MemberCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemberCard(int respCode,String respMessage) {
		super();
		this.respCode=respCode;
		this.respMessage=respMessage;
	}
	
	
	

}

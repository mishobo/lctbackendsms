/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Transaction {

	public int txnId;
	public int txnType;
	public Long id;
	public String cardNo;
	public String accNo;
	public String memberNo;
	public String providerName;
	public String bnfDesc;
	public String transDate;
	public double amount;
	public double allowed;
	public String fromDt;
	public String toDt;
	public int count;
	public boolean isActive;
	public List<Remitance> details;
	public List<Transaction> selectedTrans;
	public List<TransactionDtl> transactionDetails;
	public int txnStatus;
	public int createdBy;
	public int paidStatus;
	public int providerId;
	public int orgId;
	public String providerCode;
	public String bankCode;
	public String bankName;
	public String transferDate;
	public String reason;
	public String txnIds;
	public String empName;
	public boolean reject;
	public boolean isDisabled;
	public String image;
	public int empId;
	public double diff;
	public int voucherId;
	public int programmeId;
	public int serviceId;
	public String schemeName;
	public String schemeCode;
	public String insuranceName;
	public String insuranceCode;
    public String chargeDate;
    public String receiptNo;
    public String patientNo;
    public String patientName;
    public String membershipNo; 
    public String itemCode;
    public String itemName;
    public String transactionType;
    public String remarks;
    public double itemQuantity;
    public double patientAmount;
    public double patientDiscount;
    public double patientNet;
    public double sponsorAmount;
    public double sponsorDiscount;
    public double sponsorNet;
    public String BillNo;
    public String basketId;
    public String voucherName;
    public String serviceName;
    public List<TransactionDtl> claimtransactionDetails;
	public List<ClaimRemitance> claimdetails;
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Transaction(int txnId, String accNo, String memberNo,
			String providerName, String bnfDesc, String transDate, double amount) {
		super();
		this.txnId = txnId;
		this.accNo = accNo;
		this.memberNo = memberNo;
		this.providerName = providerName;
		this.bnfDesc = bnfDesc;
		this.transDate = transDate;
		this.amount = amount;
	}
	
}

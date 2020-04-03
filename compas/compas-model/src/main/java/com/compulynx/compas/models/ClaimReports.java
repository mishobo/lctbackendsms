package com.compulynx.compas.models;

public class ClaimReports {
	//claims field
    public Long id;
    public String insuranceCode;
    public String insuranceName;
    public String schemeCode;
    public String schemeName;
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
    public String txnStatus;
    public String providerId;
    public String paidStatus;
    public String BillNo;
    public String fromDt;
	public String toDt;
	public int count;
	public double allowed;
	public boolean isActive;
	public boolean reject;
	public String reason;
	public int voucherId;
	public int programmeId;
	public int serviceId;
	public double diff;
	public int createdBy;
	public double amount;
	public String providerCode;
	public String providerName;
	public String bankCode;
	public String bankName;
	public String txnIds;
	public String transferDate;
	public int basketId;
	public String voucherName;
	public Double ClaimserviceAmount;
	public String fromDate;
	public String toDate;
	public int orgId;
	public String merchantName;
	public String serviceName;
	public String cardNo;
	
	public ClaimReports() {
		super();
		// TODO Auto-generated constructor stub
	}
}

/**
 * 
 */
package com.compulynx.compas.models;

import java.util.Date;
import java.util.List;

/**
 * @author Kimaiga
 *
 */
public class TransactionMst {

	public String memberNo;
	public String cardNumber;
	public String accNo;
	public String billNo;
	public String transStatus;
	public int schemeId;
	public String deviceId;
	public double totalRunningBalance;
	public double totalAmount;
	public String transDate;
	public int userId;
	public String user;
	public int createdBy;
	public int respCode;
	public String invoiceNo;
	public List<TransactionDtl> transDtl;
	public int programmeId;
	public double txnAmount;
	public String serName;
	public String merName;
	public int transMstId;
	public int transDetailId;
	public List<Service> services;
	public List<TransactionDtl> serviceDtl;
	public String image;
	
	
	
	// for AKUH Claims upload
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
    public int txnStatus;
    public String providerId;
    public int paidStatus;
    public String orgId;
    public String BillNo;
    
	
}

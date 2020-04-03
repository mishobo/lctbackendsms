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
public class SmartTransactionMst {

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
	public String providerId;
}

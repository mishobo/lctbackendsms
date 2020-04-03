/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Reports {
	
	public int orgId;
	public String memberNo;
	public String fromDate;
	public String toDate;
	public String userName;
	public String totalAmount;
	public String billNo;
	public String txnDate;
	public String serviceName;
	public String subServiceName;
	public int deviceId;
	public String status;
	public String cardNo;
	public String accNo;
	public String name;
	public Double serviceAmount;
	public String productName;
	public String merchantName;
	public int productId;
	public int merchantId;
	public int userId;
	public String orgName;
	public Double memberBalance;
	public Double benefitBalance;
	public String invoiceNo;
	public Double basketValue;
	public Double basketBalance;
	public Double utilization;
	//audit trail fields
	public String createdBy;
	public String Description;
	public String created_on;
	public String ModuleAccessed;
	public String username;
	public String LoggedInUser;
	public int count;
	public String dateofBirth;
	public String memberType;
	
	
	public Reports() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return "Reports [memberNo=" + memberNo + ", serviceName=" + serviceName + ", name=" + name + ", basketValue="
				+ basketValue + ", basketBalance=" + basketBalance + ", utilization=" + utilization +"]";
	}
	
	
}

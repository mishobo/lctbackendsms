package com.compulynx.compas.models;

public class OffLCT {
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
	    public int txnStatus;
	    public String providerId;
	    public int paidStatus;
	    public String orgId;
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
		public String accNo;
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
		public String serviceName;
		public String deviceId;
		public String transStatus;
		public int userId;
		public double totalRunningBalance;
		@Override
		public String toString() {
			return "OffLCT [id=" + id + ", insuranceCode=" + insuranceCode + ", insuranceName=" + insuranceName
					+ ", schemeCode=" + schemeCode + ", schemeName=" + schemeName + ", chargeDate=" + chargeDate
					+ ", receiptNo=" + receiptNo + ", patientNo=" + patientNo + ", patientName=" + patientName
					+ ", membershipNo=" + membershipNo + ", itemCode=" + itemCode + ", itemName=" + itemName
					+ ", transactionType=" + transactionType + ", remarks=" + remarks + ", itemQuantity=" + itemQuantity
					+ ", patientAmount=" + patientAmount + ", patientDiscount=" + patientDiscount + ", patientNet="
					+ patientNet + ", sponsorAmount=" + sponsorAmount + ", sponsorDiscount=" + sponsorDiscount
					+ ", sponsorNet=" + sponsorNet + ", txnStatus=" + txnStatus + ", providerId=" + providerId
					+ ", paidStatus=" + paidStatus + ", orgId=" + orgId + ", BillNo=" + BillNo + ", fromDt=" + fromDt
					+ ", toDt=" + toDt + ", count=" + count + ", allowed=" + allowed + ", isActive=" + isActive
					+ ", reject=" + reject + ", reason=" + reason + ", voucherId=" + voucherId + ", programmeId="
					+ programmeId + ", serviceId=" + serviceId + ", diff=" + diff + ", accNo=" + accNo + ", createdBy="
					+ createdBy + ", amount=" + amount + ", providerCode=" + providerCode + ", providerName="
					+ providerName + ", bankCode=" + bankCode + ", bankName=" + bankName + ", txnIds=" + txnIds
					+ ", transferDate=" + transferDate + ", basketId=" + basketId + ", voucherName=" + voucherName
					+ ", serviceName=" + serviceName + ", deviceId=" + deviceId + ", transStatus=" + transStatus
					+ ", userId=" + userId + ", totalRunningBalance=" + totalRunningBalance + "]";
		}

		
}

/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Product {

	public Product(int productId, String productCode, String productName,
			String startDate, String endDate, double fund) {
		super();
		this.productId = productId;
		this.productCode = productCode;
		this.productName = productName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fund = fund;
	}

	public int productId;
	public String productCode;
	public String productName;
	public String startDate;
	public String endDate;
	public double fund;
	public int orgId;
	public String merchantName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public String type;
	public String typeName;
	public String insuranceCode;
	public String status;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(int productId, String productCode, String productName,
			String startDate,String endDate,double fund, boolean active,int count, String status) {
		super();
		this.productId = productId;
		this.productCode = productCode;
		this.productName = productName;
		this.startDate=startDate;
		this.endDate=endDate;
		this.fund=fund;
		this.active = active;
		this.count=count;
		this.status=status;
	}
	public Product(int productId, String productCode, String productName,
			String startDate,String endDate,double fund, boolean active,int count, String insuranceCode, String status) {
		super();
		this.productId = productId;
		this.productCode = productCode;
		this.productName = productName;
		this.startDate=startDate;
		this.endDate=endDate;
		this.fund=fund;
		this.active = active;
		this.count=count;
		this.insuranceCode = insuranceCode;
		this.status=status;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productCode=" + productCode + ", productName=" + productName
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", fund=" + fund + ", orgId=" + orgId
				+ ", merchantName=" + merchantName + ", active=" + active + ", createdBy=" + createdBy + ", respCode="
				+ respCode + ", count=" + count + ", type=" + type + ", typeName=" + typeName + ", insuranceCode="
				+ insuranceCode + ", status=" + status + "]";
	}	
}

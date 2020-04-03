package com.compulynx.compas.models;

import java.math.BigDecimal;

public class ReportsDTO {
	public int id;
	public int orgId;
	public String member_no;
	public String card_no;
	public String trans_date;
	public String acc_no;
	public String invoice_number;
	public String bill_no;
	public String trans_status;
	public BigDecimal service_amount;
	public String name;
	public BigDecimal service_balance;
	public BigDecimal basket_balance;
	public BigDecimal basket_amount;
	public String ser_name;
	public String device_id;
	public String product_name;
	public String mer_name;
	public String programmedesc;
	public int productid;
	public int merchantid;

	public ReportsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return "ReportsDTO [id=" + id + ", orgId=" + orgId + ", member_no=" + member_no + ", card_no=" + card_no
				+ ", trans_date=" + trans_date + ", acc_no=" + acc_no + ", invoice_number=" + invoice_number
				+ ", bill_no=" + bill_no + ", trans_status=" + trans_status + ", service_amount=" + service_amount
				+ ", name=" + name + ", service_balance=" + service_balance + ", basket_balance=" + basket_balance
				+ ", basket_amount=" + basket_amount + ", ser_name=" + ser_name + ", device_id=" + device_id
				+ ", product_name=" + product_name + ", mer_name=" + mer_name + ", programmedesc=" + programmedesc
				+ ", productid=" + productid + ", merchantid=" + merchantid + "]";
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getMember_no() {
		return member_no;
	}

	public void setMember_no(String member_no) {
		this.member_no = member_no;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(String trans_date) {
		this.trans_date = trans_date;
	}

	public String getAcc_no() {
		return acc_no;
	}

	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getTrans_status() {
		return trans_status;
	}

	public void setTrans_status(String trans_status) {
		this.trans_status = trans_status;
	}

	public BigDecimal getService_amount() {
		return service_amount;
	}

	public void setService_amount(BigDecimal  service_amount) {
		this.service_amount = service_amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getService_balance() {
		return service_balance;
	}

	public void setService_balance(BigDecimal service_balance) {
		this.service_balance = service_balance;
	}

	public BigDecimal getBasket_balance() {
		return basket_balance;
	}

	public void setBasket_balance(BigDecimal  basket_balance) {
		this.basket_balance = basket_balance;
	}
	
	
	public BigDecimal  getBasket_amount() {
		return basket_amount;
	}


	public void setBasket_amount(BigDecimal  basket_amount) {
		this.basket_amount = basket_amount;
	}


	public String getSer_name() {
		return ser_name;
	}

	public void setSer_name(String ser_name) {
		this.ser_name = ser_name;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getMer_name() {
		return mer_name;
	}

	public void setMer_name(String mer_name) {
		this.mer_name = mer_name;
	}

	public String getProgrammedesc() {
		return programmedesc;
	}

	public void setProgrammedesc(String programmedesc) {
		this.programmedesc = programmedesc;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public int getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(int merchantid) {
		this.merchantid = merchantid;
	}
	
	

	
}

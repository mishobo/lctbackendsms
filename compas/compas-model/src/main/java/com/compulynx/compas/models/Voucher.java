/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author shree
 *
 */
public class Voucher {

	public Voucher(int voucherId, String voucherDesc, boolean isActive,
			double voucherValue, String frqSelect, String modeSelect) {
		super();
		this.voucherId = voucherId;
		this.voucherDesc = voucherDesc;
		this.isActive = isActive;
		this.voucherValue = voucherValue;
		this.frqSelect = frqSelect;
		this.modeSelect = modeSelect;
		
	}
	public Voucher(int voucherId, String voucherDesc, boolean isActive,
			double voucherValue, String frqSelect, String modeSelect,List<Service> services) {
		super();
		this.voucherId = voucherId;
		this.voucherDesc = voucherDesc;
		this.isActive = isActive;
		this.voucherValue = voucherValue;
		this.frqSelect = frqSelect;
		this.modeSelect = modeSelect;
		this.services=services;
	}
	public int voucherId;
	public String voucherCode;
	public String voucherDesc;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public List<Service> services;
	public double voucherValue;
	public String voucherType;
	public String currency;
	public String frqSelect;
	public String modeSelect;
	public  String startDate;
	public  String endDate;
	public boolean expanded;
	public String schemeType;
	public String voucherName;
	public double voucherBalance;
	public String amountRemaining;
	public double usage;
	public String used;
	public String value;
	public boolean notNewToProgramme;
	public boolean valueHasNotChanged;
	public boolean voucherNotRemoved;
	public boolean isOld;
	public boolean isRemoved;
	public String coverType;
	public int count;
	public double serviceValue;
	public double coverLimit;
	public double serviceBalance;

	public Voucher() {
		super();
		// TODO Auto-generated constructor stub
	}
}

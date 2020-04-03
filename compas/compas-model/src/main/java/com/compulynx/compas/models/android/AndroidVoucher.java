/**
 * 
 */
package com.compulynx.compas.models.android;

import java.util.List;

/**
 * @author Anita	
 *
 */
public class AndroidVoucher {
	
	public AndroidVoucher() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int voucherId;
	public String voucherName;
	public String startDate;
	public String endDate;
	public List<AndroidServices> serviceDetails;
	public double voucherValue;
	public String voucherCode;
}

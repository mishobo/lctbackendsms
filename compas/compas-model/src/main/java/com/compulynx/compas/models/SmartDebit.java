package com.compulynx.compas.models;

import java.util.List;

public class SmartDebit {
	    public Long id;
	    public String smart_bill_id;
	    public double return_amount;
	    public String return_reason;
	    public String lct_mem_no;
	    public String provider;
	    public String invoice_no;
	    public String action_name;
	    public String benefit;
	    public String invoice_date;
		public String item_code;
		@Override
		public String toString() {
			return "SmartDebit [id=" + id + ", smart_bill_id=" + smart_bill_id + ", return_amount=" + return_amount
					+ ", return_reason=" + return_reason + ", lct_mem_no=" + lct_mem_no + ", provider=" + provider
					+ ", invoice_no=" + invoice_no + ", action_name=" + action_name + ", benefit=" + benefit
					+ ", invoice_date=" + invoice_date + ", item_code=" + item_code + "]";
		}
		
		
}

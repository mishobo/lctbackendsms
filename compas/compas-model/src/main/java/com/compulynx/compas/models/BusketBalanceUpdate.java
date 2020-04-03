package com.compulynx.compas.models;

import java.util.List;

public class BusketBalanceUpdate {
	public BusketBalanceUpdate(int acc_no) {
		super();
		this.acc_no = acc_no;
	}
	public int acc_no;
	public List<Programme> programmes;
	
	public BusketBalanceUpdate() {
		super();
	}
	

}

package com.compulynx.compas.models;

import java.util.List;

/**
 * Created by Kibet on 5/23/2016.
 */
public class Topup {

    public Topup(){

        super();
    }

    public int id;
    public int beneficiary_group_id;
    public String bnfGrpname;
    public int programme_id;
    public String programmeCode;
    public String programme_name;
    public int beneficiary_id;
    public String card_number;
    public String voucher_id;
    public int voucherId;
    public String voucherName;
    public int service_id;
    public double service_value;
    public String itemvalue;
    public String service_name;
    public double service_quantity;
    public String uom;
    public String downloaded;
    public int cycle;
    public String voucher_type;
    public String created_on;
    public List programmes;
    public List beneficiary_groups;
    public String requestType;
    public String valid_from;
    public String valid_until;
    public int count;
    public boolean isActive;
    public List topupSelection;
    public int createdBy;
    public int agentId;
    public String userName;
    public String userEmail;
    
    
    public List<BeneficiaryGroup> bgTopupDetails;
    public List<Programme> prgTopupDetails;
    public List<Topup> topupDetails;
    public List retailerSelected;
	public int bnfGrpId;
	public String fromDate;
	public String toDate;
}

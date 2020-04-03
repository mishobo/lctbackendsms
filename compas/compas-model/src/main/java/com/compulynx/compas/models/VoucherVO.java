package com.compulynx.compas.models;

import java.util.List;

/**
 * Created by Kibet on 5/23/2016.
 */
public class VoucherVO {

    public VoucherVO(){
        super();
    }

//    "cycle": 4,
//            "intervention_id": 501,
//            "intervention": "FCD ",
//            "card_number": "0000000031938004",
//            "voucher_id_number": 12177,
//            "type": 2,
//            "value": "3600.00",

    public int cycle;
    public int intervention_id;
    public String intervention;
    public String card_number;
    public String voucher_id_number;
    public String type;
    public double value;
    public List commodities;
    public String valid_from;
    public String valid_until;

}

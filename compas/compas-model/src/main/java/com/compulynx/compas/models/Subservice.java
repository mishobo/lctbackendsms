package com.compulynx.compas.models;

import java.io.Serializable;

public class Subservice implements Serializable{
	public int sub_service_id;
	public String sub_service_code;
	public String sub_service_name;
	public String sub_service_dept;
	public String sub_service_section;
	public int mst_service_id;
	@Override
	public String toString() {
		return "Subservice [sub_service_id=" + sub_service_id + ", sub_service_code=" + sub_service_code
				+ ", sub_service_name=" + sub_service_name + ", sub_service_dept=" + sub_service_dept
				+ ", sub_service_section=" + sub_service_section + ", mst_service_id=" + mst_service_id + "]";
	}
	
}

package com.compulynx.compas.models;

public class StringUtil {
	
	public static boolean hasValue(String str) {
		if(str != null && !"".equals(str.trim())) {
			return true;
		}
		return false;
	}

}

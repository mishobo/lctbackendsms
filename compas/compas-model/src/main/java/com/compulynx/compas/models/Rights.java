package com.compulynx.compas.models;

import java.util.ArrayList;
import java.util.List;

public class Rights {
	public String headerName;
	public String headerIconCss;
	public String headerIconColor;
	public List<RightsDetail> rightsList;

	public Rights(String headerName, String headerIconCss, String headerIconColor) {
		super();
		this.headerName = headerName;
		this.headerIconCss = headerIconCss;
		this.headerIconColor = headerIconColor;
		this.rightsList = new ArrayList<RightsDetail>();
	}

	public Rights() {
		super();
	}
}

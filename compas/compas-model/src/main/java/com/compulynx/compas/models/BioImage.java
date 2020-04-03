package com.compulynx.compas.models;

public class BioImage {

	public BioImage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BioImage(int memberId, String image) {
		super();
		this.memberId = memberId;
		this.image = image;
	}
	public int memberId;
	public String image;
	public String position;
}

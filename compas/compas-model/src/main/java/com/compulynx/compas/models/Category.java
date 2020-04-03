/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Category {
public int categoryId;
public String categoryCode;
public String categoryName;
public boolean active;
public int respCode;
public int createdBy;
public int count;

public Category(int categoryId, String categoryCode, String categoryName,
		boolean active,int createdBy,int respCode,int count) {
	super();
	this.categoryId = categoryId;
	this.categoryCode = categoryCode;
	this.categoryName = categoryName;
	this.active = active;
	this.createdBy = createdBy;
	this.respCode = respCode;
	this.count=count;
}

public Category() {
	super();
	// TODO Auto-generated constructor stub
}
}

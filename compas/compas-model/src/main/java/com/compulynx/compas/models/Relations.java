/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author shree
 *
 */
public class Relations {
public int relationId;
public String relationDesc;
public int createdBy;
public int respCode;

public Relations(int respCode) {
	super();
	this.respCode=respCode;
}

public Relations() {
	super();
	// TODO Auto-generated constructor stub
}

public Relations(int relationId, String relationDesc, int createdBy,
		int respCode) {
	super();
	this.relationId = relationId;
	this.relationDesc = relationDesc;
	this.createdBy = createdBy;
	this.respCode = respCode;
}
}

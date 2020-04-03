/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 * @Date 08 August 2016
 *
 */
public class GroupType {

	public int grpTypeId;
	public String grpTypeName;
	public int createdBy;
	public int respCode;
	public GroupType() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GroupType(int grpTypeId, String grpTypeName, int respCode) {
		super();
		this.grpTypeId = grpTypeId;
		this.grpTypeName = grpTypeName;
		this.respCode = respCode;
	}
}

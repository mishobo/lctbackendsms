/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Params {
	public int paramId;
	public int serviceId;
	public String paramName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public String paramValue;
	public int count;
	public String paramType;
	

	public Params() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Params(int paramId, String paramName, boolean active, int createdBy,int respCode,int count){
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.count=count;
		
	}

	public Params(int paramId, String paramName,
			boolean isActive, int respCode) {
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		this.isActive = isActive;
		this.respCode = respCode;
	}
	public Params(int paramId, String paramName) {
		super();
		this.paramId = paramId;
		this.paramName = paramName;
		
	}
}

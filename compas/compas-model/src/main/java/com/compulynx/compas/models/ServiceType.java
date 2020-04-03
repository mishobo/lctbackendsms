/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Kibet
 *
 */
public class ServiceType {

	public int serTypeId;
	public String serTypeCode;
	public String serTypeName;
	public String abbr;
	public ServiceType(int serTypeId, String serTypeCode, String serTypeName,String abbr) {
		super();
		this.serTypeId = serTypeId;
		this.serTypeCode = serTypeCode;
		this.serTypeName = serTypeName;
		this.abbr=abbr;
	}
	public ServiceType() {
		super();
		// TODO Auto-generated constructor stub
	}
}

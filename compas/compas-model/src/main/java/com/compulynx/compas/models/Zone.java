/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Zone {

	public int zoneId;
	public String zoneCode;
	public String zoneName;
	public int organizationId;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public Zone(int zoneId, String zoneCode, String zoneName,
			 boolean active,int count) {
		super();
		this.zoneId = zoneId;
		this.zoneCode = zoneCode;
		this.zoneName = zoneName;
		this.active = active;
		this.count=count;
	}
	public Zone() {
		super();
		// TODO Auto-generated constructor stub
	}
}

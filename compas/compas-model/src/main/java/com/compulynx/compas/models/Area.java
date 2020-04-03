/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Area {
	public int areaId;
	public String areaCode;
	public String areaName;
	public int zoneId;
	public String zoneName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public Area() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Area(int areaId, String areaCode, String areaName,int zoneId,String zoneName,
			 boolean active,int count) {
		super();
		this.areaId = areaId;
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.zoneId=zoneId;
		this.zoneName=zoneName;
		this.active = active;
		this.count=count;
	}
	public Area(int areaId, String areaName) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
	}
}

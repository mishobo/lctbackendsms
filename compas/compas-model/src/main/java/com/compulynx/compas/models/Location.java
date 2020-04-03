/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Location {
	public int locationId;
	public String locationCode;
	public String locationName;
	public int areaId;
	public String areaName;
	public int zoneId;
	public String zoneName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int count;
	public List<Location> locationDetails;
	public String status;
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Location(int locationId, String locationCode, String locationName,
			int areaId, String areaName, int zoneId, String zoneName,
			boolean active, int respCode, int count,
			String status) {
		super();
		this.locationId = locationId;
		this.locationCode = locationCode;
		this.locationName = locationName;
		this.areaId = areaId;
		this.areaName = areaName;
		this.zoneId = zoneId;
		this.zoneName = zoneName;
		this.active = active;
		this.respCode = respCode;
		this.count = count;
		this.status = status;
	}
;
}

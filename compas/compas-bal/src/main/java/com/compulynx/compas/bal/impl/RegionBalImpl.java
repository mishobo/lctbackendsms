/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.RegionBal;
import com.compulynx.compas.dal.impl.RegionDalImpl;
import com.compulynx.compas.models.Area;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Zone;

/**
 * @author Anita
 *
 */
@Component
public class RegionBalImpl implements RegionBal{
	@Autowired
	RegionDalImpl regionDal;

	@Override
	public CompasResponse UpdateZone(Zone zone) {
		// TODO Auto-generated method stub
		return regionDal.UpdateZone(zone);
	}

	@Override
	public List<Zone> GetZones() {
		// TODO Auto-generated method stub
		return regionDal.GetZones();
	}

	@Override
	public CompasResponse UpdateArea(Area area) {
		// TODO Auto-generated method stub
		return regionDal.UpdateArea(area);
	}

	@Override
	public List<Area> GetAreas() {
		// TODO Auto-generated method stub
		return regionDal.GetAreas();
	}

	@Override
	public CompasResponse UpdateLocation(Location location) {
		// TODO Auto-generated method stub
		return regionDal.UpdateLocation(location);
	}

	@Override
	public List<Location> GetLocations() {
		// TODO Auto-generated method stub
		return regionDal.GetLocations();
	}

	@Override
	public List<Area> GetActiveArea() {
		// TODO Auto-generated method stub
		return regionDal.GetActiveArea();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.RegionBal#GetActiveRegion()
	 */
	@Override
	public List<Zone> GetActiveRegion() {
		// TODO Auto-generated method stub
		return regionDal.GetActiveRegion();
	}
	

}

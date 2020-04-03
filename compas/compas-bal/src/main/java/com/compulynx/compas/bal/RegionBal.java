/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Area;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Zone;

/**
 * @author Anita
 *
 */
public interface RegionBal {
	CompasResponse UpdateZone(Zone zone);

	List<Zone> GetZones();
	
	CompasResponse UpdateArea(Area area);

	List<Area> GetAreas();
	
	CompasResponse UpdateLocation(Location location);

	List<Location> GetLocations();
	
	List<Area> GetActiveArea();
	List<Zone> GetActiveRegion();
}

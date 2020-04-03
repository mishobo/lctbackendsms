/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.impl.RegionBalImpl;
import com.compulynx.compas.models.Area;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Zone;

/**
 * @author Anita
 *
 */
@Component
@Path("/region")
public class RegionSvc {
	@Autowired
	RegionBalImpl regionBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtZones/")
	public Response GetMerchants() {
		try {
			List<Zone> merchants = regionBal.GetZones();
			if (!(merchants == null)) {
				return Response.status(200).entity(merchants).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updZone/")
	public Response UpdateMerchant(Zone zone) {
		try {
			CompasResponse response = regionBal.UpdateZone(zone);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAreas/")
	public Response GetAreas() {
		try {
			List<Area> merchants = regionBal.GetAreas();
			if (!(merchants == null)) {
				return Response.status(200).entity(merchants).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updArea/")
	public Response UpdateArea(Area area) {
		try {
			CompasResponse response = regionBal.UpdateArea(area);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLocations/")
	public Response GetLocations() {
		try {
			List<Location> merchants = regionBal.GetLocations();
			if (!(merchants == null)) {
				return Response.status(200).entity(merchants).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updLocation/")
	public Response UpdateLocation(Location location) {
		try {
			CompasResponse response = regionBal.UpdateLocation(location);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveAreas/")
	public Response GetActiveAreas() {
		try {
			List<Area> merchants = regionBal.GetActiveArea();
			if (!(merchants == null)) {
				return Response.status(200).entity(merchants).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveRegions/")
	public Response GetActiveRegion() {
		try {
			List<Zone> merchants = regionBal.GetActiveRegion();
			if (!(merchants == null)) {
				return Response.status(200).entity(merchants).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}

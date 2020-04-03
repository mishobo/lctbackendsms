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

import com.compulynx.compas.bal.DeviceBal;
import com.compulynx.compas.bal.MerchantBal;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.POSUser;
import com.compulynx.compas.models.CompasResponse;


/**
 * @author Anita
 *
 */
@Component
@Path("/device")
public class DeviceSvc {

	@Autowired
	DeviceBal deviceBal;
	@Autowired
	MerchantBal merchantBal;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDevices")
	public Response GetDevices() {
			List<Device> devices = deviceBal.GetAllDevicesInfo();
			return Response.status(200).entity(devices).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updDevice")
	public Response UpdateDevice(Device device) {
			CompasResponse response = deviceBal.UpdateDeviceInfo(device);
			return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtIssueDevices/{classId},{merchantId}")
	public Response GetIssueDevices(@PathParam("classId") int classId,
			@PathParam("merchantId") int merchantId) {
			List<Device> issueDevices = deviceBal.GetAllIssueDevicesInfo(classId,merchantId);
			return Response.status(200).entity(issueDevices).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updIssueDevice")
	public Response UpdateIssueDevice(Device device) {
			CompasResponse response = deviceBal.UpdateIssueDeviceInfo(device);
			return Response.status(200).entity(response).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveAgents/{branchId}")
	public Response GetActiveAgents(@PathParam("branchId") int branchId) {
			List<Agent> agents = deviceBal.GetActiveAgents(branchId);
			return Response.status(200).entity(agents).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveDevices")
	public Response GetActiveDevices() {
			List<Device> devices = deviceBal.GetActiveDevicesInfo();
			return Response.status(200).entity(devices).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveDevicess/{serialNo}")
	public Response GetActiveDevicess(@PathParam("serialNo") String serialNo) {
			List<Device> devices = deviceBal.GetActiveDevicessInfo(serialNo);
			return Response.status(200).entity(devices).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getActivePOSUsers/{merchantId}")
	public Response GetActivePOSUsers(@PathParam("merchantId") String merchantId) {	
		try
		{
			List<POSUser> posUsers = merchantBal.getMerchantPOSUsersActive(merchantId);
			return Response.status(200).entity(posUsers).build();	
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return Response.status(404).entity(null).build();	
		}
	}
}

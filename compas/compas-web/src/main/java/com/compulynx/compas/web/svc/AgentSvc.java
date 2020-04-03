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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.impl.AgentBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;



/**
 * @author shree
 *
 */
@Component
@Path("/agent")
public class AgentSvc {
	@Autowired
	AgentBalImpl agentBal;
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAgents/")
	public Response GetAgents() {
		List<Agent> agents = agentBal.GetAllAgents();
		return Response.status(200).entity(agents).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getBranchesByMerchant/{merchantId}")
	public Response GetMerchantsByBranchId(@PathParam("merchantId") int merchantId) {
		List<Branch> merchants = agentBal.GetBranchesByMerchant(merchantId);
		return Response.status(200).entity(merchants).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updAgent")
	public Response UpdateAgent(Agent agent) {
		CompasResponse response = agentBal.UpdateAgent(agent);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getLocationsByMerchant/{merchantId}")
	public Response GetLocationsByMerchant(@PathParam("merchantId") int merchantId) {
		List<Location> locations = agentBal.GetLocationsByMerchant(merchantId);
		return Response.status(200).entity(locations).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAgentsByMerchant/{merchant}")
	public Response GetAgentsByMerchant(@PathParam("merchant") String merchant) {
		List<Agent> locations = agentBal.GetAgentsByMerchant(merchant);
		return Response.status(200).entity(locations).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/testLic")
	public Response addUser(@Context HttpHeaders headers) {

		System.out.println(headers);
		String userAgent = headers.getRequestHeader("license-key").get(0);
		//CompasResponse response = agentBal.UpdateAgent(agent);
		System.out.println("userAgent@@@"+userAgent);
		return Response.status(200)
			.entity("token Received##"+userAgent)
			.build();
	}
}

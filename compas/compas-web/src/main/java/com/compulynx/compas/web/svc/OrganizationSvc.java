package com.compulynx.compas.web.svc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.impl.OrganizationBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Organization;

@Component
@Path("/organization")
public class OrganizationSvc {

	@Autowired
	OrganizationBalImpl organizationBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtOrganizations")
	public Response GetBrokers() {
		try {
			List<Organization> organizations = organizationBal.GetOrganizations();
			if (!(organizations == null)) {
				return Response.status(200).entity(organizations).build();
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
	@Path("/updOrganization")
	public Response UpdateBroker(Organization organization) {
		try {
			CompasResponse response = organizationBal.UpdateOrganization(organization);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}

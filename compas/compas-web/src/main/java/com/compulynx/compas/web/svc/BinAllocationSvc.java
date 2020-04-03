package com.compulynx.compas.web.svc;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.impl.BinAllocationBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BinAllocation;
import com.compulynx.compas.models.CompasResponse;

@Component
@Path("/bin")
public class BinAllocationSvc {
	@Autowired
	BinAllocationBalImpl binBal;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updBin")
	public Response UpdateBin(BinAllocation bin) {
		CompasResponse response = binBal.UpdateBin(bin);
		return Response.status(200).entity(response).build();
	}
}

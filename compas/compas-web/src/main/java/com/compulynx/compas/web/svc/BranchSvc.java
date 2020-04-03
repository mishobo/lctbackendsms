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

import com.compulynx.compas.bal.impl.BranchBalImpl;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Company;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
@Component
@Path("/branch")
public class BranchSvc {
	@Autowired
	BranchBalImpl branchBal;
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtBranches/")
	public Response GetBranches() {
		
			List<Branch> branches = branchBal.GetAllBranches();
			return Response.status(200).entity(branches).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCompany")
	public Response GetCompanies() {
		
			List<Company> company = branchBal.GetAllCompanies();
			return Response.status(200).entity(company).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updBranch")
	public Response UpdateBranch(Branch branch) {
		
			CompasResponse response = branchBal.UpdateBranch(branch);
			return Response.status(200).entity(response).build();
	}
}

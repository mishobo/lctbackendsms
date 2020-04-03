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

import com.compulynx.compas.bal.MerchantBal;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Detach;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Organization;
import com.compulynx.compas.models.POSUser;

/**
 * @author Anita
 *
 */
@Component
@Path("/merchant")
public class MerchantSvc {
	@Autowired
	MerchantBal merchantBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMerchants/")
	public Response GetMerchants() {
		try {
			List<Merchant> merchants = merchantBal.GetMerchants();
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
	
	
	
	//get merchants by username
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMerchantsByUserName/")
	public Response GetMerchantsById(String LoggedInUser) {
		try {
			List<Merchant> serviceProviders = merchantBal.GetMerchantsById(LoggedInUser);
			if (!(serviceProviders == null)) {
				return Response.status(200).entity(serviceProviders).build();
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
	@Path("/updMerchant/")
	public Response UpdateMerchant(Merchant merchant) {
		try {
			CompasResponse response = merchantBal.UpdateMerchant(merchant);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addMerchantPOSUser")
	public Response addMerchantPOSUser(POSUser posUser) {
		try {
			CompasResponse response = new CompasResponse();
			boolean done = merchantBal.addMerchantPOSUser(posUser);
			if (done) {
				response.respCode = 200;
			} else {
				response.respCode = 201;
			}
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMerchantPOSUsers/")
	public Response GetMerchantPOSUsers(String merchantCode) {
		try {
			List<POSUser> posUsers = merchantBal.getMerchantPOSUsers(merchantCode);
			if (!(posUsers == null)) {
				return Response.status(200).entity(posUsers).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/resetMerchantPOSUserPIN/")
	public Response resetMerchantPOSUserPIN(POSUser posUser) {
		try {
			boolean done = merchantBal.resetMerchantPOSUser(posUser);
			if (done) {
				return Response.status(200).entity(null).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editMerchantPOSUser/")
	public Response editMerchantPOSUser(POSUser posUser) {
		try {
			boolean done = merchantBal.editMerchantPOSUser(posUser);
			if (done) {
				return Response.status(200).entity(null).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	//delete POS USER
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteMerchantPOSUser/")
	public Response deleteMerchantPOSUser(POSUser posUser) {		
		try {
			CompasResponse response = merchantBal.deleteMerchantPOSUser(posUser);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
}

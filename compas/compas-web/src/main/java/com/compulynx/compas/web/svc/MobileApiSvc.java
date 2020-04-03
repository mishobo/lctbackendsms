/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.MobileApiBal;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.POSUser;
import com.compulynx.compas.models.android.APIPOSUserAuth;

/**
 * @author Anita
 *
 */
@Component
@Path("/mobileapi")
public class MobileApiSvc {
	@Autowired
	MobileApiBal apiBal;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response loginPOSUser(APIPOSUserAuth user)
	{
		CompasResponse response = apiBal.loginPOSUser(user);
		if(response.respCode==200)
		{
			return Response.status(200).entity(response).build();
		}
		return Response.status(201).entity(response).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reset")
	public Response resetPOSUser(POSUser user)
	{
		CompasResponse response = apiBal.resetPOSUser(user);
		if(response.respCode==200)
		{
			return Response.status(200).entity(response).build();
		}
		return Response.status(201).entity(response).build();
	}

}

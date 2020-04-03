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

import com.compulynx.compas.bal.impl.ResetBalImpl;
import com.compulynx.compas.models.ResetUser;
import com.compulynx.compas.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/reset")
public class ResetSvc {

	@Autowired
	ResetBalImpl ResetBal;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response resetUserPassword(User user) {
		try {
			System.out.println("username####"+user.userName);
			System.out.println("user email####"+user.userEmail);
			ResetUser ResetUser = ResetBal.resetUserPassword(user.userName);
			return Response.status(ResetUser.getRespCode()).entity(ResetUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	
}

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

import com.compulynx.compas.bal.impl.LoginBalImpl;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;

import com.compulynx.compas.models.Service;

import com.compulynx.compas.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/login")
public class LoginSvc {

	@Autowired
	LoginBalImpl loginBal;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/manualAuth")
	public Response ValidateManualLogin(User user) {
		try {
			System.out.println("username####"+user.userName);
			LoginUser loginUser = loginBal.ValidateManualLogin(user.userId,user.userName,
					user.userPwd);
			return Response.status(loginUser.respCode).entity(loginUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deviceAuth")
	public Response ValidateDeviceLogin(User user) {
		try {
			System.out.println("username####"+user.userName);
			LoginUser loginUser = loginBal.GetUserIdDevice(user.userName,
					user.userPwd,user.deviceId);
			return Response.status(loginUser.respCode).entity(loginUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bioAuth/{bioId}")
	public Response ValidateBioLogin(@PathParam("bioId") int bioId) {
		try {
			LoginUser loginUser = loginBal.ValidateBioLogin(bioId);
			return Response.status(loginUser.respCode).entity(loginUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bioRt")
	public Response bioLogin(User user) {
		try {
			LoginUser loginUser = loginBal.ValidateBioLogin(user.userName);
			return Response.status(loginUser.respCode).entity(loginUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUserRights")
	public Response GetUserLoginSession(LoginUser usrId) {
		try {
			LoginSession loginSession = loginBal.GetLoginSession(usrId);
			return Response.status(loginSession.respCode).entity(loginSession)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkIp")
	public Response CheckIpAvailable() {
		try {
			LoginUser loginUser = new LoginUser();
			loginUser.respCode=200;
			loginUser.respMessage="Success";
			//String Connect="Success";
			return Response.status(200).entity(loginUser)
					.build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
//	@Path("PDF-file.pdf/")
//	@GET
//	@Produces({"application/pdf"})
//	public StreamingOutput getPDF() throws Exception {
//	    return new StreamingOutput() {
//	        public void write(OutputStream output) throws IOException, WebApplicationException {
//	            try {
//	                PDFGenerator generator = new PDFGenerator(getEntity());
//	                generator.generatePDF(output);
//	            } catch (Exception e) {
//	                throw new WebApplicationException(e);
//	            }
//	        }
//	    };
//	}

	
	
}

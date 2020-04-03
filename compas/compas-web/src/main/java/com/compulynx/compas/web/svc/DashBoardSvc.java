package com.compulynx.compas.web.svc;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.compulynx.compas.bal.impl.DashBoardBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.DashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/dashBoard")
public class DashBoardSvc {
	@Autowired
	DashBoardBalImpl dashBoardBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDashboardCount")
	public Response GetDashBoardCount() {
		try {
			DashBoard detail = dashBoardBal.GetDashBoardCount();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/gtCountDetail")
	public Response GetDashBoardCountDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardCountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/gtAgentDetail")
	public Response GetDashBoardAgentDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardAgentDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/gtTransChartDetail")
	public Response GetTransChartDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetTransChartDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/gtAmountDetail")
	public Response GetDashBoardAmountDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardAmountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/gtFlowChartDetail")
	public Response GetFlowChartDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetFlowChartCountDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/gtCollectionDetail")
	public Response GetCollectionDetail() {
		try {
			List<DashBoard> detail = dashBoardBal.GetDashBoardCollectionDetail();
			if (!(detail == null)) {
				return Response.status(200).entity(detail).build();
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
	@Path("/isuserakuh/{userId}")
	public Response isUserAKUH(@PathParam("userId") int userId) {
		boolean isUserAKUH = dashBoardBal.isUserAKUH(userId);
		if(isUserAKUH)
		{
			 return Response.status(200).entity(new CompasResponse(200,"Success: can upload files")).build();
		}
		System.out.println("we here..."+isUserAKUH);
		return Response.status(200).entity(new CompasResponse(201,"Denied: cannot upload files")).build();
	}
}

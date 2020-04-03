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

import com.compulynx.compas.bal.impl.UserGroupBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GroupType;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.UserGroup;

/**
 * @author anita
 *
 */
@Component
@Path("/userGroups")
public class UserGroupSvc {
	
	@Autowired
	UserGroupBalImpl userGroupBal;
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGroups")
	public Response GetGroups() {
		try {
			List<UserGroup> groups = userGroupBal.GetGroups();
			if (!(groups == null)) {
				return Response.status(200).entity(groups).build();
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
	@Path("/gtRights/{rightTypeId}")
	public Response GetRights(@PathParam("rightTypeId") int rightTypeId) {
		try {
			List<RightsDetail> rights = userGroupBal.GetRights(rightTypeId);
			if (!(rights == null)) {
				return Response.status(200).entity(rights).build();
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
	@Path("/updGroup")
	public Response UpdateGroup(UserGroup group) {
		
			CompasResponse response = userGroupBal.UpdateUserGroup(group);
			return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGrpTypes")
	public Response GetGrpTypes() {
		try {
			List<GroupType> grpTypes = userGroupBal.GetGroupTypes();
			if (!(grpTypes == null)) {
				return Response.status(200).entity(grpTypes).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

}

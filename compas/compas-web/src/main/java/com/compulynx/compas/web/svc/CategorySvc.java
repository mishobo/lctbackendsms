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

import com.compulynx.compas.bal.impl.CategoryBalImpl;
import com.compulynx.compas.models.Category;
import com.compulynx.compas.models.CompasResponse;


/**
 * @author Anita
 *
 */
@Component
@Path("/category")
public class CategorySvc {
	@Autowired
	CategoryBalImpl categoryBal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCategories/")
	public Response GetCategories() {
		try {
			List<Category> merchants = categoryBal.GetCategories();
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
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updCategory/")
	public Response UpdateCategory(Category category) {
		try {
			CompasResponse response = categoryBal.UpdateCategory(category);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
}

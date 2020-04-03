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

import com.compulynx.compas.bal.impl.ProgrammeBalImpl;
import com.compulynx.compas.bal.impl.VoucherBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
@Component
@Path("/voucher")
public class VoucherSvc {
	@Autowired
	VoucherBalImpl voucherBal;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtServiceProducts/{voucherType}")
	public Response GetServiceProducts(@PathParam("voucherType") String voucherType) {
		List<Service> services = voucherBal.GetServiceProducts(voucherType);
		return Response.status(200).entity(services).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updVoucher")
	public Response UpdateVoucher(Voucher voucher) {
		CompasResponse response = voucherBal.UpdateVoucher(voucher);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtVouchers")
	public Response GetVouchers() {
		List<Voucher> programmes = voucherBal.GetAllVouchers();
		return Response.status(200).entity(programmes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCashVouchers/")
	public Response GetCahsVouchers() {
		List<Voucher> programmes = voucherBal.GetCashVouchers();
		return Response.status(200).entity(programmes).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtVouchersByOrg/{orgId}")
	public Response GetVouchersByOrg(@PathParam("orgId") int orgId) {
		List<Voucher> programmes = voucherBal.GetAllVouchersByOrg(orgId);
		return Response.status(200).entity(programmes).build();
	}
}

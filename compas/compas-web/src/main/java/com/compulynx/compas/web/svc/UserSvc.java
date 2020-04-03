/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import com.compulynx.compas.bal.ProductBal;
import com.compulynx.compas.bal.impl.UserBalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.CardActivation;
import com.compulynx.compas.models.CompasProperties;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Demo;
import com.compulynx.compas.models.DemoResponse;
import com.compulynx.compas.models.FingerPrintDetails;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.Product;
import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.TransAuthResponse;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.TransactionMst;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.android.AndroidTopup;
import com.compulynx.compas.models.android.AndroidTransaction;
import com.compulynx.compas.models.android.Pagination;
import com.compulynx.compas.models.AutoComplete;
import com.compulynx.compas.models.AutoCompleteObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;


/**
 * @author Anita
 *
 */

@Component
@Path("/user")
public class UserSvc {

	@Autowired
	UserBalImpl userBal;
	@Autowired
	ProductBal productBal;
	private static final Logger logger = Logger.getLogger(UserSvc.class.getName());

	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updUser")
	public Response UpdateUser(User user) {
		CompasResponse response = userBal.UpdateUser(user);
		return Response.status(200).entity(response).build();
	}
	
	//change user password
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/changePassword")
	public Response changePassword(User user) {
		CompasResponse response = userBal.changePassword(user);
		return Response.status(200).entity(response).build();
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtQuestions/")
	public Response GetQuestions() {
		List<User> questions = userBal.GetQuestions();
		return Response.status(200).entity(questions).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtLocationAgents/{locationId}")
	public Response GetLocationAgents(@PathParam("locationId") int locationId){
		List<Agent> agents = userBal.GetLocationAgents(locationId);
		return Response.status(200).entity(agents).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUsers/")
	public Response GetUsers() {
		try {
			List<User> users = userBal.GetAllUsers();
			if (!(users == null)) {
				return Response.status(200).entity(users).build();
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
	@Path("/gtClasses/")
	public Response GetClasses() {
		try {
			List<User> classes = userBal.GetClasses();
			if (!(classes == null)) {
				return Response.status(200).entity(classes).build();
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
	@Path("/updAndroidTrans")
	public Response UpdateAndroidTrans(List<TransactionMst> trans) {

		System.out.println("size##=" + trans.size());
		System.out
				.println("sizeTransList##=" + trans.get(0).transDtl.size());

		//
		CompasResponse response = userBal.UplodTmsTrans(trans);
		return Response.status(200).entity(response).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtGroupsByUserType/{userTypeId}")
	public Response GetGroupsByUserTypes(@PathParam("userTypeId") int userTypeId) {
		List<UserGroup> agents = userBal.GetGroupsByUserType(userTypeId);
		return Response.status(200).entity(agents).build();
	}

	@POST
	// @Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadVo")
	public Response GetAndroidDownloadData(Pagination page) {
		logger.info("Mac Address Request Received From Mobile$$$" + page.macAddress);

		AndroidDownloadVo downloadVo = userBal
				.GetAndroidDownloadData( page.macAddress);
		if (downloadVo != null) {
			return Response.status(200).entity(downloadVo).build();
		} else
			return Response.status(202).entity(null).build();
	}

	@POST
	@Consumes({"text/plain",MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadMember")
	public Response DownloadBeneficiaries(Member member) {
		logger.info("Getting Member Details$$");
		AndroidDownloadVo downloadVo = userBal.GetAndroidBeneficiary(member);
		System.out.println("AndroidDownloadVo member: "+downloadVo.member);
		if (downloadVo != null) {
			if(downloadVo.member!=null)
			{
				if(downloadVo.member.scheme!=null)
				{
					Product productScheme = productBal.getProductByProductName(downloadVo.member.scheme);
					downloadVo.member.setSchemeCode(productScheme.productCode);
					downloadVo.member.setInsuranceCode(productScheme.insuranceCode);
					System.out.println("AndroidDownloadVo member: "+downloadVo.member);
				}
				
			}
			return Response.status(200).entity(downloadVo.member).build();
		} else
			return Response.status(202).entity(null).build();
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUserById/{userId}")
	public Response GetUserById(@PathParam("userId") int userId) {

		try {
			User user = userBal.GetUserById(userId);
			if (!(user == null)) {
				return Response.status(200).entity(user).build();
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
	@Path("/updMemBioImages")
	public Response UpdateBnfBioimages(List<Member> member) {
		try {
			logger.info("Saving Bioimages for Members##");
			CompasResponse response = userBal.UpdateBnfBioImages(member);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateCardActivations")
	public Response CardActivation(List<CardActivation> cardActivation) {
		try {
			CompasResponse response = userBal.CardActivation(cardActivation);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateDemo")
	public Response UpdateDemo(Demo demo) {
		try {
//			AbisRequest data = formatRequest(demo);
//			ObjectMapper mapper = new ObjectMapper();
//			String jsonInString = mapper.writeValueAsString(data);
//			CloseableHttpClient client = HttpClients.createDefault();
//			HttpPost request=new HttpPost("http://197.220.114.46:9022/bio/api/afis");
//			request.addHeader("Content-Type","application/json");
//			 StringEntity body=new StringEntity(jsonInString);
//			 request.setEntity(body);
//			 CloseableHttpResponse  res=client.execute(request);
//			 String json = EntityUtils.toString(res.getEntity(), "UTF-8");
//			 AbisResponse obj = mapper.readValue(json, AbisResponse.class);
//			 if(obj.status == "000") {
//				 demo.applicantId = obj.applicantId;
				DemoResponse response = userBal.UpdateDemo(demo);
				return Response.status(response.respCode).entity(response).build();
//			 }else {
//				 return Response.status(201).entity(null).build();
//			 }
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getDemo")
	public Response GetDemo(Demo demo) {
		try {
			DemoResponse response = userBal.GetDemo(demo);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/verifyNumber")
	public Response VerifyNumber(Demo demo) {
		try {
			DemoResponse response = userBal.VerifyNumber(demo);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/checkAuthTransStatus")
	public Response CheckAuthTransStatus(Transaction trans) {
		TransAuthResponse response = userBal.CheckAuthTransStatus(trans);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkConnection")
	public Response CheckConnection() {
		CompasResponse response = new CompasResponse();
		response.respCode = 200;
		response.respMessage = "Connection success";
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/autocomplete/memberNo={memberNo}")
	public Response GetMatchingMembers(@PathParam("memberNo") String memberNo) {
		AutoCompleteObject response = userBal.GetMatchingMembers(memberNo);
		return Response.status(200).entity(response).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/autocompleteAll")
	public Response GetAllMembers(){
		AutoCompleteObject response = userBal.GetAllMembers();
		return Response.status(200).entity(response).build();
	}
	
	//audit trail
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllAuditLogDetail")
	public Response GetAuditLogDetails(Reports audrpt) {
		System.out.println("Fetching Audit Trail");
		List<Reports> response = userBal.GetAuditLogDetails(audrpt);
		return Response.status(200).entity(response).build();
	}
	
	

}

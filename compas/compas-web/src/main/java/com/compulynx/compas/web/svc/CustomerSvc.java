package com.compulynx.compas.web.svc;

import com.compulynx.compas.bal.impl.CustomerBalImpl;
import com.compulynx.compas.models.*;
import com.compulynx.compas.models.android.ApiProgramme;
import com.compulynx.compas.models.android.ApiVoucher;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.logging.Logger;

@Component
@Path("/member")
public class CustomerSvc {
	//rewire
	@Autowired
	CustomerBalImpl	memberBal;

	
	private static final Logger logger = Logger.getLogger(CustomerSvc.class.getName());
	private String TOMCAT_HOME ="";
    private String fileName="";
    private String PATH = "";

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtMembersToVerify/")
	public Response GetMembersToVerify(Member member) {
		try {
			List<Member> members = memberBal.GetMembersToVerify(member.fromDate, member.toDate, member.count,member.orgId);
			if (!(members == null)) {
				return Response.status(200).entity(members).build();
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
	@Path("/gtVerifiedMembers/")
	public Response GetMembersToApprove(Member member) {
		try {
			List<Member> members = memberBal.GetMembersToApprove(member.fromDate, member.toDate, member.count,member.orgId);
			if (!(members == null)) {
				return Response.status(200).entity(members).build();
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
	@Path("/gtMembers/")
	public Response GetMembers(@QueryParam("memberNo") String memberNo) {
		try {
			Member members = memberBal.GetMembers(memberNo);
			if (!(members == null)) {
				return Response.status(200).entity(members).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	//Dependant Inquiry
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtDependants/")
	public Response GetDependants(@QueryParam("memberNo") String memberNo) {
		try {
			Member members = memberBal.GetDependants(memberNo);
			if (!(members == null)) {
				return Response.status(200).entity(members).build();
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
	@Path("/updCustomer")
	public Response UpdateCustomer(Member customer) {
		try {
			System.out.println("updating customer: "+customer);
			CompasResponse response = memberBal.UpdateMember(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updAndCustomer")
	public Response UpdateAndCustomer(List<Member> customer) {
		try {
			CompasResponse response = memberBal.UpdateAndMember(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}

	


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMemberCa/")
	public Response GetCardActivationList() {
		try {
			List<Member> members = memberBal.GetCardActivationList();
			if (!(members == null)) {
				System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/gtMemberCb")
	public Response GetCardBlockList() {
		try {
			List<Member> members = memberBal.GetCardBlockList();
			if (!(members == null)) {
				System.out.println("memberlist: " + members.size());
				return Response.status(200).entity(members).build();
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
	@Path("/insertCardIssuance")
	public Response UpdateCardIssuance(MemberCard memCard) {
		CompasResponse response = memberBal.UpdateCardIssuance(memCard);
		return Response.status(200).entity(response).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updcardreissue")
	public Response updateCardReIssue(MemberCard memCard) {
		CompasResponse response = memberBal.updateCardReissue(memCard);
		return Response.status(200).entity(response).build();
	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCategoryByScheme/{schemeId}")
	public Response GetProgrammesByBnfGrp(@PathParam("schemeId") int schemeId) {
		try {
			List<Programme> programmes = memberBal.GetProgrammesByBnfId(schemeId);
			if (!(programmes == null)) {

				return Response.status(200).entity(programmes).build();
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
	@Path("/gtRelations/")
	public Response GetRelations() {
		List<Relations> relations = memberBal.GetRelations();
		return Response.status(200).entity(relations).build();
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updBnfStatus")
	public Response UpdateBnfStatus(Member customer) {
		try {
			CompasResponse response = memberBal.UpdateBnfStatus(customer);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}

	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSchemes/{orgId}")
	public Response GetSchemes(@PathParam("orgId") int orgId) {
		List<Product> relations = memberBal.GetScheme(orgId);
		return Response.status(200).entity(relations).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMemberDetachDetails/")
	public Response GetMemberDetachDetails(@QueryParam("memberNo") String memberNo) {
		try {
			Detach detach = memberBal.GetMemberDetachDetails(memberNo);
			if (!(detach == null)) {
				return Response.status(200).entity(detach).build();
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
	@Path("/detachBio")
	public Response DetachBio(Detach detach) {
		try {
			CompasResponse response = memberBal.DetachBio(detach);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	//deactivate member end-points
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deactivateMember")
	public Response DeactivateMember(Deactivate deactivate) {
		try {
			CompasResponse response = memberBal.DeactivateMember(deactivate);
			return Response.status(response.respCode).entity(response).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMemberDeactivationDetails/")
	public Response GetMemberDeactivationDetails(@QueryParam("memberNo") String memberNo) {
		try {
			Deactivate deactivate = memberBal.GetMemberDeactivationDetails(memberNo);
			if (!(deactivate == null)) {
				return Response.status(200).entity(deactivate).build();
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
	@Path("/gtDeactivationMemberBasicDetails/")
	public Response GetDeactivationMemberBasicDetails(@QueryParam("memberNo") String memberNo) {
		try {
			Member member = memberBal.GetDeactivationMemberBasicDetails(memberNo);
			if (!(member == null)) {
				return Response.status(200).entity(member).build();
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
	@Path("/gtMemberBasicDetails/")
	public Response GetMemberBasicDetails(@QueryParam("memberNo") String memberNo) {
		try {
			Member member = memberBal.GetMemberBasicDetails(memberNo);
			if (!(member == null)) {
				return Response.status(200).entity(member).build();
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
	@Path("/gtMemberProgrammes/")
	public Response GetMemberProgrammes(Member members) {
		try {
			List<ApiProgramme> member = memberBal.GetMemberProgrammes(members);
			if (!(member == null)) {
				return Response.status(200).entity(member).build();
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
	@Path("/gtMemberVouchers/")
	public Response GetMemberVouchers(Member members) {
		try {
			List<ApiVoucher> member = memberBal.GetMemberVouchers(members);
			if (!(member == null)) {
				return Response.status(200).entity(member).build();
			} else {
				return Response.status(201).entity(null).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(404).entity(null).build();
		}
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadmember")
	public Response uploadFile(
		@FormDataParam("file") InputStream uploadedInputStream, 
		@FormDataParam("file") FormDataContentDisposition fileDetail 
    )
	{
		 CompasResponse response = null;
		//boolean done = uploadmemberBal.addUploadclaimDocument("AKUH", file);
		  TOMCAT_HOME = System.getProperty("catalina.base");
		  //TOMCAT_HOME = "D:/lct_uploads";
            fileName = fileDetail.getFileName();
            PATH = TOMCAT_HOME+"/lct_uploads/members_uploads/";
            String uploadedFileLocation = PATH + fileName;
            System.out.println("TransactionSvc uploaded file path: "+uploadedFileLocation);
            File file = new File(uploadedFileLocation);
            try
            {
            	file.createNewFile();
            	System.out.println(file.getAbsolutePath());
            }catch(IOException e) { e.printStackTrace(); }
            // save it
            if(writeToFile(uploadedInputStream, uploadedFileLocation)){
                response = memberBal.UploadService(uploadedFileLocation,0,0,0,"");
                return Response.status(200).entity(new CompasResponse(200,"Filed uploading file success (?processing)")).build();
            } else{
                return Response.status(200).entity(new CompasResponse(201,"Error uploading file, Please try again")).build();
            }

	
	}
	
	// save uploaded file to new location
	private boolean writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		logger.info("#Writing to file function##");
	    try {
	        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
	        int read = 0;
	        byte[] bytes = new byte[1024];
	
	        out = new FileOutputStream(new File(uploadedFileLocation));
	        while ((read = uploadedInputStream.read(bytes)) != -1) {
	            out.write(bytes, 0, read);
	        }
	        out.flush();
	        out.close();
	
	        logger.info("##Sucessfully written to file");
	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	
	    logger.info("##Failed to write to file");
	    return false;
	}
}

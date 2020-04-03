/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

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

import com.compulynx.compas.bal.impl.ServiceBalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.SerSubtype;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.ServiceType;
import com.compulynx.compas.models.Uom;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author Anita
 *
 */
@Component
@Path("/service")
public class ServiceSvc {

	@Autowired
	ServiceBalImpl	serviceBal;
	private static final Logger logger = Logger.getLogger(ServiceSvc.class.getName());
	private String TOMCAT_HOME ="";
    private String fileName="";
    private String PATH = "";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtServices/{typeId}")
	public Response GetServices(@PathParam("typeId") String typeId) {
		List<Service> services = serviceBal.GetAllServices(typeId);
		return Response.status(200).entity(services).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSubServices/{serviceId}")
	public Response GetSubServices(@PathParam("serviceId") String serviceId) {
		System.out.println("serviceId: "+serviceId);
		int sid = Integer.parseInt(serviceId);
		List<Service> services = serviceBal.GetAllSubServices(sid);
		return Response.status(200).entity(services).build();
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updService")
	public Response UpdateService(Service service) {
		CompasResponse response = serviceBal.UpdateService(service);
		return Response.status(200).entity(response).build();
	}

	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtCurrencies/")
	public Response GetCurrencies() {
		List<Currency> types = serviceBal.GetCurrencies();
		return Response.status(200).entity(types).build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtUoms/")
	public Response GetUoms() {
		List<Uom> uoms = serviceBal.GetUoms();
		return Response.status(200).entity(uoms).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSerTypes/")
	public Response GetServiceTypes() {
		List<ServiceType> uoms = serviceBal.GetServiceTypes();
		return Response.status(200).entity(uoms).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtSerSubTypes/")
	public Response GetServiceSubTypes() {
		List<SerSubtype> uoms = serviceBal.GetServiceSubTypes();
		return Response.status(200).entity(uoms).build();
	}
	
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadservice")
	public Response uploadFile(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail 
    )
	{
		 CompasResponse response = null;
		//boolean done = uploadclaimBal.addUploadclaimDocument("AKUH", file);
		  TOMCAT_HOME = System.getProperty("catalina.base");
		  //TOMCAT_HOME = "D:/lct_uploads";
            fileName = fileDetail.getFileName();
            PATH = TOMCAT_HOME+"/lct_uploads/services_uploads/";
            String uploadedFileLocation = PATH + fileName;
            System.out.println("ServiceSvc uploaded file path: "+uploadedFileLocation);
            File file = new File(uploadedFileLocation);
            try
            {
            	file.createNewFile();
            	System.out.println(file.getAbsolutePath());
            }catch(IOException e) { e.printStackTrace(); }
            // save it
            if(writeToFile(uploadedInputStream, uploadedFileLocation)){
                response = serviceBal.UploadService(uploadedFileLocation);
                System.out.println("Server response: "+response.respCode+" : "+response.respMessage);
                Response resp = Response.status(200).entity(response).build();
                System.out.println("Response: "+resp);
                return resp;
                //Response.status(200).entity(new CompasResponse(200,"Filed uploading file success (?processing)")).build();
            } else{
            	Response resp = Response.status(201).entity(new CompasResponse(201,"Error uploading file, Please try again")).build();
            	  System.out.println("Response: "+resp);
                  return resp;
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

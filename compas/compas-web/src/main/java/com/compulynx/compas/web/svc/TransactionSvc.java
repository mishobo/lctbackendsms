/**
 * 
 */
package com.compulynx.compas.web.svc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.config.TxNamespaceHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.compulynx.compas.bal.TransactionBal;
import com.compulynx.compas.bal.UploadclaimBal;
//import com.compulynx.compas.bal.UploadOffLCTBal;
import com.compulynx.compas.dal.operations.Utility;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.AuthTransaction;
import com.compulynx.compas.models.CompasProperties;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.TransactionDtl;
import com.compulynx.compas.models.TransactionMst;
import com.compulynx.compas.models.Claim;
import com.compulynx.compas.models.OffLCT;
import com.compulynx.compas.models.ClaimReports;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author Anita
 *
 */
@Component
@Path("/transaction")
public class TransactionSvc {
	@Autowired
	TransactionBal transactionBal;
	@Autowired
	UploadclaimBal uploadclaimBal;

	private static final Logger logger = Logger.getLogger(TransactionSvc.class.getName());
	private String TOMCAT_HOME = "";
	private String fileName = "";
	private String PATH = "";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtActiveProviders")
	public Response GetActiveProviderList() {
		List<Merchant> providers = transactionBal.GetActiveProviderList();
		return Response.status(200).entity(providers).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getTransToVerify")
	public Response GetTransactionList(Transaction txn) {
		CompasResponse response = new CompasResponse();
		List<Transaction> translist = transactionBal.GetTransactionList(txn);
		return Response.status(200).entity(translist).build();
	}

	// Fetch AKUH Transactions to verify
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getAkuhTransToVerify")
	public Response GetAkuhTransactionList(Claim claimtxns) {
		// CompasResponse response=new CompasResponse();
		List<Claim> akuhtranslist = transactionBal.GetAkuhTransactionList(claimtxns);
		return Response.status(200).entity(akuhtranslist).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updTransStatus/")
	public Response updateTopupStatus(Transaction trans) {
		logger.info("Update Transaction with verify status");
		CompasResponse response = transactionBal.UpdateCardStatus(trans);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateTransStatus/")
	public Response updateTransStatus(Transaction trans) {
		logger.info("Update Transaction with verify status");
		CompasResponse response = transactionBal.UpdateTransStatus(trans);
		return Response.status(200).entity(response).build();
	}

	// update AKUH trans status
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateAkuhTransStatus")
	public Response updateAkuhTransStatus(Claim claimtrans) {
		logger.info("Update Claims Transaction with verify status");
		CompasResponse response = transactionBal.updateAkuhTransStatus(claimtrans);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtApprovedTrans")
	public Response GetApprovedTrans(Transaction txn) {
		List<Transaction> providers = transactionBal.GetApprovedTrans(txn);
		return Response.status(200).entity(providers).build();
	}

	// get approved transactions from AKUH
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtApprovedAkuhTrans")
	public Response GetApprovedClaimTrans(Claim claimtxn) {
		List<Claim> claimproviders = transactionBal.GetApprovedClaimTrans(claimtxn);
		return Response.status(200).entity(claimproviders).build();
	}

	@SuppressWarnings("unused")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtGenerateSettlementFile")
	public Response GetSettlementFile(Transaction txn) {
//		Utility compasProperties =new Utility();
		// compasProperties=transactionBal.get();
//		logger.info("Done Reading Props File##"+compasProperties.getCompasProperties());
//		if (compasProperties==null){
//			logger.info("##Failed to read properties file##");
//			return Response.status(Response.Status.OK).entity("").build();
//		}else{
		String TOMCAT_HOME = System.getProperty("catalina.base");
		System.out.println("This is the Catalina base for TOMCAT_HOME" + TOMCAT_HOME);
		logger.info("Acessing Catalina Base now...");
		String path = TOMCAT_HOME + "/lct_uploads/settlement/";
		CompasResponse response = transactionBal.GenerateSettlementFile(txn, path);
		return Response.status(Response.Status.OK).entity(response).build();
	}

	// generate AKUH settlement file:
	@SuppressWarnings("unused")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtGenerateClaimsSettlementFile")
	public Response GetClaimsSettlementFile(Claim claimtxn) {
		String TOMCAT_HOME = System.getProperty("catalina.base");
		System.out.println("This is the Catalina base for TOMCAT_HOME" + TOMCAT_HOME);
		logger.info("Acessing Catalina Base now...");
		String path = TOMCAT_HOME + "/lct_uploads/settlement/";
		CompasResponse response = transactionBal.GenerateClaimSettlementFile(claimtxn, path);
		return Response.status(Response.Status.OK).entity(response).build();
	}

	@GET
	@Path("/get_file/{file_name}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFile(@PathParam("file_name") String file_name) {
//		Utility compasProperties =new Utility();
//		String path = compasProperties.getCompasProperties().settleMentFilePath+file_name;
		String TOMCAT_HOME = System.getProperty("catalina.base");
		String path = TOMCAT_HOME + "/settlement/" + file_name;
		return download(path);
	}

	private Response download(String filePath) {
		String fileLocation = filePath;
		Response response = null;
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		// Retrieve the file
		File file = new File(fileLocation);
		if (file.exists()) {
			Response.ResponseBuilder builder = Response.ok(file);
			builder.header("Content-Disposition", "attachment; filename=" + file.getName());
			response = builder.build();
			long file_size = file.length();
			logger.info(String.format("Inside downloadFile ==> fileName: %s, fileSize: %s bytes", filePath,
					myFormat.format(file_size)));
		} else {
			logger.info(String.format("Inside downloadFile==> FILE NOT FOUND: fileName: %s", fileLocation));

			response = Response.status(404).entity("FILE NOT FOUND: " + fileLocation).type("text/plain").build();
		}

		return response;
	}

	@GET
	@Path("/get")
	@Produces("application/pdf")
	public Response getPdfFile() {
		File file = new File("C:\\Users\\user\\Downloads\\Cadets-4498-01_MINISTRY-OF-DEFENCE.pdf");
		System.out.print(file.getName());

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
		return response.build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAuthTransList/{orgId}")
	public Response GetAuthTransList(@PathParam("orgId") int orgId) {
		System.out.print("SVC: " + orgId);
		List<Transaction> providers = transactionBal.GetPendingAuth(orgId);
		return Response.status(200).entity(providers).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtAuthTransListByProvider/{providerId}")
	public Response GetAuthPendingTransByProvider(@PathParam("providerId") int providerId) {
		List<TransactionMst> agents = transactionBal.GetAuthPendingTransByProvider(providerId);
		return Response.status(200).entity(agents).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateAuthTrans")
	public Response UpdatePendingAuth(TransactionDtl txn) {
		CompasResponse response = transactionBal.UpdatePendingAuthStatus(txn);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/rejectAuthTrans")
	public Response UpdateRejectedAuth(TransactionDtl txn) {
		CompasResponse response = transactionBal.UpdateAuthRejectStatus(txn);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getUpdatedAuthTrans")
	public Response GetUpdatedAuthTrans(AuthTransaction trans) {
		System.out.println("Get Approved transactions");
		AuthTransaction response = transactionBal.GetUpdAuthTrans(trans);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllTxnDetail")
	public Response GetAllTxnsDetails(Reports rpt) {
		System.out.println("Get All transactions");
		List<Reports> response = transactionBal.GetAllTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	// SERVICE PROVIDERS PORTAL
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllServiceProvidersTxnDetail")
	public Response GetAllServiceProvidersTxnsDetails(Reports sprpt) {
		System.out.println("Get All Service Provider Transactions");
		List<Reports> response = transactionBal.GetAllServiceProvidersTxnsDetails(sprpt);
		return Response.status(200).entity(response).build();
	}

	// GERTRUDE ALL TRANSACTIONS REPORT
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllgTTxnDetail")
	public Response GetAllGtTxnsDetails(Reports gtrpt) {
		System.out.println("Get All GertRude transactions");
		List<Reports> response = transactionBal.GetAllGtTxnsDetails(gtrpt);
		return Response.status(200).entity(response).build();
	}

	// UHMC
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllUHMCTxnDetail")
	public Response GetAllDetailedUHMCHospTxn(Reports uhmcrpt) {
		System.out.println("Get All UHMC Hospital transactions");
		List<Reports> response = transactionBal.GetAllDetailedUHMCHospTxn(uhmcrpt);
		return Response.status(200).entity(response).build();
	}

	// CLINIX HEALTHCARE
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllClinixTxnDetail")
	public Response GetAllDetailedClinixTxn(Reports clinixrpt) {
		System.out.println("Get All Clinix Hospital transactions");
		List<Reports> response = transactionBal.GetAllDetailedClinixTxn(clinixrpt);
		return Response.status(200).entity(response).build();
	}

	// AKUH All Transactions Report
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllAkuhTxnDetail")
	public Response GetAllAkuhTxnsDetails(ClaimReports akuhrpt) {
		System.out.println("Get All AKUH claims transactions");
		List<ClaimReports> response = transactionBal.GetAllAkuhTxnsDetails(akuhrpt);
		return Response.status(200).entity(response).build();
	}

	// mpshah
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllMpshahTxnDetail")
	public Response GetAllMpshahTxnsDetails(Reports mpsharpt) {
		System.out.println("Get All MPSHAH claims transactions");
		List<Reports> response = transactionBal.GetAllMpshahTxnsDetails(mpsharpt);
		return Response.status(200).entity(response).build();
	}

	// nairobi Hosp
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAllNbiHospTxnDetail")
	public Response GetAllNbiTxnsDetails(Reports nbrpt) {
		System.out.println("Get All Nairobi Hospital transactions");
		List<Reports> response = transactionBal.GetAllNbiTxnsDetails(nbrpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtOutstandingTxnDetail")
	public Response GetOutstandingTxnsDetails(Reports rpt) {
		System.out.println("Get Out standing transactions");
		List<Reports> response = transactionBal.GetOutstandingTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtPendingTxnDetail")
	public Response GetPendingTxnsDetails(Reports rpt) {
		System.out.println("Get Pending transactions");
		List<Reports> response = transactionBal.GetPendingTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtUnpaidTxnDetail")
	public Response GetUnpaidTxnsDetails(Reports rpt) {
		System.out.println("Get Unpaid transactions");
		List<Reports> response = transactionBal.GetUnpaidTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtSettledTxnDetail")
	public Response GetSettledTxnsDetails(Reports rpt) {
		System.out.println("Get Settled transactions");
		List<Reports> response = transactionBal.GetSettledTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtRejectedTxnDetail")
	public Response GetRejectedTxnsDetails(Reports rpt) {
		System.out.println("Get Rejected transactions");
		List<Reports> response = transactionBal.GetRejectedTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtMemberTxnDetail")
	public Response GetMemberTxnsDetails(Reports rpt) {
		System.out.println("Get Member transactions");
		List<Reports> response = transactionBal.GetMemberTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtCancelledTxnDetail")
	public Response GetCancelledTxnsDetails(Reports rpt) {
		System.out.println("Get Cancelled transactions");
		List<Reports> response = transactionBal.GetCancelledTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtAuthorizedTxnDetail")
	public Response GetAuthorizedTxnsDetails(Reports rpt) {
		System.out.println("Get Authorized transactions");
		List<Reports> response = transactionBal.GetAuthorizedTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtProviderWiseTxnDetail")
	public Response GetProviderWiseTxnsDetails(Reports rpt) {
		System.out.println("Get ProviderWise transactions");
		List<Reports> response = transactionBal.GetProviderWiseTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtJDCProviderWiseTxnDetail")
	public Response GetJDCProviderWiseTxnsDetails(Reports rpt) {
		System.out.println("Get jdc ProviderWise transactions");
		List<Reports> response = transactionBal.GetJDCProviderWiseTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtSchemeWiseTxnDetail")
	public Response GetSchemeWiseTxnsDetails(Reports rpt) {
		System.out.println("Get SchemeWise transactions");
		List<Reports> response = transactionBal.GetSchemeWiseTxnsDetails(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/authorizeTransaction")
	public Response AuthorizeTransaction(Transaction txn) {
		CompasResponse response = transactionBal.AuthorizeTransaction(txn);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/gtProviderTrans")
	public Response GetProviderTrans(Reports rpt) {
		List<Reports> response = transactionBal.GetProviderTrans(rpt);
		return Response.status(200).entity(response).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadclaim")
	public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("orgId") String orgId, @FormDataParam("file") FormDataContentDisposition fileDetail) {
		CompasResponse response = null;
		// boolean done = uploadclaimBal.addUploadclaimDocument("AKUH", file);
		TOMCAT_HOME = System.getProperty("catalina.base");
		// TOMCAT_HOME = "D:/lct_uploads";
		fileName = fileDetail.getFileName();
		PATH = TOMCAT_HOME + "/lct_uploads/claims_uploads/";
		System.out.println("TOMCAT HOME IS: " + PATH);
		String uploadedFileLocation = PATH + fileName;
		System.out.println("TransactionSvc uploaded file path: " + uploadedFileLocation);
		File file = new File(uploadedFileLocation);
		try {
			file.createNewFile();
			System.out.println(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// save it
		if (writeToFile(uploadedInputStream, uploadedFileLocation)) {
			response = uploadclaimBal.UploadService(uploadedFileLocation);
			return Response.status(200).entity(response).build();
		} else {
			return Response.status(200).entity(new CompasResponse(201, "Error uploading file, Please try again"))
					.build();
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/gtMemberUtilizations")
	public Response GetAllDetailedMemberUtilizations() {
		List<Reports> providers = transactionBal.GetAllDetailedMemberUtilizations();
		return Response.status(200).entity(providers).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTotalUtilization")
	public Response GetAllDetailedTotalUtilizations() {
		List<Reports> providers = transactionBal.GetAllDetailedTotalUtilizations();
		return Response.status(200).entity(providers).build();
	}

// active JDC Members
	@GET
	@Path("/gtJDCMembers")
	public Response GetAllDetailedMemberReport() {
		System.out.println("Get All ACTIVE JDC Members");
		List<Reports> response = transactionBal.GetAllDetailedMemberReport();
		return Response.status(200).entity(response).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadOffLCT")
	public Response uploadOffLCTFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("orgId") String orgId, @FormDataParam("file") FormDataContentDisposition fileDetail) {
		CompasResponse response = null;
		// boolean done = uploadclaimBal.addUploadclaimDocument("AKUH", file);
		TOMCAT_HOME = System.getProperty("catalina.base");
		// TOMCAT_HOME = "D:/lct_uploads";
		fileName = fileDetail.getFileName();
		PATH = TOMCAT_HOME + "/lct_uploads/claims_uploads/";
		System.out.println("TOMCAT HOME IS: " + PATH);
		String uploadedFileLocation = PATH + fileName;
		System.out.println("TransactionSvc uploaded Off LCT path: " + uploadedFileLocation);
		File file = new File(uploadedFileLocation);
		try {
			file.createNewFile();
			System.out.println(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// save it
		if (writeToFile(uploadedInputStream, uploadedFileLocation)) {
			response = uploadclaimBal.UploadOFFLCTService(uploadedFileLocation);
			return Response.status(200).entity(response).build();
		} else {
			return Response.status(200).entity(new CompasResponse(201, "Error uploading file, Please try again"))
					.build();
		}

	}
	
	

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadSmartDebit")
	public Response uploadSmartFile(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("orgId") String orgId, @FormDataParam("file") FormDataContentDisposition fileDetail) {
		CompasResponse response = null;
		TOMCAT_HOME = System.getProperty("catalina.base");
		// TOMCAT_HOME = "D:/lct_uploads";
		fileName = fileDetail.getFileName();
		PATH = TOMCAT_HOME + "/lct_uploads/claims_uploads/";
		System.out.println("TOMCAT HOME IS: " + PATH);
		String uploadedFileLocation = PATH + fileName;
		System.out.println("TransactionSvc uploaded SMART - LCT Debit: " + uploadedFileLocation);
		File file = new File(uploadedFileLocation);
		try {
			file.createNewFile();
			System.out.println(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// save it
		if (writeToFile(uploadedInputStream, uploadedFileLocation)) {
			response = uploadclaimBal.UploadSmartClaimService(uploadedFileLocation);
			return Response.status(200).entity(response).build();
		} else {
			return Response.status(200).entity(new CompasResponse(201, "Error uploading file, Please try again"))
					.build();
		}

	}

}

package com.compulynx.compas.bal.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.compulynx.compas.bal.UploadclaimBal;
import com.compulynx.compas.dal.UploadclaimDal;
import com.compulynx.compas.models.Claim;
import com.compulynx.compas.models.CompasResponse;

@Component
public class UploadclaimBalmpl implements UploadclaimBal {
	@Autowired
	UploadclaimDal uploadclaimDal;
	private final String UPLOAD_DIR = "/uploads/uploadedclaims";
	@Override
	public boolean addUploadclaimDocument(String orgId, MultipartFile file) {
		return false;
		/*
        try 
        {
        	UploadclaimDocument doc = new UploadclaimDocument();
            doc.docName = file.getOriginalFilename();
			doc.file = file.getBytes();
			doc.orgId = orgId;
	        uploadclaimDal.addUploadclaimDocument(doc);
	        saveUploadedFile(file);
	        return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		*/
	}
	
	private String saveUploadedFile(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        uploadDir.mkdirs();
 
        StringBuilder sb = new StringBuilder();
 
        if(!file.isEmpty()) {
            String uploadFilePath = UPLOAD_DIR + "/" + file.getOriginalFilename();
 
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, bytes);
            sb.append(uploadFilePath).append(", ");
        }
        return sb.toString();
    }

	@Override
	 public CompasResponse UploadService(String filePath) {
		return uploadclaimDal.UploadClaim(filePath);
    }

	@Override
	public boolean addUploadOffLCTDocument(String orgId, MultipartFile file) {
		// TODO Auto-generated method stub
		return false;
		/*
        try 
        {
        	UploadclaimDocument doc = new UploadclaimDocument();
            doc.docName = file.getOriginalFilename();
			doc.file = file.getBytes();
			doc.orgId = orgId;
	        uploadclaimDal.addUploadclaimDocument(doc);
	        saveUploadedFile(file);
	        return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		*/
	}

	@Override
	public CompasResponse UploadOFFLCTService(String filePath) {
		// TODO Auto-generated method stub
		return uploadclaimDal.UploadOFFLCTClaim(filePath);
	}

	@Override
	public boolean addUploadSmartClaim(String orgId, MultipartFile file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CompasResponse UploadSmartClaimService(String filePath) {
		// TODO Auto-generated method stub
		return uploadclaimDal.UploadSmartClaim(filePath);
	}

}

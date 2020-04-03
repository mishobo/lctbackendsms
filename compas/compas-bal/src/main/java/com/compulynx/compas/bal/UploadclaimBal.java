package com.compulynx.compas.bal;

import org.springframework.web.multipart.MultipartFile;

import com.compulynx.compas.models.CompasResponse;

public interface UploadclaimBal {
	public boolean addUploadclaimDocument(String orgId, MultipartFile file);

	public CompasResponse UploadService(String filePath);

	// OFF LCT
	public boolean addUploadOffLCTDocument(String orgId, MultipartFile file);

	public CompasResponse UploadOFFLCTService(String filePath);

	// SMART DEBIT
	public boolean addUploadSmartClaim(String orgId, MultipartFile file);
	public CompasResponse UploadSmartClaimService(String filePath);
}

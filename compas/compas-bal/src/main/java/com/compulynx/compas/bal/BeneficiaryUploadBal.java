/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;

/**
 * @author Anita
 * Aug 11, 2016
 */
public interface BeneficiaryUploadBal {
	CompasResponse UploadAccount(String filePath, int orgId, int productId, int programmeId, String uploadedBy);
	CompasResponse UploadService(String filePath,String uploadedBy);
}

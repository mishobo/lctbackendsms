/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;

/**
 * @author Anita
 *Aug 11, 2016
 */
public interface BeneficicaryUploadDal {
	CompasResponse UploadAccount(String filePath, int orgId, int productId, int programmeId, String uploadedBy);
	CompasResponse UploadService(String filePath, String uploadedBy);
}

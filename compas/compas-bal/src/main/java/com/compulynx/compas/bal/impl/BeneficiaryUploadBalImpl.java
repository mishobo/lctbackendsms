/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.BeneficiaryUploadBal;
import com.compulynx.compas.dal.impl.BeneficiaryUploadDalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;

/**
 * @author Anita
 * Aug 11, 2016
 */
@Component
public class BeneficiaryUploadBalImpl implements BeneficiaryUploadBal{
	@Autowired
	BeneficiaryUploadDalImpl bnfUploadDal;
	  @Override
	    public CompasResponse UploadAccount(String filePath, int orgId, int productId, int programmeId, String uploadedBy) {
	        return bnfUploadDal.UploadAccount(filePath, orgId, productId, programmeId, uploadedBy);
	    }
	  
	    public CompasResponse UploadService(String filePath, String uploadedBy) {
	        return bnfUploadDal.UploadService(filePath, uploadedBy);
	    }

}

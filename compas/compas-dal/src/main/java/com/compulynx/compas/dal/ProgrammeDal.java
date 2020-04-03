/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author shree
 *
 */
public interface ProgrammeDal {
	boolean checkProgrammeName(String programmeDesc, int orgId);

	CompasResponse UpdateProgramme(Programme programme);

	List<Programme> GetAllProgrammes(int orgId);

	Programme GetProgrammeById(int programmeId);
	List<BeneficiaryGroup> GetCoverTypesById(int schemeId);
	
	List<Programme> GetProgrammesByScheme(Programme programme);


}

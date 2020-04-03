/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;











import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.ProgrammeBal;
import com.compulynx.compas.dal.impl.ProgrammeDalImpl;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
@Component
public class ProgrammeBalImpl implements ProgrammeBal{

	@Autowired
	ProgrammeDalImpl programmeDal;
	
	public CompasResponse UpdateProgramme(Programme programme) {
		return programmeDal.UpdateProgramme(programme);
	}


	public List<Programme> GetAllProgrammes(int orgId) {
		return programmeDal.GetAllProgrammes(orgId);
	}


	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ProgrammeBal#GetCoverTypesById(int)
	 */
	@Override
	public List<BeneficiaryGroup> GetCoverTypesById(int schemeId) {
		// TODO Auto-generated method stub
		return programmeDal.GetCoverTypesById(schemeId);
	}
	
	public List<Programme> GetProgrammesByScheme(Programme programme) {
		return programmeDal.GetProgrammesByScheme(programme);
	}

}

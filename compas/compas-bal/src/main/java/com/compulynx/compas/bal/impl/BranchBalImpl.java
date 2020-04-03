/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.BranchBal;
import com.compulynx.compas.dal.impl.BranchDalImpl;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Company;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
@Component
public class BranchBalImpl implements BranchBal{

	@Autowired
	BranchDalImpl branchDal;
	

	


	public CompasResponse UpdateBranch(Branch branch) {
		// TODO Auto-generated method stub
		return branchDal.UpdateBranch(branch);
	}

	public List<Branch> GetAllBranches() {
		// TODO Auto-generated method stub
		return branchDal.GetAllBranches();
	}

	
	
	
	public List<Company> GetAllCompanies() {
		// TODO Auto-generated method stub
		return branchDal.GetAllCompanies();
	}

}

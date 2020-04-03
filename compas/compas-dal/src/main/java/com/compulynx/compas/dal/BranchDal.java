/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Company;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
public interface BranchDal {
	
	CompasResponse UpdateBranch(Branch branch);

	List<Branch> GetAllBranches();

	List<Company> GetAllCompanies();
}

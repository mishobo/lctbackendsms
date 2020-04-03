package com.compulynx.compas.bal.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.OrganizationBal;
import com.compulynx.compas.dal.impl.OrganizationDalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Organization;

@Component
public class OrganizationBalImpl implements OrganizationBal {
	
	@Autowired
	OrganizationDalImpl organizationDal;
	
	public List<Organization> GetOrganizations() {
		return organizationDal.GetOrganizations();
	}

	public CompasResponse UpdateOrganization(Organization organization) {
		return organizationDal.UpdateOrganization(organization);
	}

	

	
}

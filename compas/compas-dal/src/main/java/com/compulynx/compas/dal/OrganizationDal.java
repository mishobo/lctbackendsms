package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Organization;
import com.compulynx.compas.models.CompasResponse;



public interface OrganizationDal {
	CompasResponse UpdateOrganization(Organization organization);

	boolean GetOrganizationByName(String brokerName);

	List<Organization> GetOrganizations();

}

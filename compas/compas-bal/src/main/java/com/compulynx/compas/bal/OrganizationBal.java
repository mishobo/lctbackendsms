package com.compulynx.compas.bal;

import com.compulynx.compas.models.Organization;
import com.compulynx.compas.models.CompasResponse;

import java.util.List;

public interface OrganizationBal {
	List<Organization> GetOrganizations();
	
	CompasResponse UpdateOrganization (Organization organozation);
}

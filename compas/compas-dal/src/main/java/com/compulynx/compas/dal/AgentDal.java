/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Agent;

/**
 * @author Anita
 *
 */
public interface AgentDal {
	
	CompasResponse UpdateAgent(Agent agent);

	List<Agent> GetAllAgents();

	List<Branch> GetBranchesByMerchant(int merchantId);
}

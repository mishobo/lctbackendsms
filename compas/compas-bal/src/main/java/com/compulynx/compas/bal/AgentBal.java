/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;


/**
 * @author Anita
 *
 */
public interface AgentBal {


	CompasResponse UpdateAgent(Agent agent);

	List<Agent> GetAllAgents();
	List<Branch> GetBranchesByMerchant(int merchantId);
	List<Location> GetLocationsByMerchant(int merchantId);
	List<Agent> GetAgentsByMerchant(String merchant);
}

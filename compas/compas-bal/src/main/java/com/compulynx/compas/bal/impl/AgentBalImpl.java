/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.AgentBal;
import com.compulynx.compas.dal.impl.AgentDalImpl;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;

/**
 * @author shree
 *
 */
@Component
public class AgentBalImpl implements AgentBal{
	@Autowired
	AgentDalImpl agentDal;


	public CompasResponse UpdateAgent(Agent agent) {
		// TODO Auto-generated method stub
		return agentDal.UpdateAgent(agent);
	}


	public List<Agent> GetAllAgents() {
		// TODO Auto-generated method stub
		return agentDal.GetAllAgents();
	}


	@Override
	public List<Branch> GetBranchesByMerchant(int merchantId) {
		// TODO Auto-generated method stub
		return agentDal.GetBranchesByMerchant(merchantId);
	}


	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.AgentBal#GetLocationsByMerchant(int)
	 */
	@Override
	public List<Location> GetLocationsByMerchant(int merchantId) {
		// TODO Auto-generated method stub
		return agentDal.GetLocationsByMerchant(merchantId);
	}


	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.AgentBal#GetAgentsByMerchant(java.lang.String)
	 */
	@Override
	public List<Agent> GetAgentsByMerchant(String merchant) {
		// TODO Auto-generated method stub
		return agentDal.GetAgentsByMerchant(merchant);
	}

}

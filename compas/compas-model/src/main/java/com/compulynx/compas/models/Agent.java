/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Agent {

	public int agentId;
	public String agentCode;
	public String agentDesc;
	public int merchantId;
	public String branchName;
	public boolean active;
	public int createdBy;
	public int respCode;
	public int branchId;
	public int cycle;
	public int bnfGrpId;

	
	public Agent(int agentId, String agentDesc, int branchId, boolean active,
			int createdBy, int respCode) {
		super();
		this.agentId = agentId;
		this.agentDesc = agentDesc;
		this.merchantId = branchId;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
	}

	public Agent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Programme> programmes;
	public int	count;
	public int locationId;
	public String locationName;
}

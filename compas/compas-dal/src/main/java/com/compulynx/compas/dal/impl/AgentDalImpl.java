/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.AgentDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Location;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;

/**
 * @author Anita
 *
 */
public class AgentDalImpl implements AgentDal{

	private DataSource dataSource;

	public AgentDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	

	public boolean CheckAgentName(String agentDesc,int branchId,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentByName);
			preparedStatement.setString(1, agentDesc);
			preparedStatement.setInt(2, branchId);
			preparedStatement.setInt(3, merchantId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckAgentByCode(String agentCode,int branchId,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentByName);
			preparedStatement.setString(1, agentCode);
			preparedStatement.setInt(2, branchId);
			preparedStatement.setInt(3, merchantId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckAgentNameByCode(String agentDesc,String agentCode,int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentNameByCode);
			preparedStatement.setString(1, agentDesc);
			preparedStatement.setString(2, agentCode);
			preparedStatement.setInt(3, branchId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateAgent(Agent agent) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int agentId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		
			if (agent.agentId == 0) {
				if (CheckAgentByCode(agent.agentCode, agent.locationId,agent.merchantId)) {
					return new CompasResponse(201, "Agent Code Already Exists");
				}
				if (CheckAgentName(agent.agentDesc, agent.locationId,agent.merchantId)) {
					return new CompasResponse(201, "Agent Name Already Exists");
				}

				preparedStatement = connection.prepareStatement(
						Queryconstants.insertAgentDetails,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, agent.agentDesc);
				preparedStatement.setInt(2,agent.merchantId);
				preparedStatement.setBoolean(3, agent.active);
				preparedStatement.setInt(4, agent.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(6, agent.agentCode);
				preparedStatement.setInt(7, agent.locationId);
			} else {
				if (CheckAgentNameByCode(agent.agentDesc,agent.agentCode, agent.locationId)) {
					return new CompasResponse(201, "Agent name already exists for specified location");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateAgentDetails);
				preparedStatement.setString(1, agent.agentDesc);
				preparedStatement.setInt(2,agent.merchantId);
				preparedStatement.setBoolean(3, agent.active);
				preparedStatement.setInt(4, agent.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, agent.locationId);
				preparedStatement.setInt(7, agent.agentId);
				
				agentId=agent.agentId;
			}
			if(preparedStatement.executeUpdate()>0)
			{
				// Dispose

				connection.commit();
				return new CompasResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update");
			
			}
	
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Agent Name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Agent> GetAllAgents() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgents);

			
			resultSet = preparedStatement.executeQuery();
			List<Agent> objAgents = new ArrayList<Agent>();
			while (resultSet.next()) {
				Agent agent= new Agent();
				agent.agentId=resultSet.getInt("ID");
				agent.agentCode=resultSet.getString("Agent_Code");
				agent.agentDesc=resultSet.getString("AgentDesc");
				agent.merchantId=resultSet.getInt("merchantId");
				agent.locationId=resultSet.getInt("locationid");
				agent.locationName=resultSet.getString("location_name");
				agent.active=resultSet.getBoolean("active");
				agent.count=count;
				//agent.createdBy=resultSet.getInt("CreatedBy");
//				preparedStatement = connection
//						.prepareStatement(Queryconstants.getProgrammeByAgentId);	
//				preparedStatement.setInt(1, agent.agentId);
//				preparedStatement.setInt(2, agent.agentId);
//				resultSet2=preparedStatement.executeQuery();
//				List<Programme> programme= new ArrayList<Programme>();
//				while (resultSet2.next()) {
//					programme.add(new Programme(resultSet2.getInt("ProgrammeId"),resultSet2.getString("ProgrammeDesc"),resultSet2.getBoolean("Isactive"),
//							resultSet2.getInt("CreatedBY"),200,resultSet2.getDouble("ProgrammeValue"),resultSet2.getString("Currency")));
//				}
//				agent.programmes=programme;
				objAgents.add(agent);
				count++;
			}
			return objAgents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Branch> GetBranchesByMerchant(int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchesByMerchant);
			preparedStatement.setInt(1, merchantId);
			
			resultSet = preparedStatement.executeQuery();
			List<Branch> objBranch = new ArrayList<Branch>();
			while (resultSet.next()) {
				Branch branch= new Branch();
				branch.branchId=resultSet.getInt("Id");
				branch.branchName=resultSet.getString("branchName");
				branch.active=resultSet.getBoolean("active");
				objBranch.add(branch);
			}
			return objBranch;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Location> GetLocationsByMerchant(int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLocationByMerchant);
			preparedStatement.setInt(1, merchantId);
			
			resultSet = preparedStatement.executeQuery();
			List<Location> locations = new ArrayList<Location>();
			while (resultSet.next()) {
				Location location= new Location();
				location.locationId=resultSet.getInt("Id");
				location.locationName=resultSet.getString("location_Name");
				locations.add(location);
			}
			return locations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Agent> GetAgentsByMerchant(String merchant) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentsByMerchant);
			preparedStatement.setString(1, merchant);
			
			resultSet = preparedStatement.executeQuery();
			List<Agent> objAgents = new ArrayList<Agent>();
			while (resultSet.next()) {
				Agent agent= new Agent();
				agent.agentId=resultSet.getInt("ID");
				agent.agentCode=resultSet.getString("Agent_Code");
				agent.agentDesc=resultSet.getString("AgentDesc");
				agent.merchantId=resultSet.getInt("merchantId");
				agent.locationId=resultSet.getInt("locationid");
				agent.locationName=resultSet.getString("location_name");
				agent.active=resultSet.getBoolean("active");
				agent.count=count;

				objAgents.add(agent);
				count++;
			}
			return objAgents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}

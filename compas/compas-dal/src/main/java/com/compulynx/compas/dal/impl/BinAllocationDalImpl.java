/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.compulynx.compas.dal.BinAllocationDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.BinAllocation;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author shree
 *
 */
public class BinAllocationDalImpl implements BinAllocationDal{


	private DataSource dataSource;

	public BinAllocationDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	public CompasResponse UpdateBin(BinAllocation bin) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int agentId = 0;
		try {
			connection = dataSource.getConnection();
			//connection.setAutoCommit(false);
		
			//if (bin.agentId == 0) {
			if (checkBinRange(bin.binRange)) {
				return new CompasResponse(201, "Bin range is already exists");
			}
				if (checkBinRangeProgramme(bin.programmeId)) {
					return new CompasResponse(201, "Bin is already Allocated to the selected programme");
				}

				preparedStatement = connection.prepareStatement(
						Queryconstants.insertBinRange);
				preparedStatement.setString(1, bin.binRange);
				preparedStatement.setInt(2,bin.programmeId);
			
				preparedStatement.setInt(3, bin.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			if(preparedStatement.executeUpdate()>0){
				return new  CompasResponse(200, "Record updated successfully");
			}
			else{
				return new  CompasResponse(201, "Failed to update details");
			}
	
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Bin range Already Exists");
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
	public boolean checkBinRangeProgramme(int programmeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkBinRangeProgramme);
			//preparedStatement.setString(1, agentDesc);
			preparedStatement.setInt(1, programmeId);
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
	public boolean checkBinRange(String binRange) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkBinRange);
			//preparedStatement.setString(1, agentDesc);
			preparedStatement.setString(1, binRange);
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
}

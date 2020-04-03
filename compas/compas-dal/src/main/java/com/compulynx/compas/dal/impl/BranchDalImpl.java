package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.BranchDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Company;
import com.compulynx.compas.models.CompasResponse;

public class BranchDalImpl implements BranchDal{

	private DataSource dataSource;

	public BranchDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean CheckBranchCode(String branchCode,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchByCode);
			preparedStatement.setString(1, branchCode);
			preparedStatement.setInt(2, merchantId);
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
	public boolean CheckBranchName(String branchName,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchByName);
			preparedStatement.setString(1, branchName);
			preparedStatement.setInt(2, merchantId);
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
	public boolean CheckBranchNameByCode(String branchName,String branchCode,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchNameByCode);
			preparedStatement.setString(1, branchName);
			preparedStatement.setString(2, branchCode);
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

	public CompasResponse UpdateBranch(Branch branch) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (branch.branchId == 0) {
				if (CheckBranchCode(branch.branchCode,branch.merchantId)) {
					return new CompasResponse(201, "Branch Code Already Exists");
				}
				if (CheckBranchName(branch.branchName,branch.merchantId)) {
					return new CompasResponse(201, "Branch Name Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertBranches);
				preparedStatement.setString(1, branch.branchCode);
				preparedStatement.setString(2, branch.branchName);
				preparedStatement.setInt(3, branch.merchantId);
				preparedStatement.setBoolean(4, branch.active);
				preparedStatement.setInt(5, branch.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(7, branch.locationId);
			} else {
				if (CheckBranchNameByCode(branch.branchName,branch.branchCode,branch.merchantId)) {
					return new CompasResponse(201, "Branch Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateBranches);
				preparedStatement.setString(1, branch.branchName);
				preparedStatement.setInt(2, branch.merchantId);
				preparedStatement.setBoolean(3, branch.active);
				preparedStatement.setInt(4, branch.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, branch.locationId);
				preparedStatement.setInt(7, branch.branchId);
				
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Branch Name Already Exists");
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


	public List<Branch> GetAllBranches() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranches);
			//preparedStatement.setInt(1, classId);
			//preparedStatement.setInt(2, merchantId);
			resultSet = preparedStatement.executeQuery();
			List<Branch> branches = new ArrayList<Branch>();
			while (resultSet.next()) {
				branches.add(new Branch(resultSet.getInt("ID"), resultSet
						.getString("BranchCode"), resultSet
						.getString("BranchName"), resultSet
						.getInt("MerchantID"),resultSet
						.getInt("classId"), resultSet
						.getBoolean("active"), resultSet.getInt("CreatedBy"),
						200,resultSet
						.getInt("locationid"),resultSet
						.getString("location_name"),resultSet
						.getString("mer_name"),count));
				count++;
			}
			return branches;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


	public Branch GetBranchById(int branchId) {
		// TODO Auto-generated method stub
		return null;
	}


	
	public List<Company> GetAllCompanies() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCompanies);

			resultSet = preparedStatement.executeQuery();
			List<Company> company = new ArrayList<Company>();
			while (resultSet.next()) {
				company.add(new Company(resultSet.getInt("ID"),resultSet
						.getString("CompanyCode"),resultSet
						.getString("CompanyName"), resultSet.getInt("CreatedBy"),
						200));
			}
			return company;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	
}

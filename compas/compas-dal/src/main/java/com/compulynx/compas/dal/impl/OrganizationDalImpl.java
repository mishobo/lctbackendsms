package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.sql.DataSource;

import com.compulynx.compas.dal.OrganizationDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Organization;
import com.compulynx.compas.models.CompasResponse;

public class OrganizationDalImpl implements OrganizationDal {

	private DataSource dataSource;

	public OrganizationDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public CompasResponse UpdateOrganization(Organization organization) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (organization.orgId == 0) {
				if (GetOrganizationByName(organization.orgName)) {
					return new CompasResponse(201,	"Organization Name Already Exists");
				}
				else {
					preparedStatement = connection.prepareStatement(Queryconstants.insertOrganization);
					preparedStatement.setString(1, organization.orgCode);
					preparedStatement.setString(2, organization.orgName);
					preparedStatement.setString(3, organization.orgContactPer);
					preparedStatement.setString(4, organization.orgContactNo);
					preparedStatement.setString(5, organization.orgEmail);
					preparedStatement.setBoolean(6, true);
					preparedStatement.setInt(7, organization.createdBy);
					preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
				}
			} 
			else {
				preparedStatement = connection.prepareStatement(Queryconstants.updateOrganization);
				preparedStatement.setString(1, organization.orgName);
				preparedStatement.setString(2, organization.orgContactPer);
				preparedStatement.setString(3, organization.orgContactNo);
				preparedStatement.setString(4, organization.orgEmail);

				preparedStatement.setInt(5, organization.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(7, organization.orgId);
			}
			if(preparedStatement.executeUpdate() > 0){
				//Create user for organization
//				preparedStatement1 = connection.prepareStatement(Queryconstants.insertUser);
//				preparedStatement1.setString(1, organization.orgName+".admin");
//				preparedStatement1.setString(2, organization.orgName);
//				preparedStatement1.setString(3, AES.encrypt("5555"));
//				preparedStatement1.setInt(4, 3);
//				preparedStatement1.setString(5,organization.orgEmail);
//				preparedStatement1.setString(6, organization.orgContactNo);
//				preparedStatement1.setInt(7, 0);
//				preparedStatement1.setString(8, "");
//				preparedStatement1.setBoolean(9, false);
//				preparedStatement1.setLong(10, 0);
//				preparedStatement1.setLong(11, 0);
//				preparedStatement1.setBoolean(12, true);
//				preparedStatement1.setInt(13, organization.createdBy);
//				preparedStatement1.setTimestamp(14, new java.sql.Timestamp(new java.util.Date().getTime()));
//				preparedStatement1.setInt(15, 0);
//				preparedStatement1.setInt(16,0);
//				preparedStatement1.setInt(17,2);
//				if (organization.orgId == 0) {
//					return (preparedStatement1.executeUpdate() > 0) ? new CompasResponse(200, "Records Updated") : new CompasResponse(201,"Nothing To Update");
//				}else{
//					return  new CompasResponse(200,	"Records Updated");
//				}
				return  new CompasResponse(200,	"Records Updated");
				
			}
			else {
				connection.rollback();
				return  new CompasResponse(201,	"Nothing To Update");
			}
			
		
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201,"Organization Name Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			DBOperations.DisposeSql(preparedStatement1);
		}
	}

	public boolean GetOrganizationByName(String organizationName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getOrganizationByName);
			preparedStatement.setString(1, organizationName);

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

	public List<Organization> GetOrganizations() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getOrganizations);
			resultSet = preparedStatement.executeQuery();
			List<Organization> brokers = new ArrayList<Organization>();
			while (resultSet.next()) {
				brokers.add(new Organization(resultSet.getInt("ID"), resultSet
						.getString("Org_code"),
						resultSet.getString("Org_Name"), resultSet
								.getString("contact_per"), resultSet
								.getString("contact_no"), resultSet
								.getString("email"), 200));
			}
			return brokers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	
}

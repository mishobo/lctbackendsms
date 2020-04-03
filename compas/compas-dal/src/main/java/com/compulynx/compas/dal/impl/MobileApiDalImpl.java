/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.compulynx.compas.dal.MobileApiDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.POSUser;
import com.compulynx.compas.models.android.APIPOSUserAuth;

/**
 * @author Anita Mar 16, 2017
 */
public class MobileApiDalImpl implements MobileApiDal {
	private DataSource dataSource;
	private static final Logger logger = Logger.getLogger(MobileApiDalImpl.class.getName());

	public MobileApiDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	
	}

	// change to DownloadVo or class extending user with device uid
	public boolean loginPOSUser(APIPOSUserAuth user) {
		boolean done = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		// PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		// ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.loginMerchantPOSUser);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, AES.encrypt(user.getPassword()));
			preparedStatement.setString(3, user.getDeviceSerialNo());
			System.out.println("Logging in POS user password " + AES.encrypt(user.getPassword()));
			resultSet = preparedStatement.executeQuery();
			// Insert Audit Trail
			preparedStatement1 = connection.prepareStatement(Queryconstants.insertAuditTrail);
			preparedStatement1.setInt(1, user.getId());
			preparedStatement1.setString(2, user.getUsername());
			preparedStatement1.setString(3, "Logged in");
			preparedStatement1.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
			preparedStatement1.setString(5, "POS Device User Authentication Module");
			if (preparedStatement1.executeUpdate() <= 0) {
				connection.rollback();
				return true;
			}
			logger.info("Audit Trail for " + user.getUsername() + " on device login captured");
			while (resultSet.next()) {
				done = true;
			}

			return done;

			/*
			 * preparedStatement =
			 * connection.prepareStatement(Queryconstants.loginMerchantPOSUser);
			 * preparedStatement.setString(1, user.getUsername());
			 * preparedStatement.setString(2, user.getPassword());
			 * preparedStatement.setString(3, user.getMerchantCode());
			 * resultSet=preparedStatement.executeQuery(); //password correct if
			 * (resultSet.next()) { preparedStatement2 =
			 * connection.prepareStatement(Queryconstants.getMerchantByCode);
			 * preparedStatement.setString(1, user.getMerchantCode());
			 * resultSet2=preparedStatement2.executeQuery(); if (resultSet2.next()) { int
			 * merchantId=resultSet2.getInt("id"); preparedStatement3 =
			 * connection.prepareStatement(Queryconstants.getMerchantByCode);
			 * 
			 * }
			 * 
			 * } else { done=false; }
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}

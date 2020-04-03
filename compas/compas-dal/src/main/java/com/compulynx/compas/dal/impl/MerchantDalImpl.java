/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.compulynx.compas.dal.MerchantDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.POSUser;
import java.util.logging.Logger;

/**
 * @author ANita
 *
 */
public class MerchantDalImpl implements MerchantDal {
	private static final Logger logger = Logger.getLogger(MerchantDalImpl.class.getName());
	private DataSource dataSource;

	public MerchantDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public CompasResponse UpdateMerchant(Merchant merchant) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement14 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (merchant.merchantId == 0) {
				if (CheckMerchantByName(merchant.merchantName)) {
					return new CompasResponse(201, "Service Provider Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertMerchant);
				preparedStatement.setString(1, genRandInt());
				preparedStatement.setString(2, merchant.merchantName);
				preparedStatement.setString(3, merchant.merCnctPer);
				preparedStatement.setString(4, merchant.merCnctNo);
				preparedStatement.setString(5, merchant.merEmail);
				preparedStatement.setString(6, merchant.merAdd);
				preparedStatement.setString(7, merchant.currCode);
				preparedStatement.setBoolean(8, merchant.active);
				preparedStatement.setInt(9, merchant.createdBy);
				preparedStatement.setTimestamp(10, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(11, merchant.accNumber);
				preparedStatement.setString(12, merchant.accName);
				preparedStatement.setString(13, merchant.bankCode);
				preparedStatement.setString(14, merchant.bankName);
				preparedStatement.setString(15, merchant.logo);
				preparedStatement.setBoolean(16, merchant.hasHmsi);
				preparedStatement.setString(17, merchant.stageServer);

			} else {
				if (CheckMerchantNameById(merchant.merchantName, merchant.merchantId)) {
					return new CompasResponse(201, "Service Provider Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.updateMerchant);
				preparedStatement.setString(1, merchant.merchantCode);
				preparedStatement.setString(2, merchant.merchantName);
				preparedStatement.setString(3, merchant.merCnctPer);
				preparedStatement.setString(4, merchant.merCnctNo);
				preparedStatement.setString(5, merchant.merEmail);
				preparedStatement.setString(6, merchant.merAdd);
				preparedStatement.setString(7, merchant.currCode);
				preparedStatement.setBoolean(8, merchant.active);
				preparedStatement.setInt(9, merchant.createdBy);
				preparedStatement.setTimestamp(10, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(11, merchant.accNumber);
				preparedStatement.setString(12, merchant.accName);
				preparedStatement.setString(13, merchant.bankCode);
				preparedStatement.setString(14, merchant.bankName);
				preparedStatement.setString(15, merchant.logo);
				preparedStatement.setBoolean(16, merchant.hasHmsi);
				preparedStatement.setString(17, merchant.stageServer);
				preparedStatement.setInt(18, merchant.merchantId);
			}
			
			if (preparedStatement.executeUpdate() > 0) {

				// insert audit trail
				preparedStatement14 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement14.setInt(1, merchant.createdBy);
				preparedStatement14.setInt(2, merchant.createdBy);
				preparedStatement14.setString(3, "Created Service Provider : " + merchant.merchantName);
				preparedStatement14.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement14.setString(5, "LCT Service Provider Module");
				if (preparedStatement14.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(200, "Failed to add audit trail");
				}
				logger.info("Audit Trail for " + merchant.merchantName + "on service provider module captured");

				return new CompasResponse(200, "Records Updated");

			} else {
				return new CompasResponse(201, "Nothing To Update");
			}
			
//			
//			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(200, "Records Updated")
//					: new CompasResponse(201, "Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new CompasResponse(404, "Exception Occured");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckMerchantByCode(String merchantCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantByCode);
			preparedStatement.setString(1, merchantCode);
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

	public boolean CheckMerchantByName(String merchantName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantByName);
			preparedStatement.setString(1, merchantName);
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

	public boolean CheckMerchantNameByCode(String merchantName, String merchantCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantNameByCode);
			preparedStatement.setString(1, merchantName);
			preparedStatement.setString(2, merchantCode);
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

	public List<Merchant> GetMerchants() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchants);
//			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantsByUserName);
			resultSet = preparedStatement.executeQuery();
			List<Merchant> merchants = new ArrayList<Merchant>();
			while (resultSet.next()) {
				Merchant merchant = new Merchant();
				merchant.merchantId = resultSet.getInt("ID");
				merchant.merchantCode = "SP" + resultSet.getString("ID");
				merchant.merchantName = resultSet.getString("mer_Name");
				merchant.merCnctPer = resultSet.getString("contact_per");
				merchant.merCnctNo = resultSet.getString("contact_no");
				merchant.merEmail = resultSet.getString("email");
				merchant.merAdd = resultSet.getString("mer_add");
				merchant.currCode = resultSet.getString("curr_code");
				merchant.active = resultSet.getBoolean("Active");
				merchant.respCode = 200;
				merchant.count = count;
				merchant.accNumber = resultSet.getString("acc_number");
				merchant.accName = resultSet.getString("acc_name");
				merchant.bankCode = resultSet.getString("bank_code");
				merchant.bankName = resultSet.getString("bank_name");
				merchant.hasHmsi = resultSet.getBoolean("has_hmsi");
				merchant.stageServer = resultSet.getString("stage_server");
				count++;
				merchants.add(merchant);
				// System.out.println("Merchants:" +Arrays.toString(merchants.toArray()));
			}
			return merchants;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Merchant getMerchantById(int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantById);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			Merchant merchant = new Merchant();
			while (resultSet.next()) {
				merchant.merchantId = resultSet.getInt("ID");
				System.out.println("merchant id is: " +merchant.merchantId);
				merchant.merchantCode = resultSet.getString("mer_code");
				merchant.merchantName = resultSet.getString("mer_Name");
				System.out.println("merchant name is: " +merchant.merchantName);
				merchant.merCnctPer = resultSet.getString("contact_per");
				merchant.merCnctNo = resultSet.getString("contact_no");
				merchant.merEmail = resultSet.getString("email");
				merchant.merAdd = resultSet.getString("mer_add");
				merchant.currCode = resultSet.getString("curr_code");
				merchant.active = resultSet.getBoolean("Active");
				merchant.respCode = 200;
				merchant.count = count;
				merchant.accNumber = resultSet.getString("acc_number");
				merchant.accName = resultSet.getString("acc_name");
				merchant.bankCode = resultSet.getString("bank_code");
				merchant.bankName = resultSet.getString("bank_name");
				merchant.hasHmsi = resultSet.getBoolean("has_hmsi");
				merchant.stageServer = resultSet.getString("stage_server");
				count++;
			}
			return merchant;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public String genRandInt() {
		return "SP" + (int) (Math.random() * 899);
	}

	public boolean CheckMerchantNameById(String merchantName, int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantNameById);
			preparedStatement.setString(1, merchantName);
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

	@Override
	public boolean addMerchantPOSUser(POSUser posUser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int affectedRows = 0;
		try {
			connection = dataSource.getConnection();
			if (posUser.getId() == 0) {
				if (CheckPOSUserEmailById(posUser.getEmail(), posUser.getId())) {
					return false;
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertMerchantPOSUser);
				preparedStatement.setString(1, posUser.getUsername());
				preparedStatement.setString(2, posUser.getEmail());
				preparedStatement.setString(3, AES.encrypt(posUser.getPassword()));
				// preparedStatement.setString(3, posUser.getPassword());
				preparedStatement.setString(4, posUser.getMerchantCode());
				preparedStatement.setBoolean(5, posUser.isActive());
				preparedStatement.setInt(6, posUser.getCreatedBy());
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
				affectedRows = preparedStatement.executeUpdate();
			} else {
				if (CheckPOSUserByUserName(posUser.getUsername())) {
					return false;
				}
			}

			System.out.println("Newly Created user is :" + posUser);
			System.out.println("Newly Created user password is: " + posUser.getPassword());
			System.out.println("Newly Created user Merchant code is" + posUser.getMerchantCode());
			System.out.println("New POS USER EMAIL:" + posUser.getEmail());

			if (affectedRows != 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement);
		}
	}

	private boolean CheckPOSUserByUserName(String username) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getPOSUserByUserName);
			preparedStatement.setString(1, username);
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

	private boolean CheckPOSUserEmailById(String email, int id) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserEmailById);
			preparedStatement.setString(1, email);
			preparedStatement.setInt(2, id);
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

	@Override
	public boolean editMerchantPOSUser(POSUser posUser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int affectedRows = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.updateMerchantPOSUser);
			preparedStatement.setString(1, posUser.getUsername());
			preparedStatement.setString(2, posUser.getEmail());
			preparedStatement.setBoolean(3, posUser.isActive());
			preparedStatement.setString(4, posUser.getEmail());
			preparedStatement.setString(5, posUser.getMerchantCode());
			affectedRows = preparedStatement.executeUpdate();

			if (affectedRows != 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement);
		}
	}

	@Override
	public boolean resetMerchantPOSUser(POSUser posUser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int affectedRows = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.resetMerchantPOSUser);
			preparedStatement.setString(1, AES.encrypt(posUser.getPassword()));
			// preparedStatement.setString(1, posUser.getPassword());
			preparedStatement.setString(2, posUser.getEmail());
			// preparedStatement.setString(3, posUser.getMerchantCode());
			affectedRows = preparedStatement.executeUpdate();
			if (affectedRows != 0) {
				System.out.println("New password " + posUser.getPassword() + ". PIN reset successfully.");
				return true;
			} else {
				System.out.println("PIN reset not successful.");
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement);
		}
	}

	public List<POSUser> getMerchantPOSUsers(String merchantCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 0;
		try {
			System.out.println("loading users for merchant " + merchantCode);
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantPOSUsers);
			preparedStatement.setString(1, merchantCode);
			resultSet = preparedStatement.executeQuery();
			List<POSUser> posUsers = new ArrayList<>();
			while (resultSet.next()) {
				POSUser user = new POSUser();
				user.setId(resultSet.getInt("Id"));
				user.setUsername(resultSet.getString("username"));
				System.out.println("usernames: " +user.getUsername());
				user.setEmail(resultSet.getString("email"));
				user.setActive(resultSet.getBoolean("active"));
				user.setMerchantCode(resultSet.getString("merchant_code"));
				System.out.println("merchant code fetched: " +user.getMerchantCode());
				count++;
				posUsers.add(user);
			}
			System.out.println(count + " users found.");
			return posUsers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<POSUser> getMerchantPOSUsersActive(String merchantCode) {
		System.out.println("Active users for merchant " + merchantCode);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 0;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantPOSUsersActive);
			preparedStatement.setString(1, merchantCode);
			System.out.println("mercant code  "+merchantCode);
			resultSet = preparedStatement.executeQuery();
			List<POSUser> posUsers = new ArrayList<>();
			while (resultSet.next()) {
				POSUser user = new POSUser();
				user.setId(resultSet.getInt("Id"));
				user.setUsername(resultSet.getString("username"));
				user.setEmail(resultSet.getString("email"));
				user.setActive(resultSet.getBoolean("active"));
				user.setMerchantCode(resultSet.getString("merchant_code"));
				count++;
				posUsers.add(user);
			}
			System.out.println(count + " active users found");
			return posUsers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<Merchant> GetMerchantsById(String LoggedInUser) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMerchantsByUserName);
			preparedStatement.setString(1, LoggedInUser);
			resultSet = preparedStatement.executeQuery();
			List<Merchant> merchants = new ArrayList<Merchant>();
			while (resultSet.next()) {
				Merchant merchant = new Merchant();
				merchant.merchantId = resultSet.getInt("Id");
				merchant.merchantCode = resultSet.getString("mer_code");
				merchant.merchantName = resultSet.getString("mer_Name");
				merchant.LoggedInUser = resultSet.getString("UserFullName");
				System.out.println("Username for hospital is: " +resultSet.getString("UserFullName"));
				count++;
				merchants.add(merchant);
				// System.out.println("Merchants:" +Arrays.toString(merchants.toArray()));
			}
			return merchants;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public CompasResponse deleteMerchantPOSUser(POSUser posUser) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement14 = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.deleteMerchantPOSUser);
			preparedStatement.setInt(1, posUser.getId());
			System.out.println("DELETED USER IS " + posUser.getId());
			if (preparedStatement.executeUpdate() <= 0) {
				connection.rollback();
				return new CompasResponse(201, "Error in deleting POS User");
			} else {
				connection.commit();
				
				// insert audit trail
				preparedStatement14 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement14.setInt(1, posUser.getCreatedBy());
				preparedStatement14.setInt(2, posUser.getCreatedBy());
				preparedStatement14.setString(3, "Deleted POS User : " + posUser.getUsername());
				preparedStatement14.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement14.setString(5, "LCT Service Provider Module");
				if (preparedStatement14.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(200, "Failed to add audit trail");
				}
				logger.info("Audit Trail for " + posUser.getUsername() + "on POS User module captured");
				
				return new CompasResponse(200, "POS User was deleted successfully");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");

		}
	}

}

/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.compulynx.compas.dal.LoginDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.Params;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Rights;
import com.compulynx.compas.models.RightsDetail;

import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserServices;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
public class LoginDalImpl implements LoginDal {
	private DataSource	dataSource;
	
	private static final Logger logger = Logger.getLogger(LoginDalImpl.class.getName());

	public LoginDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public LoginUser GetUserIdManual(int userId, String userName, String password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserCredentialManual);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, AES.encrypt(password));
			resultSet = preparedStatement.executeQuery();
			// Insert Audit Trail
			preparedStatement1 = connection.prepareStatement(Queryconstants.insertAuditTrail);
			preparedStatement1.setInt(1, userId);
			preparedStatement1.setString(2, userName);
			preparedStatement1.setString(3, "Logged in");
			preparedStatement1.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
			preparedStatement1.setString(5, "Web User Authentication Module");
			if (preparedStatement1.executeUpdate() <= 0) {
				connection.rollback();			
			return (preparedStatement1.executeUpdate() > 0) ? new LoginUser(200, "Audit Trail Saved")
					: new LoginUser(201, "Nothing To Save");
		}
			logger.info("Audit Trail for " +userName+ "with id : " +userId+ "captured");
			User user = new User();
			if (resultSet.next()) {
				user.firstTimeLogin = resultSet.getBoolean("first_time_login");
				System.out.println("First time login flag :" +user.firstTimeLogin);
				return new LoginUser(resultSet.getInt("UserID"), resultSet.getInt("userTypeId"), 200, resultSet.getBoolean("first_time_login"),
						resultSet.getString("UserGroupId"));
			} else {
				return new LoginUser(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	

	public LoginUser GetUserIdBio(int bioId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserCredentialBio);
			preparedStatement.setInt(1, bioId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new LoginUser(resultSet.getInt("UserID"), 200);
			} else {
				return new LoginUser(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/*
	 * Anita Comment's Method should only be called if the user has a Right
	 * to View > 1. Hence Validate User Credentials Is Called First
	 */
	public LoginSession GetUserAssgnRightsList(LoginUser user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		try {
			connection = dataSource.getConnection();
			if(user.userTypeId == 1) {
				LoginSession loginSession = new LoginSession();
				preparedStatement1 = connection.prepareStatement(Queryconstants.getUserTypeId);
				preparedStatement1.setInt(1, user.userId);
				resultSet1 = preparedStatement1.executeQuery();
				List<Rights> objlist1 = new ArrayList<Rights>();
				while (resultSet1.next()) {
					loginSession.setUserTypeId(resultSet1.getInt("userTypeId"));
					loginSession.setSessionId(resultSet1.getInt("id"));
				}
//				loginSession.setRightsList(objlist1);
				loginSession.setRespCode(200);
				return loginSession;
			}
			preparedStatement = connection.prepareStatement(Queryconstants.getUserGrpRights);
			preparedStatement.setInt(1, user.userId);
			// System.out.println("User Id: " + userId);
			resultSet = preparedStatement.executeQuery();
			List<Rights> objlist = new ArrayList<Rights>();
			LoginSession loginSession = new LoginSession();

			// Using to check Previous Header id if the ID's are the
			// same, if
			// not create new header object
			int headerId = 0;
			Rights objRights = null;
			while (resultSet.next()) {
				// The Values Remain the Same, No Harm
				// Reassigning

				loginSession.setLinkId(resultSet.getInt("LinkId"));
				loginSession.setSessionId(resultSet.getInt("ID"));
			//	loginSession.setUserClassID(resultSet.getInt("UserClassID"));
				loginSession.setUserGroupId(resultSet.getInt("UserGroupID"));
				loginSession.setUserTypeId(resultSet.getInt("userTypeId"));
				loginSession.setLinkName(resultSet.getString("LinkName"));
				loginSession.setSessionName(resultSet.getString("UserName"));
				loginSession.setSessionFullName(resultSet.getString("UserFullName"));
				System.out.println("Logged in service provider : " +resultSet.getString("UserFullName"));
				loginSession.setLinkExtInfo(resultSet.getString("LinkExtInfo"));
				// System.out.println("Comapring Header Id : " +
				// headerId +
				// " to : " + resultSet.getInt("HeaderID"));
				if (!(headerId == resultSet.getInt("HeaderID"))) {
					// Avoiding adding Null Object On First
					// loop
					if (!(objRights == null)) {
						objlist.add(objRights);
					}
					objRights = new Rights(resultSet.getString("HeaderName"), resultSet.getString("HeaderIconCss"),
							resultSet.getString("HeaderIconColor"));
				}
				if (!(objRights == null)) {
					objRights.rightsList.add(new RightsDetail(resultSet.getString("RightDisplayName"), resultSet
							.getString("RightShortCode"), resultSet.getString("RightViewName"), resultSet
							.getString("RightName"), resultSet.getBoolean("AllowAdd"), resultSet.getBoolean("AllowEdit"),
							resultSet.getBoolean("AllowDelete"), resultSet.getBoolean("AllowView"), resultSet
									.getInt("RightMaxWidth")));
				}
				headerId = resultSet.getInt("HeaderID");
				// System.out.println("HeaderId Set to: " +
				// headerId);
			}
			if (!(objRights == null)) {
				objlist.add(objRights);
			}
			loginSession.setRightsList(objlist);
			loginSession.setRespCode(200);
			return loginSession;
		} catch (Exception ex) {
			System.out.println(ex);
			return new LoginSession(500);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public LoginUser GetUserIdBio(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserBioManual);
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new LoginUser(resultSet.getInt("UserID"), resultSet.getInt("UserBioID"), 200, true, resultSet.getString("UserGroupId"));
			} else {
				return new LoginUser(201);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public LoginUser GetUserIdDevice(String userName, String password, String deviceId) { //alafu nataka place hii function inakuwa called
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserCredentialDevice);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, AES.encrypt(password));
			preparedStatement.setString(3, deviceId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int userId = resultSet.getInt("UserID");
				if (resultSet.getBoolean("active") == false) {
					return new LoginUser(resultSet.getInt("UserID"), 201, "Device is Not Active", null);
				}
				List<Programme> programmes = GetAllUserServices(userId);// ebu muulize what payload response he expects kwa login
				System.out.println("services fetched###");
				return new LoginUser(resultSet.getInt("UserID"), 200, "Successfull Login", programmes);

			} else {
				return new LoginUser(resultSet.getInt("UserID"), 201, "Invalid Login Credential", null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(0, 201, "Invalid Login Credential", null);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Programme> GetAllUserServices(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeByUserID);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ProgrammeId");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.programmeValue = resultSet.getDouble("ProgrammeValue");
				programme.currency = resultSet.getString("currency");
				preparedStatement = connection.prepareStatement(Queryconstants.getServicesByProgrammes);
				preparedStatement.setInt(1, programme.programmeId);
				resultSet2 = preparedStatement.executeQuery();
				List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					Service objService = new Service();
					objService.serviceId = resultSet2.getInt("parentServiceId");
					objService.serviceName = resultSet2.getString("serviceName");
					// objService.serviceValue=resultSet2.getDouble("serviceValue");
					// objService.parentServiceId=resultSet2.getInt("parentServiceId");

					// preparedStatement = connection
					// .prepareStatement(Queryconstants.getActiveParams);
					// preparedStatement.setInt(1,
					// objService.parentServiceId);
					//
					// resultSet3 =
					// preparedStatement.executeQuery();
					// List<Params> params=new
					// ArrayList<Params>();
					// while (resultSet3.next()) {
					// params.add(new
					// Params(resultSet3.getInt("paramId"),resultSet3.getString("ParamName")));
					// }
					// objService.params=params;
					services.add(objService);

				}
				programme.services = services;
				objProgrammes.add(programme);
			}
			return objProgrammes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


	

	public boolean ValiodateAgentPin(int userid, String agentPin) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkAgentPin);
			preparedStatement.setInt(1, userid);
			preparedStatement.setString(2, agentPin);

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

	public boolean ValiodateCardPin(String cardno, String cardpin) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkCardPin);
			preparedStatement.setString(1, cardno);
			preparedStatement.setString(2, cardpin);

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

	
	public boolean ValiodateDeviceLicense(String deviceId, String licenseNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.validateDeviceLicense);
			preparedStatement.setString(1, deviceId);
			preparedStatement.setString(2, licenseNumber);

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

/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.joda.time.DateTime;

import com.compulynx.compas.dal.UserDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.AutoComplete;
import com.compulynx.compas.models.AutoCompleteObject;
import com.compulynx.compas.models.BioImage;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.BusketBalanceUpdate;
import com.compulynx.compas.models.CardActivation;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Demo;
import com.compulynx.compas.models.DemoBio;
import com.compulynx.compas.models.DemoNumber;
import com.compulynx.compas.models.DemoResponse;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Topup;
import com.compulynx.compas.models.TransAuthResponse;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.TransactionMst;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.Voucher;
import com.compulynx.compas.models.android.AndroidBeneficiary;
import com.compulynx.compas.models.android.AndroidBnfgrp;
import com.compulynx.compas.models.android.AndroidCategories;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.android.AndroidServices;
import com.compulynx.compas.models.android.AndroidProgrammes;
import com.compulynx.compas.models.android.AndroidTopup;
import com.compulynx.compas.models.android.AndroidUsers;
import com.compulynx.compas.models.android.AndroidVoucher;
import com.compulynx.compas.models.android.ApiProgramme;
import com.compulynx.compas.models.android.ApiService;
import com.compulynx.compas.models.android.ApiVoucher;

/**
 * @author Anita
 *
 */
public class UserDalImpl implements UserDal {
	private static final Logger logger = Logger.getLogger(UserDalImpl.class.getName());

	private DataSource dataSource;

	public UserDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean checkUserName(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserByName);
			preparedStatement.setString(1, userName);

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

	public List<User> GetClasses() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getClasses);
			resultSet = preparedStatement.executeQuery();
			List<User> classes = new ArrayList<User>();
			while (resultSet.next()) {
				classes.add(new User(resultSet.getInt("ID"), resultSet.getString("ClassName")));
			}
			return classes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateUser(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (user.userId == 0) {
				if (checkUserName(user.userName)) {
					return new CompasResponse(201, "User Name Already Exists");
				}

				preparedStatement = connection.prepareStatement(Queryconstants.insertUser);
				preparedStatement.setString(1, user.userName);
				preparedStatement.setString(2, user.userFullName);
				preparedStatement.setString(3, AES.encrypt(user.userPwd));
				preparedStatement.setInt(4, user.userGroupId);
				preparedStatement.setString(5, user.userEmail);
				preparedStatement.setString(6, user.userPhone);
				preparedStatement.setInt(7, 0);
				preparedStatement.setString(8, "");
				preparedStatement.setBoolean(9, false);
				preparedStatement.setLong(10, user.userLinkedID);
				preparedStatement.setLong(11, user.userBioID);
				preparedStatement.setBoolean(12, user.active);
				preparedStatement.setInt(13, user.createdBy);
				preparedStatement.setTimestamp(14, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(15, user.merchantId);
				// preparedStatement.setInt(16, user.locationId);
				preparedStatement.setInt(16, user.userTypeId);
				preparedStatement.setInt(17, user.posUserLevel);
				preparedStatement.setBoolean(18, true);

			} else {
				preparedStatement = connection.prepareStatement(Queryconstants.updateUser);
				preparedStatement.setString(1, user.userName);
				preparedStatement.setString(2, user.userFullName);
				preparedStatement.setString(3, AES.encrypt(user.userPwd));
				// preparedStatement.setString(3, user.userPwd);
				preparedStatement.setInt(4, user.userGroupId);
				preparedStatement.setString(5, user.userEmail);
				preparedStatement.setString(6, user.userPhone);
				preparedStatement.setInt(7, user.userSecretQuestionId);
				preparedStatement.setString(8, "");
				preparedStatement.setBoolean(9, false);
				preparedStatement.setLong(10, user.userLinkedID);
				preparedStatement.setLong(11, user.userBioID);
				preparedStatement.setBoolean(12, user.active);
				preparedStatement.setInt(13, user.createdBy);
				preparedStatement.setTimestamp(14, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(15, user.merchantId);
				// preparedStatement.setInt(16, user.locationId);
				preparedStatement.setInt(16, user.userTypeId);
				preparedStatement.setInt(17, user.posUserLevel);
				preparedStatement.setInt(18, user.userId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(200, "Records Updated")
					: new CompasResponse(201, "Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "User Name Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<User> GetAllUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUsers);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				users.add(new User(resultSet.getInt("ID"), resultSet.getString("UserName"),
						resultSet.getString("UserFullName"), AES.decrypt(resultSet.getString("UserPassword")),
						resultSet.getInt("UserGroupID"), resultSet.getInt("AgentID"), resultSet.getInt("locationid"),
						resultSet.getString("UserEmail"), resultSet.getString("UserPhone"),
						resultSet.getInt("UserSecretQuestionID"),
						AES.decrypt(resultSet.getString("UserSecretQuestionAns")), resultSet.getBoolean("UserBioLogin"),
						resultSet.getInt("UserLinkedID"), resultSet.getInt("UserBioID"), resultSet.getBoolean("active"),
						resultSet.getInt("CreatedBy"), 200, resultSet.getString("UserPassword"),
						resultSet.getInt("usertypeid"), count, resultSet.getString("usertypename"),
						resultSet.getInt("pos_user_level"), resultSet.getInt("merchantId"),
						resultSet.getBoolean("first_time_login")));
				count++;
			}
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public User GetUserById(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection.prepareStatement(Queryconstants.getUserById);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new User(resultSet.getInt("UserID"), resultSet.getString("UserName"),
						resultSet.getString("UserFullName"), AES.decrypt(resultSet.getString("UserPassword")),
						resultSet.getString("UserEmail"), resultSet.getString("UserPhone"),
						resultSet.getInt("UserGroupID"), resultSet.getString("GroupName"),
						// resultSet.getInt("UserSecretQuestionID"),
						// AES.decrypt(resultSet.getString("UserSecretAns")),
						resultSet.getBoolean("active"), resultSet.getInt("CreatedBy"), 200,
						resultSet.getString("UserPassword"), resultSet.getBoolean("first_time_login"));
			} else {
				return new User(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<User> GetQuestions() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getQuestions);

			resultSet = preparedStatement.executeQuery();
			List<User> questions = new ArrayList<User>();
			while (resultSet.next()) {
				questions.add(new User(resultSet.getString("Question"), resultSet.getInt("ID")));
			}
			return questions;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<UserGroup> GetGroupsByUserType(int userTypeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getGroupsByUserType);
			preparedStatement.setInt(1, userTypeId);
			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				UserGroup objGroup = new UserGroup();
				objGroup.groupId = resultSet.getInt("ID");
				objGroup.groupCode = resultSet.getString("groupcode");
				objGroup.groupName = resultSet.getString("groupName");
				objGroup.active = resultSet.getBoolean("active");
				objGroup.grpTypeId = resultSet.getInt("grouptypeid");
				objGroup.createdBy = resultSet.getInt("createdBy");
				groups.add(objGroup);
			}
			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@SuppressWarnings("resource")
	public AndroidDownloadVo GetAndroidDownloadData(String macAddress) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		try {
			connection = dataSource.getConnection();
			// check if the device is registered
			preparedStatement = connection.prepareStatement(Queryconstants.checkDeviceRegistered);
			preparedStatement.setString(1, macAddress);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				// check if the device is issued
				preparedStatement1 = connection.prepareStatement(Queryconstants.checkDeviceIssued);
				preparedStatement1.setInt(1, resultSet.getInt("id"));
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {

					AndroidDownloadVo downloadVo = new AndroidDownloadVo();
					preparedStatement = connection.prepareStatement(Queryconstants.getAndUsers);
					preparedStatement.setString(1, macAddress);
					resultSet = preparedStatement.executeQuery();
					List<AndroidUsers> users = new ArrayList<AndroidUsers>();
					while (resultSet.next()) {
						AndroidUsers objUser = new AndroidUsers();
						objUser.userId = resultSet.getInt("ID");
						objUser.userName = resultSet.getString("userName");
						objUser.userFullName = resultSet.getString("userfullname");
						objUser.password = AES.decrypt(resultSet.getString("userpassword"));
						objUser.level = resultSet.getString("pos_user_level");
						objUser.providerName = resultSet.getString("mer_name");
						// objUser.sateliteName = resultSet.getString("agentdesc");

						users.add(objUser);
					}
					downloadVo.users = users;
					logger.info("Users##" + users.size());

					DBOperations.DisposeSql(preparedStatement, resultSet);
					preparedStatement = connection.prepareStatement(Queryconstants.getAndServices);

					resultSet = preparedStatement.executeQuery();
					List<AndroidServices> services = new ArrayList<AndroidServices>();
					while (resultSet.next()) {
						AndroidServices objService = new AndroidServices();
						objService.serviceId = resultSet.getInt("ID");
						objService.serviceCode = resultSet.getString("ser_code");
						objService.serviceName = resultSet.getString("ser_name");
						services.add(objService);
					}
					downloadVo.services = services;
					logger.info("Services##" + services.size());
					DBOperations.DisposeSql(preparedStatement, resultSet);

					preparedStatement = connection.prepareStatement(Queryconstants.getAndVouchers);

					resultSet = preparedStatement.executeQuery();
					List<Voucher> vouchers = new ArrayList<Voucher>();
					while (resultSet.next()) {
						Voucher objVoucher = new Voucher();
						objVoucher.voucherId = resultSet.getInt("ID");
						objVoucher.voucherCode = resultSet.getString("voucher_code");
						objVoucher.voucherName = resultSet.getString("voucher_name");
						vouchers.add(objVoucher);
					}
					downloadVo.vouchers = vouchers;
					logger.info("Baskets##" + vouchers.size());
					/*
					 * preparedStatement = connection
					 * .prepareStatement(Queryconstants.getAndBnfGrps);
					 * 
					 * resultSet = preparedStatement.executeQuery(); List<AndroidBnfgrp> bnfGrps =
					 * new ArrayList<AndroidBnfgrp>(); while (resultSet.next()) { AndroidBnfgrp
					 * objbnfgrp = new AndroidBnfgrp(); objbnfgrp.bnfGrpId = resultSet.getInt("ID");
					 * objbnfgrp.bnfGrpName = resultSet.getString("bnfgrp_name");
					 * bnfGrps.add(objbnfgrp); } downloadVo.bnfGrps = bnfGrps;
					 * logger.info("Beneficiary Groups##" + bnfGrps.size());
					 */
					DBOperations.DisposeSql(preparedStatement, resultSet);

					downloadVo.categories = GetAndroidProgrammes();
					// downloadVo.topupDetails=GetAndroidTopupDetails(macAddress);
					// System.out.println("Topup
					// details##"+GetAndroidTopupDetails(macAddress).size());
					return downloadVo;
				} else {
					AndroidDownloadVo downloadVo = new AndroidDownloadVo();
					downloadVo.respCode = 207;
					return downloadVo;
				}
			} else {
				AndroidDownloadVo downloadVo = new AndroidDownloadVo();
				downloadVo.respCode = 205;
				return downloadVo;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<AndroidProgrammes> GetAndroidProgrammes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getAndroidProgrammes);

			resultSet = preparedStatement.executeQuery();
			List<AndroidProgrammes> programmes = new ArrayList<AndroidProgrammes>();
			while (resultSet.next()) {
				AndroidProgrammes programme = new AndroidProgrammes();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("programmeCode");
				programme.programmeName = resultSet.getString("ProgrammeDesc");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				// String startDate = sdf.format(sdf.parse(resultSet.getString("start_date")));
				// String endDate = sdf.format(sdf.parse(resultSet.getString("end_date")));
				// programme.startDate = startDate;
				// programme.endDate = endDate;
				programme.schemeId = resultSet.getInt("productid");
				programme.prgType = resultSet.getString("prg_type");
				programme.itmModes = resultSet.getString("itm_modes");
				// programme.carryFwd=resulset.getBoo
				preparedStatement1 = connection.prepareStatement(Queryconstants.getAndroidVouchers);
				preparedStatement1.setInt(1, programme.programmeId);
				resultSet2 = preparedStatement1.executeQuery();
				List<AndroidVoucher> vouchers = new ArrayList<AndroidVoucher>();
				while (resultSet2.next()) {
					AndroidVoucher objvoucher = new AndroidVoucher();
					objvoucher.voucherId = resultSet2.getInt("voucherId");
					objvoucher.voucherCode = resultSet2.getString("voucher_code");
					objvoucher.voucherValue = resultSet2.getDouble("basket_value");
					vouchers.add(objvoucher);
					// services
					preparedStatement2 = connection.prepareStatement(Queryconstants.getAndroidProducts);
					preparedStatement2.setInt(1, objvoucher.voucherId);
					preparedStatement2.setInt(2, programme.programmeId);
					resultSet3 = preparedStatement2.executeQuery();
					List<AndroidServices> products = new ArrayList<AndroidServices>();
					while (resultSet3.next()) {
						AndroidServices objService = new AndroidServices();
						objService.serviceId = resultSet3.getInt("Service_Id");
						objService.serviceCode = resultSet3.getString("ser_code");
						objService.serviceLimit = resultSet3.getDouble("cover_limit");
						objService.serviceAuth = resultSet3.getBoolean("require_auth");
						products.add(objService);
					}

					objvoucher.serviceDetails = products;
				}
				programme.voucherDetails = vouchers;
				// System.out.println("Programmes##" + vouchers.size());
				programmes.add(programme);
				count++;
			}
			logger.info("Categories##" + programmes.size());
			return programmes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet2);
			DBOperations.DisposeSql(preparedStatement2, resultSet3);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public AndroidDownloadVo GetAndroidTopupDetails(String serialNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			System.out.println("Macaddress Received from MObile&&" + serialNo);
			connection = dataSource.getConnection();
			AndroidDownloadVo download = new AndroidDownloadVo();
			preparedStatement = connection.prepareStatement(Queryconstants.getAndroidTopup);
			preparedStatement.setString(1, serialNo);
			resultSet = preparedStatement.executeQuery();
			List<AndroidTopup> topupDeatils = new ArrayList<AndroidTopup>();
			while (resultSet.next()) {
				AndroidTopup objtopup = new AndroidTopup();
				objtopup.programmeId = resultSet.getInt("programme_id");
				objtopup.beneficiaryId = resultSet.getInt("beneficiary_id");
				objtopup.cardNumber = resultSet.getString("card_number");
				objtopup.voucherId = resultSet.getInt("voucher_id");
				// objtopup.productId=resultSet.getInt("item_id");
				objtopup.productPrice = resultSet.getDouble("item_value");
				// objtopup.productQuantity=resultSet.getDouble("item_quantity");
				// objtopup.uom=resultSet.getString("uom");
				objtopup.voucherType = resultSet.getString("voucher_Type");
				objtopup.voucherIdNumber = resultSet.getString("voucher_id_number");
				objtopup.bnfGrpId = resultSet.getString("beneficiary_group_id");
				objtopup.cycle = resultSet.getInt("cycle");
				topupDeatils.add(objtopup);

			}

			count++;
			download.topupDetails = topupDeatils;
			logger.info("TopupDetails##" + download.topupDetails.size());
			return download;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {

			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public AndroidDownloadVo GetAndroidBeneficiary(Member member) {
		final DateFormat dateFormats = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		PreparedStatement preparedStatement7 = null;
		PreparedStatement preparedStatementProg = null;
		PreparedStatement preparedStatement8 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet4 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet6 = null;
		ResultSet resultSetCard = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			AndroidDownloadVo downloadVo = new AndroidDownloadVo();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberDetails);
			preparedStatement.setString(1, member.memberNo);
			resultSet = preparedStatement.executeQuery();
			// List<Member> benficiary = new ArrayList<Member>();
			Member objbnf = new Member();
			String strOutpatientStatus = "Active";
			if (resultSet.next()) {
				objbnf.memberId = resultSet.getInt("id");
				objbnf.memberNo = resultSet.getString("memberno");
				objbnf.fullName = resultSet.getString("name");
				objbnf.gender = resultSet.getString("gender");
				objbnf.dateOfBirth = resultSet.getString("dob");
				objbnf.nationalId = resultSet.getString("nationalid");
				objbnf.accNo = resultSet.getInt("acc_no");
				objbnf.email = resultSet.getString("email");
				objbnf.productId = resultSet.getInt("product_id");
				objbnf.memberPic = resultSet.getString("emp_pic");
				objbnf.nhifNo = resultSet.getString("nhif");
				objbnf.relation = resultSet.getString("relation");
				System.out.println("Member relation: " + objbnf.relation);
				objbnf.principleId = resultSet.getInt("principle_id");
				System.out.println("Member principle Id: " + objbnf.principleId);
				objbnf.principle = resultSet.getString("principle");
				System.out.println("Member principle: " + objbnf.principle);
				objbnf.active = resultSet.getBoolean("active");
				System.out.println("Member active status after deactivation: " + objbnf.active);
				objbnf.memberType = resultSet.getString("member_type");
				if (objbnf.active == false) {
					strOutpatientStatus = "Inactive";
				} else {
					objbnf.setOutpatientStatus(resultSet.getString("outpatient_status"));
				}
				System.out.println("Member outpatient active status after deactivation: " + strOutpatientStatus);
				objbnf.bioStatus = "0";
				if (objbnf.memberPic != null) {
					String[] strImage = resultSet.getString("emp_pic").split(",");
					objbnf.memberPic = strImage[1].toString();

				} else {
					objbnf.memberPic = "";
				}

				preparedStatement2 = connection.prepareStatement(Queryconstants.getCardNumber);
				preparedStatement2.setString(1, member.memberNo);
				resultSet3 = preparedStatement2.executeQuery();
				if (resultSet3.next()) {
					objbnf.cardNumber = resultSet3.getString("card_no");
					objbnf.bioStatus = resultSet3.getString("bio_status");
				}
				DBOperations.DisposeSql(preparedStatement2, resultSet3);
				preparedStatement5 = connection.prepareStatement(Queryconstants.getMemberProgramme);
				preparedStatement5.setInt(1,
						resultSet.getInt("relation") == 0 ? resultSet.getInt("id") : resultSet.getInt("principle_id"));
				resultSet5 = preparedStatement5.executeQuery();
				if (resultSet5.next()) {
					objbnf.schemeActive = resultSet5.getBoolean("active");
					objbnf.scheme = resultSet5.getString("product_name");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					c.setTime(sdf.parse(resultSet5.getString("end_date")));
					c.add(Calendar.DAY_OF_WEEK, 1);
					String newDate = sdf.format(c.getTime());
					if (Timestamp.valueOf(newDate + " 00:00:00")
							.before(new java.sql.Timestamp(new java.util.Date().getTime()))) {
						objbnf.schemeExpired = true;
					}
				}
				DBOperations.DisposeSql(preparedStatement5, resultSet5);
				preparedStatement2 = connection.prepareStatement(Queryconstants.getMemberBiImages);
				preparedStatement2.setString(1, objbnf.cardNumber);
				resultSet3 = preparedStatement2.executeQuery();
				List<BioImage> images = new ArrayList<BioImage>();
				while (resultSet3.next()) {
					BioImage image = new BioImage();
					image.image = resultSet3.getString("image");
					images.add(image);
				}
				objbnf.bioImages = images;
				System.out.println("Bioimages###" + images.size());
				preparedStatement1 = connection.prepareStatement(Queryconstants.getMemberProgrammes);
				preparedStatement1.setInt(1,
						resultSet.getInt("relation") == 0 ? resultSet.getInt("id") : resultSet.getInt("principle_id"));
				resultSet2 = preparedStatement1.executeQuery();
				List<ApiProgramme> programme = new ArrayList<ApiProgramme>();
				while (resultSet2.next()) {
					if (resultSet2.getBoolean("IsActive")) {
						objbnf.programmeDesc = resultSet2.getString("programmeDesc");
						objbnf.scheme = resultSet2.getString("product_name");
					}
					ApiProgramme objPrg = new ApiProgramme();
					objPrg.programmeId = resultSet2.getInt("programmeid");
					objPrg.schemeId = resultSet2.getInt("product_id");
					System.out.println("Product Id: " +objPrg.schemeId);
					objPrg.programmeCode = resultSet2.getString("programmeCode");
					objPrg.programmeName = resultSet2.getString("programmeDesc");
					preparedStatement3 = connection.prepareStatement(Queryconstants.getNewMemberVouchers2);
					preparedStatement3.setInt(1, objPrg.programmeId);
					preparedStatement3.setInt(2, resultSet.getInt("relation") == 0 ? resultSet.getInt("id")
							: resultSet.getInt("principle_id"));
					preparedStatement3.setInt(3, objPrg.programmeId);
					preparedStatement3.setInt(4, resultSet.getInt("id"));
					resultSet1 = preparedStatement3.executeQuery();
					List<ApiVoucher> vouchers = new ArrayList<ApiVoucher>();
					// if (!resultSet1.next()) {
					// strOutpatientStatus = "Inactive";
					// resultSet1.beforeFirst();
					// }
					while (resultSet1.next()) {
						// strOutpatientStatus = "Active";
						ApiVoucher objVoucher = new ApiVoucher();
						objVoucher.voucherId = resultSet1.getInt("basket_id");
						objVoucher.voucherName = resultSet1.getString("voucher_name");
						objVoucher.voucherCode = resultSet1.getString("voucher_code");
						objVoucher.voucherValue = df.format(resultSet1.getDouble("basket_value"));
						objVoucher.voucherBalance = df.format(resultSet1.getDouble("basket_balance")); // voucher
																										// remaining
						objVoucher.used = df.format(resultSet1.getDouble("used"));
						objVoucher.schemeType = resultSet1.getString("scheme_type");
						System.out.println("This is the voucher value: " + objVoucher.voucherValue
								+ "And this is the baket balance: " + objVoucher.voucherBalance);
						System.out.println("This is the amount used: " + objVoucher.used + "This is what remains: "
								+ objVoucher.voucherBalance);
						// determine that out-patient status will be set to active when more than 75% of
						// basket allocation is consumed by member
						if (objVoucher.voucherName != null && objVoucher.schemeType.equals("O")
								&& Double.parseDouble(objVoucher.voucherValue.replace(",", "")) != 0) {
							double value1 = Double.parseDouble(objVoucher.voucherValue);
							double value2 = Double.parseDouble(objVoucher.used);
							double percent = (value2 / value1) * 100;
							System.out.println("percentage used: " + percent);
							System.out.println("Scheme type: " + objVoucher.schemeType);
							if (percent > 75) {
								strOutpatientStatus = "Inactive";
							}
						}
						// determine that out-patient status will be set to active when more than 75% of
						// basket allocation is consumed by member
						// if (objVoucher.voucherName != null && objVoucher.schemeType.equals("I") &&
						// Double.parseDouble(objVoucher.voucherValue.replace(",", "")) != 0) {
						// double value1=Double.parseDouble( objVoucher.voucherValue);
						// double value2=Double.parseDouble(objVoucher.used);
						// double percent=(value2/value1)*100;
						// System.out.println("percentage used: " + percent);
						// System.out.println("Scheme type: " + objVoucher.schemeType);
						// if (percent > 75) {
						// strOutpatientStatus = "Inactive";
						// }
						// }
						objVoucher.accNo = resultSet1.getString("acc_no");
						if (resultSet1.getString("cover_type").equals("2")) {
							if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 0) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.getMMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 0) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 1) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.get1MMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 1) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.get1FMemberServiceDtl);
							} else {
								preparedStatement4 = connection.prepareStatement(Queryconstants.get2MemberServiceDtl);
							}
							preparedStatement4.setInt(3, resultSet.getInt("relation") == 0 ? resultSet.getInt("id")
									: resultSet.getInt("principle_id"));
						} else {
							if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 0) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.getMMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 0) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 1) {
								preparedStatement4 = connection
										.prepareStatement(Queryconstants.get1MIndividualServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 1) {
								preparedStatement4 = connection
										.prepareStatement(Queryconstants.get1FIndividualServiceDtl);
							} else {
								preparedStatement4 = connection
										.prepareStatement(Queryconstants.get2IndividualServiceDtl);
							}
							preparedStatement4.setInt(3, resultSet.getInt("id"));
						}
						preparedStatement4.setInt(1, objPrg.programmeId);
						preparedStatement4.setInt(2, objVoucher.voucherId);
						resultSet4 = preparedStatement4.executeQuery();
						List<ApiService> services = new ArrayList<ApiService>();
						while (resultSet4.next()) {
							ApiService objSer = new ApiService();
							objSer.serviceId = resultSet4.getInt("service_id");
							objSer.serviceName = resultSet4.getString("ser_name");
							objSer.serviceCode = resultSet4.getString("ser_code");
							// objSer.serviceValue = resultSet4.getDouble("cover_limit");
							// objSer.serviceBalance = resultSet4.getDouble("cover_balance");
							objSer.voucherBalance = resultSet1.getDouble("basket_balance");
							objSer.requireAuth = resultSet4.getString("require_auth");
							// there is a sublimit here
							if (resultSet4.getDouble("cover_limit") < resultSet4.getDouble("service_value")) {
								objSer.serviceBalance = resultSet4.getDouble("cover_balance");
								objSer.serviceValue = resultSet4.getDouble("cover_limit");
								// objSer.voucherBalance = resultSet4.getDouble("service_balance");
							} else {
								// if no sub-limit
								objSer.serviceBalance = resultSet4.getDouble("basket_balance");
								objSer.serviceValue = resultSet4.getDouble("service_value");
							}
							System.out.println("Service name: " + objSer.serviceName + "service cover limit :"
									+ objSer.serviceValue + "Total allocation balance :" + objSer.serviceBalance
									+ " sub-service balance:" + objSer.serviceBalance);
							services.add(objSer);
						}
						objVoucher.services = services;
						// if (objVoucher.services.size() > 0)
						vouchers.add(objVoucher);
					}

					// set outpatient status to active
					preparedStatement7 = connection.prepareStatement(Queryconstants.updateMemberStatus);
					preparedStatement7.setString(1, strOutpatientStatus);
					preparedStatement7.setInt(2, objbnf.memberId);
					preparedStatement7.executeUpdate();
					objbnf.setOutpatientStatus(strOutpatientStatus);
					objPrg.vouchers = vouchers;
					programme.add(objPrg);
				}

				objbnf.memberProgrammes = programme;
				logger.info("Member Programme##" + objbnf.memberProgrammes.size());
				objbnf.respCode = 200;

			} else {

				objbnf.respCode = 201;
			}
			downloadVo.member = objbnf;
			logger.info("Member No Fetched##" + objbnf.memberNo);
			logger.info("Response Code##" + objbnf.respCode);
			logger.info("Outpatient Status" + strOutpatientStatus);
			return downloadVo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement1, resultSet2);
			DBOperations.DisposeSql(preparedStatement2, resultSet3);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse updateVoucherDownloadStatus(String deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		logger.info("MacAddress##" + deviceId);
		String sql = "update mst_topup set downloaded = ? where cycle=? and topup_status='A' and agent_id=? and beneficiary_group_id=?";
		List<Topup> topups = new ArrayList<Topup>();
		topups = getVouchersDownloadedToUpdate(deviceId);
		logger.info("Cycle to Update##" + topups.size());
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (Topup topup : topups) {
				preparedStatement.setBoolean(1, true);
				preparedStatement.setInt(2, topup.cycle);
				preparedStatement.setInt(3, topup.agentId);
				preparedStatement.setInt(4, topup.beneficiary_group_id);
				preparedStatement.addBatch();
			}

			int[] ins = preparedStatement.executeBatch();
			return (ins.length > 0) ? new CompasResponse(200, "Topup Update Done Successfully")
					: new CompasResponse(201, "Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

		return new CompasResponse(201, "Error occurred while saving, please try again!!");
	}

	private List<Topup> getVouchersDownloadedToUpdate(String serialNo) {
		logger.info("Download Vouchers$$");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		String sql2 = "select AgentId from DeviceIssueDetails di,DeviceRegDetails dr where dr.Id=di.DeviceRegId and dr.SerialNo=?";

		String sql = "select distinct t.cycle,t.beneficiary_group_id from "
				+ "  AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d,mst_topup t " + " where   di.AgentId=a.Id "
				+ " and d.Id=di.DeviceRegId  and t.downloaded = ? "
				+ "  and topup_status='A' and t.agent_id=di.AgentId and a.Id=t.agent_id and t.agent_id=? ";

		try {
			connection = dataSource.getConnection();
			preparedStatement1 = connection.prepareStatement(sql2);
			preparedStatement1.setString(1, serialNo);
			resultSet1 = preparedStatement1.executeQuery();
			List<Topup> vouchers = new ArrayList<Topup>();
			if (resultSet1.next()) {
				preparedStatement = connection.prepareStatement(sql);
				// preparedStatement.setString(1, serialNo);
				preparedStatement.setBoolean(1, false);
				preparedStatement.setInt(2, resultSet1.getInt("agentid"));
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Topup topup = new Topup();
					topup.cycle = resultSet.getInt("cycle");
					topup.agentId = resultSet1.getInt("agentid");
					topup.beneficiary_group_id = resultSet.getInt("beneficiary_group_id");
					vouchers.add(topup);
				}

			}

			return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Agent> GetLocationAgents(int locationId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getLocationAgents);
			preparedStatement.setInt(1, locationId);
			resultSet = preparedStatement.executeQuery();
			List<Agent> agents = new ArrayList<Agent>();
			while (resultSet.next()) {
				agents.add(new Agent(resultSet.getInt("Id"), resultSet.getString("agentDesc"),
						resultSet.getInt("locationid"), resultSet.getBoolean("active"), resultSet.getInt("CreatedBy"),
						200));
			}
			return agents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@SuppressWarnings("resource")
	public CompasResponse UpdateBnfBioImages(List<Member> member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			System.out.println("Member coming from Android device##" + member.size());

			for (int i = 0; i < member.size(); i++) {

				preparedStatement = connection.prepareStatement(Queryconstants.insertCustBioImages);
				System.out.println("bioimages size##" + member.get(i).bioImages.size());
				if (member.get(i).bioImages.size() > 0) {
					for (int j = 0; j < member.get(i).bioImages.size(); j++) {

						preparedStatement.setString(1, member.get(i).cardNumber);
						preparedStatement.setString(2, member.get(i).bioImages.get(j).image);
						preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
						if (preparedStatement.executeUpdate() <= 0) {
							connection.rollback();
							return new CompasResponse(201, "Failed to Updated Bio Images");
						}
						preparedStatement1 = connection.prepareStatement(Queryconstants.updateMemBioStatus);
						preparedStatement1.setString(1, "1");
						preparedStatement1.setString(2, member.get(i).cardNumber);
						if (preparedStatement1.executeUpdate() <= 0) {
							connection.rollback();
							return new CompasResponse(201, "Failed to Updated Bio Status For Member");
						}
					}

				} else {
					return new CompasResponse(201, "Failed to Updated Bio Images");
				}
			}
			connection.commit();
			return new CompasResponse(200, "Record Updated Successfully");

		} catch (SQLException sqlEx) {

			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Member No Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// @SuppressWarnings("resource")
	public CompasResponse UplodTmsTrans(List<TransactionMst> trans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet2 = null;
		ResultSet rs = null;
		// String response = "";
		CompasResponse response = new CompasResponse();
		int transMstId = 0;
		int transDtlId = 0;
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("Africa/Nairobi"));
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (!(trans.get(0).deviceId == null || trans.get(0).deviceId == "")) {
				preparedStatement2 = connection.prepareStatement(Queryconstants.getProviderFromDevice);
				preparedStatement2.setString(1, trans.get(0).deviceId);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					for (int i = 0; i < trans.size(); i++) {
						System.out.print(trans.get(i).accNo);
						System.out.print("Invoice Number: " + trans.get(i).invoiceNo);
						preparedStatement = connection.prepareStatement(Queryconstants.insertTmsTransMst,
								Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, trans.get(i).memberNo);
						preparedStatement.setString(2, trans.get(i).cardNumber);
						preparedStatement.setString(3, trans.get(i).billNo);
						preparedStatement.setInt(4, (trans.get(i).schemeId));
						preparedStatement.setString(5, trans.get(i).deviceId);
						preparedStatement.setDouble(6, (trans.get(i).totalRunningBalance));
						preparedStatement.setDouble(7, trans.get(i).totalAmount);
						preparedStatement.setString(8, df.format(date));
						preparedStatement.setInt(9, trans.get(i).userId);
						preparedStatement.setTimestamp(10, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement.setString(11, trans.get(i).accNo);
						preparedStatement.setInt(12, (trans.get(i).programmeId));
						preparedStatement.setString(13, trans.get(i).transStatus);
						preparedStatement.setInt(14, 110);
						preparedStatement.setInt(15, resultSet2.getInt("id"));
						preparedStatement.setString(16, trans.get(i).invoiceNo);
						if (preparedStatement.executeUpdate() > 0) {

							rs = preparedStatement.getGeneratedKeys();
							rs.next();
							transMstId = rs.getInt(1);
							DBOperations.DisposeSql(preparedStatement);

							// Insert Audit Trail
							preparedStatement1 = connection.prepareStatement(Queryconstants.insertAuditTrail);
							preparedStatement1.setInt(1, trans.get(i).userId);
							preparedStatement1.setString(2, "Device Id: " + trans.get(i).deviceId);
							preparedStatement1.setString(3, "Posted Transaction");
							preparedStatement1.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
							preparedStatement1.setString(5, "Transaction Module on SARAL Device");
							if (preparedStatement1.executeUpdate() <= 0) {
								connection.rollback();
								return new CompasResponse(200, "Failed to add audit trail");
							}
							// logger.info("Audit Trail for " +memberNo+ "with id : " +userId+ "captured");

							// insert rights
							preparedStatement = connection.prepareStatement(Queryconstants.insertTmsTransDtl,
									Statement.RETURN_GENERATED_KEYS);
							List<ApiService> services = new ArrayList<ApiService>();
							for (int j = 0; j < trans.get(i).transDtl.size(); j++) {
								if (trans.get(i).transDtl.get(j).transStatus.equalsIgnoreCase("-2")
										|| trans.get(i).transDtl.get(j).transStatus.equalsIgnoreCase("0")) {

									preparedStatement.setInt(1, transMstId);
									preparedStatement.setInt(2, trans.get(i).transDtl.get(j).serviceId);
									preparedStatement.setInt(3, (trans.get(i).transDtl.get(j).voucherId));
									preparedStatement.setDouble(4,
											Double.valueOf(trans.get(i).transDtl.get(j).txnAmount));
									preparedStatement.setDouble(5,
											Double.valueOf(trans.get(i).transDtl.get(j).runningBalance));
									preparedStatement.setString(6, df.format(date));
									preparedStatement.setString(7, trans.get(i).transStatus);
									preparedStatement.setInt(8, trans.get(i).userId);
									preparedStatement.setInt(9, 110);
									preparedStatement.setString(10, trans.get(i).transDtl.get(j).image);
									preparedStatement.setTimestamp(11,
											new java.sql.Timestamp(new java.util.Date().getTime()));
									if (preparedStatement.executeUpdate() <= 0) {
										connection.rollback();
										response.respCode = 201;
										return response;
									} else {
										rs = preparedStatement.getGeneratedKeys();
										rs.next();
										transDtlId = rs.getInt(1);
										preparedStatement1 = connection
												.prepareStatement(Queryconstants.updateBasketBalance);
										preparedStatement1.setDouble(1, trans.get(i).totalRunningBalance);
										preparedStatement1.setString(2, trans.get(i).accNo);
										preparedStatement1.setInt(3, trans.get(i).transDtl.get(j).voucherId);
										preparedStatement1.setInt(4, (trans.get(i).programmeId));
										if (preparedStatement1.executeUpdate() <= 0) {
											connection.rollback();
											response.respCode = 201;
											return response;
										}
										DBOperations.DisposeSql(preparedStatement1);
										preparedStatement1 = connection
												.prepareStatement(Queryconstants.updateServiceBalance);
										preparedStatement1.setDouble(1, trans.get(i).transDtl.get(j).runningBalance);
										preparedStatement1.setString(2, trans.get(i).accNo);
										preparedStatement1.setInt(3, trans.get(i).transDtl.get(j).voucherId);
										preparedStatement1.setInt(4, trans.get(i).transDtl.get(j).serviceId);
										preparedStatement1.setInt(5, (trans.get(i).programmeId));
										if (preparedStatement1.executeUpdate() <= 0) {
											connection.rollback();
											response.respCode = 201;
											return response;
										}
									}

									if (trans.get(i).transDtl.get(j).transStatus.equalsIgnoreCase("0")) {
										response.respCode = 200;
										response.services = services;
									} else if (trans.get(i).transDtl.get(j).transStatus.equalsIgnoreCase("-2")) {
										response.respCode = 300;
										response.txnId = transDtlId;
										response.services = services;
									}
									// return response;
								}

							}
							connection.commit();

						} else {
							connection.rollback();
							response.respCode = 201;
							response.services = null;
							return response;
						}

					}
				} else {
					response.respCode = 207;
					response.respMessage = "Device not issued";
				}
			} else {
				response.respCode = 209;
				response.respMessage = "Device id not captured";
			}
			System.out.print(response.respMessage);
			return response;

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			response.respCode = 500;
			response.services = null;
			return response;

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			response.respCode = 500;
			response.services = null;
			return response;
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(preparedStatement1);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public AndroidDownloadVo UpdateMemberBusketBalance(BusketBalanceUpdate busketBalanceUpdate) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			AndroidDownloadVo downloadVo = new AndroidDownloadVo();
			preparedStatement = connection.prepareStatement(Queryconstants.updateBusketBalance);
			for (int i = 0; i < busketBalanceUpdate.programmes.size(); i++) {
				for (int j = 0; j < busketBalanceUpdate.programmes.get(i).vouchers.size(); j++) {
					preparedStatement.setDouble(1,
							busketBalanceUpdate.programmes.get(i).vouchers.get(j).voucherBalance);
					preparedStatement.setDouble(2, busketBalanceUpdate.programmes.get(i).programmeId);
					preparedStatement.setDouble(3, busketBalanceUpdate.acc_no);
					preparedStatement.executeUpdate();
				}

			}
			return downloadVo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(preparedStatement, resultSet);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@SuppressWarnings("resource")
	public CompasResponse UpdateCardActivation(List<CardActivation> cardActivation) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			for (int i = 0; i < cardActivation.size(); i++) {
				preparedStatement = connection.prepareStatement(Queryconstants.insertActivatedCards);
				preparedStatement.setString(1, cardActivation.get(i).cardId);
				preparedStatement.setString(2, cardActivation.get(i).cardUid);
				preparedStatement.setString(3, cardActivation.get(i).memberNo);
				preparedStatement.setString(4, cardActivation.get(i).deviceId);
				preparedStatement.setInt(5, Integer.valueOf(cardActivation.get(i).activatedBy));
				preparedStatement.setString(6, cardActivation.get(i).activatedOn);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(201, "Unable to update activation records");
				}
			}
			connection.commit();
			return new CompasResponse(200, "Record Updated Successfully");

		} catch (SQLException sqlEx) {

			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Record already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@SuppressWarnings("resource")
	public DemoResponse UpdateDemo(Demo demo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int basicId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (Integer.valueOf(demo.demoId) == 0) {
				if (CheckIdNumberExists(demo.idNumber)) {
					connection.rollback();
					return new DemoResponse(201, "ID or Passport number already registered");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertDemoBasicDetails,
						Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, demo.idNumber);
				preparedStatement.setString(2, demo.dateOfBirth);
				preparedStatement.setString(3, demo.surname);
				preparedStatement.setString(4, demo.otherName);
				preparedStatement.setString(5, demo.gender);
				preparedStatement.setString(6, demo.nationality);
				preparedStatement.setString(7, demo.image);
				if (preparedStatement.executeUpdate() > 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					basicId = rs.getInt(1);
					preparedStatement1 = connection.prepareStatement(Queryconstants.insertDemoBioDetails);
					preparedStatement1.setInt(1, basicId);
					preparedStatement1.setString(2, demo.idNumber);
					preparedStatement1.setString(3, demo.bioDetails.RT);
					preparedStatement1.setString(4, demo.bioDetails.R1);
					preparedStatement1.setString(5, demo.bioDetails.R2);
					preparedStatement1.setString(6, demo.bioDetails.R3);
					preparedStatement1.setString(7, demo.bioDetails.R4);
					preparedStatement1.setString(8, demo.bioDetails.LT);
					preparedStatement1.setString(9, demo.bioDetails.L1);
					preparedStatement1.setString(10, demo.bioDetails.L2);
					preparedStatement1.setString(11, demo.bioDetails.L3);
					preparedStatement1.setString(12, demo.bioDetails.L4);
					if (preparedStatement1.executeUpdate() > 0) {
						preparedStatement2 = connection.prepareStatement(Queryconstants.insertDemoSimDetails);
						if (CheckPhoneNumberExists(demo.phoneNumber)) {
							connection.rollback();
							return new DemoResponse(201, "Phone number already registered");
						}
						preparedStatement2.setInt(1, basicId);
						preparedStatement2.setString(2, demo.idNumber);
						preparedStatement2.setString(3, demo.phoneNumber);
						if (preparedStatement2.executeUpdate() < 0) {
							connection.rollback();
							return new DemoResponse(201, "Unable to add sim details");
						}

					} else {
						connection.rollback();
						return new DemoResponse(201, "Unable to add bio details");
					}
				} else {
					connection.rollback();
					return new DemoResponse(201, "Unable to add basic details");
				}
			} else {
				preparedStatement2 = connection.prepareStatement(Queryconstants.insertDemoSimDetails);
				if (CheckPhoneNumberExists(demo.phoneNumber)) {
					connection.rollback();
					return new DemoResponse(201, "Phone number already registered");
				}
				preparedStatement2.setInt(1, demo.demoId);
				preparedStatement2.setString(2, demo.idNumber);
				preparedStatement2.setString(3, demo.phoneNumber);
				if (preparedStatement2.executeUpdate() < 0) {
					connection.rollback();
					return new DemoResponse(201, "Unable to add sim details");
				}
			}

			connection.commit();
			return new DemoResponse(200, "Record Updated Successfully");

		} catch (SQLException sqlEx) {

			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new DemoResponse(201, "Record already Exists");
			} else {
				return new DemoResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex.printStackTrace();
			return new DemoResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, rs);
		}
	}

	@SuppressWarnings("resource")
	public DemoResponse GetDemo(Demo demo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.getDemoBasicDetails);
			preparedStatement.setString(1, demo.idNumber);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Demo user = new Demo();
				user.demoId = rs.getInt("id");
				user.idNumber = rs.getString("id_number");
				user.dateOfBirth = rs.getString("date_of_birth");
				user.surname = rs.getString("surname");
				user.otherName = rs.getString("other_name");
				user.gender = rs.getString("gender");
				user.nationality = rs.getString("nationality");
				user.image = rs.getString("image");
				preparedStatement1 = connection.prepareStatement(Queryconstants.getDemoSimDetails);
				preparedStatement1.setString(1, rs.getString("id"));
				rs1 = preparedStatement1.executeQuery();
				List<DemoNumber> numbers = new ArrayList<DemoNumber>();
				while (rs1.next()) {
					DemoNumber number = new DemoNumber();
					number.phoneNumber = rs1.getString("phone_number");
					numbers.add(number);
				}
				preparedStatement2 = connection.prepareStatement(Queryconstants.getDemoBioDetails);
				preparedStatement2.setString(1, rs.getString("id"));
				rs2 = preparedStatement2.executeQuery();
				DemoBio demoBio = new DemoBio();
				while (rs2.next()) {
					demoBio.RT = rs2.getString("RT");
					demoBio.R1 = rs2.getString("R1");
					demoBio.R2 = rs2.getString("R2");
					demoBio.R3 = rs2.getString("R3");
					demoBio.R4 = rs2.getString("R4");
					demoBio.LT = rs2.getString("LT");
					demoBio.L1 = rs2.getString("L1");
					demoBio.L2 = rs2.getString("L2");
					demoBio.L3 = rs2.getString("L3");
					demoBio.L4 = rs2.getString("L4");
				}
				user.simDetails = numbers;
				user.bioDetails = demoBio;
				return new DemoResponse(200, "Records fetched successfully", user);
			}
			return new DemoResponse(201, "User not found");

		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex.printStackTrace();
			return new DemoResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, rs);
		}
	}

	public boolean CheckPhoneNumberExists(String number) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkPhoneNumberexists);
			preparedStatement.setString(1, number);
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

	public boolean CheckIdNumberExists(String idNumber) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkIdNumberexists);
			preparedStatement.setString(1, idNumber);
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

	public DemoResponse VerifyNumber(Demo demo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		DemoResponse response = new DemoResponse();
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkPhoneNumberexists);
			preparedStatement.setString(1, demo.phoneNumber);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				response.respCode = 201;
				response.respMessage = "Number already registered";
			} else {
				response.respCode = 200;
				response.respMessage = "Number not registered";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new DemoResponse(201, "Error");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
		return response;
	}

	public String genRandInt() {
		return "S" + (int) (Math.random() * 899) + "T" + (int) (Math.random() * 899);
	}

	public TransAuthResponse CheckAuthTransStatus(Transaction transaction) {
		Connection connection = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet2 = null;
		TransAuthResponse response = new TransAuthResponse();
		try {
			connection = dataSource.getConnection();
			preparedStatement2 = connection.prepareStatement(Queryconstants.getAuthPendingTrans);
			preparedStatement2.setInt(1, transaction.txnId);
			resultSet2 = preparedStatement2.executeQuery();
			while (resultSet2.next()) {

				if (resultSet2.getString("trans_status").equalsIgnoreCase("-2")) {
					response.respCode = 300;
					response.txnId = transaction.txnId;
					response.memberNo = resultSet2.getString("member_no");
					response.respMessage = "Transaction awaiting Authorization";
					response.auth_amount = resultSet2.getDouble("auth_amount");
					response.trans_amount = resultSet2.getDouble("txn_amount");
					response.reason = resultSet2.getString("auth_reason");
					return response;
				} else if (resultSet2.getString("trans_status").equalsIgnoreCase("-3")) {
					ApiService objser = new ApiService();
					objser.serviceCode = resultSet2.getString("ser_code");
					response.respMessage = "Transaction Rejected";
					response.memberNo = resultSet2.getString("member_no");
					response.respCode = 301;
					response.txnId = transaction.txnId;
					response.auth_amount = resultSet2.getDouble("auth_amount");
					response.trans_amount = resultSet2.getDouble("txn_amount");
					response.reason = resultSet2.getString("rejected_reason");
					return response;
				} else {
					response.respCode = 200;
					response.respMessage = "Transaction Authorized";
					response.txnId = transaction.txnId;
					response.memberNo = resultSet2.getString("member_no");
					response.auth_amount = resultSet2.getDouble("auth_amount");
					response.trans_amount = resultSet2.getDouble("txn_amount");
					response.reason = resultSet2.getString("auth_reason");
					return response;

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new TransAuthResponse(201, "Error");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement2, resultSet2);
		}
		return response;
	}

	public AutoCompleteObject GetMatchingMembers(String memberNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		AutoCompleteObject membersObj = new AutoCompleteObject();
		List<AutoComplete> members = new ArrayList<AutoComplete>();
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMatchingMembers);
			preparedStatement.setString(1, memberNo);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				AutoComplete member = new AutoComplete();
				member.memberNo = resultSet.getString("emp_ref_key");
				member.name = resultSet.getString("emp_fullname");
				members.add(member);
			}
			membersObj.members = members;
		} catch (

		Exception ex) {
			ex.printStackTrace();
			return membersObj;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
		return membersObj;
	}

	public AutoCompleteObject GetAllMembers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		AutoCompleteObject membersObj = new AutoCompleteObject();
		List<AutoComplete> members = new ArrayList<AutoComplete>();
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getAllMembers);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				AutoComplete member = new AutoComplete();
				member.memberNo = resultSet.getString("emp_ref_key");
				member.name = resultSet.getString("emp_fullname");
				members.add(member);
			}
			membersObj.members = members;
		} catch (

		Exception ex) {
			ex.printStackTrace();
			return membersObj;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
		return membersObj;
	}

	public CompasResponse changePassword(User user) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String userNameFullName = user.userFullName;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.updatePassword);

			preparedStatement.setString(1, AES.encrypt(user.newPassword));
			preparedStatement.setString(2, user.userName);

			System.out.println("user id........." + user.userName);
			System.out.println("New user password after changing :" + user.newPassword);
			System.out.println("New first time login flag after password change: " + user.firstTimeLogin);
			return (preparedStatement.executeUpdate() > 0)
					? new CompasResponse(200, "Great! The user information was saved succesfully")
					: new CompasResponse(201, "Nothing To Update");
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

	public List<Reports> GetAuditLogDetails(Reports audrpt) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (audrpt.fromDate == null && audrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAuditTrail);
			}
			if (audrpt.fromDate != null && audrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAuditTrailBetween);
				preparedStatement.setString(1, audrpt.fromDate);
				preparedStatement.setString(2, audrpt.toDate);
			}
			if (audrpt.fromDate != null && audrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllNboTransactionsAbove);
				preparedStatement.setString(1, audrpt.fromDate);
				preparedStatement.setString(2, audrpt.fromDate);
			}
			if (audrpt.fromDate == null && audrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllNboTransactionsBelow);
				preparedStatement.setString(1, audrpt.toDate);
				preparedStatement.setString(2, audrpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				report.userId = resultSet.getInt("userId");
				report.createdBy = resultSet.getString("created_by");
				report.created_on = resultSet.getString("created_on");
				report.Description = resultSet.getString("Description");
				report.ModuleAccessed = resultSet.getString("ModuleAccessed");
				report.username = resultSet.getString("username");
				reports.add(report);
				counter++;
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

}

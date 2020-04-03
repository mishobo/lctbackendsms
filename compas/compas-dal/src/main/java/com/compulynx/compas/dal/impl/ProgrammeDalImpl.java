/**
 * 
 */
package com.compulynx.compas.dal.impl;

import com.compulynx.compas.dal.ProgrammeDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.*;

import javax.sql.DataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Anita
 *
 */
public class ProgrammeDalImpl implements ProgrammeDal {

	private DataSource dataSource;

	public ProgrammeDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean checkProgrammeName(String programmeDesc, int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeByName);
			preparedStatement.setString(1, programmeDesc);
			preparedStatement.setInt(2, orgId);
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

	public CompasResponse UpdateProgramme(Programme programme) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		PreparedStatement preparedStatement7 = null;
		PreparedStatement preparedStatement8 = null;
		PreparedStatement preparedStatement9 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet7 = null;
		ResultSet resultSet8 = null;
		ResultSet rs = null;
		int programmeId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
//			Date startdate = dt.parse(programme.startDate);
//			Date enddate = dt.parse(programme.endDate);

			// *** same for the format String below
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			if (programme.programmeId == 0) {
				if (CheckProgrammeCode(programme.programmeCode, programme.orgId)) {
					return new CompasResponse(201,	"Programme Code Already Exists");
				}
				if (checkProgrammeName(programme.programmeDesc, programme.orgId)) {
					return new CompasResponse(201,	"Programme Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertProgrammeDetails, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, genRandInt());
				preparedStatement.setString(2, programme.programmeDesc);
				preparedStatement.setBoolean(3, programme.active);
				preparedStatement.setInt(4, programme.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(6, programme.productId);
				preparedStatement.setString(7, null);
				preparedStatement.setString(8, null);
				preparedStatement.setString(9, getItms(programme.itmList));
				preparedStatement.setString(10, getChtms(programme.chtmList));
				preparedStatement.setString(11, getIntms(programme.intmList));
				preparedStatement.setString(12, getOtms(programme.otmList));

				preparedStatement.setString(13, programme.programmeType);
				preparedStatement.setInt(14, programme.orgId);
				preparedStatement.setInt(15, programme.coverTypeId);
			} else {
				if (CheckProgrammeNameByCode(programme.programmeDesc,programme.programmeCode, programme.programmeId)) {
					return new CompasResponse(201,	"Programme Name Already Exists");
				}

				preparedStatement = connection.prepareStatement(Queryconstants.updateProgrammeDetails);
				preparedStatement.setString(1, programme.programmeDesc);
				preparedStatement.setBoolean(2, programme.active);
				preparedStatement.setInt(3, programme.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(5, null);
				preparedStatement.setString(6, null);
				preparedStatement.setString(7, getItms(programme.itmList));
				preparedStatement.setString(8, getChtms(programme.chtmList));
				preparedStatement.setString(9, getIntms(programme.intmList));
				preparedStatement.setString(10, getOtms(programme.otmList));

				preparedStatement.setInt(11, programme.productId);
				preparedStatement.setInt(12, programme.programmeId);
				programmeId = programme.programmeId;
			}
			if (preparedStatement.executeUpdate() > 0) {
				// Dispose
				if (programme.programmeId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					programmeId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeVoucherDetailsIds);
				preparedStatement.setInt(1, programmeId);
				ResultSet rst = preparedStatement.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				while(rst.next()) {
					Voucher voucher = new Voucher();
					voucher.voucherId = rst.getInt("voucherId");
					vouchers.add(voucher);
				}
				for (int i = 0; i < vouchers.size(); i++) { 
					for (int j = 0; j < programme.vouchers.size(); j++) {
						if(programme.vouchers.get(j).isActive) {
							if(vouchers.get(i).voucherId == programme.vouchers.get(j).voucherId) {
								programme.vouchers.get(j).isOld = true;
							}
						}
					}
				}
				
				for (int i = 0; i < vouchers.size(); i++) { 
					for (int j = 0; j < programme.vouchers.size(); j++) {
						if(!programme.vouchers.get(j).isActive) {
							if(vouchers.get(i).voucherId == programme.vouchers.get(j).voucherId) {
								programme.vouchers.get(j).isRemoved = true;
							}
						}
					}
				}
				DBOperations.DisposeSql(preparedStatement);
				for (int i = 0; i < programme.vouchers.size(); i++) {
					if(programme.vouchers.get(i).isActive && !programme.vouchers.get(i).isOld) {
						System.out.print("Added Voucher is : "+programme.vouchers.get(i).voucherId+"\n");
						for(int k=0;k<programme.vouchers.get(i).services.size();k++){
							preparedStatement = connection.prepareStatement(Queryconstants.insertProgrammeVouchers);
							preparedStatement.setInt(1, programmeId);
							preparedStatement.setInt(2,	programme.vouchers.get(i).voucherId);
							preparedStatement.setInt(3, programme.vouchers.get(i).services.get(k).serviceId);
							preparedStatement.setDouble(4, programme.vouchers.get(i).services.get(k).ipLimit);
							preparedStatement.setInt(5, programme.vouchers.get(i).services.get(k).serSubtypeId);
							preparedStatement.setBoolean(6,	programme.vouchers.get(i).isActive);
							preparedStatement.setInt(7,	programme.vouchers.get(i).createdBy);
							preparedStatement.setTimestamp(	8, new java.sql.Timestamp(new java.util.Date().getTime()));
							preparedStatement.setDouble(9, programme.vouchers.get(i).voucherValue);
							preparedStatement.setBoolean(10,programme.vouchers.get(i).services.get(k).requireAuth );
							preparedStatement.setString(11,programme.vouchers.get(i).services.get(k).limitDetail );
							if (preparedStatement.executeUpdate() > 0) {
								preparedStatement1 = connection.prepareStatement(Queryconstants.getAllocationsUsingProgramme);
								preparedStatement1.setInt(1, programmeId);
								resultSet1 = preparedStatement1.executeQuery();
								while(resultSet1.next()) {
									preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
									preparedStatement2.setInt(1, programmeId);
									preparedStatement2.setString(2, resultSet1.getString("card_number"));
									preparedStatement2.setInt(3, resultSet1.getInt("acc_id"));
									preparedStatement2.setString(4, resultSet1.getString("acc_no"));
									preparedStatement2.setInt(5, programme.vouchers.get(i).voucherId);
									preparedStatement2.setInt(6, programme.vouchers.get(i).services.get(k).serviceId);
									preparedStatement2.setDouble(7, programme.vouchers.get(i).services.get(k).ipLimit > 0 ? programme.vouchers.get(i).services.get(k).ipLimit : programme.vouchers.get(i).voucherValue);
									preparedStatement2.setDouble(8, programme.vouchers.get(i).voucherValue);
									preparedStatement2.setDouble(9, programme.vouchers.get(i).services.get(k).ipLimit > 0 ? programme.vouchers.get(i).services.get(k).ipLimit : programme.vouchers.get(i).voucherValue);
									preparedStatement2.setDouble(10, programme.vouchers.get(i).voucherValue);
	
									preparedStatement2.setString(11, "2017");
									preparedStatement2.setDouble(12, programme.vouchers.get(i).createdBy);
									preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
									preparedStatement2.executeUpdate();
								}
							}
							else {
								connection.rollback();
								throw new Exception("Failed to Insert Voucher Id "	+ programme.vouchers.get(i).voucherId);
							}
						}
					}
					if(!programme.vouchers.get(i).isActive && programme.vouchers.get(i).isRemoved) {
						
						for(int j = 0; j < programme.vouchers.get(i).services.size(); j++) {
							preparedStatement3 = connection.prepareStatement(Queryconstants.deleteProgrammevoucherDetails);
							preparedStatement3.setInt(1, programmeId);
							preparedStatement3.setInt(2, programme.vouchers.get(i).voucherId);
							preparedStatement3.setInt(3, programme.vouchers.get(i).services.get(j).serviceId);
							if(preparedStatement3.executeUpdate() > 0) {
								preparedStatement4 = connection.prepareStatement(Queryconstants.deleteServicesFromAllocations);
								preparedStatement4.setInt(1, programmeId);
								preparedStatement4.setInt(2, programme.vouchers.get(i).voucherId);
								preparedStatement4.setInt(3, programme.vouchers.get(i).services.get(j).serviceId);
								preparedStatement4.executeUpdate();
								
							}
							else {
								connection.rollback();
								throw new Exception("Failed to delete ProgrammeDetails Id "	+ programme.vouchers.get(i).services.get(j).serviceId);
							}
						}
					}
					if(programme.vouchers.get(i).isActive && programme.vouchers.get(i).isOld) {
						for(int t=0; t < programme.vouchers.get(i).services.size(); t++) {
							PreparedStatement prst = connection.prepareStatement(Queryconstants.updateProgrammeVoucherCoverLimits);
							prst.setDouble(1, programme.vouchers.get(i).services.get(t).ipLimit);
							prst.setBoolean(2, programme.vouchers.get(i).services.get(t).requireAuth);
							prst.setInt(3, programme.programmeId);
							prst.setInt(4, programme.vouchers.get(i).voucherId);
							prst.setInt(5, programme.vouchers.get(i).services.get(t).serviceId);
							prst.executeUpdate();
							
						}
						preparedStatement5 = connection.prepareStatement(Queryconstants.getAllocationLimits);
						preparedStatement5.setInt(1, programmeId);
						preparedStatement5.setInt(2, programme.vouchers.get(i).voucherId);
						resultSet5 = preparedStatement5.executeQuery();
						while(resultSet5.next()) {
							if(resultSet5.getDouble("basket_value") == programme.vouchers.get(i).voucherValue) {}
							else {
								for(int k=0; k<programme.vouchers.get(i).services.size(); k++){
									preparedStatement7 = connection.prepareStatement(Queryconstants.updateProgrammeVoucherDetails);
									preparedStatement7.setDouble(1, programme.vouchers.get(i).voucherValue);
									preparedStatement7.setInt(2, programmeId);
									preparedStatement7.setInt(3, programme.vouchers.get(i).voucherId);
									preparedStatement7.setInt(4, programme.vouchers.get(i).services.get(k).serviceId);
									if(preparedStatement7.executeUpdate()>0) {
										preparedStatement6 = connection.prepareStatement(Queryconstants.updateBasketVouchers);
										preparedStatement6.setDouble(1, programme.vouchers.get(i).voucherValue - resultSet5.getDouble("used") >0 ? programme.vouchers.get(i).voucherValue - resultSet5.getDouble("used") : 0);
										preparedStatement6.setDouble(2, programme.vouchers.get(i).voucherValue);
										preparedStatement6.setInt(3, programmeId);
										preparedStatement6.setInt(4, programme.vouchers.get(i).voucherId);
										preparedStatement6.setInt(5, programme.vouchers.get(i).services.get(k).serviceId);
										preparedStatement6.executeUpdate();
									}
									else {
										connection.rollback();
										throw new Exception("Failed to update ProgrammeDetails Id "	+ programme.vouchers.get(i).services.get(k).serviceId);
									}
								}
							}
						}
					}
					
				}
				connection.commit();
				return new CompasResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update");

			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Programme Name Already Exists");
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

	private String getItms(List<ItmModes> itmModes) {
		String result = "";

		for (int i = 0; i < itmModes.size(); i++) {
			if (i != itmModes.size() - 1)
				result += itmModes.get(i).itmValue + ",";

			if (i == itmModes.size() - 1)
				result += itmModes.get(itmModes.size() - 1).itmValue;

		}

		return result;

	}

	private String getChtms(List<ChtmModes> chtmModes) {
		String result = "";

		for (int i = 0; i < chtmModes.size(); i++) {
			if (i != chtmModes.size() - 1)
				result += chtmModes.get(i).chtmValue + ",";

			if (i == chtmModes.size() - 1)
				result += chtmModes.get(chtmModes.size() - 1).chtmValue;

		}

		return result;

	}

	private String getIntms(List<IntmModes> intmModes) {
		String result = "";

		for (int i = 0; i < intmModes.size(); i++) {
			if (i != intmModes.size() - 1)
				result += intmModes.get(i).intmValue + ",";

			if (i == intmModes.size() - 1)
				result += intmModes.get(intmModes.size() - 1).intmValue;

		}

		return result;

	}
	private String getOtms(List<OtmModes> otmModes) {
		String result = "";

		for (int i = 0; i < otmModes.size(); i++) {
			if (i != otmModes.size() - 1)
				result += otmModes.get(i).otmValue + ",";

			if (i == otmModes.size() - 1)
				result += otmModes.get(otmModes.size() - 1).otmValue;

		}

		return result;

	}
	/**
	 * 
	 */
	public List<Programme> GetAllProgrammes(int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammes);
			preparedStatement.setInt(1, orgId);
			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("ProgrammeCode");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.productId = resultSet.getInt("Productid");
				programme.productName = resultSet.getString("product_name");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String startDate = sdf.format(sdf.parse(resultSet.getString("start_date")));
//				String endDate = sdf.format(sdf.parse(resultSet.getString("end_date")));
//				programme.startDate = startDate;
//				programme.endDate = endDate;
				programme.active = resultSet.getBoolean("active");
				programme.createdBy = resultSet.getInt("CreatedBy");
				programme.itmModes = resultSet.getString("itm_modes");
				programme.chtmModes = resultSet.getString("chtm_modes");
				programme.intmModes = resultSet.getString("intm_modes");
				programme.programmeType = resultSet.getString("prg_type");
				programme.coverTypeId=resultSet.getInt("cover_type_id");
				programme.count = count;
				if (programme.programmeType.equalsIgnoreCase("CT")) {
					preparedStatement = connection.prepareStatement(Queryconstants.getCashVouchersByProgrammeId);

				} else {
//					preparedStatement = connection.prepareStatement(Queryconstants.getVouchersByProgrammeId);
					preparedStatement = connection.prepareStatement(Queryconstants.getVouchersFromProgramme);
				}
				preparedStatement.setInt(1, programme.programmeId);
				preparedStatement.setInt(2, orgId);
				preparedStatement.setInt(3, programme.programmeId);
				preparedStatement.setInt(4, orgId);
				resultSet2 = preparedStatement.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				while (resultSet2.next()) {
					preparedStatement1 = connection.prepareStatement(Queryconstants.getServicesByBasket);
					preparedStatement1.setInt(1,resultSet2.getInt("voucherId"));
					preparedStatement1.setInt(2,programme.programmeId);
					preparedStatement1.setInt(3,resultSet2.getInt("voucherId"));
					preparedStatement1.setInt(4,programme.programmeId);
					preparedStatement1.setInt(5,resultSet2.getInt("voucherId"));
					resultSet3=preparedStatement1.executeQuery();
					List<Service>  services= new ArrayList<Service>();
					while(resultSet3.next()){
						Service objSer= new Service();
						objSer.serviceId=resultSet3.getInt("service_id");
						objSer.serviceName=resultSet3.getString("ser_name");
						objSer.ipLimit=resultSet3.getDouble("cover_limit");
						objSer.ipLimitType=(int) (resultSet3.getDouble("cover_limit")>0?0:resultSet3.getDouble("cover_limit"));
						objSer.requireAuth=resultSet3.getBoolean("require_auth");
						objSer.serSubtypeId=resultSet3.getInt("benefit_type_id");
						services.add(objSer);
					}
					vouchers.add(new Voucher(resultSet2.getInt("voucherId"),
							resultSet2.getString("voucher_Name"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getDouble("basket_value"), resultSet2
									.getString("frqmode"), resultSet2
									.getString("frq_type"),services));
				}
				programme.vouchers = vouchers;
				objProgrammes.add(programme);
				count++;
			}
			return objProgrammes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Programme GetProgrammeById(int programmeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeById);
			preparedStatement.setInt(1, programmeId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new Programme(resultSet.getInt("ProgrammeID"),
						resultSet.getString("ProgrammeCode"),
						resultSet.getString("ProgrammeName"),
						resultSet.getBoolean("active"),
						resultSet.getInt("CreatedBy"), 200);
			} else {
				return new Programme(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckProgrammeCode(String programmeCode, int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeByCode);
			preparedStatement.setString(1, programmeCode);
			preparedStatement.setInt(2, orgId);
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

	public boolean CheckProgrammeNameByCode(String programmeName, String programmeCode, int programmeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammeNameByCode);
			preparedStatement.setString(1, programmeName);
			preparedStatement.setString(2, programmeCode);
			preparedStatement.setInt(3, programmeId);
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
	
	public List<BeneficiaryGroup> GetCoverTypesById(int schemeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection.prepareStatement(Queryconstants.getCoverTypesByScheme);
			preparedStatement.setInt(1, schemeId);

			resultSet = preparedStatement.executeQuery();
			List<BeneficiaryGroup> covertypes= new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				BeneficiaryGroup objgrp= new BeneficiaryGroup();
				objgrp.bnfGrpId=resultSet.getInt("id");
				objgrp.bnfGrpName=resultSet.getString("bnfgrp_name");
				covertypes.add(objgrp);
			} 
				return covertypes;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public String genRandInt() {
		return "C"+ (int) (Math.random()*899);
	}

	public List<Programme> GetProgrammesByScheme(Programme prog) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammesByScheme);
			preparedStatement.setInt(1, prog.orgId);
			preparedStatement.setInt(2, prog.productId);
			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("ProgrammeCode");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.productId = resultSet.getInt("Productid");
				programme.productName = resultSet.getString("product_name");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String startDate = sdf.format(sdf.parse(resultSet.getString("start_date")));
//				String endDate = sdf.format(sdf.parse(resultSet.getString("end_date")));
//				programme.startDate = startDate;
//				programme.endDate = endDate;
				programme.active = resultSet.getBoolean("active");
				programme.createdBy = resultSet.getInt("CreatedBy");
				programme.itmModes = resultSet.getString("itm_modes");
				programme.chtmModes = resultSet.getString("chtm_modes");
				programme.intmModes = resultSet.getString("intm_modes");
				programme.programmeType = resultSet.getString("prg_type");
				programme.coverTypeId=resultSet.getInt("cover_type_id");
				programme.count = count;
				if (programme.programmeType.equalsIgnoreCase("CT")) {
					preparedStatement = connection.prepareStatement(Queryconstants.getCashVouchersByProgrammeId);

				} else {
//					preparedStatement = connection.prepareStatement(Queryconstants.getVouchersByProgrammeId);
					preparedStatement = connection.prepareStatement(Queryconstants.getVouchersFromProgramme);
				}
				System.out.println("progId: "+programme.programmeId);
				preparedStatement.setInt(1, programme.programmeId);
				preparedStatement.setInt(2, programme.programmeId);
				resultSet2 = preparedStatement.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				while (resultSet2.next()) {
					preparedStatement1 = connection.prepareStatement(Queryconstants.getServicesByBasket);
					preparedStatement1.setInt(1,resultSet2.getInt("voucherId"));
					preparedStatement1.setInt(2,programme.programmeId);
					preparedStatement1.setInt(3,resultSet2.getInt("voucherId"));
					preparedStatement1.setInt(4,programme.programmeId);
					preparedStatement1.setInt(5,resultSet2.getInt("voucherId"));
					resultSet3=preparedStatement1.executeQuery();
					List<Service>  services= new ArrayList<Service>();
					while(resultSet3.next()){
						Service objSer= new Service();
						objSer.serviceId=resultSet3.getInt("service_id");
						objSer.serviceName=resultSet3.getString("ser_name");
						objSer.ipLimit=resultSet3.getDouble("cover_limit");
						objSer.ipLimitType=(int) (resultSet3.getDouble("cover_limit")>0?0:resultSet3.getDouble("cover_limit"));
						objSer.requireAuth=resultSet3.getBoolean("require_auth");
						objSer.serSubtypeId=resultSet3.getInt("benefit_type_id");
						services.add(objSer);
					}
					vouchers.add(new Voucher(resultSet2.getInt("voucherId"),
							resultSet2.getString("voucher_Name"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getDouble("basket_value"), resultSet2
									.getString("frqmode"), resultSet2
									.getString("frq_type"),services));
				}
				programme.vouchers = vouchers;
				objProgrammes.add(programme);
				count++;
			}
			return objProgrammes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}

/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.VoucherDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public class VoucherDalImpl implements VoucherDal{
	private DataSource dataSource;

	public VoucherDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	public List<Service> GetServiceProducts(String vType) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			List<Service> services = new ArrayList<Service>();
			if(vType.equalsIgnoreCase("H")){
				preparedStatement = connection.prepareStatement(Queryconstants.getActiveHealthService);
				//preparedStatement.setString(1, vType);
				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next()) {
					services.add(new Service(resultSet.getInt("ID"), resultSet
							.getString("ser_code"), resultSet
							.getString("ser_name"), resultSet
							.getBoolean("active"), resultSet.getInt("Created_By"),
							200,"", resultSet
									.getString("ser_type"),count,0,"","",""));
					count++;
				}
			}else{
			preparedStatement = connection.prepareStatement(Queryconstants.getServiceProducts);
			//preparedStatement.setString(1, vType);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				services.add(new Service(resultSet.getInt("ID"), resultSet
						.getString("serviceName"), resultSet
						.getString("serviceName"), resultSet
						.getBoolean("active"), resultSet.getInt("CreatedBy"),
						200, resultSet.getString("componame"), resultSet
								.getString("compotype"),count,0,"","",""));
				count++;
			}
			}
		
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public CompasResponse UpdateVoucher(Voucher voucher) {
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
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		ResultSet rs = null;
		int voucherId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

	        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"  
	        /*SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
	        Date startdate = dt.parse(voucher.startDate);
	        Date enddate = dt.parse(voucher.endDate);

	        // *** same for the format String below
	        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");*/
			
			if (voucher.voucherId == 0) {
				if (CheckVoucherByCode(voucher.voucherCode)) {
					return new CompasResponse(201, "Voucher Code Already Exists");
				}
				if (CheckVoucherByName(voucher.voucherDesc)) {
					return new CompasResponse(201, "Voucher Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertVoucher,Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, genRandInt());
				preparedStatement.setString(2, voucher.voucherDesc);
				preparedStatement.setString(3, voucher.voucherType);
				preparedStatement.setDouble(4, voucher.voucherValue);
				preparedStatement.setString(5, voucher.coverType);
				preparedStatement.setBoolean(6, voucher.active);
				preparedStatement.setInt(7, voucher.createdBy);
				preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
				//preparedStatement.setString(8,dt1.format(startdate));
				//preparedStatement.setString(9,dt1.format(enddate));
				preparedStatement.setString(9, voucher.schemeType);
				
			} else {
				//if (CheckVoucherNameByCode(voucher.voucherDesc,voucher.voucherCode, voucher.voucherId)) {
				//	return new CompasResponse(201, "Voucher Name Already Exists");
				//}
				preparedStatement = connection.prepareStatement(Queryconstants.updateVoucher);
				preparedStatement.setString(1, voucher.voucherDesc);
				preparedStatement.setString(2, voucher.voucherType);
				preparedStatement.setDouble(3, voucher.voucherValue);
				preparedStatement.setString(4, voucher.coverType);
				preparedStatement.setBoolean(5, voucher.active);
				preparedStatement.setInt(6, voucher.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
				//preparedStatement.setString(7, dt1.format(startdate));
				//preparedStatement.setString(8, dt1.format(enddate));
				preparedStatement.setString(8, voucher.voucherCode);
				preparedStatement.setString(9, voucher.schemeType);
				preparedStatement.setInt(10, voucher.voucherId);
				
				
				voucherId = voucher.voucherId;
			}
			if (preparedStatement.executeUpdate() > 0) {
				// Dispose
				if (voucher.voucherId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					voucherId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
//				preparedStatement = connection.prepareStatement(Queryconstants.deleteVoucherServices);
//				preparedStatement.setInt(1, voucherId);
//				preparedStatement.executeUpdate();
//
//				DBOperations.DisposeSql(preparedStatement);
				
				preparedStatement = connection.prepareStatement(Queryconstants.getAvailableServicesInVoucher);
				preparedStatement.setInt(1, voucherId);
				ResultSet rst = preparedStatement.executeQuery();
				List<Service> services = new ArrayList<Service>();
				while (rst.next()) {
					Service service = new Service();
					service.serviceId = rst.getInt("service_id");
					services.add(service);
				}
				for(int i = 0; i < services.size(); i++) {
					for(int j = 0; j < voucher.services.size(); j++) {
						if(voucher.services.get(j).isActive) {
							if(services.get(i).serviceId == voucher.services.get(j).serviceId) {
								voucher.services.get(j).isOld = true;
							}
						}
					}
					
				}
				for(int i = 0; i < services.size(); i++) {
					for(int j = 0; j < voucher.services.size(); j++) {
						if(!voucher.services.get(j).isActive) {
							if(services.get(i).serviceId == voucher.services.get(j).serviceId) {
								voucher.services.get(j).isRemoved = true;
							}
						}
					}
					
				}
				
				// insert branches
				preparedStatement = connection.prepareStatement(Queryconstants.insertVoucherServices);
				for (int i = 0; i < voucher.services.size(); i++) {
					if(voucher.services.get(i).isActive && !voucher.services.get(i).isOld) {
						preparedStatement.setInt(1, voucherId);
						preparedStatement.setInt(2,	voucher.services.get(i).serviceId);
						preparedStatement.setDouble(3, voucher.services.get(i).quantity);
						if(voucher.voucherType.equalsIgnoreCase("VA")){
							preparedStatement.setDouble(4,voucher.voucherValue);
						}else {
							preparedStatement.setDouble(4,voucher.services.get(i).serviceValue);
						}
					
						preparedStatement.setBoolean(5,	voucher.services.get(i).isActive);
						preparedStatement.setInt(6,voucher.services.get(i).createdBy);
						preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
						if (preparedStatement.executeUpdate() > 0) {
							preparedStatement1 = connection.prepareStatement(Queryconstants.getProgrammeIdsUsingVoucher);
							preparedStatement1.setInt(1, voucherId);
							resultSet1 = preparedStatement1.executeQuery();
							while(resultSet1.next()) {
								preparedStatement2 = connection.prepareStatement(Queryconstants.insertProgrammeVouchers);
								preparedStatement2.setInt(1, resultSet1.getInt("programmeId"));
								preparedStatement2.setInt(2, voucherId);
								preparedStatement2.setInt(3, voucher.services.get(i).serviceId);
								preparedStatement2.setDouble(4, voucher.services.get(i).ipLimit);
								preparedStatement2.setInt(5, voucher.services.get(i).serSubtypeId);
								preparedStatement2.setBoolean(6, voucher.services.get(i).isActive);
								preparedStatement2.setInt(7, voucher.createdBy);
								preparedStatement2.setTimestamp( 8, new java.sql.Timestamp(new java.util.Date().getTime()));
								preparedStatement2.setDouble(9, resultSet1.getDouble("basket_value"));
								preparedStatement2.setBoolean(10, voucher.services.get(i).requireAuth );
								preparedStatement2.setString(11, voucher.services.get(i).limitDetail );
								if (preparedStatement2.executeUpdate() > 0) {
									preparedStatement3 = connection.prepareStatement(Queryconstants.getAllocationsUsingProgrammeAndVoucher);
									preparedStatement3.setInt(1,resultSet1.getInt("programmeId"));
									preparedStatement3.setInt(2, voucherId);
									resultSet3 = preparedStatement3.executeQuery();
									while(resultSet3.next()) {
										preparedStatement4 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
										preparedStatement4.setInt(1, resultSet1.getInt("programmeId"));
										preparedStatement4.setString(2, resultSet3.getString("card_number"));
										preparedStatement4.setInt(3, resultSet3.getInt("acc_id"));
										preparedStatement4.setString(4, resultSet3.getString("acc_no"));
										preparedStatement4.setInt(5, voucherId);
										preparedStatement4.setInt(6, voucher.services.get(i).serviceId);
										preparedStatement4.setDouble(7, resultSet3.getDouble("basket_value"));
										preparedStatement4.setDouble(8, resultSet3.getDouble("basket_value"));
										preparedStatement4.setDouble(9, resultSet3.getDouble("basket_balance"));
										preparedStatement4.setDouble(10, resultSet3.getDouble("basket_balance"));
		
										preparedStatement4.setString(11, "2017");
										preparedStatement4.setDouble(12, voucher.createdBy);
										preparedStatement4.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
										preparedStatement4.executeUpdate();	
									}
								}
								else {
									connection.rollback();
									throw new Exception("Failed to Update Basket Id "	+ voucherId);
								}									
							}
						}
						else {
							connection.rollback();
							throw new Exception("Failed to Insert Service Id "	+ voucher.services.get(i).serviceId);
						}
					}
					if(!voucher.services.get(i).isActive && voucher.services.get(i).isRemoved) {
						System.out.print("RemovedService is :"+voucher.services.get(i).serviceId+"\n");
						preparedStatement5 = connection.prepareStatement(Queryconstants.deleteVoucherServiceById);
						preparedStatement5.setInt(1, voucherId);
						preparedStatement5.setInt(2, voucher.services.get(i).serviceId);
						if(preparedStatement5.executeUpdate() > 0) {
							preparedStatement6 = connection.prepareStatement(Queryconstants.getProgrammeIdsUsingVoucher);
							preparedStatement6.setInt(1, voucherId);
							resultSet4 = preparedStatement6.executeQuery();
							while(resultSet4.next()) {
								preparedStatement6 = connection.prepareStatement(Queryconstants.deleteProgrammevoucherDetails);
								preparedStatement6.setInt(1, resultSet4.getInt("programmeId"));
								preparedStatement6.setInt(2, voucherId);
								preparedStatement6.setInt(3, voucher.services.get(i).serviceId);
								if(preparedStatement6.executeUpdate() > 0) {
									preparedStatement7 = connection.prepareStatement(Queryconstants.deleteServicesFromAllocations);
									preparedStatement7.setInt(1, resultSet4.getInt("programmeId"));
									preparedStatement7.setInt(2, voucherId);
									preparedStatement7.setInt(3, voucher.services.get(i).serviceId);
									preparedStatement7.executeUpdate(); 
								}
								else {
									connection.rollback();
									throw new Exception("Failed to delete ProgrammeDetails Id "	+ voucher.services.get(i).serviceId);
								}
							}
						}
						else {
							connection.rollback();
							throw new Exception("Failed to delete Service Id "	+ voucher.services.get(i).serviceId);
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

	public List<Voucher> GetAllVouchers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getVouchers);
			resultSet = preparedStatement.executeQuery();
			List<Voucher> objVoucher = new ArrayList<Voucher>();
			while (resultSet.next()) {
				Voucher voucher = new Voucher();
				voucher.count = count;
				voucher.voucherId = resultSet.getInt("ID");
				voucher.voucherCode = resultSet.getString("voucher_Code");
				voucher.voucherDesc = resultSet.getString("voucher_Name");
				voucher.voucherType = resultSet.getString("voucher_type");
				voucher.voucherValue = resultSet.getDouble("voucher_value");
				voucher.coverType = resultSet.getString("cover_type");
				voucher.active = resultSet.getBoolean("active");
				voucher.createdBy = resultSet.getInt("Created_By");
				voucher.startDate = resultSet.getString("start_date");
				voucher.endDate = resultSet.getString("end_date");
				voucher.schemeType = resultSet.getString("scheme_type");
				voucher.expanded=false;
				List<Service> services = new ArrayList<Service>();
				if(voucher.voucherType.equalsIgnoreCase("HB")){
					preparedStatement = connection.prepareStatement(Queryconstants.getHealthServiceByid);
					preparedStatement.setInt(1, voucher.voucherId);
					preparedStatement.setInt(2, voucher.voucherId);
					preparedStatement.setInt(3, voucher.voucherId);
					resultSet2 = preparedStatement.executeQuery();
					
					while (resultSet2.next()) {
						services.add(new Service(resultSet2.getInt("Service_Id"),
								resultSet2.getString("ser_Name"), resultSet2
										.getBoolean("Isactive"), resultSet2
										.getInt("Created_BY"), 200, resultSet2
										.getDouble("quantity"), resultSet2
										.getDouble("service_value"), resultSet2
										.getString("ser_subtype_id"),resultSet2
										.getString("type")));
					}
				}else{
				preparedStatement = connection
						.prepareStatement(Queryconstants.getServicesByVoucherId);
				preparedStatement.setInt(1, voucher.voucherId);
				preparedStatement.setInt(2, voucher.voucherId);
				resultSet2 = preparedStatement.executeQuery();
				//List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					services.add(new Service(resultSet2.getInt("Service_Id"),
							resultSet2.getString("serviceName"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getInt("Created_BY"), 200, resultSet2
									.getDouble("quantity"), resultSet2
									.getDouble("service_value"), resultSet2
									.getString("compoName"),resultSet2
									.getString("type")));
				}
				}
				voucher.services = services;
				objVoucher.add(voucher);
				count ++;
			}
			return objVoucher;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Voucher> GetAllVouchersByOrg(int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getVouchers);
			//preparedStatement.setInt(1, orgId);
			resultSet = preparedStatement.executeQuery();
			List<Voucher> objVoucher = new ArrayList<Voucher>();
			while (resultSet.next()) {
				Voucher voucher = new Voucher();
				voucher.voucherId = resultSet.getInt("ID");
				voucher.voucherCode = resultSet.getString("voucher_Code");
				voucher.voucherDesc = resultSet.getString("voucher_Name");
				voucher.voucherType = resultSet.getString("voucher_type");
				voucher.voucherValue = resultSet.getDouble("voucher_value");
				voucher.active = resultSet.getBoolean("active");
				voucher.createdBy = resultSet.getInt("Created_By");
				voucher.startDate = resultSet.getString("start_date");
				voucher.endDate = resultSet.getString("end_date");
				voucher.expanded=false;
				List<Service> services = new ArrayList<Service>();
				if(voucher.voucherType.equalsIgnoreCase("HB")){
					preparedStatement = connection.prepareStatement(Queryconstants.getHealthServiceByid);
					preparedStatement.setInt(1, voucher.voucherId);
					preparedStatement.setInt(2, voucher.voucherId);
					preparedStatement.setInt(3, voucher.voucherId);
					resultSet2 = preparedStatement.executeQuery();
					
					while (resultSet2.next()) {
						if(resultSet2.getBoolean("Isactive")) {
						services.add(new Service(resultSet2.getInt("Service_Id"),
								resultSet2.getString("ser_Name"), resultSet2
										.getBoolean("Isactive"), resultSet2
										.getInt("Created_BY"), 200, resultSet2
										.getDouble("quantity"), resultSet2
										.getDouble("service_value"), resultSet2
										.getString("ser_subtype_id"),resultSet2
										.getString("type")));
					}
					}
				}else{
				preparedStatement = connection
						.prepareStatement(Queryconstants.getServicesByVoucherId);
				preparedStatement.setInt(1, voucher.voucherId);
				preparedStatement.setInt(2, voucher.voucherId);
				resultSet2 = preparedStatement.executeQuery();
				//List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					services.add(new Service(resultSet2.getInt("Service_Id"),
							resultSet2.getString("serviceName"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getInt("Created_BY"), 200, resultSet2
									.getDouble("quantity"), resultSet2
									.getDouble("service_value"), resultSet2
									.getString("compoName"),resultSet2
									.getString("type")));
				}
				}
				voucher.services = services;
				objVoucher.add(voucher);
			}
			return objVoucher;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	
	public List<Voucher> GetCashVouchers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCashVouchers);

			resultSet = preparedStatement.executeQuery();
			List<Voucher> objVoucher = new ArrayList<Voucher>();
			while (resultSet.next()) {
				Voucher voucher = new Voucher();
				voucher.voucherId = resultSet.getInt("ID");
				voucher.voucherCode = resultSet.getString("voucher_Code");
				voucher.voucherDesc = resultSet.getString("voucher_Name");
				voucher.voucherType = resultSet.getString("voucher_type");
				voucher.voucherValue = resultSet.getDouble("voucher_value");
				voucher.active = resultSet.getBoolean("active");
				voucher.createdBy = resultSet.getInt("Created_By");
				voucher.startDate = resultSet.getString("start_date");
				voucher.endDate = resultSet.getString("end_date");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getServicesByVoucherId);
				preparedStatement.setInt(1, voucher.voucherId);
				preparedStatement.setInt(2, voucher.voucherId);
				resultSet2 = preparedStatement.executeQuery();
				List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					services.add(new Service(resultSet2.getInt("Service_Id"),
							resultSet2.getString("serviceName"), resultSet2
									.getBoolean("Isactive"), resultSet2
									.getInt("Created_BY"), 200, resultSet2
									.getDouble("quantity"), resultSet2
									.getDouble("service_value"), resultSet2
									.getString("compoName"),resultSet2
									.getString("type")));
				}
				voucher.services = services;
				objVoucher.add(voucher);
			}
			return objVoucher;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckVoucherByName(String voucherName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVoucherByName);
			preparedStatement.setString(1, voucherName);
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
	public boolean CheckVoucherByCode(String voucherCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getVoucherByCode);
			preparedStatement.setString(1, voucherCode);
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
	public boolean CheckVoucherNameByCode(String voucherName,String voucherCode, int voucherId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getVoucherNameByCode);
			preparedStatement.setString(1, voucherName);
			preparedStatement.setString(2, voucherCode);
			preparedStatement.setInt(3, voucherId);
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
	
	public String genRandInt() {
		return "B"+ (int) (Math.random()*899);
	}
}

package com.compulynx.compas.dal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.logging.Logger;

import com.compulynx.compas.dal.UploadmemberDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Upmember;
import com.compulynx.compas.models.Voucher;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GeneralUtility;
import com.compulynx.compas.models.Service;

public class UploadmemberDalImpl implements UploadmemberDal {
	
	private static final Logger logger = Logger.getLogger(CustomerDalImpl.class.getName());
	
	private DataSource dataSource;
	private int progId;
	private int prodId;
	private String by;

	public UploadmemberDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public CompasResponse UploadMember(String filePath, int orgId, int productId, int programmeId, String createdBy) {
		prodId = productId;
		progId = programmeId;
		by = createdBy;
		System.out.println("progId: " + progId);
		try {
			ArrayList<ArrayList<Upmember>> list = getMembersExcel(filePath, orgId, createdBy);
			if (uploadMembers(list)) {

				return new CompasResponse(200, "Uploaded Successfully");
			} else {
				return new CompasResponse(201, "Oops! No Records to upload");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new CompasResponse(201, "Error. Could not process uploaded document.");
		}

	}

	// response message dependent on multiple db transations, hence in dal. refactor
	// excel reading to bal higher abs class though
	// check api for overwritten compas response messages
	private ArrayList<ArrayList<Upmember>> getMembersExcel(String pathToFile, int orgid, String createdBy)
			throws IOException {
		ArrayList<ArrayList<Upmember>> Members = new ArrayList<ArrayList<Upmember>>();
		Members.add(new ArrayList<Upmember>());
		Members.add(new ArrayList<Upmember>());
		System.out.println("CreatedBy2:: " + createdBy);
		try {
			// System.out.println("FileNameInUploadExcel##" + pathToFile);
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			workbook.getNumberOfSheets();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			// System.out.println("Number of rows>>" + rows);
			DataFormatter df = new DataFormatter();
			// last row has no organization
			for (int r = 1; r < rows; r++) {
				
				// //System.out.println("Row>>" + r);
				XSSFRow row = sheet.getRow(r);
				XSSFCell cell;
				Upmember Member = new Upmember();
				if (df.formatCellValue(row.getCell(0))==null || df.formatCellValue(row.getCell(0)) =="") {
					break;
					
				}else {
				Member.organization = df.formatCellValue(row.getCell(0));
				Member.fullName = df.formatCellValue(row.getCell(1));
				Member.principalApplicant = df.formatCellValue(row.getCell(2));
				Member.memberNo = df.formatCellValue(row.getCell(3));
				Member.nhifNo = df.formatCellValue(row.getCell(4));
				Member.dateOfBirth = df.formatCellValue(row.getCell(5));
				Member.gender = df.formatCellValue(row.getCell(6));
				Member.memberType = df.formatCellValue(row.getCell(7)).substring(0, 1);
				Member.orgId = orgid;
				Member.createdBy = Integer.parseInt(createdBy);
				// //System.out.println(Member);
				if (Member.memberType.equalsIgnoreCase("P")) {
					System.out.println("we got principal: " + r);
					Member.relation = "0";
					Members.get(0).add(Member);
				} else {
					if (Member.memberType.equalsIgnoreCase("S")) {
						System.out.println("we got spouse: " + r);
						Member.relation = "1";
					} else if (Member.memberType.equalsIgnoreCase("C")) {
						System.out.println("we got child: " + r);
						Member.relation = "2";
					}
					Member.memberType = "D";
					Members.get(1).add(Member);
				}
				}
			}
			// System.out.println("Number of rows##" + rows);
			file.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
		return Members;
	}

	public boolean uploadMembers(ArrayList<ArrayList<Upmember>> listMembers) {
		boolean done = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		int accountId = -1;
		int memberId = -1;
		String cardNumber = "";
		try {
			connection = dataSource.getConnection();
			// first add principle
			for (Upmember member : listMembers.get(0)) {
				System.out.println("Member Number" + member.memberNo);
				preparedStatement = connection.prepareStatement(Queryconstants.insertUpMemberMaster2,
						Statement.RETURN_GENERATED_KEYS);

				if (true) {
					preparedStatement.setString(1, GeneralUtility.splitPJ(member.memberNo));
					preparedStatement.setString(2, GeneralUtility.splitMemberNo(member.memberNo));
					preparedStatement.setString(3, member.memberNo);
					preparedStatement.setString(4, member.fullName.split(" ")[0]);
					preparedStatement.setString(5, member.fullName.split(" ")[member.fullName.split(" ").length - 1]);
					preparedStatement.setString(6, member.fullName);
					preparedStatement.setString(7, member.dateOfBirth);
					preparedStatement.setString(8, member.gender);
					preparedStatement.setString(9, member.nhifNo);
					preparedStatement.setBoolean(10, true);
					preparedStatement.setInt(11, member.orgId);
					preparedStatement.setTimestamp(12, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setString(13, member.memberType.substring(0, 1));
					preparedStatement.setString(14, member.memberNo);
					preparedStatement.setString(15, member.relation);
					preparedStatement.setInt(16, prodId);
					preparedStatement.setInt(17, member.createdBy);
					// boolean updated =
					int updated = preparedStatement.executeUpdate();

					if (true) {

						//insert audit trail
						preparedStatement1 = connection.prepareStatement(Queryconstants.insertAuditTrail);
						preparedStatement1.setInt(1, member.createdBy);
						preparedStatement1.setInt(2, member.createdBy);
						preparedStatement1.setString(3, "Uploaded Member List");
						preparedStatement1.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement1.setString(5, "LCT Member Upload Module");
						if (preparedStatement1.executeUpdate() <= 0) {
							connection.rollback();			
							return false;
					}
						logger.info("Audit Trail for " +member.memberNo+ " on member upload module captured");
						
						ResultSet rs = preparedStatement.getGeneratedKeys();
						rs.next();
						memberId = rs.getInt(1);
						member.memberId = memberId;
						PreparedStatement preparedStatement4 = connection
								.prepareStatement(Queryconstants.getMaxMemberAccount);
						ResultSet resultSet2 = preparedStatement4.executeQuery();
						if (resultSet2.next()) {
							member.accNo = resultSet2.getInt("maxaccno") + 1;
							DBOperations.DisposeSql(preparedStatement, rs);
							PreparedStatement preparedStatement5 = connection.prepareStatement(
									Queryconstants.insertMemberAccDtl, Statement.RETURN_GENERATED_KEYS);
							preparedStatement5.setInt(1, member.memberId);
							preparedStatement5.setInt(2, member.accNo);
							preparedStatement5.setString(3, "NA");
							preparedStatement5.setDouble(4, member.accBalance);
							preparedStatement5.setInt(5, member.createdBy);
							preparedStatement5.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
							preparedStatement5.setString(7, "N");

							if (preparedStatement5.executeUpdate() > 0) {
								rs = preparedStatement5.getGeneratedKeys();
								rs.next();
								accountId = rs.getInt(1);
								cardNumber = "804400" + generateNumber();
								preparedStatement4 = connection.prepareStatement(Queryconstants.insertCardDetails);
								preparedStatement4.setString(1, String.valueOf(accountId));
								preparedStatement4.setString(2, String.valueOf(memberId));
								preparedStatement4.setString(3, member.memberNo);
								preparedStatement4.setString(4, cardNumber);
								preparedStatement4.setString(5, member.memberNo);
								preparedStatement4.setString(6, "I");
								preparedStatement4.setInt(7, member.createdBy);
								preparedStatement4.setTimestamp(8,
										new java.sql.Timestamp(new java.util.Date().getTime()));
								preparedStatement4.executeUpdate();
							} else {
								connection.rollback();
								// return new CompasResponse(202, "Failed To Update Account Details for
								// Member");
							}

						}
					}

					member.cardNumber = "804400" + generateNumber();
					// insert member programme
					PreparedStatement preparedStatement7 = connection
							.prepareStatement(Queryconstants.insertCustomerProgramme2, Statement.RETURN_GENERATED_KEYS);
					// for (int i = 0; i < member.programmes.size(); i++) {
					preparedStatement7.setInt(1, memberId);
					preparedStatement7.setInt(2, progId);
					preparedStatement7.setDouble(3, 0);
					preparedStatement7.setBoolean(4, true);
					preparedStatement7.setInt(5, 1);
					preparedStatement7.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
					if (preparedStatement7.executeUpdate() > 0) {
						ArrayList<Voucher> vouchers = new ArrayList<>();
						PreparedStatement preparedStatement10 = connection
								.prepareStatement(Queryconstants.getVouchersByProgrammeId2);
						preparedStatement10.setInt(1, progId);
						ResultSet rs10 = preparedStatement10.executeQuery();
						while (rs10.next()) {
							Voucher voucher = new Voucher();
							voucher.voucherId = rs10.getInt("voucherId");
							vouchers.add(voucher);
						}
						for (int j = 0; j < vouchers.size(); j++) {
							Voucher voucher = vouchers.get(j);
							ArrayList<Service> services = new ArrayList<>();
							PreparedStatement preparedStatement11 = connection
									.prepareStatement(Queryconstants.getServicesByVoucherId2);
							preparedStatement11.setInt(1, voucher.voucherId);
							ResultSet rs11 = preparedStatement11.executeQuery();
							while (rs11.next()) {
								Service service = new Service();
								service.serviceId = rs11.getInt("service_id");
								PreparedStatement preparedStatement12 = connection
										.prepareStatement(Queryconstants.getProgrammeServiceBasketValue);
								preparedStatement12.setInt(1, progId);
								preparedStatement12.setInt(2, service.serviceId);
								ResultSet rs12 = preparedStatement12.executeQuery();
								while (rs12.next()) {
									service.serviceValue = rs12.getDouble("basket_value");
									service.serviceBalance = service.serviceValue;
									services.add(service);
								}
							}
							for (int k = 0; k < services.size(); k++) {
								PreparedStatement preparedStatement8 = connection
										.prepareStatement(Queryconstants.insertMemberVouchers);
								Service currService = services.get(k);
								preparedStatement8.setInt(1, progId);
								preparedStatement8.setString(2, cardNumber);
								preparedStatement8.setInt(3, accountId);
								preparedStatement8.setInt(4, member.accNo);
								preparedStatement8.setInt(5, voucher.voucherId);
								preparedStatement8.setInt(6, currService.serviceId);
								preparedStatement8.setDouble(7, currService.serviceValue);
								preparedStatement8.setDouble(8, currService.serviceValue);// member.programmes.get(i).vouchers.get(j).services.get(k).price);
								preparedStatement8.setDouble(9, currService.serviceBalance);
								preparedStatement8.setDouble(10, currService.serviceBalance);

								// cycle of basket allocation hardcoded. Need to pass java datestamp function
								preparedStatement8.setString(11,
										(String.valueOf(new java.util.Date().getYear() + 1900)));
								preparedStatement8.setDouble(12, member.createdBy);
								preparedStatement8.setTimestamp(13,
										new java.sql.Timestamp(new java.util.Date().getTime()));
								/*
								 * //System.out.println("prodId: "+prodId); preparedStatement8.setInt(7, 6);
								 * preparedStatement8.setInt(8, 30); preparedStatement8.setInt(9, 0);
								 * preparedStatement8.setInt(10, 0); preparedStatement8.setInt(11, 0);
								 * preparedStatement8.setInt(12, 0); preparedStatement8.setString(13, "2017");
								 */
								preparedStatement8.executeUpdate();
							}
						}
						// }
						// }
					} else {
						connection.rollback();
						// return new CompasResponse(202, "Failed To Update Category Details for
						// Member");
					}
					// }
					//// System.out.println("updated rows: "+updated);

				}
			}

			// then add dependant
			PreparedStatement preparedStatement30 = connection.prepareStatement(Queryconstants.insertUpMemberMaster3,
					Statement.RETURN_GENERATED_KEYS);
			for (Upmember member1 : listMembers.get(1)) {
				int orgId1 = member1.orgId;
				System.out.println("Member1 Number" + member1.memberNo);
				if (true) {
					if (orgId1 != -1) {
						PreparedStatement preparedStatement112 = connection
								.prepareStatement(Queryconstants.getPrincipleId);
						System.out.println("member no: " + member1.memberNo.split("-")[0]);
						preparedStatement112.setString(1, member1.memberNo.split("-")[0]);
						int principleId = -1;
						resultSet = preparedStatement112.executeQuery();
						if (resultSet.next()) {
							principleId = resultSet.getInt("emp_id");
						}
						System.out.println("Principal ID:: " + principleId);
						preparedStatement30.setString(1, GeneralUtility.splitPJ(member1.memberNo));
						preparedStatement30.setString(2, GeneralUtility.splitMemberNo(member1.memberNo));
						preparedStatement30.setString(3, member1.memberNo);
						preparedStatement30.setString(4, member1.fullName.split(" ")[0]);
						preparedStatement30.setString(5,
								member1.fullName.split(" ")[member1.fullName.split(" ").length - 1]);
						preparedStatement30.setString(6, member1.fullName);
						preparedStatement30.setString(7, member1.dateOfBirth);
						preparedStatement30.setString(8, member1.gender);
						preparedStatement30.setString(9, member1.nhifNo);
						preparedStatement30.setBoolean(10, true);
						preparedStatement30.setInt(11, orgId1);
						preparedStatement30.setTimestamp(12, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement30.setString(13, member1.memberType.substring(0, 1));
						preparedStatement30.setString(14, member1.memberNo);
						preparedStatement30.setString(15, member1.relation);
						preparedStatement30.setInt(16, principleId);
						preparedStatement30.setInt(17, prodId);
						preparedStatement30.setInt(18, member1.createdBy);
						// boolean updated =
						int updated = preparedStatement30.executeUpdate();

						if (true) {
							ResultSet rs = preparedStatement30.getGeneratedKeys();
							rs.next();
							memberId = rs.getInt(1);
							member1.memberId = memberId;
							PreparedStatement preparedStatement34 = connection
									.prepareStatement(Queryconstants.getMaxMemberAccount);
							ResultSet resultSet2 = preparedStatement34.executeQuery();
							if (resultSet2.next()) {
								member1.accNo = resultSet2.getInt("maxaccno") + 1;
								DBOperations.DisposeSql(preparedStatement, rs);
								PreparedStatement preparedStatement35 = connection.prepareStatement(
										Queryconstants.insertMemberAccDtl, Statement.RETURN_GENERATED_KEYS);
								;
								preparedStatement35.setInt(1, member1.memberId);
								preparedStatement35.setInt(2, member1.accNo);
								preparedStatement35.setString(3, "NA");
								preparedStatement35.setDouble(4, member1.accBalance);
								preparedStatement35.setInt(5, member1.createdBy);
								preparedStatement35.setTimestamp(6,
										new java.sql.Timestamp(new java.util.Date().getTime()));
								preparedStatement35.setString(7, "N");
								preparedStatement35.executeUpdate();

								if (preparedStatement35.executeUpdate() > 0) {
									rs = preparedStatement35.getGeneratedKeys();
									rs.next();
									accountId = rs.getInt(1);
									cardNumber = "804400" + generateNumber();
									PreparedStatement preparedStatement40 = connection
											.prepareStatement(Queryconstants.insertCardDetails);
									preparedStatement40.setString(1, String.valueOf(accountId));
									preparedStatement40.setString(2, String.valueOf(memberId));
									preparedStatement40.setString(3, member1.memberNo);
									preparedStatement40.setString(4, cardNumber);
									preparedStatement40.setString(5, member1.memberNo);
									preparedStatement40.setString(6, "I");
									preparedStatement40.setInt(7, member1.createdBy);
									preparedStatement40.setTimestamp(8,
											new java.sql.Timestamp(new java.util.Date().getTime()));
									preparedStatement40.executeUpdate();
								} else {
									connection.rollback();
									// return new CompasResponse(202, "Failed To Update Account Details for
									// Member");
								}
							}

							// }
							// }

							/*
							 * member1.cardNumber = "804400" + generateNumber(); // insert member programme
							 * PreparedStatement preparedStatement70 =
							 * connection.prepareStatement(Queryconstants.insertCustomerProgramme2,
							 * Statement.RETURN_GENERATED_KEYS); //for (int i = 0; i <
							 * member.programmes.size(); i++) { preparedStatement70.setInt(1, memberId);
							 * preparedStatement70.setInt(2, progId); preparedStatement70.setDouble(3, 0);
							 * preparedStatement70.setBoolean(4, true); preparedStatement70.setInt(5, 1);
							 * preparedStatement70.setTimestamp(6, new java.sql.Timestamp(new
							 * java.util.Date().getTime())); if (preparedStatement70.executeUpdate() > 0) {
							 * ArrayList<Voucher> vouchers = new ArrayList<>(); PreparedStatement
							 * preparedStatement80 =
							 * connection.prepareStatement(Queryconstants.getVouchersByProgrammeId2);
							 * preparedStatement80.setInt(1, progId); ResultSet rs10 =
							 * preparedStatement80.executeQuery(); while(rs10.next()) { Voucher voucher =new
							 * Voucher(); voucher.voucherId = rs10.getInt("voucherId");
							 * vouchers.add(voucher); } for (int j = 0; j < vouchers.size(); j++) { Voucher
							 * voucher = vouchers.get(j); ArrayList<Service> services = new ArrayList<>();
							 * PreparedStatement preparedStatement81 =
							 * connection.prepareStatement(Queryconstants.getServicesByVoucherId2);
							 * preparedStatement81.setInt(1, voucher.voucherId); ResultSet rs11 =
							 * preparedStatement81.executeQuery(); while(rs11.next()) { Service service =
							 * new Service(); service.serviceId = rs11.getInt("service_id");
							 * PreparedStatement preparedStatement82 =
							 * connection.prepareStatement(Queryconstants.getProgrammeServiceBasketValue);
							 * preparedStatement82.setInt(1, progId); preparedStatement82.setInt(2,
							 * service.serviceId); ResultSet rs12 = preparedStatement82.executeQuery();
							 * while(rs12.next()) { service.serviceValue = rs12.getDouble("basket_value");
							 * service.serviceBalance = service.serviceValue; services.add(service); } }
							 * for(int k = 0; k < services.size(); k++) { PreparedStatement
							 * preparedStatement88 =
							 * connection.prepareStatement(Queryconstants.insertMemberVouchers); Service
							 * currService = services.get(k); preparedStatement88.setInt(1, progId);
							 * preparedStatement88.setString(2, cardNumber); preparedStatement88.setInt(3,
							 * accountId); preparedStatement88.setInt(4, member1.accNo);
							 * preparedStatement88.setInt(5, voucher.voucherId);
							 * preparedStatement88.setInt(6, currService.serviceId);
							 * preparedStatement88.setDouble(7, currService.serviceValue);
							 * preparedStatement88.setDouble(8,
							 * currService.serviceValue);//member.programmes.get(i).vouchers.get(j).services
							 * .get(k).price); preparedStatement88.setDouble(9, currService.serviceBalance);
							 * preparedStatement88.setDouble(10, currService.serviceBalance);
							 * 
							 * preparedStatement88.setString(11, "2017"); preparedStatement88.setDouble(12,
							 * 1); preparedStatement88.setTimestamp(13, new java.sql.Timestamp(new
							 * java.util.Date().getTime())); preparedStatement88.executeUpdate(); } } //}
							 * //}
							 */
						}
						/*
						 * else { connection.rollback(); //return new CompasResponse(202,
						 * "Failed To Update Category Details for Member"); }
						 */

					}
				}
			}
			// connection.commit();
			done = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// DisposeSql(connection, preparedStatement, resultSet);
		}

		return done;
	}

	public long generateNumber() {
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return number;// (long) (Math.random() * 100000 + 4444300000L);
	}

}

/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.compulynx.compas.dal.BeneficicaryUploadDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.BeneficiaryFamilyMembers;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GeneralUtility;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.MemberCard;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public class BeneficiaryUploadDalImpl implements BeneficicaryUploadDal {
	private static final Logger logger = Logger
			.getLogger(BeneficiaryUploadDalImpl.class.getName());
	private DataSource dataSource;

	public BeneficiaryUploadDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.compulynx.compas.dal.BeneficicaryUploadDal#UploadAccount(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public CompasResponse UploadAccount(String filePath, int orgId, int productId, int programmeId, String uploadedBy) {
		List<Member> list = new ArrayList<Member>();
		List<Member> toUpload = new ArrayList<Member>();
		try {
			if(ValidateMemberExcelSheet(filePath).respCode == 200) {
			list = getAccountsExcel(filePath, orgId, productId, programmeId, uploadedBy);

			for (Member detail : list) {
				toUpload.add(detail);
			}
			if (toUpload.size() > 0) {
				int in = 0;
				int out = 0;
				for (Member detail : toUpload) {
					if (UpdateImportBeneficiary(detail).respCode != 200) {
						out+=1;
						continue;
					}else {
						in+=1;
						continue;
					}
				}
				if(in>0)
					return new CompasResponse(200, in+" Members Uploaded "+out+" members failed to upload");
				else 
					return new CompasResponse(205, out+" Members failed to upload");
			} else {
				return new CompasResponse(201, "Oops! No Records to upload");
			}
			}
			else {
				return ValidateMemberExcelSheet(filePath);
			}

		} catch (Exception ex) {
			return new CompasResponse(201, "Server Error occurred, Please try again");
		}
	}

	private ArrayList<Member> getAccountsExcel(String pathToFile, int orgId, int productId, int programmeId, String createdBy) throws IOException {
		ArrayList<Member> bnfObjs = new ArrayList<Member>();
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
			List<BeneficiaryFamilyMembers> familyMemList = new ArrayList<BeneficiaryFamilyMembers>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("ProgrammeCode");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.productId = resultSet.getInt("Productid");
				programme.productName = resultSet.getString("product_name");
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				String startDate = sdf.format(sdf.parse(resultSet.getString("start_date")));
//				String endDate = sdf.format(sdf.parse(resultSet.getString("end_date")));
//				programme.startDate = startDate;
//				programme.endDate = endDate;
				if(resultSet.getInt("ID") == programmeId) {
					programme.isActive = true;
				}
				else {
				programme.active = resultSet.getBoolean("active");
				}
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
					preparedStatement = connection.prepareStatement(Queryconstants.getVouchersByProgrammeIdNew);
				}
				preparedStatement.setInt(1, resultSet.getInt("ID"));
				resultSet2 = preparedStatement.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				while (resultSet2.next()) {
					preparedStatement1 = connection.prepareStatement(Queryconstants.getServicesByBasket);
					preparedStatement1.setInt(1,resultSet2.getInt("voucherId"));
					preparedStatement1.setInt(2,resultSet.getInt("ID"));
					preparedStatement1.setInt(3,resultSet2.getInt("voucherId"));
					preparedStatement1.setInt(4,resultSet.getInt("ID"));
					preparedStatement1.setInt(5,resultSet2.getInt("voucherId"));
					resultSet3=preparedStatement1.executeQuery();
					List<Service>  services= new ArrayList<Service>();
					while(resultSet3.next()){
						Service service= new Service();
						service.requireAuth=resultSet3.getBoolean("require_auth");
						service.serviceId = resultSet3.getInt("Service_Id");
						service.serviceName = resultSet3.getString("ser_name");
						if(resultSet3.getDouble("cover_limit") == 0) {
							service.serviceBalance = resultSet3.getDouble("basket_value");
							service.serviceValue = resultSet3.getDouble("basket_value");
						}else {
							service.serviceBalance = resultSet3.getDouble("cover_limit");
							service.serviceValue = resultSet3.getDouble("cover_limit");
						}
						service.price = resultSet3.getDouble("basket_value");
						services.add(service);
					}
					
					Voucher voucher = new Voucher();
					voucher.voucherId = resultSet2.getInt("voucherId");
					voucher.voucherDesc = resultSet2.getString("voucher_Name");
					voucher.isActive = resultSet2.getBoolean("Isactive");
					voucher.voucherValue = resultSet2.getDouble("basket_value");
					voucher.frqSelect = resultSet2.getString("frqmode");
					voucher.modeSelect = resultSet2.getString("frq_type");
					voucher.coverType = resultSet2.getString("cover_type");
					voucher.services=services;
					vouchers.add(voucher);
				}
				programme.vouchers = vouchers;
				objProgrammes.add(programme);
			}
			System.out.println("FileNameInUploadExcel##" + pathToFile);
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// XSSFSheet sheet = workbook.getSheetAt(0);
			workbook.getNumberOfSheets();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows>>" + rows);
			DataFormatter df = new DataFormatter();
			for (int r = 1; r < rows; r++) {
				System.out.println("Row>>" + r);
				XSSFRow row = sheet.getRow(r);
				XSSFCell cell;
				// cell= row.getCell(0);
				// System.out.println("cell0>>"+cell);

				Member beneficiary = new Member();
				beneficiary.memberId = 0;
				beneficiary.memberNo = df.formatCellValue(row.getCell(0));
				beneficiary.firstName = df.formatCellValue(row.getCell(1));
				beneficiary.lastName = df.formatCellValue(row.getCell(2));
				beneficiary.fullName = df.formatCellValue(row.getCell(3));
				beneficiary.idPassPortNo = df.formatCellValue(row.getCell(4));
				beneficiary.dateOfBirth = df.formatCellValue(row.getCell(5));
				beneficiary.gender = df.formatCellValue(row.getCell(6));
				beneficiary.cellPhone= df.formatCellValue(row.getCell(7));
				beneficiary.email = df.formatCellValue(row.getCell(8));
				beneficiary.physicalAdd = df.formatCellValue(row.getCell(9));
				beneficiary.nhifNo = df.formatCellValue(row.getCell(10));
				beneficiary.relation = String.valueOf(df.formatCellValue(row.getCell(11)));
				beneficiary.principle = df.formatCellValue(row.getCell(12));
				beneficiary.active = true;
				beneficiary.orgId = orgId;
				beneficiary.productId = productId;
				beneficiary.createdBy = Integer.valueOf(createdBy);
				beneficiary.programmes = objProgrammes;
				beneficiary.familyMemList = familyMemList;
				bnfObjs.add(beneficiary);
				// }
				

			}
			System.out.println("Number of rows##" + rows);
			file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bnfObjs;
	}

	public CompasResponse ValidateCustomerCode(String supplierCode, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			logger.info("Beneficicary Code Validate: " + supplierCode);
			connection = dataSource.getConnection();
			// preparedStatement =
			// connection.prepareStatement(Queryconstants.validateSupplierCode);
			preparedStatement.setString(1, supplierCode);
			preparedStatement.setInt(2, id);
			resultSet = preparedStatement.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt("count");
				logger.info("This is count>>" + count);
				if (count > 0) {
					return new CompasResponse(202,	"Beneficiary Code is Already Taken");
				}
			}
			return new CompasResponse(200, "Code valid");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(201,	"Server Error occurred, Please try again");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean GetMemberByNo(String memberNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkMemberexists);
			preparedStatement.setString(1, memberNo);

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
	public long generateNumber() {
		return (long) (Math.random() * 100000 + 3333300000L);
	}
	public CompasResponse UpdateImportBeneficiary(Member member) {

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
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet6 = null;
		ResultSet resultSet8 = null;
		ResultSet rs = null;
		int memberId = 0;
		int accountId = 0;
		String cardNumber = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			String example = "SGVsbG8gV29ybGQ=";
			byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(example.getBytes());
			// System.out.println(decoded);
			if (member.memberId == 0) {
				if (GetMemberByNo(member.memberNo)) {
					return new CompasResponse(201,	"Member Number already Exists ");
				} else {
					if(member.relation.equals(0)) {
						preparedStatement = connection.prepareStatement(Queryconstants.insertMemberMaster,Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, GeneralUtility.splitPJ(member.memberNo));
						// preparedStatement.setInt(2,
						// member.departmentId);
						preparedStatement.setString(2, GeneralUtility.splitMemberNo(member.memberNo));
						preparedStatement.setString(3, member.memberNo);
						preparedStatement.setInt(4, member.productId);
						preparedStatement.setString(5, member.firstName);
						preparedStatement.setString(6, member.lastName);
						preparedStatement.setString(7, member.fullName);
						preparedStatement.setString(8, member.idPassPortNo);
						preparedStatement.setString(9, member.dateOfBirth);
						preparedStatement.setString(10, member.gender);
						preparedStatement.setString(11, member.cellPhone);
						preparedStatement.setString(12, member.email);
						preparedStatement.setString(13, member.physicalAdd);
						preparedStatement.setString(14, member.nhifNo);
						preparedStatement.setBoolean(15, member.active);
	
						preparedStatement.setString(16, member.memberPic);
						preparedStatement.setInt(17, member.orgId);
						preparedStatement.setString(18, "N");
						preparedStatement.setInt(19, member.createdBy);
						preparedStatement.setTimestamp(20, new java.sql.Timestamp(new java.util.Date().getTime()));
										
						if (preparedStatement.executeUpdate() > 0) {
	
							// Dispose
							if (member.memberId == 0) {
								rs = preparedStatement.getGeneratedKeys();
								rs.next();
								memberId = rs.getInt(1);
								member.memberId = memberId;
								preparedStatement1 = connection.prepareStatement(Queryconstants.getMaxMemberAccount);
								resultSet = preparedStatement1.executeQuery();
								if (resultSet.next()) {
									member.accNo = resultSet.getInt("maxaccno") + 1;
									DBOperations.DisposeSql(preparedStatement,rs);
									preparedStatement = connection.prepareStatement(Queryconstants.insertMemberAccDtl, Statement.RETURN_GENERATED_KEYS);;
									preparedStatement.setInt(1, member.memberId);
									preparedStatement.setInt(2, member.accNo);
									preparedStatement.setString(3, "NA");
									preparedStatement.setDouble(4, member.accBalance);
									preparedStatement.setInt(5, member.createdBy);
									preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
									preparedStatement.setString(7, "N");
									
									if(preparedStatement.executeUpdate() > 0) {
										rs=preparedStatement.getGeneratedKeys();
										rs.next();
										accountId =rs.getInt(1);
										cardNumber = "804400" + generateNumber();
								preparedStatement1 = connection.prepareStatement(Queryconstants.insertCardDetails);
								preparedStatement1.setString(1, String.valueOf(accountId));
								preparedStatement1.setString(2, String.valueOf(memberId));
								preparedStatement1.setString(3, member.memberNo);
								preparedStatement1.setString(4, cardNumber);
								preparedStatement1.setString(5, member.memberNo);
								preparedStatement1.setString(6, "I");
								preparedStatement1.setInt(7, member.createdBy);
								preparedStatement1.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
								preparedStatement1.executeUpdate();
									}
									else {
										return new CompasResponse(202,
												"Failed To Update Account Details for Member");
									}
	
								}
							}
							member.cardNumber = "804400" + generateNumber();
							DBOperations.DisposeSql(preparedStatement);
	
							DBOperations.DisposeSql(preparedStatement);
							// insert member programme
								preparedStatement = connection.prepareStatement(Queryconstants.insertCustomerProgramme, Statement.RETURN_GENERATED_KEYS);
								for (int i = 0; i < member.programmes.size(); i++) {
									preparedStatement.setInt(1, memberId);
									preparedStatement.setInt(2,	member.programmes.get(i).programmeId);
									preparedStatement.setDouble(3, member.programmes.get(i).programmeValue);
									preparedStatement.setBoolean(4,	member.programmes.get(i).isActive);
									preparedStatement.setInt(5,	member.programmes.get(i).createdBy);
									preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
									if (preparedStatement.executeUpdate() > 0) {
										rs = preparedStatement.getGeneratedKeys();
										rs.next();
										int progId = rs.getInt(1);
										if(member.programmes.get(i).isActive) {
											for (int j = 0; j < member.programmes.get(i).vouchers.size(); j++) {
												System.out.print("ProgrammeId :"+member.programmes.get(i).vouchers.get(j).voucherId+"\n");
												System.out.print("IsActive :"+member.programmes.get(i).vouchers.get(j).isActive+"\n");
												if(member.programmes.get(i).vouchers.get(j).isActive) {
													for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
														preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
														preparedStatement2.setInt(1, member.programmes.get(i).programmeId);
														preparedStatement2.setString(2, cardNumber);
														preparedStatement2.setInt(3, accountId);
														preparedStatement2.setInt(4, member.accNo);
														preparedStatement2.setInt(5, member.programmes.get(i).vouchers.get(j).voucherId);
														preparedStatement2.setInt(6, member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
														preparedStatement2.setDouble(7, member.programmes.get(i).vouchers.get(j).services.get(k).serviceValue);
														preparedStatement2.setDouble(8, member.programmes.get(i).vouchers.get(j).voucherValue);
														preparedStatement2.setDouble(9, member.programmes.get(i).vouchers.get(j).services.get(k).serviceBalance);
														preparedStatement2.setDouble(10, member.programmes.get(i).vouchers.get(j).voucherValue);
						
														preparedStatement2.setString(11, "2017");
														preparedStatement2.setDouble(12, member.createdBy);
														preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
														preparedStatement2.executeUpdate();	
													}
												}
											}
										}
									}
									else {
										connection.rollback();
										return new CompasResponse(202,
												"Failed To Update Category Details for Member");
									}
							}	
							connection.commit();
							return new CompasResponse(200, "Records Updated");
	
						} else {
							connection.rollback();
							return new CompasResponse(202, "Nothing To Update");
						}
					} else {
						preparedStatement3 = connection.prepareStatement(Queryconstants.getPrincipleDetails);
						preparedStatement3.setString(1, member.principle);
						resultSet3 = preparedStatement3.executeQuery();
						if(resultSet3.next()) {
							if(resultSet3.getInt("bnfcount") < 5) {
								connection.rollback();
								return new CompasResponse(201, "Member "+ member.principle+" has reached maximum dependants ");
							}
							preparedStatement4 = connection.prepareStatement(Queryconstants.insertNewMemberFamilyDetails, Statement.RETURN_GENERATED_KEYS);
							if (GetMemberByNo(member.memberNo)) {
								connection.rollback();
								return new CompasResponse(201, "Member Number "+ member.memberNo+" already Exists ");
							}
							preparedStatement4.setInt(1, resultSet3.getInt("member_id"));
							preparedStatement4.setString(2, GeneralUtility.splitPJ(member.memberNo));
							preparedStatement4.setString(3, GeneralUtility.splitMemberNo(member.memberNo));
							preparedStatement4.setString(4, member.memberNo);
							preparedStatement4.setString(5, member.fullName);
							preparedStatement4.setString(6, member.dateOfBirth);
							preparedStatement4.setString(7, member.gender);
							preparedStatement4.setString(8, member.relation);
							preparedStatement4.setInt(9, 1);
							preparedStatement4.setInt(10, resultSet3.getInt("product_id"));
							preparedStatement4.setInt(11, resultSet3.getInt("org_id"));
							preparedStatement4.setInt(12, member.createdBy);
							preparedStatement4.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
							if (preparedStatement4.executeUpdate() <= 0) {
								connection.rollback();
								return new CompasResponse(202,	"Failed To Update Family Details for Member");
							}
								resultSet4 = preparedStatement4.getGeneratedKeys();
								resultSet4.next();
								memberId = resultSet4.getInt(1);
								member.memberId = memberId;
								preparedStatement5 = connection.prepareStatement(Queryconstants.getMaxMemberAccount);
								resultSet5 = preparedStatement5.executeQuery();
								if (resultSet5.next()) {
									member.accNo = resultSet5.getInt("maxaccno") + 1;
									preparedStatement6 = connection.prepareStatement(Queryconstants.insertMemberAccDtl, Statement.RETURN_GENERATED_KEYS);;
									preparedStatement6.setInt(1, member.memberId);
									preparedStatement6.setInt(2, member.accNo);
									preparedStatement6.setString(3, "NA");
									preparedStatement6.setDouble(4, member.accBalance);
									preparedStatement6.setInt(5, member.createdBy);
									preparedStatement6.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
									preparedStatement6.setString(7, "N");
									
									if(preparedStatement6.executeUpdate() > 0) {
										resultSet6=preparedStatement6.getGeneratedKeys();
										resultSet6.next();
										accountId =resultSet6.getInt(1);
										cardNumber = "804400" + generateNumber();
										DBOperations.DisposeSql(preparedStatement6,resultSet6);
								preparedStatement7 = connection.prepareStatement(Queryconstants.insertCardDetails);
								preparedStatement7.setString(1, String.valueOf(accountId));
								preparedStatement7.setString(2, String.valueOf(memberId));
								preparedStatement7.setString(3, member.memberNo);
								preparedStatement7.setString(4, cardNumber);
								preparedStatement7.setString(5, member.memberNo);
								preparedStatement7.setString(6, "I");
								preparedStatement7.setInt(7, member.createdBy);
								preparedStatement7.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
								
								if(preparedStatement7.executeUpdate() <=0) {
									connection.rollback();
									return new CompasResponse(202, "Failed To Update Card Details for Member");
								}
								DBOperations.DisposeSql(preparedStatement);
								for (int f = 0; f < member.programmes.size(); f++) {
									if(member.programmes.get(f).isActive) {
										for (int j = 0; j < member.programmes.get(f).vouchers.size(); j++) {
											System.out.print(member.programmes.get(f).vouchers.get(j).coverType);
											if(member.programmes.get(f).vouchers.get(j).isActive && member.programmes.get(f).vouchers.get(j).coverType.equals("1")) {
												for(int k = 0; k < member.programmes.get(f).vouchers.get(j).services.size(); k++) {
													preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
													preparedStatement2.setInt(1, member.programmes.get(f).programmeId);
													preparedStatement2.setString(2, cardNumber);
													preparedStatement2.setInt(3, accountId);
													preparedStatement2.setInt(4, member.accNo);
													preparedStatement2.setInt(5, member.programmes.get(f).vouchers.get(j).voucherId);
													preparedStatement2.setInt(6, member.programmes.get(f).vouchers.get(j).services.get(k).serviceId);
													preparedStatement2.setDouble(7, member.programmes.get(f).vouchers.get(j).services.get(k).serviceValue);
													preparedStatement2.setDouble(8, member.programmes.get(f).vouchers.get(j).services.get(k).price);
													preparedStatement2.setDouble(9, member.programmes.get(f).vouchers.get(j).services.get(k).serviceBalance);
													preparedStatement2.setDouble(10, member.programmes.get(f).vouchers.get(j).services.get(k).price);
					
													preparedStatement2.setString(11, "2017");
													preparedStatement2.setDouble(12, member.createdBy);
													preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
													preparedStatement2.executeUpdate();	
												}
											}
										}
									}
								}
								}
								else {
									connection.rollback();
									return new CompasResponse(202, "Failed To Update Account Details for Member");
								}
							}
						connection.commit();
						return new CompasResponse(200, "Records Updated");
						}
						return new CompasResponse(202, "Principle not found");
					}
				}
			}
			else {
				connection.rollback();
				return new CompasResponse(202,
						"Failed To Update Member Details for Member");
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Sql Exception Occured");
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
	

	public CompasResponse UpdateCardIssuance(Member card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (checkCardNumber(card.cardNumber)) {
				return new CompasResponse(201, "Crad Number Already Exists");
			}

			preparedStatement = connection.prepareStatement(Queryconstants.insertCardIssuance);
			preparedStatement.setString(1, card.cardNumber);
			preparedStatement.setString(2, "1234567890");
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
			preparedStatement.setString(4, card.expiryDate);
			preparedStatement.setString(5, "I");
			preparedStatement.setInt(6, card.memberId);
			preparedStatement.setInt(7, card.programmeId);
			preparedStatement.setInt(8, card.createdBy);
			preparedStatement.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
			preparedStatement.setString(10, "1234");
			if (preparedStatement.executeUpdate() > 0) {
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement = connection.prepareStatement(Queryconstants.updateCardStatus);
				preparedStatement.setString(1, "I");
				preparedStatement.setInt(2, card.memberId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201, "Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Card Number Already Exists");
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
	public boolean checkCardNumber(String cardNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkCardNumber);
			preparedStatement.setString(1, cardNo);

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
	public CompasResponse UploadService(String filePath, String uploadedBy) {
		List<Service> list = new ArrayList<Service>();
		List<Service> toUpload = new ArrayList<Service>();
		try {
			list = getServicesExcel(filePath, uploadedBy);

			for (Service detail : list) {

				// if(validateSupplierCode(detail.supplierCode.trim(),0).respCode==202){
				// continue;
				// } else{
				toUpload.add(detail);
				// }

			}
			System.out.println("List##" + list.size());
			if (toUpload.size() > 0) {
				for (Service detail : toUpload) {

					if (UpdateService(detail).respCode != 200) {
						continue;
					}
				}

				return new CompasResponse(200, "Uploaded Successfully");
			} else {
				return new CompasResponse(201, "Oops! No Records to upload");
			}

		} catch (Exception ex) {
			return new CompasResponse(201,
					"Server Error occurred, Please try again");
		}
	}
	
	private ArrayList<Service> getServicesExcel(String pathToFile, String createdBy) throws IOException {
		ArrayList<Service> services = new ArrayList<Service>();
		try {
			System.out.println("FileNameInUploadExcel##" + pathToFile);
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			// XSSFSheet sheet = workbook.getSheetAt(0);
			workbook.getNumberOfSheets();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows>>" + rows);
			DataFormatter df = new DataFormatter();
			for (int r = 1; r < rows; r++) {
				System.out.println("Row>>" + r);
				XSSFRow row = sheet.getRow(r);
				XSSFCell cell;
				// cell= row.getCell(0);
				// System.out.println("cell0>>"+cell);

				Service service = new Service();
				service.serviceCode = df.formatCellValue(row.getCell(0));
				service.serviceName = df.formatCellValue(row.getCell(1));
				service.createdBy = Integer.valueOf(createdBy);
				// }
				
				services.add(service);			}
			System.out.println("Number of rows##" + rows);
			file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return services;
	}
	
	public CompasResponse UpdateService(Service service) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (service.serviceId == 0) {
					if (CheckServiceName(service.serviceName)) {
						return new CompasResponse(201,	"Service Name Already Exists");
					}
						preparedStatement = connection.prepareStatement(Queryconstants.insertHealthServiceDetails);
						preparedStatement.setString(1, service.serviceCode);
						preparedStatement.setString(2, service.serviceName);
						preparedStatement.setString(3, "H");
						preparedStatement.setInt(4, 0);
						preparedStatement.setString(5, null);
						preparedStatement.setBoolean(6, true);
						preparedStatement.setInt(7, service.createdBy);
						preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
						//preparedStatement.setBoolean(9, service.active);
						// preparedStatement.setInt(8, service.categoryId);

					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Location");
					}
				return new CompasResponse(200, "Records Updated");
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Service Name Already Exists");
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

	public boolean CheckServiceName(String serviceName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceByName);
			preparedStatement.setString(1, serviceName);

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
	
	public CompasResponse ValidateMemberExcelSheet(String pathToFile) {
		try {
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			workbook.getNumberOfSheets();
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			System.out.println("Number of rows>>" + rows);
			DataFormatter df = new DataFormatter();
			XSSFRow header = sheet.getRow(0);
			if(!df.formatCellValue(header.getCell(0)).equals("memberNo"))
				return new CompasResponse(201, "The first header cell should be memberNo");
			if(!df.formatCellValue(header.getCell(1)).equals("firstName"))
				return new CompasResponse(201, "The second header cell should be firstName");
			if(!df.formatCellValue(header.getCell(2)).equals("lastName"))
				return new CompasResponse(201, "The third header cell should be lastName");
			if(!df.formatCellValue(header.getCell(3)).equals("fullName"))
				return new CompasResponse(201, "The fourth header cell should be fullName");
			if(!df.formatCellValue(header.getCell(4)).equals("idPassPortNo"))
				return new CompasResponse(201, "The fifth header cell should be idPassPortNo");
			if(!df.formatCellValue(header.getCell(5)).equals("dateOfBirth"))
				return new CompasResponse(201, "The sixth header cell should be dateOfBirth");
			if(!df.formatCellValue(header.getCell(6)).equals("gender"))
				return new CompasResponse(201, "The seventh header cell should be gender");
			if(!df.formatCellValue(header.getCell(7)).equals("cellPhone"))
				return new CompasResponse(201, "The eighth header cell should be cellPhone");
			if(!df.formatCellValue(header.getCell(8)).equals("email"))
				return new CompasResponse(201, "The ninth header cell should be email");
			if(!df.formatCellValue(header.getCell(9)).equals("physicalAdd"))
				return new CompasResponse(201, "The tenth header cell should be physicalAdd");
			if(!df.formatCellValue(header.getCell(10)).equals("nhifNo"))
				return new CompasResponse(201, "The eleventh header cell should be nhifNo");
			if(!df.formatCellValue(header.getCell(11)).equals("relation"))
				return new CompasResponse(201, "The twelveth header cell should be relation");
			if(!df.formatCellValue(header.getCell(12)).equals("principleMemberNo"))
				return new CompasResponse(201, "The thirtheenth header cell should be principleMemberNo");
			for (int r = 1; r < rows; r++) {
				XSSFRow row = sheet.getRow(r);
				if(df.formatCellValue(row.getCell(0)).equals("") || df.formatCellValue(row.getCell(0)).equalsIgnoreCase(null))
					return new CompasResponse(201, "memberNo for cell "+ String.valueOf(r+1) +" is required");
				if(df.formatCellValue(row.getCell(3)).equals("") || df.formatCellValue(row.getCell(3)).equalsIgnoreCase(null))
					return new CompasResponse(201, "fullName for cell "+ String.valueOf(r+1) +" is required");
				if(df.formatCellValue(row.getCell(5)).equals("") || df.formatCellValue(row.getCell(5)).equalsIgnoreCase(null))
					return new CompasResponse(201, "dateOfBirth for cell "+ String.valueOf(r+1) +" is required");
				if(df.formatCellValue(row.getCell(6)).equals("") || df.formatCellValue(row.getCell(6)).equalsIgnoreCase(null))
					return new CompasResponse(201, "gender for cell "+ String.valueOf(r+1) +" is required");
				if(!df.formatCellValue(row.getCell(6)).equals("M") && !df.formatCellValue(row.getCell(6)).equals("F"))
					return new CompasResponse(201, "Valid gender for cell "+ String.valueOf(r+1) +" is required");
				if(df.formatCellValue(row.getCell(11)).equals("") || df.formatCellValue(row.getCell(11)).equalsIgnoreCase(null))
					return new CompasResponse(201, "relation for cell "+ String.valueOf(r+1) +" is required");
				if(!df.formatCellValue(row.getCell(11)).equals("0") && !df.formatCellValue(row.getCell(11)).equals("1") && 
				!df.formatCellValue(row.getCell(11)).equals("2")) 
					return new CompasResponse(201, "Valid relation for cell "+ String.valueOf(r+1) +" is required");
				if(!df.formatCellValue(row.getCell(11)).equals("0") &&
						(df.formatCellValue(row.getCell(12)).equals("") || df.formatCellValue(row.getCell(12)).equalsIgnoreCase(null)))	
					return new CompasResponse(201, "principleMemberNo for cell "+ String.valueOf(r+1) +" is required");
				if(df.formatCellValue(row.getCell(11)).equals("0") &&
						!(df.formatCellValue(row.getCell(12)).equals("") || df.formatCellValue(row.getCell(12)).equalsIgnoreCase(null)))	
					return new CompasResponse(201, "Member for cell "+ String.valueOf(r+1) +" is a principle yet has a pricipleMemberNo defined");
			file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new CompasResponse(200, "Success");
		
		}
}
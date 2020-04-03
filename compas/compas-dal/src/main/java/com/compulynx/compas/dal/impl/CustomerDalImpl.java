package com.compulynx.compas.dal.impl;

//import com.compulynx.compas.dal.CardInput;
import com.compulynx.compas.dal.CustomerDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.*;
/*import com.innovatrics.idkit.IDKit;
import com.innovatrics.idkit.IDKit.FingerPosition;
import com.innovatrics.idkit.IDKitException;*/
//simport com.innovatrics.idkit.User;
import com.compulynx.compas.models.android.ApiProgramme;
import com.compulynx.compas.models.android.ApiService;
import com.compulynx.compas.models.android.ApiVoucher;

import javax.sql.DataSource;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomerDalImpl implements CustomerDal {

	private static final Logger logger = Logger.getLogger(CustomerDalImpl.class.getName());

	private DataSource dataSource;

	public CustomerDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public long generateNumber() {
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return number;// (long) (Math.random() * 100000 + 4444300000L);
	}
	
	public CompasResponse UpdateCard(CardDetails card) {
		return new CompasResponse(200, "Records Updated");
	}

	public Member GetMembers(String memberNo) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		PreparedStatement preparedStatement7 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet6 = null;
//		int programmeId = 0;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberDetails);
			preparedStatement.setString(1, memberNo);
			// preparedStatement.setString(2,memberNo);
			resultSet = preparedStatement.executeQuery();
			Member objMember = new Member();
			String strOutpatientStatus = "Active";
			while (resultSet.next()) {

				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("memberno");
				objMember.fullName = resultSet.getString("name");
				objMember.idPassPortNo = resultSet.getString("nationalid");
				objMember.gender = resultSet.getString("gender");
				objMember.dateOfBirth = resultSet.getString("dob");
				objMember.nhifNo = resultSet.getString("nhif");
				objMember.physicalAdd = resultSet.getString("address");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
				objMember.productId = resultSet.getInt("product_id");
				objMember.orgId = resultSet.getInt("org_id");
				objMember.active = resultSet.getBoolean("active");
				objMember.lctCode = resultSet.getString("lct_code");
				objMember.memberType = resultSet.getString("member_type");
				objMember.relation = resultSet.getString("relation");
				objMember.principleId = resultSet.getInt("principle_id");
				objMember.principle = resultSet.getString("principle");
				if (objMember.active == false) {
					strOutpatientStatus = "Inactive";
				} else {
					objMember.setOutpatientStatus(resultSet.getString("outpatient_status"));
				}
				preparedStatement = connection.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				System.out.println("Oupatient status " + resultSet.getString("outpatient_status"));
				List<Programme> programmes = new ArrayList<Programme>();
				while (resultSet2.next()) {
					if(resultSet2.getBoolean("IsActive")) {
						objMember.programmeDesc = resultSet2.getString("programmeDesc");
						objMember.scheme = resultSet2.getString("product_name");
						objMember.schemeCode = resultSet2.getString("product_code");
						objMember.insuranceCode = resultSet2.getString("insurance_code");
						System.out.println("Scheme Code :" + objMember.schemeCode);
						System.out.println("insuarance code :" + objMember.insuranceCode);
					}
//					int programmeId = resultSet2.getInt("ProgrammeId");
					Programme programme = new Programme();
					programme.programmeId =  resultSet2.getInt("ProgrammeId");
					programme.programmeDesc =  resultSet2.getString("ProgrammeDesc");
					programme.programmeCode =  resultSet2.getString("ProgrammeCode");
					programme.programmeValue =  resultSet2.getDouble("ProgrammeValue");
					programme.isActive =  resultSet2.getBoolean("IsActive");
					programme.createdBy =  resultSet2.getInt("CreatedBY");
					programme.respCode =  200;
					programme.itmModes =  resultSet2.getString("itm_modes");
					programme.chtmModes =  resultSet2.getString("chtm_modes");
					programme.intmModes =  resultSet2.getString("intm_modes");
					preparedStatement4 = connection.prepareStatement(Queryconstants.getVouchersByMemberNo);
					int progId = resultSet2.getInt("ProgrammeId");
					// preparedStatement4.setString(1, objMember.memberNo);
					// preparedStatement4.setString(2, objMember.memberNo);
					System.out.println("memberNo: " + objMember.memberNo);
					// System.out.println("progId: "+progId);
					// preparedStatement4.setInt(1, progId);
					// preparedStatement4.setInt(2, resultSet2.getInt("org_id"));
					// preparedStatement4.setInt(3, resultSet2.getInt("ProgrammeId"));
					// preparedStatement4.setInt(4, resultSet2.getInt("org_id"));
					preparedStatement4.setInt(1, objMember.memberId);
					preparedStatement4.setInt(2, objMember.memberId);
					resultSet5 = preparedStatement4.executeQuery();
					List<Voucher> membervouchers = new ArrayList<Voucher>();
					while (resultSet5.next()) {
						Voucher membervoucher = new Voucher();
						membervoucher.voucherId = resultSet5.getInt("voucherId");
						System.out.println(membervoucher.toString());
						membervoucher.voucherDesc = resultSet5.getString("voucher_name");
						membervoucher.voucherValue = resultSet5.getDouble("basket_value");
						membervoucher.voucherBalance = resultSet5.getDouble("basket_balance");
						membervoucher.used = resultSet5.getString("used");
						System.out.println("Voucher Name: " + membervoucher.voucherDesc + " voucher Value: "
								+ membervoucher.voucherValue + " voucher Balance: " + membervoucher.voucherBalance
								+ "Voucher Used: " + membervoucher.used);
						membervoucher.isActive = resultSet5.getBoolean("IsActive");
						membervoucher.coverType = resultSet5.getString("cover_type");

						if (resultSet5.getString("cover_type").equals("2")) {
							if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 0) {
								preparedStatement5 = connection.prepareStatement(Queryconstants.getMMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 0) {
								preparedStatement5 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 1) {
								preparedStatement5 = connection.prepareStatement(Queryconstants.get1MMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 1) {
								preparedStatement5 = connection.prepareStatement(Queryconstants.get1FMemberServiceDtl);
							} else {
								preparedStatement5 = connection.prepareStatement(Queryconstants.get2MemberServiceDtl);
							}
							preparedStatement5.setInt(3, resultSet.getInt("relation") == 0 ? resultSet.getInt("id")
									: resultSet.getInt("principle_id"));
						} else {
							if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 0) {
								preparedStatement5 = connection.prepareStatement(Queryconstants.getMMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 0) {
								preparedStatement5 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
							} else if (resultSet.getString("gender").equals("M") && resultSet.getInt("relation") == 1) {
								preparedStatement5 = connection
										.prepareStatement(Queryconstants.get1MIndividualServiceDtl);
							} else if (resultSet.getString("gender").equals("F") && resultSet.getInt("relation") == 1) {
								preparedStatement5 = connection
										.prepareStatement(Queryconstants.get1FIndividualServiceDtl);
							} else {
								preparedStatement5 = connection
										.prepareStatement(Queryconstants.get2IndividualServiceDtl);
							}
							preparedStatement5.setInt(3, resultSet.getInt("id"));
						}
						preparedStatement5.setInt(1, programme.programmeId);
						preparedStatement5.setInt(2, membervoucher.voucherId);
						resultSet6 = preparedStatement5.executeQuery();

						// preparedStatement5 =
						// connection.prepareStatement(Queryconstants.getServicesByBasket);
						// preparedStatement5.setInt(1, resultSet5.getInt("voucherId"));
						// preparedStatement5.setInt(2, resultSet2.getInt("ProgrammeId"));
						// preparedStatement5.setInt(3, resultSet5.getInt("voucherId"));
						// preparedStatement5.setInt(4, resultSet2.getInt("ProgrammeId"));
						// preparedStatement5.setInt(5, resultSet5.getInt("voucherId"));
						resultSet6 = preparedStatement5.executeQuery();
						List<Service> services = new ArrayList<Service>();
						while (resultSet6.next()) {
							Service service = new Service();
							service.requireAuth = resultSet6.getBoolean("require_auth");
							service.serviceId = resultSet6.getInt("Service_Id");
							service.serviceName = resultSet6.getString("ser_name");
							// service.serviceBalance = resultSet6.getDouble("service_balance");
							// service.serviceValue = resultSet6.getDouble("service_value");
							service.coverLimit = resultSet6.getDouble("cover_limit");
							service.price = resultSet6.getDouble("basket_value");
							service.used = resultSet6.getDouble("used");
							// service.coverBalance = resultSet6.getDouble("cover_balance");

							// check for cover limits and assign balances
							if (resultSet6.getDouble("cover_limit") < resultSet6.getDouble("service_value")) {
								service.serviceBalance = resultSet6.getDouble("basket_balance");
								service.serviceValue = resultSet6.getDouble("service_value");
								service.coverBalance = resultSet6.getDouble("cover_balance");
							} else {
								service.serviceBalance = resultSet6.getDouble("cover_limit");
								service.serviceValue = resultSet6.getDouble("cover_limit");
								service.coverBalance = resultSet6.getDouble("basket_balance");
							}

							service.price = resultSet6.getDouble("basket_value");

							services.add(service);
							System.out.println("cover limit :" + service.coverLimit + "service value: "
									+ service.serviceValue + "USED AMOUNT IS :" + service.used + "cover Balance is: "
									+ service.coverBalance + "service balance is: " + service.serviceBalance);
						}
						membervoucher.services = services;
						membervouchers.add(membervoucher);
					}
					programme.membervouchers = membervouchers;
					programmes.add(programme);
				}
				
				preparedStatement1 = connection.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setString(1, memberNo);
				
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("card_no");
					objMember.accId = resultSet3.getInt("acc_id");
//					/objMember.cardBalance = resultSet3.getDouble("balance");
				}
				preparedStatement6 = connection.prepareStatement(Queryconstants.getAccountNumber);
				preparedStatement6.setInt(1, resultSet3.getInt("acc_id"));
				
				resultSet6 = preparedStatement6.executeQuery();
				if (resultSet6.next()) {
					objMember.accNumber = resultSet6.getString("acc_no");
				}

				objMember.programmes = programmes;
				PreparedStatement preparedStatement2 = connection.prepareStatement(Queryconstants.getNewBeneficiaryFamilyMembers);
				preparedStatement2.setInt(1, objMember.memberId);
				resultSet2 = preparedStatement2.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
				BeneficiaryFamilyMembers famMember = new BeneficiaryFamilyMembers();
				famMember.familyMemId = resultSet2.getInt("emp_id");
				famMember.famMemFullName = resultSet2.getString("emp_fullname");
				famMember.famMemberNo = resultSet2.getString("emp_ref_key");
				famMember.famGender = resultSet2.getString("emp_gender");
				famMember.famDob = resultSet2.getString("emp_dob");
				famMember.relationId = resultSet2.getInt("relation");
				famMember.lctCode = resultSet2.getString("lct_code");
				famMember.memberType = resultSet2.getString("member_type");
				famMember.famMemNationalId = resultSet2.getString("emp_nationalid");
				famMember.active = resultSet2.getBoolean("active");
				if(resultSet2.getInt("relation") == 1) 
					objMember.hasSpouse = true;			
				familyMembers.add(famMember);
				}
				objMember.familyMemList = familyMembers;
				
				preparedStatement3 = connection.prepareStatement(Queryconstants.getVouchersByMemberNo);
				preparedStatement3.setInt(1, objMember.memberId);
				preparedStatement3.setInt(2, objMember.memberId);
				resultSet4 = preparedStatement3.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				if (!resultSet4.next()) {
					strOutpatientStatus = "Inactive";
					resultSet4.beforeFirst();
				}
				while (resultSet4.next()) {
					Voucher voucher = new Voucher();
					voucher.voucherName = resultSet4.getString("voucher_name");
					voucher.amountRemaining = df.format(resultSet4.getDouble("basket_balance"));
					voucher.used = df.format(resultSet4.getDouble("used"));
					voucher.value = df.format(resultSet4.getDouble("basket_value"));
					voucher.schemeType = resultSet4.getString("scheme_type");
					vouchers.add(voucher);
					System.out.println("Voucher Name: " + voucher.voucherName + " voucherValue: " + voucher.value
							+ " voucher amount used: " + voucher.used + " scheme: " + voucher.schemeType);
					// determine if outpatient status is active
					if (voucher.voucherName != null && voucher.schemeType.equals("O")
							&& Double.parseDouble(voucher.value.replace(",", "")) != 0) {
						double percent = Double.parseDouble(voucher.used.replace(",", ""))
								/ Double.parseDouble(voucher.value.replace(",", "")) * 100;
						System.out.println("percentage used by member: " + percent);
						if (percent > 75) {
							strOutpatientStatus = "Inactive";
						}
					}
				}
				objMember.vouchers = getMemberVouchersByMemberNo(objMember.memberId, objMember.memberId);// vouchers;
				objMember.memberServices = getMemberSubLimitsByMemberIdAndProgrammeId(objMember.memberId,
						objMember.programmeId, objMember.voucherId); // sub-limits
				
				//set outpatient status to active
				preparedStatement7 = connection.prepareStatement(Queryconstants.updateMemberStatus);
				preparedStatement7.setString(1, strOutpatientStatus);
				preparedStatement7.setInt(2, objMember.memberId);
				preparedStatement7.executeUpdate();
				objMember.setOutpatientStatus(strOutpatientStatus);
			}
			return objMember;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	private List<Service> getMemberSubLimitsByMemberIdAndProgrammeId(int memberId, int programmeId, int voucherId) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");

		List<Service> memberServices = new ArrayList<Service>();

		Connection connection = null;
		PreparedStatement preparedStatementService = null;
		ResultSet resultSetService = null;
		try {
			connection = dataSource.getConnection();
			preparedStatementService = connection.prepareStatement(Queryconstants.getMemberSublimits);
			preparedStatementService.setInt(1, memberId);
			preparedStatementService.setInt(2, programmeId);
			preparedStatementService.setInt(3, voucherId);

			resultSetService = preparedStatementService.executeQuery();
			while (resultSetService.next()) {
				Service memberService = new Service();
				memberService.serviceId = resultSetService.getInt("service_id");
				memberService.serviceName = resultSetService.getString("ser_name");
				memberService.serviceCode = resultSetService.getString("ser_code");
				memberService.serviceValue = resultSetService.getDouble("service_value");
				memberService.serviceBalance = resultSetService.getDouble("service_balance");
				// memberService.voucherBalance = resultSet1.getDouble("basket_balance");
				memberService.coverLimit = resultSetService.getDouble("cover_limit");
				memberService.used = resultSetService.getDouble("used");
				memberService.requireAuth = resultSetService.getBoolean("require_auth");
				System.out.println("Service name: " + memberService.serviceName + "service value :"
						+ memberService.serviceValue + "serviceBalance :" + memberService.serviceBalance
						+ " cover limit:" + memberService.coverLimit + "Used amount: " + memberService.used);
				memberServices.add(memberService);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBOperations.DisposeSql(connection, preparedStatementService, resultSetService);
		}

		return memberServices;
	}
	
	
	private List<Voucher> getMemberVouchersByMemberNo(int memberNo, int memberNo1) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");

		List<Voucher> membervouchers = new ArrayList<Voucher>();

		Connection connection = null;
		PreparedStatement preparedStatement4 = null;
		ResultSet resultSet4 = null;
		try {
			connection = dataSource.getConnection();

			// preparedStatement4 =
			// connection.prepareStatement(Queryconstants.getVouchersByMembershipNo);
			// preparedStatement4 =
			// connection.prepareStatement(Queryconstants.getVouchersByMemberNo);
			preparedStatement4 = connection.prepareStatement(Queryconstants.getNewMemberVouchers);
			// preparedStatement4 = connection.prepareStatement((String)
			// Queryconstants.getNewMemberVouchersBest);
			preparedStatement4.setInt(1, memberNo);
			preparedStatement4.setInt(2, memberNo1);
			preparedStatement4.setInt(3, memberNo);
			preparedStatement4.setInt(4, memberNo1);

			resultSet4 = preparedStatement4.executeQuery();
			while (resultSet4.next()) {
				Voucher membervoucher = new Voucher();
				membervoucher.voucherName = resultSet4.getString("voucher_name");
				membervoucher.amountRemaining = df.format(resultSet4.getDouble("basket_balance"));
				membervoucher.used = df.format(resultSet4.getDouble("used"));
				membervoucher.value = df.format(resultSet4.getDouble("basket_value"));
				membervoucher.schemeType = resultSet4.getString("scheme_type");
				membervouchers.add(membervoucher);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement4, resultSet4);
		}

		return membervouchers;

	}
	
	
	public List<Member> GetMembersToVerify(String fromDate,String toDate,int count,int orgId) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberDetailsToVerify);
			preparedStatement.setString(1, fromDate);
			preparedStatement.setString(2, toDate );
			preparedStatement.setInt(3, count);
			//preparedStatement.setString(4, "N");
			preparedStatement.setInt(4, orgId);
			// preparedStatement.setString(2,memberNo);
			resultSet = preparedStatement.executeQuery();
			List<Member> members = new ArrayList<Member>();
			while (resultSet.next()) {
				Member objMember= new Member();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("memberno");
				objMember.fullName = resultSet.getString("name");
				objMember.idPassPortNo = resultSet.getString("nationalid");
				objMember.gender = resultSet.getString("gender");
				objMember.dateOfBirth = resultSet.getString("dob");
				objMember.nhifNo = resultSet.getString("nhif");
				objMember.physicalAdd = resultSet.getString("address");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
				objMember.productId = resultSet.getInt("product_id");
				objMember.status=resultSet.getString("emp_status");
				objMember.verifyStatus=resultSet.getString("verify_status");
				preparedStatement = connection.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200, resultSet2
							.getString("itm_modes"), resultSet2
							.getString("chtm_modes"), resultSet2
							.getString("intm_modes")));
				}
				preparedStatement1 = connection.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("cardno");
					objMember.cardBalance = resultSet3.getDouble("balance");
				}

				objMember.programmes = programme;
				objMember.expanded=false;
				PreparedStatement preparedStatement2 = connection.prepareStatement(Queryconstants.getFamilyMembersToVerify);
				preparedStatement2.setInt(1, objMember.memberId);
				preparedStatement2.setString(2, "N");
				resultSet2 = preparedStatement2.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					BeneficiaryFamilyMembers beneficiary = new BeneficiaryFamilyMembers(resultSet2.getInt("emp_dtl_id"), 
							resultSet2.getString("alt_name"), 
							resultSet2.getString("ref_sub_key"), 0, "", 0, 
							200,false,resultSet2.getString("verify_status"),"",resultSet2.getString("alt_gender"),
							resultSet2.getString("lct_code"),resultSet2.getString("memberype"));
					familyMembers.add(beneficiary);
				}
				
				
				if(objMember.status.equalsIgnoreCase("V") && familyMembers.size()<=0){
					
				}else{
					objMember.familyMemList = familyMembers;
					members.add(objMember);
				}
				
				
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Member> GetMembersToApprove(String fromDate,String toDate,int count,int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberDetailsToApprove);
			preparedStatement.setString(1, fromDate);
			preparedStatement.setString(2, toDate );
			preparedStatement.setInt(3, count);
			//preparedStatement.setString(4, "N");
			preparedStatement.setInt(4, orgId);
			// preparedStatement.setString(2,memberNo);
			resultSet = preparedStatement.executeQuery();
			List<Member> members = new ArrayList<Member>();
			while (resultSet.next()) {
				Member objMember= new Member();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("memberno");
				objMember.fullName = resultSet.getString("name");
				objMember.idPassPortNo = resultSet.getString("nationalid");
				objMember.gender = resultSet.getString("gender");
				objMember.dateOfBirth = resultSet.getString("dob");
				objMember.nhifNo = resultSet.getString("nhif");
				objMember.physicalAdd = resultSet.getString("address");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
				objMember.productId = resultSet.getInt("product_id");
				objMember.status=resultSet.getString("emp_status");
				objMember.approveStatus=resultSet.getString("approve_status");
				preparedStatement = connection.prepareStatement(Queryconstants.getCustomerProgrammes);
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programme = new ArrayList<Programme>();
				while (resultSet2.next()) {
					programme.add(new Programme(resultSet2
							.getInt("ProgrammeId"), resultSet2
							.getString("ProgrammeDesc"), resultSet2
							.getDouble("ProgrammeValue"), resultSet2
							.getBoolean("Isactive"), resultSet2
							.getInt("CreatedBY"), 200, resultSet2
							.getString("itm_modes"), resultSet2
							.getString("chtm_modes"), resultSet2
							.getString("intm_modes")));
				}
				preparedStatement1 = connection.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);
				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("cardno");
					objMember.cardBalance = resultSet3.getDouble("balance");
				}

				objMember.programmes = programme;
				objMember.expanded=false;
				PreparedStatement preparedStatement2 = connection
						.prepareStatement(Queryconstants.getFamilyMembersToApprove);
				preparedStatement2.setInt(1, objMember.memberId);
				preparedStatement2.setString(2, "V");
				resultSet2 = preparedStatement2.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					BeneficiaryFamilyMembers beneficiary = new BeneficiaryFamilyMembers(resultSet2.getInt("emp_dtl_id"), 
							resultSet2.getString("alt_name"), resultSet2.getString("ref_sub_key"), 0, "", 0, 
							200,false,"",resultSet2.getString("approve_status"),resultSet2.getString("alt_gender"),
							resultSet2.getString("lct_code"),resultSet2.getString("member_type"));
					familyMembers.add(beneficiary);
				}
				
				
				if(objMember.status.equalsIgnoreCase("A") && familyMembers.size()<=0){
					
				}else if(objMember.status.equalsIgnoreCase("V")){
					objMember.familyMemList = familyMembers;
					members.add(objMember);
				}else if(objMember.status.equalsIgnoreCase("A") && familyMembers.size()>=0){
					objMember.familyMemList = familyMembers;
					members.add(objMember);
				}
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckMemberExists(String memberNo) {
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

	public CompasResponse UpdateMember(Member member) {
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
		PreparedStatement preparedStatement11 = null;
		PreparedStatement preparedStatement12 = null;
		PreparedStatement preparedStatement13 = null;
		PreparedStatement preparedStatement14 = null;
		ResultSet resultSet = null;
		ResultSet resultSet3 = null;
//		ResultSet resultSet4 = null;
		ResultSet resultSet6 = null;
		ResultSet resultSet8 = null;
		ResultSet resultSet9 = null;
		ResultSet resultSet11 = null;
		ResultSet resultSet12 = null;
		ResultSet rs = null;
		int memberId = 0;
		int accountId = 0;
		String cardNumber = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
//			String example = "SGVsbG8gV29ybGQ=";
//			byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(example.getBytes());
			// System.out.println(decoded);
			if (member.memberId == 0) {
				if (GetMemberByNo(member.memberNo)) {
					return new CompasResponse(201, "Member Number already Exists ");
				} else {

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
					preparedStatement.setString(21, member.memberType);
					preparedStatement.setString(22, member.lctCode);
					
					if (preparedStatement.executeUpdate() > 0) {
						
						// insert audit trail
						preparedStatement14 = connection.prepareStatement(Queryconstants.insertAuditTrail);
						preparedStatement14.setInt(1, member.createdBy);
						preparedStatement14.setInt(2, member.createdBy);
						preparedStatement14.setString(3, "Created Member Number: " + member.memberNo);
						preparedStatement14.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement14.setString(5, "LCT Member Management Module");
						if (preparedStatement14.executeUpdate() <= 0) {
							connection.rollback();
							return new CompasResponse(200, "Failed to add audit trail");
						}
						logger.info("Audit Trail for " + member.memberNo + "on member management module captured");

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
									connection.rollback();
									return new CompasResponse(202, "Failed To Update Account Details for Member");
								}

							}
						}
						// member.memberId=customerId;
						member.cardNumber = "804400" + generateNumber();
						DBOperations.DisposeSql(preparedStatement);
//						preparedStatement = connection.prepareStatement(Queryconstants.deleteCustomerProgramme);
//						preparedStatement.setInt(1, memberId);
//						preparedStatement.executeUpdate();

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
									if(member.programmes.get(i).isActive) {
										for (int j = 0; j < member.programmes.get(i).vouchers.size(); j++) {
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
													preparedStatement2.setDouble(8, member.programmes.get(i).vouchers.get(j).services.get(k).price);
													preparedStatement2.setDouble(9, member.programmes.get(i).vouchers.get(j).services.get(k).serviceBalance);
													preparedStatement2.setDouble(10, member.programmes.get(i).vouchers.get(j).services.get(k).price);
					
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

						DBOperations.DisposeSql(preparedStatement, rs);
//						preparedStatement = connection.prepareStatement(Queryconstants.deleteMemberFamilyDetails);
//						preparedStatement.setInt(1, memberId);
//						preparedStatement.executeUpdate();
//						DBOperations.DisposeSql(preparedStatement);
						// insert familyMembers
						PreparedStatement preparedStatement16 = connection.prepareStatement(Queryconstants.insertNewMemberFamilyDetails, Statement.RETURN_GENERATED_KEYS);
						for (int i = 0; i < member.familyMemList.size(); i++) {
							if (GetMemberByNo(member.familyMemList.get(i).famMemberNo)) {
								connection.rollback();
								return new CompasResponse(201, "Family member Number "+ member.familyMemList.get(i).famMemberNo+" already Exists ");
							}
							preparedStatement16.setInt(1, member.memberId);
							preparedStatement16.setString(2, GeneralUtility.splitPJ(member.familyMemList.get(i).famMemberNo));
							preparedStatement16.setString(3, GeneralUtility.splitMemberNo(member.familyMemList.get(i).famMemberNo));

							preparedStatement16.setString(4, member.familyMemList.get(i).famMemberNo);
							preparedStatement16.setString(5, member.familyMemList.get(i).famMemFullName);
							preparedStatement16.setString(6, member.familyMemList.get(i).famDob);
							preparedStatement16.setString(7, member.familyMemList.get(i).famGender);
							preparedStatement16.setInt(8, member.familyMemList.get(i).relationId);
							preparedStatement16.setInt(9, 1);
							preparedStatement16.setInt(10, member.productId);
							preparedStatement16.setInt(11, member.orgId);
							preparedStatement16.setInt(12, member.createdBy);
							preparedStatement16.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
							preparedStatement16.setString(14, member.familyMemList.get(i).memberType);
							preparedStatement16.setString(15, member.familyMemList.get(i).idNumber);
							preparedStatement16.setString(16, member.familyMemList.get(i).lctCode);
							if (preparedStatement16.executeUpdate() <= 0) {
								connection.rollback();
								return new CompasResponse(202,	"Failed To Update Family Details for Member");
							}
								resultSet9 = preparedStatement16.getGeneratedKeys();
								resultSet9.next();
								memberId = resultSet9.getInt(1);
//								member.memberId = memberId;
								DBOperations.DisposeSql(preparedStatement9,resultSet9);
								preparedStatement9 = connection.prepareStatement(Queryconstants.getMaxMemberAccount);
								resultSet9 = preparedStatement9.executeQuery();
								if (resultSet9.next()) {
									member.accNo = resultSet9.getInt("maxaccno") + 1;
									preparedStatement9 = connection.prepareStatement(Queryconstants.insertMemberAccDtl, Statement.RETURN_GENERATED_KEYS);;
									preparedStatement9.setInt(1, memberId);
									preparedStatement9.setInt(2, member.accNo);
									preparedStatement9.setString(3, "NA");
									preparedStatement9.setDouble(4, member.accBalance);
									preparedStatement9.setInt(5, member.createdBy);
									preparedStatement9.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
									preparedStatement9.setString(7, "N");
									
									if(preparedStatement9.executeUpdate() > 0) {
										resultSet9=preparedStatement9.getGeneratedKeys();
										resultSet9.next();
										accountId =resultSet9.getInt(1);
										cardNumber = "804400" + generateNumber();
										DBOperations.DisposeSql(preparedStatement9,resultSet9);
								preparedStatement9 = connection.prepareStatement(Queryconstants.insertCardDetails);
								preparedStatement9.setString(1, String.valueOf(accountId));
								preparedStatement9.setString(2, String.valueOf(memberId));
								preparedStatement9.setString(3, member.familyMemList.get(i).famMemberNo);
								preparedStatement9.setString(4, cardNumber);
								preparedStatement9.setString(5, member.familyMemList.get(i).famMemberNo);
								preparedStatement9.setString(6, "I");
								preparedStatement9.setInt(7, member.createdBy);
								preparedStatement9.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
								
								if(preparedStatement9.executeUpdate() <=0) {
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
						}
						connection.commit();
						return new CompasResponse(200, "Records Updated");

					} else {
						connection.rollback();
						return new CompasResponse(202, "Nothing To Update");
					}
				}
			} else {
				preparedStatement7 = connection.prepareStatement(Queryconstants.updateMemberMaster);

				preparedStatement7.setString(1, member.firstName);
				preparedStatement7.setString(2, member.lastName);
				preparedStatement7.setString(3, member.fullName);
				preparedStatement7.setString(4, member.idPassPortNo);
				preparedStatement7.setString(5, member.dateOfBirth);
				preparedStatement7.setString(6, member.gender);
				preparedStatement7.setString(7, member.cellPhone);
				preparedStatement7.setString(8, member.email);
				preparedStatement7.setString(9, member.physicalAdd);
				preparedStatement7.setString(10, member.nhifNo);
				preparedStatement7.setBoolean(11, member.active);

				preparedStatement7.setString(12, member.memberPic);
				preparedStatement7.setInt(13, member.createdBy);
				preparedStatement7.setTimestamp(14, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement7.setString(15, member.memberType);
				preparedStatement7.setString(16, member.lctCode);
				preparedStatement7.setInt(17, member.memberId);

				memberId = member.memberId;
//				boolean programmeChanged = false;
				int activeMemberProgrammeId = 0;
//				int activeProgrammeId = 0;
				boolean hasShared = false;
				if (preparedStatement7.executeUpdate() > 0) {
					preparedStatement3 = connection.prepareStatement(Queryconstants.getCustomerProgrammes);
					preparedStatement3.setInt(1, memberId);
					preparedStatement3.setInt(2, memberId);
					resultSet3 = preparedStatement3.executeQuery();
					List<Programme> programmes = new ArrayList<Programme>();
					while (resultSet3.next()) {
						if(resultSet3.getBoolean("IsActive")) {
						Programme programme = new Programme();
						programme.programmeId =  resultSet3.getInt("ProgrammeId");
						programme.programmeDesc =  resultSet3.getString("ProgrammeDesc");
						programme.programmeCode =  resultSet3.getString("ProgrammeCode");
						programme.programmeValue =  resultSet3.getDouble("ProgrammeValue");
						programme.isActive =  resultSet3.getBoolean("IsActive");
						programme.createdBy =  resultSet3.getInt("CreatedBY");
						programme.respCode =  200;
						programme.itmModes =  resultSet3.getString("itm_modes");
						programme.chtmModes =  resultSet3.getString("chtm_modes");
						programme.intmModes =  resultSet3.getString("intm_modes");
						preparedStatement6 = connection.prepareStatement(Queryconstants.getMemberVouchers);
						preparedStatement6.setInt(1, resultSet3.getInt("ProgrammeId"));
						preparedStatement6.setInt(2, memberId);
						System.out.println( "ProgrammeId: "+resultSet3.getInt("ProgrammeId")+": memberId: "+memberId);
						resultSet6 = preparedStatement6.executeQuery();
						List<Voucher> vouchers = new ArrayList<Voucher>();
						while (resultSet6.next()) {
							Voucher objVoucher = new Voucher();
							objVoucher.voucherId = resultSet6.getInt("basket_id");
							objVoucher.voucherCode = resultSet6.getString("voucher_code");
							objVoucher.voucherValue = resultSet6.getDouble("basket_value");
							objVoucher.voucherBalance = resultSet6.getDouble("basket_balance");
							objVoucher.usage = resultSet6.getDouble("used");
							objVoucher.coverType = resultSet6.getString("cover_type");
							if(resultSet6.getString("cover_type") == "1") {
								hasShared = true;
							}
							preparedStatement8 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
							preparedStatement8.setInt(1, resultSet3.getInt("ProgrammeId"));
							preparedStatement8.setInt(2, resultSet6.getInt("basket_id"));
							preparedStatement8.setInt(3, memberId);
							resultSet8 = preparedStatement8.executeQuery();
							List<Service> services = new ArrayList<Service>();
							while (resultSet8.next()) {
								Service service = new Service();
								service.serviceId = resultSet8.getInt("service_id");
								service.serviceCode = resultSet8.getString("ser_code");
								service.serviceValue = resultSet8.getDouble("service_value");
								service.serviceBalance = resultSet8.getDouble("basket_balance");
								service.usage = resultSet8.getDouble("used");
								services.add(service);
							}
							objVoucher.services = services;
							vouchers.add(objVoucher);
						}
						programme.vouchers = vouchers;
						programmes.add(programme);
					}
					}
					for (int i = 0; i < member.programmes.size(); i++) {
						if(member.programmes.get(i).isActive) {
							activeMemberProgrammeId = member.programmes.get(i).programmeId;
						}
					}
					for (int i = 0; i < programmes.get(0).vouchers.size(); i++)
						if(programmes.get(0).vouchers.get(i).usage>0) {
							for (int k = 0; k < member.programmes.size(); k++) {
								if(member.programmes.get(k).isActive) {
//									for (int n = 0; n < member.programmes.get(k).vouchers.size(); n++) {
//										if(programmes.get(0).vouchers.get(i).voucherId == member.programmes.get(k).vouchers.get(n).voucherId) {
//											member.programmes.get(k).vouchers.get(n).voucherBalance = member.programmes.get(k).vouchers.get(n).voucherBalance -	programmes.get(0).vouchers.get(i).usage >0 ?
//													member.programmes.get(k).vouchers.get(n).voucherBalance -	programmes.get(0).vouchers.get(i).usage : 0;
//										}
//										for (int j = 0; j < programmes.get(0).vouchers.get(i).services.size(); j++) {
//											if(programmes.get(0).vouchers.get(i).services.get(j).usage>0) {
//												for (int l = 0; l < member.programmes.get(k).vouchers.get(n).services.size(); l++) {
//													if(programmes.get(0).vouchers.get(i).services.get(j).serviceId == member.programmes.get(k).vouchers.get(n).services.get(l).serviceId) {
//														member.programmes.get(k).vouchers.get(n).services.get(l).serviceBalance = member.programmes.get(k).vouchers.get(n).services.get(l).serviceValue - programmes.get(0).vouchers.get(i).services.get(j).usage >0 ?
//																member.programmes.get(k).vouchers.get(n).services.get(l).serviceValue - programmes.get(0).vouchers.get(i).services.get(j).usage : 0;
//													}
//												}
//											}
//										}
//									}
								}
							}
						}
					if(activeMemberProgrammeId == programmes.get(0).programmeId) {
//						System.out.print(programmes.get(0).vouchers.size()+"\n");
//						for (int j = 0; j < programmes.get(0).vouchers.size(); j++) {
//							for (int k = 0; k < member.programmes.size(); k++) {
//								if(member.programmes.get(k).isActive) {
//									for (int n = 0; n < member.programmes.get(k).vouchers.size(); n++) {
//										System.out.print("Parent: "+programmes.get(0).vouchers.get(j).voucherId+"\n");
//										System.out.print("Child: "+member.programmes.get(k).vouchers.get(n).voucherId+"\n");
//										if(programmes.get(0).vouchers.get(j).voucherId == member.programmes.get(k).vouchers.get(n).voucherId) {
//											member.programmes.get(k).vouchers.get(n).notNewToProgramme = true;
//											System.out.print("Is not new"+member.programmes.get(k).vouchers.get(n).voucherId+"\n");
//											if(programmes.get(0).vouchers.get(j).voucherValue == member.programmes.get(k).vouchers.get(n).voucherValue) {
//												member.programmes.get(k).vouchers.get(n).valueHasNotChanged = true;
//											}
//											for(int a = 0; a < programmes.get(0).vouchers.get(j).services.size(); a++) {
//												for(int b = 0; b < member.programmes.get(k).vouchers.get(n).services.size(); b++) {
//													if(programmes.get(0).vouchers.get(j).services.get(a).serviceId == member.programmes.get(k).vouchers.get(n).services.get(b).serviceId) {
//														member.programmes.get(k).vouchers.get(n).services.get(b).notNewToVoucher = true;
//													}
//												}
//											}
//										}
//									}
//								}
//							}
//						}
//						for (int i = 0; i < member.programmes.size(); i++) {
//							if(member.programmes.get(i).isActive) {
//								for (int j = 0; j < member.programmes.get(i).vouchers.size(); j++)
//									for (int n = 0; n < programmes.get(0).vouchers.size(); n++) {
//										if(member.programmes.get(i).vouchers.get(j).voucherId == programmes.get(0).vouchers.get(n).voucherId) {
//											member.programmes.get(i).vouchers.get(j).voucherNotRemoved = true;	
//											for(int a = 0; a < member.programmes.get(i).vouchers.get(j).services.size(); a++) {
//												for(int b = 0; b < programmes.get(0).vouchers.get(n).services.size(); b++) {
//													if(member.programmes.get(i).vouchers.get(j).services.get(a).serviceId == programmes.get(0).vouchers.get(n).services.get(b).serviceId) {
//														member.programmes.get(i).vouchers.get(j).services.get(a).notRemovedFromVoucher = true;
//													}
//												}
//											}
//										}
//									}
//								}
//							}
//						
//						for(int i = 0; i < member.programmes.size(); i++) {
//							if(member.programmes.get(i).isActive) {
//								for (int j = 0; j < member.programmes.get(i).vouchers.size(); j++) {
//									if(member.programmes.get(i).vouchers.get(j).isActive) {
//										if(!member.programmes.get(i).vouchers.get(j).valueHasNotChanged) {
//											for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
//												preparedStatement2 = connection.prepareStatement(Queryconstants.updateMemberVouchers);
//												preparedStatement2.setDouble(1, member.programmes.get(i).vouchers.get(j).voucherBalance);
//												preparedStatement2.setDouble(2, member.programmes.get(i).vouchers.get(j).voucherValue);
//												preparedStatement2.setInt(3, member.accId);
//												preparedStatement2.setInt(4, member.programmes.get(i).programmeId);
//												preparedStatement2.setInt(5, member.programmes.get(i).vouchers.get(j).voucherId);
//												preparedStatement2.setInt(6, member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
//												preparedStatement2.executeUpdate();	
//											}
//										}
//										if(!member.programmes.get(i).vouchers.get(j).notNewToProgramme) {
//											for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
//												System.out.print("New voucher is added :"+member.programmes.get(i).vouchers.get(j).voucherId+"\n");
//												preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
//												preparedStatement2.setInt(1, member.programmes.get(i).programmeId);
//												preparedStatement2.setString(2, member.cardNumber);
//												preparedStatement2.setInt(3, member.accId);
//												preparedStatement2.setString(4, member.accNumber);
//												preparedStatement2.setInt(5, member.programmes.get(i).vouchers.get(j).voucherId);
//												preparedStatement2.setInt(6, member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
//												preparedStatement2.setDouble(7, member.programmes.get(i).vouchers.get(j).voucherValue);
//												preparedStatement2.setDouble(8, member.programmes.get(i).vouchers.get(j).voucherValue);
//												preparedStatement2.setDouble(9, member.programmes.get(i).vouchers.get(j).voucherBalance);
//												preparedStatement2.setDouble(10, member.programmes.get(i).vouchers.get(j).voucherBalance);
//				
//												preparedStatement2.setString(11, "2017");
//												preparedStatement2.setDouble(12, member.createdBy);
//												preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
//												preparedStatement2.executeUpdate();	
//											}
//										}
//										else {
//											for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
//												if(!member.programmes.get(i).vouchers.get(j).services.get(k).notNewToVoucher) {
//													System.out.print("New service has been added "+member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
//													preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
//													preparedStatement2.setInt(1, member.programmes.get(i).programmeId);
//													preparedStatement2.setString(2, member.cardNumber);
//													preparedStatement2.setInt(3, member.accId);
//													preparedStatement2.setString(4, member.accNumber);
//													preparedStatement2.setInt(5, member.programmes.get(i).vouchers.get(j).voucherId);
//													preparedStatement2.setInt(6, member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
//													preparedStatement2.setDouble(7, member.programmes.get(i).vouchers.get(j).voucherValue);
//													preparedStatement2.setDouble(8, member.programmes.get(i).vouchers.get(j).voucherValue);
//													preparedStatement2.setDouble(9, member.programmes.get(i).vouchers.get(j).voucherBalance);
//													preparedStatement2.setDouble(10, member.programmes.get(i).vouchers.get(j).voucherBalance);
//					
//													preparedStatement2.setString(11, "2017");
//													preparedStatement2.setDouble(12, member.createdBy);
//													preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
//													preparedStatement2.executeUpdate();	
//												}
//											}
//											
//										}
//										if(!member.programmes.get(i).vouchers.get(j).voucherNotRemoved) {
//											for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
//												preparedStatement2 = connection.prepareStatement(Queryconstants.deleteMemberServices);
//												preparedStatement2.setString(1, member.cardNumber);
//												preparedStatement2.executeUpdate();	
//											}
//										}
//										else {
//											for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
//												if(!member.programmes.get(i).vouchers.get(j).services.get(k).notRemovedFromVoucher) {
//													preparedStatement2 = connection.prepareStatement(Queryconstants.deleteMemberServices);
//													preparedStatement2.setString(1, member.cardNumber);
//													preparedStatement2.executeUpdate();	
//												}
//											}
//											
//										}
										
										
										
//									}
//								}
//							}
//							
//						}
					
						}
					else {
						preparedStatement4 = connection.prepareStatement(Queryconstants.deleteCustomerProgramme);
						preparedStatement4.setInt(1, memberId);
						if(hasShared) {
							preparedStatement11 = connection.prepareStatement(Queryconstants.getFamilyCardNumber);
							preparedStatement11.setInt(1, member.memberId);
							resultSet11 = preparedStatement11.executeQuery();
							if(resultSet11.next()) {								
								preparedStatement12 = connection.prepareStatement(Queryconstants.deleteMemberServices);
								preparedStatement12.setString(1, resultSet11.getString("card_no"));
								preparedStatement12.executeUpdate();								
							}
						}
						preparedStatement4.executeUpdate();
						preparedStatement5 = connection.prepareStatement(Queryconstants.deleteMemberServices);
						preparedStatement5.setString(1, member.cardNumber);
						preparedStatement5.executeUpdate();
						preparedStatement = connection.prepareStatement(Queryconstants.insertCustomerProgramme, Statement.RETURN_GENERATED_KEYS);
						for (int i = 0; i < member.programmes.size(); i++) {
							preparedStatement.setInt(1, memberId);
							preparedStatement.setInt(2,	member.programmes.get(i).programmeId);
							preparedStatement.setDouble(3, member.programmes.get(i).programmeValue);
							preparedStatement.setBoolean(4,	member.programmes.get(i).isActive);
							preparedStatement.setInt(5,	member.programmes.get(i).createdBy);
							preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
							if (preparedStatement.executeUpdate() > 0) {
								if(member.programmes.get(i).isActive) {
									for (int j = 0; j < member.programmes.get(i).vouchers.size(); j++) {
										if(member.programmes.get(i).vouchers.get(j).isActive) {
											for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
												preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
												preparedStatement2.setInt(1, member.programmes.get(i).programmeId);
												preparedStatement2.setString(2, member.cardNumber);
												preparedStatement2.setInt(3, member.accId);
												preparedStatement2.setString(4, member.accNumber);
												preparedStatement2.setInt(5, member.programmes.get(i).vouchers.get(j).voucherId);
												preparedStatement2.setInt(6, member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
												preparedStatement2.setDouble(7, member.programmes.get(i).vouchers.get(j).services.get(k).serviceValue);
												preparedStatement2.setDouble(8, member.programmes.get(i).vouchers.get(j).voucherValue);
												preparedStatement2.setDouble(9, member.programmes.get(i).vouchers.get(j).services.get(k).serviceBalance);
												preparedStatement2.setDouble(10, member.programmes.get(i).vouchers.get(j).voucherBalance);
				
												preparedStatement2.setString(11, "2017");
												preparedStatement2.setDouble(12, member.createdBy);
												preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
												if(preparedStatement2.executeUpdate() <=0) {
													connection.rollback();
													return new CompasResponse(202,"Failed To Update Category Details for Member");
												};	
											}
										}
										if(member.programmes.get(i).vouchers.get(j).isActive && member.programmes.get(i).vouchers.get(j).coverType.equals("1")) {
											preparedStatement13 = connection.prepareStatement(Queryconstants.getFamilyCardNumber);
											preparedStatement13.setInt(1, member.memberId);
											resultSet12 = preparedStatement13.executeQuery();
											if(resultSet12.next()) {		
												for(int k = 0; k < member.programmes.get(i).vouchers.get(j).services.size(); k++) {
													preparedStatement2 = connection.prepareStatement(Queryconstants.insertMemberVouchers);
													preparedStatement2.setInt(1, member.programmes.get(i).programmeId);
													preparedStatement2.setString(2, resultSet12.getString("card_no"));
													preparedStatement2.setInt(3, resultSet12.getInt("acc_id"));
													preparedStatement2.setString(4, resultSet12.getString("acc_no"));
													preparedStatement2.setInt(5, member.programmes.get(i).vouchers.get(j).voucherId);
													preparedStatement2.setInt(6, member.programmes.get(i).vouchers.get(j).services.get(k).serviceId);
													preparedStatement2.setDouble(7, member.programmes.get(i).vouchers.get(j).services.get(k).serviceValue);
													preparedStatement2.setDouble(8, member.programmes.get(i).vouchers.get(j).voucherValue);
													preparedStatement2.setDouble(9, member.programmes.get(i).vouchers.get(j).services.get(k).serviceBalance);
													preparedStatement2.setDouble(10, member.programmes.get(i).vouchers.get(j).voucherBalance);
					
													preparedStatement2.setString(11, "2018");
													preparedStatement2.setDouble(12, member.createdBy);
													preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
													if(preparedStatement2.executeUpdate() <=0) {
														connection.rollback();
														return new CompasResponse(202,"Failed To Update Category Details for Member");
													};	
												}
											}
										}
									}
								}
							}
							else {
								connection.rollback();
								return new CompasResponse(202,"Failed To Update Category Details for Member");
							}
							
						}
//						
					}

					DBOperations.DisposeSql(preparedStatement, rs);
//					preparedStatement = connection.prepareStatement(Queryconstants.deleteMemberFamilyDetails);
//					preparedStatement.setInt(1, memberId);
//					preparedStatement.executeUpdate();
//					DBOperations.DisposeSql(preparedStatement);
					// insert familyMembers
					PreparedStatement preparedStatement15 = connection.prepareStatement(Queryconstants.insertNewMemberFamilyDetails,  Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < member.familyMemList.size(); i++) {
						if(member.familyMemList.get(i).isNew) {
							if (GetMemberByNo(member.familyMemList.get(i).famMemberNo)) {
								connection.rollback();
								return new CompasResponse(201, "Family member Number "+ member.familyMemList.get(i).famMemberNo+" already Exists ");
							}
							preparedStatement15.setInt(1, member.memberId);
							preparedStatement15.setString(2, GeneralUtility.splitPJ(member.familyMemList.get(i).famMemberNo));
							preparedStatement15.setString(3, GeneralUtility.splitMemberNo(member.familyMemList.get(i).famMemberNo));
							preparedStatement15.setString(4, member.familyMemList.get(i).famMemberNo);
							preparedStatement15.setString(5, member.familyMemList.get(i).famMemFullName);
							preparedStatement15.setString(6, member.familyMemList.get(i).famDob);
							preparedStatement15.setString(7, member.familyMemList.get(i).famGender);
							preparedStatement15.setInt(8, member.familyMemList.get(i).relationId);
							preparedStatement15.setInt(9, 1);
							preparedStatement15.setInt(10, member.productId);
							preparedStatement15.setInt(11, member.orgId);
							preparedStatement15.setInt(12, member.createdBy);
							preparedStatement15.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
							preparedStatement15.setString(14, member.memberType);
							preparedStatement15.setString(15, member.familyMemList.get(i).idNumber);
							preparedStatement15.setString(16, member.familyMemList.get(i).lctCode);
							if (preparedStatement15.executeUpdate() <= 0) {
								connection.rollback();
								return new CompasResponse(202,	"Failed To Update Family Details for Member");
							}
							resultSet9 = preparedStatement15.getGeneratedKeys();
							resultSet9.next();
							memberId = resultSet9.getInt(1);
//							member.memberId = memberId;
							DBOperations.DisposeSql(preparedStatement9,resultSet9);
							preparedStatement9 = connection.prepareStatement(Queryconstants.getMaxMemberAccount);
							resultSet9 = preparedStatement9.executeQuery();
							if (resultSet9.next()) {
								member.accNo = resultSet9.getInt("maxaccno") + 1;
								preparedStatement9 = connection.prepareStatement(Queryconstants.insertMemberAccDtl, Statement.RETURN_GENERATED_KEYS);;
								preparedStatement9.setInt(1, memberId);
								preparedStatement9.setInt(2, member.accNo);
								preparedStatement9.setString(3, "NA");
								preparedStatement9.setDouble(4, member.accBalance);
								preparedStatement9.setInt(5, member.createdBy);
								preparedStatement9.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
								preparedStatement9.setString(7, "N");
								
								if(preparedStatement9.executeUpdate() > 0) {
									resultSet9=preparedStatement9.getGeneratedKeys();
									resultSet9.next();
									accountId =resultSet9.getInt(1);
									cardNumber = "804400" + generateNumber();
									DBOperations.DisposeSql(preparedStatement9,resultSet9);
									preparedStatement9 = connection.prepareStatement(Queryconstants.insertCardDetails);
									preparedStatement9.setString(1, String.valueOf(accountId));
									preparedStatement9.setString(2, String.valueOf(memberId));
									preparedStatement9.setString(3, member.familyMemList.get(i).famMemberNo);
									preparedStatement9.setString(4, cardNumber);
									preparedStatement9.setString(5, member.familyMemList.get(i).famMemberNo);
									preparedStatement9.setString(6, "I");
									preparedStatement9.setInt(7, member.createdBy);
									preparedStatement9.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
									
									if(preparedStatement9.executeUpdate() <=0) {
										connection.rollback();
										return new CompasResponse(202, "Failed To Update Card Details for Member");
									}
									DBOperations.DisposeSql(preparedStatement);
									preparedStatement = connection.prepareStatement(Queryconstants.insertCustomerProgramme, Statement.RETURN_GENERATED_KEYS);
									for (int f = 0; f < member.programmes.size(); f++) {
										if(member.programmes.get(f).isActive) {
											for (int j = 0; j < member.programmes.get(f).vouchers.size(); j++) {
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
														preparedStatement2.setString(11, "2020");
														preparedStatement2.setDouble(12, member.createdBy);
														preparedStatement2.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
														if(preparedStatement2.executeUpdate()<=0) {
															connection.rollback();
															return new CompasResponse(202,	"Failed To Update Category Details for Member");
														}
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
						}
					}

					connection.commit();
					return new CompasResponse(200, "Records Updated");

				} 
				else {
					connection.rollback();
					return new CompasResponse(202, "Nothing To Update");
				}

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

	@SuppressWarnings("resource")
	public CompasResponse UpdateMemberCategory(Member member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.deleteCustomerProgramme);
			preparedStatement.setInt(1, member.memberId);
			preparedStatement.executeUpdate();
			DBOperations.DisposeSql(preparedStatement);
			preparedStatement = connection.prepareStatement(Queryconstants.insertCustomerProgramme);
			for (int i = 0; i < member.programmes.size(); i++) {
				preparedStatement.setInt(1, customerId);
				preparedStatement.setInt(2,	member.programmes.get(i).programmeId);
				preparedStatement.setDouble(3, member.programmes.get(i).programmeValue);
				preparedStatement.setBoolean(4,	member.programmes.get(i).isActive);
				preparedStatement.setInt(5, member.programmes.get(i).createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Insert MemberNo"	+ member.memberNo);

				}
			}
			connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateMemberFamily(Member member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.deleteCustomerProgramme);
			preparedStatement.setInt(1, member.memberId);
			preparedStatement.executeUpdate();
			DBOperations.DisposeSql(preparedStatement);
			preparedStatement = connection.prepareStatement(Queryconstants.insertCustomerProgramme);
			for (int i = 0; i < member.programmes.size(); i++) {
				preparedStatement.setInt(1, customerId);
				preparedStatement.setInt(2,	member.programmes.get(i).programmeId);
				preparedStatement.setDouble(3, member.programmes.get(i).programmeValue);
				preparedStatement.setBoolean(4,	member.programmes.get(i).isActive);
				preparedStatement.setInt(5, member.programmes.get(i).createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Insert MemberNo"	+ member.memberNo);

				}
			}
			connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateAndMember(List<Member> member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int customerId = 0;
		try {
			connection = dataSource.getConnection();
			System.out.println("Beneficiary coming from Android device##"
					+ member.size());
			// connection.setAutoCommit(false);
			String example = "SGVsbG8gV29ybGQ=";
			byte[] decoded = org.apache.commons.codec.binary.Base64
					.decodeBase64(example.getBytes());
			// System.out.println(decoded);

			for (int i = 0; i < member.size(); i++) {
				preparedStatement = connection.prepareStatement(Queryconstants.insertAndMemberMaster,Statement.RETURN_GENERATED_KEYS);
				System.out.println("MEMBERNO##" + member.get(i).memberNo);
				preparedStatement.setString(1, member.get(i).memberNo);
				// preparedStatement.setInt(2,
				// member.departmentId);
				preparedStatement.setString(2, member.get(i).lastName);
				preparedStatement.setString(3, member.get(i).title);
				preparedStatement.setString(4, member.get(i).firstName);
				preparedStatement.setString(5, member.get(i).otherName);
				preparedStatement.setInt(6, 0);
				preparedStatement.setInt(7, 0);
				preparedStatement.setString(8, member.get(i).idPassPortNo);
				preparedStatement.setString(9, member.get(i).gender);
				// System.out.println("memberdob" + member.dateOfBirth);
				preparedStatement.setString(10, member.get(i).dateOfBirth);
				preparedStatement.setString(11, member.get(i).maritalStatus);
				preparedStatement.setString(12, member.get(i).nhifNo);
				preparedStatement.setString(13, member.get(i).height);
				preparedStatement.setString(14, member.get(i).weight);
				preparedStatement.setString(15, member.get(i).employerName);
				preparedStatement.setString(16, member.get(i).occupation);
				preparedStatement.setString(17, member.get(i).nationality);
				preparedStatement.setString(18, member.get(i).postalAdd);
				preparedStatement.setString(19, member.get(i).physicalAdd);
				preparedStatement.setString(20, member.get(i).homeTelephone);
				preparedStatement.setString(21, member.get(i).officeTelephone);
				preparedStatement.setString(22, member.get(i).cellPhone);
				preparedStatement.setString(23, member.get(i).email);

				preparedStatement.setString(24, "data:image/png;base64,"
						+ member.get(i).memberPic);
				preparedStatement.setInt(25, member.get(i).createdBy);
				preparedStatement.setTimestamp(26, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(27, "N");
				preparedStatement.setInt(28, member.get(i).familySize);
				preparedStatement.setInt(29, member.get(i).bnfGrpId);
				preparedStatement.setString(30, "N");
				preparedStatement.setInt(31, customerId);

				if (preparedStatement.executeUpdate() > 0) {

					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					customerId = rs.getInt(1);
					String cardNumber = "447744" + generateNumber();
					DBOperations.DisposeSql(preparedStatement);

					preparedStatement = connection.prepareStatement(Queryconstants.insertCardIssuance);
					preparedStatement.setString(1, cardNumber);
					preparedStatement.setString(2, "1234567890");
					preparedStatement.setTimestamp(3, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					preparedStatement.setString(4, member.get(i).expiryDate);
					preparedStatement.setString(5, "I");
					preparedStatement.setInt(6, customerId);
					preparedStatement.setInt(7, member.get(i).programmeId);
					preparedStatement.setInt(8, member.get(i).createdBy);
					preparedStatement.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setString(10, "1234");
					if (preparedStatement.executeUpdate() > 0) {
						DBOperations.DisposeSql(preparedStatement);
						preparedStatement = connection.prepareStatement(Queryconstants.updateCardStatus);
						preparedStatement.setString(1, "I");
						preparedStatement.setInt(2, customerId);
					}
					preparedStatement.executeUpdate();

					DBOperations.DisposeSql(preparedStatement);

					preparedStatement = connection.prepareStatement(Queryconstants.updateAndCustBioId);
					preparedStatement.setInt(1, customerId);
					preparedStatement.setInt(2, customerId);
					preparedStatement.executeUpdate();
					DBOperations.DisposeSql(preparedStatement, rs);
					preparedStatement = connection.prepareStatement(Queryconstants.deleteCustomerProgramme);
					preparedStatement.setInt(1, customerId);
					preparedStatement.executeUpdate();

					DBOperations.DisposeSql(preparedStatement);
					// insert rights
					preparedStatement = connection.prepareStatement(Queryconstants.insertCustomerProgramme);
					preparedStatement.setInt(1, customerId);
					preparedStatement.setInt(2, member.get(i).programmeId);
					preparedStatement.setDouble(3, 0);
					preparedStatement.setBoolean(4, true);
					preparedStatement.setInt(5, member.get(i).createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Programme Id");
					}
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.insertCustBioImages);
					System.out.println("bioimages size##"
							+ member.get(i).bioImages.size());
					for (int j = 0; j < member.get(i).bioImages.size(); j++) {

						preparedStatement.setInt(1, customerId);
						preparedStatement.setString(2,
								member.get(i).bioImages.get(j).image);
						preparedStatement.setTimestamp(
								3,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						preparedStatement.executeUpdate();
					}
				} else {
					return new CompasResponse(201, "Failed to Update");
				}
			}
			// connection.commit();
			return new CompasResponse(200, "Record Updated Successfully");

		} catch (SQLException sqlEx) {

			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Member No Already Exists");
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

	public List<Member> GetCardBlockList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet4 = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardIssDetails);

			preparedStatement.setString(1, "A");
			resultSet = preparedStatement.executeQuery();
			List<Member> members = new ArrayList<Member>();
			while (resultSet.next()) {
				Member objMember = new Member();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.lastName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
				// objMember.nokName = resultSet.getString("NokName");
				// objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
				// objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
				// objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;
				// DBOperations.DisposeSql(preparedStatement);
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setInt(1, objMember.memberId);

				resultSet4 = preparedStatement1.executeQuery();
				while (resultSet4.next()) {
					objMember.cardNumber = resultSet4.getString("cardno");
					objMember.cardBalance = resultSet4.getDouble("balance");

				}
				members.add(objMember);
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Member> GetCardActivationList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardIssDetails);

			preparedStatement.setString(1, "I");
			resultSet = preparedStatement.executeQuery();
			List<Member> members = new ArrayList<Member>();
			while (resultSet.next()) {
				Member objMember = new Member();
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("MemberNo");
				// objMember.departmentId =
				// resultSet.getInt("DepartmentID");
				// objMember.departmentName = resultSet
				// .getString("DepartmentDesc");
				objMember.firstName = resultSet.getString("FirstNames");
				objMember.lastName = resultSet.getString("Surname");
				objMember.title = resultSet.getString("title");
				objMember.otherName = resultSet.getString("OtherName");

				objMember.idPassPortNo = resultSet.getString("IdPPNo");
				objMember.gender = resultSet.getString("Gender");
				objMember.dateOfBirth = resultSet.getString("DateOfBirth");
				objMember.maritalStatus = resultSet.getString("MaritalStatus");
				objMember.nhifNo = resultSet.getString("NhifNo");
				objMember.height = resultSet.getString("Height");
				objMember.weight = resultSet.getString("Weight");
				objMember.employerName = resultSet.getString("EmployerName");
				objMember.occupation = resultSet.getString("Occupation");
				objMember.nationality = resultSet.getString("Nationality");
				objMember.postalAdd = resultSet.getString("PostalAdd");
				objMember.physicalAdd = resultSet.getString("HomeAdd");
				objMember.homeTelephone = resultSet.getString("HomeTeleNo");
				objMember.officeTelephone = resultSet.getString("OfficeTeleNo");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
				// objMember.nokName = resultSet.getString("NokName");
				// objMember.nokIdPpNo = resultSet.getString("NokIdPPNo");
				// objMember.nokTelephoneNo = resultSet.getString("NokPhoneNo");
				// objMember.nokPostalAdd = resultSet.getString("NokPostalAdd");
				objMember.fullName = resultSet.getString("fullname");
				// objMember.familySizeId =
				// resultSet.getInt("FamilySizeID");
				// objMember.familyDesc =
				// resultSet.getString("FamilyDesc");

				// objMember.parentMemberNo = resultSet
				// .getString("ParentMemberNo");
				objMember.bioId = resultSet.getString("bioid");
				// objMember.selected = false;
				objMember.memberPic = resultSet.getString("Pic");
				objMember.respCode = 200;

				members.add(objMember);
			}
			return members;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateCardIssuance(MemberCard card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (checkCardNumber(card.cardNumber)) {
				return new CompasResponse(201, "Crad Number Already Exists");
			}

			preparedStatement = connection
					.prepareStatement(Queryconstants.insertCardIssuance);
			preparedStatement.setString(1, card.cardNumber);
			preparedStatement.setString(2, card.serialNumber);
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(4, card.expiryDate);
			preparedStatement.setString(5, card.status);
			preparedStatement.setInt(6, card.customerId);
			preparedStatement.setInt(7, card.programmeId);
			preparedStatement.setInt(8, card.createdBy);
			preparedStatement.setTimestamp(9, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(10, card.pinNumber);
			if (preparedStatement.executeUpdate() > 0) {
				DBOperations.DisposeSql(preparedStatement);
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardStatus);
				preparedStatement.setString(1, card.status);
				preparedStatement.setInt(2, card.customerId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
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
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkCardNumber);
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

	public CompasResponse updateCardReissue(MemberCard card) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (checkCardNumber(card.cardNumber)) {
				return new CompasResponse(201, "Crad Number Already Exists");
			}
			preparedStatement1 = connection
					.prepareStatement(Queryconstants.insertCardReIssueLog);
			preparedStatement1.setInt(1, card.customerId);
			preparedStatement1.setString(2, card.oldCardNo);
			preparedStatement1.setString(3, card.oldSerialNo);
			preparedStatement1.setString(4, card.oldPinNo);
			preparedStatement1.setInt(5, card.createdBy);
			preparedStatement1.setTimestamp(6, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			if (preparedStatement1.executeUpdate() > 0) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardIssuance);
				preparedStatement.setString(1, card.cardNumber);
				preparedStatement.setString(2, card.serialNumber);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(4, card.expiryDate);
				preparedStatement.setString(5, card.status);
				preparedStatement.setInt(6, card.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(8, card.pinNumber);
				preparedStatement.setInt(8, card.customerId);
				if (preparedStatement.executeUpdate() > 0) {
					DBOperations.DisposeSql(preparedStatement);
					preparedStatement = connection
							.prepareStatement(Queryconstants.updateCardStatus);
					preparedStatement.setString(1, card.status);
					preparedStatement.setInt(2, card.customerId);
				}
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
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

	/**
	 *
	 * @param bnfId
	 * @return
	 */
	public List<Programme> GetProgrammesByBnfId(int bnfId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProgrammesByBnfGrp);
			preparedStatement.setInt(1, bnfId);
			resultSet = preparedStatement.executeQuery();
			List<Programme> objProgrammes = new ArrayList<Programme>();
			while (resultSet.next()) {
				Programme programme = new Programme();
				programme.programmeId = resultSet.getInt("ID");
				programme.programmeCode = resultSet.getString("ProgrammeCode");
				programme.programmeDesc = resultSet.getString("ProgrammeDesc");
				programme.productId = resultSet.getInt("Productid");
				programme.startDate = resultSet.getString("start_date");
				programme.endDate = resultSet.getString("end_date");
				programme.active = resultSet.getBoolean("active");
				programme.createdBy = resultSet.getInt("CreatedBy");
				programme.itmModes = resultSet.getString("itm_modes");
				programme.chtmModes = resultSet.getString("chtm_modes");
				programme.intmModes = resultSet.getString("intm_modes");
//				 programme.isActive=resultSet.getBoolean("isactive");
				preparedStatement = connection.prepareStatement(Queryconstants.getVouchersByProgrammeIdNew);
				preparedStatement.setInt(1, programme.programmeId);
				resultSet2 = preparedStatement.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				while (resultSet2.next()) {
					Voucher voucher = new Voucher();
					if(resultSet2.getBoolean("Isactive")) {
						voucher.voucherId = resultSet2.getInt("voucherId");
						voucher.voucherName = resultSet2.getString("voucher_Name");
						voucher.isActive = resultSet2.getBoolean("Isactive");
						voucher.voucherValue =  resultSet2.getDouble("basket_value");
						voucher.coverType = resultSet2.getString("cover_type");
						preparedStatement1 = connection.prepareStatement(Queryconstants.getServicesByBasket);
						preparedStatement1.setInt(1, resultSet2.getInt("voucherId"));
						preparedStatement1.setInt(2, programme.programmeId);
						preparedStatement1.setInt(3, resultSet2.getInt("voucherId"));
						preparedStatement1.setInt(4, programme.programmeId);
						preparedStatement1.setInt(5, resultSet2.getInt("voucherId"));
						resultSet3 = preparedStatement1.executeQuery();
						List<Service> services = new ArrayList<Service>();
						while (resultSet3.next()) {
							Service service=new Service();
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
						voucher.services = services;
						vouchers.add(voucher);
					}
				}
				programme.vouchers = vouchers;
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

	/**
	 *
	 * @return
	 */
	public List<Relations> GetRelations() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRelations);

			resultSet = preparedStatement.executeQuery();
			List<Relations> relations = new ArrayList<Relations>();
			while (resultSet.next()) {
				relations.add(new Relations(resultSet.getInt("ID"), resultSet
						.getString("RelationDesc"), resultSet
						.getInt("CreatedBy"), 200));
			}
			return relations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateBnfStatus(Member member) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			logger.info("## Member verifications update");
			for (int i = 0; i < member.verifySelection.size(); i++) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateMemStatus);
				preparedStatement.setString(1, member.bnfStatus);
				preparedStatement.setInt(2,
						member.verifySelection.get(i).memberId);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					logger.info("Failed to Update beneficiary Id "
							+ member.verifySelection.get(i).memberId);
					return new CompasResponse(202, "Nothing To Update");
				}
			}
			for(int j=0;j<member.famVerifySelection.size();j++){
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateMemFamStatus);
				preparedStatement.setString(1, member.bnfStatus);
				preparedStatement.setInt(2,
						member.famVerifySelection.get(j).familyMemId);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					logger.info("Failed to Update member Id "
							+ member.famVerifySelection.get(j).familyMemId);
					return new CompasResponse(202, "Nothing To Update");
				}
			}

			// if(Utility.Sendmail(member.userName, member.bnfStatus,
			// member.userEmail)){
			connection.commit();
			return new CompasResponse(200, "Records Updated");
			// }else{
			// connection.rollback();
			// return new CompasResponse(201, "Fail to send an email");
			// }
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

	/**
	 * @author Kibet
	 * @param itemsList
	 * @return
	 */
	public String concatenateListwithComma(List itemsList) {
		String result = "";
		for (int i = 0; i < itemsList.size(); i++) {
			if (i != (itemsList.size() - 1))
				result += "'" + itemsList.get(i) + "'" + ",";
			if (i == (itemsList.size() - 1))
				result = result + "'" + itemsList.get(itemsList.size() - 1)
						+ "'";
		}
		logger.info(result);
		return result;
	}

	/**
	 *
	 * @return
	 */
	private String generateVoucherIdNumber(Topup topup, int cycle) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(topup.beneficiary_id)
				.append(topup.beneficiary_group_id).append(topup.programme_id)
				.append(topup.voucher_id).append(cycle + 1);
		logger.info("##" + stringBuffer.toString());
		return stringBuffer.toString();
	}

	public List<Product> GetScheme(int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveScheme);
			preparedStatement.setInt(1, orgId);
			resultSet = preparedStatement.executeQuery();
			List<Product> relations = new ArrayList<Product>();
			while (resultSet.next()) {
				relations.add(new Product(resultSet.getInt("ID"), resultSet
						.getString("product_code"), resultSet
						.getString("product_name"), resultSet
						.getString("start_date"), resultSet
						.getString("end_date"), resultSet.getDouble("fund")));
			}
			return relations;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public Detach GetMemberDetachDetails(String memberNo) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;

		try {

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberByMemberNo);
			preparedStatement.setString(1, memberNo);
			resultSet = preparedStatement.executeQuery();
			Detach detach = new Detach();
			if (resultSet.next()) {
				detach.memberId = resultSet.getInt("emp_id");
				detach.hasBio = resultSet.getBoolean("bio_status");
				detach.scheme = resultSet.getString("product_name");
				detach.fullName = resultSet.getString("emp_fullname");
				detach.memberNo = memberNo;
				detach.cardNo = resultSet.getString("card_no");

				LocalDate now = LocalDate.now();
				DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate then = LocalDate.parse(resultSet.getString("emp_dob"), format);
				Period period = Period.between(then, now);
				detach.age = period.getYears();
				preparedStatement1 = connection.prepareStatement(Queryconstants.getDetachesByMemberId);
				preparedStatement1.setInt(1, resultSet.getInt("emp_id"));
				resultSet1 = preparedStatement1.executeQuery();
				List<DetachDtl> details = new ArrayList<DetachDtl>();
				while (resultSet1.next()) {
					DetachDtl dtl = new DetachDtl();
					dtl.memberId = resultSet.getInt("emp_id");
					dtl.memberNo = memberNo;
					dtl.reason = resultSet1.getString("reason");
					;
					dtl.createdBy = resultSet1.getString("createdBy");
					dtl.createdOn = resultSet1.getString("createdOn");
					details.add(dtl);
				}
				detach.detaches = details;
			}
			return detach;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse DetachBio(Detach detach) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.insertDetachRecord);
			preparedStatement.setInt(1, detach.memberId);
			preparedStatement.setString(2, detach.memberNo);
			preparedStatement.setString(3, detach.reason);
			preparedStatement.setString(4, detach.createdBy);
			preparedStatement.setString(5, detach.detach);
			preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
			if (preparedStatement.executeUpdate() <= 0) {
				connection.rollback();
				return new CompasResponse(201, "Error in detaching fingerprint");
			}
			preparedStatement1 = connection.prepareStatement(Queryconstants.deleteBioImages);
			preparedStatement1.setString(1, detach.cardNo);
			if (preparedStatement1.executeUpdate() <= 0) {
				connection.rollback();
				return new CompasResponse(201, "Error in detaching fingerprint");
			}
			preparedStatement2 = connection.prepareStatement(Queryconstants.updateBioStatus);
			// preparedStatement2.setString(1, null);
			preparedStatement2.setString(1, detach.cardNo);
			if (preparedStatement2.executeUpdate() <= 0) {
				connection.rollback();
				return new CompasResponse(201, "Error in detaching fingerprint");
			}
			connection.commit();
			return new CompasResponse(200, "Fingerprints detached successfully");
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
	
	public Member GetMemberBasicDetails(String memberNo) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;

		try {

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberBasicDetails);
			preparedStatement.setString(1, memberNo);
			resultSet = preparedStatement.executeQuery();
			Member detach = new Member();
			if(resultSet.next()) {
			detach.memberId = resultSet.getInt("emp_id");	
			detach.scheme = resultSet.getString("product_name");
			detach.fullName = resultSet.getString("emp_fullname");
			detach.memberNo = resultSet.getString("emp_ref_key");
			detach.relation = resultSet.getString("relation");
			detach.principleId = resultSet.getInt("principle_id");
			detach.gender = resultSet.getString("emp_gender");
			detach.cardNumber = resultSet.getString("card_no");
			}
			return detach;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<ApiProgramme> GetMemberProgrammes(Member member) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberProgrammes);
			preparedStatement.setInt(1, member.relation.equals(0) ? member.memberId : member.principleId);
			resultSet = preparedStatement.executeQuery();
			List<ApiProgramme> programme = new ArrayList<ApiProgramme>();
			while (resultSet.next()) {
				ApiProgramme objPrg = new ApiProgramme();
				objPrg.programmeId = resultSet.getInt("programmeid");
				objPrg.schemeId = resultSet.getInt("product_id");
				objPrg.programmeCode = resultSet.getString("programmeCode");
				objPrg.programmeName = resultSet.getString("programmeDesc");
				programme.add(objPrg);
			}
			return programme;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<ApiVoucher> GetMemberVouchers(Member member) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
//		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet4 = null;

		try {
			connection = dataSource.getConnection();
				preparedStatement3 = connection.prepareStatement(Queryconstants.getNewMemberVouchers);
				System.out.println("tuko hapa sasa...");
				preparedStatement3.setInt(1, member.programmeId);
				preparedStatement3.setInt(2, member.relation.equals(0) ? member.memberId : member.principleId);
				preparedStatement3.setInt(3, member.programmeId);
				preparedStatement3.setInt(4, member.memberId);
				resultSet1 = preparedStatement3.executeQuery();
				List<ApiVoucher> vouchers = new ArrayList<ApiVoucher>();
				while (resultSet1.next()) {
					ApiVoucher objVoucher = new ApiVoucher();
					objVoucher.voucherId = resultSet1.getInt("basket_id");
					objVoucher.voucherName = resultSet1.getString("voucher_name");
					objVoucher.voucherCode = resultSet1.getString("voucher_code");
					objVoucher.voucherValue = resultSet1.getString("basket_value");
					objVoucher.voucherBalance = resultSet1.getString("basket_balance");
					objVoucher.accNo = resultSet1.getString("acc_no");
					if(resultSet1.getString("cover_type") == "2") {
						if(member.gender.equals("M") && member.relation.equals(0)) {						
							preparedStatement4 = connection.prepareStatement(Queryconstants.getMMemberServiceDtl);
						}
						else if(member.gender.equals("F") && member.relation.equals(0)) {
							preparedStatement4 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
						}
						else if(member.gender.equals("M") && member.relation.equals(1)) {
							preparedStatement4 = connection.prepareStatement(Queryconstants.get1MMemberServiceDtl);
						}
						else if(member.gender.equals("F") && member.relation.equals(1)) {
							preparedStatement4 = connection.prepareStatement(Queryconstants.get1FMemberServiceDtl);
						}
						else {
							preparedStatement4 = connection.prepareStatement(Queryconstants.get2MemberServiceDtl);
						}
						preparedStatement4.setInt(3, member.relation.equals(0) ? member.memberId : member.principleId);
					}
					else {
						if(member.gender.equals("M") && member.relation.equals(0)) {						
							preparedStatement4 = connection.prepareStatement(Queryconstants.getMMemberServiceDtl);
						}
						else if(member.gender.equals("F") && member.relation.equals(0)) {
							preparedStatement4 = connection.prepareStatement(Queryconstants.getMemberServiceDtl);
						}
						else if(member.gender.equals("M") && member.relation.equals(1)) {
							preparedStatement4 = connection.prepareStatement(Queryconstants.get1MIndividualServiceDtl);
						}
						else if(member.gender.equals("F") && member.relation.equals(1)) {
							preparedStatement4 = connection.prepareStatement(Queryconstants.get1FIndividualServiceDtl);
						}
						else {
							preparedStatement4 = connection.prepareStatement(Queryconstants.get2IndividualServiceDtl);
						}
						preparedStatement4.setInt(3, member.memberId);
					}
					preparedStatement4.setInt(1, member.programmeId);
					preparedStatement4.setInt(2, objVoucher.voucherId);
					resultSet4 = preparedStatement4.executeQuery();
					List<ApiService> services = new ArrayList<ApiService>();
					while (resultSet4.next()) {
						ApiService objSer = new ApiService();
						objSer.serviceId = resultSet4.getInt("service_id");
						objSer.serviceName = resultSet4.getString("ser_name");
						objSer.serviceCode = resultSet4.getString("ser_code");
						objSer.serviceValue = resultSet4.getDouble("service_value");
						objSer.serviceBalance = resultSet4.getDouble("service_balance");
						objSer.voucherBalance = resultSet1.getDouble("basket_balance");
						objSer.requireAuth = resultSet4.getString("require_auth");
						services.add(objSer);
					}
					objVoucher.services = services;
					if(objVoucher.services.size()>0)
					vouchers.add(objVoucher);
				}
				return vouchers;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement);
		}
	}
	
	// dependant Inquiry
	public Member GetDependants(String memberNo) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		PreparedStatement preparedStatement7 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		ResultSet resultSet4 = null;
		ResultSet resultSet5 = null;
		ResultSet resultSet6 = null;
		// int programmeId = 0;
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDependantDetails);
			preparedStatement.setString(1, memberNo);
			resultSet = preparedStatement.executeQuery();
			Member objMember = new Member();
			String strOutpatientStatus = "Active";
			while (resultSet.next()) {
				objMember.memberId = resultSet.getInt("ID");
				objMember.memberNo = resultSet.getString("memberno");
				objMember.fullName = resultSet.getString("name");
				objMember.idPassPortNo = resultSet.getString("nationalid");
				objMember.gender = resultSet.getString("gender");
				objMember.dateOfBirth = resultSet.getString("dob");
				objMember.nhifNo = resultSet.getString("nhif");
				objMember.physicalAdd = resultSet.getString("address");
				objMember.cellPhone = resultSet.getString("MobileNo");
				objMember.email = resultSet.getString("Email");
				objMember.productId = resultSet.getInt("product_id");
				objMember.orgId = resultSet.getInt("org_id");
				objMember.active = resultSet.getBoolean("active");
				objMember.lctCode = resultSet.getString("lct_code");
				objMember.memberType = resultSet.getString("member_type");
				objMember.relation = resultSet.getString("relation");
				objMember.principleId = resultSet.getInt("principle_id");
				objMember.principle = resultSet.getString("principle");
				objMember.respCode = 200;
				if (objMember.active == false) {
					strOutpatientStatus = "Inactive";
				} else {
					objMember.setOutpatientStatus(resultSet.getString("outpatient_status"));
				}
				preparedStatement = connection.prepareStatement(Queryconstants.getCustomerProgrammes);

				// get princple member id using dependant member number
				preparedStatement.setInt(1, objMember.memberId);
				preparedStatement.setInt(2, objMember.memberId);
				resultSet2 = preparedStatement.executeQuery();
				List<Programme> programmes = new ArrayList<Programme>();
				while (resultSet2.next()) {
					if (resultSet2.getBoolean("IsActive")) {
						objMember.programmeDesc = resultSet2.getString("programmeDesc");
						objMember.scheme = resultSet2.getString("product_name");
					}
					// int programmeId = resultSet2.getInt("ProgrammeId");
					Programme programme = new Programme();
					programme.programmeId = resultSet2.getInt("ProgrammeId");
					programme.programmeDesc = resultSet2.getString("ProgrammeDesc");
					programme.programmeCode = resultSet2.getString("ProgrammeCode");
					programme.programmeValue = resultSet2.getDouble("ProgrammeValue");
					programme.isActive = resultSet2.getBoolean("IsActive");
					programme.createdBy = resultSet2.getInt("CreatedBY");
					programme.respCode = 200;
					programme.itmModes = resultSet2.getString("itm_modes");
					programme.chtmModes = resultSet2.getString("chtm_modes");
					programme.intmModes = resultSet2.getString("intm_modes");
					preparedStatement4 = connection.prepareStatement(Queryconstants.getVouchersByMembershipNo);
					int progId = resultSet2.getInt("ProgrammeId");
					preparedStatement4.setString(1, objMember.memberNo);
					preparedStatement4.setString(2, objMember.memberNo);
					System.out.println("memberNo: " + objMember.memberNo);
					// System.out.println("progId: "+progId);
					// preparedStatement4.setInt(1, progId);
					//// preparedStatement4.setInt(2, resultSet2.getInt("org_id"));
					// preparedStatement4.setInt(2, resultSet2.getInt("ProgrammeId"));
					//// preparedStatement4.setInt(4, resultSet2.getInt("org_id"));
					resultSet5 = preparedStatement4.executeQuery();
					List<Voucher> vouchers = new ArrayList<Voucher>();
					while (resultSet5.next()) {
						Voucher voucher = new Voucher();
						voucher.voucherId = resultSet5.getInt("voucherId");
						System.out.println(voucher.toString());
						voucher.voucherDesc = resultSet5.getString("voucher_name");
						voucher.voucherValue = resultSet5.getDouble("basket_value");
						voucher.voucherBalance = resultSet5.getDouble("basket_balance");
						System.out.println("name: " + voucher.voucherDesc + " voucherId: " + voucher.voucherValue
								+ " voucherBalance: " + voucher.voucherBalance);
						voucher.isActive = resultSet5.getBoolean("IsActive");
						voucher.coverType = resultSet5.getString("cover_type");
						preparedStatement5 = connection.prepareStatement(Queryconstants.getServicesByBasket);
						preparedStatement5.setInt(1, resultSet5.getInt("voucherId"));
						preparedStatement5.setInt(2, resultSet2.getInt("ProgrammeId"));
						preparedStatement5.setInt(3, resultSet5.getInt("voucherId"));
						preparedStatement5.setInt(4, resultSet2.getInt("ProgrammeId"));
						preparedStatement5.setInt(5, resultSet5.getInt("voucherId"));
						resultSet6 = preparedStatement5.executeQuery();
						List<Service> services = new ArrayList<Service>();
						while (resultSet6.next()) {
							Service service = new Service();
							service.requireAuth = resultSet6.getBoolean("require_auth");
							service.serviceId = resultSet6.getInt("Service_Id");
							service.serviceName = resultSet6.getString("ser_name");
							if (resultSet6.getDouble("cover_limit") == 0) {
								service.serviceBalance = resultSet6.getDouble("basket_value");
								service.serviceValue = resultSet6.getDouble("basket_value");
							} else {
								service.serviceBalance = resultSet6.getDouble("cover_limit");
								service.serviceValue = resultSet6.getDouble("cover_limit");
							}
							service.price = resultSet6.getDouble("basket_value");
							services.add(service);
						}
						voucher.services = services;
						vouchers.add(voucher);
					}
					programme.vouchers = vouchers;
					programmes.add(programme);
				}

				preparedStatement1 = connection.prepareStatement(Queryconstants.getCardBalance);
				preparedStatement1.setString(1, memberNo);

				resultSet3 = preparedStatement1.executeQuery();
				if (resultSet3.next()) {
					objMember.cardNumber = resultSet3.getString("card_no");
					objMember.accId = resultSet3.getInt("acc_id");
					// /objMember.cardBalance = resultSet3.getDouble("balance");
				}
				// preparedStatement6 =
				// connection.prepareStatement(Queryconstants.getAccountNumber);
				// preparedStatement6.setInt(1, resultSet3.getInt("acc_id"));

				// resultSet6 = preparedStatement6.executeQuery();
				// if (resultSet6.next()) {
				// objMember.accNumber = resultSet6.getString("acc_no");
				// }

				objMember.programmes = programmes;
				PreparedStatement preparedStatement2 = connection
						.prepareStatement(Queryconstants.getNewBeneficiaryFamilyMembers);
				preparedStatement2.setInt(1, objMember.memberId);
				resultSet2 = preparedStatement2.executeQuery();
				List<BeneficiaryFamilyMembers> familyMembers = new ArrayList<BeneficiaryFamilyMembers>();
				while (resultSet2.next()) {
					BeneficiaryFamilyMembers famMember = new BeneficiaryFamilyMembers();
					famMember.familyMemId = resultSet2.getInt("emp_id");
					famMember.famMemFullName = resultSet2.getString("emp_fullname");
					famMember.famMemberNo = resultSet2.getString("emp_ref_key");
					famMember.famGender = resultSet2.getString("emp_gender");
					famMember.famDob = resultSet2.getString("emp_dob");
					famMember.relationId = resultSet2.getInt("relation");
					famMember.lctCode = resultSet2.getString("lct_code");
					famMember.memberType = resultSet2.getString("member_type");
					famMember.famMemNationalId = resultSet2.getString("emp_nationalid");
					if (resultSet2.getInt("relation") == 1)
						objMember.hasSpouse = true;
					familyMembers.add(famMember);
				}
				objMember.familyMemList = familyMembers;

				preparedStatement3 = connection.prepareStatement(Queryconstants.getVouchersByMemberNo);
				preparedStatement3.setInt(1, objMember.memberId);
				preparedStatement3.setInt(2, objMember.memberId);
				resultSet4 = preparedStatement3.executeQuery();
				List<Voucher> vouchers = new ArrayList<Voucher>();
				if (!resultSet4.next()) {
					strOutpatientStatus = "Active";
					resultSet4.beforeFirst();
				}
				while (resultSet4.next()) {
					Voucher voucher = new Voucher();
					voucher.voucherName = resultSet4.getString("voucher_name");
					voucher.amountRemaining = df.format(resultSet4.getDouble("basket_balance"));
					voucher.used = df.format(resultSet4.getDouble("used"));
					voucher.value = df.format(resultSet4.getDouble("basket_value"));
					voucher.schemeType = resultSet4.getString("scheme_type");
					vouchers.add(voucher);
					System.out.println("voucher name...1: " + voucher.voucherName + " voucherValue: " + voucher.value
							+ " voucher used: " + voucher.used + " scheme: " + voucher.schemeType);

					if (voucher.voucherName != null && voucher.schemeType.equals("O")
							&& Double.parseDouble(voucher.value.replace(",", "")) != 0) {
						double percent = Double.parseDouble(voucher.used.replace(",", ""))
								/ Double.parseDouble(voucher.value.replace(",", "")) * 100;
						System.out.println("percentage used by member: " + percent);
						if (percent > 75) {
							strOutpatientStatus = "Inactive";
						}
					}
				}
				objMember.vouchers = getMemberVouchersByMemberNo(objMember.memberId, objMember.memberId);// vouchers;
				// set outpatient status to active
				preparedStatement7 = connection.prepareStatement(Queryconstants.updateMemberStatus);
				preparedStatement7.setString(1, strOutpatientStatus);
				preparedStatement7.setInt(2, objMember.memberId);
				preparedStatement7.executeUpdate();
				objMember.setOutpatientStatus(strOutpatientStatus);
			}
			return objMember;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}
	
	// member de-activation
		public Deactivate GetMemberDeactivationDetails(String memberNo) {
			// TODO Auto-generated method stub
			DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			PreparedStatement preparedStatement1 = null;
			ResultSet resultSet = null;
			ResultSet resultSet1 = null;
			try {
				connection = dataSource.getConnection();
				preparedStatement = connection.prepareStatement(Queryconstants.getMemberByMemberNo);
				preparedStatement.setString(1, memberNo);
				resultSet = preparedStatement.executeQuery();
				Deactivate deactivate = new Deactivate();
				if (resultSet.next()) {
					deactivate.memberId = resultSet.getInt("emp_id");
					deactivate.isActive = resultSet.getBoolean("active");
					deactivate.scheme = resultSet.getString("product_name");
					deactivate.fullName = resultSet.getString("emp_fullname");
					deactivate.memberNo = memberNo;
					deactivate.cardNo = resultSet.getString("card_no");
					LocalDate now = LocalDate.now();
					DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate then = LocalDate.parse(resultSet.getString("emp_dob"), format);
					Period period = Period.between(then, now);
					deactivate.age = period.getYears();
					preparedStatement1 = connection.prepareStatement(Queryconstants.getDeactivatesByMemberId);
					preparedStatement1.setInt(1, resultSet.getInt("emp_id"));
					resultSet1 = preparedStatement1.executeQuery();
					List<DeactivateDtl> deactivatedetails = new ArrayList<DeactivateDtl>();
					while (resultSet1.next()) {
						DeactivateDtl deactivatedetail = new DeactivateDtl();
						deactivatedetail.memberId = resultSet.getInt("emp_id");
						deactivatedetail.memberNo = memberNo;
						deactivatedetail.reason = resultSet1.getString("reason");
						deactivatedetail.createdBy = resultSet1.getString("createdBy");
						deactivatedetail.createdOn = resultSet1.getString("createdOn");
						deactivatedetails.add(deactivatedetail);
					}
					deactivate.deactivates = deactivatedetails;
				}
				return deactivate;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			} finally {
				DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			}
		}

		public CompasResponse DeactivateMember(Deactivate deactivate) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			PreparedStatement preparedStatement1 = null;
			PreparedStatement preparedStatement2 = null;

			try {
				connection = dataSource.getConnection();
				connection.setAutoCommit(false);
				preparedStatement = connection.prepareStatement(Queryconstants.insertDeativateRecord);
				preparedStatement.setInt(1, deactivate.memberId);
				preparedStatement.setString(2, deactivate.memberNo);
				preparedStatement.setString(3, deactivate.reason);
				preparedStatement.setString(4, deactivate.createdBy);
				preparedStatement.setString(5, deactivate.deactivate);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(201, "Error in deactivating member");
				}
				preparedStatement2 = connection.prepareStatement(Queryconstants.updateCustomerStatus);
				// preparedStatement2.setString(1, null);
				preparedStatement2.setInt(1, deactivate.memberId);
				if (preparedStatement2.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(201, "Error in deactivating member");
				}
				connection.commit();
				return new CompasResponse(200, "Member deactivated successfully");
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

		public Member GetDeactivationMemberBasicDetails(String memberNo) {
			DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {

				connection = dataSource.getConnection();
				preparedStatement = connection.prepareStatement(Queryconstants.getMemberBasicDetails);
				preparedStatement.setString(1, memberNo);
				resultSet = preparedStatement.executeQuery();
				Member deactivate = new Member();
				if (resultSet.next()) {
					deactivate.memberId = resultSet.getInt("emp_id");
					deactivate.scheme = resultSet.getString("product_name");
					deactivate.fullName = resultSet.getString("emp_fullname");
					deactivate.memberNo = resultSet.getString("emp_ref_key");
					deactivate.relation = resultSet.getString("relation");
					deactivate.principleId = resultSet.getInt("principle_id");
					deactivate.gender = resultSet.getString("emp_gender");
					deactivate.cardNumber = resultSet.getString("card_no");
				}
				return deactivate;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			} finally {
				DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			}

		}
}
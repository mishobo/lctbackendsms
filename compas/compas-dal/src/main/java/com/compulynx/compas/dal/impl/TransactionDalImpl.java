/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.text.DecimalFormat;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.compulynx.compas.dal.TransactionDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.AuthTransaction;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Claim;
import com.compulynx.compas.models.ClaimDtl;
import com.compulynx.compas.models.ClaimRemitance;
import com.compulynx.compas.models.ClaimReports;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Remitance;
import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.TransactionDtl;
import com.compulynx.compas.models.TransactionMst;
import com.compulynx.compas.models.android.ApiService;

/**
 * @author Anita
 *
 */
public class TransactionDalImpl implements TransactionDal {

	private DataSource dataSource;

	private static final Logger logger = Logger.getLogger(TransactionDalImpl.class.getName());

	public TransactionDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public List<Merchant> GetActiveProviderList() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getActiveProviderList);
			resultSet = preparedStatement.executeQuery();
			List<Merchant> txns = new ArrayList<Merchant>();
			while (resultSet.next()) {

				Merchant obj = new Merchant();
				obj.merchantId = resultSet.getInt("id");
				obj.merchantName = resultSet.getString("mer_name");
				obj.count = count++;
				txns.add(obj);
			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// get claims transactions
	public List<Claim> GetAkuhTransactionList(Claim claimtxns) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			if (claimtxns.txnStatus == 1) {
				logger.info("Get Verified Claims");
				preparedStatement = connection.prepareStatement(Queryconstants.getVerifiedClaims);
				preparedStatement.setString(1, claimtxns.fromDt);
				preparedStatement.setString(2, claimtxns.toDt);
			} else if (claimtxns.txnStatus == 0) {
				logger.info("Get Outstanding claims");
				preparedStatement = connection.prepareStatement(Queryconstants.getClaimsToVerify);
				preparedStatement.setString(1, claimtxns.fromDt);
				preparedStatement.setString(2, claimtxns.toDt);
			} else if (claimtxns.txnStatus == 2) {
				logger.info("Get Approved Claims");
				// preparedStatement =
				// connection.prepareStatement(Queryconstants.getApprovedTransactionList);
				preparedStatement = connection.prepareStatement(Queryconstants.getApprovedClaims);
				preparedStatement.setString(1, claimtxns.fromDt);
				preparedStatement.setString(2, claimtxns.toDt);
				// preparedStatement.setInt(3, claimtxns.orgId);
			}
			resultSet = preparedStatement.executeQuery();
			List<Claim> claimtxn = new ArrayList<Claim>();
			while (resultSet.next()) {
				Claim obj = new Claim();
				obj.id = resultSet.getLong("id");
				obj.insuranceName = resultSet.getString("insuranceName");
				obj.schemeCode = resultSet.getString("schemeCode");
				obj.schemeName = resultSet.getString("schemeName");
				obj.chargeDate = resultSet.getString("chargeDate");
				obj.patientName = resultSet.getString("patientName");
				obj.membershipNo = resultSet.getString("membershipNo");
				obj.sponsorNet = resultSet.getDouble("sponsorNet");
				obj.BillNo = resultSet.getString("BillNo");
				obj.count = count++;
				obj.reason = "";
				obj.allowed = resultSet.getDouble("sponsorNet");
				obj.allowed = resultSet.getDouble("amount");
				obj.amount = resultSet.getDouble("amount");
				// obj.voucherId = resultSet.getInt("voucher_id");
				// obj.basketId = resultSet.getInt("basket_id");
				// obj.serviceId = resultSet.getInt("service_id");
				obj.providerName = resultSet.getString("providerName");
				// obj.voucherName = resultSet.getString("voucher_name");
				// obj.serviceName = resultSet.getString("ser_name");

				preparedStatement2 = connection.prepareStatement(Queryconstants.getClaimTransactionDetails);
				preparedStatement2.setInt(1, resultSet.getInt("id"));
				resultSet2 = preparedStatement2.executeQuery();
				List<ClaimDtl> claimtransactionDetails = new ArrayList<ClaimDtl>();
				while (resultSet2.next()) {
					ClaimDtl claimtransactionDetail = new ClaimDtl();
					claimtransactionDetail.id = resultSet2.getInt("claim_id");
					claimtransactionDetail.voucherName = resultSet2.getString("voucher_name");
					claimtransactionDetail.serName = resultSet2.getString("ser_name");
					claimtransactionDetail.txnAmount = resultSet2.getInt("sponsorNet");
					// claimtransactionDetail.voucherId = resultSet.getInt("basket_id");
					// transactionDetail.programmeId = resultSet.getInt("programme_id");
					// claimtransactionDetail.serviceId = resultSet.getInt("service_id");
					claimtransactionDetails.add(claimtransactionDetail);
				}
				obj.claimtransactionDetails = claimtransactionDetails;
				claimtxn.add(obj);
			}
			return claimtxn;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Transaction> GetTransactionList(Transaction txn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			if (txn.txnStatus == 1) {
				logger.info("Get Verified transactions");
				// preparedStatement =
				// connection.prepareStatement(Queryconstants.getVerifiedTransactionList);
				preparedStatement = connection.prepareStatement(Queryconstants.getVerifiedTransactions);
			} else if (txn.txnStatus == 0) {
				logger.info("Get Outstanding transactions");
				// preparedStatement =
				// connection.prepareStatement(Queryconstants.getTransactionList);
				preparedStatement = connection.prepareStatement(Queryconstants.getBillTransactions);
			} else if (txn.txnStatus == 2) {
				logger.info("Get Approved transactions");
				// preparedStatement =
				// connection.prepareStatement(Queryconstants.getApprovedTransactionList);
				preparedStatement = connection.prepareStatement(Queryconstants.getApprovedTransactions);
				preparedStatement.setString(1, txn.fromDt);
				preparedStatement.setString(2, txn.toDt);
				preparedStatement.setInt(3, txn.orgId);
			}
			preparedStatement.setString(1, txn.fromDt);
			preparedStatement.setString(2, txn.toDt);
			preparedStatement.setInt(3, txn.providerId);
//			preparedStatement.setInt(4, txn.txnStatus);
//			preparedStatement.setInt(5, txn.paidStatus);
			preparedStatement.setInt(4, txn.orgId);
			resultSet = preparedStatement.executeQuery();
			List<Transaction> txns = new ArrayList<Transaction>();
			while (resultSet.next()) {
				Transaction obj = new Transaction();
				obj.txnId = resultSet.getInt("id");
				obj.accNo = resultSet.getString("acc_no");
				obj.memberNo = resultSet.getString("member_no");
				obj.providerName = resultSet.getString("provider_name");
				obj.bnfDesc = resultSet.getString("benefit_desc");
				obj.amount = resultSet.getDouble("amount");
				obj.allowed = resultSet.getDouble("amount");
				obj.transDate = resultSet.getString("trans_date");
				obj.image = resultSet.getString("image");
				obj.count = count++;
				obj.reason = "";
				obj.isActive = false;
				obj.reject = false;
				obj.voucherId = resultSet.getInt("basket_id");
				obj.programmeId = resultSet.getInt("programme_id");
				obj.serviceId = resultSet.getInt("service_id");

				preparedStatement1 = connection.prepareStatement(Queryconstants.getempName);
				preparedStatement1.setString(1, obj.memberNo);
				preparedStatement1.setString(2, obj.memberNo);
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {
					obj.empName = resultSet1.getString("name");
				}
				preparedStatement3 = connection.prepareStatement(Queryconstants.getTransactionDetails);
				preparedStatement3.setInt(1, resultSet.getInt("id"));
				resultSet3 = preparedStatement3.executeQuery();
				List<TransactionDtl> transactionDetails = new ArrayList<TransactionDtl>();
				while (resultSet3.next()) {
					TransactionDtl transactionDetail = new TransactionDtl();
					transactionDetail.id = resultSet3.getInt("id");
					transactionDetail.voucherName = resultSet3.getString("voucher_name");
					transactionDetail.serName = resultSet3.getString("ser_name");
					transactionDetail.txnAmount = resultSet3.getInt("txn_amount");
					transactionDetail.voucherId = resultSet.getInt("basket_id");
					transactionDetail.programmeId = resultSet.getInt("programme_id");
					transactionDetail.serviceId = resultSet.getInt("service_id");
					transactionDetails.add(transactionDetail);
				}
				obj.transactionDetails = transactionDetails;
				txns.add(obj);
			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@SuppressWarnings("resource")
	public CompasResponse UpdateCardStatus(Transaction trans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			if (trans.txnStatus == 1) {

				for (int i = 0; i < trans.selectedTrans.size(); i++) {
					if (trans.selectedTrans.get(i).txnStatus == -1) {
						preparedStatement = connection.prepareStatement(Queryconstants.updRejectedTrans);
						preparedStatement.setInt(1, -1);
						preparedStatement.setInt(2, -111);
						preparedStatement.setInt(3, trans.createdBy);
						preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement.setString(5, trans.selectedTrans.get(i).reason);
						preparedStatement.setInt(6, trans.selectedTrans.get(i).txnId);
					} else {
						preparedStatement = connection.prepareStatement(Queryconstants.updMemberTransStatus);						
						preparedStatement.setInt(1, trans.txnStatus);
						preparedStatement.setInt(2, trans.paidStatus);
						preparedStatement.setInt(3, trans.createdBy);
						preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement.setInt(5, trans.selectedTrans.get(i).txnId);
					}

					if (preparedStatement.executeUpdate() <= 0) {
						connection.rollback();
						throw new Exception("Failed to Update Transaction Id " + trans.selectedTrans.get(i).txnId);
					}
				}
			} else if (trans.txnStatus == 2) {

				for (int i = 0; i < trans.selectedTrans.size(); i++) {
					if (trans.selectedTrans.get(i).txnStatus == -1) {
						preparedStatement = connection.prepareStatement(Queryconstants.updRejectedTrans);
						preparedStatement.setInt(1, -1);
						preparedStatement.setInt(2, -111);
						preparedStatement.setInt(3, trans.createdBy);
						preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement.setString(5, trans.selectedTrans.get(i).reason);
						preparedStatement.setInt(6, trans.selectedTrans.get(i).txnId);
					} else {
						preparedStatement = connection.prepareStatement(Queryconstants.updTransApprovedStatus);
						preparedStatement.setInt(1, trans.txnStatus);
						preparedStatement.setInt(2, trans.paidStatus);
						preparedStatement.setInt(3, trans.createdBy);
						preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						preparedStatement.setInt(5, trans.selectedTrans.get(i).txnId);
					}

					if (preparedStatement.executeUpdate() <= 0) {
						connection.rollback();
						throw new Exception("Failed to Update Transaction Id " + trans.selectedTrans.get(i).txnId);
					}
				}
			} else if (trans.txnStatus == 3) {
				preparedStatement = connection.prepareStatement(Queryconstants.updTransApprovedStatus);
				for (int i = 0; i < trans.selectedTrans.size(); i++) {
					preparedStatement.setInt(1, trans.txnStatus);
					preparedStatement.setInt(2, trans.paidStatus);
					preparedStatement.setInt(3, trans.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setInt(5, trans.selectedTrans.get(i).txnId);

					if (preparedStatement.executeUpdate() <= 0) {
						connection.rollback();
						throw new Exception("Failed to Update Transaction Id " + trans.selectedTrans.get(i).txnId);
					}
				}
			}
			connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@SuppressWarnings("resource")
	public CompasResponse UpdateTransStatus(Transaction trans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (trans.txnStatus == 1) {
				preparedStatement = connection.prepareStatement(Queryconstants.updMemberTransDtlStatus);
				System.out.println("Allowed vetting amount:: "+trans.allowed);
				System.out.println("Amount:: "+trans.amount);
				if(trans.allowed<trans.amount) {
					trans.paidStatus=115;
					trans.txnStatus=4;
					trans.amount=trans.amount-trans.allowed;
					logger.info("Paid status:: "+trans.paidStatus);
					logger.info("Remaining amount after vetting:: "+trans.amount);
				}
				preparedStatement.setDouble(1, trans.amount);
				preparedStatement.setInt(2, trans.txnStatus);
				preparedStatement.setInt(3, trans.paidStatus);
				preparedStatement.setDouble(4, trans.allowed);
				preparedStatement.setInt(5, trans.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(7, trans.txnId);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(200, "Failed to Update Transaction Id " + trans.txnId);
				}
				//insert audit trail
				preparedStatement3 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement3.setInt(1, trans.createdBy);
				preparedStatement3.setInt(2, trans.createdBy);
				preparedStatement3.setString(3, "Vetted Transaction id: " +trans.txnId);
				preparedStatement3.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement3.setString(5, "Bill vetting Module");
				if (preparedStatement3.executeUpdate() <= 0) {
					connection.rollback();			
					return new CompasResponse(200, "Failed to add audit trail" + trans.txnId);
			}
				logger.info("Audit Trail for vetting transactions captured");
				if (trans.diff > 0) {
					System.out.println(trans.diff);
					System.out.println(trans.accNo);
					System.out.println(trans.serviceId);
					System.out.println(trans.voucherId);
					System.out.println(trans.programmeId);
					preparedStatement1 = connection.prepareStatement(Queryconstants.updateVerifiedBasketBalance);
					preparedStatement1.setDouble(1, trans.diff);
					preparedStatement1.setString(2, trans.accNo);
					preparedStatement1.setInt(3, trans.voucherId);
					preparedStatement1.setInt(4, trans.programmeId);
					preparedStatement1.executeUpdate();
					preparedStatement2 = connection.prepareStatement(Queryconstants.updateVerifiedServiceBalance);
					preparedStatement2.setDouble(1, trans.diff);
					preparedStatement2.setString(2, trans.accNo);
					preparedStatement2.setInt(3, trans.serviceId);
					preparedStatement2.setInt(4, trans.voucherId);
					preparedStatement2.setInt(5, trans.programmeId);
					preparedStatement2.executeUpdate();

				}

			} else if (trans.txnStatus == -1) {
				preparedStatement = connection.prepareStatement(Queryconstants.updCancelledTransDtl);
				preparedStatement.setInt(1, -1);
				preparedStatement.setInt(2, -111);
				preparedStatement.setInt(3, trans.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(5, trans.reason);
				preparedStatement.setInt(6, trans.txnId);

				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Update Transaction Id " + trans.txnId);
				}
				//insert audit trail
				preparedStatement3 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement3.setInt(1, trans.createdBy);
				preparedStatement3.setInt(2, trans.createdBy);
				preparedStatement3.setString(3, "Declined Approval for Transaction id: " +trans.txnId);
				preparedStatement3.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement3.setString(5, "Bill Approval Module");
				if (preparedStatement3.executeUpdate() <= 0) {
					connection.rollback();			
					return new CompasResponse(200, "Failed to add audit trail" + trans.txnId);
			}
			} else if (trans.txnStatus == 2) {
				preparedStatement = connection.prepareStatement(Queryconstants.updApprovedMemberTransDtl);
				System.out.println("Allowed amount:: "+trans.allowed);
				System.out.println("Amount to approve:: "+trans.amount);
				if(trans.allowed<trans.amount) {
					trans.paidStatus=114;
					trans.txnStatus=5;
					trans.amount=trans.amount-trans.allowed;
					logger.info("Paid status:: "+trans.paidStatus);
					logger.info("Remaining amount after approval:: "+trans.amount);
				}
				preparedStatement.setDouble(1, trans.amount);
				preparedStatement.setInt(2, trans.txnStatus);
				preparedStatement.setInt(3, trans.paidStatus);
				preparedStatement.setDouble(4, trans.allowed);
				preparedStatement.setInt(5, trans.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(7, trans.txnId);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Update Transaction Id " + trans.txnId);
				}
				//insert audit trail
				preparedStatement3 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement3.setInt(1, trans.createdBy);
				preparedStatement3.setInt(2, trans.createdBy);
				preparedStatement3.setString(3, "Approved Transaction id: " +trans.txnId);
				preparedStatement3.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement3.setString(5, "Bill Approval Module");
				if (preparedStatement3.executeUpdate() <= 0) {
					connection.rollback();			
					return new CompasResponse(200, "Failed to add audit trail" + trans.txnId);
			}
			} else if (trans.txnStatus == 0) {
				preparedStatement = connection.prepareStatement(Queryconstants.updAuthMemberTransDtl);
				preparedStatement.setInt(1, trans.txnStatus);
				preparedStatement.setDouble(2, trans.allowed);
				preparedStatement.setInt(3, trans.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(5, trans.txnId);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Update Transaction Id " + trans.txnId);
				}
			}
			connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Transaction> GetApprovedTrans(Transaction txn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			logger.info("Get Approved transactions");
			// preparedStatement =
			// connection.prepareStatement(Queryconstants.getApprovedTransactionList);
			preparedStatement = connection.prepareStatement(Queryconstants.getApprovedTransactions);
			preparedStatement.setString(1, txn.fromDt);
			preparedStatement.setString(2, txn.toDt);
			preparedStatement.setInt(3, txn.providerId);
			preparedStatement.setInt(4, txn.orgId);

			resultSet = preparedStatement.executeQuery();
			List<Transaction> txns = new ArrayList<Transaction>();
			while (resultSet.next()) {
				Transaction obj = new Transaction();
				obj.providerCode = resultSet.getString("mer_code");
				obj.providerName = resultSet.getString("mer_name");
				obj.bankCode = resultSet.getString("bank_code");
				obj.bankName = resultSet.getString("bank_name");
				obj.amount = resultSet.getDouble("amount");
				obj.txnIds = resultSet.getString("txnid");
				obj.count = count++;
				obj.isActive = false;
				List<Remitance> dtls = new ArrayList<Remitance>();
				List<String> ids = Arrays.asList(obj.txnIds.split("\\s*,\\s*"));
				for (int i = 0; i < ids.size(); i++) {
					preparedStatement1 = connection.prepareStatement(Queryconstants.getApprovedTransactionDtl);
					preparedStatement1.setString(1, ids.get(i));
					resultSet1 = preparedStatement1.executeQuery();
					while (resultSet1.next()) {
						Remitance dtl = new Remitance();
						dtl.txnId = Integer.valueOf(ids.get(i));
						dtl.billNo = resultSet1.getString("bill_no");
						dtl.memberNo = resultSet1.getString("member_no");
						dtl.memberName = resultSet1.getString("member_name");
						dtl.transDate = resultSet1.getString("trans_date");
						dtl.amount = resultSet1.getDouble("amount");
						dtl.txn_amount = resultSet1.getDouble("txn_amount");
						dtl.providerName = resultSet1.getString("provider_name");
						dtl.basketName = resultSet1.getString("voucher_name");
						dtl.serviceName = resultSet1.getString("ser_name");
						dtl.approvedBy = resultSet1.getString("username");
						dtl.approvedOn = resultSet1.getString("approved_on");
						dtls.add(dtl);
					}
				}
				obj.details = dtls;
				txns.add(obj);

			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse GenerateSettlementFile(Transaction trans, String filePath) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();
			dateFormat.format(cal.getTime());

			// String filePath
			// =System.getProperty("catalina.base")+"/Settlement/"+"JDC_"+dateFormat.format(cal.getTime());
			String tst = trans.selectedTrans.get(0).providerCode + dateFormat.format(cal.getTime()) + ".xls";
			String strfilePath = filePath + tst;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("FirstSheet");

			HSSFCellStyle my_style = workbook.createCellStyle();
			HSSFCellStyle style = workbook.createCellStyle();
			HSSFCellStyle style1 = workbook.createCellStyle();
			HSSFFont my_font = workbook.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);
			style1.setFont(my_font);
			style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Bill number");
			rowhead.createCell(1).setCellValue("Transaction date");
			rowhead.createCell(2).setCellValue("Member number");
			rowhead.createCell(3).setCellValue("Member name");
			rowhead.createCell(4).setCellValue("Service provider");
			rowhead.createCell(5).setCellValue("Basket");
			rowhead.createCell(6).setCellValue("Service");
			rowhead.createCell(7).setCellValue("Amount requested");
			rowhead.createCell(8).setCellValue("Amount paid");
			rowhead.createCell(9).setCellValue("Transfer date");
			for (int i = 0; i < rowhead.getLastCellNum(); i++) {// For each cell in the row
				rowhead.getCell(i).setCellStyle(my_style);// Set the style
			}
			int count = 0;
			double txn_amount = 0;
			double amount = 0;
			for (int i = 0; i < trans.selectedTrans.size(); i++) {
				for (int j = 0; j < trans.selectedTrans.get(i).details.size(); j++) {
					count++;
					HSSFRow row = sheet.createRow((short) count);
					row.createCell(0).setCellValue(trans.selectedTrans.get(i).details.get(j).billNo);
					row.createCell(1).setCellValue(trans.selectedTrans.get(i).details.get(j).transDate);
					row.createCell(2).setCellValue(trans.selectedTrans.get(i).details.get(j).memberNo);
					row.createCell(3).setCellValue(trans.selectedTrans.get(i).details.get(j).memberName);
					row.createCell(4).setCellValue(trans.selectedTrans.get(i).details.get(j).providerName);
					row.createCell(5).setCellValue(trans.selectedTrans.get(i).details.get(j).basketName);
					row.createCell(6).setCellValue(trans.selectedTrans.get(i).details.get(j).serviceName);
					row.createCell(7).setCellValue(trans.selectedTrans.get(i).details.get(j).txn_amount);
					row.createCell(8).setCellValue(trans.selectedTrans.get(i).details.get(j).amount);
					row.createCell(9).setCellValue(trans.transferDate);
					row.getCell(7).setCellStyle(style);
					row.getCell(8).setCellStyle(style);
					txn_amount += trans.selectedTrans.get(i).details.get(j).txn_amount;
					amount += trans.selectedTrans.get(i).details.get(j).amount;

				}

			}
			HSSFRow row1 = sheet.createRow((short) count + 1);
			row1.createCell(7).setCellValue(txn_amount);
			row1.createCell(8).setCellValue(amount);
			row1.getCell(7).setCellStyle(style1);
			row1.getCell(8).setCellStyle(style1);

			FileOutputStream fileOut = new FileOutputStream(strfilePath);
			workbook.write(fileOut);
			fileOut.close();
			logger.info("Your excel file has been generated!");
			return new CompasResponse(200, "Records Updated", tst);

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Failed to generate file");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateSettlementStatus(Transaction trans, String fileName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.updSettementFileStatus);
			for (int i = 0; i < trans.selectedTrans.size(); i++) {
				List<String> items = Arrays.asList(trans.selectedTrans.get(i).txnIds.split("\\s*,\\s*"));
				for (int j = 0; j < items.size(); j++) {
					preparedStatement.setInt(1, 3);
					preparedStatement.setInt(2, 113);
					preparedStatement.setInt(3, trans.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setString(5, fileName);
					preparedStatement.setInt(6, Integer.valueOf(items.get(j)));
					if (preparedStatement.executeUpdate() <= 0) {
						connection.rollback();
						throw new Exception("Failed to Update Transaction Id " + trans.selectedTrans.get(i).txnId);
					}
					
					//insert audit trail
					preparedStatement1 = connection.prepareStatement(Queryconstants.insertAuditTrail);
					preparedStatement1.setInt(1, trans.createdBy);
					preparedStatement1.setInt(2, trans.createdBy);
					preparedStatement1.setString(3, "Settled Transaction id: " +trans.txnId);
					preparedStatement1.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement1.setString(5, "Bill Settlement Module");
					if (preparedStatement1.executeUpdate() <= 0) {
						connection.rollback();			
						return new CompasResponse(200, "Failed to add audit trail" + trans.txnIds);
				}
					logger.info("Audit Trail for settling transactions captured");
				}

			}
			connection.commit();
			return new CompasResponse(200, "Records Updated", fileName);

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Transaction> GetPendingAuth(int orgId) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			logger.info("Get Pending Auth transactions");
			// preparedStatement=connection.prepareStatement(Queryconstants.getPendingAuthTransaction);
			preparedStatement = connection.prepareStatement(Queryconstants.getPendingAuthTransactions);
			preparedStatement.setInt(1, orgId);
			resultSet = preparedStatement.executeQuery();
			List<Transaction> txns = new ArrayList<Transaction>();
			while (resultSet.next()) {
				Transaction obj = new Transaction();
				obj.txnId = resultSet.getInt("id");
				obj.accNo = resultSet.getString("acc_no");
				obj.memberNo = resultSet.getString("member_no");
				obj.cardNo = resultSet.getString("card_serial");
				obj.providerName = resultSet.getString("provider_name");
				obj.bnfDesc = resultSet.getString("benefit_desc");
				obj.amount = resultSet.getDouble("amount");
				obj.allowed = resultSet.getDouble("amount");
				obj.transDate = resultSet.getString("trans_date");
				obj.image = resultSet.getString("image");
				obj.count = count++;
				obj.reason = "";
				obj.isActive = false;
				obj.reject = false;

				preparedStatement1 = connection.prepareStatement(Queryconstants.getempName);
				preparedStatement1.setString(1, obj.memberNo);
				preparedStatement1.setString(2, obj.memberNo);
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {
					obj.empName = resultSet1.getString("name");
				}
				preparedStatement3 = connection.prepareStatement(Queryconstants.getTransactionDetails);
				preparedStatement3.setInt(1, resultSet.getInt("id"));
				resultSet3 = preparedStatement3.executeQuery();
				List<TransactionDtl> transactionDetails = new ArrayList<TransactionDtl>();
				while (resultSet3.next()) {
					TransactionDtl transactionDetail = new TransactionDtl();
					transactionDetail.id = resultSet3.getInt("id");
					transactionDetail.voucherName = resultSet3.getString("voucher_name");
					transactionDetail.serName = resultSet3.getString("ser_name");
					transactionDetail.txnAmount = resultSet3.getInt("txn_amount");
					transactionDetail.voucherId = resultSet3.getInt("voucher_id");
					transactionDetail.serviceId = resultSet3.getInt("ser_id");
					transactionDetails.add(transactionDetail);
				}
				obj.transactionDetails = transactionDetails;
				txns.add(obj);
			}
			return txns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdatePendingAuthStatus(TransactionDtl txn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement1 = connection.prepareStatement(Queryconstants.getAuthTransByBill);
			preparedStatement1.setInt(1, txn.transMstId);
			resultSet = preparedStatement1.executeQuery();
			while (resultSet.next()) {
				if (resultSet.getString("txn_status").equalsIgnoreCase("-2")) {
					preparedStatement = connection.prepareStatement(Queryconstants.updateAuthTrans);
					preparedStatement.setString(1, txn.transStatus);
					preparedStatement.setInt(2, txn.transAuthUser);
					preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setInt(4, txn.transDetailId);
				} else if (resultSet.getString("txn_status").equalsIgnoreCase("0")) {
					preparedStatement = connection.prepareStatement(Queryconstants.updateAuthTransMst);
					preparedStatement.setString(1, txn.transStatus);
					preparedStatement.setInt(2, txn.transAuthUser);
					preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setInt(4, txn.transMstId);
				}

			}

			/*
			 * if(preparedStatement.executeUpdate() > 0){
			 * DBOperations.DisposeSql(preparedStatement); preparedStatement = connection
			 * .prepareStatement(Queryconstants.updateAuthTrans);
			 * preparedStatement.setString(1, txn.transStatus); preparedStatement.setInt(2,
			 * txn.transAuthUser);
			 * 
			 * preparedStatement.setTimestamp(3, new java.sql.Timestamp( new
			 * java.util.Date().getTime()));
			 * 
			 * preparedStatement.setInt(4, txn.transDetailId); }
			 */

			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(200, "Records Updated")
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

	public CompasResponse UpdateAuthRejectStatus(TransactionDtl txn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection.prepareStatement(Queryconstants.updateRejectAuthTrans);
			preparedStatement.setString(1, txn.transStatus);
			preparedStatement.setInt(2, txn.transAuthUser);

			preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
			preparedStatement.setString(4, txn.transCancelReason);
			preparedStatement.setInt(5, txn.transDetailId);

			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(200, "Records Updated")
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

	@SuppressWarnings("resource")
	public AuthTransaction GetUpdAuthTrans(AuthTransaction trans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet2 = null;
		ResultSet rs = null;
		String strResp = "200";
		CompasResponse response = new CompasResponse();
		int transMstId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			List<TransactionMst> authTrans = new ArrayList<TransactionMst>();
			AuthTransaction auth = new AuthTransaction();
			for (int i = 0; i < trans.txnsBillNos.size(); i++) {
				TransactionMst objtrans = new TransactionMst();
				preparedStatement2 = connection.prepareStatement(Queryconstants.getAuthPendingService);

				preparedStatement2.setString(1, trans.txnsBillNos.get(i).billNo);
				resultSet2 = preparedStatement2.executeQuery();

				List<Service> services = new ArrayList<Service>();
				while (resultSet2.next()) {
					if (resultSet2.getString("txn_status").equalsIgnoreCase("-1")
							|| resultSet2.getString("txn_status").equalsIgnoreCase("0")) {
						Service objSer = new Service();
						objSer.serviceId = resultSet2.getInt("serviceid");
						System.out.println(resultSet2.getString("txn_status"));
						if (resultSet2.getString("txn_status").equalsIgnoreCase("-1")) {
							objSer.status = "-1";
						} else {
							objSer.status = (resultSet2.getString("txn_status") == "0") ? "0" : "1";
						}
						// strResp = "200";
						services.add(objSer);
						objtrans.billNo = trans.txnsBillNos.get(i).billNo;
						objtrans.services = services;
					} else if (resultSet2.getString("txn_status").equalsIgnoreCase("-2")) {

					}

				}
				if (services.size() > 0) {
					authTrans.add(objtrans);
				}
				/*
				 * if (services.size() > 0) { strResp = "200"; } else { strResp = "300"; }
				 */

			}

			auth.txnsBillNos = authTrans;
			auth.respCode = strResp;
			// if (strResp == "200") {

			// } else {
			// auth.txnsBillNos = null;
			// auth.respCode = strResp;
			// }

			return auth;

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(preparedStatement1);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	public List<TransactionMst> GetPendingAuthByProvider(int providerId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			logger.info("Get Pending Auth transactions");
			preparedStatement = connection.prepareStatement(Queryconstants.getPendingAuthTrans);
			resultSet = preparedStatement.executeQuery();
			List<TransactionMst> msttxns = new ArrayList<TransactionMst>();
			while (resultSet.next()) {
				TransactionMst objTxn = new TransactionMst();
				objTxn.memberNo = resultSet.getString("member_no");
				objTxn.deviceId = resultSet.getString("device_id");
				objTxn.merName = resultSet.getString("mer_name");
				objTxn.transMstId = resultSet.getInt("trans_mst_id");
				objTxn.transDetailId = resultSet.getInt("id");
				objTxn.serName = resultSet.getString("ser_name");
				System.out.println(objTxn.transDetailId);
				objTxn.txnAmount = resultSet.getDouble("txn_amount");
				preparedStatement1 = connection.prepareStatement(Queryconstants.getPendingAuthTransListByBill);
				preparedStatement1.setInt(1, objTxn.transMstId);

				resultSet1 = preparedStatement1.executeQuery();
				List<TransactionDtl> txns = new ArrayList<TransactionDtl>();
				while (resultSet1.next()) {
					TransactionDtl obj = new TransactionDtl();
					// obj.transMstId = resultSet1.getInt("trans_mst_id");
					obj.transDetailId = resultSet1.getInt("id");
					obj.serName = resultSet1.getString("ser_name");
					obj.txnAmount = resultSet1.getDouble("txn_amount");
					txns.add(obj);
				}
				objTxn.serviceDtl = txns;
				msttxns.add(objTxn);
			}

			return msttxns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetAllTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllTransactions);
//				preparedStatement.setInt(1, rpt.orgId);
//				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				//report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("service_name");
			//	report.status = resultSet.getString("trans_status");
			//	report.cardNo = resultSet.getString("card_no");
			//	report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("member_name");
				report.serviceAmount = resultSet.getDouble("amount");
				report.productName = resultSet.getString("scheme_name");
				report.merchantName = resultSet.getString("provider_name");
			//	report.productId = resultSet.getInt("productId");
			//	report.merchantId = resultSet.getInt("merchantId");
				report.invoiceNo = resultSet.getString("invoice_number");
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

	public List<Reports> GetOutstandingTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getOutstandingTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getOutstandingTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getOutstandingTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getOutstandingTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.merchantId = resultSet.getInt("merchantId");
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

	public List<Reports> GetPendingTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getPendingTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getPendingTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getPendingTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getPendingTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.merchantId = resultSet.getInt("merchantId");
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

	public List<Reports> GetUnpaidTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getUnpaidTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getUnpaidTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getUnpaidTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getUnpaidTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.merchantId = resultSet.getInt("merchantId");
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

	public List<Reports> GetSettledTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSettledTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSettledTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSettledTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSettledTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.merchantId = resultSet.getInt("merchantId");
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

	public List<Reports> GetRejectedTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getRejectedTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getRejectedTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getRejectedTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getRejectedTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.merchantId = resultSet.getInt("merchantId");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetMemberTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getMemberTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.memberNo);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.memberNo);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getMemberTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.memberNo);
				preparedStatement.setString(3, rpt.fromDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.memberNo);
				preparedStatement.setString(6, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getMemberTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.memberNo);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.memberNo);
				preparedStatement.setString(6, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getMemberTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.memberNo);
				preparedStatement.setString(3, rpt.fromDate);
				preparedStatement.setString(4, rpt.toDate);
				preparedStatement.setInt(5, rpt.orgId);
				preparedStatement.setString(6, rpt.memberNo);
				preparedStatement.setString(7, rpt.fromDate);
				preparedStatement.setString(8, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.memberBalance=resultSet.getDouble("service_balance");
				report.benefitBalance=resultSet.getDouble("basket_amount");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetCancelledTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getCancelledTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getCancelledTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getCancelledTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getCancelledTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetAuthorizedTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAuthorizedTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAuthorizedTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAuthorizedTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAuthorizedTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetProviderWiseTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getProviderWiseTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.merchantId);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setInt(4, rpt.merchantId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getProviderWiseTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.merchantId);
				preparedStatement.setString(3, rpt.fromDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setInt(5, rpt.merchantId);
				preparedStatement.setString(6, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getProviderWiseTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.merchantId);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setInt(5, rpt.merchantId);
				preparedStatement.setString(6, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getProviderWiseTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.merchantId);
				preparedStatement.setString(3, rpt.fromDate);
				preparedStatement.setString(4, rpt.toDate);
				preparedStatement.setInt(5, rpt.orgId);
				preparedStatement.setInt(6, rpt.merchantId);
				preparedStatement.setString(7, rpt.fromDate);
				preparedStatement.setString(8, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetSchemeWiseTxnsDetails(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSchemeWiseTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.productId);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setInt(4, rpt.productId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSchemeWiseTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.productId);
				preparedStatement.setString(3, rpt.fromDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setInt(5, rpt.productId);
				preparedStatement.setString(6, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSchemeWiseTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.productId);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setInt(5, rpt.productId);
				preparedStatement.setString(6, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getSchemeWiseTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.productId);
				preparedStatement.setString(3, rpt.fromDate);
				preparedStatement.setString(4, rpt.toDate);
				preparedStatement.setInt(5, rpt.orgId);
				preparedStatement.setInt(6, rpt.productId);
				preparedStatement.setString(7, rpt.fromDate);
				preparedStatement.setString(8, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetProviderTrans(Reports rpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (Integer.valueOf(rpt.status) == 4) {
				preparedStatement = connection.prepareStatement(Queryconstants.getProviderTransactions);
			} else {
				System.out.println("Status will be:" + rpt.status);
				preparedStatement = connection.prepareStatement(Queryconstants.getSortedProviderTransactions);
				preparedStatement.setString(5, rpt.status);
			}
			preparedStatement.setInt(1, rpt.orgId);
			preparedStatement.setInt(2, rpt.userId);
			preparedStatement.setString(3, rpt.fromDate);
			preparedStatement.setString(4, rpt.toDate);
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse AuthorizeTransaction(Transaction trans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (trans.txnStatus == 0) {
				preparedStatement = connection.prepareStatement(Queryconstants.updAuthMemberTransDtl);
				preparedStatement.setInt(1, trans.txnStatus);
				preparedStatement.setDouble(2, trans.allowed);
				preparedStatement.setString(3, trans.reason);
				preparedStatement.setInt(4, trans.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(6, trans.txnId);
				if (preparedStatement.executeUpdate() > 0) {
					if (trans.diff > 0) {
						preparedStatement1 = connection.prepareStatement(Queryconstants.getBalances);
						preparedStatement1.setString(1, trans.accNo);
						preparedStatement1.setString(2, trans.cardNo);
						preparedStatement1.setInt(3, trans.transactionDetails.get(0).voucherId);
						ResultSet rst = preparedStatement1.executeQuery();
						while (rst.next()) {
							preparedStatement2 = connection.prepareStatement(Queryconstants.updateBalances);
							preparedStatement2.setDouble(1, rst.getDouble("service_balance") + trans.diff);
							preparedStatement2.setDouble(2, rst.getDouble("basket_balance") + trans.diff);
							preparedStatement2.setInt(3, rst.getInt("id"));

							if (preparedStatement2.executeUpdate() <= 0) {
								connection.rollback();
								return new CompasResponse(201,
										"Failed to Update Member balance for transaction id " + trans.txnId);
							}
						}
					}
				} else {
					connection.rollback();
					return new CompasResponse(201, "Failed to Update Transaction Id " + trans.txnId);
				}
			} else if (trans.txnStatus == -3) {
				preparedStatement = connection.prepareStatement(Queryconstants.updRejectedTransDtl);
				preparedStatement.setInt(1, -3);
				preparedStatement.setInt(2, -113);
				preparedStatement.setInt(3, trans.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(5, trans.reason);
				preparedStatement.setInt(6, trans.txnId);

				if (preparedStatement.executeUpdate() > 0) {
					preparedStatement1 = connection.prepareStatement(Queryconstants.getBalances);
					preparedStatement1.setString(1, trans.accNo);
					preparedStatement1.setString(2, trans.cardNo);
					preparedStatement1.setInt(3, trans.transactionDetails.get(0).voucherId);
					ResultSet rst = preparedStatement1.executeQuery();
					while (rst.next()) {
						preparedStatement2 = connection.prepareStatement(Queryconstants.updateBalances);
						preparedStatement2.setDouble(1, rst.getDouble("service_balance") + trans.amount);
						preparedStatement2.setDouble(2, rst.getDouble("basket_balance") + trans.amount);
						preparedStatement2.setInt(3, rst.getInt("id"));

						if (preparedStatement2.executeUpdate() <= 0) {
							connection.rollback();
							return new CompasResponse(201,
									"Failed to Update Member balance for transaction id " + trans.txnId);
						}
					}
				} else {
					connection.rollback();
					return new CompasResponse(201, "Failed to Update Transaction Id " + trans.txnId);
				}
			}

			connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// update AKUH Transactions
	public CompasResponse updateAkuhTransStatus(Claim claimtrans) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (claimtrans.txnStatus == 1) {
				preparedStatement = connection.prepareStatement(Queryconstants.updMemberTransClaimDtlStatus);
				System.out.println("Allowed verification amount:: "+claimtrans.allowed);
				System.out.println("Claim transaction Amount:: "+claimtrans.amount);
				if(claimtrans.allowed<claimtrans.amount) {
					claimtrans.paidStatus=115;
					claimtrans.txnStatus=4;
					claimtrans.amount=claimtrans.amount-claimtrans.allowed;
					logger.info("Paid status:: "+claimtrans.paidStatus);
					logger.info("Remaining amount after verifying claim:: "+claimtrans.amount);
				}
				preparedStatement.setDouble(1, claimtrans.amount);
				preparedStatement.setInt(2, claimtrans.paidStatus);
				preparedStatement.setDouble(3, claimtrans.allowed);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(5, claimtrans.txnStatus);
				preparedStatement.setLong(6, claimtrans.id);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(200, "Failed to Update Claim Id " + claimtrans.id);
				}
				//insert audit trail
				preparedStatement3 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement3.setInt(1, claimtrans.createdBy);
				preparedStatement3.setInt(2, claimtrans.createdBy);
				preparedStatement3.setString(3, "Vetted Claim Transaction id: " +claimtrans.id);
				preparedStatement3.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement3.setString(5, "AKUH Bill Vetting Module");
				if (preparedStatement3.executeUpdate() <= 0) {
					connection.rollback();			
					return new CompasResponse(200, "Failed to add audit trail" + claimtrans.id);
			}
				if (claimtrans.diff > 0) {
					System.out.println(claimtrans.diff);
					System.out.println(claimtrans.BillNo);
					System.out.println(claimtrans.serviceId);
					System.out.println(claimtrans.voucherId);
					System.out.println(claimtrans.programmeId);
					preparedStatement1 = connection.prepareStatement(Queryconstants.updateVerifiedBasketBalance);
					preparedStatement1.setDouble(1, claimtrans.diff);
					preparedStatement1.setString(2, claimtrans.accNo);
					preparedStatement1.setInt(3, claimtrans.voucherId);
					preparedStatement1.setInt(4, claimtrans.programmeId);
					preparedStatement1.executeUpdate();
					preparedStatement2 = connection.prepareStatement(Queryconstants.updateVerifiedServiceBalance);
					preparedStatement2.setDouble(1, claimtrans.diff);
					preparedStatement2.setString(2, claimtrans.accNo);
					preparedStatement2.setInt(3, claimtrans.serviceId);
					preparedStatement2.setInt(4, claimtrans.voucherId);
					preparedStatement2.setInt(5, claimtrans.programmeId);
					preparedStatement2.executeUpdate();

				}

			} else if (claimtrans.txnStatus == -1) {
				preparedStatement = connection.prepareStatement(Queryconstants.updCancelledClaimTransDtl);
				preparedStatement.setInt(1, -1);
				preparedStatement.setInt(2, -111);
				preparedStatement.setInt(3, claimtrans.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(5, claimtrans.reason);
				preparedStatement.setLong(6, claimtrans.id);

				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Update Claim Transaction Id " + claimtrans.id);
				}
				//insert audit trail
				preparedStatement3 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement3.setInt(1, claimtrans.createdBy);
				preparedStatement3.setInt(2, claimtrans.createdBy);
				preparedStatement3.setString(3, "Declined to Vet Claim Transaction id: " +claimtrans.id);
				preparedStatement3.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement3.setString(5, "AKUH Bill Vetting Module");
				if (preparedStatement3.executeUpdate() <= 0) {
					connection.rollback();			
					return new CompasResponse(200, "Failed to add audit trail" + claimtrans.id);
			}
			} else if (claimtrans.txnStatus == 2) {
				preparedStatement = connection.prepareStatement(Queryconstants.updClaimApprovedMemberTransDtl);
				System.out.println("Allowed amount:: "+claimtrans.allowed);
				System.out.println("Claim transaction Amount:: "+claimtrans.amount);
				if(claimtrans.allowed<claimtrans.amount) {
					claimtrans.paidStatus=114;
					claimtrans.txnStatus=5;
					claimtrans.amount=claimtrans.amount-claimtrans.allowed;
					logger.info("Paid claim status:: "+claimtrans.paidStatus);
					logger.info("Remaining amount after approving claim:: "+claimtrans.amount);
				}
				preparedStatement.setDouble(1, claimtrans.amount);
				preparedStatement.setInt(2, claimtrans.paidStatus);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(4, claimtrans.txnStatus);
				preparedStatement.setDouble(5, claimtrans.allowed);
				preparedStatement.setLong(6, claimtrans.id);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Update Transaction Id " + claimtrans.id);
				}
				//insert audit trail
				preparedStatement3 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement3.setInt(1, claimtrans.createdBy);
				preparedStatement3.setInt(2, claimtrans.createdBy);
				preparedStatement3.setString(3, "Approved Claim Transaction id: " +claimtrans.id);
				preparedStatement3.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement3.setString(5, "AKUH Bill Approval Module");
				if (preparedStatement3.executeUpdate() <= 0) {
					connection.rollback();			
					return new CompasResponse(200, "Failed to add audit trail" + claimtrans.id);
			}
			} else if (claimtrans.txnStatus == 0) {
				// changed here...
				preparedStatement = connection.prepareStatement(Queryconstants.updMemberTransClaimDtlStatus);
				preparedStatement.setInt(1, claimtrans.txnStatus);
				preparedStatement.setDouble(2, claimtrans.allowed);
				preparedStatement.setInt(3, claimtrans.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setLong(5, claimtrans.id);
				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					throw new Exception("Failed to Update Transaction Claim Id " + claimtrans.id);
				}
			}
			connection.commit();
			return new CompasResponse(200, "Records Updated");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// get approved claims
	public List<Claim> GetApprovedClaimTrans(Claim claimtxn) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();

			logger.info("Get Approved Claims to settle");
			// connection.prepareStatement(Queryconstants.getApprovedTransactionList);
			preparedStatement = connection.prepareStatement(Queryconstants.getApprovedClaims);
			preparedStatement.setString(1, claimtxn.fromDt);
			preparedStatement.setString(2, claimtxn.toDt);
			//preparedStatement.setString(3, claimtxn.providerId);
			// preparedStatement.setString(4, claimtxn.orgId);
			resultSet = preparedStatement.executeQuery();
			List<Claim> claimtxns = new ArrayList<Claim>();
			while (resultSet.next()) {
				Claim obj = new Claim();
				obj.providerCode = resultSet.getString("mer_code");
				obj.providerName = resultSet.getString("mer_name");
				obj.bankCode = resultSet.getString("bank_code");
				obj.bankName = resultSet.getString("bank_name");
				obj.amount = resultSet.getDouble("amount");
				obj.txnIds = resultSet.getString("txnid");
				obj.count = count++;
				obj.isActive = false;
				List<ClaimRemitance> claimdtls = new ArrayList<ClaimRemitance>();
				List<String> ids = Arrays.asList(obj.txnIds.split("\\s*,\\s*"));
				for (int i = 0; i < ids.size(); i++) {
					// preparedStatement1 =
					// connection.prepareStatement(Queryconstants.getApprovedTransactionDtl);
					preparedStatement1 = connection.prepareStatement(Queryconstants.getApprovedClaimTransactionDtl);
					preparedStatement1.setString(1, ids.get(i));
					resultSet1 = preparedStatement1.executeQuery();
					while (resultSet1.next()) {
						ClaimRemitance claimdtl = new ClaimRemitance();
						claimdtl.txnId = Integer.valueOf(ids.get(i));
						claimdtl.BillNo = resultSet1.getInt("BillNo");
						claimdtl.membershipNo = resultSet1.getString("membershipNo");
						claimdtl.patientName = resultSet1.getString("patientName");
						claimdtl.chargeDate = resultSet1.getString("chargeDate");
						claimdtl.amount = resultSet1.getDouble("amount");
						claimdtl.txn_amount = resultSet1.getDouble("sponsorAmount");
						claimdtl.approvedOn = resultSet1.getString("approved_on");
						claimdtls.add(claimdtl);
					}
				}
				obj.claimdetails = claimdtls;
				claimtxns.add(obj);
			}
			return claimtxns;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// AKUH settlement file
	public CompasResponse GenerateClaimSettlementFile(Claim claimtrans, String filePath) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();
			dateFormat.format(cal.getTime());

			// String filePath
			// =System.getProperty("catalina.base")+"/Settlement/"+"JDC_"+dateFormat.format(cal.getTime());
			String tst = claimtrans.selectedTrans.get(0).providerCode + dateFormat.format(cal.getTime()) + ".xls";
			System.out.println("This is the selected claim transaction: " + claimtrans.toString());
			String strfilePath = filePath + tst;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("FirstSheet");

			HSSFCellStyle my_style = workbook.createCellStyle();
			HSSFCellStyle style = workbook.createCellStyle();
			HSSFCellStyle style1 = workbook.createCellStyle();
			HSSFFont my_font = workbook.createFont();
			my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			my_style.setFont(my_font);
			style1.setFont(my_font);
			style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Bill number");
			rowhead.createCell(1).setCellValue("Transaction date");
			rowhead.createCell(2).setCellValue("Member number");
			rowhead.createCell(3).setCellValue("Member name");
			rowhead.createCell(4).setCellValue("Service provider");
			rowhead.createCell(5).setCellValue("Amount requested");
			rowhead.createCell(6).setCellValue("Amount paid");
			rowhead.createCell(7).setCellValue("Transfer date");
			for (int i = 0; i < rowhead.getLastCellNum(); i++) {// For each cell in the row
				rowhead.getCell(i).setCellStyle(my_style);// Set the style
			}
			int count = 0;
			double txn_amount = 0;
			double amount = 0;
			for (int i = 0; i < claimtrans.selectedTrans.size(); i++) {
				for (int j = 0; j < claimtrans.selectedTrans.get(i).claimdetails.size(); j++) {
					count++;
					HSSFRow row = sheet.createRow((short) count);
					row.createCell(0).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).BillNo);
					row.createCell(1).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).chargeDate);
					row.createCell(2).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).membershipNo);
					row.createCell(3).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).patientName);
					row.createCell(4).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).providerName);
					row.createCell(7).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).txn_amount);
					row.createCell(8).setCellValue(claimtrans.selectedTrans.get(i).claimdetails.get(j).amount);
					row.createCell(9).setCellValue(claimtrans.transferDate);
					row.getCell(7).setCellStyle(style);
					row.getCell(8).setCellStyle(style);
					txn_amount += claimtrans.selectedTrans.get(i).claimdetails.get(j).txn_amount;
					amount += claimtrans.selectedTrans.get(i).claimdetails.get(j).amount;

				}

			}
			HSSFRow row1 = sheet.createRow((short) count + 1);
			row1.createCell(7).setCellValue(txn_amount);
			row1.createCell(8).setCellValue(amount);
			row1.getCell(7).setCellStyle(style1);
			row1.getCell(8).setCellStyle(style1);

			FileOutputStream fileOut = new FileOutputStream(strfilePath);
			workbook.write(fileOut);
			fileOut.close();
			logger.info("Your excel claim remittance file has been generated!");
			return new CompasResponse(200, "Records Updated", tst);

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Failed to generate file");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateClaimSettlementStatus(Claim claimtrans, String fileName) {
		// TODO Auto-generated method stub;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(Queryconstants.updClaimSettementFileStatus);
			for (int i = 0; i < claimtrans.selectedTrans.size(); i++) {
				List<String> items = Arrays.asList(claimtrans.selectedTrans.get(i).txnIds.split("\\s*,\\s*"));
				for (int j = 0; j < items.size(); j++) {
					preparedStatement.setInt(1, 3);
					preparedStatement.setInt(2, 113);
					preparedStatement.setInt(3, claimtrans.createdBy);
					preparedStatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement.setString(5, fileName);
					preparedStatement.setInt(6, Integer.valueOf(items.get(j)));

					if (preparedStatement.executeUpdate() <= 0) {
						connection.rollback();
						throw new Exception("Failed to Update Claim Transaction Id " + claimtrans.selectedTrans.get(i).txnId);
					}
					
					//insert audit trail
					preparedStatement1 = connection.prepareStatement(Queryconstants.insertAuditTrail);
					preparedStatement1.setInt(1, claimtrans.createdBy);
					preparedStatement1.setInt(2, claimtrans.createdBy);
					preparedStatement1.setString(3, "Settled Claim Transaction id: " +claimtrans.txnIds);
					preparedStatement1.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
					preparedStatement1.setString(5, "AKUH Bill Settlement Module");
					if (preparedStatement1.executeUpdate() <= 0) {
						connection.rollback();			
						return new CompasResponse(200, "Failed to add audit trail" +claimtrans.txnIds);
				}
				}

			}
			connection.commit();
			return new CompasResponse(200, "Records Updated", fileName);

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Database Exception Occured");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	// AKUH Details
	public List<ClaimReports> GetAllAkuhTxnsDetails(ClaimReports akuhrpt) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (akuhrpt.fromDate == null && akuhrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClaimTransactions);
				//preparedStatement.setInt(1, akuhrpt.orgId);
				//preparedStatement.setInt(2, akuhrpt.orgId);
			}
			if (akuhrpt.fromDate != null && akuhrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClaimTransactionsBetween);
				preparedStatement.setString(1, akuhrpt.fromDate);
				preparedStatement.setString(2, akuhrpt.toDate);
			}
			if (akuhrpt.fromDate != null && akuhrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClaimTransactionsAbove);
				preparedStatement.setString(1, akuhrpt.fromDate);
				preparedStatement.setString(2, akuhrpt.fromDate);
			}
			if (akuhrpt.fromDate == null && akuhrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClaimTransactionsBelow);
				preparedStatement.setString(1, akuhrpt.toDate);
				preparedStatement.setString(2, akuhrpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<ClaimReports> claimreports = new ArrayList<ClaimReports>();
			while (resultSet.next()) {
				ClaimReports claimreport = new ClaimReports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				claimreport.membershipNo = resultSet.getString("membershipNo");
				// report.totalAmount = resultSet.getString("");
				claimreport.BillNo = resultSet.getString("BillNo");
				claimreport.chargeDate = resultSet.getString("chargeDate");
				claimreport.serviceName = resultSet.getString("service_name");
				claimreport.txnStatus = resultSet.getString("trans_status");
				claimreport.cardNo = resultSet.getString("card_no");
				claimreport.patientName = resultSet.getString("NAME");
				claimreport.ClaimserviceAmount = resultSet.getDouble("service_amount");
				claimreport.schemeName = resultSet.getString("schemeName");
				claimreport.merchantName = resultSet.getString("providerName");
				//report.productId = resultSet.getInt("productId");
				//report.merchantId = resultSet.getInt("merchantId");
				claimreports.add(claimreport);
				counter++;
			}
			return claimreports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetAllGtTxnsDetails(Reports gtrpt) {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (gtrpt.fromDate == null && gtrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllGertrudeTransactions);
				preparedStatement.setInt(1, gtrpt.orgId);
			}
			if (gtrpt.fromDate != null && gtrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllGertrudeTransactionsBetween);
				preparedStatement.setInt(1, gtrpt.orgId);
				preparedStatement.setString(2, gtrpt.fromDate);
				preparedStatement.setString(3, gtrpt.toDate);
			}
			if (gtrpt.fromDate != null && gtrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllGertrudeTransactionsAbove);
				preparedStatement.setInt(1, gtrpt.orgId);
				preparedStatement.setString(2, gtrpt.fromDate);
				preparedStatement.setString(3, gtrpt.fromDate);
			}
			if (gtrpt.fromDate == null && gtrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllGertrudeTransactionsBelow);
				preparedStatement.setInt(1, gtrpt.orgId);
				preparedStatement.setString(2, gtrpt.toDate);
				preparedStatement.setString(3, gtrpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.orgName=resultSet.getString("org_name");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.invoiceNo = resultSet.getString("invoice_number");
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

	public List<Reports> GetAllMpshahTxnsDetails(Reports mpsharpt) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (mpsharpt.fromDate == null && mpsharpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllMpshahTransactions);
				preparedStatement.setInt(1, mpsharpt.orgId);
			}
			if (mpsharpt.fromDate != null && mpsharpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllMpshahTransactionsBetween);
				preparedStatement.setInt(1, mpsharpt.orgId);
				preparedStatement.setString(2, mpsharpt.fromDate);
				preparedStatement.setString(3, mpsharpt.toDate);
			}
			if (mpsharpt.fromDate != null && mpsharpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllMpshahTransactionsBetween);
				preparedStatement.setInt(1, mpsharpt.orgId);
				preparedStatement.setString(2, mpsharpt.fromDate);
				preparedStatement.setString(3, mpsharpt.fromDate);
			}
			if (mpsharpt.fromDate == null && mpsharpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllMpshahTransactionsBetween);
				preparedStatement.setInt(1, mpsharpt.orgId);
				preparedStatement.setString(2, mpsharpt.toDate);
				preparedStatement.setString(3, mpsharpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.orgName=resultSet.getString("org_name");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				//report.merchantId = resultSet.getInt("merchantId");
				report.invoiceNo = resultSet.getString("invoice_number");
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

	public List<Reports> GetAllNbiTxnsDetails(Reports nbrpt) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (nbrpt.fromDate == null && nbrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllNboTransactions);
				preparedStatement.setInt(1, nbrpt.orgId);
			}
			if (nbrpt.fromDate != null && nbrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllNboTransactionsBetween);
				preparedStatement.setInt(1, nbrpt.orgId);
				preparedStatement.setString(2, nbrpt.fromDate);
				preparedStatement.setString(3, nbrpt.toDate);
			}
			if (nbrpt.fromDate != null && nbrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllNboTransactionsAbove);
				preparedStatement.setInt(1, nbrpt.orgId);
				preparedStatement.setString(2, nbrpt.fromDate);
				preparedStatement.setString(3, nbrpt.fromDate);
			}
			if (nbrpt.fromDate == null && nbrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllNboTransactionsBelow);
				preparedStatement.setInt(1, nbrpt.orgId);
				preparedStatement.setString(2, nbrpt.toDate);
				preparedStatement.setString(3, nbrpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.orgName=resultSet.getString("org_name");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				//report.merchantId = resultSet.getInt("merchantId");
				report.invoiceNo = resultSet.getString("invoice_number");
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

	public List<Reports> GetAllDetailedUHMCHospTxn(Reports uhmcrpt) {
		// TODO Auto-generated method stub
				DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
				Connection connection = null;
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				int counter = 1;
				try {
					connection = dataSource.getConnection();
					if (uhmcrpt.fromDate == null && uhmcrpt.toDate == null) {
						preparedStatement = connection.prepareStatement(Queryconstants.getAllUHMCTransactions);
						preparedStatement.setInt(1, uhmcrpt.orgId);
					}
					if (uhmcrpt.fromDate != null && uhmcrpt.toDate != null) {
						preparedStatement = connection.prepareStatement(Queryconstants.getAllUHMCTransactionsBetween);
						preparedStatement.setInt(1, uhmcrpt.orgId);
						preparedStatement.setString(2, uhmcrpt.fromDate);
						preparedStatement.setString(3, uhmcrpt.toDate);
					}
					if (uhmcrpt.fromDate != null && uhmcrpt.toDate == null) {
						preparedStatement = connection.prepareStatement(Queryconstants.getAllUHMCTransactionsAbove);
						preparedStatement.setInt(1, uhmcrpt.orgId);
						preparedStatement.setString(2, uhmcrpt.fromDate);
						preparedStatement.setString(3, uhmcrpt.fromDate);
					}
					if (uhmcrpt.fromDate == null && uhmcrpt.toDate != null) {
						preparedStatement = connection.prepareStatement(Queryconstants.getAllUHMCTransactionsBelow);
						preparedStatement.setInt(1, uhmcrpt.orgId);
						preparedStatement.setString(2, uhmcrpt.toDate);
						preparedStatement.setString(3, uhmcrpt.toDate);
					}

					resultSet = preparedStatement.executeQuery();
					List<Reports> reports = new ArrayList<Reports>();
					while (resultSet.next()) {
						Reports report = new Reports();
						// report.fromDate = resultSet.getString("");
						// report.toDate = resultSet.getString("");
						report.memberNo = resultSet.getString("member_no");
						// report.totalAmount = resultSet.getString("");
						report.billNo = resultSet.getString("bill_no");
						report.txnDate = resultSet.getString("trans_date");
						report.serviceName = resultSet.getString("ser_name");
						report.status = resultSet.getString("trans_status");
						report.cardNo = resultSet.getString("card_no");
						report.orgName=resultSet.getString("org_name");
						report.name = resultSet.getString("NAME");
						report.serviceAmount = resultSet.getDouble("service_amount");
						report.productName = resultSet.getString("product_name");
						report.merchantName = resultSet.getString("mer_name");
						report.productId = resultSet.getInt("productId");
						//report.merchantId = resultSet.getInt("merchantId");
						report.invoiceNo = resultSet.getString("invoice_number");
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

	public List<Reports> GetAllDetailedClinixTxn(Reports clinixrpt) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (clinixrpt.fromDate == null && clinixrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClinixCTransactions);
				preparedStatement.setInt(1, clinixrpt.orgId);
			}
			if (clinixrpt.fromDate != null && clinixrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClinixTransactionsBetween);
				preparedStatement.setInt(1, clinixrpt.orgId);
				preparedStatement.setString(2, clinixrpt.fromDate);
				preparedStatement.setString(3, clinixrpt.toDate);
			}
			if (clinixrpt.fromDate != null && clinixrpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClinixTransactionsAbove);
				preparedStatement.setInt(1, clinixrpt.orgId);
				preparedStatement.setString(2, clinixrpt.fromDate);
				preparedStatement.setString(3, clinixrpt.fromDate);
			}
			if (clinixrpt.fromDate == null && clinixrpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllClinixTransactionsBelow);
				preparedStatement.setInt(1, clinixrpt.orgId);
				preparedStatement.setString(2, clinixrpt.toDate);
				preparedStatement.setString(3, clinixrpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.orgName=resultSet.getString("org_name");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				//report.merchantId = resultSet.getInt("merchantId");
				report.invoiceNo = resultSet.getString("invoice_number");
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

	public List<Reports> GetAllServiceProvidersTxnsDetails(Reports sprpt) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (sprpt.fromDate == null && sprpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllSpTransactions);
				System.out.println("This is the quuery" +preparedStatement.toString());
				preparedStatement.setInt(1, sprpt.orgId);
				preparedStatement.setString(2, '%' + sprpt.merchantName + '%');
				preparedStatement.setInt(3, sprpt.orgId);
				preparedStatement.setString(4, '%'+ sprpt.merchantName + '%');
				System.out.println("This is the quuery after parameters" +preparedStatement.toString());
				System.out.println("Service provider parameter is: " +sprpt.merchantName);
			}
			if (sprpt.fromDate != null && sprpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllSpTransactionsBetween);
				preparedStatement.setInt(1, sprpt.orgId);
				preparedStatement.setString(2, '%' +sprpt.merchantName+ '%');
				preparedStatement.setString(3, sprpt.fromDate);
				preparedStatement.setString(4, sprpt.toDate);
				preparedStatement.setInt(5, sprpt.orgId);
				preparedStatement.setString(6, '%'+ sprpt.merchantName + '%');
				preparedStatement.setString(7, sprpt.fromDate);
				preparedStatement.setString(8, sprpt.toDate);
			}
			if (sprpt.fromDate != null && sprpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllSpTransactionsAbove);
				preparedStatement.setInt(1, sprpt.orgId);
				preparedStatement.setString(2, '%'+ sprpt.merchantName + '%');
				preparedStatement.setString(3, sprpt.fromDate);
				preparedStatement.setInt(4, sprpt.orgId);
				preparedStatement.setString(5, '%'+ sprpt.merchantName + '%');
				preparedStatement.setString(6, sprpt.fromDate);
			}
			if (sprpt.fromDate == null && sprpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getAllSpTransactionsBelow);
				preparedStatement.setInt(1, sprpt.orgId);
				preparedStatement.setString(2, '%'+ sprpt.merchantName + '%');
				preparedStatement.setString(3, sprpt.toDate);
				preparedStatement.setInt(4, sprpt.orgId);
				preparedStatement.setString(5, '%'+ sprpt.merchantName + '%');
				preparedStatement.setString(5, sprpt.toDate);
			}

			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				report.productId = resultSet.getInt("productId");
				report.merchantId = resultSet.getInt("merchantId");
				report.invoiceNo = resultSet.getString("invoice_number");
				System.out.println("SP TRANSACTION INVOICE NUMBER :" +report.invoiceNo);
				reports.add(report);
				counter++;
				System.out.println("Merchant Report Data:" +reports.toString());
			}
			return reports;	
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	public List<Reports> GetAllDetailedMemberUtilizations() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getMemberUtilizations);
			resultSet = preparedStatement.executeQuery();
			List<Reports> utilizations = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports obj = new Reports();
				obj.name = resultSet.getString("emp_fullname");
				obj.memberNo = resultSet.getString("emp_ref_key");
				obj.productName = resultSet.getString("product_name");
				obj.serviceName = resultSet.getString("ser_name");
				obj.basketValue = resultSet.getDouble("basket_value");
				obj.basketBalance = resultSet.getDouble("basket_balance");
				obj.utilization = resultSet.getDouble("utilization");
				obj.count = count++;
				utilizations.add(obj);
			}
			return utilizations;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetAllDetailedTotalUtilizations() {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getTotalUtilizations);
			resultSet = preparedStatement.executeQuery();
			List<Reports> totalUtilizations = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports obj = new Reports();
				obj.productName = resultSet.getString("product_name");
				obj.basketValue = resultSet.getDouble("basket_value");
				obj.utilization = resultSet.getDouble("utilization");
				System.out.println("Total utilization :" +obj.utilization);
				obj.count = count++;
				totalUtilizations.add(obj);
			}
			return totalUtilizations;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Reports> GetJDCProviderWiseTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			if (rpt.fromDate == null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getJDCProviderWiseTransactions);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setInt(2, rpt.orgId);
			}
			if (rpt.fromDate != null && rpt.toDate == null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getJDCProviderWiseTransactionsAbove);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.fromDate);
			}
			if (rpt.fromDate == null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getJDCProviderWiseTransactionsBelow);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.toDate);
				preparedStatement.setInt(3, rpt.orgId);
				preparedStatement.setString(4, rpt.toDate);
			}
			if (rpt.fromDate != null && rpt.toDate != null) {
				preparedStatement = connection.prepareStatement(Queryconstants.getJDCProviderWiseTransactionsBetween);
				preparedStatement.setInt(1, rpt.orgId);
				preparedStatement.setString(2, rpt.fromDate);
				preparedStatement.setString(3, rpt.toDate);
				preparedStatement.setInt(4, rpt.orgId);
				preparedStatement.setString(5, rpt.fromDate);
				preparedStatement.setString(6, rpt.toDate);
			}
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				// report.fromDate = resultSet.getString("");
				// report.toDate = resultSet.getString("");
				report.memberNo = resultSet.getString("member_no");
				// report.totalAmount = resultSet.getString("");
				report.billNo = resultSet.getString("bill_no");
				report.txnDate = resultSet.getString("trans_date");
				report.serviceName = resultSet.getString("ser_name");
				report.status = resultSet.getString("trans_status");
				report.cardNo = resultSet.getString("card_no");
				report.accNo = resultSet.getString("acc_no");
				report.name = resultSet.getString("NAME");
				report.serviceAmount = resultSet.getDouble("service_amount");
				report.productName = resultSet.getString("product_name");
				report.merchantName = resultSet.getString("mer_name");
				reports.add(report);
			}
			return reports;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	public List<Reports> GetAllDetailedMemberReport(){
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getAllJudiciaryMembers);
//			preparedStatement.setString(1, sprpt.productName);
			resultSet = preparedStatement.executeQuery();
			List<Reports> reports = new ArrayList<Reports>();
			while (resultSet.next()) {
				Reports report = new Reports();
				report.memberNo = resultSet.getString("emp_ref_key");
				report.name = resultSet.getString("emp_fullname");
				report.dateofBirth = resultSet.getString("emp_dob");
				report.status = resultSet.getString("active");
				report.memberType = resultSet.getString("member_type");
				report.productName = resultSet.getString("product_name");
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

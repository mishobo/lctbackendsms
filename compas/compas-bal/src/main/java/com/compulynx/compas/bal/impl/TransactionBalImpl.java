package com.compulynx.compas.bal.impl;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.TransactionBal;
import com.compulynx.compas.dal.impl.TransactionDalImpl;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.AuthTransaction;
import com.compulynx.compas.models.Branch;
import com.compulynx.compas.models.Claim;
import com.compulynx.compas.models.ClaimReports;
import com.compulynx.compas.models.CompasProperties;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.TransactionDtl;
import com.compulynx.compas.models.TransactionMst;

@Component
public class TransactionBalImpl implements TransactionBal{

	@Autowired
	TransactionDalImpl transactionDal;

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#GetActiveProviderList()
	 */
	@Override
	public List<Merchant> GetActiveProviderList() {
		// TODO Auto-generated method stub
		return transactionDal.GetActiveProviderList();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#GetTransactionList(com.compulynx.compas.models.Transaction)
	 */
	@Override
	public List<Transaction> GetTransactionList(Transaction txn) {
		// TODO Auto-generated method stub
		return transactionDal.GetTransactionList(txn);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#UpdateCardStatus(com.compulynx.compas.models.Transaction)
	 */
	@Override
	public CompasResponse UpdateCardStatus(Transaction trans) {
		// TODO Auto-generated method stub
		return transactionDal.UpdateCardStatus(trans);
	}
	
	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#UpdateCardStatus(com.compulynx.compas.models.Transaction)
	 */
	@Override
	public CompasResponse UpdateTransStatus(Transaction trans) {
		// TODO Auto-generated method stub
		return transactionDal.UpdateTransStatus(trans);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#GetApprovedTrans(com.compulynx.compas.models.Transaction)
	 */
	@Override
	public List<Transaction> GetApprovedTrans(Transaction txn) {
		// TODO Auto-generated method stub
		return transactionDal.GetApprovedTrans(txn);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#GenerateSettlementFile(com.compulynx.compas.models.Transaction)
	 */
	@Override
	public CompasResponse GenerateSettlementFile(Transaction trans,String filePath) {
		// TODO Auto-generated method stub
		CompasResponse response= new CompasResponse();
		response=transactionDal.GenerateSettlementFile(trans,filePath);
		if(response.respCode==200){
			response= transactionDal.UpdateSettlementStatus(trans, response.fileName);
		}
		return response;
	}

	  @Override
	    public CompasProperties getCompasProperties() {
	        String path = System.getProperty("catalina.base");
	        Properties properties = new Properties();
	        try{
	            FileInputStream file;
	            //load the file handle for main.properties
	            file = new FileInputStream(path.trim());
	            //load all the properties from this file
	            properties.load(file);
	            //we have loaded the properties, so close the file handle
	            file.close();
	            return new CompasProperties(properties.getProperty("settlement.filepath").trim());
	        } catch (Exception ex){
	            ex.printStackTrace();
	        }
	        return null;
	    }

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#GetPendingAuth(com.compulynx.compas.models.Transaction)
	 */
	@Override
	public List<Transaction> GetPendingAuth(int orgId) {
		// TODO Auto-generated method stub
		System.out.print("BAL :"+orgId);
		return transactionDal.GetPendingAuth(orgId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#UpdatePendingAuthStatus(com.compulynx.compas.models.TransactionDtl)
	 */
	@Override
	public CompasResponse UpdatePendingAuthStatus(TransactionDtl txn) {
		// TODO Auto-generated method stub
		return transactionDal.UpdatePendingAuthStatus(txn);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#UpdateAuthRejectStatus(com.compulynx.compas.models.TransactionDtl)
	 */
	@Override
	public CompasResponse UpdateAuthRejectStatus(TransactionDtl txn) {
		// TODO Auto-generated method stub
		return transactionDal.UpdateAuthRejectStatus(txn);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#GetUpdAuthTrans(com.compulynx.compas.models.AuthTransaction)
	 */
	@Override
	public AuthTransaction GetUpdAuthTrans(AuthTransaction trans) {
		// TODO Auto-generated method stub
		return transactionDal.GetUpdAuthTrans(trans);
	}

	public List<TransactionMst> GetAuthPendingTransByProvider(int providerId) {
		// TODO Auto-generated method stub
		return transactionDal.GetPendingAuthByProvider(providerId);
	}
	
	public List<Reports> GetAllTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllTxnsDetails(rpt);
	}
	
	public List<Reports> GetOutstandingTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetOutstandingTxnsDetails(rpt);
	}
	
	public List<Reports> GetPendingTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetPendingTxnsDetails(rpt);
	}
	
	public List<Reports> GetUnpaidTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetUnpaidTxnsDetails(rpt);
	}
	
	public List<Reports> GetSettledTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetSettledTxnsDetails(rpt);
	}
	
	public List<Reports> GetRejectedTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetRejectedTxnsDetails(rpt);
	}
	

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.TransactionBal#UpdateAuthRejectStatus(com.compulynx.compas.models.TransactionDtl)
	 */
	@Override
	public CompasResponse AuthorizeTransaction(Transaction txn) {
		// TODO Auto-generated method stub
		return transactionDal.AuthorizeTransaction(txn);
	}
	
	public List<Reports> GetMemberTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetMemberTxnsDetails(rpt);
	}
	
	
	public List<Reports> GetCancelledTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetCancelledTxnsDetails(rpt);
	}
	
	public List<Reports> GetAuthorizedTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAuthorizedTxnsDetails(rpt);
	}
	public List<Reports> GetProviderWiseTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetProviderWiseTxnsDetails(rpt);
	}
	
	public List<Reports> GetSchemeWiseTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetSchemeWiseTxnsDetails(rpt);
	}	
	
	public List<Reports> GetProviderTrans(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetProviderTrans(rpt);
	}

	//@Override
	public List<Claim> GetAkuhTransactionList(Claim claimtxn) {
		// TODO Auto-generated method stub
		return transactionDal.GetAkuhTransactionList(claimtxn);
	}
	
	// @Override
	public CompasResponse updateAkuhTransStatus(Claim claimtrans) {
		// TODO Auto-generated method stub
		return transactionDal.updateAkuhTransStatus(claimtrans);
	}

	@Override
	public List<Claim> GetApprovedClaimTrans(Claim claimtxn) {
		// TODO Auto-generated method stub
		return transactionDal.GetApprovedClaimTrans(claimtxn);
	}

	@Override
	public CompasResponse GenerateClaimSettlementFile(Claim claimtrans, String filePath) {
		// TODO Auto-generated method stub
		CompasResponse response= new CompasResponse();
		response=transactionDal.GenerateClaimSettlementFile(claimtrans, filePath);
		if(response.respCode==200){
			response= transactionDal.UpdateClaimSettlementStatus(claimtrans, response.fileName);
			System.out.println("This is the claims file:" +response.fileName);
		}
		return response;
	}
	
	@Override
	public List<ClaimReports> GetAllAkuhTxnsDetails(ClaimReports akuhrpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllAkuhTxnsDetails(akuhrpt);
	}

	@Override
	public List<Reports> GetAllGtTxnsDetails(Reports gtrpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllGtTxnsDetails(gtrpt);
	}

	@Override
	public List<Reports> GetAllMpshahTxnsDetails(Reports mpsharpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllMpshahTxnsDetails(mpsharpt);
	}

	@Override
	public List<Reports> GetAllNbiTxnsDetails(Reports nbrpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllNbiTxnsDetails(nbrpt);
	}

	@Override
	public List<Reports> GetAllDetailedUHMCHospTxn(Reports uhmcrpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllDetailedUHMCHospTxn(uhmcrpt);
	}

	@Override
	public List<Reports> GetAllDetailedClinixTxn(Reports clinixrpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllDetailedClinixTxn(clinixrpt);
	}

	@Override
	public List<Reports> GetAllServiceProvidersTxnsDetails(Reports sprpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetAllServiceProvidersTxnsDetails(sprpt);
	}

	@Override
	public List<Reports> GetAllDetailedMemberUtilizations() {
		// TODO Auto-generated method stub
		return transactionDal.GetAllDetailedMemberUtilizations();
	}

	@Override
	public List<Reports> GetAllDetailedTotalUtilizations() {
		// TODO Auto-generated method stub
		return transactionDal.GetAllDetailedTotalUtilizations();
	}

	@Override
	public List<Reports> GetJDCProviderWiseTxnsDetails(Reports rpt) {
		// TODO Auto-generated method stub
		return transactionDal.GetJDCProviderWiseTxnsDetails(rpt);
	}

	@Override
	public List<Reports> GetAllDetailedMemberReport() {
		// TODO Auto-generated method stub
		return transactionDal.GetAllDetailedMemberReport();
	}
	
}

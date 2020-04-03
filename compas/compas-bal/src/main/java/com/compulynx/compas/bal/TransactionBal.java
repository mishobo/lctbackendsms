package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.AuthTransaction;
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

public interface TransactionBal {
	List<Merchant> GetActiveProviderList();
	List<Transaction> GetTransactionList(Transaction txn);
	CompasResponse UpdateCardStatus(Transaction trans);
	CompasResponse UpdateTransStatus(Transaction trans);
	List<Transaction> GetApprovedTrans(Transaction txn);
	CompasResponse GenerateSettlementFile(Transaction trans,String filePath);
	CompasProperties getCompasProperties();
	List<Transaction> GetPendingAuth(int orgId);
	CompasResponse UpdatePendingAuthStatus(TransactionDtl txn);
	CompasResponse UpdateAuthRejectStatus(TransactionDtl txn);
	AuthTransaction GetUpdAuthTrans(AuthTransaction trans);
	
	CompasResponse AuthorizeTransaction(Transaction trans);
	
	List<Reports> GetAllTxnsDetails(Reports rpt);
	List<Reports> GetOutstandingTxnsDetails(Reports rpt);
	List<Reports> GetPendingTxnsDetails(Reports rpt);
	List<Reports> GetUnpaidTxnsDetails(Reports rpt);
	List<Reports> GetSettledTxnsDetails(Reports rpt);
	List<Reports> GetRejectedTxnsDetails(Reports rpt);
	List<Reports> GetMemberTxnsDetails(Reports rpt);
	List<Reports> GetCancelledTxnsDetails(Reports rpt);
	List<Reports> GetAuthorizedTxnsDetails(Reports rpt);
	List<Reports> GetProviderWiseTxnsDetails(Reports rpt);
	List<Reports> GetSchemeWiseTxnsDetails(Reports rpt);
	List<Reports> GetProviderTrans(Reports rpt);
	public List<TransactionMst> GetAuthPendingTransByProvider(int providerId);
	List<Claim> GetAkuhTransactionList(Claim claimtxns);
	CompasResponse updateAkuhTransStatus(Claim claimtrans);
	List<Claim> GetApprovedClaimTrans(Claim claimtxn);
	CompasResponse GenerateClaimSettlementFile(Claim claimtrans,String filePath);
	List<ClaimReports> GetAllAkuhTxnsDetails(ClaimReports akuhrpt);
	List<Reports> GetAllGtTxnsDetails(Reports gtrpt);
	List<Reports> GetAllMpshahTxnsDetails(Reports mpsharpt);
	List<Reports> GetAllNbiTxnsDetails(Reports nbrpt);
	List<Reports> GetAllDetailedUHMCHospTxn(Reports uhmcrpt);
	List<Reports> GetAllDetailedClinixTxn(Reports clinixrpt);
	List<Reports> GetAllServiceProvidersTxnsDetails(Reports sprpt);
	List<Reports> GetAllDetailedMemberUtilizations();
	List<Reports> GetAllDetailedTotalUtilizations();
	List<Reports> GetJDCProviderWiseTxnsDetails(Reports rpt);
	List<Reports> GetAllDetailedMemberReport();
}

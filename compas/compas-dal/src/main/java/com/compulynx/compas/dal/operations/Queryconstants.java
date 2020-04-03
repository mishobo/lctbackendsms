package com.compulynx.compas.dal.operations;

public class Queryconstants {
	public static String getMatchingMembers = "Select emp_id, emp_fullname, emp_ref_key from emp_mst where emp_ref_key like '%' ? '%' ";
	public static String getAllMembers = "Select emp_id, emp_fullname, emp_ref_key from emp_mst ";

	public static String getMemberBasicDetails = "Select emp_id, emp_fullname, relation, principle_id, emp_ref_key, emp_gender, "
			+ " product_name, card_no from emp_mst em inner join productmaster pm on pm.id = em.product_id "
			+ " inner join card_mst cm on cm.emp_mst_id = em.emp_id where emp_ref_key=?";
	public static String updateBioStatus = "UPDATE card_mst set bio_status = NULL where card_no=?";
	public static String deleteBioImages = "DELETE from customerbioimages where card_number=?";
	public static String insertDetachRecord = "INSERT into customerbiodetach (emp_id, member_no, reason, createdBy,detach, createdOn) values(?,?,?,?,?,?)";
	public static String getDetachesByMemberId = "Select cd.reason, cd.createdOn, (select userfullname from usermaster where id=cd.createdBy) as createdBy,cd.detach from customerbiodetach cd where emp_id=? and cd.detach = 'D'";
	public static String getMemberByMemberNo = "Select emp_id, bio_status, card_no, emp_fullname, product_name, emp_dob, em.active from emp_mst em inner join card_mst cm on cm.emp_mst_id=emp_id inner join productmaster pm on pm.id = product_id where emp_ref_key=?";
	public static String updateVerifiedServiceBalance = "UPDATE mst_allocation set service_balance = (service_balance + ?) where acc_no=? and service_id=? and basket_id=? and programme_id=? ";
	public static String updateVerifiedBasketBalance = "UPDATE mst_allocation set basket_balance = (basket_balance + ?) where acc_no=? and basket_id=? and programme_id=? ";
	public static String getPrincipleDetails = "SELECT emp_id as member_id,org_id, product_id, acc_id, (select count(emp_id) from emp_mst where principle_id = member_id) as bnfcount "
			+ " from emp_mst em inner join emp_acc_dtl ac on ac.emp_mst_id = emp_id and emp_ref_key = ?";
	public static String get1MIndividualServiceDtl = "SELECT distinct ac.basket_Id,ser_code, ser_name, cover_limit, require_auth, basket_value,ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable < 3";
	public static String get1FIndividualServiceDtl = "SELECT distinct ac.basket_Id,ser_code, ser_name,cover_limit, require_auth, basket_value, ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable < 4";
	public static String get2IndividualServiceDtl = "SELECT distinct ac.basket_Id,ser_code, ser_name, ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable =1";

	public static String get1MMemberServiceDtl = "SELECT distinct ac.basket_Id,ser_code, cover_limit, ser_name, require_auth, ac.basket_value, ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable < 3";
	public static String get1FMemberServiceDtl = "SELECT distinct ac.basket_Id,ser_code,ser_name,cover_limit, require_auth, ac.basket_value, ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable < 4";
	public static String get2MemberServiceDtl = "SELECT distinct ac.basket_Id,ser_code, cover_limit, ac.basket_value,ser_name, ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable=1";
	public static String getFamilyCardNumber = "SELECT cm.card_no, acc_no, ed.acc_id from card_mst cm inner join emp_mst em on em.principle_id =? and cm.emp_mst_id = em.emp_id inner join emp_acc_dtl ed on ed.emp_mst_id = em.emp_id";
	public static String getNewBeneficiaryFamilyMembers = "SELECT em.emp_id,em.emp_gender, relation, em.emp_fullname,em.emp_ref_key,em.emp_dob, em.active, em.member_type, em.emp_nationalid, em.lct_code "
			+ "FROM emp_mst em " + "WHERE principle_id=? AND member_type = 'D'";
	public static String insertNewMemberFamilyDetails = "INSERT INTO emp_mst(principle_id,emp_no,emp_pdc,emp_ref_key,emp_fullname,emp_dob,emp_gender,relation,active, product_id, org_id, Created_By, Created_On, member_type, emp_nationalid, lct_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String getProviderTransactions = "select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,, mer_name,pg.ProgrammeDesc, productId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id inner join usermaster us on us.merchantId=mt.provider_id where em.emp_ref_key=member_no and cp.isactive=1  and us.id=? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? order by trans_date desc ";
	public static String getSortedProviderTransactions = "select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,, mer_name,pg.ProgrammeDesc, productId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id inner join usermaster us on us.merchantId=mt.provider_id where em.emp_ref_key=member_no and cp.isactive=1  and us.id=? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=? order by trans_date desc ";

	public static String getProviderFromDevice = "Select m.id from mst_merchant m inner join deviceissuedetails di on di.merchantId = m.id inner join deviceregdetails dr on dr.id=di.DeviceRegId where dr.serialNo=?";
	public static String getApprovedTransactionDtl = "select md.id,md.bill_no,member_no, emp_fullname as member_name,md.trans_date, approved_amount as amount, txn_amount , mer_name as provider_name, ser_name, mt.approved_on, username, voucher_name from member_trans_mst md inner join member_trans_dtl mt on mt.Trans_mst_id=md.Id inner join card_mst cm on cm.pj_number=md.member_no inner join deviceregdetails dr on dr.serialno=md.device_id inner join deviceissuedetails di on di.deviceregid=dr.id inner join mst_merchant m on m.id=merchantid inner join usermaster u on u.id=mt.verified_by inner join programmemaster pm on pm.id=md.programme_id inner join mst_organization og on og.id=pm.org_id inner join emp_mst em on em.emp_ref_key=md.member_no inner join mst_health_services mh on mh.id=mt.service_id inner join voucher_mst vs on vs.id=mt.basket_id  where mt.id=? ";
	public static String getAuthPendingTrans = "select md.trans_status,ser_code,md.service_id as serviceId, md.auth_amount, txn_amount, member_no, auth_reason, rejected_reason from member_trans_dtl md,member_trans_mst m,mst_health_services h where  m.id=md.trans_mst_id and md.id=? and h.id=md.service_id ";
	public static String getMemberProgramme = "Select p.id as productId, p.end_date, product_name, p.active from productmaster p, customerprogrammedetails c, programmemaster pm where c.IsActive=1 and pm.id=programmeId and pm.productId=p.id and c.CustomerId=?";
	public static String getBalances = "SELECT id, service_balance, basket_balance from mst_allocation WHERE acc_no = ? and card_number=? AND basket_id = ?";
	public static String updateBalances = "UPDATE mst_allocation set service_balance=?, basket_balance=? WHERE id = ? ";
	public static String updateProgrammeVoucherCoverLimits = "UPDATE programmevoucherdetails set cover_limit = ?, require_auth=? WHERE programmeId = ? AND voucherId = ? AND service_id = ? ";
	public static String getVouchersByProgrammeIdNew = " SELECT distinct PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,basket_value,frqmode,frq_type, S.cover_type "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=? ";
	public static String getProgrammesByScheme = "SELECT prg_type,pm.Id, ProgrammeCode, ProgrammeDesc, pm.Active, pm.CreatedBy, pm.CreatedOn,pm.start_date,pm.end_date,productid,itm_Modes,chtm_Modes,intm_Modes,product_name,cover_type_id "
			+ "FROM ProgrammeMaster pm,productmaster p where p.id=pm.productid and pm.org_id=? and productid=?";
	public static String updAuthMemberTransDtl = "update member_trans_dtl set trans_status=?,auth_amount = ?, auth_reason=?,auth_by=?,auth_on=? where id=?";
	public static String getPendingAuthTransactions = "select  mt.id,md.bill_no as benefit_desc,member_no,md.trans_date, mt.image, txn_amount as amount ,mer_name as provider_name,ac.acc_no, card_serial from member_trans_mst md inner join member_trans_dtl mt on mt.Trans_mst_id=md.Id inner join card_mst cm on cm.pj_number=md.member_no inner join emp_acc_dtl ac on  ac.emp_mst_id=cm.emp_mst_id inner join deviceregdetails dr on dr.serialno=md.device_id inner join deviceissuedetails di on di.deviceregid=dr.id inner join mst_merchant m on m.id=merchantid inner join emp_mst em on em.emp_ref_key=md.member_no where mt.trans_status=-2 and mt.paid_status=110 and em.org_id=? order  by trans_date DESC ";
	public static String updApprovedMemberTransDtl = "update member_trans_dtl set verified_amount=?, trans_status=?,paid_status=?, approved_amount = ?, approved_by=?,approved_on=? where id=?";
	public static String updMemberTransDtlStatus = "update member_trans_dtl set txn_amount=?, trans_status=?,paid_status=?, verified_amount = ?, verified_by=?,verified_on=? where id=?";
	public static String updRejectedTransDtl = "update member_trans_dtl set trans_status=?,paid_status=?,rejected_by=?,rejected_on=?, rejected_reason=? where id=?";
	public static String updCancelledTransDtl = "update member_trans_dtl set trans_status=?,paid_status=?,cancelled_by=?,cancelled_on=?, reason=? where id=?";
	public static String getUnissuedDevices = "select DR.Id,dr.SerialNo,DR.active,DR.createdby,DR.imeiNo from DeviceRegDetails DR where Active=1 and DR.id not in (select DeviceRegId from DeviceIssueDetails)";
	public static String insertActivatedCards = "INSERT into cardactivation (card_id, card_uid, memberNo, deviceId, activatedBy, activatedOn, createdOn) values(?,?,?,?,?,?,?)";
	public static String getMMemberServiceDtl = "SELECT distinct ac.basket_Id,ser_code, cover_limit, require_auth, ac.basket_value, ser_name, ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN cover_limit = 0.00 THEN COALESCE((service_value - basket_balance),0) WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and S.applicable<>3";
	public static String updateProgrammeVoucherDetails = "UPDATE programmevoucherdetails set basket_value = ? WHERE programmeId = ? AND voucherId = ? AND service_id = ? ";
	public static String updateBasketVouchers = "UPDATE mst_allocation set basket_balance = ?, basket_value = ? WHERE programme_id = ? AND basket_id = ? AND service_id = ? ";
	public static String getAllocationLimits = "SELECT distinct programme_id, basket_value, basket_balance, coalesce((basket_value-basket_balance),0) as used FROM mst_allocation where programme_id = ?  and basket_id = ?";
	public static String getAllocationsUsingProgramme = "SELECT distinct acc_id, acc_no, card_number, basket_value, basket_balance  FROM mst_allocation where programme_id = ?";
	public static String getProgrammeVoucherDetailsIds = "SELECT distinct voucherId FROM programmevoucherdetails where IsActive = 1 and programmeId = ?";
	public static String deleteServicesFromAllocations = "delete from mst_allocation where Programme_id = ? and basket_id=? and service_id = ?";
	public static String deleteProgrammevoucherDetails = "delete from ProgrammevoucherDetails where ProgrammeId = ? and voucherId=? and service_id = ?";
	public static String deleteVoucherServiceById = "delete from voucher_dtl where voucher_id=? and service_id = ?";
	public static String getAvailableServicesInVoucher = "SELECT service_id FROM voucher_dtl where voucher_id = ?";
	public static String getAllocationsUsingProgrammeAndVoucher = "SELECT distinct acc_id, acc_no, card_number, basket_value, basket_balance  FROM mst_allocation where programme_id = ? and basket_id = ? ";
	public static String getProgrammeIdsUsingVoucher = "SELECT distinct ProgrammeId, basket_value FROM programmevoucherdetails where voucherId = ?";
	public static String getTransactionDetails = "SELECT dt.id, (case when approved_amount > 0 then approved_amount when verified_amount>0 then verified_amount when auth_amount then auth_amount else txn_amount end) as txn_amount, ms.ser_name, ms.id as ser_id, v.voucher_name, v.id as voucher_id FROM member_trans_dtl dt,mst_health_services ms, voucher_mst v where ms.id=dt.service_id and v.id=dt.basket_id and dt.id = ? ";
	public static String getAccountNumber = "SELECT acc_no from emp_acc_dtl where acc_id = ?";
	public static String deleteMemberServices = "DELETE from mst_allocation where card_number = ?";
	public static String insertCardDetails = "INSERT INTO card_mst( acc_id,emp_mst_id,pj_number,card_no,card_pj,card_status,created_by,created_on) VALUES (?,?,?,?,?,?,?,?)";
	public static String insertMemberVouchers = "INSERT INTO mst_allocation (programme_id,card_number,acc_id,acc_no,basket_id,service_id,service_value,basket_value,service_balance,basket_balance,cycle,created_by,created_on) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertMemberVouchers2 = "INSERT INTO mst_allocation (programme_id,card_number,acc_id,acc_no,created_by,created_on,basket_id, service_id,service_value,basket_value,service_balance,basket_balance,cycle) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateMemberVouchers = "UPDATE mst_allocation set basket_balance = ?, basket_value = ? WHERE acc_id= ? AND programme_id = ? AND basket_id = ? AND service_id = ? ";
	public static String updateBusketBalance = "UPDATE mst_allocation set basket_balance = ? WHERE programme_id = ? AND acc_no = ?";

	public static String getBillTransactions = "select  mt.id,md.bill_no as benefit_desc,member_no,md.trans_date, mt.image, ( case when mt.auth_amount>0 then mt.auth_amount else mt.txn_amount end) as amount ,mer_name as provider_name,ac.acc_no, service_id, basket_id, programme_id from member_trans_mst md inner join member_trans_dtl mt on mt.Trans_mst_id=md.Id inner join card_mst cm on cm.pj_number=md.member_no inner join emp_acc_dtl ac on  ac.emp_mst_id=cm.emp_mst_id inner join mst_merchant m on m.id=md.provider_id inner join emp_mst em on em.emp_ref_key=md.member_no where date(md.trans_date) >= ? and date(md.trans_date) <= ? and md.provider_id=? and mt.trans_status in(0,4,5) and mt.paid_status in(110,114,115) and em.org_id=? order  by trans_date DESC";
	public static String getVerifiedTransactions = "select  mt.id,md.bill_no as benefit_desc,member_no,md.trans_date, mt.image, ( case when verified_amount>0 then verified_amount else txn_amount end) as amount ,mer_name as provider_name,ac.acc_no, service_id, basket_id, programme_id from member_trans_mst md inner join member_trans_dtl mt on mt.Trans_mst_id=md.Id inner join card_mst cm on cm.pj_number=md.member_no inner join emp_acc_dtl ac on  ac.emp_mst_id=cm.emp_mst_id inner join mst_merchant m on m.id=md.provider_id inner join emp_mst em on em.emp_ref_key=md.member_no where date(md.trans_date) >= ? and date(md.trans_date) <= ? and md.provider_id=? and mt.trans_status in(1,5,4) and mt.paid_status in(111,114,115) and em.org_id=? order  by trans_date DESC ";
	public static String getApprovedTransactions = "select  sum(approved_amount) as amount,count(mt.id) as noofreceipt,mm.mer_name,mer_code, bank_code,bank_name, GROUP_CONCAT(mt.id) as txnid from member_trans_mst md inner join mst_merchant mm on mm.id=md.provider_id inner join emp_mst em on em.emp_ref_key=md.member_no inner join member_trans_dtl mt on  md.id=mt.trans_mst_id where date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and mt.trans_status=2 and mt.paid_status=112 and mm.id=? and org_id=? group by mm.mer_name ";
	public static String getVouchersFromProgramme =

			"SELECT distinct PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,basket_value,frqmode,frq_type, cover_type   FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId= ? union all   SELECT distinct S.Id,S.voucher_name,0 AS IsActive,S.created_by,0 as basket_value,'' as frqmode,'' as frq_type, cover_type FROM ProgrammeMaster PM,Voucher_mst S  WHERE  S.Id NOT IN ( SELECT voucherId FROM ProgrammeVoucherDetails PS WHERE PS.voucherId=S.Id AND PM.ID=PS.ProgrammeId ) AND PM.Id=?";

//	public static String getAllTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =?) "
//			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =?) order by trans_date desc";

	public static String getAllTransactions = "select  member_no, trans_date, amount, invoice_number, (select emp_fullname from emp_mst em where emp_ref_key = mt.member_no ) as member_name, (select mer_name from mst_merchant ma where id = mt.provider_id ) as provider_name, (select product_name from productmaster where id = mt.scheme_id) as scheme_name, (select ser_name from mst_health_services where id = (select service_id from member_trans_dtl where trans_mst_id = mt.id)) as service_name from member_trans_mst mt order by mt.trans_date desc";
	
	public static String getAllTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ?) order by trans_date desc";

	public static String getAllTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_numbermd.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ?) order by trans_date desc";

	public static String getAllTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?) order by trans_date desc";

	public static String getPendingTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and md.trans_status=1 and md.paid_status=111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and md.trans_status=1 and md.paid_status=111) order by trans_date desc";

	public static String getPendingTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=1 and md.paid_status=111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=1 and md.paid_status=111) order by trans_date desc";

	public static String getPendingTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=1 and md.paid_status=111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ? and md.trans_status=1 and md.paid_status=111) order by trans_date desc";

	public static String getPendingTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=1 and md.paid_status=111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.verified_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=1 and md.paid_status=111) order by trans_date desc";

	public static String getOutstandingTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and md.trans_status=0 and md.paid_status=110 order by trans_date desc) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and md.trans_status=0 and md.paid_status=110) order by trans_date desc";

	public static String getOutstandingTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=0 and md.paid_status=110 order by trans_date desc) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=0 and md.paid_status=110) order by trans_date desc";

	public static String getOutstandingTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=0 and md.paid_status=110 order by trans_date desc) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=0 and md.paid_status=110) order by trans_date desc";

	public static String getOutstandingTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=0 and md.paid_status=110 order by trans_date desc) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=0 and md.paid_status=110) order by trans_date desc";

	public static String getUnpaidTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and md.trans_status=2 and md.paid_status=112) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and md.trans_status=2 and md.paid_status=112) order by trans_date desc";

	public static String getUnpaidTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=2 and md.paid_status=112) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=2 and md.paid_status=112) order by trans_date desc";

	public static String getUnpaidTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=2 and md.paid_status=112) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ? and md.trans_status=2 and md.paid_status=112) order by trans_date desc";

	public static String getUnpaidTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=2 and md.paid_status=112) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=2 and md.paid_status=112) order by trans_date desc";

	public static String getSettledTransactions = "call {prc_getsettledbills(?),(?),(?)}";

	public static String getSettledTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=3 and md.paid_status=113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=3 and md.paid_status=113) order by trans_date desc";

	public static String getSettledTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=3 and md.paid_status=113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ? and md.trans_status=3 and md.paid_status=113) order by trans_date desc";

	public static String getSettledTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=3 and md.paid_status=113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.approved_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=3 and md.paid_status=113) order by trans_date desc";

	public static String getRejectedTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.txn_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and md.trans_status=-3 and md.paid_status=-113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and md.trans_status=-3 and md.paid_status=-113) order by trans_date desc";

	public static String getRejectedTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.txn_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=-3 and md.paid_status=-113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=-3 and md.paid_status=-113) order by trans_date desc";

	public static String getRejectedTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.txn_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=-3 and md.paid_status=-113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ? and md.trans_status=-3 and md.paid_status=-113) order by trans_date desc";

	public static String getRejectedTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, md.txn_amount as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=-3 and md.paid_status=-113) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=-3 and md.paid_status=-113) order by trans_date desc";

	//public static String getMemberTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status,    (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and member_no= ?  and md.trans_status<>-3 ) "
		//	+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id   where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and member_no= ? and md.trans_status<>-3 ) order by trans_date desc";

	public static String getMemberTransactions = "select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status,\r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount\r\n" + 
			" else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance,\r\n" + 
			" mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId\r\n" + 
			" from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no\r\n" + 
			" inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster\r\n" + 
			" pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			" inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 \r\n" + 
			"and pm.org_id =? and SUBSTRING_INDEX(member_no, '-', 1)= SUBSTRING_INDEX(?, '-', 1) and md.trans_status<>-3\r\n" + 
			"UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, \r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount\r\n" + 
			" else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as\r\n" + 
			" basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as \r\n" + 
			" MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on \r\n" + 
			" em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id \r\n" + 
			" inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join \r\n" + 
			" programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and\r\n" + 
			" SUBSTRING_INDEX(member_no, '-', 1)=  SUBSTRING_INDEX(?, '-', 1) and \r\n" + 
			" md.trans_status<>-3 ) order by trans_date desc";
	
	public static String getMemberTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status,   (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and member_no =? and date(mt.trans_date) >= ? and md.trans_status<>-3) "
			+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id   where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and member_no =? and date(mt.trans_date) >= ?  and md.trans_status<>-3 ) order by trans_date desc";

	public static String getMemberTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status,   (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and member_no =? and date(mt.trans_date) <=? and md.trans_status<>-3 ) "
			+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id   where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and member_no =? and date(mt.trans_date) <=?  and md.trans_status<>-3 ) order by trans_date desc";

	public static String getMemberTransactionsBetween = "select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status,\r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount\r\n" + 
			" else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance,\r\n" + 
			" mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId\r\n" + 
			" from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no\r\n" + 
			" inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster\r\n" + 
			" pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			" inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 \r\n" + 
			"and pm.org_id =? and SUBSTRING_INDEX(member_no, '-', 1)= SUBSTRING_INDEX(?, '-', 1) and md.trans_status<>-3 and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?" 
			+ "UNION ALL select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, \r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount\r\n" + 
			" else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as\r\n" + 
			" basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as \r\n" + 
			" MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on \r\n" + 
			" em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id \r\n" + 
			" inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join \r\n" + 
			" programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and\r\n" + 
			" SUBSTRING_INDEX(member_no, '-', 1)=  SUBSTRING_INDEX(?, '-', 1) and md.trans_status<>-3 and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? order by trans_date desc";

	public static String getAuthorizedTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and md.auth_by>0) "
			+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and md.auth_by>0) order by trans_date desc";

	public static String getAuthorizedTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.auth_by>0) "
			+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.auth_by>0) order by trans_date desc";

	public static String getAuthorizedTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.auth_by>0 ) "
			+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ? and md.auth_by>0) order by trans_date desc";

	public static String getAuthorizedTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.auth_by>0) "
			+ " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.auth_by>0) order by trans_date desc";

	public static String getCancelledTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and md.trans_status=-1 and md.paid_status=-111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and md.trans_status=-1 and md.paid_status=-111) order by trans_date desc";

	public static String getCancelledTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=-1 and md.paid_status=-111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and md.trans_status=-1 and md.paid_status=-111) order by trans_date desc";

	public static String getCancelledTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? and md.trans_status=-1 and md.paid_status=-111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <= ? and md.trans_status=-1 and md.paid_status=-111) order by trans_date desc";

	public static String getCancelledTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=-1 and md.paid_status=-111) "
			+ "UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, md.trans_status, (case when md.auth_amount>0 then md.auth_amount else md.txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? and md.trans_status=-1 and md.paid_status=-111) order by trans_date desc";

//	public static String getProviderWiseTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=?) "
//			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? order by trans_date desc";
//	
	public static String getProviderWiseTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? ) "
	            + " UNION ALL (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? ) order by trans_date desc";
	
	public static String getProviderWiseTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? and date(mt.trans_date) >= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? and date(mt.trans_date) >= ?) order by trans_date desc";

	public static String getProviderWiseTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? and date(mt.trans_date) <=? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? and date(mt.trans_date) <=?) order by trans_date desc";

	public static String getProviderWiseTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and m.id=? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?) order by trans_date desc";

	public static String getSchemeWiseTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=?) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? ) order by trans_date desc";

	public static String getSchemeWiseTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? and date(mt.trans_date) >= ?) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? and date(mt.trans_date) >= ? ) order by trans_date desc";

	public static String getSchemeWiseTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? and date(mt.trans_date) <=? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? and date(mt.trans_date) <= ?) order by trans_date desc";

	public static String getSchemeWiseTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id  where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.id=? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?) order by trans_date desc";
	public static String getUserCredentialManual = "Select ID As UserID, userTypeId, UserGroupId, first_time_login From UserMaster Where UserName = ? And UserPassword = ?";
	public static String getUserCredentialDevice = "select *from merchant_pos_users_mst pm inner join DeviceIssueDetails di on pm.id=di.posUserId inner join DeviceRegDetails dr on "
			+ "di.DeviceRegId=dr.id where pm.username=? and password=? and dr.serialno=? and dr.active=1";
	public static String getUserBioManual = "Select ID As UserID,UserBioID From UserMaster Where UserName = ? ";
	public static String getUserCredentialBio = "Select ID As UserID From UserMaster Where UserBioID = ?";
	public static String getSafComUser = "Select * from usermaster where username=? and userpassword=? and deviceid=? and usertype=?";
	public static String getUserTypeId = "Select id,userTypeId from usermaster where id=?";
	public static String getUserGrpRights = "Select Um.ID, Um.UserName, Um.UserFullName, UM.userTypeId, Ug.ID As UserGroupID, org_id  As LinkId,  useremail  As LinkExtInfo ,0 As UserClassID,  "
			+ " '' AS LinkName,"
			+ " Hm.ID As HeaderID,Hm.HeaderName,Hm.HeaderIconCss,Hm.HeaderIconColor,Rm.RightDisplayName,Rm.RightShortCode,Rm.RightViewName,Rm.RightName,Ur.AllowView,Ur.AllowEdit,Ur.AllowAdd,Ur.AllowDelete,Rm.RightMaxWidth"
			+ " From UserAssignedRights Ur Inner Join RightsMaster Rm On Ur.RightID = Rm.ID"
			+ " Inner join UserMaster Um On Um.UserGroupID = Ur.GroupId "
			+ " Inner join MenuHeaderMaster Hm on Hm.ID = Rm.RightHeaderID"
			+ " Inner Join UserGroupsMaster Ug on Ug.ID = Um.UserGroupID And Ug.ID = Ur.GroupId"
			+ " Where Um.ID = ? And Ur.AllowView = 1" + " Order By Hm.HeaderPos,rightsclassid";

	// User Master
	public static String insertUser = "INSERT INTO UserMaster (UserName,UserFullName,UserPassword,UserGroupID,UserEmail,UserPhone,UserSecretQuestionID,UserSecretQuestionAns,UserBioLogin ,UserLinkedID,UserBioID,Active,CreatedBy,CreatedOn,merchantId,usertypeid,pos_user_level, first_time_login) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateUser = "UPDATE UserMaster SET UserName=?,UserFullName=?,UserPassword=?,UserGroupID=?,UserEmail=?,UserPhone=?,UserSecretQuestionID=?,UserSecretQuestionAns=?,UserBioLogin=? ,UserLinkedID=?,UserBioID=?,Active=?,CreatedBy=?,CreatedOn=? ,merchantId=?,usertypeid=?,pos_user_level=? WHERE ID=? ";
	public static String getUserByName = "Select ID From UserMaster Where UserName =?";
	public static String getClasses = "SELECT ID,ClassName FROM S_ClassMaster";
	public static String getLocationAgents = "SELECT * FROM AGENTMASTER WHERE locationID=?";
	public static String getUsers = "SELECT a.ID,UserName,UserFullName,UserPassword,UserGroupID,UserEmail,UserPhone,AgentId,merchantId,locationid"
			+ ",UserSecretQuestionID,UserSecretQuestionAns,UserBioLogin,UserLinkedID,UserBioID,a.Active,a.CreatedBy,a.first_time_login,usertypeid,"
			+ "case when usertypeid=1 then 'POS User' else 'Web User' end as userTypeName,pos_user_level"
			+ ",a.CreatedOn FROM UserMaster a,UserGroupsMaster b " + "where a.UserGroupID=b.ID ";
	public static String getUserBranches = "SELECT B.BranchName,u.BranchId as BranchId,u.isActive as isChecked "
			+ " FROM UserMaster UM " + "INNER JOIN UserBranchDetails U ON U.UserId=UM.ID "
			+ "right JOIN BranchMaster B ON B.ID=U.BranchId " + "WHERE UM.ID=? " + "union all "
			+ "SELECT B.BranchName,b.Id as BranchId,0 as isChecked " + "FROM UserMaster UM, BranchMaster B where "
			+ "UM.ID=? and B.Id not in(select BranchId from UserBranchDetails WHERE UserBranchDetails.UserId=?) ";
	public static String deleteUserBranches = "DELETE FROM UserBranchDetails WHERE UserId=?";
	public static String insertUserBranchDetails = "Insert Into UserBranchDetails (UserId,BranchId,IsActive,CreatedBy,CreatedOn) Values(?,?,?,?,?)";
	public static String getUserById = "SELECT a.ID as userid,UserName,UserFullName,UserPassword,UserGroupID,UserEmail,UserPhone"
			+ ",a.Active,a.CreatedBy ,a.CreatedOn,a.first_time_login,b.GroupName FROM UserMaster a,UserGroupsMaster b "
			+ "where a.UserGroupID=b.ID and a.id=?";
	public static String getQuestions = " SELECT Id, Question, CreatedBY, CreatedOn FROM QuestionMaster";
	public static String getGroupsByUserType = "select * from usergroupsmaster where grouptypeid=?";
	// UserGroup

	public static String insertUserGroup = "INSERT INTO UserGroupsMaster (groupcode,GroupName,grouptypeid,Active,CreatedBy,CreatedOn) VALUES (?,?,?,?,?,?)";
	public static String updateUserGroup = "UPDATE UserGroupsMaster Set GroupName = ?,grouptypeid=?,active=?,CreatedBy = ?, CreatedOn = ? Where ID = ?";
	public static String getGroupByName = "Select ID From UserGroupsMaster Where GroupName = ? ";
	public static String getGroupById = "SELECT U.RightID,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete,"
			+ "U.AllowView AS RightView,U.AllowAdd AS RightAdd,U.AllowEdit AS RightEdit,U.AllowDelete AS RightDelete "
			+ "FROM UserGroupsMaster G " + "INNER JOIN UserAssignedRights U ON U.GroupId=G.ID "
			+ "right JOIN RightsMaster R ON R.ID=U.RightID " + "WHERE G.ID=? " + "union all "
			+ "SELECT r.Id,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete,"
			+ "0 AS RightView,0 AS RightAdd,0 AS RightEdit,0 AS RightDelete "
			+ "FROM UserGroupsMaster G ,RightsMaster R "
			+ "WHERE G.ID=? and r.Id not in(select rightid from UserAssignedRights WHERE UserAssignedRights.GroupId=?)  ";
	public static String getGroups = "Select ID,groupcode,GroupName,grouptypeid,Active,CreatedBy,case when grouptypeid=1 then 'POS' else 'WEB' end as grpTypeName From UserGroupsMaster";
	public static String deleteGroupRights = "DELETE FROM UserAssignedRights WHERE GroupId=?";
	public static String getGroupTypes = "select * from mst_grp_type";
	public static String getGroupByCode = "select id from UserGroupsMaster where groupcode=?";
	public static String getGroupNameByCode = "select id from UserGroupsMaster where groupname=? and  groupcode<>?";
	// Rights
	// public static String getRights =
	// "SELECT
	// ID,RightDisplayName,AllowView,AllowAdd,AllowEdit,AllowDelete,CreatedBy,CreatedOn
	// FROM RightsMaster ";
	public static String getRights = "SELECT ID,RightDisplayName,AllowView,AllowAdd,AllowEdit,AllowDelete,RightsClassID,CreatedBy,CreatedOn FROM RightsMaster";
	public static String insertGroupRights = "INSERT INTO UserAssignedRights (RightID,GroupId ,AllowView,AllowAdd ,AllowEdit,AllowDelete,CreatedBy,CreatedOn) VALUES (?,?,?,?,?,?,?,?)";
	public static String updateGroupRights = "UPDATE UserAssignedRights SET AllowView=?,AllowAdd=? ,AllowEdit=?,AllowDelete=?,CreatedBy=?,CreatedOn=? WHERE RightID=? AND GroupId=?";

	// Branch
	public static String getBranches = "SELECT B.Id, BranchCode, BranchName, merchantId,mer_name,classId,b.Active, B.CreatedBy, B.CreatedOn,locationId,l.location_name "
			+ "From BranchMaster B,locationmaster l, mst_merchant m where  l.id=b.locationid and m.id=b.merchantid";
	public static String insertBranches = "Insert into BranchMaster(BranchCode, BranchName,merchantId,Active,CreatedBy, CreatedOn,locationId) Values(?,?,?,?,?,?,?)";
	public static String updateBranches = "Update BranchMaster  Set  BranchName=?,merchantid=?,Active=? ,CreatedBy=?, CreatedOn=?,locationId=? Where Id=? ";
	public static String getBranchByName = "Select ID From BranchMaster Where BranchName = ? and merchantid=?";
	public static String getBranchByCode = "Select ID From BranchMaster Where BranchCode = ? and merchantid=?";
	public static String getBranchNameByCode = "Select ID From BranchMaster Where branchname=? and BranchCode <> ? and merchantid=?";
	public static String getCompanies = "SELECT Id,companyCode,CompanyName, CreatedBy, CreatedOn From CompanyMaster";

	// Device Registration
	public static String insertDeviceInfo = "Insert into DeviceRegDetails(serialNo,Active,CreatedBy,imeiNo,CreatedOn) Values(?,?,?,?,?)";
	public static String getDeviceSerialNo = "Select ID From DeviceRegDetails Where SerialNo = ? ";
	public static String getDeviceSerialNoById = "Select ID From DeviceRegDetails Where SerialNo = ? and id<>?";
	public static String updateDeviceInfo = "update deviceregdetails set Active=?, CreatedBy=?,  CreatedOn=?, imeiNo=? Where Id=?";
	public static String getDevices = "SELECT Id,serialNo,active, CreatedBy, CreatedOn,imeiNo From DeviceRegDetails";
	public static String getActiveDevices = " select DR.Id,dr.SerialNo,DR.active,DR.createdby from DeviceRegDetails DR where Active=1 ";
	public static String checkUserAssignDevice = "select id from DeviceRegDetails UG where UG.Id in (select DeviceRegId from DeviceIssueDetails UM where um.DeviceRegId=UG.Id and Ug.active=1)and ug.Id=?";

	// Issue Device
	public static String insertIssueDeviceInfo = "Insert into DeviceIssueDetails(DeviceRegId,AgentId,branchid,CreatedBy,CreatedOn,license, imeiNo) Values(?,?,?,?,?,?)";
	public static String updateIssueDeviceInfo = "Update DeviceIssueDetails set DeviceRegId=?, AgentId=?,BranchId=?,CreatedBy=? , CreatedOn=?, imeiNo=? where Id=?";
	public static String getIssueDevices = "select DI.Id,DI.DeviceRegId,dr.SerialNo,DI.AgentId,UM.agentDesc,DI.CreatedBy ,DI.license from DeviceIssueDetails DI, "
			+ " DeviceRegDetails DR,AgentMaster UM where DI.DeviceRegId=dr.Id and DI.AgentId=UM.Id";

	public static String insertIssueDevice = "Insert into DeviceIssueDetails(DeviceRegId,MerchantId,branchid,CreatedBy,CreatedOn,license,posUserId,imeiNo) Values(?,?,?,?,?,?,?,?)";
	public static String updateIssueDevice = "Update DeviceIssueDetails set DeviceRegId=?, MerchantId=?, BranchId=?, CreatedBy=?, CreatedOn=?, imeiNo=? where Id=?";
	public static String getIssuedDevices = "select DI.Id,DI.DeviceRegId,dr.SerialNo,DI.MerchantId,UM.mer_name as merchantName,DI.CreatedBy ,DI.license, DI.posUserId, DR.imeiNo from DeviceIssueDetails DI, "
			+ " DeviceRegDetails DR,mst_merchant UM where DI.DeviceRegId=dr.Id and DI.MerchantId=UM.Id";
	// public static String getIssueDevices =
	// "select
	// DI.Id,DI.DeviceRegId,dr.SerialNo,DI.AgentId,UM.agentDesc,DI.CreatedBy,UM.BranchId,DI.license
	// from DeviceIssueDetails DI, "
	// +
	// " DeviceRegDetails DR,AgentMaster UM,BranchMaster B where
	// DI.DeviceRegId=dr.Id and DI.AgentId=UM.Id AND B.Id=UM.BranchId";
	public static String getUserDeviceSerialNo = "Select ID from DeviceIssueDetails  WHERE AgentId=? and DeviceRegId=? ";
	public static String getDeviceExists = "Select ID from DeviceIssueDetails  WHERE deviceRegId=? ";
	public static String getActiveAgents = "select A.Id,AgentDesc from AgentMaster A where Active=1 and A.Id not in (select AgentId from DeviceIssueDetails DI where DI.AgentId=A.Id and deviceregid<>-1)";

	// Services
	public static String getServices = "SELECT s.Id, ServiceCode, ServiceName, s.Active, s.CreatedBy, s.CreatedOn,componame,compotype,category_id,image,servicedesc FROM ServiceMaster s";
	public static String getHealthServices = "select h.id,ser_code,ser_name,h.active,ser_type,image,h.created_by, h.applicable, (case applicable when '1' then 'All' when '2' then 'Principal and spouse' when '3' then 'Principal female or spouse' when '4' then 'Principal only' else '' end) as applicable_to from mst_health_services h";
	public static String getAKUHHealthSubServices = "SELECT * FROM mst_health_sub_services WHERE mst_serv_id=?";
	public static String getServiceById = "SELECT ServiceCode, ServiceName, Active, CreatedBy,CreatedOn FROM ServiceMaster WHERE ID=?";
	public static String getSubServiceById = "SELECT * FROM SubServiceMap where parentserviceid=? ";
	public static String getServiceByName = "SELECT ID FROM mst_health_services WHERE ser_name=?";
	public static String getServiceNameById = "SELECT ID FROM mst_health_services WHERE ser_name=? and id<>?";
	public static String getServiceNameByCode = "SELECT ID FROM ServiceMaster WHERE ServiceName=? and servicecode=? and id<>?";
	public static String getServiceByCode = "SELECT ID FROM ServiceMaster WHERE ServiceCode=?";
	public static String insertServiceDetails = "INSERT INTO ServiceMaster(ServiceCode, ServiceName,compoType,componame, Active, CreatedBy,CreatedOn,category_id,image,servicedesc) VALUES (?,?,?,?,?,?,?,?,?,?)";
	public static String updateServiceDetails = "UPDATE ServiceMaster SET ServiceName=?,compoType=?,componame=?, Active=?, CreatedBy=?, CreatedOn=?, category_id=?,image=?,servicedesc=?,servicecode=? WHERE ID=?";
	public static String insertSubServiceDetails = "INSERT INTO SubServiceMap(ParentServiceId, SubServiceName,HasChild,level,serviceValue, Active, CreatedBy,CreatedOn) VALUES (?,?,?,?,?,?,?,?)";
	public static String updateSubServiceDetails = "UPDATE SubServiceMap SET serviceValue=?, Active=?, CreatedBy=?,CreatedOn=? WHERE ParentServiceId=?";
	public static String getParams = "SELECT Id, ParamName, Active, CreatedBy,CreatedOn FROM ParamMaster where active=1";
	public static String insertParams = "INSERT INTO ParamDetails(ParamId,serviceId, IsActive, CreatedBy,CreatedOn) Values (?,?,?,?,?)";
	public static String deleteParams = "Delete from ParamDetails where serviceId=?";
	public static String getCurrencies = "select * from mst_currency";
	public static String getUoms = "select * from mst_uom";

	// insert health services
	public static String insertHealthServiceDetails = "insert into mst_health_services (ser_code,ser_name,ser_type,ser_subtype_id,image,active, applicable ,created_by,created_on) values (?,?,?,?,?,?,?,?,?)";
	public static String updateHealthServices = "update mst_health_services set ser_code=?,ser_name=?,ser_type=?,ser_subtype_id=?,image=?,active=?, applicable=?,created_by=?,created_on=? where id=?";
	// Service Types
	public static String getServiceTypes = "select * from mst_service_types";
	public static String getServiceSubTypes = "select * from mst_ser_subtype";

	// Basket
	// Params
	public static String getParamName = "Select ID From ParamMaster Where paramName = ? ";
	public static String insertParamInfo = "Insert into parammaster(paramname,Active,CreatedBy, CreatedOn) Values(?,?,?,?)";
	public static String updateParamInfo = "Update parammaster  Set paramname=?, Active=?, CreatedBy=?, CreatedOn=? Where Id=?";

	public static String getBasketServiceById = "SELECT BM.Id,BasketCode,BM.BasketName,BM.BasketStatus,BM.BasketValue,BD.ServiceId,BD.Quantity,BD.Price,SM.ServiceName"
			+ " FROM BasketMaster BM,BasketDetails BD,ServiceMaster SM "
			+ " WHERE BM.Id=BD.BasketId AND SM.Id=BD.ServiceId AND BM.ID=?";
	public static String getBaskets = " SELECT Id, BasketCode, BasketName, BasketStatus,BasketValue ,CreatedBy, CreatedOn FROM BasketMaster";
	public static String deleteBasketServices = "DELETE FROM BASKETDETAILS WHERE BASKETID=?";
	public static String insertBasketServices = "INSERT INTO BasketDetails(BasketId,ServiceId,Quantity,Price,CreatedBy,CreatedOn)VALUES(?,?,?,?,?,?)";
	public static String insertBaskets = "INSERT INTO BasketMaster(BasketCode,BasketName,BasketStatus,BasketValue,CreatedBy,CreatedOn)VALUES(?,?,?,?,?,?)";
	public static String updateBaskets = "UPDATE BasketMaster SET BasketCode=?,BasketName=?,BasketStatus=?,BasketValue=?,CreatedBy=?,CreatedOn=? WHERE Id=?";
	public static String getBasketByName = "SELECT ID FROM BasketMaster WHERE BasketName=?";

	public static String getUserServices = "select UR.RightId,RightName from UserAssignedRights UR,RightsMaster RM,UserMaster UM,UserGroupsMaster UG "
			+ " where RM.ID=UR.RightId and UG.Id=UM.UserGroupId and UM.Id=? and UR.GroupId=UG.Id and UR.AllowView='true'";

	/*
	 * public static String getMemberDetails =
	 * "SELECT distinct emp_id as id, emp_ref_key as memberno,em.emp_fullname as name,emp_nationalid as nationalid,em.emp_dob as dob,em.emp_gender as gender,em.emp_email as email,em.emp_mobileno as mobileno, "
	 * +
	 * "  em.emp_address as address,em.emp_nhifno as nhif,ac.acc_no,em.product_id "
	 * + " FROM emp_mst em,productmaster pm,emp_acc_dtl ac  " +
	 * " where pm.id=em.product_id and ac.emp_mst_id=em.emp_id " + " union  " +
	 * " select distinct emp_dtl_id as id,ref_sub_key as memberno,ed.alt_name as name,alt_nationalid as nationalid,ed.alt_dob as dob,ed.alt_gender as gender,ed.alt_email as email,ed.alt_mobileno as mobileno, "
	 * + " ed.alt_addres as address,ed.alt_nhifno as nhif,ac.acc_no,em.product_id "
	 * + " from emp_dtl ed ,emp_mst em,emp_acc_dtl ac " +
	 * " where em.emp_id=ed.emp_mst_id and ac.emp_mst_id=em.emp_id ";
	 */
	public static String getMemberDetails = "{call PRC_GetMemberdetails (?)}";
	public static String getMemberDetailsToVerify = "{call PRC_GetMemberdetailsToVerify (?,?,?,?)}";
	public static String getMemberDetailsToApprove = "{call PRC_GetMemberdetailsToApprove (?,?,?,?)}";
	public static String updateMemberStatus = "UPDATE emp_mst SET outpatient_status=? where emp_id=?";
	public static String getCustomerProgrammes = " SELECT CP.ProgrammeId,P.ProgrammeDesc, pm.end_date, P.ProgrammeCode, P.active, P.org_id, pm.product_name, pm.product_code, pm.insurance_code, CP.IsActive, "
			+ "CP.createdby,cp.programmeValue,p.itm_modes,chtm_modes,intm_modes  "
			+ "FROM CustomerProgrammeDetails CP,emp_mst M,ProgrammeMaster P ,productmaster pm   "
			+ "WHERE M.emp_id=CP.CustomerId AND P.Id=CP.ProgrammeId AND CP.CustomerId=? and pm.id=m.product_id and  pm.id=p.productId "
			+ "UNION ALL  "
			+ "SELECT P.Id,P.ProgrammeDesc, P.end_date, P.ProgrammeCode, P.active, P.org_id, pm.product_name, pm.product_code, pm.insurance_code, 0 as IsActive,p.CreatedBy,0 as programmeValue,p.itm_modes,chtm_modes,intm_modes "
			+ "FROM emp_mst M,ProgrammeMaster P,productmaster pm  "
			+ "WHERE P.Id NOT IN (SELECT ProgrammeId FROM CustomerProgrammeDetails A WHERE A.ProgrammeId=P.Id AND A.CustomerId=M.emp_Id) "
			+ "AND M.emp_id=? and pm.id=m.product_id and pm.id=p.productId";
	public static String getDependantCustomerProgrammes = " SELECT CP.ProgrammeId,P.ProgrammeDesc, P.ProgrammeCode, P.org_id, pm.product_name,CP.IsActive, "
			+ "CP.createdby,cp.programmeValue,p.itm_modes,chtm_modes,intm_modes  "
			+ "FROM CustomerProgrammeDetails CP,emp_mst M,ProgrammeMaster P ,productmaster pm   "
			+ "WHERE M.emp_id=CP.CustomerId AND P.Id=CP.ProgrammeId AND CP.CustomerId=? and pm.id=m.product_id and  pm.id=p.productId "
			+ "UNION ALL  "
			+ "SELECT P.Id,P.ProgrammeDesc, P.ProgrammeCode,P.org_id, pm.product_name,0 as IsActive,p.CreatedBy,0 as programmeValue,p.itm_modes,chtm_modes,intm_modes "
			+ "FROM emp_mst M,ProgrammeMaster P,productmaster pm  "
			+ "WHERE P.Id NOT IN (SELECT ProgrammeId FROM CustomerProgrammeDetails A WHERE A.ProgrammeId=P.Id AND A.CustomerId=M.emp_Id) "
			+ "AND M.emp_id=? and pm.id=m.product_id and pm.id=p.productId and member_type = 'D'";
	public static String getMemberProgrammes = " SELECT CP.ProgrammeId,P.ProgrammeDesc,CP.IsActive,pm.product_name,programmecode, "
			+ "CP.createdby,cp.programmeValue,p.itm_modes,chtm_modes,intm_modes,product_id,P.active, pm.end_date  "
			+ "FROM CustomerProgrammeDetails CP,emp_mst M,ProgrammeMaster P ,productmaster pm   "
			+ "WHERE M.emp_id=CP.CustomerId AND P.Id=CP.ProgrammeId AND CP.CustomerId=? and cp.isactive=1 and pm.id=m.product_id and  pm.id=p.productId ";
	// public static String getMemberProgrammes="SELECT
	// CP.ProgrammeId,P.ProgrammeDesc, P.ProgrammeCode, P.org_id,
	// pm.product_name,CP.IsActive, \r\n" +
	// " CP.createdby,cp.programmeValue,p.itm_modes,chtm_modes,intm_modes \r\n" +
	// " FROM CustomerProgrammeDetails CP,emp_mst M,ProgrammeMaster P ,productmaster
	// pm \r\n" +
	// " WHERE M.emp_id=CP.CustomerId AND P.Id=CP.ProgrammeId AND CP.CustomerId=?
	// and pm.id=m.product_id and pm.id=p.productId\r\n" +
	// " UNION ALL \r\n" +
	// " SELECT P.Id,P.ProgrammeDesc, P.ProgrammeCode,P.org_id, pm.product_name,0 as
	// IsActive,p.CreatedBy,0 as
	// programmeValue,p.itm_modes,chtm_modes,intm_modes\r\n" +
	// " FROM emp_mst M,ProgrammeMaster P,productmaster pm\r\n" +
	// " WHERE P.Id NOT IN (SELECT ProgrammeId FROM CustomerProgrammeDetails A WHERE
	// A.ProgrammeId=P.Id AND A.CustomerId=M.emp_Id)\r\n" +
	// " AND M.emp_id=? and pm.id=m.product_id and pm.id=p.productId";
	// ANY_VALUE
	public static String getMemberVouchers = /*
												 * "SELECT ac.basket_Id,voucher_name,voucher_code, ANY_VALUE(basket_balance) as basket_balance, ANY_VALUE(ac.acc_no) as acc_no, "
												 * +
												 * " ANY_VALUE(basket_value) as basket_value, ANY_VALUE(coalesce((basket_value-basket_balance),0)) as used, cover_type "
												 * + " FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed " +
												 * " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? GROUP BY basket_id"
												 * ;
												 */
			"SELECT ac.basket_Id,voucher_name,voucher_code,  MAX(basket_balance) as basket_balance,  MAX(ac.acc_no) as acc_no, "
					+ "  MAX(basket_value) as basket_value,  MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type "
					+ " FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed "
					+ " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? GROUP BY basket_id";
	// ANY_VALUE
	public static String getNewMemberVouchers = /*
												 * "SELECT ac.basket_Id,voucher_name,voucher_code, ANY_VALUE(basket_balance) as basket_balance, ANY_VALUE(ac.acc_no) as acc_no,  ANY_VALUE(basket_value) as basket_value, "
												 * +
												 * " ANY_VALUE(coalesce((basket_value-basket_balance),0)) as used, cover_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed  "
												 * +
												 * " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=2 GROUP BY basket_id"
												 * + " UNION ALL " +
												 * " SELECT ac.basket_Id,voucher_name,voucher_code, ANY_VALUE(basket_balance) as basket_balance, ANY_VALUE(ac.acc_no) as acc_no,  ANY_VALUE(basket_value) as basket_value, "
												 * +
												 * " ANY_VALUE(coalesce((basket_value-basket_balance),0)) as used, cover_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed "
												 * +
												 * " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=1 GROUP BY basket_id"
												 * ;
												 */
			"SELECT ac.basket_Id,voucher_name,voucher_code, MIN(basket_balance) as basket_balance, S.Id as voucherId, active as IsActive, MAX(ac.acc_no) as acc_no,  MAX(basket_value) as basket_value, "
					+ " MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type, scheme_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed  "
					+ " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=(select ProgrammeId from CustomerProgrammeDetails where CustomerId = ? limit 1) and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=2 GROUP BY basket_id"
					+ " UNION ALL "
					+ " SELECT ac.basket_Id,voucher_name,voucher_code, MIN(basket_balance) as basket_balance, S.Id as voucherId, active as IsActive, MAX(ac.acc_no) as acc_no,  MAX(basket_value) as basket_value, "
					+ " MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type, scheme_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed "
					+ " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=(select ProgrammeId from CustomerProgrammeDetails where CustomerId =? limit 1) and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=1 GROUP BY basket_id";

	// public static String getVouchersByMemberNo = "SELECT distinct
	// ac.basket_Id,voucher_name,voucher_code,basket_balance, "
	// + " basket_value, coalesce((basket_value-basket_balance),0) as used,
	// scheme_type "
	// + " FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed "
	// + " WHERE S.Id=ac.basket_Id and ac.card_number in( select card_no from
	// card_mst where emp_mst_id=?) and ed.acc_id=ac.acc_id and ed.emp_mst_id=?";
	public static String getNewMemberVouchers2 =  "SELECT ac.basket_Id,voucher_name,voucher_code, MAX(basket_balance) as basket_balance, MAX(ac.acc_no) as acc_no,  MAX(basket_value) as basket_value, "
            + " MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type, scheme_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed  "
            + " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=2 GROUP BY basket_id"
            + " UNION ALL "
            + " SELECT ac.basket_Id,voucher_name,voucher_code, MAX(basket_balance) as basket_balance, MAX(ac.acc_no) as acc_no,  MAX(basket_value) as basket_value, "
            + " MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type, scheme_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed "
            + " WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=1 GROUP BY basket_id";
	public static String getVouchersByMemberNo = "SELECT distinct ac.basket_Id,voucher_name,voucher_code,basket_balance, \r\n"
			+ "basket_value, cover_type, S.Id as voucherId, active as IsActive, coalesce((basket_value-basket_balance),0) as used, ac.acc_no, scheme_type\r\n"
			+ "FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed\r\n" + "WHERE S.Id=ac.basket_Id \r\n"
			+ "and ac.card_number in( select card_no from card_mst where emp_mst_id=?) \r\n"
			+ "and  ed.acc_id=ac.acc_id and ed.emp_mst_id=?";
	public static String getVouchersByMembershipNo = "SELECT distinct ac.basket_Id,S.Id as voucherId, active as IsActive, "
			+ "voucher_name,voucher_code,ac.basket_balance as basket_balance, basket_value, (basket_value-basket_balance)as used, ac.acc_no, scheme_type, cover_type "
			+ "FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed "
			+ " WHERE S.Id=ac.basket_Id and ac.card_number in( select card_no from card_mst where card_pj=?) and ed.acc_id=ac.acc_id and ed.emp_mst_id=(select emp_id from emp_mst where emp_ref_key=? LIMIT 1)";
	public static String getMemberServiceDtl = "SELECT distinct ac.basket_Id,ser_code,ser_name, cover_limit, require_auth, ac.basket_value,ac.service_id,require_auth, "
			+ "service_value,basket_balance, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - basket_balance), 0) WHEN cover_limit < service_value THEN (CASE WHEN (cover_limit - ((cover_limit) - (cover_limit - basket_balance))) < 0 THEN '0.00' ELSE cover_limit - ((cover_limit) - (cover_limit - basket_balance)) END) ELSE 0 END) AS used, (CASE WHEN cover_limit = service_value THEN COALESCE((service_value - (service_value - basket_balance)),0) WHEN cover_limit = 0 THEN basket_balance WHEN service_value > cover_limit THEN (CASE WHEN (cover_limit - COALESCE((cover_limit - basket_balance), 0)) > cover_limit then cover_limit ELSE (cover_limit - COALESCE((cover_limit - basket_balance), 0)) END) ELSE cover_limit END) AS cover_balance "
			+ "FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv "
			+ "WHERE S.Id=ac.service_Id AND ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id "
			+ "and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=?";
	public static String getCardNumber = "select card_no,bio_status from card_mst where pj_number=? and card_status='I'";
	public static String getMemberBiImages = "select image from customerbioimages where card_number=?";
	public static String getPorgrammesForIssueCard = "select isactive,cp.programmeId,P.ProgrammeDesc,p.createdBY,programmeValue from CustomerProgrammeDetails CP,programmeMaster P where IsActive=1 and CustomerId=? and cp.programmeId=p.id";
	public static String getCustomerBioImages = "select memberId,Image from CustomerBioImages c where memberId=?";
	public static String checkMemberexists = "SELECT emp_Id from emp_mst WHERE emp_ref_key=?";
	public static String insertMemberMaster = "INSERT INTO emp_mst( emp_no,emp_pdc,emp_ref_key,product_id,emp_firstname,emp_lastname,emp_fullname,emp_nationalid,emp_dob,emp_gender,emp_mobileno,emp_email,emp_address,emp_nhifno,active,emp_Pic,org_id,emp_status, Created_By, Created_On, Member_Type, lct_code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertAndMemberMaster = "INSERT INTO M_MemberMaster( MemberNo, Surname, Title, Firstnames, OtherName, CategoryID, RelationID, IdPPNo, Gender, DateOfBirth, MaritalStatus, NhifNo, Height, Weight, EmployerName, Occupation, Nationality, PostalAdd, HomeAdd, HomeTeleNo, OfficeTeleNo, MobileNo, Email,Pic, CreatedBy, CreatedOn,cardstatus,familysize,bnfGrpId,bnfStatus,bioid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateMemberMaster = "UPDATE emp_mst SET emp_firstname=?,emp_lastname=?,emp_fullname=?,emp_nationalid=?,emp_dob=?,emp_gender=?,emp_mobileno=?,emp_email=?,emp_address=?,emp_nhifno=?,active=?,emp_Pic=?, Created_By=?, Created_On=?, Member_Type=?, lct_code=? WHERE emp_id=?";
	public static String deleteCustomerProgramme = "DELETE FROM CustomerProgrammeDetails WHERE CustomerId=?";
	public static String updateAndCustBioId = "update M_MemberMaster set bioid=? WHERE Id=?";
	public static String insertCustomerProgramme = "Insert into CustomerProgrammeDetails (customerid,programmeid,programmeValue,isactive,createdBY,createdOn) values (?,?,?,?,?,?)";
	// public static String getListOfCardsToIssue =
	// "select * from M_MemberMaster where CardStatus='N' ";
	public static String insertCustomerProgramme2 = "Insert into CustomerProgrammeDetails (customerid,programmeid,programmeValue,isactive,createdBY,createdOn) values (?,?,?,?,?,?)";
	public static String getListOfCardsToIssue = "select * from M_MemberMaster M  "
			+ "where CardStatus='N' and bnfstatus='AP'";
	public static String getCustCardsToIssue = "select * from M_MemberMaster M  "
			+ "where CardStatus='N' and mem_type='C'";
	public static String updateCardStatus = "UPDATE M_MemberMaster SET CardStatus=? WHERE ID=? ";
	public static String updateMemStatus = "UPDATE emp_mst SET emp_status=? WHERE emp_ID=? ";
	public static String updateMemFamStatus = "UPDATE emp_dtl SET alt_status=? WHERE emp_dtl_ID=? ";
	public static String updateCardIssu = "UPDATE cardissuance SET Status=? WHERE customerid=? ";
	public static String updateCustomerEnr = "UPDATE M_MemberMaster SET bioId=? WHERE ID=? ";
	public static String insertCustBioImages = "insert into CustomerBioImages(card_number,image,createdOn) values (?,?,?)";
	public static String updateMemBioStatus = "update  card_mst set bio_status=? where card_no=?";

	public static String getCardIssDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd, "
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ " PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, " + "MM.DateOfBirth, MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID  "

			+ "from M_MemberMaster MM  where MM.CardStatus=? and bnfstatus='AP'";

	// + "from M_MemberMaster MM where MM.CardStatus=?";

	public static String updateMemberCardLinkId = "UPDATE CARDISSUANCE SET memberId=? ,status='I' where cardNo=?";
	public static String getListOfCards = "SELECT id,cardNo,accountType FROM CARDISSUANCE WHERE status='N'";
	public static String getListOfCardsToPrint = "select * from CardIssuance a,M_MemberMaster b where a.MemberId=b.ID and a.status='I'";
	public static String insertTransactionDetails = "Insert into TransactionDetail (CardNo, BillNo, TxnValue,RunningBalance,subserviceid,ServiceId,DeviceId,CreatedBy,CreatedOn,ProgrammeId,paymentmethod) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateCardBalance = "update CustomerProgrammeDetails set programmevalue=? where programmeId=? and customerid in(select customerid from cardissuance where cardno=?)";
	public static String insertTransactionParamDetails = "Insert into TransactionParamDetail(txnId,paramId,ParamValue,CreatedBy,CreatedOn) values (?,?,?,?,?)";
	public static String getDashBoardDetail = "Select (select count(emp_id) from emp_mst where active='1') as bnfCount, "
			+ " (select count(id) from usermaster where userTypeId = '2') as systemCount, "
			+ " (select count(id) from usermaster where userTypeId = '1') as posCount, "
			+ " (select count(id) from deviceregdetails where active = '1') as deviceCount";
	public static String getDashBoardDetailCount = "select 'CUSTOMERS' AS NAME,COUNT(M.ID) AS COUNTNO from M_MemberMaster M WHERE M.CARDSTATUS='I' "
			+ "UNION " + " SELECT 'USERS' AS NAME,COUNT(U.ID) AS COUNTNO FROM UserMaster U " + "UNION  "
			+ "SELECT 'REGISTERED DEVICES' AS NAME,COUNT(D.ID) AS COUNTNO FROM  DeviceRegDetails D " + "UNION "

			+ " SELECT 'TRANSACTIONS' AS NAME,COUNT(T.ID) AS COUNTNO FROM  TransactionDetail T " + "UNION "
			+ " select 'ISSUE CARDS' AS NAME,COUNT(M.ID) AS COUNTNO from M_MemberMaster  M WHERE M.CARDSTATUS='I'";

	public static String getAgentTxns = "   select sum(amount_charged_by_retailer) as amount,agentdesc as agent "
			+ "from tms_transaction_dtl td " + "inner join tms_transaction_mst tm on td.trans_mst_id=tm.id "
			+ "inner join usermaster u on u.username=tm.username " + "inner join agentmaster a on a.id=u.agentid "
			+ "group by agentdesc";
	public static String getTransChartDetail = "select CreatedOn as Month, "
			+ "(select COUNT(id) from TransactionDetail where TxnType=1) as Load, (select COUNT(id) from TransactionDetail where TxnType=2) as Purchase,COUNT(id) as totalTrans "
			+ "from TransactionDetail "
			+ "GROUP BY DATEPART(month,TransactionDetail.CreatedOn),DATEPART(Year,CreatedOn)";
	// programme
	public static String getProgrammes = "SELECT prg_type,pm.Id, ProgrammeCode, ProgrammeDesc, pm.Active, pm.CreatedBy, pm.CreatedOn,pm.start_date,pm.end_date,productid,itm_Modes,chtm_Modes,intm_Modes,product_name,cover_type_id "
			+ "FROM ProgrammeMaster pm,productmaster p where p.id=pm.productid and pm.org_id=?";
	public static String getProgrammeById = "SELECT ProgrammeCode, ProgrammeDesc, Active,CreatedBy,CreatedOn FROM ProgrammeMaster WHERE ID=?";
	public static String getProgrammeByName = "SELECT ID FROM ProgrammeMaster WHERE ProgrammeDesc=? and org_id=?";
	public static String getProgrammeByCode = "SELECT ID FROM ProgrammeMaster WHERE ProgrammeCode=? and org_id=?";
	public static String getProgrammeNameByCode = "SELECT ID FROM ProgrammeMaster WHERE programmedesc=? and ProgrammeCode=? and id<>?";
	public static String insertProgrammeDetails = "INSERT INTO ProgrammeMaster(ProgrammeCode, ProgrammeDesc, Active, CreatedBy,CreatedOn,productid,start_date,end_date,itm_modes,chtm_modes,intm_modes,otm_modes,prg_type,org_id,cover_type_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateProgrammeDetails = "UPDATE ProgrammeMaster SET ProgrammeDesc=?, Active=?, CreatedBy=?, CreatedOn=?,start_date=?,end_date=?,itm_modes=?,chtm_modes=?,intm_modes=?,otm_modes=?,productid=? WHERE ID=?";
	public static String getServiceProducts = "SELECT * FROM servicemaster where active=1";
	public static String getActiveHealthService = "SELECT * FROM mst_health_services where active=1 ";
	public static String insertProgrammeVouchers = "INSERT INTO ProgrammevoucherDetails(ProgrammeId, voucherId,service_id,cover_limit,benefit_type_id, IsActive, CreatedBy,CreatedOn,basket_value,require_auth,limit_details) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	public static String deleteProgrammeVouchers = "DELETE FROM ProgrammevoucherDetails WHERE programmeId=?";
	public static String getCoverTypesByScheme = "SELECT * FROM beneficiarygroup where product_id=?";
	// public static String getServicesByProgrammeId =
	// "SELECT PS.ServiceId,S.SubserviceName,ps.IsActive,PS.createdby FROM
	// ProgrammeServiceDetails PS,ProgrammeMaster PM,SubServiceMap S WHERE
	// PM.ID=PS.ProgrammeId AND IsActive=1 AND S.Id=PS.ServiceId AND
	// PS.ProgrammeId=? union all"
	// +
	// " SELECT PS.ServiceId,S.SubserviceName,ps.IsActive,PS.createdby FROM
	// ProgrammeServiceDetails PS,ProgrammeMaster PM,SubServiceMap S WHERE
	// PM.ID=PS.ProgrammeId AND IsActive=0 AND S.Id=PS.ServiceId AND
	// PS.ProgrammeId=?";
	public static String getVouchersByProgrammeId = " SELECT distinct PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,basket_value,frqmode,frq_type "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=? " + "union all "
			+ "SELECT distinct S.Id,S.voucher_name,0 AS IsActive,S.created_by,0 as basket_value,'' as frqmode,'' as frq_type "
			+ " FROM ProgrammeMaster PM,Voucher_mst S "
			+ " WHERE  S.Id NOT IN ( SELECT voucherId FROM ProgrammeVoucherDetails PS WHERE PS.voucherId=S.Id AND PM.ID=PS.ProgrammeId )"
			+ " AND PM.Id=?";
	/*
	 * SELECT PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby
	 * ,voucher_value,frqmode,frq_type,h.ser_name,cover_limit ,benefit_type_id FROM
	 * ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst
	 * S,mst_health_services h WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND
	 * PS.ProgrammeId=12 and h.id=ps.service_id union all SELECT
	 * S.Id,S.voucher_name,0 AS IsActive,S.created_by,0 as voucher_value,'' as
	 * frqmode,'' as frq_type,ser_name,0 as cover_limit, 0 as benefit_type_id FROM
	 * ProgrammeMaster PM,Voucher_mst S ,mst_health_services h,voucher_dtl vd WHERE
	 * S.Id NOT IN ( SELECT voucherId FROM ProgrammeVoucherDetails PS WHERE
	 * PS.voucherId=S.Id AND PM.ID=PS.ProgrammeId ) and h.id=vd.service_id and
	 * vd.voucher_id=s.id AND Pm.Id=12
	 */
	public static String getServicesByBasket = "select pv.service_id,ser_code,ser_name,cover_limit,basket_value,benefit_type_id,require_auth "
			+ "from programmevoucherdetails pv,mst_health_services h " + "where voucherid=? and programmeid=? "
			+ "and h.id=pv.service_id and pv.isactive=1 " + "union "
			+ "select vd.service_id,ser_code,ser_name,0 as cover_limit,0 as basket_value,0 as benefit_type_id,0 as require_auth from voucher_dtl vd,mst_health_services h "
			+ "where vd.voucher_id=? and service_id not in (select service_id from programmevoucherdetails p where programmeid=? and p.voucherId=? and p.isactive=1) "
			+ "and   vd.isactive=1 " + "and h.id=vd.service_id";
	public static String getCashVouchersByProgrammeId = " SELECT PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,voucher_value,frqmode,frq_type "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=? and voucher_type='CA' "
			+ "union all "
			+ "SELECT S.Id,S.voucher_name,0 AS IsActive,S.created_by,0 as voucher_value,'' as frqmode,'' as frq_type "
			+ " FROM ProgrammeMaster PM,Voucher_mst S "
			+ " WHERE  S.Id NOT IN ( SELECT voucherId FROM ProgrammeVoucherDetails PS WHERE PS.voucherId=S.Id AND PM.ID=PS.ProgrammeId ) AND PM.Id=? and voucher_type='CA'";
	// Organization
	public static String insertOrganization = "INSERT INTO mst_organization (org_code,org_name,contact_per,contact_no,email,active,Created_By,Created_On) VALUES (?,?,?,?,?,?,?,?)";
	public static String updateOrganization = "UPDATE mst_organization SET Org_Name = ?,contact_per=?,contact_no=?,email=? ,Created_By = ?,Created_On = ? WHERE ID = ?";
	public static String getOrganizationByName = "SELECT ID  FROM mst_organization WHERE Org_Name = ?";
	public static String getOrganizationById = "SELECT ID FROM mst_organization WHERE ID = ?";
	public static String getOrganizations = "SELECT * FROM mst_organization";
	// Merchant
	public static String insertMerchant = "INSERT INTO mst_merchant (mer_code,mer_name,contact_per,contact_no,email,mer_add,curr_code,Active,Created_By,Created_On,acc_number,acc_name,bank_code,bank_name,logo, has_hmsi, stage_server) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateMerchant = "UPDATE mst_merchant SET mer_code=?,mer_name = ?,contact_per=?,contact_no=?,email=?,mer_add=?,curr_code=?,Active=?,Created_By = ?,Created_On = ?,acc_number=?,acc_name=?,bank_code=?,bank_name=?,logo=?, has_hmsi=?, stage_server=? WHERE ID = ? ";
	public static String getMerchantByName = "SELECT ID FROM mst_merchant WHERE mer_name = ? ";
	public static String getMerchantNameById = "SELECT ID FROM mst_merchant WHERE mer_name = ? and id<>?";
	public static String getMerchantByCode = "select id from mst_merchant where mer_code=?";
	public static String getMerchantNameByCode = "select id from mst_merchant where mer_name=? and mer_code<>?";
	public static String getMerchantById = "SELECT ID FROM mst_merchant WHERE ID = ? ";
	public static String getMerchants = "SELECT * FROM mst_merchant";
	public static String insertMerchantPOSUser = "INSERT INTO merchant_pos_users_mst (username, email, password, merchant_code, active, Created_By,Created_On) VALUES (?,?,?,?,?,?,?)";
	public static String getMerchantPOSUsers = "SELECT Id, username, email, merchant_code, active FROM merchant_pos_users_mst WHERE merchant_code=?";
	public static String getMerchantPOSUsersActive = "SELECT Id, username, email, merchant_code, active FROM merchant_pos_users_mst WHERE merchant_code=?";
	public static String resetMerchantPOSUser = "UPDATE merchant_pos_users_mst SET password=? WHERE email = ?";// AND
																												// merchant_code=?";
	public static String updateMerchantPOSUser = "UPDATE merchant_pos_users_mst SET username=?, email=?, active=? WHERE email=? AND merchant_code=?";
	// public static String loginMerchantPOSUser = "select u.Id from
	// merchant_pos_users_mst u,DeviceIssueDetails di, DeviceRegDetails dr
	// ,mst_merchant m"
	// + " where u.username=? and u.password=? and u.active=1 and
	// u.merchant_code=m.mer_code and di.merchantId=m.Id and di.DeviceRegId=dr.Id";
	public static String loginMerchantPOSUser = "select *from merchant_pos_users_mst pm\r\n"
			+ "inner join mst_merchant mr on pm.merchant_code=concat('SP',mr.id)\r\n"
			+ "			inner join DeviceIssueDetails di on mr.Id=di.merchantId\r\n"
			+ "			inner join DeviceRegDetails dr on di.DeviceRegId=dr.id\r\n"
			+ "			where pm.username=? and password=? and dr.serialno=?\r\n" + "			and dr.active=1";
	// claims
	public static String insertClaim = "INSERT INTO member_trans_mst (scheme, member_no, trans_date, amount, provider_id, bill_no, invoice_number, itemCode, itemName) VALUES (?, ?, ?, ?, 1, ?, ?, ?, ?)";
	// public static String insertClaim ="INSERT INTO mst_claims
	// (insuranceCode,insuranceName,schemeCode,schemeName,chargeDate,receiptNo,patientNo,patientName,membershipNo,itemCode,itemName,transactionType,remarks,itemQuantity,patientAmount,patientDiscount,patientNet,sponsorAmount,sponsorDiscount,sponsorNet)
	// VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String isUserAKUH = "SELECT DISTINCT mm.id FROM usermaster um, mst_merchant mm where mm.id = ? AND mm.mer_name like '%AGA-KHAN UNIVERSITY HOSPITAL%'";
	public static String getClaimantMemberType = "SELECT principle_id, member_type FROM emp_mst WHERE emp_ref_key=?";
	public static String getAKUHPrincipalBasketBalance = "SELECT basket_balance FROM mst_allocation where (acc_no, acc_id) in (SELECT acc_no, acc_id FROM emp_acc_dtl WHERE emp_mst_id="
			+ "(SELECT emp_id FROM emp_mst WHERE emp_ref_key=?))"
			+ "AND service_id=(select mst_serv_id from mst_health_sub_services where service_code=?)";
	public static String getMasterServiceId = "select mst_serv_id from mst_health_sub_services where service_code=?";
	public static String getAKUHPrincipalBasketBalance2 = "SELECT id, basket_balance, service_id FROM mst_allocation where service_id=? and (acc_no, acc_id) in (SELECT acc_no, acc_id FROM emp_acc_dtl WHERE emp_mst_id="
			+ "(SELECT emp_id FROM emp_mst WHERE emp_ref_key=? LIMIT 1))";
	public static String getAKUHPrincipalBasketBalance3 = "SELECT m.id, m.basket_balance, m.service_id "
			+ "FROM mst_allocation as m "
			+ " INNER JOIN (SELECT acc_no, acc_id FROM emp_acc_dtl WHERE emp_mst_id=? LIMIT 1) as ed "
			+ " ON m.acc_no = ed.acc_no " + " where m.service_id= ?";
	public static String updateAllocationAfterClaim = "UPDATE mst_allocation SET basket_balance=? WHERE id=?";
	public static String getVouchersByProgrammeId2 = "SELECT distinct PS.voucherId,S.voucher_name,ps.IsActive,PS.createdby ,basket_value,frqmode,frq_type, cover_type "
			+ " FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ " WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=?";
	public static String getServicesByVoucherId2 = "SELECT * FROM voucher_dtl where voucher_id=?";
	public static String getProgrammeServiceBasketValue = "SELECT * FROM programmevoucherdetails where programmeid=? and service_id=?";
	// services
	public static String insertSubService = "INSERT INTO mst_health_sub_services (service_code, service_name, department, section, mst_serv_id) VALUES (?,?,?,?,?)";
	// members
	public static String getOrganizationId = "SELECT Id from mst_organization WHERE org_name = ?";
	public static String getPrincipleId = "SELECT emp_id from emp_mst WHERE SUBSTRING_INDEX(emp_ref_key, '-', 1) = ? AND member_type='P'";
	public static String insertUpMemberMaster = "INSERT INTO emp_mst( emp_no,emp_pdc,emp_ref_key,principle_id,emp_firstname,emp_lastname,emp_fullname,emp_nationalid,emp_dob,emp_gender,emp_mobileno,emp_email,emp_address,emp_nhifno,active,emp_Pic,org_id,emp_status, Created_By, Created_On, Member_Type, lct_code, relation) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertUpMemberMaster2 = "INSERT INTO emp_mst( emp_no,emp_pdc,emp_ref_key,emp_firstname,emp_lastname,emp_fullname,emp_dob,emp_gender,emp_nhifno,active,org_id, Created_On, Member_Type, lct_code, relation, product_id,created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertUpMemberMaster3 = "INSERT INTO emp_mst( emp_no,emp_pdc,emp_ref_key,emp_firstname,emp_lastname,emp_fullname,emp_dob,emp_gender,emp_nhifno,active,org_id, Created_On, Member_Type, lct_code, relation, principle_id, product_id,created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// Agent
	public static String getAgents = "select a.agent_code,a.AgentDesc,a.Id,a.merchantId,m.Mer_Name,a.active,a.locationid,l.location_name "
			+ "from AgentMaster a, mst_merchant m,locationmaster l  "
			+ "where a.merchantId=m.Id  and a.locationid=l.id ";
	public static String getAgentsByMerchant = "select a.agent_code,a.AgentDesc,a.Id,a.merchantId,m.Mer_Name,a.active,a.locationid,l.location_name "
			+ "from AgentMaster a, mst_merchant m,locationmaster l  "
			+ "where a.merchantId=m.Id  and a.locationid=l.id and m.mer_name=? ";
	public static String getAgentById = "SELECT  AgentDesc,locationId, Active, CreatedBy,CreatedOn FROM AgentMaster WHERE ID=?";
	public static String getAgentByName = "SELECT ID FROM AgentMaster WHERE agentdesc=? and locationId=? and merchantid=?";
	public static String getAgentNameByCode = "SELECT ID FROM AgentMaster WHERE agentdesc=? and agent_code<>? and locationId=?";
	public static String getAgentByCode = "SELECT ID FROM AgentMaster WHERE agent_code=? and locationId=?";
	public static String insertAgentDetails = "INSERT INTO AgentMaster(AgentDesc,merchantId, Active, CreatedBy,CreatedOn,agent_code,locationid) VALUES (?,?,?,?,?,?,?)";
	public static String updateAgentDetails = "UPDATE AgentMaster SET AgentDesc=?,merchantId=? ,Active=?, CreatedBy=?, CreatedOn=?,locationid=? WHERE ID=?";
	public static String getAgentsByLocation = "select id,agentdesc from agentmaster where locationid=? and merchantid=?";

	public static String insertAgentProgramme = "INSERT INTO AgentProgrammeDetails(AgentId,ProgrammeId,IsActive, CreatedBy,CreatedOn,ProgrammeValue,currency) VALUES (?,?,?,?,?,?,?)";
	public static String deleteAgentProgramme = "DELETE FROM AgentProgrammeDetails WHERE agentId=?";
	public static String getProgrammeByAgentId = " SELECT A.ProgrammeId,P.ProgrammeDesc,A.IsActive,A.createdby,A.programmeValue,A.currency  "
			+ "FROM AgentProgrammeDetails A,AgentMaster AM,ProgrammeMaster P  "
			+ "WHERE AM.ID=A.AgentId AND P.Id=A.ProgrammeId AND A.AgentId=? " + "UNION ALL "
			+ "SELECT P.Id,P.ProgrammeDesc,0 AS IsActive,P.createdby, 0 as programmeValue , '' as  currency "
			+ "FROM AgentMaster AM,ProgrammeMaster P "
			+ "WHERE P.Id NOT IN (SELECT ProgrammeId FROM AgentProgrammeDetails A WHERE A.ProgrammeId=P.Id AND A.AgentId=AM.Id) "
			+ "AND AM.Id=? ";
	// UserProgramme

	public static String getProgrammeByUserID = "SELECT AP.ProgrammeId,P.ProgrammeDesc,Ap.programmeValue,ap.currency FROM UserMaster U  "
			+ "INNER JOIN AgentMaster AG ON AG.Id=U.AGENTID "
			+ "INNER JOIN AgentProgrammeDetails AP ON AP.AgentId=U.AGENTID AND AP.AgentId=AG.Id "
			+ "INNER JOIN ProgrammeMaster P ON P.Id=AP.ProgrammeId AND P.ACTIVE=1 " + "WHERE U.Id=? AND AP.ISACTIVE=1";

	public static String getServicesByProgrammes = "SELECT S.Id as parentServiceId,S.ServiceName FROM SERVICEMASTER S,ProgrammeServiceDetails P WHERE P.ServiceId=S.Id AND P.ProgrammeId=? and isactive=1";
	public static String getServiceParams = "select PD.ParamId,PM.ParamName,Isactive from ParamMaster PM,ParamDetails PD "
			+ "WHERE PD.ParamId=PM.Id AND Isactive=1 AND ServiceId=? " + "UNION ALL "
			+ "select PD.ParamId,PM.ParamName,Isactive from ParamMaster PM,ParamDetails PD "
			+ "WHERE PD.ParamId=PM.Id  AND Isactive=0 AND ServiceId=?";
	public static String getActiveParams = "select PD.ParamId,PM.ParamName from ParamMaster PM,ParamDetails PD "
			+ "WHERE PD.ParamId=PM.Id AND Isactive=1 AND ServiceId=? AND PM.ACTIVE=1";

	// wallets
	public static String getWallets = "SELECT * FROM wallet";
	public static String insertWalletDetails = "INSERT INTO WalletDetails(walletId,orgId,isactive,CreatedBy,CreatedOn) VALUES (?,?,?,?,?)";
	public static String deleteWalletDetails = "delete from WalletDetails where orgId=?";
	// public static String getOrgWallets =
	// "select * from walletDetails WD, wallet W where isactive=1 and
	// w.wid=wd.walletid and orgId=?";
	public static String getOrgWallets = "select * from wallet W ";

	// Bin Allocation
	public static String checkBinRangeProgramme = "SELECT ID FROM BinMaster WHERE programmeId=? ";
	public static String checkBinRange = "SELECT ID FROM BinMaster WHERE binrange=? ";
	public static String insertBinRange = "INSERT INTO BinMaster(binrange,programmeid,createdby,createdon) values (?,?,?,?) ";

	// Card Issuance
	public static String insertCardIssuance = "Insert Into CardIssuance(CardNo, SerialNO, IssueDate, ExpiryDate, status, CustomerId,ProgrammeId, CreatedBy, CreatedOn,cardpin) values (?,?,?,?,?,?,?,?,?,?)";
	public static String updateCardIssuance = "update CardIssuance set CardNo=?, SerialNO=?, IssueDate=?, ExpiryDate=?, status=?,modifiedBy=?, modifiedOn=?,cardpin=? where customerid=?";
	public static String checkCardNumber = "SELECT Id from cardIssuance WHERE cardno=?";
	public static String getBinByProgrammeId = "Select Binrange from BinMaster where ProgrammeId=?";
	public static String getLoadWalletCardDetails = "SELECT CI.LoadAmount,CI.Balance,CI.programmeId,pm.ProgrammeDesc,cp.ProgrammeValue FROM CardIssuance CI ,ProgrammeMaster PM,CustomerProgrammeDetails CP WHERE CardNo=? AND PM.Id=CI.ProgrammeId AND CP.ProgrammeId=PM.Id AND CP.ProgrammeId=CI.ProgrammeId";
	public static String updateCardLoad = "Update CardIssuance set balance=?, loadAmount=? where cardNo=?";
	public static String insertCardReIssueLog = "insert into cardreissuance_log(customerid,cardno,serialno,pinno,createdby,createdon) values (?,?,?,?,?,?)";

	// customer enroll
	public static String verifyCustomerEnroll = "SELECT id FROM M_Membermaster WHERE bioid=?";
	public static String getProgrammeValue = "select ProgrammeValue,cp.ProgrammeId,ProgrammeDesc from CardIssuance c,CustomerProgrammeDetails cp,programmemaster pm where CardNo=? and pm.id=cp.programmeid and cp.CustomerId=c.CustomerId and IsActive=1";
	public static String getBioMemberDetails = "select DISTINCT MM.ID,MM.MemberNo,MM.FirstNames,MM.Surname,MM.Title,MM.OtherName, "
			+ "MM.IdPPNo,MM.Gender,MM.MaritalStatus,MM.NhifNo,MM.Height,MM.Weight,MM.EmployerName, NokPostalAdd, "
			+ "MM.Occupation,MM.Nationality,MM.PostalAdd, "
			+ "MM.HomeAdd,MM.HomeTeleNo,MM.OfficeTeleNo,MM.MobileNo,MM.Email,MM.NokName,MM.NokIdPPNo,MM.NokPhoneNo,MM.PostalAdd, "
			+ " PIC, MM.Firstnames+' '+MM.Surname AS FULLNAME, " + "MM.DateOfBirth,MM.BIOID, "
			+ "MM.RelationID,MM.CategoryID  " + "from M_MemberMaster MM "

			+ "WHERE MM.bioID=? ";
	public static String getAmountDetails = "select 'ISSUE CARDS' AS NAME,COUNT(M.ID) AS COUNTNO from M_MemberMaster  M WHERE M.CARDSTATUS='A' \n"
			+ "UNION select 'TOTAL AMOUNT' AS NAME,SUM(CAST(TxnValue as DECIMAL(15,4))) AS COUNTNO from TransactionDetail  M \n"
			+ "UNION \n"
			+ "select 'NET AMOUNT' AS NAME,SUM(CAST(TxnValue as DECIMAL(15,4))) AS COUNTNO from TransactionDetail  M  WHERE paymentmethod='card' \n"
			+ "UNION \n"
			+ "select 'VOIDED AMOUNT' AS NAME,SUM(CAST(TxnValue as DECIMAL(15,4))) AS COUNTNO from TransactionDetail  M   ";

	public static String insertSafComTopUp = "update cardissuance set balance=balance+? where cardno=? ";
	public static String insertSafCom = "update cardissuance set balance=balance-? where cardno=? ";
	public static String insertSafComTopTxn = "Insert into TransactionDetail (CardNo,TxnValue,DeviceId,CreatedBy,CreatedOn,TXNTYPE,oldTxnId,billno,accno,channel,utility) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static String checkAgentPin = "select * from usermaster where id=? and agentpin=?";
	public static String checkCardPin = "select * from cardissuance where cardno=? and cardpin=?";
	public static String getCustName = "select Surname+ ' ' +Firstnames as name from M_MemberMaster m ,CardIssuance c where c.CustomerId=m.ID and CardNo=? ";
	public static String getCustomerId = "select id,Surname+ ' ' +Firstnames as name from m_membermaster where id=?";
	// public static String
	// checkCardBalance="select * from cardissuance where cardno=? and cardpin=?";
	public static String getAllSafComTxns = "SELECT top 5 CASE TxnType WHEN '1' THEN 'LOAD' WHEN '2' THEN 'SALE/WITHDRAWAL' WHEN '3' THEN 'REVERSE' WHEN '4' THEN 'UTILITY TXN' END as txntype, t.CardNo,TxnValue,t.DeviceId,t.CreatedOn,ifnull(utility,'NA') as utility ,u.UserFullName,case channel when 'account' then 'Account No' else 'Card' end as channel_type FROM TransactionDetail T, CardIssuance c,usermaster u where u.id=t.createdby and c.CardNo=t.CardNo and t.CardNo=? order by t.id desc";
	public static String getPOSVersion = "select value from ConfigurationParam where code='013' ";
	public static String getFTPUrl = "select value from ConfigurationParam where code='014' ";
	public static String getReverseTxn = "select txnvalue from TransactionDetail where id=? and txntype in (1,2)";
	public static String checkCard = "select * from cardissuance where cardno=? and status='A'";
	public static String changePin = "update cardissuance set cardpin=? where  cardno=? and cardpin=?";
	public static String blockCard = "update cardissuance set Status='B' where  cardno=? ";
	public static String unblockCard = "update cardissuance set Status='A' where  cardno=? ";
	public static String getAgentList = "select * from agentmaster";
	public static String getDeviceList = "select * from deviceregdetails";
	public static String validateDeviceLicense = "select i.license from deviceissuedetails I,deviceregdetails D where  i.deviceregid=d.id and d.serialno=? and license=?";
	public static String getCardBalance = "SELECT C.Card_no, C.acc_id FROM card_mst C  WHERE pj_number=?";
	public static String getLicense = "select * from licensemanager";

	// Zone
	public static String insertZone = "INSERT INTO zoneMaster (Zone_code,Zone_Name,Active,Created_By,Created_On) VALUES (?,?,?,?,?)";
	public static String updateZone = "UPDATE zoneMaster SET Zone_Name = ? ,Active=?,Created_By = ?,Created_On = ? WHERE ID = ?";
	public static String getZoneByName = "SELECT ID,Zone_Name,Created_By,Created_On FROM zoneMaster WHERE Zone_Name = ? ";
	public static String getZones = "SELECT ID,zone_code,Zone_Name,org_Id,Active,Created_By,Created_On FROM zoneMaster";
	public static String getActiveZones = "SELECT ID,zone_code,Zone_Name,org_Id,Active,Created_By,Created_On FROM zoneMaster where active=1";
	public static String getZoneByCode = "SELECT ID,Zone_Name,Created_By,Created_On FROM zoneMaster WHERE Zone_code = ? ";
	public static String getZoneNameByCode = "SELECT ID,Zone_Name,Created_By,Created_On FROM zoneMaster WHERE Zone_Name = ? and zone_code<>?";

	// area
	public static String insertArea = "INSERT INTO areaMaster (area_code,area_Name,zone_id,Active,Created_By,Created_On) VALUES (?,?,?,?,?,?)";
	public static String updateArea = "UPDATE areaMaster SET area_Name=?,zone_id=?,Active=?,Created_By = ?,Created_On = ? WHERE ID = ? ";
	public static String getAreaByName = "SELECT ID,area_code,Created_By,Created_On FROM areaMaster WHERE area_Name = ?  and zone_id=?";
	public static String getAreas = "SELECT a.ID,area_code,area_name,org_Id,zone_id,zone_name,a.Active,a.Created_By,a.Created_On "
			+ "FROM areaMaster a,zonemaster z where a.zone_id=z.id ";
	public static String getAreaByCode = "select id from areamaster where area_code=? and zone_id=?";
	public static String getAreaNameByCode = "select id from areamaster where area_name=? and area_code<>? and zone_id=?";

	// Location
	public static String getLocations = "select m.id,location_code,location_name,area_id,m.active,w.zone_id,"
			+ " case when m.active=0 then 'Inactive' else 'Active' end as status, "
			+ "w.area_name,s.zone_name from areaMaster w, locationmaster m,zonemaster s"
			+ " where w.id=m.area_id and s.id=w.zone_id";
	public static String getActiveArea = "select a.id,area_name from areamaster a , zonemaster z  where a.active=1 and z.id=a.zone_id ";
	public static String getAreaByZone = "select id,area_name from areamaster where active=1 and zone_id=?";
	public static String checkLocationName = "select id from locationmaster where location_name=? and area_id=?";
	public static String checkLocationCode = "select id from locationmaster where location_code=? and area_id=?";
	public static String checkLocationNameByCode = "select id from locationmaster where location_name=? and location_code<>? and area_id=?";
	public static String insertLocation = "insert into locationmaster(location_code,location_name,area_id,active,created_by,created_on) values (?,?,?,?,?,?)";
	public static String updateLocation = "update locationmaster set location_name=?,area_id=?,active=?,created_by=?,created_on=?  where id=?";
	// Product
	public static String insertProduct = "INSERT INTO productMaster (product_Code,product_Name,start_date,end_date,fund,org_id,type,Active,Created_By,Created_On,Insurance_Code) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateProduct = "UPDATE productMaster SET product_Code=?,product_Name=?,start_date=?,end_date=?,fund=?,type=?,Active=?,Created_By=?,Created_On=?,Insurance_Code=? WHERE ID = ? ";
	public static String getProductByName = "SELECT ID,product_name,Created_By,Created_On FROM productMaster WHERE product_Name = ?  and org_id=?";
	public static String getProductByCode = "SELECT ID,product_name,Created_By,Created_On FROM productMaster WHERE product_Code = ?  and org_id=?";
	public static String getProductNameByCode = "SELECT ID,product_name,Created_By,Created_On FROM productMaster WHERE product_Name = ? and product_code=? and org_id=? and id<>?";
	public static String getProductByProductName = "SELECT * FROM productMaster WHERE product_Name = ?";
	public static String getProducts = "SELECT p.ID,product_code,product_name,start_date,end_date,fund, type,p.Active,p.Created_By,p.Created_On, p.Insurance_Code, "
			+ " (case type when 'F' then 'Fund deposit' when 'I' then 'Insured premium' else '' end) as type_name,"
			+ "(case Active when 1 then 'Active' when 0 then 'Inactive' else '' end) as status "
			+ "FROM productMaster p where  org_id=?";
	// voucher
	public static String insertVoucher = "insert into voucher_mst(voucher_code, voucher_name, voucher_type, voucher_value, cover_type, active, created_by, created_on,scheme_type) values (?,?,?,?,?,?,?,?,?)";
	public static String updateVoucher = "update voucher_mst set voucher_name=?, voucher_type=?, voucher_value=?, cover_type=?, active=?, created_by=?, created_on=?, voucher_code=?, scheme_type=? where id=?";
	public static String insertVoucherServices = "insert into voucher_dtl(voucher_id, service_id, quantity, service_value, isactive, created_by, created_on) values (?,?,?,?,?,?,?)";
	public static String deleteVoucherServices = "delete from voucher_dtl where voucher_id=?";
	public static String getVouchers = "select id, voucher_code, voucher_name, voucher_type, voucher_value, cover_type, "
			+ "active, created_by, created_on,start_date,end_date, scheme_type from voucher_mst"; // benefit baskets no
																									// longer
																									// organization
																									// specific
	public static String getVouchersByOrg = "select id, voucher_code, voucher_name, voucher_type, voucher_value, cover_type, "
			+ "active, created_by, created_on,start_date,end_date, scheme_type from voucher_ms where org_id=?";
	public static String getCashVouchers = "select id, voucher_code, voucher_name," + "  voucher_type, voucher_value, "
			+ "active, created_by, created_on,start_date,end_date from voucher_mst where voucher_type='CA'";
	public static String getServicesByVoucherId = " SELECT PS.Service_Id,S.serviceName,ps.IsActive,PS.created_by,quantity,service_value,compoName,service_type as type  "
			+ " FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND PS.voucher_Id=? and s.CompoType=ps.service_type "
			+ " union all "
			+ "SELECT S.Id,S.serviceName,0 AS IsActive,S.createdby,0 as quantity,0 as service_value,componame,s.CompoType as type "
			+ "FROM voucher_mst PM,ServiceMaster S " + "WHERE  S.Id NOT IN ( SELECT Service_Id FROM voucher_dtl PS) "
			+ "AND PM.Id=? and s.CompoType in (select service_type from Voucher_dtl)  ";

	public static String getHealthServiceByid = " SELECT PS.Service_Id,S.ser_Name,ps.IsActive,quantity "
			+ ",service_value,ser_subtype_id,service_type as type ,ps.Created_BY "
			+ " FROM voucher_dtl PS,voucher_mst PM,mst_health_services S "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND " + " PS.voucher_Id=? " + "union all "
			+ "SELECT S.Id,S.ser_Name,0 AS IsActive, "
			+ "  0 as quantity,0 as service_value,ser_subtype_id,s.ser_type as type ,s.Created_BY "
			+ "FROM voucher_mst PM,mst_health_services S "
			+ "WHERE  S.Id NOT IN ( SELECT Service_Id FROM voucher_dtl PS where voucher_id=?) " + "AND PM.Id=? ";
	public static String getServicesPrd = " SELECT PS.Service_Id,S.serviceName,ps.IsActive,PS.created_by,quantity,service_value,compoName,service_type as type  "
			+ " FROM voucher_dtl PS,voucher_mst PM,ServiceMaster S "
			+ " WHERE PM.ID=PS.voucher_Id AND S.Id=PS.Service_Id AND PS.service_type=? and s.CompoType=ps.service_type "
			+ " union all "
			+ "SELECT S.Id,S.serviceName,0 AS IsActive,S.createdby,0 as quantity,0 as service_value,componame,s.CompoType as type "
			+ "FROM voucher_mst PM,ServiceMaster S " + "WHERE  S.Id NOT IN ( SELECT Service_Id FROM voucher_dtl PS) "
			+ "AND s.compotype=? and s.CompoType in (select service_type from Voucher_dtl)  ";
	public static String getVoucherByName = "select id from voucher_mst where voucher_name=?";
	public static String getVoucherByCode = "select id from voucher_mst where voucher_code=?";
	public static String getVoucherNameByCode = "select id from voucher_mst where voucher_name=? and voucher_code=? and id<>?";
	// beneficiary group
	public static String insertBnfGrp = "INSERT INTO BeneficiaryGroup (bnfgrp_code, bnfgrp_name, product_id, active, created_by, created_at,house_hold_value,min_cap,max_cap,org_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
	public static String updateBnfGrp = "UPDATE BeneficiaryGroup SET bnfgrp_name=?,product_id=?,Active=?,Created_By = ?,Created_at = ?,house_hold_value=?,min_cap=?,max_cap=?, bnfgrp_code = ? WHERE ID = ? ";
	// public static String getProByName =
	// "SELECT ID,productMaster,Created_By,Created_On FROM productMaster WHERE
	// product_Name = ? and zone_id=?";
	public static String getBnfGrps = "SELECT p.ID,bnfgrp_code,bnfgrp_name,product_id,product_name,p.Active,p.Created_By,p.Created_at,house_hold_value,min_cap,max_cap "
			+ "FROM BeneficiaryGroup p,productmaster m where p.product_id=m.id and p.org_id=?";
	public static String getBnfGrpByCode = "select id from BeneficiaryGroup where bnfgrp_code=? and product_id=?";
	public static String getBnfGrpByName = "select id from BeneficiaryGroup where bnfgrp_name=? and product_id=?";
	public static String getBnfGrpNameByCode = "select id from BeneficiaryGroup where bnfgrp_name=? and bnfgrp_code=? and product_id=? and id<>?";
	public static String getProgrammesByBnfGrp = "SELECT p.Id, ProgrammeCode, ProgrammeDesc, p.Active, p.CreatedBy, p.CreatedOn, "
			+ "p.start_date,p.end_date,productid,itm_Modes,chtm_Modes,intm_Modes "
			+ "FROM ProgrammeMaster p ,productmaster b " + "where b.id=p.productid and b.id=?";
	public static String getRelations = "SELECT * FROM RELATIONMASTER";
	public static String getBeneficiaryFamilyMembers = "SELECT ed.emp_dtl_id,ed.alt_gender, relation, emp_mst_id, ed.alt_name,ed.ref_sub_key,ed.alt_dob "
			+ "FROM emp_dtl ed,emp_mst em " + "WHERE em.emp_id=ed.emp_mst_id AND emp_mst_id=?";
	public static String getFamilyMembersToVerify = "SELECT ed.emp_dtl_id, emp_mst_id, ed.alt_name,ed.ref_sub_key,ed.alt_dob,"
			+ " case alt_status when 'V' then 'Verified' when 'N' then 'Not Verified' end as verify_status "
			+ "FROM emp_dtl ed,emp_mst em " + "WHERE em.emp_id=ed.emp_mst_id AND emp_mst_id=? and alt_status=? ";
	public static String getFamilyMembersToApprove = "SELECT ed.emp_dtl_id, emp_mst_id, ed.alt_name,ed.ref_sub_key,ed.alt_dob,"
			+ " case alt_status when 'V' then 'Not Approved' when 'A' then 'Approved' end as approve_status "
			+ "FROM emp_dtl ed,emp_mst em " + "WHERE em.emp_id=ed.emp_mst_id AND emp_mst_id=? and alt_status=? ";
	public static String insertMemberFamilyDetails = "INSERT INTO emp_dtl(emp_mst_id, emp_no, emp_jdc, ref_sub_key,alt_name,alt_dob,alt_gender, relation, Created_By, Created_On) VALUES (?,?,?,?,?,?,?,?,?,?)";
	public static String deleteMemberFamilyDetails = "DELETE FROM emp_dtl WHERE emp_mst_id=?";
	public static String getBranchesByMerchant = "select * from BranchMaster where MerchantId=?";
	public static String getNoOfBnfs = "select count(id) as id from m_membermaster where bnfgrpid=?";
	public static String getLocationByMerchant = "select l.location_name,l.id from mst_merchant m,branchmaster b,locationmaster l"
			+ "  where m.id=b.merchantid and l.id=b.locationid and m.id=?";
	public static String insertMemberAccDtl = "insert into emp_acc_dtl(emp_mst_id,acc_no,acc_type,acc_balance,created_by,created_on,alt_status) values (?,?,?,?,?,?,?)";
	public static String getMaxMemberAccount = "select max(acc_no) as maxaccno from emp_acc_dtl";

	/*
	 * public static String getAgentsByMerchant =
	 * "select a.agent_code,a.AgentDesc,a.Id,a.merchantId,m.MerchantName,a.active,a.locationid,l.location_name "
	 * + "from AgentMaster a, mst_merchant m,locationmaster l  " +
	 * "where a.merchantId=m.Id  and a.locationid=l.id and m.merchantname=? ";
	 */
	// TmsDownloads
	public static String getTmsUserDownloads = "select u.id,UserName,UserFullName,UserPassword,UserGroupId,pos_pin,pos_User_Level "
			+ "from UserMaster u ,AgentMaster a,DeviceIssueDetails di,DeviceRegDetails d "
			+ "where u.AgentId=a.Id and a.Id=di.AgentId and d.Id=di.DeviceRegId and d.SerialNo=? and usertypeid=1 ";
	public static String getTmsRetailerDownload = "select AgentDesc,location_name "
			+ "from AgentMaster a,BranchMaster b,LocationMaster l,DeviceIssueDetails di,DeviceRegDetails d "
			+ "where a.branchid=b.id and l.Id=b.locationid and d.Id=di.DeviceRegId and di.AgentId=a.Id and d.SerialNo=? ";

	public static String insertTmsTransMst = "insert into member_trans_mst(member_no, card_serial, bill_no, scheme_id, device_id, total_value_remaining, amount, trans_date,created_by,created_on,acc_no,programme_id,trans_status,paid_status, provider_id, invoice_number) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertTmsTransDtl = "insert into member_trans_dtl(Trans_mst_id, service_id, basket_id, txn_amount, value_remaining,trans_date,trans_status, created_by, paid_status, image, created_on) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateBasketBalance = "update mst_allocation set basket_balance=? where acc_no=? and basket_id=? and programme_id=?";
	public static String updateServiceBalance = "update mst_allocation set service_balance=? where acc_no=? and basket_id=? and service_id=? and programme_id=?";
	public static String getAuthPendingService = "select md.trans_status,ser_code,md.service_id as serviceId from member_trans_dtl md,member_trans_mst m,mst_health_services h where  m.id=md.trans_mst_id and bill_no=? and service_id=? and h.id=md.service_id ";

	public static String getVoucherDetailsByProgramme = "select distinct p.id as programmeId,p.ProgrammeCode,p.ProgrammeDesc,\n"
			+ "vd.voucher_id,vd.service_id,vd.quantity,vd.service_value,\n"
			+ "b.id as bgId,c.CustomerId as beneficiaryId, sm.CompoName as uom,ci.CardNo as cardNumber,vm.voucher_type\n"
			+ "from BeneficiaryGroup b ,ProgrammeMaster p,\n"
			+ "CustomerProgrammeDetails c,ProgrammeVoucherDetails pv,\n"
			+ "Voucher_dtl vd,ServiceMaster sm,CardIssuance ci,Voucher_mst vm\n"
			+ " where b.product_id=p.productId and c.ProgrammeId=p.Id and c.IsActive=1 \n"
			+ " and pv.ProgrammeId=p.Id and vd.voucher_id=pv.voucherId and sm.Id=vd.service_id and ci.CustomerId=c.CustomerId\n"
			+ "   and vm.id = vd.voucher_id and p.Id in(?)";

	public static String insert_topup = "INSERT INTO mst_topup(beneficiary_group_id,programme_id,beneficiary_id,card_number,voucher_id,item_id,item_value,item_quantity,uom,downloaded,created_on,cycle,voucher_id_number,topup_Status,voucher_type,agent_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updBnfGrpTopupCount = "update beneficiarygroup set topup_count=topup_count+? where id=?";

	// For voucher Topup
	public static String getBnfGrpForTopup = "select max_cap,min_cap,bnfgrp_code,b.id,b.bnfgrp_name,b.house_hold_value,COUNT(m.id) as no_of_bnfs "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id "
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap";
	public static String getTopupedBnfGrps = "select max_cap,min_cap,bnfgrp_code,b.id,b.bnfgrp_name,b.house_hold_value,COUNT(m.id) as no_of_bnfs "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id and b.id in (select beneficiary_group_id from mst_topup)"
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap";
	public static String getBnfGrpByPrgId = "select max_cap,min_cap,bnfgrp_code,b.id,b.bnfgrp_name,b.house_hold_value,COUNT(m.id) as no_of_bnfs "
			+ " from BeneficiaryGroup b,ProductMaster p,ProgrammeMaster pm,M_MemberMaster m "
			+ " where p.id=b.product_id and pm.productId=p.id and m.bnfGrpId=b.id and pm.id=? "
			+ " group by  b.id,b.bnfgrp_name,b.house_hold_value,bnfgrp_code, max_cap,min_cap";
	// public static String getLastTopupDate =
	// "select top 1 ifnull(created_on,'') as last_topup from mst_topup where
	// beneficiary_group_id=? order by created_on desc";
	public static String getLastTopupDate = "select ifnull(created_on,'') as last_topup, case topup_status when 'N' then 'Pending Approval' else 'Approved' end as topupstatus from mst_topup where beneficiary_group_id=? order by created_on desc limit 1";
	public static String getBnfGrpHHDetails = "  select distinct vm.voucher_value,m.familysize,voucher_type "
			+ " from M_MemberMaster m ,BeneficiaryGroup b,"
			+ " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm"
			+ " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1 "
			+ " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type in ('VA','CA')";
	public static String getBnfGrpHHDetailsTopuped = "  select distinct vm.voucher_value,m.familysize,voucher_type "
			+ " from M_MemberMaster m ,BeneficiaryGroup b,"
			+ " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm"
			+ " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.IsActive=1  and b.id in(select beneficiary_group_id from mst_topup) "
			+ " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type in ('VA','CA')";
	public static String getBnfHHDetails = "select distinct vm.voucher_value,m.familysize,b.min_cap,b.max_cap "
			+ " from M_MemberMaster m ,BeneficiaryGroup b,"
			+ " ProductMaster p,ProgrammeMaster pm,ProgrammeVoucherDetails pv,Voucher_mst vm"
			+ " where b.id=m.bnfGrpId and p.id=b.product_id and pm.productId=p.id and pv.isactive=1 "
			+ " and pv.ProgrammeId=pm.Id and vm.id=pv.voucherId and m.Id=? and voucher_type=? and pm.id=?";
	public static String updateHouseHoldValue = "update BeneficiaryGroup set old_value=house_hold_value, house_hold_value=? where id=?";
	public static String getTopupDetailsToApprove = "{call PRC_GetTopupDetails (?,?,?,?)}";
	public static String getBeneficiaryCount = "select COUNT(id) bnfCount from M_MemberMaster m where bnfGrpId=? ";
	public static String getBeneficiaryId = "select m.id from M_MemberMaster m where bnfGrpId=?";

	// category
	public static String insertCategory = "insert into mst_category(category_code, category_name, active, created_by, created_on) values (?,?,?,?,?)";
	public static String updateCategory = "UPDATE mst_category SET category_name = ? ,Active=?,Created_By = ?,Created_On = ? WHERE ID = ? ";
	public static String getCategoryByName = "SELECT ID,category_name,Created_By,Created_On FROM mst_category WHERE category_name = ? ";
	public static String getCategoryByCode = "select id from mst_category where category_code=?";
	public static String getCategoryNameByCode = "select id from mst_category where category_name=? and category_code<>?";
	public static String getCategoryById = "SELECT ID,category_name,Active,Created_By,Created_On FROM mst_category WHERE ID = ? ";
	public static String getCategories = "SELECT ID,category_name,category_code,Active,Created_By,Created_On FROM mst_category ";

	// download data for android device
	public static String checkDeviceRegistered = "select * from DeviceRegDetails where serialno=?";
	public static String checkDeviceIssued = "select * from DeviceIssueDetails where deviceregid=?";
	public static String getAndUsers = "select u.Id,UserName,UserFullName,UserPassword,pos_user_level,m.mer_name from UserMaster u,DeviceRegDetails dr,DeviceIssueDetails di ,mst_merchant m where UserTypeId=1  and di.merchantId=u.merchantId and di.DeviceRegId=dr.Id and u.merchantid=m.id and u.active=1 and dr.SerialNo = ?";
	public static String getAndCategories = "select * from mst_category where active=1";
	public static String getAndBnfGrps = "select * from BeneficiaryGroup where active=1";
	public static String getAndServices = "SELECT id,ser_code,ser_name FROM mst_health_services where Active=1";
	public static String getAndVouchers = "SELECT id,voucher_code,voucher_name FROM voucher_mst where Active=1";
	public static String getAndroidProgrammes = "SELECT distinct prg_type,pm.Id, ProgrammeCode, ProgrammeDesc, pm.Active, pm.CreatedBy,pm.productId, "
			+ "pm.CreatedOn,pm.start_date,pm.end_date,productid,itm_Modes,chtm_Modes,intm_Modes,product_name "
			+ "FROM ProgrammeMaster pm,productmaster p " + "where p.id=pm.productid ";
	public static String getAndroidVouchers = " SELECT distinct PS.voucherId,voucher_code," + "basket_value "
			+ "FROM ProgrammeVoucherDetails PS,ProgrammeMaster PM,Voucher_mst S "
			+ "WHERE PM.ID=PS.ProgrammeId AND S.Id=PS.voucherId AND PS.ProgrammeId=?  and ps.isactive=1";
	public static String getAndroidProducts = " select pv.service_id,cover_limit,require_auth,ser_code from programmevoucherdetails pv,mst_health_services h where voucherid=? and programmeid=? and h.id=pv.service_id ";
	public static String getAndroidTopup = "select cycle,mt.id as topupid,mt.programme_id,mt.beneficiary_id,mt.card_number,beneficiary_group_id, "
			+ "mt.voucher_id,mt.item_id,mt.item_value,mt.item_quantity,mt.uom,mt.voucher_type,voucher_id_number "
			+ "from mst_topup mt,ProgrammeMaster pm,ProductMaster p, "
			+ "mst_merchant m,AgentMaster a,DeviceIssueDetails di,DeviceRegDetails dr "
			+ "where pm.Id=mt.programme_id and p.id=pm.productId "
			+ "and m.Id=p.merchant_id and a.merchantId=m.Id and di.AgentId=a.Id "
			+ "and dr.Id=di.DeviceRegId and dr.SerialNo=? and mt.agent_id=di.agentid and topup_status='A' and downloaded=0 ";
	// public static String
	// updateAndDwonloadStatus="update mst_topup set downloaded=1 where id=? and
	// topup_status='A'";
	public static String getFlowChartDetails = "select sm.ServiceName as service,count(bm.id) as trans_count  from Servicemaster sm inner join tms_transaction_dtl bm on bm.pos_commodity = sm.Id GROUP BY sm.ServiceName order by trans_count desc";
	public static String getTotalCollectionInfo =
			// "select ifnull(sum(a.value),0) as collection,'Value Voucher Collections' as
			// name from (select sum(amount_charged_by_retailer) value,m.voucher_type "
			// + "from tms_transaction_dtl td "
			// + "inner join tms_transaction_mst tm on tm.Id=td.Trans_mst_id "
			// + "left join mst_topup m on m.voucher_id_number=tm.voucher and "
			// + "m.voucher_type='VA' and m.item_id=td.pos_commodity "
			// +
			// "group by m.voucher_type ,pos_commodity,deducted_quantity)a where
			// a.voucher_type is not null "
			// + "union "
			// +
			// "select ifnull(sum(a.value),0) as collection,'Commodity Voucher Collections'
			// as name from (select sum(amount_charged_by_retailer) value,m.voucher_type "
			// + "from tms_transaction_dtl td "
			// + "inner join tms_transaction_mst tm on tm.Id=td.Trans_mst_id "
			// + "left join mst_topup m on m.voucher_id_number=tm.voucher and "
			// + "m.voucher_type='CM' and m.item_id=td.pos_commodity "
			// +
			// "group by m.voucher_type ,pos_commodity,deducted_quantity)a where
			// a.voucher_type is not null "
			"select sum(amount_charged_by_retailer) collection,'Value Voucher Collections' as name from tms_transaction_dtl td ";
	// + "union "
	// +
	// "select ifnull(sum(a.value),0) as collection,'Cash Voucher Collections' as
	// name from (select sum(amount_charged_by_retailer) value,m.voucher_type "
	// + "from tms_transaction_dtl td "
	// + "inner join tms_transaction_mst tm on tm.Id=td.Trans_mst_id "
	// + "left join mst_topup m on m.voucher_id_number=tm.voucher and "
	// + "m.voucher_type='CA' and m.item_id=td.pos_commodity "
	// +
	// "group by m.voucher_type ,pos_commodity,deducted_quantity)a where
	// a.voucher_type is not null ";
	public static String getAndroidBeneficiary = "select m.id,m.createdby,m.createdon,ci.CardNo,ci.SerialNO,ci.cardpin,cp.ProgrammeId,memberno,"
			+ " surname,title,firstnames,othername,mobileno,idppno,gender,dateofbirth,maritalStatus,nationality,email,pic,familysize,bnfgrpid "
			+ "from M_MemberMaster M, CardIssuance ci,CustomerProgrammeDetails cp "
			+ "where ci.CustomerId=m.ID and cp.CustomerId=m.ID and cp.IsActive=1";
	public static String checkTopupCycle = "select distinct cycle from mst_topup where agent_id=? and beneficiary_group_id=? and topup_status='N'";
	public static String checkTopupCycleDownloaded = "select distinct cycle from mst_topup where agent_id=? and beneficiary_group_id=? and topup_status='A' and downloaded=0";

	// Transaction Settlement
	public static String getActiveProviderList = "select * from mst_merchant where active=1";
	public static String getTransactionList = "{call PRC_GetBilldetails (?,?,?,?,?)}";
	public static String getVerifiedTransactionList = "{call PRC_GetVerifiedBilldetails (?,?,?,?,?)}";
	public static String getApprovedTransactionList = "{call PRC_GetApprovedBilldetails (?,?)}";
	public static String updMemberTransStatus = "update member_trans_mst set trans_status=?,paid_status=?,verified_by=?,verified_on=? where id=?";
	public static String updTransApprovedStatus = "update member_trans_mst set trans_status=?,paid_status=?,approved_by=?,approved_on=? where id=?";
	public static String updSettementFileStatus = "update member_trans_dtl set trans_status=?, paid_status=?, generated_by=?, generated_on=?, file_name=? where trans_status=2 and id=?";
	public static String updRejectedTrans = "update member_trans_mst set trans_status=?,paid_status=?,rejected_by=?,rejected_on=?,reason=? where id=?";
	public static String getempName = "select emp_fullname as name from emp_mst where emp_ref_key=? " + "union "
			+ "select alt_name as name from emp_dtl where ref_sub_key=?";
	public static String getActiveScheme = "select * from productmaster where active=1 and org_id=?";
	public static String getPendingAuthTransaction = "select m.id as trans_mst_id,md.id,member_no,ser_name ,txn_amount,device_id,mer_name, m.trans_date "
			+ " from member_trans_mst m, member_trans_dtl md,mst_health_services h,deviceregdetails dr, deviceissuedetails di,mst_merchant mt, emp_mst em "
			+ " where m.id=md.trans_mst_id and md.trans_status=-2 and h.id=md.service_id and di.deviceregid=dr.id and em.emp_ref_key=m.member_no "
			+ " and mt.id=di.merchantid and dr.serialno=m.device_id and em.org_id = ?";
	public static String getPendingAuthTrans = "{call prc_getauthtrans(?)}";
	// public static String getPendingAuthTransListByBill="{call
	// prc_getauthtransdtl(?)}";
	public static String getPendingAuthTransListByBill = "select m.id as trans_mst_id,md.id,member_no,ser_name ,txn_amount,device_id,mer_name from member_trans_mst m, member_trans_dtl md,mst_health_services h,deviceregdetails dr , deviceissuedetails di, agentmaster a, mst_merchant mt where m.id=md.trans_mst_id and md.trans_status=-2 and h.id=md.service_id and di.deviceregid=dr.id and  a.id=di.agentid and mt.id=a.merchantid and dr.serialno=m.device_id";
	public static String updateAuthTrans = "update member_trans_dtl set txn_status=?, auth_by=?,auth_on=? where id=? ";
	public static String updateAuthTransMst = "update member_trans_mst set trans_status=?, auth_by=?,auth_date=? where id=? ";
	public static String updateRejectAuthTrans = "update member_trans_dtl set txn_status=?, cancelled_by=?,cancelled_on=? ,reason=? where id=? ";
	public static String getAuthTransByBill = "select * from member_trans_dtl where trans_mst_id=? and txn_status='-2'";

	public static String insertDemoBasicDetails = "INSERT into demo_basic_details (id_number, date_of_birth, surname, other_name, gender, nationality, image) values (?,?,?,?,?,?,?)";
	public static String insertDemoBioDetails = "INSERT into demo_bio_details (basic_id, id_number, RT, R1, R2, R3, R4, LT, L1, L2, L3, L4) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String insertDemoSimDetails = "INSERT into demo_sim_details (basic_id, id_number, phone_number) values(?,?,?)";
	public static String getDemoBasicDetails = "SELECT * from demo_basic_details where id_number=?";
	public static String getDemoSimDetails = "SELECT phone_number from demo_sim_details where basic_id=?";
	public static String getDemoBioDetails = "SELECT * from demo_bio_details where basic_id=?";
	public static String checkIdNumberexists = "SELECT id from demo_basic_details WHERE id_number=?";
	public static String checkPhoneNumberexists = "SELECT id from demo_sim_details WHERE phone_number=?";
	public static String getUserByUsernameOrEmail = "SELECT userEmail from UserMaster WHERE UserEmail=?";
	public static String updateUserPassword = "UPDATE UserMaster set UserPassword=? where UserEmail=?";
	public static String getClaimsToVerify = "select mc.id, mc.insuranceName, mc.schemeCode, mc.schemeName, mc.chargeDate, mc.patientName, mc.membershipNo,\r\n"
			+ "mc.sponsorNet, mc.BillNo, mc.paidStatus,\r\n"
			+ "(case when mc.auth_amount>0 then mc.auth_amount else mc.sponsorNet end) as amount,\r\n"
			+ "(select mer_name from mst_merchant mm where mm.id = mc.providerId LIMIT 1) as providerName,\r\n"
			+ "(select card_no from card_mst where pj_number = mc.membershipNo LIMIT 1) as card_no,\r\n"
			+ "(select basket_id from mst_allocation mt where mt.card_number = card_no limit 1) as basket_id,\r\n"
			+ "(select service_id from mst_allocation mt where mt.card_number = card_no limit 1) as service_id\r\n"
			+ " from mst_claims mc WHERE date(mc.chargeDate)  >= ? AND date(mc.chargeDate) <= ? \r\n"
			+ "and mc.providerId = 1 and mc.trans_status in(0,4,5) and mc.paidStatus in(110,114,115) ORDER BY chargeDate DESC";
	public static String getAkuhTransactionDetails = "select insuranceName, schemeCode, schemeName, chargeDate, patientName, membershipNo, sponsorNet from mst_claims";
	public static String getClaimTransactionDetails = "SELECT dt.id, mc.id as claim_id,\r\n"
			+ "(case when mc.approved_amount > 0 then mc.approved_amount when mc.verified_amount>0 then mc.verified_amount when mc.auth_amount then mc.auth_amount else mc.sponsorNet end) as sponsorNet, \r\n"
			+ "ms.ser_name, ms.id as ser_id, v.voucher_name, v.id as voucher_id \r\n"
			+ "FROM member_trans_dtl dt, mst_health_services ms, voucher_mst v, mst_claims mc \r\n"
			+ "where ms.id=dt.service_id and v.id=dt.basket_id and mc.id = ?";

	public static String updMemberTransClaimDtlStatus = "update mst_claims set sponsorNet=?, paidStatus=?, verified_amount = ?, verified_on=?, trans_status=? where id=?";
	public static String getVerifiedClaims = "select mc.id, mc.insuranceName, mc.schemeCode, mc.schemeName, mc.chargeDate, mc.patientName, mc.membershipNo,\r\n"
			+ "mc.sponsorNet, mc.BillNo, mc.paidStatus, (case when mc.verified_amount > 0 then mc.verified_amount else mc.sponsorNet end) as amount,\r\n"
			+ "(select mer_name from mst_merchant mm where mm.id = mc.providerId LIMIT 1) as providerName,\r\n"
			+ "(select card_no from card_mst where pj_number = mc.membershipNo LIMIT 1) as card_no,\r\n"
			+ "(select basket_id from mst_allocation mt where mt.card_number = card_no LIMIT 1) as basket_id,\r\n"
			+ "(select service_id from mst_allocation mt where mt.card_number = card_no LIMIT 1) as service_id\r\n"
			+ "from mst_claims mc WHERE date(mc.chargeDate)  >= ?  AND date(mc.chargeDate) <= ? and mc.providerId = 1 and mc.trans_status in(1,5,4) and mc.paidStatus in(111,114,115) ORDER BY chargeDate DESC";
	public static String updClaimApprovedMemberTransDtl = "update mst_claims set verified_amount=?, paidStatus=?, approved_on=?, trans_status=?, approved_amount = ? where id=?";
	public static String getApprovedClaims = "select ROUND(sum(md.approved_amount),2) as amount, count(md.id) as noofreceipt,mm.mer_name,\r\n"
			+ "mer_code, bank_code,bank_name, GROUP_CONCAT(md.id) as txnid from mst_claims md \r\n"
			+ "inner join mst_merchant mm on mm.id=md.providerId \r\n"
			+ "inner join emp_mst em on em.emp_ref_key=md.membershipNo \r\n"
			+ "where date(md.chargeDate) >=? and date(md.chargeDate) <= ? \r\n"
			+ "and md.trans_status=2 and md.paidStatus=112 and mm.id=1 group by mm.mer_name";
	public static String updClaimSettementFileStatus = "update mst_claims set trans_status=?, paidStatus=?, generated_by=?, generated_on=?, file_name=? where id=?";
	public static String updCancelledClaimTransDtl = "update mst_claims set trans_status=?, paidStatus=?, cancelled_by = ?, cancelled_on=?, reason=? where id=?";
	public static String getApprovedClaimTransactionDtl = "select mc.id, mc.insuranceName, mc.schemeCode, mc.schemeName, mc.chargeDate, mc.patientName, membershipNo, \r\n"
			+ "mc.approved_amount as amount, mc.sponsorAmount, mc.BillNo, mc.approved_on,\r\n"
			+ "	(select mer_name from mst_merchant mm where mm.id = mc.providerId LIMIT 1) as provider_name,\r\n"
			+ "	(select card_no from card_mst where pj_number = mc.membershipNo LIMIT 1) as card_no,\r\n"
			+ "	(select basket_id from mst_allocation mt where mt.card_number = card_no LIMIT 1) as basket_id,\r\n"
			+ "	(select mt.service_id from mst_allocation mt where mt.card_number = card_no LIMIT 1) as service_id\r\n"
			+ "	from mst_claims mc WHERE mc.id=?";
	public static String getAllClaimTransactions = "select DISTINCT mc.id, membershipNo, mc.schemeCode, mc.schemeName,\r\n"
			+ "(select card_no from card_mst where pj_number = mc.membershipNo limit 1) AS card_no, mc.chargeDate,\r\n"
			+ "mc.BillNo, \r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status, \r\n"
			+ "(case when mc.approved_amount>0 then mc.approved_amount when mc.verified_amount>0 then mc.verified_amount when mc.auth_amount>0 then mc.auth_amount else sponsorNet end) as service_amount, \r\n"
			+ "patientName AS NAME, mc.amount as basket_amount, service_name,\r\n"
			+ "(select mer_name from mst_merchant mm where mm.id = mc.providerId) as providerName\r\n"
			+ "from mst_claims mc\r\n" + "inner join mst_health_sub_services mh on mc.itemCode=mh.service_code\r\n"
			+ "order by chargeDate desc";
	public static String getAllClaimTransactionsBetween = "select DISTINCT mc.id, membershipNo, mc.schemeCode, mc.schemeName,\r\n"
			+ "(select card_no from card_mst where pj_number = mc.membershipNo limit 1) AS card_no, mc.chargeDate,\r\n"
			+ "mc.BillNo, \r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status, \r\n"
			+ "(case when mc.approved_amount>0 then mc.approved_amount when mc.verified_amount>0 then mc.verified_amount when mc.auth_amount>0 then mc.auth_amount else sponsorNet end) as service_amount, \r\n"
			+ "patientName AS NAME, mc.amount as basket_amount, service_name,\r\n"
			+ "(select mer_name from mst_merchant mm where mm.id = mc.providerId) as providerName\r\n"
			+ "from mst_claims mc\r\n" + "inner join mst_health_sub_services mh on mc.itemCode=mh.service_code\r\n"
			+ "WHERE date(mc.chargeDate)>= ? AND date(mc.chargeDate) <= ? \r\n" + "order by chargeDate desc";
	public static String getAllClaimTransactionsAbove = "select DISTINCT mc.id, membershipNo, mc.schemeCode, mc.schemeName,\r\n"
			+ "(select card_no from card_mst where pj_number = mc.membershipNo limit 1) AS card_no, mc.chargeDate,\r\n"
			+ "mc.BillNo, \r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status, \r\n"
			+ "(case when mc.approved_amount>0 then mc.approved_amount when mc.verified_amount>0 then mc.verified_amount when mc.auth_amount>0 then mc.auth_amount else sponsorNet end) as service_amount, \r\n"
			+ "patientName AS NAME, mc.amount as basket_amount, service_name,\r\n"
			+ "(select mer_name from mst_merchant mm where mm.id = mc.providerId) as providerName\r\n"
			+ "from mst_claims mc\r\n" + "inner join mst_health_sub_services mh on mc.itemCode=mh.service_code\r\n"
			+ "WHERE date(mc.chargeDate)>= ? AND date(mc.chargeDate) >= ? \r\n" + "order by chargeDate desc";
	public static String getAllClaimTransactionsBelow = "select DISTINCT mc.id, membershipNo, mc.schemeCode, mc.schemeName,\r\n"
			+ "(select card_no from card_mst where pj_number = mc.membershipNo limit 1) AS card_no, mc.chargeDate,\r\n"
			+ "mc.BillNo, \r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status, \r\n"
			+ "(case when mc.approved_amount>0 then mc.approved_amount when mc.verified_amount>0 then mc.verified_amount when mc.auth_amount>0 then mc.auth_amount else sponsorNet end) as service_amount, \r\n"
			+ "patientName AS NAME, mc.amount as basket_amount, service_name,\r\n"
			+ "(select mer_name from mst_merchant mm where mm.id = mc.providerId) as providerName\r\n"
			+ "from mst_claims mc\r\n" + "inner join mst_health_sub_services mh on mc.itemCode=mh.service_code\r\n"
			+ "WHERE date(mc.chargeDate)<= ? AND date(mc.chargeDate) <= ? \r\n" + "order by chargeDate desc";
	public static String getDependantDetails = "{call PRC_GetDependantdetails(?)}";
	public static String updatePassword = "UPDATE usermaster set UserPassword=?, first_time_login=0 where UserName=?";
	public static String getAllGertrudeTransactions = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ " md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId \r\n"
			+ "from member_trans_mst mc \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id \r\n"
			+ "WHERE mer_name LIKE '%GERTRUDES CHILDRENS HOSPITAL%' AND pm.org_id=? \r\n" 
			+ "order by trans_date desc";
	public static String getAllGertrudeTransactionsBetween = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ " md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId \r\n"
			+ "from member_trans_mst mc \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id \r\n"
			+ "WHERE mer_name LIKE '%GERTRUDES CHILDRENS HOSPITAL%' AND pm.org_id=? \r\n"
			+ "AND date(mc.trans_date) >= ? and date(mc.trans_date) <= ?\r\n" + "order by trans_date desc";
	public static String getAllGertrudeTransactionsAbove = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ " md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId \r\n"
			+ "from member_trans_mst mc \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id \r\n"
			+ "WHERE mer_name LIKE '%GERTRUDES CHILDRENS HOSPITAL%' AND pm.org_id=? \r\n"
			+ "AND date(mt.trans_date) >= ? and date(mt.trans_date) >= ?\r\n" + "order by trans_date desc";
	public static String getAllGertrudeTransactionsBelow = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ " md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId \r\n"
			+ "from member_trans_mst mc \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id \r\n"
			+ "WHERE mer_name LIKE '%GERTRUDES CHILDRENS HOSPITAL%' AND pm.org_id=? \r\n"
			+ "AND date(mt.trans_date) <= ? and date(mt.trans_date) <= ?\r\n" + "order by trans_date desc";
	public static String getAllMpshahTransactions = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ " md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId \r\n"
			+ "from member_trans_mst mc \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id\r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id\r\n" + "WHERE mer_name LIKE '%MP SHAH HOSPITAL%' AND pm.org_id=? \r\n"
			+ "order by trans_date desc;\r\n" + "";
	public static String getAllMpshahTransactionsBetween = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ " md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId \r\n"
			+ "from member_trans_mst mc \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id \r\n"
			+ "WHERE mer_name LIKE '%MP SHAH HOSPITAL%' AND pm.org_id=? \r\n"
			+ "AND date(mc.trans_date) >= ? and date(mc.trans_date) <= ?\r\n" + "order by trans_date desc";
	public static String getPOSUserByUserName = "select id from merchant_pos_users_mst where username = ?";
	public static String getUserEmailById = "select id from merchant_pos_users_mst where username=? and id<>?";
	public static String getAllNboTransactions = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n"
			+ "(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n"
			+ "(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n"
			+ " mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n"
			+ "md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n"
			+ "from member_trans_mst mc  \r\n" + "inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n"
			+ "inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n"
			+ "inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n"
			+ "inner join mst_health_services h on h.id=md.service_id \r\n"
			+ "inner join productmaster pm on pm.id=mc.scheme_id \r\n"
			+ "inner join mst_merchant m on mc.provider_id=m.id\r\n"
			+ "inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n"
			+ "inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n"
			+ "INNER JOIN mst_organization og ON og.id = pm.org_id \r\n"
			+ "WHERE mer_name LIKE '%THE NAIROBI HOSPITAL%' AND pm.org_id=? order by trans_date desc";
	public static String getAllNboTransactionsBetween="select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			" mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n" + 
			"md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"from member_trans_mst mc  \r\n" + 
			"inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"INNER JOIN mst_organization og ON og.id = pm.org_id \r\n" + 
			"WHERE mer_name LIKE '%THE NAIROBI HOSPITAL%' AND pm.org_id=? \r\n" + 
			"AND date(mc.trans_date) >= ? and date(mc.trans_date) <= ? order by trans_date desc";
	public static String getAllNboTransactionsAbove="select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			" mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number,\r\n" + 
			"md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"from member_trans_mst mc  \r\n" + 
			"inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"INNER JOIN mst_organization og ON og.id = pm.org_id \r\n" + 
			"WHERE mer_name LIKE '%THE NAIROBI HOSPITAL%' AND pm.org_id=? \r\n" + 
			"AND date(mc.trans_date) >= ? and date(mc.trans_date) >= ? order by trans_date desc";
	public static String getAllNboTransactionsBelow="select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			" mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number,\r\n" + 
			"md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"from member_trans_mst mc  \r\n" + 
			"inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"INNER JOIN mst_organization og ON og.id = pm.org_id \r\n" + 
			"WHERE mer_name LIKE '%THE NAIROBI HOSPITAL%' AND pm.org_id=? \r\n" + 
			"AND date(mc.trans_date) <= ? and date(mc.trans_date) <= ? order by trans_date desc";
	public static String getMemberToDeactivateByMemberNo="Select emp_id, em.active, outpatient_status, card_no, emp_fullname, product_name, emp_dob \r\n" + 
			"from emp_mst em inner join card_mst cm on cm.emp_mst_id=emp_id \r\n" + 
			"inner join productmaster pm on pm.id = product_id where emp_ref_key=?";
	public static String getDeactivatesByMemberId = "Select cd.reason, cd.createdOn, (select userfullname from usermaster where id=cd.createdBy) as createdBy,cd.deactivate \r\n" + 
			"from customerdeactivate cd where emp_id=? and cd.deactivate = 'D'";
	public static String insertDeativateRecord="INSERT into customerdeactivate (emp_id, member_no, reason, createdBy,deactivate, createdOn) values(?,?,?,?,?,?);";
	public static String updateCustomerStatus = "UPDATE emp_mst set active=0, outpatient_status='Inactive' where emp_id=?";
	public static String getActiveMemberList="select emp_id, emp_fullname, emp_ref_key, emp_nationalid, emp_dob,\r\n" + 
			"(case when emp_gender='F' THEN 'FEMALE' WHEN emp_gender='M' THEN 'MALE' else emp_gender end) as emp_gender, \r\n" + 
			"active, outpatient_status \r\n" + 
			"from emp_mst where active =1";
	public static String insertAuditTrail ="INSERT INTO audit_logs (userId, created_by, Description, created_on, ModuleAccessed) values(?,?,?,?,?)";
	public static String getAuditTrail = "select distinct a.userId, a.created_by, a.Description, a.created_on, a.ModuleAccessed,\r\n" + 
			"(select username as username from usermaster u where u.Id = a.userId limit 1) as username from audit_logs a order by created_on desc";
	public static String getAuditTrailBetween=" select distinct a.userId, a.created_by, a.Description, a.created_on, a.ModuleAccessed,\r\n" + 
			"(select username as username from usermaster u where u.Id = a.userId limit 1) as username\r\n" + 
			"from audit_logs a WHERE date(a.created_on) >= ? and date(a.created_on) <= ? order by created_on desc";
	public static Object getNewMemberVouchersBest = "SELECT ac.basket_Id,voucher_name,voucher_code, MAX(basket_balance) as basket_balance, MAX(ac.acc_no) as acc_no,  MAX(basket_value) as basket_value, \r\n" + 
			"MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type, scheme_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed  \r\n" + 
			"WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=2 GROUP BY basket_id \r\n" + 
			"UNION ALL \r\n" + 
			"SELECT ac.basket_Id,voucher_name,voucher_code, MAX(basket_balance) as basket_balance, MAX(ac.acc_no) as acc_no,  MAX(basket_value) as basket_value, \r\n" + 
			"MAX(coalesce((basket_value-basket_balance),0)) as used, cover_type, scheme_type FROM mst_allocation ac,Voucher_mst S,emp_acc_dtl ed \r\n" + 
			"WHERE S.Id=ac.basket_Id AND ac.Programme_Id=?  and ed.acc_id=ac.acc_id and ed.emp_mst_id=? and cover_type=1 GROUP BY basket_id";
	public static String getMemberSublimits = "SELECT distinct ac.basket_Id,ser_code,ser_name, cover_limit, require_auth, ac.basket_value,ac.service_id,require_auth, \r\n" + 
			"service_value,service_balance, coalesce((service_value-service_balance),0) as used \r\n" + 
			"FROM mst_allocation ac,mst_health_services S,emp_acc_dtl ed,programmevoucherDetails pv \r\n" + 
			"WHERE S.Id=ac.service_Id and ac.Programme_Id=? and ac.basket_id=? and pv.service_id=s.id and pv.programmeid=ac.programme_id \r\n" + 
			"and pv.voucherid=ac.basket_id and ed.acc_id=ac.acc_id and ed.emp_mst_id=?";
	public static String getMerchantsByUserName = "select m.Id, mer_code, mer_name, UserFullName from mst_merchant m inner join usermaster um on m.mer_name = um.UserFullName WHERE UserFullName=?";
	public static String getAllUHMCTransactions="select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			"mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n" + 
			"md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"from member_trans_mst mc   inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"inner join mst_organization og ON og.id = pm.org_id \r\n" + 
			"WHERE mer_name LIKE '%UHMC PHARMACY%' AND pm.org_id=? order by trans_date desc";
	public static String getAllUHMCTransactionsBetween ="select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"			(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"			(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			"			 mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n" + 
			"			md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"			from member_trans_mst mc   inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"			inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"			inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"			inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"			inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"			inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"			inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"			inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"			INNER JOIN mst_organization og ON og.id = pm.org_id \r\n" + 
			"			WHERE mer_name LIKE '%UHMC PHARMACY%' AND pm.org_id=? AND date(mc.trans_date) >= ? and date(mc.trans_date) <= ? order by trans_date desc";
	public static String getAllUHMCTransactionsAbove;
	public static String getAllUHMCTransactionsBelow;
	//clinix
	public static String getAllClinixCTransactions = "select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"			(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"			(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			"			 mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n" + 
			"			md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"			from member_trans_mst mc   inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"			inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"			inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"			inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"			inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"			inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"			inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"			inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"			INNER JOIN mst_organization og ON og.id = pm.org_id \r\n" + 
			"			WHERE mer_name LIKE '%CLINIX HEALTHCARE%' AND pm.org_id=? order by trans_date desc";
	public static String getAllClinixTransactionsBetween ="select mc.id, member_no, mc.trans_date, mc.bill_no, mc.card_serial AS card_no, org_name,\r\n" + 
			"			(case when mc.trans_status=3 then 'SETTLED' when mc.trans_status=2 then 'APPROVED' when mc.trans_status=1 then 'VERIFIED' when mc.trans_status=0 then 'UNVERIFIED' when mc.trans_status=-2 then 'UNAUTHORIZED' when mc.trans_status=-1 then 'CANCELLED' when mc.trans_status=-3 then 'REJECTED' else mc.trans_status end) as trans_status,\r\n" + 
			"			(case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount,\r\n" + 
			"			 mc.amount as basket_amount, emp_fullname AS NAME, mc.invoice_number, \r\n" + 
			"			md.value_remaining as service_balance, mc.total_value_remaining as basket_balance, mc.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId\r\n" + 
			"			from member_trans_mst mc   inner join member_trans_dtl md on mc.id=md.Trans_mst_id \r\n" + 
			"			inner join emp_acc_dtl ac on ac.acc_no=mc.acc_no \r\n" + 
			"			inner join emp_mst em on ac.emp_mst_id=em.emp_id \r\n" + 
			"			inner join mst_health_services h on h.id=md.service_id \r\n" + 
			"			inner join productmaster pm on pm.id=mc.scheme_id \r\n" + 
			"			inner join mst_merchant m on mc.provider_id=m.id\r\n" + 
			"			inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id \r\n" + 
			"			inner join programmemaster pg on cp.ProgrammeId=pg.id \r\n" + 
			"			INNER JOIN mst_organization og ON og.id = pm.org_id \r\n" + 
			"			WHERE mer_name LIKE '%CLINIX HEALTHCARE%' AND pm.org_id=? AND date(mc.trans_date) >= ? and date(mc.trans_date) <= ? order by trans_date desc";
	public static String getAllClinixTransactionsAbove;
	public static String getAllClinixTransactionsBelow;
	
	
	//service providers
	public static String getAllSpTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and mer_name LIKE ?) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and mer_name LIKE ?) order by trans_date desc";

	public static String getAllSpTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and mer_name LIKE ? and date(mt.trans_date) >= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and mer_name LIKE ? and date(mt.trans_date) >= ?) order by trans_date desc";

	public static String getAllSpTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and mer_name LIKE ? and date(mt.trans_date) <=? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and mer_name LIKE ? and date(mt.trans_date) <= ?) order by trans_date desc";

	public static String getAllSpTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and mer_name LIKE ? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =? and mer_name LIKE ? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?) order by trans_date desc";
	
	public static String deleteMerchantPOSUser = "DELETE from merchant_pos_users_mst where Id=?";
	
	public static String getIndividualTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =1 and mt.bill_no =?) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, mt.invoice_number, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where cp.isactive=1 and pm.org_id =1 and mt.bill_no =?) order by trans_date desc";
	public static String getMemberUtilizations="SELECT ma.id, emp_fullname, product_name, emp_ref_key, basket_id, ser_name, service_id, basket_value, basket_balance,COALESCE((basket_value - basket_balance), 0) AS utilization\r\n" + 
			"FROM mst_health_services S, mst_allocation ma INNER JOIN emp_mst em ON ma.acc_no = em.emp_id INNER JOIN productmaster pm on ma.programme_id = pm.id WHERE S.id  = ma.service_id and product_name LIKE '%JUDICIARY CATEGORY%' AND ser_name != 'IN PATIENT OVERALL'\r\n" + 
			"        AND ser_name != 'POST HOSPITALIZATION'\r\n" + 
			"        AND ser_name != 'IN PATIENT PRE-EXISTING'\r\n" + 
			"        AND ser_name != 'IN PATIENT CONGENITAL/CHILDBRITH COVER/ NEO NATAL CONDITIONS/ PREMATURITY EXPENSES'\r\n" + 
			"        AND ser_name != 'IN PATIENT NON ACCIDENTAL DENTAL'\r\n" + 
			"        AND ser_name != 'IN PATIENT NON ACCIDENTAL OPTICAL/ OPTHAMOLOGY'\r\n" + 
			"        AND ser_name != 'IN PATIENT PSYCHIATRY AND PSYCHOTHERAPY TREATMENT'\r\n" + 
			"        AND ser_name != 'IN PATIENT FIRST CAESAREAN/SUBSEQUENT SECTION (ALL CS''s)'\r\n" + 
			"        AND ser_name != 'IN PATIENT PRE AND POST MATERNITY COMPLICATIONS'\r\n" + 
			"        AND ser_name != 'IN PATIENT MATERNITY' order by product_name ASC";
	public static String getTotalUtilizations = "select product_name, sum(basket_value) as basket_value, sum(COALESCE((basket_value - basket_balance), 0)) AS utilization from mst_allocation ma INNER JOIN productmaster pm on ma.programme_id = pm.id WHERE pm.product_name LIKE '%JUDICIARY CATEGORY%' GROUP BY product_name";
	
	public static String getJDCProviderWiseTransactions = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.product_name != 'LIAISON GROUP STAFF') "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and pm.product_name != 'LIAISON GROUP STAFF') order by mer_name asc";

	public static String getJDCProviderWiseTransactionsAbove = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ?) order by mer_name asc";

	public static String getJDCProviderWiseTransactionsBelow = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) <=?) order by mer_name asc";

	public static String getJDCProviderWiseTransactionsBetween = "(select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_acc_dtl ac on ac.acc_no=mt.acc_no inner join emp_mst em on ac.emp_mst_id=em.emp_id inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.emp_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ? ) "
			+ " UNION (select mt.id,member_no,mt.card_serial AS card_no,mt.trans_date,mt.acc_no,mt.bill_no, (case when md.trans_status=3 then 'SETTLED' when md.trans_status=2 then 'APPROVED' when md.trans_status=1 then 'VERIFIED' when md.trans_status=0 then 'UNVERIFIED' when md.trans_status=-2 then 'UNAUTHORIZED' when md.trans_status=-1 then 'CANCELLED' when md.trans_status=-3 then 'REJECTED' else md.trans_status end) as trans_status, (case when approved_amount>0 then approved_amount when verified_amount>0 then verified_amount when auth_amount>0 then auth_amount else txn_amount end) as service_amount, emp_fullname AS NAME, md.value_remaining as service_balance,mt.total_value_remaining as basket_balance, mt.amount as basket_amount, ser_name,device_id,product_name, mer_name,pg.ProgrammeDesc, productId, merchant_id as MerchantId from member_trans_mst mt inner join member_trans_dtl md on mt.id=md.Trans_mst_id inner join emp_mst em on em.emp_ref_key=member_no inner join mst_health_services h on h.id=md.service_id inner join productmaster pm on pm.id=mt.scheme_id inner join mst_merchant m on mt.provider_id=m.id inner join customerprogrammedetails cp on cp.CustomerId=em.principle_id inner join programmemaster pg on cp.ProgrammeId=pg.id where em.emp_ref_key=member_no and cp.isactive=1 and pm.org_id =? and date(mt.trans_date) >= ? and date(mt.trans_date) <= ?) order by mer_name asc";
	public static String getAllJudiciaryMembers = "SELECT emp_ref_key, emp_fullname, emp_dob, em.active, (CASE WHEN member_type = 'D' THEN 'DEPENDANT' WHEN member_type = 'P' then 'PRINCIPAL' ELSE member_type END) AS member_type, product_name FROM emp_mst em INNER JOIN productmaster pm ON em.product_id = pm.id WHERE product_name != 'LIAISON GROUP STAFF'";
	
	
	public static String insertOFFClaim = "INSERT INTO mst_off_lct (insuranceCode,insuranceName,schemeCode,schemeName,chargeDate,receiptNo,patientNo,patientName,membershipNo,itemCode,itemName,transactionType,remarks,itemQuantity,patientAmount,patientDiscount,patientNet,sponsorAmount,sponsorDiscount,sponsorNet, txnStatus, providerId, paidStatus, BillNo, trans_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, 0, 1, 110,?,0)";

	
	public static String insertSmartDebit = "INSERT INTO smart_debit(smart_bill_id, return_amount, return_reason, lct_mem_no, provider, invoice_no, action_name, benefit, item_code, invoice_date) VALUES(?,?,?,?,?,?,?,?,?,?)"; 
	
	public static String getSmartMasterServiceId = "select mst_serv_id from mst_health_sub_services where service_code=?";

}
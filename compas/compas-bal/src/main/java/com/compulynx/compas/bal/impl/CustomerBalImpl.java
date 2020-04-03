package com.compulynx.compas.bal.impl;

import com.compulynx.compas.bal.CustomerBal;
import com.compulynx.compas.dal.UploadmemberDal;
import com.compulynx.compas.dal.impl.CustomerDalImpl;
import com.compulynx.compas.models.*;
import com.compulynx.compas.models.android.ApiProgramme;
import com.compulynx.compas.models.android.ApiVoucher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class CustomerBalImpl implements CustomerBal{

	private static final Logger logger = Logger.getLogger(CustomerBalImpl.class.getName());
	//rewire
	@Autowired
	CustomerDalImpl memberDal;
	@Autowired
	UploadmemberDal uploadmemberDal;
	public Member GetMembers(String memberNo) {
		return memberDal.GetMembers(memberNo);
	}

	public CompasResponse UpdateMember(Member member) {		
		return memberDal.UpdateMember(member);
	}
	
	

	public List<Member> GetCardActivationList() {
		return memberDal.GetCardActivationList();
	}
	
	public List<Member> GetCardBlockList() {
		return memberDal.GetCardBlockList();
	}

	@Override
	 public CompasResponse UploadService(String filePath, int orgId, int productId, int programmeId, String createdBy) {
		return uploadmemberDal.UploadMember(filePath,orgId,productId,programmeId,createdBy);
  }

	@Override
	public CompasResponse UpdateCardIssuance(MemberCard card) {
		// TODO Auto-generated method stub
		return memberDal.UpdateCardIssuance(card);
	}

	
	@Override
	public CompasResponse updateCardReissue(MemberCard memCard) {
		// TODO Auto-generated method stub
		return memberDal.updateCardReissue(memCard);
	}

	@Override
	public List<Programme> GetProgrammesByBnfId(int bnfId) {
		// TODO Auto-generated method stub
		return memberDal.GetProgrammesByBnfId(bnfId);
	}

	@Override
	public List<Relations> GetRelations() {
		// TODO Auto-generated method stub
		return memberDal.GetRelations();
	}

	@Override
	public CompasResponse UpdateBnfStatus(Member member) {
		// TODO Auto-generated method stub
		return memberDal.UpdateBnfStatus(member);
	}

	

	
	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.CustomerBal#UpdateAndMember(java.util.List)
	 */
	@Override
	public CompasResponse UpdateAndMember(List<Member> member) {
		// TODO Auto-generated method stub
		return memberDal.UpdateAndMember(member);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.CustomerBal#GetScheme(int)
	 */
	@Override
	public List<Product> GetScheme(int orgId) {
		// TODO Auto-generated method stub
		return memberDal.GetScheme(orgId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.CustomerBal#GetMembersToVerify(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<Member> GetMembersToVerify(String fromDate, String toDate, int count,int orgId) {
		// TODO Auto-generated method stub
		return memberDal.GetMembersToVerify(fromDate, toDate, count,orgId);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.CustomerBal#GetMembersToApprove(java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<Member> GetMembersToApprove(String fromDate, String toDate,
			int count, int orgId) {
		// TODO Auto-generated method stub
		return memberDal.GetMembersToApprove(fromDate, toDate, count, orgId);
	}
	
	@Override
	public Detach GetMemberDetachDetails(String memberNo) {
		return memberDal.GetMemberDetachDetails(memberNo);
	}
	
	@Override
	public CompasResponse DetachBio(Detach detach) {
		// TODO Auto-generated method stub
		return memberDal.DetachBio(detach);
	}
	
	@Override
	public Member GetMemberBasicDetails(String memberNo) {
		return memberDal.GetMemberBasicDetails(memberNo);
	}
	
	@Override
	public List<ApiProgramme> GetMemberProgrammes(Member member) {
		return memberDal.GetMemberProgrammes(member);
	}
	
	@Override
	public List<ApiVoucher> GetMemberVouchers(Member member) {
		return memberDal.GetMemberVouchers(member);
	}

	public Member GetDependants(String memberNo) {
		// TODO Auto-generated method stub
		return memberDal.GetDependants(memberNo);
	}

	public CompasResponse DeactivateMember(Deactivate deactivate) {
		// TODO Auto-generated method stub
		return memberDal.DeactivateMember(deactivate);
	}

	public Deactivate GetMemberDeactivationDetails(String memberNo) {
		// TODO Auto-generated method stub
		return memberDal.GetMemberDeactivationDetails(memberNo);
	}

	@Override
	public Member GetDeactivationMemberBasicDetails(String memberNo) {
		// TODO Auto-generated method stub
		return memberDal.GetDeactivationMemberBasicDetails(memberNo);
	}
}

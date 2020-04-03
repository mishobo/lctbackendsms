package com.compulynx.compas.bal;

import com.compulynx.compas.models.*;
import com.compulynx.compas.models.android.ApiProgramme;
import com.compulynx.compas.models.android.ApiVoucher;

import java.util.List;

public interface CustomerBal {
	Member GetMembers(String memberNo);

	CompasResponse UpdateMember(Member member);

	CompasResponse UpdateAndMember(List<Member> member);

	List<Member> GetCardBlockList();

	List<Member> GetCardActivationList();

	CompasResponse UpdateCardIssuance(MemberCard card);

	CompasResponse updateCardReissue(MemberCard memCard);

	List<Programme> GetProgrammesByBnfId(int bnfId);

	List<Relations> GetRelations();

	CompasResponse UpdateBnfStatus(Member member);
	List<Product> GetScheme(int orgId);	
	List<Member> GetMembersToVerify(String fromDate,String toDate,int count,int orgId);
	List<Member> GetMembersToApprove(String fromDate,String toDate,int count,int orgId);
	
	Detach GetMemberDetachDetails(String memberNo);
	CompasResponse DetachBio(Detach detach);
	
	Member GetMemberBasicDetails(String memberNo);
	List<ApiProgramme> GetMemberProgrammes(Member member);
	List<ApiVoucher> GetMemberVouchers(Member member);
	CompasResponse UploadService(String filePath, int orgId, int productId, int programmeId, String createdBy);
	
	Member GetDependants(String memberNo);

	Deactivate GetMemberDeactivationDetails(String memberNo);
	CompasResponse DeactivateMember(Deactivate deactivate);
	Member GetDeactivationMemberBasicDetails(String memberNo);
	
}

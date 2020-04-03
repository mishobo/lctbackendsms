package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.MemberCard;
import com.compulynx.compas.models.CardDetails;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Detach;
import com.compulynx.compas.models.Product;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Relations;
import com.compulynx.compas.models.android.ApiProgramme;
import com.compulynx.compas.models.android.ApiVoucher;


public interface CustomerDal {
	Member GetMembers(String memberNo);

	CompasResponse UpdateMember(Member member);
	
	CompasResponse UpdateCard(CardDetails card);

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
	
}
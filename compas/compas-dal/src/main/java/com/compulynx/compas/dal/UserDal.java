/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.CardActivation;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Demo;
import com.compulynx.compas.models.DemoResponse;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.TransAuthResponse;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.TransactionMst;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.android.AndroidTopup;


/**
 * @author Anita
 *
 */
public interface UserDal {
	boolean checkUserName(String userName);

	CompasResponse UpdateUser(User user);

	//List<User> GetAllUsers();

	User GetUserById(int userId);

	List<User> GetQuestions();
	List<User> GetAllUsers();
	List<User> GetClasses();
	List<Agent> GetLocationAgents(int locationId);
	
	List<UserGroup> GetGroupsByUserType(int userTypeId);
	AndroidDownloadVo GetAndroidDownloadData(String macAddress);
	AndroidDownloadVo GetAndroidBeneficiary(Member member);
	AndroidDownloadVo GetAndroidTopupDetails(String serialNo);
	CompasResponse UpdateBnfBioImages(List<Member> member);
	CompasResponse updateVoucherDownloadStatus(String deviceId);
	CompasResponse UplodTmsTrans(List<TransactionMst> trans);
	TransAuthResponse CheckAuthTransStatus(Transaction trans);
	
	CompasResponse UpdateCardActivation(List<CardActivation> cardActivation);
	DemoResponse UpdateDemo(Demo demo);
	DemoResponse GetDemo(Demo demo);
	DemoResponse VerifyNumber(Demo demo);
}

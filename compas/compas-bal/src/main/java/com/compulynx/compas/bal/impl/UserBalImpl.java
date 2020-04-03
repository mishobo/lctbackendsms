/**
 * 
 */
package com.compulynx.compas.bal.impl;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import com.compulynx.compas.dal.impl.UserDalImpl;
import com.compulynx.compas.bal.UserBal;
import com.compulynx.compas.bal.UserGroupBal;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.AutoComplete;
import com.compulynx.compas.models.AutoCompleteObject;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Demo;
import com.compulynx.compas.models.DemoResponse;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.TransAuthResponse;
import com.compulynx.compas.models.Transaction;
import com.compulynx.compas.models.TransactionMst;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.UserGroup;
import com.compulynx.compas.models.android.AndroidDownloadVo;
import com.compulynx.compas.models.CardActivation;
 
 
/**
 * @author Anita
 *
 */
@Component
public class UserBalImpl implements UserBal{
    @Autowired
    UserDalImpl userDal;
 
    public CompasResponse UpdateUser(User user) {
         
        return userDal.UpdateUser(user);
    }
     
//  public List<User> GetAllUsers() {
//      return userDal.GetAllUsers();
//  }
     
    public User GetUserById(int userId) {
        return userDal.GetUserById(userId);
    }
 
 
    public List<User> GetQuestions() {
         
        return userDal.GetQuestions();
    }
    public List<User> GetAllUsers() {
        return userDal.GetAllUsers();
    }
 
     
    public List<User> GetClasses() {
        // TODO Auto-generated method stub
        return userDal.GetClasses();
    }
 
    public List<Agent> GetLocationAgents(int locationId) {
        // TODO Auto-generated method stub
        return userDal.GetLocationAgents(locationId);
    }
 
     
 
    @Override
    public List<UserGroup> GetGroupsByUserType(int userTypeId) {
        // TODO Auto-generated method stub
        return userDal.GetGroupsByUserType(userTypeId);
    }
 
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#GetAndroidDownloadData(java.lang.String)
     */
    @Override
    public AndroidDownloadVo GetAndroidDownloadData(String macAddress) {
        // TODO Auto-generated method stub
        return userDal.GetAndroidDownloadData(macAddress);
    }
 
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#GetAndroidBeneficiary()
     */
    @Override
    public AndroidDownloadVo GetAndroidBeneficiary(Member member) {
        // TODO Auto-generated method stub
        return userDal.GetAndroidBeneficiary(member);
    }
 
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#GetAndroidTopupDetails(java.lang.String)
     */
    @Override
    public AndroidDownloadVo GetAndroidTopupDetails(String serialNo) {
        // TODO Auto-generated method stub
        return userDal.GetAndroidTopupDetails(serialNo);
    }
 
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#updateVoucherDownloadStatus(java.lang.String)
     */
    @Override
    public CompasResponse updateVoucherDownloadStatus(String deviceId) {
        // TODO Auto-generated method stub
        return userDal.updateVoucherDownloadStatus(deviceId);
    }
 
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public CompasResponse UpdateBnfBioImages(List<Member> member) {
        // TODO Auto-generated method stub
        return userDal.UpdateBnfBioImages(member);
    }
 
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UplodTmsTrans(java.util.List)
     */
    @Override
    public CompasResponse UplodTmsTrans(List<TransactionMst> trans) {
        // TODO Auto-generated method stub
        return userDal.UplodTmsTrans(trans);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public CompasResponse CardActivation(List<CardActivation> cardActivation) {
        // TODO Auto-generated method stub
        return userDal.UpdateCardActivation(cardActivation);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public DemoResponse UpdateDemo(Demo demo) {
        // TODO Auto-generated method stub
        return userDal.UpdateDemo(demo);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public DemoResponse GetDemo(Demo demo) {
        // TODO Auto-generated method stub
        return userDal.GetDemo(demo);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public DemoResponse VerifyNumber(Demo demo) {
        // TODO Auto-generated method stub
        return userDal.VerifyNumber(demo);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public TransAuthResponse CheckAuthTransStatus(Transaction transaction) {
        // TODO Auto-generated method stub
        return userDal.CheckAuthTransStatus(transaction);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public AutoCompleteObject GetMatchingMembers(String memberNo) {
        // TODO Auto-generated method stub
        return userDal.GetMatchingMembers(memberNo);
    }
     
    /* (non-Javadoc)
     * @see com.compulynx.compas.bal.UserBal#UpdateBnfBioImages(java.util.List)
     */
    @Override
    public AutoCompleteObject GetAllMembers() {
        // TODO Auto-generated method stub
        return userDal.GetAllMembers();
    }

	public CompasResponse changePassword(User user) {
		// TODO Auto-generated method stub
		return userDal.changePassword(user);
	}

	public List<Reports> GetAuditLogDetails(Reports audrpt) {
		// TODO Auto-generated method stub
		return userDal.GetAuditLogDetails(audrpt);
	}
 
}
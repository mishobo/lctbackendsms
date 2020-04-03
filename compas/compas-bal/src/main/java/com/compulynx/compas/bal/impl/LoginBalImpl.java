/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.LoginBal;
import com.compulynx.compas.dal.impl.LoginDalImpl;
import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.RightsDetail;


/**
 * @author Anita
 *
 */
@Component
public class LoginBalImpl implements LoginBal{
	@Autowired
	LoginDalImpl loginDal;
	
	public LoginUser ValidateManualLogin(int userId, String userName, String password) {
		return loginDal.GetUserIdManual(userId, userName, password);
	}

	public LoginUser ValidateBioLogin(int bioId) {
		return loginDal.GetUserIdBio(bioId);
	}

	public LoginSession GetLoginSession(LoginUser userId) {
		return loginDal.GetUserAssgnRightsList(userId);
	}


	public LoginUser ValidateBioLogin(String userName) {
		return loginDal.GetUserIdBio(userName);
	}


	public LoginUser GetUserIdDevice(String userName, String password,
			String deviceId) {
		// TODO Auto-generated method stub
		return loginDal.GetUserIdDevice(userName, password, deviceId);
	}

	

	
}

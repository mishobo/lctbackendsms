/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.RightsDetail;


/**
 * @author Anita
 *
 */
public interface LoginBal {

	LoginUser ValidateManualLogin(int userId, String userName, String password);

	LoginUser ValidateBioLogin(int bioId);

	LoginSession GetLoginSession(LoginUser user);

	LoginUser ValidateBioLogin(String userName);

	LoginUser GetUserIdDevice(String userName, String password, String deviceId);

	
	
}

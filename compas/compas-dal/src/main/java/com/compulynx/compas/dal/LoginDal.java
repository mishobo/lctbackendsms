/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.LoginSession;
import com.compulynx.compas.models.LoginUser;
import com.compulynx.compas.models.RightsDetail;


/**
 * @author Anita
 *
 */
public interface LoginDal {

	LoginUser GetUserIdManual(int userId, String userName, String password);

	LoginUser GetUserIdBio(int bioId);

	LoginSession GetUserAssgnRightsList(LoginUser user);

	LoginUser GetUserIdBio(String userName);

	LoginUser GetUserIdDevice(String userName, String password, String deviceId);
	

	

	
}

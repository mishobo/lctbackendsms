/**
 * 
 */
package com.compulynx.compas.dal;

import com.compulynx.compas.models.android.APIPOSUserAuth;

/**
 * @author Anita
 *Mar 16, 2017
 */
public interface MobileApiDal {
	public boolean loginPOSUser(APIPOSUserAuth user);
}

/**
 * 
 */
package com.compulynx.compas.bal;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.POSUser;
import com.compulynx.compas.models.android.APIPOSUserAuth;

/**
 * @author Anita
 * Mar 16, 2017
 */
public interface MobileApiBal {
	public CompasResponse loginPOSUser(APIPOSUserAuth user);
	public CompasResponse resetPOSUser(POSUser user);
}

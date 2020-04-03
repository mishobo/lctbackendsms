/**
 * 
 */
package com.compulynx.compas.dal;

import com.compulynx.compas.models.ResetUser;

/**
 * @author dkabii
 *
 */
public interface ResetDal {

	ResetUser resetUserPassword(String strUser);
	
}

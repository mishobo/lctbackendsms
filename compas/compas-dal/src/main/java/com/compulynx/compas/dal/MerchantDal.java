/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.POSUser;

/**
 * @author Anita
 *
 */
public interface MerchantDal {
	CompasResponse UpdateMerchant(Merchant merchant);

	List<Merchant> GetMerchants();
	Merchant getMerchantById(int id);
	boolean addMerchantPOSUser(POSUser posUser);
	boolean editMerchantPOSUser(POSUser posUser);
	List<POSUser> getMerchantPOSUsers(String code);
	public List<POSUser> getMerchantPOSUsersActive(String merchantCode);
	boolean resetMerchantPOSUser(POSUser posUser);

	List<Merchant> GetMerchantsById(String LoggedInUser);

	CompasResponse deleteMerchantPOSUser(POSUser posUser);

}

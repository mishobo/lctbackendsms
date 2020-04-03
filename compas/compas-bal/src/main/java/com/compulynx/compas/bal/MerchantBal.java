/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.POSUser;

/**
 * @author Anita
 *
 */
public interface MerchantBal {
	CompasResponse UpdateMerchant(Merchant merchant);
	List<Merchant> GetMerchants();
	Merchant getMerchantById(int id);
	public boolean addMerchantPOSUser(POSUser posUser);
	public boolean editMerchantPOSUser(POSUser posUser);
	public List<POSUser> getMerchantPOSUsers(String merchantCode);
	public List<POSUser> getMerchantPOSUsersActive(String merchantCode);
	public boolean resetMerchantPOSUser(POSUser posUser);
	List<Merchant> GetMerchantsById(String LoggedInUser);
	CompasResponse deleteMerchantPOSUser(POSUser posUser);
}

/**
 * 
 */
package com.compulynx.compas.bal.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.compas.bal.MerchantBal;
import com.compulynx.compas.bal.MobileApiBal;
import com.compulynx.compas.dal.MobileApiDal;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.POSUser;
import com.compulynx.compas.models.android.APIPOSUserAuth;

/**
 * @author Anita
 * Mar 16, 2017
 */
public class MobileApiBalImpl implements MobileApiBal{
	@Autowired
	MobileApiDal mobileApiDal;
	@Autowired
	MerchantBal merchantBal;
	@Override
	public CompasResponse loginPOSUser(APIPOSUserAuth user) {
		System.out.println("User::"+user.getUsername());
		System.out.println("Password::"+user.getPassword());
		System.out.println("Device id::"+user.getDeviceSerialNo());
		boolean done = mobileApiDal.loginPOSUser(user);
		CompasResponse resp=new CompasResponse();
		if(done)
		{
			resp.respCode=200;
		}
		else
		{
			resp.respCode=201;
		}
		return resp;
	}
	public CompasResponse resetPOSUser(POSUser user) {
		boolean done = merchantBal.resetMerchantPOSUser(user);
		CompasResponse resp=new CompasResponse();
		if(done)
		{
			resp.respCode=200;
		}
		else
		{
			resp.respCode=201;
		}
		return resp;
	}

}

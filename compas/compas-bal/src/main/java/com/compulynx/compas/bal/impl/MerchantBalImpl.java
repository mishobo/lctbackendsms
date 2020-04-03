/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.MerchantBal;
import com.compulynx.compas.dal.MerchantDal;
import com.compulynx.compas.dal.operations.LCTMailer;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Merchant;
import com.compulynx.compas.models.POSUser;

/**
 * @author Anita
 *
 */
@Component
public class MerchantBalImpl implements MerchantBal {

	@Autowired
	MerchantDal merchantDal;

	public CompasResponse UpdateMerchant(Merchant merchant) {
		// TODO Auto-generated method stub
		return merchantDal.UpdateMerchant(merchant);
	}

	public List<Merchant> GetMerchants() {
		// TODO Auto-generated method stub
		return merchantDal.GetMerchants();
	}

	public boolean addMerchantPOSUser(POSUser posUser) {
		posUser.setPassword(generatePin());
		LCTMailer lctMailer = new LCTMailer();
		String body = "Account successfully created.<br/>PIN: <b>" + posUser.getPassword()
				+ "</b><br/>Use it to login and change your PIN.";
		boolean done = merchantDal.addMerchantPOSUser(posUser);
		if (lctMailer.sendEmail(posUser.getEmail(), "LCT POS User Created", body)) {
			System.out.println("Email sent on add pos user");
		} else {
			System.out.println("Email not sent on add pos user");
		}
		return done;
	}

	public boolean editMerchantPOSUser(POSUser posUser) {
		LCTMailer lctMailer = new LCTMailer();
		String body = "Account successfully edited.<br/>Contact the administrator for more information.";
		boolean done = merchantDal.editMerchantPOSUser(posUser);
		if (lctMailer.sendEmail(posUser.getEmail(), "LCT POS User Edit", body)) {
			System.out.println("Email sent on edit pos user");
		} else {
			System.out.println("Email not sent on edit pos user");
		}
		return done;
	}

	public List<POSUser> getMerchantPOSUsers(String code) {
		return merchantDal.getMerchantPOSUsers(code);
	}

	public List<POSUser> getMerchantPOSUsersActive(String code) {
		return merchantDal.getMerchantPOSUsersActive(code);
	}

	public Merchant getMerchantById(int id) {
		return merchantDal.getMerchantById(id);
	}

	public boolean resetMerchantPOSUser(POSUser posUser) {
		posUser.setPassword(generatePin());
		LCTMailer lctMailer = new LCTMailer();
		String body = "Account PIN successfully reset.<br/>PIN: <b>" + posUser.getPassword()
				+ "</b><br/>Use it to login to your assigned POS device.";
		boolean done = merchantDal.resetMerchantPOSUser(posUser);
		if (lctMailer.sendEmail(posUser.getEmail(), "LCT POS PIN", body)) {
			System.out.println("Email sent on POS user reset");
		} else {
			System.out.println("Email not sent on POS user reset");
		}
		return done;
	}

	private String generatePin() {
		String strPin = "";
		for (int i = 0; i < 4; i++) {
			Random rand = new Random();
			int num = rand.nextInt(10);
			strPin += num;
		}
		return strPin;
	}

	@Override
	public List<Merchant> GetMerchantsById(String LoggedInUser) {
		// TODO Auto-generated method stub
		return merchantDal.GetMerchantsById(LoggedInUser);
	}

	@Override
	public CompasResponse deleteMerchantPOSUser(POSUser posUser) {
		// TODO Auto-generated method stub
		return merchantDal.deleteMerchantPOSUser(posUser);
	}

}

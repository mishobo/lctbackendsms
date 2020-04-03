/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.ResetBal;
import com.compulynx.compas.dal.impl.ResetDalImpl;

import com.compulynx.compas.models.ResetUser;


/**
 * @author dkabii
 *
 */
@Component
public class ResetBalImpl implements ResetBal{
	@Autowired
	ResetDalImpl ResetDal;
	
	public ResetUser resetUserPassword(String strUser) {
		return ResetDal.resetUserPassword(strUser);
	}

}

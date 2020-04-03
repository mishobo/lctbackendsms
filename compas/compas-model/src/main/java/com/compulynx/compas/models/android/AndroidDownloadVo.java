/**
 * 
 */
package com.compulynx.compas.models.android;

import java.util.List;

import com.compulynx.compas.models.Category;
import com.compulynx.compas.models.Member;
import com.compulynx.compas.models.Programme;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.User;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public class AndroidDownloadVo {

	public List<AndroidUsers> users;
	public AndroidDownloadVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<AndroidCategories> categories1;
	public List<AndroidServices> services;
	public List<Voucher> vouchers;
	public List<AndroidProgrammes> categories;
	public List<AndroidTopup> topupDetails;
	public List<AndroidBnfgrp> bnfGrps;
	public Member member;
	public int respCode;
}

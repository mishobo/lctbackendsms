/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.VoucherBal;
import com.compulynx.compas.dal.impl.AgentDalImpl;
import com.compulynx.compas.dal.impl.VoucherDalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
@Component
public class VoucherBalImpl implements VoucherBal{
	@Autowired
	VoucherDalImpl voucherDal;

	@Override
	public List<Service> GetServiceProducts(String vType) {
		// TODO Auto-generated method stub
		return voucherDal.GetServiceProducts(vType);
	}

	@Override
	public List<Voucher> GetAllVouchers() {
		// TODO Auto-generated method stub
		return voucherDal.GetAllVouchers();
	}

	@Override
	public List<Voucher> GetAllVouchersByOrg(int orgId) {
		// TODO Auto-generated method stub
		return voucherDal.GetAllVouchersByOrg(orgId);
	}

	@Override
	public CompasResponse UpdateVoucher(Voucher voucher) {
		// TODO Auto-generated method stub
		return voucherDal.UpdateVoucher(voucher);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.VoucherBal#GetCashVouchers()
	 */
	@Override
	public List<Voucher> GetCashVouchers() {
		// TODO Auto-generated method stub
		return voucherDal.GetCashVouchers();
	}

}

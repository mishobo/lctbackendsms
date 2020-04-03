/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author shree
 *
 */
public interface VoucherDal {
	List<Service> GetServiceProducts(String vType);
	List<Voucher> GetAllVouchers();
	List<Voucher> GetAllVouchersByOrg(int orgId);
	CompasResponse UpdateVoucher(Voucher voucher);
	List<Voucher> GetCashVouchers();
	
}

/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Voucher;

/**
 * @author Anita
 *
 */
public interface VoucherBal {
	List<Service> GetServiceProducts(String vType);
	List<Voucher> GetAllVouchers();
	List<Voucher> GetAllVouchersByOrg(int orgid);
	CompasResponse UpdateVoucher(Voucher voucher);
	List<Voucher> GetCashVouchers();
}

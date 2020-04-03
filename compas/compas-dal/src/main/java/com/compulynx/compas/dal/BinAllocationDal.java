/**
 * 
 */
package com.compulynx.compas.dal;

import com.compulynx.compas.models.BinAllocation;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author shree
 *
 */
public interface BinAllocationDal {
	CompasResponse UpdateBin(BinAllocation bin);
}

/**
 * 
 */
package com.compulynx.compas.bal;

import com.compulynx.compas.models.BinAllocation;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 *
 */
public interface BInAllocationBal {
	CompasResponse UpdateBin(BinAllocation bin);
}

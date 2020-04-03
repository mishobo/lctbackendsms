package com.compulynx.compas.bal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.BInAllocationBal;
import com.compulynx.compas.dal.impl.BinAllocationDalImpl;
import com.compulynx.compas.models.BinAllocation;
import com.compulynx.compas.models.CompasResponse;

@Component
public class BinAllocationBalImpl implements BInAllocationBal{
	@Autowired
	BinAllocationDalImpl binDal;

	@Override
	public CompasResponse UpdateBin(BinAllocation bin) {
		// TODO Auto-generated method stub
		return binDal.UpdateBin(bin);
	}
}

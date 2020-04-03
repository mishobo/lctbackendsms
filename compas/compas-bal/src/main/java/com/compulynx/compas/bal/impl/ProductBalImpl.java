package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.ProductBal;
import com.compulynx.compas.dal.impl.AgentDalImpl;
import com.compulynx.compas.dal.impl.ProductDalImpl;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Product;

@Component
public class ProductBalImpl implements ProductBal {
	@Autowired
	ProductDalImpl productDal;

	@Override
	public CompasResponse UpdateProduct(Product product) {
		// TODO Auto-generated method stub
		return productDal.UpdateProduct(product);
	}

	@Override
	public List<Product> GetProducts(int orgId) {
		// TODO Auto-generated method stub
		return productDal.GetProducts(orgId);
	}

	@Override
	public List<BeneficiaryGroup> GetBnfGroups(int orgId) {
		// TODO Auto-generated method stub
		return productDal.GetBnfGroups(orgId);
	}

	@Override
	public CompasResponse UpdateBnfGroup(BeneficiaryGroup bnfGrp) {
		// TODO Auto-generated method stub
		return productDal.UpdateBnfGroup(bnfGrp);
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ProductBal#GetScheme(int)
	 */
	@Override
	public List<Product> GetScheme(int orgId) {
		// TODO Auto-generated method stub
		return productDal.GetProducts(orgId);
	}

	@Override
	public Product getProductByProductName(String productName) {
		return productDal.getProductByProductName(productName);
	}

}

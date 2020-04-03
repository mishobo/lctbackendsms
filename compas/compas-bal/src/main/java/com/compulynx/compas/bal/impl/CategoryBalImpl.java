/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.CategoryBal;
import com.compulynx.compas.dal.impl.BranchDalImpl;
import com.compulynx.compas.dal.impl.CategoryDalImpl;
import com.compulynx.compas.models.Category;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 * Aug 24, 2016
 */
@Component
public class CategoryBalImpl implements CategoryBal{
	@Autowired
	CategoryDalImpl categoryDal;

	
	@Override
	public CompasResponse UpdateCategory(Category category) {
		// TODO Auto-generated method stub
		return categoryDal.UpdateCategory(category);
	}

	
	@Override
	public List<Category> GetCategories() {
		// TODO Auto-generated method stub
		return categoryDal.GetCategories();
	}
	

	
}

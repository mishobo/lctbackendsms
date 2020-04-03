/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Category;

/**
 * @author Anita
 *Aug 24, 2016
 */
public interface CategoryDal {
	CompasResponse UpdateCategory(Category category);
	List<Category> GetCategories();
}

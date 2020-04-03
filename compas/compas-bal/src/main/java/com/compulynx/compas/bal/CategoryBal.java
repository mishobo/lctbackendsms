/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Category;
import com.compulynx.compas.models.CompasResponse;

/**
 * @author Anita
 * Aug 24, 2016
 */
public interface CategoryBal {
	CompasResponse UpdateCategory(Category category);

	List<Category> GetCategories();
}

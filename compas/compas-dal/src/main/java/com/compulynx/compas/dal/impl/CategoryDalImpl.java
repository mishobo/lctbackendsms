/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.CategoryDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Category;

/**
 * @author Anita
 *Aug 24, 2016
 */
public class CategoryDalImpl implements CategoryDal{
	private DataSource dataSource;

	public CategoryDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}


	public CompasResponse UpdateCategory(Category category) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (category.categoryId == 0) {
				if (CheckCategoryByCode(category.categoryCode))
				{
					return new CompasResponse(201, "Category Code Already Exists");
				}
				if (CheckCategoryByName(category.categoryName))
				{
					return new CompasResponse(201, "Category Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertCategory);
				preparedStatement.setString(1, category.categoryCode);
				preparedStatement.setString(2, category.categoryName);
				preparedStatement.setBoolean(3, category.active);
				preparedStatement.setInt(4, category.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				
			
			} else {
				if (CheckCategoryNameByCode(category.categoryName,category.categoryCode))
				{
					return new CompasResponse(201, "Category Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCategory);
				preparedStatement.setString(1, category.categoryName);
				preparedStatement.setBoolean(2, category.active);
				preparedStatement.setInt(3, category.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				
				preparedStatement.setInt(5, category.categoryId);
			
			
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			
				return new CompasResponse(404, "Exception Occured");	
			
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckCategoryByCode(String categoryCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCategoryByCode);
			preparedStatement.setString(1, categoryCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckCategoryByName(String categoryName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCategoryByName);
			preparedStatement.setString(1, categoryName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckCategoryNameByCode(String categoryName,String categoryCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCategoryNameByCode);
			preparedStatement.setString(1, categoryName);
			preparedStatement.setString(2, categoryCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Category> GetCategories() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCategories);
			resultSet = preparedStatement.executeQuery();
			List<Category> categories = new ArrayList<Category>();
			while (resultSet.next()) {
				categories.add(new Category(resultSet.getInt("ID"), resultSet
						.getString("category_code"),resultSet
						.getString("category_name"),resultSet.getBoolean("Active"),
						resultSet.getInt("Created_By"), 200,count));
				count++;
			}
			return categories;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}


	

}

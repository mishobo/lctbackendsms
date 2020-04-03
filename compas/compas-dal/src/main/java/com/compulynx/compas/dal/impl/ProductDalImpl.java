package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.ProductDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Area;
import com.compulynx.compas.models.BeneficiaryGroup;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Product;

public class ProductDalImpl implements ProductDal{
	private DataSource dataSource;

	public ProductDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	@Override
	public CompasResponse UpdateProduct(Product product) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (product.productId== 0) {
				if (CheckProductCode(product.productCode,product.orgId))
				{
					return new CompasResponse(201, "Product Code Already Exists");
				}
				if (CheckProductName(product.productName,product.orgId))
				{
					return new CompasResponse(201, "Product Name Already Exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertProduct);
				preparedStatement.setString(1, product.productCode);
				preparedStatement.setString(2, product.productName);
				preparedStatement.setString(3, product.startDate);
				preparedStatement.setString(4, product.endDate);
				preparedStatement.setDouble(5, product.fund);
				preparedStatement.setInt(6, product.orgId);
				preparedStatement.setString(7, product.type);
				preparedStatement.setBoolean(8, product.active);
				preparedStatement.setInt(9, product.createdBy);
				preparedStatement.setString(11, product.insuranceCode);
				preparedStatement.setTimestamp(10, new java.sql.Timestamp(new java.util.Date().getTime()));
			} else {
				if (CheckProductNameByCode(product.productName,product.productCode, product.orgId, product.productId))
				{
					return new CompasResponse(201, "Product Name Already Exists");
				}
				System.out.println("updating product scheme: "+product);
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateProduct);
				preparedStatement.setString(1, product.productCode);
				preparedStatement.setString(2, product.productName);
				preparedStatement.setString(3, product.startDate);
				preparedStatement.setString(4, product.endDate);
				preparedStatement.setDouble(5, product.fund);
				preparedStatement.setString(6, product.type);
				preparedStatement.setBoolean(7, product.active);
				preparedStatement.setInt(8, product.createdBy);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(10, product.insuranceCode);
				preparedStatement.setInt(11, product.productId);
				
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Product Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<Product> GetProducts(int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProducts);
			preparedStatement.setInt(1,orgId);
			resultSet = preparedStatement.executeQuery();
			List<Product> products = new ArrayList<Product>();
			while (resultSet.next()) {
				Product product = new Product();
				product.productId =resultSet.getInt("ID");
				product.productCode = resultSet.getString("product_Code");
				product.productName = resultSet.getString("product_Name");
				product.startDate = resultSet.getString("start_date");
				product.endDate = resultSet.getString("end_date");
				product.fund = resultSet.getDouble("fund");
				product.active = resultSet.getBoolean("Active");
				product.type = resultSet.getString("type");
				product.typeName = resultSet.getString("type_name");
				product.status=resultSet.getString("status");
				System.out.println("Status: " +product.status);
				product.insuranceCode = resultSet.getString("insurance_code");
				product.count = count;
				count++;
				products.add(product);
			}
			return products;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckProductCode(String productCode,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProductByCode);
			preparedStatement.setString(1, productCode);
			preparedStatement.setInt(2, merchantId);
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
	public boolean CheckProductName(String productName,int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getProductByName);
			preparedStatement.setString(1, productName);
			preparedStatement.setInt(2, merchantId);
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
	public boolean CheckProductNameByCode(String productName,String productCode,int orgId, int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProductNameByCode);
			preparedStatement.setString(1, productName);
			preparedStatement.setString(2, productCode);
			preparedStatement.setInt(3, orgId);
			preparedStatement.setInt(4, productId);
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

	public CompasResponse UpdateBnfGroup(BeneficiaryGroup bnfGrp) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (bnfGrp.bnfGrpId== 0) {
				if (CheckBnfGrpCode(bnfGrp.bnfGrpCode,bnfGrp.productId))
				{
					return new CompasResponse(201, "Beneficicary Group Code Already Exists");
				}
				if (CheckBnfGrpName(bnfGrp.productName,bnfGrp.productId))
				{
					return new CompasResponse(201, "Beneficicary Group Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertBnfGrp);
				preparedStatement.setString(1, bnfGrp.bnfGrpCode);
				preparedStatement.setString(2, bnfGrp.bnfGrpName);
				preparedStatement.setInt(3, bnfGrp.productId);
				preparedStatement.setBoolean(4, bnfGrp.active);
				preparedStatement.setInt(5, bnfGrp.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setDouble(7, bnfGrp.houseHoldValue);
				preparedStatement.setInt(8, bnfGrp.minCap);
				preparedStatement.setInt(9, bnfGrp.maxCap);
				preparedStatement.setInt(10, bnfGrp.orgId);
			} else {
				if (CheckBnfGrpNameByCode(bnfGrp.bnfGrpName,bnfGrp.bnfGrpCode,bnfGrp.productId, bnfGrp.bnfGrpId))
				{
					return new CompasResponse(201, "Beneficicary Group Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.updateBnfGrp);
				
				preparedStatement.setString(1, bnfGrp.bnfGrpName);
				preparedStatement.setInt(2, bnfGrp.productId);
				preparedStatement.setBoolean(3, bnfGrp.active);
				preparedStatement.setInt(4, bnfGrp.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setDouble(6, bnfGrp.houseHoldValue);
				preparedStatement.setInt(7, bnfGrp.minCap);
				preparedStatement.setInt(8, bnfGrp.maxCap);
				preparedStatement.setString(9, bnfGrp.bnfGrpCode);
				preparedStatement.setInt(10, bnfGrp.bnfGrpId);
				
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0)
			{
				return new CompasResponse(201, "Product Name Already Exists");
			}
			else
			{
				return new CompasResponse(404, "Exception Occured");	
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public List<BeneficiaryGroup> GetBnfGroups(int orgId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet1 = null;
		int noofbnfs=0;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrps);
			preparedStatement.setInt(1, orgId);
			resultSet = preparedStatement.executeQuery();
			
			
			List<BeneficiaryGroup> bnfgrps = new ArrayList<BeneficiaryGroup>();
			while (resultSet.next()) {
				preparedStatement1=connection.prepareStatement(Queryconstants.getNoOfBnfs);
				preparedStatement1.setInt(1, resultSet.getInt("id"));
				resultSet1=preparedStatement1.executeQuery();
				if(resultSet1.next()){
					noofbnfs=resultSet1.getInt("id");
				}
				bnfgrps.add(new BeneficiaryGroup(resultSet.getInt("ID"),resultSet
						.getString("bnfgrp_Code"), resultSet
						.getString("bnfgrp_Name"),resultSet.getInt("product_ID"),resultSet
						.getString("product_Name"),resultSet.getBoolean("Active"),count,
						false,resultSet.getDouble("house_hold_value"),resultSet.getInt("min_cap"),resultSet.getInt("max_cap"),noofbnfs));
				count++;
			}
			return bnfgrps;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public boolean CheckBnfGrpCode(String bnfGrpCode,int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpByCode);
			preparedStatement.setString(1, bnfGrpCode);
			preparedStatement.setInt(2, productId);
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
	public boolean CheckBnfGrpName(String bnfGrpName,int productId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBnfGrpByName);
			preparedStatement.setString(1, bnfGrpName);
			preparedStatement.setInt(2, productId);
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
	public boolean CheckBnfGrpNameByCode(String bnfGrpName,String bnfGrpCode,int productId, int bnfGrpId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getBnfGrpNameByCode);
			preparedStatement.setString(1, bnfGrpName);
			preparedStatement.setString(2, bnfGrpCode);
			preparedStatement.setInt(3, productId);
			preparedStatement.setInt(4, bnfGrpId);
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
	public Product getProductByProductName(String productName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Product product = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getProductByProductName);
			preparedStatement.setString(1, productName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				product = new Product();
				product.productId =resultSet.getInt("ID");
				product.productCode = resultSet.getString("product_Code");
				product.productName = resultSet.getString("product_Name");
				product.startDate = resultSet.getString("start_date");
				product.endDate = resultSet.getString("end_date");
				product.fund = resultSet.getDouble("fund");
				product.active = resultSet.getBoolean("Active");
				product.type = resultSet.getString("type");
				//product.typeName = resultSet.getString("type_name");
				product.insuranceCode = resultSet.getString("insurance_code");
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
		return product;
	}
}

package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.DashBoardDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.DashBoard;

public class DashBoardDalImpl implements DashBoardDal {

	private DataSource dataSource;

	public DashBoardDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	public boolean isUserAKUH(int userId)
	{
		System.out.println("Check if user is from AKUH...");
		boolean done = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.isUserAKUH);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				done = true;
			}
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			System.out.println("According to db: "+done);
			return done;
		} catch (Exception ex) {
			ex.printStackTrace();
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
			System.out.println("According to db: "+done);
			return done;
		}
	}
	public List<DashBoard> GetDashBoardCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDashBoardDetailCount);
			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getInt("COUNTNO"), resultSet
						.getString("Name"),
						 200));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public DashBoard GetDashBoardCount() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDashBoardDetail);
			resultSet = preparedStatement.executeQuery();
			DashBoard detail = new DashBoard();
			while (resultSet.next()) {
				detail.posUsers = resultSet.getInt("posCount");
				detail.systemUsers = resultSet.getInt("systemCount");
				detail.bnfCount = resultSet.getInt("bnfCount");
				detail.deviceCount = resultSet.getInt("deviceCount");
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<DashBoard> GetDashBoardAgentDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAgentTxns);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getInt("amount"), resultSet
						.getString("agent"),
						 200));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<DashBoard> GetTransChartDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTransChartDetail);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> chartDetail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				chartDetail.add(new DashBoard(resultSet.getInt("Load"),
						resultSet.getInt("Purchase"),
						resultSet.getInt("totalTrans"),
						resultSet
						.getString("month"),
						 200));
			}
			return chartDetail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<DashBoard> GetDashBoardAmountDetail() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getAmountDetails);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
			DashBoard obj =  new DashBoard();
			obj.detailDescription=resultSet.getString("name");
			obj.amount=df.format(resultSet.getDouble("countno"));
			//System.out.println("Amount##"+obj.amount);
			detail.add(obj);
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets the service details for piechart
	 * @return list of services and the percentage of transaction done
	 */
	public List<DashBoard> GetFlowChartCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getFlowChartDetails);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getString("service"), resultSet
						.getInt("trans_count")));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * gets details of total collections
	 * @return list of total collections 
	 */
	public List<DashBoard> GetDashBoardCollectionDetail() {
		DecimalFormat df = new DecimalFormat("##,##,##,##,##,##,##0.00");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getTotalCollectionInfo);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
			DashBoard obj =  new DashBoard();
			obj.detailDescription=resultSet.getString("name");
			obj.amount=df.format(resultSet.getDouble("collection"));
			obj.totalTxns=resultSet.getDouble("collection");
			detail.add(obj);
			}
			
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	@Override
	public boolean IsUserAKUH(int userId) {
		// TODO Auto-generated method stub
		return false;
	}
}

/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Severity;
import javax.sql.DataSource;

import com.compulynx.compas.dal.ServiceDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.SerSubtype;
import com.compulynx.compas.models.Service;
import com.compulynx.compas.models.Params;

import com.compulynx.compas.models.ServiceType;
import com.compulynx.compas.models.StringUtil;

import com.compulynx.compas.models.Uom;

/**
 * @author Anita
 *
 */
public class ServiceDalImpl implements ServiceDal {
	private DataSource dataSource;

	public ServiceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean CheckServiceCode(String serviceCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getServiceByCode);
			preparedStatement.setString(1, serviceCode);

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

	public boolean CheckServiceName(String serviceName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getServiceByName);
			preparedStatement.setString(1, serviceName);
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

	public boolean CheckServiceNameByCode(String serviceName, String serviceCode, int serviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getServiceNameByCode);
			preparedStatement.setString(1, serviceName);
			preparedStatement.setString(2, serviceCode);
			preparedStatement.setInt(3, serviceId);
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
	
	public boolean CheckServiceNameById(String serviceName, int serviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getServiceNameById);
			preparedStatement.setString(1, serviceName);
			preparedStatement.setInt(2, serviceId);
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

	public CompasResponse UpdateService(Service service) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (service.serviceId == 0) {
				if (CheckServiceName(service.serviceName)) {
					return new CompasResponse(201,	"Service Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertHealthServiceDetails);
				preparedStatement.setString(1, genRandInt());
				preparedStatement.setString(2,service.serviceName);
				preparedStatement.setString(3, service.compoType);
				preparedStatement.setInt(4, service.serSubtype);
				preparedStatement.setString(5, service.image);
				preparedStatement.setBoolean(6, service.active);
				preparedStatement.setString(7, service.applicable);
				preparedStatement.setInt(8, service.createdBy);
				preparedStatement.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));

				if (preparedStatement.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(201,	"Failed to Insert Service");
				}
				return new CompasResponse(200, "Records Updated");
			}

			else {
				if (CheckServiceNameById(service.serviceName, service.serviceId)) {
					return new CompasResponse(201,	"Service Name Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.updateHealthServices);
				preparedStatement.setString(1,service.serviceCode);
				preparedStatement.setString(2,service.serviceName);
				preparedStatement.setString(3, service.compoType);
				preparedStatement.setInt(4, service.serSubtype);
				preparedStatement.setString(5, service.image);
				preparedStatement.setBoolean(6, service.active);
				preparedStatement.setString(7, service.applicable);
				preparedStatement.setInt(8, service.createdBy);
				preparedStatement.setTimestamp(9,new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setInt(10, service.serviceId);
				
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Service Name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Service> GetAllServices(String type) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			List<Service> services = new ArrayList<Service>();
			if(type.equalsIgnoreCase("H")){
				preparedStatement = connection.prepareStatement(Queryconstants.getHealthServices);
				//preparedStatement.setString(1, type);
				resultSet = preparedStatement.executeQuery();
				count = 1;
				while (resultSet.next()) {
					Service service = new Service();
					service.serviceId = resultSet.getInt("id");
					service.serviceName = resultSet.getString("Ser_Name");
					service.serviceCode = resultSet.getString("Ser_code");
					service.isActive = resultSet.getBoolean("Active");
					service.applicable = resultSet.getString("applicable");
					service.applicableTo = resultSet.getString("applicable_to");
					service.createdBy = resultSet.getInt("Created_By");
					service.count = count;
					services.add(service);
					count ++;
				}
			}
			
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Service> GetAllSubServices(int serviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			List<Service> services = new ArrayList<Service>();
			if(true){
				preparedStatement = connection.prepareStatement(Queryconstants.getAKUHHealthSubServices);
				preparedStatement.setInt(1, serviceId);
				resultSet = preparedStatement.executeQuery();
				count = 1;
				while (resultSet.next()) {
					Service service = new Service();
					service.serviceId = resultSet.getInt("id");
					service.serviceName = resultSet.getString("Service_Name");
					service.serviceCode = resultSet.getString("Service_code");
					services.add(service);
					count ++;
				}
			}
			
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public List<Params> GetAllParams() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParams);

			resultSet = preparedStatement.executeQuery();
			List<Params> params = new ArrayList<Params>();
			while (resultSet.next()) {
				params.add(new Params(resultSet.getInt("ID"), resultSet
						.getString("ParamName"),
						resultSet.getBoolean("Active"), resultSet
								.getInt("CreatedBy"), 200, count));
				count++;
			}
			return params;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateParam(Params param) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			if (param.paramId == 0) {
				if (checkParamName(param.paramName)) {
					return new CompasResponse(201, "Param Name Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertParamInfo);
				preparedStatement.setString(1, param.paramName);
				preparedStatement.setBoolean(2, param.active);
				preparedStatement.setInt(3, param.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			} else {

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateParamInfo);
				preparedStatement.setString(1, param.paramName);
				preparedStatement.setBoolean(2, param.active);
				preparedStatement.setInt(3, param.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, param.paramId);
			}
			return (preparedStatement.executeUpdate() > 0) ? new CompasResponse(
					200, "Records Updated") : new CompasResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Param name Already Exists");
			} else {
				return new CompasResponse(202, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new CompasResponse(202, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean checkParamName(String paramName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getParamName);
			preparedStatement.setString(1, paramName);

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

	public List<Currency> GetCurrencies() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCurrencies);

			resultSet = preparedStatement.executeQuery();
			List<Currency> services = new ArrayList<Currency>();
			while (resultSet.next()) {
				services.add(new Currency(resultSet.getInt("ID"), resultSet
						.getString("curr_code")));
			}
			return services;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Uom> GetUoms() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUoms);

			resultSet = preparedStatement.executeQuery();
			List<Uom> uoms = new ArrayList<Uom>();
			while (resultSet.next()) {
				uoms.add(new Uom(resultSet.getInt("ID"), resultSet
						.getString("uom_abbr")));
			}
			return uoms;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<ServiceType> GetServiceTypes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceTypes);

			resultSet = preparedStatement.executeQuery();
			List<ServiceType> types = new ArrayList<ServiceType>();
			while (resultSet.next()) {
				types.add(new ServiceType(resultSet.getInt("ID"), resultSet
						.getString("ser_type_code"), resultSet
						.getString("ser_type_name"), resultSet
						.getString("abbr_name")));
				count++;
			}
			return types;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<SerSubtype> GetServiceSubTypes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getServiceSubTypes);

			resultSet = preparedStatement.executeQuery();
			List<SerSubtype> types = new ArrayList<SerSubtype>();
			while (resultSet.next()) {
				types.add(new SerSubtype(resultSet.getInt("ID"), resultSet
						.getString("ser_subtype_code"), resultSet
						.getString("ser_subtype_name")));
				count++;
			}
			return types;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
	public String genRandInt() {
		return "S"+ (int) (Math.random()*899);
	}
}

package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.compulynx.compas.dal.DeviceDal;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.CompasResponse;

import com.compulynx.compas.models.User;

public class DeviceDalImpl implements DeviceDal {
	
	private static final Logger logger = Logger.getLogger(DeviceDalImpl.class.getName());

	private DataSource dataSource;

	public DeviceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public boolean checkDevicedSerialNo(String serialNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDeviceSerialNo);
			preparedStatement.setString(1, serialNo);

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

	public boolean checkDevicedSerialNoById(String serialNo, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDeviceSerialNoById);
			preparedStatement.setString(1, serialNo);
			preparedStatement.setInt(2, id);
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

	public boolean checkUserDeviceSerialNo(int userId, int deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserDeviceSerialNo);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, deviceId);
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
	
	public boolean checkDeviceExists(int regId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDeviceExists);
			preparedStatement.setInt(1, regId);

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

	public boolean checkUserAssignDevice(int deviceId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.checkUserAssignDevice);
			preparedStatement.setInt(1, deviceId);

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

	public CompasResponse UpdateDeviceInfo(Device deviceInfo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement14 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (deviceInfo.active == false) {
				if (checkUserAssignDevice(deviceInfo.regId)) {
					return new CompasResponse(201, "Device Already Assign to user cannot deactivate");
				}
			}
			if (deviceInfo.regId == 0) {
				if (checkDevicedSerialNo(deviceInfo.serialNo)) {
					return new CompasResponse(201, "SerialNo Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.insertDeviceInfo);
				preparedStatement.setString(1, deviceInfo.serialNo);
				preparedStatement.setBoolean(2, deviceInfo.active);
				preparedStatement.setInt(3, deviceInfo.createdBy);
				preparedStatement.setString(4, deviceInfo.imeiNo);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
			} else {
				if (checkDevicedSerialNoById(deviceInfo.serialNo, deviceInfo.regId)) {
					return new CompasResponse(201, "SerialNo Already Exists");
				}
				preparedStatement = connection.prepareStatement(Queryconstants.updateDeviceInfo);
				//preparedStatement.setString(1, deviceInfo.serialNo);
				preparedStatement.setBoolean(1, deviceInfo.active);
				preparedStatement.setInt(2, deviceInfo.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(4, deviceInfo.imeiNo);
				preparedStatement.setInt(5, deviceInfo.regId);
			}
			
			
			if (preparedStatement.executeUpdate() > 0) {
				// insert audit trail
				preparedStatement14 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement14.setInt(1, deviceInfo.createdBy);
				preparedStatement14.setInt(2, deviceInfo.createdBy);
				preparedStatement14.setString(3, "Registered new device: " + deviceInfo.serialNo);
				preparedStatement14.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement14.setString(5, "Device managment module");
				if (preparedStatement14.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(200, "Failed to add audit trail");
				}
				logger.info("Audit Trail for " + deviceInfo.serialNo + "on device management module captured");

				return new CompasResponse(200, "Records Updated");

			} else {
				return new CompasResponse(201, "Nothing To Update");
			}
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Serial No Already Exists");
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

	public List<Device> GetAllDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getDevices);

			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("ID"), resultSet.getString("SerialNo"),
						resultSet.getBoolean("active"), resultSet.getInt("CreatedBy"), resultSet.getString("imeiNo"),
						200));
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Device> GetActiveDevicessInfo(String serialNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			//preparedStatement = connection.prepareStatement(Queryconstants.getUnissuedDevicess);
			preparedStatement.setString(1, serialNo);
			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("ID"), resultSet.getString("SerialNo"),
						resultSet.getBoolean("active"), resultSet.getInt("CreatedBy"), resultSet.getString("imeiNo"),
						200));
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	public List<Device> GetActiveDevicesInfo() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUnissuedDevices);
			resultSet = preparedStatement.executeQuery();
			List<Device> devices = new ArrayList<Device>();
			while (resultSet.next()) {
				devices.add(new Device(resultSet.getInt("ID"), resultSet.getString("SerialNo"),
						resultSet.getBoolean("active"), resultSet.getInt("CreatedBy"), resultSet.getString("imeiNo"),
						200));
			}
			return devices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateIssueDeviceInfo(Device deviceInfo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement14 = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (deviceInfo.regId != -1) {
				/*
				 * if (checkDeviceExists(deviceInfo.regId)) { if
				 * (checkUserDeviceSerialNo(deviceInfo.agentId,deviceInfo.regId)) { return new
				 * CompasResponse(201,
				 * "Agent already exists with device,Return the device and assign the new ones"
				 * ); } else { return new CompasResponse(201, "Device already assign to agent");
				 * } }
				 */

			}
			if (deviceInfo.issueId == 0) {
				preparedStatement = connection.prepareStatement(Queryconstants.insertIssueDevice);
				preparedStatement.setInt(1, deviceInfo.regId);
				preparedStatement.setInt(2, deviceInfo.merchantId);
				preparedStatement.setInt(3, deviceInfo.branchId);
				preparedStatement.setInt(4, deviceInfo.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement.setString(6, deviceInfo.licenseNumber);
				preparedStatement.setInt(7, deviceInfo.posUserId);
				preparedStatement.setString(8, deviceInfo.imeiNo);
			} else {
				// if(deviceInfo.regId!=-1){
				// if (checkDeviceExists(deviceInfo.regId)) {
				// return new MrmResponse(201, "Device already assign to another user");
				// }
				// }
				preparedStatement = connection.prepareStatement(Queryconstants.updateIssueDevice);
				preparedStatement.setInt(1, deviceInfo.regId);
				preparedStatement.setInt(2, deviceInfo.merchantId);
				preparedStatement.setInt(3, deviceInfo.branchId);
				preparedStatement.setInt(4, deviceInfo.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
				//preparedStatement.setInt(6, deviceInfo.posUserId);				
				preparedStatement.setString(6, deviceInfo.imeiNo);
				preparedStatement.setInt(7, deviceInfo.issueId);
			}
			
			if (preparedStatement.executeUpdate() > 0) {
				// insert audit trail
				preparedStatement14 = connection.prepareStatement(Queryconstants.insertAuditTrail);
				preparedStatement14.setInt(1, deviceInfo.createdBy);
				preparedStatement14.setInt(2, deviceInfo.createdBy);
				preparedStatement14.setString(3, "Issued new device to : " + deviceInfo.merchantId);
				preparedStatement14.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
				preparedStatement14.setString(5, "Device Issue module");
				if (preparedStatement14.executeUpdate() <= 0) {
					connection.rollback();
					return new CompasResponse(200, "Failed to add audit trail");
				}
				logger.info("Audit Trail for " + deviceInfo.regId + "on device issuing module captured");

				return new CompasResponse(200, "Records Updated");

			} else {
				return new CompasResponse(201, "Nothing To Update");
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Serial No Already Exists");
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

	public List<Device> GetAllIssueDevicesInfo(int classId, int merchantId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getIssuedDevices);
			// preparedStatement.setInt(1, classId);
			// preparedStatement.setInt(2, merchantId);
			resultSet = preparedStatement.executeQuery();
			List<Device> issueDevices = new ArrayList<Device>();
			while (resultSet.next()) {
				Device device = new Device();
				device.issueId = resultSet.getInt("ID");
				device.regId = resultSet.getInt("deviceRegId");
				device.serialNo = resultSet.getString("SerialNo");
				device.merchantId = resultSet.getInt("merchantId");
				device.merchantName = resultSet.getString("merchantName");
				device.createdBy = resultSet.getInt("CreatedBy");
				device.licenseNumber = resultSet.getString("license");
				device.posUserId = resultSet.getInt("posUserId");
				device.imeiNo = resultSet.getString("imeiNo");
				issueDevices.add(device);
			}
			return issueDevices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<Agent> GetActiveAgents(int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getActiveAgents);
			// preparedStatement.setInt(1, branchId);
			resultSet = preparedStatement.executeQuery();
			List<Agent> agents = new ArrayList<Agent>();
			while (resultSet.next()) {
				Agent objAgent = new Agent();
				objAgent.agentId = resultSet.getInt("ID");
				objAgent.agentDesc = resultSet.getString("AgentDesc");

				agents.add(objAgent);
			}
			return agents;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

}

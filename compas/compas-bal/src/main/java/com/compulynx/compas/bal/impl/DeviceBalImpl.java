/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.DeviceBal;
import com.compulynx.compas.dal.DeviceDal;
import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.CompasResponse;


/**
 * @author Anita
 *
 */
@Component
public class DeviceBalImpl implements DeviceBal{

	@Autowired
	DeviceDal deviceDal;
	
	
	public boolean checkDevicedSerialNo(String serialNo) {
		// TODO Auto-generated method stub
		return deviceDal.checkDevicedSerialNo(serialNo);
	}

	
	public CompasResponse UpdateDeviceInfo(Device deviceInfo) {
		// TODO Auto-generated method stub
		return deviceDal.UpdateDeviceInfo(deviceInfo);
	}

	
	public List<Device> GetAllDevicesInfo() {
		// TODO Auto-generated method stub
		return deviceDal.GetAllDevicesInfo();
	}

	
	public CompasResponse UpdateIssueDeviceInfo(Device deviceInfo) {
		// TODO Auto-generated method stub
		return deviceDal.UpdateIssueDeviceInfo(deviceInfo);
	}

	
	public List<Device> GetAllIssueDevicesInfo(int classId,int merchantId) {
		// TODO Auto-generated method stub
		return deviceDal.GetAllIssueDevicesInfo(classId,merchantId);
	}



	public List<Agent> GetActiveAgents(int branchId) {
		// TODO Auto-generated method stub
		return deviceDal.GetActiveAgents(branchId);
	}


	public List<Device> GetActiveDevicesInfo() {
		// TODO Auto-generated method stub
		return deviceDal.GetActiveDevicesInfo();
	}


	@Override
	public List<Device> GetActiveDevicessInfo(String serialNo) {
		// TODO Auto-generated method stub
		return deviceDal.GetActiveDevicessInfo(serialNo);
	}


	
}

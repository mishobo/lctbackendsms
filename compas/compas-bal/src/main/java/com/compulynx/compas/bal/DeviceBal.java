/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.Agent;
import com.compulynx.compas.models.Device;
import com.compulynx.compas.models.CompasResponse;


/**
 * @author Anita
 *
 */
public interface DeviceBal {


	boolean checkDevicedSerialNo(String serialNo);

	CompasResponse UpdateDeviceInfo(Device deviceInfo);

	List<Device> GetAllDevicesInfo();
	
	CompasResponse UpdateIssueDeviceInfo(Device deviceInfo);
	
	List<Device> GetAllIssueDevicesInfo(int classId,int merchantId);
 List<Agent> GetActiveAgents(int branchId);
	 
	 List<Device> GetActiveDevicesInfo();
	 List<Device> GetActiveDevicessInfo(String serialNo);
	 
}

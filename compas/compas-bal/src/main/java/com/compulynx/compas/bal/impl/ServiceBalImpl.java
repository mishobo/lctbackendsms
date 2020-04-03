package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.ServiceBal;
import com.compulynx.compas.dal.UploadserviceDal;
import com.compulynx.compas.dal.impl.ServiceDalImpl;
import com.compulynx.compas.dal.impl.UploadserviceDalImpl;
import com.compulynx.compas.models.CompasResponse;

import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.SerSubtype;
import com.compulynx.compas.models.Service;

import com.compulynx.compas.models.ServiceType;

import com.compulynx.compas.models.Uom;

@Component
public class ServiceBalImpl implements ServiceBal {

	@Autowired
	ServiceDalImpl serviceDal;
	@Autowired
	UploadserviceDal uploadserviceDal;

	public CompasResponse UpdateService(Service service) {

		return serviceDal.UpdateService(service);
	}

	public List<Service> GetAllServices(String typeId) {

		return serviceDal.GetAllServices(typeId);
	}
	
	public List<Service> GetAllSubServices(int serviceId) {

		return serviceDal.GetAllSubServices(serviceId);
	}
	
	@Override
	 public CompasResponse UploadService(String filePath) {
		return uploadserviceDal.UploadService(filePath);
   }



	@Override
	public List<Currency> GetCurrencies() {
		// TODO Auto-generated method stub
		return serviceDal.GetCurrencies();
	}

	@Override
	public List<Uom> GetUoms() {
		// TODO Auto-generated method stub
		return serviceDal.GetUoms();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ServiceBal#GetServiceTypes()
	 */
	@Override
	public List<ServiceType> GetServiceTypes() {
		// TODO Auto-generated method stub
		return serviceDal.GetServiceTypes();
	}

	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.ServiceBal#GetServiceSubTypes()
	 */
	@Override
	public List<SerSubtype> GetServiceSubTypes() {
		// TODO Auto-generated method stub
		return serviceDal.GetServiceSubTypes();
	}



}

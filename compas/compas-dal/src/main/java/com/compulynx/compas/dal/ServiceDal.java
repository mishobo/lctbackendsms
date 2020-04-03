/**
 * 
 */
package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.Currency;
import com.compulynx.compas.models.Params;
import com.compulynx.compas.models.SerSubtype;
import com.compulynx.compas.models.Service;

import com.compulynx.compas.models.ServiceType;

import com.compulynx.compas.models.Uom;

/**
 * @author Anita
 *
 */
public interface ServiceDal {

	CompasResponse UpdateService(Service service);

	List<Service> GetAllServices(String typeId);
	List<Service> GetAllSubServices(int serviceId);
	List<ServiceType> GetServiceTypes();

	List<Currency> GetCurrencies();

	List<Uom> GetUoms();
	List<SerSubtype> GetServiceSubTypes();
}

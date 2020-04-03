/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class Service {

	public Service(int serviceId, String serviceName, boolean isActive, int createdBy,
			int respCode,double quantity,double serviceValue,String compoName,String compoType) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.quantity=quantity;
		this.serviceValue=serviceValue;
		this.compoName=compoName;
		this.count=count;
		this.compoType=compoType;
		this.ipLimitType = -1;
		this.opLimitType = -1;
		
	}
	public Service(int serviceId, String serviceName, boolean isActive, int createdBy,
			int respCode) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.respCode = respCode;
		
	}
	public Service(int serviceId,String serviceName,double serviceValue
			) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceValue=serviceValue;
		
	}

	public int parentServiceId;
	public int level;
	public boolean hasChild;
	public int serviceId;
	public String serviceCode;
	public String serviceName;
	public double quantity;
	public double price;
	public double serviceValue;
	public boolean active;
	public int createdBy;
	public int respCode;
	public boolean isActive;
	public List<Params> params;
	public String compoType;
	public String compoName;
	public int serSubtype;
	public int count;
	public int categoryId;
	public String serviceDesc;
	public String categoryName;
	public String image;
	public List<Service> serviceDetails;
	public String uom;
	public String serSubtypeName;
	public double limit;
	public double ipLimit;
	public int ipLimitType;
	public int serSubtypeId;
	public String ipLimitPlaceHolder;
	public double opLimit;
	public int opLimitType;
	public String limitDetail;
	public boolean requireAuth;
	public double serviceBalance;
	public double usage;
	public String status;
	public boolean notNewToVoucher;
	public boolean notRemovedFromVoucher;
	public boolean isOld;
	public boolean isRemoved;
	public String applicable;
	public String applicableTo;
	public double coverLimit;
	public double used;
	public double voucherBalance;
	public double coverBalance;
	public Service() {
		super();
	}
	public Service(int respCode) {
		super();
		this.respCode=respCode;
	}

	public Service(int serviceId, String serviceCode, String serviceName,
			boolean active, int createdBy, int respCode,int level,
			boolean hasChild,double serviceValue,int count) {
		super();
		this.serviceId = serviceId;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.level=level;
		this.hasChild=hasChild;
		this.serviceValue=serviceValue;
		
	}

	public Service(int serviceId, String serviceCode, String serviceName,boolean active,
			int createdBy, int respCode,String compoName,String compoType,int count,
			int categoryId,String categoryName,String image,String serviceDesc) {
		super();
		this.serviceId = serviceId;
		this.serviceCode=serviceCode;
		this.serviceName = serviceName;
		this.active=active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.compoName=compoName;
		this.compoType=compoType;
		this.count=count;
		this.categoryId=categoryId;
		this.categoryName=categoryName;
		this.image=image;
		this.serviceDesc=serviceDesc;
				
	}
	public Service(int serviceId,String serviceCode, String serviceName,String compoType, int serSubtype, String image, boolean active,
			int createdBy, int respCode, 
			int count,String serSubtypeName) {
		super();
		this.serviceId=serviceId;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.compoType = compoType;
		this.serSubtype = serSubtype;
		this.image = image;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		
		this.count = count;
		this.serSubtypeName=serSubtypeName;
		
	}

}

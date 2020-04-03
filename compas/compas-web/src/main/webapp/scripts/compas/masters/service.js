/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.service', []).controller("servicesCtrl", ["$scope", "$filter", "serviceSvc","categorySvc","$serviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,serviceSvc,categorySvc, $serviceValid, $rootScope, blockUI, logger, $location) {

	var init;
	$scope.header="";
	$scope.services = [];
	$scope.curSelect="";
	$scope.typeSelect='H';
	$scope.serviceListMode=true;
	$scope.serviceEditMode = true;
	$scope.showUpload = false;
	$scope.priceActive=false;
	$scope.showUOM=true;
	$scope.showCur=true;
	$scope.uomSelect=""
		$scope.serviceDetails=[];
	$scope.serviceDetails=[{serviceCode:'',serviceName:'',image:'',active:true}]
	// $scope.isDisabled=false;
	
	$scope.loadServiceData = function (typeId) {
		$scope.services = [], $scope.searchKeywords = "", $scope.filteredServices = [], $scope.row = "";
		serviceSvc.GetServices(typeId).success(function (response) {
			$scope.serviceListMode=false;
			return $scope.services = response, $scope.searchKeywords = "", $scope.filteredServices = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageServices = $scope.filteredServices.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredServices = $filter("filter")($scope.services, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredServices = $filter("orderBy")($scope.services, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	
	$scope.loadSubServiceData = function (servId) {
		$scope.subServices = [], $scope.searchSubKeywords = "", $scope.filteredSubServices = [], $scope.subRow = "";
		serviceSvc.GetSubServices(servId).success(function (response) {
			//$scope.serviceListMode=false;
			//console.log('sub-services: ',response);
			return $scope.subServices = response, $scope.searchSubKeywords = "", $scope.filteredSubServices = [], $scope.subRow = "", $scope.subSelect = function (page) {
				var subEnd, subStart;
				return subStart = (page - 1) * $scope.numPerSubPage, subEnd = subStart + $scope.numPerSubPage, $scope.currentSubPageServices = $scope.filteredSubServices.slice(subStart, subEnd)
			}, $scope.onFilterSubChange = function () {
				return $scope.subSelect(1), $scope.currentSubPage = 1, $scope.subRow = ""
			}, $scope.onNumPerSubPageChange = function () {
				return $scope.subSelect(1), $scope.currentSubPage = 1
			}, $scope.onOrderSubChange = function () {
				return $scope.subSelect(1), $scope.currentSubPage = 1
			}, $scope.subSearch = function () {
				return $scope.filteredSubServices = $filter("filter")($scope.subServices, $scope.searchSubKeywords), $scope.onFilterSubChange()
			}, $scope.subOrder = function (rowName) {
				return $scope.subRow !== rowName ? ($scope.subRow = rowName, $scope.filteredSubServices = $filter("orderBy")($scope.subServices, rowName), $scope.onOrderSubChange()) : void 0
			}, $scope.numPerSubPageOpt = [3, 5, 10, 20], $scope.numPerSubPage = $scope.numPerSubPageOpt[2], $scope.currentSubPage = 1, $scope.currentSubPages = [], (init = function () {
				return $scope.subSearch(), $scope.subSelect($scope.currentSubPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	$scope.loadServiceData('H');
	$scope.cancelUpload = function(){
		$scope.myFile = null;
		$scope.serviceListMode=false;
		$scope.showUpload=false;
	}
	
	$scope.initUpload = function () {
		$scope.showUpload = true;
		$scope.serviceListMode=true;
		$scope.serviceEditMode = true;
	}
	$scope.loadCategoryData = function () {
		$scope.categories = []
		categorySvc.GetCategories().success(function (response) {
			$scope.categories = response
		})
	}
	$scope.loadCategoryData()
	$scope.loadCurrencies = function () {
		$scope.currencies = [];
		serviceSvc.GetCurrencies().success(function (response) {
			$scope.currencies = response
		})
	}

	$scope.loadUoms = function () {
		$scope.uoms = [];
		serviceSvc.GetUoms().success(function (response) {
			$scope.uoms = response
		})
	}
	$scope.loadUoms();
	$scope.loadSerSubtypes = function () {
		$scope.subTypes = [];
		serviceSvc.GetSerSubType().success(function (response) {
			console.log(response)
			$scope.subTypes = response
		})
	}
	$scope.loadSerSubtypes ()
	$scope.loadSerTypes = function () {
		$scope.types = [];
		serviceSvc.GetSerTypes().success(function (response) {
			$scope.types = response;
		})
	}
	$scope.loadSerTypes();

	$scope.onSerTypeChange=function(typeId){
		if(typeId!=null){
			$scope.loadServiceData(typeId);

			$scope.serviceListMode=false;}
	}

	$scope.editService = function (service) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the service.");
			return false;
		}
		//console.log("editing service: ",service);
		$scope.loadSubServiceData(service.serviceId);
		$scope.header="Edit Service";
		$scope.serviceEditMode = false;
		$scope.serviceListMode=true;
		$scope.isExisting = true;
		$scope.serviceId = service.serviceId;
		$scope.serviceCode = service.serviceCode;
		$scope.serviceName = service.serviceName;
		$scope.applicable = service.applicable;
		$scope.active = service.isActive;
		$scope.categorySelect=service.categoryId;
		if(service.compoType=="D"){

			$scope.typeSelect=1
			$scope.showUOM=false;
			$scope.showCur=true;
			$scope.uomSelect=service.compoName;

		}else if(service.compoType=="CA"){
			$scope.typeSelect=2
			$scope.showUOM=true;
			$scope.showCur=false;
			$scope.curSelect=service.compoName;
		}else if(service.compoType=="H"){
			$scope.typeSelect="H"

				$scope.serSubtypeSelect=service.serSubtype;
		}else{
			$scope.uomSelect="";
			$scope.curSelect="";
			$scope.showUOM=true;
			$scope.showCur=true;
		}
		$scope.myCroppedImage=service.image
		$scope.serviceDesc=service.serviceDesc
	};




	$scope.addService = function () {
		$scope.header="Create Service";
		$scope.serviceEditMode = false;
		$scope.serviceListMode=true;
		$scope.isExisting = true;
		$scope.serviceId = 0;
		$scope.serviceCode = "";
		$scope.applicable = null;
		$scope.serviceName = "";
		$scope.serSubtypeSelect="";
		$scope.active = true;
		// $scope.isDisabled = true;
		//$scope.serviceSelect="";
		//$scope.typeSelect="";
		$scope.showUOM=true;
		$scope.showCur=true;
		$scope.categorySelect="";
		$('#icon').removeAttr('src')
		$scope.serviceDesc=""
			$scope.serviceDetails=[];
		$scope.serviceDetails=[{serviceCode:'',serviceName:'',image:'',active:false}]

	};
	

	$scope.cancelService = function () {
		$scope.serviceEditMode = false;
		$scope.active = false;
		$scope.applicable = null;
		$scope.serviceSelect=0;
		$scope.serviceListMode = false;
		$scope.serviceEditMode = true;
		$scope.serviceSelect="";
		$scope.isExisting = false;
		$scope.typeSelect="";
		$scope.showUOM=true;
		$scope.showCur=true;
		$scope.uomSelect=0;
		$scope.curSelect=0;
		$scope.categorySelect="";
		$scope.myCroppedImage=""
			$('#icon').removeAttr('src')
			$scope.serviceDesc=""
				$scope.serSubtypeSelect="";
	}
	/**
	 * addes the texbox dynamically for location name,location code
	 */
	$scope.addElement = function() {
		if($scope.typeSelect=="H"){
			/*if (!$serviceValid($scope.serSubtypeSelect)) {
				logger.logWarning("Opss! You may have skipped specifying the service's Subtype. Please try again.")
				return false;
			}*/
		}else if($scope.typeSelect=="D"){
			if (!$serviceValid($scope.uomSelect)) {
				logger.logWarning("Opss! You may have skipped specifying the service's UOM. Please try again.")
				return false;
			}
		}else if($scope.typeSelect=="C"){
			if (!$serviceValid($scope.currSelect)) {
				logger.logWarning("Opss! You may have skipped specifying the service's Currency. Please try again.")
				return false;
			}
		}
			$scope.serviceDetails.push({serviceCode:'',serviceName:'',image:'',active:false })
		
		//console.log($scope.serviceDetails);
	}
	$scope.updService = function () {
		var service = {};
			if (!$serviceValid($scope.serviceName)) {
				logger.logWarning("Opss! You may have skipped specifying the service name. Please try again.")
				return false;
			}
			if (!$scope.applicable) {
				logger.logWarning("Opss! You may have skipped who is the service is applicable to. Please try again.")
				return false;
			}
			service.serviceId = $scope.serviceId;
			service.serviceCode=$scope.serviceCode;
			service.serviceName=$scope.serviceName;
			service.applicable=$scope.applicable;
			service.active = $scope.active;


		if($scope.typeSelect=="H"){
			service.compoType="H";
			service.serSubtype=0;
		}else if($scope.typeSelect=="C"){
			service.compoType="C";
			service.compoName=$scope.curSelect;
		}
		//location.service=$scope.areaSelect;
		service.image=$scope.myCroppedImage;
		service.active = $scope.active;
		service.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		serviceSvc.UpdService(service).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Service information was saved succesfully")

				$scope.serviceId = 0;
				$scope.serviceCode = "";
				$scope.serviceName = "";
				$scope.applicable = "";
				$scope.active = false;
				$scope.serviceEditMode = true;
				$scope.serviceListMode = false;
				$scope.serviceSelect=0;
				$scope.priceActive=false;
				$scope.loadServiceData($scope.typeSelect);
				$scope.serviceValue="";
				$scope.isExisting=false;
				$scope.serSubtypeSelect="";
				//$scope.typeSelect="";
				$scope.showUOM=true;
				$scope.showCur=true;
				$scope.serviceDesc="";
				$scope.uomSelect=0;
				$scope.curSelect=0;
				$scope.serviceDetails=[{serviceCode:'',serviceName:'',image:'',active:false}]

			}
			else  {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("OOps! You may have skipped to specify who the service is applicable to.")
			blockUI.stop();
		});
	};
	
    $scope.uploadServices = function () {
    	if(!$scope.applicable){
    		logger.logWarning("Oh snap! There is a problem with the server, please contact the adminstrator.")
    		return false;
    	}
        var file = $scope.myFile;
        blockUI.start()
        serviceSvc.uploadService(file,$rootScope.UsrRghts.sessionId).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Services Uploaded successfully")
                $scope.loadServiceData('H');
                $scope.myFile = null;
    			$scope.showUpload=false;
                $scope.serviceListMode = false;
    			$scope.myFile = null;
            } else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };

//	var handleFileSelect=function(evt) {
//	$scope.showCamera=true;
//	$scope.capPic=false;
//	$scope.showImage=false;
//	var file=evt.currentTarget.files[0];
//	var reader = new FileReader();
//	reader.onload = function (evt) {
//	$scope.$apply(function($scope){
//	$scope.myCroppedImage=evt.target.result;
//	});
//	};
//	reader.readAsDataURL(file);
//	if($(this).get(0).files.length > 0){ // only if a file is selected
//	var fileSize = $(this).get(0).files[0].size;
//	console.log(fileSize);
//	}
//	};
//	angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);

} ]).factory('serviceSvc', function ($http) {
	var EvServiceSvc = {};

	EvServiceSvc.GetServices = function (typeId) {
		return $http({
			url: '/compas/rest/service/gtServices/'+typeId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetSubServices = function (serviceId) {
		return $http({
			url: '/compas/rest/service/gtSubServices/'+serviceId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetParameter = function ($scope) {
		return $http({
			url: '/compas/rest/service/gtParams',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	EvServiceSvc.UpdService = function (service) {
		return $http({
			url: '/compas/rest/service/updService',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: service
		});
	};

	EvServiceSvc.GetCurrencies = function () {
		return $http({
			url: '/compas/rest/service/gtCurrencies/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetUoms = function () {
		return $http({
			url: '/compas/rest/service/gtUoms/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetSerTypes = function () {
		return $http({
			url: '/compas/rest/service/gtSerTypes/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	EvServiceSvc.GetSerSubType = function () {
		return $http({
			url: '/compas/rest/service/gtSerSubTypes/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	
	EvServiceSvc.uploadService = function (file, createdBy) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('createdBy',createdBy);   
        return $http({
            url: '/compas/rest/upload/uploadService',
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };

	return EvServiceSvc;
}).factory('$serviceValid', function () {
	return function (valData) {
		if (angular.isUndefined(valData))
			return false;
		else {
			if (valData == null)
				return false;
			else {
				if (valData.toString().trim() == "")
					return false;
				else
					return true;
			}
		}
		return false;
	};
}) ;
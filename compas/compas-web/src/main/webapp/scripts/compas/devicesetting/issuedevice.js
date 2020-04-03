/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.issueDevice', []).controller("issueDeviceCtrl", ["$scope", "$filter", "issueDeviceSvc","agentSvc","deviceSvc","merchantSvc","userSvc","branchSvc","$posUserValid","$issueDeviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,issueDeviceSvc,agentSvc,deviceSvc,merchantSvc,userSvc,branchSvc,$posUserValid,$issueDeviceValid, $rootScope, blockUI, logger, $location) {
	
    var init;
    $scope.classes = [];
	$scope.issueDevices = [];
	 $scope.merchantListMode = false;
	 $scope.editMode=true;
	 $scope.issuedDevice = {};
	 $scope.posUser = {};
	 $scope.merchant = {};
	   
	    $scope.loadIssueDeviceData = function () {
	        $scope.issueDevices = [], $scope.searchKeywords = "", $scope.filteredIssueDevices = [], $scope.row = "", $scope.issueDeviceEditMode = false;
	        issueDeviceSvc.GetIssueDevices($scope).success(function (response) {
	        	console.log("Devices to issue: "+JSON.stringify(response));
	            return $scope.issueDevices = response, $scope.searchKeywords = "", $scope.filteredIssueDevices = [], $scope.row = "", $scope.select = function (page) {
	                var end, start;
	                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageIssueDevices = $scope.filteredIssueDevices.slice(start, end)
	            }, $scope.onFilterChange = function () {
	                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
	            }, $scope.onNumPerPageChange = function () {
	                return $scope.select(1), $scope.currentPage = 1
	            }, $scope.onOrderChange = function () {
	                return $scope.select(1), $scope.currentPage = 1
	            }, $scope.search = function () {
	                return $scope.filteredIssueDevices = $filter("filter")($scope.issueDevices, $scope.searchKeywords), $scope.onFilterChange()
	            }, $scope.order = function (rowName) {
	                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredIssueDevices = $filter("orderBy")($scope.issueDevices, rowName), $scope.onOrderChange()) : void 0
	            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
	                return $scope.search(), $scope.select($scope.currentPage)
	            })();
	        }).error(function (data, status, headers, config) {
	            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
	        });
	    }
	  $scope.loadMerchantData = function () {
	        $scope.merchants = [];
	        issueDeviceSvc.GetMerchants().success(function (response) {
	            $scope.merchants = response;
	        });
	  }
	   
//	    $scope.$watch("merchantSelect", function (newValue, oldValue) {
//	        if ($issueDeviceValid(newValue)) {
//	        	$scope.merchant = $scope.merchants[newValue-1];
//	        	console.log("new merchant value: ",newValue,"\nmerchant: ",$scope.merchants[newValue-1]);
//	            if (newValue != oldValue) {
//	                $scope.linkId = $scope.merchantSelect;
//	                 $scope.loadBranchData ();
//	                 $scope.loadPOSUserData();//($scope.merchants[newValue-1].merchantCode, $scope.issuedDevice);
//	            }
//	        }
//	        else
//	            $scope.groups = [];
//	    });
	  
	    $scope.$watch("merchantSelect", function (newValue, oldValue) {
	    	$scope.merchant = $scope.merchants[newValue-1];
	    	console.log("Merchant Code : "+ newValue);
	        if ($issueDeviceValid(newValue)) {
	            if (newValue != oldValue) {
	                $scope.linkId = $scope.merchantSelect;	     
	                $scope.loadPOSUserData(newValue,$scope.issuedDevice);
	            }
	        }
	        else
	            $scope.users = [];
	    });
	    $scope.$watch("branchSelect", function (newValue, oldValue) {
	        if ($issueDeviceValid(newValue)) {
	            if (newValue != oldValue) {
	                $scope.branchId = $scope.branchSelect;
	                $scope.loadAgentData();
	            }
	        }
	        else
	            $scope.users = [];
	    });
	    
//	    $scope.$watch("deviceSelect", function (newValue, oldValue) {
//	        if ($issueDeviceValid(newValue)) {
//	            if (newValue != oldValue) {
//	                $scope.loadDeviceData(newValue);
//	            }
//	        }
//	        else
//	            $scope.users = [];
//	    });
	    
    $scope.loadIssueDeviceData();
    $scope.loadMerchantData();
    $scope.loadBranchData = function () {
        $scope.branches = [];
        branchSvc.GetBranches($scope).success(function (response) {
            return $scope.branches = response;
        });
  }
    $scope.loadAgentData = function () {
        $scope.agents = [];
        issueDeviceSvc.GetActiveAgents($scope.branchSelect).success(function (response) {
            $scope.agents = response;
        });
    }
    $scope.loadAgentData();
   // $scope.loadUserData();
    
    $scope.loadPOSUserData = function (merchantCode, issuedDevice) {
       // $scope.loadPOSUserData = function (merchantCode) {
        	
        $scope.posUsers = [];
        var ken="SP"+merchantCode
        issueDeviceSvc.getActivePOSUsers(ken, issuedDevice).success(function (response) {
     
            console.log("Active users: ",response,"\nissuedDevice: ",$scope.issuedDevice);
            $scope.posUsers = response;
            if($scope.issuedDevice.posUserId!=0)
            {
            	const currDeviceUser = $scope.posUsers.filter(function(auser) { return auser.id===$scope.issuedDevice.posUserId;  });
            	if(currDeviceUser.length===1) { $scope.username = currDeviceUser[0].username; }
            	console.log("current device user: ",currDeviceUser,"\nposUser: ",$scope.username);
            }
        	return $scope.posUsers;
        })
    };
    
    $scope.loadDeviceData = function (newValue) {
        $scope.devices = [];
        issueDeviceSvc.GetActiveDevices($scope).success(function (response) {
            return $scope.devices = response
        })
    }
    $scope.loadDeviceData();
    $scope.loadLicense = function () {
        $scope.licenses = [];
        issueDeviceSvc.GetLicense().success(function (response) {
            return $scope.licenses = response
            console.log($scope.licenses);
        })
    }
    $scope.loadLicense();
    $scope.editIssueDevice = function (issueDevice) {
    	$scope.editMode=false;
    	  $scope.selectMode=false;
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Device Issue Info.");
    		return false;
    	}
        $scope.issueDeviceEditMode = true;
        $scope.isExisting = true;
        $scope.issueId = issueDevice.issueId;
        console.log($scope.issueId," issue id")
        if( $scope.regId=="-1"){
        	 $scope.rdReturnDevice = "RD";
        	 $scope.selectDevice=true;
        }else{
        	$scope.deviceSelect=issueDevice.regId;
        	$scope.rdNewDevice="ND"
        	$scope.selectDevice=false;
        }
        $scope.merchantSelect = issueDevice.merchantId;
        $scope.branchSelect = issueDevice.branchId;
        $scope.licenseSelect=issueDevice.licenseNumber;
        $scope.serialNo=issueDevice.serialNo;
        $scope.merchantName=issueDevice.merchantName;
        console.log("Merchant name "+issueDevice.merchantName);
        $scope.issuedDevice = issueDevice;
        $scope.imeiNo = issueDevice.imeiNo;
        console.log($scope.imeiNo,"//////");
     //   $scope.loadPOSUserData($scope.merchant.merchantCode,$scope.issuedDevice);
        $scope.loadPOSUserData();
        console.log("Here is the posUser. posUser: ",$scope.posUser);
    };
    
    $scope.showDeviceSelect=function(mode){
    	if(mode=="RD"){
    		$scope.selectDevice=true;
    	}
    	else
    	{
    		$scope.selectDevice=false;
    	}
    }
 
    $scope.addIssueDevice = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create Issue Device Info.");
    		return false;
    	}
    	$scope.isExisting = false;
        $scope.issueDeviceEditMode = true;
        $scope.issueId = 0;
        $scope.serialNo= "";
        $scope.merchantId =0;
        $scope.selectDevice=false;
        $scope.selectMode=true;
        $scope.licenseSelect="";
        $scope.serialNo="";
        $scope.merchantName="";
        $scope.editMode=true;
        $scope.loadAgentData();
        $scope.loadDeviceData ();
    };

    $scope.cancelIssueDevice= function () {
        $scope.issueDeviceEditMode = false;
        $scope.issueId = 0;
        $scope.serialNo= "";
        $scope.deviceSelect=0;
        $scope.imeiNoSelect=0;
        $scope.merchantSelect=0;
        $scope.isExisting = false;
        $scope.merchant = {};
    }
    

    $scope.updIssueDevice = function () {
        var issueDevice = {};
        var posUserAssigned = {};
        /*
        if (!$posUserValid($scope.posSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the POS user. Please try again.");
            return false;
        }*/
        if(!$issueDeviceValid($scope.deviceSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Serial No. Please try again.")
            return false;
        }
//        if (!$issueDeviceValid($scope.branchSelect)) {
//            logger.logWarning("Opss! You may have skipped specifying the Branch Name. Please try again.")
//            return false;
//        }
        if($scope.rdNewDevice!="RD"){
        if (!$issueDeviceValid($scope.merchantSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Service provider. Please try again.")
            return false;
        }}
//        if (!$issueDeviceValid($scope.imeiNoSelect)) {
//            logger.logWarning("Opss! You may have skipped specifying the IMEI No. Please try again.")
//            return false;
//        }
        if (!$issueDeviceValid($scope.issueId))
        	issueDevice.issueId = 0;
        else
        	issueDevice.issueId = $scope.issueId;
      // if($scope.rdNewDevice=="ND"){
        	  issueDevice.regId = $scope.deviceSelect;
        	  console.log(issueDevice.regId)
        	  issueDevice.merchantId = $scope.merchantSelect;
        	  
        	  console.log("merchant id 2 "+$scope.merchantSelect);
        	  issueDevice.branchId = $scope.branchSelect;
        	  issueDevice.licenseNumber = $scope.licenseSelect;
//        	  posUserAssigned.id = $scope.posUserSelect;
//        	  issueDevice.posUserId = parseInt(posUserAssigned.id);
        	  issueDevice.posUserId=$scope.posUserSelect;
        	  issueDevice.regId = $scope.imeiNoSelect;

        	  console.log(issueDevice.posUserId,"issueDevice.posUserId");
        	  //issueDevice.imeiNo=$scope.imeiNo;
        	  console.log("device: ", issueDevice,"\nposUser: ",posUserAssigned,"\nusers: ",$scope.posUsers);
     // }
        	 
      if($scope.rdNewDevice=="RD"){
        	 issueDevice.regId="-1";
        	 issueDevice.merchantId=0;
        }
      
        $scope.rdNewDevice = "";
        issueDevice.createdBy =  $rootScope.UsrRghts.sessionId;
        blockUI.start();
        console.log("updating device: ",issueDevice);
        issueDeviceSvc.UpdIssueDevice(issueDevice).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The device was issued succesfully to the user")
                $scope.issueId = 0;
                $scope.serialNo = "";
                $scope.userName = "";
                $scope.merchantSelect="";
                $scope.deviceSelect="";
                $scope.posUserSelect = "";
                $scope.imeiNoSelect="";
                $scope.issueDeviceEditMode = false;
                $scope.loadIssueDeviceData();
                $scope.loadDeviceData ();
            }
            else  {
                logger.logWarning(response.respMessage);
            }
           
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

} ]).factory('issueDeviceSvc', function ($http) {
    var compasIssueDeviceSvc = {};
    compasIssueDeviceSvc.GetIssueDevices = function ($scope) {
        return $http({
            url: '/compas/rest/device/gtIssueDevices/'+0+ ',' +0,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    compasIssueDeviceSvc.UpdIssueDevice = function (issueDevice) {
        return $http({
            url: '/compas/rest/device/updIssueDevice',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: issueDevice
                
        });
    };
    
    compasIssueDeviceSvc.getActivePOSUsers = function (merchantCode) {
        return $http({
            url: '/compas/rest/device/getActivePOSUsers/'+merchantCode,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    compasIssueDeviceSvc.GetActiveDevices = function ($scope) {
        return $http({
            url: '/compas/rest/device/gtActiveDevices/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    compasIssueDeviceSvc.GetActiveDevicess = function (serialNo) {
        return $http({
            url: '/compas/rest/device/gtActiveDevicess/'+serialNo,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    compasIssueDeviceSvc.GetActiveAgents = function (branchId) {
        return $http({
            url: '/compas/rest/device/gtActiveAgents/'+0,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    compasIssueDeviceSvc.GetMerchants = function () {
        return $http({
            url: '/compas/rest/merchant/gtMerchants/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };
    
    compasIssueDeviceSvc.GetLicense = function () {
        return $http({
            url: '/compas/rest/device/gtLicense',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    return compasIssueDeviceSvc;
}).factory('$posUserValid', function () {
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
}).factory('$issueDeviceValid', function () {
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
});
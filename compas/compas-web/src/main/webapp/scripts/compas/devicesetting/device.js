/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.device', []).controller("deviceCtrl", ["$scope", "$filter", "deviceSvc","$deviceValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,deviceSvc, $deviceValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.devices = [];

    $scope.loadDeviceData = function () {
        $scope.devices = [], $scope.searchKeywords = "", $scope.filteredDevices = [], $scope.row = "", $scope.deviceEditMode = false;
        deviceSvc.GetDevices($scope).success(function (response) {
            return $scope.devices = response, $scope.searchKeywords = "", $scope.filteredDevices = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageDevices = $scope.filteredDevices.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredDevices = $filter("filter")($scope.devices, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredDevices = $filter("orderBy")($scope.devices, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
	var response ={};
	var request="0?NA";
	//blockUI.start();
	if ("WebSocket" in window) {
		console.log("WebSocket is supported by your Browser!");

		// Let us open a web socket
		//var ws = new WebSocket("ws://localhost:9998/echo");
		var ws = new WebSocket("ws://localhost:8200/");

		ws.onopen = function() {
			// Web Socket is connected, send data using send()
			console.log("Message to send");
			ws.send(request);
			console.log("Message is sent...");
		};

		ws.onmessage = function(evt) {
			response = JSON.parse(evt.data);
			console.log("Response##"+response);
			
				logger.logSuccess('Web socket called!!');
				
			
			console.log("Message is received...");
			//console.log(received_msg);
		};

		ws.onclose = function() {
			// websocket is closed.
			console.log("Connection is closed...");				  
		};
	} else {
		// The browser doesn't support WebSocket
		console.log("WebSocket NOT supported by your Browser!");
	}
 
    $scope.loadDeviceData();

  
    $scope.editDevice = function (device) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Device Info.");
    		return false;
    	}
        $scope.deviceEditMode = true;
        $scope.isExisting = true;
        $scope.regId = device.regId;
        $scope.serialNo = device.serialNo;
        $scope.active = device.active;
        $scope.imeiNo=device.imeiNo;
    };
    
    $scope.addDevice = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create Device Info.");
    		return false;
    	}
        $scope.deviceEditMode = true;
        $scope.regId = 0;
        $scope.serialNo= "";
        $scope.imeiNo="";
        $scope.active = true;
    };

    $scope.cancelDevice= function () {
        $scope.deviceEditMode = false;
        $scope.regId = 0;
        $scope.serialNo= "";
        $scope.imeiNo="";
        $scope.active = false;
    }
    

    $scope.updDevice = function () {
        var device = {};

     
        if (!$deviceValid($scope.serialNo)) {
            logger.logWarning("Opss! You may have skipped specifying the Serial No. Please try again.")
            return false;
        }
        if (!$deviceValid($scope.imeiNo)) {
            logger.logWarning("Opss! You may have skipped specifying the IMEI No. Please try again.")
            return false;
        }
      
      
       
        if (!$deviceValid($scope.regId))
            device.regId = 0;
        else
        	device.regId = $scope.regId;
        device.serialNo = $scope.serialNo;
        device.imeiNo=$scope.imeiNo;
        device.active = $scope.active;
        device.createdBy =  $rootScope.UsrRghts.sessionId;
        blockUI.start()
        deviceSvc.UpdDevice(device).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The device information was saved succesfully")
                $scope.regId = 0;
                $scope.serialNo = "";
                $scope.imeiNo="";
                $scope.active = false;
                $scope.deviceEditMode = false;
                $scope.loadDeviceData();
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

} ]).factory('deviceSvc', function ($http) {
    var compasDeviceSvc = {};
    compasDeviceSvc.GetDevices = function ($scope) {
        return $http({
            url: '/compas/rest/device/gtDevices',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    compasDeviceSvc.UpdDevice = function (device) {
    	console.log(device);
        return $http({
            url: '/compas/rest/device/updDevice',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: device
                
        });
    };
    return compasDeviceSvc;
}).factory('$deviceValid', function () {
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
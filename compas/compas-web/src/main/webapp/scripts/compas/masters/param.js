/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.param', []).controller("paramCtrl", ["$scope", "$filter", "paramSvc","$paramValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,paramSvc, $paramValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.params = [];

    $scope.loadParamData = function () {
        $scope.params = [], $scope.searchKeywords = "", $scope.filteredParam = [], $scope.row = "", $scope.deviceEditMode = false;
        paramSvc.GetParams($scope).success(function (response) {
            return $scope.params = response, $scope.searchKeywords = "", $scope.filteredParam = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageParam = $scope.filteredParam.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredParam = $filter("filter")($scope.params, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredParam = $filter("orderBy")($scope.params, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    
 
    $scope.loadParamData();

  
    $scope.editParam = function (param) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Param.");
    		return false;
    	}
        $scope.paramEditMode = true;
        $scope.isExisting = true;
        $scope.paramId = param.paramId;
        $scope.paramName = param.paramName;
        $scope.active = param.active;
        
    };
    
    $scope.addParam = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create Param");
    		return false;
    	}
        $scope.paramEditMode = true;
        $scope.paramId = 0;
        $scope.paramName= "";
        $scope.active = false;
    };

    $scope.cancelParam= function () {
        $scope.paramEditMode = false;
        $scope.paramId = 0;
        $scope.paramName= "";
        $scope.active = false;
    }
    

    $scope.updParam = function () {
        var param = {};

     
        if (!$paramValid($scope.paramName)) {
            logger.logWarning("Opss! You may have skipped specifying the Param. Please try again.")
            return false;
        }
      
       
        if (!$paramValid($scope.paramId))
        	param.paramId = 0;
        else
        	param.paramId = $scope.paramId;
        param.paramName = $scope.paramName;
        param.active = $scope.active;
        param.createdBy =  $rootScope.UsrRghts.sessionId;
        blockUI.start()
        paramSvc.UpdParam(param).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Param information was saved succesfully")
                if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                	localStorageService.clearAll();
                	loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
                			{
                		localStorageService.set('rxr', rightLst);
                		$rootScope.UsrRghts = rightLst;
                			}).error(function (data, status, headers, config) {
                				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                			});
                }
                $scope.paramId = 0;
                $scope.paramName = "";
                $scope.active = false;
                $scope.paramEditMode = false;
                $scope.loadParamData();
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

} ]).factory('paramSvc', function ($http) {
    var paramDeviceSvc = {};
    paramDeviceSvc.GetParams = function ($scope) {
        return $http({
            url: '/compas/rest/service/gtParams',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    paramDeviceSvc.UpdParam = function (device) {
        return $http({
            url: '/compas/rest/service/updParam',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: device
                
        });
    };
    return paramDeviceSvc;
}).factory('$paramValid', function () {
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
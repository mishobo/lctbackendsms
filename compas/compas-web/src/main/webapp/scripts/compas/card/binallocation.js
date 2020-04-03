/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.bin', []).controller("binCtrl", ["$scope", "$filter", "binSvc","programmeSvc","organizationSvc","$locationValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,binSvc,programmeSvc,organizationSvc, $locationValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.walletDetails = [];
	$scope.allItemsSelected = false;
	 $scope.selection=[];
//    $scope.loadLocationData = function () {
//        $scope.walletDetails = [], $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.locationEditMode = false;
//        locationSvc.GetLocations($scope).success(function (response) {
//            return $scope.walletDetails = response, $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.select = function (page) {
//                var end, start;
//                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageLocations = $scope.filteredLocations.slice(start, end)
//            }, $scope.onFilterChange = function () {
//                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
//            }, $scope.onNumPerPageChange = function () {
//                return $scope.select(1), $scope.currentPage = 1
//            }, $scope.onOrderChange = function () {
//                return $scope.select(1), $scope.currentPage = 1
//            }, $scope.search = function () {
//                return $scope.filteredLocations = $filter("filter")($scope.walletDetails, $scope.searchKeywords), $scope.onFilterChange()
//            }, $scope.order = function (rowName) {
//                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredLocations = $filter("orderBy")($scope.walletDetails, rowName), $scope.onOrderChange()) : void 0
//            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
//                return $scope.search(), $scope.select($scope.currentPage)
//            })();
//        }).error(function (data, status, headers, config) {
//            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
//        });
//    }
//    
//    
//    $scope.loadLocationData();

    $scope.loadOrganizationData = function () {
        $scope.organizations = []

        organizationSvc.GetOrganizations().success(function (response) {
            return $scope.organizations = response
            })
    }
    
    $scope.loadOrganizationData();
    
    $scope.loadProgrammeData = function () {
        $scope.programmes = [];
        programmeSvc.GetProgrammes($scope).success(function (response) {
            return $scope.programmes = response;
        });
    }
  
    
    $scope.loadProgrammeData();
    $scope.editLocation = function (location) {
// if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
// $location.path())) {
// logger.log("Oh snap! You are not allowed to modify the project.");
// return false;
// }
        $scope.locationEditMode = true;
        $scope.isExisting = true;
        $scope.locationId = location.locationId;
        $scope.locationCode = location.locationCode;
        $scope.locationName = location.locationName;
        $scope.branchSelect = location.branchId;
        $scope.active = location.active;
        
    };
    
    $scope.addLocation = function () {
        
//    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
//    			$location.path())) {
//    		logger.log("Oh snap! You are not allowed to create projects.");
//    		return false;
//    	}
    
        $scope.locationEditMode = true;
        $scope.isExisting = false;
        $scope.locationId = 0;
        $scope.locationCode = "";
        $scope.locationName = "";
        $scope.branchId = 0;
        $scope.active = false;
        $scope.isDisabled = true;
    };

    $scope.cancelBin = function () {
        $scope.locationEditMode = false;
        $scope.active = false;
        $location.path('/');
       
    }
    
    $scope.updBin = function () {
        var bin = {};

       
        bin.binRange = $scope.binRange;
        bin.programmeId = $scope.programmeSelect;
        bin.createdBy =$rootScope.UsrRghts.sessionId;
        blockUI.start()
        binSvc.UpdBin(bin).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Bin information was saved succesfully")
                
                $scope.loadProgrammeData ();
                $scope.binRange="";
              
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

} ]).factory('binSvc', function ($http) {
    var compasLocationSvc = {};
    compasLocationSvc.GetWallets = function ($scope) {
        return $http({
            url: '/compas/rest/member/gtWallets',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    compasLocationSvc.UpdBin = function (bin) {
        return $http({
            url: '/compas/rest/bin/updBin',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: bin
        });
    };
    return compasLocationSvc;
}).factory('$locationValid', function () {
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
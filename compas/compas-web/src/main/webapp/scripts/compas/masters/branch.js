/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.branch', []).controller("branchCtrl", ["$scope", "$filter", "branchSvc","userSvc","merchantSvc","locationSvc","$branchValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,branchSvc,userSvc,merchantSvc,locationSvc, $branchValid, $rootScope, blockUI, logger, $location) {
	
    var init;
$scope.header="";
	$scope.branches = [];
	 $scope.classes = [];
	 $scope.merchantListMode = false;
	    $scope.loadClassesList = function () {
	        $scope.classes = [];
	        $scope.userEditMode = false;
	        $scope.userEnrollment = false;
	        userSvc.GetClasses().success(function (response) {
	            $scope.classes = response;
	            if($rootScope.UsrRghts.userClassId==2){
	            angular.forEach($scope.classes, function (item, index) {
	                if (item.classId == 3 ) {
	                    $scope.classes.splice(index, 1);
	                }
	            });
	            }
	            if($rootScope.UsrRghts.userClassId==3){
	                angular.forEach($scope.classes, function (item, index) {
	                    if (item.classId == 2 || item.classId==1 ) {
	                        $scope.classes.splice(index, 1);
	                    }
	                });
	                }
	        }).error(function (data, status, headers, config) {
	            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
	        });
	    }
	  $scope.loadMerchantData = function () {
	        $scope.merchants = [];
	        merchantSvc.GetMerchants($rootScope.UsrRghts.linkId).success(function (response) {
	            return $scope.merchants = response;
	        });
	  }
	 
    $scope.loadBranchData = function () {
        $scope.branches = [], $scope.searchKeywords = "", $scope.filteredBranches = [], $scope.row = "", $scope.branchEditMode = false;
        branchSvc.GetBranches().success(function (response) {
            return $scope.branches = response, $scope.searchKeywords = "", $scope.filteredBranches = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageBranches = $scope.filteredBranches.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredBranches = $filter("filter")($scope.branches, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredBranches = $filter("orderBy")($scope.branches, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    
    
    $scope.loadLocationData = function () {
		$scope.locations = [], $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.locationEditMode = false;
		locationSvc.GetLocations($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.locations = response})
    }

    $scope.loadLocationData();
    
    $scope.editBranch = function (branch) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Branch");
    		return false;
    	}
    	$scope.header="Edit Branch";
        $scope.branchEditMode = true;
        $scope.isExisting = true;
        $scope.branchId = branch.branchId;
        $scope.branchCode = branch.branchCode;
        $scope.branchName = branch.branchName;
        $scope.isDisabled = true;
        $scope.active = branch.active;
        $scope.locationSelect=branch.locationId;
        $scope.merchantSelect=branch.merchantId;
        
    };
    $scope.loadBranchData();

    
    $scope.loadMerchantData();

    $scope.addBranch = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to create Branch.");
    		return false;
    	}
    	$scope.header="Create Branch";
        $scope.branchEditMode = true;
        $scope.isExisting = false;
        $scope.branchId = 0;
        $scope.branchCode = "";
        $scope.branchName = "";
        $scope.companySelect = "";
        $scope.active = false;
        $scope.isDisabled = true;
        $scope.locationSelect="";
        $scope.merchantSelect="";
    };

    $scope.cancelBranch = function () {
        $scope.branchEditMode = false;
        $scope.active = false;
        $scope.isDisabled = false;
        $scope.locationSelect="";
        $scope.classSelect="";
        $scope.branches = [];
        $scope.merchantSelect="";
        $scope.locationSelect="";
   	 $scope.merchantListMode = false;
       
    }
    

    $scope.updBranch = function () {
        var branch = {};

        if (!$branchValid($scope.branchCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Branch's Code. Please try again.")
            return false;
        }
        if (!$branchValid($scope.branchName)) {
            logger.logWarning("Opss! You may have skipped specifying the Branch's Name. Please try again.")
            return false;
        }
        if (!$branchValid($scope.locationSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the location. Please try again.")
            return false;
        }
        if (!$branchValid($scope.merchantSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Service Provider. Please try again.")
            return false;
        }
        if (!$branchValid($scope.branchId))
            branch.branchId = 0;
        else
        	branch.branchId = $scope.branchId;
        branch.branchCode = $scope.branchCode;
        branch.branchName=$scope.branchName;
        branch.classId=$rootScope.UsrRghts.userClassId;
        branch.merchantId=$scope.merchantSelect;
        branch.locationId=$scope.locationSelect
        branch.active = $scope.active;
        branch.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        branchSvc.UpdBranch(branch).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Branch information was saved succesfully")
                
                $scope.branchId = 0;
                $scope.branchCode = "";
                $scope.branchName = "";
                $scope.active = false;
                $scope.branchEditMode = false;
                $scope.isDisabled = false;
                $scope.loadBranchData();
                $scope.merchantSelect="";
                $scope.locationSelect="";
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

} ]).factory('branchSvc', function ($http) {
    var compasBranchSvc = {};
    compasBranchSvc.GetBranches = function ($scope) {
        return $http({
            url: '/compas/rest/branch/gtBranches/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    compasBranchSvc.GetCompanies = function ($scope) {
        return $http({
            url: '/compas/rest/branch/gtCompany',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    compasBranchSvc.UpdBranch = function (branch) {
        return $http({
            url: '/compas/rest/branch/updBranch',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: branch
        });
    };
    return compasBranchSvc;
}).factory('$branchValid', function () {
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
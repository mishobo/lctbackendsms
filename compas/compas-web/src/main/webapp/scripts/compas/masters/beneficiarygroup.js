/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.bnfgrp', []).controller("bnfgrpsCtrl", ["$scope", "$filter", "bnfgrpSvc","userSvc","merchantSvc","productSvc","organizationSvc","$bnfgrpValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,bnfgrpSvc,userSvc,merchantSvc,productSvc,organizationSvc,$bnfgrpValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.bnfgrps = [];
	$scope.allItemsSelected = false;
	$scope.selection=[];
	$scope.header="";
	$scope.objBg={};
	$scope.bnfgrpEditMode = true; 
	$scope.bnfgrpListMode = true; 
	$scope.loadProductData = function (orgId) {
	        $scope.products = []
	        productSvc.GetProducts(orgId).success(function (response) {
	            return $scope.products = response
	        });
	}
	
    $scope.loadBnfgrpData = function (orgId) {
        $scope.bnfgrps = [], $scope.searchKeywords = "", $scope.filteredBnfgrps = [], $scope.row = "";
        bnfgrpSvc.GetBnfgrps(orgId).success(function (response) {
            return $scope.bnfgrps = response, $scope.searchKeywords = "", $scope.filteredBnfgrps = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageBnfgrps = $scope.filteredBnfgrps.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredBnfgrps = $filter("filter")($scope.bnfgrps, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredBnfgrps = $filter("orderBy")($scope.bnfgrps, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageBnfgrps = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }

 
	$scope.loadOrganizationData = function () {
		$scope.orgs = [];
		organizationSvc.GetOrganizations().success(function (response) {
			return $scope.orgs = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	// $scope.init();
	$scope.loadOrganizationData();
  
	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			$scope.bnfgrpListMode = false; 
			$scope.loadProductData(orgId);
			   $scope.loadBnfgrpData(orgId);
		}
	}
    
    $scope.editBnfgrp = function (bnfgrp) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the bnfgrp.");
    		return false;
    	}
    	$scope.header="Edit Covet Type";
        $scope.bnfgrpEditMode = false;
        $scope.bnfgrpListMode = true; 
        $scope.isDisabled = true;
        $scope.objBg=bnfgrp;
    };
  
    $scope.addBnfgrp = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
    		logger.log("Oh snap! You are not allowed to create bnfgrp.");
    		return false;
    	}
    	$scope.header="Create Cover Type";
    	 // $scope.loadBranchData();
        $scope.bnfgrpEditMode = false;
        $scope.bnfgrpListMode = true; 
        
        $scope.isDisabled = true;
        $scope.objBg={};
        $scope.objBg.active = true;
    };

    $scope.cancelBnfgrp = function () {
    	 $scope.bnfgrpEditMode = true;
         $scope.bnfgrpListMode = true; 
        $scope.objBg={}
        $scope.orgSelect="";
        $scope.isDisabled = false;
    }
   
    $scope.updBnfgrp= function () {
       

        if (!$bnfgrpValid($scope.objBg.bnfGrpCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Covet Type's Code. Please try again.")
            return false;
        }
        if (!$bnfgrpValid($scope.objBg.bnfGrpName)) {
            logger.logWarning("Opss! You may have skipped specifying the Cover Type's name. Please try again.")
            return false;
        }
        if (!$bnfgrpValid($scope.objBg.productId)) {
            logger.logWarning("Opss! You may have skipped specifying the Scheme. Please try again.")
            return false;
        }
       
        if (!$bnfgrpValid($scope.objBg.bnfGrpId))
        	$scope.objBg.bnfGrpId = 0;
        
        $scope.objBg.orgId=$scope.orgSelect;
        $scope.objBg.createdBy = $rootScope.UsrRghts.sessionId;
      
        blockUI.start()
        bnfgrpSvc.UpdBnfgrp($scope.objBg).success(function (response) {
            if (response.respCode == 200) {
            	$scope.objBg={};
            	//$scope.orgSelect="";
            	 $scope.bnfgrpEditMode = true;
                 $scope.bnfgrpListMode = false; 
                $scope.loadBnfgrpData($scope.orgSelect);
                $scope.isDisabled = false;
             
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

} ]).factory('bnfgrpSvc', function ($http) {
    var EvBnfgrpsvc = {};
    EvBnfgrpsvc.GetBnfgrps = function (orgId) {
        return $http({
            url: '/compas/rest/product/gtBnfGrps/'+orgId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

   
    EvBnfgrpsvc.UpdBnfgrp = function (bnfgrp) {
        return $http({
            url: '/compas/rest/product/updBnfGrp',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: bnfgrp
        });
    };
    return EvBnfgrpsvc;
}).factory('$bnfgrpValid', function () {
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
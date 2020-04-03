/**
 * organization Angular Module
 */
'use strict';
angular.module('app.organization', []).controller("organizationsCtrl", ["$scope", "$filter", "organizationSvc", "$organizationValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, organizationSvc, $organizationValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.objOrg={};
    $scope.loadOrganizationData = function () {
        $scope.organizations = [], $scope.searchKeywords = "", $scope.filteredOrganizations = [], $scope.row = "", $scope.organizationEditMode = false;
        organizationSvc.GetOrganizations().success(function (response) {
            return $scope.organizations = response, $scope.searchKeywords = "", $scope.filteredOrganizations = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageOrganizations = $scope.filteredOrganizations.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredOrganizations = $filter("filter")($scope.organizations, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredOrganizations = $filter("orderBy")($scope.organizations, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageOrganizations = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadOrganizationData();

    $scope.editOrganization = function (organization) {
    	 if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
             logger.log("Oh snap! You are not allowed to edit organization.");
             return false;
         }
        $scope.organizationEditMode = true;
        $scope.objOrg=organization;
    };

    $scope.addOrganization = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new organization.");
            return false;
        }
        $scope.organizationEditMode = true;
        $scope.objOrg={};
    };

    $scope.calcelOrganization = function () {
        $scope.organizationEditMode = false;
        $scope.objOrg={};
    }

    $scope.updOrganization = function () {
      
        if (!$organizationValid($scope.objOrg.orgCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Organization's Code. Please try again.")
            return false;
        }
        
        if (!$organizationValid($scope.objOrg.orgName)) {
            logger.logWarning("Opss! You may have skipped specifying the Organization's Name. Please try again.")
            return false;
        }
        
        if (!$organizationValid($scope.objOrg.orgContactPer)) {
            logger.logWarning("Opss! You may have skipped specifying the Organization's Conatct Person. Please try again.")
            return false;
        }
        if (!$organizationValid($scope.objOrg.orgContactNo)) {
            logger.logWarning("Opss! You may have skipped specifying the Organization's Contact No. Please try again.")
            return false;
        }
		if ($scope.objOrg.orgEmail !=null){
			if($scope.objOrg.orgEmail.length>0 && !$scope.objOrg.orgEmail.match(/^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/)) {
			logger.logWarning("Opss! User email is not valid. Please try again.")
			return false;
			}
		}
		if ($scope.objOrg.orgContactNo !=null){
			if($scope.objOrg.orgContactNo.length>0 && !$scope.objOrg.orgContactNo.match(/^\+?\d{10}$/)) {
			logger.logWarning("Opss! User phone is not valid. Please try again.")
			return false;
			}
		}
        if (!$organizationValid($scope.objOrg.orgId))
        	$scope.objOrg.orgId = 0;
       // else
//            organization.orgId = $scope.objOrg.orgId;
//        organization.orgName = $scope.objOrg.orgCode;
//        organization.orgName = $scope.orgName;
//        organization.orgContactPer = $scope.orgContactPer;
//        organization.orgContactNo = $scope.orgContactNo;
//        organization.orgEmail = $scope.orgEmail;
        	$scope.objOrg.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        organizationSvc.UpdOrganization($scope.objOrg).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Organization information was saved succesfully")
                $scope.objOrg={};
                $scope.organizationEditMode = false;
                $scope.loadOrganizationData();
            }
            else if (response.respCode == 201) {
                logger.logWarning("Opss! The Organization name specified already exists. Please try again")
            }
            else {
                logger.logWarning("Opss! Something went wrong while updating the Organization. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('organizationSvc', function ($http) {
    var compasOrganizationSvc = {};
    compasOrganizationSvc.GetOrganizations = function () {
        return $http({
            url: '/compas/rest/organization/gtOrganizations',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasOrganizationSvc.UpdOrganization = function (organization) {
        return $http({
            url: '/compas/rest/organization/updOrganization',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: organization
        });
    };
    return compasOrganizationSvc;
}).factory('$organizationValid', function () {
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
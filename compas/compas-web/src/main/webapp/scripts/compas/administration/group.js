/**
 * Groups Angular Module
 */
'use strict';
angular.module('app.group.rights', []).controller("groupsCtrl", ["$scope", "$filter", "userSvc", "groupSvc","organizationSvc","merchantSvc", "$groupValid", "$rootScope", "blockUI", "logger",  "$location", "localStorageService", "loginSvc", function ($scope, $filter, userSvc, groupSvc,organizationSvc,merchantSvc, $groupValid, $rootScope, blockUI, logger, $location, localStorageService, loginSvc) {
	var init;

	$scope.groups = [];
	$scope.showRights=true;
	$scope.isExisting=false;

	$scope.loadGroupData = function () {
		$scope.groups = [], $scope.searchKeywords = "", $scope.filteredGroups = [], $scope.row = "", $scope.groupEditMode = false;
		groupSvc.GetGroups().success(function (response) {
			console.log(response)
			return $scope.groups = response, $scope.searchKeywords = "", $scope.filteredGroups = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageGroups = $scope.filteredGroups.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredGroups = $filter("filter")($scope.groups, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredGroups = $filter("orderBy")($scope.groups, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageGroups = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadGroupData();

	$scope.loadRightList = function (grpTypeId) {
		$scope.rights = [];
		groupSvc.GetRights(grpTypeId).success(function (response) {
			$scope.rights = response;
			console.log(response)
			if($scope.rights.length>0){
				$scope.showRights=false;
			}else{
				$scope.rights = [];
				$scope.showRights=true;
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	// This executes when checkbox in table header is checked
	$scope.selectAllViewRights = function () {
		for (var i = 0; i < $scope.rights.length; i++) {
			$scope.rights[i].rightView = $scope.allViewRightsSelected;
		}
	};

	$scope.selectAllAddRights = function () {
		for (var i = 0; i < $scope.rights.length; i++) {
			$scope.rights[i].rightAdd = $scope.allAddRightsSelected;
		}
	};

	$scope.selectAllEditRights = function () {
		for (var i = 0; i < $scope.rights.length; i++) {
			$scope.rights[i].rightEdit = $scope.allEditRightsSelected;
		}
	};

	$scope.resetAllRights = function () {
		$scope.allViewRightsSelected = false;
		$scope.allAddRightsSelected = false;
		$scope.allEditRightsSelected = false;
		for (var i = 0; i < $scope.rights.length; i++) {
			$scope.rights[i].rightView = $scope.allViewRightsSelected;
			$scope.rights[i].rightAdd = $scope.allAddRightsSelected;
			$scope.rights[i].rightEdit = $scope.allEditRightsSelected;
		}
	};

	$scope.loadGrpTypes = function () {
		$scope.grpTypes = [];
		groupSvc.GetGrpTypes().success(function (response) {
			$scope.grpTypes = response;
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadGrpTypes();

	$scope.$watch("grpTypeSelect", function (newValue, oldValue) {
		if ($groupValid(newValue)) {
			if (newValue != oldValue) {
				if($scope.groupId==0){
					if($scope.grpTypeSelect!=1){
						$scope.loadRightList(newValue);
					}
				}
			}

		}
		else
			$scope.rights = [];
	});
	$scope.editGroup = function (group) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the user group and rights.");
			return false;
		}
		$scope.groupEditMode = true;
		$scope.groupHeader = "Edit Group";
		$scope.groupId = group.groupId;
		$scope.groupCode = group.groupCode;
		$scope.groupName = group.groupName;
		$scope.grpTypeSelect = group.grpTypeId;
		if($scope.grpTypeSelect==2){
			$scope.showRights=false;


		}else{
			$scope.showRights=true;
		}
		$scope.rights=[];
		$scope.rights = group.rights;
		$scope.isExisting=true;
		$scope.active=group.active;
	};

	$scope.addGroup = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new user groups and assign rights.");
			return false;
		}
		$scope.groupHeader = "Group Creation";
		$scope.groupId = 0;
		$scope.groupCode = "";
		$scope.groupName = "";
		$scope.grpTypeSelect="";
		$scope.groupEditMode = true;
		$scope.isExisting=false;
		$scope.showRights=true;
		$scope.active=true;

	};

	$scope.cancelGroup = function () {
		$scope.groupEditMode = false;
		$scope.isExisting = false;
		$scope.groupId = 0;
		$scope.groupCode = "";
		$scope.groupName = "";
		$scope.grpTypeSelect="";
		$scope.loadGroupData();
		$scope.showRights=true;
		$scope.active=false;
	}


	$scope.updGroup = function () {
		var group = {};
		if (!$groupValid($scope.groupCode)) {
			logger.logWarning("Opss! You may have skipped specifying the Group's Code. Please try again.")
			return false;
		}
		if (!$groupValid($scope.groupName)) {
			logger.logWarning("Opss! You may have skipped specifying the Group's Name. Please try again.")
			return false;
		}
		if ($scope.groupName.length > 30) {
			logger.logWarning("Opss!Group Name is reach to maximum length of 30 ")
			return false;
		}
		if (!$groupValid($scope.grpTypeSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Group's Type. Please try again.")
			return false;
		}
		if (!$groupValid($scope.groupId))
			$scope.groupId = 0;
		else
			group.groupId = $scope.groupId;
		group.groupCode = $scope.groupCode;
		group.groupName = $scope.groupName;
		group.grpTypeId = $scope.grpTypeSelect;
		group.active = $scope.active;
		group.rights = $scope.rights;
		group.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		groupSvc.UpdGroup(group).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The group information was saved succesfully")
				if (parseFloat(group.groupId) == parseFloat($rootScope.UsrRghts.userGroupId)) {
					localStorageService.clearAll();
					loginSvc.GetRights({userId: $rootScope.UsrRghts.sessionId}).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
					});
				}
				$scope.cancelGroup();
			}
			else if (response.respCode == 201) {
				logger.logWarning(response.respMessage);
			}
			else {
				logger.logWarning("Opss! Something went wrong while updating the user. Please try again after sometime")
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};

}]).factory('groupSvc', function ($http) {
	var compasGroupSvc = {};
	compasGroupSvc.GetGroups = function () {
		return $http({
			url: '/compas/rest/userGroups/gtGroups/',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasGroupSvc.UpdGroup = function (group) {
		return $http({
			url: '/compas/rest/userGroups/updGroup',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: group
		});
	};
	compasGroupSvc.GetRights = function (rightTypeId) {
		return $http({
			url: '/compas/rest/userGroups/gtRights/'+rightTypeId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasGroupSvc.GetGrpTypes = function () {
		return $http({
			url: '/compas/rest/userGroups/gtGrpTypes/',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	return compasGroupSvc;
}).factory('$groupValid', function () {
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
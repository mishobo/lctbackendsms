/**
 * User Angular Module
 */
'use strict';
angular.module('app.user', []).controller("usersCtrl", ["$scope", "$filter", "userSvc","organizationSvc","merchantSvc","loginSvc","groupSvc","locationSvc", "$userValid", "$rootScope", "blockUI", "logger", "$location","localStorageService", function ($scope, $filter, userSvc ,organizationSvc,merchantSvc,loginSvc,groupSvc,locationSvc,$userValid, $rootScope, blockUI, logger, $location,localStorageService) {
	var init;
	$scope.classes = [];
	$scope.groups = [];
	$scope.questions = [];
	$scope.isExisting = false;
	$scope.organizationListMode = false;
	$scope.merchantListMode = false;
	$scope.memberListMode = false;
	$scope.showlocation=false;
	$scope.showMerchants=true;
	$scope.header="";
	$scope.userTypeSelect=""
	$scope.groupSelect = "";
	$scope.locationSelect="";
	$scope.merchantSelect="";
	$scope.loadLocationData = function () {
		$scope.locations = [];
		locationSvc.GetLocations().success(function (response) {
			return $scope.locations = response;
		});
	}
	$scope.loadLocationData();
	$scope.loadGrpTypes = function () {
		$scope.userTypes = [];
		groupSvc.GetGrpTypes().success(function (response) {
			$scope.userTypes = response;
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadGrpTypes();
	$scope.loadGroupList = function (newValue) {
		$scope.groups = [];
		userSvc.GetGroupsByUserType(newValue).success(function (response) {
			$scope.groups = response;
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	
	$scope.$watch("userTypeSelect", function (newValue, oldValue) {
		if ($userValid(newValue)) {
			if (newValue != oldValue) {
				$scope.loadGroupList(newValue);
				if(newValue==1){
					$scope.showlocation=false;
					$scope.showMerchants=false;
				}else{
					$scope.showlocation=true;
					$scope.showMerchants=true;
				}
			}
		}
	});
	
	$scope.loadLevels=function(){
		$scope.levels=[];
		$scope.levels=[{'level':0,'levelName':'Pos Admin'},{'level':1,'levelName':'Pos User'}]
	}
	$scope.loadLevels();
	var original;
	 original = angular.copy($scope.form), $scope.revert = function () {
         return $scope.form = angular.copy(original), $scope.form_constraints.$setPristine()
     },
     $scope.loadUserData = function () {
 		$scope.users = [], $scope.searchKeywords = "", $scope.filteredUsers = [], $scope.row = "", $scope.userEditMode = false, $scope.locationSelectionMode=false;
 		userSvc.GetUsers($scope).success(function (response) {
 			return $scope.users = response, $scope.searchKeywords = "", $scope.filteredUsers = [], $scope.row = "", $scope.select = function (page) {
 				var end, start;
 				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUsers = $scope.filteredUsers.slice(start, end)
 			}, $scope.onFilterChange = function () {
 				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
 			}, $scope.onNumPerPageChange = function () {
 				return $scope.select(1), $scope.currentPage = 1
 			}, $scope.onOrderChange = function () {
 				return $scope.select(1), $scope.currentPage = 1
 			}, $scope.search = function () {
 				return $scope.filteredUsers = $filter("filter")($scope.users, $scope.searchKeywords), $scope.onFilterChange()
 			}, $scope.order = function (rowName) {
 				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUsers = $filter("orderBy")($scope.users, rowName), $scope.onOrderChange()) : void 0
 			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUsers = [], (init = function () {
 				return $scope.search(), $scope.select($scope.currentPage)
 			})();
 		}).error(function (data, status, headers, config) {
 			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
 		});
 	}
	$scope.loadUserData(); 
	$scope.loadMerchantData = function () {
		$scope.merchants = [];
		userSvc.GetMerchants().success(function (response) {
			$scope.merchants = response;
		});
	}
	$scope.loadMerchantData();
	$scope.onLocationChange=function(locationId){
		if(locationId>0){
//			$scope.loadAgentData(locationId)
		}
	}
	/*$scope.$watch("locationSelect", function (newValue, oldValue) {
		if ($userValid(newValue)) {
			if (newValue != oldValue) {
				// $scope.branchId = $scope.locationSelect;
				
				
			}
		}
		else
			$scope.users = [];
	});*/
	$scope.editUser = function (user) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify the user.");
			return false;
		}
		$scope.header="Edit User";
		$scope.userEditMode = true;
		$scope.isExisting = true;
		$scope.userId = user.userId;
		$scope.userName = user.userName;
		$scope.userFullName = user.userFullName;
		$scope.memberSelect=user.userLinkedID;
		$scope.userPwd = user.userPwd;
		$scope.confirmPwd=user.userPwd;
		$scope.userEmail = user.userEmail;
		$scope.userPhone = user.userPhone;
		$scope.active = user.active;
		$scope.userTypeSelect=user.userTypeId;
		if(user.userTypeId==1){
			$scope.showlocation=false;
			$scope.showMerchants=false;
			
		}else{
			$scope.showlocation=true;
			$scope.showMerchants=true;
		}
		$scope.groupSelect = user.userGroupId;
//		$scope.locationSelect=user.branchId;

		$scope.merchantSelect=user.merchantId;
		$scope.levelSelect=user.posUserLevel;
		$scope.isDisabled = true;

	};
	//$scope.loadAgentData();
	$scope.addUser = function () {
		if ($rootScope.UsrRghts.userClassId == 4) {
			if (!$userValid($scope.classSelect)) {
				logger.logWarning("Opss! you skipped selecting the class. please select a class and try again.")
				return false;
			}
			if (($scope.classSelect == 1) && !$userValid($scope.brokerSelect)) {
				logger.logWarning("Opss! you skipped selecting the Funder. please select a Funder and try again.")
				return false;
			}
			if (($scope.classSelect == 2) && !$userValid($scope.providerSelect)) {
				logger.logWarning("Opss! you skipped selecting the provider. please select a provider and try again.")
				return false;
			}
		}
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create users.");
			return false;
		}
		if($scope.classSelect == 3){
			$scope.memberListMode = true;
		}
		$scope.header="Create User";
		$scope.userEditMode = true;
		$scope.isExisting = false;
		$scope.userEnrollment=false;
		$scope.userId = 0;
		$scope.userName = "";
		$scope.userFullName = "";
		$scope.userPwd = "";
		$scope.groupSelect = "";
		$scope.userEmail = "";
		$scope.userPhone = "";
		$scope.questionSelect = "";
		$scope.merchantSelect="";
		$scope.locationSelect="";
		$scope.userTypeSelect = "";
		$scope.active = true;
		$scope.isDisabled = true;
		$scope.showlocation=true;
		$scope.showMerchants=true;
		$scope.levelSelect="";
		$scope.confirmPwd=""
		//$scope.loadBranchData();
		//$scope.loadAgentData();
		//$scope.loadGroupList();
	};

	$scope.cancelUser = function () {
		$scope.userEditMode = false;
		$scope.userEnrollment=false;
		$scope.isDisabled = false;
		$scope.active = false;
		$scope.userPhone = "";
		$scope.questionSelect = "";
		$scope.merchantSelect=0;
		$scope.locationSelect=0;
		$scope.userTypeSelect = 0;
		$scope.active = false;
		$scope.isDisabled = true;
		$scope.levelSelect="";
		$scope.confirmPwd=""
		//$scope.loadGrpTypes();
		//$scope.loadGroupList();
		///$scope.loadBranchData();
		//$scope.loadAgentData();
		$scope.showlocation=true;
		$scope.showMerchants=true;
	}

	$scope.enrollUser = function () {
		$scope.userEditMode = false;
		$scope.userEnrollment=true;
		$scope.isDisabled = true;
		$scope.active = false;
	}

	$scope.updUser = function () {
		var user = {};

		if (!$userValid($scope.userName)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Name. Please try again.")
			return false;
		}
		if ($scope.userName.length > 30) {
			logger.logWarning("Opss!User Name is limited to maximum length of 30 characters")
			return false;
		}
		if (!$userValid($scope.userFullName) ) {
			logger.logWarning("Opss! You may have skipped specifying the User's Full Name. Please try again.")
			return false;
		}
		if (!$userValid($scope.userPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Password. Please try again.")
			return false;
		}
		if (!$userValid($scope.confirmPwd)) {
			logger.logWarning("Opss! You may have skipped specifying the User's confirm userPwd. Please try again.")
			return false;
		}
		if ($scope.userPwd!=$scope.confirmPwd) {
			logger.logWarning("Opss! Password and Confirm password does not match. Please try again.")
			return false;
		}
		if (!$userValid($scope.userTypeSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Type. Please try again.")
			return false;
		}
		if (!$userValid($scope.groupSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the User's Group. Please try again.")
			return false;
		}
		if($scope.userTypeSelect==1){
			if (!$userValid($scope.merchantSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the service provider.Please try again.")
			return false;
			}
			if(isNaN($scope.userPwd)){
				logger.logWarning("Opss! POS user password should be numeric.Please try again.")
				return false
			}
		}

		if ($scope.userEmail !=null){
			if($scope.userEmail.length>0 && !$scope.userEmail.match(/^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/)) {
			logger.logWarning("Opss! User email is not valid. Please try again.")
			return false;
			}
		}
		if ($scope.userPhone !=null){
			if($scope.userPhone.length>0 && !$scope.userPhone.match(/^\+?\d{10}$/)) {
			logger.logWarning("Opss! User phone is not valid. Please try again.")
			return false;
			}
		}

		if (!$userValid($scope.userId))
			user.userId = 0;
		else
			user.userId = $scope.userId;
		user.userName = $scope.userName;
		user.userPwd = $scope.userPwd;
		user.userGroupId = $scope.groupSelect;
		if($scope.userTypeSelect==2){
			user.merchantId =0;
			user.branchId=0
			$scope.levelSelect=0
		}else{
			user.merchantId = $scope.merchantSelect;
			user.branchId=$scope.locationSelect;
		}
		user.userEmail = $scope.userEmail;
		user.userPhone = $scope.userPhone;
		user.branchId = 0;
		user.userFullName = $scope.userFullName;
		user.active = $scope.active;
		user.userTypeId=$scope.userTypeSelect;
		user.createdBy = $rootScope.UsrRghts.sessionId;
//		user.posUserLevel=$scope.levelSelect;
		blockUI.start()
		console.log(user)
		userSvc.UpdUser(user).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The user information was saved succesfully")
				if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
					localStorageService.clearAll();
					loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
						localStorageService.set('rxr', rightLst);
						$rootScope.UsrRghts = rightLst;
					}).error(function (data, status, headers, config) {
						logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
					});
				}
				$scope.userId = 0;
				$scope.confirmPwd=""
				$scope.userName = "";
				$scope.userFullName = "";
				$scope.userPwd = "";
				$scope.groupSelect = "";
				$scope.userEmail = "";
				$scope.userPhone = "";
				$scope.userTypeSelect = "";
				$scope.userSecretAns = "";
				$scope.active = false;
				$scope.userEditMode = false;
				$scope.userEnrollment=false;
				$scope.levelSelect="";
				$scope.loadUserData();
				$scope.isDisabled =false;
				$scope.showlocation=true;
				$scope.showMerchants=true;
			}
			else if (response.respCode == 201) {
				logger.logWarning("Opss! The username specified is already taken. Please try again")
			}
			// added by Anits-04-Sep-2104 to check if member is already exists
			// as user
			else if (response.respCode == 202) {
				logger.logWarning("Opss! The Member specified is already taken as User. Please try again")
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

} ]).factory('userSvc', function ($http) {
	var compasUserSvc = {};
	compasUserSvc.GetUsers = function () {
		return $http({
			url: '/compas/rest/user/gtUsers/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};


	compasUserSvc.GetGroupsByUserType = function (userTypeId) {
		return $http({
			url: '/compas/rest/user/gtGroupsByUserType/'+userTypeId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};

	compasUserSvc.GetMerchants = function () {
        return $http({
            url: '/compas/rest/merchant/gtMerchants/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

	compasUserSvc.GetLocationAgents=function(locationId){
		return $http({
			url: '/compas/rest/user/gtLocationAgents/'+locationId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	}
	compasUserSvc.UpdUser = function (user) {
		return $http({
			url: '/compas/rest/user/updUser',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: user
		});
	};
	return compasUserSvc;
}).factory('$userValid', function () {
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
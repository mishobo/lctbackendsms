/**
 * merchant Angular Module
 */
'use strict';
angular.module('app.merchant', []).controller("merchantsCtrl", ["$scope", "$filter", "merchantSvc", "serviceSvc", "$merchantValid", "$rootScope", "blockUI", "logger", "$location", "$anchorScroll", function ($scope, $filter, merchantSvc, serviceSvc, $merchantValid, $rootScope, blockUI, logger, $location, $anchorScroll) {
	var init;
	var dlg = null;
	$scope.header = "";
	$scope.objMerchant = {};
	$scope.myCroppedImage = "";
	$scope.user = {};
	$scope.user.username = "";
	$scope.user.email = "";
	$scope.user.active = false;
	$scope.posUsers = [];
	$scope.isEdit = false;
	$scope.isDelete = false;
	$scope.loadMerchantData = function () {
		$scope.merchants = [], $scope.searchKeywords = "", $scope.filteredMerchants = [], $scope.row = "", $scope.merchantEditMode = false;
		$scope.showPosUsers = false;
		merchantSvc.GetMerchants().success(function (response) {
			//merchantSvc.GetMerchantsByUserName().success(function (response) {
			return $scope.merchants = response, $scope.searchKeywords = "", $scope.filteredMerchants = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMerchants = $scope.filteredMerchants.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredMerchants = $filter("filter")($scope.merchants, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMerchants = $filter("orderBy")($scope.merchants, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageMerchants = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	// $scope.init();
	$scope.loadMerchantData();

	$scope.editMerchant = function (merchant) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify  Service Provider.");
			return false;
		}
		$scope.header = "Edit Service Provider";
		$scope.merchantEditMode = true;
		$scope.showPosUsers = true;
		$scope.objMerchant = merchant;
		$scope.currSelect = $scope.objMerchant.currCode;
		$scope.isDisabled = true;
		$scope.loadPOSUsers();
	};

	$scope.addPOSUser = function () {
		$scope.user.merchantCode = $scope.objMerchant.merchantCode;
		$scope.user.createdBy = $rootScope.UsrRghts.sessionId;
		console.log("adding pos user: ", $scope.user);
		blockUI.start();
		merchantSvc.createPOSUser($scope.user).success(function (resp) {
			console.log("server response: ", resp);
			if (resp.respCode === 200) {
				logger.log("POS user created successfully.");
			} else if (resp.respCode === 201) {
				logger.log("POS user creation failed. Ensure no other user has the same username or email.");
			} else if (resp.respCode === 403) {
				logger.log("POS user creation failed. Ensure no other user has the same username or password.");
			} else if (resp.respCode === 404) {
				logger.logError("Error encountered when creating POS user.");
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
			blockUI.stop();
		});
	};

	$scope.loadPOSUsers = function () {
		console.log("loading pos users for ", $scope.objMerchant.merchantCode);
		merchantSvc.loadPOSUsers($scope.objMerchant.merchantCode).success(function (resp) {
			console.log("server response: ", resp);
			$scope.posUsers = resp;
			if (resp.respCode === 200) {
				logger.log("POS user loaded successfully.");
			} else if (resp.respCode === 201) {
				logger.log("POS user loading failed.");
			} else if (resp.respCode === 404) {
				logger.logError("Error encountered when loading POS user.");
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
			blockUI.stop();
		});
	};
	
	//delete POS user
	$scope.deleteUser = function (i, task) {
		if (task === "delete") {
			const posUser = $scope.posUsers[i];
			blockUI.start();
			merchantSvc.deletePOSUser(posUser).success(function (resp) {
				console.log("server response: ", resp);
				if (resp.respCode === 200) {
					logger.log("POS user was deleted succesfully.");
					$scope.loadPOSUsers();
				} else if (resp.respCode === 201) {
					logger.log("POS user deletion failed.");
				} else if (resp.respCode === 404) {
					logger.logError("Error encountered when deleting POS user.");
				}
				blockUI.stop();
			}).error(function (data, status, headers, config) {
				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
				blockUI.stop();
			});
		} else if (task === "delete") {
			$scope.isDelete = true;
			$scope.user = $scope.posUsers[i];
			console.log("deleting: ", $scope.user);
		}
	}
	
	$scope.editUser = function (i, task) {
		if (task === "reset") {
			const posUser = $scope.posUsers[i];
			blockUI.start();
			merchantSvc.resetPOSUserPIN(posUser).success(function (resp) {
				console.log("server response: ", resp);
				if (resp.respCode === 200) {
					logger.log("POS user PIN reset successfully. Check " + $scope.posUsers[i].email + " for new PIN.");
					$scope.loadPOSUsers();
				} else if (resp.respCode === 201) {
					logger.log("POS user PIN reset failed.");
				} else if (resp.respCode === 404) {
					logger.logError("Error encountered when resetting POS user.");
				}
				blockUI.stop();
			}).error(function (data, status, headers, config) {
				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
				blockUI.stop();
			});
		} else if (task === "edit") {
			$scope.isEdit = true;
			$scope.user = $scope.posUsers[i];
			console.log("editing: ", $scope.user);
		}
	}
	$scope.editedPOSUser = function () {
		blockUI.start();
		merchantSvc.editPOSUser($scope.user).success(function (resp) {
			console.log("server response: ", resp);
			$scope.user = {};
			if (resp.respCode === 200) {
				logger.log("POS user PIN edited successfully.");
			} else if (resp.respCode === 201) {
				logger.log("POS user PIN editing failed.");
			} else if (resp.respCode === 404) {
				logger.logError("Error encountered when editing POS user.");
			}
			blockUI.stop();
			$scope.isEdit = false;
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
			blockUI.stop();
			$scope.isEdit = false;
		});
	}

	$scope.addMerchant = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new Service Provider.");
			return false;
		}
		$scope.header = "Create Service Provider";
		$scope.merchantEditMode = true;
		$scope.currSelect = ""
		$scope.objMerchant = {};
		$scope.objMerchant.active = true;
		$scope.isDisabled = false;
	};

	$scope.calcelMerchant = function () {
		$scope.merchantEditMode = false;
		$scope.showPosUsers = false;
		$scope.currSelect = ""
		$scope.objMerchant = {};
		$scope.isDisabled = false;
	}
	$scope.cancelMember = function () {
		$scope.showPosUsers = false;
		$scope.merchantEditMode = false;
		$scope.user = ""
		$scope.entry = {}
		$scope.isDisabled = false;
	}
	$scope.loadCurrencies = function () {
		$scope.currencies = [];
		serviceSvc.GetCurrencies().success(function (response) {
			$scope.currencies = response
		})
	}
	$scope.loadCurrencies();
	$scope.updMerchant = function () {
		if (!$merchantValid($scope.objMerchant.merchantName)) {
			logger.logWarning("Opss! You may have skipped specifying the Service Provider's Name. Please try again.")
			return false;
		}
		if ($scope.objMerchant.merchantName.length > 50) {
			logger.logWarning("Opss!Service Provider Name is reach to maximum length of 50 ")
			return false;
		}
		if ($scope.objMerchant.merEmail != null) {
			if ($scope.objMerchant.merEmail.length > 0 && !$scope.objMerchant.merEmail.match(/^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/)) {
				logger.logWarning("Opss! provider email is not valid. Please try again.")
				return false;
			}
		}
		if ($scope.objMerchant.merCnctNo != null) {
			if ($scope.objMerchant.merCnctNo.length > 0 && !$scope.objMerchant.merCnctNo.match(/^\+?\d{10}$/)) {
				logger.logWarning("Opss! provider phone is not valid. Please try again.")
				return false;
			}
		}
		if ($scope.objMerchant.hasHmsi == true && !$merchantValid($scope.objMerchant.stageServer)) {
			logger.logWarning("Opss! Stage server. Please try again.")
			return false;
		}
		if (!$merchantValid($scope.objMerchant.merchantId))
			$scope.objMerchant.merchantId = 0;
		else
			//			merchant.merchantId = $scope.merchantId;
			//			merchant.merchantCode = $scope.merchantCode;
			//			merchant.merchantName = $scope.merchantName;
			//			merchant.active = $scope.active;
			$scope.objMerchant.createdBy = $rootScope.UsrRghts.sessionId;
		$scope.objMerchant.orgId = $rootScope.UsrRghts.linkId;
		$scope.objMerchant.currCode = $scope.currSelect;
		$scope.objMerchant.logo = $scope.myCroppedImage;
		blockUI.start();

		merchantSvc.UpdMerchant($scope.objMerchant).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Service Provider information was saved succesfully")
				$scope.objMerchant = {};
				$scope.merchantEditMode = false;
				$scope.isDisabled = false;
				$scope.loadMerchantData();
				$scope.currSelect = ""
			} else {
				logger.logWarning(response.respMessage)
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};
	var handleFileSelect = function (evt) {
		$scope.showCamera = true;
		$scope.capPic = false;
		$scope.showImage = false;
		var file = evt.currentTarget.files[0];
		var reader = new FileReader();
		reader.onload = function (evt) {
			$scope.$apply(function ($scope) {
				$scope.myCroppedImage = evt.target.result;
			});
		};
		reader.readAsDataURL(file);
		if ($(this).get(0).files.length > 0) { // only if a file is selected
			var fileSize = $(this).get(0).files[0].size;
			console.log(fileSize);
		}
	};
	angular.element(document.querySelector('#fileInput')).on('change', handleFileSelect);
}]).factory('merchantSvc', function ($http) {
	var compasMerchantSvc = {};
	compasMerchantSvc.GetMerchants = function () {
		return $http({
			url: '/compas/rest/merchant/gtMerchants/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		});
	};
	
	
	compasMerchantSvc.GetMerchantsByUserName = function (LoggedInUser) {
		return $http({
			url: '/compas/rest/merchant/gtMerchantsByUserName/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: LoggedInUser 
		});
	};

	compasMerchantSvc.createPOSUser = function (posUser) {
		return $http({
			url: '/compas/rest/merchant/addMerchantPOSUser',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: posUser
		});
	};

	compasMerchantSvc.loadPOSUsers = function (merchantCode) {
		return $http({
			url: '/compas/rest/merchant/getMerchantPOSUsers',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: merchantCode
		});
	};
	
	//POS USER DELETE
	compasMerchantSvc.deletePOSUser = function (posUser) {
		return $http({
			url: '/compas/rest/merchant/deleteMerchantPOSUser',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: posUser
		});
	};
	
	compasMerchantSvc.resetPOSUserPIN = function (posUser) {
		return $http({
			url: '/compas/rest/merchant/resetMerchantPOSUserPIN',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: posUser
		});
	};

	compasMerchantSvc.editPOSUser = function (posUser) {
		return $http({
			url: '/compas/rest/merchant/editMerchantPOSUser',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: posUser
		});
	};

	compasMerchantSvc.loadPOSUsers = function (merchantCode) {
		return $http({
			url: '/compas/rest/merchant/getMerchantPOSUsers',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: merchantCode
		});
	};

	compasMerchantSvc.UpdMerchant = function (merchant) {
		return $http({
			url: '/compas/rest/merchant/updMerchant',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: merchant
		});
	};
	return compasMerchantSvc;
}).factory('$merchantValid', function () {
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
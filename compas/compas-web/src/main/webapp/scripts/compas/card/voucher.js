/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.voucher', []).controller("vouchersCtrl", ["$scope", "$filter", "voucherSvc", "$voucherValid", "$rootScope", "blockUI", "logger", "$location", "$anchorScroll", function ($scope, $filter, voucherSvc, $voucherValid, $rootScope, blockUI, logger, $location, $anchorScroll) {

	var init;

	$scope.vouchers = [];
	$scope.allItemsSelected = false;
	$scope.showServices = true;
	$scope.selection = [];
	$scope.services = [];
	$scope.voucherType = "";
	$scope.header = "";
	$scope.isExisting = false;
	$scope.voucherEditMode = true;
	$scope.voucherListMode = true;
	$scope.mem = [];
	$scope.loadVoucherData = function () {
		$scope.vouchers = [], $scope.searchKeywords = "", $scope.filteredVouchers = [], $scope.row = "", $scope.previewServices = false;
		voucherSvc.GetVouchers().success(function (response) {
			console.log(response)
			return $scope.vouchers = response, $scope.searchKeywords = "", $scope.filteredVouchers = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageVouchers = $scope.filteredVouchers.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredVouchers = $filter("filter")($scope.vouchers, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredVouchers = $filter("orderBy")($scope.vouchers, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageVouchers = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}

	$scope.loadVoucherData();

	$scope.editVoucher = function (voucher) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
				$location.path())) {
			logger.log("Oh snap! You are not allowed to modify the voucher.");
			return false;
		}
		$scope.header = "Edit Benefit Basket";
		// cope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
		$scope.voucherEditMode = false;
		$scope.voucherListMode = true;
		$scope.isExisting = true;
		$scope.showServices = false;
		$scope.voucherId = voucher.voucherId;
		$scope.voucherCode = voucher.voucherCode;
		$scope.voucherDesc = voucher.voucherDesc;
		$scope.voucherValue = voucher.voucherValue;
		$scope.active = voucher.active;
		$scope.coverType = voucher.coverType;
		$scope.schemeType = voucher.schemeType;
		$scope.mem.startDate = $filter('date')(new Date(voucher.startDate), 'MM-dd-yyyy');
		$scope.mem.endDate = $filter('date')(new Date(voucher.endDate), 'MM-dd-yyyy');

		$scope.services = voucher.services;
		$location.hash("editor");
		$anchorScroll();
	};

	$scope.loadServiceData = function () {
		$scope.voucherType = "H";
		voucherSvc.GetServiceProducts($scope.voucherType).success(function (response) {
			return $scope.services = response;
		});
	}
	// $scope.loadServiceData();

	$scope.voucherTypes = [
		/*{typeId:1,typeName:"Disbursement"},
			                     {typeId:2,typeName:"Cash"},*/
		{
			typeId: 4,
			typeName: "Health"
		}, {
			typeId: 3,
			typeName: "Cash"
		}
	]



	$scope.addVoucher = function () {

		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create benefit basket.");
			return false;
		}
		$scope.header = "Create Benefit Basket";

		$scope.loadServiceData();
		$scope.showServices = false;
		$scope.voucherEditMode = false;
		$scope.voucherListMode = true;
		$scope.isExisting = false;
		$scope.voucherId = 0;
		$scope.voucherCode = "";
		$scope.voucherDesc = "";
		$scope.active = true;
		$scope.previewServices = false;
		$scope.typeSelect = "";

		$scope.mem = [];
		$location.hash("editor");
		$anchorScroll();
	};

	$scope.cancelVoucher = function () {
		$scope.voucherEditMode = true;
		$scope.previewServices = false;
		$scope.voucherListMode = false;
		$scope.active = false;
		$scope.basketSelect = "";
		$scope.isExisting = false;
		$scope.projectSelect = "";
		$scope.services = [];
		$scope.basketValue = "";
		$scope.showServices = true;
		$scope.typeSelect = "";
		$scope.mem = [];
		$scope.voucherId = 0;
		$scope.voucherCode = "";
		$scope.voucherDesc = "";
		$scope.coverType = "";
		$scope.schemeType = "";

	}
	$scope.selectService = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
		$scope.selection = [];
		for (var i = 0; i < $scope.services.length; i++) {
			// var idx = $scope.selection.indexOf(branchId);

			if (index > -1 && $scope.services[i].isActive) {
				$scope.selection.push({
					"serviceId": $scope.services[i].serviceId
				});


			} else if (!$scope.services[i].isActive) {

				$scope.selection.splice(i, 1);

			}

			if (!$scope.services[i].isActive) {
				$scope.allItemsSelected = false;
				return;
			}
		}

		// If not the check the "allItemsSelected" checkbox
		$scope.allItemsSelected = true;
	};

	// This executes when checkbox in table header is checked
	$scope.selectAllServices = function () {
		// Loop through all the entities and set their isChecked property
		$scope.selection = [];
		for (var i = 0; i < $scope.services.length; i++) {
			$scope.services[i].isActive = $scope.allItemsSelected;
			if ($scope.services[i].isActive) {
				$scope.selection.push({
					"serviceId": $scope.services[i].serviceId
				});


			} else if (!$scope.services[i].isActive) {

				$scope.selection.splice(i, 1);

			}

		}
	};

	$scope.updVoucher = function () {
		var voucher = {};
		$scope.tempServices = [];
		var vValue = 0.00;
		if (!$voucherValid($scope.voucherDesc)) {
			logger.logWarning("Opss! You may have skipped specifying the Benefit Basket's Name. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.coverType)) {
			logger.logWarning("Opss! You may have skipped specifying the Cover type. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.schemeType)) {
			logger.logWarning("Opss! You may have skipped specifying the scheme type. Please try again.")
			return false;
		}
		for (var i = 0; i < $scope.services.length; i++) {
			if ($scope.services[i].isActive == true) {
				$scope.tempServices.push($scope.services[i]);
			}
		}
		if ($scope.tempServices.length <= 0) {
			logger.logWarning("Opss! You may have skipped specifying the Services for Benefit Basket. Please try again.")
			return false;
		}
		if (!$voucherValid($scope.voucherId))
			voucher.voucherId = 0;
		else
			voucher.voucherId = $scope.voucherId;
		voucher.voucherCode = $scope.voucherCode;
		voucher.voucherDesc = $scope.voucherDesc;
		voucher.active = $scope.active;
		voucher.createdBy = $rootScope.UsrRghts.sessionId;
		voucher.services = $scope.services;
		voucher.voucherValue = vValue;
		voucher.coverType = $scope.coverType;
		voucher.schemeType = $scope.schemeType;
		//voucher.startDate= $filter('date')($scope.mem.startDate,'MM-dd-yyyy');
		//voucher.endDate= $filter('date')($scope.mem.endDate,'MM-dd-yyyy');
		voucher.voucherType = "HB";


		for (var i = 0; i < $scope.services.length; i++) {
			if ($scope.services[i].serviceValue > 0) {
				vValue += $scope.services[i].serviceValue;
			}
		}
		if ($scope.typeSelect == 3) {
			voucher.voucherValue = $scope.voucherValue;
		} else {
			voucher.voucherValue = vValue;
		}
		console.log('updating voucher: ', voucher);
		blockUI.start();
		voucherSvc.UpdVoucher(voucher).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Benefit Basket information was saved succesfully")
				$scope.voucherId = 0;
				$scope.voucherCode = "";
				$scope.voucherDesc = "";
				$scope.coverType = "";
				$scope.schemeType = "";
				$scope.active = false;
				$scope.projectId = 0;
				$scope.basketId = 0;
				$scope.voucherEditMode = true;
				$scope.voucherListMode = false;
				$scope.projectSelect = "";
				$scope.services = [];
				$scope.basketValue = "";
				$scope.showServices = true;
				$scope.loadVoucherData();
				$scope.mem = [];
				$scope.typeSelect = "";
			} else {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};

}]).factory('voucherSvc', function ($http) {
	var EvVoucherSvc = {};
	EvVoucherSvc.GetVouchers = function () {
		return $http({
			url: '/compas/rest/voucher/gtVouchers/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		});
	};
	//	/
	EvVoucherSvc.GetServiceProducts = function (voucherType) {
		return $http({
			url: '/compas/rest/voucher/gtServiceProducts/' + voucherType,
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			}
		});
	};

	EvVoucherSvc.UpdVoucher = function (voucher) {
		return $http({
			url: '/compas/rest/voucher/updVoucher',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: voucher
		});
	};
	
	// judiciary active members
	EvVoucherSvc.GetCurrentDetailedMemberReport = function () {
		return $http({
			url: '/compas/rest/transaction/gtJDCMembers/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};
	
	//detailed utilizations
	EvVoucherSvc.GetAllDetailedMemberReport= function () {
		return $http({
			url: '/compas/rest/transaction/gtJDCMembers/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},
		});
	};
	
	
	// for service providers
	EvVoucherSvc.GetAllDetailedServiceProvidersTxn = function (gate) {
		console.log("SP TRANS :", JSON.stringify(gate));
		return $http({
			url: '/compas/rest/transaction/gtAllServiceProvidersTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: gate

		});
	};
	
	
	EvVoucherSvc.GetCurrentDetailedServiceProvidersTxn = function (gate) {
		console.log("SP TRANS :" +response);
		return $http({
			url: '/compas/rest/transaction/gtAllServiceProvidersTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: gate
		});
	};
	

	EvVoucherSvc.GetAllDetailedTxn = function (gate) {
		return $http({
			url: '/compas/rest/transaction/gtAllTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: gate

		});
	};
	
	
	// member utilization
	EvVoucherSvc.GetAllDetailedMemberUtilizations = function () {
		return $http({
			url: '/compas/rest/transaction/gtMemberUtilizations/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},
		});
	};
	
	EvVoucherSvc.GetCurrentDetailedMemberUtilizations = function () {
		return $http({
			url: '/compas/rest/transaction/gtMemberUtilizations/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};
	
	//detailed utilizations
	EvVoucherSvc.GetAllDetailedTotalUtilizations = function () {
		return $http({
			url: '/compas/rest/transaction/getTotalUtilization/',
			method: 'GET',
			headers: {
				'Content-Type': 'application/json'
			},
		});
	};
	

	//Fetch AKUH TRANSACTIONS
	EvVoucherSvc.GetAllDetailedAkuhTxn = function (claimgate) {
		return $http({
			url: '/compas/rest/transaction/gtAllAkuhTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: claimgate

		});
	};

	//fetch Gertrude Transactions
	EvVoucherSvc.GetAllDetailedGertTxn = function (Gertgate) {
		return $http({
			url: '/compas/rest/transaction/gtAllgTTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: Gertgate

		});
	};


	EvVoucherSvc.GetCurrentDetailedTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};

	//GET Current detailed transactions for Gertrude
	EvVoucherSvc.GetCurrentDetailedGertTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllgTTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
		});
	};

	//GET Current detailed transactions for AKUH
	EvVoucherSvc.GetCurrentDetailedAKUHTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllAkuhTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};

	//mpshah
	EvVoucherSvc.GetAllDetailedMpshahTxn = function (claimgate) {
		return $http({
			url: '/compas/rest/transaction/gtAllMpshahTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: claimgate

		});
	};

	EvVoucherSvc.GetCurrentDetailedMpshahTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllMpshahTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};


	//Nairobi Hospital
	EvVoucherSvc.GetAllDetailedNairobiHospTxn = function (claimgate) {
		return $http({
			url: '/compas/rest/transaction/gtAllNbiHospTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: claimgate

		});
	};

	//UHMC
	EvVoucherSvc.GetAllDetailedUHMCHospTxn = function (uhmcgate) {
		return $http({
			url: '/compas/rest/transaction/gtAllUHMCTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: uhmcgate

		});
	};


	EvVoucherSvc.GetCurrentDetailedUHMCTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllUHMCTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},

		});
	};

	//Clinix

	EvVoucherSvc.GetAllDetailedClinixTxn = function (clinixgate) {
		return $http({
			url: '/compas/rest/transaction/gtAllClinixTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: clinixgate

		});
	};


	EvVoucherSvc.GetCurrentDetailedClinixTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllClinixTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},

		});
	};

	EvVoucherSvc.GetCurrentDetailedNairobiHospTxn = function () {
		return $http({
			url: '/compas/rest/transaction/gtAllNbiHospTxnDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};

	//audit log
	EvVoucherSvc.GetAllDetailedAuditLog = function (claimgate) {
		return $http({
			url: '/compas/rest/user/gtAllAuditLogDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			data: claimgate

		});
	};

	EvVoucherSvc.GetCurrentDetailedAuditLog = function () {
		return $http({
			url: '/compas/rest/user/gtAllAuditLogDetail/',
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},


		});
	};


	return EvVoucherSvc;
}).factory('$voucherValid', function () {
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
/**
 * product Angular Module
 */
'use strict';
angular.module('app.product', []).controller("productsCtrl", ["$scope", "$filter", "productSvc","merchantSvc","organizationSvc", "$productValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, productSvc,merchantSvc,organizationSvc, $productValid, $rootScope, blockUI, logger, $location) {
	var init;
	var dlg = null;
	$scope.header="";
	$scope.productEditMode = false;
	$scope.productListMode = false;
	$scope.objProduct={};
	$scope.loadProductData = function (orgId) {
		$scope.products = [], $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = ""
			productSvc.GetProducts(orgId).success(function (response) {
				response = response.map(scheme => {
					if(!scheme.insuranceCode) { scheme.insuranceCode="N/A"; }
					return scheme;
				});
				console.log("Product schemes: ", response);
				return $scope.products = response, $scope.searchKeywords = "", $scope.filteredProducts = [], $scope.row = "", $scope.select = function (page) {
					var end, start;
					return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageProducts = $scope.filteredProducts.slice(start, end)
				}, $scope.onFilterChange = function () {
					return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
				}, $scope.onNumPerPageChange = function () {
					return $scope.select(1), $scope.currentPage = 1
				}, $scope.onOrderChange = function () {
					return $scope.select(1), $scope.currentPage = 1
				}, $scope.search = function () {
					return $scope.filteredProducts = $filter("filter")($scope.products, $scope.searchKeywords), $scope.onFilterChange()
				}, $scope.order = function (rowName) {
					return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredProducts = $filter("orderBy")($scope.products, rowName), $scope.onOrderChange()) : void 0
				}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageProducts = [], (init = function () {
					return $scope.search(), $scope.select($scope.currentPage)
				})();
			}).error(function (data, status, headers, config) {
				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			});
	}


	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			 $scope.productListMode = true;
			$scope.loadProductData(orgId);
		}
	}


	$scope.loadMerchantData = function () {
		$scope.merchants = []
		merchantSvc.GetMerchants($rootScope.UsrRghts.linkId).success(function (response) {
			return $scope.merchants = response
		})
	}
	$scope.loadMerchantData();
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
	$scope.editProduct = function (product) {
		if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to modify  Scheme.");
			return false;
		}
		$scope.header="Edit Scheme";
		$scope.productEditMode = true;
		$scope.productListMode=false;
		$scope.objProduct.startDate=$filter('date')(new Date(product.startDate),'MM-dd-yyyy');
		$scope.objProduct.endDate=$filter('date')(new Date(product.startDate),'MM-dd-yyyy');
		$scope.objProduct=product;
		$scope.objProduct.type=product.type;
		$scope.isDisabled = true;
	};

	$scope.addProduct = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new Scheme.");
			return false;
		}
		$scope.header="Create Scheme";
		$scope.productListMode=false;
		$scope.productEditMode = true;
		$scope.objProduct={};
		$scope.objProduct.type="";
	    $scope.objProduct.active = true;
		$scope.isDisabled = true;
	};

	$scope.calcelProduct = function () {
		$scope.productListMode=true;
		$scope.productEditMode = false;
		$scope.objProduct={};
		$scope.isDisabled = false;
	}

	$scope.updProduct = function () {
		var product = {};
		if (!$productValid($scope.objProduct.productCode)) {
			logger.logWarning("Opss! You may have skipped specifying the Scheme's Code. Please try again.")
			return false;
		}
		if (!$productValid($scope.objProduct.productName)) {
			logger.logWarning("Opss! You may have skipped specifying the Scheme's Name. Please try again.")
			return false;
		}
		if (!$productValid($scope.objProduct.startDate)) {
			logger.logWarning("Opss! You may have skipped specifying the Start Date. Please try again.")
			return false;
		}
		if (!$productValid($scope.objProduct.endDate)) {
			logger.logWarning("Opss! You may have skipped specifying the End Date. Please try again.")
			return false;
		}
		if (!$productValid($scope.objProduct.type)) {
			logger.logWarning("Opss! You may have skipped specifying the type. Please try again.")
			return false;
		}
		if ($scope.objProduct.startDate >= $scope.objProduct.endDate){
			logger.logWarning("Opss! The start date cannot be later than end date. Please try again.")
			return false;
		}
		var Dt= $filter('date')($scope.objProduct.startDate,'MM-dd-yyyy');
		if (!$productValid($scope.objProduct.productId))
			$scope.objProduct.productId = 0;
		//else
		$scope.objProduct.orgId= $scope.orgSelect;
		$scope.objProduct.startDate=$filter('date')($scope.objProduct.startDate,'yyyy-MM-dd');
		$scope.objProduct.endDate=$filter('date')($scope.objProduct.endDate,'yyyy-MM-dd');
		$scope.objProduct.createdBy = $rootScope.UsrRghts.sessionId;
		$scope.objProduct.fund = 1000000;
		blockUI.start()

		productSvc.UpdProduct($scope.objProduct).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Scheme information was saved succesfully")
				$scope.objProduct={};
				$scope.productListMode=true;
				$scope.productEditMode = false;
				$scope.objProduct={};
				//$scope.orgSelect="";
				$scope.isDisabled = false;
				$scope.loadProductData($scope.orgSelect);
			}
			else  {
				logger.logWarning(response.respMessage)
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};

}]).factory('productSvc', function ($http) {
	var compasProductSvc = {};
	compasProductSvc.GetProducts = function (orgId) {
		return $http({
			url: '/compas/rest/product/gtProducts/'+orgId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};

	compasProductSvc.UpdProduct = function (product) {
		return $http({
			url: '/compas/rest/product/updProduct',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data:product
		});
	};
	return compasProductSvc;
}).factory('$productValid', function () {
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
})
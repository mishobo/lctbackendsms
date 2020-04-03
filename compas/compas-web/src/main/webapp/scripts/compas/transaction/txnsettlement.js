/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.txnsettlement', []).controller("txnSettlemetCtrl", ["$scope", "$filter", "txnSettlementSvc","organizationSvc","txnVerificationSvc","$txnSettlementValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, txnSettlementSvc,organizationSvc,txnVerificationSvc,$txnSettlementValid, $rootScope, blockUI, logger, $location,$http,$window) {
	var init;
	var today = new Date();
	var dd = today.getDate();
	 var mm = today.getMonth()+1;
	 var yyyy = today.getFullYear();

	 $scope.maxDate = yyyy + '-' + mm + '-' + dd;
	$scope.showBillDetails=true;
	$scope.allTxnSelected=false;
	$scope.showProvider=false;
	
	$scope.mem=[];
	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			$scope.showProvider=true;
		}
	}
	$scope.onProviderChange=function(providerId){
		if(providerId>0){
			$scope.providerSelected=true;
		}
	}
	$scope.loadOrganizationData = function () {
		$scope.orgs = [];
		organizationSvc.GetOrganizations().success(function (response) {
			return $scope.orgs = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadOrganizationData();

	$scope.loadActiveroividers=function(){
		$scope.providers = [];
		txnVerificationSvc.GetProviders().success(function (response) {
			return $scope.providers = response;
		});
	}

	$scope.loadActiveroividers();
	$scope.getTransDetails=function(){
		$scope.trans = [];
		var txn={}
		$scope.fromDt= $filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
		$scope.toDt=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
		txn.fromDt= $scope.fromDt
		txn.toDt=$scope.toDt
		if($scope.fromDt == null){
			logger.logWarning("Opss! You may have skipped specifying from date")
			   return false;
		}else if($scope.toDt == null){
			logger.logWarning("Opss! You may have skipped specifying to date")
			   return false;
		}
		else if(txn.fromDt > txn.toDt){
			   logger.logWarning("Oops! From date cannot be later than to date.")
			   return false;
		   }
		else{
			$scope.total=0;
			$scope.showBillDetails=false;
		txn.orgId=$scope.orgSelect;
		txn.providerId=$scope.providerSelect;
		txn.txnStatus=2
		txn.paidStatus=112
		txnSettlementSvc.GetApprovedTrans(txn).success(function (response) {
			console.log(response)
			return $scope.trans = response, $scope.searchKeywords = "", $scope.filteredTrans = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageTrans = $scope.filteredTrans.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredTrans = $filter("filter")($scope.trans, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredTrans = $filter("orderBy")($scope.trans, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageTrans = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		});
	}
	}
	$scope.getTotal = function(){
		var total = 0;
		$scope.total=0;
		for(var i = 0; i < $scope.trans.length; i++){
			var product = $scope.trans[i];
			total += (product.amount);
		}
		$scope.total=$filter('number')(total, fractionSize);
		// return total;
	}

	$scope.selectAllTxn = function () {
		// Loop through all the entities and set their isChecked property
		$scope.txn_selection=[];
		for (var i = 0; i < $scope.trans.length; i++) {
			$scope.trans[i].isActive = $scope.allTxnSelected;
			if ($scope.trans[i].isActive) {
				$scope.txn_selection.push(
						$scope.trans[i].txnId
				);
			}
			else if(!$scope.trans[i].isActive){
				$scope.txn_selection.splice(i, 1);

			}
		}
	};


	/**
	 * select one item
	 * @param index
	 */
	$scope.selectTxn = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox

		$scope.txn_selection=[];
		var total = 0;
		$scope.total=0;
		for (var i = 0; i < $scope.trans.length; i++) {
			console.log($scope.trans[i].isActive);

			if (index > -1 && $scope.trans[i].isActive) {



				total += $scope.trans[i].amount;

				$scope.total=total;
				$scope.txn_selection.push(
						$scope.trans[i].txnId
				);
			}
			if(!$scope.trans[i].isActive){
				$scope.txn_selection.splice(i, 1);

			}

			if (!$scope.trans[i].isActive) {
				$scope.allTxnSelected = false;
				return;
			}

		}
		$scope.allTxnSelected = true;

	};

	$scope.generatefile=function(){
		if (!$txnSettlementValid($scope.mem.FromDt)) {
			logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
			return false;
		}
		if (!$txnSettlementValid($scope.mem.ToDt)) {
			logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
			return false;
		}

		var trans = {};
		$scope.bg_selection=[];
		$scope.fromDt= $filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
		$scope.toDt=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
		$scope.transferDt=$filter('date')($scope.mem.transferDate,'yyyy-MM-dd');

		trans.toDt=$scope.toDt
		trans.transferDate=$scope.transferDt;
		for(var i=0;i<$scope.trans.length;i++){
			if($scope.trans[i].isActive)
				$scope.bg_selection.push($scope.trans[i])
		}
		trans.selectedTrans=$scope.bg_selection;
	
		if($scope.bg_selection!=null){
			if($scope.bg_selection.length<=0){
				logger.logWarning("Error ! You have skipped selecting transaction, Please try again");
				return false;
			}
		}
		trans.txnStatus=3
		trans.paidStatus=112
		trans.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		txnSettlementSvc.GenerateSettlementFile(trans).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! Settlement File Generated succesfully")
				$location.path('/transaction/settlement');
				$scope.downloadFile(response.fileName);
				$scope.showBillDetails=true;
				$scope.trans=[];
				$scope.providerSelect=""
				$scope.mem=[];

			}
			else  {
				logger.logWarning(response.respMessage);
			}

			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	}
	 $scope.downloadFile = function (file_name) {
		 $window.location.href = '/compas/reports?type=RI&FrDt=' +$scope.fromDt+'&ToDt='+$scope.toDt+'&tDt='+$scope.transferDt+'&file_name='+file_name;
     }
	$scope.cancelVerify=function(){
		$scope.showBillDetails=true;
		$scope.trans=[];
		$scope.providerSelect=""
		$scope.mem=[];
	}
}]).factory('txnSettlementSvc', function ($http) {
	var compasTxnSettlement = {};

	compasTxnSettlement.UpdateTransStatus= function (txn) {
		return $http({
			url: '/compas/rest/transaction/updTransStatus/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: txn
		});
	};
	compasTxnSettlement.GetApprovedTrans= function (txn) {
		return $http({
			url: '/compas/rest/transaction/gtApprovedTrans/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: txn
		});
	};
	compasTxnSettlement.GenerateSettlementFile= function (txn) {
		return $http({
			url: '/compas/rest/transaction/gtGenerateSettlementFile/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: txn
		});
	}
	
	compasTxnSettlement.DownloadSettlementFile= function (file_name) {
		return $http({
			url: '/compas/rest/transaction/get_file/'+file_name,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});
	}
	return compasTxnSettlement;
}).factory('$txnSettlementValid', function () {
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

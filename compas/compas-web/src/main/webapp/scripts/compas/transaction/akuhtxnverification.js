/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.akuhtxnverification', []).controller("AkuhtxnVerificationCtrl", ["$scope", "$filter", "AkuhtxnVerificationSvc","merchantSvc", "organizationSvc","$txnVerificationValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","$modal","$sce", function ($scope, $filter, AkuhtxnVerificationSvc,merchantSvc,organizationSvc,$txnVerificationValid, $rootScope, blockUI, logger, $location,$http,$window,$modal,$sce) {
	var init;
	var today = new Date();
	var dd = today.getDate();
	 var mm = today.getMonth()+1;
	 var yyyy = today.getFullYear();

	 $scope.maxDate = yyyy + '-' + mm + '-' + dd;
	$scope.showBillDetails=true;
	$scope.allTxnSelected=false;
	 $scope.reject=false;
	 $scope.orgSelected=false;
	 $scope.state = 0;
	$scope.mem=[];
	$scope.reason=""
		$scope.styleClass="glyphicon glyphicon-remove"
			
			
	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			$scope.orgSelected=true;
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
	$scope.previewTrans=function(mem)
	{
		$scope.getTransDetails($scope.providerSelect);
	}

	$scope.loadActiveroividers=function(){
		$scope.providers = [];
		AkuhtxnVerificationSvc.GetProviders().success(function (response) {
			return $scope.providers = response;
		});
	}

	$scope.loadActiveroividers();
	$scope.getTransDetails=function(providerId){
		$scope.trans = [];
		var txnnstatus='0';
		var paidstatus='110'
		var claimtxns={}
		$scope.fromDt= $filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
		$scope.toDt=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
		claimtxns.fromDt= $scope.fromDt;
		claimtxns.toDt=$scope.toDt;
		if($scope.fromDt == null){
			logger.logWarning("Opss! You may have skipped specifying from date")
			   return false;
		}else if($scope.toDt == null){
			logger.logWarning("Opss! You may have skipped specifying to date")
			   return false;
		}
		else if(providerId == null){
			logger.logWarning("Opss! You may have skipped specifying the service provider")
			   return false;
		}
		else if(claimtxns.fromDt > claimtxns.toDt){
			   logger.logWarning("Oops! From date cannot be later than to date.")
			   return false;
		   }
		else{
		claimtxns.providerId=providerId;
		claimtxns.txnStatus=txnnstatus;
		claimtxns.paidStatus=paidstatus;
		claimtxns.orgId=$scope.orgSelect;
		AkuhtxnVerificationSvc.GetTransaction(claimtxns).success(function (response) {
			$scope.showBillDetails=false;
			return $scope.trans = response, 
			$scope.searchKeywords = "", $scope.filteredTrans = [], $scope.row = "", $scope.select = function (page) {
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
	$scope.verify=function(claimtxns){
		if(!claimtxns.allowed > 0){
			logger.logWarning("Opss! You may have skipped specifying amount to verify.")
			return false;
		}
		if(claimtxns.allowed > claimtxns.sponsorNet){
			logger.logWarning("Opss! The verify amount cannot be greater than transaction amount.")
			return false;
		}
		if(claimtxns.allowed != claimtxns.sponsorNet){
			claimtxns.diff = claimtxns.sponsorNet - claimtxns.allowed;
		}
		claimtxns.txnStatus=1
		claimtxns.paidStatus=111
		claimtxns.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		console.log(claimtxns)
		AkuhtxnVerificationSvc.UpdateTransStatus(claimtxns).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Transaction Verified succesfully");
				$location.path('transaction/vetakuhbills');
				$scope.showBillDetails=false;
				$scope.getTransDetails($scope.providerSelect);
				$scope.state +=1;
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
	
	$scope.reject=function(claimtxns){
		if(!claimtxns.reason > 0){
			logger.logWarning("Opss! You may have skipped specifying the reason to reject .")
			return false;
		}
		claimtxns.txnStatus= -1
		claimtxns.paidStatus= -111
		claimtxns.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		AkuhtxnVerificationSvc.UpdateTransStatus(claimtxns).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Transaction Cancelled succesfully");
				$location.path('transaction/vetakuhbills');
				$scope.showBillDetails=false;
				$scope.getTransDetails($scope.providerSelect);
				$scope.state +=1;
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
	
	$scope.cancelVerify=function(){
		$scope.showBillDetails=true;
		$scope.trans=[];
		$scope.providerSelect=""
		$scope.mem=[];
	}
	var ModalInstanceCtrl = function ($scope, $modalInstance,claimtxn) {
		$scope.$watch("state", function (newValue, oldValue) {
				if (newValue != oldValue) {
					$modalInstance.dismiss("cancel")
				}
		});
		$scope.cancelModel=function(){
				$modalInstance.dismiss("cancel")
		}
				
	};
	var ModalInstanceCtrl1 = function ($scope, $modalInstance, updatereason) {
	
		$scope.saveReason = function () {
			$modalInstance.close(updatereason)
			updatereason.txnStatus="-1"
			updatereason.paidStatus="-1";
			updatereason.reject=true;
			logger.logSuccess("Great! The Claim Transaction Rejected succesfully");
			//updatereason.styleClass="glyphicon glyphicon-ok"
			
		}
		$scope.cancelReason=function(){
				$modalInstance.dismiss("cancel")
		}
	};
	$scope.viewTransDtl = function (merchants) {
		 $scope.selectedMerIds=[];
		 var claimtxn={}
		$scope.contactname="";
		$scope.claimtxns={};
		$scope.claimtxns=merchants;	
		$scope.modalInstance  = $modal.open({
				templateUrl: 'views/transaction/akuhtxnverifydetails.html',
				controller:ModalInstanceCtrl,
			    backdrop: 'static',
				scope: $scope,
				resolve: {
					claimtxn: function () {
						 return $scope.claimtxns
					}					
			}
				
			});


		};
		$scope.unrejectTxn = function (merchants) {
			 merchants.reject=false;
			 merchants.txnStatus=0
			 merchants.paidStatus=110
		}
		$scope.rejectTxn = function (rejtrans) {
			 $scope.selectedMerIds=[];
			 var updatereason={}
			$scope.contactname="";
			$scope.updatereason={}
			$scope.updatereason=rejtrans;	
			$scope.modalInstance  = $modal.open({
					templateUrl: 'views/transaction/reason.html',
					controller:ModalInstanceCtrl1,
				    backdrop: 'static',
					scope: $scope,
					resolve: {
						updatereason: function () {
							 return $scope.updatereason
						}
				}
					
				});


			};

}]).factory('AkuhtxnVerificationSvc', function ($http) {
	var compasTxnSettlement = {};
	compasTxnSettlement.GetProviders= function () {
		return $http({
			url: '/compas/rest/transaction/gtActiveProviders/',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	compasTxnSettlement.GetTransaction= function (claimtxns) {
		return $http({
			url: '/compas/rest/transaction/getAkuhTransToVerify/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: claimtxns
		});
	};
	compasTxnSettlement.UpdateTransStatus= function (claimtxns) {
		return $http({
			url: '/compas/rest/transaction/updateAkuhTransStatus/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: claimtxns
		});
	};
	return compasTxnSettlement;
}).factory('$txnVerificationValid', function () {
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

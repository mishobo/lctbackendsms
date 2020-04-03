/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.txnapproval', []).controller("txnApprovalCtrl", ["$scope", "$filter", "txnApprovalSvc","organizationSvc","txnVerificationSvc","$txnApprovalValid", "$rootScope", "blockUI", "logger", "$location","$http","$window","$modal","$sce", function ($scope, $filter, txnApprovalSvc,organizationSvc,txnVerificationSvc,$txnApprovalValid, $rootScope, blockUI, logger, $location,$http,$window,$modal,$sce) {
	var init;
	var today = new Date();
	var dd = today.getDate();
	 var mm = today.getMonth()+1;
	 var yyyy = today.getFullYear();

	 $scope.maxDate = yyyy + '-' + mm + '-' + dd;
	$scope.showBillDetails=true;
	$scope.allTxnSelected=false;
	 $scope.reject=false;
	$scope.mem=[];
	$scope.reason=""
	$scope.state = 0;
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
		txnVerificationSvc.GetProviders().success(function (response) {
			return $scope.providers = response;
		});
	}

	$scope.loadActiveroividers();
	$scope.getTransDetails=function(providerId){
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
		else if(providerId == null){
			logger.logWarning("Opss! You may have skipped specifying the service provider")
			   return false;
		}
		else if(txn.fromDt > txn.toDt){
			   logger.logWarning("Oops! From date cannot be later than to date.")
			   return false;
		   }
		else{
		txn.providerId=providerId;
		txn.txnStatus=1
		txn.paidStatus=111
		txn.orgId=$scope.orgSelect;
		txnVerificationSvc.GetTransaction(txn).success(function (response) {
			$scope.showBillDetails=false;
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
	$scope.approve = function(txn){
		if(txn.allowed > txn.amount){
			logger.logWarning("Opss! The verify amount cannot be greater than transaction amount.")
			return false;
		}
		if(txn.allowed != txn.amount && txn.reason.length==0){
			logger.logWarning("Opss! The specify the reason.")
			return false;
		}
		txn.txnStatus=2
		txn.paidStatus=112
		txn.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		txnVerificationSvc.UpdateTransStatus(txn).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Transaction Approved succesfully")
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
	$scope.reject=function(txn){
		if(!txn.reason > 0){
			logger.logWarning("Opss! You may have skipped specifying the reason to reject .")
			return false;
		}
		txn.txnStatus= -1
		txn.paidStatus= -111
		txn.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		txnVerificationSvc.UpdateTransStatus(txn).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Transaction Rejected succesfully");
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
	$scope.cancelApprove=function(){
		$scope.showBillDetails=true;
		$scope.trans=[];
		$scope.providerSelect=""
		$scope.mem=[];
	}
	var ModalInstanceCtrl = function ($scope, $modalInstance,txns) {
		$scope.$watch("state", function (newValue, oldValue) {
			if (newValue != oldValue) {
				$modalInstance.dismiss("cancel")
			}
		});
		$scope.cancelModel=function(){
				$modalInstance.dismiss("cancel")
		}
		
	};
	var ModalInstanceCtrl1 = function ($scope, $modalInstance,updatereason) {
	
		$scope.saveReason = function () {
			$modalInstance.close(updatereason)
			updatereason.txnStatus="-1"
			updatereason.paidStatus="-1";
			updatereason.reject=true;
			//updatereason.styleClass="glyphicon glyphicon-ok"
			
		}
		$scope.cancelReason=function(){
				$modalInstance.dismiss("cancel")
		}
	};
	$scope.viewTransDtl = function (merchants) {
		 $scope.selectedMerIds=[];
		 //$scope.reject=true
		 var txns={}
		$scope.contactname="";
		$scope.txn={};
		$scope.txn=merchants;	
		$scope.modalInstance  = $modal.open({
				templateUrl: 'views/transaction/txnapprovedetails.html',
				controller:ModalInstanceCtrl,
			    backdrop: 'static',
				scope: $scope,
				resolve: {
					txns: function () {
						//console.log($scope.familyMemDetails);
						 return $scope.txn
					}/*,contactname:function(){
						return $scope.contactname
					}*/
					
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
							//console.log($scope.familyMemDetails);
							 return $scope.updatereason
						}/*,contactname:function(){
							return $scope.contactname
						}*/
						
				}
					
				});


			};
}]).factory('txnApprovalSvc', function ($http) {
	var compasTxnSettlement = {};

	/*compasTxnSettlement.UpdateTransStatus= function (txn) {
		return $http({
			url: '/compas/rest/transaction/updTransStatus/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: txn
		});
	};*/
	return compasTxnSettlement;
}).factory('$txnApprovalValid', function () {
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

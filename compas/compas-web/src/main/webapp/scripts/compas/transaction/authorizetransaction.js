/**
 * Transaction Authorization Angular Module
 */
'use strict';
angular.module('app.authtransaction', []).controller("authTransCtrl", ["$scope", "$filter", "authTransSvc","merchantSvc", "txnVerificationSvc", "$rootScope", "blockUI", "logger", "$location", "$timeout","$authTransValid","organizationSvc","$modal", function ($scope, $filter, authTransSvc,merchantSvc,txnVerificationSvc, $rootScope, blockUI, logger, $location, $timeout,$authTransValid,organizationSvc,$modal) {
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
			$scope.getTransDetails($scope.orgSelect);
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
	$scope.refresh = function (){
		$scope.getTransDetails($scope.orgSelect)
	};
	$scope.getTransDetails=function(orgId){
		authTransSvc.GetAuthPendingTrans(orgId).success(function (response) {
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
	$scope.authorize=function(txn){
		if(!txn.allowed > 0){
			logger.logWarning("Opss! You may have skipped specifying amount to verify.")
			return false;
		}
		if(txn.allowed > txn.amount){
			logger.logWarning("Opss! The verify amount cannot be greater than transaction amount.")
			return false;
		}
		if(txn.allowed != txn.amount && !txn.reason.length > 0){
			logger.logWarning("Opss! The Please provide the reason.")
			return false;
		}
		if(txn.allowed != txn.amount)
			txn.diff = txn.amount - txn.allowed;
		txn.txnStatus=0
		txn.paidStatus=110
		txn.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		authTransSvc.UpdateTransStatus(txn).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Transaction Authorized succesfully");
				$scope.showBillDetails=false;
				$scope.getTransDetails($scope.orgSelect);
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
		if(!txn.reason.length > 0){
			logger.logWarning("Opss! You may have skipped specifying the reason to reject .")
			return false;
		}
		txn.txnStatus= -3
		txn.paidStatus= -113
		txn.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		authTransSvc.UpdateTransStatus(txn).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Transaction Rejected succesfully");
				$scope.showBillDetails=false;
				$scope.getTransDetails($scope.orgSelect);
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
	var ModalInstanceCtrl1 = function ($scope, $modalInstance, updatereason) {
	
		$scope.saveReason = function () {
			$modalInstance.close(updatereason)
			updatereason.txnStatus="-1"
			updatereason.paidStatus="-1";
			updatereason.reject=true;
			logger.logSuccess("Great! The Transaction Rejected succesfully");
			//updatereason.styleClass="glyphicon glyphicon-ok"
			
		}
		$scope.cancelReason=function(){
				$modalInstance.dismiss("cancel")
		}
	};
	$scope.viewTransDtl = function (merchants) {
		 $scope.selectedMerIds=[];
		 var txns={}
		$scope.contactname="";
		$scope.txn={};
		$scope.txn=merchants;	
		$scope.modalInstance  = $modal.open({
				templateUrl: 'views/transaction/txnauthorizedetails.html',
				controller:ModalInstanceCtrl,
			    backdrop: 'static',
				scope: $scope,
				resolve: {
					txns: function () {
						 return $scope.txn
					}					
			}
				
			});


		};

}]).factory('authTransSvc', function ($http) {
	var shpAuthTransSvc = {};

	shpAuthTransSvc.GetAuthPendingTrans = function (orgId) {
		return $http({
			url: '/compas/rest/transaction/gtAuthTransList/'+ orgId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	shpAuthTransSvc.GetAuthPendingTransByProvider = function (providerId) {
		return $http({
			url: '/compas/rest/transaction/gtAuthTransListByProvider/' + providerId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}		
		});
	};

	shpAuthTransSvc.rejectAuthEntry = function (cancelDetail) {
		////console.log(cancelDetail);
		return $http({
			url: '/compas/rest/transaction/rejectAuthTrans',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: cancelDetail
		});
	};
	shpAuthTransSvc.updateAuthEntry = function (updateAuthDetails) {
		//console.log(updateAuthDetails);
		return $http({
			url: '/compas/rest/transaction/updateAuthTrans',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: updateAuthDetails
		});
	};
	shpAuthTransSvc.UpdateTransStatus= function (txn) {
		return $http({
			url: '/compas/rest/transaction/authorizeTransaction/',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: txn
		});
	};
	return shpAuthTransSvc;
}).factory('$authTransValid', function () {
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
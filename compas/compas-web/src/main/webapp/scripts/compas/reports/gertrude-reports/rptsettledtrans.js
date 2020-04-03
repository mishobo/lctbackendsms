/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptSettledTransaction', []).controller("rptsettledTransactionCtrl", ["$scope", "$filter", "settledTransactionsSvc","memberInquirySvc", "merchantSvc", "productSvc", "organizationSvc","agentSvc","deviceSvc","voucherTopupSvc", "$memValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, settledTransactionsSvc,memberInquirySvc, merchantSvc, productSvc,organizationSvc,agentSvc,deviceSvc,voucherTopupSvc,$memValid, $rootScope, blockUI, logger, $location,$http,$window) {
	$scope.mem=[];
    $scope.showFilters=true;
    $scope.showDetails=true;
    $scope.showTransactions=false;
    $scope.showProvider=false;
    $scope.showScheme=false;
    $scope.tempAmountList=[];
    $scope.voideAmount=0;
    $scope.invalidAmount=0;
    $scope.totalAmount=0;
    $scope.netAmount=0;
    $scope.billNo=0;
    $scope.states=0;
    var tmrAuthData;
    var init;
	var today = new Date();
	var dd = today.getDate();
	 var mm = today.getMonth()+1;
	 var yyyy = today.getFullYear();

	 $scope.maxDate = yyyy + '-' + mm + '-' + dd;
    $scope.excel = "E";
    $scope.pdf = "P";
    $scope.ready = false;
	$scope.changed = function(){
		$scope.ready = false;
	} 
    
    $scope.exportReport=function(type)    {
        var FromDt= $filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
        var ToDt=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
        var orgId=$scope.orgSelect;
        if(FromDt == null){
       	 logger.logWarning("Opss! You may have skipped specifying the Start date. Please try again.")
       	 return false;  
        }
        else if(ToDt == null){
       	 logger.logWarning("Opss! You may have skipped specifying the End date. Please try again.")
       	 return false;  
        }else if(orgId == null){
       	 logger.logWarning("Opss! You may have skipped specifying the Organization. Please try again.")
       	 return false;  
        }else if(FromDt > ToDt){
		   logger.logWarning("Oops! From date cannot be later than to date.")
		   return false;
	   }else if(FromDt > ToDt){
 		   logger.logWarning("Oops! From date cannot be later than to date.")
 		   return false;
 	   }else if(FromDt > ToDt){
 		   logger.logWarning("Oops! From date cannot be later than to date.")
 		   return false;
 	   }else{
            $scope.url = '/compas/reports?type=SB&FrDt=' +FromDt+'&ToDt='+ToDt+'&orgId='+orgId+'&eType='+type;
        }
         

   }
    
    
    $scope.$watch("states", function (newValue, oldValue) {
    	if (newValue != oldValue) {
    		if($scope.merchantSelect > 0 && $scope.productSelect == 0){
    			return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {merchantId: $scope.merchantSelect}), $scope.onFilterChange()
    		}
    		else if($scope.merchantSelect == 0 && $scope.productSelect > 0){
    			return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {productId: $scope.productSelect}), $scope.onFilterChange()
    		}
    		else if($scope.merchantSelect > 0 && $scope.productSelect > 0){
    			return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {productId: $scope.productSelect, merchantId: $scope.merchantSelect}), $scope.onFilterChange()
    		}
    		else{
    			return $scope.filteredUserTxns = $scope.userTxns;
    		}
    	}
    });
$scope.previewReport=function(mem)
{
	  // $scope.showDetails=false;
//	 if (!$memStatementValid(mem.FromDt)) {
//      logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
//      return false;
//  }
//	 else  if (!$memStatementValid(mem.ToDt)) {
//      logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
//      return false;
//  }
	  // $scope.showDetails=false;
	   $scope.userTransaction = [];
	   $scope.fromDt= $filter('date')(mem.FromDt,'yyyy-MM-dd');
	   $scope.toDt=$filter('date')(mem.ToDt,'yyyy-MM-dd');
	   if(($scope.fromDt !=null && $scope.toDt !=null) && ($scope.fromDt > $scope.toDt)){
		   logger.logWarning("Oops! From date cannot be later than to date.")
		   return false;
	   }
	   else{
		   $scope.loadTransactions();  
	   } 
	
	
}

$scope.loadTransactions=function(){
	   $scope.showDetails=false;
	   $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
  var gate ={};
 
  gate.fromDate=$scope.fromDt;
  gate.toDate=$scope.toDt;
  gate.orgId = $scope.orgSelect;
  settledTransactionsSvc.GetSettledDetailedTxn(gate).success(function (response) {
	  $scope.showTransactions=true;
	  if(response.length>0){
		  $scope.ready = true;
	  }
      return $scope.userTxns = response, $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "",  $scope.states+=1, $scope.select = function (page) {
          var end, start;
          return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUserTxns = $scope.filteredUserTxns.slice(start, end)
      }, $scope.onFilterChange = function () {
          return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
      }, $scope.onNumPerPageChange = function () {
          return $scope.select(1), $scope.currentPage = 1
      }, $scope.onOrderChange = function () {
          return $scope.select(1), $scope.currentPage = 1
      }, $scope.search = function () {
          return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, $scope.searchKeywords), $scope.onFilterChange()
      }, $scope.order = function (rowName) {
          return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUserTxns = $filter("orderBy")($scope.userTxns, rowName), $scope.onOrderChange()) : void 0
      }, $scope.numPerPageOpt = [10,20, 50, 100,200], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUserTxns = [], (init = function () {
          return $scope.search(), $scope.select($scope.currentPage)
      })();
  }).error(function (data, status, headers, config) {
      logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
  });
}
//$scope.loadTransactions();

	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			$scope.fromDt=null;
			$scope.toDt=null;
			$scope.mem.FromDt = null;
			$scope.mem.ToDt = null;
			$scope.loadTransactions();
			$scope.loadMerchantData();
			$scope.loadProductData(orgId);
		    $scope.showProvider=true;
		    $scope.showScheme=true;
		    $scope.productSelect=0;
		    $scope.merchantSelect=0;
		    $scope.ready = false;
		}
	}
	$scope.loadOrganizationData = function () {
		$scope.orgs = [];
		organizationSvc.GetOrganizations().success(function (response) {
			console.log(response)
			return $scope.orgs = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadOrganizationData();
	$scope.onFilter=function(){
		if($scope.merchantSelect > 0 && $scope.productSelect == 0){
			return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {merchantId: $scope.merchantSelect}), $scope.onFilterChange()
		}
		else if($scope.merchantSelect == 0 && $scope.productSelect > 0){
			return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {productId: $scope.productSelect}), $scope.onFilterChange()
		}
		else if($scope.merchantSelect > 0 && $scope.productSelect > 0){
			return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {productId: $scope.productSelect, merchantId: $scope.merchantSelect}), $scope.onFilterChange()
		}
		else{
			return $scope.filteredUserTxns = $scope.userTxns;
		}
	}
	$scope.loadMerchantData = function () {
		$scope.merchants = [];
		merchantSvc.GetMerchants().success(function (response) {
			return $scope.merchants = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	
	$scope.loadProductData = function (orgId) {
		$scope.products = [];
		productSvc.GetProducts(orgId).success(function (response) {
			return $scope.products = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.onTimeout = function(){

	    	if ($scope.userTxns.length > 0) {
	          
	            $scope.$apply($scope.loadTransactions());
	         //   $scope.authPending = $scope.setAuthPending();
	        }
	
    tmrAuthData = $timeout($scope.onTimeout,1000);
} 


}]).factory('settledTransactionsSvc', function ($http) {
    var settledTransactions = {};
    settledTransactions.GetSettledDetailedTxn = function (gate) {
        return $http({
            url: '/compas/rest/transaction/gtSettledTxnDetail/',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data:gate
          
        });
    };
   
    
    return settledTransactions;
}).factory('$memValid', function () {
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

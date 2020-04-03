/**
 * Transaction Authorization Angular Module
 */
'use strict';
angular.module('app.offlinetransaction', []).controller("offlineTransCtrl", ["$scope", "$filter", "offlineTransSvc","merchantSvc", "txnVerificationSvc", "$rootScope", "blockUI", "logger", "$location", "$timeout","$offlineTransValid","organizationSvc","$modal", function ($scope, $filter, offlineTransSvc,merchantSvc,txnVerificationSvc, $rootScope, blockUI, logger, $location, $timeout,$offlineTransValid,organizationSvc,$modal) {
	var init; 
	$scope.showOrg=true;
	$scope.showSearch=false;	
	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			$scope.showSearch=true;
		}
	}
	
	$scope.onProgrammeChange=function(programmeId){
		if(programmeId>0){
			$scope.getMemberVouchers();
		}
	}
	
	$scope.onVoucherChange=function(voucher){
		$scope.vouchers = angular.fromJson(voucher)
	}
	
	$scope.onServiceChange=function(service){
		$scope.services = angular.fromJson(service)
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
	
	$scope.loadMerchantData = function () {
		$scope.merchants = [];
		merchantSvc.GetMerchants().success(function (response) {
			return $scope.merchants = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadMerchantData();
	$scope.cancel=function(){
		$scope.orgSelect=""
		$scope.showSearch=true;
	}
	
	$scope.viewMember = function (memberNo) {
		$scope.member={}
		offlineTransSvc.GetMemberBasicDetails(memberNo).success(function (response) {
			$scope.member=response;
			if($scope.member.memberId == 0){
				logger.logWarning("Member not registered")
			}
			else{
			$scope.showSearch = false;
			$scope.showBasics = true;
			$scope.showNext = true;
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	};

	
	$scope.cancelMember = function () {
		this.memberNo = "";
		$scope.showBasics = false;
		$scope.showSearch = true;
	};
	
	$scope.getMemberProgrammes = function () {
		offlineTransSvc.GetMemberProgrammes($scope.member).success(function (response) {
			$scope.member.programmes=response;
			if($scope.member.programmes == []){
				logger.logWarning("No services available")
			}
			else{
			$scope.showSearch = false;
			$scope.showBasics = true;
			$scope.showNext = false;
			$scope.open = true;
			
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	};
	
	$scope.getMemberVouchers = function () {
		$scope.member.programmeId= $scope.programmeSelect;
		console.log($scope.member)
		offlineTransSvc.GetMemberVouchers($scope.member).success(function (response) {
			console.log(response)
			$scope.member.vouchers=response;
			if($scope.member.vouchers == []){
				logger.logWarning("No services available")
			}
			else{
			$scope.showSearch = false;
			$scope.showBasics = true;
			$scope.showNext = false;
			$scope.open = true;
			
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	};

	
	$scope.updateTransaction = function () {
		var mem = {};
		mem.memberNo = $member.memberNo;
		mem.cardNumber = $member.cardNumber;
		mem.
		offlineTransSvc.GetMemberVouchers($scope.member).success(function (response) {
			console.log(response)
			$scope.member.vouchers=response;
			if($scope.member.vouchers == []){
				logger.logWarning("No services available")
			}
			else{
			$scope.showSearch = false;
			$scope.showBasics = true;
			$scope.showNext = false;
			$scope.open = true;
			
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	};


}]).factory('offlineTransSvc', function ($http) {
	var shpAuthTransSvc = {};

	shpAuthTransSvc.GetMemberBasicDetails = function (memberNo) {
	 	   return $http({
	     	url: '/compas/rest/member/gtMemberBasicDetails?memberNo=' + encodeURIComponent(memberNo),
	         method: 'GET' 
	     });
		};
		
		shpAuthTransSvc.GetMemberProgrammes = function (data) {
		 	   return $http({
		     	url: '/compas/rest/member/gtMemberProgrammes',
		         method: 'POST',
		         headers: {'Content-Type': 'application/json'},
		         data: data
		     });
			};
			
			shpAuthTransSvc.GetMemberVouchers = function (data) {
			 	   return $http({
			     	url: '/compas/rest/member/gtMemberVouchers',
			         method: 'POST',
			         headers: {'Content-Type': 'application/json'},
			         data: data
			     });
				};



	return shpAuthTransSvc;
}).factory('$offlineTransValid', function () {
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
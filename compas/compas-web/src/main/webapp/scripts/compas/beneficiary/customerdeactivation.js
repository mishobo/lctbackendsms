/**
 * Member Angular Module
 */
'use strict';
angular.module('app.deactivate', []).controller("deactivateCtrl", ["$scope", "$filter", "deactivateSvc","memberInquirySvc","programmeSvc","organizationSvc", "$detachValid", "$rootScope", "blockUI", "logger" ,"$location", function ($scope, $filter, deactivateSvc,memberInquirySvc,programmeSvc,organizationSvc, $detachValid, $rootScope, blockUI, logger, $location) {
    var init;
    $scope.showSearch = true;
    $scope.viewMember = function (memberNo) {
			$scope.member={}
			deactivateSvc.GetMemberDetachDetails(memberNo).success(function (response) {
				$scope.member=response;
				if($scope.member.memberId == 0){
					logger.logWarning("Member not registered")
				}
//				else if(!$scope.member.hasBio){
//					logger.logWarning("Bio details have not been captured")
//				}
				else{
				$scope.showSearch = false;
				$scope.deactivates = $scope.member.deactivates;
				}
			}).error(function (data, status, headers, config) {
				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			});
			
			
			
		};

	    
	    $scope.deactivate=function(){
	    	console.log($scope.member)
			if($scope.member.reason == null || $scope.member.reason == ""){
				logger.logWarning("Deactivation reason is required")
				return false;
			}else{
				$scope.member.createdBy = $rootScope.UsrRghts.sessionId;
				$scope.member.deactivate="D";
				blockUI.start();
				deactivateSvc.DetachFingerprints($scope.member).success(function (response) {
					if (response.respCode == 200) {
						logger.logSuccess("Great! The member has been deactivated");
						$scope.showSearch = true;
					}
					else  {
						logger.logWarning(response.respMessage);
					}
					blockUI.stop();
				}).error(function (data, status, headers, config) {
					blockUI.stop();
					logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
				});
				
			}
	   }

	    $scope.cancel=function(){
	    	$scope.showSearch = true;
	    	$scope.memberNo = "";
	    }
}]).factory('deactivateSvc', function ($http) {
	var compasDeactivateSvc = {};
	compasDeactivateSvc.GetMemberDetachDetails = function (memberNo) {
 	   return $http({
     	url: '/compas/rest/member/gtMemberDeactivationDetails?memberNo=' + encodeURIComponent(memberNo),
         method: 'GET' 
     });
	};
	compasDeactivateSvc.DetachFingerprints = function (member) {
	   return $http({
		   url: '/compas/rest/member/deactivateMember',
		   method: 'POST',
		   headers: { 'Content-Type': 'application/json' },
		   data: member
       
	   });
 	};
	return compasDeactivateSvc;
}).factory('$detachValid', function () {
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

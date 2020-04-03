/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptmemberlist', []).controller("rptMemberListCtrl", ["$scope", "$filter", "rptMemberListSvc", "$rptMemberListValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, rptMemberListSvc, $rptMemberListValid, $rootScope, blockUI, logger, $location,$http,$window) {
 
   
	 $scope.previewReport=function()
	    {
	    
	   var brokerId=$rootScope.UsrRghts.linkId;
	   $scope.url = '/shp/reports?type=ML&BrokerId='+brokerId;
   }
  

}]).factory('rptMemberListSvc', function ($http) {
    var shpMemberList = {};

   
       
    return shpMemberList;
}).factory('$rptMemberListValid', function () {
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

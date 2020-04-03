/**
 * Broker Angular Module
 */
'use strict';
angular.module('app.memberCard', []).controller("memberCardCtrl", ["$scope", "$filter", "cardIssSvc","memberInquirySvc", "$memberCardValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, cardIssSvc,memberInquirySvc, $memberCardValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
   
    $scope.loadMemberData = function () {
    	
        $scope.members = [];
        memberInquirySvc.GetMembers($rootScope.UsrRghts.linkId).success(function (response) {
        	$scope.members =response;
        })
    }
        	
            return $scope.members = response
    $scope.calcelCardIss = function () {
        $scope.brokerEditMode = false;
        $scope.brokerId = 0;
        $scope.brokerName = "";
    }

    $scope.updCardIss = function () {
        var memberCard = [];
        
  
        	memberCard.cardNo = $scope.cardNo;
        memberCard.memberSelect = $scope.memberSelect;
        memberCard.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        cardIssSvc.UpdCard(memberCard).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Card Issue information was saved succesfully")
                $scope.memberId = 0;
                $scope.cardNo = "";
          
            }
           
            else {
                logger.logWarning("Opss! Something went wrong while updating the Funder. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('memberCardSvc', function ($http) {
    var shpMemCardSvc = {};
 
    shpMemCardSvc.UpdCard = function (memberCard) {
        return $http({
            url: '/compas/rest/member/updatecardlinkid',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: {
                'cardNo': memberCard.cardNo,
                'memberId': memberCard.memberId,
                'createdBy': memberCard.createdBy
            }
        });
    };
    return shpMemCardSvc;
}).factory('$memCardValid', function () {
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
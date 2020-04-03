/**
 * Provider Angular Module
 */
'use strict';
angular.module('app.acivatecard', []).controller("activateCardsCtrl", ["$scope", "$filter", "activateCardsSvc", "$activateCardsValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, activateCardsSvc, $activateCardsValid, $rootScope, blockUI, logger, $location) {
    var init;

    $scope.loadMemberListData = function () {
        $scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.activateMode = false;
        activateCardsSvc.GetMembersToActivate().success(function (response) {
            return $scope.members = response, $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMembers = $scope.filteredMembers.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredMembers = $filter("filter")($scope.members, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMembers = $filter("orderBy")($scope.members, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageMembers = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadMemberListData();

  
    $scope.activateCard = function (memberId) {
        var member= [];
       
        member.memberId = memberId;
        member.cardStatus = "A";
      
        blockUI.start()
        activateCardsSvc.UpdCardStatus(member).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! Card Activated succesfully")
                $scope.memberId = 0;
                $scope.cardStatus = "";
                $scope.activateMode = false;
                $scope.loadMemberListData();
            }
           
            else {
                logger.logWarning("Opss! Something went wrong while activating the card. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('activateCardsSvc', function ($http) {
    var compasActivateCardsSvc = {};
    compasActivateCardsSvc.GetMembersToActivate = function () {
        return $http({
            url: '/compas/rest/member/gtMemberCa/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasActivateCardsSvc.UpdCardStatus = function (member) {
        return $http({
            url: '/compas/rest/member/updCardStatus',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: {
                'memberId': member.memberId,
                'cardStatus': member.cardStatus
            }
        });
    };
    return compasActivateCardsSvc;
}).factory('$activateCardsValid', function () {
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
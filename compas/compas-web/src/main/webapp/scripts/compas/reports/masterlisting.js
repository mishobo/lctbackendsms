/**
 * Transaction Held Angular Module
 */
'use strict';
angular.module('app.rptmasterlisting', []).controller("rptmasterlistingCtrl", ["$scope", "$filter", "mSvc", "$mValid", "$rootScope", "blockUI", "logger", "$location","$http","$window", function ($scope, $filter, mSvc, $mValid, $rootScope, blockUI, logger, $location,$http,$window) {
    $scope.transMemList = [];
    $scope.mem=[];
    var init;
    $scope.masters=[]
    $scope.showdata=true;
    $scope.showSer=true;
    $scope.showAgent=false;

   
   $scope.masters=[{id:1,masterName:"Agents"},{id:2,masterName:"Devices"}]
   
   $scope.loadAgentList = function () {
       $scope.txns = [], $scope.searchKeywords = "", $scope.filteredTxns = [], $scope.row = "", $scope.branchEditMode = false;
       mSvc.GetAgentList().success(function (response) {
           return $scope.txns = response, $scope.searchKeywords = "", $scope.filteredTxns = [], $scope.row = "", $scope.select = function (page) {
        	   console.log($scope.txns)
        	    $scope.colDesc="Agent Name";
        	   //$scope.showSer=f;
               var end, start;
               return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageTxns = $scope.filteredTxns.slice(start, end)
           }, $scope.onFilterChange = function () {
               return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
           }, $scope.onNumPerPageChange = function () {
               return $scope.select(1), $scope.currentPage = 1
           }, $scope.onOrderChange = function () {
               return $scope.select(1), $scope.currentPage = 1
           }, $scope.search = function () {
               return $scope.filteredTxns = $filter("filter")($scope.txns, $scope.searchKeywords), $scope.onFilterChange()
           }, $scope.order = function (rowName) {
               return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredTxns = $filter("orderBy")($scope.txns, rowName), $scope.onOrderChange()) : void 0
           }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
               return $scope.search(), $scope.select($scope.currentPage)
           })();
       }).error(function (data, status, headers, config) {
           logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
       });
   }
   
   $scope.loadDeviceList = function () {
       $scope.txns = [], $scope.searchKeywords = "", $scope.filteredTxns = [], $scope.row = "", $scope.branchEditMode = false;
       mSvc.GetDeviceList().success(function (response) {
           return $scope.txns = response, $scope.searchKeywords = "", $scope.filteredTxns = [], $scope.row = "", $scope.select = function (page) {
        	   console.log($scope.txns)
        	   $scope.colDesc="Device Name";
        	   $scope.showSer=false;
        	   $scope.showAgent=true;
               var end, start;
               return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageTxns = $scope.filteredTxns.slice(start, end)
           }, $scope.onFilterChange = function () {
               return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
           }, $scope.onNumPerPageChange = function () {
               return $scope.select(1), $scope.currentPage = 1
           }, $scope.onOrderChange = function () {
               return $scope.select(1), $scope.currentPage = 1
           }, $scope.search = function () {
               return $scope.filteredTxns = $filter("filter")($scope.txns, $scope.searchKeywords), $scope.onFilterChange()
           }, $scope.order = function (rowName) {
               return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredTxns = $filter("orderBy")($scope.txns, rowName), $scope.onOrderChange()) : void 0
           }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
               return $scope.search(), $scope.select($scope.currentPage)
           })();
       }).error(function (data, status, headers, config) {
           logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
       });
   }
   
   $scope.$watch("masterSelect", function (newValue, oldValue) {
       if ($mValid(newValue)) {
           if (newValue != oldValue) {
        	    $scope.showdata=false;
              if(newValue==1){
            	  $scope.loadAgentList() 
            	  $scope.showAgent=false;
            	  $scope.showSer=true;
              }else{
            	  $scope.showSer=false;
            	  $scope.showAgent=true;
            	  $scope.loadDeviceList() 
              }
           }
       }
       
         //  $scope.devices = [];
     //  $scope.agents = [];
   });
   
}]).factory('mSvc', function ($http) {
    var shpMemStatement = {};
    shpMemStatement.GetSafComTxns= function ($scope) {
        return $http({
            url: '/compas/rest/transaction/gtSafComTxns',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
   
    shpMemStatement.GetAgentList= function ($scope) {
        return $http({
            url: '/compas/rest/transaction/gtAgentList',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    shpMemStatement.GetDeviceList= function ($scope) {
        return $http({
            url: '/compas/rest/transaction/gtDeviceList',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    return shpMemStatement;
}).factory('$mValid', function () {
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

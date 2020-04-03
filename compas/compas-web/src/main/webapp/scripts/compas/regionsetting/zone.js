/**
 * zone Angular Module
 */
'use strict';
angular.module('app.zone', []).controller("zonesCtrl", ["$scope", "$filter", "zoneSvc", "$zoneValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, zoneSvc, $zoneValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.header="";
    $scope.loadZoneData = function () {
        $scope.zones = [], $scope.searchKeywords = "", $scope.filteredZones = [], $scope.row = "", $scope.zoneEditMode = false;
        zoneSvc.GetZones().success(function (response) {
            return $scope.zones = response, $scope.searchKeywords = "", $scope.filteredZones = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageZones = $scope.filteredZones.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredZones = $filter("filter")($scope.zones, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredZones = $filter("orderBy")($scope.zones, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageZones = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadZoneData();

    $scope.editZone = function (zone) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify  Region.");
            return false;
        }
        $scope.header="Edit Region";
        $scope.zoneEditMode = true;
        $scope.zoneId = zone.zoneId;
        $scope.zoneCode = zone.zoneCode;
        $scope.zoneName = zone.zoneName;
        $scope.active = zone.active;
        $scope.isExisting = true;
    };

    $scope.addZone = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new Region.");
            return false;
        }
        $scope.header="Create Region";
        $scope.zoneEditMode = true;
        $scope.zoneId = 0;
        $scope.zoneCode="";
        $scope.zoneName = "";
        $scope.active =false;
        $scope.isExisting = false;
    };

    $scope.calcelZone = function () {
        $scope.zoneEditMode = false;
        $scope.zoneId = 0;
        $scope.zoneCode="";
        $scope.zoneName = "";
        $scope.isExisting = false;
    }

    $scope.updZone = function () {
        var zone = {};
        if (!$zoneValid($scope.zoneCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Region's Code. Please try again.")
            return false;
        }
        if (!$zoneValid($scope.zoneName)) {
            logger.logWarning("Opss! You may have skipped specifying the Region's Name. Please try again.")
            return false;
        }
        if ($scope.zoneName.length > 50) {
            logger.logWarning("Opss!Region Name is reach to maximum length of 50 ")
            return false;
        }
        if (!$zoneValid($scope.zoneId))
            zone.zoneId = 0;
        else
            zone.zoneId = $scope.zoneId;
        zone.zoneCode = $scope.zoneCode;
        zone.zoneName = $scope.zoneName;
        //zone.organizationId =  $rootScope.UsrRghts.linkId;
        zone.active = $scope.active;
        zone.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        console.log(zone);
        zoneSvc.UpdZone(zone).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Region information was saved succesfully")
                $scope.zoneId = 0;
                $scope.zoneName = "";
                $scope.zoneCode="";
                $scope.zoneEditMode = false;
                $scope.isExisting = false;
                $scope.loadZoneData();
            }
           else  {
                logger.logWarning(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('zoneSvc', function ($http) {
    var compasZoneSvc = {};
    compasZoneSvc.GetZones = function () {
        return $http({
            url: '/compas/rest/region/gtZones/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasZoneSvc.UpdZone = function (zone) {
        return $http({
            url: '/compas/rest/region/updZone',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data:zone
        });
    };
    return compasZoneSvc;
}).factory('$zoneValid', function () {
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
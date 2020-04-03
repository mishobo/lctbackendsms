/**
 * area Angular Module
 */
'use strict';
angular.module('app.area', []).controller("areasCtrl", ["$scope", "$filter", "areaSvc","zoneSvc", "$areaValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, areaSvc,zoneSvc, $areaValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.header="";
    $scope.loadAreaData = function () {
        $scope.areas = [], $scope.searchKeywords = "", $scope.filteredAreas = [], $scope.row = "", $scope.areaEditMode = false;
        areaSvc.GetAreas().success(function (response) {
            return $scope.areas = response, $scope.searchKeywords = "", $scope.filteredAreas = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageAreas = $scope.filteredAreas.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredAreas = $filter("filter")($scope.areas, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredAreas = $filter("orderBy")($scope.areas, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageAreas = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadAreaData();
    $scope.loadActiveZoneData = function () {
        $scope.zones = [], $scope.searchKeywords = "", $scope.filteredZones = [], $scope.row = "", $scope.zoneEditMode = false;
        areaSvc.GetActiveZones().success(function (response) {
            return $scope.zones = response
            })
    }
    $scope.loadActiveZoneData()
    $scope.editArea = function (area) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify  County.");
            return false;
        }
        $scope.header="Edit County";
        $scope.areaEditMode = true;
        $scope.areaId = area.areaId;
        $scope.areaCode = area.areaCode;
        $scope.areaName = area.areaName;
        $scope.zoneSelect = area.zoneId;
        $scope.active = area.active;
        $scope.isExisting = true;
    };

    $scope.addArea = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new County.");
            return false;
        }
        $scope.header="Create County";
        $scope.areaEditMode = true;
        $scope.areaId = 0;
        $scope.areaCode="";
        $scope.areaName = "";
        $scope.zoneSelect="";
        $scope.active =false;
        $scope.isExisting = false;
    };

    $scope.calcelArea = function () {
        $scope.areaEditMode = false;
        $scope.areaId = 0;
        $scope.areaCode="";
        $scope.zoneSelect="";
        $scope.areaName = "";
        $scope.isExisting = false;
    }

    $scope.updArea = function () {
        var area = {};
        if (!$areaValid($scope.areaCode)) {
            logger.logWarning("Opss! You may have skipped specifying the County's Code. Please try again.")
            return false;
        }
        if (!$areaValid($scope.areaName)) {
            logger.logWarning("Opss! You may have skipped specifying the County's Name. Please try again.")
            return false;
        }
        if ($scope.areaName.length > 50) {
            logger.logWarning("Opss!County Name is reach to maximum length of 50 ")
            return false;
        }
        if (!$areaValid($scope.zoneSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Region. Please try again.")
            return false;
        }
        if (!$areaValid($scope.areaId))
            area.areaId = 0;
        else
            area.areaId = $scope.areaId;
        area.areaCode = $scope.areaCode;
        area.areaName = $scope.areaName;
        area.zoneId =  $scope.zoneSelect;
        area.active = $scope.active;
        area.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        console.log(area);
        areaSvc.UpdArea(area).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The County information was saved succesfully")
                $scope.areaId = 0;
                $scope.areaName = "";
                $scope.areaCode="";
                $scope.zoneSelect="";
                $scope.areaEditMode = false;
                $scope.isExisting = false;
                $scope.loadAreaData();
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

}]).factory('areaSvc', function ($http) {
    var compasAreaSvc = {};
    compasAreaSvc.GetAreas = function () {
        return $http({
            url: '/compas/rest/region/gtAreas/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };
    compasAreaSvc.GetActiveZones = function () {
        return $http({
            url: '/compas/rest/region/gtActiveRegions/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };
    compasAreaSvc.UpdArea = function (area) {
        return $http({
            url: '/compas/rest/region/updArea',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data:area
        });
    };
    return compasAreaSvc;
}).factory('$areaValid', function () {
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
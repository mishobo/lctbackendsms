/**
 * category Angular Module
 */
'use strict';
angular.module('app.category', []).controller("categoryCtrl", ["$scope", "$filter", "categorySvc", "$categoryValid", "$rootScope", "blockUI", "logger" , "$location", function ($scope, $filter, categorySvc, $categoryValid, $rootScope, blockUI, logger, $location) {
    var init;
    var dlg = null;
    $scope.header="";
    $scope.loadCategoryData = function () {
        $scope.categorys = [], $scope.searchKeywords = "", $scope.filteredCategories = [], $scope.row = "", $scope.categoryEditMode = false;
        categorySvc.GetCategories().success(function (response) {
            return $scope.categories = response, $scope.searchKeywords = "", $scope.filteredCategories = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageCategories = $scope.filteredCategories.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredCategories = $filter("filter")($scope.categories, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredCategories = $filter("orderBy")($scope.categories, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageCategories = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadCategoryData();

    $scope.editCategory = function (category) {
        if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to modify  category.");
            return false;
        }
        $scope.header="Edit Category";
        $scope.categoryEditMode = true;
        $scope.categoryId = category.categoryId;
        $scope.categoryName = category.categoryName;
        $scope.categoryCode = category.categoryCode;
        $scope.active = category.active;
        $scope.isDisabled=true;
    };

    $scope.addCategory = function () {
        if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
            logger.log("Oh snap! You are not allowed to create new category.");
            return false;
        }
        $scope.header="Create Category";
        $scope.categoryEditMode = true;
        $scope.categoryId = 0;
        $scope.categoryName = "";
        $scope.categoryCode = "";
        $scope.active =false;
        $scope.isDisabled=false;
    };

    $scope.calcelCategory = function () {
        $scope.categoryEditMode = false;
        $scope.categoryId = 0;
        $scope.categoryName = "";
        $scope.categoryCode = "";
        $scope.isDisabled=false;
    }

    $scope.updCategory = function () {
        var category = {};
        if (!$categoryValid($scope.categoryCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Category's Code. Please try again.")
            return false;
        }
        if (!$categoryValid($scope.categoryName)) {
            logger.logWarning("Opss! You may have skipped specifying the Category's Name. Please try again.")
            return false;
        }
        if ($scope.categoryName.length > 50) {
            logger.logWarning("Opss!category Name is reach to maximum length of 50 ")
            return false;
        }
        if (!$categoryValid($scope.categoryId))
            category.categoryId = 0;
        else
            category.categoryId = $scope.categoryId;
        category.categoryCode = $scope.categoryCode;
        category.categoryName = $scope.categoryName;
        category.active = $scope.active;
        category.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        console.log(category);
        categorySvc.UpdCategory(category).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Category information was saved succesfully")
                $scope.categoryId = 0;
                $scope.categoryName = "";
                $scope.categoryCode="";
                $scope.categoryEditMode = false;
                $scope.isDisabled=false;
                $scope.loadCategoryData();
            }
           
            else {
                logger.logWarning(response.respMessage)
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

}]).factory('categorySvc', function ($http) {
    var compasCategorySvc = {};
    compasCategorySvc.GetCategories = function () {
        return $http({
            url: '/compas/rest/category/gtCategories/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };

    compasCategorySvc.UpdCategory = function (category) {
        return $http({
            url: '/compas/rest/category/updCategory',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data:category
        });
    };
    return compasCategorySvc;
}).factory('$categoryValid', function () {
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
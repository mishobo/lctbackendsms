/**
 * Created by Eli on 1/11/2016.
 */


/**
 *
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.tms', []).controller("licenceCtrl", ["$scope", "$filter", "issueDeviceSvc","agentSvc",
    "deviceSvc","merchantSvc","userSvc","branchSvc","$issueDeviceValid", "$rootScope",
    "blockUI", "logger", "$location","tmsSvc", function ($scope, $filter,issueDeviceSvc,agentSvc,deviceSvc,
                                                         merchantSvc,userSvc,branchSvc, $issueDeviceValid,
                                                         $rootScope, blockUI, logger, $location,tmsSvc) {

    $scope.cancelLicenseGen= function () {
        $location.path('/');
    };

        /**
         * generate license
         * @param license
         */
    $scope.generateLicense = function (license) {
        blockUI.start()
        tmsSvc.generateLicense(license).success(function (response) {
            if (response.responseCode == 200) {
                logger.logSuccess(response.responseMessage)
                $location.path('/');

            } else {
                logger.logError(response.responseMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };

        /**
         *
         */
    $scope.uploadFirmware = function () {
        var file = $scope.myFile;
        blockUI.start()
        tmsSvc.uploadApplicationFile(file,version).success(function (response) {
            if (response.responseCode == 200) {
                logger.logSuccess("Firmware Uploaded successfully!!")
                $location.path('/');

            } else {
                logger.logError(response.responseMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    }


} ]).controller("tmsDashboardCtrl", ["$scope", "$filter", "issueDeviceSvc","agentSvc","deviceSvc",
    "merchantSvc","userSvc","branchSvc","$issueDeviceValid", "$rootScope", "blockUI", "logger",
    "$location","tmsSvc","$timeout", function ($scope, $filter,issueDeviceSvc,agentSvc,deviceSvc,merchantSvc,
                           userSvc,branchSvc, $issueDeviceValid, $rootScope, blockUI, logger, $location,tmsSvc,$timeout) {

        $scope.device_stats =[];
        $scope.dashboard_obj = {};


        $scope.fetchDeviceStats = function(){
            blockUI.start();

            tmsSvc.fetchDeviceStats().success(function (response) {
                console.log(response);
                $scope.device_stats = response;

            }).error(function (error) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
            });
            blockUI.stop();
        }

        /**
         *
         */
        $scope.fetchDashboardStats = function(){
            tmsSvc.fetchDashboardStats().success(function (response) {
                $scope.dashboard_obj = response;
                console.log($scope.dashboard_obj);
            }).error(function (error) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
            });
        }

        $scope.fetchDeviceStats();

        $scope.fetchDashboardStats();


        $scope.onTimeout = function(){
            $scope.$apply($scope.fetchDeviceStats());
            timer = $timeout($scope.onTimeout,180000);
        }

        $scope.onTimeout1 = function(){
            $scope.$apply($scope.fetchDashboardStats());
            timer1 = $timeout($scope.onTimeout1,180000);
        }


        var timer = $timeout($scope.onTimeout,180000);
        var timer1 = $timeout($scope.onTimeout1,180000);



}]).factory('tmsSvc', function ($http) {
    var licenseSvc = {};

    licenseSvc.generateLicense = function (license) {
        return $http({
            url: '/compas/rest/api/generate_license/',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: license
        });
    };

    licenseSvc.uploadApplicationFile = function (file,version) {
        var fd= new FormData();
        fd.append('file',file);
        fd.append('version',version);
        return $http({
            url:'/compas/rest/api/uploadapplication',
            method:'POST',
            transformRequest: angular.identity,
            headers: {'Content-Type':undefined},
            data: fd
        });
    };

    licenseSvc.fetchDashboardStats = function () {
        return $http({
            url: '/compas/rest/api/get_dash_stats',
            method: 'GET',
            headers: {'Content-Type':'application/json'}
        });
    };


    licenseSvc.fetchDeviceStats = function () {
        return $http({
            url : '/compas/rest/api/get_device_stats',
            method :'GET',
            headers:{'Content-Type':'application/json'}
        });
    };

    return licenseSvc;
}).directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

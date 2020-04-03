/**
 * Lock Screen Angular Module
 */
'use strict';
angular.module('app.login.lock', []).controller("lockCtrl", ["$scope", "$filter", "$chkNotNullUndefinedBlank", "$rootScope", "blockUI", "logger", "$location", "localStorageService", "loginSvc", function ($scope, $filter, $chkNotNullUndefinedBlank, $rootScope, blockUI, logger, $location, localStorageService, loginSvc) {
	$scope.login= [];
	$scope.SetLockData = function()
	{
		$scope.login= [];
		$scope.login.userId = $rootScope.UsrRghts.sessionId;
		$scope.login.userName = $rootScope.UsrRghts.sessionName;
		$scope.login.userFullName = $rootScope.UsrRghts.sessionFullName;
		$scope.login.userClassId = $rootScope.UsrRghts.userClassId;
		$scope.login.userGroupId = $rootScope.UsrRghts.userGroupId;
	};
	$scope.SetLockData();
    $scope.UnLock = function (UserName, Password) {
        if (!$chkNotNullUndefinedBlank(UserName)) {
            logger.logWarning("Opss! You may have skipped entering your user name. Please try again.")
            return false;
        }
        if (!$chkNotNullUndefinedBlank(Password)) {
            logger.logWarning("Opss! You may have skipped entering your password. Please try again.")
            return false;
        }
        blockUI.start();
        localStorageService.clearAll();
        $scope.userName = UserName;
        $scope.password = Password;
        loginSvc.AuthManual($scope).success(function (response) {
            if (response.respCode == 201) {
                logger.logWarning("Opss! You specified invalid login credentials, Please try again.")
            } else {
                loginSvc.GetRights(response.userId).success(function (rightLst) {
                    localStorageService.set('rxr', rightLst);
                    $rootScope.UsrRghts = rightLst;// .RightList;
                    $location.path('/dashboard');
                }).error(function (data, status, headers, config) {
                    logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                });
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    }
}]);
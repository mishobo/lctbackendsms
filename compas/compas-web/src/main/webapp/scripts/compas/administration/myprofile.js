/**
 * User Angular Module
 */
'use strict';
angular.module('app.profile', []).controller("profileCtrl", ["$scope", "$filter", "profileSvc", "userSvc", "loginSvc", "localStorageService", "$profileValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, profileSvc, userSvc, loginSvc, localStorageService, $profileValid, $rootScope, blockUI, logger, $location) {
    $scope.groups = [];
    $scope.questions = [];
    $scope.loadGroupList = function () {
        $scope.groups = [];
        $scope.classSelect = $rootScope.UsrRghts.userClassId;
        $scope.linkId = $rootScope.UsrRghts.linkId;
        userSvc.GetGroups($scope).success(function (response) {
            blockUI.stop();
            $scope.groups = response;
        }).error(function (data, status, headers, config) {
            blockUI.stop();
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
        });
    };
    $scope.loadQuestionsList = function () {
        $scope.questions = [];
        userSvc.GetQuestions().success(function (response) {
            blockUI.stop();
            $scope.questions = response;
        }).error(function (data, status, headers, config) {
            blockUI.stop();
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
        });
    };
    $scope.loadProfileDetail = function () {
        $scope.profileSave = [];
        profileSvc.GetUserById($rootScope.UsrRghts.sessionId).success(function (response) {
            blockUI.stop();
            $scope.profileSave.userId = response.userId;
            $scope.profileSave.userName = response.userName;
            $scope.profileSave.userFullName = response.userFullName;
            $scope.profileSave.userPwd = response.userPwd;
            $scope.profileSave.groupSelect = response.userGroupId;
            $scope.profileSave.groupName = response.groupName;
            $scope.profileSave.userEmail = response.userEmail;
            $scope.profileSave.userPhone = response.userPhone;
           // $scope.profileSave.questionSelect= response.userSecretQuestionId;
          // $scope.profileSave.userSecretAns = response.userSecretAns;
          // $scope.profileSave.linkId = response.userLinkedID;
            $scope.profileSave.active = response.active;
            $scope.idDisabled = true;
        }).error(function (data, status, headers, config) {
            blockUI.stop();
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
        });
    };
    //$scope.loadQuestionsList();
    //$scope.loadGroupList();
    $scope.loadProfileDetail();
    $scope.cancelProfile = function () {
        $scope.isDisabled = false;
        $scope.active = false;
        $location.path('/dashboard');
    };

    $scope.updUser = function () {
        var profile = [];
        if (!$profileValid($scope.profileSave.userFullName)) {
            logger.logWarning("Opss! You may have skipped specifying the User's Full Name. Please try again.");
            return false;
        }
        if (!$profileValid($scope.profileSave.groupSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the User's Group. Please try again.");
            return false;
        }
        if (!$profileValid($scope.profileSave.userEmail)) {
            logger.logWarning("Opss! You may have skipped specifying the User's Email. Please try again.");
            return false;
        }
        if (!$profileValid($scope.profileSave.userPhone)) {
            logger.logWarning("Opss! You may have skipped specifying the User's Phone. Please try again.");
            return false;
        }
        if (!$profileValid($scope.profileSave.questionSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Secret Question. Please try again.");
            return false;
        }
        if (!$profileValid($scope.profileSave.userSecretAns)) {
            logger.logWarning("Opss! You may have skipped specifying the Secret Answer. Please try again.");
            return false;
        }
        
        if (!$profileValid($scope.profileSave.userId))
            user.userId = 0;
        else
            profile.userId = $scope.profileSave.userId;
        profile.userName = $scope.profileSave.userName;
        profile.userFullName = $scope.profileSave.userFullName;
        profile.userPwd = $scope.profileSave.userPwd;
        profile.userGroupId = $scope.profileSave.groupSelect;
        profile.userEmail = $scope.profileSave.userEmail;
        profile.userPhone = $scope.profileSave.userPhone;
        profile.questionSelect = $scope.profileSave.questionSelect;
        profile.userSecretAns = $scope.profileSave.userSecretAns;
        profile.userLinkedID = $scope.profileSave.linkId;
        profile.active = $scope.profileSave.active;
        profile.createdBy = $rootScope.UsrRghts.sessionId;
        //console.log(profile);
        blockUI.start();
        userSvc.UpdUser(profile).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The profile information was saved succesfully");
                if (parseFloat($scope.profileSave.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                    localStorageService.clearAll();
                    loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst) {
                        localStorageService.set('rxr', rightLst);
                        $rootScope.UsrRghts = rightLst;
                        $location.path('/dashboard');
                    }).error(function (data, status, headers, config) {
                        logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
                    });
                }
                $scope.userId = 0;
                $scope.userName = "";
                $scope.userFullName = "";
                $scope.userPwd = "";
                $scope.groupSelect = "";
                $scope.userEmail = "";
                $scope.userPhone = "";
                $scope.questionSelect = "";
                $scope.userSecretAns = "";
                $scope.active = false;
            }
            else {
                logger.logWarning("Opss! Something went wrong while updating the profile. Please try again after sometime");
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.");
            blockUI.stop();
        });
    };
} ]).factory('profileSvc', function ($http) {
    var compasProfileSvc = {};
    compasProfileSvc.GetUserById = function (userId) {
        return $http({
            url: '/compas/rest/user/gtUserById/' + userId,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
    };
    return compasProfileSvc;
}).factory('$profileValid', function () {
    return function (valData) {
        if (angular.isUndefined(valData))
            return false;
        else {
            if (valData == null)
                return false;
            else {
                return !(valData.toString().trim() == "");
            }
        }
    };
}).filter('getGroupName', function () {
    return function (input, id) {
        var i = 0, len = input.length;
        for (; i < len; i++) {
        	//console.log("Checking: " + input[i].groupId + " against: " + id);
            if (input[i].groupId == id) {
                return input[i];
            }
        }
        return null;
    }
});
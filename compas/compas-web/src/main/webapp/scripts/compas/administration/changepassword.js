/**
 * User Angular Module
 */
'use strict';
angular
        .module('app.changepassword', [])
        .controller(
                "changePasswordCtrl",
                [
                        "$scope",
                        "$filter",
                        "profileSvc",
                        "userSvc",
                        "loginSvc",
                        "changePasswordSvc",
                        "localStorageService",
                        "$profileValid",
                        "$userValid",
                        "$rootScope",
                        "blockUI",
                        "logger",
                        "$location",
                        function($scope, $filter, profileSvc, userSvc,
                                loginSvc, changePasswordSvc,
                                localStorageService, $profileValid, $userValid,
                                $rootScope, blockUI, logger, $location) {
 
                            // fetch username by ID
                            $scope.loadProfileDetail = function() {
                                $scope.profileSave = [];
                                profileSvc
                                        .GetUserById(
                                                $rootScope.UsrRghts.sessionId)
                                        .success(
                                                function(response) {
                                                    blockUI.stop();
                                                    $scope.profileSave.userId = response.userId;
                                                    $scope.profileSave.userName = response.userName;
                                                    $scope.profileSave.userPwd = response.userPwd;
                                                    $scope.profileSave.active = response.active;
                                                    $scope.idDisabled = true;
                                                })
                                        .error(
                                                function(data, status, headers,
                                                        config) {
                                                    blockUI.stop();
                                                    logger
                                                            .logError("Oh snap! There is a problem with the server, please contact the administrator.");
                                                });
                            };
                            $scope.loadProfileDetail();
                            $scope.cancelProfile = function() {
                                $scope.isDisabled = false;
                                $scope.active = false;
                                $location.path('/dashboard');
                            };
                             
                            $scope.cancelUser = function () {
                                $location.path('/dashboard');
                            }
                            $scope.passwordChange = function()
 
                            {
                                var local = localStorageService.get('ucred');
                                $scope.UserName = local.username;
                                console.log('user name----', $scope.UserName);
                                $scope.userEnrollment = false;
                                $scope.userchangePassword = true;
                                // $scope.userId = 0;
                                $scope.editmo = 0;
                                $scope.userPwd = "";
                                $scope.newPassword = "";
                                $scope.confirmPwd = ""
 
                            };
 
                            // change User password
                            $scope.userChangePassword = function() {
                                var user = {};
                                if (!$userValid($scope.newPassword)
                                        && $scope.editmo == 1) {
                                    logger.logWarning("Opss! You may have skipped specifying the User's new Password. Please try again.")
                                    return false;
                                }
                                if (!$userValid($scope.confirmPwd)
                                        && $scope.editmo == 1) {
                                    logger.logWarning("Opss! You may have skipped specifying the User's confirm userPwd. Please try again.")
                                    return false;
                                }
                                if ($scope.newPassword != $scope.confirmPwd
                                        && $scope.editmo == 1) {
                                    logger.logWarning("Opss! Password and Confirm password does not match. Please try again.")
                                    return false;
                                }
 
                                // if (!$userValid($scope.userId))
                                // user.userId = 0;
                                // else
 
                                user.userName = $scope.profileSave.userName;
                                console.log(" username ", user.userName)
                                // user.userPwd=$scope.userPwd;
                                user.newPassword = $scope.newPassword;
                                blockUI.start()
                                changePasswordSvc
                                        .userChangePassword(user)
                                        .success(
                                                function(response) {
                                                    if (response.respCode == 200) {
                                                        console.log(user.newPassword);
                                                        logger.logSuccess("Great! The user information was saved succesfully")
                                                        if (parseFloat($scope.userId) === parseFloat($rootScope.UsrRghts.sessionId)) {
                                                            localStorageService
                                                                    .clearAll();
                                                            loginSvc
                                                                    .GetRights(
                                                                            $rootScope.UsrRghts.sessionId)
                                                                    .success(
                                                                            function(
                                                                                    rightLst) {
                                                                                localStorageService
                                                                                        .set(
                                                                                                'rxr',
                                                                                                rightLst);
                                                                                $rootScope.UsrRghts = rightLst;
                                                                            })
                                                                    .error(
                                                                            function(
                                                                                    data,
                                                                                    status,
                                                                                    headers,
                                                                                    config) {
                                                                                logger
                                                                                        .logError("Oh snap! There is a problem with the server, please contact the administrator.")
                                                                            });
                                                        }
                                                        $scope.UserName = "";
                                                        $scope.newPassword = "";
                                                        $scope.confirmPwd = "";
                                                        $scope.userEditMode = true;
 
                                                        $scope.userchangePassword = true;
                                                        $location
                                                                .path('/login');
                                                        $scope.loadUserData();
 
                                                        logger
                                                                .logSuccess(response.respMessage)
                                                    } else if (response.respCode == 409) {
 
                                                        logger
                                                                .logWarning("Opss! The password is weak, Shoud be at least eight characters, one or more digits,combination of upper- and lower-case characters and one or more special character ")
                                                    } else {
                                                        logger
                                                                .logWarning(response.respMessage)
                                                    }
                                                    blockUI.stop();
                                                })
                                        .error(
                                                function(data, status, headers,
                                                        config) {
                                                    logger
                                                            .logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                                                    blockUI.stop();
                                                });
 
                            };
                        } ]).factory('changePasswordSvc', function($http) {
            var compasProfileSvc = {};
            compasProfileSvc.GetUserById = function(userId) {
                return $http({
                    url : '/compas/rest/user/gtUserById/' + userId,
                    method : 'GET',
                    headers : {
                        'Content-Type' : 'application/json'
                    }
                });
            };
 
            compasProfileSvc.userChangePassword = function(user) {
                return $http({
                    url : '/compas/rest/user/changePassword',
                    method : 'POST',
                    headers : {
                        'Content-Type' : 'application/json'
                    },
                    data : user
                });
            };
            return compasProfileSvc;
 
        }).factory('$profileValid', function() {
            return function(valData) {
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
        }).filter('getGroupName', function() {
            return function(input, id) {
                var i = 0, len = input.length;
                for (; i < len; i++) {
                    // console.log("Checking: " + input[i].groupId + " against:
                    // " + id);
                    if (input[i].groupId == id) {
                        return input[i];
                    }
                }
                return null;
            }
        });
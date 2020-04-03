/**
 * Login Angular Module
 */
var afisIp = 'localhost';
var afisPort = '8192'; 
/*var afisIp = '192.168.13.14';
var afisPort = '8123';*/
'use strict';
angular.module('app.login', []).controller('loginCtrl', function($scope, loginSvc, $location, $rootScope, blockUI, logger, localStorageService, $loginValidation) {
	
//	$scope.$on("$viewcontentloaded", function(){
//	  //  console.log('Login Init Called');
//	 	$("#bioId")[0].OnClear();
//    });
//	
//	$scope.$on("$destroy", function(){
//	    //console.log('Login Destroy Called');
//	 	$("#bioId")[0].OnClear();
//    });
//	
//	$scope.$watch('tgBio', function() {
//		if($scope.tgBio){
//		 //console.log('BIO Called');
//		 $("#bioId")[0].OnClear();
//	    }
//	});
	
	$scope.UserName = "";
	$scope.Password = "";
	$scope.UserId = 0;
	$rootScope.UsrRghts = [];
//	$scope.bioLogin = function(UserName) {
//		logger.logWarning("Opss! This functionality is not available as of now. It will be available in the Next Release")
//	};

	$scope.Login = function(UserName, Password) {
		if (!$loginValidation(UserName)) {
			logger.logWarning("Opss! You may have skipped entering your user name. Please try again.")
			return false;
		}
		if (!$loginValidation(Password)) {
			logger.logWarning("Opss! You may have skipped entering your password. Please try again.")
			return false;
		}
		blockUI.start();
		localStorageService.clearAll();
		var loginUser = new Object();
		loginUser.userName = UserName;
		loginUser.userPwd = Password;
		$scope.userName = UserName;
		$scope.password = Password;
		loginSvc.AuthManual(loginUser).success(function(response) {
			if (response.respCode == 201) {
				logger.logWarning("Opss! You specified invalid login credentials, Please try again.")
			} else {
				loginSvc.GetRights(response).success(function(rightLst) {
					console.log(response);
					console.log("First Time Login status: " +response.firstTimeLogin);
					console.log("User Group Id: " +response.UserGroupId);
					console.log('rightLst: ', rightLst);
					localStorageService.set('rxr', rightLst);
					localStorageService.set('userId', response.userId);
					$rootScope.UsrRghts = rightLst;
					// .RightList;
					//$location.path('/dashboard');
					if (response.firstTimeLogin == true){
						console.log("Time to change password");
						logger.logWarning("You are logging in for the first time, you will be now redirected to change your password.")
						$location.path('/user/changepassword');
					}
					else {
						$location.path('/dashboard');
					}
				}).error(function(data, status, headers, config) {
					logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
				});
			}
			blockUI.stop();
		}).error(function(data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};
	
	$scope.toResetPassword = function()
	{
		console.log('we here...');
		$location.path('/reset');
	};

//	$scope.captureImage = function(data) {
//		var obj = $("#bioId")[0].OnFingerScan(data);
//		var res = jQuery.parseJSON(obj);
//		return res;
//	};
	
})
	//.directive('captureb', function(logger, blockUI, loginSvc, localStorageService, $rootScope, $location) {
//	return {
//		link : function($scope, element, attrs) {
//			element.bind('click', function() {
//				blockUI.start();
//				if ($scope.userBioID > 0) {
//					var fingerPos = $scope.userBioID + '|' + afisIp + '|' + afisPort;
//					//console.log(fingerPos);
//					var captureResponse = $.parseJSON($("#bioId")[0].OnValidate(fingerPos));
//					//console.log(captureResponse);
//					if (captureResponse.code != '200') {
//						logger.logError(captureResponse.detail);
//					} else {
//						var src = "data:image/png;base64,";
//						src = src + captureResponse.rawImg64;
//						$(this).removeAttr('src');
//						$(this).attr('src', src).attr('style', 'border-radius:20px;width:120px;height:120px;');
//						if (captureResponse.quality[0] >= 80) {
//							logger.log(' Validated Succesfully!!');
//							loginSvc.GetRights($scope.userId).success(function(rightLst) {
//								localStorageService.set('rxr', rightLst);
//								$rootScope.UsrRghts = rightLst;
//								// .RightList;
//								$location.path('/dashboard');
//							}).error(function(data, status, headers, config) {
//								logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
//							});
//
//						} else {
//							logger.logError('Unable to Capture : Quality extracted:' + captureResponse.quality[0]);
//						}
//					}
//
//				} else {
//					logger.logError('User Not Enrolled for BIO');
//				}
//				blockUI.stop();
//			})
//		},
//	}
//}).directive('ngBlur', ['$parse', 'loginSvc', 'blockUI', 'localStorageService', 'logger',
//function($parse, loginSvc, blockUI, localStorageService, logger) {
//	return function($scope, element, attr) {
//		var fn = $parse(attr['ngBlur']);
//		element.bind('blur', function(event) {
//			blockUI.start();
//			localStorageService.clearAll();
//			var loginUser = new Object();
//			loginUser.userName = $scope.UserName;
//			//$scope.userName = UserName;
//			loginSvc.AuthBio(loginUser).success(function(response) {
//				//console.log(response);
//				$scope.userBioID = response.userBioID;
//				$scope.userId = response.userId;
//				//$scope.bioApplet = $("#bioId");
//				if ($scope.tgBio) {
//					if (response.respCode == 201) {
//						logger.logWarning("Opss! You specified invalid login credentials, Please try again.")
//					} else {
//						var d = $.parseJSON($("#bioId")[0].OnDeviceInit());
//						if (d.code != '200') {
//							logger.logError('No Device detected!!');
//						} else {							
//							if ($scope.userBioID === 0) {
//								$scope.tgBio = false;
//								logger.logWarning('Opss! User Not Enrolled, Please use Manual Login');
//							} else {
//								logger.logSuccess('Please Scan Finger Print and Press Image');
//							}
//						}
//						/*loginSvc.GetRights(response.userId).success(function(rightLst) {
//						 localStorageService.set('rxr', rightLst);
//						 $rootScope.UsrRghts = rightLst;
//						 // .RightList;
//						 $location.path('/dashboard');
//						 }).error(function(data, status, headers, config) {
//						 logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
//						 });*/
//					}
//				}
//				blockUI.stop();
//			});
//		});
//	}
//}])
	.directive('showErrors', function($timeout) {
	return {
		restrict : 'A',
		require : '^form',
		link : function(scope, el, attrs, formCtrl) {
			// find the text box element, which has the
			// 'name' attribute
			// directive not assiging correct class, need to
			// check later
			var inputEl = el[0].querySelector("[name]");
			// convert the native text box element to an
			// angular element
			var inputNgEl = angular.element(inputEl);
			// get the name on the text box
			var inputName = inputNgEl.attr('name');

			// only apply the has-error class after the
			// user leaves the text box
			inputNgEl.bind('blur', function() {
				el.toggleClass('input-danger', formCtrl[inputName].$invalid);
			});

			scope.$on('show-errors-check-validity', function() {
				el.toggleClass('input-danger', formCtrl[inputName].$invalid);
			});

			scope.$on('show-errors-reset', function() {
				$timeout(function() {
					el.removeClass('input-danger');
				}, 0, false);
			});
		}
	}
}).factory('$loginValidation', function() {
	return function(valData) {
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
}).factory('loginSvc', function($http) {
	var compasLoginSvc = {};

	compasLoginSvc.AuthManual = function(loginUser) {
		//console.log(loginUser);
		return $http({
			url : '/compas/rest/login/manualAuth',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : loginUser

		});
	};
	compasLoginSvc.AuthBio = function(bioId) {
		return $http({
			url : '/compas/rest/login/bioAuth/' + bioId,
			method : 'GET',
			headers : {
				'Content-Type' : 'application/json'
			}
		});
	};
	compasLoginSvc.AuthDeviceUser = function(deviceUser) {
		return $http({
			url : '/compas/rest/login/deviceAuth/',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
					
			},
			data : deviceUser
		});
	};
	compasLoginSvc.GetRights = function(user) {
		return $http({
			url : '/compas/rest/login/getUserRights',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : user
		});
	};

	compasLoginSvc.AuthBio = function(loginUser) {
		//console.log(loginUser);
		return $http({
			url : '/compas/rest/login/bioRt',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : loginUser

		});
	};

	return compasLoginSvc;
});

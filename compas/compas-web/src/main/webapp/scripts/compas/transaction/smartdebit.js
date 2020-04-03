//const $injector = angular.injector(['ng']);
//const $q = $injector.get('$q');
//console.log('$q: ', $q);
'use strict';
angular.module('app.smartdebit', []).controller('smartDebitCtrl', //['$scope', '$filter', 'uploadclaimSvc','organizationSvc', '$rootScope', 
	//'blockUI', 'logger', '$location','$http',
	//function ($scope, $filter, uploadclaimSvc,organizationSvc, $rootScope, blockUI, logger, $location,$http) 
		["$scope", "$filter", "uploadSmartclaimAppsvc",  "$rootScope", "blockUI", "logger", "$location", "localStorageService", function ($scope, $filter, uploadSmartclaimAppsvc, $rootScope, blockUI, logger, $location, localStorageService){
	console.log('upload controller okay...');
	//console.log('dealing with user ', localStorageService.get('user'));
	$scope.uploadClaim = function()
	{
		console.log('uploading: ', $scope.myFile);
		uploadSmartclaimAppsvc.saveFile($scope.myFile, $scope.orgId).success(function (response) {
		      if (response.respCode == 200) {
	                logger.logSuccess("Great! The File Information Uploaded successfully")

	              $location.path('/dashboard');

	            } else {
	                logger.logError(response.respMessage);
	            }
		}).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")

		  });
    };
    
} ]).factory('uploadSmartclaimAppsvc', function ($http) {
     var uploadSvc={};    
   uploadSvc.saveFile = function (file,orgId) {
			console.log("uploading SMART DEBIT file");
		    var fd = new FormData();
		    fd.append('file', file);
		    fd.append('orgId',orgId);
		    return $http({
		        url: '/compas/rest/transaction/uploadSmartDebit',
		        method: 'POST',
		        transformRequest: angular.identity,
		        headers: { 'Content-Type': undefined },
		        data: fd
		    });
		};
		 return uploadSvc;
}).directive('fileModel', [ '$parse', function($parse) {
    return {
        restrict : 'A',
        link : function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function() {
                scope.$apply(function() {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
} ]);

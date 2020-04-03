//let uploadserviceApp = 
'use strict';
angular.module('app.uploadservice', [])//;
//uploadserviceApp
.controller('uploadserviceCtrl', ["$scope", "$filter", "uploadserviceSvc",  "$rootScope", "blockUI", "logger", "$location","localStorageService","$modal", function ($scope, $filter, uploadserviceSvc, $rootScope, blockUI, logger, $location, localStorageService, $modal)
		{
	console.log('upload controller okay...');
	console.log('logger: ',logger);
	console.log('$modal: ',$modal);
	$scope.uploadService = function()
	{
		console.log('uploading: ', $scope.myFile);
		 blockUI.start()
		uploadserviceSvc.saveFile($scope.myFile, $scope.orgId).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The File Information Uploaded successfully")

                $location.path('/dashboard');

            } else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };
}])//;

//uploadserviceApp
.service('uploadserviceSvc', function($http){
	this.saveFile = function (file,orgId) {
			console.log("uploading file...");
		    var fd = new FormData();
		    fd.append('file', file);
		    fd.append('orgId',orgId);
		    return $http({
		        url: '/compas/rest/service/uploadservice',
		        method: 'POST',
		        transformRequest: angular.identity,
		        headers: { 'Content-Type': undefined },
		        data: fd
		    });
		};
})//;

//uploadserviceApp
.directive('fileModel', [ '$parse', function($parse) {
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

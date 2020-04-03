const uploadmemberApp = angular.module('app.uploadmember', []);
uploadmemberApp.controller('uploadmemberCtrl', ["$scope", "$filter", "uploadmemberSvc", "localStorageService",  "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, uploadmemberSvc, $rootScope, blockUI, logger, $location, localStorageService)
		{
	console.log('upload controller okay...');
	$scope.uploadMember = function()
	{
		console.log('uploading: ', $scope.myFile);
		uploadmemberSvc.saveFile($scope.myFile, $scope.orgId);
	}
}]);

uploadmemberApp.service('uploadmemberSvc', function($http){
	this.saveFile = function (file,orgId) {
			console.log("uploading file...");
		    var fd = new FormData();
		    fd.append('file', file);
		    fd.append('orgId',orgId);
		    return $http({
		        url: '/compas/rest/member/uploadmember',
		        method: 'POST',
		        transformRequest: angular.identity,
		        headers: { 'Content-Type': undefined },
		        data: fd
		    });
		};
});

uploadmemberApp.directive('fileModel', [ '$parse', function($parse) {
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

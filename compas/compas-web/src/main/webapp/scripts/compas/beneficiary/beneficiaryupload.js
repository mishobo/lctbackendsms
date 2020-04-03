
/**
 * Beneficiary Import Angular Module
 */
'use strict';
angular.module('app.beneficiaryupload', []).controller("fileuploadCtrl", ["$scope", "$filter", "fileuploadSvc","organizationSvc", "productSvc","memberSvc","loginSvc", "localStorageService",  "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, fileuploadSvc,organizationSvc,productSvc, memberSvc, loginSvc, localStorageService, $rootScope, blockUI, logger, $location) {
    //var init;
	$scope.mem=[];
    $scope.showUpload=false;
    $scope.showProducts=false;
    $scope.orgSelect = 0;
    var tmrAuthData;
    var init;

    $scope.cancelUpload=function(){
			$scope.orgSelect=0;
			$scope.productSelect = 0;
			$scope.showProducts=false;
			$scope.showUpload=false;
			$scope.showProgrammes=false;
			$scope.myFile=null;
	}
	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			$scope.showProducts=true;
			$scope.showUpload=false;
			$scope.showProgrammes=false;
			$scope.loadProductData(orgId);
		}
	}
	$scope.onProductChange =function(productId){
			if(productId>0){
				$scope.showUpload=true;
				$scope.showProgrammes=true;
				$scope.LoadCategoriesByScheme(productId)
			}
		}
	$scope.loadOrganizationData = function () {
		$scope.orgs = [];
		organizationSvc.GetOrganizations().success(function (response) {
			return $scope.orgs = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	
	$scope.selectProgramme = function(index){
		for(var i=0;i<$scope.categories.length;i++){
			$scope.categories[i].isActive=false
			}
		$scope.categories[index].isActive = true;
	}
	$scope.loadOrganizationData();
    $scope.loadProductData = function (orgId) {
		$scope.products = []

		memberSvc.GetScheme(orgId).success(function (response) {
			return $scope.products = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.LoadCategoriesByScheme = function (schemeId) {
		$scope.categories=[];
		memberSvc.GetCategoryByScheme(schemeId).success(function (response) {
			$scope.categories = response;
			console.log($scope.categories)
			//console.log( $scope.programmes)
		});
	}
    $scope.uploadBeneficiaryDetails = function () {
        //logger.logSuccess("Uploading file in progress");
        var file = $scope.myFile;
        var programmeId = null;
        for(var i=0;i<$scope.categories.length;i++){
			if($scope.categories[i].isActive==true){
				programmeId = $scope.categories[i].programmeId;
			}
		}
    	if (programmeId == null) {
			logger.logWarning("Opss! You may have skipped specifying the cover category. Please try again.")
			return false;
		}
    	else{
        blockUI.start()
        fileuploadSvc.uploadBeneficiary(file,$scope.orgSelect, $scope.productSelect ,programmeId, $rootScope.UsrRghts.sessionId).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! "+response.respMessage)
                $scope.showProducts=true;
    			$scope.showUpload=false;
    			$scope.showProgrammes=false;
    			$scope.myFile = null;
    			$scope.productSelect = 0;
    			$scope.programmeSelect = 0;
            } else if(response.respCode == 205){
            	logger.logWarning(response.respMessage);
            }
            	else {
                logger.logError(response.respMessage);
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            blockUI.stop();
        });
    };
    };

} ]).factory('fileuploadSvc', function ($http) {
    var uploadSvc = {};

    uploadSvc.uploadBeneficiary = function (file,orgId,productId, programmeId, createdBy) {
        var fd = new FormData();
        fd.append('file', file);
        fd.append('orgId',orgId);
        fd.append('productId',productId);
        fd.append('programmeId',programmeId);
        fd.append('createdBy',createdBy);   
        return $http({
            url: '/compas/rest/upload/uploadbeneficiary',
            method: 'POST',
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined },
            data: fd
        });
    };


 

    return uploadSvc;
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
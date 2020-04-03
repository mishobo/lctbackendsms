const resetApp = angular.module('app.reset', []);
resetApp.controller('resetCtrl', function($scope, $location, resetSvc, blockUI, logger, $loginValidation) {
	$scope.UserName = "";
	$scope.resetUser = {};
	$scope.Reset = function() {
		if (!$loginValidation($scope.UserName)) {
			logger.logWarning("Opss! You may have skipped entering your username or Email. Please try again.")
			return false;
		}
		console.log('reset password for username/ email ', $scope.UserName);
		blockUI.start();
		$scope.resetUser.userName = $scope.UserName;
		resetSvc.Reset($scope.resetUser).success(function(response) {
			console.log('server response: ',response);
			if (response.respCode == 201) 
			{
				logger.logWarning("Failed: Username/ email does not exist, Please try again.");
			} 
			else if (response.respCode == 400) 
			{
				logger.logWarning("Failed: Could not reset password for provide username/ email.");
			} 
			else if(response.respCode == 200)
			{	
				console.log(response);
				logger.logSuccess("Success: Password reset details successfully sent to "+response.email+".");
				$location.path('/login');
			}
			blockUI.stop();
		}).error(function(data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.");
			blockUI.stop();
		});
	};
});

resetApp.service('resetSvc', function($http) {
	this.Reset = function(resetUser)
	{
		console.log('resetUser: ', resetUser);
		return $http({
			url : '/compas/rest/reset',
			method : 'POST',
			headers : {
				'Content-Type' : 'application/json'
			},
			data : resetUser
		});
	};
})
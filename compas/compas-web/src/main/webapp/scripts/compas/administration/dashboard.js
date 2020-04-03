/**
 * Member Angular Module
 */
'use strict';
angular.module('app.dashboard', []).controller("dashBoardCtrl", ["$scope", "$filter", "$rootScope", "dashBoardSvc","blockUI", "logger" ,"$location", function ($scope, $filter, $rootScope,dashBoardSvc, blockUI, logger, $location) {
	var init;
	var percentage=0;
	$scope.hide=false;
	$scope.tempCountList = [];
	$scope.donutChart2=[];
	$scope.easypiechart3=[]
	$scope.countList=[];
	$scope.agentTxnList=[];
	$scope.userCount=0;
	$scope.memberCount=0;
	$scope.proiderCount=0;
	$scope.brokerCount=0;
	$scope.tempLineChartDetail=[];
	$scope.lineChartDetail=[];
	$scope.dashBoardViewMode=true;
	$scope.totalAmount=0.00;
	$scope.netAmount=0.00;
	$scope.voideAmount=0.00;
	$scope.tempAmountList =[];
	$scope.pieChart=[];
	$scope.flowChartList=[]
	$scope.totalCollection=0.00
	$scope.sbp=0.00
	$scope.landrates=0.00
	$scope.pos=0.00
	$scope.cmPercentage=0.00; 
	$scope.vaPercentage= 0.00; 
	$scope.caPercentage=0.00; 
	$scope.data;
	$scope.loadCountDetail=function(){
		blockUI.start()
		dashBoardSvc.GetCountDetail().success(function (response) {
			$scope.data = response;
			blockUI.stop()
		}).error(function (data, status, headers, config) {
			blockUI.stop()
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadCountDetail();


}]).factory('dashBoardSvc', function ($http) {

	var compasDashBoardSvc = {};
	compasDashBoardSvc.GetFlowChartDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtFlowChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetCountDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtDashboardCount',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};

	compasDashBoardSvc.GetTransChartDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtTransChartDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};

	compasDashBoardSvc.GetAmountDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtAmountDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetAgentDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtAgentDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.GetCollectionDetail = function () {
		return $http({
			url: '/compas/rest/dashBoard/gtCollectionDetail',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasDashBoardSvc.isUserAKUH = function (currUserId) {
		return $http({
			url: '/compas/rest/dashBoard/isuserakuh/'+currUserId,
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	return compasDashBoardSvc;
})
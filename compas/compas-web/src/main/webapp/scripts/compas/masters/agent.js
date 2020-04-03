/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.agent', []).controller("agentsCtrl", ["$scope", "$filter", "agentSvc","userSvc","merchantSvc","branchSvc","programmeSvc","$agentValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,agentSvc,userSvc,merchantSvc,branchSvc,programmeSvc,$agentValid, $rootScope, blockUI, logger, $location) {
	
    var init;
    $scope.header="";
	$scope.agents = [];
	$scope.allItemsSelected = false;
	 $scope.selection=[];
	 
	
	  $scope.loadMerchantData = function () {
	        $scope.merchants = [];
	        merchantSvc.GetMerchants($rootScope.UsrRghts.linkId).success(function (response) {
	            return $scope.merchants = response;
	        });
	  }
	
	$scope.loadMerchantData = function () {
        $scope.merchants = [];
        merchantSvc.GetMerchants().success(function (response) {
            return $scope.merchants = response;
        });
  }
	$scope.loadMerchantData();

	  if($scope.merchantSelect>0){
		  $scope.loadLocationsByMerchantId($scope.merchantSelect);
	  }
	 $scope.loadLocationsByMerchantId = function (merchantId) {
	        $scope.locations = [];
	        agentSvc.GetLocationsByMerchant(merchantId).success(function (response) {
	            return $scope.locations = response;
	        });
	  }
	
    $scope.loadAgentData = function () {
        $scope.agents = [], $scope.searchKeywords = "", $scope.filteredAgents = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
        agentSvc.GetAgents().success(function (response) {
            return $scope.agents = response, $scope.searchKeywords = "", $scope.filteredAgents = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageAgents = $scope.filteredAgents.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredAgents = $filter("filter")($scope.agents, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredAgents = $filter("orderBy")($scope.agents, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageAgents = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }

    $scope.loadAgentData();
    
    $scope.onMerchantChange=function (merchantId) {
        if ($agentValid(merchantId)) {
            
            	//if($scope.merchantSelect==""){
            	$scope.loadLocationsByMerchantId(merchantId);
            	//}
            	
        }
        }
        
  
    $scope.editAgent = function (agent) {
    	if (!$filter('checkRightToEdit')($rootScope.UsrRghts.rightsHeaderList, "#" +
    			$location.path())) {
    		logger.log("Oh snap! You are not allowed to modify the Satelite.");
    		return false;
    	}
    	$scope.header="Edit Satelite";
        $scope.agentEditMode = true;
        $scope.isExisting = true;
        $scope.agentId = agent.agentId;
        $scope.agentCode = agent.agentCode;
        $scope.agentDesc = agent.agentDesc;
        $scope.active = agent.active;
        $scope.locationSelect=agent.locationId;
        // $scope.loadBranchData();
        $scope.merchantSelect=agent.merchantId;
        $scope.programmes=agent.programmes;
    };
  
    $scope.addAgent = function () {
        
    	if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
    		logger.log("Oh snap! You are not allowed to create Satelite.");
    		return false;
    	}
    	$scope.header="Create Satelite";

        $scope.agentEditMode = true;
        $scope.isExisting = false;
        $scope.agentId = 0;
        $scope.agentCode = "";
        $scope.agentDesc = "";
        $scope.active = false;
        $scope.previewServices=false;
        $scope.isDisabled=true;
        $scope.locationSelect="";
        $scope.merchantSelect="";
    };

    $scope.cancelAgent = function () {
        $scope.agentEditMode = false;
        $scope.previewServices=false;
        $scope.active = false;
        $scope.basketSelect="";
        $scope.locationSelect="";
        $scope.programmes=[];
        $scope.basketValue="";
        $scope.isDisabled=false;
        $scope.merchantSelect="";
       
    }

    $scope.updAgent= function () {
        var agent = {};

        if (!$agentValid($scope.agentCode)) {
            logger.logWarning("Opss! You may have skipped specifying the Satelite's Code. Please try again.")
            return false;
        }
        if (!$agentValid($scope.agentDesc)) {
            logger.logWarning("Opss! You may have skipped specifying the Satelite's Description. Please try again.")
            return false;
        }
        if (!$agentValid($scope.merchantSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the Service Provider. Please try again.")
            return false;
        }
        if (!$agentValid($scope.locationSelect)) {
            logger.logWarning("Opss! You may have skipped specifying the location. Please try again.")
            return false;
        }
        if (!$agentValid($scope.agentId))
            agent.agentId = 0;
        else
        	agent.agentId = $scope.agentId;
        agent.agentCode=$scope.agentCode;
        agent.agentDesc=$scope.agentDesc;
        agent.merchantId=$scope.merchantSelect;
        agent.locationId=$scope.locationSelect;
        agent.active = $scope.active;
        agent.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        agentSvc.UpdAgent(agent).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Satelite information was saved succesfully")
                $scope.agentId = 0;
                $scope.agentCode = "";
                $scope.agentDesc = "";
                $scope.active = false;
                $scope.projectId=0;
                $scope.merchantSelect="";
                $scope.basketId=0;
                $scope.agentEditMode = false;
                $scope.loadAgentData();
            }
            else  {
                logger.logWarning(response.respMessage);
            }
           
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

} ]).factory('agentSvc', function ($http) {
    var EvAgentsvc = {};
    EvAgentsvc.GetAgents = function () {
        return $http({
            url: '/compas/rest/agent/gtAgents/',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
// /
    EvAgentsvc.GetBranchesByMerchant = function (merchantId) {
        return $http({
            url: '/compas/rest/agent/getBranchesByMerchant/'+merchantId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    EvAgentsvc.GetLocationsByMerchant = function (merchantId) {
        return $http({
            url: '/compas/rest/agent/getLocationsByMerchant/'+merchantId,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    EvAgentsvc.UpdAgent = function (agent) {
        return $http({
            url: '/compas/rest/agent/updAgent',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: agent
        });
    };
    
    EvAgentsvc.GetAgentsByMerchant=function(merchant){
		return $http({
			url: '/compas/rest/agent/getAgentsByMerchant/'+merchant,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	}
    return EvAgentsvc;
}).factory('$agentValid', function () {
    return function (valData) {
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
});
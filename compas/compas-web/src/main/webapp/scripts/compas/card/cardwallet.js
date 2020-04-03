/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.wallet', []).controller("walletCtrl", ["$scope", "$filter", "walletSvc","branchSvc","organizationSvc","$locationValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,walletSvc,branchSvc,organizationSvc, $locationValid, $rootScope, blockUI, logger, $location) {
	
    var init;

	$scope.walletDetails = [];
	$scope.allItemsSelected = false;
	 $scope.selection=[];
//    $scope.loadLocationData = function () {
//        $scope.walletDetails = [], $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.locationEditMode = false;
//        locationSvc.GetLocations($scope).success(function (response) {
//            return $scope.walletDetails = response, $scope.searchKeywords = "", $scope.filteredLocations = [], $scope.row = "", $scope.select = function (page) {
//                var end, start;
//                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageLocations = $scope.filteredLocations.slice(start, end)
//            }, $scope.onFilterChange = function () {
//                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
//            }, $scope.onNumPerPageChange = function () {
//                return $scope.select(1), $scope.currentPage = 1
//            }, $scope.onOrderChange = function () {
//                return $scope.select(1), $scope.currentPage = 1
//            }, $scope.search = function () {
//                return $scope.filteredLocations = $filter("filter")($scope.walletDetails, $scope.searchKeywords), $scope.onFilterChange()
//            }, $scope.order = function (rowName) {
//                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredLocations = $filter("orderBy")($scope.walletDetails, rowName), $scope.onOrderChange()) : void 0
//            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPages = [], (init = function () {
//                return $scope.search(), $scope.select($scope.currentPage)
//            })();
//        }).error(function (data, status, headers, config) {
//            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
//        });
//    }
//    
//    
//    $scope.loadLocationData();

    $scope.loadOrganizationData = function () {
        $scope.organizations = []

        organizationSvc.GetOrganizations().success(function (response) {
            return $scope.organizations = response
            })
    }
    
    $scope.loadOrganizationData();
    
    $scope.loadWalletData = function () {
        $scope.wallets = [];
        walletSvc.GetWallets().success(function (response) {
            return $scope.wallets = response
            })
    }
    $scope.loadWalletData ();
    $scope.editLocation = function (location) {

        $scope.locationEditMode = true;
        $scope.isExisting = true;
        $scope.locationId = location.locationId;
        $scope.locationCode = location.locationCode;
        $scope.locationName = location.locationName;
        $scope.branchSelect = location.branchId;
        $scope.active = location.active;
        
    };
    
    $scope.addLocation = function () {
        
    	
    
        $scope.locationEditMode = true;
        $scope.isExisting = false;
        $scope.locationId = 0;
        $scope.locationCode = "";
        $scope.locationName = "";
        $scope.branchId = 0;
        $scope.active = false;
        $scope.isDisabled = true;
    };

    $scope.cancelLocation = function () {
        $scope.locationEditMode = false;
        $scope.active = false;
       
    }
    $scope.selectWallet = function (index) {
        // If any entity is not checked, then uncheck the "allItemsSelected" checkbox
    	$scope.selection=[];
        for (var i = 0; i < $scope.wallets.length; i++) {
        	// var idx = $scope.selection.indexOf(branchId);
        
        	   if (index > -1 && $scope.wallets[i].isActive) {
        		   $scope.selection.push(
     	        		  {"walletId": $scope.wallets[i].walletId
     	        			  });
        		  

        	   }
        	  else if(!$scope.wallets[i].isActive){
        	   
        		  $scope.selection.splice(i, 1);
        	   
        	  }

            if (!$scope.wallets[i].isActive) {
                $scope.allItemsSelected = false;
                return;
            }
        }

        //If not the check the "allItemsSelected" checkbox
        $scope.allItemsSelected = true;
    };

    // This executes when checkbox in table header is checked
    $scope.selectAllWallets = function () {
    	// Loop through all the entities and set their isChecked property
    	$scope.selection=[];
    	for (var i = 0; i < $scope.wallets.length; i++) {
    		$scope.wallets[i].isActive = $scope.allItemsSelected;
    		if ($scope.wallets[i].isActive) {
    			$scope.selection.push(
    					{"walletId": $scope.wallets[i].walletId
    					});


    		}
    		else if(!$scope.wallets[i].isActive){

    			$scope.selection.splice(i, 1);

    		}

    	}
    }

    $scope.updWallet = function () {
        var wallet = {};

       
        wallet.organizationId = $scope.organizationSelect;
        wallet.wallets = $scope.wallets;
     
        location.createdBy =$rootScope.UsrRghts.sessionId;
        blockUI.start()
        walletSvc.UpdWallet(wallet).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! The Wallet information was saved succesfully")
                if (parseFloat($scope.userId) == parseFloat($rootScope.UsrRghts.sessionId)) {
                	localStorageService.clearAll();
                	loginSvc.GetRights($rootScope.UsrRghts.sessionId).success(function (rightLst)
                			{
                		localStorageService.set('rxr', rightLst);
                		$rootScope.UsrRghts = rightLst;
                			}).error(function (data, status, headers, config) {
                				logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                			});
                }
                $scope.loadWalletData ();
                $scope.loadOrganizationData();
              
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

} ]).factory('walletSvc', function ($http) {
    var compasLocationSvc = {};
    compasLocationSvc.GetWallets = function ($scope) {
        return $http({
            url: '/compas/rest/member/gtWallets',
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };

    compasLocationSvc.UpdWallet = function (wallet) {
        return $http({
            url: '/compas/rest/member/updWallet',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: wallet
        });
    };
    return compasLocationSvc;
}).factory('$locationValid', function () {
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
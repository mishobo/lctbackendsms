/**
 *  * Transaction Held Angular Module  
 */
'use strict';
angular.module('app.rptMembers', []).controller("rptMemberReportCtrl", ["$scope", "$filter", "memStatementSvc", "merchantSvc", "productSvc", "$memStatementValid", "$rootScope", "voucherSvc", "organizationSvc", "blockUI", "logger", "$location", "$http", "$window", "$timeout", "localStorageService", function($scope, $filter, memStatementSvc, merchantSvc, productSvc, $memStatementValid, $rootScope, voucherSvc, organizationSvc, blockUI, logger, $location, $http, $window, $timeout, localStorageService) {
    $scope.mem = [];
    $scope.Individualmem = [];
    $scope.showFilters = true;
    $scope.showDetails = true;
    $scope.showTransactions = false;
    $scope.showProvider = false;
    $scope.showScheme = false;
    $scope.tempAmountList = [];
    $scope.voideAmount = 0;
    $scope.invalidAmount = 0;
    $scope.totalAmount = 0;
    $scope.netAmount = 0;
    $scope.billNo = 0;
    $scope.name = 0;
    $scope.memberNo = 0;
    $scope.txnDate = 0;
    // $scope.invoiceNo = 0;
    $scope.productName = 0;
    $scope.merchantName = 0;
    $scope.serviceName = 0;
    var tmrAuthData;
    var init;
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    $scope.excel = "E";
    $scope.pdf = "P";

    $scope.maxDate = yyyy + '-' + mm + '-' + dd;
    $scope.states = 0;
    var cancelledStyle = {
        'background-color': '#fdefee',
        'color': '#b13d31'
    };
    var cancelledStyleInputs = {
        'background-color': '#fdefee',
        'color': '#b13d31',
        'border-color': '#fdefee'
    };

    var pendingAuthStyle = {
        'background-color': 'floralwhite',
        'color': '#af7f18'
    };
    var pendingAuthStyleInputs = {
        'color': '#af7f18'
    };

    var authenticatedStyle = {
        'background-color': '#eef8fc',
        'color': '#26929c'
    };
    var authenticatedStyleInputs = {
        'color': '#26929c'
    };
    $scope.ready = false;
    $scope.changed = function() {
        $scope.ready = false;
    }

    // export Report
    $scope.exportReport = function(type) {
        var FromDt = $filter('date')($scope.mem.FromDt, 'yyyy-MM-dd');
        var ToDt = $filter('date')($scope.mem.ToDt, 'yyyy-MM-dd');
        var orgId = $scope.orgSelect;
        var mer_name = current;
        if (FromDt == null) {
            logger.logWarning("Opss! You may have skipped specifying the Start date. Please try again.")
            return false;
        } else if (ToDt == null) {
            logger.logWarning("Opss! You may have skipped specifying the End date. Please try again.")
            return false;
        } else if (orgId == null) {
            logger.logWarning("Opss! You may have skipped specifying the Organization. Please try again.")
            return false;
        } else if (FromDt > ToDt) {
            logger.logWarning("Oops! From date cannot be later than to date.")
            return false;
        } else {
        	
        	if (LoggedInAdmin === 1 || LoggedInAdmin === 19) {
                $scope.url = '/compas/reports?type=ALLTR&FrDt=' + FromDt + '&ToDt=' + ToDt + '&orgId=' + orgId + '&eType=' + type;
                $location.path('/reports/transaction');
        	} else{
                $scope.url = '/compas/reports?type=ALLSPTR&FrDt=' + FromDt + '&ToDt=' + ToDt + '&orgId=' + orgId + '&mer_name=' + mer_name + '&eType=' + type;
                $location.path('/reports/transaction');
        	}
        }


    }
    
    // export Individual Report
    $scope.exportIndividuaReport = function(type, txn) {    
    	console.log("Transaction::", txn);
    	console.log("Bill Number::", txn.billNo);
        var billNo = txn.billNo;
            $scope.url = '/compas/reports?type=ITR&billNo=' + billNo + '&eType=' + type;
            $location.path('/reports/transaction');
    }
    // report preview
    $scope.previewReport = function(mem) {
        $scope.userTransaction = [];
        $scope.fromDt = $filter('date')(mem.FromDt, 'yyyy-MM-dd');
        $scope.toDt = $filter('date')(mem.ToDt, 'yyyy-MM-dd');
        if (($scope.fromDt != null && $scope.toDt != null) && ($scope.fromDt > $scope.toDt)) {
            logger.logWarning("Oops! From date cannot be later than to date.")
            return false;
        } else {
            $scope.loadTransactions();
        }


    }
    let current;
    // check if admin is logged in
    let LoggedInAdmin;

    // fetch user session
    const currUserId = localStorageService.get("userId");
    $http({
        url: '/compas/rest/dashBoard/isuserakuh/' + currUserId,
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).success(function(response) {
        console.log(response);
        console.log('UsrRights: ', $rootScope.UsrRghts);
        console.log('USER GROUP TYPE ID IS : ' + $rootScope.UsrRghts.userGroupId);
        LoggedInAdmin = $rootScope.UsrRghts.userGroupId;
        console.log(`Assigned Logged group type id: ${LoggedInAdmin}`);
        console.log("Logged in User: " + $rootScope.UsrRghts.sessionFullName);
        const LoggedInUser = $rootScope.UsrRghts.sessionFullName;
        console.log(`Assigned Logged in User: ${LoggedInUser}`);
        current = LoggedInUser;
        let rightsList = $rootScope.UsrRghts.rightsHeaderList[3].rightsList;
        rightsList = rightsList.filter(function(menu) {
            return menu.rightViewName !== "#/masters/uploadmember";
        });
        $rootScope.UsrRghts.rightsHeaderList[3].rightsList = rightsList;

    });


    $scope.loadTransactions = function() {
        $scope.showDetails = false;
        $scope.userTxns = [], $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.agentEditMode = false;
        $scope.previewServices = false;
        var gate = {};

        gate.fromDate = $scope.fromDt;
        gate.toDate = $scope.toDt;
        gate.orgId = $scope.orgSelect;
        gate.merchantName= current;
        // gate.merchantId = $scope.merchantSelect;
        // gate.productId = $scope.productSelect;
        
        	gate.merchantName = current;
        voucherSvc.GetAllDetailedMemberReport(gate).success(function(response) {
            $scope.showTransactions = true;
            if (response.length > 0) {
                $scope.ready = true;
            }
            return $scope.userTxns = response, $scope.searchKeywords = "", $scope.filteredUserTxns = [], $scope.row = "", $scope.states += 1, $scope.select = function(page) {
                var end, start;

                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageUserTxns = $scope.filteredUserTxns.slice(start, end)
            }, $scope.onFilterChange = function() {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function() {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function() {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function() {
                return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function(rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredUserTxns = $filter("orderBy")($scope.userTxns, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [10, 20, 50, 100, 200], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageUserTxns = [], (init = function() {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function(data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }

    $scope.$watch("states", function(newValue, oldValue) {
        if (newValue != oldValue) {
            if ($scope.merchantSelect > 0 && $scope.productSelect == 0) {
                return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {
                    merchantId: $scope.merchantSelect
                }), $scope.onFilterChange()
            } else if ($scope.merchantSelect == 0 && $scope.productSelect > 0) {
                return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {
                    productId: $scope.productSelect
                }), $scope.onFilterChange()
            } else if ($scope.merchantSelect > 0 && $scope.productSelect > 0) {
                return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {
                    productId: $scope.productSelect,
                    merchantId: $scope.merchantSelect
                }), $scope.onFilterChange()
            } else {
                return $scope.filteredUserTxns = $scope.userTxns;
            }
        }
    });

    $scope.onOrgChange = function(orgId) {

        console.log("typeof", current)
        if (orgId > 0) {
            $scope.fromDt = null;
            $scope.toDt = null;
            $scope.mem.FromDt = null;
            $scope.mem.ToDt = null;
            $scope.mem.merchantName = current;
            console.log($scope.mem.merchantName);
            $scope.loadTransactions();
            $scope.loadMerchantData(current);
            $scope.loadProductData(orgId);
            if (LoggedInAdmin === 1) { 
            	$scope.showProvider = true;
            } else{
            	$scope.showProvider = false;
            }
            $scope.showScheme = false;
            $scope.productSelect = 0;
            $scope.merchantSelect = 0;
            $scope.ready = false;
        }
    }

    $scope.onFilter = function() {
        if ($scope.merchantSelect > 0 && $scope.productSelect == 0) {
            return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {
                merchantId: $scope.merchantSelect
            }), $scope.onFilterChange()
        } else if ($scope.merchantSelect == 0 && $scope.productSelect > 0) {
            return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {
                productId: $scope.productSelect
            }), $scope.onFilterChange()
        } else if ($scope.merchantSelect > 0 && $scope.productSelect > 0) {
            return $scope.filteredUserTxns = $filter("filter")($scope.userTxns, {
                productId: $scope.productSelect,
                merchantId: $scope.merchantSelect
            }), $scope.onFilterChange()
        } else {
            return $scope.filteredUserTxns = $scope.userTxns;
        }
    }

    $scope.loadOrganizationData = function() {
        $scope.orgs = [];
        organizationSvc.GetOrganizations().success(function(response) {
            return $scope.orgs = response
        }).error(function(data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.loadOrganizationData();

    //     $scope.loadMerchantData = function () {
    //         $scope.merchants = [];
    //         merchantSvc.GetMerchants().success(function (response) {
    //             return $scope.merchants = response
    //         }).error(function (data, status, headers, config) {
    //             logger.logError("Oh snap! There is a problem with the server,
    // please contact the adminstrator.")
    //         });
    //     }

    // fetch merchants by username
    $scope.loadMerchantData = function(LoggedInUser) {
        $scope.merchants = [];
        if (LoggedInAdmin === 1 || LoggedInAdmin === 19) {
            merchantSvc.GetMerchants().success(function(response) {
                console.log(response);
                return $scope.merchants = response
            }).error(function(data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            });

        } else {
            merchantSvc.GetMerchantsByUserName(LoggedInUser).success(function(response) {
                console.log(response);
                return $scope.merchants = response
            }).error(function(data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            });
        }

    }

    $scope.loadProductData = function(orgId) {
        $scope.products = [];
        productSvc.GetProducts(orgId).success(function(response) {
            return $scope.products = response
        }).error(function(data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    $scope.onTimeout = function() {

        if ($scope.userTxns.length > 0) {

            $scope.$apply($scope.loadTransactions());
            // $scope.authPending = $scope.setAuthPending();
        }

        tmrAuthData = $timeout($scope.onTimeout, 1000);
    }
    // tmrAuthData = $timeout($scope.onTimeout,1000);
    $scope.loadAmpuntDetail = function() {
    	if (LoggedInAdmin === 1 || LoggedInAdmin === 19) {            
           voucherSvc.GetCurrentDetailedMemberReport().success(function(response) {
            $scope.tempAmountList = response;
            if ($scope.tempAmountList.length > 0) {

                for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {

                    $scope.totalAmount = $scope.tempAmountList[i].totalAmount // +$scope.tempAmountList[i].invalidAmount;
                    $scope.voideAmount = $scope.tempAmountList[i].voidAmount;
                    $scope.invalidAmount = $scope.tempAmountList[i].invalidAmount;
                    // $scope.netAmount=(($scope.totalAmount-$scope.voideAmount)-$scope.invalidAmount);
                    $scope.netAmount = $scope.tempAmountList[i].netAmount;
                    $scope.billNo = $scope.tempAmountList[i].billNo;
                }
            }
        })
            
    	} else{
            voucherSvc.GetCurrentDetailedServiceProvidersTxn(gate).success(function(response) {
                $scope.tempAmountList = response;
                console.log("SP TRANS :" +response);
                if ($scope.tempAmountList.length > 0) {

                    for (var i = 0; i <= $scope.tempAmountList.length - 1; i++) {

                        $scope.totalAmount = $scope.tempAmountList[i].totalAmount // +$scope.tempAmountList[i].invalidAmount;
                        $scope.voideAmount = $scope.tempAmountList[i].voidAmount;
                        $scope.invalidAmount = $scope.tempAmountList[i].invalidAmount;
                        // $scope.netAmount=(($scope.totalAmount-$scope.voideAmount)-$scope.invalidAmount);
                        $scope.netAmount = $scope.tempAmountList[i].netAmount;
                        $scope.billNo = $scope.tempAmountList[i].billNo;
                    }
                }
            })
            
    }
  }
    // $scope.loadAmpuntDetail();

}]).factory('memStatementSvc', function($http) {
    var compasRptStatementSvc = {};



    return compasRptStatementSvc;
}).factory('$memStatementValid', function() {
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

});
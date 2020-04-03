/**
 * Created by Kibet on 5/23/2016.
 */

'use strict';
angular.module('app.voucher_topup', []).controller("voucherTopupCtrl", ["$scope", "$filter", "cardSvc","$rootScope",
    "blockUI", "logger" ,"$location","memberSvc","$vouchertopupValid","programmeSvc","bnfgrpSvc","voucherSvc","voucherTopupSvc","issueDeviceSvc",
    function ($scope, $filter,cardSvc, $rootScope, blockUI, logger, $location,memberSvc,$vouchertopupValid,programmeSvc,bnfgrpSvc,voucherSvc,voucherTopupSvc,issueDeviceSvc) {
        $scope.programmes = [];
        $scope.benefiaryGroups = [];
        $scope.selectedProgrammes = [];
        $scope.selection = [];
        $scope.bg_selection = [];
        $scope.allBgItemsSelected = false;
        $scope.showProgrammes  = false;
        $scope.showBeneficiaryGroups = false;
        $scope.showBeneficiary = false;
        $scope.allRetailerSelected=false;
        $scope.showRetailers=false;
        $scope.showList=false;
        $scope.showTopupCreation=true;
       
        $scope.generateTopup=function(){
        	$scope.showTopupCreation=false;
        	 $scope.showList=true;
        }
        $scope.loadProgrammes = function () {
            programmeSvc.GetProgrammes($scope).success(function (response) {
                $scope.programmes = response;
                console.log($scope.programmes);
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            });

        };
        /**
         * loads Beneficiary group by programmeId 
         */
       
       
        

        /**
         * select one item
         * @param index
         */
        $scope.selectProgramme = function (index) {
            // If any entity is not checked, then uncheck the "allItemsSelected"
            // checkbox
            console.log("Selected Index##");
            console.log(index);
            $scope.selection=[];
            for (var i = 0; i < $scope.programmes.length; i++) {
                // var idx = $scope.selection.indexOf(branchId);
                console.log($scope.programmes[i].isActive);
                
                if (index > -1 && $scope.programmes[i].isActive) {
                    $scope.selection.push(
                        $scope.programmes[i].programmeId
                    );

                }
                else if(!$scope.programmes[i].isActive){
                    $scope.selection.splice(i, 1);
                }

                if (!$scope.programmes[i].isActive) {
                    $scope.allItemsSelected = false;
                    return;
                }
            }
            $scope.allItemsSelected = true;
        };

        /**
         * This executes when checkbox in table header is checked
         * selects all programmes
         */
        $scope.selectAllProgrammes = function () {
            // Loop through all the entities and set their isChecked property
            $scope.selection=[];
            for (var i = 0; i < $scope.programmes.length; i++) {
                $scope.programmes[i].isActive = $scope.allItemsSelected;
                if ($scope.programmes[i].isActive) {
                    $scope.selection.push(
                        $scope.programmes[i].programmeId
                    );

                    //$scope.selectedProgrammes.push($scope.programmes[i].programmeId);
                }
                else if(!$scope.programmes[i].isActive){
                    $scope.selection.splice(i, 1);
                   // $scope.selectedProgrammes.splice(i,1);
                }

            }
        };



        /**
         * do the top up here now
         * generates topup values in the db.
         */
        $scope.doTopUp = function () {
        	$scope.topupDetails=[];
        	$scope.retailerSelected=[];
        	for(var j=0;j<$scope.retailers.length;j++){
        		if($scope.retailers[j].isActive){
        			$scope.retailerSelected.push($scope.retailers[j].agentId);
        		}
        	}
            var value = $scope.topupType;
            if($vouchertopupValid(value)){
                if(value==="PROG"){
                    //logger.log(value);
                   // console.log($scope.selection);
                    //console.log($scope.selection.length());
                    var topup ={};
                    $scope.bg_selection=[];
                    for(var i=0;i<$scope.beneficiaryGroups.length;i++){
                		if($scope.beneficiaryGroups[i].isActive){
                			$scope.topupDetails.push($scope.beneficiaryGroups[i])
                			$scope.bg_selection.push($scope.beneficiaryGroups[i].bnfGrpId);
                		}
                	}
                    topup.beneficiary_groups = $scope.bg_selection;
                    topup.requestType = 'PROG';
                    topup.bgTopupDetails=$scope.topupDetails
                    topup.retailerSelected=$scope.retailerSelected
                    topup.programme_id=$scope.programmeSelect;
                    if($scope.bg_selection.length > 0){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully");
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped selecting programmes, Please try again");
                    }


                }

                if (value === "BG"){
                    //logger.log(value);
                    // console.log($scope.selection);
                    //console.log($scope.selection.length());
                	$scope.bg_selection=[];
                	for(var i=0;i<$scope.beneficiaryGroups.length;i++){
                		if($scope.beneficiaryGroups[i].isActive){
                			$scope.topupDetails.push($scope.beneficiaryGroups[i])
                			$scope.bg_selection.push($scope.beneficiaryGroups[i].bnfGrpId);
                		}
                	}
                    var topup ={};
                    
                    topup.beneficiary_groups = $scope.bg_selection;
                    topup.requestType = 'BG';
                    topup.bgTopupDetails=$scope.topupDetails
                    topup.retailerSelected=$scope.retailerSelected
                    topup.programme_id=0;
                    if($scope.bg_selection.length > 0){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully!!");
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped selecting beneficiary groups, Please try again");
                    }
                }

                if (value === "BEN"){
                    //logger.log(value);
                    // console.log($scope.selection);
                    //console.log($scope.selection.length());
                    var topup ={};
                    topup.card_number = $scope.cardNumber;
                    topup.requestType = 'BEN';
                    topup.retailerSelected=$scope.retailerSelected
                    if($vouchertopupValid($scope.cardNumber)){
                        blockUI.start();
                        voucherTopupSvc.doTopup(topup).success(function (response) {
                            if (response.respCode == 200) {
                                logger.logSuccess("Great! The voucher topup saved successfully!!");
                                $scope.programmes = [];
                                $scope.benefiaryGroups = [];
                                $scope.selectedProgrammes = [];
                                $scope.selection = [];
                                $scope.bg_selection = [];
                                $scope.allBgItemsSelected = false;
                                $location.path('/');
                            }
                            else  {
                                logger.logWarning(response.respMessage);
                            }
                            blockUI.stop();
                        }).error(function (data, status, headers, config) {
                            logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
                            blockUI.stop();
                        });
                    } else{
                        logger.logError("Error ! You have skipped entering the Card Number, Please try again");
                    }
                }
            }
        }

        /**
         * exit topup screen
         */
        $scope.cancelTopup = function () {
            //logger.logSuccess("Firmware Uploaded successfully!!");
            $scope.programmes = [];
            $scope.benefiaryGroups = [];
            $scope.selectedProgrammes = [];
            $scope.selection = [];
            $scope.bg_selection = []
            $scope.allBgItemsSelected = false
            $location.path('/');
        }
        
        $scope.updateTopupValue=function(bg){
        
        }
        $scope.getTotal = function(){
            var total = 0;
            $scope.total=0;
            for(var i = 0; i < $scope.beneficiaryGroups.length; i++){
                var product = $scope.beneficiaryGroups[i];
                total += (product.topupValue);
            }
            $scope.total=$filter('number')(total, fractionSize);
           // return total;
        }
       

    }]).factory('voucherTopupSvc', function ($http) {
    	var voucherTopupSvc = {};
    	

    	voucherTopupSvc.doTopup = function (topup) {
    		return $http({
    			url: '/compas/rest/vouchertopup/topup',
    			method: 'POST',
    			headers: { 'Content-Type': 'application/json' },
    			data: topup
    		});
    	};
    	voucherTopupSvc.GetBnfGrpsForTopup=function(){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsForTopup/',
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}
    	voucherTopupSvc.GetBnfGrpsByPrgId=function(programmeId){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsByPrgId/'+programmeId,
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}

     	voucherTopupSvc.GetBnfGrpsTopuped=function(){
    		return $http({
    			url: '/compas/rest/vouchertopup/gtBnfgrpsTopuped/',
    			method: 'GET',
    			headers: { 'Content-Type': 'application/json' }
    		});
    	}

    	return voucherTopupSvc;
    }).factory('$vouchertopupValid', function () {
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


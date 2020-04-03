/**
 * Created by Kibet on 5/23/2016.
 */

'use strict';
angular.module('app.topupapproval', []).controller("topupApprovalCtrl", ["$scope", "$filter", "$rootScope",
    "blockUI", "logger" ,"$location","memberSvc","$topupapprovalValid","programmeSvc","bnfgrpSvc","topupApprovalSvc","voucherTopupSvc","issueDeviceSvc",
    function ($scope, $filter,$rootScope, blockUI, logger, $location,memberSvc,$topupapprovalValid,programmeSvc,bnfgrpSvc,topupApprovalSvc,voucherTopupSvc,issueDeviceSvc) {
    var init;
        $scope.benefiaryGroups = [];
        $scope.showBeneficiary = false;
        $scope.mem=[];
        $scope.showTopupDetails=true;
        $scope.allTopupSelected=false;
        $scope.allRetailerSelected = false;
        $scope.showAgents=true;
      
        /**
		 * loads beneficiary groups when topup type is programme
		 */
        $scope.loadBeneficiaryGroups = function () {
        	voucherTopupSvc.GetBnfGrpsForTopup().success(function (response) {
                console.log("Beneficiary groups##");
                console.log(response);
                $scope.beneficiaryGroups = response;
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
            });

        };
        $scope.loadBeneficiaryGroups(); 
        

      
       
       

        /**
		 * This executes when checkbox in table header is checked selects all
		 * the beneficiary groups
		 */

        $scope.selectAllTopup = function () {
            // Loop through all the entities and set their isChecked property
            $scope.bg_selection=[];
            for (var i = 0; i < $scope.topups.length; i++) {
                $scope.topups[i].isActive = $scope.allTopupSelected;
                if ($scope.topups[i].isActive) {
                    $scope.bg_selection.push(
                        $scope.topups[i].id
                    );
                }
                else if(!$scope.topups[i].isActive){
                    $scope.bg_selection.splice(i, 1);

                }
            }
        };


        /**
		 * select one item
		 * 
		 * @param index
		 */
        $scope.selectTopup = function (index) {
            // If any entity is not checked, then uncheck the "allItemsSelected"
            // checkbox
            console.log("Selected Index##");
            console.log(index);
            $scope.bg_selection=[];
            for (var i = 0; i < $scope.topups.length; i++) {
                console.log($scope.topups[i].isActive);
                
                if (index > -1 && $scope.topups[i].isActive) {
                    $scope.bg_selection.push(
                        $scope.topups[i].id
                    );
                }
                else if(!$scope.topups[i].isActive){
                    $scope.bg_selection.splice(i, 1);

                }

                if (!$scope.topups[i].isActive) {
                    $scope.allTopupSelected = false;
                    return;
                }

            }
            $scope.allTopupSelected = true;
        };

        $scope.loadToupDeatils = function () {
            $scope.topups = [], $scope.searchKeywords = "", $scope.filteredTopups = [], $scope.row = "", $scope.agentEditMode = false; $scope.previewServices=false;
            var bnfGrp={}
     	   $scope.fromDt= $filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
     	   $scope.toDt=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
           if($scope.bnfGrpSelect==""){
        	   bnfGrp.bnfGrpId=0;
           }else{
        	   bnfGrp.bnfGrpId=$scope.bnfGrpSelect;
           }
           if($scope.agenSelect==""){
        	   bnfGrp.agentId=0;
           }else{
        	   bnfGrp.agentId=$scope.agentSelect;
           }
            bnfGrp.fromDate= $scope.fromDt
            bnfGrp.toDate=$scope.toDt
            topupApprovalSvc.GetTopupDetailsToApprove(bnfGrp).success(function (response) {
                return $scope.topups = response, $scope.searchKeywords = "", $scope.filteredTopups = [], $scope.row = "", $scope.select = function (page) {
                    var end, start;
                    return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageTopups = $scope.filteredTopups.slice(start, end)
                }, $scope.onFilterChange = function () {
                    return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
                }, $scope.onNumPerPageChange = function () {
                    return $scope.select(1), $scope.currentPage = 1
                }, $scope.onOrderChange = function () {
                    return $scope.select(1), $scope.currentPage = 1
                }, $scope.search = function () {
                    return $scope.filteredTopups = $filter("filter")($scope.topups, $scope.searchKeywords), $scope.onFilterChange()
                }, $scope.order = function (rowName) {
                    return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredTopups = $filter("orderBy")($scope.topups, rowName), $scope.onOrderChange()) : void 0
                }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageTopups = [], (init = function () {
                    return $scope.search(), $scope.select($scope.currentPage)
                })();
            }).error(function (data, status, headers, config) {
                logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            });
        }
        
        $scope.preview=function(){
        	 if (!$topupapprovalValid($scope.mem.FromDt)) {
                 logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
                 return false;
             }
        	  if (!$topupapprovalValid($scope.mem.ToDt)) {
                 logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
                 return false;
             }
            if (!$topupapprovalValid($scope.bnfGrpSelect)) {
                logger.logWarning("Opss! You may have skipped specifying the Beneficiary Group. Please try again.")
                return false;
            }
          
        	  $scope.showTopupDetails=false;
        	  $scope.allTopupSelected = false;
              $scope.loadToupDeatils()
        }
        $scope.cancelapprove=function(){
        	$scope.showTopupDetails=true;
			$scope.bg_selection=[];
			$scope.bnfGrpSelect="";
			$scope.mem=[]
			$scope.retailerSelected=[];
			 $scope.allTopupSelected=false;
			 $scope.allRetailerSelected = false;
		    $scope.showAgents=true;
      }
        /**
		 * do the top up here now generates topup values in the db.
		 */
    	$scope.approve=function(){
    		if (!$topupapprovalValid($scope.mem.FromDt)) {
                logger.logWarning("Opss! You may have skipped specifying the From Date. Please try again.")
                return false;
            }
       	  if (!$topupapprovalValid($scope.mem.ToDt)) {
                logger.logWarning("Opss! You may have skipped specifying the To Date. Please try again.")
                return false;
            }
           if (!$topupapprovalValid($scope.bnfGrpSelect)) {
               logger.logWarning("Opss! You may have skipped specifying the Beneficiary Group. Please try again.")
               return false;
           }
    		$scope.retailerSelected=[];
        	for(var j=0;j<$scope.retailers.length;j++){
        		if($scope.retailers[j].isActive){
        			$scope.retailerSelected.push($scope.retailers[j].agentId);
        		}
        	}
        	if($scope.retailerSelected.length<=0){
        	    logger.logWarning("Opss! You may have skipped specifying the Retailer. Please try again.")
                return false;
        	}
    		var bnf = {};
    		$scope.bg_selection=[];
    		$scope.fromDt= $filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
      	   $scope.toDt=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
            if($scope.bnfGrpSelect==""){
            	bnf.bnfGrpId=0;
            }else{
            	bnf.bnfGrpId=$scope.bnfGrpSelect;
            }
            if($scope.agenSelect==""){
            	bnf.agentId=0;
            }else{
            	bnf.agentId=$scope.agentSelect;
            }
            bnf.fromDate= $scope.fromDt
             bnf.toDate=$scope.toDt
    		//for(var i=0;i<$scope.topups.length;i++){
    			//if($scope.topups[i].isActive)
    			//$scope.bg_selection.push($scope.topups[i].id)
    		//}
    		//bnf.topupSelection=$scope.bg_selection;
    		bnf.retailerSelected=$scope.retailerSelected
    		//if($scope.bg_selection!=null){
    		//if($scope.bg_selection.length<=0){
    		//	 logger.logWarning("Error ! You have skipped selecting topup, Please try again");
    			// return false;
    		//}
    		//}
    		//bnf.bnfStatus="A"

    		bnf.createdBy = $rootScope.UsrRghts.sessionId;
    		blockUI.start()
    		topupApprovalSvc.UpdTopupStatus(bnf).success(function (response) {
    			if (response.respCode == 200) {
    				logger.logSuccess("Great! The Topup Approved succesfully")
    				$scope.showTopupDetails=true;
    				$scope.bg_selection=[];
    				$scope.bnfGrpSelect="";
    				$scope.agentSelect="";
    				$scope.retailerSelected=[];
    			    $scope.showAgents=true;
    			    $scope.allTopupSelected=false;
    			    $scope.allRetailerSelected = false;
    				$scope.mem=[]
    				
    			}
    			else  {
    				logger.logWarning(response.respMessage);
    			}

    			blockUI.stop();
    		}).error(function (data, status, headers, config) {
    			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
    			blockUI.stop();
    		});
    	}
        
    	   $scope.loadIssueDeviceData = function () {
   	        $scope.retailers = []
   	        issueDeviceSvc.GetIssueDevices($scope).success(function (response) {
   	          $scope.retailers = response
   	            })
           }
           $scope.loadIssueDeviceData() 
           
           $scope.loadIssueDeviceData = function () {
   	        $scope.retailers = []
   	        issueDeviceSvc.GetIssueDevices($scope).success(function (response) {
   	          $scope.retailers = response
   	            })
           }
           $scope.loadIssueDeviceData() 
           
           /**
            * This executes when checkbox in table header is checked
            * selects all the retailers
            */

           $scope.selectAllRetailers = function () {
               // Loop through all the entities and set their isChecked property
           	 $scope.selection=[];
               for (var i = 0; i < $scope.retailers.length; i++) {
                   $scope.retailers[i].isActive = $scope.allRetailerSelected;
                   if ($scope.retailers[i].isActive) {
                       $scope.selection.push(
                           $scope.retailers[i].bnfGrpId
                       );
                   }
                   else if(!$scope.retailers[i].isActive){
                       $scope.selection.splice(i, 1);

                   }
               }
           };


           /**
            * select one item
            * @param index
            */
           $scope.selectRetailer = function (index) {
               // If any entity is not checked, then uncheck the "allItemsSelected"
               // checkbox
               console.log("Selected Index##");
               console.log(index);
               $scope.selection=[];
               for (var i = 0; i < $scope.retailers.length; i++) {
                   console.log($scope.retailers[i].isActive);
                   
                   if (index > -1 && $scope.retailers[i].isActive) {
                       $scope.selection.push(
                           $scope.retailers[i].agentId
                       );
                   }
                   if(!$scope.retailers[i].isActive){
                       $scope.selection.splice(i, 1);

                   }

                   if (!$scope.retailers[i].isActive) {
                       $scope.allRetailerSelected = false;
                       return;
                   }

               }
               $scope.allRetailerSelected = true;
           };
           $scope.$watch("bnfGrpSelect", function (newValue, oldValue) {
               if ($topupapprovalValid(newValue)) {
                   if (newValue != oldValue) {
                   	//if($scope.merchantSelect==""){
                   $scope.showAgents=false;
                   $scope.loadIssueDeviceData() ;
                   	//}
                   	
                   }
               }
               
           });
    }]).factory('topupApprovalSvc', function ($http) {
    	var topupApprovalSvc = {};
    	

    	topupApprovalSvc.UpdTopupStatus = function (bnf) {
    		return $http({
    			url: '/compas/rest/vouchertopup/updTopupStatus',
    			method: 'POST',
    			headers: { 'Content-Type': 'application/json' },
    			data: bnf
    		});
    	};
    	topupApprovalSvc.GetTopupDetailsToApprove=function(bnfGrp){
    		return $http({
    			url: '/compas/rest/vouchertopup/topupdetails/',
    			method: 'POST',
    			headers: { 'Content-Type': 'application/json' },
    			data: bnfGrp
    		});
    	}

    	return topupApprovalSvc;
    }).factory('$topupapprovalValid', function () {
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


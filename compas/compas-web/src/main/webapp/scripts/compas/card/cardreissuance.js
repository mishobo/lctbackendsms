/**
 * Provider Angular Module
 */
'use strict';
angular.module('app.cardreissue', []).controller("cardReIssueCtrl", ["$scope", "$filter", "cardReIssueSvc","blockCardsSvc","$cardReIssueValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, cardReIssueSvc,blockCardsSvc,$cardReIssueValid, $rootScope, blockUI, logger, $location) {
    var init;
    $scope.mem=[];
    $scope.loadMemberListData = function () {
        $scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.activateMode = false;$scope.memberEditMode = false; $scope.memberViewMode=true;
        blockCardsSvc.GetCardToBlock($rootScope.UsrRghts.linkId).success(function (response) {
            return $scope.members = response, $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.select = function (page) {
                var end, start;
                return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPageMembers = $scope.filteredMembers.slice(start, end)
            }, $scope.onFilterChange = function () {
                return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
            }, $scope.onNumPerPageChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.onOrderChange = function () {
                return $scope.select(1), $scope.currentPage = 1
            }, $scope.search = function () {
                return $scope.filteredMembers = $filter("filter")($scope.members, $scope.searchKeywords), $scope.onFilterChange()
            }, $scope.order = function (rowName) {
                return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredMembers = $filter("orderBy")($scope.members, rowName), $scope.onOrderChange()) : void 0
            }, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPageMembers = [], (init = function () {
                return $scope.search(), $scope.select($scope.currentPage)
            })();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
    }
    // $scope.init();
    $scope.loadMemberListData();
    $scope.issueCardView = function (member) {
        $scope.memberViewMode = false;
        $scope.memberEditMode = true;
        $scope.memberId = member.memberId;
        $scope.memberNo = member.memberNo;
        $scope.showCamera=false;
        $scope.capPic=true;
        $scope.showImage=true;
        $scope.surName =member.surName;
        $scope.title=member.title;
        $scope.firstName=member.firstName;
        $scope.otherName = member.otherName;
        $scope.idPassPortNo = member.idPassPortNo;
        if(member.gender=="M"){
      	  $scope.gender="Male"; 
      	  $scope.male=true; 
        }
        else if(member.gender=="F"){
      	  $scope.gender="Female"; 
      	  $scope.feMale=true; 
        }
     
   
        $scope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
        $scope.maritalStatus = member.maritalStatus;
        $scope.weight = member.weight;
        $scope.height = member.height;
        $scope.nhifNo = member.nhifNo;
        $scope.employerName =member.employerName;
        $scope.occupation = member.occupation;
        $scope.nationality = member.nationality;
        $scope.postalAdd = member.postalAdd;
        $scope.physicalAdd = member.physicalAdd;
        $scope.homeTelephone =member.homeTelephone;
        $scope.officeTelephone =member.officeTelephone;
        $scope.cellPhone =member.cellPhone;
        $scope.email =member.email;
        $scope.nokName = member.nokName;
        $scope.relationSelect =member.relationId;
        $scope.relationDesc=member.relationDesc;
        $scope.nokIdPpNo = member.nokIdPpNo;
        $scope.nokTelephoneNo = member.nokTelephoneNo;
        $scope.nokPostalAdd = member.nokPostalAdd;
        $scope.myCroppedImage=member.memberPic;
        $scope.coverSelect=member.coverId;
        $scope.coverName=member.coverName;
        $scope.categorySelect=member.categoryId;
        $scope.categoryName=member.categoryName;
        $scope.fullName=member.fullName;
        $scope.ipLimit=member.ipLimit;
        $scope.opLimit=member.opLimit;
        $scope.cams=0;
        if(member.accType==1){
       	 $scope.accType="Corporate";
       }else{
       	$scope.accType="Retail";
       }
        $scope.oldCardNumber=member.cardNumber;
        $scope.binRange=member.binRange;
        $scope.serialNumber="";
        $scope.pinNumber="";
        $scope.programmeDesc=member.programmeDesc;
        $scope.programmes=member.programmes;
        $scope.imageList=member.imageList;
        $scope.oldSerialNumber=member.cardSerialNo;
        $scope.oldPinNumber=member.cardPin;
        console.log($scope.binRange);
        
    };
    
    $scope.updCardReIssue=function(){
    	$scope.programmeId=$scope.programmes[0].programmeId;
    	$scope.programmeDesc=$scope.programmes[0].programmeDesc;
    	$scope.programmeValue=$scope.programmes[0].programmeValue;
        var memberCard = {};
        	memberCard.cardNumber = $scope.cardNumber;
        	memberCard.customerId=$scope.memberId;
        	memberCard.accType=0;
        	memberCard.binRange=$scope.binRange;
        	memberCard.organizationId= $rootScope.UsrRghts.linkId;
        	memberCard.serialNumber=$scope.serialNumber;
        	memberCard.firstName=$scope.firstName+"  "+$scope.surName;
        	memberCard.surName=$scope.surName;
        	memberCard.programmeDesc=$scope.programmeDesc;
        	memberCard.status="I";
        	memberCard.createdBy=$rootScope.UsrRghts.sessionId;
        	memberCard.pinNumber=$scope.pinNumber;
        	memberCard.programmeId=$scope.programmeId;
        	memberCard.programmeValue=$scope.programmeValue;
        	memberCard.programmes=$scope.programmes
        	memberCard.bioImages=$scope.imageList;
        	membercard.oldCardNo=  $scope.oldCardNumber;
        	membercard.oldSerialNo=$scope.oldSerialNumber;
        	membercard.oldPinNo=$scope.oldPinNumber;
        	memberCard.issueType="R"
        	console.log(memberCard);
        blockUI.start()
        cardSvc.UpdCard(memberCard).success(function (response) {
            
               // logger.logSuccess("Great! The Card Issue information was
				// saved succesfully")
               console.log(response);
               var personalize=response;
               var obj = document.getElementById("cardId");
               var connect = obj.deviceConnected();
               if(connect == "0")
               {
                   /*
					 * cardTrans.createStructures("E23274FB67857983",
					 * "3861463163", "I20/12346/7895", "D39B3516E23274FB00",
					 * "0002", "0010", "2FE2", "000A", "1001", "00EF", "1004",
					 * "0442", "DA", "03", "1005", "170C", "76", "03", "1006",
					 * "00AF", "23", "03", "1007", "0073", "17", "03", "1008",
					 * "00B4", "14", "01", "10F4", "0658", "CB", "01");
					 */
            	   
            	   
                var response= obj.cardPers("6770776585828977",personalize.cardNumber,personalize.serialNumber,"6770776585828977",
                		personalize.fCardNumber,personalize.lCardNumber,personalize.fSerialNumber,
                		personalize.lSerialNumber,personalize.fPesonalFile,personalize.lPersonalFile,
                		personalize.fUpkeepFile,personalize.lUpkeepFile,personalize.rUpkeepFile,personalize.tUpkeepFile,
                		personalize.fTransFile,	personalize.lTransFile,personalize.rTransFile,	personalize.tTransFile,
                		personalize.fCafeFile,personalize.lCafeFile,personalize.rCafeFile,personalize.tCafeFile,
                		personalize.fRunnigCafeFile,personalize.lRunnigCafeFile,personalize.rRunnigCafeFile,personalize.tRunnigCafeFile,
                		personalize.fFeeFile,personalize.lFeeFile,personalize.rFeeFile,personalize.tFeeFile,
                		personalize.fBioFile,personalize.lBioFile,personalize.rBioFile,personalize.tBioFile,
                		personalize.personalString);
                if(response==0){
                	memberCard.cardNumber = personalize.cardNumber;
                	cardReIssueSvc.UpdCardReIssuance(memberCard).success(function (response) {
                		 if(response.respCode==200){                			 
                			
                			 console.log(personalize.programmeString);
                			// for(var k=0;k<personalize.programmeString.length;k++){
                				 
                				 var success= obj.writeWallet("6770776585828977", "1007", "03E8", "32", "03",personalize.programmeString);
                				 obj.readWallet("6770776585828977", "1007", " 03E8", "32", "03");
                				// var success= obj.writeWallet("6770776585828977", "1007", "09F6", "FF", "01",personalize.programmeString[k]);
                			// }
//                			
//                			var fsu = memberCard.bioImages;
//                			var z = '';
//                			for(var t=0;t<4;t++)
//            				{
//                				z = z+fsu[t].image;
//            				}
//                			
                			// if(success==0){
                				//console.log(z)
                				//var result=obj.writeBio(personalize.fBioFile, personalize.rBioFile,z);
                				//console.log(result)
                			//	if(result==0){
                			 if(success==0){
                				logger.logSuccess("Great!Card Personalized successfully")
                				//obj.readWallet("6770776585828977", "1007", "13EC", "FF", "01");
                					//obj.readWallet("6770776585828977", "1007", "09F6", "FF", "01");
                				  $scope.memberEditMode = true;
	                           	  $scope.memberDetailMode = true;
	                          	  $scope.photoUploadMode=true;
	                          	  $scope.memberViewMode=true;
	                          	  $scope.showCamera=false;
                                  $scope.capPic=true;
                                  $scope.showImage=true;
                                  $scope.loadMemberData();
                			}
                			else{
                				 logger.logError("Oh snap! There is a problem with writing programmes on card, please contact the adminstrator.")
                			}
                			
                			
                		 }
                		 else{
                			 logger.logWarning(response.respMessage);
                		 }
                	 })
                	
                
                }else{
                	 logger.logError(response);
                }
                   console.log(response);
               }
               else
            	   {
            	   logger.logError(response);
            	   }
         
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    }
  
}]).factory('cardReIssueSvc', function ($http) {
    var compasCardReIssueSvc = {};

  
    compasCardReIssueSvc.UpdCardReIssue = function (memberCard) {
    	console.log(memberCard);
        return $http({
            url: '/compas/rest/member/updcardreissue',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            data: memberCard
        });
    };
    
    return compasCardReIssueSvc;
}).factory('$cardReIssueValid', function () {
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
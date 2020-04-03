/**
 * Provider Angular Module
 */
'use strict';
angular.module('app.blockcard', []).controller("blockCardsCtrl", ["$scope", "$filter", "blockCardsSvc","activateCardsSvc" ,"$blockCardsValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter, blockCardsSvc,activateCardsSvc, $blockCardsValid, $rootScope, blockUI, logger, $location) {
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
    $scope.cardView = function (member) {
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
        $scope.cardNumber=member.cardNumber;
        $scope.binRange=member.binRange;
        $scope.serialNumber="";
        $scope.pinNumber="";
        $scope.programmeDesc=member.programmeDesc;
        $scope.programmes=member.programmes;
        $scope.imageList=member.imageList;
        $scope.cardBalance=member.cardBalance;
        $scope.cardNumber=member.cardNumber;
        console.log($scope.binRange);
    };
    $scope.cancelInq = function () {
    	  $scope.memberEditMode = true;
     	  $scope.memberDetailMode = true;
    	  $scope.photoUploadMode=true;
    	  $scope.memberViewMode=true;
    	  $scope.showCamera=false;
          $scope.capPic=true;
          $scope.showImage=true;
          $scope.memberId =0;
          $scope.memberNo = "";
          $scope.surName = "";
          $scope.title="";
          $scope.firstName="";
          $scope.otherName = "";
          $scope.idPassPortNo = "";
          $scope.feMale=false;
          $scope.male=false;  
          $scope.mem.dateOfBirth="";
          $scope.maritalStatus = "";
          $scope.weight = "";
          $scope.height = "";
          $scope.nhifNo = "";
          $scope.employerName = "";
          $scope.occupation = "";
          $scope.nationality = "";
          $scope.postalAdd = "";
          $scope.physicalAdd = "";
          $scope.homeTelephone = "";
          $scope.officeTelephone = "";
          $scope.cellPhone = "";
          $scope.email = "";
          $scope.nokName = "";
          $scope.relationSelect = 0;
          $scope.nokIdPpNo = "";
          $scope.nokTelephoneNo = "";
          $scope.nokPostalAdd = "";
          $scope.categorySelect=0;
          $scope.coverSelect=0;
          $scope.ipLimit="";
          $scope.opLimit="";
          $scope.memberPic="";
          $scope.loadMemberListData();
      };
    $scope.blockCard = function (memberId) {
        var member= [];
       
        member.memberId = memberId;
        member.cardStatus = "B";
      
        blockUI.start()
        activateCardsSvc.UpdCardStatus(member).success(function (response) {
            if (response.respCode == 200) {
                logger.logSuccess("Great! Card Blocked succesfully")
                $scope.memberId = 0;
                $scope.cardStatus = "";
                $scope.activateMode = false;
                $scope.loadMemberListData();
            }
           
            else {
                logger.logWarning("Opss! Something went wrong while blocking the card. Please try agiain after sometime")
            }
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };
}]).factory('blockCardsSvc', function ($http) {
    var compasBlockCardsSvc = {};

  
    compasBlockCardsSvc.GetCardToBlock = function (brokerId) {
        return $http({
            url: '/compas/rest/member/gtMemberCb/',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        });
    };
    
    return compasBlockCardsSvc;
}).factory('$blockCardsValid', function () {
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
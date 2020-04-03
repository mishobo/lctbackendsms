/**
 * Member Angular Module
 */
'use strict';
angular.module('app.dependantinquiry', []).controller("dependantInquiryCtrl", ["$scope", "$filter", "memberInquirySvc","memStatementSvc", "$memberValid", "$rootScope", "blockUI", "logger" ,"$location","memberSvc","organizationSvc", function ($scope, $filter, memberInquirySvc,memStatementSvc, $memberValid, $rootScope, blockUI, logger, $location,memberSvc,organizationSvc) {
    var init;
    $scope.bioView = true;
    $scope.memVerify = false;
    $scope.memVerify = true;
    $scope.showMemDetails=true;
    $scope.memberViewMode=true;
    $scope.loadedMember = {};
 
  $scope.loadMemberData = function () {
     
        $scope.members = [], $scope.vouchers = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberInquiryEditMode = false;
        memberSvc.GetDependants().success(function (response) {
            //console.log('inquiry member: ', response);
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
  
  
 
  
   
 
  $scope.onOrgChange=function(orgId){
        if(orgId>0){
             $scope.memberEditMode = false;
             
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
    $scope.loadOrganizationData();
  
  $scope.viewMember = function (memberNo) {
        var member={}
        memberSvc.GetDependants(memberNo).success(function (response) {
	console.log(response);
	member=response
	console.log("Member Type: " +member.memberType);
	//check if member is a principal and decline to send their details
	if (member.memberType === "P"){
		logger.logWarning("Member is a principal");
		$location.path('/masters/member');
	}
	if(response.respCode == 200){
                $scope.memberEditMode = true;
                $scope.memberDetailMode = false;
                $scope.memberViewMode=false;
                $scope.memberId = member.memberId;
                $scope.memberNo = member.memberNo;
                $scope.category = member.programmeDesc;
				$scope.insuranceCode=member.insuranceCode;
				$scope.schemeCode=member.schemeCode;
                $scope.scheme = member.scheme;
                $scope.memberType = member.memberType;
                $scope.memberTypeValue = "N/A";
                if($scope.memberType==="P") { $scope.memberTypeValue = "Principal"; }
                else if($scope.memberType==="D") { $scope.memberTypeValue = "Dependant"; }
                $scope.outpatientStatus = member.outpatientStatus;
                $scope.orgSelect = member.orgId;
                $scope.showCamera=false;
                $scope.capPic=true;
                $scope.showImage=true;
                $scope.surName =member.surName;
                $scope.title=member.title;
                $scope.firstName=member.firstName;
                //$scope.vouchers=[];
                $scope.otherName = member.otherName;
                $scope.idPassPortNo = member.idPassPortNo;
                 
                $scope.programmes=member.programmes;
                 
                for(var i=0; i<(member.programmes).length; i++){
                    console.log(member.programmes[i]);
                    for(var y=0; y<((member.programmes[i].vouchers).length); y++)
                    {
                    //console.log(member.programmes[i].vouchers[y] + "This is the list of vouchers");
                     
                    }
                }
                $scope.vouchers = member.vouchers;
                 
                console.log(JSON.stringify(member.programmes[0].vouchers));
                 
                if(member.gender=="M"){
                    $scope.gender="Male"; 
                    $scope.male=true; 
                }
                else if(member.gender=="F"){
                    $scope.gender="Female"; 
                    $scope.feMale=true; 
                }
                $scope.dateOfBirth=member.dateOfBirth;
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
                $scope.productSelect=member.productId;
                $scope.showProgrammes=false
                $scope.cardNumber=member.cardNumber
                $scope.categories=member.programmes;
                $scope.bioId=member.bioId;
                $scope.active=member.active;
                if($scope.bioId==0){
                    $scope.showEnroll=false;
                }
                else{
                    $scope.showEnroll=true;
                }
                $scope.famMemEntries=member.familyMemList;
                $scope.familySize=member.familySize;
                $scope.isDisabled=true;
                 
                $scope.loadedMember["rec_id"] = member.memberId;
                $scope.loadedMember["scheme_name"] = member.scheme;
                $scope.loadedMember["membership_number"] = member.memberNo;
                const totalNames = member.fullName.split(" ").length;
                if(member.firstName)
                {
                    $scope.loadedMember["first_name"] = member.firstName;
                }
                else if(member.fullName)
                {
                    $scope.loadedMember["first_name"] = member.fullName.split(" ")[0];
                }
                 
                if(member.lastName)
                {
                    $scope.loadedMember["last_name"] = member.lastName;
                }
                else if(member.surName)
                {
                    $scope.loadedMember["last_name"] = member.surName;
                }
                else if(member.fullName)
                {
                    $scope.loadedMember["last_name"] = member.fullName.split(" ")[totalNames-1];
                }
                 
                $scope.loadedMember["middle_name"] = member.otherName;
                //$scope.loadedMember["last_name"] = member.surName;
                $scope.loadedMember["emp_fullname"] = member.fullName;
                $scope.loadedMember["outpatient_status"] = member.outpatientStatus;
                $scope.loadedMember["member_type"] = member.memberType;
                //$scope.loadedMember["insurance_id"] = member.surName;
                //$scope.loadedMember["principal_relationship"] = member.;
				$scope.loadedMember["insurance_id"] = member.insuranceCode;
				$scope.loadedMember["scheme_code"] = member.schemeCode;
                console.log("loadedMember: ", $scope.loadedMember);
                 
             
           }
	else{
	logger.logWarning("Dependant details doesnt  exists");
}
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
        });
         
         
         
    };
 
     
    $scope.cancelMember=function(){
     $scope.memberInquiryEditMode = false;
     $scope.userClassId=classId;
        $scope.memberId = 0;
        $scope.surName = "";
        console.log("cancelled");
        $scope.memVerify = false;        
        $scope.showMemDetails=true;
        
        $scope.isDisabled=false;
   }
     
    $scope.cancelInq=function(){
        $scope.memberInquiryEditMode = false;
        
        $scope.memberViewMode=true;
         
    }
     
    $scope.sendMemberInfo = function(){
        memberInquirySvc.sendInfo($scope.loadedMember).success(function(resp) {
            console.log('sent successfully...',resp);
        }).error(function(data, status, headers, config) { console.log("error: ",data); });
    }
 
 
}]).factory('memberInquirySvc', function ($http) {
    var shpMemberInquirySvc = {};
   
    shpMemberInquirySvc.GetSingleMember = function (memberNo) {
           return $http({
            url: '/compas/rest/member/gtsingleMember?memberNo=' + encodeURIComponent(memberNo),
            method: 'GET'
             
        });
    };
    shpMemberInquirySvc.GetMemberBio = function (bioId) {
       return $http({
        url: '/compas/rest/member/gtbioMember?bioId=' + encodeURIComponent(bioId),
         method: 'GET'
          
     });
     
 };
     shpMemberInquirySvc.sendInfo = function (loadedMember) {
         console.log('posting to AKUH: ', JSON.stringify(loadedMember)); 
         return $http({
//		url: 'http://192.168.13.237:8085/lct/akuh/memberVerification/save_verified_lct_member',
//		url: 'http://192.168.13.173:8085/lct/akuh/memberVerification/save_verified_lct_member',
		url: 'http://41.139.212.137:8085/compas/lct/akuh/memberVerification/save_verified_lct_member',
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
		withCredentials: true,
        data: loadedMember           
      });
    };
 
    return shpMemberInquirySvc;
}).factory('$memberValid', function () {
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
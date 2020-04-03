/**
 * Member Angular Module
 */
'use strict';
angular.module('app.bnfapprovals', []).controller("bnfapprovalsCtrl", ["$scope", "$filter","bnfapprovalSvc", "memberInquirySvc","organizationSvc", "$bnfapprovalsValid", "$rootScope", "blockUI", "logger" ,"$location", function ($scope, $filter, bnfapprovalSvc,memberInquirySvc,organizationSvc, $bnfapprovalsValid, $rootScope, blockUI, logger, $location) {
	var init;
	$scope.bioView = true;
	$scope.memVerify = false;
	$scope.memVerify = true;
	$scope.showMemDetails=true;
	$scope.memberViewMode=true;
	$scope.memberInquiryEditMode = true;
	$scope.allBnfSelected = false;
	$scope.verifySelection=[];
	$scope.selection=[];
	$scope.mem=[];
	$scope.showOrg=false;
	var classId=$rootScope.UsrRghts.userClassId;
	
	$scope.counts=[{count:100},{count:200},{count:300},{count:400},{count:500}]
	$scope.onOrgChange=function(orgId){
		if(orgId>0){
			 $scope.memberEditMode = false;
			//$scope.loadSchemeData(orgId);
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
	
	

	$scope.loadMemberData = function () {
		var member={}
		member.fromDate=$filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
			member.toDate=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
				member.count=$scope.countSelect;
				member.orgId=$scope.orgSelect;
		$scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberInquiryEditMode = false;
		bnfapprovalSvc.GetMembersToVerify(member).success(function (response) {

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

	$scope.previewMembers=function(){
		$scope.loadMemberData ();
	}
	$scope.previewVMembers=function(){
		$scope.loadVMemberData();
	}


	$scope.loadVMemberData = function () {
		var member={}
		member.fromDate=$filter('date')($scope.mem.FromDt,'yyyy-MM-dd');
			member.toDate=$filter('date')($scope.mem.ToDt,'yyyy-MM-dd');
				member.count=$scope.countSelect;
				member.orgId=$scope.orgSelect;
		$scope.vmembers = [], $scope.searchKeywords = "", $scope.filteredvMembers = [], $scope.row = "", $scope.memberInquiryEditMode = false;
		bnfapprovalSvc.GetVerifiedMembers(member).success(function (response) {

			return $scope.vmembers = response, $scope.searchKeywords = "", $scope.filteredvMembers = [], $scope.row = "", $scope.select = function (page) {
				var end, start;
				return start = (page - 1) * $scope.numPerPage, end = start + $scope.numPerPage, $scope.currentPagevMembers = $scope.filteredvMembers.slice(start, end)
			}, $scope.onFilterChange = function () {
				return $scope.select(1), $scope.currentPage = 1, $scope.row = ""
			}, $scope.onNumPerPageChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.onOrderChange = function () {
				return $scope.select(1), $scope.currentPage = 1
			}, $scope.search = function () {
				return $scope.filteredvMembers = $filter("filter")($scope.vmembers, $scope.searchKeywords), $scope.onFilterChange()
			}, $scope.order = function (rowName) {
				return $scope.row !== rowName ? ($scope.row = rowName, $scope.filteredvMembers = $filter("orderBy")($scope.vmembers, rowName), $scope.onOrderChange()) : void 0
			}, $scope.numPerPageOpt = [3, 5, 10, 20], $scope.numPerPage = $scope.numPerPageOpt[2], $scope.currentPage = 1, $scope.currentPagevMembers = [], (init = function () {
				return $scope.search(), $scope.select($scope.currentPage)
			})();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});

	}
	//
	$scope.viewMember = function (member) {
		$scope.showOrg=true;
		$scope.memberInquiryEditMode = true;
		$scope.memberViewMode = false;
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


		$scope.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
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
		$scope.bnfGrpSelect=member.bnfGrpId;
		$scope.showProgrammes=false
		$scope.programmes=member.programmes;
		console.log( $scope.programmes)
		console.log(member.bioId)
		$scope.bioId=member.bioId;
		if($scope.bioId==0){
			$scope.showEnroll=false;
		}
		else{
			$scope.showEnroll=true;
		}
		$scope.famMemEntries=member.familyMemList;
		$scope.familySize=member.familySize;
		$scope.cardNumber=member.cardNumber;
		$scope.cardBalance=member.cardBalance;
	};



	$scope.cancelVerify=function(){
		$scope.memberInquiryEditMode = false;
		$scope.memberViewMode=true;
		$scope.selection=[];

	}
	$scope.approve=function(){
		var bnf = {};
		$scope.famselection=[];
		for(var i=0;i<$scope.vmembers.length;i++){
			if($scope.vmembers[i].isChecked){
				$scope.selection.push({"memberId":
					$scope.vmembers[i].memberId}
				);

			}
			for(var j=0;j<$scope.vmembers[i].familyMemList.length;j++){
				if($scope.vmembers[i].familyMemList[j].isChecked){
					$scope.famselection.push({"familyMemId":
						$scope.vmembers[i].familyMemList[j].familyMemId}
					);
				}
			}
			
		}
		
		bnf.verifySelection=$scope.selection;
		bnf.famVerifySelection=$scope.famselection;
		bnf.bnfStatus="A"
		bnf.createdBy = $rootScope.UsrRghts.sessionId;
		bnf.userName = $rootScope.UsrRghts.sessionName;
		bnf.userEmail=$rootScope.UsrRghts.linkExtInfo;
		if($scope.selection.length > 0){
			blockUI.start()
			bnfapprovalSvc.UpdBnfStatus(bnf).success(function (response) {
				if (response.respCode == 200) {
					logger.logSuccess("Great! The Beneficiary Approved succesfully")
					$scope.memberInquiryEditMode = false;
					$scope.memberViewMode = true;
					$scope.selection=[];
					$scope.loadVMemberData();
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
			logger.logWarning("Error ! You have skipped selecting beneficiary to approve, Please try again");
		}
	}
	$scope.verify=function(){
		var bnf = {};
		$scope.famselection=[];
		for(var i=0;i<$scope.members.length;i++){
			if($scope.members[i].isChecked){
				$scope.selection.push({"memberId":
					$scope.members[i].memberId}
				);

			}
			for(var j=0;j<$scope.members[i].familyMemList.length;j++){
				if($scope.members[i].familyMemList[j].isChecked){
					$scope.famselection.push({"familyMemId":
						$scope.members[i].familyMemList[j].familyMemId}
					);
				}
			}
		}
		bnf.verifySelection=$scope.selection;
		bnf.famVerifySelection=$scope.famselection;
		bnf.bnfStatus="V"
		bnf.createdBy = $rootScope.UsrRghts.sessionId;
		bnf.userName = $rootScope.UsrRghts.sessionName;
		bnf.userEmail=$rootScope.UsrRghts.linkExtInfo;
		if($scope.selection.length >= 0 || $scope.famselection>=0){
			blockUI.start()
			bnfapprovalSvc.UpdBnfStatus(bnf).success(function (response) {
				if (response.respCode == 200) {
					logger.logSuccess("Great! The Member Verified succesfully")
					$scope.memberInquiryEditMode = false;
					$scope.memberViewMode = true;
					$scope.selection=[];
					$scope.loadMemberData ();

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
			logger.logWarning("Error ! You have skipped selecting beneficiary to verify, Please try again");
		}
	}

	$scope.rejectApproval=function(){
		var bnf = {};



		bnf.memberId=$scope.memberId;
		bnf.bnfStatus="RA"

			bnf.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		bnfapprovalSvc.UpdBnfStatus(bnf).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Beneficiary information was saved succesfully")
				$scope.memberInquiryEditMode = false;
				$scope.memberViewMode = true;
				$scope.loadMemberData ();
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
	$scope.rejectVerified=function(){
		var bnf = {};



		bnf.verifySelection=$scope.verifySelection;
		bnf.bnfStatus="RV"

			bnf.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		bnfapprovalSvc.UpdBnfStatus(bnf).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Beneficiary information was saved succesfully")
				$scope.memberInquiryEditMode = false;
				$scope.memberViewMode = true;
				$scope.loadMemberData ();

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

	/**
	 * single record selection for beneficiary
	 * 
	 */
	$scope.selectBeneficiary = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
		$scope.selection=[];

		for (var i = 0; i < $scope.members.length; i++) {
			// var idx = $scope.selection.indexOf(branchId);

			if (index > -1 && $scope.members[i].isChecked) {
				$scope.selection.push({"memberId":
					$scope.members[i].memberId}
				);


			}
			else if(!$scope.members[i].isChecked){

				$scope.selection.splice(i, 1);

			}

			if (!$scope.members[i].isChecked) {
				$scope.allBnfSelected = false;
				return;
			}

		}
		$scope.allBnfSelected = true;

		// If not the check the "allItemsSelected" checkbox

	};

	/**
	 * This executes when checkbox in table header is checked  for selecting all beneficiaries
	 */ 
	$scope.selectAllBeneficiary= function () {
		// Loop through all the entities and set their isChecked property
		$scope.selection=[];
		for (var i = 0; i < $scope.members.length; i++) {
			$scope.members[i].isChecked = $scope.allBnfSelected;
			if ($scope.members[i].isChecked) {
				$scope.selection.push({"memberId":
					$scope.members[i].memberId}
				);


			}
			else if(!$scope.members[i].isChecked){

				$scope.selection.splice(i, 1);

			}


		}
	};
	/**
	 * single record selection for beneficiary
	 * 
	 */
	$scope.selectBnfToApprove = function (index) {
		// If any entity is not checked, then uncheck the "allItemsSelected"
		// checkbox
		$scope.selection=[];

		for (var i = 0; i < $scope.vmembers.length; i++) {
			// var idx = $scope.selection.indexOf(branchId);

			if (index > -1 && $scope.vmembers[i].isChecked) {
				$scope.selection.push({"memberId":
					$scope.vmembers[i].memberId}
				);


			}
			else if(!$scope.vmembers[i].isChecked){

				$scope.selection.splice(i, 1);

			}

			if (!$scope.vmembers[i].isChecked) {
				$scope.allBnfSelected = false;
				return;
			}

		}

		// If not the check the "allItemsSelected" checkbox
		$scope.allBnfSelected = true;
	};

	/**
	 * This executes when checkbox in table header is checked  for selecting all beneficiaries
	 */ 
	$scope.selectAllBnfToApprove= function () {
		// Loop through all the entities and set their isChecked property
		$scope.selection=[];
		for (var i = 0; i < $scope.vmembers.length; i++) {
			$scope.vmembers[i].isChecked = $scope.allBnfSelected;
			if ($scope.vmembers[i].isChecked) {
				$scope.selection.push({"memberId":
					$scope.vmembers[i].memberId}
				);
			}
			else if(!$scope.vmembers[i].isChecked){

				$scope.selection.splice(i, 1);

			}
		}
	};


}]).factory('bnfapprovalSvc', function ($http) {
	var bnfapprovalsSvc = {};

	bnfapprovalsSvc.UpdBnfStatus = function (bnf) {
		return $http({
			url: '/compas/rest/member/updBnfStatus',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: bnf
		});
	};//

	bnfapprovalsSvc.GetVerifiedMembers = function (member) {
		return $http({
			url: '/compas/rest/member/gtVerifiedMembers',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: member
		});
	};
	bnfapprovalsSvc.GetMembersToVerify = function (member) {
		return $http({
			url: '/compas/rest/member/gtMembersToVerify',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: member
		});
	};
	return bnfapprovalsSvc;
}).factory('$bnfapprovalsValid', function () {
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
})
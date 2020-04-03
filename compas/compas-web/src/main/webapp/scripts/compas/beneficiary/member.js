/**
 * Member Angular Module
 */
'use strict';
angular.module('app.member', []).controller("membersCtrl", ["$scope", "$filter", "memberSvc","memberInquirySvc","programmeSvc","organizationSvc", "$memberValid", "$rootScope", "blockUI", "logger" ,"$location", function ($scope, $filter, memberSvc,memberInquirySvc,programmeSvc,organizationSvc, $memberValid, $rootScope, blockUI, logger, $location) {
	var init;
	var today = new Date();
	var dd = today.getDate();
	 var mm = today.getMonth()+1;
	 var yyyy = today.getFullYear();

	 $scope.maxDate = yyyy + '-' + mm + '-' + dd;
	$scope.showSearch=false;
	$scope.members = [];
	$scope.memberNo="";
	$scope.programmes = [];
	$scope.relations = [];
	$scope.bnfGrpSelect="";
	$scope.isParent=true;
	$scope.mem=[];
	$scope.entry = [];
	$scope.famMemEntries = [];
	$scope.selection=[]
	$scope.beneficiaries = [];
	$scope.beneficiaryEditMode = false;
	$scope.allItemsSelected = false;
	$scope.familyMemDetails =[];
	$scope.showFamilyMembers=true;
	$scope.orgSelect=""
	var familyMemId={};
	var style = {'background-color': '#eef8fc', 'color': '#26929c'};
	var styleInputs = {'color': '#26929c'};
	$scope.memberEditMode = true;    $scope.photoUploadMode=true;  $scope.memberDetailMode = true;    $scope.memberViewMode=true; 
	$scope.showProgrammes=true;
	
	
	$scope.loadRelations=function(){
		$scope.relations=[];
		memberSvc.GetRelations().success(function(response){
			$scope.relations=response;
			console.log('relations: ', $scope.relations);
		})
	}
	$scope.loadRelations();
	

	$scope.loadSchemeData = function (orgId) {
		$scope.scheme = [], $scope.searchKeywords = "", $scope.filteredBnfgrps = [], $scope.row = "", $scope.bnfgrpEditMode = false; $scope.previewServices=false;
		memberSvc.GetScheme(orgId).success(function (response) {
			console.log('schemes: ', response);
			return $scope.scheme = response
		})
	}


	$scope.LoadCategoriesByScheme = function (schemeId) {
		$scope.categories=[];
		memberSvc.GetCategoryByScheme(schemeId).success(function (response) {
			$scope.categories = response;
			console.log('categories: ', $scope.categories);
		});
	}


	$scope.onSchemeChange=function(schemeId){
		if(schemeId>0){
			$scope.showProgrammes=false
			$scope.LoadCategoriesByScheme(schemeId);
		}
	}
	
	$scope.$watch("orgSelect", function (newValue, oldValue) {
		if (newValue != oldValue) {
			if(newValue>0){
				$scope.memberEditMode = false;
				$scope.loadSchemeData($scope.orgSelect);
				if($scope.memberDetailMode){
					$scope.showSearch=true;
				}
			}
		}
	});
	$scope.loadOrganizationData = function () {
		$scope.orgs = [];
		organizationSvc.GetOrganizations().success(function (response) {
			return $scope.orgs = response
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadOrganizationData();

	$scope.editMember = function (memberNo) {
		var member={}
		memberSvc.GetMembers(memberNo).success(function (response) {
			member=response;
			console.log('member: ', response);
			$scope.showSearch=false;
			if(!member.memberNo){
				logger.logWarning("Member is not registered.")
				$scope.showSearch=true;
			}
			else if(member.relation != 0){
				logger.logWarning("Member is dependent of "+member.principle)
				$scope.showSearch=true;
			}
			else{
			$scope.memberEditMode = true;
			$scope.isDisabled = true;
			$scope.memberDetailMode = false;
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
			$scope.cardNumber = member.cardNumber;
			$scope.accId = member.accId;
			$scope.accNumber = member.accNumber;
			$scope.gender = member.gender;
			$scope.orgSelect=member.orgId;
			$scope.mem.dateOfBirth=member.dateOfBirth;
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
			$scope.lctCode = member.lctCode;
			$scope.memberType =member.memberType;
			$scope.memberTypeValue = "N/A";
			if($scope.memberType==="P") { $scope.memberTypeValue = "Principal"; }
			else if($scope.memberType==="D") { $scope.memberTypeValue = "Dependant"; }
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
			$scope.categories=member.programmes;
			$scope.bioId=member.bioId;
			$scope.active=member.active;
			$scope.hasSpouse = member.hasSpouse;
			if($scope.bioId==0){
				$scope.showEnroll=false;
			}
			else{
				$scope.showEnroll=true;
			}
			$scope.famMemEntries=member.familyMemList;
			$scope.familySize=member.familySize;
			$scope.isDisabled=true;
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
		
		
		
	};


	$scope.addMember = function () {
		if (!$filter('checkRightToAdd')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
			logger.log("Oh snap! You are not allowed to create new Members.");
			return false;
		}
		if($scope.orgSelect < 1){
			logger.logWarning("Please select Organization")
			$scope.showSearch=true;
		}
		// $scope.loadProgrammeData();
		console.log($scope.maxDate)
		$scope.showSearch=false;
		$scope.allItemsSelected = false;
		$scope.isDisabled=false;
		$scope.photoUploadMode=true;
		$scope.memberEditMode = true;
		$scope.memberDetailMode = false;
		$scope.showCamera=false;
		$scope.capPic=true;
		$scope.showImage=true;
		$scope.memberId =0;
		$scope.memberNo = "";
		$scope.surName = "";
		$scope.title="";
		$scope.firstName="";
		$scope.fullName = "";
		$scope.idPassPortNo = "";
		$scope.gender="";
		$scope.male=false;  
		$scope.fammale=false;
		$scope.famfeMale=false;
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
		$scope.lctCode = "";
		$scope.memberType = "P";
		$scope.active=true;
		$scope.nokName = "";
		$scope.relationSelect = 0;
		$scope.nokIdPpNo = "";
		$scope.nokTelephoneNo = "";
		$scope.nokPostalAdd = "";
		$scope.ipLimit="";
		$scope.opLimit="";
		$scope.categorySelect=0;
		$scope.coverSelect=0;
		// $scope.departmentSelect =0;
		// $scope.familySizeSelect = 0;
		$scope.bioId=0;
		$scope.cams=0;
		// $scope.selected=false;
		// $scope.parentMemberNo="";
		$scope.memberPic="";
		$scope.bnfGrpSelect="";
		$scope.familySize=0;
		$scope.famMemEntries = [];
		$scope.showProgrammes=true
		$scope.programmes=[];

	};

	$scope.cancelMember = function () {
		$scope.showSearch=false;
		$scope.memberEditMode = true;
		$scope.familySize=0;
		$scope.memberEditMode = true;
		$scope.memberDetailMode =true;
		$scope.showFamilyMembers=true;
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
		$scope.gender = ""; 
		$scope.dateOfBirth="";
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
		$scope.lctCode = "";
		$scope.memberType = "P";
		$scope.nokName = "";
		$scope.relationSelect = 0;
		$scope.nokIdPpNo = "";
		$scope.nokTelephoneNo = "";
		$scope.nokPostalAdd = "";
		$scope.categorySelect=0;
		$scope.coverSelect=0;
		$scope.ipLimit="";
		$scope.opLimit="";
		$scope.showProgrammes=true;
		$scope.productSelect="";
		$scope.orgSelect="";
		$scope.active=true;
		$scope.hasSpouse = false;
		// $scope.departmentSelect =0;
		// $scope.familySizeSelect = 0;

		$scope.isDisabled=false;
		// $scope.selected=false;
		// $scope.parentMemberNo="";
		$scope.memberPic="";
		$scope.programmes=[];
		$scope.entry = [];
		$scope.bioId=0;
	}
	$scope.next=function(){
		if (!$memberValid($scope.memberNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Member number. Please try again.")
			return false;
			}
		if (!$memberValid($scope.fullName)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's fullname. Please try again.")
			return false;
		}
		/*
		if (!$memberValid($scope.idPassPortNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's Id/Passport no. Please try again.")
			return false;
		}
		*/
		if (!$memberValid($scope.mem.dateOfBirth)) {
		logger.logWarning("Opss! You may have skipped specifying the Member's Date of Birth. Please try again.")
		return false;
		}
		if (!$memberValid($scope.productSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's Scheme. Please try again.")
			return false;
			}
		if ($scope.email !=null){
			if($scope.email.length>0 && !$scope.email.match(/^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/)) {
			logger.logWarning("Opss! User email is not valid. Please try again.")
			return false;
			}
		}
		if ($scope.cellPhone !=null){
			if($scope.cellPhone.length>0 && !$scope.cellPhone.match(/^\+?\d{10}$/)) {
			logger.logWarning("Opss! Member phone is not valid. Please try again.")
			return false;
			}
		}
		$scope.tempPrgs=[];
		for(var i=0;i<$scope.categories.length;i++){
			if($scope.categories[i].isActive==true){
				$scope.tempPrgs.push($scope.categories[i]);
			}
		}
		if ($scope.tempPrgs.length<=0) {
			logger.logWarning("Opss! You may have skipped specifying the cover category. Please try again.")
			return false;
		}
		if($scope.memberId==0){
			$scope.memberEditMode = true;
			$scope.memberDetailMode = true;
			//$scope.photoUploadMode=false;
		}
		else{
			$scope.memberEditMode = true;
			$scope.memberDetailMode = true;
			//$scope.photoUploadMode=false;
			$scope.capPic=false;
			$scope.showImage=true;
			$scope.showCamera=false;
		}
		$scope.showFamilyMembers=false;
	}

	$scope.back=function(){
		$scope.memberDetailMode = false;
		$scope.photoUploadMode=true;
	}
	
	$scope.selectProgramme = function(index){
		for(var i=0;i<$scope.categories.length;i++){
			$scope.categories[i].isActive=false
			}
		$scope.categories[index].isActive = true;
	}

	$scope.addEntry=function(){
		$scope.tempBeneficiaryId=""
			if(!$memberValid($scope.entry.famMemFullName)){
				logger.logWarning("Opss! You may have skipped specifying the full name. Please try again.")
				return false;
			};
			if(!$memberValid($scope.entry.famMemberNo)){
				logger.logWarning("Opss! You may have skipped specifying the member number. Please try again.")
				return false;
			};
			if(!$memberValid($scope.entry.famDob)){
				logger.logWarning("Opss! You may have skipped specifying the date of birth. Please try again.")
				return false;
			};
			if(!$memberValid($scope.entry.relation)){
				logger.logWarning("Opss! You may have skipped specifying the relation. Please try again.")
				return false;
			};
			if(!$memberValid($scope.entry.gender)){
				logger.logWarning("Opss! You may have skipped specifying the gender. Please try again.")
				return false;
			};
			if(!$memberValid($scope.entry.lctCode)){
				logger.logWarning("Opss! You may have skipped specifying member LCT type. Please try again.")
				return false;
			};
			if(!$memberValid($scope.entry.memberType)){
				logger.logWarning("Opss! You may have skipped specifying member type. Please try again.")
				return false;
			};
			if($scope.famMemEntries.length >= 5){
				logger.logWarning("Opss! You are not allowed to add more than 5 members.")
				return false;
			};
			if($scope.entry.famMemberNo == $scope.memberNo) {
				logger.logWarning("Opss! Dependency member No cannot be the same as principle member no")
				return false;
				}
			for(let i=0; i < $scope.famMemEntries.length; i++){
				if($scope.famMemEntries[i].famMemberNo == $scope.entry.famMemberNo){
					logger.logWarning("Opss! Dependency member No already exists")
					return false;
				}
			}
			$scope.famMemEntries.push({
				"famMemFullName":$scope.entry.famMemFullName,
				"famMemberNo":$scope.entry.famMemberNo,
				"relationId":$scope.entry.relation,
//				"relationDesc": $filter('getRelationById')($scope.relations, $scope.entry.relationId).relationDesc,
				"familyMemPic":"",
				"famMemNationalId":$scope.entry.famMemNationalId,
				"tempFamilyMemId":Math.floor(Math.random()*89999+10000),
				"famDob": $filter('date')($scope.entry.famDob,'yyyy-MM-dd'),
				"famGender":$scope.entry.gender,
				"lctCode": $scope.entry.lctCode,
				"memberType": $scope.entry.memberType,
				"idNumber": $scope.entry.idNumber,
				"editMode": false,
				"iconStyle": "glyphicon glyphicon-pencil",
				"cancelled": false,
				"isNew": true
			});
			if($scope.entry.relation == '1')
				$scope.hasSpouse = true;
		$scope.entry=[];
	 console.log($scope.famMemEntries)
	}
	$scope.removeEntry = function(index){				
		if($scope.famMemEntries[index].relationId == '1')
			$scope.hasSpouse = false;
		$scope.famMemEntries.splice( index, 1 );	
	};
	
	$scope.$watch("entry.gender", function (newValue, oldValue) {
		if (newValue != oldValue) {
			$scope.entry.relation = '';
		}
	});

	$scope.updateMember = function () {
		var member = {};
		$scope.photoUploadMode=true;
		if (!$memberValid($scope.fullName)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's fullname. Please try again.")
			return false;
		}
		/*
		if (!$memberValid($scope.idPassPortNo)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's Id/Passport no. Please try again.")
			return false;
		}
		*/
		if (!$memberValid($scope.mem.dateOfBirth)) {
		logger.logWarning("Opss! You may have skipped specifying the Member's Date of Birth. Please try again.")
		return false;
		}
		if (!$memberValid($scope.productSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's Scheme. Please try again.")
			return false;
			}
		if ($scope.email !=null){
			if($scope.email.length>0 && !$scope.email.match(/^[\w-]+(\.[\w-]+)*@([a-z0-9-]+(\.[a-z0-9-]+)*?\.[a-z]{2,6}|(\d{1,3}\.){3}\d{1,3})(:\d{4})?$/)) {
			logger.logWarning("Opss! User email is not valid. Please try again.")
			return false;
			}
		}
		if ($scope.cellPhone !=null){
			if($scope.cellPhone.length>0 && !$scope.cellPhone.match(/^\+?\d{10}$/)) {
			logger.logWarning("Opss! Member phone is not valid. Please try again.")
			return false;
			}
		}
		$scope.tempPrgs=[];
		for(var i=0;i<$scope.categories.length;i++){
			if($scope.categories[i].isActive==true){
				$scope.tempPrgs.push($scope.categories[i]);
			}
		}
		if ($scope.tempPrgs.length<=0) {
			logger.logWarning("Opss! You may have skipped specifying the programme. Please try again.")
			return false;
		}
		if (!$memberValid($scope.memberId)){
			member.memberId = 0;
		}
		else {
			//member.tempPrgs=$scope.tempPrgs;
			member.memberId=$scope.memberId ;
		member.memberNo=$scope.memberNo;
		member.surName= $scope.lastName;
		member.title=  $scope.title;
		member.firstName=   $scope.firstName;
		member.fullName= $scope.fullName;
		member.idPassPortNo=$scope.idPassPortNo;
		member.gender=$scope.gender;
		var Dt= $filter('date')($scope.mem.dateOfBirth,'yyyy-MM-dd');
		console.log(Dt)
		member.dateOfBirth=Dt;
		member.maritalStatus=$scope.maritalStatus;
		member.weight=$scope.weight;
		member.height=$scope.height;
		member.nhifNo=$scope.nhifNo;
		member.employerName=$scope.employerName;
		member.occupation=$scope.occupation;
		member.nationality=$scope.nationality;
		member.postalAdd=$scope.postalAdd;
		member.physicalAdd=$scope.physicalAdd;
		member.homeTelephone=$scope.homeTelephone;
		member.officeTelephone=$scope.officeTelephone;
		member.cellPhone=$scope.cellPhone;
		member.email=$scope.email;
		member.lctCode = $scope.lctCode;
		member.memberType = $scope.memberType;
		member.cardNumber = $scope.cardNumber;
		member.accId = $scope.accId;
		member.accNumber = $scope.accNumber;
		member.memberPic=  $scope.myCroppedImage;
		member.createdBy = $rootScope.UsrRghts.sessionId;
		member.programmes=$scope.categories;
		member.familyMemList=$scope.famMemEntries;
		console.log('member update: ', member);
		}
		if($scope.familySize==0){
			member.familySize= 1;
		}else{
			member.familySize= $scope.familySize;
		}
	
		member.productId=$scope.productSelect;
		member.orgId=$scope.orgSelect;
		member.active = $scope.active;
		member.outpatientStatus = $scope.outpatientStatus;
		member.memType='B';
		blockUI.start()
		console.log(member)
		memberSvc.UpdMember(member).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Member's information was saved succesfully")
				$scope.showSearch=false;
				$scope.memberEditMode = false;
				$scope.memberDetailMode =true;
				$scope.showFamilyMembers=true;
				$scope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
				if( $scope.male==true){
					$scope.gender="Male"; 
					// $scope.male=true;
				}
				else if( $scope.feMale==true){
					$scope.gender="Female"; 
				}
				$scope.allItemsSelected = false;
				$scope.famMemEntries=[];
				$scope.isDisabled=false;
				$scope.memberNo="";
				$scope.orgSelect = "";
				$scope.hasSpouse = false;
			}
			else  {
				logger.logWarning(response.respMessage);
				$scope.memberDetailMode = false;
				$scope.photoUploadMode=true;
				$scope.showFamilyMembers=true;
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};


}]).factory('memberSvc', function ($http) {
	var compasMemberSvc = {};
	compasMemberSvc.GetMembers = function (memberNo) {
	        return $http({
	            url: '/compas/rest/member/gtMembers?memberNo=' + encodeURIComponent(memberNo),
	            method: 'GET'
	            
	        });
	    };
	    
	    //dependant inquiry
		compasMemberSvc.GetDependants = function (memberNo) {
	        return $http({
	            url: '/compas/rest/member/gtDependants?memberNo=' + encodeURIComponent(memberNo),
	            method: 'GET'  
	        });
	    };
	compasMemberSvc.UpdMember = function (member) {
		console.log(member);
		return $http({
			url: '/compas/rest/member/updCustomer',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: member
		});
	};
	
	compasMemberSvc.GetCategoryByScheme = function (schemeId) {
		return $http({
			url: '/compas/rest/member/gtCategoryByScheme/'+schemeId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' }
		});
	};
	compasMemberSvc.GetRelations = function () {
		return $http({
			url: '/compas/rest/member/gtRelations',
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});
	};
	compasMemberSvc.GetScheme = function (orgId) {
		return $http({
			url: '/compas/rest/member/gtSchemes/'+orgId,
			method: 'GET',
			headers: { 'Content-Type': 'application/json' },
		});
	};
	return compasMemberSvc;
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
}).filter('getRelationById', function () {
	return function (input, id) {
		var i = 0, len = input.length;
		for (; i < len; i++) {
			if (+input[i].relationId == +id) {
				return input[i];
			}
		}
		return null;
	}
});

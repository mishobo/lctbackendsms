'use strict';;
angular.module('app.card', []).controller("cardCtrl", ["$scope", "$filter", "cardSvc","$rootScope", "blockUI",
                                                       "logger" ,"$location","memberSvc","$memberValid", function ($scope, $filter,cardSvc, $rootScope, blockUI, logger, $location,memberSvc,$memberValid) {
	var init;
	$scope.categories = [];
	$scope.members = [];
	$scope.departments = [];
	$scope.familySizes = [];
	$scope.relations = [];
	$scope.data={};
	$scope.covers = [];
	$scope.categories = [];
	$scope.isParent=true;
	$scope.mem=[];
	$scope.donutChart2=[];
	$scope.data=[];
	$scope.fingerPrint="";
	$scope.showTapCard=true;
	$scope.imageList=[];

	$scope.loadCardList = function () {
		$scope.cardList = [];
		cardSvc.GetCardList().success(function (response) {
			$scope.cardList =[];
			$scope.cardNo= void 0;
			for (var i = 0; i <= response.length - 1; i++) {
				$scope.cardList.push(response[i].cardNumber);
			}
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
		});
	}
	$scope.loadCardList();

	$scope.loadMemberData = function () {    	
		$scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberViewMode = false , $scope.memberEditMode = false;    $scope.photoUploadMode=true;  $scope.memberDetailMode = true;    $scope.memberViewMode=true;
		cardSvc.GetCardIss($rootScope.UsrRghts.linkId).success(function (response) {
			console.log(response)
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
	$scope.loadMemberData();
	$scope.loadCustomerData = function () {    	
		$scope.members = [], $scope.searchKeywords = "", $scope.filteredMembers = [], $scope.row = "", $scope.memberViewMode = false , $scope.memberEditMode = false;    $scope.photoUploadMode=true;  $scope.memberDetailMode = true;    $scope.memberViewMode=true;
		cardSvc.GetCustCardIss($rootScope.UsrRghts.linkId).success(function (response) {
			console.log(response)
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

	if($rootScope.UsrRghts.sessionName=="Padmin"){
		$scope.loadCustomerData();
	}else{
		$scope.loadMemberData();
	}
	//
	if($rootScope.UsrRghts.userClassId==1){
		$scope.loadMemberData();
		$scope.memberViewMode = true;
		console.log( $scope.memberEditMode)
	}



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
		$scope.cardNumber=member.cardNumber;
		$scope.binRange=member.binRange;
		$scope.serialNumber="";
		$scope.pinNumber="";
		$scope.programmeDesc=member.programmeDesc;
		$scope.programmes=member.programmes;
		$scope.imageList=member.imageList;
		$scope.bioImage=member.bioImage;
		console.log($scope.bioImages);
	};

	$scope.cancelMember = function () {
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
		if($rootScope.UsrRghts.sessionName=="Padmin"){
			$scope.loadCustomerData();
		}else{
			$scope.loadMemberData();
		}
		$scope.showTapCard=true;
	};

	$scope.printCard = function () {
		var member = [];
		$scope.memberEditMode = true;
		$scope.memberViewMode=false;
		$scope.photoUploadMode=true;        
		if (!$memberValid($scope.surName)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's surname. Please try again.")
			return false;
		}
		if (!$memberValid($scope.firstName)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's firstname. Please try again.")
			return false;
		}
		if (!$memberValid($scope.mem.dateOfBirth)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's date of birth. Please try again.")
			return false;
		}
		if (!$memberValid($scope.relationSelect)) {
			logger.logWarning("Opss! You may have skipped specifying the Member's relation. Please try again.")
			return false;
		}
		if (!$memberValid($scope.categorySelect)) {
			logger.logWarning("Opss! You may have skipped specifying the category. Please try again.")
			return false;
		}

		if (!$memberValid($scope.memberId))
			member.memberId = 0;
		else
			member.memberId=$scope.memberId ;
		member.memberNo="JB/01/"+$scope.idPassPortNo;
		member.surName= $scope.surName;
		member.title=  $scope.title;
		member.firstName=   $scope.firstName;
		member.otherName= $scope.otherName;
		member.idPassPortNo=$scope.idPassPortNo;
		if($scope.male==true){
			member.gender="M";
		}
		else if($scope.feMale==true){
			member.gender="F";
		}
		var Dt= $filter('date')($scope.mem.dateOfBirth,'MM-dd-yyyy');
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
		member.nokName=$scope.nokName;
		member.relationId= $scope.relationSelect;
		member.nokIdPpNo=$scope.nokIdPpNo;
		member.nokTelephoneNo=$scope.nokTelephoneNo;
		member.nokPostalAdd=$scope.nokPostalAdd;
		member.categoryId= $scope.categorySelect;
		member.memberPic=  $scope.myCroppedImage;
		member.createdBy = $rootScope.UsrRghts.sessionId;
		blockUI.start()
		cardSvc.printCard(member).success(function (response) {
			if (response.respCode == 200) {
				logger.logSuccess("Great! The Member Card Printed successfully")
				$scope.mem.dateOfBirth=$filter('date')(member.dateOfBirth,'MM-dd-yyyy');;
				if( $scope.male==true){
					$scope.gender="Male"; 
				}
				else if( $scope.feMale==true){
					$scope.gender="Female"; 
				}
				$scope.memberEditMode = true;
				$scope.memberViewMode=false;
			}
			else if (response.respCode == 201) {
				logger.logWarning("Opss! The Member name specified already exists. Please try again")
			}
			else if (response.respCode == 202) {
				logger.logWarning("Opss! "+response.respMessage);
			}
			else {
				logger.logWarning("Opss! Something went wrong while updating the Member. Please try again after sometime")
			}
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
	};
	$scope.formatCard=function(){
		var format={};
		format="5?NA"
			var response={};
		if ("WebSocket" in window) {
			console.log("WebSocket is supported by your Browser!");

			// Let us open a web socket
			//var ws = new WebSocket("ws://localhost:9998/echo");
			var ws = new WebSocket("ws://localhost:8200/");

			ws.onopen = function() {
				// Web Socket is connected, send data using send()
				console.log("Message to send");
				ws.send(format);
				console.log("Message is sent...");
			};

			ws.onmessage = function(evt) {
				response = JSON.parse(evt.data);
				console.log("Response##"+response);
				if (response == "0") {
					logger.logSuccess("Great!Card Formatted successfully")
				}else{
					logger.logError("Oh snap!Failed to format card");
				}
				console.log("Message is received...");
				//console.log(received_msg);
			};

			ws.onclose = function() {
				// websocket is closed.
				console.log("Connection is closed...");				  
			};
		} else {
			// The browser doesn't support WebSocket
			console.log("WebSocket NOT supported by your Browser!");
		}

	}


	$scope.updCard = function () {
		//$scope.programmeId=$scope.programmes[0].programmeId;
		//$scope.programmeDesc=$scope.programmes[0].programmeDesc;
		//$scope.programmeValue=$scope.programmes[0].programmeValue;
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
		console.log("##bioimages##"+memberCard.bioImages)
		memberCard.issueType="I"
			console.log(memberCard);
		blockUI.start();


		cardSvc.UpdCard(memberCard).success(function (response) {
			console.log(response);
			var personalize=response;
			var cardrequest={};
			var cardresponse={}
			cardrequest="2?NA";
			if ("WebSocket" in window) {
				console.log("WebSocket is supported by your Browser!");

				// Let us open a web socket
				//var ws = new WebSocket("ws://localhost:9998/echo");
				var ws = new WebSocket("ws://localhost:8200/");

				ws.onopen = function() {
					// Web Socket is connected, send data using send()
					console.log("Message to send");
					ws.send(cardrequest);
					console.log("Message is sent...");
				};

				ws.onmessage = function(evt) {
					cardresponse = JSON.parse(evt.data);
					console.log("Response##"+cardresponse);
					if (cardresponse == "0") {
						var cardPerse="6770776585828977"+","+personalize.cardNumber+","+personalize.serialNumber+","+6770776585828977
						+","+personalize.fCardNumber+","+personalize.lCardNumber+","+personalize.fSerialNumber+","+personalize.lSerialNumber+","+personalize.fPesonalFile+","+personalize.lPersonalFile+","+
						personalize.fUpkeepFile+","+personalize.lUpkeepFile+","+personalize.rUpkeepFile+","+personalize.tUpkeepFile+","+
						personalize.fTransFile+","+personalize.lTransFile+","+personalize.rTransFile+","+personalize.tTransFile+","+
						personalize.fCafeFile+","+personalize.lCafeFile+","+personalize.rCafeFile+","+personalize.tCafeFile+","+
						personalize.fRunnigCafeFile+","+personalize.lRunnigCafeFile+","+personalize.rRunnigCafeFile+","+personalize.tRunnigCafeFile+","+
						personalize.fFeeFile+","+personalize.lFeeFile+","+personalize.rFeeFile+","+personalize.tFeeFile+","+
						personalize.fBioFile+","+personalize.lBioFile+","+personalize.rBioFile+","+personalize.tBioFile+","+
						personalize.personalString;
						var persrequest={};
						persrequest="3?"+cardPerse;
						if ("WebSocket" in window) {
							console.log("WebSocket is supported by your Browser!");

							// Let us open a web socket
							//var ws = new WebSocket("ws://localhost:9998/echo");
							var ws = new WebSocket("ws://localhost:8200/");
							ws.onopen = function() {
								// Web Socket is connected, send data using send()
								console.log("Pers to send");
								ws.send(persrequest);
								console.log("Pers is sent...");
							};
							ws.onmessage = function(evt) {
								cardresponse = JSON.parse(evt.data);
								console.log("Response##"+cardresponse);
								if (cardresponse == "0") {
									if($scope.bioImage.length>0){
										var biorequest={};
										biorequest="4?"+personalize.fBioFile+"|"+personalize.rBioFile+"|"+$scope.bioImage;
										if ("WebSocket" in window) {
											console.log("WebSocket is supported by your Browser!");

											// Let us open a web socket
											//var ws = new WebSocket("ws://localhost:9998/echo");
											var ws = new WebSocket("ws://localhost:8200/");
											ws.onopen = function() {
												// Web Socket is connected, send data using send()
												console.log("Bio to send");

												ws.send(biorequest);


												console.log("Bio is sent...");
											};
											ws.onmessage = function(evt) {
												cardresponse = JSON.parse(evt.data);
												console.log("Response##"+cardresponse);
												if (cardresponse == "0") {
													memberCard.cardNumber = personalize.cardNumber;
													cardSvc.UpdCardIssuance(memberCard).success(function (response) {
														if(response.respCode==200){
															logger.logSuccess("Great!Card Personalized successfully")
															$scope.memberEditMode = true;
															$scope.memberDetailMode = true;
															$scope.photoUploadMode=true;
															$scope.memberViewMode=true;
															$scope.showCamera=false;
															$scope.capPic=true;
															$scope.showImage=true;
															if($rootScope.UsrRghts.sessionName=="Padmin"){
																$scope.loadCustomerData();
															}else{
																$scope.loadMemberData();
															}
														}else{
															logger.logError("Oh snap!Failed to Personalized card");
														}     
													}).error(function (data, status, headers, config) {
														logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
														blockUI.stop();
													});
												}
											}
											ws.onclose = function() {
												// websocket is closed.
												console.log("Connection is closed...");				  
											};
										}
									}
									else{
										memberCard.cardNumber = personalize.cardNumber;
										cardSvc.UpdCardIssuance(memberCard).success(function (response) {
											if(response.respCode==200){
												logger.logSuccess("Great!Card Personalized successfully")
												$scope.memberEditMode = true;
												$scope.memberDetailMode = true;
												$scope.photoUploadMode=true;
												$scope.memberViewMode=true;
												$scope.showCamera=false;
												$scope.capPic=true;
												$scope.showImage=true;
												if($rootScope.UsrRghts.sessionName=="Padmin"){
													$scope.loadCustomerData();
												}else{
													$scope.loadMemberData();
												}
											}else{
												logger.logError("Oh snap!Failed to Personalized card");
											}     
										}).error(function (data, status, headers, config) {
											logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
											blockUI.stop();
										});
									}
								}

							}
						}
						ws.onclose = function() {
							// websocket is closed.
							console.log("Connection is closed...");				  
						};
					}
					else {
						logger.logError('No Device detected!!');
					}
					console.log("Message is received...");
					//console.log(received_msg);
				};

				ws.onclose = function() {
					// websocket is closed.
					//console.log("Connection is closed...");				  
				};
			} else {
				// The browser doesn't support WebSocket
				console.log("WebSocket NOT supported by your Browser!");
			}
//logger


//			var cardInitRequest ={};
//			var socketRequest ={};
//			cardInitRequest.request = "INIT#";
//			cardInitRequest.requestType = "INIT";

//			socketRequest.requestType = "CARD";
//			socketRequest.request = cardInitRequest;
//			blockUI.start();
//			cardSvc.OnCardDeviceInit(socketRequest).success(function (response) {
//			console.log(response);
//			if (response == "0") {
//			// write personal file on the card
//			var cardPerse="6770776585828977"+","+personalize.cardNumber+","+personalize.serialNumber+","+6770776585828977
//			+","+personalize.fCardNumber+","+personalize.lCardNumber+","+personalize.fSerialNumber+","+personalize.lSerialNumber+","+personalize.fPesonalFile+","+personalize.lPersonalFile+","+
//			personalize.fUpkeepFile+","+personalize.lUpkeepFile+","+personalize.rUpkeepFile+","+personalize.tUpkeepFile+","+
//			personalize.fTransFile+","+personalize.lTransFile+","+personalize.rTransFile+","+personalize.tTransFile+","+
//			personalize.fCafeFile+","+personalize.lCafeFile+","+personalize.rCafeFile+","+personalize.tCafeFile+","+
//			personalize.fRunnigCafeFile+","+personalize.lRunnigCafeFile+","+personalize.rRunnigCafeFile+","+personalize.tRunnigCafeFile+","+
//			personalize.fFeeFile+","+personalize.lFeeFile+","+personalize.rFeeFile+","+personalize.tFeeFile+","+
//			personalize.fBioFile+","+personalize.lBioFile+","+personalize.rBioFile+","+personalize.tBioFile+","+
//			personalize.personalString;
//			var perRequest={};
//			var socketRequest ={};
//			perRequest.request = "PERSONALIZE#"+cardPerse;
//			perRequest.requestType = "PERSONALIZE";
//			socketRequest.requestType = "CARD";
//			socketRequest.request = perRequest;
//			cardSvc.OnCardDeviceInit(socketRequest).success(function (response) {
//			if (response == "0") {
//			// write fingerprint on the card
//			console.log("bioimages")
//			console.log($scope.bioImage)
//			var socketRequest ={};
//			var bioRequest={};
//			bioRequest.request = "WRITEBIO#"+personalize.fBioFile+","+personalize.rBioFile+","+$scope.bioImage;
//			bioRequest.requestType = "WRITEBIO";

//			socketRequest.requestType = "CARD";
//			socketRequest.request = bioRequest;
//			// console.log(z)
//			cardSvc.OnCardDeviceInit(socketRequest).success(function (response) {
//			if (response == "0") {
//			// insert in card issuance table
//			memberCard.cardNumber = personalize.cardNumber;
//			cardSvc.UpdCardIssuance(memberCard).success(function (response) {
//			if(response.respCode==200){
//			logger.logSuccess("Great!Card Personalized successfully")
//			$scope.memberEditMode = true;
//			$scope.memberDetailMode = true;
//			$scope.photoUploadMode=true;
//			$scope.memberViewMode=true;
//			$scope.showCamera=false;
//			$scope.capPic=true;
//			$scope.showImage=true;
//			$scope.loadMemberData();
//			}else{
//			logger.logError("Oh snap!Failed to Personalized card");
//			}     
//			}).error(function (data, status, headers, config) {
//			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
//			blockUI.stop();
//			});
//			}else{

//			}
//			}).error(function (data, status, headers, config) {
//			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
//			blockUI.stop();
//			});
//			}else{
//			logger.logError("Oh snap!Failed to Personalized card");
//			}
//			}).error(function (data, status, headers, config) {
//			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
//			blockUI.stop();
//			});


//			}
//			else {
//			logger.logWarning("Oops! Error initializing device. Please try again after sometime")
//			}
//			blockUI.stop();
//			}).error(function (data, status, headers, config) {
//			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
//			blockUI.stop();
//			});


//			var obj = document.getElementById("cardId");
//			var connect = obj.deviceConnected();
//			if(connect == "0")
//			{
//			/*
//			* cardTrans.createStructures("E23274FB67857983",
//			* "3861463163", "I20/12346/7895", "D39B3516E23274FB00",
//			* "0002", "0010", "2FE2", "000A", "1001", "00EF", "1004",
//			* "0442", "DA", "03", "1005", "170C", "76", "03", "1006",
//			* "00AF", "23", "03", "1007", "0073", "17", "03", "1008",
//			* "00B4", "14", "01", "10F4", "0658", "CB", "01");
//			*/


//			var response=
//			obj.cardPers("6770776585828977",personalize.cardNumber,personalize.serialNumber,"6770776585828977",
//			personalize.fCardNumber,personalize.lCardNumber,personalize.fSerialNumber,
//			personalize.lSerialNumber,personalize.fPesonalFile,personalize.lPersonalFile,
//			personalize.fUpkeepFile,personalize.lUpkeepFile,personalize.rUpkeepFile,personalize.tUpkeepFile,
//			personalize.fTransFile, personalize.lTransFile,personalize.rTransFile,
//			personalize.tTransFile,
//			personalize.fCafeFile,personalize.lCafeFile,personalize.rCafeFile,personalize.tCafeFile,
//			personalize.fRunnigCafeFile,personalize.lRunnigCafeFile,personalize.rRunnigCafeFile,personalize.tRunnigCafeFile,
//			personalize.fFeeFile,personalize.lFeeFile,personalize.rFeeFile,personalize.tFeeFile,
//			personalize.fBioFile,personalize.lBioFile,personalize.rBioFile,personalize.tBioFile,
//			personalize.personalString);
//			if(response==0){
//			memberCard.cardNumber = personalize.cardNumber;
//			cardSvc.UpdCardIssuance(memberCard).success(function (response) {
//			if(response.respCode==200){

//			console.log(personalize.programmeString);
//			// for(var k=0;k<personalize.programmeString.length;k++){

//			var success= obj.writeWallet("6770776585828977", "1007", "03E8", "32",
//			"01",personalize.programmeString);
//			obj.readWallet("6770776585828977", "1007", " 03E8", "32", "01");
//			// var success= obj.writeWallet("6770776585828977", "1007", "09F6", "FF",
//			"01",personalize.programmeString[k]);
//			// }

//			var fsu = memberCard.bioImages;
//			console.log(fsu)
//			var z = '';
//			for(var t=0;t<=4;t++)
//			{
//			z = z+fsu[t].image;
//			}

//			if(success==0){
//			console.log(z)
//			var result=obj.writeBio(personalize.fBioFile, personalize.rBioFile,z);
//			}
//			//console.log(result)
//			// if(result==0){
//			if(success==0){
//			logger.logSuccess("Great!Card Personalized successfully")
//			//obj.readWallet("6770776585828977", "1007", "13EC", "FF", "01");
//			//obj.readWallet("6770776585828977", "1007", "09F6", "FF", "01");
//			$scope.memberEditMode = true;
//			$scope.memberDetailMode = true;
//			$scope.photoUploadMode=true;
//			$scope.memberViewMode=true;
//			$scope.showCamera=false;
//			$scope.capPic=true;
//			$scope.showImage=true;
//			$scope.loadMemberData();
//			}
//			else{
//			logger.logError("Oh snap! There is a problem with writing programmes on card,
//			please contact the adminstrator.")
//			}


//			}
//			else{
//			logger.logWarning(response.respMessage);
//			}
//			})


//			}else{
//			logger.logError(response);
//			}
//			console.log(response);
//			}
//			else
//			{
//			logger.logError(response);
//			}
			

	
			blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
			blockUI.stop();
		});
		
		if($scope.showTapCard==false){
			var memberCard = {};
			memberCard.cardNumber = $scope.cardNumber;
			memberCard.customerId=$scope.memberId;
			memberCard.accType=0;
			memberCard.binRange=$scope.binRange;
			memberCard.organizationId= $rootScope.UsrRghts.linkId;
			memberCard.serialNumber="1234567890";
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
			cardSvc.UpdCardIssuance(memberCard).success(function (response) {
				if(response.respCode==200){
					logger.logSuccess("Great!Card Personalized successfully")
					$scope.memberEditMode = true;
					$scope.memberDetailMode = true;
					$scope.photoUploadMode=true;
					$scope.memberViewMode=true;
					$scope.showCamera=false;
					$scope.capPic=true;
					$scope.showImage=true;
					$scope.showTapCard=true;
					$scope.loadMemberData();
					
				}else{
					logger.logError("Oh snap!Failed to Personalized card");
				}     
			}).error(function (data, status, headers, config) {
				logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
				blockUI.stop();
			});
		}
	};
	
	$scope.tapCard=function(){
		$scope.showTapCard=false;
	}

	
}]).factory('cardSvc', function ($http) {
	var compasCardSvc = {};
	compasCardSvc.GetCardIss = function (brokerId) {    	
		return $http({
			url: '/compas/rest/member/gtMemberCi/',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasCardSvc.GetCustCardIss = function (brokerId) {    	
		return $http({
			url: '/compas/rest/member/gtCustomerCi/',
			method: 'GET',
			headers: {'Content-Type': 'application/json'}
		});
	};
	compasCardSvc.GetSingleMember = function (memberNo) {
		return $http({
			url: '/compas/rest/member/gtsingleMember?memberNo=' + encodeURIComponent(memberNo),
			method: 'GET'            
		});
	};
	compasCardSvc.GetMemberBio = function (memberNo) {
		return $http({
			url: '/compas/rest/member/gtbioMember?memberNo=' + encodeURIComponent(memberNo),
			method: 'GET'         
		}); 	
	};
	compasCardSvc.GetCardList = function () {
		return $http({
			url: '/compas/rest/member/getunissuedcards',
			method: 'GET'         
		}); 	
	};

	compasCardSvc.UpdCard = function (memberCard) {
		console.log(memberCard);
		return $http({
			url: '/compas/rest/member/updatecardlinkid',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: memberCard
		});
	};
	compasCardSvc.UpdCardIssuance = function (memberCard) {
		console.log(memberCard);
		return $http({
			url: '/compas/rest/member/insertCardIssuance',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: memberCard
		});
	};
	compasCardSvc.VerifyCustomerEnroll = function (bioData) {
		// console.log(bioImage);
		return $http({
			url: '/compas/rest/member/verifyCustomerEnr',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data:bioData

		});
	};

	compasCardSvc.OnCardDeviceInit = function (socket) {
		return $http({
			url: '/compas/rest/member/socket_ops',
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			data: socket
		});
	};
	return compasCardSvc;
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
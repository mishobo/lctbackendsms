'use strict';
var totalCount = 0;
var lCount = 0;
var rCount = 0;
var lCounter = 0;
var rCounter = 0;
var cancelRefresh;

angular.module('app.EnrollController', []).controller('EnrollCtrl', function($scope, logger,$interval,$location,blockUI,enrollUserSvc,enrollMemSvc,memberSvc) {
	$scope.imageList=[];
	$scope.bioImage="";
	$scope.rawImageList=[];
	$scope.$on("$routeChangeSuccess", function(){
		$interval.cancel(stop);
		$scope.disableEnrollment();
	});

	$scope.$on("$destroy", function(){
		//console.log('Destroy Called');
		$interval.cancel(stop);
		$scope.disableEnrollment();
	});


	
	/**
	 *
	 */
	if(!$scope.showEnroll){
	$scope.$watch('memEnrollment', function() {
		//console.log($scope.userEnrollment);
		$scope.disableEnrollment();
		//if ($scope.bioEnrollment) {
		$scope.disableEnrollment();
		var response ={};
		var request="0?NA";
		//blockUI.start();
		if ("WebSocket" in window) {
			console.log("WebSocket is supported by your Browser!");

			// Let us open a web socket
			//var ws = new WebSocket("ws://localhost:9998/echo");
			var ws = new WebSocket("ws://localhost:8200/");

			ws.onopen = function() {
				// Web Socket is connected, send data using send()
				console.log("Message to send");
				ws.send(request);
				console.log("Message is sent...");
			};

			ws.onmessage = function(evt) {
				response = JSON.parse(evt.data);
				console.log("Response##"+response);
				if (response.code == 200) {
					logger.logSuccess('Device Detected!!');
					$scope.startEnrollment();
					ws.send("9|NA");
				}
				else {
					logger.logError('No Device detected!!');
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


		//}
		//else
		//{
		//	$interval.cancel(stop);
		//	$scope.disableEnrollment();
		//}
	});


	}


	/**
	 * resets device
	 */
	$scope.resetDevice = function() {

		var response ={};
		var request="0?NA";
		//blockUI.start();
		if ("WebSocket" in window) {
			console.log("WebSocket is supported by your Browser!");

			// Let us open a web socket
			//var ws = new WebSocket("ws://localhost:9998/echo");
			var ws = new WebSocket("ws://localhost:8200/");

			ws.onopen = function() {
				// Web Socket is connected, send data using send()
				console.log("Message to send");
				ws.send(request);
				console.log("Message is sent...");
			};

			ws.onmessage = function(evt) {
				response = JSON.parse(evt.data);
				console.log("Response##"+response);
				if (response.code == 200) {
					logger.logSuccess('Device Detected!!');
					$scope.startEnrollment();
					ws.send("9|NA");
				}
				else {
					logger.logError('No Device detected!!');
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

	};
	//$scope.resetDevice();
	/**
	 *
	 * @param data
	 */
	$scope.captureImage = function(data) {
		var res = {};
		var socketRequest ={};
		var bioRequest ={};
		bioRequest.request = "FINGERSCAN#"+data;
		bioRequest.requestType = "INIT";
		socketRequest.requestType = "BIO";
		socketRequest.request = bioRequest;
		blockUI.start();
		memberSvc.OnDeviceInit(socketRequest).success(function (response) {
			console.log(response);
			res = response;
			console.log('ResponseCaptureImage');
			console.log(res);
			$scope.setBioResponse(res);
			return res;
			//blockUI.stop();
		}).error(function (data, status, headers, config) {
			logger.logError("Oh snap! There is a problem with the server, please contact the administrator.")
			//blockUI.stop();
		});
	};


	$scope.setBioResponse = function (data) {
		$scope.bioResponse = data;
	}

	$scope.proceedCapture = function(tdImg, nxtImg, pos) {
		var spImg = tdImg.split('|');
		if (spImg[1] == 'L') {
			lCounter = pos;
		} else {
			rCounter = pos;
		}
		var prFlClass = 'fl-' + spImg[1] + spImg[0];
		var ntFlClass = '.fl-' + spImg[1] + (parseInt(spImg[0]) + 1);
		if (spImg[1] == 'L' && parseInt(lCounter) < parseInt(lCount)) {
			$scope.stopFlikr(prFlClass, 'S' + spImg[1] + spImg[0]);
			$scope.nextCapture(spImg[1] + (parseInt(spImg[0]) + 1));
		} else if (spImg[1] == 'L' && lCounter == lCount) {
			// Start Right Hand capture
			$scope.stopFlikr(prFlClass, 'S' + spImg[1] + spImg[0]);
			$scope.nextCapture('R0');
		} else if (spImg[1] == 'R' && rCounter < rCount) {
			$scope.stopFlikr(prFlClass, 'S' + spImg[1] + spImg[0]);
			$scope.nextCapture(spImg[1] + (parseInt(spImg[0]) + 1));
		} else if (lCounter == lCount && rCounter == rCount) {
			// Start Right Hand capture
			logger.log('Proceed to Save the BIO details');
			$scope.stopFlikr(prFlClass, 'S' + spImg[1] + spImg[0]);
		}
	};

	$scope.onEnroll = function() {
		var eO = $("#bioId")[0].OnEnroll(afisIp + '|' + afisPort);
		var eR = jQuery.parseJSON(eO);
		//console.log($scope.classSelect);
		//console.log(eR);
		if (eR.code == '200') {
			//console.log(eR.idkitId+':--:'+$scope.userId)
			if (eR.idkitId != 0) {
				//ADDED BY ANITA 04Sep2014
				if($scope.classSelect==3){
					var member = [];
					member.transMemberId =$scope.memberSelect;
					member.transMemberBioId = eR.idkitId;
					$scope.bioId=eR.idkitId;
					//	blockUI.start();
					enrollMemSvc.UpdMemEnr(member).success(function (response) {
						if (response.respCode == 200) {
							logger.logSuccess("Great! The Member BIO details were saved succesfully");
						}
						else{
							logger.logError("Failed to Update the details!!");
						}		            
						$location.path('/dashboard');
					});
					//blockUI.stop();
				}else{
					var user = [];
					user.userId = $scope.userId;
					user.userBioID = eR.idkitId;
					blockUI.start();
					enrollUserSvc.UpdUserEnr(user).success(function (response) {
						if (response.respCode == 200) {
							logger.logSuccess("Great! The user BIO details were saved succesfully");
							//blockUI.stop();
						}
						else
						{
							logger.logError("Failed to Update the details!!");
						}
						$location.path('/dashboard');
					});
					//	blockUI.stop();
				}
			}
		} else {
			errorDisplay('System Error while Saving BIO details!! Contact admin');
		}
	};


	escape = function (str) {
		  return str
		    .replace(/[\\]/g, '\\\\')
		    .replace(/[\"]/g, '\\\"')
		    .replace(/[\/]/g, '\\/')
		    .replace(/[\b]/g, '\\b')
		    .replace(/[\f]/g, '\\f')
		    .replace(/[\n]/g, '\\n')
		    .replace(/[\r]/g, '\\r')
		    .replace(/[\t]/g, '\\t');
		};
		
	/**
	 * enroll member
	 * 1st save to innovatrics db
	 */
	$scope.onEnrollMem = function() {
		var fingerPos = afisIp + '|' + afisPort;
		var res = {};
		var enrollRequest="";
		//var test = JSON.stringify($scope.rawImageList);
		var tArray = [];
		var i;
		for(i = 0; i < ($scope.rawImageList.length)-1; i++)
			{
				tArray.push($scope.rawImageList[i].image);
			}
		//enrollRequest="6*"+tArray;
		enrollRequest="6?"+JSON.stringify($scope.rawImageList);
		console.log("ReqLenght##"+enrollRequest.length);
		if ("WebSocket" in window) {
		console.log("WebSocket is supported by your Browser!");

		// Let us open a web socket
		//var ws = new WebSocket("ws://localhost:9998/echo");
		var ws = new WebSocket("ws://localhost:8200/");
		ws.binaryType = "arraybuffer";
		//ws.bufferedAmount(enrollRequest.length);
		ws.onopen = function() {
			// Web Socket is connected, send data using send()
			console.log("Message to send");
		
		    
			ws.send(enrollRequest);
			console.log("Message is sent...");
		};

		ws.onmessage = function(evt) {
			console.log(evt.data);
			//blockUI.start();
			res = JSON.parse(evt.data);
			//res=response;
			console.log("Response##"+res);
			//if (response.code == 200) {
				if (res.code != '200') {
					
					logger.logError(res.detail);
					//$scope.$apply(blockUI.stop());
					
				} else {
					console.log(res.idkitId+':--:')
					if (res.idkitId != 0) {
						var member = [];
						member.memberId = $scope.memberId;
						member.bioId = $scope.memberId;// res.idkitId;
						member.imageList=$scope.imageList;
							
						enrollMemSvc.UpdMemEnr(member).success(function (response) {
							if (response.respCode == 200) {
								logger.logSuccess("Great! The Beneficiary BIO details were saved successfully");
								$scope.memberViewMode=false;
								$location.path('/dashboard');
							}
							else{
								logger.logError("Failed to Update the details!!");
								return false;
							}
							$location.path('/dashboard');
						});
						
					}
					//$scope.$apply(blockUI.stop());
				}
			//}
			//else {
				//logger.logError('No Device detected!!');
			//}
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


	blockUI.stop();

};



$scope.disableEnrollment = function(){
	totalCount = 6;
	lCount = totalCount / 2;
	rCount = lCount;
	$('td[class^=cp-]').addClass('off');
	$('img[class^=fl-]').addClass('off');
	$('div[class^=qt-]').addClass('off');
	$('div[class^=qt-]').find('.progress-bar').attr('style', 'width:0%');
}

$scope.startEnrollment = function(){
	$(".cp-L0").removeClass('off').addClass('on');
	$(".qt-L0").removeClass('off').addClass('on');
	$scope.applyFlikr('.fl-L0');
}


var stop;
$scope.applyFlikr = function(className) {
	//console.log(stop);
	if ( angular.isDefined(stop) ) return;
	stop = $interval(function() {
		$(className).toggleClass('on off');
	}, 100);
};

$scope.stopFlikr = function(className, imgName) {
	if (angular.isDefined(stop)) {
		$interval.cancel(stop);
		stop = undefined;
		var url = "images/enroll/" + imgName + ".png";
		$('.'+className).attr('class', '').attr('class', className + ' imgFade').removeAttr('src').attr('src', url);
	}
};	

$scope.nextCapture = function(nextImage) {
	$(".cp-" + nextImage).removeClass('off').addClass('on');
	$(".qt-" + nextImage).removeClass('off').addClass('on');
	$scope.applyFlikr('.fl-' + nextImage);
}

$scope.cancelUserEnr = function () {
	$scope.userEditMode = false;
	$scope.userEnrollment=false;
	$scope.isDisabled = false;
	$scope.active = false;
	$location.path('/dashboard');
}

//cancelMemEnr()
$scope.cancelMem = function () {
	$scope.blnNewTrans = false;
	$scope.authPending = false;
	$scope.memEnrollment = false;
	$scope.memVerify = false;
	$location.path('/dashboard');
	// $scope.loadMemberData();
	//$scope.memberEditMode = true;
	// $scope.memberViewMode=false; 

	// $location.path('/transactions/transaction');
}


}).factory('enrollUserSvc', function ($http) {
	var shpEnUserSvc = {};
	shpEnUserSvc.UpdUserEnr = function (user) {
		return $http({
			url: '/compas/rest/user/updUserEn',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: {
				'userId': user.userId,
				'userBioID': user.userBioID
			}
		});
	};
	return  shpEnUserSvc;  
}).factory('enrollMemSvc', function ($http) {
	var shpEnMemSvc = {};


	shpEnMemSvc.UpdMemEnr = function (member) {
		return $http({
			url: '/compas/rest/member/updCustomerEnr',
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			data: {
				'memberId': member.memberId,
				'bioId': member.bioId,
				'imageList':member.imageList
			}
		});
	};




	return  shpEnMemSvc;  
}).directive('capture', function(logger,blockUI,memberSvc) {
	return {
		link : function($scope, element, attrs) {
			element.bind('click', function() {
				console.log("Clicked##")
				//blockUI.start();
				var fingerPos = element.attr('data-href') +'|' + afisIp + '|' + afisPort;
				console.log(fingerPos);
				var res = {};
				var response = {};
				var captureResponse = {};
				//var socketRequest ={};
				var bioRequest ="1?"+element.attr('data-href') +'|' + afisIp + '|' + afisPort+"|"+$scope.imageList;

				if ("WebSocket" in window) {
					console.log("WebSocket is supported by your Browser!");

					// Let us open a web socket
					//var ws = new WebSocket("ws://localhost:9998/echo");
					var ws = new WebSocket("ws://localhost:8200/");
					ws.onopen = function() {
						// Web Socket is connected, send data using send()
						console.log("FingerPrint to send");
						ws.send(bioRequest);
						console.log("FingerPrint is sent...");
					};

					ws.onmessage = function(evt) {
						captureResponse = JSON.parse(evt.data);						
						console.log("Message is received...");
						if (captureResponse.code != '200') {
							logger.logError(captureResponse.detail);
						} else {
							var src = "data:image/png;base64,";
							src = src + captureResponse.rawImg64;

							$scope.bioImage=src;
							//console.log($scope.bioImage);
							element.find('img').removeAttr('src');
							element.find('img').attr('src', src).attr('style', 'border-radius:20px;');
							if (captureResponse.quality[0] >= 80) {
								$('.qt-' + element.attr('data-img')).find('.progress-bar').removeClass('progress-bar-danger').attr('style', 'width:' + captureResponse.quality[0] + '%');
								logger.log(element.attr('data-info') + ' Captured Successfully!!');
								$scope.proceedCapture(element.attr('data-href'), element.attr('data-img'),element.attr('data-pos'));
								var miniImage=captureResponse.minImgStr64;
								$scope.imageList.push({'image': miniImage,'position':element.attr('data-href')});
								$scope.rawImageList.push({'image': captureResponse.rawImg64,'position':element.attr('data-href')});
								//console.log($scope.imageList);
							} else {
								$('.qt-' + element.attr('data-img')).find('.progress-bar').addClass('progress-bar-danger').attr('style', 'width:' + captureResponse.quality[0] + '%');
								logger.logError('Unable to Capture ' + element.attr('data-info') + ': Quality extracted:' + captureResponse.quality[0]);
							}
						}
					};


					ws.onclose = function() {
						// websocket is closed.
						console.log("Connection is closed...");
					};
				} else {
					// The browser doesn't support WebSocket
					console.log("WebSocket NOT supported by your Browser!");
				}		

			})
		},
	}
}).directive('capturev', function(logger, blockUI, loginSvc, localStorageService, $rootScope, $location) {
	return {
		link : function($scope, element, attrs) {
			element.bind('click', function() {
				var d = $.parseJSON($("#bioId")[0].OnDeviceInit());
				//console.log(d);
				if (d.code == '200') {
					//blockUI.start();				
					if ($scope.memberInfo.transMemberBioId > 0) {
						var fingerPos = $scope.memberInfo.transMemberBioId  + '|' + afisIp + '|' + afisPort;
						//console.log(fingerPos);
						var captureResponse = $.parseJSON($("#bioId")[0].OnValidate(fingerPos));
						//console.log(captureResponse);
						if (captureResponse.code != '200') {
							logger.logError(captureResponse.detail);
						} else {
							var src = "data:image/png;base64,";
							src = src + captureResponse.rawImg64;
							$(this).removeAttr('src');
							$(this).attr('src', src).attr('style', 'border-radius:20px;width:258px;height:320px;');
							if (captureResponse.quality[0] >= 80) {
								logger.logSuccess(' Validated Successfully!!');
								$scope.proceedTxn();

							} else {
								logger.logError('Unable to Capture : Quality extracted:' + captureResponse.quality[0]);
							}
						}

					} else {
						logger.logError('User Not Enrolled for BIO');
					}
					//blockUI.stop();
				}
				else
				{
					logger.logError('No Device detected!!');
				}
			})
		},
	}
})




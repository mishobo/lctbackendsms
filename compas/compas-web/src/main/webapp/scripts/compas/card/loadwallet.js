/**
 * 
 */
/**
 * User Angular Module
 */
'use strict';
angular.module('app.loadwallet', []).controller("loadWalletCtrl", ["$scope", "$filter", "loadWalletSvc","$loadWalletValid", "$rootScope", "blockUI", "logger", "$location", function ($scope, $filter,loadWalletSvc,$loadWalletValid, $rootScope, blockUI, logger, $location) {
	
    var init;
    
    $scope.onLoad=function(){
    	var obj = document.getElementById("cardId");
    	var connect = obj.deviceConnected();
    	 if(connect == "0")
         {
           var response= obj.readPers("1002","00EF");
         // var cardBalance=obj.readWallet("6770776585828977","1007", "0EEC", "BF", "03")
             var cardBal= cardBalance.replace(0, 30);
            alert(cardBal);
            // var cardBl=cardBal.split('C', 1)[0] ;
             if(cardBl.length<=0)
                 cardBl=0;
     // alert(cardBl) ;

           // bootbox.alert(response);
             if(response.length > 102)
             {
                 var cardNumber = response.substring(0,20);
                 cardNumber= cardNumber.trim();
                // alert("Card Read :"+cardNo);
                 var loadStr=cardNumber.concat(",").concat(cardBl)   ;
                 alert(loadStr)
                 loadWalletSvc.GetLoadWallet(cardNumber).success(function (wallet){
                	 $scope.programmeDesc=wallet.programmeDesc;
                	 $scope.programmeValue=0;//wallet.programmeValue;
                	 $scope.balance=wallet.balance;
                	 $scope.cardNumber=cardNumber;
                	 $scope.loadCardString=wallet.loadCardString;
                	// var response= obj.writeWallet("6770776585828977","1007","0EEC","BF","03",wallet.loadCardString);
                     
                  //   if(response == '0')
                      // {
                    	 // if($("#code").val() == "F")
                        // {
                           // var resp=
							// obj.writeLinear("6770776585828977","1008","00B4","14","01",wallet.programmeValue);
                          
     					 // if(resp=="0")
     					 // {
     						 // var rep=obj.readWallet("6770776585828977","1007", "0EEC", "BF", "03");
     						 //  alert(rep)
     					  // }
                        // }
                       // }
                 })
             }
         }
         else
         {
        
         }
    }
    $scope.onLoad();

    $scope.updLoadCard = function () {
        var load = {};

       
      
       
      
        load.loadAmount = $scope.programmeValue;
        load.balance= parseFloat($scope.balance)+parseFloat($scope.programmeValue);
        load.cardNumber=$scope.cardNumber;
        load.createdBy = $rootScope.UsrRghts.sessionId;
        blockUI.start()
        loadWalletSvc.UpdLoadCard(load).success(function (response) {
        	console.log(response);
            if (response.respCode == 200) {
             	var obj = document.getElementById("cardId");
            	var connect = obj.deviceConnected();
            	 if(connect == "0")
                 {
            var appResp= obj.writeProgramme("6770776585828977","1002","00EF",$scope.loadCardString);
            if(appResp=="0"){
            	 var rep=obj.readLinear("1002", "00EF");
            	 alert(rep)
            }
            else{
            	logger.logError(appResp)
            }
                 }logger.logSuccess("Great! The branch information was saved succesfully")
               
                $scope.onLoad();

              
            }
            else  {
                logger.logWarning(response.respMessage);
            }
           
            blockUI.stop();
        }).error(function (data, status, headers, config) {
            logger.logError("Oh snap! There is a problem with the server, please contact the adminstrator.")
            blockUI.stop();
        });
    };

} ]).factory('loadWalletSvc', function ($http) {
    var compasLoadWalletSvc = {};
  
    compasLoadWalletSvc.UpdLoadCard = function (load) {
        return $http({
            url: '/compas/rest/member/updCardLoad',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: load
        });
    };
    
    compasLoadWalletSvc.GetLoadWallet = function (cardNumber) {
        return $http({
            url: '/compas/rest/member/gtLoadCardInfo/'+cardNumber,
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
    };
    return compasLoadWalletSvc;
}).factory('$loadWalletValid', function () {
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
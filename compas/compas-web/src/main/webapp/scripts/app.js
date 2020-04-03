(function () {
 
    angular.module("app.ui.form.directives", []).directive("uiRangeSlider", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                return ele.slider()
            }
        }
    }]).directive("uiFileUpload", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                return ele.bootstrapFileInput()
            }
        }
    }]).directive("uiSpinner", [function () {
        return {
            restrict: "A",
            compile: function (ele) {
                return ele.addClass("ui-spinner"), {
                    post: function () {
                        return ele.spinner()
                    }
                }
            }
        }
    }]).directive("uiWizardForm", [function () {
        return {
            link: function (scope, ele) {
                return ele.steps()
            }
        }
    }])
}).call(this), function () {
    "use strict";
    angular.module("app.ui.ctrls", []).controller("NotifyCtrl", ["$scope", "logger", function ($scope, logger) {
        return $scope.notify = function (type) {
            switch (type) {
            case "info":
                return logger.log("Heads up! This alert needs your attention, but it's not super important.");
            case "success":
                return logger.logSuccess("Well done! You successfully read this important alert message.");
            case "warning":
                return logger.logWarning("Warning! Best check yo self, you're not looking too good.");
            case "error":
                return logger.logError("Oh snap! Change a few things up and try submitting again.")
            }
        }
    }]).controller("ModalDemoCtrl", ["$scope", "$modal", "$log", function ($scope, $modal, $log) {
        $scope.items = ["item1", "item2", "item3"], $scope.open = function () {
            var modalInstance;
            modalInstance = $modal.open({
                templateUrl: "myModalContent.html",
                controller: "ModalInstanceCtrl",
                resolve: {
                    items: function () {
                        return $scope.items
                    }
                }
            }), modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem
            }, function () {
                $log.info("Modal dismissed at: " + new Date)
            })
        }
    }]).controller("ModalInstanceCtrl", ["$scope", "$modalInstance", "items", function ($scope, $modalInstance, items) {
        $scope.items = items, $scope.selected = {
                item: $scope.items[0]
        }, $scope.ok = function () {
            $modalInstance.close($scope.selected.item)
        }, $scope.cancel = function () {
            $modalInstance.dismiss("cancel")
        }
    }])
}.call(this), function () {
    "use strict";
    angular.module("app.ui.directives", []).directive("uiTime", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                var checkTime, startTime;
                return startTime = function () {
                    var h, m, s, t, time, today;
                    return today = new Date, h = today.getHours(), m = today.getMinutes(), s = today.getSeconds(), m = checkTime(m), s = checkTime(s), time = h + ":" + m + ":" + s, ele.html(time), t = setTimeout(startTime, 500)
                }, checkTime = function (i) {
                    return 10 > i && (i = "0" + i), i
                }, startTime()
            }
        }
    }]).directive("uiWeather", [function () {
        return {
            restrict: "A",
            link: function (scope, ele, attrs) {
                var color, icon, skycons;
                return color = attrs.color, icon = Skycons[attrs.icon], skycons = new Skycons({
                    color: color,
                    resizeClear: !0
                }), skycons.add(ele[0], icon), skycons.play()
            }
        }
    }])
}.call(this), function () {
    "use strict";
    angular.module("app.ui.services", []).factory("logger", [function () {
        var logIt;
        return toastr.options = {
                closeButton: !0,
                positionClass: "toast-top-right",
                timeOut: "3000"
        }, logIt = function (message, type) {
            return toastr[type](message)
        }, {
            log: function (message) {
                logIt(message, "info")
            },
            logWarning: function (message) {
                logIt(message, "warning")
            },
            logSuccess: function (message) {
                logIt(message, "success")
            },
            logError: function (message) {
                logIt(message, "error")
            }
        }
    }])
}.call(this), function () {
    "use strict";
    angular.module("app", ["ngRoute",
                           "ngAnimate",
                           "ngSanitize",
                           "ui.bootstrap",
                           "textAngular",
                           "app.ui.ctrls",
                           "app.ui.directives",
                           "app.ui.services",
                           "app.controllers",
                           "app.directives",
                           "app.ui.form.directives",
                           "app.ui.form.ctrls",
                           "app.localization",
                           "app.login",
                           "app.user",
                           "app.service",
                           "app.group.rights",
                           "app.login.lock",
                           "app.profile",
                           "blockUI",
                           "LocalStorageModule",
                           "ui.select2",
                           "ngImgCrop",
                           "app.dashboard",
                           "app.branch",
                           "app.device",
                           "app.member",
                           "app.issueDevice",
                           "app.memberinquiry",
                           "app.blockcard",
                           "app.acivatecard",
                           "app.card",
                           "app.rptTransaction",
                           "app.programme",
                           "app.organization",
                           "app.EnrollController",
                           "app.merchant",
                           "app.agent",
                           "app.wallet",
                           "app.bin",
                           "app.loadwallet",
                           "app.param",
                           "app.rptCashTransaction",
                           "app.rptPendingTransaction",
                           "app.rptUnpaidTransaction",
                           "app.rptSettledTransaction",
                           "app.rptRejectedTransaction",
                           "app.rptmasterlisting",
                           "app.cardreissue",
                           "app.zone",
                           "app.area",
                           "app.location",
                           "app.product",
                           "app.voucher",
                           "app.bnfgrp",
                           "app.bnfapprovals",
                           "app.voucher_topup",
                           "app.beneficiaryupload",
                           "app.topupapproval",
                           "app.txnverification",
                           "app.category",
                           "easypiechart",
                           "app.txnapproval",
                           "app.txnsettlement",
                           "app.authtransaction",
                           "app.offlinetransaction",
                           "app.memberstatement",
                           "app.rptprovidertrans",
                           "app.rptcovertrans",
                           "app.rptcancelledtrans",
                           "app.rptauthorizedtrans",
                           "app.providertransaction",
                           "app.detach",
                           "app.reset",
                           "app.uploadclaim",
                           "app.uploadservice",
                           "app.uploadmember",
                           "app.akuhtxnverification",
                           "app.akuhtxnapproval",
                           "app.akuhtxnsettlement",
						   "app.akuhrptTransaction",
						   "app.dependantinquiry",
						   "app.changepassword",
						   "app.gtrptTransaction",
						   "app.mpshahrptTransaction",
						   "app.nhrptTransaction",
						   "app.deactivate",
						   "app.nbhdashboard",
						   "app.rptAuditTrail",
						   "app.uhmcrptTransaction",
						   "app.rptUtilization",
						   "app.rptjdcprovidertrans",
						   "app.rptMembers",
						   "app.uploadofflct",
						   "app.smartdebit"
                           ]
    ).config(["$routeProvider", function ($routeProvider) {
        return $routeProvider.when("/", {
            redirectTo: "/dashboard"
        }).when("/login", {
            templateUrl: "views/administration/signin.html",
            controller: "loginCtrl"
        }).when("/lock", {
            templateUrl: "views/administration/lock-screen.html"
        }).when("/user/group", {
            templateUrl: "views/administration/group.html"
        }).when("/user/users", {
            templateUrl: "views/administration/user.html"
        }).when("/user/changepassword", {
			templateUrl: "views/administration/changepassword.html"
		}).when("/user/profile", {
            templateUrl: "views/administration/myprofile.html"
        }).when("/dashboard", {
            templateUrl: "views/administration/dashboard.html"
        }).when("/nbihospital", {
	            templateUrl: "views/administration/nbhdashboard.html"
		}).when("/masters/location", {
            templateUrl: "views/regionsetting/location.html"
        }).when("/masters/zone", {
            templateUrl: "views/regionsetting/zone.html"
        }).when("/masters/area", {
            templateUrl: "views/regionsetting/area.html"
        }).when("/masters/device", {
            templateUrl: "views/devicesetting/device.html"
        }).when("/masters/issuedevice", {
            templateUrl: "views/devicesetting/issuedevice.html"
        }).when("/masters/branch", {
            templateUrl: "views/masters/branch.html"
        }).when("/masters/service", {
            templateUrl: "views/masters/service.html"
        }).when("/masters/product", {
            templateUrl: "views/masters/product.html"
        }).when("/masters/bnfgrp", {
            templateUrl: "views/masters/beneficiarygroup.html"
        }).when("/masters/organization", {
            templateUrl: "views/masters/organization.html"
        }).when("/masters/merchant", {
            templateUrl: "views/masters/merchants.html"
        }).when("/masters/programme", {
            templateUrl: "views/masters/programme.html"
         }).when("/masters/category", {
              templateUrl: "views/masters/category.html"
        }).when("/masters/agent", {
            templateUrl: "views/masters/agent.html"
        }).when("/masters/custinquiry", {
            templateUrl: "views/beneficiary/inquiry.html"
        }).when("/masters/inquiry/dependant", {
            templateUrl: "views/beneficiary/dependantinquiry.html"
        }).when("/masters/bnfapproval", {
            templateUrl: "views/beneficiary/bnfapprovals.html"
        }).when("/masters/bnfverify", {
            templateUrl: "views/beneficiary/bnfverify.html"
        }).when("/masters/member", {
            templateUrl: "views/beneficiary/membermaster.html"
        }).when("/masters/inquiry", {
            templateUrl: "views/beneficiary/custinquiry.html"
        }).when("/masters/bnfupload", {
            templateUrl: "views/beneficiary/beneficiaryupload.html"
        }).when("/bios/detach", {
            templateUrl: "views/beneficiary/detach.html"
        }).when("/card/print", {
            templateUrl: "views/card/issuecard.html"
        }).when("/card/block", {
            templateUrl: "views/card/blockcards.html"
        }).when("/masters/voucher", {
            templateUrl: "views/card/voucher.html"
        }) .when("/card/activate", {
            templateUrl: "views/card/activatecards.html"
        }) .when("/card/wallet", {
            templateUrl: "views/card/cardwallet.html"
        }) .when("/card/binallocation", {
            templateUrl: "views/card/binallocation.html"
        }) .when("/card/loadwallet", {
            templateUrl: "views/card/loadwallet.html"
        }).when("/card/cardreissue", {
            templateUrl: "views/card/cardreissue.html"
        }).when("/card/topup", {
            templateUrl: "views/card/voucher_topup.html"
        }).when("/card/topupapproval", {
            templateUrl: "views/card/topupapproval.html"
        }).when("/transaction/vetbills", {
            templateUrl: "views/transaction/txnverification.html"
        }).when("/transaction/vetakuhbills", {
            templateUrl: "views/transaction/akuhtxnverification.html"
        }).when("/transaction/approvebills", {
            templateUrl: "views/transaction/txnapproval.html"
        }).when("/transaction/approveakuhbills", {
            templateUrl: "views/transaction/akuhtxnapproval.html"
        }).when("/transaction/settlement", {
            templateUrl: "views/transaction/txnsettlemet.html"
        }).when("/transaction/akuhsettlement", {
            templateUrl: "views/transaction/akuhtxnsettlemet.html"
        }).when("/transaction/authtrans", {
            templateUrl: "views/transaction/authorizetransaction.html"
        }).when("/transaction/offline", {
            templateUrl: "views/transaction/offlinetransaction.html"
        }).when("/transaction/providerTrans", {
            templateUrl: "views/transaction/providertransaction.html"
        }).when("/reports/transaction", {
            templateUrl: "views/reports/rpttransactions.html"
        }).when("/reports/nairobihosp/transaction", {
	        templateUrl: "views/reports/nrbrpttransactions.html"
		}).when("/reports/outstandingbills", {
            templateUrl: "views/reports/rptcashtxn.html"
        }).when("/reports/pendingbills", {
            templateUrl: "views/reports/rptpendingbills.html"
         }).when("/reports/unpaidbills", {
              templateUrl: "views/reports/rptunpaidbills.html"
         }).when("/reports/settledbills", {
             templateUrl: "views/reports/rptsettledbills.html"
         }).when("/reports/rejectedbills", {
             templateUrl: "views/reports/rptrejectedbills.html"
         }).when("/reports/aprvdtopup", {
             templateUrl: "views/reports/rptapprovedtopup.html"
         }).when("/reports/statement", {
            templateUrl: "views/reports/memberstatement.html"
        }).when("/reports/providerwise", {
            templateUrl: "views/reports/rptprovidertrans.html"
        }).when("/reports/coverwise", {
            templateUrl: "views/reports/rptcoverwisetrans.html"
        }).when("/reports/cancelled", {
            templateUrl: "views/reports/rptcancelledtrans.html"
        }).when("/reports/authorized", {
            templateUrl: "views/reports/rptauthorizedtrans.html"
        }).when("/members/deactivate", {
	            templateUrl: "views/beneficiary/customerdeactivation.html"
		}).when("/reports/utilization", {
			    templateUrl: "views/reports/rptutilization.html"
		}).when("/reset", {
            templateUrl: "views/administration/reset.html",
            controller: "resetCtrl"
        })
        .when("/transaction/uploadclaim", {
            templateUrl: "views/transaction/uploadclaim.html",
            controller: "uploadclaimCtrl"
        })
		.when("/transaction/uploadofflct", {
	            templateUrl: "views/transaction/uploadofflct.html",
	            controller: "uploadoffLCTCtrl"
	    })
		.when("/transaction/smartdebit", {
	         templateUrl: "views/transaction/smartdebit.html",
	         controller: "smartDebitCtrl"
	    })
        .when("/masters/uploadservice", {
            templateUrl: "views/masters/uploadservice.html",
            controller: "uploadserviceCtrl"
        })
        .when("/masters/uploadmember", {
            templateUrl: "views/beneficiary/uploadmember.html",
            controller: "uploadmemberCtrl"
        }).when("/akuhreports/transaction", {
	            templateUrl: "views/reports/akuhrpttransactions.html"
		}).when("/reports/gertrude/transaction", {
	            templateUrl: "views/reports/getrude-reports/gtrpttransactions.html"
		}).when("/reports/mpshah/transaction", {
	            templateUrl: "views/reports/mpshahrpttransactions.html"
		}).when("/reports/audit/auditlog", {
	            templateUrl: "views/reports/rptaudittrail.html"
		}).when("/reports/uhmc/transaction", {
	            templateUrl: "views/reports/uhmcrpttransactions.html"
		}).when("/reports/clinix/transaction", {
	            templateUrl: "views/reports/clinixrpttransactions.html"
		}).when("/reports/judiciary/providerwise", {
	            templateUrl: "views/reports/rptjdcprovidertrans.html"
		}).when("/reports/judiciary/members", {
			templateUrl: "views/reports/rptjudiciarymembers.html"
		})
        .when("/404", {
            templateUrl: "views/others/404.html"
        }).otherwise({
            redirectTo: "/404"
        })
    }]).run(function ($chkNotNullUndefined, $rootScope, localStorageService, $location, $filter, logger) {
        $rootScope.$on("$routeChangeStart", function (event, next, current) {
            console.log('next: ', next);
            if ($chkNotNullUndefined(localStorageService.get('rxr')) == false) {
                localStorageService.clearAll();
                if (next.templateUrl == "views/administration/reset.html") {
                    // do nothing
                }
                else if (!(next.templateUrl == "views/administration/signin.html")) {
                    $location.path("/login");
                }
            }
            else {
                if (next.templateUrl == "views/administration/signin.html") {
                    localStorageService.clearAll();
                }
                else {
                    if (angular.isUndefined($rootScope.UsrRghts)) {
                        $rootScope.UsrRghts = localStorageService.get('rxr');
                        $location.path("/dashboard");
                    }
                    else {
                        if ($rootScope.UsrRghts == null) {
                            $rootScope.UsrRghts = localStorageService.get('rxr');
                            $location.path("/dashboard");
                        }
                    }
                    if (next.templateUrl == "views/administration/lock-screen.html") {
                        localStorageService.clearAll();
                    }
                    else {
                        if (!($location.path().toLowerCase() == "/dashboard"
                            || $location.path().toLowerCase() == "/login"
                                || $location.path().toLowerCase() == "/404"
                                    || $location.path().toLowerCase() == "/lock"
                                        || $location.path().toLowerCase() == "/"
                                            || $location.path().toLowerCase() == "/user/profile")) {
                            if ($chkNotNullUndefined(next.templateUrl)) {
                                if (!$filter('checkRightToView')($rootScope.UsrRghts.rightsHeaderList, "#" + $location.path())) {
                                    $location.path("/dashboard");
                                    logger.log("Oh snap! You are not allowed to view this screen.");
                                }
                            }
                        }
                    }
                }
            }
        });
    }).filter('checkRightToView', function () {
        return function (menuList, rightViewName) {
            var i = 0, len = menuList.length;
 
            for (; i < len; i++) {
                var j = 0, rlen = menuList[i].rightsList.length;
                for (; j < rlen; j++) {
                    if (menuList[i].rightsList[j].rightViewName.toLowerCase().trim() == rightViewName.toLowerCase().trim()) {
                        return menuList[i].rightsList[j].allowView;
                    }
                }
            }
            return false;
        }
    }).filter('checkRightToAdd', function () {
        return function (menuList, rightViewName) {
            var i = 0, len = menuList.length;
 
            for (; i < len; i++) {
                var j = 0, rlen = menuList[i].rightsList.length;
                for (; j < rlen; j++) {
                    if (menuList[i].rightsList[j].rightViewName.toLowerCase().trim() == rightViewName.toLowerCase().trim()) {
                        return menuList[i].rightsList[j].allowAdd;
                    }
                }
            }
            return false;
        }
    }).filter('checkRightToEdit', function () {
        return function (menuList, rightViewName) {
            var i = 0, len = menuList.length;
 
            for (; i < len; i++) {
                var j = 0, rlen = menuList[i].rightsList.length;
                for (; j < rlen; j++) {
                    if (menuList[i].rightsList[j].rightViewName.toLowerCase().trim() == rightViewName.toLowerCase().trim()) {
                        return menuList[i].rightsList[j].allowEdit;
                    }
                }
            }
            return false;
        }
    }).config(["blockUIConfigProvider", function (blockUIConfigProvider) {
 
        // Change the default overlay message
        blockUIConfigProvider.message('Please Wait, While the Operation Completes!');
 
        // Change the default delay to 100ms before the blocking is visible
        blockUIConfigProvider.delay(1000);
 
    }]).factory('$chkNotNullUndefined', function () {
        return function (data) {
            if (angular.isUndefined(data)) {
                return false;
            }
            else {
                return !(data == null);
            }
        };
    }).factory('$chkNotNullUndefinedBlank', function () {
        return function (data) {
            if (angular.isUndefined(data)) {
                return false;
            }
            else {
                if (data == null) {
                    return false;
                }
                else {
                    return !(data.trim() == "");
                }
            }
        };
    }).filter('propsFilter', function () {
        return function (items, props) {
            var out = [];
 
            if (angular.isArray(items)) {
                items.forEach(function (item) {
                    var itemMatches = false;
 
                    var keys = Object.keys(props);
                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }
 
                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }
 
            return out;
        };
    })
}.call(this), function () {
    angular.module("app.directives", []).directive("imgHolder", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                return Holder.run({
                    images: ele[0]
                })
            }
        }
    }]).directive("customBackground", function () {
        return {
            restrict: "A",
            controller: ["$scope", "$element", "$location", function ($scope, $element, $location) {
                var addBg, path;
                return path = function () {
                    return $location.path()
                }, addBg = function (path) {
                    switch ($element.removeClass("body-home body-special body-tasks body-lock"), path) {
                    case "/":
                        return $element.addClass("body-home");
                    case "/404":
                    case "/pages/500":
                    case "/login":
                    case "/pages/signup":
                    case "/reset":
                        return $element.addClass("body-special");
                    case "/lock":
                        return $element.addClass("body-special body-lock");
                    case "/tasks":
                        return $element.addClass("body-tasks")
                    }
                }, addBg($location.path()), $scope.$watch(path, function (newVal, oldVal) {
                    return newVal !== oldVal ? addBg($location.path()) : void 0
                })
            }]
        }
    }).directive("uiColorSwitch", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                return ele.find(".color-option").on("click", function (event) {
                    var $this, hrefUrl, style;
                    if ($this = $(this), hrefUrl = void 0, style = $this.data("style"), "loulou" === style) hrefUrl = "styles/main.css", $('link[href^="styles/main"]').attr("href", hrefUrl);
                    else {
                        if (!style) return !1;
                        style = "-" + style, hrefUrl = "styles/main" + style + ".css", $('link[href^="styles/main"]').attr("href", hrefUrl)
                    }
                    return event.preventDefault()
                })
            }
        }
    }]).directive("toggleMinNav", ["$rootScope", function ($rootScope) {
        return {
            restrict: "A",
            link: function (scope, ele) {
                var $window, Timer, app, updateClass;
                return app = $("#app"), $window = $(window), ele.on("click", function (e) {
                    return app.hasClass("nav-min") ? app.removeClass("nav-min") : (app.addClass("nav-min"), $rootScope.$broadcast("minNav:enabled")), e.preventDefault()
                }), Timer = void 0, updateClass = function () {
                    var width;
                    return width = $window.width(), 768 > width ? app.removeClass("nav-min") : void 0
                }, $window.resize(function () {
                    var t;
                    return clearTimeout(t), t = setTimeout(updateClass, 300)
                })
            }
        }
    }]).directive("collapseNav", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                var $a, $aRest, $lists, $listsRest, app;
 
                return $lists = $('#nav').find("ul").parent("li"),
                $lists.append('<i class="fa fa-caret-right icon-has-ul"></i>'),
                $a = $lists.children("a"),
                $listsRest = $('#nav').children("li").not($lists),
                $aRest = $listsRest.children("a"),
                app = $("#app"), $a.unbind(), $aRest.unbind(), scope.unbind(),
                $a.on("click", function (event) {
                    var $parent, $this;
                    return app.hasClass("nav-min") ? !1 : ($this = $(this), $parent = $this.parent("li"), $lists.not($parent).removeClass("open").find("ul").slideUp(), $parent.toggleClass("open").find("ul").slideToggle(), event.preventDefault())
                }), $aRest.on("click", function () {
                    return $lists.removeClass("open").find("ul").slideUp()
                }), scope.$on("minNav:enabled", function () {
                    return $lists.removeClass("open").find("ul").slideUp()
                })
            }
        }
    }]).directive('onFinishRender', function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                if (scope.$last === true) {
                    $timeout(function () {
                        scope.$emit('ngRepeatFinished');
                    });
                }
            }
        }
    }).directive("highlightActive", [function () {
        return {
            restrict: "A",
            controller: ["$scope", "$element", "$attrs", "$location", function ($scope, $element, $attrs, $location) {
                var highlightActive, links, path;
                return links = $element.find("a"), path = function () {
                    return $location.path()
                }, highlightActive = function (links, path) {
                    return path = "#" + path, angular.forEach(links, function (link) {
                        var $li, $link, href;
                        return $link = angular.element(link), $li = $link.parent("li"), href = $link.attr("href"), $li.hasClass("active") && $li.removeClass("active"), 0 === path.indexOf(href) ? $li.addClass("active") : void 0
                    })
                }, highlightActive(links, $location.path()), $scope.$watch(path, function (newVal, oldVal) {
                    return newVal !== oldVal ? highlightActive(links, $location.path()) : void 0
                })
            }]
        }
    }]).directive("toggleOffCanvas", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                return ele.on("click", function () {
                    return $("#app").toggleClass("on-canvas")
                })
            }
        }
    }]).directive("slimScroll", [function () {
        return {
            restrict: "A",
            link: function (scope, ele) {
                return ele.slimScroll({
                    height: "100%"
                })
            }
        }
    }]).directive("goBack", [function () {
        return {
            restrict: "A",
            controller: ["$scope", "$element", "$window", function ($scope, $element, $window) {
                return $element.on("click", function () {
                    return $window.history.back()
                })
            }]
        }
    }])
}.call(this), function () {
    "use strict";
    angular.module("app.localization", []).factory("localize", ["$http", "$rootScope", "$window", function ($http, $rootScope, $window) {
        var localize;
        return localize = {
                language: "",
                url: void 0,
                resourceFileLoaded: !1,
                successCallback: function (data) {
                    return localize.dictionary = data, localize.resourceFileLoaded = !0, $rootScope.$broadcast("localizeResourcesUpdated")
                },
                setLanguage: function (value) {
                    return localize.language = value.toLowerCase().split("-")[0], localize.initLocalizedResources()
                },
                setUrl: function (value) {
                    return localize.url = value, localize.initLocalizedResources()
                },
                buildUrl: function () {
                    return localize.language || (localize.language = ($window.navigator.userLanguage || $window.navigator.language).toLowerCase(), localize.language = localize.language.split("-")[0]), "i18n/resources-locale_" + localize.language + ".js"
                },
                initLocalizedResources: function () {
                    var url;
                    return url = localize.url || localize.buildUrl(), $http({
                        method: "GET",
                        url: url,
                        cache: !1
                    }).success(localize.successCallback).error(function () {
                        return $rootScope.$broadcast("localizeResourcesUpdated")
                    })
                },
                getLocalizedString: function (value) {
                    var result, valueLowerCase;
                    return result = void 0, localize.dictionary && value ? (valueLowerCase = value.toLowerCase(), result = "" === localize.dictionary[valueLowerCase] ? value : localize.dictionary[valueLowerCase]) : result = value, result
                }
        }
    }]).directive("i18n", ["localize", function (localize) {
        var i18nDirective;
        return i18nDirective = {
                restrict: "EA",
                updateText: function (ele, input, placeholder) {
                    var result;
                    return result = void 0, "i18n-placeholder" === input ? (result = localize.getLocalizedString(placeholder), ele.attr("placeholder", result)) : input.length >= 1 ? (result = localize.getLocalizedString(input), ele.text(result)) : void 0
                },
                link: function (scope, ele, attrs) {
                    return scope.$on("localizeResourcesUpdated", function () {
                        return i18nDirective.updateText(ele, attrs.i18n, attrs.placeholder)
                    }), attrs.$observe("i18n", function (value) {
                        return i18nDirective.updateText(ele, value, attrs.placeholder)
                    })
                }
        }
    }]). config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
        $httpProvider.defaults.cache = false;
        if (!$httpProvider.defaults.headers.get) {
            $httpProvider.defaults.headers.get = {};
        }
        // disable IE ajax request caching
        $httpProvider.defaults.headers.get['If-Modified-Since'] = '0';
        // .....here proceed with your routes
    }]).
    controller("LangCtrl", ["$scope", "localize", function ($scope, localize) {
        return $scope.lang = "English", $scope.setLang = function (lang) {
            switch (lang) {
            case "English":
                localize.setLanguage("EN-US");
                break;
            case "EspaÃ±ol":
                localize.setLanguage("ES-ES");
                break;
            case "æ—¥æœ¬èªž":
                localize.setLanguage("JA-JP");
                break;
            case "ä¸­æ–‡":
                localize.setLanguage("ZH-TW");
                break;
            case "Deutsch":
                localize.setLanguage("DE-DE");
                break;
            case "franÃ§ais":
                localize.setLanguage("FR-FR");
                break;
            case "Italiano":
                localize.setLanguage("IT-IT");
                break;
            case "Portugal":
                localize.setLanguage("PT-BR");
                break;
            case "Ð ÑƒÑ�Ñ�ÐºÐ¸Ð¹ Ñ�Ð·Ñ‹Ðº":
                localize.setLanguage("RU-RU");
                break;
            case "í•œêµ­ì–´":
                localize.setLanguage("KO-KR")
            }
            return $scope.lang = lang
        }
    }])
}.call(this), function () {
    "use strict";
    angular.module("app.ui.form.ctrls", []).controller("DatepickerDemoCtrl", ["$scope", function ($scope) {
        return $scope.today = function () {
            return $scope.dt = new Date
        }, $scope.today(), $scope.showWeeks = !0, $scope.toggleWeeks = function () {
            return $scope.showWeeks = !$scope.showWeeks
        }, $scope.clear = function () {
            return $scope.dt = null
        }, 
//       $scope.disabled = function (date, mode) {
//       return "day" === mode && (0 === date.getDay() || 6 === date.getDay())
//       },
        $scope.toggleMin = function () {
            var _ref;
            return $scope.minDate = null != (_ref = $scope.minDate) ? _ref : {
                "null": new Date
            }
        }, $scope.toggleMin(), $scope.open = function ($event) {
            return $event.preventDefault(), $event.stopPropagation(), $scope.opened = !0
        }, $scope.dateOptions = {
                "year-format": "'yy'",
                "starting-day": 1
        }, $scope.formats = ["dd-MMMM-yyyy", "yyyy/MM/dd", "shortDate"], $scope.format = $scope.formats[0]
    }])
}
.call(this), function () {
    "use strict";
    angular.module("app.controllers", []).controller("AppCtrl", ["$scope", "$location", function ($scope, $location) {
        return $scope.isSpecificPage = function () {
            var path;
            return path = $location.path(), _.contains(["/404", "/pages/500", "/pages/login", "/login", "/pages/signin1", "/pages/signin2", "/pages/signup", "/pages/signup1", "/pages/signup2", "/lock", "/pages/reset", "/reset"], path)
        }, $scope.main = {
                brand: "COMPAS",
                name: "",
                scriptVersion: "1.1.0"
        }
    }]).controller("NavCtrl", ["$scope","$rootScope","$http","localStorageService", function ($scope,$rootScope, $http, localStorageService) {
        $scope.$on('ngRepeatFinished', function (ngRepeatFinishedEvent) {
            var $a, $aRest, $lists, $listsRest, app;
             
            const currUserId = localStorageService.get("userId");
            $http({
                url: '/compas/rest/dashBoard/isuserakuh/'+currUserId,
                method: 'GET',
                headers: {'Content-Type': 'application/json'}
            }).success(function(response){
                console.log(response);
                console.log('UsrRights: ',$rootScope.UsrRghts);
                let rightsList = $rootScope.UsrRghts.rightsHeaderList[3].rightsList;
                rightsList = rightsList.filter(function(menu)
                {
                    return menu.rightViewName !== "#/masters/uploadmember";
                });
                $rootScope.UsrRghts.rightsHeaderList[3].rightsList = rightsList;
                /*
				 * if(response.respCode === 201)                 {
				 *                     $scope.showUploadClaim = false;
				 *                     console.log("no uploading AKUH
				 * files...");                     let rightsList =
				 * $rootScope.UsrRghts.rightsHeaderList[1].rightsList;
				 *                     rightsList =
				 * rightsList.filter(function(menu)                     {
				 *                         return menu.rightViewName !==
				 * "#/masters/uploadservice";                     });
				 *                     $rootScope.UsrRghts.rightsHeaderList[1].rightsList =
				 * rightsList;                      
				 *                     rightsList =
				 * $rootScope.UsrRghts.rightsHeaderList[3].rightsList;
				 *                     rightsList =
				 * rightsList.filter(function(menu)                     {
				 *                         return menu.rightViewName !==
				 * "#/masters/uploadmember";                     });
				 *                     $rootScope.UsrRghts.rightsHeaderList[3].rightsList =
				 * rightsList;                       rightsList =
				 * $rootScope.UsrRghts.rightsHeaderList[4].rightsList;
				 *                     rightsList =
				 * rightsList.filter(function(menu)                     {
				 *                         return menu.rightViewName !==
				 * "#/transaction/uploadclaim";                     });
				 *                     $rootScope.UsrRghts.rightsHeaderList[4].rightsList =
				 * rightsList;                     console.log('After UsrRights:
				 * ',$rootScope.UsrRghts);                 }
				 *                 else if(response.respCode === 200)
				 *                 {                     $scope.showUploadClaim =
				 * true;                     console.log("can upload AKUH
				 * files...");                 }                 
				 */
            });
             
            return $lists = $('#nav').find("ul").parent("li"),
            $lists.append('<i class="fa fa-caret-right icon-has-ul"></i>'),
            $a = $lists.children("a"),
            $listsRest = $('#nav').children("li").not($lists),
            $aRest = $listsRest.children("a"),
            app = $("#app"),
            $a.on("click", function (event) {
                var $parent, $this;
                return app.hasClass("nav-min") ? !1 : ($this = $(this), $parent = $this.parent("li"),
                        $lists.not($parent).removeClass("open").find("ul").slideUp(), $parent.toggleClass("open").find("ul").slideToggle(), event.preventDefault())
            }), $aRest.on("click", function () {
                return $lists.removeClass("open").find("ul").slideUp()
            }), $scope.$on("minNav:enabled", function () {
                return $lists.removeClass("open").find("ul").slideUp()
            })
        });
    }])
    .controller('DemoCtrl', function ($scope, $http, $timeout) {
        $scope.disabled = undefined;
 
        $scope.enable = function () {
            $scope.disabled = false;
        };
 
        $scope.disable = function () {
            $scope.disabled = true;
        };
 
        $scope.clear = function () {
            $scope.person.selected = undefined;
            $scope.address.selected = undefined;
            $scope.country.selected = undefined;
        };
 
        $scope.someGroupFn = function (item) {
 
            if (item.name[0] >= 'A' && item.name[0] <= 'M')
                return 'From A - M';
 
            if (item.name[0] >= 'N' && item.name[0] <= 'Z')
                return 'From N - Z';
 
        };
 
        $scope.personAsync = {selected: "wladimir@email.com"};
        $scope.peopleAsync = [];
 
        $timeout(function () {
            $scope.peopleAsync = [
                                  { name: 'Adam', email: 'adam@email.com', age: 12, country: 'United States' },
                                  { name: 'Amalie', email: 'amalie@email.com', age: 12, country: 'Argentina' },
                                  { name: 'EstefanÃ­a', email: 'estefania@email.com', age: 21, country: 'Argentina' },
                                  { name: 'Adrian', email: 'adrian@email.com', age: 21, country: 'Ecuador' },
                                  { name: 'Wladimir', email: 'wladimir@email.com', age: 30, country: 'Ecuador' },
                                  { name: 'Samantha', email: 'samantha@email.com', age: 30, country: 'United States' },
                                  { name: 'Nicole', email: 'nicole@email.com', age: 43, country: 'Colombia' },
                                  { name: 'Natasha', email: 'natasha@email.com', age: 54, country: 'Ecuador' },
                                  { name: 'Michael', email: 'michael@email.com', age: 15, country: 'Colombia' },
                                  { name: 'NicolÃ¡s', email: 'nicole@email.com', age: 43, country: 'Colombia' }
                                  ];
        }, 3000);
 
        $scope.counter = 0;
        $scope.someFunction = function (item, model) {
            $scope.counter++;
            $scope.eventResult = {item: item, model: model};
        };
 
        $scope.person = {};
        $scope.people = [
                         { name: 'Adam', email: 'adam@email.com', age: 12, country: 'United States' },
                         { name: 'Amalie', email: 'amalie@email.com', age: 12, country: 'Argentina' },
                         { name: 'EstefanÃ­a', email: 'estefania@email.com', age: 21, country: 'Argentina' },
                         { name: 'Adrian', email: 'adrian@email.com', age: 21, country: 'Ecuador' },
                         { name: 'Wladimir', email: 'wladimir@email.com', age: 30, country: 'Ecuador' },
                         { name: 'Samantha', email: 'samantha@email.com', age: 30, country: 'United States' },
                         { name: 'Nicole', email: 'nicole@email.com', age: 43, country: 'Colombia' },
                         { name: 'Natasha', email: 'natasha@email.com', age: 54, country: 'Ecuador' },
                         { name: 'Michael', email: 'michael@email.com', age: 15, country: 'Colombia' },
                         { name: 'NicolÃ¡s', email: 'nicole@email.com', age: 43, country: 'Colombia' }
                         ];
 
        $scope.address = {};
        $scope.refreshAddresses = function (address) {
            var params = {address: address, sensor: false};
            return $http.get(
                    'http://maps.googleapis.com/maps/api/geocode/json',
                    {params: params}
            ).then(function (response) {
                $scope.addresses = response.data.results;
            });
        };
 
        $scope.country = {};
        $scope.countries = [ // Taken from
                             // https://gist.github.com/unceus/6501985
                             {name: 'Afghanistan', code: 'AF'},
                             {name: 'Ã…land Islands', code: 'AX'},
                             {name: 'Albania', code: 'AL'},
                             {name: 'Algeria', code: 'DZ'},
                             {name: 'American Samoa', code: 'AS'},
                             {name: 'Andorra', code: 'AD'},
                             {name: 'Angola', code: 'AO'},
                             {name: 'Anguilla', code: 'AI'},
                             {name: 'Antarctica', code: 'AQ'},
                             {name: 'Antigua and Barbuda', code: 'AG'},
                             {name: 'Argentina', code: 'AR'},
                             {name: 'Armenia', code: 'AM'},
                             {name: 'Aruba', code: 'AW'},
                             {name: 'Australia', code: 'AU'},
                             {name: 'Austria', code: 'AT'},
                             {name: 'Azerbaijan', code: 'AZ'},
                             {name: 'Bahamas', code: 'BS'},
                             {name: 'Bahrain', code: 'BH'},
                             {name: 'Bangladesh', code: 'BD'},
                             {name: 'Barbados', code: 'BB'},
                             {name: 'Belarus', code: 'BY'},
                             {name: 'Belgium', code: 'BE'},
                             {name: 'Belize', code: 'BZ'},
                             {name: 'Benin', code: 'BJ'},
                             {name: 'Bermuda', code: 'BM'},
                             {name: 'Bhutan', code: 'BT'},
                             {name: 'Bolivia', code: 'BO'},
                             {name: 'Bosnia and Herzegovina', code: 'BA'},
                             {name: 'Botswana', code: 'BW'},
                             {name: 'Bouvet Island', code: 'BV'},
                             {name: 'Brazil', code: 'BR'},
                             {name: 'British Indian Ocean Territory', code: 'IO'},
                             {name: 'Brunei Darussalam', code: 'BN'},
                             {name: 'Bulgaria', code: 'BG'},
                             {name: 'Burkina Faso', code: 'BF'},
                             {name: 'Burundi', code: 'BI'},
                             {name: 'Cambodia', code: 'KH'},
                             {name: 'Cameroon', code: 'CM'},
                             {name: 'Canada', code: 'CA'},
                             {name: 'Cape Verde', code: 'CV'},
                             {name: 'Cayman Islands', code: 'KY'},
                             {name: 'Central African Republic', code: 'CF'},
                             {name: 'Chad', code: 'TD'},
                             {name: 'Chile', code: 'CL'},
                             {name: 'China', code: 'CN'},
                             {name: 'Christmas Island', code: 'CX'},
                             {name: 'Cocos (Keeling) Islands', code: 'CC'},
                             {name: 'Colombia', code: 'CO'},
                             {name: 'Comoros', code: 'KM'},
                             {name: 'Congo', code: 'CG'},
                             {name: 'Congo, The Democratic Republic of the', code: 'CD'},
                             {name: 'Cook Islands', code: 'CK'},
                             {name: 'Costa Rica', code: 'CR'},
                             {name: 'Cote D\'Ivoire', code: 'CI'},
                             {name: 'Croatia', code: 'HR'},
                             {name: 'Cuba', code: 'CU'},
                             {name: 'Cyprus', code: 'CY'},
                             {name: 'Czech Republic', code: 'CZ'},
                             {name: 'Denmark', code: 'DK'},
                             {name: 'Djibouti', code: 'DJ'},
                             {name: 'Dominica', code: 'DM'},
                             {name: 'Dominican Republic', code: 'DO'},
                             {name: 'Ecuador', code: 'EC'},
                             {name: 'Egypt', code: 'EG'},
                             {name: 'El Salvador', code: 'SV'},
                             {name: 'Equatorial Guinea', code: 'GQ'},
                             {name: 'Eritrea', code: 'ER'},
                             {name: 'Estonia', code: 'EE'},
                             {name: 'Ethiopia', code: 'ET'},
                             {name: 'Falkland Islands (Malvinas)', code: 'FK'},
                             {name: 'Faroe Islands', code: 'FO'},
                             {name: 'Fiji', code: 'FJ'},
                             {name: 'Finland', code: 'FI'},
                             {name: 'France', code: 'FR'},
                             {name: 'French Guiana', code: 'GF'},
                             {name: 'French Polynesia', code: 'PF'},
                             {name: 'French Southern Territories', code: 'TF'},
                             {name: 'Gabon', code: 'GA'},
                             {name: 'Gambia', code: 'GM'},
                             {name: 'Georgia', code: 'GE'},
                             {name: 'Germany', code: 'DE'},
                             {name: 'Ghana', code: 'GH'},
                             {name: 'Gibraltar', code: 'GI'},
                             {name: 'Greece', code: 'GR'},
                             {name: 'Greenland', code: 'GL'},
                             {name: 'Grenada', code: 'GD'},
                             {name: 'Guadeloupe', code: 'GP'},
                             {name: 'Guam', code: 'GU'},
                             {name: 'Guatemala', code: 'GT'},
                             {name: 'Guernsey', code: 'GG'},
                             {name: 'Guinea', code: 'GN'},
                             {name: 'Guinea-Bissau', code: 'GW'},
                             {name: 'Guyana', code: 'GY'},
                             {name: 'Haiti', code: 'HT'},
                             {name: 'Heard Island and Mcdonald Islands', code: 'HM'},
                             {name: 'Holy See (Vatican City State)', code: 'VA'},
                             {name: 'Honduras', code: 'HN'},
                             {name: 'Hong Kong', code: 'HK'},
                             {name: 'Hungary', code: 'HU'},
                             {name: 'Iceland', code: 'IS'},
                             {name: 'India', code: 'IN'},
                             {name: 'Indonesia', code: 'ID'},
                             {name: 'Iran, Islamic Republic Of', code: 'IR'},
                             {name: 'Iraq', code: 'IQ'},
                             {name: 'Ireland', code: 'IE'},
                             {name: 'Isle of Man', code: 'IM'},
                             {name: 'Israel', code: 'IL'},
                             {name: 'Italy', code: 'IT'},
                             {name: 'Jamaica', code: 'JM'},
                             {name: 'Japan', code: 'JP'},
                             {name: 'Jersey', code: 'JE'},
                             {name: 'Jordan', code: 'JO'},
                             {name: 'Kazakhstan', code: 'KZ'},
                             {name: 'Kenya', code: 'KE'},
                             {name: 'Kiribati', code: 'KI'},
                             {name: 'Korea, Democratic People\'s Republic of', code: 'KP'},
                             {name: 'Korea, Republic of', code: 'KR'},
                             {name: 'Kuwait', code: 'KW'},
                             {name: 'Kyrgyzstan', code: 'KG'},
                             {name: 'Lao People\'s Democratic Republic', code: 'LA'},
                             {name: 'Latvia', code: 'LV'},
                             {name: 'Lebanon', code: 'LB'},
                             {name: 'Lesotho', code: 'LS'},
                             {name: 'Liberia', code: 'LR'},
                             {name: 'Libyan Arab Jamahiriya', code: 'LY'},
                             {name: 'Liechtenstein', code: 'LI'},
                             {name: 'Lithuania', code: 'LT'},
                             {name: 'Luxembourg', code: 'LU'},
                             {name: 'Macao', code: 'MO'},
                             {name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK'},
                             {name: 'Madagascar', code: 'MG'},
                             {name: 'Malawi', code: 'MW'},
                             {name: 'Malaysia', code: 'MY'},
                             {name: 'Maldives', code: 'MV'},
                             {name: 'Mali', code: 'ML'},
                             {name: 'Malta', code: 'MT'},
                             {name: 'Marshall Islands', code: 'MH'},
                             {name: 'Martinique', code: 'MQ'},
                             {name: 'Mauritania', code: 'MR'},
                             {name: 'Mauritius', code: 'MU'},
                             {name: 'Mayotte', code: 'YT'},
                             {name: 'Mexico', code: 'MX'},
                             {name: 'Micronesia, Federated States of', code: 'FM'},
                             {name: 'Moldova, Republic of', code: 'MD'},
                             {name: 'Monaco', code: 'MC'},
                             {name: 'Mongolia', code: 'MN'},
                             {name: 'Montserrat', code: 'MS'},
                             {name: 'Morocco', code: 'MA'},
                             {name: 'Mozambique', code: 'MZ'},
                             {name: 'Myanmar', code: 'MM'},
                             {name: 'Namibia', code: 'NA'},
                             {name: 'Nauru', code: 'NR'},
                             {name: 'Nepal', code: 'NP'},
                             {name: 'Netherlands', code: 'NL'},
                             {name: 'Netherlands Antilles', code: 'AN'},
                             {name: 'New Caledonia', code: 'NC'},
                             {name: 'New Zealand', code: 'NZ'},
                             {name: 'Nicaragua', code: 'NI'},
                             {name: 'Niger', code: 'NE'},
                             {name: 'Nigeria', code: 'NG'},
                             {name: 'Niue', code: 'NU'},
                             {name: 'Norfolk Island', code: 'NF'},
                             {name: 'Northern Mariana Islands', code: 'MP'},
                             {name: 'Norway', code: 'NO'},
                             {name: 'Oman', code: 'OM'},
                             {name: 'Pakistan', code: 'PK'},
                             {name: 'Palau', code: 'PW'},
                             {name: 'Palestinian Territory, Occupied', code: 'PS'},
                             {name: 'Panama', code: 'PA'},
                             {name: 'Papua New Guinea', code: 'PG'},
                             {name: 'Paraguay', code: 'PY'},
                             {name: 'Peru', code: 'PE'},
                             {name: 'Philippines', code: 'PH'},
                             {name: 'Pitcairn', code: 'PN'},
                             {name: 'Poland', code: 'PL'},
                             {name: 'Portugal', code: 'PT'},
                             {name: 'Puerto Rico', code: 'PR'},
                             {name: 'Qatar', code: 'QA'},
                             {name: 'Reunion', code: 'RE'},
                             {name: 'Romania', code: 'RO'},
                             {name: 'Russian Federation', code: 'RU'},
                             {name: 'Rwanda', code: 'RW'},
                             {name: 'Saint Helena', code: 'SH'},
                             {name: 'Saint Kitts and Nevis', code: 'KN'},
                             {name: 'Saint Lucia', code: 'LC'},
                             {name: 'Saint Pierre and Miquelon', code: 'PM'},
                             {name: 'Saint Vincent and the Grenadines', code: 'VC'},
                             {name: 'Samoa', code: 'WS'},
                             {name: 'San Marino', code: 'SM'},
                             {name: 'Sao Tome and Principe', code: 'ST'},
                             {name: 'Saudi Arabia', code: 'SA'},
                             {name: 'Senegal', code: 'SN'},
                             {name: 'Serbia and Montenegro', code: 'CS'},
                             {name: 'Seychelles', code: 'SC'},
                             {name: 'Sierra Leone', code: 'SL'},
                             {name: 'Singapore', code: 'SG'},
                             {name: 'Slovakia', code: 'SK'},
                             {name: 'Slovenia', code: 'SI'},
                             {name: 'Solomon Islands', code: 'SB'},
                             {name: 'Somalia', code: 'SO'},
                             {name: 'South Africa', code: 'ZA'},
                             {name: 'South Georgia and the South Sandwich Islands', code: 'GS'},
                             {name: 'Spain', code: 'ES'},
                             {name: 'Sri Lanka', code: 'LK'},
                             {name: 'Sudan', code: 'SD'},
                             {name: 'Suriname', code: 'SR'},
                             {name: 'Svalbard and Jan Mayen', code: 'SJ'},
                             {name: 'Swaziland', code: 'SZ'},
                             {name: 'Sweden', code: 'SE'},
                             {name: 'Switzerland', code: 'CH'},
                             {name: 'Syrian Arab Republic', code: 'SY'},
                             {name: 'Taiwan, Province of China', code: 'TW'},
                             {name: 'Tajikistan', code: 'TJ'},
                             {name: 'Tanzania, United Republic of', code: 'TZ'},
                             {name: 'Thailand', code: 'TH'},
                             {name: 'Timor-Leste', code: 'TL'},
                             {name: 'Togo', code: 'TG'},
                             {name: 'Tokelau', code: 'TK'},
                             {name: 'Tonga', code: 'TO'},
                             {name: 'Trinidad and Tobago', code: 'TT'},
                             {name: 'Tunisia', code: 'TN'},
                             {name: 'Turkey', code: 'TR'},
                             {name: 'Turkmenistan', code: 'TM'},
                             {name: 'Turks and Caicos Islands', code: 'TC'},
                             {name: 'Tuvalu', code: 'TV'},
                             {name: 'Uganda', code: 'UG'},
                             {name: 'Ukraine', code: 'UA'},
                             {name: 'United Arab Emirates', code: 'AE'},
                             {name: 'United Kingdom', code: 'GB'},
                             {name: 'United States', code: 'US'},
                             {name: 'United States Minor Outlying Islands', code: 'UM'},
                             {name: 'Uruguay', code: 'UY'},
                             {name: 'Uzbekistan', code: 'UZ'},
                             {name: 'Vanuatu', code: 'VU'},
                             {name: 'Venezuela', code: 'VE'},
                             {name: 'Vietnam', code: 'VN'},
                             {name: 'Virgin Islands, British', code: 'VG'},
                             {name: 'Virgin Islands, U.S.', code: 'VI'},
                             {name: 'Wallis and Futuna', code: 'WF'},
                             {name: 'Western Sahara', code: 'EH'},
                             {name: 'Yemen', code: 'YE'},
                             {name: 'Zambia', code: 'ZM'},
                             {name: 'Zimbabwe', code: 'ZW'}
                             ];
    });
}.call(this);
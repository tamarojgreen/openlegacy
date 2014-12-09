(function() {

	'use strict';

	/* App Module */

	var olApp = angular.module('olApp',
			[ 'controllers', 'services', 'ngRoute', 'ui.router' ]).run(
			function($rootScope, $state) {
				$rootScope.allowHidePreloader = true;
				$rootScope.allowShowPreloader = true;

				$rootScope.showPreloader = function() {
					if ($rootScope.allowShowPreloader == true) {
						$(".preloader").show();
						$(".content-wrapper").hide();
						$rootScope.allowShowPreloader = false;
					}
				}

				$rootScope.hidePreloader = function() {
					if ($rootScope.allowHidePreloader == true) {
						$(".preloader").hide();
						$(".content-wrapper").show();
						$rootScope.allowShowPreloader = true;
					} else {
						$rootScope.allowHidePreloader = true;
					}
				}

				$rootScope.$on("$stateChangeStart", function() {
					$rootScope.showPreloader();
				});

				$rootScope.$on("$stateChangeSuccess", function() {
					$rootScope.hidePreloader();
				});
				
				$rootScope.$on("$stateChangeError", function(event, toState, toParams, fromState, fromParams, error) {			
					$rootScope.hidePreloader();					
					if (toState.name === "login") {
						$state.go(toParams.redirectTo.name);
					} else {
						$state.go("login", {"redirectTo":{"name": toState.name, "params":toParams}}, {"reload":true});
					}
				});	
			});

	olApp.config([ '$stateProvider', '$urlRouterProvider',
			function($stateProvider, $urlRouterProvider) {
		
				$urlRouterProvider.otherwise("/login");
				
				var header = { templateUrl: "views/partials/header.html", controller: "headerCtrl" };
				var isAuthFailed = false;
				
				var auth = function($q, $http) {					
					var deferred = $q.defer();
						$http(
							{						
								method: 'GET',
								data: '',
								url: olConfig.baseUrl + 'authenticate',												 
								headers: {
									'Content-Type' : 'application/json',
									'Accept' : 'application/json'
								}
							}).then(function() {
								deferred.resolve();
							}, function(response) {
								isAuthFailed = true;
								if (response.data) {
									alert(response.data.error);
								} else {
									alert(response.data);
								}
								deferred.reject();						
							});			 		
					
					
					return deferred.promise;
				}
				
				var authLogin = function($q, $http, $state) {
					var deferred = $q.defer();
					
					if (isAuthFailed) {
						isAuthFailed = false;
						deferred.resolve();
					} else {	
						if ($state.current.name != "" && $state.current.name != "logoff") {
							$http(
								{						
									method: 'GET',
									data: '',
									url: olConfig.baseUrl + 'authenticate',												 
									headers: {
										'Content-Type' : 'application/json',
										'Accept' : 'application/json'
									}
								}).then(function() {
									deferred.reject();
								}, function(response) {						
									deferred.resolve();						
								});
							
						} else {
							deferred.resolve();
						}
					}
					
					return deferred.promise;
				}
				
				$stateProvider.state('login', {
					url : '/login',
					views : {
						"" : {
							templateUrl : "views/login.html",
							controller : 'loginCtrl'
						}
					},
					params : {
						redirectTo : {
							name : "menu"
						}
					},
					resolve: {
						authLogin: authLogin
					}
				}).state('logoff', {
					url : '/logoff',
					views : {
						"" : {
							templateUrl : "views/logoff.html",
							controller : 'logoffCtrl'
						}
					}
				}).state('menu', {
					url : '/menu',
					views : {
						"" : {
							templateUrl : "views/menu.html",
							controller : 'menuCtrl'
						},
						"header" : header,
						"sideMenu" : {
							templateUrl : "views/partials/sideMenu.html",
							controller : "menuCtrl"
						}
					},
					resolve: { auth: auth }
				});
			} ]);
})();
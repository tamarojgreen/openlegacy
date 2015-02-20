(function() {

	'use strict';

	/* App Module */

	var olApp = angular.module('olApp',
			[ 'controllers', 'services', 'ngRoute', 'ui.router']).run(
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
						$state.go('menu');
					} else {
						$state.go("login", {"redirectTo":{"name": toState.name, "params":toParams}}, {"reload":true});
						
					}
				});	
			});

	olApp.config([ '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
		
				$urlRouterProvider.otherwise("/login");
				
				var header = { templateUrl: "views/partials/header.html", controller: "headerCtrl" };
				var sideMenu = { templateUrl: "views/partials/sideMenu.html", controller: "menuCtrl" };				

				var auth = function($q, $http, $rootScope, $state) {
					console.log('auth');
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
					}).then(function(response) {						
						deferred.resolve();
					}, function(response) {						
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
						
					if ($state.current.name != "" && $state.current.name != "logoff" && $state.current.name != "login") {
						deferred.reject();							
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
					resolve: { authLogin: authLogin	}
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
							templateUrl : "views/menu.html"							
						},
						"header" : header,
						"sideMenu" : sideMenu
					},
					resolve: { auth: auth }
				});
				
				function addRoute(stateName, entityName, ctrlName, url) {
					$stateProvider.state(stateName, {
						 url: url,
						 views: {
							 "": {
								 templateUrl: "views/" + entityName + ".html",
								 controller: ctrlName
							 },
							 "header": header,
							 "sideMenu": {
								 templateUrl: "views/partials/sideMenu.html",
								 controller: "menuCtrl"
							 }
						 },
						 resolve: { auth: auth },
						 params: {
							 keys:[]
						 }
					});
				}
				
				var urlsToFilter = [];

				/* Register controller place-holder start				
					<#if entityName??>					
						<#list actions as action>						
							<#switch action.actionName>
								<#case "READ">
									<#if keys?size &gt; 0>
										var url = "/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>";
										if ($.inArray(url, urlsToFilter) == -1) {
											addRoute("${entityName}Details", "${entityName}.edit", "${entityName}DetailsCtrl", url);
											urlsToFilter.push(url);
										}
									</#if>
									<#break>
								<#case "CREATE">
									var url = "/${entityName}/new";
									if ($.inArray(url, urlsToFilter) == -1) {
										addRoute("${entityName}New", "${entityName}.edit", "${entityName}NewCtrl", url);
										urlsToFilter.push(url);
									}
							</#switch>							
						</#list>
						var url = "/${entityName}";
						if ($.inArray(url, urlsToFilter) == -1) {
							addRoute("${entityName}", "${entityName}.list", "${entityName}Ctrl", url);
							urlsToFilter.push(url);
						}					
					</#if>	
				Register controller place-holder end */
				<#if entitiesDefinitions??>
					<#list entitiesDefinitions as entityDefinition>
						<#list entityDefinition.actions as action>
							<#if action.actionName == "CREATE">
								var url = "/${entityDefinition.entityName}/new";
								if ($.inArray(url, urlsToFilter) == -1) {
									addRoute("${entityDefinition.entityName}New", "${entityDefinition.entityName}.edit", "${entityDefinition.entityName}NewCtrl", url);
								}
							</#if>
						</#list>
						<#list entityDefinition.actions as action>
							<#if action.actionName == "READ" && entityDefinition.keys?size &gt; 0>
								var url = "/${entityDefinition.entityName}/:<#list entityDefinition.keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>";
								if ($.inArray(url, urlsToFilter) == -1) {
									addRoute("${entityDefinition.entityName}Details", "${entityDefinition.entityName}.edit", "${entityDefinition.entityName}DetailsCtrl", url);
								}
							</#if>
						</#list>
						var url = "/${entityDefinition.entityName}";
						if ($.inArray(url, urlsToFilter) == -1) {
							addRoute("${entityDefinition.entityName}", "${entityDefinition.entityName}.list", "${entityDefinition.entityName}Ctrl", url);
						}
					</#list>
				</#if>	
			} ]);
})();
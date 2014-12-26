( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', ['controllers', 'services', 'directives', 'ngRoute', 'ui.router']).run(['$themeService', '$rootScope', '$state', function($themeService, $rootScope, $state) {
		$rootScope.allowHidePreloader = true;
		$rootScope.allowShowPreloader = true;		
		
		$rootScope._showPreloader = false;
		$rootScope._showContent = true;
		
		$rootScope.showPreloader = function(hideContent) {
			if ($rootScope.allowShowPreloader == true) {
				$rootScope._showPreloader = true;				
				$rootScope._showContent = hideContent !== undefined ? !hideContent : false;
			}		
		}
		
		$rootScope.hidePreloader = function() {						
			if ($rootScope.allowHidePreloader == true) {
				$rootScope._showPreloader = false;
				$rootScope._showContent = true;
			}			
		}
		if ($("#sessionImage") != null){
			$("#sessionImage").attr("src","../sessionViewer/image?" + new Date().getTime());
		}
		
		
		$rootScope.$on("$stateChangeError", function(event, toState, toParams, fromState, fromParams, error) {			
			$rootScope.hidePreloader();
			if (toState.name === "login") {
				$state.go(toParams.redirectTo.name);
			} else {
				$state.go("login", {"redirectTo":{"name": toState.name, "params":toParams}}, {"reload":true});
			}
		});		
		
		$rootScope.changeTheme = function() {
			$themeService.changeTheme();
		};
		$rootScope.theme = $themeService.getCurrentTheme();
	}]);	

	olApp.config( ['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
		
		$urlRouterProvider.otherwise("/login");
		
		var header = { templateUrl: "views/partials/header.html", controller: "headerCtrl" };
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
			
			return deferred.promise;
		}
		 
		 $stateProvider
		 .state('login', {
			 url: '/login',
			 views: {
				 "": {
					 templateUrl: "views/login.html",
					 controller: 'loginCtrl'
				 }
			 },
			 params: {
				 redirectTo: {
					 name: "menu"
				 }
			 },
			 resolve: {
				 authLogin: authLogin
			 }
		})
		 .state('logoff', {
			 url: '/logoff',
			 views: {
				 "": {
					 templateUrl: "views/logoff.html",
					 controller: 'logoffCtrl'
				 }
			 }		     
		})
		 .state('menu', {
			 url: '/menu',
			 views: {
				 "": {
					 templateUrl: "views/menu.html",
					 controller: 'menuCtrl'
				 },
				 "header": header,
				 "sideMenu": {
					 templateUrl: "views/partials/sideMenu.html",
					 controller: "menuCtrl"
				 }
			 },
			 resolve: { auth: auth }
		});
		
		 function addRoute(stateName, entityName, url) {
				$stateProvider.state(stateName, {
					 url: url,
					 views: {
						 "": {
							 templateUrl: "views/" + entityName + ".html",
							 controller: entityName + 'Ctrl'
						 },
						 "header": header,
						 "breadcrumbs": {
							 templateUrl: "views/partials/breadcrumbs.html",
							 controller: "breadcrumbsCtrl"
						 },
						 "sideMenu": {
							 templateUrl: "views/partials/sideMenu.html",
							 controller: "menuCtrl"
						 }
					 },
					 resolve: { auth: auth }
				});
			}		 
			
			var urlsToFilter = [];

			addRoute("emulation", "emulation", "/emulation");
			/* Register controller place-holder start
			<#if entityName??>
				<#if keys?size &gt; 0>				
					addRoute("${entityName}WithKey", "${entityName}", "/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>");
					urlsToFilter.push('/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>');				
				</#if>			
				addRoute("${entityName}", "${entityName}", "/${entityName}");
				urlsToFilter.push('/${entityName}');
			</#if>
			Register controller place-holder end */									
			<#if entitiesDefinitions??>
				<#list entitiesDefinitions as entityDefinition>				
					<#if entityDefinition.keys?size &gt; 0>					
						var url = "/${entityDefinition.entityName}/:<#list entityDefinition.keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>";
						if ($.inArray(url, urlsToFilter) == -1) {
							addRoute("${entityDefinition.entityName}WithKey", "${entityDefinition.entityName}", url);
						}
					</#if>
					var url = "/${entityDefinition.entityName}";
					if ($.inArray(url, urlsToFilter) == -1) {
						addRoute("${entityDefinition.entityName}", "${entityDefinition.entityName}", url);
					}				
				</#list>
			</#if>	
			
		} ] );
} )();

//function appOnLoad($cookies,$rootScope,$location,$olHttp){
//	// fix relative URL's
//	if (olConfig.baseUrl.indexOf("http" < 0)){
//		olConfig.baseUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + olConfig.baseUrl;
//	}
//	
//	if ($cookies.loggedInUser != null){
//		$rootScope.loggedInUser = $cookies.loggedInUser;
//		//$location.path("/TviotParams");
//	}
////	$olHttp.get("menu",function(data){
//	//	$rootScope.menus = data.simpleMenuItem.menuItems;
//	//});
//}


function search(baseUrl){
	var url = baseUrl;
	$("#keys :input").each(function(i){
		url = url + $(this).val() + "+";
	});
	url = url.substring(0,url.length-1);
	location.href = url;
}

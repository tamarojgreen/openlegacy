( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', ['controllers', 'services', 'directives', 'ngRoute', 'ui.router']).run(['$themeService', '$rootScope', function($themeService, $rootScope) {
		$rootScope.allowHidePreloader = true;
		$rootScope.allowShowPreloader = true;
		
		$rootScope.showPreloader = function() {
			if ($rootScope.allowShowPreloader == true) {
				console.log("location start");
				$(".preloader").show();
				$(".content-wrapper").hide();
				$rootScope.allowShowPreloader = false;
			}		
		}
		
		$rootScope.hidePreloader = function() {		
			if ($rootScope.allowHidePreloader == true) {
				console.log("location end");
				$(".preloader").hide();
				$(".content-wrapper").show();
				$rootScope.allowShowPreloader = true;
			} else {
				$rootScope.allowHidePreloader = true;
			}		
		}
		
		$rootScope.$on("$locationChangeStart", function(){		
			$rootScope.showPreloader();
		});
		
		$rootScope.$on("$locationChangeSuccess", function(){		
			$rootScope.hidePreloader();
		});		
		
		$rootScope.changeTheme = function() {
			$themeService.changeTheme();
		};
		$rootScope.theme = $themeService.getCurrentTheme();
	}]);	

	olApp.config( ['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
		
		$urlRouterProvider.otherwise("/login");
		 
		 $stateProvider
		 .state('login', {
			 url: '/login',
			 views: {
				 "": {
					 templateUrl: "views/login.html",
					 controller: 'loginCtrl'
				 },
				 "header": {
					 templateUrl: "views/partials/header.html",
					 controller: "headerCtrl"
				 }
			 }		     
		})
		 .state('logoff', {
			 url: '/logoff',
			 views: {
				 "": {
					 templateUrl: "views/logoff.html",
					 controller: 'logoffCtrl'
				 },
				 "header": {
					 templateUrl: "views/partials/header.html",
					 controller: "headerCtrl"
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
				 "header": {
					 templateUrl: "views/partials/header.html",
					 controller: "headerCtrl"
				 },
				 "sideMenu": {
					 templateUrl: "views/partials/sideMenu.html",
					 controller: "menuCtrl"
				 }
			 }		     
		});
		
		 function addRoute(stateName, entityName, url) {
				$stateProvider.state(stateName, {
					 url: url,
					 views: {
						 "": {
							 templateUrl: "views/" + entityName + ".html",
							 controller: entityName + 'Ctrl'
						 },
						 "header": {
							 templateUrl: "views/partials/header.html",
							 controller: "headerCtrl"
						 },
						 "breadcrumbs": {
							 templateUrl: "views/partials/breadcrumbs.html",
							 controller: "breadcrumbsCtrl"
						 },
						 "sideMenu": {
							 templateUrl: "views/partials/sideMenu.html",
							 controller: "menuCtrl"
						 }
					 }		     
				});
			}		 
			
			var urlsToFilter = [];

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
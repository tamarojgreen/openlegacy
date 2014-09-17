( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'controllers', 'services', 'directives'] ).run(['$themeService', '$rootScope', function($themeService, $rootScope) {		
		$rootScope.changeTheme = function() {
			$themeService.changeTheme();
		};
		$rootScope.theme = $themeService.getCurrentTheme();
	}]);	

	olApp.config( [ '$routeProvider', function( $routeProvider ) {
		
		$routeProvider = $routeProvider.when( '/login', {templateUrl: 'views/login.html', controller: 'loginController'} );
		$routeProvider = $routeProvider.when( '/logoff', {templateUrl: 'views/logoff.html', controller: 'logoffController'} );
		$routeProvider = $routeProvider.when( '/menu', {templateUrl: 'views/menu.html'} );
		//$routeProvider = $routeProvider.when( '/ItemDetails', {templateUrl: 'views/ItemDetails.html',controller: 'ItemDetailsController'} );
		<#if entitiesDefinitions??>
			<#list entitiesDefinitions as entityDefinition>
			$routeProvider = $routeProvider.when( '/${entityDefinition.entityName}', {templateUrl: 'views/${entityDefinition.entityName}.html', controller: '${entityDefinition.entityName}Controller'} );
			<#if entityDefinition.keys?size &gt; 0>
			$routeProvider = $routeProvider.when( '/${entityDefinition.entityName}/:<#list entityDefinition.keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>', {templateUrl: 'views/${entityDefinition.entityName}.html', controller: '${entityDefinition.entityName}Controller'} );
			</#if>
			</#list>
		</#if>
		
// auto generated register start - Items
				$routeProvider = $routeProvider.when( '/Items', {templateUrl: 'views/Items.html', controller: 'ItemsController'} );
// auto generated register end - Items
/* Register controller place-holder start
		<#if entityName??>
			<#if keys?size &gt; 0>
$routeProvider = $routeProvider.when( '/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>', {templateUrl: 'views/${entityName}.html', controller: '${entityName}Controller'} );
			</#if>
		$routeProvider = $routeProvider.when( '/${entityName}', {templateUrl: 'views/${entityName}.html', controller: '${entityName}Controller'} );
		</#if>
		Register controller place-holder end */
		
		$routeProvider = $routeProvider.otherwise( {redirectTo: '/login'} );
		
	} ] );
} )();

function appOnLoad($cookies,$rootScope,$location,$olHttp){
	// fix relative URL's
	if (olConfig.baseUrl.indexOf("http" < 0)){
		olConfig.baseUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + olConfig.baseUrl;
	}
	
	if ($cookies.loggedInUser != null){
		$rootScope.loggedInUser = $cookies.loggedInUser;
		//$location.path("/TviotParams");
	}
//	$olHttp.get("menu",function(data){
	//	$rootScope.menus = data.simpleMenuItem.menuItems;
	//});
}


function search(baseUrl){
	var url = baseUrl;
	$("#keys :input").each(function(i){
		url = url + $(this).val() + "+";
	});
	url = url.substring(0,url.length-1);
	location.href = url;
}
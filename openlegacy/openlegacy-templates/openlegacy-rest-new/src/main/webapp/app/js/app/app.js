( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'ngCookies','controllers', 'services'] );

	olApp.config( [ '$routeProvider', function( $routeProvider) {
		
		$routeProvider = $routeProvider.when( '/login', {templateUrl: 'views/login.html', controller: 'loginController'} );
		$routeProvider = $routeProvider.when( '/logoff', {templateUrl: 'views/logoff.html', controller: 'logoffController'} );
		$routeProvider = $routeProvider.when( '/menu', {templateUrl: 'views/menu.html'} );
		$routeProvider = $routeProvider.when( '/TviotByTvia', {templateUrl: 'views/TviotByTvia.html', controller: 'TviotByTviaController'} );
		$routeProvider = $routeProvider.when( '/TviotByPolisa', {templateUrl: 'views/TviotByPolisa.html', controller: 'TviotByPolisaController'} );
		$routeProvider = $routeProvider.when( '/NetuneyTvia/:misparTvia', {templateUrl: 'views/NetuneyTvia.html', controller: 'NetuneyTviaController'} );

	
// auto generated register start - TviotParams
				$routeProvider = $routeProvider.when( '/TviotParams', {templateUrl: 'views/TviotParams.html', controller: 'TviotParamsController'} );
// auto generated register end - TviotParams
/* Register controller place-holder start
		<#if keys?size &gt; 0>
$routeProvider = $routeProvider.when( '/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>', {templateUrl: 'views/${entityName}.html', controller: '${entityName}Controller'} );
		</#if>
		$routeProvider = $routeProvider.when( '/${entityName}', {templateUrl: 'views/${entityName}.html', controller: '${entityName}Controller'} );
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
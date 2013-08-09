( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'ngCookies','controllers', 'services'] );

	olApp.config( [ '$routeProvider', function( $routeProvider) {
		
		$routeProvider = $routeProvider.when( '/login', {templateUrl: 'views/login.html', controller: 'loginController'} );
		$routeProvider = $routeProvider.when( '/logoff', {templateUrl: 'views/logoff.html', controller: 'logoffController'} );
		$routeProvider = $routeProvider.when( '/menu', {templateUrl: 'views/menu.html', controller: 'menuController'} );

		// Register controller place-holder
		
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
		$location.path("/menu");
	}
//	$olHttp.get("menu",function(data){
	//	$rootScope.menus = data.simpleMenuItem.menuItems;
	//});
}


( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'ngCookies','controllers', 'services', 'ui.router'] );

//	olApp.config( [ '$routeProvider', function( $routeProvider) {
//		
////		$routeProvider = $routeProvider.when( '/login', {templateUrl: 'views/login.html', controller: 'loginController'} );
//		$routeProvider = $routeProvider.when( '/logoff', {templateUrl: 'views/logoff.html', controller: 'logoffController'} );
//		$routeProvider = $routeProvider.when( '/mainMenu', {templateUrl: 'views/menu.html'});
//		$routeProvider = $routeProvider.when( '/items', {templateUrl: 'views/items.html', controller: 'itemsController'} );
//
//		$routeProvider = $routeProvider.when( '/itemDetails/:itemNumber', {templateUrl: 'views/itemDetails.html',controller: 'itemDetailsController'});
//		
//		$routeProvider = $routeProvider.otherwise( {redirectTo: '/login'} );
//		
//	} ] );
	
	olApp.config(function($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise("/Items");
		$stateProvider
			.state('login', {
				url: "/login",
				templateUrl: "views/login.html",
				controller: "loginCtrl"			    
		    })
		    .state('items', {
		    	url: "/items",
	    		templateUrl: "views/items.html",
		    	controller: "itemsCtrl"		    	    	 
		    });
	});
} )();




function appOnLoad($cookies,$rootScope,$location,$olHttp){	
	// fix relative URL's
	if (olConfig.baseUrl.indexOf("http" < 0)){
		olConfig.baseUrl = $location.protocol() + "://" + $location.host() + ":" + $location.port() + olConfig.baseUrl;
	}
	$cookies.loggedInUser = null;
	if ($cookies.loggedInUser != null){
		$rootScope.loggedInUser = $cookies.loggedInUser;
		$location.path("/items");
	}
//	$olHttp.get("menu",function(data){
	//	$rootScope.menus = data.simpleMenuItem.menuItems;
	//});
}


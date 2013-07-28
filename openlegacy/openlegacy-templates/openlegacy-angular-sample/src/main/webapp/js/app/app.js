( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'ngCookies','controllers', 'services'] );

	olApp.config( [ '$routeProvider', function( $routeProvider) {
		
		$routeProvider = $routeProvider.when( '/login', {templateUrl: 'views/login.html', controller: 'loginController'} );
		$routeProvider = $routeProvider.when( '/logoff', {templateUrl: 'views/logoff.html', controller: 'logoffController'} );
		$routeProvider = $routeProvider.when( '/mainMenu', {templateUrl: 'views/menu.html'});
		$routeProvider = $routeProvider.when( '/items', {templateUrl: 'views/items.html', controller: 'itemsController'} );

		$routeProvider = $routeProvider.when( '/itemDetails/:itemNumber', {templateUrl: 'views/itemDetails.html',controller: 'itemDetailsController'});
		
		$routeProvider = $routeProvider.otherwise( {redirectTo: '/login'} );
		
		olApp.directive('myfield', function(){
			  return {
			    template: '<div>ROI</div>'
			  };
		});
	} ] );
} )();

function appOnLoad($cookies,$rootScope,$location,$olHttp){
	if ($cookies.loggedInUser != null){
		$rootScope.loggedInUser = $cookies.loggedInUser;
		$location.path("/items");
	}
	$olHttp.get("menu",function(data){
		$rootScope.menus = data.simpleMenuItem.menuItems;
	});
}


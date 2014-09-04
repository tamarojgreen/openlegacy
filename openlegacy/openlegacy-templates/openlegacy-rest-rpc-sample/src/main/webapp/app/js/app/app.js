( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'ngCookies','controllers', 'services', 'ui.router'] )
	.run(['$rootScope', '$themeService', function ($rootScope, $themeService) {		
		$rootScope.theme = $themeService.getCurrentTheme();		
	}]);
	
	olApp.config(function($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise("/items");
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
		    })
			.state('mainMenu', {
		    	url: "/mainMenu",
	    		templateUrl: "views/mainMenu.html"		    	    	    	 
		    })
		    .state('inventoryMenu', {
		    	url: "/inventoryMenu",
	    		templateUrl: "views/inventoryMenu.html"		    	    	    	 
		    })
			.state('itemDetails', {
		    	url: "/itemDetails/:id",
	    		templateUrl: "views/itemDetails.html",
	    		controller: "itemDetailsCtrl"
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


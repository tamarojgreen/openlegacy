( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', [ 'ngCookies','controllers', 'services', 'ui.router'] )
	.run(['$rootScope', '$themeService', function ($rootScope, $themeService) {		
		$rootScope.theme = $themeService.getCurrentTheme();		
	}]);
	
	olApp.config(function($stateProvider, $urlRouterProvider) {
		$urlRouterProvider.otherwise("/stockItem");
		$stateProvider
			.state('login', {
				url: "/login",
				templateUrl: "views/login.html",
				controller: "loginCtrl"			    
		    })
		    .state('logoff', {
				url: "/logoff",
				templateUrl: "views/logoff.html",
				controller: "logoffCtrl"			    
		    })
		    .state('stockItem', {
		    	url: "/stockItem",
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
		    	url: "/StockItem/:id",
	    		templateUrl: "views/itemDetails.html",
	    		controller: "itemDetailsCtrl"
		    })
		    .state('productTree', {
				url: "/productTree",
				templateUrl: "views/productTree.html",
				controller: "productTreeCtrl"			    
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
		$location.path("/stockItem");
	}
//	$olHttp.get("menu",function(data){
	//	$rootScope.menus = data.simpleMenuItem.menuItems;
	//});
}


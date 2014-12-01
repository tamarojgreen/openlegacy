var olApp = angular.module('olApp', [
    'ui.router',
    'ngTouch',
    'controllers',
    'ngCookies',
    'services',
    "ui.bootstrap"
]).run(['$rootScope', '$state', '$themeService', function ($rootScope, $state, $themeService) {
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
	
	$rootScope.$on("$locationChangeStart", function(){		
		$rootScope.showPreloader();
	});
	
	$rootScope.$on("$locationChangeSuccess", function(){		
		$rootScope.hidePreloader();
	});
	
    $rootScope.theme = $themeService.getCurrentTheme();
}]);
 
olApp.config(function($stateProvider, $urlRouterProvider) {
    // For any unmatched url, redirect to /state1	 
    $urlRouterProvider.otherwise("/Items");
    // Now set up the states
    $stateProvider
    .state('logoff', {
    	url: '/logoff',    	 
        templateUrl: "views/logoff.html",
        controller: 'logoffController'
    })
    .state('Items', {
        url: "/Items",         
        templateUrl: "partials/Items.html",
        controller: 'itemListCtrl'            
    })
    .state('ItemDetails', {
        url: "/ItemDetails/",         
	    templateUrl: "partials/ItemDetails.html",
	    controller: 'itemDetailsCtrl'
    })
    .state('ItemDetailsWithId', {
        url: "/ItemDetails/:itemId",         
        templateUrl: "partials/ItemDetails.html",
        controller: 'itemDetailsCtrl'            
    })
    .state('Warehouses', {
        url: "/Warehouses",         
        templateUrl: "partials/Warehouses.html",
        controller: 'warehouseListCtrl'            
    })
    .state('WarehouseDetails', {
        url: "/WarehouseDetails",         
	    templateUrl: "partials/WarehouseDetails.html",
	    controller: 'warehouseDetailsCtrl'
    })
    .state('WarehouseDetailsWithId', {
        url: "/WarehouseDetails/:warehouseId",         
        templateUrl: "partials/WarehouseDetails.html",
        controller: 'warehouseDetailsCtrl'            
    })
    .state('WarehouseTypes', {
        url: "/WarehouseTypes",         
        templateUrl: "partials/WarehouseTypes.html",
        controller: 'warehouseTypesCtrl'            
    })
    .state('login', {
        url: "/login",         
        templateUrl: "partials/login.html",
        controller: 'loginController'            
    })
    .state('menu', {
    	url: "/MainMenu",    	
    	templateUrl: "partials/MainMenu.html"    		
    })
    .state('InventoryMenu', {
    	url: "/InventoryMenu",    	
		templateUrl: "partials/InventoryMenu.html"
    })    
});

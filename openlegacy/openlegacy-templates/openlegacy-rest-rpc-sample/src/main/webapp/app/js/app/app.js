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
		    .state('logoff', {
				url: "/logoff",
				templateUrl: "views/logoff.html",
				controller: "logoffCtrl"			    
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
	
	olApp.directive('datepicker', function() {
	    return {
	        restrict: 'A',
	        require : 'ngModel',
	        link : function (scope, element, attrs, ngModelCtrl) {
	            $(function(){
	            	element.datepicker({                    
	                }).on("changeDate", function(ev) {
	            	    ngModelCtrl.$setViewValue(ev.date);            	    
	                });
	            	ngModelCtrl.$formatters.unshift(function (modelValue) {	            		
	            		if (modelValue != null && modelValue != "" ) {
	            			var date = new Date(modelValue);	            			
	            			element.datepicker("setValue", date);
	            			
	            			console.log(date);
	            			
	            			return ("0" + (date.getMonth() + 1)).slice(-2) + "/" + ("0" + (date.getDate())).slice(-2) + "/" + date.getFullYear();            			
	            		} else {
	            			return modelValue;
	            		}               		
	                });
	            	
	            	ngModelCtrl.$parsers.push(function (viewValue) {            		
	            		return Date.parse(viewValue)/1000;
	            	});
	            });
	        }
	    }
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


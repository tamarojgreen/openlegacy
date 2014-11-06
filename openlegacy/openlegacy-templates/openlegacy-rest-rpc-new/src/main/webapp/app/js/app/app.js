( function() {

	'use strict';

	/* App Module */
	
	var olApp = angular.module( 'olApp', ['controllers', 'services'] ).run(['$themeService', '$rootScope', function($themeService, $rootScope) {
		$rootScope.theme = $themeService.getCurrentTheme();
	}]);

	olApp.config( [ '$routeProvider', function( $routeProvider) {
		
		$routeProvider = $routeProvider.when( '/login', {templateUrl: 'views/login.html', controller: 'loginController'} );
		$routeProvider = $routeProvider.when( '/logoff', {templateUrl: 'views/logoff.html', controller: 'logoffController'} );
		$routeProvider = $routeProvider.when( '/menu', {templateUrl: 'views/menu.html'} );
		$routeProvider = $routeProvider.when( '/inventoryMenu', {templateUrl: 'inventoryMenu.html'} );
		
		var urlsToFilter = [];

		/* Register controller place-holder start
		<#if entityName??>
			<#if keys?size &gt; 0>
				$routeProvider = $routeProvider.when( '/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>', {templateUrl: 'views/${entityName}.html', controller: '${entityName}Controller'} );
				urlsToFilter.push('/${entityName}/:<#list keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>');				
			</#if>
			$routeProvider = $routeProvider.when( '/${entityName}', {templateUrl: 'views/${entityName}.html', controller: '${entityName}Controller'} );			
			urlsToFilter.push('/${entityName}');
		</#if>
		Register controller place-holder end */
									
		<#if entitiesDefinitions??>
			<#list entitiesDefinitions as entityDefinition>
					var url = "/${entityDefinition.entityName}";
					if ($.inArray(url, urlsToFilter) == -1) {
						$routeProvider = $routeProvider.when( url, {templateUrl: 'views/${entityDefinition.entityName}.html', controller: '${entityDefinition.entityName}Controller'} );
					}
					<#if entityDefinition.keys?size &gt; 0>
						url = "/${entityDefinition.entityName}/:<#list entityDefinition.keys as key>${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list>";
						if ($.inArray(url, urlsToFilter) == -1) {
							$routeProvider = $routeProvider.when( url, {templateUrl: 'views/${entityDefinition.entityName}.html', controller: '${entityDefinition.entityName}Controller'} );
						}
					</#if>
				
			</#list>
		</#if>
		
		$routeProvider = $routeProvider.otherwise( {redirectTo: '/login'} );
		
	} ] );
	
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
(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', []);

	module = module.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$scope.user = {userName:"elbpr",password:"el%23978bpr1"};
			$scope.login = function() {
				$cookies.loggedInUser = $scope.user.userName;
				$rootScope.loggedInUser = $scope.user.userName;
				$olHttp.get('login?user=' + $scope.user.userName + '&password='+ $scope.user.password, 
					function() {
						$cookies.loggedInUser = $scope.user.userName;
						$rootScope.loggedInUser = $scope.user.userName;
						$location.path("/TviotParams");
					}
				);
			};
		});
	module = module.controller(
		'logoffController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$olHttp.get('logoff', 
				function() {
					delete $cookies['loggedInUser'];
					$scope.loggedInUser = null;
					$rootScope.loggedInUser = null;
				}
			);
		});

	// template for all entities 
	<#if entitiesDefinitions??>
	<#list entitiesDefinitions as entityDefinition>
	module = module.controller('${entityDefinition.entityName}Controller',
			function($scope, $location, $olHttp,$routeParams) {
				$scope.read = function(){
					$olHttp.get('${entityDefinition.entityName}/' <#if entityDefinition.keys?size &gt; 0>+ $routeParams.${entityDefinition.keys[0].name?replace(".", "_")}</#if>,
						function(data) {
							$scope.model = data.model.entity;
						}
					);
				};		
				<#list entityDefinition.actions as action>
				<#if action.alias != "read">
				$scope.${action.alias} = function(){
					$olHttp.post('${entityDefinition.entityName}?action=${action.alias}',$scope.model, 
						function(data) {
							if (data.model.entityName == '${entityDefinition.entityName}'){
								$scope.model = data.model.entity;
							}
							else{
								$location.path("/" + data.model.entityName);
							}
						}
					);
				};
				</#if>
				</#list>
				<#if entityDefinition.keys?size &gt; 0>
				if ($routeParams.${entityDefinition.keys[0].name?replace(".", "_")} != null && $routeParams.${entityDefinition.keys[0].name?replace(".", "_")}.length > 0){
					$scope.read();
				}
				<#else>
					$scope.read();
				</#if>

			});
	
	</#list>
	</#if>
	
/* Controller code place-holder start
	<#if entityName??>
	module = module.controller('${entityName}Controller',
		function($scope, $location, $olHttp,$routeParams) {
			$scope.read = function(){
				$olHttp.get('${entityName}/' <#if keys?size &gt; 0>+ $routeParams.${keys[0].name?replace(".", "_")}</#if>,
					function(data) {
						$scope.model = data.model.entity;
					}
				);
			};		
			<#list actions as action>
			<#if action.alias != "read">
			$scope.${action.alias} = function(){
				$olHttp.post('${entityName}?action=${action.alias}',$scope.model, 
					function(data) {
						if (data.model.entityName == '${entityName}'){
							$scope.model = data.model.entity;
						}
						else{
							$location.path("/" + data.model.entityName);
						}
					}
				);
			};
			</#if>
			</#list>
			<#if keys?size &gt; 0>
			if ($routeParams.${keys[0].name?replace(".", "_")} != null && $routeParams.${keys[0].name?replace(".", "_")}.length > 0){
				$scope.read();
			}
			<#else>
				$scope.read();
			</#if>

		});
	</#if>
	Controller code place-holder end */

	/* Controller with JSONP code place-holder start
	<#if entityName??>
	module = module.controller('${entityName}Controller',
		function($scope, $location, $http,$routeParams,$templateCache) {
			<#list actions as action>
			$scope.${action.alias} = function(){
				$http({method: 'JSONP', url: olConfig.hostUrl + '/${entityName}/${action.programPath}/'<#if keys?size &gt; 0> + </#if><#list keys as key>$routeParams.${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list> + '?callback=JSON_CALLBACK', cache: $templateCache}).
			      success(function(data, status) {
						$scope.${entityName?uncap_first} = data;
			      }).
			      error(function(data, status) {
			    	  alert("failed");
			    });
			};
			</#list>
			if ($routeParams.${keys[0].name?replace(".", "_")} != null){
				$scope.read();
			}
			
		});
	</#if>
 	Controller with JSONP code place-holder end */
})();

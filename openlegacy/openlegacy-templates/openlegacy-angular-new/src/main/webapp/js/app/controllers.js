(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', []);

	module = module.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$scope.user = {userName:"test",password:"testp"};
			$scope.login = function() {
				$cookies.loggedInUser = $scope.user.userName;
				$rootScope.loggedInUser = $scope.user.userName;
				$location.path("/menu");

				/* By pass the login
				$olHttp.get('login?user=' + $scope.user.userName + '&password='+ $scope.user.password, 
					function() {
						$cookies.loggedInUser = $scope.user.userName;
						$rootScope.loggedInUser = $scope.user.userName;
						$location.path("/items");
					}
				);*/
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
	
	/* Controller code place-holder start
	module = module.controller('${entityName}Controller',
		function($scope, $location, $olHttp,$routeParams) {
			<#list actions as action>
			$scope.${action.alias} = function(){
				$olHttp.get('${entityName}?action=${action.alias}',$scope.model, 
					function(data) {
						$scope.${entityName?uncap_first} = data.model.entity;
					}
				);
			};
			</#list>
			if ($routeParams.${keys[0].name?replace(".", "_")} != null && $routeParams.${keys[0].name?replace(".", "_")}.length > 0){
				if ($scope.read == null){
					alert("No READ action defined for entity");
				}
				$scope.read();
			}
			
			<@partActions partsDefinitions?values/>

	<#macro partActions parts>
		<#list parts as part>
			<#if part.occur &gt; 1>
				<#list part.actions as action>

		$scope.${action.targetEntityDefinition.entityName?uncap_first}_${action.alias} = function(item){
				$olHttp.get('${action.targetEntityDefinition.entityName?uncap_first}?action=${action.alias}',$scope.model, 
					function(data) {
						$scope.${action.targetEntityDefinition.entityName?uncap_first} = data.model.entity;
					}
				);
		    });
		};
				</#list>
			</#if>
		<@partActions part.innerPartsDefinitions?values/>
		</#list>
	</#macro>
		});
	Controller code place-holder end */

	/* Controller with JSONP code place-holder start
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
			
			<@partActions partsDefinitions?values/>
		});

	<#macro partActions parts>
		<#list parts as part>
			<#if part.occur &gt; 1>
				<#list part.actions as action>

		$scope.${action.targetEntityDefinition.entityName?uncap_first}_${action.alias} = function(key){
			$http({method: 'JSONP', url: olConfig.hostUrl + '/${part.partName}/${action.programPath}?key=' + key + '&callback=JSON_CALLBACK', cache: $templateCache}).
		      success(function(data, status) {
					$scope.${action.targetEntityDefinition.entityName?uncap_first} = data;
		      }).
		      error(function(data, status) {
		    	  alert("failed");
		    });
		};
		
				</#list>
			</#if>
		<@partActions part.innerPartsDefinitions?values/>
		</#list>
	</#macro>
	 Controller with JSONP code place-holder end */
})();

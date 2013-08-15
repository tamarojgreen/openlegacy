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
			$olHttp.get('${entityName}/' <#if keys?size &gt; 0> + </#if><#list keys as key>$routeParams.${key.name}<#if key_has_next>+ '+'</#if></#list>, 
					function(data) {
						$scope.${entityName?uncap_first} = data.model.entity;
					}
				);
			<#list actions as action>
			$scope.${action.alias} = function(){
				$olHttp.post('${entityName}?action=${action.alias}XX${action.programPath}',$scope.model, 
					function(data) {
						$scope.${entityName?uncap_first} = data.model.entity;
					}
				);
			};
			</#list>
			
		});
	Controller code place-holder end */
})();

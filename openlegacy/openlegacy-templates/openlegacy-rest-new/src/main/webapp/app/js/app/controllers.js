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


	
	module = module.controller('TviotByTviaController',
			function($scope, $location, $olHttp,$routeParams) {
				$olHttp.get('TviotByTvia', 
						function(data) {
							$scope.model = data.model.entity;
						}
					);
			});

	module = module.controller('TviotByPolisaController',
			function($scope, $location, $olHttp,$routeParams) {
				$olHttp.get('TviotByPolisa', 
						function(data) {
							$scope.model = data.model.entity;
						}
					);
				$scope.selectRow = function(){
					alert($scope.tvia);
				};
			});
	
// auto generated controller start - TviotParams
	module = module.controller('TviotParamsController',
		function($scope, $location, $olHttp,$routeParams) {
			$scope.read = function(){
				$olHttp.get('TviotParams/' ,
					function(data) {
						$scope.model = data.model.entity;
					}
				);
			};		
			$scope.find = function(){
				$scope.model.actions = null;
				$olHttp.post('TviotParams?action=find',$scope.model, 
					function(data) {
						if (data.model.entityName == 'TviotParams'){
							$scope.model = data.model.entity;
						}
						else{
							$location.path("/" + data.model.entityName);
						}
					}
				);
			};
				$scope.read();

		});// auto generated controller end - TviotParams
/* Controller code place-holder start
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
			$scope.${action.alias} = function(){
				$scope.model.actions = null;
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
			</#list>
			<#if keys?size &gt; 0>
			if ($routeParams.${keys[0].name?replace(".", "_")} != null && $routeParams.${keys[0].name?replace(".", "_")}.length > 0){
				$scope.read();
			}
			<#else>
				$scope.read();
			</#if>

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
			
		});

	 Controller with JSONP code place-holder end */
})();

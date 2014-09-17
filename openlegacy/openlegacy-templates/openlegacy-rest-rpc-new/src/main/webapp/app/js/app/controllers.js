(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', []);

	module = module.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			if ($cookies.loggedInUser != undefined) {
				$location.path("/Items");					
			}
			$scope.login = function(username, password) {
				$olHttp.get('login?user=' + username + '&password='+ password, 
						function() {					
							$cookies.loggedInUser = username;
							$rootScope.$broadcast("olApp:login:authorized", username);							
							$location.path("/Items");
														
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
	
	module = module.controller('HeaderCtrl',
			function ($cookies, $rootScope, $scope, $http, $location, $themeService) {		
				$rootScope.$on("olApp:login:authorized", function(e, value) {
					console.log("asd");
					$scope.username = value;
				});
				
				if ($cookies.loggedInUser != undefined) {
					$scope.username = $cookies.loggedInUser;
				}
				
				
				$scope.logout = function(){
					delete $scope.username
					delete $rootScope.loggedInUser
					delete $cookies.loggedInUser				
					$location.path("/login");
				}
				
				$scope.changeTheme = function() {
					$themeService.changeTheme();
				};
			
		});

	// template for all entities	
	<#if entitiesDefinitions??>	
	<#list entitiesDefinitions as entityDefinition>
		module = module.controller('${entityDefinition.entityName}Controller',
			function($scope, $location, $olHttp,$routeParams, flatMenu) {
				$scope.noTargetScreenEntityAlert = function() {
					alert('No target entity specified for table action in table class @ScreenTableActions annotation');
				}; 
				$scope.read = function(){					
					$olHttp.get('${entityDefinition.entityName}/' <#if entityDefinition.keys?size &gt; 0>+ $routeParams.${entityDefinition.keys[0].name?replace(".", "_")}</#if>,					
						function(data) {						
						console.log(data.model.entity);
							$scope.model = data.model.entity;							
							$scope.baseUrl = olConfig.baseUrl;
							
							$scope.doActionNoTargetEntity = function(rowIndex, actionValue) {					
								$scope.model.actions=null;
								$scope.model.itemsRecords[rowIndex].action_ = actionValue;
								
								$olHttp.post('${entityDefinition.entityName}/', $scope.model, function(data) {
									$scope.model = data.model.entity;									
								});
										
							};
						}							
					);
				};	
				
				flatMenu(function(data) {					
					$scope.menuArray = data;
				});
				
				$scope.doAction = function(entityName, actionAlias) {
					
					delete $scope.model.actions;					
					$olHttp.post(entityName + "?action=" + actionAlias,$scope.model, 
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
						
				$scope.read();

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

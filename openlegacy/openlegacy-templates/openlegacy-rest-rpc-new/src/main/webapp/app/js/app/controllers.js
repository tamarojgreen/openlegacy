(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', []);

	module = module.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope) {
			if ($.cookie('loggedInUser') != undefined) {
				$location.path("/menu");
			}
			$scope.login = function(username, password) {				
				
				var data = {"user":username,"password":password}
				$olHttp.post('login',data, 
						function() {
							var $expiration = new Date();
							var minutes = olConfig.expiration;
							$expiration.setTime($expiration.getTime() + minutes*60*1000)
							
							$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
							$rootScope.$broadcast("olApp:login:authorized", username);
							$location.path("/menu");							
						}
					);
			};		
		});
	module = module.controller(
		'logoffController',
		function($scope, $location, $olHttp, $rootScope) {
			$olHttp.get('logoff', 
				function() {
					$.removeCookie("loggedInUser", {path: '/'});
				}
			);
		});
	
	module = module.controller('HeaderCtrl',
		function ($rootScope, $scope, $http, $location, $themeService) {    
			$rootScope.$on("olApp:login:authorized", function(e, value) {
				$scope.username = value;
			});
			
			if ($.cookie('loggedInUser') != undefined) {
				$scope.username = $.cookie('loggedInUser');
			}
			
			
			$scope.logout = function(){
				delete $scope.username
				$location.path("/logoff");
			}
			
			$scope.changeTheme = function() {
				$themeService.changeTheme();
			};
		
	});
	
	module = module.controller(
		'menuCtrl',
		function($scope, flatMenu) {
			flatMenu(function(data) {					
				$scope.menuArray = data;
			});
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
			function($scope, $location, $olHttp,$routeParams, flatMenu) {
				$scope.noTargetScreenEntityAlert = function() {
					alert('No target entity specified for table action in table class @ScreenTableActions annotation');
				}; 
				$scope.read = function(){					
					$olHttp.get('${entityName}/' <#if keys?size &gt; 0>+ $routeParams.${keys[0].name?replace(".", "_")}</#if>,					
						function(data) {						
						console.log(data.model.entity);
							$scope.model = data.model.entity;							
							$scope.baseUrl = olConfig.baseUrl;
							
							$scope.doActionNoTargetEntity = function(rowIndex, actionValue) {					
								$scope.model.actions=null;
								$scope.model.itemsRecords[rowIndex].action_ = actionValue;
								
								$olHttp.post('${entityName}/', $scope.model, function(data) {
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
							if (data.model.entityName == '${entityName}'){								
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

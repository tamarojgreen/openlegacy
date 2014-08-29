(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', []);

	module = module.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$scope.user = {userName:"",password:""};
			$scope.login = function() {
				$cookies.loggedInUser = $scope.user.userName;
				$rootScope.loggedInUser = $scope.user.userName;
				$olHttp.get('login?user=' + $scope.user.userName + '&password='+ $scope.user.password, 
					function() {
						$cookies.loggedInUser = $scope.user.userName;
						$rootScope.loggedInUser = $scope.user.userName;
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

	// template for all entities 
	<#if entitiesDefinitions??>
	<#list entitiesDefinitions as entityDefinition>	
	module = module.controller('${entityDefinition.entityName}Controller',
			function($scope, $location, $olHttp,$routeParams) {
				$scope.noTargetScreenEntityAlert = function() {
					alert('No target entity specified for table action in table class @ScreenTableActions annotation');
				}; 
				$scope.read = function(){
					$olHttp.get('${entityDefinition.entityName}/' <#if entityDefinition.keys?size &gt; 0>+ $routeParams.${entityDefinition.keys[0].name?replace(".", "_")}</#if>,
						function(data) {						
							$scope.model = data.model.entity;
							$scope.breadcrumbs = data.model.paths;
							$scope.baseUrl = olConfig.baseUrl;
							
							$scope.doActionNoTargetEntity = function(rowIndex, columnName, actionValue) {					
								$scope.model.actions=null;
								$scope.model.itemsRecords[rowIndex].action_ = actionValue;
								
								$olHttp.post('${entityDefinition.entityName}/', $scope.model, function(data) {
									$scope.model = data.model.entity;									
								});
										
							};
						}
					);
				};		
				$olHttp.get('menu/flatMenu', function(data) {
					$scope.menuArray = [];					
					var getMenuString = function(data) {						
						angular.forEach(data, function(value) {							
							$scope.menuArray.push(value);
							getMenuString(value.menuItems);
					    });					     
					}
					getMenuString(data.simpleMenuItemList);					
				});
				
				$scope.doAction = function(entityName, actionAlias) {					
					$scope.model.actions = null;					
					$olHttp.post(entityName + '?action=' + actionAlias,$scope.model, 
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
				
				<#if (entityDefinition.sortedFields?size > 0)>
					<#list entityDefinition.sortedFields as field>
						<#if field.fieldTypeDefinition.typeName == 'fieldWithValues'>						
						$olHttp.get("${field.name?cap_first}s", function(data) {							
							$scope.${field.name}s = data.model.entity.${field.name}sRecords;							
							$scope.${field.name?cap_first}Click = function(${field.name}) {								
								$scope.model.${field.name} = ${field.name}.type;			
							}
						});
						</#if>						
					</#list>
				</#if>
				
				<#if (entityDefinition.childEntitiesDefinitions?size > 0)>
				$scope.loadTab = function(entityName) {
					$scope.model.actions=null;
					$olHttp.get(entityName + '/' <#if (entityDefinition.keys?size > 0)>+ $routeParams.${entityDefinition.keys[0].name}</#if>, 
						function(data) {
							$scope.model = data.model.entity;																
						});					
				};
				</#if>
				
				$scope.read();
			});
	
	</#list>
	</#if>
	
/* Controller code place-holder start
	<#if entityName??>
	module = module.controller('${entityName}Controller',
				function($scope, $location, $olHttp,$routeParams) {
				$scope.noTargetScreenEntityAlert = function() {
					alert('No target entity specified for table action in table class @ScreenTableActions annotation');
				}; 
				$scope.read = function(){
					$olHttp.get('${entityName}/'  <#if keys?size &gt; 0>+ $routeParams.${keys[0].name?replace(".", "_")}</#if>,
						function(data) {						
							$scope.model = data.model.entity;
							$scope.breadcrumbs = data.model.paths;
							$scope.baseUrl = olConfig.baseUrl;
							
							$scope.doActionNoTargetEntity = function(rowIndex, columnName, actionValue) {					
								$scope.model.actions=null;
								$scope.model.itemsRecords[rowIndex].action_ = actionValue;
								
								$olHttp.post('${entityName}/', $scope.model, function(data) {
									$scope.model = data.model.entity;									
								});
										
							};
						}
					);
				};		
				$olHttp.get('menu/flatMenu', function(data) {
					$scope.menuArray = [];					
					var getMenuString = function(data) {						
						angular.forEach(data, function(value) {							
							$scope.menuArray.push(value);
							getMenuString(value.menuItems);
					    });					     
					}
					getMenuString(data.simpleMenuItemList);					
				});
				
				$scope.doAction = function(entityName, actionAlias) {					
					$scope.model.actions = null;					
					$olHttp.post(entityName + '?action=' + actionAlias,$scope.model, 
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
				
				<#if (sortedFields?size > 0)>
					<#list sortedFields as field>
						<#if field.fieldTypeDefinition?? && field.fieldTypeDefinition.typeName == 'fieldWithValues'>						
						$olHttp.get("${field.name?cap_first}s", function(data) {							
							$scope.${field.name}s = data.model.entity.${field.name}sRecords;							
							$scope.${field.name?cap_first}Click = function(${field.name}) {								
								$scope.model.${field.name} = ${field.name}.type;			
							}
						});
						</#if>						
					</#list>
				</#if>
				
				<#if (childEntitiesDefinitions?size > 0)>
				$scope.loadTab = function(entityName) {
					$scope.model.actions=null;
					$olHttp.get(entityName + '/' <#if (keys?size > 0)>+ $routeParams.${keys[0].name}</#if>, 
						function(data) {
							$scope.model = data.model.entity;																
						});					
				};
				</#if>
				
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

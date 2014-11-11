(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ["ui.bootstrap"]);

	module = module.controller(
			'loginCtrl',
			function($scope, $olHttp, $rootScope, $state) {			
				if ($.cookie('loggedInUser') != undefined) {
					$state.go("menu");
				}
				$scope.login = function(username, password) {
					$olHttp.get('login?user=' + username + '&password='+ password, 
							function() {
								var $expiration = new Date();
								var minutes = 30;
								$expiration.setTime($expiration.getTime() + minutes*60*1000)
								
								$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
								$rootScope.$broadcast("olApp:login:authorized", username);
								$state.go("menu");
							}
						);
				};		
			});
		module = module.controller(
			'logoffCtrl',
			function($scope, $olHttp, $rootScope) {
				$olHttp.get('logoff', 
					function() {
						$.removeCookie("loggedInUser", {path: '/'});
					}
				);
			});
		
		module = module.controller('headerCtrl',
			function ($rootScope, $scope, $http, $location, $themeService, $olHttp, $modal) {			
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
				
				$scope.showMessages = false;
				$olHttp.get("messages", function(data){
					if (data.model != null && data.model != undefined && data.model != "") {						
						$scope.showMessages = true;
						
						$scope.messages = function() {
							var modalInstance = $modal.open({
								templateUrl: "views/messages.html",
								controller: "messagesModalCtrl",
								resolve:{
									messages: function() {
										return data.model;
									} 
								}
							});
						};
						
						if (olConfig.showSystemMessages) {				
							$scope.messages();
						}
					}		
				});
			});	
			
		module = module.controller('messagesModalCtrl', ['$scope', '$modalInstance','messages', function($scope, $modalInstance, messages) {			
			$scope.messages = messages;	
			$scope.close = function() {
				$modalInstance.close();
			};
			
		}]);
		
		module = module.controller(
			'menuCtrl',
			function($scope, flatMenu) {
				flatMenu(function(data) {				
					$scope.menuArray = data;
				});
			});
		
		module = module.controller('breadcrumbsCtrl', function($scope, $rootScope, $olHttp, $state) {
			var entityName = $state.current.name.replace("WithKey", "");
				$olHttp.get(entityName + "/breadcrumbs", function(data) {				
					if (data != null && data.breadcrumbs != null) {					
						$scope.breadcrumbs = data.breadcrumbs;
					}				
				});
		});

		// template for all entities 
		<#if entitiesDefinitions??>
		<#list entitiesDefinitions as entityDefinition>	
		module = module.controller('${entityDefinition.entityName}Ctrl',
				function($scope, $location, $olHttp,$routeParams, flatMenu, $themeService) {
					$scope.noTargetScreenEntityAlert = function() {
						alert('No target entity specified for table action in table class @ScreenTableActions annotation');
					}; 
					$scope.read = function(){
						$olHttp.get('${entityDefinition.entityName}/' <#if entityDefinition.keys?size &gt; 0>+ $routeParams.${entityDefinition.keys[0].name?replace(".", "_")}</#if>,
							function(data) {						
								$scope.model = data.model.entity;							
								$scope.baseUrl = olConfig.baseUrl;
								
								$scope.doActionNoTargetEntity = function(rowIndex, columnName, actionValue) {					
									$scope.model.actions=null;
									<#list entityDefinition.tableDefinitions?keys as key> 
									$scope.model.${entityDefinition.tableDefinitions[key].tableEntityName}s[rowIndex][columnName] = actionValue;
								    </#list>	
									
									$olHttp.post('${entityDefinition.entityName}/', $scope.model, function(data) {
										if (data.model.entityName == '${entityDefinition.entityName}'){
											$scope.model = data.model.entity;								
										}
										else{					
											$location.path("/" + data.model.entityName);
										}
									});
								};
							}							
						);
					};		

					flatMenu(function(data) {
						$scope.menuArray = data;
					});
					
					$scope.doAction = function(entityName, actionAlias) {
						var findAndClearActions = function(data) {						
							if (data.actions != null && data.actions != undefined) {
								data.actions = null;
							}			
							for (var key in data) {
							  if (typeof data[key] == "object" && data[key] != null) {
								if (data[key].actions != null && data[key].actions != undefined) {
									data[key].actions = null;
								}
								
								findAndClearActions(data[key]);
							  }
							}
							
							return data;
						};
						if (actionAlias == "") {
				    		var url = entityName + actionAlias;
				    	} else {
				    		var url = entityName + "?action=" + actionAlias;
				    	}  
						$olHttp.post(url,findAndClearActions($scope.model), 
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
		module = module.controller('${entityName}Ctrl',
					function($scope, $location, $olHttp,$routeParams, flatMenu) {
					$scope.noTargetScreenEntityAlert = function() {
						alert('No target entity specified for table action in table class @ScreenTableActions annotation');
					}; 
					$scope.read = function(){
						$olHttp.get('${entityName}/'  <#if keys?size &gt; 0>+ $routeParams.${keys[0].name?replace(".", "_")}</#if>,
							function(data) {						
								$scope.model = data.model.entity;							
								$scope.baseUrl = olConfig.baseUrl;
								
								$scope.doActionNoTargetEntity = function(rowIndex, columnName, actionValue) {					
									$scope.model.actions=null;
									<#list tableDefinitions?keys as key> 
									$scope.model.${tableDefinitions[key].tableEntityName}s[rowIndex][columnName] = actionValue;
								    </#list>	
									
									$olHttp.post('${entityName}/', $scope.model, function(data) {
										if (data.model.entityName == '${entityName}'){
											$scope.model = data.model.entity;								
										}
										else{					
											$location.path("/" + data.model.entityName);
										}
									});
								};
							}
						);
					};		

					flatMenu(function(data) {
						$scope.menuArray = data;
					});	
		
					
					$scope.doAction = function(entityName, actionAlias) {					
						var findAndClearActions = function(data) {						
							if (data.actions != null && data.actions != undefined) {
								data.actions = null;
							}			
							for (var key in data) {
							  if (typeof data[key] == "object" && data[key] != null) {
								if (data[key].actions != null && data[key].actions != undefined) {
									data[key].actions = null;
								}
								
								findAndClearActions(data[key]);
							  }
							}
							
							return data;
						};
						if (actionAlias == "") {
				    		var url = entityName + actionAlias;
				    	} else {
				    		var url = entityName + "?action=" + actionAlias;
				    	}					
						$olHttp.post(url,findAndClearActions($scope.model), 
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

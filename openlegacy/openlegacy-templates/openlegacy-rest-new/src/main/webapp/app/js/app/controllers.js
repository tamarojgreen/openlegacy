(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ["ui.bootstrap"]);

	module = module.controller(
			'emulationCtrl',
			function($scope, $olHttp, $rootScope, $state, $stateParams) {
				
				$scope.doAction = function(key){
					var url = "emulation?";
					$("form :input").each(function(){
						var val = $(this).val();
						if ($(this).attr('name') == "KeyboardKey"){
							val = key;
						}
						url = url + $(this).attr('name') + "=" + val + "&"; 
					});
					$olHttp.get(url,
							function(data){
								if (data.model.entity == null){
									location.reload();
								}
								else{
									$(document).off();
									$state.go(data.model.entityName);
								}
						}); 
				}
				
				$scope.handleKeyboard = function(e){
					if (window.keyboardMappings != null){
						for (var i=0;i<keyboardMappings.mappings.length;i++){
							var mapping = keyboardMappings.mappings[i];
							if (e.keyCode == eval("keyCode." + mapping.KeyboardKey)){
								var keyPrefix = "";
								if (e.shiftKey || e.ctrlKey || e.altKey){
									if (e.shiftKey && mapping.additionalKey == "SHIFT"){
										keyPrefix = "SHIFT-";
									}
									else if (e.ctrlKey && mapping.additionalKey == "CTRL"){
										keyPrefix = "CTRL-";
									}
									else if (e.altKey && mapping.additionalKey == "ALT"){
										keyPrefix = "ALT-";
									}
									else{
										continue;
									}
								}
								e.stopPropagation();
								e.preventDefault();
								$scope.doAction(keyPrefix + mapping.KeyboardKey);
								break;
							}
						}
					}
				}
				$scope.handleEmulation = function(){
					// set onload focus
					var focusInput = $("#" + $("#TerminalCursor").val());
					window.setTimeout(function(){focusInput.focus()},500);
					// capture focus event
					
					$(document).keydown(function(e) {
						$scope.handleKeyboard(e);
					});
					$("form :input").each(function(){
						$(this).focus(function(){
							$("#TerminalCursor").val($(this).attr("name"));
						});
						// capture keydown event
						$(this).keydown(function(e){
							$scope.handleKeyboard(e);
						});
					});
				}
				$scope.handleEmulation();
				$rootScope.hidePreloader();
			});

	module = module.controller(
			'loginCtrl',
			function($scope, $olHttp, $rootScope, $state, $stateParams) {
				$scope.login = function(username, password) {				
				var userData = {"user":username,"password":password}
				$olHttp.post('login',userData, 
							function(data) {
								var $expiration = new Date();
								var minutes = olConfig.expiration;
								$expiration.setTime($expiration.getTime() + minutes*60*1000)
								
								$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
								$rootScope.$broadcast("olApp:login:authorized", username);
								if (data == ""){
									$state.go("emulation");
								}
								else{
									$state.go(olConfig.afterLoginView);
								}
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
						$rootScope.hidePreloader();
					}
				);
			});
		
		module = module.controller('headerCtrl',
			function ($rootScope, $scope, $http, $themeService, $olHttp, $modal, $state) {			
				$rootScope.$on("olApp:login:authorized", function(e, value) {
					$scope.username = value;
				});
				
				if ($.cookie('loggedInUser') != undefined) {
					$scope.username = $.cookie('loggedInUser');
				}				
				
				$scope.logout = function(){					
					delete $scope.username
					$state.go("logoff");
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
			function($scope, flatMenu, $state, $rootScope) {
				flatMenu(function(data) {				
					$scope.menuArray = data;
					if ($state.current.name == 'menu') {
						$rootScope.hidePreloader();
					};
				});
			});
		
		module = module.controller('breadcrumbsCtrl', function($scope, $rootScope, $olHttp, $state) {			
			$rootScope.$on("olApp:breadcrumbs", function(e, value) {
				$scope.breadcrumbs = value;
			});
		});

		// template for all entities 
		<#if entitiesDefinitions??>
		<#list entitiesDefinitions as entityDefinition>	
		module = module.controller('${entityDefinition.entityName}Ctrl',
				function($scope, $olHttp,$stateParams, $themeService, $rootScope, $state,$modal) {
					$scope.noTargetScreenEntityAlert = function() {
						alert('No target entity specified for table action in table class @ScreenTableActions annotation');
					}; 
					
					$scope.isReadOnly = function(data,column){
						return $rootScope.isReadOnly(data,column,$scope.model);
					}
					$scope.readOnlyCss = function(data,column){
						return $rootScope.readOnlyCss(data,column);
					}
					$scope.isActionAvailable = function(alias){
						return $rootScope.isActionAvailable(alias,$scope.model);
					};
					
					$scope.read = function(){						  
					      $olHttp.get('${entityDefinition.entityName}/' <#if entityDefinition.keys?size &gt; 0>+ $stateParams.${entityDefinition.keys[0].name?replace(".", "_")}</#if> + "?children=false",
							function(data) {					    	  	
								$scope.model = data.model.entity;							
								$scope.baseUrl = olConfig.baseUrl;
								$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
								
								
								$scope.doActionNoTargetEntity = function(alias) {					
								    var suffix = alias != null ? "&action=" + alias : "";
									$olHttp.post('${entityDefinition.entityName}/?children=false' + suffix, $rootScope.clearObjectsFromPost($scope.model), function(data) {
										if (data.model.entityName == '${entityDefinition.entityName}'){											
											$scope.model = data.model.entity;
											$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
											$rootScope.hidePreloader();
										} else {
											<#if entityDefinition.window>
											$rootScope.modalInstance.close();
											$state.transitionTo($state.current, $stateParams, {
											    reload: true,
											    inherit: false,
											    notify: true
											});
											<#else>
											$state.go(data.model.entityName);
											</#if>
										}
									});
								};
								
								<#if (entityDefinition.childEntitiesDefinitions?size > 0)>
									var tabsContent = {};						
									tabsContent["${entityDefinition.entityName}"] = $scope.model;
									$scope.loadTab = function(entityName) {
										if (tabsContent[entityName] == null) { 
											$scope.model.actions=null;											
											$olHttp.get(entityName + '/' <#if (entityDefinition.keys?size > 0)>+ $stateParams.${entityDefinition.keys[0].name}</#if> + "?children=false", 
												function(data) {													
													$scope.model = data.model.entity;
													tabsContent[entityName] = data.model.entity;
													$rootScope.hidePreloader();
												});
										} else {
											$scope.model = tabsContent[entityName];
										}					
									};
								</#if>
								
								$rootScope.hidePreloader();
					    	  	<#if entityDefinition.window>
					    	  		$rootScope.allowShowPreloader = false;
					    	  	<#else>	
					    	  		$rootScope.allowShowPreloader = true;
					    	  	</#if>
							}							
						);
					};
										
					$scope.doAction = function(entityName, actionAlias) {						
						if (actionAlias == "") {
				    		var url = entityName + actionAlias;
				    	} else {
				    		var url = entityName + "?action=" + actionAlias;
				    	}  
						
						if (actionAlias.indexOf("lookup-") > -1 || ${entityDefinition.window?string} == 'true') {
							$rootScope.showPreloader(false);
						}
						
						$olHttp.post(url,$rootScope.clearObjectsFromPost($scope.model), 
							function(data) {
								if (data == ""){
									$state.go("emulation");
									return;
								}
								if (data.model.entityName == '${entityDefinition.entityName}'){
									$rootScope.hidePreloader();
									$scope.model = data.model.entity;								
								} else {
									if (data.model.window){
										$rootScope.modalInstance = $modal.open({
											templateUrl: $state.get(data.model.entityName).views[""].templateUrl,
											controller: $state.get(data.model.entityName).views[""].controller,										
										});
									}
									else{
										$state.go(data.model.entityName);
									}
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
					
					$scope.read();
				});
		
		</#list>
		</#if>
	
		/* Controller code place-holder start
		<#if entityName??>
				module = module.controller('${entityName}Ctrl',
				function($scope, $olHttp,$stateParams, $themeService, $rootScope, $state) {
					$scope.noTargetScreenEntityAlert = function() {
						alert('No target entity specified for table action in table class @ScreenTableActions annotation');
					}; 
					
					$scope.isReadOnly = function(data,column){
						return $rootScope.isReadOnly(data,column,$scope.model);
					}
					$scope.readOnlyCss = function(data,column){
						return $rootScope.readOnlyCss(data,column);
					}
					$scope.isActionAvailable = function(alias){
						return $rootScope.isActionAvailable(alias,$scope.model);
					};
					
					$scope.read = function(){						  
					      $olHttp.get('${entityName}/' <#if keys?size &gt; 0>+ $stateParams.${keys[0].name?replace(".", "_")}</#if> + "?children=false",
							function(data) {					    	  	
								$scope.model = data.model.entity;							
								$scope.baseUrl = olConfig.baseUrl;
								$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
								
								
								$scope.doActionNoTargetEntity = function(alias) {					
								    var suffix = alias != null ? "?action=" + alias : "";
									$olHttp.post('${entityDefinition.entityName}/' + suffix, $rootScope.clearObjectsFromPost($scope.model), function(data) {
										if (data.model.entityName == '${entityName}'){											
											$scope.model = data.model.entity;
											$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
											$rootScope.hidePreloader();
										} else {
											<#if window>																						
											$state.transitionTo($state.current, $stateParams, {
											    reload: true,
											    inherit: false,
											    notify: true
											});
											<#else>
											$state.go(data.model.entityName);
											</#if>
										}
									});
								};
								
								<#if (childEntitiesDefinitions?size > 0)>
									var tabsContent = {};						
									tabsContent["${entityName}"] = $scope.model;
									$scope.loadTab = function(entityName) {
										if (tabsContent[entityName] == null) { 
											$scope.model.actions=null;											
											$olHttp.get(entityName + '/' <#if (keys?size > 0)>+ $stateParams.${keys[0].name}</#if> + "?children=false", 
												function(data) {													
													$scope.model = data.model.entity;
													tabsContent[entityName] = data.model.entity;
													$rootScope.hidePreloader();
												});
										} else {
											$scope.model = tabsContent[entityName];
										}					
									};
								</#if>
								
								$rootScope.hidePreloader();
					    	  	<#if window>
					    	  		$rootScope.allowShowPreloader = false;
					    	  	<#else>	
					    	  		$rootScope.allowShowPreloader = true;
					    	  	</#if>
							}							
						);
					};		

					$scope.doAction = function(entityName, actionAlias) {						
						if (actionAlias == "") {
				    		var url = entityName + actionAlias;
				    	} else {
				    		var url = entityName + "?action=" + actionAlias;
				    	}  
						
						if (actionAlias.indexOf("lookup-") > -1 || ${window?string} == 'true') {
							$rootScope.showPreloader(false);
						}
						
						$olHttp.post(url,$rootScope.clearObjectsFromPost($scope.model), 
							function(data) {
								if (data == ""){
									$state.go("emulation");
									return;
								}
								if (data.model.entityName == '${entityName}'){
									$rootScope.hidePreloader();
									$scope.model = data.model.entity;								
								} else {
									if (data.model.window){
										$rootScope.modalInstance = $modal.open({
											templateUrl: $state.get(data.model.entityName).views[""].templateUrl,
											controller: $state.get(data.model.entityName).views[""].controller,										
										});
									}
									else{
										$state.go(data.model.entityName);
									}
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
					
					$scope.read();
				});
		</#if>
		Controller code place-holder end */

	/* Controller with JSONP code place-holder start
	<#if entityName??>
	module = module.controller('${entityName}Controller',
		function($scope, $location, $http,$stateParams,$templateCache) {
			<#list actions as action>
			$scope.${action.alias} = function(){
				$http({method: 'JSONP', url: olConfig.hostUrl + '/${entityName}/${action.programPath}/'<#if keys?size &gt; 0> + </#if><#list keys as key>$stateParams.${key.name?replace(".", "_")}<#if key_has_next>+</#if></#list> + '?callback=JSON_CALLBACK', cache: $templateCache}).
			      success(function(data, status) {
						$scope.${entityName?uncap_first} = data;
			      }).
			      error(function(data, status) {
			    	  alert("failed");
			    });
			};
			</#list>
			if ($stateParams.${keys[0].name?replace(".", "_")} != null){
				$scope.read();
			}
			
		});
	</#if>
 	Controller with JSONP code place-holder end */
})();

var keyCode = {
    BACKSPACE: 8,
    COMMA: 188,
    DELETE: 46,
    DOWN: 40,
    END: 35,
    ENTER: 13,
    ESCAPE: 27,
    HOME: 36,
    LEFT: 37,
    NUMPAD_ADD: 107,
    NUMPAD_DECIMAL: 110,
    NUMPAD_DIVIDE: 111,
    NUMPAD_ENTER: 108,
    NUMPAD_MULTIPLY: 106,
    NUMPAD_SUBTRACT: 109,
    PAGE_DOWN: 34,
    PAGE_UP: 33,
    PERIOD: 190,
    RIGHT: 39,
    SPACE: 32,
    TAB: 9,
    UP: 38,
    F1: 112,
    F2: 113,
    F3: 114,
    F4: 115,
    F5: 116,
    F6: 117,
    F7: 118,
    F8: 119,
    F9: 120,
    F10: 121,
    F11: 122,
    F12: 123
}
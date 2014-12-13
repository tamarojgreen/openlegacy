(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', [])

	module = module.controller( 'loginCtrl', function($scope, $olHttp, $rootScope, $stateParams, $state) {
		$scope.login = function(username, password) {
			var data = {
				"user" : username,
				"password" : password
			}
			$olHttp.post('login', data, function() {
				var $expiration = new Date();
				var minutes = olConfig.expiration;
				$expiration.setTime($expiration.getTime() + minutes	* 60 * 1000)
	
				$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
				$rootScope.$broadcast("olApp:login:authorized", username);
				$state.go($stateParams.redirectTo.name, $stateParams.redirectTo.params);
			});
		};
	});
	module = module.controller('logoffCtrl', function($scope, $olHttp, $rootScope) {				
			$olHttp.get('logoff', 
				function() {
					$rootScope.hidePreloader();
					$.removeCookie("loggedInUser", {path: '/'});
				}
			);
		});
	module = module.controller('menuCtrl', function(flatMenu, $scope) {		
		flatMenu(function(data) {
			$scope.menuArray = data;			
		});
	});
	module = module.controller('headerCtrl', function ($rootScope, $scope, $state) {
		if ($.cookie('loggedInUser') != undefined) {
			$scope.username = $.cookie('loggedInUser');
		}
		
		
		$scope.logout = function(){
			$rootScope.allowHidePreloader = false;
			delete $scope.username
			$state.go("logoff");
		}
		
//		$scope.showMessages = false;
//		$olHttp.get("messages", function(data){
//			$rootScope.hidePreloader();
//			if (data.model != null && data.model != undefined && data.model != "") {						
//				$scope.showMessages = true;
//				
//				$scope.messages = function() {
//					var modalInstance = $modal.open({
//						templateUrl: "views/messages.html",
//						controller: "messagesModalCtrl",
//						resolve:{
//							messages: function() {
//								return data.model;
//							} 
//						}
//					});
//				};
//				
//				if (olConfig.showSystemMessages) {				
//					$scope.messages();
//				}
//			}		
//		});
	});
	// template for all entities 
	<#if entitiesDefinitions??>
	<#list entitiesDefinitions as entityDefinition>
		<#list entityDefinition.actions as action>
			<#switch action.actionName>
				<#case "READ">
	module = module.controller('${entityDefinition.entityName}DetailsCtrl', function($scope, $stateParams, $olHttp, $state) {		
		$scope.currentAction = "READ";		

		$olHttp.get('${entityDefinition.entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {
			$scope.entityName = data.model.entityName;
			$scope.model = data.model;						
			$scope.doREADAction = function(targetEntityName, rowIndex, propertyName) {				
	        	<#list entitiesDefinitions as entity>
	        		if (targetEntityName == "${entity.entityName}") {
	        			var targetData = $scope.model.entity[propertyName]
	        			var keys = Object.keys( targetData );	        			    
    			    	$state.go(targetEntityName + "Details", {<#list entity.keys as key>${key.name?replace(".", "_")}:targetData[keys[rowIndex]].${key.name}<#if key_has_next>+</#if></#list>});
	        		}
	        	</#list>		      
        	}			
		});
	});
				<#break>
				<#case "CREATE">
	module = module.controller('${entityDefinition.entityName}NewCtrl', function($scope) {
		$scope.currentAction = "CREATE";
	});
			</#switch>
		</#list>
	module = module.controller('${entityDefinition.entityName}Ctrl', function($olHttp, $scope, $location, $state, $stateParams) {		
		$scope.showNext = true;
		$scope.showPrev = true;
		var getItems = function() {
			var queryParamsString = "?";
			var page = null;
			angular.forEach($location.search(), function(value, key) {
				queryParamsString += key + "=" + value + "&";
				if (key == "page") {
					page = parseInt(value);
				}
				
			});
			
			queryParamsString = queryParamsString.substring(0, queryParamsString.length - 1);
			
			$olHttp.get('${entityDefinition.entityName}' + queryParamsString, function(data) {				
				$scope.model = data.model;
		        if (page == parseInt(data.model.pageCount)) {
		        	$scope.showNext = false;
		        	$scope.showPrev = true;
		        } else if (page > parseInt(data.model.pageCount)) {
		        	page = 1;
		        } else if (parseInt(data.model.pageCount) == 0 || page == null || page > parseInt(data.model.pageCount) || page == 1) {			        	
		        	$scope.showPrev = false;
		        	$scope.showNext = true;
		        } else {
		        	$scope.showPrev = true;
		        	$scope.showNext = true;
		        }
		        
		        
		        $scope.next = function() {
		        	if (page == 0 || page == null) {			        		
		        		$location.url("/${entityDefinition.entityName}?page=2");
		        		getItems();
		        	} else {			        		
		        		$location.url("/${entityDefinition.entityName}?page=" + (page + 1));
		        		getItems();
		        	}
		        };
		        
		        $scope.prev = function() {			        	
	        		$location.url("/${entityDefinition.entityName}?page=" + (page - 1));
	        		getItems();			        	
		        };
		        
		        $scope.doREADAction = function(entityName, rowIndex) {		        	
		        	<#list entitiesDefinitions as entity>
		        		if (entityName == "${entity.entityName}") {		        			
		        			$state.go(entityName + "Details", {<#list entity.keys as key>${key.name?replace(".", "_")}:$scope.model.entity[rowIndex].${key.name}<#if key_has_next>+</#if></#list>});
		        		}
		        	</#list>		      
	        	}
		        
//		        $scope.postAction = function(actionAlias) {			        				        	
//		        	$olHttp.post('${entityDefinition.entityName}' + "?action=" + actionAlias, data.model.entity, function(data) {			        		
//		        		if ($state.current.name == data.model.entityName.toLowerCase()) {
//		        			$scope.items = data.model.entity.innerRecord;
//		        			console.log("OK");
//		        		} else {
//		        			$state.go(data.model.entityName.toLowerCase());
//		        		}
//		        		
//		        	});
//		        };
		        
		        $scope.exportExcelUrl = olConfig.baseUrl + data.model.entityName + "/excel";        
					
			});
		}
		
		getItems();
	});				
	</#list>
	</#if>
})();

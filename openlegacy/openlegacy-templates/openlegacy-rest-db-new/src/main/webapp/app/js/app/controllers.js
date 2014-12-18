(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ['ui.bootstrap'])

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

	});
	// template for all entities 
	<#if entitiesDefinitions??>
	<#list entitiesDefinitions as entityDefinition>
		<#list entityDefinition.actions as action>
			<#switch action.actionName>
				<#case "READ">
	module = module.controller('${entityDefinition.entityName}DetailsCtrl', function($scope, $stateParams, $olHttp, $state, $modal) {		
		$scope.currentAction = "READ";		

		$olHttp.get('${entityDefinition.entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {
			$scope.entityName = data.model.entityName;
			$scope.model = data.model;
			console.log($scope.model.entity.notes);
			$scope.doREADAction = function(targetEntityName, rowIndex, propertyName) {				
	        	<#list entitiesDefinitions as entity>
	        		if (targetEntityName == "${entity.entityName}") {
	        			var targetData = $scope.model.entity[propertyName]
	        			var keys = Object.keys( targetData );	        			    
    			    	$state.go(targetEntityName + "Details", {${entity.keys[0].name?replace(".", "_")}:<#list entity.keys as key>targetData[keys[rowIndex]].${key.name}<#if key_has_next>+</#if></#list>});
	        		}
	        	</#list>
        	}
			$scope.doUPDATEAction = function() {				
				var modalInstance = $modal.open({
					templateUrl: 'views/partials/confirmation_dialog.html',
					controller: 'ConfirmationDialogCtrl',
					resolve: {
						func: function () {
							return function() {
								$olHttp.post('${entityDefinition.entityName}?action=', $scope.model.entity, function(data) {					
									alert("Entity was successfully updated!");
								});
							} 
						}
					}
			    });				
			}
			
			$scope.doDELETEAction = function() {
				var modalInstance = $modal.open({
					templateUrl: 'views/partials/confirmation_dialog.html',
					controller: 'ConfirmationDialogCtrl',					
					resolve: {
						func: function () {
							return function() {
								$olHttp.remove('${entityDefinition.entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {
									$state.go('${entityDefinition.entityName}');
								});
							} 
						}
					}
			    });
			}
		});
	});
				<#break>
				<#case "CREATE">
	module = module.controller('${entityDefinition.entityName}NewCtrl', function($scope, $modal, $olHttp) {
		$scope.currentAction = "CREATE";
		$scope.model = {'entity':{}};		
		<#list entityDefinition.columnFieldsDefinitions?keys as key>								
			<#assign column = entityDefinition.columnFieldsDefinitions[key]>
			<#if (!column.internal?? || column.internal == false) && column.oneToManyDefinition??>			
				$scope.model['entity']['${column.name}'] = {};
			</#if>			
		</#list>
		console.log($scope.model);
		
		$scope.doUPDATEAction = function() {				
			var modalInstance = $modal.open({
				templateUrl: 'views/partials/confirmation_dialog.html',
				controller: 'ConfirmationDialogCtrl',
				resolve: {
					func: function () {
						return function() {
							console.log($scope.model);
							$olHttp.post('${entityDefinition.entityName}?action=', $scope.model.entity, function(data) {					
								alert("Entity was successfully created!");
							});
						} 
					}
				}
		    });				
		}
	});
			</#switch>
		</#list>
	module = module.controller('${entityDefinition.entityName}Ctrl', function($olHttp, $scope, $location, $state, $stateParams) {		
		$scope.showNext = true;
		$scope.showPrev = true;
		var getItems = function() {
			var queryParamsString = "?";
			var page = 1;
			angular.forEach($location.search(), function(value, key) {
				queryParamsString += key + "=" + value + "&";
				if (key == "page") {
					page = parseInt(value);
				}
				
			});
			
			queryParamsString = queryParamsString.substring(0, queryParamsString.length - 1);			
			$olHttp.get('${entityDefinition.entityName}' + queryParamsString, function(data) {				
				$scope.model = data.model;				
				var setPageNavigators = function() {
					if (parseInt(data.model.pageCount) <= 1) {
						$scope.showNext = false;
			        	$scope.showPrev = false;
					} else if (page == parseInt(data.model.pageCount)) {
			        	$scope.showNext = false;
			        	$scope.showPrev = true;
			        } else if (page > parseInt(data.model.pageCount) || page <= 0) {
			        	page = 1;
			        	$location.search('page', '1');
			        	setPageNavigators();
			        } else if (page == null || page == 1) {			        	
			        	$scope.showPrev = false;
			        	$scope.showNext = true;
			        } else {
			        	$scope.showPrev = true;
			        	$scope.showNext = true;
			        }
				}
				
				setPageNavigators();
		        
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
		        			$state.go(entityName + "Details", {${entity.keys[0].name?replace(".", "_")}:<#list entity.keys as key>$scope.model.entity[rowIndex].${key.name}<#if key_has_next>+</#if></#list>});
		        		}
		        	</#list>		      
	        	}
		        
		        $scope.exportExcelUrl = olConfig.baseUrl + data.model.entityName + "/excel";        
					
			});
		}
		
		getItems();
	});				
	</#list>
	</#if>
	
	module = module.controller('ConfirmationDialogCtrl', function ($scope, $modalInstance, func) {
		  $scope.ok = function () {
		    $modalInstance.close();
		    func();		    
		  };

		  $scope.cancel = function () {
		    $modalInstance.dismiss();
		  };
		});
})();

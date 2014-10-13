(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ['ui.router'])

	.controller(
		'loginCtrl',
		function($scope, $location, $olHttp, $rootScope, $cookies, $state) {			
			
			if ($.cookie('loggedInUser') != undefined) {
				$state.go("items");
			}
			$scope.login = function(username, password) {				
				
				$olHttp.get('login?user=' + username + '&password='+ password, 
						function() {
							var $expiration = new Date();
							var minutes = 30;
							$expiration.setTime($expiration.getTime() + minutes*60*1000)
							
							$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
							$rootScope.$broadcast("olApp:login:authorized", username);
							$state.go("items");							
						}
					);
			};
		})
	.controller(
		'logoffCtrl',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$olHttp.get('logoff', 
				function() {
					$.removeCookie("loggedInUser", {path: '/'});
				}
			);
		})
	.controller('itemsCtrl',
		function($state, $scope, $location, $olHttp) {
			$olHttp.get('Items', 
				function(data) {
				
					$scope.items = data.model.entity.innerRecord;
			        
			        var actions = []; 
			        angular.forEach(data.model.actions, function(value, key) {
			        	if (value.actionName != "READ") {
			        		actions.push(value);	
			        	}
		        	});
			        
			        $scope.actions = actions;
			        
			        $scope.postAction = function(actionAlias) {			        	
			        	$olHttp.post(data.model.entityName + "?action=" + actionAlias, data.model.entity, function(data) {
			        		if ($state.current.name == data.model.entityName.toLowerCase()) {
			        			$scope.items = data.model.entity.innerRecord;
			        			console.log("OK");
			        		} else {
			        			$state.go(data.model.entityName.toLowerCase());
			        		}
			        		
			        	});
			        };
			        
			        $scope.exportExcelUrl = olConfig.baseUrl + data.model.entityName + "/excel";        
						
				});			
		})
	.controller('itemDetailsCtrl',
			function($scope, $location, $olHttp,$routeParams, $state) {
				$olHttp.get("ItemDetails/" + $routeParams.id, function(data) {
					$scope.itemDetails = data.model.entity;					
					var actions = []; 
			        angular.forEach(data.model.actions, function(value, key) {
			        	if (value.actionName != "READ") {
			        		actions.push(value);	
			        	}
		        	});
			        
			        $scope.actions = actions;
					
					$scope.postAction = function(actionAlias) {						
						$olHttp.post(data.model.entityName + "?action=" + actionAlias, $scope.itemDetails, function(data) {
							var entityName = data.model.entityName[0].toLowerCase() + data.model.entityName.substring(1);
							if ($state.current.name == entityName) {
			        			$scope.items = data.model.entity.innerRecord;
			        			$scope.itemDetails = data.model.entity;
			        			console.log("OK");
			        		} else {
			        			$state.go(entityName);
			        		}
						});				    	
				    };					
				});

			})	
	.controller('HeaderCtrl',
		function ($cookies, $rootScope, $state, $scope, $http, $location, $themeService) {    
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
		
	});
})();

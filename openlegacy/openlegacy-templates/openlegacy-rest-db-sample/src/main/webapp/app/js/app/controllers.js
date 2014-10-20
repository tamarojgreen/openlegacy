(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ['ui.router'])

	.controller(
		'loginCtrl',
		function($scope, $location, $olHttp, $rootScope, $cookies, $state) {			
			
			if ($.cookie('loggedInUser') != undefined) {
				$state.go("stockItem");
			}
			$scope.login = function(username, password) {				
				
				$olHttp.get('login?user=' + username + '&password='+ password, 
						function() {
							var $expiration = new Date();
							var minutes = 30;
							$expiration.setTime($expiration.getTime() + minutes*60*1000)
							
							$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
							$rootScope.$broadcast("olApp:login:authorized", username);
							$state.go("stockItem");							
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
					console.log($.cookie('loggedInUser'));
				}
			);
		})
	.controller('itemsCtrl',
		function($state, $scope, $location, $olHttp) {
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
				
				$olHttp.get('StockItem' + queryParamsString, function(data) {
					console.log(data);
					$scope.items = data.model.entity;					
			        $scope.actions = data.model.actions;
			        console.log("page: " + page);
			        console.log("pageCount: " + data.model.pageCount);
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
			        		$location.url("/stockItem?page=2");
			        		getItems();
			        	} else {			        		
			        		$location.url("/stockItem?page=" + (page + 1));
			        		getItems();
			        	}
			        };
			        
			        $scope.prev = function() {			        	
		        		$location.url("/stockItem?page=" + (page - 1));
		        		getItems();			        	
			        };
			        
			        $scope.postAction = function(actionAlias) {			        				        	
			        	$olHttp.post("StockItem" + "?action=" + actionAlias, data.model.entity, function(data) {			        		
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
			};
			
			getItems();
		})
	.controller('itemDetailsCtrl',
			function($scope, $location, $olHttp,$stateParams, $state) {		
				$olHttp.get("StockItem/" + $stateParams.id, function(data) {
					console.log(data);
					$scope.itemDetails = data.model.entity;					
					$scope.actions = data.model.actions;
					
					$scope.postAction = function(actionAlias) {
						$olHttp.post(data.model.entityName + "?action=" + actionAlias, data.model.entity, function(data) {
							console.log(data);
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

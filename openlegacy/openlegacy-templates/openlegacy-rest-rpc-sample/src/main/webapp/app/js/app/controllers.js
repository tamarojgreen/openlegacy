(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ['ui.router'])

	.controller(
		'loginCtrl',
		function($scope, $location, $olHttp, $rootScope, $cookies, $state) {			
			
			if ($cookies.loggedInUser != undefined) {
				$state.go("Items");
			}
			$scope.login = function(username, password) {				
				
				$olHttp.get('login?user=' + username + '&password='+ password, 
						function() {
							$cookies.loggedInUser = username;
							$rootScope.$broadcast("olApp:login:authorized", username);
							$state.go("items");							
						}
					);
			};
		})
	.controller(
		'logoffController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$olHttp.get('logoff', 
				function() {
					delete $cookies['loggedInUser'];
					$scope.loggedInUser = null;
					$rootScope.loggedInUser = null;
				}
			);
		})
	.controller('itemsCtrl',
		function($scope, $location, $olHttp) {
			$olHttp.get('Items', 
					function(data) {						
						$scope.items = data.model.entity.innerRecord;
					}
				);			
		})
	.controller('itemDetailsController',
			function($scope, $location, $olHttp,$routeParams) {
				$scope.read = function(){
					$olHttp.get('ItemDetails/' + $routeParams.itemNumber, 
						function(data) {
							$scope.model = data.model.entity;
						}
					);
				}
				$scope.save = function(){
					$olHttp.post('ItemDetails?action=update',$scope.model, 
						function(data) {
							$scope.model = data.model.entity;
						}
					);
				};
				$scope.read();

			})	
	.controller('HeaderCtrl',
		function ($cookies, $rootScope, $state, $scope, $http, $location/*, $themeService*/) {    
			$rootScope.$on("olApp:login:authorized", function(e, value) {
				$scope.username = value;
			});
			
			$scope.logout = function(){
				delete $scope.username
				delete $rootScope.loggedInUser
				delete $cookies.loggedInUser				
				$state.go("login");
			}
			//TODO: implement theme changing
//			$scope.changeTheme = function() {
//				$themeService.changeTheme();
//			};
		
	});
})();

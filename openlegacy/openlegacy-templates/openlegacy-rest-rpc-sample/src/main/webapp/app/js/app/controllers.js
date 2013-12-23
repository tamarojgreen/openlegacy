(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', [])

	.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$scope.user = {userName:"test",password:"testp"};
			$scope.login = function() {
				$cookies.loggedInUser = $scope.user.userName;
				$rootScope.loggedInUser = $scope.user.userName;
				$location.path("/itemDetails/2000");

				$olHttp.get('login?user=' + $scope.user.userName + '&password='+ $scope.user.password, 
					function() {
						$cookies.loggedInUser = $scope.user.userName;
						$rootScope.loggedInUser = $scope.user.userName;
						$location.path("/items");
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
	.controller('itemsController',
		function($scope, $location, $olHttp) {
			$olHttp.get('Items', 
					function(data) {
						$scope.items = data.model.entity.itemsRecords;
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

			});
})();

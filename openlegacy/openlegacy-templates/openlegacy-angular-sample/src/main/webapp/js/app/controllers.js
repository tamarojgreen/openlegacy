var BASE_URL = "http://localhost:8080/openlegacy-rest-sample/";

(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', [])

	.controller(
		'loginController',
		function($scope, $location, $olHttp, $rootScope, $cookies) {
			$scope.user = {userName:"test",password:"testp"};
			$scope.login = function() {
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
						$scope.items = data.screenModel.screenEntity.itemsRecords;
					}
				);
			
		})
	.controller('itemDetailsController',
			function($scope, $location, $olHttp,$routeParams) {
				$olHttp.get('ItemDetails/' + $routeParams.itemNumber, 
						function(data) {
							$scope.model = data.screenModel.screenEntity;
						}
					);
				$scope.save = function(){
					$olHttp.post('ItemDetails?action=prompt',$scope.model, 
							function(data) {
								$scope.model = data.screenModel.screenEntity;
							}
						);
					
				};
			});
})();

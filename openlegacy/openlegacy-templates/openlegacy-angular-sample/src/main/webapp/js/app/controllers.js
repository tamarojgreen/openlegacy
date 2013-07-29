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
		function($scope, $location, $http,$templateCache) {

		$scope.fetch = function(){
		    $http({method: 'JSONP', url: olConfig.baseUrl + 'dummy?callback=JSON_CALLBACK1', cache: $templateCache}).
		      success(function(data, status) {
				//$scope.items = data.screenModel.screenEntity.itemsRecords;
		      }).
		      error(function(data, status) {
		    	  //alert(data);
		    });			
			
		};
			
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
			})

	.controller('creditController',
			function($scope, $location, $http,$routeParams,$templateCache) {
		
			    $http({method: 'JSONP', url: 'http://localhost:8081/JsonpServer/credit?callback=JSON_CALLBACK', cache: $templateCache}).
			      success(function(data, status) {
						$scope.model = data;
			      }).
			      error(function(data, status) {
			    	  alert("failed");
			    });
			});

})();

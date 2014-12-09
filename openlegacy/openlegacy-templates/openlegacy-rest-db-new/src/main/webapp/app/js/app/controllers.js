(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', [])

	.controller( 'loginCtrl', function($scope, $olHttp, $rootScope, $stateParams, $state) {
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
	})
	.controller('logoffCtrl', function($scope, $olHttp, $rootScope) {				
			$olHttp.get('logoff', 
				function() {
					$rootScope.hidePreloader();
					$.removeCookie("loggedInUser", {path: '/'});
				}
			);
		})
	.controller('menuCtrl', function() {
//		flatMenu(function(data) {
//			$scope.menuArray = data;
//		});
	})
	.controller('headerCtrl', function ($rootScope, $scope, $state) {			
//		$rootScope.$on("olApp:login:authorized", function(e, value) {
//			$scope.username = value;
//		});
//		
		if ($.cookie('loggedInUser') != undefined) {
			$scope.username = $.cookie('loggedInUser');
		}
		
		
		$scope.logout = function(){
			$rootScope.allowHidePreloader = false;
			delete $scope.username
			$state.go("logoff");
		}
		
		$scope.showMessages = false;
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
})();

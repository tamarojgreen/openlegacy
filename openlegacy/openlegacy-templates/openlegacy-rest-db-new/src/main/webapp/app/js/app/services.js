( function() {

	'use strict';

	/* Services */
	angular.module( 'services', [] )

	/**
	 * Simulate database.
	 */

	.factory( 'contacts', function( $http ) {

		// Return a promise

		return $http.get( 'js/contacts.json' );
	} )

	.factory( '$olHttp', function( $http ) {
		
		return{
			get:function(url,callback){
				$http(
						{
							method : 'GET',
							data : '',
							url : olConfig.baseUrl + url,
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						})
				.success(function(data, status, headers, config) {
					callback(data);
				}).error(function(data, status, headers, config) {					
					alert(data);
				});
				
			},

			post:function(url,model,callback){				
				$http(
						{
							method : 'POST',
							data : angular.toJson(model),
							url : olConfig.baseUrl + url,
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						})
				.success(function(data, status, headers, config) {
					callback(data);
				}).error(function(data, status, headers, config) {
					if (data.error != undefined) {
						alert(data.error);
					} else {						
						alert(data);
					}				
				});
			},
			remove:function(url, callback){	
				$http(
						{
							method : 'DELETE',
							url : olConfig.baseUrl + url,
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						})
				.success(function(data, status, headers, config) {
					console.log("success")
					callback(data);
				}).error(function(data, status, headers, config) {
					console.log("error")
					alert(data);
				});
			}		
		
		};
	} )
	.factory('flatMenu', function($http) {		
		return function(callback) {
			$http({
				method: 'GET',
				data: '',				
				url: olConfig.baseUrl + 'menu',
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(data, status, headers, config) {
				//callback(data.JSONObjectList);
				callback(data.simpleMenuItem);
			}).error(function(data, status, headers, config) {
//				if(data.error){
//					alert('Error: ' + data.error);
//				} else {
//					alert(data);
//				}
			});
		};
	})
		.factory('$idleTimeout', function($olHttp, $timeout, $rootScope, $state) {		
		var timer = null;
		
		var activeMethod = function(awayTimeout) {
			console.log('activeMethod');
			if (timer != null) {
				$timeout.cancel(timer);
				timer = null;
			}			
			var logoff = function() {
				$olHttp.get('logoff', 
					function() {							
						$.removeCookie("loggedInUser", {path: '/'});
						$rootScope.hidePreloader();
						idleTimeout.stop();
						$state.go('login');						
					}
				);
			};
			
			timer = $timeout(logoff, awayTimeout*60*1000, false);
		};
		
		function throttle(callback, limit) {			
		    var wait = false;
		    return function () {		    	
		        if (!wait) {
		            callback.call();
		            wait = true;
		            setTimeout(function () {
		                wait = false;
		            }, limit);
		        }
		    }
		};		
		
		var listener = throttle(function() {				
			activeMethod(olConfig.idleTimeout);
		}, 2000);			
		
		var idleTimeout = {
			'start': function() {				
				window.addEventListener('click', listener);
				window.addEventListener('mousemove', listener);
				window.addEventListener('mouseenter', listener);
				window.addEventListener('keydown', listener);
				window.addEventListener('scroll', listener);
				window.addEventListener('mousewheel', listener);								
			},
			'stop': function() {
				if (timer != null) {
					$timeout.cancel(timer);
					timer = null;
					window.removeEventListener('click', listener);
					window.removeEventListener('mousemove', listener);
					window.removeEventListener('mouseenter', listener);
					window.removeEventListener('keydown', listener);
					window.removeEventListener('scroll', listener);
					window.removeEventListener('mousewheel', listener);					
				}				
			}
		}
		
		return idleTimeout;
	});
	
} )();
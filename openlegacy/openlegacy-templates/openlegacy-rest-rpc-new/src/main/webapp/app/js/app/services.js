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

	.factory( '$olHttp', function( $http, $rootScope ) {
		
		return{
			get:function(url,callback){
				$rootScope.showPreloader();
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
					$rootScope.hidePreloader();
					if(data.error){
						alert(data.error);
					} else {
						alert(data);
					}
				});
				
			},

			post:function(url,model,callback){
				$rootScope.showPreloader();
				$http(
						{
							method : 'POST',
							data : encodeURIComponent(angular.toJson(model)),
							url : olConfig.baseUrl + url,
							headers : {
								'Content-Type' : 'application/json',
								'Accept' : 'application/json'
							}
						})
				.success(function(data, status, headers, config) {
					callback(data);
				}).error(function(data, status, headers, config) {
					$rootScope.hidePreloader();
					if(data.error){
						alert(data.error);
					} else {
						alert(data);
					}
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
				var menuArray = [];					
				var getMenuString = function(data) {						
					angular.forEach(data, function(value) {							
						menuArray.push(value);
						getMenuString(value.menuItems);
				    });					     
				}
				getMenuString(data.simpleMenuItem.menuItems);
				callback(menuArray);
			}).error(function(data, status, headers, config) {
				if(data.error){
					alert('Error: ' + data.error);
				} else {
					alert(data);
				}
			});
		};
	})
	.factory('$themeService', ['$rootScope', function($rootScope) {
		return {
			'changeTheme': function() {
				var themes = this.getThemeList();
				if ($.cookie('ol_theme') == undefined) {
					$.cookie('ol_theme', themes[0]);
				}
				var index = themes.indexOf($.cookie('ol_theme'));
				if (themes.length == index + 1 ) {
					$.cookie('ol_theme', themes[0]);				  
				} else {
					$.cookie('ol_theme', themes[index + 1]);			
				} 
				
				$rootScope.theme = $.cookie('ol_theme');
			},
			
			'getCurrentTheme': function() {				
				if ($.cookie('ol_theme') == undefined) {
					$.cookie('ol_theme', this.getThemeList()[0]);					
					return this.getThemeList()[0];
				} else {					
					return $.cookie('ol_theme');
				}
			},
			
			'getThemeList': function() {
				return ['light', 'emily', 'dynamics'];
			}
			
		};
	}])
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
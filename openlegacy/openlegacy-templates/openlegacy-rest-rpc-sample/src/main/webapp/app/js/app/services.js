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
					alert(data);
				});
			}
		
		
		};
	} )
	.factory('$themeService',['$cookies', '$rootScope', function($cookies, $rootScope) {
		return {
			'changeTheme': function() {
				var themes = this.getThemeList();
				if ($cookies.ol_theme == undefined) {
					$cookies.ol_theme = themes[0];
				}
				var index = themes.indexOf($cookies.ol_theme);
				if (themes.length == index + 1 ) {
					$cookies.ol_theme = themes[0];				  
				} else {
					$cookies.ol_theme = themes[index + 1];			
				} 
				
				$rootScope.theme = $cookies.ol_theme;
			},
			
			'getCurrentTheme': function() {				
				if ($cookies.ol_theme == undefined) {
					$cookies.ol_theme = this.getThemeList()[0];					
					return this.getThemeList()[0];
				} else {					
					return $cookies.ol_theme;
				}
			},
			
			'getThemeList': function() {
				return ['default', 'emily'];
			}
			
		};
	}]);
	
} )();
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
	.factory('flatMenu', function($http) {
		return function(callback) {
			$http({
				method: 'GET',
				data: '',				
				url: olConfig.baseUrl + 'menu/flatMenu',
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
				getMenuString(data.simpleMenuItemList);
				callback(menuArray);
			}).error(function(data, status, headers, config) {
				alert(data);
			});
		};
	});
	
} )();
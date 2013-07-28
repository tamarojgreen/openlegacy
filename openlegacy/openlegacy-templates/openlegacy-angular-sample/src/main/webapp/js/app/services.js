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
							url : BASE_URL + url,
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
							data : JSON.stringify(model),
							url : BASE_URL + url,
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
	} );
	
} )();
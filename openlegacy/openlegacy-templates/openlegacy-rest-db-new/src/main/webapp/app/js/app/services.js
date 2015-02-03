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
				console.log("asdasd");
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
					console.log("succ")
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
				callback(data.JSONObjectList);				
			}).error(function(data, status, headers, config) {
//				if(data.error){
//					alert('Error: ' + data.error);
//				} else {
//					alert(data);
//				}
			});
		};
	});
	
} )();
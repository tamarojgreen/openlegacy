( function() {

	'use strict';
	
	/* Services */
	angular.module( 'services', [] )
	
	.factory('$olHttp', function( $http, $rootScope ) {		
		return {
			get:function(url, callback) {
				$rootScope.showPreloader();
				$http({
		                method : 'GET',
		                data : '',
		                url : olConfig.baseURL + '/' + url,
		                headers : {
		                    'Content-Type' : 'application/json',
		                    'Accept' : 'application/json'
		                }
		            }
		        )
		        .success(function(data,status,headers,config){		        	
//		        	if (data != "" && data != null && data != undefined) {
//		        		data = angular.fromJson(data);
//		        	}		        	
		        	
		        	callback(data);
		        })
		        .error(function(data, status, headers, config){
		        	$rootScope.hidePreloader();
		            alert(data)
		        });
	        },
		        
			post: function(url, data, callback) {
				$rootScope.showPreloader();
				var findAndClearActions = function(data) {						
					if (data.actions != null && data.actions != undefined) {
						data.actions = null;
					}			
					for (var key in data) {
					  if (typeof data[key] == "object" && data[key] != null) {
						if (data[key].actions != null && data[key].actions != undefined) {
							data[key].actions = null;
						}
						
						findAndClearActions(data[key]);
					  }
					}
					
					return data;
				};				
				
		    	$http({
		    		method : 'POST',
		            data : angular.toJson(findAndClearActions(data)),
		            url : olConfig.baseURL + "/" + url,
		            headers : {
		                'Content-Type' : 'application/json',
		                'Accept' : 'application/json'
		            }
		    	}).success(function(data, status, headers, config) {		    		
		    		callback(angular.fromJson(data));    		
		    	})
		    	.error(function(data, status, headers, config){
		    		$rootScope.hidePreloader();
		            alert(data)
		        });
			}				
		};
	})
	
	.factory('flatMenu', function($http) {
		return function(callback) {
			$http({
				method: 'GET',
				data: '',				
				url: olConfig.baseURL + '/menu/flatMenu',
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
				hidePreloader();
				if(data.error){
					alert('Error: ' + data.error);
				} else {
					alert(data);
				}
			});
		};
	})
	
	.factory('$themeService', ['$cookies', '$rootScope', function($cookies, $rootScope) {
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
				return ['light', 'emily', 'dynamics'];
			}
			
		};
	}]);
})();


       


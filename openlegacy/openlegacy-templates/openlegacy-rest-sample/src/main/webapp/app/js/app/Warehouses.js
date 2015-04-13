/*module name for controllers can be change by the user. Please, keep in mind to replace module name with
the new one for designtime generation.		 
*/

angular.module('controllers').controller('WarehousesCtrl', function($scope, $http, $olHttp,$stateParams, $themeService, $rootScope, $state,$modal, uiGmapGoogleMapApi) {
		$scope.noTargetScreenEntityAlert = function() {
			alert('No target entity specified for table action in table class @ScreenTableActions annotation');
		}; 
		
		$scope.isReadOnly = function(data,column){
			return $rootScope.isReadOnly(data,column,$scope.model);
		}
		$scope.readOnlyCss = function(data,column){
			return $rootScope.readOnlyCss(data,column);
		}
		$scope.isActionAvailable = function(alias){
			return $rootScope.isActionAvailable(alias,$scope.model);
		};
		
		$scope.read = function(){						  
		      $olHttp.get('Warehouses/'  + "?children=false",
				function(data) {					    	  	
					$scope.model = data.model.entity;							
					$scope.baseUrl = olConfig.baseUrl;
					$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
					
					
					$scope.doActionNoTargetEntity = function(alias) {					
					    var suffix = alias != null ? "&action=" + alias : "";
						$olHttp.post('Warehouses/?children=false' + suffix, $rootScope.clearObjectsFromPost($scope.model), function(data) {
							if (data.model.entityName == 'Warehouses'){											
								$scope.model = data.model.entity;
								$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
								$rootScope.hidePreloader();
							} else {
								$state.go(data.model.entityName);
							}
						});
					};
					
					
					$rootScope.hidePreloader();
		    	  		$rootScope.allowShowPreloader = true;
				}							
			);
		};		

		$scope.doAction = function(entityName, actionAlias) {						
			if (actionAlias == "") {
	    		var url = entityName + actionAlias;
	    	} else {
	    		var url = entityName + "?action=" + actionAlias;
	    	}  
			
			if (actionAlias.indexOf("lookup-") > -1 || false == 'true') {
				$rootScope.showPreloader(false);
			}
			
			$olHttp.post(url,$rootScope.clearObjectsFromPost($scope.model), 
				function(data) {
					if (data == ""){
						$state.go("emulation");
						return;
					}
					if (data.model.entityName == entityName){
						$rootScope.hidePreloader();
						$scope.model = data.model.entity;								
					} else {
						if (data.model.window){
							$rootScope.modalInstance = $modal.open({
								templateUrl: $state.get(data.model.entityName).views[""].templateUrl,
								controller: $state.get(data.model.entityName).views[""].controller,										
							});
						}
						else{
							$state.go(data.model.entityName);
						}
					}
				}
			);
		};			
		
		$scope.map = {};
		$scope.options = {};
		$scope.markers = {
			      id: 0,
			      coords: {
			        latitude: 0,
			        longitude: 0
			      },
			      options: { draggable: false },
			      events: {
			        dragend: function (marker, eventName, args) {
			          $log.log('marker dragend');
			          var lat = marker.getPosition().lat();
			          var lon = marker.getPosition().lng();
			          $log.log(lat);
			          $log.log(lon);

			          $scope.marker.options = {
			            draggable: true,
			            labelContent: "lat: " + $scope.marker.coords.latitude + ' ' + 'lon: ' + $scope.marker.coords.longitude,
			            labelAnchor: "100 0",
			            labelClass: "marker-labels"
			          };
			        }
			      }
			    };
		
		/*uiGmapGoogleMapApi.then(function(maps) {
			setMapLocation("HaTa'asiya St Tel Aviv-Yafo");
	    });*/
		
		$scope.setMapLocation = function (address) {
			var addr = 'https://maps.googleapis.com/maps/api/geocode/json?address=' + address;
			$http.get(addr).
			  success(function(result, status, headers, config) {
				  var lat = result.results[0].geometry.location.lat;
				  var lng = result.results[0].geometry.location.lng;
				  $scope.map = {center: {latitude: lat, longitude: lng}, zoom: 14 };
			      $scope.options = {scrollwheel: false};
			      $scope.markers.coords = {
			    	        latitude: lat,
			    	        longitude: lng
			    	      };
			  }).
			  error(function(result, status, headers, config) {
				  
			  });		
		}

		$scope.read();
	});

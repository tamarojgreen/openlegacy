/*module name for controllers can be change by the user. Please, keep in mind to replace module name with
the new one for designtime generation.		 
*/

angular.module('controllers').controller('ItemReviseCtrl', function($scope, $olHttp,$stateParams, $themeService, $rootScope, $state,$modal) {
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
		      $olHttp.get('ItemRevise/' + "?children=true",
				function(data) {					    	  	
					$scope.model = data.model.entity;							
					$scope.baseUrl = olConfig.baseUrl;
					$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
					
					
					$scope.doActionNoTargetEntity = function(alias) {					
					    var suffix = alias != null ? "&action=" + alias : "";
						$olHttp.post('ItemRevise/?children=false' + suffix, $rootScope.clearObjectsFromPost($scope.model), function(data) {
							if (data.model.entityName == 'ItemRevise'){											
								$scope.model = data.model.entity;
								$rootScope.$broadcast("olApp:breadcrumbs", data.model.paths);
								$rootScope.hidePreloader();
							} else {
								$state.go(data.model.entityName);
							}
						});
					};
					
						var tabsContent = {};						
						tabsContent["ItemRevise"] = $scope.model;
						$scope.loadTab = function(entityName) {
							if (tabsContent[entityName] == null) { 
								$scope.model.actions=null;											
								$olHttp.get(entityName + '/'  + "?children=false", 
									function(data) {													
										$scope.model = data.model.entity;
										tabsContent[entityName] = data.model.entity;
										$rootScope.hidePreloader();
									});
							} else {
								$scope.model = tabsContent[entityName];
							}					
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
		
		$scope.read();
		
		// sales chart options
	    $scope.chartOptions = {
	    	"salesChartUSD":{
	            title: {
	            	text: 'Monthly Sales Worldwide',
	                x: -20 //center
	            },
	            subtitle: {
	                text: 'in USD',
	                x: -20
	            },
	            xAxis: {
	                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	            },
	            yAxis: {
	                title: {
	                    text: 'Sales (USD)'
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }]
	            },
	            tooltip: {
	                valueSuffix: '$'
	            },
	            legend: {
	                layout: 'vertical',
	                align: 'right',
	                verticalAlign: 'middle',
	                borderWidth: 0
	            },
	            series: [{
	                name: 'Sales',
	                data: [11234, 11762, 14526, 15662, 17653, 14050, 12293, 11532, 12773, 15762, 16273, 17839]
	            }]    		    			
	    	}, //end of sales chart in USD options
	    	"salesChartItems":{
	            title: {
	            	text: 'Monthly Sales Worldwide',
	                x: -20 //center
	            },
	            subtitle: {
	                text: 'Number of itesm sold',
	                x: -20
	            },
	            xAxis: {
	                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	            },
	            yAxis: {
	                title: {
	                    text: 'Sales (Items)'
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }]
	            },
	            tooltip: {
	                //valueSuffix: '$'
	            },
	            colors: ['#ff4136'],           	         
	            legend: {
	                layout: 'vertical',
	                align: 'right',
	                verticalAlign: 'middle',
	                borderWidth: 0
	            },
	            series: [{
	                name: 'Sales',
	                data: [234, 244, 298, 325, 355, 332, 254, 240, 276, 311, 319, 332]
	            }]    		    			
	    	}, //end of sales chart in items options
	    } //end of chartOptions 
	});

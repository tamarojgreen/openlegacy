
var olControllers = angular.module('olControllers', ["ui.bootstrap"]);

olControllers.controller('logonCtrl', ['$rootScope', '$state', '$scope','$http', '$location', '$olData', function ($rootScope, $state, $scope, $http, $location, $olData) {
    	$scope.login = function(username, password) {
    		$olData.login('login?user=' + username + '&password='+ password, 
					function() {
    					$rootScope.user = username;
	    				$state.go("MainMenu");
					}
				);
		};
}]);

olControllers.controller('HeaderCtrl', ['$rootScope', '$state','$scope','$http', '$location', '$themeService', "$olData", "$modal", function ($rootScope, $state, $scope, $http, $location, $themeService, $olData, $modal) {    
	if ($rootScope.user != undefined) {
		$scope.username = $rootScope.user
	}
	
	$scope.showMessages = false;
	$olData.getMessages(function(data){		
		if (data.model != null && data.model != undefined && data.model != "") {				
			$scope.showMessages = true;			
			
			$scope.messages = function() {				
				var modalInstance = $modal.open({
					template: $("#messagesModal").html(),
					controller: "messagesModalCtrl",
					resolve:{
						messages: function() {
							return data.model;
						} 
					}
				});
			};
			
			if (olConfig.showSystemMessages) {				
				$scope.messages();
			}
			
		}		
	});
	
	$scope.logout = function(){
		$olData.logoff(function() {
					delete $scope.username
					delete $rootScope.user
					$state.go("logon")
				}
			);		
	}
	
	$scope.changeTheme = function() {
		$themeService.changeTheme();
	};
	
}]);

olControllers.controller('messagesModalCtrl', ['$scope', '$modalInstance','messages', function($scope, $modalInstance, messages) {	
	$scope.messages = messages;	
	$scope.close = function() {
		$modalInstance.close();
	};
	
}]);

olControllers.controller('FooterCtrl', ['$scope','$http', '$location', function ($scope, $http, $location) {    
    $scope.testtxt = "footer"
}]);

olControllers.controller('sidebarCtrl', ['$scope','$http', '$location', function ($scope, $http, $location) {    
    $scope.testtxt = "sidebar"
}]);

olControllers.controller('warehouseListCtrl', ['$scope','$http', '$location', '$stateParams', '$state', '$olData', function ($scope, $http, $location, $stateParams, $state, $olData) {    
    $olData.getWarehouses(function(data){        
        $scope.warehouses = data.model.entity.warehousesRecords
        $scope.actions = data.model.entity.actions;
        
        $scope.postAction = function(actionAlias) {        	
        	$olData.postAction(data.model.entityName, actionAlias, data.model.entity, function(data) {        		
        		$state.go(data.model.entityName);
        	});
        };
        
        $scope.exportExcelUrl = olConfig.baseURL + "/" + data.model.entityName + "/excel";
    });
}]);

olControllers.controller('warehouseDetailsCtrl', ['$scope','$http', '$location', '$stateParams', '$state', '$olData', function ($scope, $http, $location, $stateParams, $state, $olData) {
	$olData.getWarehouseDetails($stateParams.warehouseId,function(data){		
		$scope.warehouseDetails = data.model.entity;		
		$scope.actions = data.model.entity.actions;
		
		$scope.postAction = function(actionAlias) {        	
	    	$olData.postAction(data.model.entityName, actionAlias, data.model.entity, function(data) {
	    		//$state.go(data.model.entityName);
	    	});
	    };
	});
	
	$olData.getWarehouseTypes(function(data) {		
		$scope.types = data.model.entity.warehouseTypesRecords;
		$scope.WhTypeClick = function(type) {
			$scope.warehouseDetails.warehouseType = type.type;			
		}
	});
}]);




olControllers.controller('itemListCtrl', ['$scope','$http', '$location', '$stateParams', '$state', '$olData', function ($scope, $http, $location, $stateParams, $state, $olData) {	
	$olData.getItems(function(data){                
        $scope.items = data.model.entity.itemsRecords
        $scope.actions = data.model.entity.actions;
        
        $scope.postAction = function(actionAlias) {        	
        	$olData.postAction(data.model.entityName, actionAlias, data.model.entity, function(data) {        		
        		if ($state.current.name == data.model.entityName) {
        			$scope.items = data.model.entity.itemsRecords
        		} else {
        			$state.go(data.model.entityName);
        		}        		
        	});
        };
        
        $scope.exportExcelUrl = olConfig.baseURL + "/" + data.model.entityName + "/excel";        
    });
}]);



olControllers.controller('itemDetailsCtrl', ['$scope','$http', '$location', '$stateParams', '$state', '$olData', function ($scope, $http, $location, $stateParams, $state, $olData) {
	$olData.getItemDetails($stateParams.itemId,function(data){
		$scope.uploadStockItemImage = function(files) {			
			var formData = new FormData();
			formData.append("file",files[0]);
	        
	        $http.post(olConfig.baseURL + '/uploadImage', formData, {
	        	transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}     
		            
		        }).success(function(data, status) {
		        	if (data.filename != null || data.filename != "") {
		        		var fnameWithoutExt = data.filename.split(".")[0];
		        		if (fnameWithoutExt != null) {
		        			$(".modal_images").append("<div id='" + fnameWithoutExt + "' class='modal fade' tabindex='-1' role='dialog' aria-labelledby='screenshotModal' aria-hidden='true'><div class='modal-dialog modal-lg'><div class='modal-content text-center transparant'><img src='" + olConfig.baseURL + "/image?filename=" + data.filename + "' class='img-thumbnail'></div></div></div>");
			        		$(".images").append("<div class='col-sm-4'><a  data-toggle='modal' data-target='#" + fnameWithoutExt + "' class='clickable'><img src='" + olConfig.baseURL + "/image?filename=" + data.filename + "' class='img-thumbnail'></a></div>");
		        		} 
		        		
		        	}
		        }).error(function(data, status) {
	
		        });
			};
		
		$scope.itemDetails = data.model.entity
		$scope.actions = data.model.entity.actions;
		
		$scope.postAction = function(actionAlias) {
			var payload_data = data.model.entity;			
			payload_data.itemDetails2.actions = null;			
	    	$olData.postAction(data.model.entityName, actionAlias, payload_data, function(data) {
	    		//$state.go(data.model.entityName);	    		
	    	});
	    }; 
		
	});

    $olData.getShippingList(function(data){
        $scope.shippingList = data.shippingList;
    });  
	
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
}]);



var showPreloader = function() {
	$(".preloader").show();
	$(".content-wrapper").hide();
};

var hidePreloader = function() {
	if (allowHidePreloader = true) {			
		$(".preloader").hide(0);
		$(".content-wrapper").show(0);
	} else {
		allowHidePreloader = true;
	}		
};

var olControllers = angular.module('controllers', []);

olControllers.controller('loginController', function ($rootScope, $state, $scope, $olHttp) {			
	if ($.cookie('loggedInUser') != undefined) {
		$state.go('menu');
	}
	
	$scope.login = function(username, password) {
		showPreloader();
		$olHttp.get('login?user=' + username + '&password='+ password,	function() {
				var $expiration = new Date();				
				$expiration.setTime($expiration.getTime() + loginExpirationTime*60*1000);
				
				$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});				
				$rootScope.$broadcast("olApp:login:authorized", username);				
				$state.go('menu');							
			}
		);
	};	
});

olControllers.controller('logoffController', function ($rootScope, $state, $scope, $olHttp) {	
	$olHttp.get('logoff', 
		function() {
			hidePreloader();
			$.removeCookie("loggedInUser", {path: '/'});
		}
	);
});

olControllers.controller('HeaderController', function ($rootScope, $state, $scope, $olHttp, $themeService, $modal) {	
	$rootScope.$on("olApp:login:authorized", function(e, value) {
		$scope.username = value;
	});
	
	if ($.cookie('loggedInUser') != undefined) {
		$scope.username = $.cookie('loggedInUser');		
	}
	
	$scope.logout = function() {
		showPreloader();
		allowHidePreloader = false;
		delete $scope.username
		$state.go("logoff")			
	}
	
	$scope.showMessages = false;
	$olHttp.get('messages', function(data){		
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
	
	
	$scope.changeTheme = function() {
		$themeService.changeTheme();
	};
	
});

olControllers.controller('messagesModalCtrl', ['$scope', '$modalInstance','messages', function($scope, $modalInstance, messages) {	
	$scope.messages = messages;	
	$scope.close = function() {
		$modalInstance.close();
	};
	
}]);

olControllers.controller('warehouseListCtrl', ['$scope', '$state', '$olHttp', 'flatMenu',  function ($scope, $state, $olHttp, flatMenu) {    
    $olHttp.get("Warehouses", function(data) {
    	hidePreloader();    	
    	$scope.model = data.model;
        $scope.entity = data.model.entity;        
        $scope.breadcrumbs = data.model.paths;
        
        flatMenu(function(data) {
			$scope.menuArray = data;
		});
        
        $scope.doAction = function(actionAlias) {
        	showPreloader();
        	if (actionAlias == "") {
        		var url = data.model.entityName + actionAlias;
        	} else {
        		var url = data.model.entityName + "?action=" + actionAlias;
        	}        	
        	
        	$olHttp.post(url, data.model.entity, function(data) {        		
        		if (data.model.entityName == $scope.model.entityName) {
        			hidePreloader();
        			$scope.entity = data.model.entity;
        		} else {
        			allowHidePreloader = false;
        			$state.go(data.model.entityName);
        		}        		
        	});
        };
        
        $scope.exportExcelUrl = olConfig.baseURL + "/" + data.model.entityName + "/excel"; 
    });
}]);

olControllers.controller('warehouseDetailsCtrl', ['$scope', '$stateParams', '$state', '$olHttp', 'flatMenu', function ($scope, $stateParams, $state, $olHttp, flatMenu) {
	$olHttp.get("WarehouseDetails/" + $stateParams.warehouseId, function(data){
		hidePreloader();
		$scope.model = data.model;
        $scope.entity = data.model.entity;        
        $scope.breadcrumbs = data.model.paths;
        
        flatMenu(function(data) {
			$scope.menuArray = data;
		});
		
		$scope.doAction = function(actionAlias) {
			showPreloader();
			if (actionAlias == "") {
        		var url = data.model.entityName + actionAlias;
        	} else {
        		var url = data.model.entityName + "?action=" + actionAlias;
        	}       
			
	    	$olHttp.post(url, data.model.entity, function(data) {
	    		if (data.model.entityName == $scope.model.entityName){
	    			hidePreloader();
	    			$scope.entity = data.model.entity;								
				}
				else {
					allowHidePreloader = false;
					$state.go(data.model.entityName);
				}	    		
	    	});
	    };
	});
	
	$olHttp.get("WarehouseTypes", function(data) {		
		$scope.types = data.model.entity.warehouseTypesRecords;
		$scope.WhTypeClick = function(type) {
			$scope.entity.warehouseType = type.type;			
		}
	});
}]);

olControllers.controller('warehouseTypesCtrl', ['$scope', '$state', '$olHttp', 'flatMenu', function ($scope, $state, $olHttp, flatMenu) {
	$olHttp.get("WarehouseTypes", function(data) {
		hidePreloader();
		$scope.model = data.model;
        $scope.entity = data.model.entity;        
        $scope.breadcrumbs = data.model.paths;
        
        flatMenu(function(data) {
			$scope.menuArray = data;
		});
        
		$scope.doAction = function(actionAlias) {
			showPreloader();
			if (actionAlias == "") {
        		var url = data.model.entityName + actionAlias;
        	} else {
        		var url = data.model.entityName + "?action=" + actionAlias;
        	}       
			
	    	$olHttp.post(url, data.model.entity, function(data) {	    		
	    		if (data.model.entityName == $scope.model.entityName){
	    			hidePreloader();
	    			$scope.entity = data.model.entity;								
				}
				else {
					allowHidePreloader = false;
					$state.go(data.model.entityName);
				}	    		
	    	});
	    };
	    $scope.exportExcelUrl = olConfig.baseURL + "/" + data.model.entityName + "/excel";
	});
}]);




olControllers.controller('itemListCtrl', ['$scope', '$state', '$olHttp', 'flatMenu', function ($scope, $state, $olHttp, flatMenu) {	
	$olHttp.get("Items", function(data){
		hidePreloader();
		$scope.model = data.model;
        $scope.entity = data.model.entity;        
        $scope.breadcrumbs = data.model.paths;
        
        flatMenu(function(data) {
			$scope.menuArray = data;
		});
        
    	$scope.doAction = function(actionAlias) {
			showPreloader();
			if (actionAlias == "") {
        		var url = data.model.entityName + actionAlias;
        	} else {
        		var url = data.model.entityName + "?action=" + actionAlias;
        	}       
			
	    	$olHttp.post(url, data.model.entity, function(data) {	    		
	    		if (data.model.entityName == $scope.model.entityName){
	    			hidePreloader();
	    			$scope.entity = data.model.entity;								
				}
				else {
					allowHidePreloader = false;
					$state.go(data.model.entityName);
				}	    		
	    	});
	    };
        
        $scope.exportExcelUrl = olConfig.baseURL + "/" + data.model.entityName + "/excel";        
    });
}]);



olControllers.controller('itemDetailsCtrl', ['$scope', '$stateParams', '$state', '$olHttp', 'flatMenu', function ($scope, $stateParams, $state, $olHttp, flatMenu) {
	$olHttp.get("ItemDetails/" + $stateParams.itemId,function(data){
		hidePreloader();
		$scope.model = data.model;
        $scope.entity = data.model.entity;        
        $scope.breadcrumbs = data.model.paths;
        
        flatMenu(function(data) {
			$scope.menuArray = data;
		});
        
        $scope.doAction = function(actionAlias) {
			showPreloader();
			if (actionAlias == "") {
        		var url = data.model.entityName + actionAlias;
        	} else {
        		var url = data.model.entityName + "?action=" + actionAlias;
        	}       
			
	    	$olHttp.post(url, data.model.entity, function(data) {	    		
	    		if (data.model.entityName == $scope.model.entityName){
	    			hidePreloader();
	    			$scope.entity = data.model.entity;								
				}
				else {
					hidePreloader();
					//$state.go(data.model.entityName);
				}	    		
	    	});
	    };
		
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
	});
	
    $scope.shippingList = [
      {name:"Jennifer R. Young", address:"3103 Byrd Lane Tijeras NM 87008", phone:"903-427-0380", date:"10/11/2012"},
      {name:"Arthur B. Craft", address:"2056 Florence Street Clarksville TX 75426", phone:"902-327-1340", date:"13/10/2012"},
      {name:"Brian K. Milliken", address:"2870 Mudlick Road Spokane WA 99201", phone:"509-481-5251", date:"12/03/2012"},
      {name:"John M. Popham", address:"4943 My Drive New York NY 10011", phone:"347-228-6752", date:"10/04/2013"}
    ];
    
      
	
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


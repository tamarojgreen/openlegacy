
var olControllers = angular.module('olControllers', []);

olControllers.controller('HeaderCtrl', ['$scope','$http', '$location', function ($scope, $http, $location) {    
    $scope.testtxt = "header"
}]);

olControllers.controller('FooterCtrl', ['$scope','$http', '$location', function ($scope, $http, $location) {    
    $scope.testtxt = "footer"
}]);

olControllers.controller('sidebarCtrl', ['$scope','$http', '$location', function ($scope, $http, $location) {    
    $scope.testtxt = "sidebar"
}]);



olControllers.controller('itemListCtrl', ['$scope','$http', '$location', '$stateParams', '$state', '$olData', function ($scope, $http, $location, $stateParams, $state, $olData) {    
    $olData.getItems(function(data){
        console.log(JSON.stringify(data.model.entity.itemsRecords));
        $scope.items = data.model.entity.itemsRecords
    });      
}]);



olControllers.controller('itemDetailsCtrl', ['$scope','$http', '$location', '$stateParams', '$state', '$olData', function ($scope, $http, $location, $stateParams, $state, $olData) {    
	console.log(JSON.stringify($stateParams.itemId));
	
	$olData.getItemDetails($stateParams.itemId,function(data){
		console.log(JSON.stringify(data.model.entity));
		$scope.itemDetails = data.model.entity
	});  

    $olData.getShippingList(function(data){
        $scope.shippingList = data.shippingList;
    });  
	
	
	
}]);


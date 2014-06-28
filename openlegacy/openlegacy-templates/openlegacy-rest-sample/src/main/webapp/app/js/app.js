var olApp = angular.module('olApp', [
    'ui.router',
    'ngTouch',
    'olControllers'
]).run(['$rootScope', '$state', function ($rootScope, $state) {
     
    // here will be the code to pevent unauthorised access to pages
    $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {        
    	if ($rootScope.user == undefined && toState.name != 'logon') {
        console.log('not logged in, forcing dashboard');
            event.preventDefault(); 
            $state.go("logon");
      }
    });
    
}]);
 
olApp.config(function($stateProvider, $urlRouterProvider) {
    // For any unmatched url, redirect to /state1
    $urlRouterProvider.otherwise("/itemList");
    // Now set up the states
    $stateProvider
    .state('itemList', {
        url: "/itemList",
        views: {
            "main": { 
                templateUrl: "partials/itemList.html",
                controller: 'itemListCtrl'
            },
            //"sidebar": { 
            //    templateUrl: "partials/sidebar.html",
            //    controller: 'SidebarCtrl'                
            //},            
            "header": { 
                templateUrl: "partials/header.html",
                controller: 'HeaderCtrl'
            }
            //"footer": { 
            //    templateUrl: "partials/footer.html",
            //    controller: 'FooterCtrl'
            //}
        }
    })    
    .state('itemDetails', {
        url: "/itemDetails/:itemId",
        views: {
            "main": { 
                templateUrl: "partials/itemDetails.html",
                controller: 'itemDetailsCtrl'
            },
            //"sidebar": { 
            //    templateUrl: "partials/sidebar.html",
            //    controller: 'SidebarCtrl'
            //},                        
            "header": { 
                templateUrl: "partials/header.html",
                controller: 'HeaderCtrl'
            },
            //"footer": { 
            //    templateUrl: "partials/footer.html",
            //    controller: 'FooterCtrl'
            //}
        }
    })
    .state('warehouseList', {
        url: "/warehouseList",
        views: {
            "main": { 
                templateUrl: "partials/warehouseList.html",
                controller: 'warehouseListCtrl'
            },
            //"sidebar": { 
            //    templateUrl: "partials/sidebar.html",
            //    controller: 'SidebarCtrl'
            //},                        
            "header": { 
                templateUrl: "partials/header.html",
                controller: 'HeaderCtrl'
            },
            //"footer": { 
            //    templateUrl: "partials/footer.html",
            //    controller: 'FooterCtrl'
            //}
        }
    })
    .state('warehouseDetails', {
        url: "/warehouseDetails/:warehouseId",
        views: {
            "main": { 
                templateUrl: "partials/warehouseDetails.html",
                controller: 'warehouseDetailsCtrl'
            },
            //"sidebar": { 
            //    templateUrl: "partials/sidebar.html",
            //    controller: 'SidebarCtrl'
            //},                        
            //"header": { 
            //    templateUrl: "partials/header.html",
            //    controller: 'HeaderCtrl'
            //},
            //"footer": { 
            //    templateUrl: "partials/footer.html",
            //    controller: 'FooterCtrl'
            //}
        }
    })
    .state('logon', {
        url: "/logon",
        views: {
            "main": { 
                templateUrl: "partials/logon.html",
                controller: 'logonCtrl'
            },
            //"sidebar": { 
            //    templateUrl: "partials/sidebar.html",
            //    controller: 'SidebarCtrl'
            //},                        
            "header": { 
                templateUrl: "partials/header.html",
                controller: 'HeaderCtrl'
            },
            //"footer": { 
            //    templateUrl: "partials/footer.html",
            //    controller: 'FooterCtrl'
            //}
        }
    })
});

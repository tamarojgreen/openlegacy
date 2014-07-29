var olApp = angular.module('olApp', [
    'ui.router',
    'ngTouch',
    'olControllers',
    'ngCookies'
]).run(['$rootScope', '$state', '$themeService', function ($rootScope, $state, $themeService) {
     
    // here will be the code to pevent unauthorised access to pages
    $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {        
    	if ($rootScope.user == undefined && toState.name != 'logon') {
        console.log('not logged in, forcing dashboard');
            event.preventDefault(); 
            $state.go("logon");
      }
    });
    
    $rootScope.theme = $themeService.getCurrentTheme();
}]);
 
olApp.config(function($stateProvider, $urlRouterProvider) {
    // For any unmatched url, redirect to /state1	 
    $urlRouterProvider.otherwise("/Items");
    // Now set up the states
    $stateProvider
    .state('Items', {
        url: "/Items",
        views: {
            "main": { 
                templateUrl: "partials/Items.html",
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
    .state('ItemDetails', {
        url: "/ItemDetails/",
        views: {
            "main": { 
                templateUrl: "partials/ItemDetails.html",
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
    .state('ItemDetailsWithId', {
        url: "/ItemDetails/:itemId",
        views: {
            "main": { 
                templateUrl: "partials/ItemDetails.html",
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
    .state('Warehouses', {
        url: "/Warehouses",
        views: {
            "main": { 
                templateUrl: "partials/Warehouses.html",
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
    .state('WarehouseDetails', {
        url: "/WarehouseDetails/",
        views: {
            "main": { 
                templateUrl: "partials/WarehouseDetails.html",
                controller: 'warehouseDetailsCtrl'
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
    .state('WarehouseDetailsWithId', {
        url: "/WarehouseDetails/:warehouseId",
        views: {
            "main": { 
                templateUrl: "partials/WarehouseDetails.html",
                controller: 'warehouseDetailsCtrl'
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
    .state('MainMenu', {
    	url: "/MainMenu",
    	views: {
    		"main": {
    			templateUrl: "partials/MainMenu.html"
    		},
    		"header": { 
                templateUrl: "partials/header.html",
                controller: 'HeaderCtrl'
            }
    	}
    })
    .state('InventoryMenu', {
    	url: "/InventoryMenu",
    	views: {
    		"main": {
    			templateUrl: "partials/InventoryMenu.html"
    		},
    		"header": { 
                templateUrl: "partials/header.html",
                controller: 'HeaderCtrl'
            }
    	}
    })    
});

var olApp = angular.module('olApp', [
    'ui.router',
    'ngTouch',
    'olControllers'
]).run(['$rootScope', '$state', function ($rootScope, $state) {
    /* 
    // here will be the code to pevent unauthorised access to pages
    $rootScope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {    
      
        if ($rootScope.user == undefined && toState.name != 'dashboard') {
        console.log('not logged in, forcing dashboard');
            event.preventDefault(); 
            $state.go("dashboard");
      }
    });
    */
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
            }
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
    .state('details', {
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
});

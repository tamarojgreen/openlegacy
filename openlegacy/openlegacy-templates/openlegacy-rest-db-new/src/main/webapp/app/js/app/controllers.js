(function() {

	'use strict';

	/* Controllers */

	var module = angular.module('controllers', ['ui.bootstrap'])

	module = module.controller( 'loginCtrl', function($scope, $olHttp, $rootScope, $stateParams, $state) {
		$scope.login = function(username, password) {
			var data = {
				"user" : username,
				"password" : password
			}
			$olHttp.post('login', data, function() {
				var $expiration = new Date();
				var minutes = olConfig.expiration;
				$expiration.setTime($expiration.getTime() + minutes	* 60 * 1000)
	
				$.cookie('loggedInUser', username, {expires: $expiration, path: '/'});
				$rootScope.$broadcast("olApp:login:authorized", username);
				$state.go($stateParams.redirectTo.name, $stateParams.redirectTo.params);
			});
		};
	});
	module = module.controller('logoffCtrl', function($scope, $olHttp, $rootScope) {				
			$olHttp.get('logoff', 
				function() {
					$rootScope.hidePreloader();
					$.removeCookie("loggedInUser", {path: '/'});
				}
			);
		});
	module = module.controller('menuCtrl', function(flatMenu, $scope, $olHttp) {		
		flatMenu(function(data) {
			$scope.menuArray = data;			
		});
		$scope.reload = function() {
			$olHttp.get('reload', function() {
				location.reload();
			});
		};
	});
	module = module.controller('headerCtrl', function ($rootScope, $scope, $state) {
		if ($.cookie('loggedInUser') != undefined) {
			$scope.username = $.cookie('loggedInUser');
		}
		
		
		$scope.logout = function(){
			$rootScope.allowHidePreloader = false;
			delete $scope.username
			$state.go("logoff");
		}

	});
	// template for all entities 
	<#if entitiesDefinitions??>
	<#list entitiesDefinitions as entityDefinition>
		<#list entityDefinition.actions as action>
			<#switch action.actionName>
				<#case "READ">
	//=================================READ======================================
	module = module.controller('${entityDefinition.entityName}DetailsCtrl', function($scope, $stateParams, $olHttp, $state, $modal) {		
		$scope.currentAction = "READ";
		
		$olHttp.get('${entityDefinition.entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {			
			$scope.entityName = data.model.entityName;
			$scope.model = data.model;			
			$scope.doREADAction = function(targetEntityName, rowIndex, propertyName) {				
	        	<#list entitiesDefinitions as entity>
	        		if (targetEntityName == "${entity.entityName}") {
	        			var targetData = $scope.model.entity[propertyName]
	        			var keys = Object.keys( targetData );	        			    
    			    	$state.go(targetEntityName + "Details", {${entity.keys[0].name?replace(".", "_")}:<#list entity.keys as key>targetData[keys[rowIndex]].${key.name}<#if key_has_next>+</#if></#list>});
	        		}
	        	</#list>
        	}
			$scope.doUPDATEAction = function() {				
				var modalInstance = $modal.open({
					templateUrl: 'views/partials/confirmation_dialog.html',
					controller: 'ConfirmationDialogCtrl',
					resolve: {
						func: function () {
							return function() {
								$olHttp.post('${entityDefinition.entityName}?action=', $scope.model.entity, function(data) {					
									alert("Entity was successfully updated!");
								});
							} 
						}
					}
			    });				
			}
			
			$scope.doDELETEAction = function() {
				var modalInstance = $modal.open({
					templateUrl: 'views/partials/confirmation_dialog.html',
					controller: 'ConfirmationDialogCtrl',					
					resolve: {
						func: function () {
							return function() {
								$olHttp.remove('${entityDefinition.entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {
									$state.go('${entityDefinition.entityName}');
								});
							} 
						}
					}
			    });
			}
		});
	});
				<#break>
				<#case "CREATE">
	//==========================================CREATE========================================================			
	module = module.controller('${entityDefinition.entityName}NewCtrl', function($scope, $modal, $olHttp, $state) {
		$scope.currentAction = "CREATE";
		$scope.model = {'entity':{}};
		$scope.nestedModels = {};
		
		
		<#if entityDefinition.columnFieldsDefinitions??>
		<#list entityDefinition.columnFieldsDefinitions?keys as key>								
			<#assign column = entityDefinition.columnFieldsDefinitions[key]>
			<#if (!column.internal?? || column.internal == false) && column.oneToManyDefinition??>
				$scope.${column.name}_showNext = true;
				$scope.${column.name}_showPrev = true;				
				getItems('${column.javaTypeName}', '${column.name}', false, 1, $scope, $olHttp, $state, null);
				$scope.nestedModels['${column.name}'] = [];
			</#if>
		</#list>
		</#if>
		
		$scope.toggleSelection = function toggleSelection(item, itemArray, joinColumnName) {
			delete item[joinColumnName];
		    var idx = itemArray.indexOf(item);

		    if (idx > -1) {
		    	itemArray.splice(idx, 1);
		    } else {		    	
		    	itemArray.push(item);
		    }
		  };
		
		$scope.doUPDATEAction = function() {				
			var modalInstance = $modal.open({
				templateUrl: 'views/partials/confirmation_dialog.html',
				controller: 'ConfirmationDialogCtrl',
				resolve: {
					func: function () {
						return function() {
							var entity = $.extend($scope.model.entity, $scope.nestedModels);							
							$olHttp.post('${entityDefinition.entityName}?action=', entity, function(data) {
								alert("Entity was created successfully!");
								$state.go('${entityDefinition.entityName}');
							});												
						} 
					}
				}
		    });				
		}
	});
			</#switch>
		</#list>
	//=============================================LIST============================================================
	module = module.controller('${entityDefinition.entityName}Ctrl', function($olHttp, $scope, $location, $state, $stateParams) {
		$scope.model = {"entity":{}};
		$scope._showNext = true;
		$scope._showPrev = true;
		getItems('${entityDefinition.entityName}', null, true, 1, $scope, $olHttp, $state, $location);		
	});				
	</#list>
	</#if>
	
	module = module.controller('ConfirmationDialogCtrl', function ($scope, $modalInstance, func) {
		  $scope.ok = function () {
		    $modalInstance.close();
		    func();		    
		  };

		  $scope.cancel = function () {
		    $modalInstance.dismiss();
		  };
		});
	
	var getItems = function(entityName, propertyName, doREADAction, page, $scope, $olHttp, $state, $location) {
		if (propertyName == null) {
			propertyName = "";
		}
		var queryParamsString = "?";
			if ($location != null) {
			angular.forEach($location.search(), function(value, key) {
				if (key != "page") {
					queryParamsString += key + "=" + value + "&";
				}
			});
		}
		queryParamsString += "page=" + page;
		
		$olHttp.get(entityName + queryParamsString, function(data) {
			if (propertyName != "") {
				$scope.model.entity[propertyName] = data.model.entity;
				var showNextBtn = propertyName + "_showNext";
				var showPrevBtn = propertyName + "_showPrev";				
			} else {
				$scope.model.entity = data.model.entity;
				var showNextBtn = "_showNext";
				var showPrevBtn = "_showPrev";
			}
			
			var setPageNavigators = function() {				
				if (parseInt(data.model.pageCount) <= 1) {
					$scope[showNextBtn] = false;
		        	$scope[showPrevBtn] = false;
				} else if (page == parseInt(data.model.pageCount)) {
		        	$scope[showNextBtn] = false;
		        	$scope[showPrevBtn] = true;
		        } else if (page > parseInt(data.model.pageCount) || page <= 0) {
		        	page = 1;		
		        	setPageNavigators();
		        } else if (page == null || page == 1) {			        	
		        	$scope[showPrevBtn] = false;
		        	$scope[showNextBtn] = true;
		        } else {
		        	$scope[showPrevBtn] = true;
		        	$scope[showNextBtn] = true;
		        }
			}
			
			setPageNavigators();
	        
	        $scope[propertyName + '_next'] = function() {	        	
	        	if (page == 0 || page == null) {
	        		page = 1;
	        		getItems(entityName, propertyName, doREADAction, page, $scope, $olHttp, $state, $location);
	        	} else {
	        		page = page + 1;
	        		getItems(entityName, propertyName, doREADAction, page, $scope, $olHttp, $state, $location);
	        	}
	        };
	        
	        $scope[propertyName + '_prev'] = function() {
	        	page = page - 1;
        		getItems(entityName, propertyName, doREADAction, page, $scope, $olHttp, $state, $location);
	        };
	        
	        if (doREADAction == true) {
		        $scope.doREADAction = function(entityName, rowIndex) {		        	
		        	<#list entitiesDefinitions as entity>
		        		if (entityName == "${entity.entityName}") {		        			
		        			$state.go(entityName + "Details", {${entity.keys[0].name?replace(".", "_")}:<#list entity.keys as key>$scope.model.entity[rowIndex].${key.name}<#if key_has_next>+</#if></#list>});
		        		}
		        	</#list>		      
	        	}
	        }
		});
	}
	
	/* Controller code place-holder start
	<#if entityName??>
		<#list actions as action>
		<#switch action.actionName>
			<#case "READ">
		//=================================READ======================================
		module = module.controller('${entityName}DetailsCtrl', function($scope, $stateParams, $olHttp, $state, $modal) {		
			$scope.currentAction = "READ";
			
			$olHttp.get('${entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {			
				$scope.entityName = data.model.entityName;
				$scope.model = data.model;			
				$scope.doREADAction = function(targetEntityName, rowIndex, propertyName) {				
		        	
        		if (targetEntityName == "${entityName}") {
        			var targetData = $scope.model.entity[propertyName]
        			var keys = Object.keys( targetData );	        			    
			    	$state.go(targetEntityName + "Details", {${keys[0].name?replace(".", "_")}:<#list keys as key>targetData[keys[rowIndex]].${key.name}<#if key_has_next>+</#if></#list>});
        		}
		        	
	        	}
				$scope.doUPDATEAction = function() {				
					var modalInstance = $modal.open({
						templateUrl: 'views/partials/confirmation_dialog.html',
						controller: 'ConfirmationDialogCtrl',
						resolve: {
							func: function () {
								return function() {
									$olHttp.post('${entityName}?action=', $scope.model.entity, function(data) {					
										alert("Entity was successfully updated!");
									});
								} 
							}
						}
				    });				
				}
				
				$scope.doDELETEAction = function() {
					var modalInstance = $modal.open({
						templateUrl: 'views/partials/confirmation_dialog.html',
						controller: 'ConfirmationDialogCtrl',					
						resolve: {
							func: function () {
								return function() {
									$olHttp.remove('${entityName}/' + $stateParams[Object.keys($stateParams)[0]], function(data) {
										$state.go('${entityName}');
									});
								} 
							}
						}
				    });
				}
			});
		});
					<#break>
					<#case "CREATE">
		//==========================================CREATE========================================================			
		module = module.controller('${entityName}NewCtrl', function($scope, $modal, $olHttp, $state) {
			$scope.currentAction = "CREATE";
			$scope.model = {'entity':{}};
			$scope.nestedModels = {};
			
			
			<#if columnFieldsDefinitions??>
			<#list columnFieldsDefinitions?keys as key>								
				<#assign column = columnFieldsDefinitions[key]>
				<#if (!column.internal?? || column.internal == false) && column.oneToManyDefinition??>
					$scope.${column.name}_showNext = true;
					$scope.${column.name}_showPrev = true;				
					getItems('${column.javaTypeName}', '${column.name}', false, 1, $scope, $olHttp, $state, null);
					$scope.nestedModels['${column.name}'] = [];
				</#if>
			</#list>
			</#if>
			
			$scope.toggleSelection = function toggleSelection(item, itemArray, joinColumnName) {
				delete item[joinColumnName];
			    var idx = itemArray.indexOf(item);
	
			    if (idx > -1) {
			    	itemArray.splice(idx, 1);
			    } else {		    	
			    	itemArray.push(item);
			    }
			  };
			
			$scope.doUPDATEAction = function() {				
				var modalInstance = $modal.open({
					templateUrl: 'views/partials/confirmation_dialog.html',
					controller: 'ConfirmationDialogCtrl',
					resolve: {
						func: function () {
							return function() {
								var entity = $.extend($scope.model.entity, $scope.nestedModels);							
								$olHttp.post('${entityName}?action=', entity, function(data) {
									alert("Entity was created successfully!");
									$state.go('${entityName}');
								});												
							} 
						}
					}
			    });				
			}
		});
				</#switch>
			</#list>
		//=============================================LIST============================================================
		module = module.controller('${entityName}Ctrl', function($olHttp, $scope, $location, $state, $stateParams) {
			$scope.model = {"entity":{}};
			$scope._showNext = true;
			$scope._showPrev = true;
			getItems('${entityName}', null, true, 1, $scope, $olHttp, $state, $location);		
		});
	</#if>				
	Controller code place-holder end */
})();

( function() {

	'use strict';

	/* Directives */
	angular.module( 'directives', [] )


	.directive('bdatepicker', function() {		
	    return {
	        restrict: 'A',
	        require : 'ngModel',
	        link : function (scope, element, attrs, ngModelCtrl) {
	            $(function(){	            	
	            	element.datepicker({                    
	                }).on("changeDate", function(ev) {
	            	    ngModelCtrl.$setViewValue(ev.date);            	    
	                });	            	
	            	ngModelCtrl.$formatters.unshift(function (modelValue) {                		
	            		if (modelValue != null && modelValue != "" ) {	            			
	            			var date = new Date(parseInt(modelValue));
	            			element.datepicker("setValue", date);	            			
	                		return ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getDate() + "/" + date.getFullYear();            			
	            		} else {
	            			return modelValue;
	            		}               		
	                });
	            	
	            	ngModelCtrl.$parsers.push(function (viewValue) {            		
	            		return Date.parse(viewValue)/1000;
	            	});
	            });
	        }
	    }
	});
})();
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
	            			//return ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getDate() + "/" + date.getFullYear();  
	            			return (addZero(date.getDate()) + "/" + addZero((date.getMonth() + 1)).slice(-2)) + "/" + date.getFullYear();        			
	            		} else {
	            			return modelValue;
	            		}               		
	                });
	            	
	            	ngModelCtrl.$parsers.push(function (viewValue) {            		
	            		return Date.parse(viewValue);
	            	});
	            });
	            
			    function addZero(val) {
		        	var newVal = 0;
		        	if (val < 10) {
		        		newVal = '0' + val;
		        	}
		        	else {
		        		newVal = val;
		        	}
		        	
		        	return newVal;
		        }
	        }
	    }
	})
	
	.directive('highcharts', function(){
	return{
		restrict:'C',
		link: function(scope, element, attrs){			
			$(element).highcharts(scope.chartOptions[attrs.highchartsOptions]);
		}
	}
})
})();
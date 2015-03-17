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
	            		return Date.parse(viewValue);
	            	});
	            });
	        }
	    }
	})
	.directive('mask', function () {		
	    return {
	    	restrict: 'A',	    	
	        require: 'ngModel',
	        
	        link: function (scope, elem, attrs, ctrl) {        	
	        	var mask = attrs.mask;
	        	var viewValue;
	        	ctrl.$parsers.push(function (value) {
	        		if (value == null) {
	        			return;
	        		}
	        		var returnValue;
	        		if (value.toString().length > attrs.mask.length) {
	        			returnValue = viewValue;
	        			ctrl.$setViewValue(viewValue);
	        			ctrl.$render();
	        		} else {
	        			returnValue = value;	        			
	        			
	        			mask = mask.replace(',', '.');
		        		var strReturnValue = returnValue.toString();	        		
		        		if (mask.match(/[\.]/) != null && strReturnValue.indexOf('.') == -1) {	        			
		        			var maskParts = mask.split('.');
		        			var firstMaskPartLength = maskParts[0].length;
		        			if (strReturnValue.length > firstMaskPartLength) {
		        				var strReturnValue = returnValue.toString();
		        				var strReturnValue = [strReturnValue.slice(0, firstMaskPartLength), '.', strReturnValue.slice(firstMaskPartLength)].join('');	        				
		        				returnValue = parseFloat(strReturnValue);
		        				viewValue = returnValue;
		        			}
		        		}
		        		
		        		viewValue = returnValue;
		        		ctrl.$setViewValue(returnValue);
		        		ctrl.$render();
	        		}	        		
	        		
	        		return returnValue;	        			        		
        		});	        		
	        		
        	}	        
	    };
	});
})();
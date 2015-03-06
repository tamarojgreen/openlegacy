( function() {

	'use strict'
	
	angular.module( 'directives', [] )
	.directive('mask', function () {		
	    return {
	    	restrict: 'A',	    	
	        require: 'ngModel',
	        
	        link: function (scope, elem, attrs, ctrl) {        	
	        	var mask = attrs.mask;
	        	var viewValue;
	        	ctrl.$parsers.push(function (value) {	        		
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
} )();
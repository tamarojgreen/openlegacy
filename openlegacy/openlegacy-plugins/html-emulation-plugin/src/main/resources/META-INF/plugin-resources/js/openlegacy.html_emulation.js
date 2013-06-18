// default timeouts. when page is loaded checking sequence after 1000, then 3000 and so..
var timeouts = [1000,3000,10000];

var currentTimeoutIndex = 0;

function getMainForm() {
	if (document.openlegacyForm != null){
		return document.openlegacyForm;
	};
	return document.forms[0];
}

function TerminalSession() {

	this.doAction = function(keyboardKey) {
		getMainForm().KeyboardKey.value = keyboardKey;
		getMainForm().submit();
	};
}

var terminalSession = new TerminalSession();

require(["dojo/ready"], function(ready){
	ready(emulationOnLoad);
});


function emulationOnLoad(){
	require(["dojo/dom", "dojo/on"], function(dom, on){
		if (getMainForm() == null){
			return;
		}
		if (getMainForm().TerminalCursor != null){
			setFocus();
			attachFieldsFocus(on);
		}
		if (getMainForm().Sequence != null){
			setTimeout(checkSequence,timeouts[currentTimeoutIndex]);
		}
		captureOnChange(on);
		setLabelDoubleClick();
	});
}
function checkSequence(){
	var xhrArgs = {
		handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" },
		url : "sequence",
		load : function(data) {
			
			if (Number(data) == 0){
				location.href = "logoff";
			}
			else if (Number(getMainForm().Sequence.value) != Number(data)){
				location.href = location.href;
			}
			else{
				// step to the next timeout
				if (currentTimeoutIndex+1 < timeouts.length){
					currentTimeoutIndex++;
				}
				setTimeout(checkSequence,timeouts[currentTimeoutIndex]);
			}
		},
		error : function(e) {
			alert("Server not available:"  + e);
		}
	}
	var deferred = dojo.xhrGet(xhrArgs);
}

function setFocus(){
	if (getMainForm() == null){
		return;
	}
    var elements = getMainForm().elements;
    if (getMainForm().TerminalCursor == null){
    	return;
    }
    var focusFieldName = getMainForm().TerminalCursor.value;
    
    var focusField = dojo.byId(focusFieldName);
    if (focusField != null){
    	if (focusField.focus != null) focusField.focus();
    }
}

var ol_currentLabelId = null;
function attachFieldsFocus(on){
	if (getMainForm() == null){
		return;
	}
    var elements = getMainForm().elements;

    for (var i=0;i<elements.length;i++){
    	var element = elements[i];
    	on(element, "focusin", function(e){
    		getMainForm().TerminalCursor.value = e.target.name; 
    	});
    }
    require(["dojo/query","dojo/dom-class"], function(query,domClass){
    	  query("#terminalSnapshot span").forEach(function(label){
    	    	on(label, "click", function(e){
    	    		if (e.target.id != null){
    	    			if (ol_currentLabelId != null){
    	    				domClass.remove(ol_currentLabelId, "label_selected");
    	    			}
    	    			ol_currentLabelId = e.target.id; 
	    				domClass.add(ol_currentLabelId, "label_selected");
        	    		getMainForm().TerminalCursor.value = e.target.id; 
    	    		}
    	    	});
    		  
    	  });
    	});    
}

function setTabIndexRtl(){
	
	var inputs = document.getElementsByTagName("input");
	
	var inputsRow = new Array();
	var currentTabIndex = 1;
	
	for (var i=0;i<inputs.length;i++){
		var input = inputs[i];
		if (input.type == "hidden"){
			continue;
		}
		if (inputsRow.length == 0){
			inputsRow[inputsRow.length] = input;
		}
		else {
			var lastInput = inputsRow[inputsRow.length-1]; 
			if (input.offsetTop == lastInput.offsetTop){
				inputsRow[inputsRow.length] = input;
			}
			else{
				for (var j=inputsRow.length-1;j>=0;j--){
					inputsRow[j].tabIndex = currentTabIndex;
					currentTabIndex++;
				}
				inputsRow = new Array();
				inputsRow[0] = input;
			}
		}
	}
	if (inputsRow.length > 0){
		for (var j=inputsRow.length-1;j>=0;j--){
			inputsRow[j].tabIndex = currentTabIndex;
			currentTabIndex++;
		}
	}
}

function setLabelDoubleClick(){
    require(["dojo/dom", "dojo/on","dojo/query","dojo/dom-class"], function(dom,on,query,domClass){
  	  query("#terminalSnapshot span").forEach(function(label){
  	    	on(label, "dblclick", function(e){
  	    		if (e.target.id != null){
  	    			if (ol_currentLabelId != null){
  	    				domClass.remove(ol_currentLabelId, "label_selected");
  	    			}
  	    			ol_currentLabelId = e.target.id; 
	    				domClass.add(ol_currentLabelId, "label_selected");
      	    		getMainForm().TerminalCursor.value = e.target.id;
					terminalSession.doAction("ENTER");
  	    		}
  	    	});
  		  
  	  });
  	});    

}

function captureOnChange(on){
	
	var inputs = document.getElementsByTagName("input");
	
	for (var i=0;i<inputs.length;i++){
		var input = inputs[i];
		if (!isValidInput(input)){
			continue;
		}
		on(input,"change",function(e){
			handleRightAdjust(e.target);			
		});
	}
}


function handleRightAdjust(inputElement){
	if (!isValidInput(inputElement)){
		return;
	}
	var val = inputElement.value;
	var spacesToAdd = inputElement.maxLength - val.length;
	var rightAdjust = inputElement.getAttribute("data-ra");
	
	var charToAdd = null;
	switch (rightAdjust)
	{
		case "ZERO_FILL":
			charToAdd = "0";
			break;
		case "BLANKS_FILL":
			charToAdd = " ";
			break;
	}
	if (charToAdd != null){
		for (var i=0;i<spacesToAdd;i++){
			inputElement.value = charToAdd + inputElement.value ;			
		}
	}
	
}




////////////////////// Helper functions

function isValidInput(inputElement) {
	if (inputElement == null){
		return false;
	}
	if (!isInputElement(inputElement)){
		return false;
	}
	if (inputElement.readonly != null || inputElement.style.visibility=="hidden" || inputElement.style.display == "none") {
		return false;
	}
	if (equalsIgnoreCase(inputElement.tagName,"select") || equalsIgnoreCase(inputElement.tagName,"textarea")){
		return true;
	}

	var result = false;
	switch (inputElement.type) {
		case "text":
		case "password":
		case "radio":
		case "checkbox":
			result = true;
			break;
	}
	return result;
}

function equalsIgnoreCase(txt1,txt2){

	if (txt1 == null || txt2 == null){
		return false;
	}

	if (txt1 == txt2){
		return true;
	}
	if (txt1.toUpperCase() == txt2.toUpperCase()){
		return true;
	}
	
	return false;
}

function isInputElement(element){
	if (equalsIgnoreCase(element.tagName,"input") || equalsIgnoreCase(element.tagName,"select") || equalsIgnoreCase(element.tagName,"textarea")){
		return true;
	}
	return false;
}

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
	});
}
function checkSequence(){
	var xhrArgs = {
		handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" },
		url : "sequence?r=" + Math.random(),
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
    	if (focusField.focus != null) {
    		setTimeout(function() { focusField.focus(); }, 10);
    	}
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
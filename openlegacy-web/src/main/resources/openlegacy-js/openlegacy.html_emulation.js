function getMainForm() {
	return document.openlegacyForm;
}

function TerminalSession() {

	this.doAction = function(keyboardKey) {
		getMainForm().KeyboardKey.value = keyboardKey;
		getMainForm().submit();
	};
}

var terminalSession = new TerminalSession();

require(["dojo/ready"], function(ready){
	ready(function(){
		require(["dojo/dom", "dojo/on"], function(dom, on){
		    var elements = getMainForm().elements;
		    var focusFieldName = getMainForm().TerminalCursor.value;
		    
		    var focusField = dom.byId(focusFieldName);
		    if (focusField != null){
		    	if (focusField.focus != null) focusField.focus();
		    }
		    for (var i=0;i<elements.length;i++){
		    	var element = elements[i];
		    	on(element, "onfocus", function(e){
		    		getMainForm().TerminalCursor.value = e.target.name; 
		    	});
		    }
		});
	});
});

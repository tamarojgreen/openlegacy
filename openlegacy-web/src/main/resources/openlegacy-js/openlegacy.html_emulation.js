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

dojo.addOnLoad(function(){
    var elements = getMainForm().elements;
    var focusFieldName = getMainForm().TerminalCursor.value;
    
    var focusField = dojo.byId(focusFieldName);
    if (focusField != null){
    	if (focusField.focus != null) focusField.focus();
    }
    for (var i=0;i<elements.length;i++){
    	var element = elements[i];
    	dojo.connect(element, "onfocus", function(e) {
    		getMainForm().TerminalCursor.value = e.target.name; 
    	});
    }
});
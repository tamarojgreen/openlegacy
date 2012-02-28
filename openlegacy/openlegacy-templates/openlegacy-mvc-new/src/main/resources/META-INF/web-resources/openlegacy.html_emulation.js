function TerminalSession() {

	this.doAction = function(keyboardKey) {
		document.HtmlEmulation.KeyboardKey.value = keyboardKey;
		document.HtmlEmulation.submit();
	};
}

var terminalSession = new TerminalSession();

dojo.addOnLoad(function(){
    var elements = document.HtmlEmulation.elements;
    for (var i=0;i<elements.length;i++){
    	var element = elements[i];
    	dojo.connect(element, "onfocus", function(e) {
    		document.HtmlEmulation.TerminalCursor.value = e.target.name; 
    	});
    }
});
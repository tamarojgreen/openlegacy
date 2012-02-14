function sendFunctionKey(keyCode,shiftKey){
	var action =  "F" + (keyCode - 111);
	if (shiftKey){
		action = "SHIFT-" + action;
	}
	terminalSession.doAction(action);
} 
dojo.connect(dojo.doc.body, "onkeydown", function(e) {

	var handled = true;
	switch (e.keyCode) {
		case dojo.keys.ENTER:
			terminalSession.doAction("ENTER");
			break;
		case dojo.keys.F1:
		case dojo.keys.F2:
		case dojo.keys.F3:
		case dojo.keys.F4:
		case dojo.keys.F5:
		case dojo.keys.F6:
		case dojo.keys.F7:
		case dojo.keys.F8:
		case dojo.keys.F9:
		case dojo.keys.F10:
		case dojo.keys.F11:
		case dojo.keys.F12:
			sendFunctionKey(e.keyCode,e.shiftKey);
			break;
		default:
			handled = false;

	}
	if (handled){
		dojo.stopEvent(e);
	}
});
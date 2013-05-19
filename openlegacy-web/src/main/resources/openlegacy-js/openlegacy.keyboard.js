function sendFunctionKey(keyCode,shiftKey){
	var action =  "F" + (keyCode - 111);
	if (shiftKey){
		action = "SHIFT-" + action;
	}
	terminalSession.doAction(action);
} 

require(["dojo/ready"], function(ready){
	ready(function(){
		require(["dojo/on", "dojo/_base/window", "dojo/keys"], function(on, win, keys){
			on(win.doc, "keydown", function(e){
				var handled = true;
				switch (e.keyCode) {
					case keys.ESCAPE:
						terminalSession.doAction("ESC");
						break;
					case keys.ENTER:
						terminalSession.doAction("ENTER");
						break;
					case keys.F1:
					case keys.F2:
					case keys.F3:
					case keys.F4:
					case keys.F5:
					case keys.F6:
					case keys.F7:
					case keys.F8:
					case keys.F9:
					case keys.F10:
					case keys.F11:
					case keys.F12:
						sendFunctionKey(e.keyCode,e.shiftKey);
						break;
					case keys.PAGE_UP:
						terminalSession.doAction("PAGEUP");
						break;
					case keys.PAGE_DOWN:
						terminalSession.doAction("PAGEDOWN");
						break;
					default:
						handled = false;
				}
				if (handled){
					dojo.stopEvent(e);
				}
				return handled;
			});
		});
	});
});
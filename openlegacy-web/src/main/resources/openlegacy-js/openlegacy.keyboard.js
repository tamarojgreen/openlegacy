function getFunctionKey(keyCode,shiftKey){
	var action =  "F" + (keyCode - 111);
	if (shiftKey){
		action = "SHIFT-" + action;
	}
	return action;
} 

require(["dojo/ready"], function(ready){
	ready(function(){
		mapEmulationKeyboard();
	});
});

function stopEvent(e){
	dojo.stopEvent(e);
	e.keyCode = 0;
	if (e.preventDefault){
		e.preventDefault();
	}
	else{
		e.returnValue = false;
	}
} 
function mapEmulationKeyboard(){
	require(["dojo/on", "dojo/_base/window", "dojo/keys"], function(on, win, keys){
		on(win.doc, "keydown", function(e){
			var action = handleKeyboardMappings(e);
			if (action != null){
				stopEvent(e);
				terminalSession.doAction(action);
			}
		});
		on(win.doc, "help", function(e){
			var e1 = {keyCode:112, shiftKey : false};
			if (handleKeyboardMappings(e1) != null){
				stopEvent(e);
			}
		});
	});
}

function handleKeyboardMappings(e){
	var keys = require("dojo/keys");
	
	if (window.keyboardMappings != null){
		for (var i=0;i<keyboardMappings.mappings.length;i++){
			var mapping = keyboardMappings.mappings[i];
			if (e.keyCode == eval("keys." + mapping.KeyboardKey)){
				var keyPrefix = "";
				if (e.shiftKey || e.ctrlKey || e.altKey){
					if (e.shiftKey && mapping.additionalKey == "SHIFT"){
						keyPrefix = "SHIFT-";
					}
					else if (e.ctrlKey && mapping.additionalKey == "CTRL"){
						keyPrefix = "CTRL-";
					}
					else if (e.altKey && mapping.additionalKey == "ALT"){
						keyPrefix = "ALT-";
					}
					else{
						continue;
					}
				}
				
				return (keyPrefix + mapping.KeyboardKey);
			}
		}
	}
	else{
		return defaultEmulationKeyboard(e);
	}
	return null;
}
function defaultEmulationKeyboard(){
	var handled = true;
	switch (e.keyCode) {
		case keys.ESCAPE:
			return "ESCAPE";
		case keys.ENTER:
			return "ENTER";
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
			return getFunctionKey(e.keyCode,e.shiftKey);
		case keys.PAGE_UP:
			return "PAGE_UP";
		case keys.PAGE_DOWN:
			return "PAGE_DOWN";
		case 107:  // Numpad +
			fieldPlus(e.target);
			stopEvent(e);
			break;
		default:
			handled = false;
	}
	return null;

}

function fieldPlus(element){

	var inputs = document.getElementsByTagName("input");
	for (var i=0;i<inputs.length;i++){
		if (inputs[i].tabIndex != null && element.tabIndex != null){
			if (inputs[i].tabIndex == (element.tabIndex+1)){
				inputs[i].focus();
				break;
			}
		}
	}
}

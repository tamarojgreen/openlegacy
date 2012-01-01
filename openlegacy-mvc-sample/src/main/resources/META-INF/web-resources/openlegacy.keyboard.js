dojo.require("dojo.collections.Dictionary");

function TerminalKeyboardMapping(additionalKey,keyCode,functionElement){
	this.additionalKey = additionalKey;
	this.keyCode = keyCode;
	this.functionElement = functionElement;
}

function TerminalKeyboardMappings(){
	this.mappings = new dojo.collections.Dictionary();
	this.add = 
		function (keyElement){
			this.mappings.put(keyElement.additionalKey + "+" + keyElement.keyCode,keyElement);
		};
	this.get = 
		function (additionalKey,keyCode){
			return this.mappings.get(additionalKey + "+" + keyCode);
		};
}

var AdditionalKeys = new function(){
	this.NONE  = 0;
	this.CTRL  = 1;
	this.ALT   = 2;
	this.SHIFT = 3;
}


var KeyCodes = new function(){
	this.BACKSPACE  = 8;	
	this.TAB 		= 9;
	this.ENTER 		= 13;	
	this.SHIFT 		= 16;	
	this.CTRL 		= 17;
	this.ALT 		= 18;	
	this.CAPSLOCK 	= 20;	
	this.ESC		= 27;	
	this.PAGEUP		= 33;	
	this.PAGEDOWN	= 34;	
	this.END 		= 35;	
	this.HOME 		= 36;	
	this.LEFT 		= 37;
	this.UP 		= 38;
	this.RIGHT 		= 39;
	this.DOWN 		= 40;
	this.PLUS		= 107;	
	this.INSERT		= 45;	
	this.DELETE		= 46;	

	this.F1  = 112;
	this.F2  = 113;
	this.F3  = 114;
	this.F4  = 115;
	this.F5  = 116;
	this.F6  = 117;
	this.F7  = 118;
	this.F8  = 119;
	this.F9  = 120;
	this.F10 = 121;
	this.F11 = 122;
	this.F12 = 123;

	this.isAdditionalKey = function(keyCode){
		if (keyCode == GXKeyCodes.SHIFT || keyCode == GXKeyCodes.CTRL || keyCode == GXKeyCodes.ALT){
			return true;
		}
		return false;		
	}
}

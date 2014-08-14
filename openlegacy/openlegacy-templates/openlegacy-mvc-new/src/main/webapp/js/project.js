/* Uncomment for Ajax emulation
function doAction(keyboardKey){
	getMainForm().KeyboardKey.value = keyboardKey;
	doAjaxPost(getMainForm().name,"container","","body",emulationOnLoad);
}

if (window.terminalSession){
	terminalSession.doAction = doAction; 
}
*/
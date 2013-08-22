// WORK IN PROGRESS
//the field id which the lookup dialog result should return 
function openMobileLookupDialog(fieldId,displayFieldId,url,style){
	ol_lookupFieldId = fieldId;
	ol_lookupDisplayFieldId = displayFieldId;
	require(["dojo/ready", "dijit/registry", "dojo/dom"], function(ready, registry,dom){
	    ready(function(){
	    	var lookDialog = registry.byId("lookupDialog");
	    	alert("work in progress");
	    	lookDialog.set("href",url);
	    	lookDialog.show();
	    });
	});	
}


/* Uncomment for Ajax emulation
function doAction(keyboardKey){
	getMainForm().KeyboardKey.value = keyboardKey;
	doAjaxPost(getMainForm().name,"container","","body",emulationOnLoad);
}

if (window.terminalSession){
	terminalSession.doAction = doAction; 
}
*/
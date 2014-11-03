/* Uncomment for Ajax emulation
function doAction(keyboardKey){
	getMainForm().KeyboardKey.value = keyboardKey;
	doAjaxPost(getMainForm().name,"container","","body",emulationOnLoad);
}

if (window.terminalSession){
	terminalSession.doAction = doAction; 
}
*/


function setLabelDoubleClick(){
    require(["dojo/dom", "dojo/on","dojo/query","dojo/dom-class"], function(dom,on,query,domClass){
    	
    	require(["dojo/ready", "dijit/registry", "dojo/dom"], function(ready, registry,dom){
    		on(dojo.body(),"dblclick",function(e){
    			// avoid duplicate enter
				terminalSession.doAction("ENTER");
    		});
    	});
		query("#terminalSnapshot span").forEach(function(label){
  	    	on(label, "dblclick", function(e){
  	    		if (e.target.id != null){
  	    			if (ol_currentLabelId != null){
  	    				domClass.remove(ol_currentLabelId, "label_selected");
  	    			}
  	    			ol_currentLabelId = e.target.id; 
    				domClass.add(ol_currentLabelId, "label_selected");
      	    		getMainForm().TerminalCursor.value = e.target.id;

      	    		var offset = Math.round((e.clientX-e.srcElement.parentElement.offsetLeft)/10);
      	    		
					var text = e.srcElement.innerHTML;
      	    		if (text.match(/F\d=.*/)){
      	    			var texts = e.srcElement.innerHTML.split("  ");
      	    			var totalText = "";
      	    			for (var i = 0;i < texts.length;i++){
      	    				if (totalText.length <= offset){
      	    					totalText = totalText + "  " + texts[i];
      	    				}
      	    				else{
      	    					break;
      	    				}
      	    			}
      	    			var partText = texts[i-1];
      	    			var fText = partText.split("=");
      	    			
  	    				var action = fText[0]; 
      	    			if (fText[0].indexOf("F") == 0){
      	    				var fNumber = parseInt(fText[0].substr(1));
      	    				if (fNumber > 12){
      	    					action = "SHIFT-F" + (fNumber - 12);
      	    				}
      	    			}
      	    			terminalSession.doAction(action);
      	    		}
      	    		else{
          	    		terminalSession.doAction("ENTER");
      	    		}
  	    		}
  	    	});

  			query("#terminalSnapshot input").forEach(function(input){
  	  	    	on(input, "dblclick", function(e){
  	  	    		if (e.target.id != null){
						OLBrowserUtil.cancelEvent(e);
  	  	    		}
  	  	    	});
  	  	  	});
  	  });
  	});    
}






require(["dojo/ready"], function(ready){
	ready(projectOnLoad);
});

function projectOnLoad(){

	if (window.terminalSession){
		terminalSession.doAction = doAction; 
	}
	
	setTabIndexLtr();

	require(["dojo/on", "dojo/_base/window"], function(on, win){
		on(win.doc, "help", function(e){
			dojo.stopEvent(e);
			return true;
		});
	});
	mapProjectKeyboard();
}

function doAction(keyboardKey){
	if (getMainForm().KeyboardKey.value == ""){
		getMainForm().KeyboardKey.value = keyboardKey;
		doAjaxPost(getMainForm().name,"container","","body",doAfterAjaxPost);
	}
}

function showSessionViewer(baseUrl) {
	showDialog("sessionViewerDialog", baseUrl + "/sessionViewer?ts=" + (new Date()));
}

function doAfterAjaxPost(){
	emulationOnLoad();
	projectOnLoad();
}


function mapProjectKeyboard(){
	require(["dojo/on", "dojo/_base/window", "dojo/keys"], function(on, win, keys){
		on(win.doc, "keydown", function(e){
			var handled = true;
			switch (e.keyCode) {
				case 107:
					fieldPlus(e.target);
					break;
				default:
					handled = false;
			}
			if (handled){
				dojo.stopEvent(e);
			}
			return !handled;
		});
	});
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


function setTabIndexLtr(){
	
	var inputs = document.getElementsByTagName("input");
	
	for (var i=0;i<inputs.length;i++){
		var input = inputs[i];
		if (input.type == "hidden"){
			continue;
		}
		inputs[i].tabIndex = i;
	}
}

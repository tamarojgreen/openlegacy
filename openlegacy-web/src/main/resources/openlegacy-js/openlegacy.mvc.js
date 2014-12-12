function getForm(formName) {
	var form = null;
	if (formName != null) {
		form = document.getElementById(formName);
		if (form != null)
			return form;
	}

	if (form == null) {
		if (document.forms.length == 0) {
			throw "Unable to find form";
		}
		if (document.forms.length > 1) {
			throw "Found more then 1 form on page. Unable to detrmine main form";
		}
		return document.forms[0];
	}
}

function doPost(formName, actionName) {
	showLoading();
	var form = getForm(formName);
	form.action = form.action.split("?")[0];
	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "?action=" + actionName;
	}
	if (QueryString.target != null){
		var seperator = "?"
		if (form.action.indexOf("?") > 0){
			seperator = "&"
		}
		form.action = form.action + seperator + "target=" + QueryString.target;
	}
	
	form.submit();
}


var QueryString = function () {
	  // This function is anonymous, is executed immediately and 
	  // the return value is assigned to QueryString!
	  var query_string = {};
	  var query = window.location.search.substring(1);
	  var vars = query.split("&");
	  for (var i=0;i<vars.length;i++) {
	    var pair = vars[i].split("=");
	    	// If first entry with this name
	    if (typeof query_string[pair[0]] === "undefined") {
	      query_string[pair[0]] = pair[1];
	    	// If second entry with this name
	    } else if (typeof query_string[pair[0]] === "string") {
	      var arr = [ query_string[pair[0]], pair[1] ];
	      query_string[pair[0]] = arr;
	    	// If third or later entry with this name
	    } else {
	      query_string[pair[0]].push(pair[1]);
	    }
	  } 
	    return query_string;
	} ();
	
/**
 * Submit the given <entityName>Form in ajax post, and loads the result into the
 * given entity name panel
 * 
 * @param entityName
 * @param actionName
 */
function doAjaxPost(formName, areaName, actionName,fragments,callback) {
	showLoading();
	var dom = require("dojo/dom");
	var dijit = require("dijit/registry");
	var domForm = require("dojo/dom-form");
	
	var form = dom.byId(formName);
	var container = null;
	var title = null;
	if (areaName != null) {
		container = dijit.byId(areaName);
		if (container != null){
			title = container.get('title');
		}
	}
	form.action = form.action.split("?")[0] + "?partial=1";
	if (fragments != null){
		form.action = form.action + "&fragments=" + fragments;
	}

	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "&action=" + actionName;
	}

	var xhr = require("dojo/request/xhr");
	var promise = xhr.post(form.action, {
		data: domForm.toObject(formName),
		//handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" }
	});
	promise.then(function(data){
		if (container != null) {
			if (title != null){
				container.set('title', title);
			}
			if (data.length > 0){
				container.set('content', data);
			}
			if (callback != null){
				callback.call();
			}
		}
	}, function(e){
		alert(e);
	});
	promise.response.then(function(response) {
		var windowUrl = response.getHeader("Spring-Modal-View");
		var redirectUrl = response.getHeader("Spring-Redirect-URL");
		if (windowUrl){
			showDialog("lookupDialog", redirectUrl);
		}
		else{
			if (redirectUrl){
				location.href = redirectUrl;
			}			
		}
    });	
	
	if (container != null) {
		container.set('title', 'Updating...');
	}
}

function showSessionViewer(baseUrl) {
	showDialog("sessionViewerDialog", baseUrl + "/sessionViewer?ts=" + (new Date().getTime()));
}

function showEntityViewer(baseUrl,entityName) {
	showDialog("sessionViewerDialog", baseUrl + "/entityViewer/" + entityName + "?ts=" + (new Date().getTime()));
}

function showMobileSessionViewer(baseUrl){
	var registry = require("dijit/registry"); 
	var dialog = registry.byId("sessionViewer");
	dialog.set("href",baseUrl + "/sessionViewer?small=" + Math.random());
	dialog.show();

}

function showDialog(dialogTagId,url) {
	var dijit = require("dijit/registry");
	var dialog = dijit.byId(dialogTagId);
	if(dialog == null){
		alert(dialogTagId +  " tag not found");
		return;
	}
	if (url != null){
		dialog.set("href",url);
	}
	dialog.show();
}

/**
 * 
 * @param tagId The containing widget tag id
 * @param jsonUrl The URL to fetch data from
 * @param keyField The key field within the json data list
 * @param displayField The display field within the json data list 
 * @param baseUrl The base URL for each list item. The key field value will be appended to it.
 */
function loadMore(tagId,jsonUrl,keyFieldsJoined,displayFieldJoined,baseUrl){

	showLoading();
	
	var dijit = require("dijit/registry");
	var container = dijit.byId(tagId);
	
	var array = require("dojo/_base/array");
	var ListItem = require("dojox/mobile/ListItem");
	
	var xhr = require("dojo/request/xhr");
	
	var displayFields = displayFieldJoined.split(",");
	
	var keyFields = keyFieldsJoined.split(",");
	xhr.get(jsonUrl, {
		handleAs : "json"
	}).then(function(records){
		array.forEach(records, function(entry, i){

			var key = "";
			for (var i = 0;i<keyFields.length;i++){
				key += eval("entry." + keyFields[i]);
				if (i<keyFields.length-1){
					key += "+";
				}
			}
			
			var text = "";
			for (var i = 0;i<displayFields.length;i++){
				text += eval("entry." + displayFields[i]);
				if (i<displayFields.length-1){
					text += " - ";
				}
			}
			
			var newWidget = new ListItem({transition:"slide", url: baseUrl + key, label: text, onclick:"showLoading();"});
			newWidget.placeAt(container.containerNode);
			newWidget.startup();
		});
		hideLoading();
	}, function(e){
		alert(e);
	});
}

function asyncLoadMobilePanel(panelTagId,url,delay){
	delay = delay != null ? delay : 0; 
	setTimeout(function() {loadMobilePanel(panelTagId,url);},delay);
}

function loadMobilePanel(panelTagId,url){

	var dom = require("dojo/dom");
	var parser = require("dojox/mobile/parser");
	
	var container = dom.byId(panelTagId);

	// do not load panels twice
	if (container.innerHTML != "Loading..."){
	    return;
	}
	
	var parent = container.parentNode; 

	var xhr = require("dojo/request/xhr");
	xhr.get(url, {
		handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" }
	}).then(function(data){
		container.innerHTML = data;
		try{
			parser.parse(container);
		}
		catch(e){
			// ignore parsing errors
		}
	}, function(e){
		alert(e);
	});
}

/**
 * A separate AJAX post method for mobile then doAjaxPost mostly since uses *.innerHTML to update to DOM and run parse while doAjaxPost uses dijit.set("content",...)
 * @param formName The form name to submit in AJAX
 * @param areaName Tag id to update with the AJAX response (Partial Page Rendering)
 * @param actionName Host action alias name. If null, standard submit (ENTER) will be performed
 * @param fragments Server side part to render. Handled by AjaxTilesView in Spring Web MVC file
 */
function doMobilePost(formName, areaName, actionName,fragments) {
	showLoading();
	var dom = require("dojo/dom");
	var parser = require("dojox/mobile/parser");
	var domConstruct = require("dojo/dom-construct");
	
	var form = dom.byId(formName);
	var container = null;
	var title = null;
	if (areaName != null) {
		container = dom.byId(areaName);
	}
	form.action = form.action + "?partial=1";
	if (fragments != null){
		form.action = form.action + "&fragments=" + fragments;
	}

	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "&action=" + actionName;
	}

	var domForm = require("dojo/dom-form");
	
	var xhr = require("dojo/request/xhr");
	xhr.post(form.action, {
		handleAs : "text",
		data: domForm.toObject(formName),
		headers: { "Accept": "text/html;type=ajax" }
	}).then(function(data){
		
		var container = dom.byId("bodyContainer");
		container.innerHTML = data;
		try{
			parser.parse(container);
		}
		catch(e){
			// ignore parsing errors
		}
		hideLoading();
	}, function(e){
		alert(e);
	});
}


/** Back handling **/

//history state class
function HistoryState(state)
{
    this.state = state;
    this.restoreState = function() { 
    	restoreView(this.state); 
    };
    // back, forward and changeUrl are needed by dojo
    this.back = this.restoreState;
    this.forward = this.restoreState;
    this.changeUrl = true;
}


function restoreView(viewId)
{
	require(["dijit/registry"], function(registry){
		var view = registry.byId(viewId);
		view.show();
	});
}

function addViewToHistory(viewId){
	require(["dojo/back"], function(back){
		back.addToHistory(new HistoryState(viewId));
	});
}

/** End Back handling **/

/** Calendar functions to get/set value from input field to calendar widget **/

function getDateFromCalendar(node, v){
	if(v === true){ // Done clicked
		var registry = require("dijit/registry");
		var w = registry.byId("calendarWidget");
		var date;
		try {
			date = w.get("value");
		} catch (e) {
			return;
		}
		var locale = require("dojo/date/locale");
		var formattedDate = locale.format(date, {datePattern:"yyyy-MM-dd", selector: 'date'});
		node.value = formattedDate;
		
	}
}
function setDateToCelendar(node){
	var v = node.value.split(/-/);
	if(v.length == 3){
		var registry = require("dijit/registry");
		var w = registry.byId("calendarWidget");
		w.setValue(v);
	}
}

function showLoading(){
	var dom = require("dojo/dom");
	var loading = dom.byId('loadingMessage');
	if (loading == null) return;
	
	dom.byId('loadingMessage').style.display='';
	hideLoading();
}

function hideLoading(){
	var dom = require("dojo/dom");
	window.setTimeout(function() {dom.byId('loadingMessage').style.display='none';},300);
	
}

function createMap(address){
	var lat = '';
    var lng = '';
    var title = address;
    var geocoder = new google.maps.Geocoder();
    geocoder.geocode( { 'address': address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
         lat = results[0].geometry.location.lat();
         lng = results[0].geometry.location.lng();
      }
	  else {
		  lat = 1;
		  lng = 1;
		  title = "Unable to present location";
      }

	   	var myLatlng = new google.maps.LatLng(lat, lng);
	    var myOptions = {
	        zoom: 13,
	        center: myLatlng,
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    }
	    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	
	    var marker = new google.maps.Marker({
	        position: myLatlng, 
	        map: map,
	        title:title
	    });   
      
    });
	
}

function createFormattedMap(address){
	address = address.replace('\r\n',',');
	address = address.replace('\n',',');
	address = address.replace('\r',',');
	address = address.toLowerCase();
	createMap(address);
	
}

function changeTheme(themes){
	var cookie = require("dojo/cookie");
	var currentTheme = cookie("ol_theme");
	for (var i=0;i<themes.length;i++){
		if (themes[i] == currentTheme){
			var newTheme = i < (themes.length-1) ? themes[i+1] : themes[0];
			var url = location.href;
			if (url.indexOf("?") > 0){
				url = location.href.substring(0,url.indexOf("?"));
			}
			location.href = url+ "?ol_theme=" + newTheme; 
		}
	}
	
}

function reloadApplicationContext(baseUrl){
	var xhr = require("dojo/request/xhr");
	xhr.get(baseUrl + "/reload").then(function(data){
		location.href = location.href;
	}, function(e){
		alert(e);
	});
}

function resizeDojoWidget(id) {
	require([ "dijit/registry", "dojo/ready" ], function(registry, ready) {
		ready(function(){
			var widget = registry.byId(id);
			if (widget) {
				setTimeout(function(){
					widget.resize();
				}, 1000);
			}
		});
	});
}


//the field id which the lookup dialog result should return 
var ol_lookupFieldId;
var ol_lookupDisplayFieldId;
var ol_autoSubmit;
function openLookupDialog(fieldId,displayFieldId,url,style,autoSubmit){
	require(["dojo/ready", "dijit/registry", "dijit/Dialog","dojo/dom"], function(ready, registry,Dialog,dom){
	    ready(function(){
	    	var lookDialog = registry.byId("lookupDialog");
	    	
	    	// don't override lookup if window is opened 
	    	if (!lookDialog.open){
		    	ol_lookupFieldId = fieldId;
		    	ol_lookupDisplayFieldId = displayFieldId;
		    	ol_autoSubmit = autoSubmit;
	    	}
	    	
	    	lookDialog.set("style",style);
	    	lookDialog.set("href",url + "?ts=" + (new Date()));
	    	
	    	lookDialog.show();
	    });
	});	
}
function closeAndUpdateLookup(value,displayValue){
	require([ "dojo/dom" , "dijit/registry","dojo/dom-form"], function(dom,registry,domForm) {
		
  	var lookupDialog = registry.byId("lookupDialog");
		var resultField= dom.byId(ol_lookupFieldId);
		resultField.value = value;
		ol_lookupFieldId = null;
		if (ol_lookupDisplayFieldId != null){
			var resultDisplayField = dom.byId(ol_lookupDisplayFieldId);
			if (resultDisplayField != null){
				resultDisplayField.innerHTML = displayValue;
			}
			ol_lookupDisplayFieldId = null;
		}
		lookupDialog.hide();
		lookupDialog.set("content","");
		
		var xhr = require("dojo/request/xhr");
		xhr.post(resultField.form.action + "?action=none", {
			data: domForm.toObject(resultField.form),
			handleAs : "text",
			headers: { "Accept": "text/html;type=ajax" }
		}).then(function(data){
			if (ol_autoSubmit != 'null'){
				ol_autoSubmit = ol_autoSubmit == "enter" ? "" : ol_autoSubmit;
				doPost(getMainForm().name,ol_autoSubmit);
			}
		}, function(e){
			alert(e);
		});
	});
}

function setLanguage(language){
	var url = location.href;
	 var urlParts = url.split('?');
     if (urlParts.length >= 2) {
         url = urlParts[0];
     }
     location.href=url + "?lang=" + language
}

require(["dojo/ready"], function(ready){
	ready(function(){
		bindKeyboard();
		runOnloads();
	});
});

function bindKeyboard() {
	require(
			[ "dojo/on", "dojo/_base/window", "dojo/keys", "dojo/dom-form",
					"dijit/registry", "dojo/query" ],
			function(on, win, keys, dom, registry, query) {
				on(
						win.doc,
						"keydown",
						function(e) {
							var loadingMessage = document.getElementById("loadingMessage");
							if (loadingMessage != null && loadingMessage.style.display != "none") {
								return;
							}
							var handled = true;
							var tabs = registry.byId("tabbed_area");
							switch (e.keyCode) {
							case keys.ENTER:
								var lookupDialog = registry
										.byId("lookupDialog");
								if (lookupDialog.open) {
									if (!document.forms[2]) {
										doAjaxPost(document.forms[1].id,
												'window_container', '', 'body');
									} else {
										doAjaxPost(document.forms[2].id,
												'window_container', '', 'body');
									}
								} else {
									var enter = null;

									if (tabs == null) {
										enter = query("#submit");
									} else {
										var exp = "#"
												+ tabs.selectedChildWidget.id
												+ " #submit";
										console.log(exp);
										enter = query(exp);
									}
									console.log(enter.length);
									if (enter.length > 0) {
										enter[0].click();
									} else {
										document.activeElement.blur();
									}
								}
								break;
							case keys.PAGE_UP:
								var lookupDialog = registry
										.byId("lookupDialog");
								if (lookupDialog.open) {
									if (!document.forms[2]) {
										doAjaxPost(document.forms[1].id,
												'window_container', 'previous',
												'body');
									} else {
										doAjaxPost(document.forms[2].id,
												'window_container', 'previous',
												'body');
									}
								} else {
									var prev = null;
									if (tabs == null) {
										prev = query("#previous");
									} else {
										var exp = "#"
												+ tabs.selectedChildWidget.id
												+ " #previous";
										console.log(exp);
										prev = query(exp);
									}
									console.log(prev.length);
									if (prev.length > 0) {
										prev[0].click();
									}
								}
								break;
							case keys.PAGE_DOWN:
								var lookupDialog = registry
										.byId("lookupDialog");
								if (lookupDialog.open) {
									if (!document.forms[2]) {
										doAjaxPost(document.forms[1].id,
												'window_container', 'next',
												'body');
									} else {
										doAjaxPost(document.forms[2].id,
												'window_container', 'next',
												'body');
									}
								} else {
									var next = null;
									if (tabs == null) {
										next = query("#next");
									} else {
										var exp = "#"
												+ tabs.selectedChildWidget.id
												+ " #next";
										console.log(exp);
										next = query(exp);
									}
									console.log(next.length);
									if (next.length > 0) {
										next[0].click();
									}
								}
								break;
							case 8:
								if (e.target.tagName == "INPUT") {
									handled = false;
								}
								break;
							default:
								handled = false;
							}
							if (handled) {
								dojo.stopEvent(e);
							}
							return handled;
						});
			});

}

function runOnloads(){
	require(["dojo/query"], function(query){
		  var nl = query("[data-onload]").forEach(function(node){
			  eval(node.getAttribute("data-onload"));
		  });
		});	
}

function changeHidden(element,checkedValue,uncheckedValue){
	
	if (element.checked){
		document.getElementById(element.name.substr(2)).value = checkedValue;	
	}
	else{
		document.getElementById(element.name.substr(2)).value = uncheckedValue;	
	}
}

function doBackgroundUpdate(url){
	require([ "dojo/dom" , "dijit/registry","dojo/dom-form"], function(dom,registry,domForm) {
		var xhr = require("dojo/request/xhr");
		xhr.post(url + "?action=none", {
			data: domForm.toObject(getMainForm()),
			handleAs : "text",
			headers: { "Accept": "text/html;type=ajax" }
		}).then(function(data){
		}, function(e){
			alert(e);
		});
	});
}
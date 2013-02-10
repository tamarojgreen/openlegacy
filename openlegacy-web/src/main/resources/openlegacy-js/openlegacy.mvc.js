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
	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "?action=" + actionName;
	}
	
	form.submit();
}

/**
 * Submit the given <entityName>Form in ajax post, and loads the result into the
 * given entity name panel
 * 
 * @param entityName
 * @param actionName
 */
function doAjaxPost(formName, areaName, actionName,fragments) {
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
	form.action = form.action + "?partial=1";
	if (fragments != null){
		form.action = form.action + "&fragments=" + fragments;
	}

	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "&action=" + actionName;
	}

	var xhr = require("dojo/request/xhr");
	xhr.post(form.action, {
		data: domForm.toObject(formName),
		handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" }
	}).then(function(data){
		if (container != null) {
			if (title != null){
				container.set('title', title);
			}
			container.set('content', data);
		}
	}, function(e){
		alert(e);
	});
	if (container != null) {
		container.set('title', 'Updating...');
	}
}

function showSessionViewer(baseUrl) {
	showDialog("sessionViewerDialog", baseUrl + "/sessionViewer");
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
function loadMore(tagId,jsonUrl,keyField,displayField,baseUrl){

	showLoading();
	
	var dijit = require("dijit/registry");
	var container = dijit.byId(tagId);
	
	var array = require("dojo/_base/array");
	var ListItem = require("dojox/mobile/ListItem");
	
	var xhr = require("dojo/request/xhr");
	xhr.get(jsonUrl, {
		handleAs : "json"
	}).then(function(records){
		array.forEach(records, function(entry, i){
			var key = eval("entry." + keyField);
			var text = eval("entry." + displayField);
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
		var container = domConstruct.create("div")
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

function changeTheme(themesList){
	var cookie = require("dojo/cookie");
	var currentTheme = cookie("ol_theme");
	var themes = themesList.split(",");
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
function openLookupDialog(fieldId,displayFieldId,url,style){
	ol_lookupFieldId = fieldId;
	ol_lookupDisplayFieldId = displayFieldId;
	require(["dojo/ready", "dijit/registry", "dijit/Dialog","dojo/dom"], function(ready, registry,Dialog,dom){
	    ready(function(){
	    	var lookDialog = registry.byId("lookupDialog");
	    	lookDialog.set("style",style);
	    	lookDialog.set("href",url);
	    	
	    	lookDialog.show();
	    });
	});	
}
function closeAndUpdateLookup(value,displayValue){
	require([ "dojo/dom" , "dijit/registry"], function(dom,registry) {
		
  	var lookupDialog = registry.byId("lookupDialog");
		var resultField= dom.byId(ol_lookupFieldId);
		resultField.value = value;
		ol_lookupFieldId = null;
		if (ol_lookupDisplayFieldId != null){
			var resultDisplayField = dom.byId(ol_lookupDisplayFieldId);
			resultDisplayField.innerHTML = displayValue;
			ol_lookupDisplayFieldId = null;
		}
		lookupDialog.hide();	
	});
}


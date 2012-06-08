function getForm(formName) {
	var form = null;
	if (formName != null) {
		form = dojo.byId(formName);
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
		form.action = location.href + "?action=" + actionName;
	} else {
		form.action = location.href;
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
	var form = dojo.byId(formName);
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

	var xhrArgs = {
		form : form,
		handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" },
		url : form.action,
		load : function(data) {
			if (container != null) {
				if (title != null){
					container.set('title', title);
				}
				container.set('content', data);
			}
		},
		error : function(e) {
			alert(e);
		}
	}
	if (container != null) {
		container.set('title', 'Updating...');
	}
	var deferred = dojo.xhrPost(xhrArgs);
}

function showSessionViewer(baseUrl) {
	showDialog("sessionViewerDialog", baseUrl + "/HtmlEmulation/sessionViewer");
}

function showDialog(dialogTagId,url) {
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
	
	var container = dijit.byId(tagId);
	
	var xhrArgs = {
			handleAs : "json",
			url : jsonUrl,
			load : function(records) {
				dojo.forEach(records, function(entry, i){
					var key = eval("entry." + keyField);
					var text = eval("entry." + displayField);
					var newWidget = new dojox.mobile.ListItem({transition:"slide", url: baseUrl + key, label: text, onclick:"showLoading();"});
					newWidget.placeAt(container.containerNode);
					newWidget.startup();
				});
				hideLoading();
			},
			error : function(e) {
				alert(e);
			}
		}
	var deferred = dojo.xhrGet(xhrArgs);
	
}

function asyncLoadMobilePanel(panelTagId,url,delay){
	delay = delay != null ? delay : 0; 
	setTimeout(function() {loadMobilePanel(panelTagId,url);},delay);
}

function loadMobilePanel(panelTagId,url){

	var container = dojo.byId(panelTagId);
	var parent = container.parentNode; 
	var xhrArgs = {
			handleAs : "text",
			headers: { "Accept": "text/html;type=ajax" },
			url : url,
			load : function(data) {
				container.innerHTML = data;
				try{
					dojox.mobile.parser.parse(container);
				}
				catch(e){
					// ignore parsing errors
				}
			},
			error : function(e) {
				alert(e);
			}
		}
	var deferred = dojo.xhrGet(xhrArgs);

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
	var form = dojo.byId(formName);
	var container = null;
	var title = null;
	if (areaName != null) {
		container = dojo.byId(areaName);
	}
	form.action = form.action + "?partial=1";
	if (fragments != null){
		form.action = form.action + "&fragments=" + fragments;
	}

	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "&action=" + actionName;
	}

	var xhrArgs = {
		form : form,
		handleAs : "text",
		headers: { "Accept": "text/html;type=ajax" },
		url : form.action,
		load : function(data) {
			var container = dojo.create("div")
			container.innerHTML = data;
			try{
				dojox.mobile.parser.parse(container);
			}
			catch(e){
				// ignore parsing errors
			}
			hideLoading();
		},
		error : function(e) {
			alert(e);
		}
	}
	var deferred = dojo.xhrPost(xhrArgs);
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
	var view = dijit.byId(viewId);
	view.show();
}

function addViewToHistory(viewId){
	dojo.back.addToHistory(new HistoryState(viewId));
}

/** End Back handling **/

/** Calendar functions to get/set value from input field to calendar widget **/

function getDateFromCalendar(node, v){
	if(v === true){ // Done clicked
		var w = dijit.byId("calendarWidget");
		var date;
		try {
			date = w.get("value");
		} catch (e) {
			return;
		}
		var formattedDate = dojo.date.locale.format(date, {datePattern:"yyyy-MM-dd", selector: 'date'});
		node.value = formattedDate;
		
	}
}
function setDateToCelendar(node){
	var v = node.value.split(/-/);
	if(v.length == 3){
		var w = dijit.byId("calendarWidget");
		w.setValue(v);
	}
}

function showLoading(){
	dojo.byId('loadingMessage').style.display='';
	hideLoading();
}

function hideLoading(){
	window.setTimeout(function() {dojo.byId('loadingMessage').style.display='none';},300);
	
}
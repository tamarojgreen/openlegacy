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

function showHelp() {
	showDialog("helpDialog",location.href + "/help");
}

function showSessionViewer() {
	showDialog("sessionViewerDialog", "HtmlEmulation/sessionViewer");
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
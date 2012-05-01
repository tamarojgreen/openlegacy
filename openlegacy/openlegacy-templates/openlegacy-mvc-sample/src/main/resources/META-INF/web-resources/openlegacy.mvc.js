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
function doAjaxPost(formName, areaName, actionName) {
	var form = dojo.byId(formName);
	var container = null;
	if (areaName != null) {
		container = dijit.byId(areaName);
		var title = container.get('title');
	}

	form.action = form.action + "?partial=1";

	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "&action=" + actionName;
	}

	var xhrArgs = {
		form : form,
		handleAs : "text",
		url : form.action,
		load : function(data) {
			if (container != null) {
				container.set('title', title);
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
	helpDialog = dijit.byId('helpDialog');
	if(helpDialog == null){
		alert("'helpDialog' tag not found");
		return;
	}
	helpDialog.set("href",location.href + "/help");
	helpDialog.show();
}
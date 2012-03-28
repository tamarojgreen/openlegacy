function getMainForm() {
	return document.openlegacyForm;
}

function findParentTag(node, tagName) {
	while (node != null && node.tagName.toUpperCase() != tagName.toUpperCase()) {
		node = node.parentNode;
	}
	return node;
}

function doPost(formName,actionName){
	if (actionName != null && actionName.length > 0){
		getMainForm().action = location.href + "?action=" + actionName;
	}
	else{
		getMainForm().action = location.href;
	}
	
	getMainForm().submit();
}


/**
 * Submit the given <entityName>Form in ajax post, and loads the result into the given entity name panel
 * @param entityName
 * @param actionName
 */
function doAjaxPost(entityName, actionName) {
	var form = dojo.byId(entityName + "Form");
	var container = dijit.byId(entityName);
	var title = container.get('title');
	
	if (actionName != null && actionName.length > 0) {
		form.action = form.action + "?action=" + actionName;
	}

	var xhrArgs = {
			form : form,
			handleAs : "text",
			url:entityName,
			load : function(data) {
				container.set('title',title);
				container.set('content',data);
			},
		}
	container.set('title','Loading...');
	var deferred = dojo.xhrPost(xhrArgs);
}
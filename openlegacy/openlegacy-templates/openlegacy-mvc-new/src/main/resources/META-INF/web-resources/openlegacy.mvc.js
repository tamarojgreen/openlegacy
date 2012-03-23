function getMainForm(){
	return document.openlegacyForm;
}

function doPost(actionName){
	if (actionName != null && actionName.length > 0){
		getMainForm().action = location.href + "?action=" + actionName;
	}
	else{
		getMainForm().action = location.href;
	}
	
	getMainForm().submit();
}
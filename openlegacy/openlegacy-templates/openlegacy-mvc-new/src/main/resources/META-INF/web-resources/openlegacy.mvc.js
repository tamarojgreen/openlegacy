function doPost(actionName){
	if (actionName != null && actionName.length > 0){
		document.openlegacyForm.action = location.href + "?" + actionName;
	}
	else{
		document.openlegacyForm.action = location.href;
	}
	
	document.openlegacyForm.submit();
}
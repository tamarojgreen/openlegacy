dojo.require("dojo.io.iframe");

function uploadStockItemImage() {
	var form = dojo.byId("uploadForm");
	var container = dijit.byId("ItemImages");
	var itemNumber = dojo.byId("itemNumber").value;
	var td = dojo.io.iframe.send({
		url : form.action,
		content : {
			itemNumber : itemNumber
		},
		form : form,
		method : "post",
		preventCache : true,
		handleAs: "html",
		handle : function(data, ioArgs) {
			dijit.byId("ItemImages").refresh();
		},
		error : function(res, ioArgs) {
			alert(res);
		}
	});
}

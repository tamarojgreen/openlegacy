function uploadStockItemImage() {
	require([ "dojo/request/iframe", "dojo/dom", "dijit/registry" ], function(
			iframe, dom, registry) {
		var form = dom.byId("uploadForm");
		var itemNumber = dom.byId("itemNumber").value;
		iframe.post(form.action, {
			data : {
				itemNumber : itemNumber
			},
			form : form,
			preventCache : true,
			handleAs : "html"
		}).then(function(data) {
			registry.byId("ItemImages").refresh();
		}, function(res) {
			alert(res);
		});
	});
};

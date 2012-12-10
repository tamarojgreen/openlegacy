function disconnect(url) {
	var doDisconnect = confirm("Are you sure you want to disconnect session?");
	if (doDisconnect) {
		var container = dijit.byId("container");
		var xhr = require("dojo/request/xhr");
		xhr.get(url + "?fragments=body", {
			headers : {
				"Accept" : "text/html;type=ajax"
			}
		}).then(function(data) {
			container.set('content', data);
		}, function(e) {
			alert(e);
		});
	}
}

function refresh() {
	var container = dijit.byId("container");
	var xhr = require("dojo/request/xhr");
	xhr.get("management?fragments=body", {
		headers : {
			"Accept" : "text/html;type=ajax"
		}
	}).then(function(data) {
		container.set('content', data);
	}, function(e) {
		alert(e);
	});
}
window.setInterval(refresh, 3000);

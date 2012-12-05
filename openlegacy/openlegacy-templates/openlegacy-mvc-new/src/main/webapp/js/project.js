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
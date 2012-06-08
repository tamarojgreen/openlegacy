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

function createMap(address){
	var lat = '';
    var lng = '';
    var title = address;
    var geocoder = new google.maps.Geocoder();
    geocoder.geocode( { 'address': address}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
         lat = results[0].geometry.location.lat();
         lng = results[0].geometry.location.lng();
      }
	  else {
		  lat = 1;
		  lng = 1;
		  title = "Unable to present location";
      }

	   	var myLatlng = new google.maps.LatLng(lat, lng);
	    var myOptions = {
	        zoom: 13,
	        center: myLatlng,
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	    }
	    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	
	    var marker = new google.maps.Marker({
	        position: myLatlng, 
	        map: map,
	        title:title
	    });   
      
    });
	
}

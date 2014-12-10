<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" ng-app="olApp">
	<head>
	<link href="/openlegacy-mvc-sample/favicon.ico" rel="shortcut icon" />
	<title>OpenLegacy Toys</title>
	<meta name="viewport" content="width=device-width">
		<!-- favorites icon -->
		<link href="../favicon.ico" rel="shortcut icon" />
		
		<!-- bootstrap css -->	
		<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">	
	
		<link ng-href="themes/light/light.css" rel="stylesheet">
			
		<!-- Include project css -->
		<link href="css/project.css" rel="stylesheet">
		<style>
			[ng-cloak] {
			  display: none !important;
			}
		</style>
		
		<!-- jquery js -->
		<script src="lib/jquery/jquery-2.1.1.min.js"></script>	
		<script src="lib/jquery/jquery.cookie.js"></script>
		
		<!-- bootstrap js -->
		<script src="lib/bootstrap/js/bootstrap.min.js"></script>	
		
		<!-- angular js -->	
		<script src="lib/angular/js/angular.min.js"></script>
		<script src="lib/angular/js/angular-route.min.js"></script>
		<script src="lib/angular/js/angular-ui-router.min.js"></script>
		
		
		<!-- project js -->
		<script src="js/app/app.js_ng" type="text/javascript"></script>
		<script src="js/app/controllers.js_ng" type="text/javascript"></script>	
		<script src="js/app/config.js" type="text/javascript"></script>
		<script src="js/app/services.js" type="text/javascript"></script>	
	</head>
	<body class="main" ng-class="theme">
		<img src="img/preloader.gif" class="preloader">
		<div class="content-wrapper">
			<div ui-view="header" ng-cloak></div>	
			<div class="container-fluid">    
		    	<div class="row">		  			  			
					<div class="col-sm-3 col-md-2 no-gutters-sm">				
						<div ui-view="sideMenu" ng-cloak></div>
					</div>						
					<div class="col-sm-9 col-md-10">								
						<div ui-view="breadcrumbs" ng-cloak></div>
						<!-- content view -->				
						<div ui-view ng-cloak></div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
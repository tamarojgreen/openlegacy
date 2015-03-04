<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" data-ng-app="olApp" lang="en">
	<head>
		<title>OpenLegacy - Application Name</title>
		<meta name="viewport" content="width=device-width">
		<!-- favorites icon -->
		<link href="../favicon.ico" rel="shortcut icon" />
			
		
		<!-- Bootstrap core CSS -->
	<link href="bootstrap/css/bootstrap.min.css" media="all" type="text/css" rel="stylesheet">	
		
	    <!-- Bootstrap extention CSS -->
	    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">	
		<!-- <link href="bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">	 -->		
		
		<!-- custom style for this project -->
		<link ng-href="themes/light/light.css" rel="stylesheet">		
		<link type="text/css" rel="stylesheet" href="css/project.css" />	
			
		<script src="lib/jquery/jquery-2.1.0.min.js" type="text/javascript"></script>
		<script src="lib/jquery/jquery.cookie.js" type="text/javascript"></script>
			
	    <!-- Include the bootstrap -->    
	    <script src="lib/bootstrap/js/bootstrap.js"></script>	    
	 
		<!-- Include angular JS -->
		<script src="lib/angular/angular.min.js"></script>
		<script src="lib/angular/angular-route.min.js"></script>
		<script src="lib/angular/angular-ui-router.min.js"></script>	
		
		<script src="lib/bootstrap/js/ui-bootstrap-tpls-0.11.2.min.js"></script>	

		<script src="js/app/app.js_ng" type="text/javascript"></script>
		<script src="js/app/controllers.js_ng" type="text/javascript"></script>
		<script src="js/app/services.js" type="text/javascript"></script>	
		<script src="js/app/directives.js" type="text/javascript"></script>	
		<script src="js/app/config.js" type="text/javascript"></script>
		<!-- Include controller place-holder start
		<script src="js/app/${entityName}.js" type="text/javascript"></script>
		Include controller place-holder end -->
	</head>
	<body class="main" ng-class="theme">
		<img src="img/preloader.gif" class="preloader" ng-show="_showPreloader" style="z-index:99">
		<div class="content-wrapper" ng-show="_showContent">	
			<div ui-view="header" ng-cloak></div>	
			<div class="container-fluid">    
		    	<div class="row">		  			  			
					<div class="col-xs-12 col-sm-3 col-md-2 no-gutters-sm">				
						<div ui-view="sideMenu" ng-cloak></div>
					</div>						
					<div class="col-xs-12 col-sm-9 col-md-10">								
						<div ui-view="breadcrumbs" ng-cloak></div>
						<!-- content view -->				
						<div ui-view ng-cloak></div>
						<!-- /content view -->
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade ol-screenshot" tabindex="-1" role="dialog" aria-labelledby="screenshotModal" aria-hidden="true">
        	<div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <img id="sessionImage" class="img-thumbnail">
               </div>
        	</div>
        </div>
	</body>
</html>
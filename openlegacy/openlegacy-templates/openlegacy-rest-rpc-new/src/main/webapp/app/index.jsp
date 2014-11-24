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
	<link href="bootstrap/css/datepicker.css" media="all" type="text/css" rel="stylesheet">
	
    <!-- Bootstrap extention CSS -->
    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">	
	<!-- <link href="bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">	 -->
	
	<!-- custom style for this project -->
	<style>
		[ng-cloak] {
		  display: none !important;
		}
	</style>
	<link type="text/css" rel="stylesheet" href="css/project.css" />
	<link ng-href="themes/#projectThemeRoot#/#projectTheme#.css" rel="stylesheet">
	
	<script src="js/jquery-1.10.2.min.js"></script>
	<script src="lib/angular/angular.min.js"></script>
	<script src="lib/angular/angular-route.min.js"></script>	
	<script src="lib/angular/angular-ui-router.min.js"></script>
	<script src="bootstrap/js/bootstrap.js"></script>
	<script src="bootstrap/js/bootstrap-datepicker.js"></script>
	<script src="lib/jquery.cookie.js"></script>
	<script src="js/app/app.js_ng" type="text/javascript"></script>
	<script src="js/app/controllers.js_ng" type="text/javascript"></script>
	<script src="js/app/services.js" type="text/javascript"></script>
	<script src="js/app/config.js" type="text/javascript"></script>
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
					<!-- /content view -->
				</div>
				
			</div>
		</div>
	</div>
</body>
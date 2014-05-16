<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" data-ng-app="olApp" lang="en">

<head>
	<title>OpenLegacy - Application Name</title>
	<meta name="viewport" content="width=device-width">
	<!-- favorites icon -->
	<link href="../favicon.ico" rel="shortcut icon" />	
	
	<!-- Bootstrap core CSS -->
	<link href="bootstrap/css/bootstrap.min.css" media="all" type="text/css" rel="stylesheet">
	<!-- <link href="bootstrap/css/bootstrap-rtl.min.css" media="all" type="text/css" rel="stylesheet"> -->	
	
    <!-- Bootstrap extention CSS -->
    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">	
	<!-- <link href="bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">	 -->
	
	<!-- custom style for this project -->
	<link type="text/css" rel="stylesheet" href="css/project.css" />
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
            	<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">openLegacy</a>
            </div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right" ng-show="loggedInUser">
					<li ng-repeat="subMenu in menu.menuItems"><a href="#/{{subMenu.targetEntityName}}">{{subMenu.displayName}}</a></li>
					<li class="active"><a href="#">Home</a></li>                      
				</ul>
				<ul class="nav navbar-nav navbar-left">
					<li><a href="#/logoff">Logoff</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="container">
		<div ng-view="true"></div>
	</div>
	
</body>


	<script src="js/jquery-1.10.2.min.js"></script>
	<script src="js/angular-1.0.7/angular.min.js"></script>
	<script src="js/angular-1.0.7/angular-cookies.min.js"></script>
	<script src="bootstrap/js/bootstrap.js"></script>
	<script src="js/app/app.js_ng" type="text/javascript"></script>
	<script src="js/app/controllers.js_ng" type="text/javascript"></script>
	<script src="js/app/services.js" type="text/javascript"></script>
	<script src="js/app/config.js" type="text/javascript"></script>
</body>
</html>
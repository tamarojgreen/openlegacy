<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Login  
</h1>

<form method="post" action="signon">
	<input id="user" value="${signon.user}" /> 
	<input id="password" type="password" value="${signon.password}" />
	
	<span id="error">${signon.error}</span>
	
	<input type="submit"/> 
</form>
</body>
</html>

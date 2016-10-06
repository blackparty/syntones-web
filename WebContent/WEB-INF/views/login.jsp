<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Syntones - Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="Modern Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
	Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
	<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
	 <!-- Bootstrap Core CSS -->
	<link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel='stylesheet' type='text/css' />
	<!-- Custom CSS -->
	<link href="<%=request.getContextPath()%>/resources/css/style.css" rel='stylesheet' type='text/css' />
	<link href="<%=request.getContextPath()%>/resources/css/font-awesome.css" rel="stylesheet"> 
	<!-- jQuery -->
	<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
	<!----webfonts--->
	<link href='http://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700,900' rel='stylesheet' type='text/css'>
	<!---//webfonts--->  
	<!-- Bootstrap Core JavaScript -->
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
</head>
<body id="login">
  <div class="login-logo">
    <a href="index.html"><img src="<%=request.getContextPath()%>/resources/images/syntones-logo.png" alt=""/></a>
  </div>
  <h2 class="form-heading">login</h2>
  <div class="app-cam">
  	<label class="error-message">${err_message}</label>
	  <form action = "loginAdmin" method="POST">
		<input type="text" class="text" name="username" placeholder="Username">
		<input type="password" name="password" placeholder="********">
		<div class="submit"><input type="submit" onclick="myFunction()" value="Login"></div>
	</form>
  </div>
   <div class="copy_layout login">
      <p>Copyright &copy; 2016 Modern. All Rights Reserved | BY <a href="#" target="_blank">Black Party</a> </p>
   </div>
</body>
</html>
<%@page import="com.blackparty.syntones.model.Song"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.List"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="${mainJs}"></script>
	<meta name="viewport" content="width=device-width, initial-scale=1">
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
<body>
	<div class="graphs">
		<div class="col-md-12 graphs">
			<div class="xs">
			    <h3>Add Song</h3>
			  	   <div class="well1 white">
			  	    <div class="form-group">
			  	    	<form class="form-floating ng-pristine" action="readMp3" method="POST" enctype="multipart/form-data" action="upload">
							File to upload: <input type="file" name="file" /><br>
							<input class="btn-primary btn" type="submit" name="action" value="Read File" /> 
							<a href="index"><input class="navbar-right btn-warning btn" type="button"value="Cancel"/></a>
						</form>
			  	    </div>
			  	</div>
		  	</div>
		</div>
	</div>
<jsp:include page="footer.jsp"></jsp:include>
	
</body>
</html>
	
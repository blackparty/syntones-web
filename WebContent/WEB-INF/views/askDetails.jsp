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
<spring:url value="/resources/javascript/mainJS.js" var="mainJs" />
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
				    <h3>Validate Song Data</h3>
				    
				  	    <div class="bs-example4 tab-content">
							<c:if test="${not empty succ_message}">
				  	    		<div class="isa_success">
     								<i class="fa fa-check"></i>
     								${succ_message}
     							</div>
				  	    	</c:if>
				  	    	<c:if test="${not empty err_message}">
				  	    		<div class="isa_error">
     								<i class="fa fa-times-circle"></i>
     								${err_message}
     							</div>
				  	    	</c:if>
							<div class="tab-pane active bs-example4" id="horizontal-form">
								<form class="form-horizontal" action="checkDetails" method="post">
									<div class="form-group">
										<label class="col-sm-2 control-label label-input-sm">Song Title</label>
										<div class="col-sm-8">
											<input type="text" class="form-control1 input-sm" id="inputError1" placeholder="Song Title" value="${ songTitle }" name="songTitle">
										</div>
									</div>
									<div class="form-group">
										<label for="smallinput" class="col-sm-2 control-label label-input-sm">Artist Name</label>
										<div class="col-sm-8">
											<input type="text" class="form-control1 input-sm" id="inputError1" placeholder="Artist Name" value="${ artistName }" name="artistName">
										</div>
									</div>
									<div class="navbar-right form-group">
										<input class="btn-primary btn" type="submit" name="action" value="Validate" /> 
										<a href="index"><input class="navbar-right btn-warning btn" type="button"value="Cancel"/></a>
									</div>
								</form>
							</div>
						</div>
				  	</div>
			  	</div>
			</div>
<jsp:include page="footer.jsp"></jsp:include>
			
</body>
</html>
<!--<form action="checkDetails" method="post">
		<input type="text" name="songTitle" value="${ songTitle }" placeholder="song title"/>
		<input type="text" name="artistName" value="${ artistName }" placeholder="artist name"/>
		<input type="submit" value="validate"> 
	</form>
<form action="saveSong" method="post">
		<input type="text" name="songTitle" value="${ songTitle }" hidden="true" placeholder="song title"/>
		<input type="text" name="artistName" value="${ artistName }" hidden="true" placeholder="artist name"/>
		<input type="submit" value="save"> 
	</form>
	
  -->
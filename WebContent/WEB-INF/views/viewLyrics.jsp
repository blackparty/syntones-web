<%@page import="com.blackparty.syntones.model.Song"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="header.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<spring:url value="/resources/javascript/mainJS.js" var="mainJs" />
<script type="text/javascript" src="${mainJs}"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords"
	content="Modern Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
	Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- Bootstrap Core CSS -->
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel='stylesheet' type='text/css' />
<!-- Custom CSS -->
<link href="<%=request.getContextPath()%>/resources/css/style.css"
	rel='stylesheet' type='text/css' />
<link
	href="<%=request.getContextPath()%>/resources/css/font-awesome.css"
	rel="stylesheet">
<!-- jQuery -->
<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
<!----webfonts--->
<link
	href='http://fonts.googleapis.com/css?family=Roboto:400,100,300,500,700,900'
	rel='stylesheet' type='text/css'>
<!---//webfonts--->
<!-- Bootstrap Core JavaScript -->
<script
	src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
</head>
<body>
	<%
	Song song = (Song)request.getAttribute("song");
%>
	<div class="graphs">
		<div class="col-md-12 graphs">
		<a href="index" ><button class="navbar-right btn-warning btn">Back</button></a>
			<div class="xs">
				<h3>Song Info</h3>
				<div class="bs-example4 tab-content">
					<h4 class="stitle media-heading">${song.songTitle}</h4>
					<h4 class="stitle panel-title">${song.artist.artistName}</h4>
					<br><br>
					<div class="stitle">
					<%
						String output = "<p>"+ song.getLyrics().replaceAll("\\n", "<br>") +"</p>";
						out.print(output);
					%>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
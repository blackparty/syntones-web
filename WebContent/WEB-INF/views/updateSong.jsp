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
<meta name="keywords"
	content="Modern Responsive web template, Bootstrap Web Templates, Flat Web Templates, Andriod Compatible web template, 
	Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<script type="application/x-javascript">
	
	
	 addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } 


</script>
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
	Song song = (Song) request.getAttribute("song");
	long songId = song.getSongId();
	String songTitle = song.getSongTitle();
	String artistName = song.getArtist().getArtistName();
	String lyrics = song.getLyrics();
%>
	<div class="graphs">
		<div class="col-md-12 graphs">
			<div class="xs">
				<h3>Update Song Data</h3>

				<div class="bs-example4 tab-content">
					<div class="tab-pane active bs-example4" id="horizontal-form">
						<form class="form-horizontal" action="updateSong" method="post">
							<div class="form-group">
								<label class="col-sm-2 control-label label-input-sm">Song
									Title</label>
								<div class="col-sm-8">
									<input type="text" class="form-control1 input-sm"
										id="inputError1" placeholder="Song Title"
										value="${ songTitle }" name="songTitle" disabled>
								</div>
							</div>
							<div class="form-group">
								<label for="smallinput"
									class="col-sm-2 control-label label-input-sm">Artist
									Name</label>
								<div class="col-sm-8">
									<input type="text" class="form-control1 input-sm"
										id="inputError1" placeholder="Artist Name"
										value="${ artistName }" name="artistName" disabled>
								</div>
							</div>
							<div class="form-group">
								<label for="smallinput"
									class="col-sm-2 control-label label-input-sm">Lyrics</label>
								<div class="col-sm-8">
									<textarea class="form-control1 input-sm" name="lyrics"><c:out
											value="${lyrics}" /></textarea>
								</div>
							<input type="hidden" value="${songId}" name="songId"/>
							</div>
							<div class="navbar-right form-group">
								<input class="btn-primary btn" type="submit" name="action"
									value="Update Song" /> <a href="index"><input
									class="navbar-right btn-warning btn" type="button"
									value="Cancel" /></a>
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
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
		String offSet = request.getParameter("offSet");
		int disableLINK = 0;
		if (offSet != null) {
			disableLINK = Integer.parseInt(offSet);
		}
		List<Song> songList = (List) request.getAttribute("songList");
		int size = Integer
				.parseInt(session.getAttribute("size").toString());
		String flag = request.getParameter("searchflag");
		String search_err = request.getParameter("search_err");
	%>
	<div id="page-wrapper">
		<div class="col-md-12 graphs">
			<div class="xs bs-example4">
				<div class="app-cam navbar-right ">
					<div class="submit">
						<a href="addSongPage"><input type="submit"
							onclick="myFunction()" value="Add Song"></a>
					</div>
				</div>
				<h3>
					Song List
					<h3>
						<!-- <c:if test="${not empty succ_message}">
							<div class="success-message">
								<i class="fa fa-check"></i> ${succ_message}
							</div>
						</c:if>
						<c:if test="${not empty err_message}">
							<div class="isa_error">
								<i class="fa fa-times-circle"></i> ${err_message}
							</div>
						</c:if>
						 -->
						<form class="form-inline" action="search" method="post">
							<div class="form-group">
								<input type="text" class="text"
									name="input" placeholder="Search">

								<button type="submit" class="btn btn-default">Search</button>
							</div>
						</form>
						<div class="bs-example4" data-example-id="contextual-table">
							<table class="table">
								<thead>
									<tr>
										<th>Song ID</th>
										<th>Song Title</th>
										<th>Artist</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody>
								<c:if test="${not empty search_err }">
									<tr>
										<td>${search_err}<td>
									</tr>
								</c:if>
									<c:forEach items="${songList}" var="song">
										<tr>
											<th scope="row">${song.songId }</th>
											<td>${song.songTitle }</td>
											<td>${song.artist.artistName}</td>
											<td><a class="btn-link"
												href="${pageContext.request.contextPath}/admin/viewLyrics?songId=${song.songId }">View
													Lyrics</a> <!--  	<form action="viewLyrics">
													<input type="hidden" name="songId" value="${song.songId }"> page-link
													<input class="btn-primary btn 	" type="submit"
														value="View Lyrics">
												</form>
											--></td>
											<!--<td><a class="btn-link"
												href="${pageContext.request.contextPath}/admin/edit?songId=${song.songId }">Update
													Song</a></td>  -->
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<nav aria-label="...">
						<ul class="pagination pagination-sm navbar-right">
							<!--   <li class="page-item disabled"> -->
							<!--  <li class="page-item active">
      <a class="page-link" href="#">1 <span class="sr-only">(current)</span></a>
    </li>
   -->
							<c:choose>
								<c:when test="${not empty flag}">
								p
								</c:when>
								<c:otherwise>
									<c:forEach var="i" begin="0" end="${size-1}">
										<c:choose>
											<c:when test="${i eq disableLINK}">
												<li class="active page-item"><a class="page-link"
													href="${pageContext.request.contextPath}/admin/index?offSet=${i}">${i+1}</a>
												</li>
											</c:when>
											<c:otherwise>
												<li class="page-item"><a class="page-link"
													href="${pageContext.request.contextPath}/admin/index?offSet=${i}">${i+1}</a>
												</li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</ul>
						</nav>
			</div>
		</div>

		<!-- 
	<p>you are at the index page.</p>
	<p>${system_message}</p>
	<a href="addSongPage"><button>Add a song to the database</button></a>
	<br>
	<br>
	<a href="songList"><button>Show list of songs in the
			database</button></a>	
	<br>
	
	
	
	
	
	

	<br>
	<a href="globalSummarize"><button>Global Summarize</button></a>
	
	<a href="arRecom"><button>Association Rule</button></a>
	<br>
	<br>
	<a href="startSummarize"><button>start summarize</button></a>
	<br>
	<br>
	<a href="addNewTag"><button>add new tag</button></a>
	<br>
	<br>
	<a href="commonWordsToDB"><button>save common words to DB</button></a>
	<br>
	<br>
	<a href="getSynonym"><button>set tag synonym</button></a>
	<br>
	<br>
	<a href="tagSongs"><button>tagSongs</button></a>
	<br>
	
	<form action="/searchTagForSong" method="post">
		<input type="text" name="artistName" placeholder="artistName"/>
		<input type="text" name="songTitle" placeholder="songTitle"/>
		<input type="submit">
	</form>
 	-->


		<jsp:include page="footer.jsp"></jsp:include>
</body>

</html>

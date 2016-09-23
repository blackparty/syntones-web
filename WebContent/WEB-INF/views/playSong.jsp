<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Play Song</title>
</head>
<body>

	<h1>SONG LIST !</h1>


	<table>
		<tr>
			<td>ID</td>
			<td>SONG</td>
		</tr>

		<c:forEach var="song" items="${songList}">
			<tr>
				<td>${song.songId}</td>
				<td>${song.songTitle}</td>
				<td>
					<button>
						<a
							href="${pageContext.request.contextPath}/viewSong?id=${song.songId}">></a>
					</button>
				</td>
			</tr>
		</c:forEach>
	</table>


</body>
</html>
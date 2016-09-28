<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="javax.ws.rs.core.Response"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Song Info</title>
</head>
<body>
	<form role="form"
		action="${pageContext.request.contextPath}/playThisSong" method="POST">
		<p>${songId}</p>
		<input type="hidden" value="${songId}" name="song_id" /> <select
			name="session_id">
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>

		</select>
		<button type="submit">Play</button>
	</form>

</body>
</html>
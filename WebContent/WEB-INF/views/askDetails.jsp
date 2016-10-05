<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ask Details</title>
</head>
<body>
	<p>${system_message}</p>
	<form action="checkDetails" method="post">
		<input type="text" name="songTitle" value="${ songTitle }" placeholder="song title"/>
		<input type="text" name="artistName" value="${ artistName }" placeholder="artist name"/>
		<input type="submit" value="validate"> 
	</form>
	<form action="saveSong" method="post">
		<input type="text" name="songTitle" value="${ songTitle }" hidden="true" placeholder="song title"/>
		<input type="text" name="artistName" value="${ artistName }" hidden="true" placeholder="artist name"/>
		<input type="submit" value="save"> 
	</form>
	<a href="index"><button>Cancel</button></a>
</body>
</html>
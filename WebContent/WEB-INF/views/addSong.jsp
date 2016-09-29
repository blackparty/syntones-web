<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Song</title>
</head>
<body>
	<form action="readMp3" method="POST" enctype="multipart/form-data" action="upload">
		File to upload: <input type="file" name="file" /><br>
		<input type="submit" name="action" value="read file" /> 
	</form>
	<a href="index"><button>Cancel</button></a>
</body>
</html>
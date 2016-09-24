<%@page import="java.util.List"%>
<%@page import="com.blackparty.syntones.model.Song"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		List<Song> songList= (List)request.getAttribute("songList"); 
	%>
	<p>songs in the database are as follows:</p>
	<%
		int counter = 0;
		for(Song e:songList){
			counter++;
			String songTitle = e.getSongTitle();
			String artistName = e.getArtist().getArtistName();
			out.print("<p>"+counter+")"+songTitle+","+artistName+"</p>");
		}
	%>
	<a href="index"><button>back</button></a>
</body>
</html>
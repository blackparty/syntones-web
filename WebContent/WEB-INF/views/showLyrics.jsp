
<%@page import="java.util.List"%>
<%@page import="com.blackparty.syntones.model.Song"%>
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
		List<String> lyricsList = (List<String>)request.getSession().getAttribute("lyrics");
		Song song = (Song)request.getSession().getAttribute("song");
		String artistName;
		String songTitle;
		if(song != null){
			artistName = song.getArtistName();
			songTitle = song.getSongTitle();
		}else{
			artistName = (String)request.getAttribute("artistName");
			songTitle = (String)request.getAttribute("songTitle");
		}
	%>	
	<p>${system_message}</p>
	<form action="saveSong" method="post">
		<input type="text" name="songTitle" value="${songTitle }"/>
		<input type="text" name="artistName" value="${artistName }"/>
		<input type="submit" value="Add song"/> 
	</form>
	<%
		if (lyricsList != null) {
			for (int i = 0; i < lyricsList.size(); i++) {
				String output = "<p>" + lyricsList.get(i) + "</p>";
				out.print(output);
				/* if(lyricsList.get(i).equals(",")&&lyricsList.get(i-1).equals(",")){
					out.println("<br>");
				}else{
					out.println(lyricsList.get(i));
				}	 */
			}
		}
	%>
</body>
</html>
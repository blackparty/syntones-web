<%@page import="com.blackparty.syntones.model.Song"%>
<%@page import="com.blackparty.syntones.model.Artist"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		List<String> lyricsList = (List<String>) session.getAttribute("lyrics");
		Song song = (Song) session.getAttribute("song");
		if (song != null) {
			String artistName = song.getArtistName();
			String songTitle = song.getSongTitle();
		}

		ArrayList<Song> prioSong = (ArrayList<Song>) session.getAttribute("resultSongPrio");
		ArrayList<Song> songs = (ArrayList<Song>) session.getAttribute("resultSong");
		ArrayList<Artist> artists = (ArrayList<Artist>) session.getAttribute("resultArtist");
		String resultmessage = (String) session.getAttribute("resultMessage");
	%>
	<p>you are at the index page.</p>
	<p>${message}</p>

	<form method="POST" enctype="multipart/form-data" action="upload">
		File to upload: <input type="file" name="file" /><br> <input
			type="text" name="artistName" value="${artistName} " /> <input
			type="text" name="songTitle" value="${songTitle}" /> <input
			type="submit" name="action" value="read" /> <input type="submit"
			name="action" value="save" />
	</form>
	<%
		if(resultmessage!=null){
			out.print(resultmessage);
		}
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
	<br>
	<br>
	<form method="POST" action="search">
		SEARCH SONG <input type="text" name="searchSong" /> <input
			type="submit" name="SUBMIT" value="submit" />
	</form>
	<br>
	<br>
	<h3>Search result:</h3>
	<br>
	<h4>Songs</h4>
	<%
		if (prioSong != null) {
			for (Song sPrio : prioSong) {
				String output = "<h3>" + sPrio.getSongTitle() + "</h3><br>";
				out.print(output);
			}
		}
	%>
	<br>

	<%
		if (songs != null) {
			for (Song s : songs) {
				String output = "<h4>" + s.getSongTitle() + "</h4><br>";
			}
		}
	%>
	<h4>Artists</h4>
	<%
		if (artists != null) {
			for (Artist artist : artists) {
				String output = "<h3>" + artist.getArtistName()
						+ "</h3><br>";
				out.print(output);
			}
		}
	%>
</body>
</html>
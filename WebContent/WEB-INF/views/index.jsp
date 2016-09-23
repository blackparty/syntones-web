<%@page import="com.blackparty.syntones.model.Song"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.List"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<spring:url value="/resources/javascript/mainJS.js" var="mainJs" />
<script type="text/javascript" src="${mainJs}"></script>
</head>
</head>
<body>
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
	<!-- 
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
 	-->
</body>
</html>
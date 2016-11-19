package com.blackparty.syntones.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class LyricsExtractor {

	String songLyricsURL= "http://www.songlyrics.com";
    


    public LyricsExtractor (){}
    public LyricsExtractor (String mySongLyricsURL){
        this.songLyricsURL = mySongLyricsURL;
    }
    
    public List<String> getSongLyrics( String band, String songTitle) throws IOException {
     List<String> lyrics= new ArrayList<String>();
     //System.setProperty("http.proxyHost", "127.0.0.1"); //127.0.0.1
    // System.setProperty("http.proxyPort", "8182"); //8182
     
  //   Document doc = Jsoup.connect(songLyricsURL+ "/"+band.replace(" ", "-").toLowerCase()+"/"+songTitle.replace(" ", "-").toLowerCase()+"-lyrics/").userAgent("Chrome").get();
     Response response= Jsoup.connect(songLyricsURL+ "/"+band.replace(" ", "-").toLowerCase()+"/"+songTitle.replace(" ", "-").toLowerCase()+"-lyrics/").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").referrer("http://www.google.com").timeout(12000) 
             .followRedirects(true)
             .execute();
     Document doc = response.parse();
     String title = doc.title();
     System.out.println(title);
     Element p = doc.select("p.songLyricsV14").get(0);   
      for (Node e: p.childNodes()) {
          if (e instanceof TextNode) {
            lyrics.add(((TextNode)e).getWholeText());
          }     
      }
     return lyrics;
   }
	
}

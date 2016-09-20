package com.blackparty.syntones.core;

import com.blackparty.syntones.model.Song;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class TrackSearcher {
	private String apiKey = "ffdf855f88a5a5e7555f0c1043ca3341";
	private String artistName = "";
	private String songTitle = "";
	private String resultName;
	private String resultSong;
	private Song songResult;
	
	
	
	public Song search(Song song){	

        String searchTrack = "http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + song.getSongTitle() + "&api_key=ffdf855f88a5a5e7555f0c1043ca3341&format=json";

        try {

            URL url = new URL(searchTrack);
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String l = null;
            String result = "";
            while ((l = br.readLine()) != null) {
                result = result.concat(l);
            }
            br.close();

            //System.out.println(result);
            JSONObject json = (JSONObject) new JSONParser().parse(result);
            String x = json.get("results").toString();
            json = (JSONObject) new JSONParser().parse(x);
            String y = json.get("trackmatches").toString();

            ArrayList<Song> songList = new ArrayList<Song>();

            System.out.println("TRACK MATCHES\n"+y);
            //parsing
            LinkedList<String> curlyBraceList = new LinkedList<String>();
            LinkedList<String> squareBracketList = new LinkedList<String>();
            int counter = 0;
            char character = ' ';
            String item = "";
            boolean flag = false;
            //reading each items starts when it reads a "[" . each item is encased with curly braces
            //starts traversing until it reaches square bracket.
            do {
                character = y.charAt(counter);
                counter++;
            } while (character != '[');
            squareBracketList.push("[");
            curlyBraceList.push("{");
            while (!squareBracketList.isEmpty()) {
                character = y.charAt(counter);
                //System.out.println("character = " + character);
                if (character == '{') {
                    curlyBraceList.push("{");
                    //System.out.println("push {");
                    flag = true;
                } else if (character == '}') {
                    curlyBraceList.pop();
                    //System.out.println("pop");

                } else if (character == '[') {
                    squareBracketList.push("[");
                    //System.out.println("hit!!");
                } else if (character == ']') {
                    squareBracketList.pop();
                    //System.out.println("poppin");
                }
                item = item + y.charAt(counter);

                counter++;
                if (curlyBraceList.size() == 1) {
                    json = (JSONObject) new JSONParser().parse(item);
                    resultName = json.get("artist").toString();
                    resultSong = json.get("name").toString();
                    Song s = new Song(resultSong,resultName);
                    System.out.println(s.toString());
                    songList.add(s);
                    item = "";
                    counter++;
                }
                if (curlyBraceList.size() == 0) {
                    break;
                }
            }
            
            //travers, get LD
            System.out.println("Lastfm returned "+songList.size()+" result(s)");
            int smallestDistance = 100;
            for(int i=0;i<songList.size();i++){
                int distance = StringUtils.getLevenshteinDistance(song.getArtistName().toLowerCase(),songList.get(i).getArtistName().toLowerCase());
                songList.get(i).setDistance(distance);
                if(smallestDistance > distance){
                    smallestDistance = distance;
                }
            }
            for (Song s : songList) {
                System.out.print(s.toString()+ " ??   ");
                if(smallestDistance == s.getDistance()){
                    System.out.println("HIT!!!");
                    songResult = s;
                    break;
                }else {
                    System.out.println("no....");
                }
            }
            

        } catch (NoSuchElementException e) {
            //nothing to do here yet. mag exceptin tungod kay ang linkedlist na hurot samtang nag "pop" siya..
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return songResult;
	}
	
}

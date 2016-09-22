package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.DAO.CommonWordDAO;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.service.CommonWordService;

public class Summarize {
	@Autowired CommonWordService commonWordService;
	private Map<String, Integer> wordMap;

	public Map<String, Integer> getWordMap(){
		return wordMap;
	}
	
	public List<SongLine> start(Song song) throws Exception {
        String line = "";
        ArrayList<String> lyrics = new ArrayList<String>();
        String songLyrics = song.getLyrics();
        Stemmer stem = new Stemmer();
        StringTokenizer stringTokenizer = new StringTokenizer(songLyrics);
        while (stringTokenizer.hasMoreTokens()) {
            //checks for any special characters: if found. ignore the line
            String string = stringTokenizer.nextToken();
            if (string.contains("(") || string.contains("[") || string.contains("*")) {
                while (stringTokenizer.hasMoreTokens()) {
                    string = stringTokenizer.nextToken();
                    if (string.contains(")") || string.contains("]") || string.contains("*")) {
                        break;
                    }
                }
                continue;
            }
            if (string.contains(",")) {
                string = string.replace(",", "");
            }
            if (string.contains("'")) {
                string = string.replace("'", "");
            }
            if (string.contains("\\n")) {
                string = string.replace("\\n", "");
                String stemmedString = stem.stem(string);
                System.out.println(" stemming " + string + " == " + stemmedString);
                line = line.concat(stemmedString);
                lyrics.add(line);
                line = "";
            } else {
                String stemmedString = stem.stem(string);
                //System.out.println(" stemming " + string + " == " + stemmedString);
                string = stem.stem(stemmedString);
                line = line.concat(stemmedString + " ");
            }
        }
        
        for (int i = 0; i < lyrics.size(); i++) {
            if (lyrics.get(i).length() == 0) {
                lyrics.remove(i);
            }
        }
        
        
        
        
        CompressionHashing compressionHashing = new CompressionHashing();
        List<String> hashedLines = compressionHashing.hash(lyrics);
        VectorWeight lsWeight = new VectorWeight();
        List<Float> vectorWeights = lsWeight.getVectorWeight(hashedLines);

        SentenceWeight sw = new SentenceWeight();
        wordMap = sw.wordCount(lyrics);
        
        List<Float> sentenceWeights = sw.getSentenceWeight(lyrics, wordMap);
        SongLine[] songLines = new SongLine[lyrics.size()];

        //calculating results
        for (int i = 0; i < hashedLines.size(); i++) {
            float r = (float) (sentenceWeights.get(i) + vectorWeights.get(i)) / 2;
            SongLine s = new SongLine(i + 1, lyrics.get(i), r);
            songLines[i] = s;
            System.out.println("Line " + (i + 1) + " = " + r);
        }
        //sorting
        Arrays.sort(songLines, SongLine.SongLineComparator);

        ArrayList<SongLine> finalSongLine = new ArrayList<>();
        System.out.println("SORTED LIST");
        float temp = 0;
        for (int i = 0; i < songLines.length; i++) {
            //removing repeating lines.
            if (temp == 0) {
                temp = songLines[i].getResult();
                System.out.print("Line " + songLines[i].getLineNumber() + " ");
                System.out.print(songLines[i].getLine() + " ");
                System.out.println(songLines[i].getResult());
                SongLine sl = new SongLine(songLines[i].getLineNumber(), songLines[i].getLine(), songLines[i].getResult());
                finalSongLine.add(sl);
            } else {
                if (temp != songLines[i].getResult()) {
                    temp = songLines[i].getResult();
                    System.out.print("Line " + songLines[i].getLineNumber() + " ");
                    System.out.print(songLines[i].getLine() + " ");
                    System.out.println(songLines[i].getResult());
                    SongLine sl = new SongLine(songLines[i].getLineNumber(), songLines[i].getLine(), songLines[i].getResult());
                    finalSongLine.add(sl);
                }
            }
        }
        return finalSongLine;
    }
}

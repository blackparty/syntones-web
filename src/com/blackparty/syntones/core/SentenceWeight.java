package com.blackparty.syntones.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.model.CommonWord;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.service.CommonWordService;

public class SentenceWeight {
	@Autowired private CommonWordService commonWordService;
	 private int totalNumberOfWordsInTheSong = 0;
	    private int numberOfWordsInTheLine = 0;

	    public SentenceWeight() {
	    }

	    public Map<String, Integer> wordCount(List<SongLine> songLyrics) throws FileNotFoundException, IOException, Exception {
	        System.out.println("Running word count..");
	        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	        String word;
	        for (SongLine s : songLyrics) {
	            StringTokenizer st = new StringTokenizer(s.getLine());
	            while (st.hasMoreTokens()) {
	                word = st.nextToken();
	                word = word.toLowerCase();
	                // System.out.print("word token:" + word + "..\t");
	                if (wordMap.isEmpty()) {
	                    wordMap.put(word, 1);
	                } else {
	                    if (wordMap.containsKey(word)) {
	                        wordMap.put(word, wordMap.get(word) + 1);
	                        // System.out.println("found in map");
	                    } else {
	                        wordMap.put(word, 1);
	                        //System.out.println("not found in map");
	                    }

	                }
	            }
	        }
	        //sorted treemap
	        int counter = 0;
	        Map<String, Integer> map = new TreeMap<String, Integer>(wordMap);
	        System.out.println("Reading the map in alphabetical order.");
	        Set set = map.entrySet();
	        Iterator iterator = set.iterator();
	        while (iterator.hasNext()) {
	            Map.Entry entry = (Map.Entry) iterator.next();
	            System.out.println("> " + entry.getKey() + "\t\t\t" + entry.getValue());
	            counter = counter + (int) entry.getValue();
	            //couter++;
	        }
	        System.out.println("Number of words in the song: " + counter);
	        totalNumberOfWordsInTheSong = counter;
	        return wordMap;
	    }

	    public ArrayList<Float> getSentenceWeight(List<SongLine> songLyrics, Map<String, Integer> wordMap) {
	        System.out.println("Calculting sentence weight.");
	        ArrayList<Float> sentenceWeight = new ArrayList<>();
	        double sum = 0;
	      
	        for (SongLine line : songLyrics) {
	            ArrayList<Float> affinityWeight = null;
	            if (line.getLine().length() == 0) {
	                System.out.println("hit!");
	                continue;
	            }
	            System.out.println(line);
	            affinityWeight = getAffinityWeight(line.getLine(), wordMap);
	            for (float s : affinityWeight) {
	                sum = (sum + s);
	            }
	            System.out.println("sum = " + sum);
	            float sWeight = ((float) 1 / numberOfWordsInTheLine) * (float) sum;
	            numberOfWordsInTheLine = 0;
	            sum = 0;
	            System.out.println("Sentence Weight: " + sWeight);
	            sentenceWeight.add(sWeight);
	        }
	        return sentenceWeight;
	    }
	    
	    
	    
	    public ArrayList<Float> getAffinityWeight(String line, Map<String, Integer> wordMap) {
	        ArrayList<Float> affinityWeight = new ArrayList<>();
	        StringTokenizer stringTokenizer = new StringTokenizer(line);
	        while (stringTokenizer.hasMoreTokens()) {
	            String word = stringTokenizer.nextToken();
	            System.out.print("Calculating affinity of the word: " + word);
	            numberOfWordsInTheLine++;
	            int count = wordMap.get(word.toLowerCase());
	            System.out.print("\t " + count + " / " + totalNumberOfWordsInTheSong + " = ");
	            float aWeight = (float) count / totalNumberOfWordsInTheSong;
	            System.out.println(aWeight);
	            affinityWeight.add(aWeight);
	        }
	        double sum = 0;
	        for (double a : affinityWeight) {
	            sum = sum + a;
	        }
	        //System.out.println("number of words in the line" + numberOfWordsInTheLine);
	        return affinityWeight;
	    }
}

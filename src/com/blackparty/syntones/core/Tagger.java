package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.DAO.TagDAO;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.model.TagSong;
import com.blackparty.syntones.model.TagSynonym;
import com.blackparty.syntones.service.TagService;

public class Tagger {
	private Summarize summarize = new Summarize();
	private Stemmer stem = new Stemmer();
	
	public Tagger(){
		
	}
	
	public List<TagSong> start(Song song,List<Tag>tags) throws Exception {
		System.out.println("start tagging the songs");
		ArrayList<TagSong> tagSong = new ArrayList<>();
		HashMap<String, Integer> wordMap = mapWords(song);
		int totalNumberOfWordsInTheSong =  getNumberOfWords(wordMap);
		// pulling one tag
		for(int i=0;i<tags.size();i++){
			boolean flag = false;
			// preparing the tag with its synonyms
			ArrayList<String> tagWithFriends = new ArrayList<>();
			tagWithFriends.add(tags.get(i).getTag());
			for (TagSynonym syn : tags.get(i).getSynonyms()) {
				tagWithFriends.add(syn.getSynonym());
			}
			System.out.println("Evaluating the song with the following tags: ");
			for(String s:tagWithFriends){
				System.out.println(s);
			}
			ArrayList<String> hitTag = new ArrayList<>();
			float affinityWeight = 0;
			for (String tag : tagWithFriends) {
				System.out.println("evaluating tag: "+tag);
				if (wordMap.containsKey(tag)) {
					flag = true;
					//gets affinityWeight
					System.out.print("HIT! ");
					System.out.print("Tag: "+tag+"\t "+wordMap.get(tag)+" / "+totalNumberOfWordsInTheSong);
					float temp = (float)wordMap.get(tag)/totalNumberOfWordsInTheSong;
					System.out.print(" =  "+temp);
					if(affinityWeight == 0){
						affinityWeight = temp;
					}else{
						if(affinityWeight<temp){
							System.out.println(" higher!");
							affinityWeight = temp;
						}else{
							System.out.println(" not .");
						}
						
					}
				}
			}
			//update tag's flag
			tags.get(i).setFlag(flag);
			if(affinityWeight != 0){
				TagSong tg = new TagSong();
				tg.setAffinity(affinityWeight);
				tg.setTag(tags.get(i).getTag());
				tg.setSongId(song.getSongId());
				tagSong.add(tg);
			}
		}
		return tagSong;
	}
	public List<String> cleanLyrics(Song song){
		System.out.println("Placing the lyrics into a List<String>...");
		String line = "";
		
		ArrayList<String> lyrics = new ArrayList<>();
		
			int counter = 1;
			String songLyrics = song.getLyrics();
			StringTokenizer stringTokenizer = new StringTokenizer(songLyrics);
			while (stringTokenizer.hasMoreTokens()) {
				// checks for any special characters: if found. ignore the line
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
				string = string.replace(",", "");
				if (string.contains("'")) {
					string = string.replace("'", "");
				}
				if (string.contains("\\n")) {
					string = string.replace("\\n", "");
					String stemmedString = stem.stem(string);
					// System.out.println(" stemming " + string + " == " +
					// stemmedString);
					line = line.concat(stemmedString);
					lyrics.add(line);
					line = "";
				} else {
					String stemmedString = stem.stem(string);
					// System.out.println(" stemming " + string + " == " +
					// stemmedString);
					string = stem.stem(stemmedString);
					line = line.concat(stemmedString + " ");
				}
			}
			//removes songLine that is blank
			for(int i=0;i<lyrics.size();i++){
				if(lyrics.get(i).length() == 0){
					lyrics.remove(i);
				}
			}
		return lyrics;
	}
	public HashMap<String, Integer> mapWords(Song song) throws Exception {
		List<String> lyrics = cleanLyrics(song);
		// maps each word
		System.out.println("mapping..");
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		String word;
		for (String s : lyrics) {
			StringTokenizer st = new StringTokenizer(s);
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
						// System.out.println("not found in map");
					}
				}
			}
		}
		return wordMap;

	}

	public int getNumberOfWords(HashMap<String,Integer> wordMap){
		int totalNumberOfWordsInTheSong = 0;
		// sorted treemap
		int counter = 0;
		Map<String, Integer> map = new TreeMap<String, Integer>(wordMap);
		System.out.println("Reading the map in alphabetical order.");
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			System.out.println("> " + entry.getKey() + "\t\t\t" + entry.getValue());
			counter = counter + (int) entry.getValue();
			// couter++;
		}
		System.out.println("Number of words in the song: " + counter);
		totalNumberOfWordsInTheSong = counter;
		return totalNumberOfWordsInTheSong;
	}

}

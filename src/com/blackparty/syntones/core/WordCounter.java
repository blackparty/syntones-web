package com.blackparty.syntones.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.blackparty.syntones.model.CommonWord;
import com.blackparty.syntones.service.StopWordService;

public class WordCounter {

	public List<CommonWord> count(List<String> lines, List<String> stopwords) throws Exception {
		List<CommonWord> commonWords = new ArrayList<>();

		System.out.println("Running word count..");
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		String word;
		Stemmer stem = new Stemmer();

		for (String s : lines) {
			StringTokenizer st = new StringTokenizer(s);
			while (st.hasMoreTokens()) {
				word = st.nextToken();
				word = word.toLowerCase();
				//add only nouns
				//if (stem.checkNoun(word)) {
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
					//}
				}
				// System.out.print("word token:" + word + "..\t");
			}
		}
		// removing stop words
		for (String s : stopwords) {
			wordMap.remove(s);
		}

		for (String key : wordMap.keySet()) {
			CommonWord cw = new CommonWord(key, wordMap.get(key));
			commonWords.add(cw);
		}

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

		return commonWords;

	}
}

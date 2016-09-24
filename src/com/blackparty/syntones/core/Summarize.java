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
	@Autowired
	CommonWordService commonWordService;
	private Map<String, Integer> wordMap;
	private Stemmer stem = new Stemmer();

	public Map<String, Integer> getWordMap() {
		return wordMap;
	}

	public List<SongLine> cleanLyrics(List<Song> songs) throws Exception {
		System.out.println("Placing the lyrics into a List<String>...");
		String line = "";
		
		ArrayList<SongLine> lyrics = new ArrayList();
		for (Song s : songs) {
			int counter = 1;
			String songLyrics = s.getLyrics();
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
					SongLine songLine = new SongLine();
					songLine.setSongId(s.getSongId());
					songLine.setLineNumber(counter++);
					songLine.setLine(line);
					lyrics.add(songLine);
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
				if(lyrics.get(i).getLine().length() == 0){
					lyrics.remove(i);
				}
			}

		}

		return lyrics;

	}

	public List<SongLine> start(List<Song> songs) throws Exception {

		List<SongLine> lyrics = cleanLyrics(songs);
		CompressionHashing compressionHashing = new CompressionHashing();
		List<String> hashedLines = compressionHashing.hash(lyrics);
		VectorWeight lsWeight = new VectorWeight();
		List<Float> vectorWeights = lsWeight.getVectorWeight(hashedLines);

		SentenceWeight sw = new SentenceWeight();
		wordMap = sw.wordCount(lyrics);

		List<Float> sentenceWeights = sw.getSentenceWeight(lyrics, wordMap);
		SongLine[] songLines = new SongLine[lyrics.size()];

		// calculating results
		for (int i = 0; i < lyrics.size(); i++) {
			float r = (float) (sentenceWeights.get(i) + vectorWeights.get(i)) / 2;
			//SongLine s = new SongLine(i + 1, lyrics.get(i), r);
			lyrics.get(i).setResult(r);
		}

		// sorting
		//Arrays.sort(songLines, SongLine.SongLineComparator);
		//ArrayList<SongLine> finalSongLine = new ArrayList<>();
		//System.out.println("SORTED LIST");
		/*String temp = "";
		
		
		for (int i = 0; i < lyrics.size(); i++) {
			// removing repeating lines.
			
			*/
			
			
			
			/*if (temp.length() == 0) {
				temp = lyrics.get(i).getLine();
				System.out.print("Line " + lyrics.get(i).getLineNumber() + " ");
				System.out.print(lyrics.get(i).getLine() + " ");
				System.out.println(lyrics.get(i).getResult());
//				SongLine sl = new SongLine(songLines[i].getLineNumber(), songLines[i].getLine(),
//						songLines[i].getResult());
//				finalSongLine.add(sl);
			} else {
				if (t) {
					temp = lyrics.get(i).getResult();
					System.out.println("SongId :"+lyrics.get(i).getSongId()+"  ");
					System.out.print("Line " + lyrics.get(i).getLineNumber() + " ");
					System.out.print(lyrics.get(i).getLine() + " ");
					System.out.println(lyrics.get(i).getResult()+ "  ");*/
					
//					SongLine sl = new SongLine(songLines[i].getLineNumber(), songLines[i].getLine(),
//							songLines[i].getResult());
////					finalSongLine.add(sl);
//				}
//			}
//		}
		return lyrics;
	}
}

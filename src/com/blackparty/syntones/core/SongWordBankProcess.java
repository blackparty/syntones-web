package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.model.*;
import com.blackparty.syntones.service.SongWordBankService;

public class SongWordBankProcess {
	@Autowired
	private SongWordBankService ss;

	public TemporaryModel WBSongProcess(ArrayList<Song> songs) throws Exception {
		ArrayList<String> bagOfWordsSong = new ArrayList();
		ArrayList<TokenNCount> perSong = new ArrayList();
		ArrayList<ArrayList<TokenNCount>> wordsPerSong = new ArrayList();
		ArrayList<SongWordBank> bow = new ArrayList<SongWordBank>();
		TemporaryModel tm = new TemporaryModel();

		String delimeter = "., ?!-;()[]{}@#$%^&*\"\n";
		String[] title = new String[songs.size()];

		int counter = 0;
		for (Song song : songs) {
			String songSL = song.getSongTitle() + "\n" + song.getLyrics();
			title[counter] = song.getSongTitle();
			counter++;
			StringTokenizer str = new StringTokenizer(songSL, delimeter);
			while (str.hasMoreTokens()) {
				String token = str.nextToken().trim();
				if (!token.equalsIgnoreCase(null)) {
					if (bagOfWordsSong.isEmpty()) {
						bagOfWordsSong.add(token);
					} else {
						if (!checkModelList(token, bagOfWordsSong)) {
							bagOfWordsSong.add(token);
						}
					}
				}
			}
			perSong = getTokenCount(songSL, delimeter);

			wordsPerSong.add(perSong);

		}
		bagOfWordsSong = sortModelList(bagOfWordsSong);

		for (String w : bagOfWordsSong) {
			SongWordBank wb = new SongWordBank();
			wb.setWord(w);
			bow.add(wb);
		}

		int[][] matrix_songs = getMatrix(songs.size(), bagOfWordsSong,
				wordsPerSong, title);
		tm.setSongs(saveMatrixToList(songs, matrix_songs));
		tm.setWords(bow);
		return tm;
	}

	private int[][] getMatrix(int size, ArrayList<String> bagOfWords,
			ArrayList<ArrayList<TokenNCount>> wordsPerIndex, String[] title) {
		boolean flag;
		int[][] matrix = new int[size][bagOfWords.size()];

		for (int i = 0; i < wordsPerIndex.size(); i++) {
			int c = 0;
			for (int a = 0; a < bagOfWords.size(); a++) {
				flag = false;

				for (int b = 0; b < wordsPerIndex.get(i).size(); b++) {
					if (bagOfWords.get(a).equalsIgnoreCase(
							wordsPerIndex.get(i).get(b).getToken())) {
						if (title != null) {
							matrix[i][c++] = wordsPerIndex.get(i).get(b)
									.getCount()
									+ titleWeight(title[i], wordsPerIndex
											.get(i).get(b).getToken());
							flag = true;
							break;
						} else {
							matrix[i][c++] = wordsPerIndex.get(i).get(b)
									.getCount();
							flag = true;
							break;
						}
					}

				}
				if (flag == false) {
					matrix[i][c++] = 0;
				}

			}

		}
		return matrix;
	}

	public int titleWeight(String article_path, String word) {
		StringTokenizer str = new StringTokenizer(article_path, " ,()&");
		int weight = 0, count = 0;
		while (str.hasMoreTokens()) {
			String token = str.nextToken().trim();
			if (token.equalsIgnoreCase(word)) {
				count += 1;
			}
		}
		weight = count * 20;
		return weight;
	}

	private ArrayList<TokenNCount> getTokenCount(String data, String delimeter) {
		ArrayList<TokenNCount> model_list = new ArrayList();
		StringTokenizer str = new StringTokenizer(data, delimeter);
		while (str.hasMoreTokens()) {
			String token = str.nextToken().trim();
			if (model_list.isEmpty()) {
				TokenNCount md = new TokenNCount();
				md.setToken(token);
				md.setCount(1);
				model_list.add(md);
			} else {
				if (!checkWordList(token, model_list)) {
					TokenNCount stcm = new TokenNCount();
					stcm.setToken(token);
					stcm.setCount(1);
					model_list.add(stcm);
				} else {
					for (TokenNCount model : model_list) {
						if (model.getToken().equalsIgnoreCase(token)) {
							model.setCount(model.getCount() + 1);
						}
					}
				}
			}
		}
		return model_list;
	}

	private boolean checkModelList(String word, ArrayList<String> bagOfWords) {
		for (int x = 0; x < bagOfWords.size(); x++) {
			if (bagOfWords.get(x).equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkWordList(String word, ArrayList<TokenNCount> word_list) {

		for (int i = 0; i < word_list.size(); i++) {
			if (word.equalsIgnoreCase(word_list.get(i).getToken())) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<String> sortModelList(ArrayList<String> model_list) {
		boolean flag = true;
		Collections.sort(model_list, ALPHABETICAL_ORDER);
		return model_list;
	}

	private Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
		public int compare(String str1, String str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
			if (res == 0) {
				res = str1.compareTo(str2);
			}
			return res;
		}
	};

	public ArrayList<Song> saveMatrixToList(ArrayList<Song> songs,
			int[][] matrix) {
		int counter = 0;
		if (songs.isEmpty()) {
			System.out.println("bakit");
		}
		for (Song song : songs) {
			String temp = "";
			for (int i = 0; i < matrix[counter].length; i++) {
				temp += String.valueOf(matrix[counter][i]) + " ";
			}
			song.setVectorSpace(temp);
			counter++;
		}

		return songs;
	}
}

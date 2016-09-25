package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.TemporaryModel;
import com.blackparty.syntones.model.TokenNCount;

public class ArtistWordBankProcess {
	public TemporaryModel WBArtistProcess(List<Artist> artists) {
		ArrayList<String> bagOfWordsArtists = new ArrayList<String>();
		ArrayList<TokenNCount> perArtist = new ArrayList<TokenNCount>();
		ArrayList<ArrayList<TokenNCount>> wordsPerArtist = new ArrayList<>();
		ArrayList<ArtistWordBank> bow = new ArrayList<ArtistWordBank>();
		TemporaryModel tm = new TemporaryModel();

		String delimeter = ". ,()&";

		for (Artist artist : artists) {
			StringTokenizer str = new StringTokenizer(artist.getArtistName(),
					delimeter);
			while (str.hasMoreTokens()) {
				String token = str.nextToken().trim();
				if (!token.equalsIgnoreCase(null)) {
					if (bagOfWordsArtists.isEmpty()) {
						bagOfWordsArtists.add(token);
					} else {
						if (!checkModelList(token, bagOfWordsArtists)) {
							bagOfWordsArtists.add(token);
						}
					}
				}
			}
			perArtist = (ArrayList<TokenNCount>) getTokenCount(artist.getArtistName(), delimeter);

			wordsPerArtist.add(perArtist);

		}
		bagOfWordsArtists = (ArrayList<String>) sortModelList(bagOfWordsArtists);

		for (String w : bagOfWordsArtists) {
			ArtistWordBank wb = new ArtistWordBank();
			wb.setWord(w);
			bow.add(wb);
		}

		int[][] matrix_artists = getMatrix(artists.size(), bagOfWordsArtists,
				wordsPerArtist);

		tm.setArtists(saveMatrixToList(artists, matrix_artists));
		tm.setAwords(bow);
		return tm;
	}

	private int[][] getMatrix(int size, List<String> bagOfWords,
			ArrayList<ArrayList<TokenNCount>> wordsPerArtist) {
		boolean flag;
		int[][] matrix = new int[size][bagOfWords.size()];

		for (int i = 0; i < wordsPerArtist.size(); i++) {
			int c = 0;
			for (int a = 0; a < bagOfWords.size(); a++) {
				flag = false;

				for (int b = 0; b < wordsPerArtist.get(i).size(); b++) {
					if (bagOfWords.get(a).equalsIgnoreCase(
							wordsPerArtist.get(i).get(b).getToken())) {
						matrix[i][c++] = wordsPerArtist.get(i).get(b).getCount();
						flag = true;
						break;
					}

				}
				if (flag == false) {
					matrix[i][c++] = 0;
				}

			}

		}
		return matrix;
	}

	private ArrayList<TokenNCount> getTokenCount(String data, String delimeter) {
		ArrayList<TokenNCount> model_list = new ArrayList<TokenNCount>();
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

	public List<Artist> saveMatrixToList(List<Artist> artists,
			int[][] matrix) {
		int counter = 0;
		for (Artist artist : artists) {
			String temp = "";
			for (int i = 0; i < matrix[counter].length; i++) {
				temp += String.valueOf(matrix[counter][i]) + " ";
			}
			artist.setVectorSpace(temp);
			counter++;
		}
		return artists;
	}

}

package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SearchModel;
import com.blackparty.syntones.model.SearchResultModel;
import com.blackparty.syntones.model.Song;

public class SearchProcess {
	public SearchResultModel SearchProcess(String searchWord, ArrayList<String> artist_wb,
			ArrayList<String> song_wb, ArrayList<Song> songs,
			ArrayList<Artist> artists) {
		ArrayList<String> paths = new ArrayList<>();
		ArrayList<String> vectors;
		ArrayList<String> vector_path;
		ArrayList<SearchModel> smodel = new ArrayList<>();
		ArrayList<SearchModel> amodel = new ArrayList<>();
		ArrayList<SearchModel> sresult = new ArrayList<>();
		ArrayList<SearchModel> aresult = new ArrayList<>();
		float vSearch, absV, absSearch, cos_angle, radians, degree;
		int document_count_song = 0, document_count_artist, token_count_artist, token_count_song;
		float[] idf_song, idf_artist, step4_matrix_song, step4_matrix_artist = null;
		float[][] step3_matrix_song, step3_matrix_artist = null;
		int[] vector_arr_song = null;
		int[] vector_arr_artist = null;

		SearchResultModel result = new SearchResultModel();
		
		
		// song;
		document_count_song = songs.size();
		System.out.println("song size" + document_count_song);
		token_count_song = song_wb.size();
		vector_arr_song = getQueryWord(searchWord, song_wb);
		smodel = toMatrixList(songs,null,"song");
		idf_song = getIDF(smodel, document_count_song);
		step3_matrix_song = step3(idf_song, smodel, document_count_song,
				token_count_song);
		step4_matrix_song = step4(vector_arr_song, idf_song);

		for (int i = 0; i < step3_matrix_song.length; i++) {
			vSearch = SumProduct(step3_matrix_song[i], step4_matrix_song);
			absV = (float) Math.sqrt(SumProduct(step3_matrix_song[i],
					step3_matrix_song[i]));
			absSearch = (float) Math.sqrt(SumProduct(step4_matrix_song,
					step4_matrix_song));
			cos_angle = (float) vSearch / (absV * absSearch);
			radians = (float) Math.acos(cos_angle);
			degree = (float) Math.toDegrees(radians);
			smodel.get(i).setCos_angle(cos_angle);
			smodel.get(i).setDegrees(degree);
		}
		int countNAN = 0;
		for (int i = 0; i < smodel.size(); i++) {
			if (Double.isNaN(smodel.get(i).getDegrees())) {
				countNAN++;
			}
		}
		if (countNAN >= smodel.size()) {
			System.out.println(searchWord + " not found.");
			result.setSongNan(true);
		} else {
			Collections.sort(smodel, new Comparator<SearchModel>() {
				@Override
				public int compare(SearchModel am1, SearchModel am2) {
					if (am1.getDegrees() > am2.getDegrees()) {
						return 1;
					} else if (am1.getDegrees() < am2.getDegrees()) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			System.out.println("\n\n === RESULTS ====");
			for (int i = 0; i < smodel.size(); i++) {
				if (smodel.get(i).getDegrees() < 90.0) {
					sresult.add(smodel.get(i));
					System.out.println("SongID : " + smodel.get(i).getId()
							+ "    Degrees :" + smodel.get(i).getDegrees());
				}else{
					smodel.remove(i);
				}
			}

		}

		// artist
		document_count_artist = artists.size();
		System.out.println("song size" + document_count_artist);
		token_count_artist = artist_wb.size();
		vector_arr_artist = getQueryWord(searchWord, artist_wb);
		amodel = toMatrixList(null,artists,"artists");
		idf_artist = getIDF(amodel, document_count_artist);
		step3_matrix_artist = step3(idf_artist, amodel, document_count_artist,
				token_count_artist);
		step4_matrix_artist = step4(vector_arr_artist, idf_artist);

		for (int i = 0; i < step3_matrix_artist.length; i++) {
			vSearch = SumProduct(step3_matrix_artist[i], step4_matrix_artist);
			absV = (float) Math.sqrt(SumProduct(step3_matrix_artist[i],
					step3_matrix_artist[i]));
			absSearch = (float) Math.sqrt(SumProduct(step4_matrix_artist,
					step4_matrix_artist));
			cos_angle = (float) vSearch / (absV * absSearch);
			radians = (float) Math.acos(cos_angle);
			degree = (float) Math.toDegrees(radians);
			amodel.get(i).setCos_angle(cos_angle);
			amodel.get(i).setDegrees(degree);
		}
		
		countNAN = 0;
		for (int i = 0; i < amodel.size(); i++) {
			if (Double.isNaN(amodel.get(i).getDegrees())) {
				countNAN++;
			}
		}
		if (countNAN >=amodel.size()) {
			System.out.println(searchWord + " not found.");
			result.setArtistNan(true);
		} else {
			Collections.sort(amodel, new Comparator<SearchModel>() {
				@Override
				public int compare(SearchModel am1, SearchModel am2) {
					if (am1.getDegrees() > am2.getDegrees()) {
						return 1;
					} else if (am1.getDegrees() < am2.getDegrees()) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			System.out.println("\n\n === RESULTS ====");
			for (int i = 0; i < amodel.size(); i++) {
				if (amodel.get(i).getDegrees() < 90.0) {
					aresult.add(amodel.get(i));
					System.out.println("ArtistID : " + amodel.get(i).getId()
							+ "    Degrees :" + amodel.get(i).getDegrees());
				}else{
				}
			}

		}
		
		result.setSongs(sresult);
		result.setArtists(aresult);
		
		return result;
	}

	public static float SumProduct(float[] array1, float[] array2) {
		float sum = 0, product;
		for (int i = 0; i < array1.length; i++) {
			product = array1[i] * array2[i];
			sum += product;
		}
		return sum;
	}

	public static int[] getQueryWord(String search_word,
			ArrayList<String> word_bank) {
		String[] words = search_word.split(" ");
		int[] vector_arr = new int[word_bank.size()];
		for (int i = 0; i < vector_arr.length; i++) {
			vector_arr[i] = 0;
		}
		for (int i = 0; i < word_bank.size(); i++) {
			for (int j = 0; j < words.length; j++) {
				if (word_bank.get(i).trim().equalsIgnoreCase(words[j])) {
					if (word_bank.get(i).trim().equalsIgnoreCase(words[j])) {
						System.out.println("hey!!!!");
						vector_arr[i] = vector_arr[i] + 1;
					} else {
						// System.out.println(word_bank.get(i) + " = " +
						// words[j]);
						vector_arr[i] = vector_arr[i] + 1;
						// System.out.println("va = " + vector_arr[i]);
					}
				}
			}
		}
		return vector_arr;
	}

	public static boolean checkWordBank(String word, ArrayList<String> word_bank) {
		for (int x = 0; x < word_bank.size(); x++) {
			if (word.equalsIgnoreCase(word_bank.get(x))) {
				return true;
			}
		}
		return false;
	}

	public static int getMax(int[] inputArray) {
		int maxValue = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] > maxValue) {
				maxValue = inputArray[i];
			}
		}
		return maxValue;
	}

	public static float[] step4(int[] vector_arr, float[] idf) {
		float[] tf_idf = new float[vector_arr.length];
		for (int i = 0; i < vector_arr.length; i++) {
			// System.out.println(vector_arr[i] +" / "+getMax(vector_arr)+
			// " * "+idf[i]);
			float temp = ((float) vector_arr[i] / (float) getMax(vector_arr))
					* (float) idf[i];
			// if (!Double.isNaN(temp)) {
			tf_idf[i] = temp;
			// } else {
			// tf_idf[i] = (float) 0.0;
			// }
		}
		System.out.println("step 4 na ta!");
		for (int i = 0; i < tf_idf.length; i++) {
			System.out.print(tf_idf[i] + "  ");
		}
		return tf_idf;
	}

	public float[][] step3(float[] idf, ArrayList<SearchModel> array_model,
			int docuCount, int token_count) {
		System.out.println("Step 3!");
		int[][] matrix = new int[docuCount][];
		for (int i = 0; i < docuCount; i++) {
			matrix[i] = array_model.get(i).getVector();
		}
		float[][] step3_matrix = new float[docuCount][matrix[0].length];
		for (int i = 0; i < docuCount; i++) {
			for (int j = 0; j < token_count; j++) {
				float temp = (float) matrix[i][j] * (float) idf[j];
				if (!Double.isNaN(temp)) {
					step3_matrix[i][j] = temp;
				} else {
					step3_matrix[i][j] = (float) 0.0;
				}
			}
		}

		// for (int i = 0; i < docuCount; i++) {
		// System.out.println("tf-idf : ");
		// for (int j = 0; j < token_count; j++) {
		// System.out.print("  " + step3_matrix[i][j]);
		// }
		// System.out.println("");
		// }
		return step3_matrix;
	}

	public float[] getIDF(ArrayList<SearchModel> array_model, int docuCount) {

		int[][] matrix = new int[docuCount][];
		for (int i = 0; i < array_model.size(); i++) {
			matrix[i] = array_model.get(i).getVector();
		}
		float[] idf = new float[matrix[0].length];
		for (int i = 0; i < matrix[0].length; i++) {
			int sum = 0;
			for (int j = 0; j < matrix.length; j++) {
				sum += matrix[j][i];
			}
			idf[i] = (float) log2((double) matrix.length / (double) sum);
		}
		return idf;
	}

	public double log2(double x) {
		return Math.log(x) / Math.log(2.0d);
	}

	public ArrayList<SearchModel> toMatrixList(ArrayList<Song> songs,
			ArrayList<Artist> artists, String str) {
		ArrayList<SearchModel> array_model = new ArrayList();
		if (str.equalsIgnoreCase("song")) {
			for (int i = 0; i < songs.size(); i++) {
				int[] array = getArray(songs.get(i).getVectorSpace());
				SearchModel obj = new SearchModel();
				obj.setVector(array);
				obj.setId(songs.get(i).getSongId());
				array_model.add(obj);
			}
			return array_model;
		} else {
			for (int i = 0; i < artists.size(); i++) {
				int[] array = getArray(artists.get(i).getVectorSpace());
				SearchModel obj = new SearchModel();
				obj.setVector(array);
				obj.setId(artists.get(i).getArtistId());
				array_model.add(obj);
			}
			return array_model;
		}
	}

	public int[] getArray(String aString) {
		String[] vString = aString.split(" ");
		int[] vector = new int[vString.length];
		for (int i = 0; i < vString.length; i++) {
			vector[i] = Integer.parseInt(vString[i]);
		}
		return vector;
	}

}

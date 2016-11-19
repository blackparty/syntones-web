package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.IdfModel;
import com.blackparty.syntones.model.SearchModel;
import com.blackparty.syntones.model.SearchResultModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.SongWordBankService;

public class SearchProcess {

	public static String delimeter = "  ' \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?";
	public static Stemmer stem = new Stemmer();
	

	public SearchResultModel searchProcess(String searchWord,
			List<SongWordBank> swbs, List<Song> songs, List<Artist> artists,
			List<ArtistWordBank> awbs) throws Exception {
		ArrayList<SearchModel> smSong = new ArrayList<SearchModel>();
		ArrayList<SearchModel> smArtist = new ArrayList<SearchModel>();
		SearchResultModel result = new SearchResultModel();
		ArrayList<IdfModel> smodel = new ArrayList<IdfModel>();
		System.out.println("word entered >>>" + searchWord);
		ArrayList<SongWordBank> tempSwbs = new ArrayList<SongWordBank>();
		ArrayList<ArtistWordBank> tempAwbs = new ArrayList<ArtistWordBank>();
		
		StringTokenizer str = new StringTokenizer(searchWord, delimeter);
		while (str.hasMoreTokens()) {
			String token = str.nextToken().trim();
			if (!token.equalsIgnoreCase("")) {
				String baseform = stem.stem(token).trim();
				if (!isStopWord(baseform)) {
					if (!baseform.equalsIgnoreCase(null)) {
						if (smodel.isEmpty()) {
							IdfModel im = new IdfModel();
							im.setTerm(baseform);
							im.setCount(1);
							smodel.add(im);
						} else {
							if (!checkModelList(baseform, smodel)) {
								IdfModel im = new IdfModel();
								im.setTerm(baseform);
								im.setCount(1);
								im.setSoaId(0);
								smodel.add(im);
							} else {
								for (IdfModel im : smodel) {
									if (im.getTerm().equalsIgnoreCase(baseform)) {
										im.setCount(im.getCount() + 1);
									}
								}
							}
						}
					}
				}
			}
		}

		/*
		 * Song
		 */
		for (SongWordBank swb : swbs) {
			if (songs.get(0).getSongId() == swb.getSongId()) {
				tempSwbs.add(swb);
			}
		}
		int[] queryTF = new int[tempSwbs.size()];
		for (int i = 0; i < queryTF.length; i++) {
			queryTF[i] = 0;
		}
		int index = 0;
		for (SongWordBank swb : tempSwbs) {
			for (IdfModel idfmodel : smodel) {
				if (swb.getWord().equalsIgnoreCase(idfmodel.getTerm())) {
					queryTF[index] = idfmodel.getCount();
				}
			}
			index++;
		}
		System.out.println("step4 result");
		int flagSong = 0;
		float[] step4Song = getStep4Song(queryTF, tempSwbs);
		for (int i = 0; i < step4Song.length; i++) {
			System.out.println(step4Song[i]);
			if(step4Song[i] == 0f){
				flagSong+=1;
			}
		}

		float step5QuerySong = getQueryStep5(step4Song);
		System.out.println("step5 result >> " + step5QuerySong);
		int countNAN=0;
		for (int i = 0; i < songs.size(); i++) {
			float step3[] = new float[tempSwbs.size()];
			index = 0;
			for (SongWordBank swb : swbs) {
				if (swb.getSongId() == songs.get(i).getSongId()) {
					step3[index] = (float) swb.getStep3();
					index++;
				}
			}
			float vSearch = SumProduct(step3, step4Song);
			float absV = (float) Math.sqrt(SumProduct(step3, step3));
			float absSearch = (float) songs.get(i).getStep5Tfidf();
			float cos_angle = (float) vSearch / (absV * absSearch);
			float radians = (float) Math.acos(cos_angle);
			float degree = (float) Math.toDegrees(radians);

			SearchModel sm = new SearchModel();
			sm.setId(songs.get(i).getSongId());
			sm.setDegree(degree);
			if (Double.isNaN(degree)) {
				countNAN++;
			}else{
				smSong.add(sm);
			}
		}
		if (countNAN >= smSong.size()) {
			System.out.println(searchWord + " not found.");
		} else {
			Collections.sort(smSong, new Comparator<SearchModel>() {

				@Override
				public int compare(SearchModel am1, SearchModel am2) {
					if (am1.getDegree() > am2.getDegree()) {
						return 1;
					} else if (am1.getDegree() < am2.getDegree()) {
						return -1;
					} else {
						return 0;
					}
				}
			});
		}
		/*
		 * Artist
		 * 
		 */

		for (ArtistWordBank swb : awbs) {
			if (artists.get(0).getArtistId() == swb.getArtistId()) {
				tempAwbs.add(swb);
			}
		}
		int[] queryTFArtist = new int[tempAwbs.size()];
		
		index = 0;
		for (ArtistWordBank awb : tempAwbs) {
			for (IdfModel idfmodel : smodel) {
				if (awb.getWord().equalsIgnoreCase(idfmodel.getTerm())) {
					queryTFArtist[index] = idfmodel.getCount();
				}
			}
			index++;
		}
		System.out.println("step4 result");
		int flagArtist = 0;
		float[] step4Artist = getStep4Artist(queryTFArtist, tempAwbs);
		for (int i = 0; i < step4Artist.length; i++) {
			System.out.println(step4Artist[i]);
			if(step4Artist[i] == 0f){
				flagArtist +=1;
			}
		}

		float step5QueryArtist = getQueryStep5(step4Artist);
		System.out.println("step5 result >> " + step5QueryArtist);
		
		countNAN=0;
		for (int i = 0; i < artists.size(); i++) {
			float step3[] = new float[tempAwbs.size()];
			index = 0;
			for (ArtistWordBank swb : awbs) {
				if (swb.getArtistId() == artists.get(i).getArtistId()) {
					step3[index] = (float) swb.getStep3();
					index++;
				}
			}
			float vSearch = SumProduct(step3, step4Artist);
			float absV = (float) Math.sqrt(SumProduct(step3, step3));
			float absSearch = (float) songs.get(i).getStep5Tfidf();
			float cos_angle = (float) vSearch / (absV * absSearch);
			float radians = (float) Math.acos(cos_angle);
			float degree = (float) Math.toDegrees(radians);

			SearchModel sm = new SearchModel();
			sm.setId(artists.get(i).getArtistId());
			sm.setDegree(degree);
			if (Double.isNaN(degree)) {
				countNAN++;
			}else{
				smArtist.add(sm);
			}
		}
		if (countNAN >= smArtist.size()) {
			System.out.println(searchWord + " not found.");
		} else {
			Collections.sort(smArtist, new Comparator<SearchModel>() {

				@Override
				public int compare(SearchModel am1, SearchModel am2) {
					if (am1.getDegree() > am2.getDegree()) {
						return 1;
					} else if (am1.getDegree() < am2.getDegree()) {
						return -1;
					} else {
						return 0;
					}
				}
			});
		}
		if(flagSong >= step4Song.length){
			smSong.removeAll(smSong);
		}
		if(flagArtist >= step4Artist.length){
			smArtist.removeAll(smArtist);
		}
		result.setSongs(smSong);
		result.setArtists(smArtist);
		return result;
	}

	private boolean isStopWord(String word) {
		String[] string = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
				.split(" ");
		String[] stpWords = { "a", "an", "as", "are", "is", "was",
			"were", "as", "at", "be", "by", "for", "from", "in", "on", "to",
			"this", "the", "that", "will", "with", "or", "nor", "whether",
			"neither", "and" };
		
		for (String token : stpWords) {
			if (word.equalsIgnoreCase(token)) {
				return true;
			}
		}
		for (String token : string) {
			if (word.equalsIgnoreCase(token)) {
				return true;
			}
		}

		return false;
	}

	private boolean checkModelList(String word, ArrayList<IdfModel> model_list) {
		for (int x = 0; x < model_list.size(); x++) {
			if (word.equalsIgnoreCase(model_list.get(x).getTerm())) {
				return true;
			}
		}
		return false;
	}

	private float[] getStep4Song(int[] queryTF, List<SongWordBank> swbs) {
		float[] step4Result = new float[queryTF.length];

		for (int i = 0; i < queryTF.length; i++) {
			step4Result[i] = ((float) queryTF[i] / (float) swbs.get(i)
					.getMaxCount()) * (float) swbs.get(i).getIdf();
		}
		return step4Result;
	}

	private float[] getStep4Artist(int[] queryTF, List<ArtistWordBank> awbs) {
		float[] step4Result = new float[queryTF.length];

		for (int i = 0; i < queryTF.length; i++) {
			step4Result[i] = ((float) queryTF[i] / (float) awbs.get(i)
					.getMaxCount()) * (float) awbs.get(i).getIdf();
		}
		return step4Result;
	}

	private float getQueryStep5(float[] step4Result) {
		float step5Result = 0f, temp = 0f;

		for (int i = 0; i < step4Result.length; i++) {
			temp += (float) Math.pow(step4Result[i], 2);
		}
		step5Result = (float) Math.sqrt(temp);

		return step5Result;
	}
	public static float SumProduct(float[] array1, float[] array2) {
		float sum = 0, product;
		for (int i = 0; i < array1.length; i++) {
			product = array1[i] * array2[i];
			sum += product;
		}
		return sum;
	}
}

package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import antlr.Token;

import com.blackparty.syntones.model.IDFReturn;
import com.blackparty.syntones.model.IdfModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;

public class SongIDFProcess {
	public static String delimeter = "1234567890  ' \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?";
	public static Stemmer stem = new Stemmer();
	public static String[] stpWords = { "a", "an", "as", "are", "is", "was",
			"were", "as", "at", "be", "by", "for", "from", "in", "on", "to",
			"this", "the", "that", "will", "with", "or", "nor", "whether",
			"neither", "and" };

	public IDFReturn SongIdfProcess(List<Song> songs, int docuCount)
			throws Exception {
		ArrayList<IdfModel> bagOfWordsSong = new ArrayList<IdfModel>();
		ArrayList<IdfModel> perSong = new ArrayList<IdfModel>();
		ArrayList<ArrayList<IdfModel>> words = new ArrayList<ArrayList<IdfModel>>();
		ArrayList<SongWordBank> swb = new ArrayList<SongWordBank>();

		System.out.println("Song Count : " + docuCount);
		String[] title = new String[songs.size()];
		int counter = 0;
		for (Song song : songs) {
			String songSL = song.getSongTitle() + "\n" + song.getLyrics();
			title[counter] = song.getSongTitle();
			counter++;
			StringTokenizer str = new StringTokenizer(songSL, delimeter);
			while (str.hasMoreTokens()) {
				String token = str.nextToken().trim();
				if (!token.equalsIgnoreCase("")) {
					String baseform = stem.stem(token).trim();
					if (!isStopWord(baseform)) {
						if (!baseform.equalsIgnoreCase(null)) {
							if (bagOfWordsSong.isEmpty()) {
								IdfModel im = new IdfModel();
								im.setTerm(baseform);
								im.setCount(1);
								im.setSoaId(0);
								bagOfWordsSong.add(im);
							} else {
								if (!checkModelList(baseform, bagOfWordsSong)) {
									IdfModel im = new IdfModel();
									im.setTerm(baseform);
									im.setCount(1);
									im.setSoaId(0);
									bagOfWordsSong.add(im);
								} else {
									for (IdfModel im : bagOfWordsSong) {
										if (im.getTerm().equalsIgnoreCase(
												baseform)) {
											im.setCount(im.getCount() + 1);
										}
									}
								}
							}
							if (perSong.isEmpty()) {
								IdfModel im = new IdfModel();
								im.setTerm(baseform);
								im.setCount(1);
								im.setSoaId(song.getSongId());
								perSong.add(im);
							} else {
								if (!checkModelList(baseform, perSong)) {
									IdfModel im = new IdfModel();
									im.setTerm(baseform);
									im.setCount(1);
									im.setSoaId(song.getSongId());
									perSong.add(im);
								} else {
									for (IdfModel im : perSong) {
										if (im.getTerm().equalsIgnoreCase(
												baseform)) {
											im.setCount(im.getCount() + 1);
										}
									}
								}
							}
						}
					}
				}
			}
			words.add(perSong);
			perSong = new ArrayList();
		}
		//System.out.println("WordBank all");

		int count = 0;
		for (ArrayList<IdfModel> innerList : words) {
			// System.out.println("@index >> "+count);
			for (IdfModel model : innerList) {
				model.setCount(model.getCount()
						+ titleWeight(title[count], model.getTerm()));
				// System.out.println(model.getTerm()+ ">> "+model.getCount());
			}
			count++;
		}
		bagOfWordsSong = getIDF(bagOfWordsSong, docuCount);
		swb = setWordBank(words, bagOfWordsSong);
		swb = sortModelList(swb);
//		for (SongWordBank sw : swb) {
//			System.out.println("song id : " + sw.getSongId() + " >> count: "
//					+ sw.getTf() + " word " + sw.getWord() + " >> idf : "
//					+ sw.getIdf());
//		}
		System.out.println("ste5");
		songs = getStep5(swb, songs);
//		for (Song song : songs) {
//			System.out.println("song id : " + song.getSongId() + " step5 : "
//					+ song.getStep5Tfidf() + "\n\n");
//		}
		IDFReturn ir = new IDFReturn();
		ir.setSongs(songs);
		ir.setSwb(swb);
		return ir;
	}

	private boolean checkModelList(String word, ArrayList<IdfModel> model_list) {
		for (int x = 0; x < model_list.size(); x++) {
			if (word.equalsIgnoreCase(model_list.get(x).getTerm())) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<SongWordBank> sortModelList(
			ArrayList<SongWordBank> model_list) {
		Collections.sort(model_list, ALPHABETICAL_ORDER);
		return model_list;
	}

	private Comparator<SongWordBank> ALPHABETICAL_ORDER = new Comparator<SongWordBank>() {
		public int compare(SongWordBank str1, SongWordBank str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(str1.getWord(),
					str2.getWord());
			if (res == 0) {
				res = str1.getWord().compareTo(str2.getWord());
			}
			return res;
		}
	};

	private ArrayList<IdfModel> getIDF(ArrayList<IdfModel> bagOfWords,
			int docuCount) {
		for (IdfModel word : bagOfWords) {
			float temp = (float) docuCount / (float) word.getCount();
			word.setIdf((float) log2(temp));
		}
		return bagOfWords;
	}

	private double log2(double x) {
		return Math.log(x) / Math.log(2.0d);
	}

	private ArrayList<SongWordBank> setWordBank(
			ArrayList<ArrayList<IdfModel>> perSong,
			ArrayList<IdfModel> bagOfWords) {
		ArrayList<SongWordBank> swb = new ArrayList<SongWordBank>();

		for (IdfModel idfModel : bagOfWords) {
			for (ArrayList<IdfModel> innerList : perSong) {
				for (IdfModel model : innerList) {
					if (model.getTerm().equalsIgnoreCase(idfModel.getTerm())) {
						float result = idfModel.getIdf() * model.getCount();
						SongWordBank wb = new SongWordBank();
						wb.setMaxCount(idfModel.getCount());
						wb.setSongId(model.getSoaId());
						wb.setTf(model.getCount());
						wb.setStep3(result);
						wb.setIdf(idfModel.getIdf());
						wb.setWord(model.getTerm());

						swb.add(wb);
					}
				}
				for (IdfModel model : innerList) {
					if (!model.getTerm().equalsIgnoreCase(idfModel.getTerm())) {
						SongWordBank wb = new SongWordBank();
						wb.setSongId(model.getSoaId());
						wb.setWord(idfModel.getTerm());
						wb.setTf(0);
						wb.setIdf(idfModel.getIdf());
						wb.setMaxCount(idfModel.getCount());
						if (!checkWordBank(wb, swb)) {
							swb.add(wb);
						}
					}
				}

			}
		}
		return swb;
	}

	private int titleWeight(String title, String word) {
		StringTokenizer str = new StringTokenizer(title, " ,()&");
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

	private boolean isStopWord(String word) {
		String[] string = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z"
				.split(" ");
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

	private List<Song> getStep5(ArrayList<SongWordBank> swbs, List<Song> songs) {
		float step5Result = 0f;
		System.out.print("here step5");
		for (Song song : songs) {
			for (SongWordBank swb : swbs) {
				if (swb.getSongId() == song.getSongId()) {
					step5Result += (float) Math.pow(swb.getStep3(), 2);
				}
			}
			step5Result = (float) Math.sqrt(step5Result);
			System.out.println("song id : " + song.getSongId() + " step5 : "
					+ step5Result + "\n\n");
			song.setStep5Tfidf(step5Result);
		}
		return songs;
	}

	private boolean checkWordBank(SongWordBank model,
			ArrayList<SongWordBank> model_list) {
		for (SongWordBank songWordBank : model_list) {
			if (songWordBank.getWord().equalsIgnoreCase(model.getWord())) {
				if (songWordBank.getSongId() == model.getSongId()) {
					return true;
				}
			}
		}
		return false;
	}

}

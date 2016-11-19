package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.IDFReturn;
import com.blackparty.syntones.model.IdfModel;

public class ArtistIDFProcess {
	public static String delimeter = "  \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?";
	public static Stemmer stem = new Stemmer();
	public static String[] stpWords = { "a", "an", "as", "are", "is", "was",
			"were", "as", "at", "be", "by", "for", "from", "in", "on", "to",
			"this", "the", "that", "will", "with", "or", "nor", "whether",
			"neither", "and", };

	public IDFReturn ArtistIdfProcess(List<Artist> artists, int docuCount)
			throws Exception {
		ArrayList<IdfModel> bagOfWordsArtist = new ArrayList<IdfModel>();
		ArrayList<IdfModel> perArtist = new ArrayList<IdfModel>();
		ArrayList<ArrayList<IdfModel>> words = new ArrayList<ArrayList<IdfModel>>();
		ArrayList<ArtistWordBank> swb = new ArrayList<ArtistWordBank>();

		System.out.println("Artist Count : " + docuCount);
		for (Artist artist : artists) {
			String artistSL = artist.getArtistName();
			StringTokenizer str = new StringTokenizer(artistSL, delimeter);
			while (str.hasMoreTokens()) {
				String token = str.nextToken().trim();
				if (!token.equalsIgnoreCase("")) {
					String baseform = stem.stem(token).trim();
					if (!isStopWord(baseform)) {
						if (!baseform.equalsIgnoreCase(null)) {
							if (bagOfWordsArtist.isEmpty()) {
								IdfModel im = new IdfModel();
								im.setTerm(baseform);
								im.setCount(1);
								im.setSoaId(0);
								bagOfWordsArtist.add(im);
							} else {
								if (!checkModelList(baseform, bagOfWordsArtist)) {
									IdfModel im = new IdfModel();
									im.setTerm(baseform);
									im.setCount(1);
									im.setSoaId(0);
									bagOfWordsArtist.add(im);
								} else {
									for (IdfModel im : bagOfWordsArtist) {
										if (im.getTerm().equalsIgnoreCase(
												baseform)) {
											im.setCount(im.getCount() + 1);
										}
									}
								}
							}
							if (perArtist.isEmpty()) {
								IdfModel im = new IdfModel();
								im.setTerm(baseform);
								im.setCount(1);
								im.setSoaId(artist.getArtistId());
								perArtist.add(im);
							} else {
								if (!checkModelList(baseform, perArtist)) {
									IdfModel im = new IdfModel();
									im.setTerm(baseform);
									im.setCount(1);
									im.setSoaId(artist.getArtistId());
									perArtist.add(im);
								} else {
									for (IdfModel im : perArtist) {
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
			words.add(perArtist);
			perArtist = new ArrayList();
		}
		System.out.println("WordBank all");
	
		bagOfWordsArtist = getIDF(bagOfWordsArtist, docuCount);
		swb = setWordBank(words, bagOfWordsArtist);
		swb = sortModelList(swb);
		for (ArtistWordBank sw : swb) {
			System.out.println("Artist id : " + sw.getArtistId() + " >> count: "
					+ sw.getTf() + " word " + sw.getWord() + " >> idf : "
					+ sw.getIdf());
		}
		System.out.println("ste5");
		artists = getStep5(swb, artists);
		for (Artist Artist : artists) {
			System.out.println("Artist id : " + Artist.getArtistId() + " step5 : "
					+ Artist.getStep5Tfidf() + "\n\n");
		}
		IDFReturn ir = new IDFReturn();
		ir.setArtists(artists);
		ir.setAwb(swb);
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

	private ArrayList<ArtistWordBank> sortModelList(
			ArrayList<ArtistWordBank> model_list) {
		Collections.sort(model_list, ALPHABETICAL_ORDER);
		return model_list;
	}

	private Comparator<ArtistWordBank> ALPHABETICAL_ORDER = new Comparator<ArtistWordBank>() {
		public int compare(ArtistWordBank str1, ArtistWordBank str2) {
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
			System.out.println("temp res >>"+temp);
			float idf = (float) log2(temp);
			word.setIdf(idf);

			System.out.println("idf >> "+word.getIdf());
		}
		return bagOfWords;
	}

	private float log2(float x) {
		return (float) Math.log((double) x) / (float) Math.log(2.0d);
	}

	private ArrayList<ArtistWordBank> setWordBank(
			ArrayList<ArrayList<IdfModel>> perArtist,
			ArrayList<IdfModel> bagOfWords) {
		ArrayList<ArtistWordBank> swb = new ArrayList<ArtistWordBank>();

		for (IdfModel idfModel : bagOfWords) {
			for (ArrayList<IdfModel> innerList : perArtist) {
				for (IdfModel model : innerList) {
					if (model.getTerm().equalsIgnoreCase(idfModel.getTerm())) {
						float result = idfModel.getIdf() * model.getCount();
						ArtistWordBank wb = new ArtistWordBank();
						wb.setMaxCount(idfModel.getCount());
						wb.setArtistId(model.getSoaId());
						wb.setTf(model.getCount());
						System.out.println("result >> "+result);
						wb.setStep3(result);
						wb.setIdf(idfModel.getIdf());
						wb.setWord(model.getTerm());

						swb.add(wb);
					}
				}
				for (IdfModel model : innerList) {
					if (!model.getTerm().equalsIgnoreCase(idfModel.getTerm())) {
						ArtistWordBank wb = new ArtistWordBank();
						wb.setArtistId(model.getSoaId());
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

	private List<Artist> getStep5(ArrayList<ArtistWordBank> swbs, List<Artist> artists) {
		float step5Result = 0f;
		System.out.print("here step5");
		for (Artist artist : artists) {
			for (ArtistWordBank swb : swbs) {
				if (swb.getArtistId() == artist.getArtistId()) {
					step5Result += (float) Math.pow(swb.getStep3(), 2);
				}
			}
			step5Result = (float) Math.sqrt(step5Result);
			System.out.println("Artist id : " + artist.getArtistId() + " step5 : "
					+ step5Result + "\n\n");
			artist.setStep5Tfidf(step5Result);
		}
		return artists;
	}

	private boolean checkWordBank(ArtistWordBank model,
			ArrayList<ArtistWordBank> model_list) {
		for (ArtistWordBank ArtistWordBank : model_list) {
			if (ArtistWordBank.getWord().equalsIgnoreCase(model.getWord())) {
				if (ArtistWordBank.getArtistId() == model.getArtistId()) {
					return true;
				}
			}
		}
		return false;
	}

}

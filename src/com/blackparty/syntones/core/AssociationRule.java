package com.blackparty.syntones.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.model.TwoItemSetCombo;
import com.blackparty.syntones.model.TwoItemSetRecomSong;
import com.blackparty.syntones.model.PlayedTwoItemSet;
import com.blackparty.syntones.model.ThreeItemSetCombo;
import com.blackparty.syntones.model.ThreeItemSetRecomSong;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.model.PlayedThreeItemSet;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.service.PlayedSongsService;

public class AssociationRule {
	@Autowired
	private PlayedSongsService playedSongsService;
	private static final int THRESHOLD = 50;

	public static ArrayList<String> getUniqueOneItemTracks(List<PlayedSongs> played_songs_list) {

		ArrayList<String> track_id_list = new ArrayList<>();

		for (int a = 0; a < played_songs_list.size(); a++) {
			track_id_list.add(played_songs_list.get(a).getTrack_id());
		}

		Set<String> hs = new HashSet<>();
		hs.addAll(track_id_list);
		track_id_list.clear();
		track_id_list.addAll(hs);

		Collections.sort(track_id_list);

		return track_id_list;

	}

	public static ArrayList<String> getUniqueSessions(List<PlayedSongs> played_songs_list) {
		ArrayList<String> session_id_list = new ArrayList<>();

		for (int a = 0; a < played_songs_list.size(); a++) {
			session_id_list.add(played_songs_list.get(a).getUser_id());

		}

		Set<String> hs = new HashSet<>();
		hs.addAll(session_id_list);
		session_id_list.clear();
		session_id_list.addAll(hs);

		Collections.sort(session_id_list);

		return session_id_list;
	}

	public static int[][] getOneItemBasket(List<PlayedSongs> played_songs_list, ArrayList<String> session_id_list,
			ArrayList<String> track_id_list) {
		int row = track_id_list.size(), col = session_id_list.size();
		int[][] basket = new int[row][col];
		String track = null;
		String session;

		for (row = 0; row < track_id_list.size(); row++) {
			for (col = 0; col < session_id_list.size(); col++) {

				for (int a = 0; a < played_songs_list.size(); a++) {
					track = played_songs_list.get(a).getTrack_id();
					session = played_songs_list.get(a).getUser_id();

					if (session.equals(session_id_list.get(col))) {
						if (track.equals(track_id_list.get(row))) {
							basket[row][col] = 1;
						}
					}
				}
				System.out.print("\t" + basket[row][col]);

			}
			System.out.println();
		}
		return basket;

	}

	public ArrayList<OneItemSetCount> getOneItemCount(ArrayList<String> session_id_list, int[][] oneItemBasket,
			List<PlayedSongs> played_songs_list, ArrayList<String> track_id_list) throws SQLException {
		int row = track_id_list.size(), col = session_id_list.size(), sum = 0;
		ArrayList<OneItemSetCount> one_item_set_count_list = new ArrayList<>();
		for (row = 0; row < track_id_list.size(); row++) {
			for (col = 0; col < session_id_list.size(); col++) {

				sum += oneItemBasket[row][col];

			}

			OneItemSetCount one_item_set_count = new OneItemSetCount(track_id_list.get(row), sum);
			one_item_set_count_list.add(one_item_set_count);
			sum = 0;
		}
		return one_item_set_count_list;

	}

	public static ArrayList<TwoItemSetCombo> getTwoItemCombo(ArrayList<String> track_id_list) throws SQLException {

		int r = 2;
		int n = track_id_list.size();
		String[] temp_storage = new String[r], track;
		ArrayList<TwoItemSetCombo> two_item_set_combo_list = new ArrayList<TwoItemSetCombo>();
		ArrayList<String> played_two_item_combo_list = getCombination(track_id_list, temp_storage, new ArrayList<>(), 0,
				n - 1, 0, r);

		for (int a = 0; a < played_two_item_combo_list.size(); a++) {

			track = played_two_item_combo_list.get(a).split(",");

			TwoItemSetCombo two_item_set_combo = new TwoItemSetCombo(track[0] + "," + track[1], track[1]);

			two_item_set_combo_list.add(two_item_set_combo);
		}

		return two_item_set_combo_list;
	}

	public static ArrayList<ThreeItemSetCombo> getThreeItemCombo(ArrayList<String> track_id_list) throws SQLException {

		int r = 3;
		int n = track_id_list.size();
		String[] temp_storage = new String[r], track;
		ArrayList<ThreeItemSetCombo> three_item_combo_list = new ArrayList<>();
		ArrayList<String> played_three_item_combo_list = getCombination(track_id_list, temp_storage, new ArrayList<>(),
				0, n - 1, 0, r);

		for (int a = 0; a < played_three_item_combo_list.size(); a++) {
			track = played_three_item_combo_list.get(a).split(",");

			ThreeItemSetCombo three_item_set_combo = new ThreeItemSetCombo(track[0] + "," + track[1] + "," + track[2],
					track[2]);

			three_item_combo_list.add(three_item_set_combo);

		}

		return three_item_combo_list;
	}

	public static ArrayList<String> getCombination(ArrayList<String> track_id_list, String[] temp_storage,
			ArrayList<String> result, int start, int end, int index, int r) {
		if (index == r) {
			String dataString = "";
			for (String dataItem : temp_storage) {
				dataString += dataItem + ",";
			}
			result.add(dataString);
		} else {
			for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
				temp_storage[index] = track_id_list.get(i);
				result = getCombination(track_id_list, temp_storage, result, i + 1, end, index + 1, r);
			}
		}
		return result;
	}

	public static int[][] getTwoItemBasket(ArrayList<TwoItemSetCombo> played_two_item_combo_list, int[][] oneItemBasket,
			ArrayList<String> track_id_list, ArrayList<String> session_id_list) {

		ArrayList<PlayedTwoItemSet> two_item_combo_tracks_list = new ArrayList<>();
		String[] two_item_tracks;
		String twoItem_track = null;
		String twoItem_session;
		int row_tItem = played_two_item_combo_list.size(), col_tItem = session_id_list.size();
		int[][] twoItemBasket = new int[row_tItem][col_tItem];
		int row = track_id_list.size(), col = session_id_list.size();

		for (int a = 0; a < played_two_item_combo_list.size(); a++) {
			two_item_tracks = played_two_item_combo_list.get(a).getCombination().split(",");
			for (row = 0; row < track_id_list.size(); row++) {
				for (col = 0; col < session_id_list.size(); col++) {

					if (two_item_tracks[0].equals(track_id_list.get(row))) {
						if (oneItemBasket[row][col] == 1) {

							for (int row_1 = 1; row_1 < track_id_list.size(); row_1++) {
								if (two_item_tracks[1].equals(track_id_list.get(row_1))) {
									if (oneItemBasket[row_1][col] == 1) {

										// System.out.println(
										// two_item_tracks[0] +
										// two_item_tracks[1] + " | " +
										// track_id_list.get(row)
										// + track_id_list.get(row_1) + " - " +
										// session_id_list.get(col));

										PlayedTwoItemSet played_two_item_set_list = new PlayedTwoItemSet();
										played_two_item_set_list
												.setCombination(two_item_tracks[0] + "," + two_item_tracks[1]);
										played_two_item_set_list.setSession_id(session_id_list.get(col));
										two_item_combo_tracks_list.add(played_two_item_set_list);

										// twoItemBasket[row_tItem][col_tItem] =
										// String.valueOf(col);
									}

								}

							}
						}

					}
				}
			}

		}

		for (row_tItem = 0; row_tItem < played_two_item_combo_list.size(); row_tItem++) {
			for (col_tItem = 0; col_tItem < session_id_list.size(); col_tItem++) {
				for (int a = 0; a < two_item_combo_tracks_list.size(); a++) {

					twoItem_track = two_item_combo_tracks_list.get(a).getCombination();
					twoItem_session = two_item_combo_tracks_list.get(a).getSession_id();

					if (twoItem_track.equals(played_two_item_combo_list.get(row_tItem).getCombination())) {
						// System.out.println(twoItem_track + " - " +
						// twoItem_session + " | "
						// +
						// played_two_item_combo_list.get(row_tItem).getCombination());

						if (twoItem_session == session_id_list.get(col_tItem)) {
							twoItemBasket[row_tItem][col_tItem] = 1;
						}

					}
				}

				// System.out.print("\t" + twoItemBasket[row_tItem][col_tItem]);
			}

			// System.out.println();
		}
		return twoItemBasket;
	}

	public ArrayList<TwoItemSet> getTwoItemSet(ArrayList<OneItemSetCount> one_item_set_count_list,
			ArrayList<String> track_id_list, int[][] twoItemBasket,
			ArrayList<TwoItemSetCombo> played_two_item_combo_list, ArrayList<String> session_id_list)
			throws SQLException {

		int row_tItem = played_two_item_combo_list.size(), col_tItem = session_id_list.size(), sum = 0;
		ArrayList<TwoItemSet> two_item_set_count_list = new ArrayList<>();
		TwoItemSet two_item_set_count;
		for (row_tItem = 0; row_tItem < played_two_item_combo_list.size(); row_tItem++) {
			for (col_tItem = 0; col_tItem < session_id_list.size(); col_tItem++) {

				sum += twoItemBasket[row_tItem][col_tItem];

			}
			two_item_set_count = new TwoItemSet(played_two_item_combo_list.get(row_tItem).getCombination(), sum);
			two_item_set_count_list.add(two_item_set_count);
			// JDBCConnection db = new JDBCConnection();
			// db.main(args);
			// db.insertTwoItemSetCount(two_item_set_count);
			sum = 0;
		}

		String[] track;
		float confidence;
		ArrayList<TwoItemSet> two_item_set_confidence_list = new ArrayList<>();
		ArrayList<TwoItemSet> two_item_set_list = new ArrayList<>();
		for (int a = 0; a < two_item_set_count_list.size(); a++) {

			for (int b = 0; b < one_item_set_count_list.size(); b++) {
				track = two_item_set_count_list.get(a).getTrack_id().split(",");
				if (track[0].equals(one_item_set_count_list.get(b).getTrack_id())) {

					if (two_item_set_count_list.get(a).getCount() != 0) {
						confidence = 100 / (one_item_set_count_list.get(b).getCount()
								/ two_item_set_count_list.get(a).getCount());
					} else {

						confidence = 0;
					}

					TwoItemSet two_item_set = new TwoItemSet(two_item_set_count_list.get(a).getTrack_id(), track[1],
							two_item_set_count_list.get(a).getCount(), confidence);
					two_item_set_confidence_list.add(two_item_set);

					two_item_set_list.add(two_item_set);
				}

			}

		}
		return two_item_set_list;

	}

	public static int[][] getThreeItemBasket(int[][] oneItemBasket, ArrayList<String> track_id_list,
			ArrayList<ThreeItemSetCombo> three_item_combo_list, int[][] twoItemBasket,
			ArrayList<TwoItemSetCombo> played_two_item_combo_list, ArrayList<String> session_id_list) {

		ArrayList<PlayedThreeItemSet> three_item_combo_tracks_list = new ArrayList<>();
		String[] three_item_tracks, two_item_tracks, two_item_tracks1;
		String threeItem_track = null;
		String threeItem_session;
		int row_tItem = three_item_combo_list.size(), col_tItem = session_id_list.size();
		int[][] threeItemBasket = new int[row_tItem][col_tItem];
		int row = played_two_item_combo_list.size(), col = session_id_list.size();
		int one_item_row = track_id_list.size(), one_item_col = session_id_list.size();

		for (int a = 0; a < three_item_combo_list.size(); a++) {

			three_item_tracks = three_item_combo_list.get(a).getTrack_id().split(",");

			for (row = 0; row < played_two_item_combo_list.size(); row++) {

				for (col = 0; col < session_id_list.size(); col++) {

					two_item_tracks = played_two_item_combo_list.get(row).getCombination().split(",");

					if (three_item_tracks[0].equals(two_item_tracks[0])) {
						if (three_item_tracks[1].equals(two_item_tracks[1])) {
							if (twoItemBasket[row][col] == 1) {

								for (int row_1 = 1; row_1 < track_id_list.size(); row_1++) {
									if (oneItemBasket[row_1][col] == 1) {

										if (three_item_tracks[2].equals(track_id_list.get(row_1))) {

											PlayedThreeItemSet played_three_item_set_list = new PlayedThreeItemSet();
											played_three_item_set_list.setCombination(three_item_tracks[0] + ","
													+ three_item_tracks[1] + "," + three_item_tracks[2]);
											played_three_item_set_list.setSession_id(session_id_list.get(col));
											three_item_combo_tracks_list.add(played_three_item_set_list);
										}

									}
								}
							}

						}
					}

				}
			}

		}

		for (row_tItem = 0; row_tItem < three_item_combo_list.size(); row_tItem++)

		{
			for (col_tItem = 0; col_tItem < session_id_list.size(); col_tItem++) {
				for (int a = 0; a < three_item_combo_tracks_list.size(); a++) {

					threeItem_track = three_item_combo_tracks_list.get(a).getCombination();
					threeItem_session = three_item_combo_tracks_list.get(a).getSession_id();

					if (threeItem_track.equals(three_item_combo_list.get(row_tItem).getTrack_id())) {

						if (threeItem_session == session_id_list.get(col_tItem)) {
							threeItemBasket[row_tItem][col_tItem] = 1;
						}

					}
				}

				// System.out.print("\t" +
				// threeItemBasket[row_tItem][col_tItem]);
			}

			// System.out.println();
		}
		return threeItemBasket;
	}

	public static ArrayList<ThreeItemSet> getThreeItemSet(ArrayList<ThreeItemSetCombo> three_item_set_combo_list,
			ArrayList<TwoItemSet> two_item_set_list, int[][] threeItemBasket, ArrayList<String> session_id_list)
			throws SQLException {

		int row_tItem = three_item_set_combo_list.size(), col_tItem = session_id_list.size(), sum = 0;
		ArrayList<ThreeItemSet> three_item_set_count_list = new ArrayList<>();
		ThreeItemSet three_item_set_count;

		for (row_tItem = 0; row_tItem < three_item_set_combo_list.size(); row_tItem++) {
			for (col_tItem = 0; col_tItem < session_id_list.size(); col_tItem++) {

				sum += threeItemBasket[row_tItem][col_tItem];

			}
			three_item_set_count = new ThreeItemSet(three_item_set_combo_list.get(row_tItem).getTrack_id(), sum);
			three_item_set_count_list.add(three_item_set_count);

			sum = 0;
		}

		String[] three_item_track;
		String three_item_combo;
		float confidence;
		ArrayList<ThreeItemSet> three_item_set_confidence_list = new ArrayList<>();
		ArrayList<ThreeItemSet> three_item_set_list = new ArrayList<>();
		for (int a = 0; a < three_item_set_count_list.size(); a++) {

			for (int b = 0; b < two_item_set_list.size(); b++) {
				three_item_track = three_item_set_count_list.get(a).getTrack_id().split(",");
				three_item_combo = three_item_track[0] + "," + three_item_track[1];

				if (three_item_combo.equals(two_item_set_list.get(b).getTrack_id())) {
			
					if (three_item_set_count_list.get(a).getCount() != 0) {
						confidence = 100
								/ (two_item_set_list.get(b).getCount() / three_item_set_count_list.get(a).getCount());

					} else {

						confidence = 0;
					}

					ThreeItemSet three_item_set = new ThreeItemSet(three_item_set_count_list.get(a).getTrack_id(),
							three_item_track[2], three_item_set_count_list.get(a).getCount(), confidence);
					three_item_set_confidence_list.add(three_item_set);

					three_item_set_list.add(three_item_set);
				}

			}

		}
		return three_item_set_list;

	}

	public static ArrayList<TwoItemSetRecomSong> getTwoItemRecomSong(ArrayList<TwoItemSet> two_item_set_list) {

		ArrayList<TwoItemSetRecomSong> two_item_recom_song_list = new ArrayList<>();

		for (TwoItemSet a : two_item_set_list) {

			if (a.getConfidence() >= THRESHOLD) {
				TwoItemSetRecomSong two_item_recom_song = new TwoItemSetRecomSong();

				two_item_recom_song.setRecom_song(a.getRecom_song());
				two_item_recom_song.setConfidence(a.getConfidence());
				two_item_recom_song_list.add(two_item_recom_song);

			}
		}

		return two_item_recom_song_list;
	}

	public static ArrayList<ThreeItemSetRecomSong> getThreeItemRecomSong(ArrayList<ThreeItemSet> three_item_set_list) {

		ArrayList<ThreeItemSetRecomSong> three_item_recom_song_list = new ArrayList<>();

		for (ThreeItemSet a : three_item_set_list) {

			if (a.getConfidence() >= THRESHOLD) {
				ThreeItemSetRecomSong three_item_set_recom_song = new ThreeItemSetRecomSong();
				three_item_set_recom_song.setRecom_song(a.getRecom_song());
				three_item_set_recom_song.setConfidence(a.getConfidence());
				three_item_recom_song_list.add(three_item_set_recom_song);

			}
		}

		return three_item_recom_song_list;
	}

}

package com.blackparty.syntones.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.PlayedSongsDAO;
import com.blackparty.syntones.DAO.UserDAO;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.TemporaryDB;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.TwoItemSet;

@Service
public class PlayedSongsService {
	@Autowired
	private PlayedSongsDAO playedSongsDAO;

	
	
	
	public void saveTemporaryDB(List<TemporaryDB> temporaryDB){
		playedSongsDAO.saveTemporaryDB(temporaryDB);
	}
	
	public void savePlayedSongs(PlayedSongs playedSongs) {

		playedSongsDAO.savePlayedSongs(playedSongs);

	}

	
	public List<PlayedSongs> getPlayedSongs() {

		return playedSongsDAO.getPlayedSongs();
	}

	public void insertOneItemSetCount(ArrayList<OneItemSetCount> one_item_set_count_list) {
		playedSongsDAO.insertOneItemSetCount(one_item_set_count_list);
	}

	public void insertTwoItemSet(ArrayList<TwoItemSet> two_item_set_list) {
		playedSongsDAO.insertTwoItemSet(two_item_set_list);
	}

	public void insertThreeItemSet(ArrayList<ThreeItemSet> three_item_set_list) {
		playedSongsDAO.insertThreeItemSet(three_item_set_list);
	}

	public List<TwoItemSet> getTwoItemSet() {
		return playedSongsDAO.getTwoItemSet();
	}

	public List<ThreeItemSet> getThreeItemSet() {
		return playedSongsDAO.getThreeItemSet();
	}

	public List<OneItemSetCount> getOneItemSetCount() {
		return playedSongsDAO.getOneItemSetCount();
	}
	
	public boolean checkIfPlayedSongExists(long session_id, String song_id){
		return playedSongsDAO.checkIfPlayedSongExists(session_id, song_id);
	}
}

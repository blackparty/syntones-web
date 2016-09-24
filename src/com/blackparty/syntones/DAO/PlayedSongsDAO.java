package com.blackparty.syntones.DAO;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.OneItemSet;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.service.PlayedSongsService;

@Repository
@Transactional
public class PlayedSongsDAO {
	@Autowired
	private SessionFactory sf;
	@Autowired
	private PlayedSongsService playedSongsService;

	public void savePlayedSongs(PlayedSongs playedSongs) {
		Session session = sf.openSession();

		playedSongs.getSession_id();
		playedSongs.getTrack_id();

		session.save(playedSongs);
		session.flush();
		session.close();
	}

	public List<PlayedSongs> getPlayedSongs() {
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs");
		return query.list();

	}

	public void insertOneItemSetCount(ArrayList<OneItemSetCount> one_item_set_count_list) {

		Session session = sf.openSession();

		for (OneItemSetCount a : one_item_set_count_list) {

			a.getTrack_id();
			a.getCount();
			session.save(a);

			session.flush();

		}
		session.close();

	}

	public void insertTwoItemSet(ArrayList<TwoItemSet> two_item_set_list) {

		Session session = sf.openSession();

		for (TwoItemSet a : two_item_set_list) {

			a.getTrack_id();
			a.getCount();
			a.getConfidence();
			a.getRecom_song();

			session.save(a);

			session.flush();

		}
		session.close();

	}

	public void insertThreeItemSet(ArrayList<ThreeItemSet> three_item_set_list) {

		Session session = sf.openSession();

		for (ThreeItemSet a : three_item_set_list) {

			a.getTrack_id();
			a.getCount();
			a.getConfidence();
			a.getRecom_song();

			session.save(a);

			session.flush();

		}
		session.close();

	}

	public List<TwoItemSet> getTwoItemSet() {

		Session session = sf.openSession();
		Query query = session.createQuery("from TwoItemSet");
		return query.list();
	}

	public List<ThreeItemSet> getThreeItemSet() {

		Session session = sf.openSession();
		Query query = session.createQuery("from ThreeItemSet");
		return query.list();
	}

	public List<OneItemSetCount> getOneItemSetCount() {

		Session session = sf.openSession();
		Query query = session.createQuery("from OneItemSetCount");
		return query.list();
	}

	public boolean checkIfPlayedSongExists(long session_id, String song_id) {
		boolean songExists;
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs where session_id = :session_id AND track_id = :song_id");
		query.setLong("session_id", session_id);
		query.setString("song_id", song_id);

		PlayedSongs playedSongs = (PlayedSongs) query.uniqueResult();
		if (playedSongs!=null) {
			songExists = true;
		} else {
			songExists = false;
		}

		session.flush();
		session.close();
		return songExists;
	}

}

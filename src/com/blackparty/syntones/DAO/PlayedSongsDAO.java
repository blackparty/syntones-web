package com.blackparty.syntones.DAO;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.TemporaryDB;
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

	// INSERTS
	public void saveTemporaryDB(List<TemporaryDB> temporaryDB) {
		Session session = sf.openSession();

		for (TemporaryDB a : temporaryDB) {

			a.getSong_id();
			a.getUser_id();
			session.save(a);
			session.flush();
		}

		session.close();
	}

	public void savePlayedSongs(PlayedSongs playedSongs) {
		Session session = sf.openSession();

		playedSongs.getTrack_id();
		playedSongs.getUser_id();
		session.save(playedSongs);
		session.flush();

		session.close();
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

	// FETCHES

	public List<TemporaryDB> getTemporaryDB() {
		Session session = sf.openSession();
		Query query = session.createQuery("from TemporaryDB");
		return query.list();
	}

	public List<PlayedSongs> getPlayedSongs() {
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs");
		return query.list();

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

	public boolean checkIfPlayedSongExists(String user_id, String song_id) {
		boolean songExists;
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs where user_id = :user_id AND track_id = :song_id");
		query.setString("user_id", user_id);
		query.setString("song_id", song_id);

		PlayedSongs playedSongs = (PlayedSongs) query.uniqueResult();
		if (playedSongs != null) {
			songExists = true;
		} else {
			songExists = false;
		}

		session.flush();
		session.close();
		return songExists;
	}
	//use username instead of userID
	public List<PlayedSongs> getPlayedSongsByUserId(String user_id){
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs where user_id =:user_id");
		query.setString("user_id", user_id);
		List<PlayedSongs> playedsongs = query.list();
		return playedsongs;
	}

	// DELETES

	public void truncateTemporaryDB() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from TemporaryDB");
		query.executeUpdate();
		session.close();
	}

	public void truncateOneSetItemCount() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from OneItemSetCount");
		query.executeUpdate();
		session.close();
	}

	public void truncateTwoItemSet() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from TwoItemSet");
		query.executeUpdate();
		session.close();
	}

	public void truncateThreeItemSet() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from ThreeItemSet");
		query.executeUpdate();
		session.close();
	}

}
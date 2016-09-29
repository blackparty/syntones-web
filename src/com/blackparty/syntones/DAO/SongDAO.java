package com.blackparty.syntones.DAO;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.core.Mp3Uploader;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SearchModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.ArtistService;

@Repository
@Transactional
public class SongDAO {
	@Autowired private SessionFactory sf;
	@Autowired private ArtistService as;
	
	public List<Long> getAllSongByArtist(Artist artist)throws Exception{
		Session session = sf.openSession();
		Query query = session.createSQLQuery("select song_id from song_tbl where artist_artist_id = :id").addScalar("song_id",LongType.INSTANCE);
		query.setLong("id", artist.getArtistId());
		List<Long> list = query.list();
		session.flush();
		session.close();
		return list;
	}
	public long addSong(Song song)throws Exception{
		Session session = sf.openSession();
		Artist fetchedArtist =  as.getArtist(song.getArtistName());
		song.setArtist(fetchedArtist);
		long songId = (Long)session.save(song);
		session.flush();
		session.close();
		//call mp3uploader to save a copy of the mp3 on the server side
		long artistId = song.getArtist().getArtistId();
		Mp3Uploader uploader = new Mp3Uploader();
		String file = uploader.upload(song.getFile(),songId,artistId);
		updateSong(songId, file);
		return songId;
	}


	public List<Song> getAllSongsFromDb(){
		Session session = sf.openSession();
		Query query = session.createQuery("from Song");
		return query.list();
	}

	public Song getSong(long songId)throws Exception{
		Session session = sf.openSession();
		System.out.println("Querying song for :"+songId);
		Query q = session.createQuery("from Song where song_id =:id");
		q.setLong("id", songId);
		Song song = (Song)q.uniqueResult();
		session.flush();
		session.close();
		song.setArtist(as.getArtist(song.getArtist().getArtistId()));
		return song;
	}
	
	public List<Song> getAllSongs() throws Exception{
		Session session = sf.openSession();
		Query q = session.createQuery("from Song");
		List<Song> songList = q.list();
		session.flush();
		session.close();
		for(Song s:songList){
			s.setArtist(as.getArtist(s.getArtist().getArtistId()));
		}
		return songList;
	}

	public ArrayList<Song> fetchAllSong() throws Exception {
		Session session = sf.openSession();
		Query query = session.createQuery("from Song");
		@SuppressWarnings("unchecked")
		ArrayList<Song> songs = (ArrayList<Song>) query.list();
		return songs;

	}

	public void updateBatchAllSongs(List<Song> songs)throws Exception{
		StatelessSession session = sf.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(Song s:songs){
			session.update(s);
		}
		trans.commit();
		session.close();
	}


	public ArrayList<Song> getSongs(ArrayList<SearchModel> sm) {
		Session session = sf.openSession();
		ArrayList<Song> songs = new ArrayList();
		for (SearchModel model : sm) {
			Query query = session.createQuery("from Song where songId=:id");
			query.setLong("id", model.getId());
			Song song = (Song) query.uniqueResult();
			songs.add(song);
		}
		return songs;
	}
	
	public void updateSong(long songId, String file){
		Session session = sf.openSession();
			Query query = session.createQuery("from Song where songId=:id");
			query.setLong("id", songId);
			Song song = (Song) query.uniqueResult();
			song.setFilePath(file);
			session.update(song);
			session.flush();
			session.close();
			
	}

}

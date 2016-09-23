package com.blackparty.syntones.DAO;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.blackparty.syntones.core.Mp3Uploader;
import com.blackparty.syntones.model.Artist;
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
		//call mp3uploader to save a copy of the mp3 on the server side
		long artistId = song.getArtist().getArtistId();
		Mp3Uploader uploader = new Mp3Uploader();
		uploader.upload(song.getFile(),songId,artistId);
		session.flush();
		session.close();
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
		return song;
	}
	
	public List<Song> getAllSongs() throws Exception{
		Session session = sf.openSession();
		Query q = session.createQuery("from Song");
		List<Song> songList = q.list();
		session.flush();
		session.close();
		return songList;
	}

	

}

package com.blackparty.syntones.DAO;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
	

	public ArrayList<Song> getAllSongs(String[] songIdList)throws Exception{
		ArrayList<Song> songList = new ArrayList<Song>();
		
		//search all songs using the songId
		for(String e:songIdList){
			songList.add(getSong(e));
		}
		
		
		
		return songList;
	}

	
	public void addSong(Song song)throws Exception{
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
	}
	

	public List<Song> getAllSongsFromDb(){
		Session session = sf.openSession();
		Query query = session.createQuery("from Song");
		return query.list();
		
	}

	public Song getSong(String songId)throws Exception{
		Session session = sf.openSession();
		Query q = session.createQuery("from Song where song_id =:id");
		q.setLong("id", Long.parseLong(songId));
		Song song = (Song)q.uniqueResult();
		return song;
	}
	
	public List<Song> getAllSongs() throws Exception{
		Session session = sf.openSession();
		Query q = session.createQuery("from Song");
		List<Song> songList = q.list();
		return songList;
	}

	
}

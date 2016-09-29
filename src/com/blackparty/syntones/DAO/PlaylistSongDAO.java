package com.blackparty.syntones.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.SongService;

@Repository
@Transactional
public class PlaylistSongDAO {
	@Autowired private SessionFactory sessionFactory;
	@Autowired private SongService songService;
	
	
	
	public void removeToPlaylist(PlaylistSong playlistSong)throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("delete PlaylistSong where songId=:sId and playlistId=:pId");
		query.setLong("sId",playlistSong.getSongId());
		query.setLong("pId",playlistSong.getPlaylistId());
		query.executeUpdate();
		session.flush();
		session.close();
	}
	
	
	public void savebatchPlaylistSong(List<PlaylistSong> songs)throws Exception{
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(PlaylistSong ps:songs){
			session.insert(ps);
		}
		trans.commit();
		session.close();
	}
	
	public void removePlaylist(Playlist playlist){
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("delete PlaylistSong where playlistId=:id");
		query.setLong("id", playlist.getPlaylistId());
		query.executeUpdate();
		session.flush();
		session.close();
	}
	public void addToplaylist(PlaylistSong playlistSong) throws Exception{
		Session session = sessionFactory.openSession();
		session.save(playlistSong);
		session.flush();
		session.close();
	}
	public List<Song> getSongs(long playlistId) throws Exception{
		Session session = sessionFactory.openSession();
		Query query =  session.createQuery("select songId from PlaylistSong where playlistId = :id");
		query.setLong("id", playlistId);
		List<Long> songIds = query.list();
		List<Song> songs = new ArrayList<>();
		for(long s:songIds){
			Song song = songService.getSong(s);
			songs.add(song);
		}
		System.out.println("From DAO:");
		for(Song s : songs){
			System.out.println(s.toString());
		}
		session.flush();
		session.close();
		return songs;
	}
}

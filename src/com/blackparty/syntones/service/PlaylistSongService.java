package com.blackparty.syntones.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.PlaylistSongDAO;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;

@Service
public class PlaylistSongService {

	@Autowired PlaylistSongDAO playlistSongDao;
	public void addToplaylist(PlaylistSong playlistSong) throws Exception{
		playlistSongDao.addToplaylist(playlistSong);
	}
	
	public List<Song> getSongs(long playlistId) throws Exception{
		return playlistSongDao.getSongs(playlistId);
	}
	
	public void savebatchPlaylistSong(List<PlaylistSong> songs)throws Exception{
		playlistSongDao.savebatchPlaylistSong(songs);
	}
	
	public void removeToPlaylist(PlaylistSong playlistSong)throws Exception{
		playlistSongDao.removeToPlaylist(playlistSong);
	}
	public void removePlaylist(Playlist playlist){
		playlistSongDao.removePlaylist(playlist);
	}
}

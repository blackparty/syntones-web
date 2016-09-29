package com.blackparty.syntones.service;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.PlaylistDAO;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.User;

@Service
public class PlaylistService {
	@Autowired PlaylistDAO playlistDao;
	public void savePlaylist(Playlist playlist)throws Exception{
		playlistDao.addPlaylist(playlist);
	}
	public void updateLastPlayedPlaylist(long playlistId,Timestamp lastPlayed)throws Exception{
		playlistDao.updateLastPlayedPlaylist(playlistId, lastPlayed);
	}
	public long addGeneratedPlaylist(Playlist playlist)throws Exception{
		return playlistDao.addGeneratedPlaylist(playlist);
	}
	public List<Playlist> getPlaylist(User user)throws Exception{
		return playlistDao.getPlaylist(user);
	}
	
	public Playlist getSongsFromPlaylist(long id)throws Exception{
		return playlistDao.getSongsFromPlaylist(id);
	}
	
	public void removePlaylist(Playlist playlist)throws Exception{
		playlistDao.removePlaylist(playlist);
	}
}

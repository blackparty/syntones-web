package com.blackparty.syntones.service;

import java.io.File;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blackparty.syntones.DAO.SongDAO;
import com.blackparty.syntones.model.Song;

@Service
public class SongService {
	@Autowired
	private SongDAO songDao;
	
	
	public ArrayList<Song> getAllSongs(String[] songIdList)throws Exception{
		return songDao.getAllSongs(songIdList);
	}
	
	public void addSong(Song song) throws Exception{
		songDao.addSong(song);
	}

	public List<Song> getAllSongsFromDb(){
		return songDao.getAllSongsFromDb();
	}

	public List<Song> getAllSongs() throws Exception{
		List<Song> songList = songDao.getAllSongs();
		return songList;
	}
	
	public Song getSong(String songId)throws Exception{
		return songDao.getSong(songId);
	}

}

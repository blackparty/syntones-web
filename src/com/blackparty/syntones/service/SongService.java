	package com.blackparty.syntones.service;



import java.util.ArrayList;
import java.io.File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SongDAO;
import com.blackparty.syntones.model.SearchModel;
import com.blackparty.syntones.model.Song;

@Service
public class SongService {
	@Autowired
	private SongDAO songDao;
	
	
	public void addSong(Song song) throws Exception{
		songDao.addSong(song);
	}


	public List<Song> getAllSongsFromDb(){
		return songDao.getAllSongsFromDb();
	}

	public ArrayList<Song> fetchAllSong()throws Exception{
		return songDao.fetchAllSong();
	}

	public List<Song> getAllSongs() throws Exception{
		List<Song> songList = songDao.getAllSongs();
		return songList;
	}
	
	public Song getSong(long songId)throws Exception{
		return songDao.getSong(songId);
	}
	
	public void updateAllSong(ArrayList<Song> songs) throws Exception{
		songDao.updateAllSong(songs);
		
	}
	
	public ArrayList<Song> getSongs(ArrayList<SearchModel> model){
		return songDao.getSongs(model);
	}
}

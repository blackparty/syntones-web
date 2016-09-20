package com.blackparty.syntones.service;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blackparty.syntones.DAO.SongDAO;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SearchModel;
import com.blackparty.syntones.model.Song;

@Service
public class SongService {
	@Autowired
	private SongDAO songDao;
	
	public void addSong(Song song) throws Exception{
		songDao.addSong(song);
	}
	public ArrayList<Song> fetchAllSong()throws Exception{
		return songDao.fetchAllSong();
	}
	
	public void updateAllSong(ArrayList<Song> songs) throws Exception{
		songDao.updateAllSong(songs);
		
	}
	
	public ArrayList<Song> getSongs(ArrayList<SearchModel> model){
		return songDao.getSongs(model);
	}
}

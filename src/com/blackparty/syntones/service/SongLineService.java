package com.blackparty.syntones.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SongLineDAO;
import com.blackparty.syntones.model.SongLine;

@Service
public class SongLineService {
	@Autowired private SongLineDAO songLineDao;
	public void addSongLine(SongLine songLine){
		songLineDao.addSongLine(songLine);
	}
	public List<Long> getAllSongs(){
		return songLineDao.getAllSongs();
	}
	
	public List<String> getAllLines(){
		return songLineDao.getAllLines();
	}
}

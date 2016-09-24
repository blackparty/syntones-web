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
	
	
	public void truncateTable(){
		songLineDao.truncateTable();
	}
	public void saveBatchSongLines(List<SongLine> songLines)throws Exception{
		songLineDao.saveBatchSongLines(songLines);
	}
	public void addSongLine(SongLine songLine)throws Exception{
		songLineDao.addSongLine(songLine);
	}
	public List<Long> getAllSongs()throws Exception{
		return songLineDao.getAllSongs();
	}
	
	public List<SongLine> getAllLines()throws Exception{
		return songLineDao.getAllLines();
	}
}

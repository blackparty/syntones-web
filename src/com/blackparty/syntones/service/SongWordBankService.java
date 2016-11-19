package com.blackparty.syntones.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SongWordBankDAO;
import com.blackparty.syntones.model.SongWordBank;

@Service
public class SongWordBankService {
	@Autowired
	private SongWordBankDAO swbDao;
	
	public void updateWordBank(List<SongWordBank> words) throws Exception{
		swbDao.updateWordBank(words);
	}
	
	public List<SongWordBank> fetchAllWordBank() throws Exception{
		return swbDao.fetchAllWordBank();
	}
	public List<SongWordBank> fetchWBbySongId(long songId)throws Exception{
		return swbDao.fetchWordBankbySongId(songId);
	}
}

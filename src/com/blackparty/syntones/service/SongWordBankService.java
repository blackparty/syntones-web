package com.blackparty.syntones.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SongWordBankDAO;
import com.blackparty.syntones.model.SongWordBank;

@Service
public class SongWordBankService {
	@Autowired
	private SongWordBankDAO swbDao;
	
	public void updateWordBank(ArrayList<SongWordBank> words) throws Exception{
		swbDao.updateWordBank(words);
	}
	
	public ArrayList<String> fetchAllWordBank() throws Exception{
		return swbDao.fetchAllWordBank();
	}
}

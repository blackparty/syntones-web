package com.blackparty.syntones.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.ArtistWordBankDAO;
import com.blackparty.syntones.model.ArtistWordBank;

@Service
public class ArtistWordBankService {
	@Autowired
	private ArtistWordBankDAO awbDao;

	public void updateWordBank(ArrayList<ArtistWordBank> words)
			throws Exception {
		awbDao.updateWordBank(words);
	}
	
	public ArrayList<String> fetchAllWordBank() throws Exception{
		return awbDao.fetchAllWordBank();
		
	}

}

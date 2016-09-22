package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.StopWordDAO;

@Service
public class StopWordService {
	@Autowired private StopWordDAO stopwordDao;
	
	public List<String> getStopWords()throws Exception{
		return stopwordDao.getAllStopWords();
	}
}

package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.CommonWordDAO;
import com.blackparty.syntones.model.CommonWord;

@Service
public class CommonWordService {
	@Autowired private CommonWordDAO commonWordDao;
		
	
	public void deleteCommonWords()throws Exception{
		commonWordDao.deleteCommonWords();
	}
	public CommonWord getCommonWord(String word)throws Exception{
		return commonWordDao.getCommonWord(word);
	}
	
	public void saveBatchCommonWords(List<CommonWord> words)throws Exception{
		commonWordDao.saveBatchCommonWords(words);
	}
	public void saveCommonWord(String word, int count)throws Exception {
		commonWordDao.saveCommonWord(word,count);
	}
	public void updateCommonWord(CommonWord word)throws Exception{
		commonWordDao.updateCommonWord(word);
	}
}

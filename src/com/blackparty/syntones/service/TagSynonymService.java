package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.TagSynonymDAO;
import com.blackparty.syntones.model.TagSynonym;

@Service
public class TagSynonymService {
	@Autowired TagSynonymDAO tagSynonymDAO;
	
	public void saveSynonym(TagSynonym tag){
		tagSynonymDAO.saveSynonym(tag);
	}
	
	public List<TagSynonym> getTagSynonym(long id)throws Exception{
		return tagSynonymDAO.getTagSynonym(id);
	}
	
	public void saveBatchSynonym(List<TagSynonym> tags)throws Exception{
		tagSynonymDAO.saveBatchSynonym(tags);
	}
}

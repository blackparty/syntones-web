package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.TagDAO;
import com.blackparty.syntones.model.Tag;

@Service
public class TagService {
	@Autowired private TagDAO tagDao;
	
	public void addTag(Tag tag) throws Exception{
		tagDao.addTag(tag);
	}
	public List<Tag> getAllTags()throws Exception{
		return tagDao.getAllTags();
	}
	public Tag getTag(String tag)throws Exception{
		return tagDao.getTag(tag);
	} 
	public void updateFlag(long tagId, boolean flag)throws Exception{
		tagDao.updateFlag(tagId, flag);
	}
	
	public void updateBatchFlags(List<Tag>tags)throws Exception{
		tagDao.updateBatchFlags(tags);
	}
}

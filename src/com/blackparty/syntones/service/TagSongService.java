package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.TagSongDAO;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.TagSong;

@Service
public class TagSongService {
	@Autowired TagSongDAO tagSongDao;
	public void saveBatchTagSong(List<TagSong> tagSong)throws Exception{
		tagSongDao.saveBatchTagSong(tagSong);
	}
	public List<TagSong> getTagsOfTheSong(Song song)throws Exception{
		return tagSongDao.getTagsOfTheSong(song);
	}
	
	public List<String> getAvailableTags()throws Exception{
		return tagSongDao.getAvailableTags();
	}
	
	public List<TagSong> getSongsByTags(String tag)throws Exception{
		return tagSongDao.getSongByTags(tag);
	}
}

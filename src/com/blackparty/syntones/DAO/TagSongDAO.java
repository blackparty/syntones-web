package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.TagSong;

@Repository
@Transactional
public class TagSongDAO {
	@Autowired SessionFactory sessionFactory;
	public void saveBatchTagSong(List<TagSong> tagSong)throws Exception{
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(TagSong ts:tagSong){
			session.insert(ts);
		}
		trans.commit();
		session.close();
	}
	
	public List<String> getAvailableTags()throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select distinct tag from TagSong");
		List<String> fetchedTags = query.list();
		session.flush();
		session.close();
		return fetchedTags;
	}
	
	public List<TagSong> getSongByTags(String tag)throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from TagSong where tag= :name");
		query.setString("name", tag);
		List<TagSong> ts = query.list();
		session.flush();
		session.close();
		return ts;
	}
	
	public List<TagSong> getTagsOfTheSong(Song song)throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from TagSong where songId = :id");
		query.setLong("id", song.getSongId());
		List<TagSong> ts = query.list();
		session.flush();
		session.close();
		return ts;
	}
	
}

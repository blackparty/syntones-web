package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Tag;

@Repository
@Transactional
public class TagDAO {
	@Autowired private SessionFactory sessionFactory;
	
	public void addTag(Tag tag) throws Exception{
		Session session = sessionFactory.openSession();
		session.save(tag);
		session.flush();
		session.close();
	}
	public List<Tag> getAllTags()throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Tag");
		List<Tag> tags = query.list();
		session.flush();
		session.close();
		return tags;
	}
}

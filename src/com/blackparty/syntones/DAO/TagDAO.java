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
	public void updateFlag(long tagId, boolean flag)throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("update Tag set flag =:flag where id=:tagId");
		query.setBoolean("flag", flag);
		query.setLong("tagId", tagId);
		query.executeUpdate();
		session.flush();
		session.close();
	}
	
	public void updateBatchFlags(List<Tag>tags)throws Exception{
		for(Tag t : tags){
			Session session = sessionFactory.openSession();
			Query query = session.createQuery("update Tag set flag =:flag where id=:tagId");
			query.setBoolean("flag", t.isFlag());
			query.setLong("tagId", t.getId());
			query.executeUpdate();
			session.flush();
			session.close();
		}
	}
	
	public Tag getTag(String tag)throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Tag where tag =:name");
		Tag fetchedTag = (Tag)query.uniqueResult();
		session.flush();
		session.close();
		return fetchedTag;
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

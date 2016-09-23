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

import com.blackparty.syntones.model.TagSynonym;

@Repository
@Transactional
public class TagSynonymDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public void saveSynonym(TagSynonym tag) {
		Session session = sessionFactory.openSession();
		session.save(tag);
		session.flush();
		session.close();
	}

	public List<TagSynonym> getTagSynonym(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from TagSynonym ts  where ts.tagId = :id");
		query.setLong("id", id);
		List<TagSynonym> syn = query.list();
		System.out.println("from database "+syn.size());
		session.flush();
		session.close();
		return syn;
	}

	public void saveBatchSynonym(List<TagSynonym> tags) throws Exception {
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for (TagSynonym cw : tags) {
			session.insert(cw);
		}
		trans.commit();
		session.close();
	}

}

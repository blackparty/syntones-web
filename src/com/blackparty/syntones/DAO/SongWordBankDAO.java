package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SongWordBank;

@Repository
@Transactional
public class SongWordBankDAO {
	@Autowired
	private SessionFactory sf;

	public void updateWordBank(List<SongWordBank> words) throws Exception {
		Session session = sf.openSession();
		if (!session.createQuery("select 1 from SongWordBank").setMaxResults(1)
				.list().isEmpty()) {
			session.createQuery("delete from SongWordBank").executeUpdate();
		}
		if (!words.isEmpty()) {
			for (SongWordBank word : words) {
				session.save(word);
			}

			session.flush();
			session.close();
		} else {
			System.out.print("hoholo");
			session.close();
		}
		System.out.print("here @ updateWordBank");
	}
	
	public List<SongWordBank> fetchAllWordBank() throws Exception{
		Session session = sf.openSession();
		Query query = session.createQuery("from SongWordBank");
		@SuppressWarnings("unchecked")
		List<SongWordBank> words = (List<SongWordBank>) query.list();
		session.close();
		return words;
	}
	
	@SuppressWarnings("unchecked")
	public List<SongWordBank> fetchWordBankbySongId(long songId){
		Session session = sf.openSession();
		try{
		Query query = session.createQuery("from SongWordBank where songId = :songId");
		query.setLong("songId", songId);
		List<SongWordBank> words = (List<SongWordBank>) query.list();
		session.close();
		return words;
		}catch(Exception e){
			System.out.println("at songwordbankdao >> "+e);
			return null;
		}
	}
}
package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.SongWordBank;



@Repository
@Transactional
public class ArtistWordBankDAO {
	@Autowired
	private SessionFactory sf;
	
	public void updateWordBank(List<ArtistWordBank> words) throws Exception {
		Session session = sf.openSession();
		if (!session.createQuery("select 1 from ArtistWordBank").setMaxResults(1)
				.list().isEmpty()) {
			session.createQuery("delete from ArtistWordBank").executeUpdate();
		}
		if (!words.isEmpty()) {
			for (ArtistWordBank word : words) {
				session.save(word);
			}

			session.flush();
			session.close();
		} else {
			System.out.print("hoholo");
		}
	}
	
	public List<ArtistWordBank> fetchAllWordBank() throws Exception{
		Session session = sf.openSession();
		Query query = session.createQuery("from ArtistWordBank");
		@SuppressWarnings("unchecked")
		List<ArtistWordBank> words = (List<ArtistWordBank>) query.list();
		session.close();
		return words;
	}
	
	public List<ArtistWordBank> fetchWordBank(long artistId) throws Exception{
		Session session = sf.openSession();
		Query query = session.createQuery("from ArtistWordBank where artistId=:artistId");
		query.setLong("artistId", artistId);
		@SuppressWarnings("unchecked")
		List<ArtistWordBank> words = (List<ArtistWordBank>) query.list();
		session.close();
		return words;
	}
}

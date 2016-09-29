package com.blackparty.syntones.DAO;




import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SearchModel;

@Repository
@Transactional


public class ArtistDAO {

	@Autowired
	private SessionFactory sf;
	
	
	public List<Artist> getAllArtist()throws Exception{
		Session session = sf.openSession();
		Query query = session.createQuery("from Artist");
		List<Artist> artists = query.list();
		session.flush();
		session.close();
		return artists;
	}
	
	public void addArtist(Artist artist){
		Session session = sf.openSession();
		session.save(artist);
		session.flush();
		session.close();
	}
	
	public Artist getArtist(String artistName){
		System.out.println("getting the artist using artist name: "+artistName);
		Session session = sf.openSession();
		Query query = session.createQuery("from Artist where artistName = :name");
		query.setString("name", artistName);
		Artist result = (Artist) query.uniqueResult();
		if(result != null){
			System.out.println("ARTIST ID!!! "+result.getArtistId());
		}
		session.flush();
		session.close();
		return result;
	}
	
	public Artist getArtist(long artistId){
		Session session = sf.openSession();
		Query query = session.createQuery("from Artist where artistId =:id");
		query.setLong("id", artistId);
		Artist result = (Artist) query.uniqueResult();
		session.flush();
		session.close();
		return result;
	}

	public void updateBatchAllArtist(List<Artist> artists) throws Exception{
		StatelessSession session = sf.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(Artist a:artists){
			session.update(a);
		}
		trans.commit();
		session.close();
	}
	
	public ArrayList<Artist> getArtists(ArrayList<SearchModel> model) {
		Session session = sf.openSession();
		ArrayList<Artist> artists = new ArrayList();
		for (SearchModel sm : model) {
			Query query = session
					.createQuery("from Artist where artistId =:id");
			query.setLong("id", sm.getId());
			Artist artist = (Artist) query.uniqueResult();
			artists.add(artist);
		}
		return artists;
	}
}

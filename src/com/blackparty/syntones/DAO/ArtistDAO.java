package com.blackparty.syntones.DAO;



import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Artist;

@Repository
@Transactional


public class ArtistDAO {

	@Autowired
	private SessionFactory sf;
	
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
}

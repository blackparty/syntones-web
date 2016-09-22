package com.blackparty.syntones.DAO;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SongLine;

@Repository
@Transactional
public class SongLineDAO {
	@Autowired SessionFactory sessionFactory;
	
	public void addSongLine(SongLine songLine){
		Session session = sessionFactory.openSession();
		session.save(songLine);
		session.flush();
		session.close();
	}
	
	public List<String> getAllLines(){
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select line from SongLine");
		List<String> line = query.list();
		session.flush();
		session.close();
		return line;
	}
	public List<Long> getAllSongs(){
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select distinct song_song_id from song_line_tbl").addScalar("song_song_id",LongType.INSTANCE);
		List<Long> songIdList = query.list();
		return songIdList;
	}
}

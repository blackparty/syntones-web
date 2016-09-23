package com.blackparty.syntones.DAO;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SongLine;

@Repository
@Transactional
public class SongLineDAO {
	@Autowired SessionFactory sessionFactory;
	

	public void addSongLine(SongLine songLine)throws Exception{
		Session session = sessionFactory.openSession();
		session.save(songLine);
		session.flush();
		session.close();
	}
	

	public void truncateTable(){
		Session session = sessionFactory.openSession();
		session.createSQLQuery("truncate table song_line_tbl").executeUpdate();
		session.flush();
		session.close();
		System.out.println("SongLine Table is truncated;");
	}
	
	public void saveBatchSongLines(List<SongLine> songLines){
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(SongLine sl : songLines){
			session.insert(sl);
		}
		trans.commit();
		session.close();
	}
	public List<SongLine> getAllLines()throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from SongLine group by line order by result desc");
		List<SongLine> lines = query.list();
		session.flush();
		session.close();
		return lines;
	}
	public List<Long> getAllSongs()throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select distinct song_song_id from song_line_tbl").addScalar("song_song_id",LongType.INSTANCE);
		List<Long> songIdList = query.list();
		return songIdList;
	}
}

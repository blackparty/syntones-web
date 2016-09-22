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

import com.blackparty.syntones.model.CommonWord;


@Repository
@Transactional
public class CommonWordDAO {
	@Autowired private SessionFactory sessionFactory;
	
	
	public CommonWord getCommonWord(String word)throws Exception{
		CommonWord commonWord = new CommonWord();
		Session session = sessionFactory.openSession();
		Query query =  session.createQuery("from CommonWord where word=:name");
		query.setString("name", word);
		commonWord = (CommonWord)query.uniqueResult();
		session.flush();
		session.close();
		return commonWord;
	}
	
	
	public void deleteCommonWords()throws Exception{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("delete from CommonWord");
		query.executeUpdate();
		session.flush();
		session.close();
	}
	
	public void saveBatchCommonWords(List<CommonWord> words)throws Exception{
		StatelessSession session = sessionFactory.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(CommonWord cw:words){
			session.insert(cw);
		}
		trans.commit();
		session.close();
	}
	
	public void saveCommonWord(String word,int count)throws Exception {
		CommonWord fetchedWord = getCommonWord(word);
		if(fetchedWord == null){
			CommonWord commonWord = new CommonWord(word, count);
			Session session = sessionFactory.openSession();
			session.save(commonWord);
			session.flush();
			session.close();
		}else{
			fetchedWord.setCount(count+fetchedWord.getCount());
			updateCommonWord(fetchedWord);
		}
	}
	public void updateCommonWord(CommonWord word)throws Exception{
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(word);
		session.getTransaction().commit();
		session.flush();
		session.close();
	}
	
}

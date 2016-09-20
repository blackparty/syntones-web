package com.blackparty.syntones.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.User;

@Repository
@Transactional
public class UserDAO {

	@Autowired
	private SessionFactory sf;

	public Message addUser(User user) throws Exception {
		Message message = new Message();
		User fetchedUser = null;
		fetchedUser = getUser(user);

		if (fetchedUser == null) {
			Session session = sf.openSession();
			session.save(user);
			session.flush();
			session.close();
			message.setFlag(true);
			message.setMessage(user.getUsername()+" is succesfully added.");
		} else {
			message.setMessage("username is already existed.");
			message.setFlag(false);
		}
		return message;
	}
	
	public User getUser(User user) throws Exception {
		System.out.println("Fetching personal information of :"+user.getUsername());
		Session session = sf.openSession();	
		Query q = session.createQuery("from User where username = :name");
		q.setString("name", user.getUsername());
		User fetchedUser = (User) q.uniqueResult();

		if(fetchedUser == null){
			return null;
		}
		session.flush();
		session.close();

		System.out.println("Unique result :"+fetchedUser.toString());


		return fetchedUser;
	}
}

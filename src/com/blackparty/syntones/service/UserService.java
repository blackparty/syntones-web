package com.blackparty.syntones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.UserDAO;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.User;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;

	public Message addUser(User user) throws Exception {
		Message message = userDAO.addUser(user);
		return message;
	}

	public User getUser(User user) throws Exception {
		User userResult = userDAO.getUser(user);
		return userResult;
	}

	public Message authenticateUser(User user) throws Exception {
		User fetchedUser;
		Message message = new Message();
		fetchedUser = getUser(user);
		String m = "";
		if (fetchedUser != null) {
			if (!fetchedUser.getPassword().equals(user.getPassword())) {
				m = "password is not correct.";
				System.out.println(m);
				message.setMessage(m);
				message.setFlag(false);
				return message;
			}
		} else {
			m = user.getUsername()+" does not exists.";
			System.out.println(m);
			message.setMessage(m);
			message.setFlag(false);
			return message;
		}
		message.setFlag(true);
		return message;
	}
	
	public Message authenticateAdmin(User user) throws Exception {
		User fetchedUser;
		Message message = new Message();
		fetchedUser = getUser(user);
		String m = "";
		if (fetchedUser != null) {
			if (!fetchedUser.getPassword().equals(user.getPassword())) {
				m = "Password is not correct.";
				System.out.println(m);
				message.setMessage(m);
				message.setFlag(false);
				return message;
			}else if((fetchedUser.getAdminFlag() == 0)){
				m = "\""+user.getUsername()+"\" does not exists.";
				System.out.println(m);
				message.setMessage(m);
				message.setFlag(false);
				return message;
			}
		} else {
			m = user.getUsername()+" does not exists.";
			System.out.println(m);
			message.setMessage(m);
			message.setFlag(false);
			return message;
		}
		message.setFlag(true);
		return message;
	}
}

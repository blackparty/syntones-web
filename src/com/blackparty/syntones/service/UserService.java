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
	
	public Message addUser(User user)throws Exception{
		Message message = userDAO.addUser(user);
		return message;
	}
	
	public User getUser(User user)throws Exception{
		User userResult = userDAO.getUser(user);
		return userResult;
	}
	
	public User authenticateUser(User user) throws Exception{
		User fetchedUser = null;
		fetchedUser = getUser(user);
		if(!fetchedUser.getPassword().equals(user.getPassword())){
			fetchedUser = null;
		}
		return fetchedUser;
	}
}

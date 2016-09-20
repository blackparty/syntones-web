package com.blackparty.syntones.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.service.UserService;

@RestController
@Component
public class UserEndpoint {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public User login(@RequestBody User user) {
		System.out.println("Login request is received coming from " + user.getUsername());
		User fetchedUser = null;
		try{
			fetchedUser = userService.authenticateUser(user);
			System.out.println("fetchedUser: "+fetchedUser.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return fetchedUser;
	}

	@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Message register(@RequestBody User user) {
		Message message = new Message();
		System.out.println("Register request is received coming from " + user.getUsername());
		
		try{
			message = userService.addUser(user);
		}catch(Exception e){
			e.printStackTrace();
		}

		return message;
	}

}

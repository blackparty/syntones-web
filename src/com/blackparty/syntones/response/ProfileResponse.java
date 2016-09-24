package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.User;

public class ProfileResponse {
	private Message message;
	private User user;
	
	public ProfileResponse (){}
	
	public ProfileResponse (User user, Message message){
		this.user = user;
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}

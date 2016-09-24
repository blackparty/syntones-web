package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;

public class LogoutResponse {
	private Message message;

	
	public LogoutResponse() {
		super();
	}

	public LogoutResponse(Message message) {
		super();
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}

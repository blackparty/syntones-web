package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;

public class RemoveToPlaylistResponse {
	private Message message;
	
	
	public RemoveToPlaylistResponse(){
	}


	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}
	
}

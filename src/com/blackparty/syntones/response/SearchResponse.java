package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;

public class SearchResponse {
	private Message message;
	
	public SearchResponse(){
	}
	public SearchResponse(Message message){
		this.message = message;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}

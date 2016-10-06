package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;

public class SongPlayedHistoryResponse {
	private Message message;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
}

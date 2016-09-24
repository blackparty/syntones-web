package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Song;

public class ListenResponse {
	private List<Song> recommendedSongs;
	private Message message;
	
	
	public ListenResponse(){
		
		
	}

	public List<Song> getRecommendedSongs() {
		return recommendedSongs;
	}


	public void setRecommendedSongs(List<Song> recommendedSongs) {
		this.recommendedSongs = recommendedSongs;
	}


	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}

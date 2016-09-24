package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.User;


public class LibraryResponse {
	
	
	
	private Message message;
	private List<Playlist> recentlyPlayedPlaylists;
	
	public LibraryResponse(){}

	
	public LibraryResponse(User user, Message message, List<Playlist> recentlyPlayedPlaylists){
		this.message = message;
		this.recentlyPlayedPlaylists = recentlyPlayedPlaylists;
	}
	
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Playlist> getRecentlyPlayedPlaylists() {
		return recentlyPlayedPlaylists;
	}

	public void setRecentlyPlayedPlaylists(List<Playlist> recentPlaylistsPlayed) {
		this.recentlyPlayedPlaylists = recentPlaylistsPlayed;
	}


		
}

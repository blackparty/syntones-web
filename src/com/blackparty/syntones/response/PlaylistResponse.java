package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.Song;

public class PlaylistResponse {
	private Message message;
	private List<Playlist> playlists;
	
	
	public PlaylistResponse(){}
	public PlaylistResponse(List<Playlist> playlists){
		this.playlists = playlists;
	}
	public PlaylistResponse(List<Playlist> playlists, Message message){
		this.playlists = playlists;
		this.message = message;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public List<Playlist> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}
	
}

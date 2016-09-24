package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Song;



public class SongListResponse {
	private Message message;
	private List<Song> songList;
	public SongListResponse(){} 
	public SongListResponse(Message message,List<Song> songList){
		this.message = message;
		this.songList = songList;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public List<Song> getSongs(){
		return this.songList;
	}
	public void setSongList(List<Song> songList){
		this.songList = songList;
	}
	
}

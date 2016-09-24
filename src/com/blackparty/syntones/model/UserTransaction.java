package com.blackparty.syntones.model;

import java.util.List;

public class UserTransaction {
	
	private User user;
	private List<Song> playedSong;
	private int counter;
	private String transactionId; 
	
	public UserTransaction(){
		
	}
	
	public UserTransaction(User user,List<Song> playedSong,int counter){
		this.user = user;
		this.playedSong = playedSong;
		this.counter = counter;
	}
	public int getCounter(){
		return counter;
	}
	public void setCounter(int counter){
		this.counter = counter;
	}
	public String getTransactionId(){
		return transactionId;
	}
	public void setTransactionId(String id){
		this.transactionId = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Song> getPlayedSong() {
		return playedSong;
	}
	public void setPlayedSong(List<Song> playedSong) {
		this.playedSong = playedSong;
	}
	
}

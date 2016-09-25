package com.blackparty.syntones.model;

import java.util.List;

public class SearchResultModel {
	
	private List<SearchModel> songs;
	private List<SearchModel> artists;
	boolean artistNan;
	boolean songNan;
	
	public SearchResultModel(){
		
	}
	public List<SearchModel> getSongs() {
		return songs;
	}
	public void setSongs(List<SearchModel> songs) {
		this.songs = songs;
	}
	public List<SearchModel> getArtists() {
		return artists;
	}
	public void setArtists(List<SearchModel> artists) {
		this.artists = artists;
	}
	public boolean isArtistNan() {
		return artistNan;
	}
	public void setArtistNan(boolean artistNan) {
		this.artistNan = artistNan;
	}
	public boolean isSongNan() {
		return songNan;
	}
	public void setSongNan(boolean songNan) {
		this.songNan = songNan;
	}
	
	

}

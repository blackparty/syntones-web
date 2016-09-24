package com.blackparty.syntones.model;

import java.util.ArrayList;
import java.util.List;

public class TemporaryModel {
	private List<Song> songs;
	private List<SongWordBank> words;
	private List<Artist> artists;
	private List<ArtistWordBank> awords;
	
	
	public TemporaryModel() {
		
	}


	public List<Song> getSongs() {
		return songs;
	}


	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}


	public List<SongWordBank> getWords() {
		return words;
	}


	public void setWords(List<SongWordBank> words) {
		this.words = words;
	}


	public List<Artist> getArtists() {
		return artists;
	}


	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}


	public List<ArtistWordBank> getAwords() {
		return awords;
	}


	public void setAwords(List<ArtistWordBank> awords) {
		this.awords = awords;
	}

	
	
}
